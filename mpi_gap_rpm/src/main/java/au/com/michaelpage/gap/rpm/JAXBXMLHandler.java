package au.com.michaelpage.gap.rpm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import com.google.common.io.Files;

import au.com.michaelpage.gap.rpm.model.Event;
import au.com.michaelpage.gap.rpm.model.Events;

public class JAXBXMLHandler {
	 
    private static class MyStreamReaderDelegate extends StreamReaderDelegate {
    	 
        public MyStreamReaderDelegate(XMLStreamReader xsr) {
            super(xsr);
        }
 
        @Override
        public String getAttributeLocalName(int index) {
            return super.getAttributeLocalName(index).toLowerCase();
        }
 
        @Override
        public String getLocalName() {
            return super.getLocalName().toLowerCase();
        }
 
    }    
	
    public static <T> T unmarshal(File importFile, Class<T> type) throws JAXBException, UnsupportedEncodingException, FileNotFoundException {
        int count = 0;
        while (true) {
        	count++;
        	//System.out.println("Attempt: " + (++count));
    		//FileInputStream fis = null;
            //BufferedInputStream bis = null;
            
            XMLInputFactory xif = null; 
            XMLStreamReader xsr = null;

        	try {
        		xif = XMLInputFactory.newInstance();
                xsr = xif.createXMLStreamReader(new FileInputStream(importFile));
        		
        		//fis = new FileInputStream(importFile);
        		//bis = new BufferedInputStream(fis);
        		JAXBContext context = JAXBContext.newInstance(type);
                Unmarshaller um = context.createUnmarshaller();
                um.setEventHandler(new DefaultValidationEventHandler());
        		return ((T)um.unmarshal(new MyStreamReaderDelegate(xsr)));
			} catch (JAXBException e) {
				try {
					Pattern p = Pattern.compile(".+An invalid XML character \\(Unicode: 0x(\\w+)\\).+");
					String errorMessage = e.getCause().toString().replaceAll("\n", "");;
					Matcher m = p.matcher(errorMessage);
					if (m.matches()) {
						if (!stripInvalidXMLCharacter(importFile, Integer.parseInt(m.group(1), 16))) {
							throw e;
						}
					} else {
						stripAllInvalidXMLCharacters(importFile);
					}
					
		        	if (count > 5) {
		        		throw e;
		        	}
					
				} catch (Exception e2) {
					//e.addSuppressed(e2);
					//e.
					throw e;
				}
				
			} catch (Throwable t) {
	        	throw new RuntimeException(t);
	        } finally {
	        	if (xsr != null) {
	        		try {
	        			xsr.close();
					} catch (XMLStreamException e) {
						// TODO Auto-generated catch block
					}
	        	}
	        }
        	
        } 
    }
    
    private static String getCharsetName(File file) throws IOException {
    	BufferedReader br = null;
    	String charset = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(file));

			if ((sCurrentLine = br.readLine()) != null) {
				
				Pattern p = Pattern.compile(".+encoding=\'([^']+)\'.+");
				Matcher m = p.matcher(sCurrentLine);
				if (m.matches()) {
					charset = m.group(1);
				}
			}
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return charset;
    }
    
    private static boolean stripInvalidXMLCharacter(File file, int invalid) throws IOException {
		Reader reader = null;
		Writer writer = null;
    	File output = File.createTempFile("temp_", ".xml");
		String charsetName = getCharsetName(file);
		charsetName = charsetName == null ?  "UTF-16LE" : charsetName; 
		
		boolean invalidCharRemoved = false;
		try {
			reader = new InputStreamReader(new FileInputStream(file), charsetName);
			writer = new OutputStreamWriter(new FileOutputStream(output), charsetName);
			
			for(int data; (data = reader.read()) != -1;) {
				
				if (data != invalid) {
					writer.write(data);
					writer.flush();
				} else {
					System.out.println("Stripping invalid XML character (Unicode: 0x" + Integer.toHexString(invalid) + ") File: " + file.getCanonicalPath());
					invalidCharRemoved = true;
				}
		    }
		} finally {
			if (reader != null) {
				reader.close();
			}

			if (writer != null) {
				writer.close();
			}
		}
		
		if (invalidCharRemoved) {
			file.delete();
			Files.copy(output, file);
			output.delete();
		} else {
			output.delete();
		}
		
		return invalidCharRemoved;
	}

    private static void stripAllInvalidXMLCharacters(File file) throws IOException {
    	
    	File output = File.createTempFile("temp_", ".xml");
    	
    	BufferedReader br = null;
    	PrintWriter out = null;
		try {
			br = new BufferedReader(new FileReader(file));
			out = new PrintWriter(output);
			
			StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		    	sb.append(line.replaceAll("&#\\d{5,8};", ""));
		        sb.append("\n");
		        line = br.readLine();
		    }
		    
		    out.println(sb);
		} finally {
		    if (br != null) {
		    	br.close();
		    }
		    
		    if (out != null) {
		    	out.close();
		    }
		}	
		
		file.delete();
		Files.copy(output, file);
		output.delete();
	}
    
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, JAXBException {
    	String name = "c:\\temp\\1\\event.xml";
    	List<Event> events = JAXBXMLHandler.unmarshal(new File(name), Events.class).getEvents();
    	System.out.println(events.size());
    }
 
}
