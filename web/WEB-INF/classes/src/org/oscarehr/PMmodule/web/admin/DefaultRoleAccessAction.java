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
import org.oscarehr.PMmodule.web.BaseAction;

public class DefaultRoleAccessAction extends  BaseAction {
	private static Log log = LogFactory.getLog(DefaultRoleAccessAction.class);
	
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
	
}
