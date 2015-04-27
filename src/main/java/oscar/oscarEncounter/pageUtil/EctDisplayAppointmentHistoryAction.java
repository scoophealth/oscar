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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.StringUtils;

public class EctDisplayAppointmentHistoryAction extends EctDisplayAction {
    private static final String cmd = "appointmentHistory";

    
 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
	
	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "r", null)) {
		throw new SecurityException("missing required security object (_appointment)");
	}

 try {

	 String cpp =request.getParameter("cpp");
	 if(cpp==null) {
		 cpp=new String();
	 }

    //Set lefthand module heading and link
    String winName = "AppointmentHistory" + bean.demographicNo;
    String pathview, pathedit;

    pathview = request.getContextPath() + "/demographic/demographiccontrol.jsp?demographic_no="+bean.demographicNo+"&orderby=appttime&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25";
    pathedit = request.getContextPath() + "/eyeform/SpecsHistory.do?specs.demographicNo=" + bean.demographicNo;


    String url = "popupPage(500,900,'" + winName + "','" + pathview + "')";
    Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.viewAppointmentHistory"));
    Dao.setLeftURL(url);

    //set right hand heading link
    winName = "AddSpecsHistory" + bean.demographicNo;
    url = "popupPage(500,600,'" + winName + "','" + pathedit + "'); return false;";
    Dao.setRightURL("return false;");
    Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action


   ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
   

    OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
    List<Appointment> appts = appointmentDao.getAppointmentHistory(Integer.parseInt(bean.getDemographicNo()));

    int limit = 5;
    int index=0;
    for(Appointment sh:appts) {
    	if(index>=limit) break;
    	NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
    	//item.setDate(sh.getAppointmentDate());
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    	//Demographic d = demographicDao.getClientByDemographicNo(sh.getDemographicNo());
    	//Provider p = d.getProvider();
    	Provider p = providerDao.getProvider(sh.getProviderNo());

    	String title = formatter.format(sh.getAppointmentDate());
    	title += " " + p.getTeam() + " " + sh.getReason();

    	String itemHeader = StringUtils.maxLenString(title, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
        item.setLinkTitle(itemHeader);
        item.setTitle(itemHeader);
        int hash = Math.abs(winName.hashCode());
        url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/eyeform/Eyeform.do?method=print&apptNos="+sh.getId()+"&cpp="+cpp+"'); return false;";
        item.setURL(url);
        Dao.addItem(item);
        index++;
    }


     Dao.sortItems(NavBarDisplayDAO.DATESORT);
 }catch( Exception e ) {
     MiscUtils.getLogger().error("Error", e);
     return false;
 }
    return true;
 	//}
  }

 public String getCmd() {
     return cmd;
 }
}
