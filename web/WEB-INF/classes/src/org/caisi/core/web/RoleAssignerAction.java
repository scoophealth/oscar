package org.caisi.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.caisi.service.CaisiRoleManager;

public class RoleAssignerAction extends DispatchAction {
	
	protected CaisiRoleManager roleManager;
	
	public void setCaisiRoleManager(CaisiRoleManager roleManager) {
		this.roleManager = roleManager;
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
			return list(mapping,form,request,response);
	}

	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("list");
		request.setAttribute("providers",roleManager.getProviders());
		request.setAttribute("roles",roleManager.getRoles());
		return mapping.findForward("list");
	}
	
	public ActionForward assign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("assign");
		String change_provider = request.getParameter("change_provider");
		String role = request.getParameter("select_" + change_provider);
		//if choose null 
		if (role==null||"".equals(role.trim())) {
			request.setAttribute("providers",roleManager.getProviders());
			request.setAttribute("roles",roleManager.getRoles());
			return mapping.findForward("list");
		}
		
		this.roleManager.assignRole(change_provider,role);
		
		return list(mapping,form,request,response);
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return list(mapping,form,request,response);
	}
}
