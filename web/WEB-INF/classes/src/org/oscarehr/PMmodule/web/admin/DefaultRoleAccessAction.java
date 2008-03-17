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

package org.oscarehr.PMmodule.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.casemgmt.service.CaseManagementManager;

import com.quatro.service.LookupManager;

public class DefaultRoleAccessAction extends  BaseAction {
	private static Log log = LogFactory.getLog(DefaultRoleAccessAction.class);
    protected LookupManager lookupManager;
    protected CaseManagementManager caseManagementManager;
    protected AdmissionManager admissionManager;
    protected GenericIntakeManager genericIntakeManager;
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping,form,request,response);
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//DynaActionForm accessForm = (DynaActionForm)form;
		request.setAttribute("default_roles", programManager.getDefaultRoleAccesses());
		return mapping.findForward("list");
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm accessForm = (DynaActionForm)form;
		DefaultRoleAccess dra = null;
		
		String id = request.getParameter("id");
		
		if(id != null) {
			dra = programManager.getDefaultRoleAccess(id);
			if(dra != null) {
				accessForm.set("form", dra);
			}
		} 
		if(dra == null) {
			dra = new DefaultRoleAccess();
		}
		
		accessForm.set("form", dra);
		request.setAttribute("roles",roleManager.getRoles());
		request.setAttribute("access_types", programManager.getAccessTypes());
		
		return mapping.findForward("form");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm accessForm = (DynaActionForm)form;
		DefaultRoleAccess dra = (DefaultRoleAccess)accessForm.get("form");
		
		if(dra.getId().longValue() == 0) {
			dra.setId(null);
		}
		
		if(programManager.findDefaultRoleAccess(dra.getRoleId(), dra.getAccessTypeId()) == null) {		
			programManager.saveDefaultRoleAccess(dra);
		}
		this.addMessage(request,"message","Saved Access");
		
		return mapping.findForward("rlist");
	}		
	
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		
		if(id != null) {
			programManager.deleteDefaultRoleAccess(id);
		}
		
		this.addMessage(request,"message","Removed Access");
		
		return mapping.findForward("rlist");
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
	
}
