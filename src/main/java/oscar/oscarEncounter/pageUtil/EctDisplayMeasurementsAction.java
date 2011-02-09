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
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.pageUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;

import oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig;
import oscar.oscarResearch.oscarDxResearch.bean.dxResearchBeanHandler;
import oscar.util.DateUtils;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayMeasurementsAction extends EctDisplayAction {
    private static final String cmd = "measurements";
    
   public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {              
        
	 boolean a = true;
     Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.measurements");
     String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
     a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
     if(!a) {
     	return true; //Messurement link won't show up on new CME screen.
     } else {
	   
	   String menuId = "3"; //div id for popup menu
       
        //set text for lefthand module title
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.Index.measurements")); 
        
        //set link for lefthand module title
        String winName = "measurements" + bean.demographicNo;
        String url = "popupPage(600,1000,'" + winName + "','" + request.getContextPath() + "/oscarEncounter/oscarMeasurements/SetupHistoryIndex.do')";
        Dao.setLeftURL(url.toString());        
        
        //we're going to display a pop up menu of measurement groups
        Dao.setRightHeadingID(menuId);
        Dao.setMenuHeader(messages.getMessage("oscarEncounter.LeftNavBar.InputGrps"));
        Dao.setRightURL("return !showMenu('" + menuId + "', event);");
        
        //first we add flowsheets to the module items
        dxResearchBeanHandler dxRes = new dxResearchBeanHandler(bean.demographicNo);
        Vector dxCodes = dxRes.getActiveCodeListWithCodingSystem();        
        ArrayList flowsheets = MeasurementTemplateFlowSheetConfig.getInstance().getFlowsheetsFromDxCodes(dxCodes);                            
        int hash;
        for (int f = 0; f < flowsheets.size();f++){
            NavBarDisplayDAO.Item item = Dao.Item();
            String flowsheetName = (String) flowsheets.get(f);
            String dispname = MeasurementTemplateFlowSheetConfig.getInstance().getDisplayName(flowsheetName);

            winName = flowsheetName + bean.demographicNo;
            hash = Math.abs(winName.hashCode());
            url = "popupPage(700,1000,'" + hash + "','" + request.getContextPath() + "/oscarEncounter/oscarMeasurements/TemplateFlowSheet.jsp?demographic_no=" + bean.demographicNo + "&template=" + flowsheetName + "');return false;";
            item.setLinkTitle(dispname);
            dispname = StringUtils.maxLenString(dispname, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            item.setTitle(dispname);
            item.setURL(url);
            Dao.addItem(item);            
        }
        
        //now we grab measurement groups for popup menu
        for(int j=0; j<bean.measurementGroupNames.size(); j++) {
            
            String tmp = (String)bean.measurementGroupNames.get(j);             
            winName = tmp + bean.demographicNo;
            hash = Math.abs(winName.hashCode());            
            url = "popupPage(500,1000,'" + hash  + "','" + request.getContextPath() + "/oscarEncounter/oscarMeasurements/SetupMeasurements.do?groupName=" + tmp + "');measurementLoaded('" + hash + "')";
            Dao.addPopUpUrl(url);
            Dao.addPopUpText(tmp);            
        }
        
        //if there are none, we tell user
        if( bean.measurementGroupNames.size() == 0) {
            Dao.addPopUpUrl("");
            Dao.addPopUpText("None");
        }            

        //finally we add specific measurements to module item list
        String demo = (String) bean.getDemographicNo();
        oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler hd = new oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler(demo);
        oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean data;
        Vector measureTypes = (Vector) hd.getMeasurementsDataVector();
        
        for( int idx = 0; idx < measureTypes.size(); ++idx ) {            
            data = (oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean) measureTypes.get(idx);
            String title = data.getTypeDisplayName();
            String type = data.getType();
            
            winName = type + bean.demographicNo;
            hash = Math.abs(winName.hashCode());
            
            hd = new oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler(demo, data.getType());
            Vector measures = (Vector) hd.getMeasurementsDataVector();
            
            if(measures.size() > 0 ) {
                NavBarDisplayDAO.Item item = Dao.Item();
                data = (oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean) measures.get(0);
                Date date = data.getDateObservedAsDate();
                String formattedDate = DateUtils.getDate(date,dateFormat, request.getLocale());
                item.setLinkTitle(title + " " + data.getDataField() + " " + formattedDate);
                title = padd(title, data.getDataField());
                String tmp = "<span class=\"measureCol1\">" + title + "</span>";
                //tmp += "<span class=\"measureCol2\">" + data.getDataField() + "&nbsp;</span>";
                item.setValue(data.getDataField());
                //tmp += "<span class=\"measureCol3\">" + formattedDate + "</span><br style=\"clear:both\">";
                item.setTitle(tmp);                       
                item.setDate(date);            
                item.setURL("popupPage(300,800,'" + hash + "','" + request.getContextPath() + "/oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?type=" + type + "'); return false;");
                Dao.addItem(item);
            }
        }
        Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
        return true;
     }  
  }
   public String getCmd() {
       return cmd;
   }
   
   /**
    *truncate string to specified length so that measurements are always displayed
    *in a column
    */
   public String padd(String str, String data) {
       String tmp;
       int overflow = str.length() + data.length() - MAX_LEN_TITLE;
       //if we are over limit, truncate
       if( overflow > 0 ) {
           int maxsize = (str.length()-overflow) > 0 ? str.length()-overflow : 1;
           int minsize = maxsize > 3 ? maxsize - 3 : 0;
           String ellipses = new String();
           ellipses = org.apache.commons.lang.StringUtils.rightPad(ellipses, maxsize - minsize, '.');
           tmp = StringUtils.maxLenString(str, maxsize, minsize, ellipses);
       }
       else
           tmp = str;
       
       return tmp;
   }
   
}
