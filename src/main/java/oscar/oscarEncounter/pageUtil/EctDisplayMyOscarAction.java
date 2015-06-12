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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.service.myoscar.MeasurementsManager;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class EctDisplayMyOscarAction extends EctDisplayAction {

	Logger logger = MiscUtils.getLogger();

	private static final String cmd = "PHR";

	private static final MedicalDataType[] MED_DATA_TYPES = { MedicalDataType.BLOOD_PRESSURE, MedicalDataType.HEIGHT_AND_WEIGHT, MedicalDataType.GLUCOSE };

	DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	 
	public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		Demographic demographic = demographicManager.getDemographic(loggedInInfo, bean.getDemographicNo());

		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_phr", "r", null)){
			return true;
		}else{
		

			//Does a patient have a myoscar account
			String myoscarusername = demographic.getMyOscarUserName();
			if (myoscarusername == null || myoscarusername.trim().equals("")) {//No Account don't show
				logger.debug("no myoscar account registered");
				Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.LeftNavBar.PHR"));
				NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
				String registrationUrl = "popupPage(700,1000,'indivoRegistration','" + request.getContextPath() + "/phr/indivo/RegisterIndivo.jsp?demographicNo=" + demographic.getDemographicNo() + "');return false;";
				item.setURL(registrationUrl);
				item.setTitle(messages.getMessage(request.getLocale(), "demographic.demographiceditdemographic.msgRegisterPHR"));
				Dao.addItem(item);
				Dao.setRightURL(registrationUrl);
				Dao.setRightHeadingID(cmd);
				return true;
			}

			//Is provider not logged in?
			MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(request.getSession());
			if (myOscarLoggedInInfo != null && myOscarLoggedInInfo.isLoggedIn()) {
				Dao.setHeadingColour("83C659");
			} else {
				logger.debug("provider not logged into myoscar");
				Dao.setHeadingColour("C0C0C0");
			}

			String curProvider_no = (String) request.getSession().getAttribute("user");

			//set text for lefthand module title
			Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.LeftNavBar.PHR"));

			//set link for lefthand module title
			String winName = "viewPatientPHR" + bean.demographicNo;
			String url = "popupPage(600,900,'" + winName + "','" + request.getContextPath() + "/demographic/viewPhrRecord.do?demographic_no=" + bean.demographicNo + "')";
			Dao.setLeftURL(url);

			//set the right hand heading link
			winName = "SendMyoscarMsg" + bean.demographicNo;
			url = "popupPage(700,960,'" + winName + "','" + request.getContextPath() + "/phr/PhrMessage.do?method=createMessage&providerNo=" + curProvider_no + "&demographicNo=" + bean.demographicNo + "'); return false;";
			Dao.setRightURL(url);
			Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action
	
			try {
				Map<MedicalDataType, List<Measurement>> mm = MeasurementsManager.getMeasurementsFromMyOscar(myOscarLoggedInInfo, demographic.getDemographicNo(), MED_DATA_TYPES);
				String demoNo = demographic.getDemographicNo().toString();
				for (MedicalDataType mdt : MED_DATA_TYPES) {
					String title = toReadableName(mdt);
					List<Measurement> measurements = mm.get(mdt);
					if (measurements == null || measurements.isEmpty()) {
						Dao.addItem(newItem(title, "black"));
						continue;
					}
	
					Measurement latestMeasurement = getLatestMeasurement(measurements);
					NavBarDisplayDAO.Item item = newItem(title, getPageName(request, mdt, demoNo), "blue");
					item.setURLJavaScript(false);
					item.setDate(latestMeasurement.getDateObserved());
					Dao.addItem(item);
				}
			} catch (Exception e) {
				logger.error("Unexpected error", e);
			}

			return true;
		}
	}

	private String getPageName(HttpServletRequest request, MedicalDataType mdt, String demoNo) {
	    return request.getContextPath() + "/oscarEncounter/myoscar/measurements_" + mdt.name().toLowerCase() + ".do?demoNo=" + demoNo + "&type=" + mdt.name();
    }



	private Measurement getLatestMeasurement(List<Measurement> measurements) {
		Measurement latest = measurements.get(0);
		for (Measurement m : measurements) {
			if (latest.getDateObserved().before(m.getDateObserved())) {
				latest = m;
			}
		}

		return latest;
	}

	private String toReadableName(MedicalDataType mdt) {
		StringBuilder buf = new StringBuilder(mdt.name().toLowerCase().replaceAll("_", " "));
		buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
		return buf.toString();
	}

	public String getCmd() {
		return cmd;
	}
}
