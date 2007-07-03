/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * PHRLoginAction.java
 *
 * Created on April 16, 2007, 3:28 PM
 *
 */

package org.oscarehr.phr.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.indivo.service.impl.IndivoServiceImpl;
import org.oscarehr.phr.service.PHRService;

/**
 *
 * @author jay
 */
public class PHRLoginAction extends DispatchAction {
     private static Log log = LogFactory.getLog(PHRLoginAction.class);
     PHRService phrService;
    
    /**
     * Creates a new instance of PHRLoginAction
     */
    public PHRLoginAction() {
    }
    
    public void setPhrService(PHRService phrService){
        this.phrService = phrService;
    }
     
     
     /*
        Check to make sure user has and indivo Id ( if not return )
         Check for valid ticket (if valid continue on )
            Check for password in request (if not forward to login )
                Authenicate with indivo (if auth, put ticket in session)
                      foward to login
     */
    public ActionForward execute(ActionMapping mapping, ActionForm  form, HttpServletRequest request, HttpServletResponse response)throws Exception {
       HttpSession session = request.getSession();
       
       String curUserNo = (String) session.getAttribute("user");
       
       
       String intendedAction = request.getRequestURL().toString()+"?"+request.getQueryString();
       String nextAction = request.getParameter("nextAction");
       
       log.debug("intendedAction "+intendedAction+" nextAction "+nextAction);
       if (nextAction != null && !nextAction.equals(null)){
           intendedAction = nextAction;
       }
       
       if (!phrService.canAuthenticate(curUserNo)){//Need to add message about how to set up a account
           return (mapping.findForward("login"));
       }
       
       PHRAuthentication  auth = (PHRAuthentication) session.getAttribute("INDIVO_AUTH");
       log.debug("AUTH CHECK "+auth);
       if (!phrService.validAuthentication(auth)){
           log.debug("not valid authentication");
           String indivoPass = (String) request.getParameter("indivoPW");
           if (indivoPass == null){
               log.debug("returning to login");
               request.setAttribute("nextAction",intendedAction);
               return (mapping.findForward("login"));
           }else{
               //Should catch the exception so that error can be displayed to user
               log.debug("Trying to authenticate");
               auth = phrService.authenticate( curUserNo, indivoPass);
               if ( auth == null){
                   log.debug("not valid authentication  2 sending to login");
                   request.setAttribute("nextAction",intendedAction);
                   return (mapping.findForward("login"));
               } 
               log.debug("Set sessino var");
               session.setAttribute("INDIVO_AUTH",auth);
               
               
           }
       }
       
       String method = (String) request.getAttribute("method");
       request.setAttribute("INDIVO_AUTH",auth);
       
       if (nextAction != null && !nextAction.equals(null)){
           ActionForward af = new ActionForward();
           af.setPath(nextAction);
           af.setRedirect(true);
           return af;
       }

       return super.execute(mapping,   form,  request,  response);
    }
    
        
}