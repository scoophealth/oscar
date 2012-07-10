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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayMyOscarAction extends EctDisplayAction {
	
	Logger logger = MiscUtils.getLogger();
    
    private static final String cmd = "myoscar";
  
    DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
    
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) { 
    	Demographic demographic = demographicDao.getDemographic(bean.getDemographicNo());
    	
    	
    	//Does a patient have a myoscar account
    	String myoscarusername = demographic.getMyOscarUserName();
    	if(myoscarusername == null ||  myoscarusername.trim().equals("")){//No Account don't show
    		logger.debug("no myoscar account registered");
    		return true;
    	}
    	
    	//Is provider not logged in?
    	PHRAuthentication auth=MyOscarUtils.getPHRAuthentication(request.getSession());
    	if(auth != null){
    		Dao.setHeadingColour("83C659");
    	}else{
			logger.debug("provider not logged into myoscar");
    		Dao.setHeadingColour("C0C0C0");
    	}
    			
    	
    	String curProvider_no = (String) request.getSession().getAttribute("user");
    	
        //set text for lefthand module title
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.LeftNavBar.Myoscar"));
        
        //set link for lefthand module title
        String winName = "viewPatientPHR" + bean.demographicNo;          
        String url = "popupPage(600,900,'" + winName + "','" + request.getContextPath() + "/demographic/viewPhrRecord.do?demographic_no=" + bean.demographicNo +"')";
        Dao.setLeftURL(url);
            
        //set the right hand heading link
        winName = "SendMyoscarMsg" + bean.demographicNo;
        url = "popupPage(700,960,'" + winName + "','"+ request.getContextPath() + "/phr/PhrMessage.do?method=createMessage&providerNo="+curProvider_no+"&demographicNo=" + bean.demographicNo + "'); return false;";
        Dao.setRightURL(url);
        Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action
            
            /* Not yet implemented
            for( ...   
                NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
                item.setDate(date);
                item.setURL(url);                
                item.setTitle(msgSubject);
                item.setLinkTitle(msgData.getSubject() + " " + msgDate);
                Dao.addItem(item);
            }
            */
   
           return true;
      	
  }
    
     public String getCmd() {
         return cmd;
     }
}
