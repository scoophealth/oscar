/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */


package org.oscarehr.common.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.PMmodule.utility.Utility;
import org.oscarehr.util.MiscUtils;

/**
 * This is the object class that relates to the demographic table. Any customizations belong here.
 */
public class Demographic implements Serializable {

	private static final String DEFAULT_MONTH = "01";
	private static final String DEFAULT_DATE = "01";
	private static final String DEFAULT_YEAR = "1900";
	private static final String DEFAULT_SEX = "M";
	private static final String DEFAULT_PATIENT_STATUS = PatientStatus.AC.name();
	private static final String DEFAULT_HEATH_CARD_TYPE = "ON";
	private static final String DEFAULT_FUTURE_DATE = "2100-01-01";
	public static final String ANONYMOUS = "ANONYMOUS";
	public static final String UNIQUE_ANONYMOUS = "UNIQUE_ANONYMOUS";
	
	private final static Pattern FD_LAST_NAME = Pattern.compile(".*<rd>([^,]*),.*</rd>.*");
	private final static Pattern FD_FIRST_NAME = Pattern.compile(".*<rd>[^,]*,(.*)</rd>.*");
	private final static Pattern FD_OHIP = Pattern.compile("<rdohip>(.*)</rdohip>.*");
	
	
	private int hashCode = Integer.MIN_VALUE;// primary key
	private Integer demographicNo;// fields
	private String phone;
	private String patientStatus;
	private Date patientStatusDate;
	private String rosterStatus;
	private String providerNo;
	private String myOscarUserName;
	private String hin;
	private String address;
	private String province;
	private String monthOfBirth;
	private String ver;
	private String dateOfBirth;
	private String sex;
	private String sexDesc;
	private Date dateJoined;
	private String familyDoctor;
	private String city;
	private String firstName;
	private String postal;
	private Date hcRenewDate;
	private String phone2;
	private String pcnIndicator;
	private Date endDate;
	private String lastName;
	private String hcType;
	private String chartNo;
	private String email;
	private String yearOfBirth;
	private Date effDate;
	private Date rosterDate;
	private Date rosterTerminationDate;
	private String rosterTerminationReason;
	private String links;
	private DemographicExt[] extras;
	private String alias;
	private String previousAddress;
	private String children;
	private String sourceOfIncome;
	private String citizenship;
	private String sin;
	private Integer headRecord = null;
	private Set<Integer> subRecord = null;
	private String anonymous = null;
	private String spokenLanguage;

	private int activeCount = 0;
	private int hsAlertCount = 0;
	private String displayName=null;

	private Provider provider;
	private String lastUpdateUser = null;
	private Date lastUpdateDate = new Date();

	private String title;
	private String officialLanguage;

    private String countryOfOrigin;
    private String newsletter;
    

        public String getTitle() {
        	return title;
        }

		public void setTitle(String title) {
        	this.title = title;
        }

		public String getOfficialLanguage() {
        	return officialLanguage;
        }

		public void setOfficialLanguage(String officialLanguage) {
        	this.officialLanguage = officialLanguage;
        }

		public String getLastUpdateUser() {
        	return lastUpdateUser;
        }

		public void setLastUpdateUser(String lastUpdateUser) {
        	this.lastUpdateUser = lastUpdateUser;
        }

		public Date getLastUpdateDate() {
        	return lastUpdateDate;
        }

		public void setLastUpdateDate(Date lastUpdateDate) {
        	this.lastUpdateDate = lastUpdateDate;
        }

	/**
     * @return the rosterDate
     */
    public Date getRosterDate() {
        return rosterDate;
    }

    /**
     * @param rosterDate the rosterDate to set
     */
    public void setRosterDate(Date rosterDate) {
        this.rosterDate = rosterDate;
    }

    public Date getRosterTerminationDate() {
        return rosterTerminationDate;
    }

    public void setRosterTerminationDate(Date rosterTermDate) {
        this.rosterTerminationDate = rosterTermDate;
    }

