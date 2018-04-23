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
package org.oscarehr.integration.dhir;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.integration.fhir.api.DHIR;
import org.oscarehr.integration.fhir.builder.FhirBundleBuilder;
import org.oscarehr.util.LoggedInInfo;

public class SubmitImmunizationAction extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	/**
	 * /dhir/submit.do?method=test&demographicNo=??&preventionId=??
	 * 
	 * This method can be deleted.
	 */
	@SuppressWarnings("unused")
	public ActionForward test(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String demographicNo = request.getParameter("demographicNo");
		String preventionId = request.getParameter("preventionId");
		int demographicInt = Integer.parseInt( demographicNo );
		int preventionInt = Integer.parseInt(preventionId);
		
		MiscUtils.getLogger().info("Running DHIR FHIR Test Case");
		
		FhirBundleBuilder fhirBundleBuilder = DHIR.getFhirBundleBuilder( loggedInInfo, demographicInt, preventionInt );
		
		MiscUtils.getLogger().info("FHIR Message Bundle Builder " +  fhirBundleBuilder);

		MiscUtils.getLogger().info("FHIR Bundle " +  fhirBundleBuilder.getBundle() );	
		MiscUtils.getLogger().info("Test JSON FHIR Shortcut Method " + DHIR.getMessageJSON( loggedInInfo, demographicInt, preventionInt ) ); 

		request.setAttribute("fhirBundleBuilder", fhirBundleBuilder);
		
		return mapping.findForward("testresult");
	}
	
//	@Override
//	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		
//		//need prevention, patient, and loggedInProvider
//		
//		//create some kind of log entry
//		
//		//generate the message - update log
//		
//		//submit through hial - update log
//		
//		MiscUtils.getLogger().info("submitting to DHIR");
//		
//		
//		return mapping.findForward("success");
//	}

	
}
