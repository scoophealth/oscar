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

import oscar.oscarMessenger.util.MsgDemoMap;
import oscar.oscarMessenger.data.MsgMessageData;
import oscar.util.DateUtils;
import oscar.util.StringUtils;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.MessageResources;
import org.apache.commons.lang.StringEscapeUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayMsgAction extends EctDisplayAction {
    
    private static final String cmd = "msgs";
  
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {                                              
                          
            String winName = "ViewMsg" + bean.demographicNo;
            String url = request.getContextPath() + "/oscarMessenger/DisplayDemographicMessages.do?orderby=date&boxType=3&demographic_no=" + bean.demographicNo + "&providerNo=" + bean.providerNo + "&userName=" + bean.userName;
            String heading = "<h3 ><a href=\"#\" onClick=\"popupPage(600,900,'" + winName + "', '" + url + "'); return false;\" >" + messages.getMessage("oscarEncounter.LeftNavBar.Messages") + "</a></h3>";
            Dao.setLeftHeading(heading);
            
            winName = "SendMsg" + bean.demographicNo;
            url = "<a href=\"#\" onClick=\"popupPage(700,960,'" + winName + "','"+ request.getContextPath() + "/oscarMessenger/SendDemoMessage.do?demographic_no=" + bean.demographicNo + "'); return false;\">" + "+" + "</a>";
            heading = "<div id=\"SendMsg\" style=\"clear: both; display: inline; float: right;\"><h3>" + url + "</h3></div>";                 
            //heading = "<h3>" + url + "</h3>";
            
            Dao.setRightHeading(heading);            

            MsgDemoMap msgDemoMap = new MsgDemoMap();
            Vector msgVector = msgDemoMap.getMsgVector(bean.demographicNo);
            MsgMessageData msgData; 
            String msgId;
            String msgSubject;
            String msgDate;
            String dbFormat = "yyyy-MM-dd";
            int hash;
            for( int i=0; i<msgVector.size(); i++) {    
                msgId = (String) msgVector.elementAt(i);
                msgData = new MsgMessageData(msgId);
                msgSubject = StringUtils.maxLenString(msgData.getSubject(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
                msgDate = msgData.getDate();
                NavBarDisplayDAO.Item item = Dao.Item();
                try {                 
                    DateFormat formatter = new SimpleDateFormat(dbFormat);                                        
                    Date date = (Date)formatter.parse(msgDate);
                    msgDate = DateUtils.getDate(date, dateFormat);
                    item.setDate(date);                                                            
                }
                catch(ParseException e ) {
                        System.out.println("EctDisplayMsgAction: Error creating date " + e.getMessage());
                        msgDate = "Error";
                }                
                
                hash = winName.hashCode();
                hash = hash < 0 ? hash * -1 : hash;
                url = "popupPage(600,900,'" + hash + "','" + request.getContextPath() + "/oscarMessenger/ViewMessageByPosition.do?from=encounter&orderBy=!date&demographic_no=" + bean.demographicNo + "&messagePosition="+i + "'); return false;";
                item.setURL(url);                
                item.setTitle(msgSubject + " " + msgDate);
                Dao.addItem(item);
            }
   
           return true;
  }
    
     public String getCmd() {
         return cmd;
     }
}
