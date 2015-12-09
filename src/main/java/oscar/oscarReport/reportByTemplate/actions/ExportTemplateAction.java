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
package oscar.oscarReport.reportByTemplate.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.app.OAuth1Utils;
import org.oscarehr.common.dao.AppDefinitionDao;
import org.oscarehr.common.dao.AppUserDao;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.model.RssItem;

import oscar.oscarReport.reportByTemplate.ReportManager;

public class ExportTemplateAction extends Action {
	private static final AppDefinitionDao appDefinitionDao = SpringUtils.getBean(AppDefinitionDao.class);
	private static final AppUserDao appUserDao = SpringUtils.getBean(AppUserDao.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		MiscUtils.getLogger().info("Entered manage template action");
		String roleName$ = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
		if(!com.quatro.service.security.SecurityManager.hasPrivilege("_admin", roleName$)  && !com.quatro.service.security.SecurityManager.hasPrivilege("_report", roleName$)) {
			throw new SecurityException("Insufficient Privileges");
		}
		
        String name = request.getParameter("name");
        String templateId = request.getParameter("templateid");
        String message = "Failed to export report, please contact an administrator";
        
        request.setAttribute("templateid", templateId);
        
        MiscUtils.getLogger().info("Entered export template action with : " + name + ", id: " + templateId);
   	 	try {
			AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
			if(k2aApp != null) {
				LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
				AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),loggedInInfo.getLoggedInProvider().getProviderNo());
				
				if(k2aUser != null) {
					ReportManager reportManager = new ReportManager();
					String xml = reportManager.getTemplateXml(templateId);
					
					if(xml != null) {
						RssItem report = new RssItem();
						report.setName(name);
						report.setBody(xml);
						report.setType("Report");
						String jsonString = OAuth1Utils.getOAuthPostResponse(loggedInInfo, k2aApp, k2aUser, "/ws/api/posts", "/ws/api/posts", OAuth1Utils.getProviderK2A(), report);
			    		
			    		if(jsonString != null && !jsonString.isEmpty()) {
			    			message = "Sucessfully exported report with name: " + name;
			    		}
					} else {
						message = "Unable to retreive template from database, please contact an administrator";
						request.setAttribute("message", message);
						return mapping.findForward("fail");
					}
				} else {
					request.setAttribute("message", message);
					return mapping.findForward("fail");
				}
			} else {
				request.setAttribute("message", message);
				return mapping.findForward("fail");
			}
   	 	} catch(Exception e) {
 			MiscUtils.getLogger().error("Failed to export Report by Template to K2A", e);
 			request.setAttribute("message", message);
 			return mapping.findForward("fail");
 		}
   	 request.setAttribute("message", message);
   	 return mapping.findForward("success");
	}
}
