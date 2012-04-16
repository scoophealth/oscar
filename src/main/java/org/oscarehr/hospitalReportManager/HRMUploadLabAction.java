/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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

package org.oscarehr.hospitalReportManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.all.pageUtil.LabUploadForm;
import oscar.oscarLab.ca.all.util.Utilities;

public class HRMUploadLabAction extends DispatchAction {

	@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        LabUploadForm frm = (LabUploadForm) form;
        FormFile importFile = frm.getImportFile();
        
        try {
	        String filePath = Utilities.saveFile(importFile.getInputStream(), importFile.getFileName());
	        
	        HRMReport report = HRMReportParser.parseReport(filePath);
	        if (report != null) HRMReportParser.addReportToInbox(report);
	        
	        request.setAttribute("success", true);
	        
        } catch (Exception e) {
	        MiscUtils.getLogger().error("Couldn't handle uploaded HRM lab", e);
	        
	        request.setAttribute("success", false);
        } 
        
        
        
        return mapping.findForward("success");
	}
	
}
