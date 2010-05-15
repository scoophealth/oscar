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

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.util.MessageResources;

import oscar.oscarEncounter.data.EctFormData;
import oscar.util.DateUtils;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;


public class EctDisplayFormAction extends EctDisplayAction {
    //private static final String BGCOLOUR = "917611";
    private String cmd = "forms"; 
    private String menuId = "1";
    
  public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
       
	boolean a = true;
	Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.forms");
	String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
	a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
	if(!a) {
	 	return true; //The link of form won't show up on new CME screen.
	} else {
      try {       
        
        String winName = "Forms" + bean.demographicNo;
        StringBuffer url = new StringBuffer("popupPage(600, 700, '" + winName + "', '" + request.getContextPath() + "/oscarEncounter/formlist.jsp?demographic_no=" + bean.demographicNo + "')");                               

        //set text for lefthand module title
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.Index.msgForms")); 
        //set link for lefthand module title
        Dao.setLeftURL(url.toString());               
                
        //we're going to display a pop up menu of forms so we set the menu title and id num of menu
        Dao.setRightHeadingID(menuId);
        Dao.setMenuHeader(messages.getMessage("oscarEncounter.LeftNavBar.AddFrm"));
        StringBuffer javascript = new StringBuffer("<script type=\"text/javascript\">");
        String js = "";   
        String dbFormat = "yy/MM/dd";        
        String serviceDateStr;
        StringBuffer strTitle;
        String fullTitle;
        Date date;
        String key;
        int hash;        
        //grab all of the forms
        EctFormData.Form[] forms = new EctFormData().getForms();        
        //String bgcolour = "CCFFCC";
        String BGCOLOUR = request.getParameter("hC");
        for( int j=0; j<forms.length; j++) {
            EctFormData.Form frm = forms[j];         
            winName = frm.getFormName() + bean.demographicNo;                                    
            
            String table = frm.getFormTable();
            if(!table.equalsIgnoreCase("")){
                EctFormData.PatientForm[] pforms = new EctFormData().getPatientForms(bean.demographicNo, table);
                //if a form has been started for the patient, create a module item for it
                if(pforms.length>0) {                                           
                    NavBarDisplayDAO.Item item = Dao.Item();                        
                    EctFormData.PatientForm pfrm = pforms[0];
                    
                    //convert date to that specified in base class
                    DateFormat formatter = new SimpleDateFormat(dbFormat);
                    String dateStr = pfrm.getCreated();
                    try {
                        date = (Date)formatter.parse(dateStr);                        
                    }
                    catch(ParseException ex ) {
                        System.out.println("EctDisplayFormAction: Error creating date " + ex.getMessage());                        
                        //date = new Date(System.currentTimeMillis());
                        date = null;
                    }
                    
                    if( date != null )
                        serviceDateStr = DateUtils.getDate(date, dateFormat, request.getLocale());
                    else
                        serviceDateStr = "";
                    
                    item.setDate(date);
                     
                    fullTitle = frm.getFormName();
                    strTitle = new StringBuffer(StringUtils.maxLenString(fullTitle, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES));
                    
                    hash = Math.abs(winName.hashCode()); 
                    url = new StringBuffer("popupPage(700,960,'" + hash + "started', '" + request.getContextPath() + "/form/forwardshortcutname.jsp?formname="+frm.getFormName()+"&demographic_no="+bean.demographicNo + "');");                    
                    key = StringUtils.maxLenString(fullTitle, MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + "(" + serviceDateStr + ")";
                    key = StringEscapeUtils.escapeJavaScript(key);
                    
                    //auto completion arrays and colour code are set
                    js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompList.push('" + key + "'); autoCompleted['" + key + "'] = \"" + url + "\";";                    
                    javascript.append(js);                                                                               

                    //set item href text
                    item.setTitle(strTitle.toString());
                    //set item link
                    url.append("return false;");
                    item.setURL(url.toString());
                    //set item link title text
                    item.setLinkTitle(fullTitle + " " + serviceDateStr);                    
                    
                    Dao.addItem(item);
                }                    
            }
            
            //we add all unhidden forms to the pop up menu
            if( !frm.isHidden() ) {                
                hash = Math.abs(winName.hashCode());
                url = new StringBuffer("popupPage(700,960,'" + hash + "new', '" + frm.getFormPage()+bean.demographicNo+"&formId=0&provNo="+bean.providerNo + "&parentAjaxId=" + cmd + "')");
                Dao.addPopUpUrl(url.toString());
                key = StringUtils.maxLenString(frm.getFormName(), MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + " (new)";
                Dao.addPopUpText(frm.getFormName());
                key = StringEscapeUtils.escapeJavaScript(key);
                
                //auto completion arrays and colour code are set
                js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompList.push('" + key + "'); autoCompleted['" + key + "'] = \"" + url + ";\";";                    
                javascript.append(js);                                    
            }
        }        
        url = new StringBuffer("return !showMenu('" + menuId + "', event);");
        Dao.setRightURL(url.toString());
        
        javascript.append("</script>");
        Dao.setJavaScript(javascript.toString());
        
        //sort module items, i.e. forms, from most recently started to more distant
        Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
    }
    catch(SQLException e ) {
        System.out.println("EctDisplayFormAction SQL ERROR: " + e.getMessage());                    
        return false;
    }

    return true;
	}
  } 
  
  public String getCmd() {
      return cmd;
  }
}
