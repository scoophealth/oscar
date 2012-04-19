/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


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
