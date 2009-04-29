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

 
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;

import oscar.oscarTickler.TicklerData;
import oscar.util.DateUtils;
import oscar.util.StringUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayTicklerAction extends EctDisplayAction {
    private static final String cmd = "tickler";
    
 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
                      
 try {         

    //Set lefthand module heading and link
    String winName = "ViewTickler" + bean.demographicNo;
    String pathview, pathedit;
    if( org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable() ) {
        pathview = request.getContextPath() + "/Tickler.do";
        pathedit = request.getContextPath() + "/Tickler.do?method=edit&tickler.demographic_webName="+bean.patientLastName + "," + bean.patientFirstName+"&tickler.demographic_no=" + bean.demographicNo;
    }
    else {
        pathview = request.getContextPath() + "/tickler/ticklerDemoMain.jsp?demoview=" + bean.demographicNo + "&parentAjaxId=" + cmd;
        pathedit = request.getContextPath() + "/appointment/appointmentcontrol.jsp?keyword=" + URLEncoder.encode(bean.patientLastName + "," + bean.patientFirstName,"UTF-8") + "&displaymode=" + URLEncoder.encode("Search ", "UTF-8") + "&search_mode=search_name&originalpage=" + URLEncoder.encode(request.getContextPath() + "/tickler/ticklerAdd.jsp", "UTF-8") + "&orderby=last_name&appointment_date=2000-01-01&limit1=0&limit2=5&status=t&start_time=10:45&end_time=10:59&duration=15&dboperation=add_apptrecord&type=&demographic_no=" + bean.demographicNo + "&parentAjaxId=" + cmd + "&updateParent=false";
    }
    
    String url = "popupPage(500,900,'" + winName + "','" + pathview + "')";
    Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.viewTickler"));
    Dao.setLeftURL(url);        
    
    //set right hand heading link
    winName = "AddTickler" + bean.demographicNo;
    url = "popupPage(500,600,'" + winName + "','" + pathedit + "'); return false;";
    Dao.setRightURL(url);
    Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action
        
    String dateBegin = "1900-01-01";
    String dateEnd = "8888-12-31";        
     
    TicklerData tickler = new TicklerData();
    ResultSet rs = tickler.listTickler(bean.demographicNo, TicklerData.ACTIVE, dateBegin, dateEnd);
    
    Date serviceDate;
    Date today = new Date(System.currentTimeMillis());
    String itemHeader;    
    int hash;
    long days;
    while(rs.next()) {
        NavBarDisplayDAO.Item item = Dao.Item();                        
        serviceDate = rs.getDate("service_date");
        item.setDate(serviceDate);
        days = (today.getTime() - serviceDate.getTime())/(1000*60*60*24);
        if( days > 0 )
            item.setColour("FF0000");
            
        itemHeader = StringUtils.maxLenString(rs.getString("message"), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);                      
        item.setLinkTitle(itemHeader+ " " + DateUtils.getDate(serviceDate,dateFormat,request.getLocale()));        
        item.setTitle(itemHeader);
        winName = StringUtils.maxLenString(oscar.Misc.getString(rs,"message"), MAX_LEN_TITLE, MAX_LEN_TITLE, "");                
        hash = Math.abs(winName.hashCode());        
        url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/tickler/ticklerDemoMain.jsp?demoview=" + bean.demographicNo + "&parentAjaxId=" + cmd + "'); return false;";        
        item.setURL(url);        
        Dao.addItem(item);

    }
    
     Dao.sortItems(NavBarDisplayDAO.DATESORT);
 }catch( Exception e ) {
     System.out.println("Error retrieving " + cmd + " : " + e.getMessage());
     e.printStackTrace();
     return false;
 }
    return true;
        
  }
 
 public String getCmd() {
     return cmd;
 }
}
