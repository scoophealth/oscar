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
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.util.UtilDateUtilities;

public class EctViewConsultationRequestsAction extends Action {
	private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	
        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "r", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
    	
        EctViewConsultationRequestsForm frm = (EctViewConsultationRequestsForm) form;
        
        String defaultPattern = "yyyy-MM-dd";   
        String sendTo = null;
        String includeCompleted = null;                
        Date startDate = null;
        Date endDate = null;
        boolean includedComp = false;        
                
        sendTo = frm.getSendTo();
        includeCompleted = frm.getIncludeCompleted();
                                
        if(includeCompleted != null && includeCompleted.equals("include") ){
           includedComp = true; 
        }                        
        try{
           startDate = UtilDateUtilities.getDateFromString(frm.getStartDate(),defaultPattern);            
        }catch(Exception e){/*ignore*/}        
        try{
           endDate = UtilDateUtilities.getDateFromString(frm.getEndDate(),defaultPattern);            
        }catch(Exception e){/*ignore*/}
                                
        request.setAttribute("startDate",startDate);               
        request.setAttribute("endDate",endDate);               
        request.setAttribute("includeCompleted",new Boolean(includedComp));               
        request.setAttribute("teamVar", sendTo);
        request.setAttribute("orderby",frm.getOrderby());
        request.setAttribute("desc",frm.getDesc());
        request.setAttribute("searchDate",frm.getSearchDate());                
        return mapping.findForward("success");
    }
    
    
}
