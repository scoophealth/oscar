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


package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.util.ConcatPDF;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author jay
 */
public class ConsultationPrintDocsAction extends Action {
    
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "r", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
    	
        String reqId = request.getParameter("reqId");
        String demoNo = request.getParameter("demographicNo");
        //ArrayList docs = EDocUtil.listDocs( demoNo, reqId, EDocUtil.ATTACHED);        
        String[] docs = request.getParameterValues("docNo");
        ArrayList alist = new ArrayList();
        String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");        
        
        for (int i =0; i < docs.length; i++) 
            alist.add(path+docs[i]);
         

        if (alist.size() > 0 ){
            response.setContentType("application/pdf");  //octet-stream
            response.setHeader("Content-Disposition", "attachment; filename=\"combinedPDF-"+UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")+".pdf\"");
            try {
                ConcatPDF.concat(alist,response.getOutputStream());            
            } catch (IOException ex) {MiscUtils.getLogger().error("Error", ex);
            }            
            return null;
        }
                  
        return mapping.findForward("noprint");
    }
    
    /**
     * Creates a new instance of ConsultationPrintDocsAction
     */
    public ConsultationPrintDocsAction() {
    }
    
}
