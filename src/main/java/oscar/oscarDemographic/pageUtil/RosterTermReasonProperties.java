package oscar.oscarDemographic.pageUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class RosterTermReasonProperties extends Properties {
	private static RosterTermReasonProperties rosterTermReasonProperties = new RosterTermReasonProperties();
	private static SortedSet<String> termReasons = new TreeSet<String>();
	
	private static Logger logger = MiscUtils.getLogger();

	static {
		String propFile = "/roster_termination_reasons.properties";
		InputStream is = RosterTermReasonProperties.class.getResourceAsStream(propFile);
		if (is==null) try {
	        is = new FileInputStream(propFile);
        } catch (FileNotFoundException e) {
	        logger.error("Roster Termination Reaons file not found!", e);
        }
		
		try {
			rosterTermReasonProperties.load(is);
        } catch (IOException e) {
	        logger.error("Error loading Roster Termination Reasons!", e);
        }
	}
	
	public static RosterTermReasonProperties getInstance() {
		return rosterTermReasonProperties;
	}
	
	public String getReasonByCode(String code) {
		return rosterTermReasonProperties.getProperty(code);
	}
	
	public SortedSet<String> getTermReasonCodes() {
		if (termReasons.isEmpty()) {
			
			Set<Object> kset = rosterTermReasonProperties.keySet();
			for (Object key : kset) {
				termReasons.add((String)key);
			}
		}
		return termReasons;
		
	}
}
