/**
 * Copyright (C) 2011-2012  PeaceWorks Technology Solutions
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


package org.oscarehr.oscarRx.erx.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

/**
 * A value type that stores the patient data accepted by the External Prescriber.
 * 
 */
public class ERxPatientData {
	/**
	 * Formats dates in a way that the the External Prescriber web services can understand.
	 * 
	 * @param date
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	/**
	 * The unique patient identifier provided by the client [OSCAR] software. It
	 * must always refer to the same patient. This field is mandatory. When
	 * merging records, this field should contain the old patientId (i.e.: the
	 * record that will be deleted); while the mergeId field should contain the
	 * patientId of the record to keep.
	 */
	private String patientId;
	/**
	 * A new unique patient identifier. When merging records, this field should
	 * contain the patientId of the record to keep; while the patientId field
	 * should contain the old patientId (i.e.: the record that will be deleted).
	 */
	private String mergeToId;

	/**
	 * Whether or not to delete this patient's record. 0 = create or update the
	 * patient 1 = delete the patient
	 */
	private int delete;
	/**
	 * The patient's first given name. This field is mandatory.
	 */
	private String firstname;
	/**
	 * The patient's family name. This field is mandatory.
	 */
	private String lastname;
	/** The patient's second given name, if applicable. */
	private String middleName;
	/**
	 * The patient's name suffix. Examples include: * "PEng"
	 * ("Professional Engineer") * "CC" ("Companion of the Order of Canada")
	 */
	private String nameSuffix;

	/**
	 * The patient's name prefix. Examples include: * "Sir"
	 * ("has been granted knighthood")
	 */
	private String namePrefix;
	/**
	 * The patient's date of birth in free-form text. If possible, use the form
	 * yyyy-MM-dd.
	 */
	private String birthdate;
	/**
	 * The patient's date of birth.
	 */
	private Date validBirthDate;

	/** The patient's gender in the form "M" or "F". */
	private String gender;
	/** The patient's street address. */
	private String address;
	/** The patient's city */
	private String city;
	/** The patient's state or province. */
	private String state;
	/** The patient's zipcode or postal code. */
	private String zipCode;
	/** The patient's primary telephone number. */
	private String phone1;
	/** The patient's alternate telephone number. */
	private String phone2;
	/**
	 * The patient's chart number. This field is not mandatory, but is strongly
	 * recommended.
	 */
	private String chartNumber;

	/** The patient's alternate chart number. */
	private String altChartNumber;
	/**
	 * The patient's primary identification number. This field is not mandatory,
	 * but is strongly recommended.
	 */
	private String pin1;
	/**
	 * The type of primary identification number. Valid values are: 1 = HCN
	 * (Health Communication Network?) 2 = Medicare 3 = SSN (Social Security
	 * Number) 4 = Medicaid
	 */
	private Integer pin1type;
	/**
	 * The date that the primary identification number expires in free-form
	 * text.
	 */
	private String pin1Expiration;
	/** The patient's secondary identification number. */
	private String pin2;
	/**
	 * The type of secondary identification number. Valid values are: 1 = HCN
	 * (Health Communication Network?) 2 = Medicare 3 = SSN (Social Security
	 * Number) 4 = Medicaid
	 */
	private Integer pin2type;
	/**
	 * The date that the secondary identification number expires in free-form
	 * text.
	 */
	private String pin2Expiration;
	/** The patient's tertiary identification number. */
	private String pin3;
	/**
	 * The type of tertiary identification number. Valid values are: 1 = HCN
	 * (Health Communication Network?) 2 = Medicare 3 = SSN (Social Security
	 * Number) 4 = Medicaid
	 */
	private Integer pin3type;

	/**
	 * The date that the tertiary identification number expires in free-form
	 * text.
	 */
	private String pin3Expiration;

	/**
	 * The patient's public Health Insurance Plan identifier. Valid values are:
	 * 1 = RAMQ (R�gie de l'assurance maladie du Qu�bec) 2 = OHIP (Ontario
	 * Health Insurance Plan)
	 */
	private Integer hip;

