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
package org.oscarehr.common.dao;

import java.io.InputStream;
import java.util.Iterator;

import javax.persistence.Query;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONObject;
import org.oscarehr.common.model.ISO36612;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ISO36612Dao  extends AbstractDao<ISO36612>{

	public ISO36612Dao() {
		super(ISO36612.class);
	}
	
	public ISO36612 findByCode(String code) {
		String sqlCommand = "select i from ISO36612 where code=?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, code);
		
		return this.getSingleResultOrNull(query);
	}
	
	public String findProvinceByCode(String code) {
		ISO36612 result = findByCode(code);
		if(result != null) {
			return result.getProvince();
		}
		return null;
	}
	
	public String findCountryByCode(String code) {
		ISO36612 result = findByCode(code);
		if(result != null) {
			return result.getCountry();
		}
		return null;
	}
	
	public boolean reloadTable() {
		
		InputStream in = null;
		JSONObject topLevelObj = null;
		try {
			in = this.getClass().getClassLoader().getResourceAsStream("iso-3166-2.json");
			String theString = IOUtils.toString(in, "UTF-8");
			topLevelObj = new JSONObject(theString);
		}catch(Exception e) {
			MiscUtils.getLogger().warn("Warning", e);
			return false;
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		deleteAll();
		
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
					
					ISO36612 item = new ISO36612();
					item.setCode(divisionCode);
					item.setProvince(divisionName);
					item.setCountry(countryName);
					persist(item);
					
				}	
			}
		}catch(Exception e) {
			MiscUtils.getLogger().warn("Warning", e);
		}
		
		return true;
	}
	
	protected void deleteAll() {
		Query query = entityManager.createQuery("DELETE FROM ISO36612");
		query.executeUpdate();
	}
	
}