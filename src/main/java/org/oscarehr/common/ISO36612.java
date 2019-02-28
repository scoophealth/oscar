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
package org.oscarehr.common;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.oscarehr.util.MiscUtils;

public class ISO36612 {

	private static ISO36612 obj = null;
	
	Map<String,String> codeToHRStringMap = new HashMap<String,String>();
	
	
	public static ISO36612 getInstance() {
		if(obj == null) {
			obj = new ISO36612();
		}
		return obj;
	}
	
	public ISO36612() {
		InputStream in = null;
		JSONObject topLevelObj = null;
		try {
			in = this.getClass().getClassLoader().getResourceAsStream("iso-3166-2.json");
			String theString = IOUtils.toString(in, "UTF-8");
			topLevelObj = new JSONObject(theString);
		}catch(Exception e) {
			MiscUtils.getLogger().warn("Warning", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		try {
		Iterator<String> iter =  topLevelObj.keys();
		while(iter.hasNext()) {
			String countryCode = iter.next();
			JSONObject country = topLevelObj.getJSONObject(countryCode);
			String countryName = country.getString("name");
			JSONObject divisions = (JSONObject)country.get("divisions");
			Iterator<String> iter2 =  divisions.keys();
			while(iter2.hasNext()) {
				String divisionCode = iter2.next();
				String divisionName = divisions.getString(divisionCode);
				codeToHRStringMap.put(divisionCode, divisionName + "," + countryName);
				if(divisionCode.startsWith("CA-")) {
					codeToHRStringMap.put(divisionCode.substring(3), divisionName + "," + countryName);
				}
			}
			
		}
		}catch(Exception e) {
			MiscUtils.getLogger().warn("Warning", e);
		}
		
	}
	
	public String translateCodeToHumanReadableString(String code) {
		if(StringUtils.isEmpty(code)) {
			return null;
		}
		return codeToHRStringMap.get(code);
	}
	
	public static void main(String args[]) {
			MiscUtils.getLogger().info(ISO36612.getInstance().translateCodeToHumanReadableString("ON"));
		
		
	}
}
