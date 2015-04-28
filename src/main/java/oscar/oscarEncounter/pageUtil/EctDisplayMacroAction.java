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

 
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.eyeform.dao.MacroDao;
import org.oscarehr.eyeform.model.Macro;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

public class EctDisplayMacroAction extends EctDisplayAction {
    private static final String cmd = "macro";
    
 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eyeform", "r", null)) {
		throw new SecurityException("missing required security object (_eyeform)");
	}
     
	 boolean a = true;
 	Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.macro");
     String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
     a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
     a=true;
 	if(!a) {
 		return true; //The link of tickler won't show up on new CME screen.
 	} else {
 	 
 try {         

	 String appointmentNo = request.getParameter("appointment_no");
	 String cpp =request.getParameter("cpp");
	 if(cpp==null) {
		 cpp=new String();
	 }
	 
    //Set lefthand module heading and link
    String winName = "macro" + bean.demographicNo;
    String pathview, pathedit;
    
    pathview = request.getContextPath() + "/eyeform/Macro.do?method=list";
    //pathedit = request.getContextPath() + "/eyeform/Macro.do?method=list";
    pathedit = request.getContextPath() + "/eyeform/Macro.do?method=addMacro";
   
    
    String url = "popupPage(500,900,'" + winName + "','" + pathview + "')";
    Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.macro"));
    Dao.setLeftURL(url);        
    
    //set right hand heading link
    winName = "AddMacro" + bean.demographicNo;
    url = "popupPage(500,600,'" + winName + "','" + pathedit + "'); return false;";
    Dao.setRightURL(url);
    Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action
        

    MacroDao shDao = (MacroDao)SpringUtils.getBean("MacroDAO");
    List<Macro> shs = shDao.getAll();

    for(Macro sh:shs) {
    	NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();                  
    	//item.setDate(new Date());
    	String itemHeader = StringUtils.maxLenString(sh.getLabel(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);                      
        item.setLinkTitle(itemHeader);        
        item.setTitle(itemHeader);
        int hash = Math.abs(winName.hashCode());        
       // url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/eyeform/Macro.do?macro.id="+ sh.getId() +"'); return false;";
        url = "return runMacro2("+sh.getId()+",'"+ sh.getLabel() +"',"+appointmentNo+",'"+cpp+"');";       
        item.setURL(url);               
        Dao.addItem(item);
    }

    // Dao.sortItems(NavBarDisplayDAO.DATESORT);
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
