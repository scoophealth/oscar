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

package oscar.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

public final class UnlockAccountAction extends DispatchAction {

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
    	return list(mapping, form, request, response);
    }
    
    public ActionForward unlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    {
    	
    		
	    	DynaValidatorForm myForm = (DynaValidatorForm) form;
	    	String [] userIds = myForm.getString("userId").split(",");
	    	
	    	 
	    	  LoginList vec = null;
	    	  LoginCheckLogin cl = new LoginCheckLogin();
	    	  
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

    private ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
    	return mapping.findForward("list");
    	
    }
    
	
	
 }
