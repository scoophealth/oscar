// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.pageUtil;

 
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.util.MiscUtils;

import oscar.util.OscarRoleObjectPrivilege;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayConReportAction extends EctDisplayAction {
    private static final String cmd = "conReport";
    
 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
     
	 boolean a = true;
 	 ArrayList<Object> v = OscarRoleObjectPrivilege.getPrivilegePropAsArrayList("_newCasemgmt.conReport");
     String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
     a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (ArrayList) v.get(1));
     a=true;
 	if(!a) {
 		return true; //The link of tickler won't show up on new CME screen.
 	} else {
 	 
 try {         

	 String appointmentNo = request.getParameter("appointment_no");
			 
    //Set lefthand module heading and link
    String winName = "ConReport" + bean.demographicNo;
    String pathview, pathedit;
    
    pathview = request.getContextPath() + "/eyeform/Eyeform.do?method=list&demographicNo=" + bean.demographicNo;
    pathedit = request.getContextPath() + "/eyeform/Eyeform.do?method=prepareConReport&demographicNo="+bean.demographicNo + "&appNo="+appointmentNo + "&flag=new";
    
    
    String url = "popupPage(500,900,'" + winName + "','" + pathview + "')";
    Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.viewConReport"));
    Dao.setLeftURL(url);        
    
    //set right hand heading link
    winName = "AddConReport" + bean.demographicNo;
    url = "popupPage(500,600,'" + winName + "','" + pathedit + "'); return false;";
    Dao.setRightURL(url);
    Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action
 

     Dao.sortItems(NavBarDisplayDAO.DATESORT);
 }catch( Exception e ) {
     MiscUtils.getLogger().error("Error", e);
     return false;
 }
    return true;
 	}      
  }
 
 public String getCmd() {
     return cmd;
 }
}
