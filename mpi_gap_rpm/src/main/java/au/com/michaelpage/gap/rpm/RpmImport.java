package au.com.michaelpage.gap.rpm;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.michaelpage.gap.common.Settings;
import au.com.michaelpage.gap.common.util.DatabaseManager;
import au.com.michaelpage.gap.common.util.SQLGeneratorHelper;
import au.com.michaelpage.gap.common.util.SQLUtil;
import au.com.michaelpage.gap.common.util.Util;
import au.com.michaelpage.gap.rpm.model.Event;
import au.com.michaelpage.gap.rpm.model.EventRole;
import au.com.michaelpage.gap.rpm.model.EventRoles;
import au.com.michaelpage.gap.rpm.model.Events;
import au.com.michaelpage.gap.rpm.model.Opportunities;
import au.com.michaelpage.gap.rpm.model.Opportunity;
import au.com.michaelpage.gap.rpm.model.PermPlacement;
import au.com.michaelpage.gap.rpm.model.PermPlacements;
import au.com.michaelpage.gap.rpm.model.Person;
import au.com.michaelpage.gap.rpm.model.Persons;
import au.com.michaelpage.gap.rpm.model.Staff;
import au.com.michaelpage.gap.rpm.model.Staffs;
import au.com.michaelpage.gap.rpm.model.TempPlacement;
import au.com.michaelpage.gap.rpm.model.TempPlacements;

import com.google.common.io.Files;

public class RpmImport {
	
	private static final Logger logger = LoggerFactory.getLogger(RpmImport.class);
	
	private Map<String, Double> currencyExchangeRates = new HashMap<String, Double>();
	
	
	public void importZipFile(String zipFile) {
		String tempFolder = unzipFileToTempFolder(zipFile);
		try {
			insertDataIntoTables(tempFolder + File.separator);
			createIndexes();
		} catch (Exception e) {
			throw new RuntimeException(e); 
		} finally {
			Util.delete(tempFolder);	
		}
	}
	
	private void insertDataIntoTables(String tempFolder) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DatabaseManager.INSTANCE.getConnection();

			for (Class clazz : new Class[] {Event.class, EventRole.class, Person.class, Opportunity.class, PermPlacement.class,  
					TempPlacement.class, Staff.class}) {
				String sqlCreateTable = SQLGeneratorHelper.generateCreateTable(clazz);
				ps = conn.prepareStatement(sqlCreateTable);
				ps.execute();
				ps.close();
			}
			
			ps = null;
			File importFile = null;
			
			importFile = new File(tempFolder + "event.xml");
			if (importFile.length() > 100) {
				List<Event> events = JAXBXMLHandler.unmarshal(importFile, Events.class).getEvents();
				logger.info("Imported {} events from {}", events.size(), importFile.getCanonicalPath());
				for (Event event : events) {
					
					// Keep message only for P02 type (CV received) in order to get Green & Amber flags
					if (!event.getType().trim().startsWith("P02")) {
						event.setDisplayName(null);
					}

					if (ps == null) {
						ps = conn.prepareStatement(SQLGeneratorHelper.generateInsert(event.getClass()));
					}
					SQLUtil.populatePS(ps, event);
					ps.executeUpdate();
				}
				ps.close();
			} else {
				logger.info("Imported 0 events from {}", importFile.getCanonicalPath());
			}
			
			
			ps = null;
			importFile = new File(tempFolder + "event_role.xml");
			if (importFile.length() > 100) {
				List<EventRole> eventRoles = JAXBXMLHandler.unmarshal(importFile, EventRoles.class).getEventRoles();
				logger.info("Imported {} event roles from {}", eventRoles.size(), importFile.getCanonicalPath());
				for (EventRole eventRole : eventRoles) {
					if (ps == null) {
						ps = conn.prepareStatement(SQLGeneratorHelper.generateInsert(eventRole.getClass()));
					}
					SQLUtil.populatePS(ps, eventRole);
					ps.executeUpdate();
				}
				ps.close();
			} else {
				logger.info("Imported 0 event roles from {}", importFile.getCanonicalPath());
			}
			
			
			ps = null;
			importFile = new File(tempFolder + "person.xml");
			if (importFile.length() > 100) {
				List<Person> persons = JAXBXMLHandler.unmarshal(importFile, Persons.class).getPersons();
				logger.info("Imported {} persons from {}", persons.size(), importFile.getCanonicalPath());
				for (Person person : persons) {
					if (ps == null) {
						ps = conn.prepareStatement(SQLGeneratorHelper.generateInsert(person.getClass()));
					}
					SQLUtil.populatePS(ps, person);
					ps.executeUpdate();
				}
				ps.close();
			} else {
				logger.info("Imported 0 persons from {}", importFile.getCanonicalPath());
			}


