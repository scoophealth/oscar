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

package org.oscarehr.er;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.casemgmt.service.CaseManagementManager;

public class ReceptionistReportAction extends DispatchAction {
	private static Log log = LogFactory.getLog(ReceptionistReportAction.class);

	private ProgramManager programManager;
	private ClientManager clientManager;
	private AdmissionManager admissionManager;
	private CaseManagementManager caseManagementManager;
	private ProviderManager providerManager;
	
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}
	
	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}
	
	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}
	
	public void setCaseManagementManager(CaseManagementManager mgr) {
		this.caseManagementManager = mgr;
	}
	
	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}
	
	protected void postMessage(HttpServletRequest request, String key, String val) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(key,val));
		saveMessages(request,messages);
	}
	
	protected void postMessage(HttpServletRequest request, String key) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(key));
		saveMessages(request,messages);
	}

	protected String getProviderNo(HttpServletRequest request) {
		return (String)request.getSession().getAttribute("user");
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return show_report(mapping,form,request,response);
	}
	
	/*
	 * Client Name
	 * DOB
	 * Health Card
	 * List of current Bed and Service programs (+contact info)
	 * list of current issues
	 * list of medications
	 * 
	 */
	public ActionForward show_report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String clientId = request.getParameter("id");
		
		Demographic client = clientManager.getClientByDemographicNo(clientId);
		
		if(client == null) {
			postMessage(request,"client.missing");
			log.warn("client not found");
			return mapping.findForward("error");
		}
		
		String name = client.getFormattedName();
		String dob = client.getFormattedDob();
		String healthCard = client.getHin() + " " + client.getVer();
		
		List admissions = admissionManager.getCurrentAdmissions(Integer.valueOf(clientId));
		for(Iterator iter = admissions.iterator();iter.hasNext();) {
			Admission admission = (Admission)iter.next();
			admission.setProgram(programManager.getProgram(String.valueOf(admission.getProgramId())));
		}
		
		request.setAttribute("demographicNo",clientId);
		request.setAttribute("client_name",name);
		request.setAttribute("client_dob",dob);
		request.setAttribute("client_healthCard",healthCard);
		request.setAttribute("admissions",admissions);
		
		//List programDomain = providerManager.getProgramDomain(getProviderNo(request));
		//String programId = String.valueOf(((ProgramProvider)programDomain.get(0)).getProgramId());
		
		//List accessRights = this.caseManagementManager.getAccessRight(getProviderNo(request),clientId,programId);
		//List issues = this.caseManagementManager.getActiveIssues(getProviderNo(request),clientId,accessRights);
		List issues = this.caseManagementManager.getActiveIssues(getProviderNo(request),clientId);
		request.setAttribute("issues",issues);
		
		List prescriptions = this.caseManagementManager.getPrescriptions(clientId,false);
		request.setAttribute("prescriptions",prescriptions);
		
		List allergies = this.caseManagementManager.getAllergies(clientId);
		request.setAttribute("allergies",allergies);
		
		
		return mapping.findForward("report");
	}
}
