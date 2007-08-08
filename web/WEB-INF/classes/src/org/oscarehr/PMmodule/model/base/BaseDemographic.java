/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the demographic table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="demographic"
 */

public abstract class BaseDemographic  implements Serializable {

	// constructors
	public BaseDemographic () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseDemographic (java.lang.Integer demographicNo) {
		this.setDemographicNo(demographicNo);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseDemographic (
		java.lang.Integer demographicNo,
		java.lang.String firstName,
		java.lang.String lastName) {

		this.setDemographicNo(demographicNo);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		initialize();
	}

	protected void initialize () {}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer demographicNo;

	// fields
	private java.lang.String phone;
	private java.lang.String patientStatus;
	private java.lang.String rosterStatus;
	private java.lang.String providerNo;
	private java.lang.String pin;
	private java.lang.String hin;
	private java.lang.String address;
	private java.lang.String province;
	private java.lang.String monthOfBirth;
	private java.lang.String ver;
	private java.lang.String dateOfBirth;
	private java.lang.String sex;
	private java.util.Date dateJoined;
	private java.lang.String familyDoctor;
	private java.lang.String city;
	private java.lang.String firstName;
	private java.lang.String postal;
	private java.util.Date hcRenewDate;
	private java.lang.String phone2;
	private java.lang.String pcnIndicator;
	private java.util.Date endDate;
	private java.lang.String lastName;
	private java.lang.String hcType;
	private java.lang.String chartNo;
	private java.lang.String email;
	private java.lang.String yearOfBirth;
	private java.util.Date effDate;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="demographic_no"
     */
	public java.lang.Integer getDemographicNo () {
		return demographicNo;
	}

	/**
	 * Set the unique identifier of this class
	 * @param demographicNo the new ID
	 */
	public void setDemographicNo (java.lang.Integer demographicNo) {
		this.demographicNo = demographicNo;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: phone
	 */
	public java.lang.String getPhone () {
		return phone;
	}

	/**
	 * Set the value related to the column: phone
	 * @param phone the phone value
	 */
	public void setPhone (java.lang.String phone) {
		this.phone = phone;
	}



	/**
	 * Return the value associated with the column: patient_status
	 */
	public java.lang.String getPatientStatus () {
		return patientStatus;
	}

	/**
	 * Set the value related to the column: patient_status
	 * @param patientStatus the patient_status value
	 */
	public void setPatientStatus (java.lang.String patientStatus) {
		this.patientStatus = patientStatus;
	}



	/**
	 * Return the value associated with the column: roster_status
	 */
	public java.lang.String getRosterStatus () {
		return rosterStatus;
	}

	/**
	 * Set the value related to the column: roster_status
	 * @param rosterStatus the roster_status value
	 */
	public void setRosterStatus (java.lang.String rosterStatus) {
		this.rosterStatus = rosterStatus;
	}



	/**
	 * Return the value associated with the column: provider_no
	 */
	public java.lang.String getProviderNo () {
		return providerNo;
	}

	/**
	 * Set the value related to the column: provider_no
	 * @param providerNo the provider_no value
	 */
	public void setProviderNo (java.lang.String providerNo) {
		this.providerNo = providerNo;
	}



	/**
	 * Return the value associated with the column: pin
	 */
	public java.lang.String getPin () {
		return pin;
	}

	/**
	 * Set the value related to the column: pin
	 * @param pin the pin value
	 */
	public void setPin (java.lang.String pin) {
		this.pin = pin;
	}



	/**
	 * Return the value associated with the column: hin
	 */
	public java.lang.String getHin () {
		return hin;
	}

	/**
	 * Set the value related to the column: hin
	 * @param hin the hin value
	 */
	public void setHin (java.lang.String hin) {
		this.hin = hin;
	}



	/**
	 * Return the value associated with the column: address
	 */
	public java.lang.String getAddress () {
		return address;
	}

	/**
	 * Set the value related to the column: address
	 * @param address the address value
	 */
	public void setAddress (java.lang.String address) {
		this.address = address;
	}



	/**
	 * Return the value associated with the column: province
	 */
	public java.lang.String getProvince () {
		return province;
	}

	/**
	 * Set the value related to the column: province
	 * @param province the province value
	 */
	public void setProvince (java.lang.String province) {
		this.province = province;
	}



	/**
	 * Return the value associated with the column: month_of_birth
	 */
	public java.lang.String getMonthOfBirth () {
		return monthOfBirth;
	}

	/**
	 * Set the value related to the column: month_of_birth
	 * @param monthOfBirth the month_of_birth value
	 */
	public void setMonthOfBirth (java.lang.String monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}



	/**
	 * Return the value associated with the column: ver
	 */
	public java.lang.String getVer () {
		return ver;
	}

	/**
	 * Set the value related to the column: ver
	 * @param ver the ver value
	 */
	public void setVer (java.lang.String ver) {
		this.ver = ver;
	}



	/**
	 * Return the value associated with the column: date_of_birth
	 */
	public java.lang.String getDateOfBirth () {
		return dateOfBirth;
	}

	/**
	 * Set the value related to the column: date_of_birth
	 * @param dateOfBirth the date_of_birth value
	 */
	public void setDateOfBirth (java.lang.String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}



	/**
	 * Return the value associated with the column: sex
	 */
	public java.lang.String getSex () {
		return sex;
	}

	/**
	 * Set the value related to the column: sex
	 * @param sex the sex value
	 */
	public void setSex (java.lang.String sex) {
		this.sex = sex;
	}



	/**
	 * Return the value associated with the column: date_joined
	 */
	public java.util.Date getDateJoined () {
		return dateJoined;
	}

	/**
	 * Set the value related to the column: date_joined
	 * @param dateJoined the date_joined value
	 */
	public void setDateJoined (java.util.Date dateJoined) {
		this.dateJoined = dateJoined;
	}



	/**
	 * Return the value associated with the column: family_doctor
	 */
	public java.lang.String getFamilyDoctor () {
		return familyDoctor;
	}

	/**
	 * Set the value related to the column: family_doctor
	 * @param familyDoctor the family_doctor value
	 */
	public void setFamilyDoctor (java.lang.String familyDoctor) {
		this.familyDoctor = familyDoctor;
	}



	/**
	 * Return the value associated with the column: city
	 */
	public java.lang.String getCity () {
		return city;
	}

	/**
	 * Set the value related to the column: city
	 * @param city the city value
	 */
	public void setCity (java.lang.String city) {
		this.city = city;
	}



	/**
	 * Return the value associated with the column: first_name
	 */
	public java.lang.String getFirstName () {
		return firstName;
	}

	/**
	 * Set the value related to the column: first_name
	 * @param firstName the first_name value
	 */
	public void setFirstName (java.lang.String firstName) {
		this.firstName = firstName;
	}



	/**
	 * Return the value associated with the column: postal
	 */
	public java.lang.String getPostal () {
		return postal;
	}

	/**
	 * Set the value related to the column: postal
	 * @param postal the postal value
	 */
	public void setPostal (java.lang.String postal) {
		this.postal = postal;
	}



	/**
	 * Return the value associated with the column: hc_renew_date
	 */
	public java.util.Date getHcRenewDate () {
		return hcRenewDate;
	}

	/**
	 * Set the value related to the column: hc_renew_date
	 * @param hcRenewDate the hc_renew_date value
	 */
	public void setHcRenewDate (java.util.Date hcRenewDate) {
		this.hcRenewDate = hcRenewDate;
	}



	/**
	 * Return the value associated with the column: phone2
	 */
	public java.lang.String getPhone2 () {
		return phone2;
	}

	/**
	 * Set the value related to the column: phone2
	 * @param phone2 the phone2 value
	 */
	public void setPhone2 (java.lang.String phone2) {
		this.phone2 = phone2;
	}



	/**
	 * Return the value associated with the column: pcn_indicator
	 */
	public java.lang.String getPcnIndicator () {
		return pcnIndicator;
	}

	/**
	 * Set the value related to the column: pcn_indicator
	 * @param pcnIndicator the pcn_indicator value
	 */
	public void setPcnIndicator (java.lang.String pcnIndicator) {
		this.pcnIndicator = pcnIndicator;
	}



	/**
	 * Return the value associated with the column: end_date
	 */
	public java.util.Date getEndDate () {
		return endDate;
	}

	/**
	 * Set the value related to the column: end_date
	 * @param endDate the end_date value
	 */
	public void setEndDate (java.util.Date endDate) {
		this.endDate = endDate;
	}



	/**
	 * Return the value associated with the column: last_name
	 */
	public java.lang.String getLastName () {
		return lastName;
	}

	/**
	 * Set the value related to the column: last_name
	 * @param lastName the last_name value
	 */
	public void setLastName (java.lang.String lastName) {
		this.lastName = lastName;
	}



	/**
	 * Return the value associated with the column: hc_type
	 */
	public java.lang.String getHcType () {
		return hcType;
	}

	/**
	 * Set the value related to the column: hc_type
	 * @param hcType the hc_type value
	 */
	public void setHcType (java.lang.String hcType) {
		this.hcType = hcType;
	}



	/**
	 * Return the value associated with the column: chart_no
	 */
	public java.lang.String getChartNo () {
		return chartNo;
	}

	/**
	 * Set the value related to the column: chart_no
	 * @param chartNo the chart_no value
	 */
	public void setChartNo (java.lang.String chartNo) {
		this.chartNo = chartNo;
	}



	/**
	 * Return the value associated with the column: email
	 */
	public java.lang.String getEmail () {
		return email;
	}

	/**
	 * Set the value related to the column: email
	 * @param email the email value
	 */
	public void setEmail (java.lang.String email) {
		this.email = email;
	}



	/**
	 * Return the value associated with the column: year_of_birth
	 */
	public java.lang.String getYearOfBirth () {
		return yearOfBirth;
	}

	/**
	 * Set the value related to the column: year_of_birth
	 * @param yearOfBirth the year_of_birth value
	 */
	public void setYearOfBirth (java.lang.String yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}



	/**
	 * Return the value associated with the column: eff_date
	 */
	public java.util.Date getEffDate () {
		return effDate;
	}

	/**
	 * Set the value related to the column: eff_date
	 * @param effDate the eff_date value
	 */
	public void setEffDate (java.util.Date effDate) {
		this.effDate = effDate;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.Demographic)) return false;
		else {
			org.oscarehr.PMmodule.model.Demographic demographic = (org.oscarehr.PMmodule.model.Demographic) obj;
			if (null == this.getDemographicNo() || null == demographic.getDemographicNo()) return false;
			else return (this.getDemographicNo().equals(demographic.getDemographicNo()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getDemographicNo()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getDemographicNo().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}


}