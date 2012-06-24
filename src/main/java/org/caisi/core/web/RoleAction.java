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

package org.caisi.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;
import org.caisi.model.Role;

public class RoleAction extends DispatchAction {

	//protected CaisiRoleManager roleManager;

	/*
	public void setCaisiRoleManager(CaisiRoleManager roleManager) {
		this.roleManager = roleManager;
	}*/

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
			return edit(mapping,form,request,response);
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		log.debug("edit");
		LazyValidatorForm roleForm = (LazyValidatorForm)form;
		Role role=new Role();
		roleForm.set("role",role);
		//List existRole=roleManager.getRoles();
		//request.setAttribute("roleList",existRole);
		return mapping.findForward("edit");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		log.debug("save");
		LazyValidatorForm roleForm = (LazyValidatorForm)form;
		Role role = (Role)roleForm.get("role");
		role.setOscar_name("");
		role.setName(role.getName().trim());
		//String rt=roleManager.saveRole(role);
		/*
		if (rt!=null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					rt));
			saveMessages(request, messages);

		}
	*/
		roleForm.set("role",new Role());
		//List existRole=roleManager.getRoles();
		//request.setAttribute("roleList",existRole);
		return mapping.findForward("edit");
	}

}
