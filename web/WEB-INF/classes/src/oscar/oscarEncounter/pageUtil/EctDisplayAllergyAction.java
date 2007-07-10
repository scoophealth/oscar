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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.sql.SQLException;
import org.apache.struts.util.MessageResources;
import oscar.util.StringUtils;
import oscar.util.DateUtils;

/**
 *
 * retrieves info to display Disease entries for demographic
 */
public class EctDisplayAllergyAction extends EctDisplayAction {    
    private String cmd = "allergies";
    
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {

        //set lefthand module heading and link
        String winName = "Allergy" + bean.demographicNo;
        String url = "popupPage(580,900,'" + winName + "','" + request.getContextPath() + "/oscarRx/showAllergy.do?demographicNo=" + bean.demographicNo + "')";        
        Dao.setLeftHeading(messages.getMessage("oscarEncounter.NavBar.Allergy"));
        Dao.setLeftURL(url);
        
        //set righthand link to same as left so we have visual consistency with other modules
        url += "; return false;";
        Dao.setRightURL(url);        
        Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action 
        
        //grab all of the diseases associated with patient and add a list item for each
        String dbFormat = "yyyy-MM-dd";
        String serviceDateStr;
        Date date; 
        oscar.oscarRx.data.RxPatientData.Patient.Allergy[] allergies;
        
        try {            
            allergies = new oscar.oscarRx.data.RxPatientData().getPatient(Integer.parseInt(bean.demographicNo)).getAllergies();
        
        
        for(int idx = 0; idx < allergies.length; ++idx ) {
            NavBarDisplayDAO.Item item = Dao.Item();                                    
                                     
            date = allergies[idx].getEntryDate();
            serviceDateStr = DateUtils.getDate(date, dateFormat);                                        
      
            item.setDate(date);            
            String strTitle = StringUtils.maxLenString(allergies[idx].getAllergy().getDESCRIPTION(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            
            item.setTitle(strTitle + " " + serviceDateStr);
            item.setLinkTitle(allergies[idx].getAllergy().getDESCRIPTION() + " " + serviceDateStr);
            item.setURL("return false;");
            Dao.addItem(item);
        }
        Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
        
        }
        catch( SQLException e ) {
            System.out.println("ERROR FETCHING ALLERGIES");
            e.printStackTrace();
            return false;
        }
        return true;        
    }
    
    public String getCmd() {
      return cmd;
    }
}
