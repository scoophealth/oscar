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
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.pageUtil;

import oscar.util.*;
import oscar.oscarEncounter.data.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.commons.lang.StringEscapeUtils;


public class EctDisplayFormAction extends EctDisplayAction {
    private String cmd = "forms";
    
  public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
                                        
    try {       
        
        StringBuffer url = new StringBuffer(request.getContextPath() + "/oscarEncounter/formlist.jsp?demographic_no=" + bean.demographicNo);
        String winName = "Forms" + bean.demographicNo;
        StringBuffer heading = new StringBuffer("<h3><a href='#' onClick=\"popupPage(600,700,'" + winName + "','" + url + "'); return false;\" >" + 
                messages.getMessage("oscarEncounter.Index.msgForms") + "</a></h3>");
        
        Dao.setLeftHeading(heading.toString());                               
               
        EctFormData.Form[] forms = new EctFormData().getForms();
        int numforms = 0;
        boolean column = false;
        String width = "125";  //width of menu
        
        if( forms.length > 40 ) { //magic number of forms for creating columns in menu
            column = true;
            width = "250";
        }
        
        heading = new StringBuffer("<div id=menu1 class='menu' onload='this.style.width=" + width + "' onclick='event.cancelBubble = true;'>" +
                            "<h3 style='text-align:center'>" + messages.getMessage("oscarEncounter.LeftNavBar.AddFrm") + "</h3>");

        StringBuffer javascript = new StringBuffer("<script type=\"text/javascript\">");
        String js = "";   
        String dbFormat = "yy/MM/dd";        
        String serviceDateStr;
        StringBuffer strTitle;
        Date date;
        //String bgcolour = "CCFFCC";
        for( int j=0; j<forms.length; j++) {
            EctFormData.Form frm = forms[j];
            
            try {
                winName = URLEncoder.encode(frm.getFormName(), "UTF-8") + bean.demographicNo;
            }
            catch( UnsupportedEncodingException e ) {
                System.out.println("URLEncoding error " + e.getMessage());
                winName = "formName" + bean.demographicNo;
            }                        
            
            String table = frm.getFormTable();
            if(!table.equalsIgnoreCase("")){
                EctFormData.PatientForm[] pforms = new EctFormData().getPatientForms(bean.demographicNo, table);
                if(pforms.length>0) {                                           
                    NavBarDisplayDAO.Item item = Dao.Item();                        
                    EctFormData.PatientForm pfrm = pforms[0];
                    
                    DateFormat formatter = new SimpleDateFormat(dbFormat);
                    String dateStr = pfrm.getCreated();
                    try {
                        date = (Date)formatter.parse(dateStr);                        
                    }
                    catch(ParseException ex ) {
                        System.out.println("EctDisplayFormAction: Error creating date " + ex.getMessage());
                        serviceDateStr = "Error";
                        date = new Date(System.currentTimeMillis());
                    }
                    
                    serviceDateStr = DateUtils.getDate(date, dateFormat);
                    item.setDate(date);
                    
                    strTitle = new StringBuffer(StringUtils.maxLenString(frm.getFormName(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES));
                    
                    url = new StringBuffer("popupPage(700,960,'" + winName + "started', '" + request.getContextPath() + "/form/forwardshortcutname.jsp?formname="+frm.getFormName()+"&demographic_no="+bean.demographicNo + "');");
                    js = "autoCompList.push('" + strTitle + "(started " + serviceDateStr + ")'); autoCompleted['" + strTitle + "(started " + serviceDateStr + ")'] = \"" + url + "\";";                    
                    javascript.append(js);                                                           
                    ++numforms;
                                        
                    item.setURL(url.toString());
                    item.setTitle(strTitle + " " + serviceDateStr);
                    //item.setBgColour(bgcolour);
                    Dao.addItem(item);
                }                    
            }
            
            if( !frm.isHidden() ) {                
                heading.append("<a href='#' class='");                
                if( column )
                    heading.append((numforms%2==0?"menuItemleft":"menuItemright") + "' ");
                else
                    heading.append("menuItemleft' ");
                
                heading.append("onmouseover='this.style.color=\"black\"' onmouseout='this.style.color=\"white\"' ");                
                url = new StringBuffer("popupPage(700,960,'" + winName + "new', '" + frm.getFormPage()+bean.demographicNo+"&formId=0&provNo="+bean.providerNo + "');");
                js = "autoCompList.push('" + frm.getFormName() + " (new)'); autoCompleted['" + frm.getFormName() + " (new)'] = \"" + url + "\";";                    
                javascript.append(js);                    
                heading.append("onclick=\"" + url + " return false;\">" + frm.getFormName() + "</a>");                
                heading.append("<br/>");
                
                ++numforms;
            }
        }
        heading.append("</div><div id='menuTitle1' style=\"clear: both; display: inline; float: right;\"><h3><a href=\"#\" onmouseover=\"return !showMenu('1', event);\">+</a></h3></div>");
        Dao.setRightHeading(heading.toString());
        javascript.append("</script>");
        Dao.setJavaScript(javascript.toString());
        Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
    }
    catch(SQLException e ) {
        System.out.println("EctDisplayFormAction SQL ERROR: " + e.getMessage());                    
        return false;
    }

    return true;
  } 
  
  public String getCmd() {
      return cmd;
  }
}
