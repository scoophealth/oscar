/**
 * Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.integration.mcedt.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.login.LoginForm;

public class UserAction extends DispatchAction{
	private static Logger logger = Logger.getLogger(UserAction.class);
	private UserPropertyDAO userPropertyDAO = SpringUtils.getBean(UserPropertyDAO.class);

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {				
		
		request.getSession().setAttribute("mcedtUsername",OscarProperties.getInstance().getProperty("mcedt.service.user"));
		
		if(request.getSession().getAttribute("isPassChange")!=null){
			request.getSession().removeAttribute("isPassChange");
		}		
		
		return mapping.findForward("success");
	}		
	
	public ActionForward changePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)  {
		LoginForm resourceForm = (LoginForm) form;
		try {
		
			UserProperty prop = userPropertyDAO.getProp(UserProperty.MCEDT_ACCOUNT_PASSWORD);
			if (prop == null) {
				prop = new UserProperty();
				prop.setName(UserProperty.MCEDT_ACCOUNT_PASSWORD);
				}
			prop.setValue(resourceForm.getPassword());
			userPropertyDAO.saveProp(prop);
			request.getSession().setAttribute("isPassChange", "true");
			} catch (Exception e) {
				request.getSession().setAttribute("isPassChange", "false");
			}
		
		return mapping.findForward("success");
	}
	
	public ActionForward cancel(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getSession().getAttribute("isPassChange")!=null){
			request.getSession().removeAttribute("isPassChange");
		}		
		if(request.getSession().getAttribute("mcedtUsername")!=null){
			request.getSession().removeAttribute("mcedtUsername");
		}
		return mapping.findForward("cancel");
	}
	
	public UserPropertyDAO getUserPropertyDAO() {
		return userPropertyDAO;
	}
	
	public void setUserPropertyDAO(UserPropertyDAO userPropertyDAO) {
		this.userPropertyDAO = userPropertyDAO;	
	}
}
