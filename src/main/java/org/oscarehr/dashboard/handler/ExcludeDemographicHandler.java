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
import java.util.Date;
//import java.util.ArrayList;
import java.util.List;
//import java.util.Objects;

import org.apache.log4j.Logger;
//import org.oscarehr.common.dao.DemographicExtArchiveDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Property;
import org.oscarehr.dashboard.display.beans.DrilldownBean;
import org.oscarehr.managers.DashboardManager;
//import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
//import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONArray;

public class ExcludeDemographicHandler {
	
	private static Logger logger = MiscUtils.getLogger();
	
	static DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	List<Integer> demoIds;
	List<DemographicExt> demoExts;
	private LoggedInInfo loggedInInfo;
	private String excludeIndicator = "excludeIndicator";
	Date now = new java.util.Date();

	public List<Integer> getDemoIds(String indicatorName) {
		if (indicatorName == null || indicatorName.isEmpty()) return null;
		demoIds = new ArrayList<Integer>();
		List<DemographicExt> allProviderDemoExts = demographicExtDao.getDemographicExtByKeyAndValue(excludeIndicator, indicatorName);
		logger.debug("getDemosIds: " + allProviderDemoExts + " matching extensions for template " + indicatorName);
		String providerNo = getProviderNo();
		for (DemographicExt e: allProviderDemoExts) {
			if (e.getProviderNo().equals(providerNo) && isCurrentExclusion(e)) {
				demoIds.add(e.getDemographicNo());
				logger.debug("template: " + indicatorName +" getDemoIds returning: " + e.getDemographicNo());
			}
		}
		return demoIds;
	}
	
	public List<DemographicExt> getDemoExts(String indicatorName) {
		if (indicatorName == null || indicatorName.isEmpty()) return null;
		demoExts = new ArrayList<DemographicExt>();
		List<DemographicExt> allProviderDemoExts = demographicExtDao.getDemographicExtByKeyAndValue(excludeIndicator, indicatorName);
		logger.debug("getDemosExts: " + allProviderDemoExts + " matching extensions for template " + indicatorName);
		String providerNo = getProviderNo();
		for (DemographicExt e: allProviderDemoExts) {
			if (e.getProviderNo().equals(providerNo) && isCurrentExclusion(e)) {
				demoExts.add(e);
				logger.debug("template: " + indicatorName +" getDemoExts returning: " + e.getDemographicNo());
			}
		}
		return demoExts;
	}
	
	public void excludeDemoId(Integer demographicNo, String indicatorName) {
		if (demographicNo == null || indicatorName == null || indicatorName.isEmpty()) return;
		String providerNo = getProviderNo();
		// It is possible that there is already a exclusion present in demographicExt but the
		// exclusion is no longer current.  The old one could be removed, its creation date
		// could be updated to the current date, or we can just ignore it.  For the moment we
		// will ignore non-current entries.  There probably wouldn't be many and they serve as
		// a record that the patient was excluded from the indicator in the past.
		demographicExtDao.addKey(providerNo, demographicNo, excludeIndicator, indicatorName);
		logger.info("demo: " + demographicNo + " excluded from indicatorTemplate " + indicatorName);
	}
	
	public void excludeDemoIds(List<Integer> demographicNos, String indicatorName) {
		if (demographicNos == null || demographicNos.isEmpty() || indicatorName == null || indicatorName.isEmpty()) return;
		String providerNo = getProviderNo();
		for (Integer demographicNo: demographicNos) {
			demographicExtDao.addKey(providerNo, demographicNo, excludeIndicator, indicatorName);
			logger.info("demo: " + demographicNo + " excluded from indicatorTemplate " + indicatorName);
		}
	}
	
	public void unExcludeDemoIds(List<Integer> demographicNos, String indicatorName) {
		if (demographicNos == null || demographicNos.isEmpty() || indicatorName == null || indicatorName.isEmpty()) return;
		String providerNo = getProviderNo();
		List<DemographicExt> allProviderDemoExts = demographicExtDao.getDemographicExtByKeyAndValue(excludeIndicator, indicatorName);
		logger.debug("unExcludeDemoIds: " + allProviderDemoExts + " matching extensions for template " + indicatorName);
		for (DemographicExt e: allProviderDemoExts) {
			// remove exclusion if provider_no matches or is null and the demongraphic_no matches
			if (e.getProviderNo().equals(providerNo) && demographicNos.contains(e.getDemographicNo())) {
				demographicExtDao.removeDemographicExt(e.getId());
				logger.info("demo: " + e.getDemographicNo() + " unexcluded from indicatorTemplate " + indicatorName);
			}
		}
	}
	