    public String getRosterTerminationReason() {
    	return rosterTerminationReason;
    }

    public void setRosterTerminationReason(String rosterTermReason) {
    	this.rosterTerminationReason = rosterTermReason;
    }

	public enum PatientStatus {
		AC, IN, DE, IC, ID, MO, FI
	}

	/**
	 * @deprecated default for birth day should be null
	 */
	public static Demographic create(String firstName, String lastName, String gender, String monthOfBirth, String dateOfBirth, String yearOfBirth, String hin, String ver, boolean applyDefaultBirthDate) {
		return(create(firstName, lastName, gender, monthOfBirth, dateOfBirth, yearOfBirth, hin, ver));
	}

	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param gender
	 * @param monthOfBirth
	 * @param dateOfBirth
	 * @param yearOfBirth
	 * @param hin
	 * @param ver
	 * @return Demographic
	 */
	public static Demographic create(String firstName, String lastName, String gender, String monthOfBirth, String dateOfBirth, String yearOfBirth, String hin, String ver) {
		Demographic demographic = new Demographic();

		demographic.setFirstName(firstName);
		demographic.setLastName(lastName);
		demographic.setMonthOfBirth(monthOfBirth);
		demographic.setDateOfBirth(dateOfBirth);
		demographic.setYearOfBirth(yearOfBirth);
		demographic.setHin(hin);
		demographic.setVer(ver);

		demographic.setHcType(DEFAULT_HEATH_CARD_TYPE);
		demographic.setPatientStatus(DEFAULT_PATIENT_STATUS);
                demographic.setPatientStatusDate(new Date());
		demographic.setSex(gender == null || gender.length() == 0 ? DEFAULT_SEX : gender.substring(0, 1).toUpperCase());

		demographic.setDateJoined(new Date());
		//demographic.setEffDate(new Date());
		demographic.setEndDate(DateTimeFormatUtils.getDateFromString(DEFAULT_FUTURE_DATE));
		//demographic.setHcRenewDate(DateTimeFormatUtils.getDateFromString(DEFAULT_FUTURE_DATE));

		return demographic;
	}

	// constructors
	public Demographic() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Demographic(Integer demographicNo) {
		this.setDemographicNo(demographicNo);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public Demographic(Integer demographicNo, String firstName, String lastName) {

		this.setDemographicNo(demographicNo);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		initialize();
	}

        public String getDisplayName(){
            if(displayName==null){
                displayName=getLastName() + ", " + getFirstName();
                return displayName;
            }else return displayName;
        }
	/**
	 * Return the unique identifier of this class
	 *
	 */
	public Integer getDemographicNo() {
		return demographicNo;
	}

	/**
	 * Set the unique identifier of this class
	 *
	 * @param demographicNo the new ID
	 */
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Set the value related to the column: phone
	 *
	 * @param phone the phone value
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Return the value associated with the column: patient_status
	 */
	public String getPatientStatus() {
		return patientStatus;
	}

	public Date getPatientStatusDate() {
		return patientStatusDate;
	}

	/**
	 * Set the value related to the column: patient_status
	 *
	 * @param patientStatus the patient_status value
	 */
	public void setPatientStatus(String patientStatus) {
		this.patientStatus = patientStatus;
	}

	public void setPatientStatusDate(Date patientStatusDate) {
		this.patientStatusDate = patientStatusDate;
	}

	/**
	 * Return the value associated with the column: roster_status
	 */
	public String getRosterStatus() {
		return rosterStatus;
	}

	/**
	 * Set the value related to the column: roster_status
	 *
	 * @param rosterStatus the roster_status value
	 */
	public void setRosterStatus(String rosterStatus) {
		this.rosterStatus = rosterStatus;
	}

	/**
	 * Return the value associated with the column: provider_no
	 */
	public String getProviderNo() {
		return providerNo;
	}

	/**
	 * Set the value related to the column: provider_no
	 *
	 * @param providerNo the provider_no value
	 */
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}



