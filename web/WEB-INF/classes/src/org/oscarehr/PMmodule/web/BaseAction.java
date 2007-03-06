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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.RedirectingActionForward;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.AgencyManager;
import org.oscarehr.PMmodule.service.BedCheckTimeManager;
import org.oscarehr.PMmodule.service.BedDemographicManager;
import org.oscarehr.PMmodule.service.BedManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ConsentManager;
import org.oscarehr.PMmodule.service.FormsManager;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.service.IntakeAManager;
import org.oscarehr.PMmodule.service.IntakeCManager;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.PMmodule.service.LogManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProgramQueueManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.service.RatePageManager;
import org.oscarehr.PMmodule.service.RoleManager;
import org.oscarehr.PMmodule.service.RoomManager;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BaseAction extends DispatchAction {

	protected static final String PARAM_START = "?";
	protected static final String PARAM_EQUALS = "=";
	protected static final String PARAM_AND = "&";
	
	protected AdmissionManager admissionManager;
	protected AgencyManager agencyManager;
	protected BedCheckTimeManager bedCheckTimeManager;
	protected BedDemographicManager bedDemographicManager;
	protected BedManager bedManager;
	protected ClientManager clientManager;
	protected ConsentManager consentManager;
	protected FormsManager formsManager;
	protected GenericIntakeManager genericIntakeManager;
	protected IntakeAManager intakeAManager;
	protected IntakeCManager intakeCManager;
	protected IntegratorManager integratorManager;
	protected LogManager logManager;
	protected ProgramManager programManager;
	protected ProviderManager providerManager;
	protected ProgramQueueManager programQueueManager;
	protected RoleManager roleManager;
	protected RoomManager roomManager;
	protected SurveyManager surveyManager;

	public void addError(HttpServletRequest req, String message) {
		ActionMessages msgs = getErrors(req);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", message));
		addErrors(req, msgs);
	}

	public void addMessage(HttpServletRequest req, String message) {
		ActionMessages msgs = getMessages(req);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", message));
		addMessages(req, msgs);
	}

	public void addMessage(HttpServletRequest req, String key, String val) {
		ActionMessages msgs = getMessages(req);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, val));
		addMessages(req, msgs);
	}

	public ApplicationContext getAppContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext());
	}

	public AdmissionManager getAdmissionManager() {
		return (AdmissionManager) getAppContext().getBean("admissionManager");
	}

	public AgencyManager getAgencyManager() {
		return (AgencyManager) getAppContext().getBean("agencyManager");
	}

	public ClientManager getClientManager() {
		return (ClientManager) getAppContext().getBean("clientManager");
	}

	public GenericIntakeManager getGenericIntakeManager() {
	    return genericIntakeManager;
    }
	
	public IntakeAManager getIntakeAManager() {
		return (IntakeAManager) getAppContext().getBean("intakeAManager");
	}

	public IntakeCManager getIntakeCManager() {
		return (IntakeCManager) getAppContext().getBean("intakeCManager");
	}

	public ProgramManager getProgramManager() {
		return (ProgramManager) getAppContext().getBean("programManager");
	}

	public ProgramQueueManager getProgramQueueManager() {
		return (ProgramQueueManager) getAppContext().getBean("programQueueManager");
	}

	public ProviderManager getProviderManager() {
		return (ProviderManager) getAppContext().getBean("providerManager");
	}

	public RatePageManager getRateManager() {
		return (RatePageManager) getAppContext().getBean("ratePageManager");
	}

	public void notifyIntegrator(short dataType, String id) {
		IntegratorManager mgr = (IntegratorManager) getAppContext().getBean("integratorManager");
		mgr.notifyUpdate(dataType, id);
	}

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}

	public void setAgencyManager(AgencyManager mgr) {
		this.agencyManager = mgr;
	}
	
	public void setBedCheckTimeManager(BedCheckTimeManager bedCheckTimeManager) {
	    this.bedCheckTimeManager = bedCheckTimeManager;
    }
	
	public void setBedDemographicManager(BedDemographicManager demographicBedManager) {
		this.bedDemographicManager = demographicBedManager;
	}

	public void setBedManager(BedManager bedManager) {
		this.bedManager = bedManager;
	}

	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}

	public void setConsentManager(ConsentManager mgr) {
		this.consentManager = mgr;
	}

	public void setFormsManager(FormsManager mgr) {
		this.formsManager = mgr;
	}
	
	public void setGenericIntakeManager(GenericIntakeManager genericIntakeManager) {
	    this.genericIntakeManager = genericIntakeManager;
    }
	
	public void setIntakeAManager(IntakeAManager mgr) {
		this.intakeAManager = mgr;
	}

	public void setIntakeCManager(IntakeCManager mgr) {
		this.intakeCManager = mgr;
	}

	public void setIntegratorManager(IntegratorManager mgr) {
		this.integratorManager = mgr;
	}

	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}

	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}

	public void setProgramQueueManager(ProgramQueueManager mgr) {
		this.programQueueManager = mgr;
	}

	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}

	public void setRoleManager(RoleManager mgr) {
		this.roleManager = mgr;
	}

	public void setRoomManager(RoomManager roomManager) {
		this.roomManager = roomManager;
	}

	public void setSurveyManager(SurveyManager mgr) {
		this.surveyManager = mgr;
	}

	protected String getIP(HttpServletRequest request) {
		return request.getRemoteAddr();
	}
	
	protected String getProviderNo(HttpServletRequest request) {
		return getProvider(request).getProviderNo();
	}

	protected Provider getProvider(HttpServletRequest request) {
		return (Provider) request.getSession().getAttribute("provider");
	}
	
	protected String getParameter(HttpServletRequest request, String parameterName) {
		return request.getParameter(parameterName);
	}
	
	protected ActionForward createRedirectForward(ActionMapping mapping, String forwardName, StringBuilder parameters) {
		ActionForward forward = mapping.findForward(forwardName);
		StringBuilder path = new StringBuilder(forward.getPath());
		path.append(parameters);
		
		return new RedirectingActionForward(path.toString());
	}

}