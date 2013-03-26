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


import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.util.MiscUtils;

import oscar.oscarTickler.TicklerData;
import oscar.util.DateUtils;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayTicklerAction extends EctDisplayAction {
    private static final String cmd = "tickler";

 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {

	 boolean a = true;
 	Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.viewTickler");
     String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
     a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
 	if(!a) {
 		return true; //The link of tickler won't show up on new CME screen.
 	} else {

 try {

    //Set lefthand module heading and link
    String winName = "ViewTickler" + bean.demographicNo;
    String pathview, pathedit;
    if( org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable() ) {
    	pathview = request.getContextPath() + "/Tickler.do?filter.demographic_webName="+ URLEncoder.encode(bean.patientLastName + "," + bean.patientFirstName,"UTF-8") +"&filter.demographic_no=" + bean.demographicNo +"&filter.assignee=";
    	pathedit = request.getContextPath() + "/Tickler.do?method=edit&tickler.demographic_webName="+ URLEncoder.encode(bean.patientLastName + "," + bean.patientFirstName,"UTF-8") +"&tickler.demographic_no=" + bean.demographicNo;
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
        NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
        serviceDate = rs.getDate("service_date");
        item.setDate(serviceDate);
        days = (today.getTime() - serviceDate.getTime())/(1000*60*60*24);
        if( days > 0 )
            item.setColour("#FF0000");

        itemHeader = StringUtils.maxLenString(rs.getString("message"), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
        item.setLinkTitle(itemHeader+ " " + DateUtils.formatDate(serviceDate,request.getLocale()));
        item.setTitle(itemHeader);
        //item.setValue(rs.getString("tickler_no"));
        winName = StringUtils.maxLenString(oscar.Misc.getString(rs,"message"), MAX_LEN_TITLE, MAX_LEN_TITLE, "");
        hash = Math.abs(winName.hashCode());
        if( org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable() ) {
        	url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/Tickler.do?method=view&id="+oscar.Misc.getString(rs,"tickler_no")+"'); return false;";
        } else {
        	url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/tickler/ticklerDemoMain.jsp?demoview=" + bean.demographicNo + "&parentAjaxId=" + cmd + "'); return false;";
        }
        item.setURL(url);
        Dao.addItem(item);

    }

     Dao.sortItems(NavBarDisplayDAO.DATESORT);
 }catch( Exception e ) {
     MiscUtils.getLogger().debug("Error retrieving " + cmd + " : " + e.getMessage());
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
