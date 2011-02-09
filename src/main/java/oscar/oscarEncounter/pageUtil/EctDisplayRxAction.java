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

import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;

import oscar.util.DateUtils;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

/**
 *
 * retrieves info to display Disease entries for demographic
 */
public class EctDisplayRxAction extends EctDisplayAction {
    private String cmd = "Rx";

    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {

    	boolean a = true;
        Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.prescriptions");
        String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
        a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
        if(!a) {
             return true; //Prescription link won't show up on new CME screen.
        } else {    	
    	
        //set lefthand module heading and link
        String winName = "Rx" + bean.demographicNo;
        String leftUrl="popupPage(580,1027,'" + winName + "','" + request.getContextPath() + "/oscarRx/choosePatient.do?providerNo=" + bean.providerNo + "&demographicNo=" + bean.demographicNo + "')";
        String url = "popupPage(580,1027,'" + winName + "','" + request.getContextPath() + "/oscarRx/choosePatient.do?providerNo=" + bean.providerNo +"&ltm=true"+ "&demographicNo=" + bean.demographicNo + "')";
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.NavBar.Prescriptions"));
        Dao.setLeftURL(leftUrl);

        //set righthand link to same as left so we have visual consistency with other modules
        url += "; return false;";
        Dao.setRightURL(url);
        Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action

        //grab all of the diseases associated with patient and add a list item for each
        String dbFormat = "yyyy-MM-dd";
        String serviceDateStr;
        Date date;
        oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
        oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = {};
        arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(bean.demographicNo));
        long now = System.currentTimeMillis();
        long month = 1000L * 60L * 60L * 24L * 30L;
        for(int idx = 0; idx < arr.length; ++idx ) {
            oscar.oscarRx.data.RxPrescriptionData.Prescription drug = arr[idx];
            if( drug.isArchived() )
                continue;

            NavBarDisplayDAO.Item item = Dao.Item();

            String styleColor = "";
            if (drug.isCurrent() && (drug.getEndDate().getTime() - now <= month)) {
                styleColor="style=\"color:orange;font-weight:bold;\"";
            }else if (drug.isCurrent() )  {
                styleColor="style=\"color:blue;\"";
            }else
                continue;

            date = drug.getRxDate();
            serviceDateStr = DateUtils.getDate(date, dateFormat, request.getLocale());

            String tmp = drug.getFullOutLine().replaceAll(";", " ");
            String strTitle = StringUtils.maxLenString(tmp, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            strTitle = "<span " + styleColor + ">" + strTitle + "</span>";
            item.setTitle(strTitle);
            item.setLinkTitle(tmp + " " + serviceDateStr + " - " + arr[idx].getEndDate());
            item.setURL("return false;");
            Dao.addItem(item);
        }
        Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);

        return true;
        }
    }

    public String getCmd() {
      return cmd;
    }
}
