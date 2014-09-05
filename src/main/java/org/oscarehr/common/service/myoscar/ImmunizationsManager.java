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


package org.oscarehr.common.service.myoscar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.dao.SentToPHRTrackingDao;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.SentToPHRTracking;
import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.myoscar_server.ws.InvalidRequestException_Exception;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer4;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.ws.UnsupportedEncodingException_Exception;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;

public final class ImmunizationsManager {
	private static final Logger logger = MiscUtils.getLogger();
	private static final String OSCAR_IMMUNIZATIONS_DATA_TYPE = "IMMUNIZATION";
	private static final SentToPHRTrackingDao sentToPHRTrackingDao = (SentToPHRTrackingDao) SpringUtils.getBean("sentToPHRTrackingDao");
	
	private static HashMap<String,String> preventionExtLabels = null; 

	public static void sendImmunizationsToMyOscar(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, Integer demographicId) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException, NotAuthorisedException_Exception, UnsupportedEncodingException_Exception, InvalidRequestException_Exception {
		// get last synced info

		// get the items for the person which are changed since last sync
		// for each item
		// send the item or update it

		Date startSyncTime = new Date();
		SentToPHRTracking sentToPHRTracking = MyOscarMedicalDataManagerUtils.getExistingOrCreateInitialSentToPHRTracking(demographicId, OSCAR_IMMUNIZATIONS_DATA_TYPE, MyOscarLoggedInInfo.getMyOscarServerBaseUrl());
		logger.debug("sendImmunizationsToMyOscar : demographicId=" + demographicId + ", lastSyncTime=" + sentToPHRTracking.getSentDatetime());

		PreventionDao preventionDao = (PreventionDao) SpringUtils.getBean("preventionDao");
		PreventionExtDao preventionExtDao = (PreventionExtDao) SpringUtils.getBean("preventionExtDao");
		List<Prevention> changedPreventions = preventionDao.findByDemographicIdAfterDatetime(demographicId, sentToPHRTracking.getSentDatetime());
		for (Prevention prevention : changedPreventions) {
			HashMap<String,String> preventionExt = preventionExtDao.getPreventionExt(prevention.getId());
			if (preventionExt.containsKey("result")) continue; //prevention tests are filtered out
			
			logger.debug("sendImmunizationsToMyOscar : preventionId=" + prevention.getId());
			MedicalDataTransfer4 medicalDataTransfer = toMedicalDataTransfer(loggedInInfo, myOscarLoggedInInfo, prevention, preventionExt);

			MyOscarMedicalDataManagerUtils.addMedicalData(loggedInInfo.getLoggedInProviderNo(), myOscarLoggedInInfo, medicalDataTransfer, OSCAR_IMMUNIZATIONS_DATA_TYPE, prevention.getId(), true, true);
		}

		sentToPHRTracking.setSentDatetime(startSyncTime);
		sentToPHRTrackingDao.merge(sentToPHRTracking);
	}

	public static Document toXml(Prevention prevention, HashMap<String,String> preventionExt) throws ParserConfigurationException {
		Document doc = XmlUtils.newDocument("Immunization");
		
		String temp = StringUtils.trimToNull(prevention.getPreventionType());
		XmlUtils.appendChildToRootIgnoreNull(doc, "Type", temp);

		if (prevention.getNextDate() != null) {
			temp = DateFormatUtils.ISO_DATETIME_FORMAT.format(prevention.getNextDate());
			XmlUtils.appendChildToRootIgnoreNull(doc, "NextDate", temp);
		}
		
		if (prevention.isRefused()) XmlUtils.appendChildToRootIgnoreNull(doc, "Status", "Refused");
		else if (prevention.isIneligible()) XmlUtils.appendChildToRootIgnoreNull(doc, "Status", "Ineligible");
		else XmlUtils.appendChildToRootIgnoreNull(doc, "Status", "Completed");
		
		if (preventionExtLabels==null) {
			fillPreventionExtLabels();
		}
		
		//append dose1 & dose2 to comments
		String comments = preventionExt.get("comments");
		
		comments = appendLine(comments, "Dose1: ", preventionExt.get("dose1"));
		comments = appendLine(comments, "Dose2: ", preventionExt.get("dose2"));
		
		if (preventionExt.containsKey("comments")) preventionExt.remove("comments");
		if (StringUtils.isNotBlank(comments)) preventionExt.put("comments", comments);
		
		//append prevention extra info to xml
		for (String key : preventionExtLabels.keySet()) {
			temp = StringUtils.trimToNull(preventionExt.get(key));
			XmlUtils.appendChildToRootIgnoreNull(doc, preventionExtLabels.get(key), temp);
		}

		return (doc);
	}
	

	private static MedicalDataTransfer4 toMedicalDataTransfer(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, Prevention prevention, HashMap<String,String> preventionExt) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException {

		String providerNo = prevention.getProviderNo();

		if (providerNo != null) {
			MedicalDataTransfer4 medicalDataTransfer = MyOscarMedicalDataManagerUtils.getEmptyMedicalDataTransfer(myOscarLoggedInInfo, prevention.getPreventionDate(), providerNo, prevention.getDemographicId());
			medicalDataTransfer.setCompleted(false); // preventions are changeable, therefore, they're never completed

			Document doc = toXml(prevention, preventionExt);
			medicalDataTransfer.setData(XmlUtils.toString(doc, false));

			medicalDataTransfer.setMedicalDataType(MedicalDataType.IMMUNISATION.name());

			medicalDataTransfer.setOriginalSourceId(MyOscarMedicalDataManagerUtils.generateSourceId(loggedInInfo.getCurrentFacility().getName(), OSCAR_IMMUNIZATIONS_DATA_TYPE, prevention.getId()));

			boolean active = true;
			if (prevention.isDeleted()) active = false;
			medicalDataTransfer.setActive(active);

			return (medicalDataTransfer);
		} else {
			return (null);
		}

	}

	private static void fillPreventionExtLabels() {
		if (preventionExtLabels!=null) return;
		
		preventionExtLabels = new HashMap<String,String>();
		preventionExtLabels.put("name", "VaccineName");
		preventionExtLabels.put("location", "AppliedBodyLocation");
		preventionExtLabels.put("route", "Route");
		preventionExtLabels.put("dose", "Dose");
		preventionExtLabels.put("lot", "Lot");
		preventionExtLabels.put("manufacture", "Manufacture");
		preventionExtLabels.put("comments", "Comments");
	}
	
	private static String appendLine(String s1, String label, String s2) {
		if (StringUtils.isBlank(s2)) return StringUtils.trimToEmpty(s1);
		
		if (StringUtils.isNotBlank(s1) && StringUtils.isNotBlank(s2)) s1 += "\n";
		return StringUtils.trimToEmpty(s1) + StringUtils.trimToEmpty(label) + StringUtils.trimToEmpty(s2);
	}
}
