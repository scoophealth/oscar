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


package oscar.oscarTickler.pageUtil;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDemographic.data.DemographicNameAgeString;

/**
 * This class is used to forward to the add tickler screen with the demographic preselected
 * @author jay
 */
public class ForwardDemographicTicklerAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    /** Creates a new instance of ForwardDemographicTicklerAction */
    public ForwardDemographicTicklerAction() {
    }
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_tickler", "u", null)) {
			throw new RuntimeException("missing required security object (_tickler)");
		}
  	
       String demoNo = request.getParameter("demographic_no");
       if ( demoNo != null ){
          Hashtable h = DemographicNameAgeString.getInstance().getNameAgeSexHashtable(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo);
          request.setAttribute("demographic_no", demoNo);
          request.setAttribute("demoName", ""+h.get("lastName")+", "+h.get("firstName"));
          
          String docType = request.getParameter("docType");
          String docId = request.getParameter("docId");
          
          request.setAttribute("docType", docType);
          request.setAttribute("docId", docId);
          
          
       }
       return mapping.findForward("success");   
    }     
}
