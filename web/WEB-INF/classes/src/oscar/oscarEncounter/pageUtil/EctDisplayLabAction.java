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
import oscar.oscarLab.ca.on.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayLabAction extends EctDisplayAction {
    private static final String cmd = "labs";
    
  public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {          
        CommonLabResultData comLab = new CommonLabResultData();
        ArrayList labs = comLab.populateLabResultsData("",bean.demographicNo, "", "","","U");
        Collections.sort(labs);

        String winName = "Labs" + bean.demographicNo;
        String url = "popupPage(700,599,'" + winName + "','" + request.getContextPath() + "/lab/DemographicLab.jsp?demographicNo=" + bean.demographicNo + "'); return false;";
        String header = "<h3><a href=\"#\" onclick=\"" + url + "\">" + messages.getMessage("oscarEncounter.LeftNavBar.Labs") + "</a></h3>";
        Dao.setLeftHeading(header);
        
        StringBuffer heading = new StringBuffer("<div id=menu2 class='menu' onload='this.style.width=125;' onclick='event.cancelBubble = true;'>" +
                            "<h3 style='text-align:center'>" + messages.getMessage("oscarEncounter.LeftNavBar.LabMenuHeading") + "</h3>"); 
        
        heading.append("<a href='#' class='menuItemleft' onmouseover='this.style.color=\"black\"' onmouseout='this.style.color=\"white\"'");        
        
        winName = "AllLabs" + bean.demographicNo;
        url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues2.jsp?demographic_no=" + bean.demographicNo + "');return false;";
        heading.append(" onclick=\"" + url + "\">" + messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem1") + " </a><br/>");
        url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues.jsp?demographic_no=" + bean.demographicNo + "');return false;";
        heading.append("<a href='#' class='menuItemleft' onmouseover='this.style.color=\"black\"' onmouseout='this.style.color=\"white\"'");
        heading.append(" onclick=\"" + url + "\">" + messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem2") + " </a>");
        heading.append("</div><div id='menuTitle2' style=\"clear: both; display: inline; float: right;\"><h3><a href=\"#\" onmouseover=\"return !showMenu('2', event);\">+</a></h3></div>");        
        Dao.setRightHeading(heading.toString());
        
        NavBarDisplayDAO.Item item = Dao.Item();
        LabResultData result;
        String labDisplayName;
        //String bgcolour = "FFFFCC";
        StringBuffer func;
        for( int idx = 0; idx < labs.size(); ++idx ) {
            result = (LabResultData) labs.get(idx);
            Date date = result.getDateObj();
            String formattedDate = DateUtils.getDate(date,dateFormat);               
            func = new StringBuffer("popupPage(700,960,'");
            
            if ( result.isMDS() ){ 
                labDisplayName = result.getDiscipline();               
                url = request.getContextPath() + "/oscarMDS/SegmentDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+result.segmentID+"&status="+result.getReportStatus();
            }else if (result.isCML()){ 
                labDisplayName = result.getDiscipline();
                url = request.getContextPath() + "/lab/CA/ON/CMLDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+result.segmentID;                 
            }else {
                labDisplayName = result.getDiscipline();
                url = request.getContextPath() + "/lab/CA/BC/labDisplay.jsp?segmentID="+result.segmentID+"&providerNo="+bean.providerNo;                
            }

            try {
                winName = URLEncoder.encode(labDisplayName, "UTF-8");
            }
            catch( UnsupportedEncodingException e ) {
                winName = "viewLab" + bean.demographicNo;
                System.out.println("URLEncoder Error: " + e.getMessage());
            }
             
            labDisplayName = StringUtils.maxLenString(labDisplayName, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES) +" "+formattedDate;
            func.append(winName + "','" + url + "'); return false;");            
            item.setTitle(labDisplayName);
            item.setURL(func.toString());
            //item.setBgColour(bgcolour);
            Dao.addItem(item);
            item = Dao.Item();
        }

        return true;
  }
  
  public String getCmd() {
      return cmd;
  }
}
