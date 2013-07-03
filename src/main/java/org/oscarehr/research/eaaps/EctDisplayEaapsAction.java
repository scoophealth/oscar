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
import org.oscarehr.common.dao.StudyDataDao;
import org.oscarehr.common.dao.UserDSMessagePrefsDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.StudyData;
import org.oscarehr.common.model.UserDSMessagePrefs;
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

	private static Logger logger = Logger.getLogger(EctDisplayEaapsAction.class);
	
	private boolean enabled;
	
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	
	private StudyDataDao studyDataDao = SpringUtils.getBean(StudyDataDao.class);
	
	private UserDSMessagePrefsDao userDsMessagePrefsDao = SpringUtils.getBean(UserDSMessagePrefsDao.class);
	
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
		
		Demographic demographic = demographicDao.getDemographic(bean.getDemographicNo());
		if (demographic == null) {
			logger.warn("Unable to find Demographic " + bean.getDemographicNo());
			return true;
		}
		
		EaapsHash hash = new EaapsHash(demographic);
		StudyData studyData = studyDataDao.findSingleByContent(hash.getHash());
		if (studyData == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Demographic " + demographic + " is not entered for a study");
			}
			return true;
		}
		
		Dao.setLeftHeading("eAAPs");
		Dao.setHeadingColour("FF6600"); // orange
		Dao.setMenuHeader("Menu Header");
		Dao.setRightURL(getEaapsUrl(request.getContextPath() + "/eaaps/eaaps.jsp"));  
        Dao.setRightHeadingID("eaaps");
		
		EaapsPatientData patientData; 
		try {
			EaapsServiceClient client = new EaapsServiceClient();
	        patientData = client.getPatient(hash.getHash());
        } catch (Exception e) {
        	logger.debug("Unable to retrieve patient data", e);
        	request.getSession().removeAttribute("eaapsInfo");        	
        	Dao.addItem(newItem("Unable to retrive patient data"));
        	return true;
        }
		
		configureMostResponsiblePhysicianFlag(patientData, demographic);
		
		request.getSession().setAttribute("eaapsInfo", patientData);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Loaded patient data: " + patientData);
		}
		
		// ensure that notification is displayed for each popup, if necessary  
		boolean isNotificationRequired = isNotificationRequired(hash.getHash(), patientData);
		if (isNotificationRequired) {
			String js = "<script language=\"javascript\">displayEaapsWindow(\"" +  patientData.getUrl() + "\", \"" + hash.getHash() + "\", \"" + StringEscapeUtils.escapeJavaScript(patientData.getMessage()) + "\" );</script>";
			Dao.setJavaScript(js);
		}
		
		if (!patientData.isEligibleForStudy()) {
			Dao.addItem(newItem("Not eligible", getEaapsUrl(request.getContextPath() + "/eaaps/eaaps.jsp"), null));
			return true;
		}
		
		
		// messages.getMessage(request.getLocale(), "oscarEncounter.LeftNavBar.Myoscar")
		if (patientData.isAapReviewCompleted()) {
			Dao.addItem(newItem("AAP delivered", getEaapsUrl(request.getContextPath() + "/eaaps/eaaps.jsp"), "red"));
			return true;			
		}
        
		if (patientData.isAapReviewStarted()) {
			Dao.addItem(newItem("AAP review started", getEaapsUrl(request.getContextPath() + "/eaaps/eaaps.jsp"), "red"));
			return true;			
		}	
		
		if (patientData.isRecommendationsAvailable()) {
			Dao.addItem(newItem("Recommendations are available", getEaapsUrl(patientData.getUrl(), true), null));
		}
		
		NavBarDisplayDAO.Item item;
		if (patientData.isAapAvailable()) {
			item = newItem("AAP is available", getEaapsUrl(patientData.getUrl(), true));
		} else {
			item = newItem("AAP is <b>not</b> available", getEaapsUrl(request.getContextPath() + "/eaaps/eaaps.jsp"), "red");
		}
		Dao.addItem(item);
		
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
	private void configureMostResponsiblePhysicianFlag(EaapsPatientData patientData, Demographic demo) {
		String url = patientData.getUrl();		
		if (url == null || url.trim().isEmpty()) {
			logger.debug("URL is not provided - exiting without replacing");
			return;
		}
		
	    String loggedInProviderNo = getProviderNo();
	    String mrpProviderNo = demo.getProviderNo();
	    boolean isMrpPhysicianLookingAtTheRecord = loggedInProviderNo.equals(mrpProviderNo);
	    
	    if (url.contains("?")) {
	    	url = url.concat("&");
	    } else {
	    	url = url.concat("?");
	    }
	    url = url.concat("isMrp=" + isMrpPhysicianLookingAtTheRecord);
	    
	    patientData.replaceUrl(url);
    }

	private boolean isNotificationRequired(String hash, EaapsPatientData patientData) {
		String providerNo = getProviderNo();
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

	private String getProviderNo() {
		LoggedInInfo info = LoggedInInfo.loggedInInfo.get();
		if (info == null) {
			return null;
		}
		
		if (info.loggedInProvider == null) {
			return null;
		}
			
		return info.loggedInProvider.getProviderNo();
    }

	private NavBarDisplayDAO.Item newItem(String title) {
		return newItem(title, null);
	}
	
	private NavBarDisplayDAO.Item newItem(String title, String color) {
		return newItem(title, "return false;", color);
	}
	
	private NavBarDisplayDAO.Item newItem(String title, String url, String color) {
		NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
	    item.setTitle(title);
	    item.setURL(url);
	    if (color != null) {
	    	item.setColour(color);
	    }
	    item.setURLJavaScript(true);	    
	    return item;
    }
	
	private String getEaapsUrl(String url) {
		return getEaapsUrl(url, false);
	}
	
	private String getEaapsUrl(String url, boolean isNarrow) {
		int width = isNarrow ? 400 : 1000;
		return "popupPage(700," + width + ",'eaaps','" + url + "');return false;";
	}

	@Override
	public String getCmd() {
		return "eaaps";
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
