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


package oscar.oscarEncounter.pageUtil;

 
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

public class EctDisplayExaminationHistoryAction extends EctDisplayAction {
    private static final String cmd = "examhistory";
    
 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eyeform", "r", null)) {
		throw new SecurityException("missing required security object (_eyeform)");
	}
    
 	 
 try {         

	 String appointmentNo = request.getParameter("appointment_no");
	 String cpp =request.getParameter("cpp");
	 if(cpp==null) {
		 cpp=new String();
	 }
	 
    //Set lefthand module heading and link
    String winName = "examinationhistory" + bean.demographicNo;
    String pathview, pathedit;
    
    pathview = request.getContextPath() + "/eyeform/ExaminationHistory.do?demographicNo="+bean.demographicNo;
    //pathedit = request.getContextPath() + "/eyeform/Macro.do?method=list";
    pathedit = request.getContextPath() + "/eyeform/Macro.do?method=addMacro";
   
    
    String url = "popupPage(500,900,'" + winName + "','" + pathview + "')";
    Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.examinationHistory"));
    Dao.setLeftURL(url);        
    
    //set right hand heading link
   // winName = "AddMacro" + bean.demographicNo;
    //url = "popupPage(500,600,'" + winName + "','" + pathedit + "'); return false;";
    Dao.setRightURL("return false;");
    Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action
        


 }catch( Exception e ) {
     MiscUtils.getLogger().error("Error", e);
     return false;
 }
    return true;
 	}      
 
 public String getCmd() {
     return cmd;
 }
}
