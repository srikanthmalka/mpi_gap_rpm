package au.com.michaelpage.gap.rpm;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.michaelpage.gap.common.Settings;
import au.com.michaelpage.gap.common.generator.DataOrigin;
import au.com.michaelpage.gap.common.generator.DimensionsGenerator;
import au.com.michaelpage.gap.common.generator.HitsGenerator;
import au.com.michaelpage.gap.common.google.GoogleDimensionsUploader;
import au.com.michaelpage.gap.common.google.GoogleHitsUploader;
import au.com.michaelpage.gap.common.google.GoogleUploaderDao;
import au.com.michaelpage.gap.common.util.DatabaseManager;
import au.com.michaelpage.gap.common.util.Util;

public class RpmTransformMainApp {

	private static final Logger logger = LoggerFactory.getLogger(RpmTransformMainApp.class);
	
	private static final String OUTPUT_FOLDER = "C:\\GTS\\Output\\RPM\\";
	
	private static final String OUTPUT_FOLDER_FOR_GLOBAL_REGIONAL = "C:\\GTS\\global-regional\\";
	
	private static final String ARCHIVE_FOLDER = "C:\\GTS\\Archive\\RPM\\";
	
	private static final String DATABASE_LOCATION = "c:\\Temp\\GTS_DB_RPM";
	