	/**
	 * Creates an instance of a ERxPatientData. This constructor fills all
	 * available fields.
	 * 
	 * @param patientId
	 *            The unique patient identifier provided by the client [OSCAR]
	 *            software.
	 * @param mergeToId
	 *            A new unique patient identifier.
	 * @param delete
	 *            Whether or not to delete this patient's record.
	 * @param firstname
	 *            The patient's first given name.
	 * @param lastname
	 * @param middleName
	 * @param nameSuffix
	 * @param namePrefix
	 * @param birthdate
	 * @param validBirthDate
	 * @param gender
	 * @param address
	 * @param city
	 * @param state
	 * @param zipCode
	 * @param phone1
	 * @param phone2
	 * @param chartNumber
	 * @param altChartNumber
	 * @param pin1
	 * @param pin1type
	 * @param pin1Expiration
	 * @param pin2
	 * @param pin2type
	 * @param pin2Expiration
	 * @param pin3
	 * @param pin3type
	 * @param pin3Expiration
	 * @param hip
	 */
	public ERxPatientData(String patientId, String mergeToId, int delete,
	    String firstname, String lastname, String middleName,
	    String nameSuffix, String namePrefix, String birthdate,
	    Date validBirthDate, String gender, String address, String city,
	    String state, String zipCode, String phone1, String phone2,
	    String chartNumber, String altChartNumber, String pin1, int pin1type,
	    String pin1Expiration, String pin2, int pin2type,
	    String pin2Expiration, String pin3, int pin3type,
	    String pin3Expiration, int hip) {
		super();
		this.patientId = patientId;
		this.mergeToId = mergeToId;
		this.delete = delete;
		this.firstname = firstname;
		this.lastname = lastname;
		this.middleName = middleName;
		this.nameSuffix = nameSuffix;
		this.namePrefix = namePrefix;
		this.birthdate = birthdate;
		this.validBirthDate = validBirthDate;
		this.gender = gender;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.chartNumber = chartNumber;
		this.altChartNumber = altChartNumber;
		this.pin1 = pin1;
		this.pin1type = pin1type;
		this.pin1Expiration = pin1Expiration;
		this.pin2 = pin2;
		this.pin2type = pin2type;
		this.pin2Expiration = pin2Expiration;
		this.pin3 = pin3;
		this.pin3type = pin3type;
		this.pin3Expiration = pin3Expiration;
		this.hip = hip;
	}

	/**
	 * Creates an instance of a ERxPatientData. This constructor only fills the
	 * mandatory fields.
	 * 
	 * @param patientId
	 *            The unique patient identifier provided by the client [OSCAR]
	 *            software.
	 * @param firstname
	 *            The patient's first given name.
	 * @param lastname
	 *            The patient's family name.
	 */
	public ERxPatientData(String patientId, String firstname, String lastname) {
		super();
		this.patientId = patientId;
		this.firstname = firstname;
		this.lastname = lastname;
		// Set the other string fields to empty strings
		this.mergeToId = "";
		this.middleName = "";
		this.nameSuffix = "";
		this.namePrefix = "";
		this.birthdate = "";
		this.gender = "";
		this.address = "";
		this.city = "";
		this.state = "";
		this.zipCode = "";
		this.phone1 = "";
		this.phone2 = "";
		this.chartNumber = "";
		this.altChartNumber = "";
		this.pin1 = "";
		this.pin1Expiration = "";
		this.pin2 = "";
		this.pin2Expiration = "";
		this.pin3 = "";
		this.pin3Expiration = "";
		// Set other fields
		this.delete = 0;
	}

