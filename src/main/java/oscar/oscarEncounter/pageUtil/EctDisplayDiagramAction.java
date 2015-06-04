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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.eform.EFormUtil;
import oscar.util.StringUtils;

public class EctDisplayDiagramAction extends EctDisplayAction {
    private static final String cmd = "diagrams";
    
  public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {               	             
	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "r", null)) {
		throw new SecurityException("missing required security object (_eform)");
	}

	  LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        //set text for lefthand module title
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.LeftNavBar.Diagrams"));
        
        //set link for lefthand module title
        String winName = "Diagrams" + bean.demographicNo;
        String url = "popupPage(500,950,'" + winName + "', '" + request.getContextPath() + "/eform/efmpatientformlist.jsp?group_view=Eye+Form&demographic_no="+bean.demographicNo+"&apptProvider="+bean.getCurProviderNo()+"&appointment="+bean.appointmentNo+"&parentAjaxId=" + cmd + "')";
        Dao.setLeftURL(url);
        
        //we're going to display popup menu of 2 selections - row display and grid display
        String menuId = "4";
        Dao.setRightHeadingID(menuId);
        Dao.setRightURL("return !showMenu('" + menuId + "', event);");
        Dao.setMenuHeader("Ocular Diagrams");                
        
        winName = "AllDiagrams" + bean.demographicNo;
              
        ArrayList<HashMap<String, ? extends Object>> eforms = EFormUtil.listEForms(loggedInInfo, "eform_groups.group_name", "current", "Eye Form", null);
        for(int x=0;x<eforms.size();x++) {
        	HashMap<String, ? extends Object> ht = eforms.get(x);
        	url = "popupPage(700,1000,'"+winName+"', '"+ request.getContextPath() +"/eform/efmformadd_data.jsp?fid="+ht.get("fid")+"&demographic_no="+bean.demographicNo+"&appointment="+bean.appointmentNo+"')";
        	Dao.addPopUpUrl(url);
        	Dao.addPopUpText((String)ht.get("formName"));
        }
        
        ArrayList<HashMap<String,? extends Object>> patientEforms = EFormUtil.listPatientEForms(loggedInInfo, "eform_data.form_date", "current", bean.demographicNo, "Eye Form", null);
        for(int x=0;x<patientEforms.size();x++) {
        	NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
        	HashMap<String,? extends Object> form = patientEforms.get(x);
        	String strDate = (String)form.get("formDate");
        	String strTime = (String)form.get("formTime");
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	Date formDate = null;
        	try {
        		formDate = formatter.parse(strDate + " " + strTime);
        		item.setDate(formDate);
        	}catch(Exception e) {MiscUtils.getLogger().error("error:",e);item.setDate(new Date());}
        	
        	String formName = (String)form.get("formName");
        	String formSubject = (String)form.get("formSubject");
        	
        	StringBuilder title = new StringBuilder();
        	title.append(formName);
        	if(formSubject != null && formSubject.length()>0) {
        		title.append(" - ");
        		title.append(formSubject);
        	}
        	
        	String itemHeader = StringUtils.maxLenString(title.toString(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);                              
            item.setTitle(itemHeader);            
            item.setLinkTitle(title.toString());
           
            int hash = Math.abs(winName.hashCode());        
            url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/eform/efmshowform_data.jsp?fdid="+form.get("fdid")+"&appointment="+bean.appointmentNo+"&parentAjaxId=diagrams" +"'); return false;";
            item.setURL(url);               
            Dao.addItem(item);                        
        }

        Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
        
        return true;  	
  }
  
  public String getCmd() {
      return cmd;
  }
}
