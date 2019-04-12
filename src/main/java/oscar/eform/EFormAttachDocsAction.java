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


package oscar.eform;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class EFormAttachDocsAction
    extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response)

      throws ServletException, IOException {    

	  	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "u", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
	  
        DynaActionForm frm = (DynaActionForm)form;
        LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
        
        String requestId = frm.getString("requestId");
        String demoNo = frm.getString("demoNo");
        String provNo = frm.getString("providerNo");
        
        if(StringUtils.isEmpty( requestId)) {
        	return mapping.findForward("success");
        }
        if (!OscarProperties.getInstance().isPropertyActive("consultation_indivica_attachment_enabled")) {
	        String[] arrDocs = frm.getStrings("attachedDocs");
	                
	        EFormAttachDocs Doc = new EFormAttachDocs(provNo,demoNo,requestId,arrDocs);
	        Doc.attach(loggedInInfo);
	        
	        EFormAttachLabs Lab = new EFormAttachLabs(provNo,demoNo,requestId,arrDocs);
	        Lab.attach(loggedInInfo);
	        
			EFormAttachHRMReports hrmReports = new EFormAttachHRMReports(provNo, demoNo, requestId, arrDocs);
			hrmReports.attach();

			EFormAttachEForms eForms = new EFormAttachEForms(provNo, demoNo, requestId, arrDocs);
            eForms.attach(loggedInInfo);
	        return mapping.findForward("success");
        }
        else { 
        	String[] labs = request.getParameterValues("labNo");
            String[] docs = request.getParameterValues("docNo");
            String[] hrmReportIds = request.getParameterValues("hrmNo");
            String[] eFormIds = request.getParameterValues("eFormNo");
            
            if (labs == null) { labs = new String[] { }; }
            if (docs == null) { docs = new String[] { }; }
            if (hrmReportIds == null) { hrmReportIds = new String[] { }; }
            if (eFormIds == null) { eFormIds = new String[] { }; }
            
            EFormAttachDocs Doc = new EFormAttachDocs(provNo,demoNo,requestId,docs);
            Doc.attach(loggedInInfo);
            
            EFormAttachLabs Lab = new EFormAttachLabs(provNo,demoNo,requestId,labs);
            Lab.attach(loggedInInfo);
            EFormAttachHRMReports hrmReports = new EFormAttachHRMReports(provNo, demoNo, requestId, hrmReportIds);
			hrmReports.attach();
			EFormAttachEForms eForms = new EFormAttachEForms(provNo, demoNo, requestId, eFormIds);
            eForms.attach(loggedInInfo);
            return mapping.findForward("success");	
        }
    }  
}
