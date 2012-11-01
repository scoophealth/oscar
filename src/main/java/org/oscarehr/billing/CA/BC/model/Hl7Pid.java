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
package org.oscarehr.billing.CA.BC.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="hl7_pid")
public class Hl7Pid extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pid_id")
	private Integer id;
	
	@Column(name="message_id")
	private int messageId;
	
	@Column(name="set_id")
	private String setId;
	
	@Column(name="external_id")
	private String externalId;
	
	@Column(name="internal_id")
	private String internalId;
	
	@Column(name="alternate_id")
	private String alternateId;
	
	@Column(name="patient_name")
	private String patientName;
	
	@Column(name="mother_maiden_name")
	private String motherMaidenName;
	
	@Column(name="date_of_birth")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfBirth;
	
	private String sex;
	
	@Column(name="patient_alias")
	private String patientAlias;
	
	private String race;
	
	@Column(name="patient_address")
	private String patientAddress;
	
	@Column(name="country_code")
	private String countryCode;
	
	@Column(name="home_number")
	private String homeNumber;
	
	@Column(name="work_number")
	private String workNumber;
	
	private String language;
	
	@Column(name="marital_status")
	private String maritalStatus;
	
	private String religion;
	
	@Column(name="patient_account_number")
	private String patientAccountNumber;
	
	@Column(name="ssn_number")
	private String ssnNumber;
	
	@Column(name="driver_license")
	private String driverLicense;
	
	@Column(name="mother_identifier")
	private String motherIdentifier;
	
	@Column(name="ethnic_group")
	private String ethnicGroup;
	
	@Column(name="birth_place")
	private String birthPlace;
	
	@Column(name="multiple_birth_indicator")
	private String multipleBirthIndicator;
	
	@Column(name="birth_order")
	private String birthOrder;
	
	private String citizenship;
	
	@Column(name="veteran_military_status")
	private String veteranMilitaryStatus;
	
	private String nationality;
	
	@Column(name="patient_death_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date patientDeathDateTime;
	
	@Column(name="patient_death_indicator")
	private String patientDeathIndicator;
	
	private String note;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getSetId() {
		return setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getAlternateId() {
		return alternateId;
	}

	public void setAlternateId(String alternateId) {
		this.alternateId = alternateId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getMotherMaidenName() {
		return motherMaidenName;
	}

	public void setMotherMaidenName(String motherMaidenName) {
		this.motherMaidenName = motherMaidenName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPatientAlias() {
		return patientAlias;
	}

	public void setPatientAlias(String patientAlias) {
		this.patientAlias = patientAlias;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getPatientAddress() {
		return patientAddress;
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getHomeNumber() {
		return homeNumber;
	}

	public void setHomeNumber(String homeNumber) {
		this.homeNumber = homeNumber;
	}

	public String getWorkNumber() {
		return workNumber;
	}

	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getPatientAccountNumber() {
		return patientAccountNumber;
	}

	public void setPatientAccountNumber(String patientAccountNumber) {
		this.patientAccountNumber = patientAccountNumber;
	}

	public String getSsnNumber() {
		return ssnNumber;
	}

	public void setSsnNumber(String ssnNumber) {
		this.ssnNumber = ssnNumber;
	}

	public String getDriverLicense() {
		return driverLicense;
	}

	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}

	public String getMotherIdentifier() {
		return motherIdentifier;
	}

	public void setMotherIdentifier(String motherIdentifier) {
		this.motherIdentifier = motherIdentifier;
	}

	public String getEthnicGroup() {
		return ethnicGroup;
	}

	public void setEthnicGroup(String ethnicGroup) {
		this.ethnicGroup = ethnicGroup;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getMultipleBirthIndicator() {
		return multipleBirthIndicator;
	}

	public void setMultipleBirthIndicator(String multipleBirthIndicator) {
		this.multipleBirthIndicator = multipleBirthIndicator;
	}

	public String getBirthOrder() {
		return birthOrder;
	}

	public void setBirthOrder(String birthOrder) {
		this.birthOrder = birthOrder;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public String getVeteranMilitaryStatus() {
		return veteranMilitaryStatus;
	}

	public void setVeteranMilitaryStatus(String veteranMilitaryStatus) {
		this.veteranMilitaryStatus = veteranMilitaryStatus;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Date getPatientDeathDateTime() {
		return patientDeathDateTime;
	}

	public void setPatientDeathDateTime(Date patientDeathDateTime) {
		this.patientDeathDateTime = patientDeathDateTime;
	}

	public String getPatientDeathIndicator() {
		return patientDeathIndicator;
	}

	public void setPatientDeathIndicator(String patientDeathIndicator) {
		this.patientDeathIndicator = patientDeathIndicator;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
}
