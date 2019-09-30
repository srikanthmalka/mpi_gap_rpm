package au.com.michaelpage.gap.rpm;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DbUnitDateAdapter extends XmlAdapter<String, Date> {
	
	private static final Logger logger = LoggerFactory.getLogger(DbUnitDateAdapter.class);

    private SimpleDateFormat xmlDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    
    private SimpleDateFormat xmlDateFormatShort = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    
    private SimpleDateFormat dbUnitDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    private SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String marshal(Date v) throws Exception {
        return dbUnitDateFormat.format(v);
    }

    @Override
    public Date unmarshal(String v) {
        try {
        	if (v.contains("T")) {
            	if (v.length() == 19) {
            		return xmlDateFormatShort.parse(v);
            	} else {
                	return xmlDateFormat.parse(v);
            	}
            } else {
            	if (v.length() == 10) { 
            		return dateOnlyFormat.parse(v);
            	} else {
            		return dbUnitDateFormat.parse(v);
            	}
            }
        } catch (Exception e) {
			logger.error("An error occurred during conversion of date {}, please check log files for details.", v);
			logger.debug(e.getMessage(), e);
        	throw new RuntimeException(e);
        }
    }

}