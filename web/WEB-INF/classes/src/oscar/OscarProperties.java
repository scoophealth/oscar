// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster Unviersity
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/*
 * Updated by Eugene Petruhin on 21 jan 2009 while fixing missing "New Note" link
 * New isPropertyActive() function is introduced and everybody is encouraged to use it
 */
/**
 * This class will hold OSCAR & CAISI properties.
 * It is a singleton class. Do not instantiate it, use the method getInstance().
 * Every time the properties file changes, tomcat must be restarted.
 */
public class OscarProperties extends Properties {

	private static final long serialVersionUID = -5965807410049845132L;
	private static OscarProperties oscarProperties = new OscarProperties();
	private static boolean loaded = false;
	private static final Set<String> activeMarkers = new HashSet<String>(Arrays.asList(new String[] {"true", "yes", "on"}));

	static {
		try {
			readFromFile("/oscar_mcmaster.properties", oscarProperties);
		} catch (IOException e) {
			// don't use a logger here or your asking for trouble, it's a static initialiser
			e.printStackTrace();
		}
	}
        
	/**
	 * @return OscarProperties the instance of OscarProperties
	 */
	public static OscarProperties getInstance() {
		return oscarProperties;
	}

	private static void readFromFile(String url, Properties p) throws IOException {
		InputStream is = OscarProperties.class.getResourceAsStream(url);
		if (is == null) is = new FileInputStream(url);

		try {
			p.load(is);
		} finally {
			is.close();
		}
	}

	/* If cant find the file, inform and continue */
	/*
	 * private OscarProperties() {
	 * 
	 * InputStream is = getClass().getResourceAsStream("/oscar_mcmaster.properties"); try { load(is); } catch (Exception e) { System.out.println("Error, file oscar_mcmaster.properties not found.");
	 * System.out.println("This file must be placed at WEB-INF/classes."); }
	 * 
	 * try{ is.close(); } catch (IOException e) { System.out.println("IO error."); e.printStackTrace(); } } //OscarProperties - end
	 */

	/* Do not use this constructor. Use getInstance instead */
	private OscarProperties() {
		System.out.println("OSCAR PROPS CONSTRUCTOR");
	}

        
        /*
         * Check to see if the properties to see if that property exists.
         */
        public boolean hasProperty(String key){
            boolean prop = false;
            String propertyValue = getProperty(key);
            if (propertyValue != null) {
                    prop = true;
            }
            return prop;
        }
	/**
	 * Will check the properties to see if that property is set and if it's set to the given value.
	 * If it is method returns true if not method returns false.
	 * 
	 * This method was improved to ensure positive response on any "true", "yes" or "on" property value.
	 * 
	 * @param key
	 *            key of property
	 * @param val
	 *            value that will cause a true value to be returned
	 * @return boolean
	 */
	public boolean getBooleanProperty(String key, String val) {
		// if we're checking for positive value, any "active" one will do
		if (val != null && activeMarkers.contains(val.toLowerCase())) {
			return isPropertyActive(key);
		}

		return getProperty(key, "").equalsIgnoreCase(val);
	}

	/**
	 * Will check the properties to see if that property is set and if it's set to "true", "yes" or "on" value.
	 * If it is method returns true if not method returns false.
	 * 
	 * @param key
	 *            key of property
	 * @return boolean
	 *            whether the property is active
	 */
	public boolean isPropertyActive(String key) {
		return activeMarkers.contains(getProperty(key, "").toLowerCase());
	}

	public void loader(InputStream propertyStream) {
		if (!loaded) {
			try {
				load(propertyStream);
				propertyStream.close();
				loaded = true;
			} catch (IOException ex) {
				System.err.println("IO Error: " + ex.getMessage());
			}
		}
	}

	public void loader(String propFileName) throws java.io.FileNotFoundException {
		if (!loaded) {
			FileInputStream fis2 = new FileInputStream(propFileName);
			try {
				load(fis2);
				fis2.close();
				loaded = true;
			} catch (IOException ex) {
				System.err.println("IO Error: " + ex.getMessage());
			}
		}
	}

	/*
	 * Comma delimited spring configuration modules
	 * Options:  Caisi,Indivo
	 * Caisi  - Required to run the Caisi Shelter Management System
	 * Indivo - Indivo PHR record. Required for integration with Indivo.
	 */

	/*
	 * not being used - commenting out
	public final String ModuleNames = "ModuleNames";
	*/  

	public Date getStartTime(){
		String str  = getProperty("OSCAR_START_TIME");
		Date ret = null;
		try {
			ret = new Date(Long.parseLong(str));
		} catch (Exception e){/*No Date Found*/}
		return ret;
	}

	public boolean isTorontoRFQ() {
		return isPropertyActive("TORONTO_RFQ");
	}
     
    public boolean isProviderNoAuto() {
		return isPropertyActive("AUTO_GENERATE_PROVIDER_NO");
	}

	public boolean isPINEncripted() {
		return isPropertyActive("IS_PIN_ENCRYPTED");
	}

	public boolean isSiteSecured() {
		return isPropertyActive("security_site_control");
	}

	public boolean isAdminOptionOn() {
		return isPropertyActive("with_admin_option");
	}

	public boolean isLogAccessClient() {
		return isPropertyActive("log_accesses_of_client");
	}

	public boolean isLogAccessProgram() {
		return isPropertyActive("log_accesses_of_program");
	}

	public boolean isAccountLockingEnabled() {
		return isPropertyActive("ENABLE_ACCOUNT_LOCKING");
	}

	public boolean isCaisiLoaded() {
		return isPropertyActive("caisi");
	}

	public String getDbType() {
		return getProperty("db_type");
	}

	public String getDbUserName() {
		return getProperty("db_username");
	}

	public String getDbPassword() {
		return getProperty("db_password");
	}

	public String getDbUri() {
		return getProperty("db_uri");
	}

	public String getDbDriver() {
		return getProperty("db_driver");
	}

	public String getBuildDate() {
		return getProperty("builddate");
	}
}
