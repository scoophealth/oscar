/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * This software was written for the
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.provider.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.apache.log4j.Logger;

public class DashboardUserPreferenceAction extends DispatchAction {

	private UserPropertyDAO dao = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
	private Logger logger = org.oscarehr.util.MiscUtils.getLogger();
	
	@Override	   
	public ActionForward unspecified(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {	
		logger.info("here in unspecified");
		return view(mapping, actionform, request, response);	   
	}
	   
	public ActionForward view(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		UserProperty prop = dao.getProp(providerNo, "surrogate_for_provider");
		logger.info("here in view with prop=" + prop);
		if(prop != null)
			request.setAttribute("dashboardUser", prop.getValue());

		prop = dao.getProp(providerNo, "dashboard_share");
		if(prop != null)
			request.setAttribute("shareDashboard", prop.getValue());

		return mapping.findForward("form");	   
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
		String dashUser = request.getParameter("dashboardUser");
		String shareDash = request.getParameter("shareDashboard");
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		logger.info("here in save with dashUser="+dashUser);
		logger.info("and shareDash="+shareDash);
		if(dashUser != null ) {
			UserProperty prop = dao.getProp(providerNo, "surrogate_for_provider");
			if(prop == null) {
				prop = new UserProperty();
				prop.setName("surrogate_for_provider");
				prop.setProviderNo(providerNo);				
			}
			prop.setValue(dashUser);
			dao.saveProp(prop);
		}
		
		if(shareDash != null ) {
			UserProperty prop = dao.getProp(providerNo, "dashboard_share");
			if(prop == null) {
				prop = new UserProperty();
				prop.setName("dashboard_share");
				prop.setProviderNo(providerNo);				
			}
			prop.setValue(shareDash);
			dao.saveProp(prop);
		}
		
		return view(mapping,actionform,request,response);
	}
}
