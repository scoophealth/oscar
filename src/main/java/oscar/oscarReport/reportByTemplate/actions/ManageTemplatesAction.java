/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

//Action that takes place when adding or editing template XML
/*
 * GenerateReport.java
 *
 * Created on March 02/2007, 10:47 PM
 *
 */

package oscar.oscarReport.reportByTemplate.actions;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.LoggedInInfo;

import oscar.oscarReport.reportByTemplate.ReportManager;

/**
 *
 * @author apavel (Paul)
 */
public class ManageTemplatesAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
    	
    	String roleName$ = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
    	if(!com.quatro.service.security.SecurityManager.hasPrivilege("_admin", roleName$)  && !com.quatro.service.security.SecurityManager.hasPrivilege("_report", roleName$)) {
    		throw new SecurityException("Insufficient Privileges");
    	}
    	
         String action = request.getParameter("action");
         String templateId = request.getParameter("templateid");
         String xmltext = request.getParameter("xmltext");
         ReportManager reportManager = new ReportManager();
         String message = "Error: Improper request - Action param missing";
         if (action.equals("delete")) {
            message = reportManager.deleteTemplate(templateId);
            if (message.equals("")) return mapping.findForward("deleted");
         }
         else if (action.equals("add"))
            message = reportManager.addTemplate(null, xmltext, LoggedInInfo.getLoggedInInfoFromSession(request));
         else if (action.equals("edit"))
            message = reportManager.updateTemplate(null, templateId, xmltext, LoggedInInfo.getLoggedInInfoFromSession(request));
         request.setAttribute("message", message);
         request.setAttribute("action", action);
         request.setAttribute("templateid", request.getParameter("templateid"));
         request.setAttribute("opentext", request.getParameter("opentext"));
         return mapping.findForward("success");
    }
    
}
