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


package oscar.oscarReport.pageUtil;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarReport.data.ManageLetters;

/**
 * locate and create the letter template for the selected template.
 * For a list or patients create the letter and add it to the patient record.
 * mark in patients record that a letter was generated
 * Combine the list and return a full list
 * @author jay
 */
public class DownloadPatientLettersAction extends Action {
    
    private static Logger log = MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    /** Creates a new instance of GeneratePatientLetters */
    public DownloadPatientLettersAction() {   
    }
    
     public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
    	 if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_report", "r", null)) {
     		  throw new SecurityException("missing required security object (_report)");
     	  	}
    	 
        if (log.isTraceEnabled()) { log.trace("Start of DownloadPatientLettersAction Action");}
   
        String fileId = request.getParameter("reportID");
        try {
            ManageLetters manageLetters = new ManageLetters();
            Hashtable h = manageLetters.getReportData(fileId);
            String filename = (String) h.get("file_name");
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            response.addHeader("Content-Disposition", "attachment;filename=report.txt" );   
            manageLetters.writeLetterToStream(fileId,response.getOutputStream());
        } catch (Exception ex) {MiscUtils.getLogger().error("Error", ex);
        }
        
        if (log.isTraceEnabled()) { log.trace("End of DownloadPatientLettersAction Action");}
        return null;
     }   
}
