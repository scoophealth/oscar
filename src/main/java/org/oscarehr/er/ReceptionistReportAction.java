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

package org.oscarehr.er;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.web.CaseManagementViewAction.IssueDisplay;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarRx.pageUtil.RxSessionBean;

public class ReceptionistReportAction extends DispatchAction {
	private static Logger log = MiscUtils.getLogger();

	private static IssueDAO issueDao = (IssueDAO)SpringUtils.getBean("IssueDAO");
	
	private ProgramManager programManager;
	private ClientManager clientManager;
	private AdmissionManager admissionManager;
	private CaseManagementManager caseManagementManager;
	
	
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
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
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
		List issues = this.caseManagementManager.getIssues(Integer.parseInt(clientId),false);
		request.setAttribute("issues",issues);
		
		ArrayList<IssueDisplay> issuesToDisplay = new ArrayList<IssueDisplay>();
		this.addRemoteIssues(loggedInInfo, issuesToDisplay, Integer.parseInt(clientId), false);
		request.setAttribute("remote_issues",issuesToDisplay);
		
				
		List<Drug> prescriptions = null;
		boolean viewAll=true;

		request.setAttribute("isIntegratorEnabled", loggedInInfo.getCurrentFacility().isIntegratorEnabled());			
		prescriptions = caseManagementManager.getPrescriptions(loggedInInfo, Integer.valueOf(clientId), viewAll);
		
		request.setAttribute("Prescriptions", prescriptions);		    	
    	
		// Setup RX bean start
		RxSessionBean bean = new RxSessionBean();
		bean.setProviderNo(providerNo);
		bean.setDemographicNo(Integer.valueOf(clientId));
		request.getSession().setAttribute("RxSessionBean", bean);
		// set up RX end

		
		
		
		
		List allergies = this.caseManagementManager.getAllergies(clientId);
		request.setAttribute("allergies",allergies);
		
		
		return mapping.findForward("report");
	}

	private void addRemoteIssues(LoggedInInfo loggedInInfo, ArrayList<IssueDisplay> issuesToDisplay, int demographicNo, boolean resolved) {

		if (!loggedInInfo.getCurrentFacility().isIntegratorEnabled()) return;

		try {
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
			List<CachedDemographicIssue> remoteIssues = demographicWs.getLinkedCachedDemographicIssuesByDemographicId(demographicNo);

			for (CachedDemographicIssue cachedDemographicIssue : remoteIssues) {
				try {
					if (resolved == cachedDemographicIssue.isResolved()) {
						issuesToDisplay.add(getIssueToDisplay(loggedInInfo, cachedDemographicIssue));
					}
				} catch (Exception e) {
					log.error("Unexpected error.", e);
				}
			}
		} catch (Exception e) {
			log.error("Unexpected error.", e);
		}
	}

	private IssueDisplay getIssueToDisplay(LoggedInInfo loggedInInfo, CachedDemographicIssue cachedDemographicIssue) throws MalformedURLException {
		IssueDisplay issueDisplay = new IssueDisplay();

		issueDisplay.acute = cachedDemographicIssue.isAcute() ? "acute" : "chronic";
		issueDisplay.certain = cachedDemographicIssue.isCertain() ? "certain" : "uncertain";
		issueDisplay.code = cachedDemographicIssue.getFacilityDemographicIssuePk().getIssueCode();
		issueDisplay.codeType = "ICD10"; // temp hard coded hack till issue is resolved

		Issue issue = null;
		// temp hard coded icd hack till issue is resolved
		if ("ICD10".equalsIgnoreCase(OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE").toUpperCase())) {
			issue = issueDao.findIssueByCode(cachedDemographicIssue.getFacilityDemographicIssuePk().getIssueCode());
		}

		if (issue != null) {
			issueDisplay.description = issue.getDescription();
			issueDisplay.priority = issue.getPriority();
			issueDisplay.role = issue.getRole();
		} else {
			issueDisplay.description = "Not Available";
			issueDisplay.priority = "Not Available";
			issueDisplay.role = "Not Available";
		}

		Integer remoteFacilityId = cachedDemographicIssue.getFacilityDemographicIssuePk().getIntegratorFacilityId();
		CachedFacility remoteFacility = CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(),remoteFacilityId);
		if (remoteFacility != null) issueDisplay.location = "remote: " + remoteFacility.getName();
		else issueDisplay.location = "remote, name unavailable";

		issueDisplay.major = cachedDemographicIssue.isMajor() ? "major" : "not major";
		issueDisplay.resolved = cachedDemographicIssue.isResolved() ? "resolved" : "unresolved";

		return (issueDisplay);
	}
}
