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

import org.apache.struts.actions.DispatchAction;

public class RoleAssignerAction extends DispatchAction {
/*	
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
	*/
}
