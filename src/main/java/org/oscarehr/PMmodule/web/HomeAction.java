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

package org.oscarehr.PMmodule.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import oscar.OscarProperties;

import com.quatro.common.KeyConstants;
import com.quatro.model.security.NoAccessException;
import com.quatro.service.security.SecurityManager;

public class HomeAction extends DispatchAction {
	
	
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	try {
    		setMenu(request,KeyConstants.MENU_HOME);
    		return mapping.findForward("home");
    	}
    	catch(NoAccessException e)
    	{
    		return mapping.findForward("failure");
    	}
	}
    
	private void setMenu(HttpServletRequest request,String currentMenu) throws NoAccessException {
		/*
		  isPageChangedFlag appeared?
		*/
		
		if (request.getAttribute("pageChanged") == null) {
			if(request.getParameter("pageChanged")!= null) request.setAttribute("pageChanged", request.getParameter("pageChanged"));
		}
		String lastMenu = (String) request.getSession().getAttribute("currMenu");
		if (lastMenu == null) {
			initMenu(request);
		}
		else
		{
			request.getSession().setAttribute(lastMenu, KeyConstants.ACCESS_VIEW);
		}
		// check home page access
		if(!currentMenu.equals(KeyConstants.MENU_HOME))
		{
			if (request.getSession().getAttribute(currentMenu).equals(KeyConstants.ACCESS_NULL))
			{
				throw new NoAccessException();
			}
		}
		String scrollPosition = request.getParameter("scrollPosition");
		if(null != scrollPosition) {
			request.setAttribute("scrPos", scrollPosition);
		}
		else
		{
			request.setAttribute("scrPos", "0");
		}
	}
	
	private void initMenu(HttpServletRequest request)
	{
		SecurityManager sec = (SecurityManager) request.getSession().getAttribute(KeyConstants.SESSION_KEY_SECURITY_MANAGER);
		
		if (sec==null) return;		
		//Client Management
		if(sec.hasReadAccess(KeyConstants.FUN_CLIENT, request.getSession().getAttribute("userrole") + "," + request.getSession().getAttribute("user"))) {
			request.getSession().setAttribute(KeyConstants.MENU_CLIENT, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession().setAttribute(KeyConstants.MENU_CLIENT, KeyConstants.ACCESS_NULL);
	
		//Program
		if (sec.hasReadAccess(KeyConstants.FUN_PROGRAM, request.getSession().getAttribute("userrole") + "," + request.getSession().getAttribute("user"))) {
			request.getSession().setAttribute(KeyConstants.MENU_PROGRAM, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession().setAttribute(KeyConstants.MENU_PROGRAM, KeyConstants.ACCESS_NULL);

		//Facility Management
		if (sec.hasReadAccess(KeyConstants.FUN_FACILITY, request.getSession().getAttribute("userrole") + "," + request.getSession().getAttribute("user"))) {
			request.getSession().setAttribute(KeyConstants.MENU_FACILITY, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession().setAttribute(KeyConstants.MENU_FACILITY, KeyConstants.ACCESS_NULL);

		//Report Runner
		if (sec.hasReadAccess(KeyConstants.FUN_REPORTS, request.getSession().getAttribute("userrole") + "," + request.getSession().getAttribute("user"))) {
			request.getSession().setAttribute(KeyConstants.MENU_REPORT, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession().setAttribute(KeyConstants.MENU_REPORT, KeyConstants.ACCESS_NULL);

		//System Admin
		if (OscarProperties.getInstance().isAdminOptionOn() && sec.hasReadAccess("_admin", request.getSession().getAttribute("userrole") + "," + request.getSession().getAttribute("user"))) {
			request.getSession().setAttribute(KeyConstants.MENU_ADMIN, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession().setAttribute(KeyConstants.MENU_ADMIN, KeyConstants.ACCESS_NULL);
		request.getSession().setAttribute(KeyConstants.MENU_HOME, KeyConstants.ACCESS_VIEW);
		request.getSession().setAttribute(KeyConstants.MENU_TASK, KeyConstants.ACCESS_VIEW);
	}
}
