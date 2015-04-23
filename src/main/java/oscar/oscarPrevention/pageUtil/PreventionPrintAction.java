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


/*
 * PreventionReportAction.java
 *
 * Created on May 30, 2005, 7:52 PM
 */

package oscar.oscarPrevention.pageUtil;


import java.io.IOException;

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

import com.lowagie.text.DocumentException;

/**
 *
 * Convert submitted preventions into pdf and return file
 */
public class PreventionPrintAction extends Action {
    private static Logger logger=MiscUtils.getLogger(); 

    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
   
   public PreventionPrintAction() {
   }
   
   public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
       
	   if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_prevention", "r", null)) {
 		  throw new SecurityException("missing required security object (_prevention)");
 	  }
	   
       
       try {           
           PreventionPrintPdf pdf = new PreventionPrintPdf();
           pdf.printPdf(request, response);
           
       }catch(DocumentException de) {            
            logger.error("", de);
            request.setAttribute("printError", new Boolean(true));
            return mapping.findForward("error");
       }
       catch(IOException ioe) {
            logger.error("", ioe);
            request.setAttribute("printError", new Boolean(true));
            return mapping.findForward("error");
       } 
              
       return null;
   }   
                                    
}
