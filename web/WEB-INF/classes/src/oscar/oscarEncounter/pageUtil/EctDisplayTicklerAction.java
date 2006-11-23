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

 
import oscar.oscarTickler.TicklerData;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.util.MessageResources;
import oscar.util.StringUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayTicklerAction extends EctDisplayAction {
    private static final String cmd = "tickler";
    
 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
                      
 try {         

    String winName = "ViewTickler" + bean.demographicNo;
    String url = request.getContextPath() + "/tickler/ticklerDemoMain.jsp?demoview=" + bean.demographicNo;
    String heading = "<h3><a href=\"#\" onclick=\"popupPage(500,900,'" + winName + "','" + url + "');return false;\">" + 
             messages.getMessage("global.viewTickler") + "</a></h3>";
    
    Dao.setLeftHeading(heading);
    
    winName = "AddTickler" + bean.demographicNo;
    url = request.getContextPath() + "/appointment/appointmentcontrol.jsp?keyword=" + URLEncoder.encode(bean.patientLastName + "," + bean.patientFirstName,"UTF-8") + "&displaymode=" + URLEncoder.encode("Search ", "UTF-8") + "&search_mode=search_name&originalpage=" + URLEncoder.encode(request.getContextPath() + "/tickler/ticklerAdd.jsp", "UTF-8") + "&orderby=last_name&appointment_date=2000-01-01&limit1=0&limit2=5&status=t&start_time=10:45&end_time=10:59&duration=15&dboperation=add_apptrecord&type=&demographic_no=" + bean.demographicNo;
    heading = "<div id=\"addTickler\" style=\"display: inline; float: right;\"><h3><a href=\"#\" onclick=\"popupPage(500,600,'" + winName + "','" +
            url + "'); return false;\">" + messages.getMessage("oscarEncounter.Index.addTickler") + "</a></h3></div>";
    Dao.setRightHeading(heading);    
    
    String dateBegin = "0001-01-01";
    String dateEnd = "9999-12-31";        
     
    TicklerData tickler = new TicklerData();
    ResultSet rs = tickler.listTickler(bean.demographicNo, TicklerData.ACTIVE, dateBegin, dateEnd);
    
    Date createDate;
    SimpleDateFormat formater;
    String itemHeader;
    
    while(rs.next()) {
        createDate = rs.getDate("update_date");
        formater =  new SimpleDateFormat("yyyy-MM-dd");
        itemHeader = formater.format(createDate);
        itemHeader += " : " + rs.getString("message");
        
        NavBarDisplayDAO.Item item = Dao.Item();                        
        winName = StringUtils.maxLenString(rs.getString("message"), 25, 25, "");        
        try {
            winName = URLEncoder.encode(winName, "UTF-8");
        }
        catch( UnsupportedEncodingException e ) {
            System.out.println("URLEncoder error " + e.getMessage());
            winName = "ticklerView" + bean.demographicNo;
        }        
        url = "popupPage(500,900,'" + winName + "','" + request.getContextPath() + "/tickler/ticklerDemoMain.jsp?demoview=" + bean.demographicNo + "'); return false;";
        item.setURL(url);
        item.setTitle(itemHeader);
        Dao.addItem(item);

    }                                
     
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
