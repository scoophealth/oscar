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

import oscar.oscarEncounter.oscarMeasurements.*;
import oscar.oscarResearch.oscarDxResearch.bean.*;
import oscar.OscarProperties;
import oscar.util.DateUtils;
import oscar.util.StringUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.MessageResources;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayMeasurementsAction extends EctDisplayAction {
    private static final String cmd = "measurements";
    
   public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
       
        String winName = "measurements" + bean.demographicNo;
        String url = request.getContextPath() + "/oscarEncounter/oscarMeasurements/SetupHistoryIndex.do";       
        StringBuffer header = new StringBuffer("<h3><a href=\"#\" onclick=\"popupPage(600,1000,'" + winName + "','" + url + "'); return false;\">" + 
                messages.getMessage("oscarEncounter.Index.measurements") + "</a></h3>");
        Dao.setLeftHeading(header.toString());         
        header = new StringBuffer("<div id=menu3 class='menu' onclick='event.cancelBubble = true;'>" +
                            "<h3 style='text-align:center'>" + messages.getMessage("oscarEncounter.LeftNavBar.InputGrps") + "</h3>");
        
        dxResearchBeanHandler dxRes = new dxResearchBeanHandler(bean.demographicNo);
        Vector dxCodes = dxRes.getActiveCodeListWithCodingSystem();
        ArrayList flowsheets = MeasurementTemplateFlowSheetConfig.getInstance().getFlowsheetsFromDxCodes(dxCodes);                            
        for (int f = 0; f < flowsheets.size();f++){
            NavBarDisplayDAO.Item item = Dao.Item();
            String flowsheetName = (String) flowsheets.get(f);
            String dispname = MeasurementTemplateFlowSheetConfig.getInstance().getDisplayName(flowsheetName);

            winName = flowsheetName + bean.demographicNo;
            url = "popupPage(700,1000,'" + winName + "','" + request.getContextPath() + "/oscarEncounter/oscarMeasurements/TemplateFlowSheet.jsp?demographic_no=" + bean.demographicNo + "&template=" + flowsheetName + "');return false;";
            dispname = StringUtils.maxLenString(dispname, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            item.setTitle(dispname);
            item.setURL(url);
            Dao.addItem(item);
            //header.append("<a href='#' class='menuItemLeft' onmouseover='this.style.color=\"black\"' onmouseout='this.style.color=\"white\"' ");
            //header.append(" onclick=\"" + url + "\">" + dispname + "</a><br/>");

        }
        
        for(int j=0; j<bean.measurementGroupNames.size(); j++) {
            
            String tmp = (String)bean.measurementGroupNames.get(j);             
            winName = tmp + bean.demographicNo;
            try {
                winName = URLEncoder.encode(winName,"UTF-8");
            }
            catch( UnsupportedEncodingException e ) {
                System.out.println("URLEncoder error " + e.getMessage());
                winName = "measurementGroup" + bean.demographicNo;
            } 
            url = "popupPage(500,1000,'" + winName + "','" + request.getContextPath() + "/oscarEncounter/oscarMeasurements/SetupMeasurements.do?groupName=" + tmp + "');measurementLoaded('" + winName + "');return false;";
            header.append("<a href='#' class='menuItemLeft' onmouseover='this.style.color=\"black\"' onmouseout='this.style.color=\"white\"' ");
            header.append(" onclick=\"" + url + "\">" + tmp + "</a><br/>");
        }
        
        header.append("</div><div id='menuTitle3' style=\"clear: both; display: inline; float: right;\"><h3><a href=\"#\" onmouseover=\"return !showMenu('3', event);\">+</a></h3></div>");
        Dao.setRightHeading(header.toString());
       
        String demo = (String) bean.getDemographicNo();
        oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler hd = new oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler(demo);
        oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean data;
        Vector measureTypes = (Vector) hd.getMeasurementsDataVector();
        
        for( int idx = 0; idx < measureTypes.size(); ++idx ) {            
            data = (oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean) measureTypes.get(idx);
            String title = data.getTypeDisplayName();
            hd = new oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler(demo, data.getType());
            Vector measures = (Vector) hd.getMeasurementsDataVector();
            
            NavBarDisplayDAO.Item item = Dao.Item();
            data = (oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean) measures.get(0);
            Date date = data.getDateObservedAsDate();
            String formattedDate = DateUtils.getDate(date,dateFormat);
            String tmp = title + "  " + data.getDataField();
            tmp = StringUtils.maxLenString(tmp, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            tmp += " - " + formattedDate;
            item.setTitle(tmp);
            item.setDate(date);
            item.setURL("return false;");
            Dao.addItem(item);
            
        }
        
        return true;
        
  }
   public String getCmd() {
       return cmd;
   }
   
}
