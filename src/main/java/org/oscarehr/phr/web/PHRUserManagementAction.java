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
 */

package org.oscarehr.phr.web;


import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.myoscar_server.ws.AccountWs;
import org.oscarehr.myoscar_server.ws.InvalidRequestException_Exception;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.ws.PersonTransfer;
import org.oscarehr.myoscar_server.ws.Relation;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.RegistrationHelper;
import org.oscarehr.phr.dao.PHRActionDAO;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.indivo.service.accesspolicies.IndivoAPService;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.phr.util.MyOscarServerWebServicesManager;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.WebUtils;

import oscar.oscarDemographic.data.DemographicData;


public class PHRUserManagementAction extends DispatchAction {  
    
    private static Logger log = MiscUtils.getLogger();
    
    PHRDocumentDAO phrDocumentDAO;
    PHRActionDAO phrActionDAO;
    PHRService phrService;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       return super.execute(mapping, form, request, response);
    }
    
    public PHRUserManagementAction() {
    }
    
    public void setPhrDocumentDAO(PHRDocumentDAO phrDocumentDAO) {
        this.phrDocumentDAO = phrDocumentDAO;
    }
    
    public void setPhrActionDAO(PHRActionDAO phrActionDAO) {
        this.phrActionDAO = phrActionDAO;
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

    	ActionRedirect ar = new ActionRedirect(mapping.findForward("registrationResult").getPath());
    	
        PHRAuthentication phrAuth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
        if (phrAuth == null || phrAuth.getMyOscarUserId() == null) {
            ar.addParameter("failmessage", "Permission Denied: You must be logged into myOSCAR to register users");
            return ar;
        }
                
        HashMap<String, Object> ht = new HashMap<String, Object>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String param = (String) paramNames.nextElement();
            if (param.indexOf("list:") == -1) 
                ht.put(param, request.getParameter(param));
            else
                ht.put(param, request.getParameterValues(param));
        }
        ht.put("registeringProviderNo", request.getSession().getAttribute("user"));
        try {
            PersonTransfer newAccount=phrService.sendUserRegistration(phrAuth, ht);
            //if all is well, add the "pin" in the demographic screen
            String demographicNo = request.getParameter("demographicNo");
            
            DemographicData dd = new DemographicData();
            dd.setDemographicPin(demographicNo, newAccount.getUserName());
            
            addRelationships(request, newAccount);
        }
        catch (InvalidRequestException_Exception e)
        {
        	log.debug("error", e);
            ar.addParameter("failmessage", "Error, most likely cause is the username already exists. Check to make sure this person doesn't already have a myoscar user or try a different username.");
        }
        catch (Exception e) {
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
    
    private void addRelationships(HttpServletRequest request, PersonTransfer newAccount) throws NotAuthorisedException_Exception {
    	
    	if (log.isDebugEnabled())
    	{
    		WebUtils.dumpParameters(request);
    	}
    	
    	PHRAuthentication auth=MyOscarUtils.getPHRAuthentication(request.getSession());
		AccountWs accountWs=MyOscarServerWebServicesManager.getAccountWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
		
		@SuppressWarnings("unchecked")
        Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			
			if (key.startsWith("enable_primary_relation_")) handlePrimaryRelation(accountWs, request, newAccount, key);
			if (key.startsWith("enable_reverse_relation_")) handleReverseRelation(accountWs, request, newAccount, key);
		}

		RegistrationHelper.storeSelectionDefaults(request);
    }

	private void handleReverseRelation(AccountWs accountWs, HttpServletRequest request, PersonTransfer newAccount, String key) throws NotAuthorisedException_Exception {
		if (!WebUtils.isChecked(request, key)) return;
	    
		Long otherMyOscarUserId=new Long(key.substring("enable_reverse_relation_".length()));
		Relation relation=Relation.valueOf(request.getParameter("reverse_relation_"+otherMyOscarUserId));
		accountWs.createRelationship(otherMyOscarUserId, newAccount.getId(), relation);
    }

	private void handlePrimaryRelation(AccountWs accountWs, HttpServletRequest request, PersonTransfer newAccount, String key) throws NotAuthorisedException_Exception {
		if (!WebUtils.isChecked(request, key)) return;
	    
		Long otherMyOscarUserId=new Long(key.substring("enable_primary_relation_".length()));
		Relation relation=Relation.valueOf(request.getParameter("primary_relation_"+otherMyOscarUserId));
		accountWs.createRelationship(newAccount.getId(), otherMyOscarUserId, relation);
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
