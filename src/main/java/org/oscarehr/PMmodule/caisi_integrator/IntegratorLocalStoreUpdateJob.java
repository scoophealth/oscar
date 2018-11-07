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

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.ws.WebServiceException;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.BenchmarkTimer;
import org.oscarehr.util.CxfClientUtilsOld;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class IntegratorLocalStoreUpdateJob implements OscarRunnable {

	private static final Logger logger = MiscUtils.getLogger();
	
	private FacilityDao facilityDao = SpringUtils.getBean(FacilityDao.class);
	private UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
	private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	
	
	private Provider provider;
	private Security security;

	public void setLoggedInProvider(Provider provider) {
		this.provider = provider;
	}

	public void setLoggedInSecurity(Security security) {
		this.security = security;
	}
	
	@Override
	public void run() {
		MiscUtils.getLogger().info("Running IntegratorFileLogUpdateJob");
		LoggedInInfo x = new LoggedInInfo();
		x.setLoggedInProvider(provider);
		x.setLoggedInSecurity(security);
	
		try {
			pullForAllFacilities(x);
		} catch (Exception e) {
			logger.error("unexpected error occurred", e);
		} 
		
	}
	
	public void pullForAllFacilities(LoggedInInfo loggedInInfo)  {
		List<Facility> facilities = facilityDao.findAll(true);

		for (Facility facility : facilities) {
			try {
				if (facility.isIntegratorEnabled()) {
					findChangedRecordsFromIntegrator(loggedInInfo, facility);
				}
			} catch (WebServiceException e) {
				if (CxfClientUtilsOld.isConnectionException(e)) {
					logger.warn("Error connecting to integrator. " + e.getMessage());
					logger.debug("Error connecting to integrator.", e);
				} else {
					logger.error("Unexpected error.", e);
				}
			} catch (Exception e) {
				logger.error("Unexpected error.", e);
			}
		}
	}
	
	/*
	1) demographicWs.getDemographicIdPushedAfterDateByRequestingFacility : 
		which gets the local demographicId's which have changed, it will traverse linked records so if a linked record changes, your local id is reported as changed.

	2) demographicWs.getDemographicsPushedAfterDate : 
		which is a raw listing of the direct records which have changed, i.e. (facilityId, oscarDemographicId).
	*/	
	protected void findChangedRecordsFromIntegrator(LoggedInInfo loggedInInfo, Facility facility) throws MalformedURLException {//throws IOException, ShutdownException {
		logger.info("Start fetch data for facility : " + facility.getId() + " : " + facility.getName());
		boolean integratorLocalStore = OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE","yes");
		if(!integratorLocalStore){
			logger.info("Integrator Local Store (INTEGRATOR_LOCAL_STORE) is not enabled. Aborting. You can disable this job from the admin menu");
			return;
		}
		DemographicWs demographicService = CaisiIntegratorManager.getDemographicWs(loggedInInfo, facility);
			
		Calendar nextTime = Calendar.getInstance();
		
		Date lastPushDate = new Date(0);
		try{
			UserProperty lastPull = userPropertyDao.getProp(UserProperty.INTEGRATOR_LAST_PULL_PRIMARY_EMR+"+"+facility.getId());
			lastPushDate.setTime(Long.parseLong(lastPull.getValue()));
		}catch(Exception epull){
			MiscUtils.getLogger().error("lastPull Error:",epull);
			lastPushDate = new Date(0);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastPushDate);
		
		List<Integer> demographicNos = demographicService.getDemographicIdPushedAfterDateByRequestingFacility(cal);
		
		if(demographicNos.isEmpty()){
			logger.debug("No demographics updated on the integrator");
		}else{
			logger.debug("demos changed "+demographicNos.size());
		}
		int demographicFetchCount = 0;
		for(Integer demographicNo:demographicNos){
			logger.debug("Demographic "+demographicNo+" updated on the integrator, primary emr ? ");
			DemographicExt demographicExt = demographicExtDao.getLatestDemographicExt(demographicNo, "primaryEMR");
			if (demographicExt != null && demographicExt.getValue().equals("1")){
				demographicFetchCount++;
				BenchmarkTimer benchTimer = new BenchmarkTimer("fetch and save for facilityId:" + facility.getId() + ", demographicId:" + demographicNo + "  " + demographicFetchCount + " of " + demographicNos.size());
				IntegratorFallBackManager.saveLinkNotes(loggedInInfo,demographicNo);
				benchTimer.tag("saveLinkedNotes");
				IntegratorFallBackManager.saveRemoteForms(loggedInInfo,demographicNo);
				benchTimer.tag("saveRemoteForms");
				
				
				
				IntegratorFallBackManager.saveDemographicIssues(loggedInInfo,demographicNo);
				benchTimer.tag("saveDemographicIssues");
				IntegratorFallBackManager.saveDemographicPreventions(loggedInInfo,demographicNo);
				benchTimer.tag("saveDemographicPreventions");
				IntegratorFallBackManager.saveDemographicDrugs(loggedInInfo,demographicNo);
				benchTimer.tag("saveDemographicDrugs");
				IntegratorFallBackManager.saveAdmissions(loggedInInfo,demographicNo);
				benchTimer.tag("saveAdmissions");
				IntegratorFallBackManager.saveAppointments(loggedInInfo,demographicNo);
				benchTimer.tag("saveAppointments");
				IntegratorFallBackManager.saveAllergies(loggedInInfo,demographicNo);
				benchTimer.tag("saveAllergies");
				IntegratorFallBackManager.saveDocuments(loggedInInfo,demographicNo);
				benchTimer.tag("saveDocuments");
				IntegratorFallBackManager.saveLabResults(loggedInInfo,demographicNo);
				benchTimer.tag("saveLabResults");
 
				//These don't exist
				//IntegratorFallBackManager.saveMeasurements(demographicNo); // Not being displayed yet
				//IntegratorFallBackManager.saveDxresearchs(demographicNo);  //Not being displayed yet
				//IntegratorFallBackManager.saveBillingItems(demographicNo);//Not being displayed yet
				//IntegratorFallBackManager.saveEforms(demographicNo);//Not being displayed yet

				logger.debug(benchTimer.report());
			}
			userPropertyDao.saveProp(UserProperty.INTEGRATOR_LAST_PULL_PRIMARY_EMR+"+"+facility.getId(), "" + nextTime.getTime().getTime());
		}
		logger.info("End fetch data for facility : " + facility.getId() + " : " + facility.getName());
	}
	
	@Override
	public void setConfig(String string) {
	}
	
}