	public String getMyOscarUserName() {
    	return (myOscarUserName);
    }

	public void setMyOscarUserName(String myOscarUserName) {
    	this.myOscarUserName = StringUtils.trimToNull(myOscarUserName);
    }

	/**
	 * Return the value associated with the column: hin
	 */
	public String getHin() {
		return hin;
	}

	/**
	 * Set the value related to the column: hin
	 *
	 * @param hin the hin value
	 */
	public void setHin(String hin) {
		this.hin = hin;
	}

	/**
	 * Return the value associated with the column: address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Set the value related to the column: address
	 *
	 * @param address the address value
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Return the value associated with the column: province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * Set the value related to the column: province
	 *
	 * @param province the province value
	 */
	public void setProvince(String province) {
		province=StringUtils.trimToNull(province);

		if (province!=null) province=province.toUpperCase();

		this.province = province;
	}

	/**
	 * Return the value associated with the column: month_of_birth
	 */
	public String getMonthOfBirth() {
		return monthOfBirth;
	}

	/**
	 * Set the value related to the column: month_of_birth
	 *
	 * @param monthOfBirth the month_of_birth value
	 */
	public void setMonthOfBirth(String monthOfBirth) {
		this.monthOfBirth = StringUtils.trimToNull(monthOfBirth);
	}

	/**
	 * Return the value associated with the column: ver
	 */
	public String getVer() {
		return ver;
	}

	/**
	 * Set the value related to the column: ver
	 *
	 * @param ver the ver value
	 */
	public void setVer(String ver) {
		this.ver = ver;
	}

	/**
	 * Return the value associated with the column: date_of_birth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * Set the value related to the column: date_of_birth
	 *
	 * @param dateOfBirth the date_of_birth value
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = StringUtils.trimToNull(dateOfBirth);
	}

	/**
	 * Return the value associated with the column: sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * Set the value related to the column: sex
	 *
	 * @param sex the sex value
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * Return the value associated with the column: date_joined
	 */
	public Date getDateJoined() {
		return dateJoined;
	}
	
	public String getFormattedDateJoined() {
		Date d = getDateJoined();
		if (d != null) return (DateFormatUtils.ISO_DATE_FORMAT.format(d));
		else return ("");
	}

	/**
	 * Set the value related to the column: date_joined
	 *
	 * @param dateJoined the date_joined value
	 */
	public void setDateJoined(Date dateJoined) {
		this.dateJoined = dateJoined;
	}

	/**
	 * Return the value associated with the column: family_doctor
	 */
	public String getFamilyDoctor() {
		if(StringUtils.isBlank(familyDoctor)) {
			this.familyDoctor = "";
		}
		return familyDoctor;
	}

	/**
	 * Set the value related to the column: family_doctor
	 *
	 * @param familyDoctor the family_doctor value
	 */
	public void setFamilyDoctor(String familyDoctor) {
		this.familyDoctor = familyDoctor;
	}

	/**
	 * Return the last name as parsed from column: family_doctor
	 */
	public String getFamilyDoctorLastName() {

		Matcher m = FD_LAST_NAME.matcher(getFamilyDoctor());

		if( m.find() ) {
			return m.group(1);
		}
		return ""; 
	}

	/**
	 * Return the first name as parsed from column: family_doctor
	 */
	public String getFamilyDoctorFirstName() {
		Matcher m = FD_FIRST_NAME.matcher(getFamilyDoctor());

		if( m.find()) {
			return m.group(1);
		}
		return "";
	}

	/**
	 * Return the doctor number as parsed from column: family_doctor
	 */
	public String getFamilyDoctorNumber() {

		Matcher m = FD_OHIP.matcher(getFamilyDoctor());

		if( m.find() ) {
			return m.group(1);
		}
		
		return "";
	}

	/**
	 * Return the value associated with the column: city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Set the value related to the column: city
	 *
	 * @param city the city value
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Return the value associated with the column: first_name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets demographic's full name.
	 * 
	 * @return
	 * 		Returns the last name, first name pair.
	 */
	public String getFullName() {
		return getLastName() + ", " + getFirstName();
	}
	
