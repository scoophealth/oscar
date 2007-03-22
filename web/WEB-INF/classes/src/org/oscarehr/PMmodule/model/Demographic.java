/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.oscarehr.PMmodule.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.model.base.BaseDemographic;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.PMmodule.utility.Utility;

/**
 * This is the object class that relates to the demographic table. Any customizations belong here.
 */
public class Demographic extends BaseDemographic {

	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_MONTH = "01";
	private static final String DEFAULT_DATE = "01";
	private static final String DEFAULT_YEAR = "1900";
	private static final String DEFAULT_SEX = "D";
	private static final String DEFAULT_PATIENT_STATUS = "AC";
	private static final String DEFAULT_FUTURE_DATE = "2100-01-01";
	
	private static final String SEPERATOR = "-";

	public static Demographic create(String firstName, String lastName, String monthOfBirth, String dateOfBirth, String yearOfBirth, String hin, String ver) {
		Demographic demographic = new Demographic();
		
		demographic.setFirstName(firstName);
		demographic.setLastName(lastName);
		demographic.setMonthOfBirth(monthOfBirth != null && monthOfBirth.length() > 0 ? monthOfBirth : DEFAULT_MONTH);
		demographic.setDateOfBirth(dateOfBirth != null && dateOfBirth.length() > 0 ? dateOfBirth : DEFAULT_DATE);
		demographic.setYearOfBirth(yearOfBirth != null && yearOfBirth.length() > 0 ? yearOfBirth : DEFAULT_YEAR);
		demographic.setHin(hin);
		demographic.setVer(ver);
		
		demographic.setChartNo(StringUtils.EMPTY);
		demographic.setFamilyDoctor(StringUtils.EMPTY);
		demographic.setHcType(StringUtils.EMPTY);
		demographic.setPcnIndicator(StringUtils.EMPTY);
		demographic.setPatientStatus(DEFAULT_PATIENT_STATUS);
		demographic.setPin(StringUtils.EMPTY);
		demographic.setRosterStatus(StringUtils.EMPTY);
		demographic.setSex(DEFAULT_SEX);
		
		demographic.setDateJoined(DateTimeFormatUtils.getToday());
		demographic.setEffDate(DateTimeFormatUtils.getToday());
		demographic.setEndDate(DateTimeFormatUtils.getDateFromString(DEFAULT_FUTURE_DATE));
		demographic.setHcRenewDate(DateTimeFormatUtils.getDateFromString(DEFAULT_FUTURE_DATE));
		
		demographic.setAddress(StringUtils.EMPTY);
		demographic.setCity(StringUtils.EMPTY);
		demographic.setProvince(StringUtils.EMPTY);
		demographic.setPostal(StringUtils.EMPTY);
		demographic.setEmail(StringUtils.EMPTY);
		demographic.setPhone(StringUtils.EMPTY);
		demographic.setPhone2(StringUtils.EMPTY);
		
		return demographic;
	}
	
	private long agencyId;
	private String links;
	private DemographicExt[] extras;
	
	/* [CONSTRUCTOR MARKER END] */

	public Demographic() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Demographic(java.lang.Integer _demographicNo) {
		super(_demographicNo);
	}

	/**
	 * Constructor for required fields
	 */
	public Demographic(java.lang.Integer _demographicNo, java.lang.String _firstName, java.lang.String _lastName) {
		super(_demographicNo, _firstName, _lastName);
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	
	@Override
	protected void initialize() {
		links = StringUtils.EMPTY;
	}

	public String addZero(String text, int num) {
		text = text.trim();
		
		for (int i = text.length(); i < num; i++) {
			text = "0" + text;
		}
		
		return text;
	}

	public String getAge() {
		return (String.valueOf(Utility.calcAge(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), DEFAULT_YEAR), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), DEFAULT_MONTH), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), DEFAULT_DATE))));
	}

	public String getAgeAsOf(Date asofDate) {
		return Utility.calcAgeAtDate(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), DEFAULT_YEAR), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), DEFAULT_MONTH), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), DEFAULT_DATE)), asofDate);
	}

	public int getAgeInYears() {
		return Utility.getNumYears(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), DEFAULT_YEAR), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), DEFAULT_MONTH), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), DEFAULT_DATE)), Calendar.getInstance().getTime());
	}

	public int getAgeInYearsAsOf(Date asofDate) {
		return Utility.getNumYears(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), DEFAULT_YEAR), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), DEFAULT_MONTH), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), DEFAULT_DATE)), asofDate);
	}

	public long getAgencyId() {
		return agencyId;
	}

	public String getAgencyName() {
		return Agency.getAgencyName(agencyId);
	}

	public DemographicExt[] getExtras() {
		return extras;
	}

	public String getFormattedDob() {
		return this.getYearOfBirth() + SEPERATOR + this.getMonthOfBirth() + SEPERATOR + this.getDateOfBirth();
	}
	
	public String getFormattedDob(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, new Integer(getYearOfBirth()).intValue());
		cal.set(Calendar.MONTH, new Integer(getMonthOfBirth()).intValue()-1);
		cal.set(Calendar.DAY_OF_MONTH, new Integer(getDateOfBirth()).intValue());		
		return formatter.format(cal.getTime());		
	}
	
	public void setFormattedDob(String format,String value) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			Date d = formatter.parse(value);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			setYearOfBirth(String.valueOf(cal.get(Calendar.YEAR)));
			String month = String.valueOf(cal.get(Calendar.MONTH)+1);
			if(month.length()==1) {
				month = "0" + month;
			}
			setMonthOfBirth(month);
			String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			if(day.length()==1) {
				day = "0" + day;
			}
			setDateOfBirth(day);
		}catch(Exception e){}
	}

	public String getFormattedLinks() {
		StringBuilder response = new StringBuilder();

		if (getNumLinks() > 0) {
			String[] links = getLinks().split(",");
			for (int x = 0; x < links.length; x++) {
				String components[] = links[x].split("/");
				String agencyId = components[0];
				if (response.length() > 0) {
					response.append(",");
				}
				response.append(Agency.getAgencyName(Long.valueOf(agencyId)));

			}
		}

		return response.toString();
	}

	public String getFormattedName() {
		return getLastName() + ", " + getFirstName();
	}

	public String getLinks() {
		return links;
	}

	public int getNumLinks() {
		if (getLinks() == null) {
			return 0;
		}

		if (getLinks().equals("")) {
			return 0;
		}

		return getLinks().split(",").length;
	}

	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}

	public void setExtras(DemographicExt[] extras) {
		this.extras = extras;
	}

	public void setLinks(String links) {
		this.links = links;
	}

}