	public void excludeDemoIds( String jsonString, String indicatorName ) {
		String providerNo = getProviderNo();
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
			logger.info("demo: " + jsonArray.getInt(i) + " excluded from indicatorTemplate " + indicatorName);
		}
	}
	
	public void unExcludeDemoIds( String jsonString, String indicatorName ) {
		if( jsonString == null || jsonString.isEmpty() || indicatorName == null || indicatorName.isEmpty()) return;
		if( ! jsonString.startsWith("[")) {
			jsonString = "[" + jsonString;
		}
		if( ! jsonString.endsWith("]")) {
			jsonString = jsonString + "]";
		}
		JSONArray jsonArray = JSONArray.fromObject( jsonString );
		String providerNo = getProviderNo();
		List<DemographicExt> allProviderDemoExts = demographicExtDao.getDemographicExtByKeyAndValue(excludeIndicator, indicatorName);
		logger.debug("unExcludeDemoIds (json): " + allProviderDemoExts + " matching extensions for template " + indicatorName);
		for (DemographicExt e: allProviderDemoExts) {
			// remove exclusion if provider_no matches or is null and the demongraphic_no matches
			if (e.getProviderNo().equals(providerNo) && jsonArray.contains(e.getDemographicNo())) {
				demographicExtDao.removeDemographicExt(e.getId());
				logger.info("demo: " + e.getDemographicNo() + " unexcluded from indicatorTemplate " + indicatorName);
			}
		}
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
			String surrogate = surrogateForProvider(providerNo);
			if (!surrogate.isEmpty()) {
				providerNo = surrogate;
			}
		}
		return providerNo;
	}
	
	/**
	 *Retrieve provider for which current provider is acting as a surrogate.
	 */
	public static String surrogateForProvider(String surrogate_providerNo) {
		PropertyDao dao = SpringUtils.getBean(PropertyDao.class);
		List<Property> props = dao.findByNameAndProvider("surrogate_for_provider", surrogate_providerNo);
		if(props.size()>0) {
			return props.get(0).getValue();
		}
		return new String();
	}
	
	// An exclusion is only valid for a finite interval.  The interval may need to be modified
	// based on user feedback.
	private Boolean isCurrentExclusion(DemographicExt de) {
		Boolean result = true;
		
		int MILLIS_IN_SECOND = 1000;
	    int SECONDS_IN_MINUTE = 60;
	    int MINUTES_IN_HOUR = 60;
	    int HOURS_IN_DAY = 24;
	    int DAYS_IN_YEAR = 365;
	    long MILLISECONDS_IN_YEAR =
	    		(long)MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR *
	    		HOURS_IN_DAY * DAYS_IN_YEAR;
		
	    if (now.getTime() - de.getDateCreated().getTime() > MILLISECONDS_IN_YEAR ) {
			result = false;
		}
		
	    return result;
	}
	
    public String getDrilldownIdentifier(int indicatorTemplateId) {
    	logger.info("entering getDrilldownIdentifer with indicatorTemplateId="+indicatorTemplateId);
    	String identifier = null;
    	DashboardManager dashboardManager = SpringUtils.getBean(DashboardManager.class);
    	if (dashboardManager != null && loggedInInfo != null) {
    		DrilldownBean drilldown = dashboardManager.getDrilldownData(loggedInInfo, indicatorTemplateId, "null");
    		if (drilldown != null) {
    			identifier = getDrilldownIdentifier(drilldown.getName(),drilldown.getSubCategory(),drilldown.getCategory());
    		} else {
    			logger.info("drilldown is null");
    		}
    	} else {
    		logger.info("dashboardManager is null");
    	}
    	logger.info("getDrilldownIdentifer returning " + identifier + " for indicatorTemplateId " + indicatorTemplateId);
    	return identifier;
    }
    
    public String getDrilldownIdentifier(String name, String category, String subCategory) {
    	return name + "|" + subCategory + "|" + category;
    }
}
