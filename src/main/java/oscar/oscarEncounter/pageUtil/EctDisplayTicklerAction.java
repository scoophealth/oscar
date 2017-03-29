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


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.DateUtils;
import oscar.util.StringUtils;

public class EctDisplayTicklerAction extends EctDisplayAction {
    private static final String cmd = "tickler";

 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
	 LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	 
	if (!securityInfoManager.hasPrivilege(loggedInInfo, "_tickler", "r", null)) {
 		return true; //The link of tickler won't show up on new CME screen.
 	} else {

 

    //Set lefthand module heading and link
    String winName = "ViewTickler" + bean.demographicNo;
    String pathview, pathedit;
    if( org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable() ) {
    	pathview = request.getContextPath() + "/Tickler.do?filter.demographic_webName="+ encode(bean) +"&filter.demographicNo=" + bean.demographicNo +"&filter.assignee=";
    	pathedit = request.getContextPath() + "/Tickler.do?method=edit&tickler.demographic_webName="+ encode(bean) +"&tickler.demographicNo=" + bean.demographicNo;
    }
    else {
        pathview = request.getContextPath() + "/tickler/ticklerDemoMain.jsp?demoview=" + bean.demographicNo + "&parentAjaxId=" + cmd;
        pathedit = request.getContextPath() + "/appointment/appointmentcontrol.jsp?keyword=" + encode(bean) + "&displaymode=" + encode("Search ") + "&search_mode=search_name&originalpage=" + encode(request.getContextPath() + "/tickler/ticklerAdd.jsp") + "&orderby=last_name&appointment_date=2000-01-01&limit1=0&limit2=5&status=t&start_time=10:45&end_time=10:59&duration=15&dboperation=search_demorecord&type=&demographic_no=" + bean.demographicNo + "&parentAjaxId=" + cmd + "&updateParent=false";
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

    TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
    List<Tickler> ticklers = ticklerManager.findActiveByDemographicNo(loggedInInfo,Integer.parseInt(bean.demographicNo));

    Date serviceDate;
    Date today = new Date(System.currentTimeMillis());
    String itemHeader;
    String priority;
    String flag;
    int hash;
    long days;
    for(Tickler t : ticklers) {
        NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
        serviceDate = t.getServiceDate();
        priority = t.getPriorityWeb().toUpperCase( );

        item.setDate(serviceDate);
        days = (today.getTime() - serviceDate.getTime())/(1000*60*60*24);
        if( days > 0 )
            item.setColour("#FF0000");

        itemHeader = StringUtils.maxLenString(t.getMessage(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
        item.setLinkTitle(priority + ": " + itemHeader + " " + DateUtils.formatDate(serviceDate,request.getLocale()));
        if(priority.equals("HIGH")){
        	flag = "<span style='color:red'>&#9873;</span> ";
        	itemHeader = "<b>"+itemHeader+"</b>";
        }else{
        	flag = "";
        }
        item.setTitle(flag + itemHeader);
        // item.setValue(String.valueOf(t.getTickler_no()));
        winName = StringUtils.maxLenString(t.getMessage(), MAX_LEN_TITLE, MAX_LEN_TITLE, "");
        hash = Math.abs(winName.hashCode());
        if( org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable() ) {
        	url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/Tickler.do?method=view&id="+t.getId()+"'); return false;";
        } else {
        	url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/tickler/ticklerDemoMain.jsp?demoview=" + bean.demographicNo + "&parentAjaxId=" + cmd + "'); return false;";
        }
        item.setURL(url);
        Dao.addItem(item);

    }
 	}

     Dao.sortItems(NavBarDisplayDAO.DATESORT);
 
    return true;
  }

	private String encode(EctSessionBean bean) {
		return encode(bean.patientLastName + "," + bean.patientFirstName);
	}

	private String encode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			MiscUtils.getLogger().error("Unable to encode string using UTF-8", e);
			throw new RuntimeException(e);
		}
	}

	public String getCmd() {
		return cmd;
	}
}
