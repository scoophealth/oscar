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


package oscar.oscarWaitingList.pageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarWaitingList.bean.WLPatientWaitingListBeanHandler;

public final class WLSetupDisplayPatientWaitingListAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
        
    	if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", null)) {
			throw new RuntimeException("missing required security object (_demographic)");
		}
    	
        String demographicNo = request.getParameter("demographic_no");
        DemographicData demoData = new DemographicData();
        org.oscarehr.common.model.Demographic demo = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo);
        String demoInfo = demo.getLastName() + ", " + demo.getFirstName() + " " + demo.getSex() + " " + demo.getAge();
        WLPatientWaitingListBeanHandler hd = new WLPatientWaitingListBeanHandler(demographicNo);           
        HttpSession session = request.getSession();
        session.setAttribute("demoInfo", demoInfo);
        session.setAttribute( "patientWaitingList", hd );  
        session.setAttribute("demographicNo", demographicNo);
       
        return (mapping.findForward("continue"));
    }
}