	/**
	 * Creates an instance of a ERxPatientData. This constructor only fills the
	 * mandatory fields and strongly recommended fields.
	 * 
	 * @param patientId
	 *            The unique patient identifier provided by the client [OSCAR]
	 *            software.
	 * @param firstname
	 *            The patient's first given name.
	 * @param lastname
	 *            The patient's surname.
	 * @param chartNumber
	 *            The patient's chart number.
	 * @param pin1
	 *            The patient's primary identification number.
	 * @param pin1type
	 *            The type of primary identification number.
	 * @param pin1Expiration
	 *            The date that the primary identification number expires in
	 *            free-form text.
	 * @param hip
	 *            The patient's public Health Insurance Plan identifier.
	 */
	public ERxPatientData(String patientId, String firstname, String lastname,
	    String chartNumber, String pin1, int pin1type, String pin1Expiration,
	    int hip) {
		super();
		this.patientId = patientId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.chartNumber = chartNumber;
		this.pin1 = pin1;
		this.pin1type = pin1type;
		this.pin1Expiration = pin1Expiration;
		this.hip = hip;
		// Set other fields
		this.delete = 0;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * @return the altChartNumber
	 */
	public String getAltChartNumber() {
		return this.altChartNumber;
	}

	/**
	 * @return the birthdate
	 */
	public String getBirthdate() {
		return this.birthdate;
	}

	/**
	 * @return the chartNumber
	 */
	public String getChartNumber() {
		return this.chartNumber;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * @return the delete
	 */
	public int getDelete() {
		return this.delete;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return this.firstname;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return this.gender;
	}

	/**
	 * @return the hip
	 */
	public int getHip() {
		return this.hip.intValue();
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return this.lastname;
	}

	/**
	 * @return the mergeToId
	 */
	public String getMergeToId() {
		return this.mergeToId;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * @return the namePrefix
	 */
	public String getNamePrefix() {
		return this.namePrefix;
	}

	/**
	 * @return the nameSuffix
	 */
	public String getNameSuffix() {
		return this.nameSuffix;
	}

	/**
	 * @return the patientId
	 */
	public String getPatientId() {
		return this.patientId;
	}

	/**
	 * @return the phone1
	 */
	public String getPhone1() {
		return this.phone1;
	}

	/**
	 * @return the phone2
	 */
	public String getPhone2() {
		return this.phone2;
	}

	/**
	 * @return the pin1
	 */
	public String getPin1() {
		return this.pin1;
	}

	/**
	 * @return the pin1Expiration
	 */
	public String getPin1Expiration() {
		return this.pin1Expiration;
	}

	/**
	 * @return the pin1type
	 */
	public int getPin1type() {
		return this.pin1type.intValue();
	}

	/**
	 * @return the pin2
	 */
	public String getPin2() {
		return this.pin2;
	}

	/**
	 * @return the pin2Expiration
	 */
	public String getPin2Expiration() {
		return this.pin2Expiration;
	}

	/**
	 * @return the pin2type
	 */
	public int getPin2type() {
		return this.pin2type.intValue();
	}

	/**
	 * @return the pin3
	 */
	public String getPin3() {
		return this.pin3;
	}

	/**
	 * @return the pin3Expiration
	 */
	public String getPin3Expiration() {
		return this.pin3Expiration;
	}

	/**
	 * @return the pin3type
	 */
	public int getPin3type() {
		return this.pin3type.intValue();
	}

	/**
	 * Get a SOAP document fragment representing this object.
	 * 
	 * @return A SOAPElement representing this patient data.
	 * @throws SOAPException
	 *             If an error occurred when trying to construct the element.
	 */
	public SOAPElement getSOAPElement() throws SOAPException {
		SOAPElement answer = SOAPFactory.newInstance().createElement("patient");

		answer.addChildElement("PatientId").addTextNode(this.patientId);
		answer.addChildElement("MergeToId").addTextNode(this.mergeToId);
		answer.addChildElement("Delete").addTextNode(
		    Integer.toString(this.delete));
		answer.addChildElement("Firstname").addTextNode(this.firstname);
		answer.addChildElement("Lastname").addTextNode(this.lastname);
		answer.addChildElement("MiddleName").addTextNode(this.middleName);
		answer.addChildElement("NameSuffix").addTextNode(this.nameSuffix);
		answer.addChildElement("NamePrefix").addTextNode(this.namePrefix);
		answer.addChildElement("DateOfBirth").addTextNode(this.birthdate);

		if (this.validBirthDate != null) {
			answer.addChildElement("CleanDateOfBirth").addTextNode(
			    ERxPatientData.formatDate(this.validBirthDate));
		}

		answer.addChildElement("Gender").addTextNode(this.gender);
		answer.addChildElement("Address").addTextNode(this.address);
		answer.addChildElement("City").addTextNode(this.city);
		answer.addChildElement("State").addTextNode(this.state);
		answer.addChildElement("ZipCode").addTextNode(this.zipCode);
		answer.addChildElement("Phone1").addTextNode(this.phone1);
		answer.addChildElement("Phone2").addTextNode(this.phone2);
		answer.addChildElement("ChartNumber").addTextNode(this.chartNumber);
		answer.addChildElement("AltChartNumber").addTextNode(
		    this.altChartNumber);

		answer.addChildElement("PIN1").addTextNode(this.pin1);
		if (this.pin1type != null) {
			answer.addChildElement("PIN1type").addTextNode(
			    Integer.toString(this.pin1type));
		}

		answer.addChildElement("PIN1Expiration").addTextNode(
		    this.pin1Expiration);
		answer.addChildElement("PIN2").addTextNode(this.pin2);

		if (this.pin2type != null) {
			answer.addChildElement("PIN2type").addTextNode(
			    Integer.toString(this.pin2type));
		}

		answer.addChildElement("PIN2Expiration").addTextNode(
		    this.pin2Expiration);
		answer.addChildElement("PIN3").addTextNode(this.pin3);

		if (this.pin3type != null) {
			answer.addChildElement("PIN3type").addTextNode(
			    Integer.toString(this.pin3type));
		}

		answer.addChildElement("PIN3Expiration").addTextNode(
		    this.pin3Expiration);

		if (this.hip != null) {
			answer.addChildElement("HIP").addTextNode(
			    Integer.toString(this.hip));
		}

		return answer;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * @return the validBirthDate
	 */
	public Date getValidBirthDate() {
		return this.validBirthDate;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return this.zipCode;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param altChartNumber
	 *            the altChartNumber to set
	 */
	public void setAltChartNumber(String altChartNumber) {
		this.altChartNumber = altChartNumber;
	}

	/**
	 * @param birthdate
	 *            the birthdate to set
	 */
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	/**
	 * @param chartNumber
	 *            the chartNumber to set
	 */
	public void setChartNumber(String chartNumber) {
		this.chartNumber = chartNumber;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param delete
	 *            the delete to set
	 */
	public void setDelete(int delete) {
		this.delete = delete;
	}

	/**
	 * @param firstname
	 *            the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @param hip
	 *            the hip to set
	 */
	public void setHip(int hip) {
		this.hip = new Integer(hip);
	}

	/**
	 * @param lastname
	 *            the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @param mergeToId
	 *            the mergeToId to set
	 */
	public void setMergeToId(String mergeToId) {
		this.mergeToId = mergeToId;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @param namePrefix
	 *            the namePrefix to set
	 */
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	/**
	 * @param nameSuffix
	 *            the nameSuffix to set
	 */
	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}

	/**
	 * @param patientId
	 *            the patientId to set
	 */
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	/**
	 * @param phone1
	 *            the phone1 to set
	 */
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	/**
	 * @param phone2
	 *            the phone2 to set
	 */
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	/**
	 * @param pin1
	 *            the pin1 to set
	 */
	public void setPin1(String pin1) {
		this.pin1 = pin1;
	}

	/**
	 * @param pin1Expiration
	 *            the pin1Expiration to set
	 */
	public void setPin1Expiration(String pin1Expiration) {
		this.pin1Expiration = pin1Expiration;
	}

	/**
	 * @param pin1type
	 *            the pin1type to set
	 */
	public void setPin1type(int pin1type) {
		this.pin1type = new Integer(pin1type);
	}

	/**
	 * @param pin2
	 *            the pin2 to set
	 */
	public void setPin2(String pin2) {
		this.pin2 = pin2;
	}

	/**
	 * @param pin2Expiration
	 *            the pin2Expiration to set
	 */
	public void setPin2Expiration(String pin2Expiration) {
		this.pin2Expiration = pin2Expiration;
	}

	/**
	 * @param pin2type
	 *            the pin2type to set
	 */
	public void setPin2type(int pin2type) {
		this.pin2type = new Integer(pin2type);
	}

	/**
	 * @param pin3
	 *            the pin3 to set
	 */
	public void setPin3(String pin3) {
		this.pin3 = pin3;
	}

	/**
	 * @param pin3Expiration
	 *            the pin3Expiration to set
	 */
	public void setPin3Expiration(String pin3Expiration) {
		this.pin3Expiration = pin3Expiration;
	}

	/**
	 * @param pin3type
	 *            the pin3type to set
	 */
	public void setPin3type(int pin3type) {
		this.pin3type = new Integer(pin3type);
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @param validBirthDate
	 *            the validBirthDate to set
	 */
	public void setValidBirthDate(Date validBirthDate) {
		this.validBirthDate = validBirthDate;
	}

	/**
	 * @param zipCode
	 *            the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}