	/**
	 * Set the value related to the column: first_name
	 *
	 * @param firstName the first_name value
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Return the value associated with the column: postal
	 */
	public String getPostal() {
		return postal;
	}

	/**
	 * Set the value related to the column: postal
	 *
	 * @param postal the postal value
	 */
	public void setPostal(String postal) {
		this.postal = postal;
	}

	/**
	 * Return the value associated with the column: hc_renew_date
	 */
	public Date getHcRenewDate() {
		return hcRenewDate;
	}

	/**
	 * Set the value related to the column: hc_renew_date
	 *
	 * @param hcRenewDate the hc_renew_date value
	 */
	public void setHcRenewDate(Date hcRenewDate) {
		this.hcRenewDate = hcRenewDate;
	}

	/**
	 * Return the value associated with the column: phone2
	 */
	public String getPhone2() {
		return phone2;
	}

	/**
	 * Set the value related to the column: phone2
	 *
	 * @param phone2 the phone2 value
	 */
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	/**
	 * Return the value associated with the column: pcn_indicator
	 */
	public String getPcnIndicator() {
		return pcnIndicator;
	}

	/**
	 * Set the value related to the column: pcn_indicator
	 *
	 * @param pcnIndicator the pcn_indicator value
	 */
	public void setPcnIndicator(String pcnIndicator) {
		this.pcnIndicator = pcnIndicator;
	}

	/**
	 * Return the value associated with the column: end_date
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	public String getFormattedEndDate() {
		Date d = getEndDate();
		if (d != null) return (DateFormatUtils.ISO_DATE_FORMAT.format(d));
		else return ("");
	}

	/**
	 * Set the value related to the column: end_date
	 *
	 * @param endDate the end_date value
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Return the value associated with the column: last_name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set the value related to the column: last_name
	 *
	 * @param lastName the last_name value
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Return the value associated with the column: hc_type
	 */
	public String getHcType() {
		return hcType;
	}

	/**
	 * Set the value related to the column: hc_type
	 *
	 * @param hcType the hc_type value
	 */
	public void setHcType(String hcType) {
		this.hcType = hcType;
	}

	/**
	 * Return the value associated with the column: chart_no
	 */
	public String getChartNo() {
		return chartNo;
	}

	/**
	 * Set the value related to the column: chart_no
	 *
	 * @param chartNo the chart_no value
	 */
	public void setChartNo(String chartNo) {
		this.chartNo = chartNo;
	}

	/**
	 * Return the value associated with the column: email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set the value related to the column: email
	 *
	 * @param email the email value
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Return the value associated with the column: year_of_birth
	 */
	public String getYearOfBirth() {
		return yearOfBirth;
	}

	/**
	 * Set the value related to the column: year_of_birth
	 *
	 * @param yearOfBirth the year_of_birth value
	 */
	public void setYearOfBirth(String yearOfBirth) {
		this.yearOfBirth = StringUtils.trimToNull(yearOfBirth);
	}

	/**
	 * Return the value associated with the column: eff_date
	 */
	public Date getEffDate() {
		return effDate;
	}

	public String getFormattedEffDate() {
		Date d = getEffDate();
		if (d != null) return (DateFormatUtils.ISO_DATE_FORMAT.format(d));
		else return ("");
	}

	public String getAnonymous() {
		return anonymous;
	}

	/**
	 * @param anonymous can be any string indicating it's anonymisity (if that's a word), null means it's not anonymous.
	 */
	public void setAnonymous(String anonymous) {
		this.anonymous = anonymous;
	}

