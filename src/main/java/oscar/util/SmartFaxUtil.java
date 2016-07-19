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


/***
 * Utility for smart fax.
 * 
 * @author Rohit Prajapati (rohitprajapati54@gmail.com)
 * 
 */
package oscar.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import oscar.OscarProperties;

public class SmartFaxUtil
{
	private static final String MODEM_KEY_PREFIX = "smartfax_modem_";
	
	/***
     * checking if provider is using default fax. if so return the default fax key in oscar.properties
     * @param providerFaxNo
     * @return
     */
    public static String getDefaultFaxModem(String providerFaxNo)
    {
    	String defaultFaxModem = "";

    	if(providerFaxNo!=null && providerFaxNo.trim().length()>0)
    	{
    		providerFaxNo = providerFaxNo.replaceAll("\\D", "");
    		Map<String, String> smartFaxProperties = getDefaultSmartFaxModemProperties();
    		
    		if(smartFaxProperties.get(providerFaxNo)!=null && smartFaxProperties.get(providerFaxNo).trim().length()>0)
    		{
    			defaultFaxModem = smartFaxProperties.get(providerFaxNo).trim();
    			defaultFaxModem = defaultFaxModem.replace(MODEM_KEY_PREFIX, "");
    		}
    		
    	}
    	
    	return defaultFaxModem;
    }
    
    /**
     * get smartfax modem properties from oscar.properties
     * returns map - key = fax no.(only digits), val = property key
     * @return
     */
    private static Map<String, String> smartFaxProperties = null;
    public static Map<String, String> getDefaultSmartFaxModemProperties()
    {
    	smartFaxProperties = null;
    	if(smartFaxProperties!=null)
    	{
    		return smartFaxProperties;
    	}
    	else
    	{
    		smartFaxProperties = new HashMap<String, String>();
    	}
    	
    	OscarProperties oscarProperties = OscarProperties.getInstance();
    	Set<Object> keySet = oscarProperties.keySet();
    	Iterator<Object> iter = keySet.iterator();
    	
    	Object key = null;
    	String keyStr = "";
    	String val = "";
    	while(iter.hasNext())
    	{
    		key = iter.next();
    		if(key!=null && key.toString().startsWith("smartfax_modem"))
    		{
    			keyStr = key.toString().trim();
    			val = oscarProperties.getProperty(keyStr).trim();
    			val = val.replaceAll("\\D", "");
    			smartFaxProperties.put(val, keyStr);
    		}
    	}
    	
    	return smartFaxProperties;
    }
}
