/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web.forms;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.FormFollowUp;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.FormsManager;
import org.oscarehr.PMmodule.service.LogManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;

public class FollowUpAction extends DispatchAction {
	private static Logger log = MiscUtils.getLogger();
	private  final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	private  final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
	
	private ProgramManager programManager;
	private ClientManager clientManager;
	private ProviderManager providerManager;
	private LogManager logManager;
	private FormsManager formsManager;
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}
	
	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}
	
	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}
	
	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}
	
	public void setFormsManager(FormsManager mgr) {
		this.formsManager = mgr;
	}
	
	public String getProviderNo(HttpServletRequest request) {
		Provider p =  (Provider)request.getSession().getAttribute("provider");
		return p.getProviderNo();
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm inputForm = (DynaActionForm)form;
		inputForm.set("intake", new FormFollowUp());

		String demographicNo = request.getParameter("demographicNo");
		log.debug("form demographicNo=" + demographicNo);
		if(demographicNo==null) {
			demographicNo=(String)request.getAttribute("demographicNo");
		}
		log.debug("(try2) form demographicNo=" + demographicNo);

		FormFollowUp theForm = (FormFollowUp)formsManager.getCurrentForm(demographicNo,FormFollowUp.class);
		boolean update=(theForm != null);
		
		//view form, provider
		Demographic client  = clientManager.getClientByDemographicNo(demographicNo);
		
		if(update) {
			if(client == null) {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("intake.invalid_client"));
				saveMessages(request,messages);			
				return mapping.findForward("error");
			}  
			theForm.setDemographicNo(Long.valueOf(demographicNo));
		} else {
			theForm = new FormFollowUp();
			//set basic client info
			if(client != null) {
				theForm.setDemographicNo(Long.valueOf(demographicNo));
				theForm.setClientFirstName(client.getFirstName());
				theForm.setClientSurname(client.getLastName());
				theForm.setYear(client.getYearOfBirth());
				theForm.setMonth(client.getMonthOfBirth());
				theForm.setDay(client.getDateOfBirth());
			}
		}
		theForm.setDateAssessment(formatter.format(new Date()));
		theForm.setAssessStartTime(timeFormatter.format(new Date()));
		theForm.setProviderNo(Long.valueOf(getProviderNo(request)));		
		inputForm.set("intake",theForm);

		logManager.log("read","followupform",demographicNo,request);
	   	request.setAttribute("demographicNo",demographicNo);
		return mapping.findForward("form");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm inputForm = (DynaActionForm)form;
		FormFollowUp theForm = (FormFollowUp)inputForm.get("intake");
		
		
		if(theForm.getId() != null && theForm.getId().longValue()>0) {
			
		}
		
		theForm.setProviderNo(Long.valueOf(this.getProviderNo(request)));
		theForm.setFormEdited(new Date());
		formsManager.saveForm(theForm);
		
		request.setAttribute("demographicNo",String.valueOf(theForm.getDemographicNo()));		
		inputForm.reset(mapping,request);
		
		return mapping.findForward("success");
	}
	
	public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm inputForm = (DynaActionForm)form;
		FormFollowUp theForm = (FormFollowUp)inputForm.get("intake");
		
		Long demographicNo = theForm.getDemographicNo();
		request.setAttribute("demographicNo",demographicNo);
		inputForm.reset(mapping,request);
		
		return mapping.findForward("error");
	}
}
