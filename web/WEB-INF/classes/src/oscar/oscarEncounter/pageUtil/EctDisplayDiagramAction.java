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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;

import oscar.eform.EFormUtil;
import oscar.util.StringUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayDiagramAction extends EctDisplayAction {
    private static final String cmd = "diagrams";
    
  public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {               	             

	  String appointmentNo = request.getParameter("appointment_no");
		
        //set text for lefthand module title
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.LeftNavBar.Diagrams"));
        
        //set link for lefthand module title
        String winName = "Diagrams" + bean.demographicNo;
        String url = "popupPage(700,599,'" + winName + "','" + request.getContextPath() + "/lab/DemographicLab.jsp?demographicNo=" + bean.demographicNo + "'); return false;";
       // Dao.setLeftURL(url);
        
        //we're going to display popup menu of 2 selections - row display and grid display
        String menuId = "4";
        Dao.setRightHeadingID(menuId);
        Dao.setRightURL("return !showMenu('" + menuId + "', event);");
        Dao.setMenuHeader("Ocular Diagrams");                
        
        winName = "AllDiagrams" + bean.demographicNo;
              
        ArrayList eforms = EFormUtil.listEForms("eform_groups.group_name", "current", "Eye Form", null);
        for(int x=0;x<eforms.size();x++) {
        	Hashtable ht = (Hashtable)eforms.get(x);
        	//url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/eform/efmformadd_data.jsp?fid=" + ht.get("fid") +"&demographic_no=" + bean.demographicNo + "&appointment=" + appointmentNo + "')";
        	url = "popupPage(700,1000,'"+winName+"', '"+ request.getContextPath() +"/eform/efmformadd_data.jsp?fid="+ht.get("fid")+"&demographic_no=" + bean.demographicNo + "&appointment=" + appointmentNo+"')";        	               	
        	Dao.addPopUpUrl(url);
        	Dao.addPopUpText((String)ht.get("formName"));
        }
        
        ArrayList patientEforms = EFormUtil.listPatientEForms("eform_data.form_date", "current", bean.demographicNo, "Eye Form", null);
        for(int x=0;x<patientEforms.size();x++) {
        	NavBarDisplayDAO.Item item = Dao.Item();
        	java.util.Hashtable form = (java.util.Hashtable)patientEforms.get(x);
        	String strDate = (String)form.get("formDate");
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        	try {
        		item.setDate(formatter.parse(strDate));
        	}catch(Exception e) {item.setDate(new Date());}
        	
        	String itemHeader = StringUtils.maxLenString((String)form.get("formName"), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);                      
            item.setLinkTitle(itemHeader);        
            item.setTitle(itemHeader);
            int hash = Math.abs(winName.hashCode());        
            url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/eform/efmshowform_data.jsp?fdid="+form.get("fdid")+"&parentAjaxId=eforms" +"'); return false;";        
            item.setURL(url);               
            Dao.addItem(item);
        }

        Dao.sortItems(NavBarDisplayDAO.DATESORT);
        
        return true;  	
  }
  
  public String getCmd() {
      return cmd;
  }
}
