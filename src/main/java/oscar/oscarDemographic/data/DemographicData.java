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


package oscar.oscarDemographic.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.DemographicCustDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicCust;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.util.UtilDateUtilities;

public class DemographicData {

	private DemographicCustDao demographicCustDao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");
	private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	
	public DemographicData() {
	}

	public String getDemographicFirstLastName(LoggedInInfo loggedInInfo, String demographicNo) {
		org.oscarehr.common.model.Demographic demographic = demographicManager.getDemographic(loggedInInfo,demographicNo);
		if(demographic != null) {
			return demographic.getFirstName() + " " + demographic.getLastName();
		}

		return "";
	}

	public Date getDemographicDOB(LoggedInInfo loggedInInfo, String demographicNo) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		org.oscarehr.common.model.Demographic demographic = demographicManager.getDemographic(loggedInInfo,demographicNo);
		if(demographic != null) {
			try {
				date = formatter.parse(demographic.getYearOfBirth() + "-" + demographic.getMonthOfBirth() + "-" + demographic.getDateOfBirth());
			}catch(ParseException e) {
				date = null;
			}
		}
		return date;
	}

	public String getDemographicNoByIndivoId(LoggedInInfo loggedInInfo, String myOscarUserName) {
		org.oscarehr.common.model.Demographic demographic = demographicManager.getDemographicByMyOscarUserName(loggedInInfo, myOscarUserName);
		if(demographic != null) {
			return demographic.getDemographicNo().toString();
		}
		return "";
	}

	public String getDemoNoByNamePhoneEmail(LoggedInInfo loggedInInfo, String firstName, String lastName, String hPhone, String wPhone, String email) {
		org.oscarehr.common.model.Demographic demographic = demographicManager.getDemographicByNamePhoneEmail(loggedInInfo, firstName, lastName, hPhone, wPhone, email);

		if(demographic != null) {
			return String.valueOf(demographic.getDemographicNo());
		}
		return "";
	}

	// //
	public int numDemographicsWithHIN(LoggedInInfo loggedInInfo, String hin) {
		return demographicManager.searchByHealthCard(loggedInInfo, hin).size();
	}

	public boolean isUniqueHin(LoggedInInfo loggedInInfo, String hin) {
		return numDemographicsWithHIN(loggedInInfo, hin) == 0;
	}

	public ArrayList<Demographic> getDemographicWithHIN(LoggedInInfo loggedInInfo, String hin) {
		ArrayList<Demographic> list = new ArrayList<Demographic>();
		List<org.oscarehr.common.model.Demographic> demos = demographicManager.searchByHealthCard(loggedInInfo, hin);
		for(org.oscarehr.common.model.Demographic demo:demos) {
			list.add(getDemographic(loggedInInfo, String.valueOf(demo.getDemographicNo())));
		}
		return list;
	}

	public ArrayList<Demographic> getDemographicWithLastFirstDOB(LoggedInInfo loggedInInfo, String lastname, String firstname, String dob) {
		if (dob != null) {
			Date bDate = UtilDateUtilities.StringToDate(dob, "yyyy-MM-dd");
			String year_of_birth = UtilDateUtilities.DateToString(bDate, "yyyy");
			String month_of_birth = UtilDateUtilities.DateToString(bDate, "MM");
			String date_of_birth = UtilDateUtilities.DateToString(bDate, "dd");

			return getDemographicWithLastFirstDOB(loggedInInfo, lastname, firstname, year_of_birth, month_of_birth, date_of_birth);
		} else {
			return (getDemographicWithLastFirstDOB(loggedInInfo, lastname, firstname, null, null, null));
		}
	}

	public ArrayList<Demographic> getDemographicWithLastFirstDOB(LoggedInInfo loggedInInfo, String lastname, String firstname, String year_of_birth, String month_of_birth, String date_of_birth) {
		ArrayList<Demographic> list = new ArrayList<Demographic>();
		List<org.oscarehr.common.model.Demographic> demos = demographicManager.getDemographicWithLastFirstDOB(loggedInInfo, lastname, firstname, year_of_birth, month_of_birth, date_of_birth);
		for(org.oscarehr.common.model.Demographic demo:demos) {
			list.add(getDemographic(loggedInInfo, String.valueOf(demo.getDemographicNo())));
		}

		return list;
	}

	public String getNameAgeString(LoggedInInfo loggedInInfo, String demographicNo) {
		String nameage = "";
		org.oscarehr.common.model.Demographic demographic = demographicManager.getDemographic(loggedInInfo,demographicNo);
		if(demographic != null) {
			String age = UtilDateUtilities.calcAge(UtilDateUtilities.calcDate(demographic.getYearOfBirth(), demographic.getMonthOfBirth(), demographic.getDateOfBirth()));
			if (age == null) {
				age = "";
			}
			nameage = demographic.getLastName() + ", " + demographic.getFirstName() + " " + demographic.getSex() + " " + age;
		}

		return nameage;
	}

	public String[] getNameAgeSexArray(LoggedInInfo loggedInInfo, Integer demographicNo) {
		String[] nameage = null;
		org.oscarehr.common.model.Demographic demographic =  demographicManager.getDemographic(loggedInInfo,demographicNo);
		if(demographic != null) {
			String age = UtilDateUtilities.calcAge(UtilDateUtilities.calcDate(demographic.getYearOfBirth(), demographic.getMonthOfBirth(), demographic.getDateOfBirth()));
			if (age == null) {
				age = "";
			}
			nameage = new String[] {demographic.getLastName(),demographic.getFirstName(), demographic.getSex(),age};
		}

		return nameage;
	}

	public String getDemographicSex(LoggedInInfo loggedInInfo, String demographicNo) {
		org.oscarehr.common.model.Demographic demographic =  demographicManager.getDemographic(loggedInInfo,demographicNo);
		if(demographic != null) {
			return demographic.getSex();
		}
		return "";
	}

	public Demographic getSubstituteDecisionMaker(LoggedInInfo loggedInInfo, String DemographicNo) {
		Demographic demographic = null;
		DemographicRelationship dr = new DemographicRelationship();
		String demoNo = dr.getSDM(DemographicNo);
		if (demoNo != null) {
			demographic = getDemographic(loggedInInfo, demoNo);
		}
		return demographic;
	}

	public String getDemographicNotes(String demographicNo) {
		String retval = "";
		DemographicCust demographicCust = demographicCustDao.find(Integer.parseInt(demographicNo));
		if(demographicCust != null) {
			retval = demographicCust.getNotes();
		}

		if (retval.startsWith("<unotes>")) retval = retval.substring(8);
		if (retval.endsWith("</unotes>")) retval = retval.substring(0, retval.length() - 9);

		return retval;
	}

	public Demographic getDemographic(LoggedInInfo loggedInInfo, String DemographicNo) {
		Demographic demographic =  demographicManager.getDemographic(loggedInInfo,DemographicNo);
		return demographic;
	}

	public String getDemographicNoByMyOscarUserName(LoggedInInfo loggedInInfo, String myOscarUserName) {
		org.oscarehr.common.model.Demographic d = demographicManager.getDemographicByMyOscarUserName(loggedInInfo, myOscarUserName);
		if(d != null) {
			return d.getDemographicNo().toString();
		}
		return "";
	}

	public String getDemographicDateJoined(LoggedInInfo loggedInInfo, String demographicNo) {
		org.oscarehr.common.model.Demographic d =  demographicManager.getDemographic(loggedInInfo,demographicNo);
		if(d != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(d.getDateJoined());
		}
		return null;
	}

	public void setDemographicPin(LoggedInInfo loggedInInfo, String demographicNo, String myOscarUserName) {
		org.oscarehr.common.model.Demographic d =  demographicManager.getDemographic(loggedInInfo,demographicNo);
		if(d != null) {
			d.setMyOscarUserName(myOscarUserName);
			demographicManager.updateDemographic(loggedInInfo, d);
		}
	}

	public void setDemographic(LoggedInInfo loggedInInfo, Demographic dm) {
		if (dm.getDemographicNo() == null) return;
		demographicManager.updateDemographic(loggedInInfo, dm);
	}
	
	public void setDemographicEmail(LoggedInInfo loggedInInfo, String demographicNo, String email) {
		Demographic d =  demographicManager.getDemographic(loggedInInfo,demographicNo);
		if(d != null) {
			d.setEmail(email);
			demographicManager.updateDemographic(loggedInInfo, d);
		}
	}


	public static String getAge(Demographic d) {
		if (oscar.util.StringUtils.empty(d.getYearOfBirth()) || oscar.util.StringUtils.empty(d.getMonthOfBirth()) || oscar.util.StringUtils.empty(d.getDateOfBirth())) {
			return "";
		}
		return (String.valueOf(oscar.util.UtilDateUtilities.calcAge(d.getYearOfBirth(), d.getMonthOfBirth(), d.getDateOfBirth())));
	}

	public static String getAgeAsOf(Demographic d,Date asofDate) {
		return UtilDateUtilities.calcAgeAtDate(UtilDateUtilities.calcDate(d.getYearOfBirth(), d.getMonthOfBirth(), d.getDateOfBirth()), asofDate);
	}

	public static int getAgeInMonths(Demographic d) {
		return UtilDateUtilities.getNumMonths(UtilDateUtilities.calcDate(d.getYearOfBirth(),d.getMonthOfBirth(), d.getDateOfBirth()), Calendar.getInstance().getTime());
	}

	public static int getAgeInMonthsAsOf(Demographic d, Date asofDate) {
		return UtilDateUtilities.getNumMonths(UtilDateUtilities.calcDate(d.getYearOfBirth(), d.getMonthOfBirth(), d.getDateOfBirth()), asofDate);
	}

	public static int getAgeInYears(Demographic d) {
		return UtilDateUtilities.getNumYears(UtilDateUtilities.calcDate(d.getYearOfBirth(), d.getMonthOfBirth(), d.getDateOfBirth()), Calendar.getInstance().getTime());
	}

	public static int getAgeInYearsAsOf(Demographic d,Date asofDate) {
		return UtilDateUtilities.getNumYears(UtilDateUtilities.calcDate(d.getYearOfBirth(), d.getMonthOfBirth(), d.getDateOfBirth()), asofDate);
	}

	public static String getDob(Demographic d) {
		return addZero(d.getYearOfBirth(), 4) + addZero(d.getMonthOfBirth(), 2) + addZero(d.getDateOfBirth(), 2);
	}

	public static long getAgeInDays(Demographic d) {
		return UtilDateUtilities.getNumDays(UtilDateUtilities.calcDate(d.getYearOfBirth(), d.getMonthOfBirth(), d.getDateOfBirth()), Calendar.getInstance().getTime());
	}

	public static Date getDOBObj(Demographic d) {
		Date date = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			date = formatter.parse(addZero(d.getYearOfBirth(), 4) + "-" + addZero(d.getMonthOfBirth(), 2) + "-" + addZero(d.getDateOfBirth(), 2));
		} catch (Exception eg) {
			// this is okay, it means the date is not set or invalid data was set.
		}
		return date;
	}

	public static String getDob(Demographic d, String seperator) {
		if (d.getYearOfBirth() == null || d.getMonthOfBirth() == null || d.getDateOfBirth() == null) return (null);

		return d.getYearOfBirth() + seperator + d.getMonthOfBirth() + seperator + d.getDateOfBirth();
	}

	public static boolean isFemale(Demographic d) {
		boolean female = false;
		if (d.getSex() != null && d.getSex().trim().equalsIgnoreCase("f")) {
			female = true;
		}
		return female;
	}

	public static boolean isMale(Demographic d) {
		boolean male = false;
		if (d.getSex() != null && d.getSex().trim().equalsIgnoreCase("m")) {
			male = true;
		}
		return male;
	}

	public static String addZero(String text, int num) {
		if (text == null) return (null);

		text = text.trim();
		String zero = "0";
		for (int i = text.length(); i < num; i++) {
			text = zero + text;
		}
		return text;
	}





	public DemographicAddResult addDemographic(LoggedInInfo loggedInInfo, String title, String last_name, String first_name, String address,
			String city, String province, String postal, String phone, String phone2, String year_of_birth,
			String month_of_birth, String date_of_birth, String hin, String ver, String roster_status,
			String roster_date, String roster_termination_date, String roster_termination_reason,
			String patient_status, String patient_status_date, String date_joined, String chart_no,
			String official_lang,String spoken_lang, String provider_no, String sex, String end_date,
			String eff_date, String pcn_indicator, String hc_type, String hc_renew_date, String family_doctor,
			String email, String myOscarUserName, String alias, String previousAddress, String children,
			String sourceOfIncome, String citizenship, String sin) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Demographic demographic = new Demographic();
		demographic.setTitle(title);
		demographic.setLastName(last_name);
		demographic.setFirstName(first_name);
		demographic.setAddress(address);
		demographic.setCity(city);
		demographic.setProvince(province);
		demographic.setPostal(postal);
		demographic.setPhone(phone);
		demographic.setPhone2(phone2);
		demographic.setYearOfBirth(year_of_birth);
		demographic.setMonthOfBirth(month_of_birth);
		demographic.setDateOfBirth(date_of_birth);
		demographic.setHin(hin);
		demographic.setVer(ver);
		demographic.setRosterStatus(roster_status);
		if(roster_date != null && roster_date.length()>0) {
			demographic.setRosterDate(formatter.parse(roster_date));
		}
		if(roster_termination_date != null && roster_termination_date.length()>0) {
			demographic.setRosterTerminationDate(formatter.parse(roster_termination_date));
		}
		demographic.setRosterTerminationReason(roster_termination_reason);
		demographic.setPatientStatus(patient_status);
		if(patient_status_date != null && patient_status_date.length()>0) {
			demographic.setPatientStatusDate(formatter.parse(patient_status_date));
		}
		if(date_joined != null && date_joined.length()>0) {
			demographic.setDateJoined(formatter.parse(date_joined));
		}
		demographic.setChartNo(chart_no);
		demographic.setOfficialLanguage(official_lang);
		demographic.setSpokenLanguage(spoken_lang);
		demographic.setProviderNo(provider_no);
		demographic.setSex(sex);
		if(end_date != null && end_date.length()>0) {
			demographic.setEndDate(formatter.parse(end_date));
		}
		if(eff_date != null && eff_date.length()>0) {
			demographic.setEffDate(formatter.parse(eff_date));
		}
		demographic.setPcnIndicator(pcn_indicator);
		demographic.setHcType(hc_type);
		if(hc_renew_date != null && hc_renew_date.length()>0) {
			demographic.setHcRenewDate(formatter.parse(hc_renew_date));
		}
		demographic.setFamilyDoctor(family_doctor);
		demographic.setEmail(email);
		demographic.setMyOscarUserName(myOscarUserName);
		demographic.setAlias(alias);
		demographic.setPreviousAddress(previousAddress);
		demographic.setChildren(children);
		demographic.setSourceOfIncome(sourceOfIncome);
		demographic.setCitizenship(citizenship);
		demographic.setSin(sin);

		boolean duplicateRecord = false;
		DemographicAddResult ret = new DemographicAddResult();

		end_date = StringUtils.trimToNull(end_date);
		eff_date = StringUtils.trimToNull(eff_date);
		hc_renew_date = StringUtils.trimToNull(hc_renew_date);
		patient_status_date = StringUtils.trimToNull(patient_status_date);

		ArrayList<Demographic> demos = new ArrayList<Demographic>();
		if (hin != null && !hin.trim().equals("")) {
			demos = getDemographicWithHIN(loggedInInfo, hin);
		}

		if (demos.size() == 0) { // Unique HIN
			demos = null;
			demos = getDemographicWithLastFirstDOB(loggedInInfo, last_name, first_name, year_of_birth, month_of_birth, date_of_birth);
			if (demos.size() != 0) {
				duplicateRecord = true;
				ret.addWarning("Patient " + last_name + ", " + first_name + " DOB (" + year_of_birth + "-" + month_of_birth + "-" + date_of_birth + ") exists in database. Record not added");
			}
		} else { // Duplicate HIN
			for (int i = 0; i < demos.size(); i++) {
				Demographic d = demos.get(i);
				if (last_name.equalsIgnoreCase(d.getLastName()) && first_name.equalsIgnoreCase(d.getFirstName()) && year_of_birth != null && year_of_birth.equals(d.getYearOfBirth()) && month_of_birth != null && month_of_birth.equals(d.getMonthOfBirth()) && date_of_birth != null && date_of_birth.equals(d.getDateOfBirth())) {
					// DUP don't add

					duplicateRecord = true;
					ret.addWarning("Patient " + last_name + ", " + first_name + " DOB (" + year_of_birth + "-" + month_of_birth + "-" + date_of_birth + ") exists in database. Record not added");
				} else {// add without HIN

					ret.addWarning("Patient " + last_name + ", " + first_name + " DOB (" + year_of_birth + "-" + month_of_birth + "-" + date_of_birth + ") added without HIN - HIN already in use");
					hin = "";
				}
			}
		}

		demos = null;

		if (!duplicateRecord) {
			demographicManager.addDemographic(loggedInInfo, demographic);
			ret.setId(demographic.getDemographicNo().toString());
		}

		return ret;
	}

	public void addDemographiccust(String demoNo, String content) {
		DemographicCust demographicCust = new DemographicCust();
		demographicCust.setId(Integer.parseInt(demoNo));
		demographicCust.setAlert("");
		demographicCust.setMidwife("");
		demographicCust.setNurse("");
		demographicCust.setResident("");
		demographicCust.setNotes("<unotes>" + content + "</unotes>");
		demographicCustDao.persist(demographicCust);
	}

}
