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

import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONArray;

public class ExcludeDemographicHandler {
	
	private static Logger logger = MiscUtils.getLogger();
	
	static DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	List<Integer> demoIds;
	List<DemographicExt> demoExts;
	private LoggedInInfo loggedInInfo;
	private String excludeIndicator = "excludeIndicator";
	
	public List<Integer> getDemoIds(String indicatorName) {
		demoIds = demographicExtDao.findDemographicIdsByKeyVal(excludeIndicator, indicatorName);
		return demoIds;
	}
	
	public List<DemographicExt> getDemoExts(String indicatorName) {
		demoExts = new ArrayList<DemographicExt>();
		List<DemographicExt> allProviderExts = demographicExtDao.getDemographicExtByKeyAndValue(excludeIndicator, indicatorName);
		String providerNo = null;
		if (loggedInInfo != null) {
			providerNo = getLoggedinInfo().getLoggedInProviderNo();
		}
		for (DemographicExt e: allProviderExts) {
			if (e.getProviderNo().equals(providerNo)) {
				demoExts.add(e);
			}
		}
		return demoExts;
	}
	
	public void excludeDemoId(Integer demographicNo, String indicatorName) {
		if (demographicNo == null || indicatorName == null || indicatorName.isEmpty()) return;
		String providerNo = null;
		if (loggedInInfo != null) {
			providerNo = getLoggedinInfo().getLoggedInProviderNo();
		}
		demographicExtDao.addKey(providerNo, demographicNo, excludeIndicator, indicatorName);
	}
	
	public void excludeDemoIds(List<Integer> demographicNos, String indicatorName) {
		if (demographicNos == null || demographicNos.isEmpty() || indicatorName == null || indicatorName.isEmpty()) return;
		String providerNo = null;
		if (loggedInInfo != null) {
			providerNo = getLoggedinInfo().getLoggedInProviderNo();
		}
		for (Integer demographicNo: demographicNos) {
			demographicExtDao.addKey(providerNo, demographicNo, excludeIndicator, indicatorName);
			logger.info("demo: " + demographicNo + "excluded from indicatorTemplate " + indicatorName);
		}
	}
	
	public void unExcludeDemoIds(List<Integer> demographicNos, String indicatorName) {
		if (demographicNos == null || demographicNos.isEmpty() || indicatorName == null || indicatorName.isEmpty()) return;
		List<DemographicExt> allProvidersExts = demographicExtDao.getDemographicExtByKeyAndValue(excludeIndicator, indicatorName);
		String providerNo = null;
		if (loggedInInfo != null) {
			providerNo = getLoggedinInfo().getLoggedInProviderNo();
		}
		for (DemographicExt e: allProvidersExts) {
			// remove exclusion if provider_no matches or is null and the demongraphic_no matches
			if (e.getProviderNo().equals(providerNo) && demographicNos.contains(e.getDemographicNo())) {
				demographicExtDao.removeDemographicExt(e.getId());
				logger.info("demo: " + e.getDemographicNo() + "unexcluded from indicatorTemplate " + indicatorName);
			}
		}
	}
	
	public void excludeDemoIds( String jsonString, String indicatorName ) {
		String providerNo = null;
		if (loggedInInfo != null) {
			providerNo = getLoggedinInfo().getLoggedInProviderNo();
		}
		if( jsonString == null || jsonString.isEmpty() || indicatorName == null || indicatorName.isEmpty()) return;
		if( ! jsonString.startsWith("[")) {
			jsonString = "[" + jsonString;
		}
		if( ! jsonString.endsWith("]")) {
			jsonString = jsonString + "]";
		}
		JSONArray jsonArray = JSONArray.fromObject( jsonString );
		Integer arraySize = jsonArray.size();
		for (int i = 0; i < arraySize; i++) {
			demographicExtDao.addKey(providerNo, jsonArray.getInt(i), excludeIndicator, indicatorName);
		}
	}
	
	public void unExcludeDemoIds( String jsonString, String indicatorName ) {
		String providerNo = null;
		if (loggedInInfo != null) {
			providerNo = getLoggedinInfo().getLoggedInProviderNo();
		}
		if( jsonString == null || jsonString.isEmpty() || indicatorName == null || indicatorName.isEmpty()) return;
		if( ! jsonString.startsWith("[")) {
			jsonString = "[" + jsonString;
		}
		if( ! jsonString.endsWith("]")) {
			jsonString = jsonString + "]";
		}
		JSONArray jsonArray = JSONArray.fromObject( jsonString );
//		Integer arraySize = jsonArray.size();
//		for (int i = 0; i < arraySize; i++) {
//			demographicExtDao.addKey(providerNo, jsonArray.getInt(i), excludeIndicator, indicatorName);
//		}
	}
	
	public LoggedInInfo getLoggedinInfo() {
		return loggedInInfo;
	}

	public void setLoggedinInfo(LoggedInInfo loggedInInfo) {
		this.loggedInInfo = loggedInInfo;
	}	
}
