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
package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DemographicTo1 implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer demographicNo; // fields
	private AddressTo1 address = new AddressTo1();
	private String phone;
	private String alternativePhone;
	private String patientStatus;
	private Date patientStatusDate;
	private String rosterStatus;
	private String providerNo;
	private String myOscarUserName;
	private String hin;
	private String ver;
	private Date dateOfBirth;
	private String dobYear;
	private String dobMonth;
	private String dobDay;
	private String sex;
	private String sexDesc;
	private Date dateJoined;
	private String familyDoctor;
	private String firstName;
	private String lastName;
	private Date hcRenewDate;
	private String pcnIndicator;
	private Date endDate;
	private String hcType;
	private String chartNo;
	private String email;
	private Date effDate;
	private Date rosterDate;
	private Date rosterTerminationDate;
	private String rosterTerminationReason;
	private String links;
	private String alias;
	private AddressTo1 previousAddress = new AddressTo1();
	private String children;
	private String sourceOfIncome;
	private String citizenship;
	private String sin;
	private String anonymous;
	private String spokenLanguage;
	private int activeCount;
	private int hsAlertCount;
	private String displayName;
	private ProviderTo1 provider;
	private String lastUpdateUser;
	private Date lastUpdateDate;
	private String title;
	private String officialLanguage;
	private String countryOfOrigin;
	private String newsletter;
	private String nurse;
	private String resident;
	private String alert;
	private String midwife;
	private String notes;
	private Integer waitingListID;
	private String waitingListNote;
	private Date onWaitingListSinceDate;
	private AgeTo1 age;
	private Integer admissionProgramId;
	
	private List<DemographicContactFewTo1> demoContacts = new ArrayList<DemographicContactFewTo1>();
	private List<DemographicContactFewTo1> demoContactPros = new ArrayList<DemographicContactFewTo1>();
	private List<DemographicExtTo1> extras = new ArrayList<DemographicExtTo1>();
	private List<ProviderTo1> doctors = new ArrayList<ProviderTo1>();
	private List<ProviderTo1> nurses = new ArrayList<ProviderTo1>();
	private List<ProviderTo1> midwives = new ArrayList<ProviderTo1>();
	private List<ProfessionalSpecialistTo1> referralDoctors = new ArrayList<ProfessionalSpecialistTo1>();
	private List<WaitingListNameTo1> waitingListNames = new ArrayList<WaitingListNameTo1>();
	private List<StatusValueTo1> patientStatusList = new ArrayList<StatusValueTo1>();
	private List<StatusValueTo1> rosterStatusList = new ArrayList<StatusValueTo1>();
	
	
	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public AddressTo1 getAddress() {
		return address;
	}

	public void setAddress(AddressTo1 address) {
		this.address = address;
	}

	public String getPatientStatus() {
		return patientStatus;
	}

	public void setPatientStatus(String patientStatus) {
		this.patientStatus = patientStatus;
	}

	public Date getPatientStatusDate() {
		return patientStatusDate;
	}

	public void setPatientStatusDate(Date patientStatusDate) {
		this.patientStatusDate = patientStatusDate;
	}

	public String getRosterStatus() {
		return rosterStatus;
	}

	public void setRosterStatus(String rosterStatus) {
		this.rosterStatus = rosterStatus;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getMyOscarUserName() {
		return myOscarUserName;
	}

	public void setMyOscarUserName(String myOscarUserName) {
		this.myOscarUserName = myOscarUserName;
	}

	public String getHin() {
		return hin;
	}

	public void setHin(String hin) {
		this.hin = hin;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDobYear() {
		return dobYear;
	}

	public void setDobYear(String dobYear) {
		this.dobYear = dobYear;
	}

	public String getDobMonth() {
		return dobMonth;
	}

	public void setDobMonth(String dobMonth) {
		this.dobMonth = dobMonth;
	}

	public String getDobDay() {
		return dobDay;
	}

	public void setDobDay(String dobDay) {
		this.dobDay = dobDay;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSexDesc() {
		return sexDesc;
	}

	public void setSexDesc(String sexDesc) {
		this.sexDesc = sexDesc;
	}

	public Date getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(Date dateJoined) {
		this.dateJoined = dateJoined;
	}

	public String getFamilyDoctor() {
		return familyDoctor;
	}

	public void setFamilyDoctor(String familyDoctor) {
		this.familyDoctor = familyDoctor;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getHcRenewDate() {
		return hcRenewDate;
	}

	public void setHcRenewDate(Date hcRenewDate) {
		this.hcRenewDate = hcRenewDate;
	}

	public String getPcnIndicator() {
		return pcnIndicator;
	}

	public void setPcnIndicator(String pcnIndicator) {
		this.pcnIndicator = pcnIndicator;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getHcType() {
		return hcType;
	}

	public void setHcType(String hcType) {
		this.hcType = hcType;
	}

	public String getChartNo() {
		return chartNo;
	}

	public void setChartNo(String chartNo) {
		this.chartNo = chartNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getEffDate() {
		return effDate;
	}

	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}

	public Date getRosterDate() {
		return rosterDate;
	}

	public void setRosterDate(Date rosterDate) {
		this.rosterDate = rosterDate;
	}

	public Date getRosterTerminationDate() {
		return rosterTerminationDate;
	}

	public void setRosterTerminationDate(Date rosterTerminationDate) {
		this.rosterTerminationDate = rosterTerminationDate;
	}

	public String getRosterTerminationReason() {
		return rosterTerminationReason;
	}

	public void setRosterTerminationReason(String rosterTerminationReason) {
		this.rosterTerminationReason = rosterTerminationReason;
	}

	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public AddressTo1 getPreviousAddress() {
		return previousAddress;
	}

	public void setPreviousAddress(AddressTo1 previousAddress) {
		this.previousAddress = previousAddress;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getSourceOfIncome() {
		return sourceOfIncome;
	}

	public void setSourceOfIncome(String sourceOfIncome) {
		this.sourceOfIncome = sourceOfIncome;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public String getSin() {
		return sin;
	}

	public void setSin(String sin) {
		this.sin = sin;
	}

	public String getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(String anonymous) {
		this.anonymous = anonymous;
	}

	public String getSpokenLanguage() {
		return spokenLanguage;
	}

	public void setSpokenLanguage(String spokenLanguage) {
		this.spokenLanguage = spokenLanguage;
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public ProviderTo1 getProvider() {
		return provider;
	}

	public void setProvider(ProviderTo1 provider) {
		this.provider = provider;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAlternativePhone() {
		return alternativePhone;
	}

	public void setAlternativePhone(String alternativePhone) {
		this.alternativePhone = alternativePhone;
	}

	public String getNurse() {
		return nurse;
	}
	
	public void setNurse(String nurse) {
		this.nurse = nurse;
	}
	
	public String getResident() {
		return resident;
	}
	
	public void setResident(String resident) {
		this.resident = resident;
	}
	
	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}
	
	public String getMidwife() {
		return midwife;
	}
	
	public void setMidwife(String midwife) {
		this.midwife = midwife;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Integer getWaitingListID() {
		return waitingListID;
	}
	
	public void setWaitingListID(Integer waitingListID) {
		this.waitingListID = waitingListID;
	}
	
	public String getWaitingListNote() {
		return waitingListNote;
	}
	
	public void setWaitingListNote(String note) {
		this.waitingListNote = note;
	}
	
	public Date getOnWaitingListSinceDate() {
		return onWaitingListSinceDate;
	}
	
	public void setOnWaitingListSinceDate(Date onWaitingListSinceDate) {
		this.onWaitingListSinceDate = onWaitingListSinceDate;
	}

	public List<DemographicExtTo1> getExtras() {
		return extras;
	}

	public void setExtras(List<DemographicExtTo1> extras) {
		this.extras = extras;
	}

	public List<DemographicContactFewTo1> getDemoContacts() {
		return demoContacts;
	}

	public void setDemoContacts(List<DemographicContactFewTo1> demoContacts) {
		this.demoContacts = demoContacts;
	}

	public List<DemographicContactFewTo1> getDemoContactPros() {
		return demoContactPros;
	}

	public void setDemoContactPros(List<DemographicContactFewTo1> demoContactPros) {
		this.demoContactPros = demoContactPros;
	}
	
	public List<ProviderTo1> getDoctors() {
		return doctors;
	}
	
	public void setDoctors(List<ProviderTo1> doctors) {
		this.doctors = doctors;
	}
	
	public List<ProviderTo1> getNurses() {
		return nurses;
	}
	
	public void setNurses(List<ProviderTo1> nurses) {
		this.nurses = nurses;
	}
	
	public List<ProviderTo1> getMidwives() {
		return midwives;
	}
	
	public void setMidwives(List<ProviderTo1> midwives) {
		this.midwives = midwives;
	}
	
	public List<ProfessionalSpecialistTo1> getReferralDoctors() {
		return referralDoctors;
	}
	
	public void setReferralDoctors(List<ProfessionalSpecialistTo1> referralDoctors) {
		this.referralDoctors = referralDoctors;
	}
	
	public List<WaitingListNameTo1> getWaitingListNames() {
		return waitingListNames;
	}
	
	public void setWaitingListNames(List<WaitingListNameTo1> waitingListNames) {
		this.waitingListNames = waitingListNames;
	}
	
	public List<StatusValueTo1> getPatientStatusList() {
		return patientStatusList;
	}
	
	public void setPatientStatusList(List<StatusValueTo1> patientStatusList) {
		this.patientStatusList = patientStatusList;
	}
	
	public List<StatusValueTo1> getRosterStatusList() {
		return rosterStatusList;
	}
	
	public void setRosterStatusList(List<StatusValueTo1> rosterStatusList) {
		this.rosterStatusList = rosterStatusList;
	}

	public AgeTo1 getAge() {
		return age;
	}

	public void setAge(AgeTo1 age) {
		this.age = age;
	}

	public Integer getAdmissionProgramId() {
		return admissionProgramId;
	}
	
	public void setAdmissionProgramId(Integer programId) {
		this.admissionProgramId = programId;
	}
	
}
