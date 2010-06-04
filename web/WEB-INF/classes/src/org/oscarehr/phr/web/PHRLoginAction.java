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

import java.util.Calendar;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.phr.PHRAuthentication;
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
     
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       HttpSession session = request.getSession();
       
       String providerNo = (String) session.getAttribute("user");
       PHRAuthentication phrAuth = null;
       String forwardTo = (String) request.getParameter("forwardto");
       //ActionForward af = new ActionForward();
       //af.setPath(forwardTo);
       //af.setRedirect(true);
       ActionForward ar = new ActionForward(forwardTo);
       request.setAttribute("forwardToOnSuccess", request.getParameter("forwardToOnSuccess"));
       //log.debug("Request URI: " + forwardTo);
       //log.debug("from request uri: " + (String) request.getParameter("forwardto"));
       //log.debug("referrer header: " + request.getHeader("referer"));
       if (!phrService.canAuthenticate(providerNo)) {
           //TODO: Need to add message about how to set up a account
           request.setAttribute("phrUserLoginErrorMsg", "You have not registered for MyOSCAR");
           request.setAttribute("phrTechLoginErrorMsg", "No MyOSCAR information in the database");
           return ar;
       }
       
       try {
           phrAuth = phrService.authenticate(providerNo, request.getParameter("phrPassword"));
       } catch (Exception e) {
           e.printStackTrace();
            /*if ((e.getCause() != null && e.getCause().getClass() == java.net.ConnectException.class)
            || (e.getCause() != null && e.getCause().getClass() == java.net.NoRouteToHostException.class)
            || (e.getCause() != null && e.getCause().getClass(). == )) {*/
           if (e.getCause() != null && e.getCause().getClass().getName().indexOf("java.") != -1) {
                //server probably offline
                log.warn("Connection to MyOSCAR server refused");
                request.setAttribute("phrUserLoginErrorMsg", "Error contacting MyOSCAR server, please try again later");
                request.setAttribute("phrTechLoginErrorMsg", e.getMessage());
                return ar;
            }
            //assume wrong password at this point, but could be due to problems on the server
            //log.warn("indivo Exception cause: " + e.getMessage());
            //log.warn("canonical name: " + e.getClass().getCanonicalName());
            //log.warn("getname: " + e.getCause().getClass().getName());
            //log.warn("getname2: " + e.getClass().getName());
            //log.warn("service server wrong");
           
           //server probably offline
           log.debug("exception");
           request.setAttribute("phrUserLoginErrorMsg", "Incorrect password");
           request.setAttribute("phrTechLoginErrorMsg", e.getMessage());
           return ar;
       }
       session.setAttribute(PHRAuthentication.SESSION_PHR_AUTH, phrAuth);
       //set next PHR Exchange for next available time
       Calendar cal = Calendar.getInstance();
       cal.roll(Calendar.HOUR_OF_DAY, false);
       session.setAttribute(phrService.SESSION_PHR_EXCHANGE_TIME, cal.getTime());
       ActionRedirect arr = new ActionRedirect(forwardTo);
       log.debug("Correct user/pass, auth success");
       return arr;
    } 
}
