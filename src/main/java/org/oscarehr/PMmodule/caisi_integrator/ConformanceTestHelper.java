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


package org.oscarehr.PMmodule.caisi_integrator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.ProviderCommunicationTransfer;
import org.oscarehr.caisi_integrator.ws.ProviderWs;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import oscar.OscarProperties;
import oscar.oscarTickler.TicklerCreator;
import oscar.util.DateUtils;

public final class ConformanceTestHelper {
	private static Logger logger = MiscUtils.getLogger();
	public static boolean enableConformanceOnlyTestFeatures=Boolean.parseBoolean(OscarProperties.getInstance().getProperty("ENABLE_CONFORMANCE_ONLY_FEATURES"));
	
	public static void populateLocalTicklerWithRemoteProviderMessageFollowUps(LoggedInInfo loggedInInfo) {
		try {
			ProviderWs providerWs = CaisiIntegratorManager.getProviderWs(loggedInInfo, loggedInInfo.getCurrentFacility());
			List<ProviderCommunicationTransfer> followUps = providerWs.getProviderCommunications(loggedInInfo.getLoggedInProviderNo(), "FOLLOWUP", true);

			if (followUps == null) return;

			logger.debug("Folowups found : " + followUps.size());

			for (ProviderCommunicationTransfer providerCommunication : followUps) {
				Document doc = XmlUtils.toDocument(providerCommunication.getData());
				Node root = doc.getFirstChild();
				String demographicId = XmlUtils.getChildNodeTextContents(root, "destinationDemographicId");
				String note = XmlUtils.getChildNodeTextContents(root, "note");

				TicklerCreator t = new TicklerCreator();

				logger.debug("Create tickler : " + demographicId + ", " + providerCommunication.getDestinationProviderId() + ", " + note);
				
				FacilityIdStringCompositePk senderProviderId=new FacilityIdStringCompositePk();
				senderProviderId.setIntegratorFacilityId(providerCommunication.getSourceIntegratorFacilityId());
				senderProviderId.setCaisiItemId(providerCommunication.getSourceProviderId());
				CachedProvider senderProvider=CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(), senderProviderId);
				if (senderProvider!=null)
				{
					note="Sent by remote provider : "+senderProvider.getLastName()+", "+senderProvider.getFirstName()+"<br />--------------------<br />"+note;
				}
				
				t.createTickler(loggedInInfo,demographicId, providerCommunication.getDestinationProviderId(), note);

				providerWs.deactivateProviderCommunication(providerCommunication.getId());
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}

	public static void copyLinkedDemographicsPropertiesToLocal(LoggedInInfo loggedInInfo, Integer localDemographicId) {
		try {
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
			List<DemographicTransfer> directLinks=demographicWs.getDirectlyLinkedDemographicsByDemographicId(localDemographicId);
			
			logger.debug("found linked demographics size:"+directLinks.size());
			
			if (directLinks.size()>0)
			{
				DemographicTransfer demographicTransfer=directLinks.get(0);
				
				logger.debug("remoteDemographic:"+ReflectionToStringBuilder.toString(demographicTransfer));
				
				DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
				Demographic demographic=demographicDao.getDemographicById(localDemographicId);
				
				CaisiIntegratorManager.copyDemographicFieldsIfNotNull(demographicTransfer, demographic);
				
				demographic.setRosterDate(new Date());
				
				demographicDao.save(demographic);				
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}

	public static boolean hasDifferentRemoteDemographics(LoggedInInfo loggedInInfo, Integer localDemographicId) {
		boolean ret = false;
		try {
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
			List<DemographicTransfer> directLinks=demographicWs.getDirectlyLinkedDemographicsByDemographicId(localDemographicId);
			
			logger.debug("found linked demographics size:"+directLinks.size());
			
			if (directLinks.size()>0)
			{
				DemographicTransfer demographicTransfer=directLinks.get(0);
				
				logger.debug("remoteDemographic:"+ReflectionToStringBuilder.toString(demographicTransfer));
				
				DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
				Demographic demographic=demographicDao.getDemographicById(localDemographicId);
				
				if (demographicTransfer.getBirthDate()!=null &&  !(DateUtils.getNumberOfDaysBetweenTwoDates(demographicTransfer.getBirthDate(),demographic.getBirthDay()) == 0)) ret = true;
				if (demographicTransfer.getCity()!=null && !demographicTransfer.getCity().equalsIgnoreCase(demographic.getCity())) ret = true;
				if (demographicTransfer.getFirstName()!=null && !demographicTransfer.getFirstName().equals(demographic.getFirstName())) ret = true;
				if (demographicTransfer.getGender()!=null && !demographicTransfer.getGender().toString().equals(demographic.getSex())) ret = true;
				if (demographicTransfer.getHin()!=null && !demographicTransfer.getHin().equals(demographic.getHin())) ret = true;
				if (demographicTransfer.getHinType()!=null && !demographicTransfer.getHinType().equals(demographic.getHcType())) ret = true;
				if (demographicTransfer.getHinVersion()!=null && !demographicTransfer.getHinVersion().equals(demographic.getVer())) ret = true;
				if (isRemoteDateDifferent(DateUtils.toGregorianCalendar(demographic.getEffDate()),demographicTransfer.getHinValidStart())) ret = true;
				if (isRemoteDateDifferent(DateUtils.toGregorianCalendar(demographic.getHcRenewDate()),demographicTransfer.getHinValidEnd())) ret = true;
				if (demographicTransfer.getLastName()!=null && !demographicTransfer.getLastName().equals(demographic.getLastName())) ret = true;
				if (demographicTransfer.getProvince()!=null && !demographicTransfer.getProvince().equalsIgnoreCase(demographic.getProvince())) ret = true;
				if (demographicTransfer.getSin()!=null && !demographicTransfer.getSin().equals(demographic.getSin())) ret = true;
				if (demographicTransfer.getStreetAddress()!=null && !demographicTransfer.getStreetAddress().equals(demographic.getAddress())) ret = true;
				if (demographicTransfer.getPhone1()!=null && !demographicTransfer.getPhone1().equals(demographic.getPhone())) ret = true;
				if (demographicTransfer.getPhone2()!=null&& !demographicTransfer.getPhone2().equals(demographic.getPhone2())) ret = true;
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return ret;
	}


	/*
	local remote
	null  null   = false
	value null   = false
	null  value  = true
	value value  = compare
	*/
	public static boolean isRemoteDateDifferent(Calendar local,Calendar remote){
		boolean isRemoteDateDifferent = false;
		if(remote != null){
			if(local == null){
				isRemoteDateDifferent = true;
			}else if( !( DateUtils.getNumberOfDaysBetweenTwoDates(local, remote) == 0)){
				isRemoteDateDifferent = true;
			}
		}
		return isRemoteDateDifferent;
	}



}
