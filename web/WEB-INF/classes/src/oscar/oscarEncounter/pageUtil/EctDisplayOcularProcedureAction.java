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

 
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.eyeform.dao.OcularProcDao;
import org.oscarehr.eyeform.model.OcularProc;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayOcularProcedureAction extends EctDisplayAction {
    private static final String cmd = "ocularProcedure";
    
 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
     
	 boolean a = true;
 	Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.ocularProcedure");
     String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
     a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
     a=true;
 	if(!a) {
 		return true; //The link of tickler won't show up on new CME screen.
 	} else {
 	 
 try {         

	String appointmentNo = request.getParameter("appointment_no");
	
    //Set lefthand module heading and link
    String winName = "OcularProcedure" + bean.demographicNo;
    String pathview, pathedit;
    
    pathview = "javascript:alert('Not yet available');";
    pathedit = request.getContextPath() + "/eyeform/OcularProc.do?proc.demographicNo=" + bean.demographicNo + "&proc.appointmentNo=" + appointmentNo;
    
    
    String url = "popupPage(500,900,'" + winName + "','" + pathview + "')";
    Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.viewOcularProcedure"));
    Dao.setLeftURL(url);        
    
    //set right hand heading link
    winName = "AddOcularProcedure" + bean.demographicNo;
    url = "popupPage(500,600,'" + winName + "','" + pathedit + "'); return false;";
    Dao.setRightURL(url);
    Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action
        

    OcularProcDao opDao = (OcularProcDao)SpringUtils.getBean("OcularProcDAO");
    List<OcularProc> ops = opDao.getByAppointmentNo(Integer.parseInt(appointmentNo));

    for(OcularProc op:ops) {
    	NavBarDisplayDAO.Item item = Dao.Item();                  
    	item.setDate(op.getDate());
    	String itemHeader = StringUtils.maxLenString(op.getProcedureName(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);                      
        item.setLinkTitle(itemHeader);        
        item.setTitle(itemHeader);
        int hash = Math.abs(winName.hashCode());        
        url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/eyeform/OcularProc.do?proc.id="+ op.getId() +"'); return false;";        
        item.setURL(url);               
        Dao.addItem(item);
    }

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
