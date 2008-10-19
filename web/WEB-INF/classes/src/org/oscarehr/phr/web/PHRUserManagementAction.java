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
 * PHRMessageAction.java
 *
 * Created on June 4, 2007, 4:51 PM
 *
 */

package org.oscarehr.phr.web;


import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.dao.PHRActionDAO;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.indivo.service.accesspolicies.IndivoAPService;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.phr.PHRAuthentication;
import oscar.oscarDemographic.data.DemographicData;




/**
 *
 * @author jay
 */
public class PHRUserManagementAction extends DispatchAction {  
    
    private static Log log = LogFactory.getLog(PHRUserManagementAction.class);
    
    PHRDocumentDAO phrDocumentDAO;
    PHRActionDAO phrActionDAO;
    PHRService phrService;
    PHRConstants phrConstants;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       return super.execute(mapping, form, request, response);
    }
    
    /** Creates a new instance of PHRMessageAction */
    public PHRUserManagementAction() {
    }
    
    public void setPhrDocumentDAO(PHRDocumentDAO phrDocumentDAO) {
        this.phrDocumentDAO = phrDocumentDAO;
    }
    
    public void setPhrActionDAO(PHRActionDAO phrActionDAO) {
        this.phrActionDAO = phrActionDAO;
    }
    
    public void setPhrConstants(PHRConstants phrConstants) {
        this.phrConstants = phrConstants;
    }
    
    public void setPhrService(PHRService phrService) {
      this.phrService = phrService;
    }
    
    public ActionForward unspecified(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           return null;
    }
    
    public ActionForward registerUser(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();*/
        ActionRedirect ar = new ActionRedirect(mapping.findForward("registrationResult").getPath());
        PHRAuthentication phrAuth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
        if (phrAuth == null || phrAuth.getToken() == null || phrAuth.getToken().equals("")) {
            ar.addParameter("failmessage", "Permission Denied: You must be logged into myOSCAR to register users");
            return ar;
        }
        Hashtable ht = new Hashtable();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String param = (String) paramNames.nextElement();
            if (param.indexOf("list:") == -1) 
                ht.put(param, request.getParameter(param));
            else
                ht.put(param, request.getParameterValues(param));
        }
        ht.put("registeringProviderNo", (String) request.getSession().getAttribute("user"));
        try {
            phrService.sendUserRegistration(ht, (String) request.getSession().getAttribute("user"));
            //if all is well, add the "pin" in the demographic screen
            String demographicNo = request.getParameter("demographicNo");
            DemographicData.Demographic demographic = new DemographicData().getDemographic(demographicNo);
            DemographicData dd = new DemographicData();
            dd.setDemographicPin(demographicNo, request.getParameter("username"));
        } catch (Exception e) {
            log.error("Failed to register myOSCAR user", e);
            if (e.getClass().getName().indexOf("ActionNotPerformedException") != -1) {
                ar.addParameter("failmessage", "Error on the myOSCAR server.  Perhaps the user already exists.");
            } else if (e.getClass().getName().indexOf("IndivoException") != -1) {
                ar.addParameter("failmessage", "Error on the myOSCAR server.  Perhaps the user already exists.");
            } else if (e.getClass().getName().indexOf("JAXBException") != -1) {
                ar.addParameter("failmessage", "Error: Could not generate sharing permissions (JAXBException)");
            } else {
                ar.addParameter("failmessage", "Unknown Error: Check the log file for details.");
            }
        }
        return ar;
    }
    
    public ActionForward approveAction(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        IndivoAPService apService = new IndivoAPService(phrService);
        String actionId = request.getParameter("actionId");
        PHRAction action = phrActionDAO.getActionById(actionId);
        apService.approveAccessPolicy(action);
        return mapping.findForward("msgIndex");
    }
    
    public ActionForward denyAction(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        IndivoAPService apService = new IndivoAPService(phrService);
        String actionId = request.getParameter("actionId");
        PHRAction action = phrActionDAO.getActionById(actionId);
        apService.denyAccessPolicy(action);
        return mapping.findForward("msgIndex");
    }
    
}
