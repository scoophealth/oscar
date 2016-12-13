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


package org.oscarehr.integration.clinicalconnect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.oscarehr.util.MiscUtils;

import com.medseek.clinical.service.*;
import com.medseek.clinical.service.GetDefaultLaunchUrl.Props;

public class ClinicalConnectSSO {
	private static final String VMO_SERVICE = "/VmoService";
	private static final String PATIENT_SERVICE = "/PatientService";
	private static VmoService vmoService = null;
	private static PatientService patientService = null;
	
	private static String serviceUserName = null;
	private static String servicePassword = null;
	private static String serviceLocation = null;
	
	public static String getLaunchURL(String serviceUserName, String servicePassword, String serviceLocation, String username, String authType, String patientHCN){
		String url = null;
		try
		{
			if (isCredentialsChanged(serviceUserName, servicePassword, serviceLocation)) {
				
				// create the WS Client
				JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
				factory.setServiceClass(VmoService.class);
				factory.setAddress(getAddress(VMO_SERVICE));
				
				// WS-SECURITY stuff follows
				Map<String, Object> outProps = new HashMap<String, Object>();
				outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN+" "+WSHandlerConstants.TIMESTAMP);
				
				// Specify service username
				outProps.put(WSHandlerConstants.USER, serviceUserName);
				
				// Password type : plain text
				outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
				
				// set our password callback class this is used to lookup the password for a user.
				outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new PasswordCallbackHandler(servicePassword));
				WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
				factory.getOutInterceptors().add(wssOut);
				vmoService = (VmoService)factory.create();
				
				factory.setServiceClass(PatientService.class);
				factory.setAddress(getAddress(PATIENT_SERVICE));
				patientService = (PatientService)factory.create();
			}
			
			long id = vmoService.findUserMappingId(authType, username);
			url = vmoService.getDefaultLaunchUrl(id, new Props(), getPatientToken(patientHCN, id), false);
		}
		catch (Exception e)
		{
			MiscUtils.getLogger().error("getLaunchURL Error!", e);
		}
		return url;
	}
	
	private static boolean isCredentialsChanged(String srvUserName, String srvPassword, String srvLocation) {
		boolean changed = false;
		if (!srvUserName.equals(serviceUserName)) { serviceUserName = srvUserName; changed = true; }
		if (!srvPassword.equals(servicePassword)) { servicePassword = srvPassword; changed = true; }
		if (!srvLocation.equals(serviceLocation)) { serviceLocation = srvLocation; changed = true; }
		return changed;
	}
	
	private static String getAddress(String whichService) {
		String url = serviceLocation;
		while (url.endsWith("/")) {
			url = url.substring(0, url.length()-1);
		}
		if (url.endsWith(VMO_SERVICE)) url.substring(0, url.length()-VMO_SERVICE.length());
		return url+whichService;
	}
	
	private static String getPatientToken(String patientHCN, long id) {
		String patientToken = null;
		if (patientHCN!=null && !patientHCN.trim().isEmpty()) {
			
			// create a PatientSearchCriteria with HCN
			 PatientSearchCriteria patientSearchCriteria = new PatientSearchCriteria();
			 patientSearchCriteria.setHcn(patientHCN);
			 
			// create RequestData to hold user id
			 RequestData requestData = new RequestData();
			 SecurityEnvelope se = new SecurityEnvelope();
			 se.setUserMappingId(Long.valueOf(id));
			 requestData.setSecurityEnvelope(se);
			 
			 // loop thru all facilities until the patient is found or list exhausted
			 List<String> facilities = patientService.getSearchSources(requestData);
			 for (String facility : facilities) {
				 String facilityToken = patientService.getSourceToken(requestData, facility);
				 PatientList patientList = vmoService.searchPatients(requestData, facilityToken, patientSearchCriteria, false);
				 if (patientList==null || patientList.getList().isEmpty()) continue;
				 
				 List<PatientSummary> psList = patientList.getList();
				 if (psList!=null && !psList.isEmpty()) {
					 patientToken = psList.get(0).getToken();
					 break;
				 }
			 }
		}
		return patientToken;
	}
}
