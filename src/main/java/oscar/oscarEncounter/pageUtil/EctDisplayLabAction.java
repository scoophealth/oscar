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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.DateUtils;
import oscar.util.StringUtils;

public class EctDisplayLabAction extends EctDisplayAction {
	private static final Logger logger=MiscUtils.getLogger();
	
	private static final String cmd = "labs";
    
  public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {          
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	
	if(!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", "r", null)) {
		throw new SecurityException("missing required security object (_lab)");
	}
     
	  logger.debug("EctDisplayLabAction");
	  
	  CommonLabResultData comLab = new CommonLabResultData();
        ArrayList<LabResultData> labs = comLab.populateLabResultsData(loggedInInfo, "",bean.demographicNo, "", "","","U");
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
        url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues2.jsp?demographic_no=" + bean.demographicNo + "')";
        Dao.addPopUpUrl(url);
        Dao.addPopUpText(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem1"));
        
        url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues.jsp?demographic_no=" + bean.demographicNo + "')";
        Dao.addPopUpUrl(url);
        Dao.addPopUpText(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem2"));
             
        //now we add individual module items
        LabResultData result;
        String labDisplayName, label;
        //String bgcolour = "FFFFCC";
        StringBuilder func; 
        int hash;
        for( int idx = 0; idx < labs.size(); ++idx ) {
            result =  labs.get(idx);
            Date date = result.getDateObj();
            String formattedDate = DateUtils.formatDate(date,request.getLocale());               
            func = new StringBuilder("popupPage(700,960,'");
            label = result.getLabel();
            
            if ( result.isMDS() ){ 
            	if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
            	else labDisplayName = label;                
                url = request.getContextPath() + "/oscarMDS/SegmentDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+result.segmentID+"&status="+result.getReportStatus();
            }else if (result.isCML()){ 
            	if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
            	else labDisplayName = label; 
                url = request.getContextPath() + "/lab/CA/ON/CMLDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+result.segmentID;                 
            }else if (result.isHL7TEXT()){
            	if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
            	else labDisplayName = label; 
                url = request.getContextPath() + "/lab/CA/ALL/labDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+result.segmentID;
            }else {
            	if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
            	else labDisplayName = label;
                url = request.getContextPath() + "/lab/CA/BC/labDisplay.jsp?segmentID="+result.segmentID+"&providerNo="+bean.providerNo;
            }

            NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
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
  
  public String getCmd() {
      return cmd;
  }
}