	public void setFormattedEffDate(String formattedDate) {
		 if(StringUtils.isBlank(formattedDate))
                        return;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(formattedDate);
			this.setEffDate(d);
		} catch (ParseException e) {
			MiscUtils.getLogger().error("Error", e);
		}

	}

	public String getFormattedRenewDate() {
		Date d = getHcRenewDate();
		if (d != null) return (DateFormatUtils.ISO_DATE_FORMAT.format(d));
		else return ("");
	}

	public void setFormattedRenewDate(String formattedDate) {
	 	if(StringUtils.isBlank(formattedDate))
                       return;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(formattedDate);
			this.setHcRenewDate(d);
		} catch (ParseException e) {
			MiscUtils.getLogger().error("Error", e);
		}

	}

	/**
	 * Set the value related to the column: eff_date
	 *
	 * @param effDate the eff_date value
	 */
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public String getPreviousAddress() {
		return previousAddress;
	}

	public void setPreviousAddress(String previousAddress) {
		this.previousAddress = previousAddress;
	}

	public String getSin() {
		return sin;
	}

	public void setSin(String sin) {
		this.sin = sin;
	}

	public String getSourceOfIncome() {
		return sourceOfIncome;
	}

	public void setSourceOfIncome(String sourceOfIncome) {
		this.sourceOfIncome = sourceOfIncome;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof Demographic)) return false;
		else {
			Demographic demographic = (Demographic) obj;
			if (null == this.getDemographicNo() || null == demographic.getDemographicNo()) return false;
			else return (this.getDemographicNo().equals(demographic.getDemographicNo()));
		}
	}

	@Override
	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getDemographicNo()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getDemographicNo().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	@Override
	public String toString() {
		return super.toString();
	}

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
		return (String.valueOf(Utility.calcAge(Utility.convertToReplaceStrIfEmptyStr(getYearOfBirth(), DEFAULT_YEAR), Utility.convertToReplaceStrIfEmptyStr(getMonthOfBirth(), DEFAULT_MONTH), Utility.convertToReplaceStrIfEmptyStr(getDateOfBirth(), DEFAULT_DATE))));
	}

	public String getAgeAsOf(Date asofDate) {
		return Utility.calcAgeAtDate(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(getYearOfBirth(), DEFAULT_YEAR), Utility.convertToReplaceStrIfEmptyStr(getMonthOfBirth(), DEFAULT_MONTH), Utility.convertToReplaceStrIfEmptyStr(getDateOfBirth(), DEFAULT_DATE)), asofDate);
	}


        //NEED TO IMPLEMENT

//            public long getAgeInDays(){
//           return UtilDateUtilities.getNumDays(UtilDateUtilities.calcDate(year_of_birth,month_of_birth,date_of_birth),Calendar.getInstance().getTime());
//        }
//
//
//        public int getAgeInMonths(){
//           return UtilDateUtilities.getNumMonths(UtilDateUtilities.calcDate(year_of_birth,month_of_birth,date_of_birth),Calendar.getInstance().getTime());
//        }
//
//        public int getAgeInMonthsAsOf(Date asofDate){
//           return UtilDateUtilities.getNumMonths(UtilDateUtilities.calcDate(year_of_birth,month_of_birth,date_of_birth),asofDate);
//        }

	public int getAgeInYears() {
		return Utility.getNumYears(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(getYearOfBirth(), DEFAULT_YEAR), Utility.convertToReplaceStrIfEmptyStr(getMonthOfBirth(), DEFAULT_MONTH), Utility.convertToReplaceStrIfEmptyStr(getDateOfBirth(), DEFAULT_DATE)), Calendar.getInstance().getTime());
	}

	public int getAgeInYearsAsOf(Date asofDate) {
		return Utility.getNumYears(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(getYearOfBirth(), DEFAULT_YEAR), Utility.convertToReplaceStrIfEmptyStr(getMonthOfBirth(), DEFAULT_MONTH), Utility.convertToReplaceStrIfEmptyStr(getDateOfBirth(), DEFAULT_DATE)), asofDate);
	}

	public DemographicExt[] getExtras() {
		return extras;
	}

	public String getFormattedDob() {
		Calendar cal = getBirthDay();
		if (cal != null) return (DateFormatUtils.ISO_DATE_FORMAT.format(cal));
		else return ("");
	}

	public void setFormattedDob(String formattedDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(formattedDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			this.setBirthDay(cal);
		} catch (ParseException e) {
			MiscUtils.getLogger().error("Error", e);
		}

	}

