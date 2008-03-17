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
import java.util.Collection;
import java.util.Iterator;
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
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.casemgmt.service.CaseManagementManager;

import com.quatro.service.LookupManager;

public class ClientSearchAction2 extends BaseAction {
	
	protected LookupManager lookupManager;
    protected CaseManagementManager caseManagementManager;
    protected AdmissionManager admissionManager;

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
	
}
