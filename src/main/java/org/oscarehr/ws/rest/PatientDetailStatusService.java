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
package org.oscarehr.ws.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.integration.mchcv.HCValidationFactory;
import org.oscarehr.integration.mchcv.HCValidationResult;
import org.oscarehr.integration.mchcv.HCValidator;
import org.oscarehr.integration.mchcv.OnlineHCValidator;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.model.PatientDetailStatusTo1;
import org.springframework.beans.factory.annotation.Autowired;

import oscar.OscarProperties;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarProvider.data.ProviderMyOscarIdData;


@Path("/patientDetailStatusService")
@Produces({MediaType.APPLICATION_JSON})
public class PatientDetailStatusService extends AbstractServiceImpl {
	@Autowired
	private DemographicManager demographicManager;
	
	private OscarProperties oscarProperties = OscarProperties.getInstance();
	private Logger logger = MiscUtils.getLogger();
	
	
	@GET
	@Path("/getStatus")
	public PatientDetailStatusTo1 getStatus(@QueryParam("demographicNo") Integer demographicNo) {
		PatientDetailStatusTo1 status = new PatientDetailStatusTo1();
		
		//Integrator status
		status.setIntegratorEnabled(getLoggedInInfo().getCurrentFacility().isIntegratorEnabled());
		if (status.isIntegratorEnabled()) {
			status.setIntegratorOffline(CaisiIntegratorManager.isIntegratorOffline(getLoggedInInfo().getSession()));
			
			int secondsTillConsideredStale = -1;
			try{
				secondsTillConsideredStale = Integer.parseInt(oscarProperties.getProperty("seconds_till_considered_stale"));
			}catch(Exception e){
				logger.error("OSCAR Property: seconds_till_considered_stale did not parse to an int",e);
				secondsTillConsideredStale = -1;
			}
			
			boolean allSynced = true;
			try{
				allSynced  = CaisiIntegratorManager.haveAllRemoteFacilitiesSyncedIn(getLoggedInInfo(), getLoggedInInfo().getCurrentFacility(), secondsTillConsideredStale); 
			}catch(Exception remoteFacilityException){
				logger.error("Error checking Remote Facilities Sync status",remoteFacilityException);
				CaisiIntegratorManager.checkForConnectionError(getLoggedInInfo().getSession(),remoteFacilityException);
			}
			
			if(secondsTillConsideredStale == -1){  
				allSynced = true; 
			}
			status.setIntegratorAllSynced(allSynced);
		}
		
		//McMaster PHR status
		MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(getLoggedInInfo().getSession());
		if (myOscarLoggedInInfo!=null) {
			status.setMacPHRLoggedIn(myOscarLoggedInInfo.isLoggedIn());
		}
		
		if (ProviderMyOscarIdData.idIsSet(getLoggedInInfo().getLoggedInProviderNo())) {
		    if( demographicNo>0 ) {
		    	Demographic demo = new DemographicData().getDemographic(getLoggedInInfo(), demographicNo.toString()); 
		    	String myOscarUserName = demo.getMyOscarUserName();
		    	if(myOscarUserName!=null && !myOscarUserName.equals("")) {
		    		status.setMacPHRIdsSet(true);
		    		
		    		String verificationLevel = demographicManager.getPhrVerificationLevelByDemographicId(getLoggedInInfo(),demographicNo);
		    		status.setMacPHRVerificationLevel(verificationLevel);
		    	}
		    }
		}
		
		//from oscar.properties
		status.setConformanceFeaturesEnabled(oscarProperties.isPropertyActive("ENABLE_CONFORMANCE_ONLY_FEATURES"));
		status.setWorkflowEnhance(oscarProperties.isPropertyActive("workflow_enhance"));
		status.setBillregion(oscarProperties.getProperty("billregion", ""));
		status.setDefaultView(oscarProperties.getProperty("default_view", ""));
		status.setHospitalView(oscarProperties.getProperty("hospital_view", status.getDefaultView()));
		status.setShowPrimaryCarePhysicianCheck(oscarProperties.isPropertyActive("showPrimaryCarePhysicianCheck"));
		status.setShowEmploymentStatus(oscarProperties.isPropertyActive("showEmploymentStatus"));
		
		return status;
	}
	
	@GET
	@Path("/validateHC")
	public HCValidationResult validateHC(@QueryParam("hin") String healthCardNo, @QueryParam("ver") String versionCode) {
		HCValidator validator = HCValidationFactory.getHCValidator();
		HCValidationResult result = null;
		
		if (validator.getClass().equals(OnlineHCValidator.class)) {
			HCValidator simpleValidator = HCValidationFactory.getSimpleValidator();
			result = simpleValidator.validate(healthCardNo,versionCode);
			
			if (result.isValid()) result = null;
		}
		
		if (result==null) {
			try {
				result = validator.validate(healthCardNo,versionCode);
			}
			catch (Exception ex) {
				logger.error("Error doing HCValidation", ex);
			}
		}
		
		if (result!=null && result.getResponseDescription()==null) {
			if (result.isValid()) result.setResponseDescription("Valid Health Card Number");
			else result.setResponseDescription("Invalid Health Card Number");
		}
		return result;
	}
	
	@GET
	@Path("/isUniqueHC")
	public GenericRESTResponse isUniqueHC(@QueryParam("hin") String healthCardNo, @QueryParam("demographicNo") Integer demographicNo) {
		GenericRESTResponse response = new GenericRESTResponse();
		if (healthCardNo!=null && !healthCardNo.trim().isEmpty() && demographicNo!=null) {
			List<Demographic> demos = demographicManager.searchByHealthCard(getLoggedInInfo(), healthCardNo);
			if (demos!=null) {
				if (demos.size()>1 || (demos.size()==1 && !demos.get(0).getDemographicNo().equals(demographicNo))) {
					response.setSuccess(false);
				}
			}
		}
		return response;
	}
}
