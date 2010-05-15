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
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import oscar.OscarProperties;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.DateUtils;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayLabAction2 extends EctDisplayAction {
    Logger logger = Logger.getLogger(EctDisplayLabAction.class);
    private static final String cmd = "labs";
    
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
        
        
    	boolean a = true;
      	Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.labResult");
        String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
        a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
      	if(!a) {
      		return true; //Lab result link won't show up on new CME screen.
      	} else {
      	    
    	 
    	CommonLabResultData comLab = new CommonLabResultData();
        ArrayList labs = comLab.populateLabResultsData("",bean.demographicNo, "", "","","U");
        Collections.sort(labs);
        
        //set text for lefthand module title
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.LeftNavBar.Labs"));
        
        //set link for lefthand module title
        String winName = "Labs" + bean.demographicNo;
        String url = "popupPage(700,599,'" + winName + "','" + request.getContextPath() + "/lab/DemographicLab.jsp?demographicNo=" + bean.demographicNo + "'); return false;";
        Dao.setLeftURL(url);
        
        //we're going to display popup menu of 2 selections - row display and grid display
        String menuId = "2";
        Dao.setRightHeadingID(menuId);
        Dao.setRightURL("return !showMenu('" + menuId + "', event);");
        Dao.setMenuHeader(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuHeading"));
        
        winName = "AllLabs" + bean.demographicNo;
        
        if (OscarProperties.getInstance().getBooleanProperty("HL7TEXT_LABS","yes")){
            url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues3.jsp?demographic_no=" + bean.demographicNo + "')";            
            Dao.addPopUpUrl(url);
            Dao.addPopUpText(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem1"));
            url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues2.jsp?demographic_no=" + bean.demographicNo + "')";
            Dao.addPopUpUrl(url);
            Dao.addPopUpText(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem1")+"-OLD");
        }else{
            url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues2.jsp?demographic_no=" + bean.demographicNo + "')";
            Dao.addPopUpUrl(url);
            Dao.addPopUpText(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem1"));
        }
        url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues.jsp?demographic_no=" + bean.demographicNo + "')";
        Dao.addPopUpUrl(url);
        Dao.addPopUpText(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem2"));
        
        //now we add individual module items
        LabResultData result;
        String labDisplayName;
        //String bgcolour = "FFFFCC";
        StringBuffer func;
        int hash;
        
        LinkedHashMap accessionMap = new LinkedHashMap();

        for( int i = 0; i < labs.size(); i++ ) {
            result = (LabResultData) labs.get(i);
            if (result.accessionNumber == null || result.accessionNumber.equals("")){
                accessionMap.put("noAccessionNum"+i+result.labType, result);
            }else{
                if (!accessionMap.containsKey(result.accessionNumber+result.labType))
                    accessionMap.put(result.accessionNumber+result.labType, result);
            }
        }
        labs = new ArrayList(accessionMap.values());
        logger.info("number of labs: "+labs.size());
        for (int j=0; j < labs.size(); j++){
            result = (LabResultData) labs.get(j);
            Date date = result.getDateObj();
            String formattedDate = DateUtils.getDate(date,"dd-MMM-yyyy", request.getLocale());
            //String formattedDate = DateUtils.getDate(date);
            func = new StringBuffer("popupPage(700,960,'");
            
            if ( result.isMDS() ){
                labDisplayName = result.getDiscipline();
                url = request.getContextPath() + "/oscarMDS/SegmentDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+result.segmentID+"&multiID="+result.multiLabId+"&status="+result.getReportStatus();
            }else if (result.isCML()){
                labDisplayName = result.getDiscipline();
                url = request.getContextPath() + "/lab/CA/ON/CMLDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+result.segmentID+"&multiID="+result.multiLabId;
            }else if (result.isHL7TEXT()){
                labDisplayName = result.getDiscipline();
                //url = request.getContextPath() + "/lab/CA/ALL/labDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+result.segmentID;
                url = request.getContextPath() + "/lab/CA/ALL/labDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+result.segmentID+"&multiID="+result.multiLabId;
            }else {
                labDisplayName = result.getDiscipline();
                url = request.getContextPath() + "/lab/CA/BC/labDisplay.jsp?segmentID="+result.segmentID+"&providerNo="+bean.providerNo+"&multiID="+result.multiLabId;
            }
            
            NavBarDisplayDAO.Item item = Dao.Item();
            logger.info("Adding link: "+labDisplayName+" : "+formattedDate);
            item.setLinkTitle(labDisplayName + " " + formattedDate);
            labDisplayName = StringUtils.maxLenString(labDisplayName, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES); // +" "+formattedDate;
            hash = winName.hashCode();
            hash = hash < 0 ? hash * -1 : hash;
            func.append(hash + "','" + url + "'); return false;");
            item.setTitle(labDisplayName);
            item.setURL(func.toString());
            item.setDate(date);
            //item.setBgColour(bgcolour);
            Dao.addItem(item);
        }
        
        return true;
      	}
    }
    
    public String getCmd() {
        return cmd;
    }
}
