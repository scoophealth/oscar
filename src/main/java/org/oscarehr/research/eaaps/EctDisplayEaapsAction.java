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
package org.oscarehr.research.eaaps;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.UserDSMessagePrefsDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.UserDSMessagePrefs;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarEncounter.pageUtil.EctDisplayAction;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.oscarEncounter.pageUtil.NavBarDisplayDAO;

/**
 * Struts action for handling display of the EAAPs widget on the eChart.
 */
public class EctDisplayEaapsAction extends EctDisplayAction {

	private static final String EAAPS = "eaaps";
	
	private static final String EAAPS_ERROR_MESSAGE = "Patient not found in eAAPS database";

	private static Logger logger = Logger.getLogger(EctDisplayEaapsAction.class);
	
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	
	private UserDSMessagePrefsDao userDsMessagePrefsDao = SpringUtils.getBean(UserDSMessagePrefsDao.class);
	
	private DxresearchDAO dxresearchDAO = SpringUtils.getBean(DxresearchDAO.class);
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public EctDisplayEaapsAction() {
		OscarProperties props = OscarProperties.getInstance();
		setEnabled(Boolean.parseBoolean(props.getProperty("eaaps.enabled", "false")));
	}
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return super.execute(mapping, form, request, response);
	}

	/**
	 * Sets up the nav bar dao depending on the EAAPS service query results and demographic information.
	 * 
	 * @see oscar.oscarEncounter.pageUtil.EctDisplayAction#getInfo(oscar.oscarEncounter.pageUtil.EctSessionBean, javax.servlet.http.HttpServletRequest, oscar.oscarEncounter.pageUtil.NavBarDisplayDAO, org.apache.struts.util.MessageResources)
	 */
	@Override
	public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
		if (!isEnabled()) {
			return true;
		}
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_newCasemgmt.eaaps", "r", null)) {
        	return true;
        }
		
		Demographic demographic = demographicDao.getDemographic(bean.getDemographicNo());
		if (demographic == null) {
			logger.warn("Unable to find Demographic " + bean.getDemographicNo());
			return true;
		}
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		EaapsHash hash = new EaapsHash(demographic);
		if(!dxresearchDAO.activeEntryExists(demographic.getDemographicNo(), "icd9", "493")){
			if (logger.isDebugEnabled()) {
				logger.debug("Demographic " + demographic + " is not entered for a study");
			}
			return true;
		}
		
		Dao.setLeftHeading("eAAPS");
		Dao.setHeadingColour("FF6600"); // orange
		Dao.setMenuHeader("Menu Header");  
        Dao.setRightHeadingID("eaaps");
		
		EaapsPatientData patientData;
		try {
			EaapsServiceClient client = new EaapsServiceClient();
	        patientData = client.getPatient(hash.getHash());
        } catch (Exception e) {
        	logger.debug("Unable to retrieve patient data", e);
        	request.getSession().removeAttribute("eaapsInfo");        	
        	Dao.addItem(newItem(EAAPS_ERROR_MESSAGE));
        	return true;
        }
		
		configureMostResponsiblePhysicianFlag(loggedInInfo, patientData, demographic);
		request.getSession().setAttribute("eaapsInfo", patientData);		
		if (logger.isDebugEnabled()) {
			logger.debug("Loaded patient data: " + patientData);
		}
		
		// ensure that notification is displayed for page load, if necessary  
		boolean isNotificationRequired = isNotificationRequired(loggedInInfo, hash.getHash(), patientData);
		if (isNotificationRequired) {
			String linkUrl = patientData.isUrlProvided() ? patientData.getUrl() : "";
			String js = "<script language=\"javascript\">displayEaapsWindow(\"" +  linkUrl + "\", \"" + hash.getHash() + "\", \"" + StringEscapeUtils.escapeJavaScript(patientData.getMessage()) + "\" );</script>";
			Dao.setJavaScript(js);
		}
		
		String eaapsUrl = null;
		if (patientData.isUrlProvided()) {
			eaapsUrl = getEaapsUrl(patientData.getUrl(), true);
		}
		
		String widgetMessage = patientData.getWidgetMessage();
		if (widgetMessage == null || widgetMessage.isEmpty()) {
			widgetMessage = EAAPS_ERROR_MESSAGE;
		}
		Dao.addItem(newItem(widgetMessage, eaapsUrl, null));
		
		return true;
	}

	/**
	 * Signal if the MRP is currently looking at the record. 
	 * 
	 * @param patientData
	 * 		Patient data loaded from the web service
	 * @param demo
	 * 		Demographic being loaded
	 */
	private void configureMostResponsiblePhysicianFlag(LoggedInInfo loggedInInfo, EaapsPatientData patientData, Demographic demo) {
		String urlString = patientData.getUrl();		
		if (urlString == null || urlString.trim().isEmpty()) {
			logger.debug("URL is not provided - exiting without replacing");
			return;
		}
		
	    String mrpProviderNo = demo.getProviderNo();
	    boolean isMrpPhysicianLookingAtTheRecord = loggedInInfo.getLoggedInProviderNo().equals(mrpProviderNo);
	    
	    StringBuilder buf = new StringBuilder(urlString);
	    
	    if (urlString.contains("?")) {
	    	buf.append("&");
	    } else {
	    	buf.append("?");
	    }
	    buf.append("isMrp=").append(isMrpPhysicianLookingAtTheRecord);
	    buf.append("&pNo=").append(loggedInInfo.getLoggedInProviderNo());
	    patientData.replaceUrl(buf.toString());
    }

	private boolean isNotificationRequired(LoggedInInfo loggedInInfo, String hash, EaapsPatientData patientData) {
		String providerNo = loggedInInfo.getLoggedInProviderNo();
		String resourceId = hash;
		List<UserDSMessagePrefs> prefs = userDsMessagePrefsDao.findMessages(providerNo, EAAPS, resourceId, false);
	    // no pref's saved means that this notification hasn't been dismissed yet by this user 
		if (prefs.isEmpty()) {
	    	return true;
	    }
	    
		// in case no updated timestamp is available - assume no notification is required
		Date statusChangeTimestamp = patientData.getUpdatedTimestamp();
		if (statusChangeTimestamp == null) {
			return false;
		}
		
		// in case status changed after resource was updated - still display the notification
		// this takes care of the case when status changed on the eAAPs side, but notification 
		// has been dismissed
		for(UserDSMessagePrefs pref : prefs) {
			if (pref.getResourceUpdatedDate() == null ) {
				continue;
			}
			
			if (pref.getResourceUpdatedDate().before(statusChangeTimestamp)) {
				return true;
			}
		}
	
		// for all other cases, say that notification has been dismissed
		return false;
    }
	
	private String getEaapsUrl(String url, boolean isNarrow) {
		int width = isNarrow ? 400 : 1000;
		return "popupPage(700," + width + ",'eaaps','" + url + "');return false;";
	}

	@Override
	public String getCmd() {
		return "eaaps";
	}

}
