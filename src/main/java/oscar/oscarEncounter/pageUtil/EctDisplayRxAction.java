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
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarRx.data.RxPrescriptionData.Prescription;
import oscar.util.DateUtils;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

/**
 *
 * retrieves info to display Disease entries for demographic
 */
public class EctDisplayRxAction extends EctDisplayAction {
    private String cmd = "Rx";
    private static final Logger logger=MiscUtils.getLogger();

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
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.NavBar.Medications"));
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
        Prescription[] arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(bean.demographicNo));
        
        ArrayList<Prescription> uniqueDrugs=new ArrayList<Prescription>();
        for (Prescription p : arr) uniqueDrugs.add(p);
        
        int demographicId=Integer.parseInt(bean.demographicNo);
        
		// --- get integrator drugs ---
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		if (loggedInInfo.currentFacility.isIntegratorEnabled()) {
			try {
				
			
				List<CachedDemographicDrug> remoteDrugs  = null;
				try {
					if (!CaisiIntegratorManager.isIntegratorOffline()){
					   remoteDrugs = CaisiIntegratorManager.getDemographicWs().getLinkedCachedDemographicDrugsByDemographicId(demographicId);
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Unexpected error.", e);
					CaisiIntegratorManager.checkForConnectionError(e);
				}
				
				if(CaisiIntegratorManager.isIntegratorOffline()){
				   remoteDrugs = IntegratorFallBackManager.getRemoteDrugs(demographicId);	
				}
				
				logger.debug("remote Drugs : "+remoteDrugs.size());
				
				for (CachedDemographicDrug remoteDrug : remoteDrugs)
				{
					Prescription p=new Prescription(remoteDrug.getFacilityIdIntegerCompositePk().getIntegratorFacilityId(), remoteDrug.getCaisiProviderId(), demographicId);
					p.setArchived(remoteDrug.isArchived()?"1":"0");
					if (remoteDrug.getEndDate()!=null) p.setEndDate(remoteDrug.getEndDate().getTime());
					if (remoteDrug.getRxDate()!=null) p.setRxDate(remoteDrug.getRxDate().getTime());
					p.setSpecial(remoteDrug.getSpecial());
					
					// okay so I'm not exactly making it unique... that's the price of last minute conformance test changes.
					uniqueDrugs.add(p);
				}
			} catch (Exception e) {
				logger.error("error getting remote allergies", e);
			}
		}

		long now = System.currentTimeMillis();
        long month = 1000L * 60L * 60L * 24L * 30L;
        for( Prescription drug :uniqueDrugs ) {
            if( drug.isArchived() )
                continue;
            if(drug.isHideCpp()) {
            	continue;
            }

            NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();

            String styleColor = "";
            if (drug.isCurrent() && (drug.getEndDate().getTime() - now <= month)) {
                styleColor="style=\"color:orange;font-weight:bold;\"";
            }else if (drug.isCurrent() )  {
                styleColor="style=\"color:blue;\"";
            }else if (drug.isLongTerm() )  {
                styleColor="style=\"color:grey;\"";
            }else
                continue;

            date = drug.getRxDate();
            serviceDateStr = DateUtils.formatDate(date, request.getLocale());

            String tmp = "";
            if (drug.getFullOutLine()!=null) tmp=drug.getFullOutLine().replaceAll(";", " ");
            String strTitle = StringUtils.maxLenString(tmp, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            strTitle = "<span " + styleColor + ">" + strTitle + "</span>";
            item.setTitle(strTitle);
            item.setLinkTitle(tmp + " " + serviceDateStr + " - " + drug.getEndDate());
            item.setURL("return false;");
            Dao.addItem(item);
        }
        //Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);

        return true;
        }
    }

    public String getCmd() {
      return cmd;
    }
}
