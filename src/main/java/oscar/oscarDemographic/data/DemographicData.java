/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarDemographic.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class DemographicData {
	private static final Logger logger = MiscUtils.getLogger();

	public DemographicData() {
	}

	public String getDemographicFirstLastName(String demographicNo) {
		String fullName = "";
		logger.debug("test");
		MiscUtils.getLogger().debug("test");
		try {
			
			ResultSet rs;
			String sql = "SELECT first_name, last_name FROM demographic WHERE demographic_no = '" + demographicNo + "'";
			rs = DBHandler.GetSQL(sql);
			MiscUtils.getLogger().debug("sql: " + sql);

			if (rs.next()) {
				MiscUtils.getLogger().debug(oscar.Misc.getString(rs, "first_name"));
				fullName = oscar.Misc.getString(rs, "first_name") + " " + oscar.Misc.getString(rs, "last_name");
			}
			rs.close();

		} catch (SQLException sqe) {
			logger.error("Could not get demographic first/last name", sqe);
		}
		return fullName;
	}

	public Date getDemographicDOB(String demographicNo) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;

		try {
			
			ResultSet rs;
			String sql = "SELECT year_of_birth,month_of_birth,date_of_birth FROM demographic WHERE demographic_no = '" + demographicNo + "'";
			rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				try {
					date = (Date) formatter.parse(oscar.Misc.getString(rs, "year_of_birth") + "-" + oscar.Misc.getString(rs, "month_of_birth") + "-" + oscar.Misc.getString(rs, "date_of_birth"));
				} catch (Exception eg) {
				}
			}

			rs.close();

		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return date;
	}

	public String getDemographicNoByIndivoId(String pin) {
		String demographicNo = "";

		try {
			
			ResultSet rs;
			String sql = "SELECT demographic_no FROM demographic WHERE pin = '" + pin + "'";
			rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				demographicNo = oscar.Misc.getString(rs, "demographic_no");
			}
			rs.close();
			return demographicNo;
		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return demographicNo;
	}

	public String getDemoNoByNamePhoneEmail(String firstName, String lastName, String hPhone, String wPhone, String email) {
		String demographicNo = "";

		try {
			
			ResultSet rs;

			firstName = "first_name='" + firstName.trim() + "' ";
			lastName = lastName.trim().equals("") ? "" : "AND last_name='" + lastName.trim() + "' ";
			hPhone = hPhone.trim().equals("") ? "" : "AND (phone='" + hPhone.trim() + "') ";
			wPhone = wPhone.trim().equals("") ? "" : "AND (phone2='" + wPhone.trim() + "') ";
			email = email.trim().equals("") ? "" : "AND email='" + email.trim() + "'";

			String sql = "SELECT demographic_no FROM demographic WHERE " + firstName + lastName + hPhone + wPhone + email;

			rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				demographicNo = oscar.Misc.getString(rs, "demographic_no");
			}
			rs.close();
			return demographicNo;
		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return demographicNo;
	}

	// //
	public int numDemographicsWithHIN(String hin) {
		int num = 0;
		try {
			
			ResultSet rs;
			String sql = "SELECT count(*) as c FROM demographic WHERE hin = '" + hin + "'";
			rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				num = rs.getInt("c");
			}
			rs.close();
		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return num;
	}

	public boolean isUniqueHin(String hin) {
		return numDemographicsWithHIN(hin) == 0;
	}

	public ArrayList getDemographicWithHIN(String hin) {
		ArrayList list = new ArrayList();
		try {
			
			ResultSet rs;
			String sql = "SELECT demographic_no FROM demographic WHERE hin = '" + hin + "'";

			rs = DBHandler.GetSQL(sql);
			while (rs.next()) {
				String demoNo = oscar.Misc.getString(rs, "demographic_no");

				list.add(getDemographic(demoNo));
			}
			rs.close();
		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return list;
	}

	public ArrayList getDemographicWithLastFirstDOB(String lastname, String firstname, String dob) {
		if (dob!=null)
		{
			Date bDate = UtilDateUtilities.StringToDate(dob, "yyyy-MM-dd");
			String year_of_birth = UtilDateUtilities.DateToString(bDate, "yyyy");
			String month_of_birth = UtilDateUtilities.DateToString(bDate, "MM");
			String date_of_birth = UtilDateUtilities.DateToString(bDate, "dd");

			return getDemographicWithLastFirstDOB(lastname, firstname, year_of_birth, month_of_birth, date_of_birth);
		}
		else 
		{
			return(getDemographicWithLastFirstDOB(lastname, firstname, null,null,null));
		}
	}

	public ArrayList getDemographicWithLastFirstDOB(String lastname, String firstname, String year_of_birth, String month_of_birth, String date_of_birth) {
		ArrayList list = new ArrayList();
		try {
			
			ResultSet rs;
			String sql = "SELECT demographic_no FROM demographic " + " WHERE last_name like '" + lastname + "%' and first_name like '" + lastname + "%'";
			if (year_of_birth!=null) sql=sql+"  and year_of_birth = '" + year_of_birth + "'";
			if (month_of_birth!=null) sql=sql+" and month_of_birth = '" + month_of_birth + "'";
			if (date_of_birth!=null) sql=sql+" and date_of_birth = '" + date_of_birth + "'";
			
			rs = DBHandler.GetSQL(sql);
			while (rs.next()) {
				String demoNo = oscar.Misc.getString(rs, "demographic_no");

				list.add(getDemographic(demoNo));
			}
			rs.close();
		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return list;
	}

	public String getNameAgeString(String demographicNo) {
		String nameage = "";
		try {
			
			ResultSet rs;
			String sql = "SELECT last_name, first_name, year_of_birth,sex,month_of_birth,date_of_birth FROM demographic WHERE demographic_no = '" + demographicNo + "'";
			rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				String age = UtilDateUtilities.calcAge(UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"), oscar.Misc.getString(rs, "date_of_birth")));
				if (age == null) {
					age = "";
				}
				nameage = oscar.Misc.getString(rs, "last_name") + ", " + oscar.Misc.getString(rs, "first_name") + " " + oscar.Misc.getString(rs, "sex") + " " + age;
			}

			rs.close();

		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return nameage;
	}

	public String[] getNameAgeSexArray(String demographicNo) {
		String[] nameage = null;
		try {
			
			ResultSet rs;
			String sql = "SELECT last_name, first_name, year_of_birth,sex,month_of_birth,date_of_birth FROM demographic WHERE demographic_no = '" + demographicNo + "'";
			rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				String age = UtilDateUtilities.calcAge(UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"), oscar.Misc.getString(rs, "date_of_birth")));
				if (age == null) {
					age = "";
				}
				nameage = new String[] { oscar.Misc.getString(rs, "last_name"), oscar.Misc.getString(rs, "first_name"), oscar.Misc.getString(rs, "sex"), age };
			}

			rs.close();

		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return nameage;
	}

	public String getDemographicSex(String demographicNo) {
		String retval = "";
		try {
			
			ResultSet rs;
			String sql = "SELECT sex FROM demographic WHERE demographic_no = '" + demographicNo + "'";
			rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				try {
					retval = oscar.Misc.getString(rs, "sex");
				} catch (Exception eg) {
				}
			}

			rs.close();

		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return retval;
	}

	public Demographic getSubstituteDecisionMaker(String DemographicNo) {
		Demographic demographic = null;
		DemographicRelationship dr = new DemographicRelationship();
		String demoNo = dr.getSDM(DemographicNo);
		if (demoNo != null) {
			demographic = getDemographic(demoNo);
		}
		return demographic;
	}

	public Demographic getDemographic(String DemographicNo) {
		Demographic demographic = null;

		try {
			
			ResultSet rs;
			String sql = "SELECT * FROM demographic WHERE demographic_no = '" + DemographicNo + "'";

			rs = DBHandler.GetSQL(sql);

			if (rs.next()) {
				demographic = new Demographic(DemographicNo, oscar.Misc.getString(rs, "title"), oscar.Misc.getString(rs, "last_name"), oscar.Misc.getString(rs, "first_name"), oscar.Misc.getString(rs, "address"), oscar.Misc.getString(rs, "city"), oscar.Misc.getString(rs, "province"), oscar.Misc.getString(rs, "postal"), oscar.Misc.getString(rs, "phone"), oscar.Misc.getString(rs, "phone2"), oscar.Misc.getString(rs, "email"), oscar.Misc.getString(rs, "pin"), oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"), oscar.Misc.getString(rs, "date_of_birth"), oscar.Misc.getString(rs, "hin"), oscar.Misc.getString(rs, "ver"), oscar.Misc.getString(rs, "roster_status"), oscar.Misc.getString(rs, "patient_status"), oscar.Misc.getString(rs, "date_joined"), oscar.Misc.getString(rs, "chart_no"), oscar.Misc.getString(rs, "official_lang"), oscar.Misc.getString(rs, "spoken_lang"), oscar.Misc.getString(rs, "provider_no"), oscar.Misc.getString(rs, "sex"), oscar.Misc.getString(rs, "end_date"), oscar.Misc.getString(rs, "eff_date"), oscar.Misc.getString(rs, "pcn_indicator"), oscar.Misc.getString(rs, "hc_type"), oscar.Misc.getString(rs, "hc_renew_date"), oscar.Misc.getString(rs, "family_doctor"), oscar.Misc.getString(rs, "alias"), oscar.Misc.getString(rs, "previousAddress"), oscar.Misc.getString(rs, "children"), oscar.Misc.getString(rs, "sourceOfIncome"), oscar.Misc.getString(rs, "citizenship"), oscar.Misc.getString(rs, "sin"));
			}

			rs.close();

		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return demographic;
	}

	public String getDemographicNoByPIN(String pin) {
		String demographicNo = "";

		try {
			
			ResultSet rs;
			String sql = "SELECT demographic_no FROM demographic WHERE pin = '" + pin + "'";
			rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				demographicNo = oscar.Misc.getString(rs, "demographic_no");
			}
			rs.close();
			return demographicNo;
		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return demographicNo;
	}

	public String getDemographicDateJoined(String DemographicNo) {
		String date = null;
		try {
			
			ResultSet rs;
			String sql = "SELECT date_joined FROM demographic WHERE demographic_no = '" + DemographicNo + "'";

			rs = DBHandler.GetSQL(sql);

			if (rs.next()) {
				date = oscar.Misc.getString(rs, "date_joined");// getString("date_joined");
			}

			rs.close();

		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return date;
	}

	public void setDemographicPin(String demographicNo, String pin) throws Exception {
		
		String sql = "UPDATE demographic SET pin = '" + pin + "' WHERE demographic_no = " + demographicNo;
		DBHandler.RunSQL(sql);
	}

	public void setDemographic(Demographic dm) throws Exception {
		if (dm.getDemographicNo()==null || dm.getDemographicNo().trim().equals("")) return;

		
		String sql = "UPDATE demographic SET " +
						"title = '" + dm.getTitle() + "', " +
						"last_name = '" + dm.getLastName() + "', " +
						"first_name = '" + dm.getFirstName() + "', " +
						"address = '" + dm.getAddress() + "', " +
						"city = '" + dm.getCity() + "', " +
						"province = '" + dm.getProvince() + "', " +
						"postal = '" + dm.getPostal() + "', " +
						"phone = '" + dm.getPhone() + "', " +
						"phone2 = '" + dm.getPhone2() + "', " +
						"email = '" + dm.getEmail() + "', " +
						"pin = '" + dm.getPin() + "', " +
						"year_of_birth = '" + dm.getYearOfBirth() + "', " +
						"month_of_birth = '" + dm.getMonthOfBirth() + "', " +
						"date_of_birth = '" + dm.getDateOfBirth() + "', " +
						"hin = '" + dm.getHIN() + "', " +
						"ver = '" + dm.getVersionCode() + "', " +
						"roster_status = '" + dm.getRosterStatus() + "', " +
						"patient_status = '" + dm.getPatientStatus() + "', " +
						"date_joined = '" + dm.getDateJoined() + "', " +
						"chart_no = '" + dm.getChartNo() + "', " +
						"official_lang = '" + dm.getOfficialLang() + "', " +
						"spoken_lang = '" + dm.getSpokenLang() + "', " +
						"provider_no = '" + dm.getProviderNo() + "', " +
						"sex = '" + dm.getSex() + "', " +
						"end_date = '" + dm.getEndDate() + "', " +
						"eff_date = '" + dm.getEffDate() + "', " +
						"pcn_indicator = '" + dm.getPCNindicator() + "', " +
						"hc_type = '" + dm.getHCType() + "', " +
						"hc_renew_date = '" + dm.getHCRenewDate() + "', " +
						"family_doctor = '" + dm.getFamilyDoctor() + "', " +
						"alias = '" + dm.getAlias() + "', " +
						"previousAddress = '" + dm.getPreviousAddress() + "', " +
						"children = '" + dm.getChildren() + "', " +
						"sourceOfIncome = '" + dm.getSourceOfIncome() + "', " +
						"citizenship = '" + dm.getCitizenship() + "', " +
						"sin = '" + dm.getSin() + "' " +
					"WHERE demographic_no = " + dm.getDemographicNo();
		DBHandler.RunSQL(sql);
	}

	public class Demographic {

		protected String demographic_no;
		protected String title;
		protected String last_name;
		protected String first_name;
		protected String address;
		protected String city;
		protected String province;
		protected String postal;
		protected String phone;
		protected String phone2;
		protected String email;
		protected String pin;
		protected String year_of_birth;
		protected String month_of_birth;
		protected String date_of_birth;
		protected String hin;
		protected String ver;
		protected String roster_status;
		protected String patient_status;
		protected String date_joined;
		protected String chart_no;
		protected String official_lang;
		protected String spoken_lang;
		protected String provider_no;
		protected String sex;
		protected String end_date;
		protected String eff_date;
		protected String pcn_indicator;
		protected String hc_type;
		protected String hc_renew_date;
		protected String family_doctor;
		protected String alias;
		protected String previousAddress;
		protected String children;
		protected String sourceOfIncome;
		protected String citizenship;
		protected String sin;

		public RxInformation RxInfo;
		public EctInformation EctInfo;

		public Demographic(String DemographicNo) {
			init(DemographicNo);
		}

		protected Demographic(String DemographicNo, String title, String last_name, String first_name, String address, String city, String province, String postal, String phone, String phone2, String email, String pin, String year_of_birth, String month_of_birth, String date_of_birth, String hin, String ver, String roster_status, String patient_status, String date_joined, String chart_no, String official_lang, String spoken_lang, String provider_no, String sex, String end_date, String eff_date,
		        String pcn_indicator, String hc_type, String hc_renew_date, String family_doctor, String alias, String previousAddress, String children, String sourceOfIncome, String citizenship, String sin) {
			this.demographic_no = DemographicNo;
			this.title = title;
			this.last_name = last_name;
			this.first_name = first_name;
			this.address = address;
			this.city = city;
			this.province = province;
			this.postal = postal;
			this.phone = phone;
			this.phone2 = phone2;
			this.email = email;
			this.pin = pin;
			this.year_of_birth = year_of_birth;
			this.month_of_birth = month_of_birth;
			this.date_of_birth = date_of_birth;
			this.hin = hin;
			this.ver = ver;
			this.roster_status = roster_status;
			this.patient_status = patient_status;
			this.date_joined = date_joined;
			this.chart_no = chart_no;
			this.official_lang = official_lang;
			this.spoken_lang = spoken_lang;
			this.provider_no = provider_no;
			this.sex = sex;
			this.end_date = end_date;
			this.eff_date = eff_date;
			this.pcn_indicator = pcn_indicator;
			this.hc_type = hc_type;
			this.hc_renew_date = hc_renew_date;
			this.family_doctor = family_doctor;
			this.alias = alias;
			this.previousAddress = previousAddress;
			this.children = children;
			this.sourceOfIncome = sourceOfIncome;
			this.citizenship = citizenship;
			this.sin = sin;

			this.RxInfo = getRxInformation();
			this.EctInfo = getEctInformation();
		}

		private void init(String DemographicNo) {

			try {
				
				ResultSet rs;
				String sql = "SELECT * FROM demographic WHERE demographic_no = '" + DemographicNo + "'";

				rs = DBHandler.GetSQL(sql);

				if (rs.next()) {
					this.demographic_no = DemographicNo;
					this.title = oscar.Misc.getString(rs, "title");
					this.last_name = oscar.Misc.getString(rs, "last_name");
					this.first_name = oscar.Misc.getString(rs, "first_name");
					this.address = oscar.Misc.getString(rs, "address");
					this.city = oscar.Misc.getString(rs, "city");
					this.province = oscar.Misc.getString(rs, "province");
					this.postal = oscar.Misc.getString(rs, "postal");
					this.phone = oscar.Misc.getString(rs, "phone");
					this.phone2 = oscar.Misc.getString(rs, "phone2");
					this.email = oscar.Misc.getString(rs, "email");
					this.pin = oscar.Misc.getString(rs, "pin");
					this.year_of_birth = oscar.Misc.getString(rs, "year_of_birth");
					this.month_of_birth = oscar.Misc.getString(rs, "month_of_birth");
					this.date_of_birth = oscar.Misc.getString(rs, "date_of_birth");
					this.hin = oscar.Misc.getString(rs, "hin");
					this.ver = oscar.Misc.getString(rs, "ver");
					this.roster_status = oscar.Misc.getString(rs, "roster_status");
					this.patient_status = oscar.Misc.getString(rs, "patient_status");
					this.date_joined = oscar.Misc.getString(rs, "date_joined");
					this.chart_no = oscar.Misc.getString(rs, "chart_no");
					this.official_lang = oscar.Misc.getString(rs, "official_lang");
					this.spoken_lang = oscar.Misc.getString(rs, "spoken_lang");
					this.provider_no = oscar.Misc.getString(rs, "provider_no");
					this.sex = oscar.Misc.getString(rs, "sex");
					this.end_date = oscar.Misc.getString(rs, "end_date");
					this.eff_date = oscar.Misc.getString(rs, "eff_date");
					this.pcn_indicator = oscar.Misc.getString(rs, "pcn_indicator");
					this.hc_type = oscar.Misc.getString(rs, "hc_type");
					this.hc_renew_date = oscar.Misc.getString(rs, "hc_renew_date");
					this.family_doctor = oscar.Misc.getString(rs, "family_doctor");
					this.alias = oscar.Misc.getString(rs, "alias");
					this.previousAddress = oscar.Misc.getString(rs, "previousAddress");
					this.children = oscar.Misc.getString(rs, "children");
					this.sourceOfIncome = oscar.Misc.getString(rs, "sourceOfIncome");
					this.citizenship = oscar.Misc.getString(rs, "citizenship");
					this.sin = oscar.Misc.getString(rs, "sin");
				}

				rs.close();
				this.RxInfo = getRxInformation();
				this.EctInfo = getEctInformation();

			} catch (SQLException e) {
				MiscUtils.getLogger().error("Error", e);
			}
		}

		public String getDemographicNo() {
			return demographic_no;
		}

		public void setDemographicNo(String data) {
			demographic_no = data;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String data) {
			title = data;
		}

		public String getLastName() {
			return last_name;
		}

		public void setLastName(String data) {
			last_name = data;
		}

		public String getFirstName() {
			return first_name;
		}

		public void setFirstName(String data) {
			first_name = data;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String data) {
			address = data;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String data) {
			city = data;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String data) {
			province = data;
		}

		public String getPostal() {
			return postal;
		}

		public void setPostal(String data) {
			postal = data;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String data) {
			phone = data;
		}

		public String getPhone2() {
			return phone2;
		}

		public void setPhone2(String data) {
			phone2 = data;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String data) {
			email = data;
		}

		public String getPin() {
			return pin;
		}

		public void setPin(String data) {
			pin = data;
		}

		public String getIndivoId() {
			return pin;
		}

		public String getAge() {
			if (oscar.util.StringUtils.empty(year_of_birth) ||
				oscar.util.StringUtils.empty(month_of_birth) ||
				oscar.util.StringUtils.empty(date_of_birth)) {
				return "";
			}
			return (String.valueOf(oscar.util.UtilDateUtilities.calcAge(year_of_birth, month_of_birth, date_of_birth)));
		}

		public String getAgeAsOf(Date asofDate) {
			return UtilDateUtilities.calcAgeAtDate(UtilDateUtilities.calcDate(year_of_birth, month_of_birth, date_of_birth), asofDate);
		}

		public int getAgeInMonths() {
			return UtilDateUtilities.getNumMonths(UtilDateUtilities.calcDate(year_of_birth, month_of_birth, date_of_birth), Calendar.getInstance().getTime());
		}

		public int getAgeInMonthsAsOf(Date asofDate) {
			return UtilDateUtilities.getNumMonths(UtilDateUtilities.calcDate(year_of_birth, month_of_birth, date_of_birth), asofDate);
		}

		public int getAgeInYears() {
			return UtilDateUtilities.getNumYears(UtilDateUtilities.calcDate(year_of_birth, month_of_birth, date_of_birth), Calendar.getInstance().getTime());
		}

		public int getAgeInYearsAsOf(Date asofDate) {
			return UtilDateUtilities.getNumYears(UtilDateUtilities.calcDate(year_of_birth, month_of_birth, date_of_birth), asofDate);
		}

		public String getDob() {
			return addZero(year_of_birth, 4) + addZero(month_of_birth, 2) + addZero(date_of_birth, 2);
		}

		public long getAgeInDays() {
			return UtilDateUtilities.getNumDays(UtilDateUtilities.calcDate(year_of_birth, month_of_birth, date_of_birth), Calendar.getInstance().getTime());
		}

		public Date getDOBObj() {
			Date date = null;
			try {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				date = formatter.parse(addZero(year_of_birth, 4) + "-" + addZero(month_of_birth, 2) + "-" + addZero(date_of_birth, 2));
			} catch (Exception eg) {
				// this is okay, it means the date is not set or invalid data was set.
			}
			return date;
		}

		public String getDob(String seperator) {
			if (getYearOfBirth()==null || getMonthOfBirth()==null || getDateOfBirth()==null) return(null);
			
			return this.getYearOfBirth() + seperator + this.getMonthOfBirth() + seperator + this.getDateOfBirth();
		}

		public String getYearOfBirth() {
			return addZero(year_of_birth, 4);
		}

		public void setYearOfBirth(String data) {
			year_of_birth = data;
		}

		public String getMonthOfBirth() {
			return addZero(month_of_birth, 2);
		}

		public void setMonthOfBirth(String data) {
			month_of_birth = data;
		}

		public String getDateOfBirth() {
			return addZero(date_of_birth, 2);
		}

		public void setDateOfBirth(String data) {
			date_of_birth = data;
		}

		public String getHIN() {
			return hin + ver;
		}

		public String getJustHIN() {
			return hin;
		}

		public void setJustHIN(String data) {
			hin = data;
		}

		public String getVersionCode() {
			return ver;
		}

		public void setVersionCode(String data) {
			ver = data;
		}

		public String getRosterStatus() {
			return roster_status;
		}

		public void setRosterStatus(String data) {
			roster_status = data;
		}

		public String getPatientStatus() {
			return patient_status;
		}

		public void setPatientStatus(String data) {
			patient_status = data;
		}

		public String getDateJoined() {
			return date_joined;
		}

		public void setDateJoined(String data) {
			date_joined = data;
		}

		public String getChartNo() {
			return chart_no;
		}

		public void setChartNo(String data) {
			chart_no = data;
		}

		public String getOfficialLang() {
			return official_lang;
		}

		public void setOfficialLang(String data) {
			official_lang = data;
		}

		public String getSpokenLang() {
			return spoken_lang;
		}

		public void setSpokenLang(String data) {
			spoken_lang = data;
		}

		public String getProviderNo() {
			return provider_no;
		}

		public void setProviderNo(String data) {
			provider_no = data;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String data) {
			sex = data;
		}

		public boolean isFemale() {
			boolean female = false;
			if (sex != null && sex.trim().equalsIgnoreCase("f")) {
				female = true;
			}
			return female;
		}

		public boolean isMale() {
			boolean male = false;
			if (sex != null && sex.trim().equalsIgnoreCase("m")) {
				male = true;
			}
			return male;
		}

		public String getEndDate() {
			return end_date;
		}

		public void setEndDate(String data) {
			end_date = data;
		}

		public String getEffDate() {
			return eff_date;
		}

		public void setEffDate(String data) {
			eff_date = data;
		}

		public String getPCNindicator() {
			return pcn_indicator;
		}

		public void setPCNindicator(String data) {
			pcn_indicator = data;
		}

		public String getHCType() {
			return hc_type;
		}

		public void setHCType(String data) {
			hc_type = data;
		}

		public String getHCRenewDate() {
			return hc_renew_date;
		}

		public void setHCRenewDate(String data) {
			hc_renew_date = data;
		}

		public String getFamilyDoctor() {
			return family_doctor;
		}

		public void setFamilyDoctor(String data) {
			family_doctor = data;
		}

		public String getAlias() {
			return alias;
		}

		public void setAlias(String data) {
			alias = data;
		}

		public String getChildren() {
			return children;
		}

		public void setChildren(String data) {
			children = data;
		}

		public String getCitizenship() {
			return citizenship;
		}

		public void setCitizenship(String data) {
			citizenship = data;
		}

		public String getPreviousAddress() {
			return previousAddress;
		}

		public void setPreviousAddress(String data) {
			previousAddress = data;
		}

		public String getSin() {
			return sin;
		}

		public void setSin(String data) {
			sin = data;
		}

		public String getSourceOfIncome() {
			return sourceOfIncome;
		}

		public void setSourceOfIncome(String data) {
			sourceOfIncome = data;
		}

		public String addZero(String text, int num) {
			if (text==null) return(null);
			
			text = text.trim();
			String zero = "0";
			for (int i = text.length(); i < num; i++) {
				text = zero + text;
			}
			return text;
		}

		public RxInformation getRxInformation() {
			return new RxInformation();
		}

		public EctInformation getEctInformation() {
			return new EctInformation();
		}

		public class RxInformation {
			private String currentMedication;
			private String allergies;

			public String getCurrentMedication() {
				oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
				oscar.oscarRx.data.RxPrescriptionData.Prescription[] arr = {};
				arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demographic_no));
				StringBuilder stringBuffer = new StringBuilder();
				for (int i = 0; i < arr.length; i++) {
					if (arr[i].isCurrent()) {

						stringBuffer.append(arr[i].getFullOutLine().replaceAll(";", " ") + "\n");
						// stringBuffer.append(arr[i].getRxDisplay()+"\n");
					}
				}
				this.currentMedication = stringBuffer.toString();
				return this.currentMedication;
			}

			public String getAllergies() {
				try {
					oscar.oscarRx.data.RxPatientData pData = new oscar.oscarRx.data.RxPatientData();
					oscar.oscarRx.data.RxPatientData.Patient patient = pData.getPatient(Integer.parseInt(demographic_no));
					oscar.oscarRx.data.RxPatientData.Patient.Allergy[] allergies = {};
					allergies = patient.getAllergies();
					StringBuilder stringBuffer = new StringBuilder();
					for (int i = 0; i < allergies.length; i++) {
						oscar.oscarRx.data.RxAllergyData.Allergy allerg = allergies[i].getAllergy();
						stringBuffer.append(allerg.getDESCRIPTION() + "  " + allerg.getTypeDesc() + " \n");
					}
					this.allergies = stringBuffer.toString();
				} catch (SQLException e) {
					MiscUtils.getLogger().error("Error", e);
				}
				return this.allergies;
			}
		}

		public class EctInformation {

			private oscar.oscarEncounter.data.EctPatientData.Patient patient;
			private oscar.oscarEncounter.data.EctPatientData.Patient.eChart eChart;

			EctInformation() {
				init();
			}

			private void init() {
				try {
					oscar.oscarEncounter.data.EctPatientData patientData = new oscar.oscarEncounter.data.EctPatientData();
					this.patient = patientData.getPatient(demographic_no);
					this.eChart = patient.getEChart();
				} catch (SQLException e) {
					MiscUtils.getLogger().error("Error", e);
				}
			}

			public Date getEChartTimeStamp() {
				return eChart.getEChartTimeStamp();
			}

			public String getSocialHistory() {
				return eChart.getSocialHistory();
			}

			public String getFamilyHistory() {
				return eChart.getFamilyHistory();
			}

			public String getMedicalHistory() {
				return eChart.getMedicalHistory();
			}

			public String getOngoingConcerns() {
				return eChart.getOngoingConcerns();
			}

			public String getReminders() {
				return eChart.getReminders();
			}

			public String getEncounter() {
				return eChart.getEncounter();
			}

			public String getSubject() {
				return eChart.getSubject();
			}
		}
	}

	// //////////////
	String add_record_string = "insert into demographic (title,last_name,first_name,address,city,province,postal,phone,phone2,email," + "pin,year_of_birth,month_of_birth,date_of_birth,hin,ver,roster_status,patient_status,date_joined,chart_no," + "official_lang,spoken_lang,provider_no,sex,end_date,eff_date,pcn_indicator,hc_type,hc_renew_date," + "family_doctor,alias,previousAddress,children,sourceOfIncome,citizenship,sin) " + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public DemographicAddResult addDemographic(String title, String last_name, String first_name, String address, String city,
			String province, String postal, String phone, String phone2, String year_of_birth, String month_of_birth,
			String date_of_birth, String hin, String ver, String roster_status, String patient_status, String date_joined,
			String chart_no, String official_lang, String spoken_lang, String provider_no, String sex, String end_date,
			String eff_date, String pcn_indicator, String hc_type, String hc_renew_date, String family_doctor, String email,
			String pin, String alias, String previousAddress, String children, String sourceOfIncome, String citizenship, 
			String sin) {
		
		boolean duplicateRecord = false;
		DemographicAddResult ret = new DemographicAddResult();

		// "insert into demographic (last_name, first_name, address, city, province, postal, phone, phone2, email, pin, year_of_birth, month_of_birth, date_of_birth, hin, ver, roster_status, patient_status, date_joined, chart_no, provider_no, sex, end_date, eff_date, pcn_indicator, hc_type, hc_renew_date, family_doctor) values(?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?)"
		// },

		end_date=StringUtils.trimToNull(end_date);
		eff_date=StringUtils.trimToNull(eff_date);
		hc_renew_date=StringUtils.trimToNull(hc_renew_date);

		ArrayList demos = new ArrayList();
		if (hin != null && !hin.trim().equals("")) {
			demos = getDemographicWithHIN(hin);
		}

		if (demos.size() == 0) { // Unique HIN
			demos = null;
			demos = getDemographicWithLastFirstDOB(last_name, first_name, year_of_birth, month_of_birth, date_of_birth);
			if (demos.size() != 0) {
				duplicateRecord = true;
				ret.addWarning("Patient " + last_name + ", " + first_name + " DOB (" + year_of_birth + "-" + month_of_birth + "-" + date_of_birth + ") exists in database. Record not added");
			}
		} else { // Duplicate HIN
			for (int i = 0; i < demos.size(); i++) {
				Demographic d = (Demographic) demos.get(i);
				if (last_name.equalsIgnoreCase(d.getLastName()) && first_name.equalsIgnoreCase(d.getFirstName()) && year_of_birth!=null && year_of_birth.equals(d.getYearOfBirth()) && month_of_birth!=null && month_of_birth.equals(d.getMonthOfBirth()) && date_of_birth!=null && date_of_birth.equals(d.getDateOfBirth())) {
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
			try {
				
				Connection conn = DbConnectionFilter.getThreadLocalDbConnection();

				PreparedStatement add_record = conn.prepareStatement(add_record_string);
				add_record.setString(1, title);
				add_record.setString(2, last_name);
				add_record.setString(3, first_name);
				add_record.setString(4, address);
				add_record.setString(5, city);
				add_record.setString(6, province);
				add_record.setString(7, postal);
				add_record.setString(8, phone);
				add_record.setString(9, phone2);
				add_record.setString(10, email);
				add_record.setString(11, "");
				add_record.setString(12, year_of_birth);
				add_record.setString(13, month_of_birth);
				add_record.setString(14, date_of_birth);
				add_record.setString(15, hin);
				add_record.setString(16, ver);
				add_record.setString(17, roster_status);
				add_record.setString(18, patient_status);
				add_record.setString(19, date_joined);
				add_record.setString(20, chart_no);
				add_record.setString(21, official_lang);
				add_record.setString(22, spoken_lang);
				add_record.setString(23, provider_no);
				add_record.setString(24, sex);
				add_record.setString(25, end_date);
				add_record.setString(26, eff_date);
				add_record.setString(27, pcn_indicator);
				add_record.setString(28, hc_type);
				add_record.setString(29, hc_renew_date);
				add_record.setString(30, family_doctor);
				add_record.setString(31, alias);
				add_record.setString(32, previousAddress);
				add_record.setString(33, children);
				add_record.setString(34, sourceOfIncome);
				add_record.setString(35, citizenship);
				add_record.setString(36, sin);

				String outString = add_record.toString();
				int firstmark = outString.indexOf(':');

				add_record.executeUpdate();
				// conn.createStatement().execute(sql
				ResultSet rs = add_record.getGeneratedKeys();
				if (rs.next()) {
					ret.setAdded(true);
					ret.setId("" + rs.getInt(1));
				}
				add_record.close();
				rs.close();
			} catch (Exception e) {
				MiscUtils.getLogger().debug("LOG ADD RECORD " + chart_no);
				MiscUtils.getLogger().error("Error", e);
				ret = null;
			}
		} else {
			MiscUtils.getLogger().debug("NOT ADDED");
		}

		return ret;
	}

	public void addDemographiccust(String demoNo, String content) {
		String sql = "INSERT INTO demographiccust VALUES ('" + demoNo + "','','','','','<unotes>" + content + "</unotes>')";
		DBHandler db;
		try {
			
			DBHandler.RunSQL(sql);
		} catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
		}
	}

	public class DemographicAddResult {
		ArrayList warnings = null;
		boolean added = false;
		String id = null;

		public void addWarning(String str) {

			if (warnings == null) {
				warnings = new ArrayList();
			}
			warnings.add(str);
		}

		public String[] getWarnings() {
			String[] s = {};
			if (warnings != null) {
				s = (String[]) warnings.toArray(s);
			}
			return s;
		}

		public ArrayList getWarningsCollection() {
			if (warnings == null) {
				warnings = new ArrayList();
			}
			return warnings;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public boolean wasAdded() {
			return added;
		}

		public void setAdded(boolean b) {
			added = b;
		}
	}

	// //////////////

}
