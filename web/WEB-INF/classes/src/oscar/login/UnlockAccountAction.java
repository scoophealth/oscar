/*
 * Copyright (c) 2005. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. This program is free
 * software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version. * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. * * You should have
 * received a copy of the GNU General Public License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * <OSCAR
 * TEAM> This software was written for the Department of Family Medicine McMaster University
 * Hamilton Ontario, Canada
 */
package oscar.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.web.admin.BaseAdminAction;
import org.oscarehr.util.SpringUtils;

import com.quatro.common.KeyConstants;
import com.quatro.model.security.NoAccessException;
import com.quatro.service.LookupManager;

public final class UnlockAccountAction extends BaseAdminAction {
    private static final Logger _logger = Logger.getLogger(LoginAction.class);
    private static final String LOG_PRE = "Login!@#$: ";

    private ProviderManager providerManager = (ProviderManager) SpringUtils.getBean("providerManager");
    private LookupManager lookupManager = (LookupManager) SpringUtils.getBean("lookupManager");

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
    	return list(mapping, form, request, response);
    }
    
    public ActionForward unlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	try {
    		super.getAccess(request, KeyConstants.FUN_ADMIN_UNLOCKUSER);
	    	DynaValidatorForm myForm = (DynaValidatorForm) form;
	    	String [] userIds = myForm.getString("userId").split(",");
	    	
	    	 
	    	  LoginList vec = null;
	    	  LoginCheckLogin cl = new LoginCheckLogin();
	    	  //LoginList vec = cl.findLockList();
	    	  
	    	    // unlock
	    	  for(int i=0; i<userIds.length; i++)
	    	  {
	    		  String userName = userIds[i];
	    		  if (userName.equals("")) continue;
	    	      vec.remove(userName);
	    	      cl.unlock(userName);
	    	  }
	    	  ActionMessages messages = new ActionMessages();
	    	  messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("Selected Users are successfully unlocked "));
	    	  saveMessages(request, messages);
	    	  return list(mapping, form, request, response);
    	}
    	catch(NoAccessException e)
    	{
    		return mapping.findForward("failure");
    	}
    }
    private ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	try {
    		super.getAccess(request, KeyConstants.FUN_ADMIN_UNLOCKUSER);
    		//LoginCheckLogin cl = new LoginCheckLogin();
    		//List users = cl.getLockUserList();
    		//request.setAttribute("users", users);
    		return mapping.findForward("list");
    	}
    	catch(NoAccessException e)
    	{
    		return mapping.findForward("failure");
    	}
    }
 }