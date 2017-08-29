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


package oscar.eform.upload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.eform.EFormUtil;
import oscar.util.StringUtils;

public class HtmlUploadAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "w", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
    	
        HtmlUploadForm fm = (HtmlUploadForm) form;
        FormFile formHtml = fm.getFormHtml();
        try {
            String formHtmlStr = StringUtils.readFileStream(formHtml);
            formHtmlStr = formHtmlStr.replaceAll("\\\\n", "\\\\\\\\n");
            String formName = fm.getFormName();
            String roleType = fm.getRoleType();
            String subject = fm.getSubject();
            String programNo = fm.getProgramNo();
            boolean showLatestFormOnly = fm.isShowLatestFormOnly();
            boolean patientIndependent = fm.isPatientIndependent();
            boolean restrictByProgram = fm.isRestrictByProgram();
            boolean disableUpdate = fm.isDisableUpdate();
            
            String fileName = formHtml.getFileName();
            EFormUtil.saveEForm(formName, subject, fileName, formHtmlStr, showLatestFormOnly, patientIndependent, roleType, programNo, restrictByProgram,disableUpdate);
            request.setAttribute("status", "success");
            return(mapping.findForward("success"));
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
            return(mapping.findForward("fail"));
        }
        
    }
}
