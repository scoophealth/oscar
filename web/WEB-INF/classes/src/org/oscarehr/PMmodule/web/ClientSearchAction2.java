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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Program;
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
import org.oscarehr.PMmodule.service.RoleManager;
import org.oscarehr.PMmodule.service.RoomDemographicManager;
import org.oscarehr.PMmodule.service.RoomManager;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.casemgmt.service.CaseManagementManager;

import com.quatro.service.LookupManager;

public class ClientSearchAction2 extends BaseAction {
	
	protected LookupManager lookupManager;
    protected CaseManagementManager caseManagementManager;
    protected AdmissionManager admissionManager;
    protected GenericIntakeManager genericIntakeManager;
    protected AgencyManager agencyManager;
    protected BedCheckTimeManager bedCheckTimeManager;
    protected RoomDemographicManager roomDemographicManager;
    protected BedDemographicManager bedDemographicManager;
    protected BedManager bedManager;
    protected ClientManager clientManager;
    protected ConsentManager consentManager;
    protected FormsManager formsManager;
    protected IntakeAManager intakeAManager;
    protected IntakeCManager intakeCManager;
    protected IntegratorManager integratorManager;
    protected LogManager logManager;
    protected ProgramManager programManager;
    protected ProviderManager providerManager;
    protected ProgramQueueManager programQueueManager;
    protected RoleManager roleManager;
    protected RoomManager roomManager;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(clientManager.isOutsideOfDomainEnabled()){ 
			request.getSession().setAttribute("outsideOfDomainEnabled","true");
		}else{
			request.getSession().setAttribute("outsideOfDomainEnabled","false");
		}
		
		List<Program> allBedPrograms = new ArrayList<Program>();
		Program[] allBedProgramsInArr = programManager.getBedPrograms();

		for(int i=0; i < allBedProgramsInArr.length; i++){
			allBedPrograms.add((Program)allBedProgramsInArr[i]);
		}
		request.setAttribute("allBedPrograms", allBedPrograms);
		
		request.setAttribute("genders",lookupManager.LoadCodeList("GEN", true, null, null));
		
		return mapping.findForward("form");
	}
	
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		DynaActionForm searchForm = (DynaActionForm)form;
		ClientSearchFormBean formBean = (ClientSearchFormBean)searchForm.get("criteria");
		
		List<Program> allBedPrograms = new ArrayList<Program>();
		Program[] allBedProgramsInArr = programManager.getBedPrograms();

		for(int i=0; i < allBedProgramsInArr.length; i++){
			allBedPrograms.add((Program)allBedProgramsInArr[i]);
		}
		request.setAttribute("allBedPrograms", allBedPrograms);
		
		formBean.setProgramDomain((List)request.getSession().getAttribute("program_domain"));
		boolean allowOnlyOptins=UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external);
		
		/* do the search */
		request.setAttribute("clients",clientManager.search(formBean, allowOnlyOptins));
		
		// sort out the consent type used to search
		String consentSearch=StringUtils.trimToNull(request.getParameter("search_with_consent"));
		String emergencySearch=StringUtils.trimToNull(request.getParameter("emergency_search"));
		String consent=null;
		
		if (consentSearch!=null && emergencySearch!=null) throw(new IllegalStateException("This is an unexpected state, both search_with_consent and emergency_search are not null."));
		else if (consentSearch!=null) consent=Demographic.ConsentGiven.ALL.name();
		else if (emergencySearch!=null) consent=Demographic.ConsentGiven.ALL.name();
		request.setAttribute("consent", consent);

		if(formBean.isSearchOutsideDomain()) {
			logManager.log("read","out of domain client search","",request);
		}
		request.setAttribute("genders",lookupManager.LoadCodeList("GEN", true, null, null));
				
		return mapping.findForward("form");
	}

    public void setLookupManager(LookupManager lookupManager) {
    	this.lookupManager = lookupManager;
    }

    public void setCaseManagementManager(CaseManagementManager caseManagementManager) {
    	this.caseManagementManager = caseManagementManager;
    }

    public void setAdmissionManager(AdmissionManager mgr) {
    	this.admissionManager = mgr;
    }

    public void setGenericIntakeManager(GenericIntakeManager genericIntakeManager) {
        this.genericIntakeManager = genericIntakeManager;
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

    public void setRoomDemographicManager(RoomDemographicManager roomDemographicManager) {
    	this.roomDemographicManager = roomDemographicManager;
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
	
}
