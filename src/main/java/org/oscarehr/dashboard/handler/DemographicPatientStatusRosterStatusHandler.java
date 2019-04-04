/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.dashboard.handler;

//import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONArray;

import org.oscarehr.common.model.Demographic; 
import org.oscarehr.common.dao.DemographicDao; 
import org.oscarehr.common.dao.DemographicArchiveDao; 
//import org.oscarehr.common.model.DemographicArchive; 
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DemographicExtArchiveDao; 
//import org.oscarehr.common.model.DemographicExt; 
//import org.oscarehr.common.model.DemographicExtArchive; 
import org.oscarehr.util.LoggedInInfo; 

public class DemographicPatientStatusRosterStatusHandler {

	private static Logger logger = MiscUtils.getLogger();
    private LoggedInInfo loggedInInfo;
	static DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	DemographicArchiveDao demographicArchiveDao = SpringUtils.getBean(DemographicArchiveDao.class);
	DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	DemographicExtArchiveDao demographicExtArchiveDao = SpringUtils.getBean(DemographicExtArchiveDao.class);
	
	public Boolean setPatientStatusInactiveJson( String jsonString) {
		Boolean result = false;
		if( jsonString == null || jsonString.isEmpty()) return false;
		String providerNo = getProviderNo();
		if (providerNo == null || providerNo.isEmpty()) return false;

		if( ! jsonString.startsWith("[")) {
			jsonString = "[" + jsonString;
		}
		if( ! jsonString.endsWith("]")) {
			jsonString = jsonString + "]";
		}
		JSONArray jsonArray = JSONArray.fromObject( jsonString );
		Integer arraySize = jsonArray.size();
		for (int i = 0; i < arraySize; i++) {
			result = setPatientStatusInactive(jsonArray.getString(i));
			if (!result) return false;
		}
		return true;
	}
	
	public Boolean setPatientStatusInactive(String demographicNo) {
		Demographic demographic = demographicDao.getDemographic(demographicNo);
		if(demographic != null) {
			demographic.setPatientStatus("IN");
			demographic.setPatientStatusDate(null);
			String providerNo = getProviderNo();
			if (providerNo != null && !providerNo.isEmpty()) {
				demographic.setLastUpdateUser(providerNo);
			} else {
				return false;
			}
			demographic.setLastUpdateDate(new java.util.Date());
			demographicDao.save(demographic);
			logger.info("demographic_no="+demographicNo+" set INACTIVE by "+providerNo);
			return true;
		}
		return false;
	}
	
	public Boolean setRosterStatus(String demographicNo, String rosterStatus) {
		Demographic demographic = demographicDao.getDemographic(demographicNo);
		if(demographic != null) {
			demographic.setRosterStatus(rosterStatus);
			demographic.setRosterDate(null);
			//TODO: Determine whether we need to do more here to save roster history
			String providerNo = getProviderNo();
			if (providerNo != null && !providerNo.isEmpty()) {
				demographic.setLastUpdateUser(providerNo);
			} else {
				return false;
			}
			demographic.setLastUpdateDate(new java.util.Date());
			demographicDao.save(demographic);
			return true;
		}
		return false;
	}

	public LoggedInInfo getLoggedinInfo() {
		return loggedInInfo;
	}

	public void setLoggedinInfo(LoggedInInfo loggedInInfo) {
		this.loggedInInfo = loggedInInfo;
	}       

	private String getProviderNo() {
		String providerNo = null;
		if (loggedInInfo != null) {
			providerNo = getLoggedinInfo().getLoggedInProviderNo();
		}
		return providerNo;
	}

	//Do we need to do anything with these tables?
	//DemographicExt
	//DemographicArchive
	//DemographicArchiveExt
	//Log

}
    
