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

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarReport.data.ManageLetters;

/**
 
 * @author jay
 */
public class ManagePatientLettersAction extends Action {
    
    private static Logger log = MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    /** Creates a new instance of GeneratePatientLetters */
    public ManagePatientLettersAction() {
        
    }
    
     public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
    	 if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_report", "r", null)) {
     		  throw new SecurityException("missing required security object (_report)");
     	  	}
    	 
        if (log.isTraceEnabled()) { log.trace("Start of ManagePatientLetters Action");}
   
        String classpath = (String) request.getSession().getServletContext().getAttribute("org.apache.catalina.jsp_classpath");
        System.setProperty("jasper.reports.compile.class.path", classpath); 
         
        if (log.isTraceEnabled()) { 
        }
         
        ManagePatientLettersForm frm = (ManagePatientLettersForm) form;
        FormFile fFile = frm.getReportFile();
        
        byte[] fileData = null;
        
        try {
            
            fileData = fFile.getFileData();
            String reportName = request.getParameter("reportName");
            
            //Getter Stream for letter
            //Validate that it is a valid jasper report file
            //Save to database
            
            JasperReport jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(fileData));
        
            ManageLetters manageLetters = new ManageLetters();
            manageLetters.saveReport( (String) request.getSession().getAttribute("user"), reportName,fFile.getFileName(), fileData);
        } catch (FileNotFoundException ex) {MiscUtils.getLogger().error("Error", ex);
        } catch (IOException ex) {MiscUtils.getLogger().error("Error", ex);
        } catch (JRException ex) {MiscUtils.getLogger().error("Error", ex);
        }

        if (log.isTraceEnabled()) { log.trace("End of ManagePatientLetters Action");}
        
        if ("success_manage_from_prevention".equals(request.getParameter("goto")) ){
            return mapping.findForward("success_manage_from_prevention");
        }
        return mapping.findForward("success");   
     }
    
    
    
}
