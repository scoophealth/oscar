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

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicAllergy;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.util.DateUtils;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

/**
 * retrieves info to display Disease entries for demographic
 */
public class EctDisplayAllergyAction extends EctDisplayAction {

	private static Logger logger = MiscUtils.getLogger();

	private String cmd = "allergies";

	public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {

		boolean a = true;
		Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.allergies");
		String roleName = (String) request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
		a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
		if (!a) {
			return true; // Allergies link won't show up on new CME screen.
		} else {

			// set lefthand module heading and link
			String winName = "Allergy" + bean.demographicNo;
			String url = "popupPage(580,900,'" + winName + "','" + request.getContextPath() + "/oscarRx/showAllergy.do?demographicNo=" + bean.demographicNo + "')";
			Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.NavBar.Allergy"));
			Dao.setLeftURL(url);

			// set righthand link to same as left so we have visual consistency with other modules
			url += "; return false;";
			Dao.setRightURL(url);
			Dao.setRightHeadingID(cmd); // no menu so set div id to unique id for this action

			// grab all of the diseases associated with patient and add a list item for each

			oscar.oscarRx.data.RxPatientData.Patient.Allergy[] allergies;

			Integer demographicId = Integer.parseInt(bean.demographicNo);
			Locale locale=request.getLocale();

			try {
				allergies = new oscar.oscarRx.data.RxPatientData().getPatient(demographicId).getAllergies();

				// --- get local allergies ---
				for (int idx = 0; idx < allergies.length; ++idx) {
					Date date = allergies[idx].getEntryDate();
					NavBarDisplayDAO.Item item = makeItem(date, allergies[idx].getAllergy().getDESCRIPTION(), allergies[idx].getAllergy().getSeverityOfReaction(), locale);
					Dao.addItem(item);
				}

				// --- get integrator allergies ---
				LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
				if (loggedInInfo.currentFacility.isIntegratorEnabled()) {
					try {
						DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
						List<CachedDemographicAllergy> remoteAllergies = demographicWs.getLinkedCachedDemographicAllergies(demographicId);
						
						for (CachedDemographicAllergy remoteAllergy : remoteAllergies)
						{
							Date date=null;
							if (remoteAllergy.getEntryDate()!=null) date=remoteAllergy.getEntryDate().getTime();

							NavBarDisplayDAO.Item item = makeItem(date, remoteAllergy.getDescription(), remoteAllergy.getSeverityOfReaction(), locale);
							Dao.addItem(item);
						}
					} catch (Exception e) {
						logger.error("error getting remote allergies", e);
					}
				}

				// --- sort all results ---
				Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
			} catch (SQLException e) {
				logger.error("ERROR FETCHING ALLERGIES", e);
				return false;
			}
			return true;
		}
	}

	private static NavBarDisplayDAO.Item makeItem(Date entryDate, String description, String severity, Locale locale)
	{
		NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
		
		item.setDate(entryDate);
		if (severity != null && severity.equals("3")) {
			item.setColour("red");
		} else if (severity != null && severity.equals("2")) {
			item.setColour("orange");
		}

		item.setTitle(StringUtils.maxLenString(description, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES));
		item.setLinkTitle(description + " " + DateUtils.formatDate(entryDate, locale));
		item.setURL("return false;");	
		
		return(item);
	}
	
	public String getCmd() {
		return cmd;
	}
}
