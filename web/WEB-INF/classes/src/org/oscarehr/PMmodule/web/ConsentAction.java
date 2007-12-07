/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.web;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Consent;
import org.oscarehr.PMmodule.model.ConsentInterview;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.IntegratorManager;

public class ConsentAction extends BaseAction {
	private static Log log = LogFactory.getLog(ConsentAction.class);


	public ActionForward unspecified(ActionMapping mapping,	ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm consentForm = (DynaActionForm)form;
		
		String id = request.getParameter("id");
		String formName = request.getParameter("formName");
		String formMapping = getRandomForm();
		
		String gotoStr = request.getParameter("goto");
		
		if(id == null) {
			return mapping.findForward("error");
		}
				
		Demographic demographic = clientManager.getClientByDemographicNo(id);
		
		if(demographic == null) {
			return mapping.findForward("error");
		}
		
		Consent consent = consentManager.getMostRecentConsent(Long.valueOf(id));
		if(consent != null) {
			Consent newConsent = new Consent();
			try {
				BeanUtils.copyProperties(newConsent,consent);
				newConsent.setId(null);
			}catch(Exception e) {log.warn(e);}
			if(consent.getStatus().equals("consent not given")) {
				consent.setExclusionString("all");
			}
			consentForm.set("consent", consent);
			formMapping = consent.getFormName();
		}
		request.setAttribute("id",id);
		request.setAttribute("clientName", demographic.getFirstName() + " " + demographic.getLastName());
		
		if(gotoStr != null && !gotoStr.equals("")) {
			return new ActionForward(gotoStr);
		}
		
		if(formName != null) {
			return mapping.findForward("form" + formName.toUpperCase());
		}
		
		
		return mapping.findForward(formMapping);
	}
	
	
	public ActionForward saveConsent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		log.debug("Saving Consent");
		
		DynaActionForm consentForm = (DynaActionForm)form;
		
		Consent consent= (Consent)consentForm.get("consent");
		
		String id = (String)request.getParameter("id");
		consent.setDemographicNo(Long.valueOf(id));
		
		Provider p =  (Provider)request.getSession().getAttribute("provider");
		consent.setProviderNo(p.getProviderNo());
		consent.setProviderName(p.getFormattedName());		
		
		consent.setDateSigned(new Timestamp(System.currentTimeMillis()));
		consent.setHardcopy(true);
		
		if(consent.getOptout() != null && consent.getOptout().equalsIgnoreCase("yes")) {
			consent.setStatus("consent not given");
		}
		
		if(consent.getExclusionString().equals("all")) {
			consent.setStatus("consent not given");
			consent.setExclusionString("");
		}
		if(consent.getExclusionString().equals("non-hic")) {
			consent.setStatus("consent given");
		}
		
		consentManager.saveConsent(consent);	
		
		String gotoStr = request.getParameter("goto");
		if(gotoStr != null && !gotoStr.equals("")) {
			return form(mapping,form,request,response);
		}
		
		ConsentInterview interview = consentManager.getConsentInterviewByDemographicNo(String.valueOf(consent.getDemographicNo()));
		if(interview == null) {
			return mapping.findForward("interview");
		} else {
			return mapping.findForward("success");
		}
	}

	public ActionForward saveInterview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		log.debug("Saving Interview");
	
		DynaActionForm consentForm = (DynaActionForm)form;		
		Consent consent= (Consent)consentForm.get("consent");
		ConsentInterview interview = (ConsentInterview)consentForm.get("interview");
		
		interview.setConsentId(consent.getId().longValue());
		interview.setDemographicNo(consent.getDemographicNo());
		interview.setProviderNo(consent.getProviderNo());
		interview.setFormName(consent.getFormName());
		interview.setFormVersion(consent.getFormVersion());
		
		consentManager.saveConsentInterview(interview);
	
		return mapping.findForward("success");
	}
	
	
	public ActionForward agency_list(ActionMapping mapping,	ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		Map agencyMap = Agency.getAgencyMap();
		if(agencyMap == null) {
			
		}
		request.setAttribute("agencies", agencyMap);
		
		return mapping.findForward("agency_list");
	}
	
	private String getRandomForm() {
		int d = (int)(Math.random()*2);
		if(d == 0) {
			return "formA";
		}
		return "formB";
	}
}



