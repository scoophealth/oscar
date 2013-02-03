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

package org.oscarehr.PMmodule.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.utility.RoleCache;

import com.quatro.service.security.RolesManager;

public class DefaultRoleAccessAction extends  DispatchAction {
    private ProgramManager programManager;
    private RolesManager roleManager;
	
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
		
		RoleCache.reload();
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
		
		RoleCache.reload();
		
		return mapping.findForward("rlist");
	}		
	
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		
		if(id != null) {
			programManager.deleteDefaultRoleAccess(id);
		}
		
		this.addMessage(request,"message","Removed Access");
		
		RoleCache.reload();
		
		return mapping.findForward("rlist");
	}

    public void setProgramManager(ProgramManager mgr) {
    	this.programManager = mgr;
    }

    public void setRolesManager(RolesManager mgr) {
    	this.roleManager = mgr;
    }
    
    private  void addMessage(HttpServletRequest req, String key, String val) {
		ActionMessages msgs = getMessages(req);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, val));
		addMessages(req, msgs);
	}
}