			ps = null;
			importFile = new File(tempFolder + "opportunity.xml");
			if (importFile.length() > 100) {
				List<Opportunity> opportunities = JAXBXMLHandler.unmarshal(importFile, Opportunities.class).getOpportunities();
				logger.info("Imported {} opportunities from {}", opportunities.size(), importFile.getCanonicalPath());
				for (Opportunity opportunity : opportunities) {
					if (ps == null) {
						ps = conn.prepareStatement(SQLGeneratorHelper.generateInsert(opportunity.getClass()));
					}
					SQLUtil.populatePS(ps, opportunity);
					ps.executeUpdate();
				}
				ps.close();
			} else {
				logger.info("Imported 0 opportunities from {}", importFile.getCanonicalPath());
			}

			
			ps = null;
			importFile = new File(tempFolder + "perm_booking.xml");
			if (importFile.length() > 100) {
				List<PermPlacement> permPlacements = JAXBXMLHandler.unmarshal(importFile, PermPlacements.class).getPermPlacements();
				logger.info("Imported {} perm placements from {}", permPlacements.size(), importFile.getCanonicalPath());
				for (PermPlacement permPlacement : permPlacements) {
					convertMultiCurrency(permPlacement);
					if (ps == null) {
						ps = conn.prepareStatement(SQLGeneratorHelper.generateInsert(permPlacement.getClass()));
					}
					SQLUtil.populatePS(ps, permPlacement);
					ps.executeUpdate();
				}
				ps.close();
			} else {
				logger.info("Imported 0 perm placements from {}", importFile.getCanonicalPath());
			}

			
			ps = null;
			importFile = new File(tempFolder + "temp_booking.xml");
			if (importFile.length() > 100) {
				List<TempPlacement> tempPlacements = JAXBXMLHandler.unmarshal(importFile, TempPlacements.class).getTempPlacements();
				logger.info("Imported {} temp placements from {}", tempPlacements.size(), importFile.getCanonicalPath());
				for (TempPlacement tempPlacement : tempPlacements) {
					if (ps == null) {
						ps = conn.prepareStatement(SQLGeneratorHelper.generateInsert(tempPlacement.getClass()));
					}
					SQLUtil.populatePS(ps, tempPlacement);
					ps.executeUpdate();
				}
				ps.close();
			} else {
				logger.info("Imported 0 temp placements from {}", importFile.getCanonicalPath());
			}
			
			
			ps = null;
			importFile = new File(tempFolder + "staff.xml");
			if (importFile.length() > 100) {
				List<Staff> staffs = JAXBXMLHandler.unmarshal(importFile, Staffs.class).getStaffs();
				logger.info("Imported {} staff from {}", staffs.size(), importFile.getCanonicalPath());
				for (Staff staff : staffs) {
					if (ps == null) {
						ps = conn.prepareStatement(SQLGeneratorHelper.generateInsert(staff.getClass()));
					}
					SQLUtil.populatePS(ps, staff);
					ps.executeUpdate();
				}
				ps.close();
			} else {
				logger.info("Imported 0 staff from {}", importFile.getCanonicalPath());
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			DatabaseManager.INSTANCE.closeConnection(null, ps, conn);
		}
	}
	
	private String unzipFileToTempFolder(String zipFile) {
		File tempFolder = Files.createTempDir();
		Util.unzip(zipFile, tempFolder.getAbsolutePath());
		return tempFolder.getAbsolutePath();
	}
	
	private void createIndexes() throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DatabaseManager.INSTANCE.getConnection();

			ps = conn.prepareStatement("create index idx_event_eventref on event(eventref)");
			ps.execute();
			ps.close();
			
			ps = conn.prepareStatement("create index idx_person_md5emailaddress on person(md5emailaddress)");
			ps.execute();
			ps.close();
			
			conn.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			DatabaseManager.INSTANCE.closeConnection(null, ps, conn);
		}
	}
	
	private void convertMultiCurrency(PermPlacement permPlacement) {
		if (!Util.isEmpty(permPlacement.getCurrencyCode())) {
			Double exchangeRate = currencyExchangeRates.get(permPlacement.getCurrencyCode());
			if (exchangeRate == null || exchangeRate <= 0) {
				exchangeRate = Util.getExchangeRate(permPlacement.getCurrencyCode(), 
						Settings.INSTANCE.getCurrencyCode(), Settings.INSTANCE.getExtractDate());
				currencyExchangeRates.put(permPlacement.getCurrencyCode(), exchangeRate);
			}
			permPlacement.setFeeAmount(new Double(permPlacement.getFeeAmount() * exchangeRate).floatValue());
		}
	}
	

}
