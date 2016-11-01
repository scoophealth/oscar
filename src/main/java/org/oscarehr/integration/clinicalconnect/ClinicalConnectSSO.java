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
	
	public static String getLaunchURL(String serviceUserName, String servicePassword, String serviceLocation, String username, String authType, String patientHCN){
		String url = null;
		try
		{
			// create the WS Client
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.setServiceClass(VmoService.class);
			factory.setAddress(serviceLocation);
			
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
			VmoService vmoService = (VmoService) factory.create();
			
			// Ad specific parameter: sAMAccountName
			long id = vmoService.findUserMappingId(authType, username);
			
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
				 PatientList patientList = vmoService.searchPatients(requestData, "", patientSearchCriteria, false);
				 
				 List<PatientSummary> psList = patientList.getList();
				 if (psList!=null && !psList.isEmpty()) {
					 patientToken = psList.get(0).getToken();
				 }
			}
			 
			// launching to a particular landing page in the Portal
			url = vmoService.getDefaultLaunchUrl(id, new Props(), patientToken, false);
		}
		catch (Exception e)
		{
			MiscUtils.getLogger().error("getLaunchURL Error!", e);
		}
		return url;
	}
}
