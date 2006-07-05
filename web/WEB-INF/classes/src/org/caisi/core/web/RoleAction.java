package org.caisi.core.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;
import org.caisi.model.Role;
import org.caisi.service.CaisiRoleManager;

public class RoleAction extends DispatchAction {

	protected CaisiRoleManager roleManager;
	
	public void setCaisiRoleManager(CaisiRoleManager roleManager) {
		this.roleManager = roleManager;
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
			return edit(mapping,form,request,response);
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("edit");
		LazyValidatorForm roleForm = (LazyValidatorForm)form;
		Role role=new Role();
		roleForm.set("role",role);
		List existRole=roleManager.getRoles();
		request.setAttribute("roleList",existRole);
		return mapping.findForward("edit");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("save");
		LazyValidatorForm roleForm = (LazyValidatorForm)form;
		Role role = (Role)roleForm.get("role");
		role.setOscar_name("");
		role.setName(role.getName().trim());
		String rt=roleManager.saveRole(role);
		
		if (rt!=null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					rt));
			saveMessages(request, messages);
			
		}
	
		roleForm.set("role",new Role());
		List existRole=roleManager.getRoles();
		request.setAttribute("roleList",existRole);
		return mapping.findForward("edit");
	}
	
}