//      Implement?
//      public String getDob() {
//           return addZero(year_of_birth,4)+addZero(month_of_birth,2)+addZero(date_of_birth,2);
//      }
//      public String getDob(String seperator){
//	   return this.getYearOfBirth() + seperator + this.getMonthOfBirth() + seperator + this.getDateOfBirth();
//	}

	public String getFormattedLinks() {
		StringBuilder response = new StringBuilder();

		if (getNumLinks() > 0) {
			String[] links = getLinks().split(",");
			for (int x = 0; x < links.length; x++) {
				if (response.length() > 0) {
					response.append(",");
				}
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

	public void setExtras(DemographicExt[] extras) {
		this.extras = extras;
	}

	public void setLinks(String links) {
		this.links = links;
	}

	public Integer getHeadRecord() {
		return headRecord;
	}

	public void setHeadRecord(Integer headRecord) {
		this.headRecord = headRecord;
	}

	public Integer getCurrentRecord() {
		if (headRecord != null) return headRecord;
		return demographicNo;
	}

	public Set<Integer> getSubRecord() {
		return subRecord;
	}

	public void setSubRecord(Set<Integer> subRecord) {
		this.subRecord = subRecord;
	}

	public String getSexDesc() {
		return sexDesc;
	}

	public void setSexDesc(String sexDesc) {
		this.sexDesc = sexDesc;
	}

	public boolean isActive() {
		return activeCount > 0;
	}

	public boolean hasHsAlert() {
		return hsAlertCount > 0;
	}

	public int getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(int activeCount) {
		this.activeCount = activeCount;
	}

	public int getHsAlertCount() {
		return hsAlertCount;
	}

	public void setHsAlertCount(int hsAlertCount) {
		this.hsAlertCount = hsAlertCount;
	}

	public void setBirthDay(Calendar cal) {
		if (cal == null) {
			dateOfBirth = monthOfBirth = yearOfBirth = null;
		} else {
			dateOfBirth = addZero(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2);
			monthOfBirth = addZero(String.valueOf(cal.get(Calendar.MONTH) + 1), 2);
			yearOfBirth = addZero(String.valueOf(cal.get(Calendar.YEAR)), 4);
		}
	}

	public GregorianCalendar getBirthDay() {
		GregorianCalendar cal = null;

		if (dateOfBirth != null && monthOfBirth != null && yearOfBirth != null) {
			cal = new GregorianCalendar();
			cal.setTimeInMillis(0);
			cal.set(Integer.parseInt(yearOfBirth), Integer.parseInt(monthOfBirth) - 1, Integer.parseInt(dateOfBirth));

			// force materialisation of data
			cal.getTimeInMillis();
		}

		return (cal);
	}

	// Returns birthday in the format yyyy-mm-dd
	public String getBirthDayAsString() {
		return getYearOfBirth() + "-" + getMonthOfBirth() + "-" + getDateOfBirth();
	}

	public String getSpokenLanguage() {
		return spokenLanguage;
	}

	public void setSpokenLanguage(String spokenLanguage) {
		this.spokenLanguage = spokenLanguage;
	}

     /**
     * @return the provider
     */
    public Provider getProvider() {
        return provider;
}

    /**
     * @param provider the provider to set
     */
    public void setProvider(Provider provider) {
        this.provider = provider;
    }

	public String getCountryOfOrigin() {
    	return countryOfOrigin;
    }

	public void setCountryOfOrigin(String countryOfOrigin) {
    	this.countryOfOrigin = countryOfOrigin;
    }

	public String getNewsletter() {
    	return newsletter;
    }

	public void setNewsletter(String newsletter) {
    	this.newsletter = newsletter;
    }

    public static final Comparator<Demographic> FormattedNameComparator = new Comparator<Demographic>() {	
        @Override	
        public int compare(Demographic dm1, Demographic dm2) {	
            return dm1.getFormattedName().compareToIgnoreCase(dm2.getFormattedName());	
        }	
    }; 
	public static final Comparator<Demographic> LastNameComparator = new Comparator<Demographic>() {
        public int compare(Demographic dm1, Demographic dm2) {
        	return dm1.getLastName().compareTo(dm2.getLastName());
        }
    }; 
	public static final Comparator<Demographic> FirstNameComparator = new Comparator<Demographic>() {
        public int compare(Demographic dm1, Demographic dm2) {
        	return dm1.getFirstName().compareTo(dm2.getFirstName());
        }
    }; 
	public static final Comparator<Demographic> DemographicNoComparator = new Comparator<Demographic>() {
        public int compare(Demographic dm1, Demographic dm2) {
        	return dm1.getDemographicNo().compareTo(dm2.getDemographicNo());
        }
    }; 
	public static final Comparator<Demographic> SexComparator = new Comparator<Demographic>() {
        public int compare(Demographic dm1, Demographic dm2) {
        	return dm1.getSex().compareTo(dm2.getSex());
        }
    }; 
	public static final Comparator<Demographic> AgeComparator = new Comparator<Demographic>() {
        public int compare(Demographic dm1, Demographic dm2) {
        	return dm1.getAge().compareTo(dm2.getAge());
        }
    }; 
	public static final Comparator<Demographic> DateOfBirthComparator = new Comparator<Demographic>() {
        public int compare(Demographic dm1, Demographic dm2) {
        	return dm1.getBirthDayAsString().compareTo(dm2.getBirthDayAsString());
        }
    }; 
	public static final Comparator<Demographic> RosterStatusComparator = new Comparator<Demographic>() {
        public int compare(Demographic dm1, Demographic dm2) {
        	return dm1.getRosterStatus().compareTo(dm2.getRosterStatus());
        }
    }; 
	public static final Comparator<Demographic> ChartNoComparator = new Comparator<Demographic>() {
        public int compare(Demographic dm1, Demographic dm2) {
        	return dm1.getChartNo().compareTo(dm2.getChartNo());
        }
    }; 
	public static final Comparator<Demographic> ProviderNoComparator = new Comparator<Demographic>() {
        public int compare(Demographic dm1, Demographic dm2) {
        	return dm1.getProviderNo().compareTo(dm2.getProviderNo());
        }
    }; 
	public static final Comparator<Demographic> PatientStatusComparator = new Comparator<Demographic>() {
        public int compare(Demographic dm1, Demographic dm2) {
        	return dm1.getPatientStatus().compareTo(dm2.getPatientStatus());
        }
    }; 
	public static final Comparator<Demographic> PhoneComparator = new Comparator<Demographic>() {
        public int compare(Demographic dm1, Demographic dm2) {
        	return dm1.getPhone().compareTo(dm2.getPhone());
        }
    }; 


	
	public String getStandardIdentificationHTML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<b>" + getLastName().toUpperCase() + "</b>");
		sb.append(",");
		sb.append(getFirstName());
		if(getTitle() != null && getTitle().length()>0) {
			sb.append("(" + getTitle() + ")");
		}
		sb.append("<br/>");
		sb.append("Born ");
		sb.append("<b>" + getFormattedDob() + "</b>");
		if(getHin() != null && getHin().length()>0) {
			sb.append("<br/>");
			sb.append("HC ");
			sb.append("<b>");
			sb.append(getHin() + " " + getVer());
			sb.append("(" + getHcType() + ")");
			sb.append("</b>");
		}
		if(getChartNo() != null && getChartNo().length()>0) {
			sb.append("<br/>");
			sb.append("Chart No ");
			sb.append("<b>");
			sb.append(getChartNo());
			sb.append("</b>");
		}
		return sb.toString();
	}
	
}