	public static void main(String[] args) throws Exception {

		logger.info("Started RPM Transformations");
		
		String incomingFolder = System.getProperty("gts.incomingFolder");
		String hostName = Settings.INSTANCE.getHostName(); 
		String databaseLocation = DATABASE_LOCATION + "\\" + hostName;
		
		String archiveFolder = ARCHIVE_FOLDER + hostName + "\\";
		
		int attempts = 0;
		boolean processingFinished = false;
		boolean newFilesProcessed = false;
		
		// this is to use for one-off uploads - it allows to extract data without uploading
		boolean extractOnly = !Util.isEmpty(System.getProperty("gts.extractOnly")) && "true".equals(System.getProperty("gts.extractOnly"));
		
		while (true) {
			attempts++;

			if (attempts > 10 && !processingFinished) {
				logger.info("This job hasn't completed successfully after 10 attempts.");
				throw new RuntimeException();
			} else {
				if (!extractOnly) {
					if (processingFinished) {
						break;
					}
					
					if (attempts > 1) {
						logger.info("5 minutes pause before attempt #{}.", attempts);
						Thread.sleep(1000*60*5);
					}
				}
				
				try {
					Map<Integer, String> outstandingFiles = new HashMap<Integer, String>(); 
					
					DatabaseManager.INSTANCE.initDatabase(databaseLocation);
					
					if (!extractOnly) {
						outstandingFiles = new GoogleUploaderDao().findOutstandingFiles(); 
						
						if (outstandingFiles.size() == 0) {
							if (newFilesProcessed) {
								logger.info("Finished RPM Transformations successfully.");
								break;
							} else {
								logger.info("No incomplete uploads found, proceeding with transformations.");
							}
						} else {
							logger.info("The following {} incomplete uploads have been found, trying to upload them first.", outstandingFiles.size());
							
							for (String fileName : outstandingFiles.values()) {
								logger.info(fileName);
							}

							Map<Integer, String> outstandingDimensionFiles = new GoogleUploaderDao().findOutstandingFiles("DIMENSION"); 
							for (String fileName : outstandingDimensionFiles.values()) {
								new GoogleDimensionsUploader().upload(fileName);
							}

							Map<Integer, String> outstandingHitFiles = new GoogleUploaderDao().findOutstandingFiles("HIT"); 
							for (String fileName : outstandingHitFiles.values()) {
								new GoogleHitsUploader().upload(fileName);
							}
							
							outstandingFiles = new GoogleUploaderDao().findOutstandingFiles();
							if (outstandingFiles.size() > 0) {
								logger.info("Incomplete uploads are still found, restarting uploads.");
								continue;
							} else {
								logger.info("Finished RPM Transformations successfully.");
								break;
							}
						}
					}
					
					DatabaseManager.INSTANCE.shutdownDatabase(true, false);
					
					DatabaseManager.INSTANCE.initDatabase(databaseLocation, false);

					// Importing one zip file
					File rpmFolder = new File(incomingFolder);
					String zipFile = null; 
					
					if (rpmFolder != null)
					for (File file : rpmFolder.listFiles()) {
						if (file.getCanonicalPath().endsWith(".zip")) {
							// Import the file into local DB
							zipFile = file.getCanonicalPath();
							new RpmImport().importZipFile(zipFile);
							break;
						}
					}
					
					if (zipFile == null) {
						logger.info("No new zip files to process are found in folder {}", incomingFolder);
						logger.info("Finished RPM Transformations, no new files have been imported.");
						throw new RuntimeException();
					}

					// Getting currency exchange rate
					Date extractDate = Settings.INSTANCE.getExtractDate();
					if (extractDate != null) {
						Settings.INSTANCE.setExchangeRate(Util.getExchangeRate(Settings.INSTANCE.getCurrencyCode(), extractDate));
					} else {
						logger.warn("Can't get extract date - is event.xml file empty?");
					}
					
					// Reopen the database to make sure exchange rate is written into the database
					DatabaseManager.INSTANCE.shutdownDatabase(false, false);
					
					DatabaseManager.INSTANCE.initDatabase(databaseLocation);
					
					// Preparing folders
					String timestamp = Util.getTimestamp();
					String outputFolder = OUTPUT_FOLDER + hostName + "\\" + Util.extractFileName(zipFile) + "-" + timestamp + "\\";
					
					String hitsOutputFolder = outputFolder + "Hits\\";
					logger.info("Hits output folder: " + hitsOutputFolder);
					
					String dimensionsOutputFolder = outputFolder + "Dimensions\\";
					logger.info("Dimensions output folder: " + dimensionsOutputFolder);
					

					// Generate hits
					new HitsGenerator(hitsOutputFolder).generate(DataOrigin.RPM, true);

					// Generate dimensions
					boolean dimensionsGenerated = new DimensionsGenerator(dimensionsOutputFolder, OUTPUT_FOLDER_FOR_GLOBAL_REGIONAL, timestamp).generate(DataOrigin.RPM);
					
					// Archive zip file
					if (!Util.moveFile(zipFile, archiveFolder)) {
						logger.error("Can't archive file {} in folder {}", zipFile, archiveFolder);
						throw new RuntimeException();
					}
					logger.info("Archived file {} to folder {}", zipFile, archiveFolder);
					
					newFilesProcessed = true;
					
					if (!extractOnly) {
						//Upload dimensions
						if (dimensionsGenerated) {
							File dimensionsFolder = new File(dimensionsOutputFolder);
							for (File file : dimensionsFolder.listFiles()) {
								new GoogleDimensionsUploader().upload(file.getCanonicalPath());
							}
						} else {
							logger.info("No dimensions have been generated.");
						}
						
						// Upload hits
						File hitsFolder = new File(hitsOutputFolder);
						for (File file : hitsFolder.listFiles()) {
							if (!file.getCanonicalPath().contains("VALIDATION_FAILED")) {
								new GoogleHitsUploader().upload(file.getCanonicalPath());
							}
						}
						
						outstandingFiles = new GoogleUploaderDao().findOutstandingFiles(); 
						if (outstandingFiles.size() > 0) {
							processingFinished = false;
						} else {
							processingFinished = true;
							logger.info("Finished RPM Transformations successfully.");
						}
					} else {
						processingFinished = true;
					}
					
				} catch (Throwable t) {
					logger.error("An error occured, please check log files for details.");
					logger.debug(t.getMessage(), t);
					throw new RuntimeException();
				} finally {
					DatabaseManager.INSTANCE.shutdownDatabase(false, false);
				}
				
			}
		}
		
	}

}
