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


package oscar.oscarReport.oscarMeasurements.pageUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;


public final class RptSelectCDMReportAction extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_report", "r", null)) {
    		  throw new SecurityException("missing required security object (_report)");
    	}
    	
        RptSelectCDMReportForm frm = (RptSelectCDMReportForm) form;    
        HttpSession session = request.getSession();        
        String CDMgroup = (String) frm.getValue("CDMgroup");
        String forward = frm.getForward();
        
        MiscUtils.getLogger().debug("The selected group is" + CDMgroup);
        RptMeasurementTypesBeanHandler hd = new RptMeasurementTypesBeanHandler(CDMgroup);
        Vector mInstrcVector = hd.getMeasuringInstrcBeanVector();
        
        for(int i=0; i<mInstrcVector.size(); i++){
            RptMeasuringInstructionBeanHandler mInstrcs = (RptMeasuringInstructionBeanHandler) mInstrcVector.elementAt(i);
            String mInstrcName = "mInstrcs" + i;
            session.setAttribute(mInstrcName, mInstrcs);
            
        }
        MiscUtils.getLogger().debug("the value of forward is :" + forward);
        GregorianCalendar now=new GregorianCalendar(); 
        int curYear = now.get(Calendar.YEAR);
        int curMonth = (now.get(Calendar.MONTH)+1);
        int curDay = now.get(Calendar.DAY_OF_MONTH);
        String today = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DATE) ;        
        String lastYear = now.get(Calendar.YEAR)-1+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DATE) ; 
        
        session.setAttribute("measurementTypes", hd);        
        session.setAttribute("CDMGroup", CDMgroup);
        session.setAttribute("today", today);
        session.setAttribute("lastYear", lastYear);
        
        if(forward!=null){
            if(forward.compareTo("patientWhoMetGuideline")==0){
                return (mapping.findForward("patientWhoMetGuideline"));
            }
            else if(forward.compareTo("patientInAbnormalRange")==0){
                return (mapping.findForward("patientInAbnormalRange"));
            }
            else if(forward.compareTo("freqencyOfReleventTests")==0){
                return (mapping.findForward("freqencyOfReleventTests"));
            }
            else if(forward.compareTo("medicationsPrescribed")==0){
                return (mapping.findForward("medicationsPrescribed"));
            }
        }
         return (mapping.findForward("patientWhoMetGuideline"));
    }   
}
