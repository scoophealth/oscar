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

package org.oscarehr.PMmodule.web.formbean;

import org.apache.struts.validator.ValidatorForm;

public class IntakeAForm extends ValidatorForm  
{

	public String ID = "";
	public String demographic_no = "";
	public String provider_no = "";
	public String formCreated = "";
	public String formEdited = "";
	public String assessDate = "";
	public String assessStartTime = "";
	public String enterSeatonDate = "";
	public String cbox_newClient = "";
	public String cbox_dateOfReadmission = "";
	public String dateOfReadmission = "";
	public String cbox_isStatementRead = "";
	public String clientSurname = "";
	public String clientFirstName = "";
	public String month = "";
	public String day = "";
	public String year = "";
	public String cbox_speakEnglish = "";
	public String cbox_speakFrench = "";
	public String cbox_speakOther = "";
	public String speakOther = "";
	public String reasonToSeaton = "";
	public String datesAtSeaton = "";
	public String cbox_assistInHealth = "";
	public String cbox_assistInIdentification = "";
	public String cbox_assistInAddictions = "";
	public String cbox_assistInHousing = "";
	public String cbox_assistInEducation = "";
	public String cbox_assistInEmployment = "";
	public String cbox_assistInFinance = "";
	public String cbox_assistInLegal = "";
	public String cbox_assistInImmigration = "";
	public String cbox_noID = "";
	public String cbox_sinCard = "";
	public String sinNum = "";
	public String cbox_healthCard = "";
	public String healthCardNum = "";
	public String healthCardVer = "";
	public String cbox_birthCertificate = "";
	public String birthCertificateNum = "";
	public String cbox_citzenshipCard = "";
	public String citzenshipCardNum = "";
	public String cbox_immigrant = "";
	public String immigrantNum = "";
	public String cbox_refugee = "";
	public String refugeeNum = "";
	public String cbox_otherID = "";
	public String otherIdentification = "";
	public String cbox_idFiled = "";
	public String cbox_idNone = "";
	public String commentsOnID = "";
	public String cbox_OW = "";
	public String cbox_ODSP = "";
	public String cbox_WSIB = "";
	public String cbox_Employment = "";
	public String cbox_EI = "";
	public String cbox_OAS = "";
	public String cbox_CPP = "";
	public String cbox_OtherIncome = "";
	public String radio_onlineCheck = "";
	public String radio_active = "";
	public String cbox_noRecord = "";
	public String lastIssueDate = "";
	public String office = "";
	public String workerNum = "";
	public String amtReceived = "";
	public String radio_hasDoctor = "";
	public String doctorName = "";
	public String doctorPhone = "";
	public String doctorPhoneExt = "";
	public String doctorAddress = "";
	public String radio_seeDoctor = "";
	public String radio_healthIssue = "";
	public String healthIssueDetails = "";
	public String cbox_hasDiabetes = "";
	public String cbox_insulin = "";
	public String cbox_epilepsy = "";
	public String cbox_bleeding = "";
	public String cbox_hearingImpair = "";
	public String cbox_visualImpair = "";
	public String cbox_mobilityImpair = "";
	public String mobilityImpair = "";
	public String radio_otherHealthConcern = "";
	public String otherHealthConerns = "";
	public String radio_takeMedication = "";
	public String namesOfMedication = "";
	public String radio_helpObtainMedication = "";
	public String helpObtainMedication = "";
	public String radio_allergicToMedication = "";
	public String allergicToMedicationName = "";
	public String reactionToMedication = "";
	public String radio_mentalHealthConcerns = "";
	public String mentalHealthConcerns = "";
	public String cbox_isStatement6Read = "";
	public String frequencyOfSeeingDoctor = "";
	public String cbox_visitWalkInClinic = "";
	public String cbox_visitHealthCenter = "";
	public String cbox_visitEmergencyRoom = "";
	public String cbox_visitOthers = "";
	public String cbox_visitHealthOffice = "";
	public String radio_seeSameDoctor = "";
	public String frequencyOfSeeingEmergencyRoomDoctor = "";
	public String radio_didNotReceiveHealthCare = "";
	public String cbox_treatPhysicalHealth = "";
	public String cbox_treatMentalHealth = "";
	public String cbox_regularCheckup = "";
	public String cbox_treatOtherReasons = "";
	public String treatOtherReasons = "";
	public String cbox_treatInjury = "";
	public String cbox_goToWalkInClinic = "";
	public String cbox_goToHealthCenter = "";
	public String cbox_goToEmergencyRoom = "";
	public String cbox_goToOthers = "";
	public String goToOthers = "";
	public String cbox_HealthOffice = "";
	public String radio_appmtSeeDoctorIn3Mths = "";
	public String radio_needRegularDoctor = "";
	public String radio_objectToRegularDoctorIn4Wks = "";
	public String radio_rateOverallHealth = "";
	public String radio_speakToResearcher = "";
	public String contactName = "";
	public String contactPhone = "";
	public String contactAddress = "";
	public String contactRelationship = "";
	public String radio_hasMentalIllness = "";
	public String radio_hasDrinkingProblem = "";
	public String radio_hasDrugProblem = "";
	public String radio_hasHealthProblem = "";
	public String radio_hasBehaviorProblem = "";
	public String radio_needSeatonService = "";
	public String radio_seatonTour = "";
	public String seatonNotToured = "";
	public String radio_pamphletIssued = "";
	public String pamphletNotIssued = "";
	public String summary = "";
	public String completedBy = "";
	public String assessCompleteTime = "";
	public String cbox_pamphletIssued = "";
	public String cbox_hostel = "";
	public String cbox_rotaryClub = "";
	public String cbox_annexHarm = "";
	public String cbox_longTermProgram = "";
	public String cbox_birchmountResidence = "";
	public String cbox_oNeillHouse = "";
	public String cbox_fortYork = "";
	public String cbox_downsviewDells = "";
	public String cbox_sexMale ="";
	public String cbox_sexFemale ="";
	public String cbox_sexTransgendered ="";
	
//############################################################################	
	public String getAllergicToMedicationName() {
		return allergicToMedicationName;
	}
	public void setAllergicToMedicationName(String allergicToMedicationName) {
		this.allergicToMedicationName = allergicToMedicationName;
	}
	public String getAmtReceived() {
		return amtReceived;
	}
	public void setAmtReceived(String amtReceived) {
		this.amtReceived = amtReceived;
	}
	public String getAssessCompleteTime() {
		return assessCompleteTime;
	}
	public void setAssessCompleteTime(String assessCompleteTime) {
		this.assessCompleteTime = assessCompleteTime;
	}
	public String getAssessDate() {
		return assessDate;
	}
	public void setAssessDate(String assessDate) {
		this.assessDate = assessDate;
	}
	public String getAssessStartTime() {
		return assessStartTime;
	}
	public void setAssessStartTime(String assessStartTime) {
		this.assessStartTime = assessStartTime;
	}
	public String getBirthCertificateNum() {
		return birthCertificateNum;
	}
	public void setBirthCertificateNum(String birthCertificateNum) {
		this.birthCertificateNum = birthCertificateNum;
	}
	public String getCbox_annexHarm() {
		return cbox_annexHarm;
	}
	public void setCbox_annexHarm(String cbox_annexHarm) {
		this.cbox_annexHarm = cbox_annexHarm;
	}
	public String getCbox_assistInAddictions() {
		return cbox_assistInAddictions;
	}
	public void setCbox_assistInAddictions(String cbox_assistInAddictions) {
		this.cbox_assistInAddictions = cbox_assistInAddictions;
	}
	public String getCbox_assistInEducation() {
		return cbox_assistInEducation;
	}
	public void setCbox_assistInEducation(String cbox_assistInEducation) {
		this.cbox_assistInEducation = cbox_assistInEducation;
	}
	public String getCbox_assistInEmployment() {
		return cbox_assistInEmployment;
	}
	public void setCbox_assistInEmployment(String cbox_assistInEmployment) {
		this.cbox_assistInEmployment = cbox_assistInEmployment;
	}
	public String getCbox_assistInFinance() {
		return cbox_assistInFinance;
	}
	public void setCbox_assistInFinance(String cbox_assistInFinance) {
		this.cbox_assistInFinance = cbox_assistInFinance;
	}
	public String getCbox_assistInHealth() {
		return cbox_assistInHealth;
	}
	public void setCbox_assistInHealth(String cbox_assistInHealth) {
		this.cbox_assistInHealth = cbox_assistInHealth;
	}
	public String getCbox_assistInHousing() {
		return cbox_assistInHousing;
	}
	public void setCbox_assistInHousing(String cbox_assistInHousing) {
		this.cbox_assistInHousing = cbox_assistInHousing;
	}
	public String getCbox_assistInIdentification() {
		return cbox_assistInIdentification;
	}
	public void setCbox_assistInIdentification(String cbox_assistInIdentification) {
		this.cbox_assistInIdentification = cbox_assistInIdentification;
	}
	public String getCbox_assistInImmigration() {
		return cbox_assistInImmigration;
	}
	public void setCbox_assistInImmigration(String cbox_assistInImmigration) {
		this.cbox_assistInImmigration = cbox_assistInImmigration;
	}
	public String getCbox_assistInLegal() {
		return cbox_assistInLegal;
	}
	public void setCbox_assistInLegal(String cbox_assistInLegal) {
		this.cbox_assistInLegal = cbox_assistInLegal;
	}
	public String getCbox_birchmountResidence() {
		return cbox_birchmountResidence;
	}
	public void setCbox_birchmountResidence(String cbox_birchmountResidence) {
		this.cbox_birchmountResidence = cbox_birchmountResidence;
	}
	public String getCbox_birthCertificate() {
		return cbox_birthCertificate;
	}
	public void setCbox_birthCertificate(String cbox_birthCertificate) {
		this.cbox_birthCertificate = cbox_birthCertificate;
	}
	public String getCbox_bleeding() {
		return cbox_bleeding;
	}
	public void setCbox_bleeding(String cbox_bleeding) {
		this.cbox_bleeding = cbox_bleeding;
	}
	public String getCbox_citzenshipCard() {
		return cbox_citzenshipCard;
	}
	public void setCbox_citzenshipCard(String cbox_citzenshipCard) {
		this.cbox_citzenshipCard = cbox_citzenshipCard;
	}
	public String getCbox_CPP() {
		return cbox_CPP;
	}
	public void setCbox_CPP(String cbox_CPP) {
		this.cbox_CPP = cbox_CPP;
	}
	public String getCbox_dateOfReadmission() {
		return cbox_dateOfReadmission;
	}
	public void setCbox_dateOfReadmission(String cbox_dateOfReadmission) {
		this.cbox_dateOfReadmission = cbox_dateOfReadmission;
	}
	public String getCbox_downsviewDells() {
		return cbox_downsviewDells;
	}
	public void setCbox_downsviewDells(String cbox_downsviewDells) {
		this.cbox_downsviewDells = cbox_downsviewDells;
	}
	public String getCbox_EI() {
		return cbox_EI;
	}
	public void setCbox_EI(String cbox_EI) {
		this.cbox_EI = cbox_EI;
	}
	public String getCbox_Employment() {
		return cbox_Employment;
	}
	public void setCbox_Employment(String cbox_Employment) {
		this.cbox_Employment = cbox_Employment;
	}
	public String getCbox_epilepsy() {
		return cbox_epilepsy;
	}
	public void setCbox_epilepsy(String cbox_epilepsy) {
		this.cbox_epilepsy = cbox_epilepsy;
	}
	public String getCbox_fortYork() {
		return cbox_fortYork;
	}
	public void setCbox_fortYork(String cbox_fortYork) {
		this.cbox_fortYork = cbox_fortYork;
	}
	public String getCbox_goToEmergencyRoom() {
		return cbox_goToEmergencyRoom;
	}
	public void setCbox_goToEmergencyRoom(String cbox_goToEmergencyRoom) {
		this.cbox_goToEmergencyRoom = cbox_goToEmergencyRoom;
	}
	public String getCbox_goToHealthCenter() {
		return cbox_goToHealthCenter;
	}
	public void setCbox_goToHealthCenter(String cbox_goToHealthCenter) {
		this.cbox_goToHealthCenter = cbox_goToHealthCenter;
	}
	public String getCbox_goToOthers() {
		return cbox_goToOthers;
	}
	public void setCbox_goToOthers(String cbox_goToOthers) {
		this.cbox_goToOthers = cbox_goToOthers;
	}
	public String getCbox_goToWalkInClinic() {
		return cbox_goToWalkInClinic;
	}
	public void setCbox_goToWalkInClinic(String cbox_goToWalkInClinic) {
		this.cbox_goToWalkInClinic = cbox_goToWalkInClinic;
	}
	public String getCbox_hasDiabetes() {
		return cbox_hasDiabetes;
	}
	public void setCbox_hasDiabetes(String cbox_hasDiabetes) {
		this.cbox_hasDiabetes = cbox_hasDiabetes;
	}
	public String getCbox_healthCard() {
		return cbox_healthCard;
	}
	public void setCbox_healthCard(String cbox_healthCard) {
		this.cbox_healthCard = cbox_healthCard;
	}
	public String getCbox_HealthOffice() {
		return cbox_HealthOffice;
	}
	public void setCbox_HealthOffice(String cbox_HealthOffice) {
		this.cbox_HealthOffice = cbox_HealthOffice;
	}
	public String getCbox_hearingImpair() {
		return cbox_hearingImpair;
	}
	public void setCbox_hearingImpair(String cbox_hearingImpair) {
		this.cbox_hearingImpair = cbox_hearingImpair;
	}
	public String getCbox_hostel() {
		return cbox_hostel;
	}
	public void setCbox_hostel(String cbox_hostel) {
		this.cbox_hostel = cbox_hostel;
	}
	public String getCbox_idFiled() {
		return cbox_idFiled;
	}
	public void setCbox_idFiled(String cbox_idFiled) {
		this.cbox_idFiled = cbox_idFiled;
	}
	public String getCbox_idNone() {
		return cbox_idNone;
	}
	public void setCbox_idNone(String cbox_idNone) {
		this.cbox_idNone = cbox_idNone;
	}
	public String getCbox_immigrant() {
		return cbox_immigrant;
	}
	public void setCbox_immigrant(String cbox_immigrant) {
		this.cbox_immigrant = cbox_immigrant;
	}
	public String getCbox_insulin() {
		return cbox_insulin;
	}
	public void setCbox_insulin(String cbox_insulin) {
		this.cbox_insulin = cbox_insulin;
	}
	public String getCbox_isStatement6Read() {
		return cbox_isStatement6Read;
	}
	public void setCbox_isStatement6Read(String cbox_isStatement6Read) {
		this.cbox_isStatement6Read = cbox_isStatement6Read;
	}
	public String getCbox_isStatementRead() {
		return cbox_isStatementRead;
	}
	public void setCbox_isStatementRead(String cbox_isStatementRead) {
		this.cbox_isStatementRead = cbox_isStatementRead;
	}
	public String getCbox_longTermProgram() {
		return cbox_longTermProgram;
	}
	public void setCbox_longTermProgram(String cbox_longTermProgram) {
		this.cbox_longTermProgram = cbox_longTermProgram;
	}
	public String getCbox_mobilityImpair() {
		return cbox_mobilityImpair;
	}
	public void setCbox_mobilityImpair(String cbox_mobilityImpair) {
		this.cbox_mobilityImpair = cbox_mobilityImpair;
	}
	public String getCbox_newClient() {
		return cbox_newClient;
	}
	public void setCbox_newClient(String cbox_newClient) {
		this.cbox_newClient = cbox_newClient;
	}
	public String getCbox_noID() {
		return cbox_noID;
	}
	public void setCbox_noID(String cbox_noID) {
		this.cbox_noID = cbox_noID;
	}
	public String getCbox_noRecord() {
		return cbox_noRecord;
	}
	public void setCbox_noRecord(String cbox_noRecord) {
		this.cbox_noRecord = cbox_noRecord;
	}
	public String getCbox_OAS() {
		return cbox_OAS;
	}
	public void setCbox_OAS(String cbox_OAS) {
		this.cbox_OAS = cbox_OAS;
	}
	public String getCbox_ODSP() {
		return cbox_ODSP;
	}
	public void setCbox_ODSP(String cbox_ODSP) {
		this.cbox_ODSP = cbox_ODSP;
	}
	public String getCbox_oNeillHouse() {
		return cbox_oNeillHouse;
	}
	public void setCbox_oNeillHouse(String cbox_oNeillHouse) {
		this.cbox_oNeillHouse = cbox_oNeillHouse;
	}
	public String getCbox_otherID() {
		return cbox_otherID;
	}
	public void setCbox_otherID(String cbox_otherID) {
		this.cbox_otherID = cbox_otherID;
	}
	public String getCbox_OtherIncome() {
		return cbox_OtherIncome;
	}
	public void setCbox_OtherIncome(String cbox_OtherIncome) {
		this.cbox_OtherIncome = cbox_OtherIncome;
	}
	public String getCbox_OW() {
		return cbox_OW;
	}
	public void setCbox_OW(String cbox_OW) {
		this.cbox_OW = cbox_OW;
	}
	public String getCbox_pamphletIssued() {
		return cbox_pamphletIssued;
	}
	public void setCbox_pamphletIssued(String cbox_pamphletIssued) {
		this.cbox_pamphletIssued = cbox_pamphletIssued;
	}
	public String getCbox_refugee() {
		return cbox_refugee;
	}
	public void setCbox_refugee(String cbox_refugee) {
		this.cbox_refugee = cbox_refugee;
	}
	public String getCbox_regularCheckup() {
		return cbox_regularCheckup;
	}
	public void setCbox_regularCheckup(String cbox_regularCheckup) {
		this.cbox_regularCheckup = cbox_regularCheckup;
	}
	public String getCbox_rotaryClub() {
		return cbox_rotaryClub;
	}
	public void setCbox_rotaryClub(String cbox_rotaryClub) {
		this.cbox_rotaryClub = cbox_rotaryClub;
	}
	public String getCbox_sinCard() {
		return cbox_sinCard;
	}
	public void setCbox_sinCard(String cbox_sinCard) {
		this.cbox_sinCard = cbox_sinCard;
	}
	public String getCbox_speakEnglish() {
		return cbox_speakEnglish;
	}
	public void setCbox_speakEnglish(String cbox_speakEnglish) {
		this.cbox_speakEnglish = cbox_speakEnglish;
	}
	public String getCbox_speakFrench() {
		return cbox_speakFrench;
	}
	public void setCbox_speakFrench(String cbox_speakFrench) {
		this.cbox_speakFrench = cbox_speakFrench;
	}
	public String getCbox_speakOther() {
		return cbox_speakOther;
	}
	public void setCbox_speakOther(String cbox_speakOther) {
		this.cbox_speakOther = cbox_speakOther;
	}
	public String getCbox_treatInjury() {
		return cbox_treatInjury;
	}
	public void setCbox_treatInjury(String cbox_treatInjury) {
		this.cbox_treatInjury = cbox_treatInjury;
	}
	public String getCbox_treatMentalHealth() {
		return cbox_treatMentalHealth;
	}
	public void setCbox_treatMentalHealth(String cbox_treatMentalHealth) {
		this.cbox_treatMentalHealth = cbox_treatMentalHealth;
	}
	public String getCbox_treatOtherReasons() {
		return cbox_treatOtherReasons;
	}
	public void setCbox_treatOtherReasons(String cbox_treatOtherReasons) {
		this.cbox_treatOtherReasons = cbox_treatOtherReasons;
	}
	public String getCbox_treatPhysicalHealth() {
		return cbox_treatPhysicalHealth;
	}
	public void setCbox_treatPhysicalHealth(String cbox_treatPhysicalHealth) {
		this.cbox_treatPhysicalHealth = cbox_treatPhysicalHealth;
	}
	public String getCbox_visitEmergencyRoom() {
		return cbox_visitEmergencyRoom;
	}
	public void setCbox_visitEmergencyRoom(String cbox_visitEmergencyRoom) {
		this.cbox_visitEmergencyRoom = cbox_visitEmergencyRoom;
	}
	public String getCbox_visitHealthCenter() {
		return cbox_visitHealthCenter;
	}
	public void setCbox_visitHealthCenter(String cbox_visitHealthCenter) {
		this.cbox_visitHealthCenter = cbox_visitHealthCenter;
	}
	public String getCbox_visitHealthOffice() {
		return cbox_visitHealthOffice;
	}
	public void setCbox_visitHealthOffice(String cbox_visitHealthOffice) {
		this.cbox_visitHealthOffice = cbox_visitHealthOffice;
	}
	public String getCbox_visitOthers() {
		return cbox_visitOthers;
	}
	public void setCbox_visitOthers(String cbox_visitOthers) {
		this.cbox_visitOthers = cbox_visitOthers;
	}
	public String getCbox_visitWalkInClinic() {
		return cbox_visitWalkInClinic;
	}
	public void setCbox_visitWalkInClinic(String cbox_visitWalkInClinic) {
		this.cbox_visitWalkInClinic = cbox_visitWalkInClinic;
	}
	public String getCbox_visualImpair() {
		return cbox_visualImpair;
	}
	public void setCbox_visualImpair(String cbox_visualImpair) {
		this.cbox_visualImpair = cbox_visualImpair;
	}
	public String getCbox_WSIB() {
		return cbox_WSIB;
	}
	public void setCbox_WSIB(String cbox_WSIB) {
		this.cbox_WSIB = cbox_WSIB;
	}
	public String getCitzenshipCardNum() {
		return citzenshipCardNum;
	}
	public void setCitzenshipCardNum(String citzenshipCardNum) {
		this.citzenshipCardNum = citzenshipCardNum;
	}
	public String getClientFirstName() {
		return clientFirstName;
	}
	public void setClientFirstName(String clientFirstName) {
		this.clientFirstName = clientFirstName;
	}
	public String getClientSurname() {
		return clientSurname;
	}
	public void setClientSurname(String clientSurname) {
		this.clientSurname = clientSurname;
	}
	public String getCommentsOnID() {
		return commentsOnID;
	}
	public void setCommentsOnID(String commentsOnID) {
		this.commentsOnID = commentsOnID;
	}
	public String getCompletedBy() {
		return completedBy;
	}
	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactRelationship() {
		return contactRelationship;
	}
	public void setContactRelationship(String contactRelationship) {
		this.contactRelationship = contactRelationship;
	}
	public String getDateOfReadmission() {
		return dateOfReadmission;
	}
	public void setDateOfReadmission(String dateOfReadmission) {
		this.dateOfReadmission = dateOfReadmission;
	}
	public String getDatesAtSeaton() {
		return datesAtSeaton;
	}
	public void setDatesAtSeaton(String datesAtSeaton) {
		this.datesAtSeaton = datesAtSeaton;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}
	public String getDoctorAddress() {
		return doctorAddress;
	}
	public void setDoctorAddress(String doctorAddress) {
		this.doctorAddress = doctorAddress;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDoctorPhone() {
		return doctorPhone;
	}
	public void setDoctorPhone(String doctorPhone) {
		this.doctorPhone = doctorPhone;
	}
	public String getDoctorPhoneExt() {
		return doctorPhoneExt;
	}
	public void setDoctorPhoneExt(String doctorPhoneExt) {
		this.doctorPhoneExt = doctorPhoneExt;
	}
	public String getEnterSeatonDate() {
		return enterSeatonDate;
	}
	public void setEnterSeatonDate(String enterSeatonDate) {
		this.enterSeatonDate = enterSeatonDate;
	}
	public String getFormCreated() {
		return formCreated;
	}
	public void setFormCreated(String formCreated) {
		this.formCreated = formCreated;
	}
	public String getFormEdited() {
		return formEdited;
	}
	public void setFormEdited(String formEdited) {
		this.formEdited = formEdited;
	}
	public String getFrequencyOfSeeingDoctor() {
		return frequencyOfSeeingDoctor;
	}
	public void setFrequencyOfSeeingDoctor(String frequencyOfSeeingDoctor) {
		this.frequencyOfSeeingDoctor = frequencyOfSeeingDoctor;
	}
	public String getFrequencyOfSeeingEmergencyRoomDoctor() {
		return frequencyOfSeeingEmergencyRoomDoctor;
	}
	public void setFrequencyOfSeeingEmergencyRoomDoctor(
			String frequencyOfSeeingEmergencyRoomDoctor) {
		this.frequencyOfSeeingEmergencyRoomDoctor = frequencyOfSeeingEmergencyRoomDoctor;
	}
	public String getGoToOthers() {
		return goToOthers;
	}
	public void setGoToOthers(String goToOthers) {
		this.goToOthers = goToOthers;
	}
	public String getHealthCardNum() {
		return healthCardNum;
	}
	public void setHealthCardNum(String healthCardNum) {
		this.healthCardNum = healthCardNum;
	}
	public String getHealthCardVer() {
		return healthCardVer;
	}
	public void setHealthCardVer(String healthCardVer) {
		this.healthCardVer = healthCardVer;
	}
	public String getHealthIssueDetails() {
		return healthIssueDetails;
	}
	public void setHealthIssueDetails(String healthIssueDetails) {
		this.healthIssueDetails = healthIssueDetails;
	}
	public String getHelpObtainMedication() {
		return helpObtainMedication;
	}
	public void setHelpObtainMedication(String helpObtainMedication) {
		this.helpObtainMedication = helpObtainMedication;
	}
	public String getID() {
		return ID;
	}
	public void setID(String id) {
		ID = id;
	}
	public String getImmigrantNum() {
		return immigrantNum;
	}
	public void setImmigrantNum(String immigrantNum) {
		this.immigrantNum = immigrantNum;
	}
	public String getLastIssueDate() {
		return lastIssueDate;
	}
	public void setLastIssueDate(String lastIssueDate) {
		this.lastIssueDate = lastIssueDate;
	}
	public String getMentalHealthConcerns() {
		return mentalHealthConcerns;
	}
	public void setMentalHealthConcerns(String mentalHealthConcerns) {
		this.mentalHealthConcerns = mentalHealthConcerns;
	}
	public String getMobilityImpair() {
		return mobilityImpair;
	}
	public void setMobilityImpair(String mobilityImpair) {
		this.mobilityImpair = mobilityImpair;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getNamesOfMedication() {
		return namesOfMedication;
	}
	public void setNamesOfMedication(String namesOfMedication) {
		this.namesOfMedication = namesOfMedication;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getOtherHealthConerns() {
		return otherHealthConerns;
	}
	public void setOtherHealthConerns(String otherHealthConerns) {
		this.otherHealthConerns = otherHealthConerns;
	}
	public String getOtherIdentification() {
		return otherIdentification;
	}
	public void setOtherIdentification(String otherIdentification) {
		this.otherIdentification = otherIdentification;
	}
	public String getPamphletNotIssued() {
		return pamphletNotIssued;
	}
	public void setPamphletNotIssued(String pamphletNotIssued) {
		this.pamphletNotIssued = pamphletNotIssued;
	}
	public String getProviderNo() {
		return provider_no;
	}
	public void setProviderNo(String provider_no) {
		this.provider_no = provider_no;
	}
	public String getRadio_active() {
		return radio_active;
	}
	public void setRadio_active(String radio_active) {
		this.radio_active = radio_active;
	}
	public String getRadio_allergicToMedication() {
		return radio_allergicToMedication;
	}
	public void setRadio_allergicToMedication(String radio_allergicToMedication) {
		this.radio_allergicToMedication = radio_allergicToMedication;
	}
	public String getRadio_appmtSeeDoctorIn3Mths() {
		return radio_appmtSeeDoctorIn3Mths;
	}
	public void setRadio_appmtSeeDoctorIn3Mths(String radio_appmtSeeDoctorIn3Mths) {
		this.radio_appmtSeeDoctorIn3Mths = radio_appmtSeeDoctorIn3Mths;
	}
	public String getRadio_didNotReceiveHealthCare() {
		return radio_didNotReceiveHealthCare;
	}
	public void setRadio_didNotReceiveHealthCare(
			String radio_didNotReceiveHealthCare) {
		this.radio_didNotReceiveHealthCare = radio_didNotReceiveHealthCare;
	}
	public String getRadio_hasBehaviorProblem() {
		return radio_hasBehaviorProblem;
	}
	public void setRadio_hasBehaviorProblem(String radio_hasBehaviorProblem) {
		this.radio_hasBehaviorProblem = radio_hasBehaviorProblem;
	}
	public String getRadio_hasDoctor() {
		return radio_hasDoctor;
	}
	public void setRadio_hasDoctor(String radio_hasDoctor) {
		this.radio_hasDoctor = radio_hasDoctor;
	}
	public String getRadio_hasDrinkingProblem() {
		return radio_hasDrinkingProblem;
	}
	public void setRadio_hasDrinkingProblem(String radio_hasDrinkingProblem) {
		this.radio_hasDrinkingProblem = radio_hasDrinkingProblem;
	}
	public String getRadio_hasDrugProblem() {
		return radio_hasDrugProblem;
	}
	public void setRadio_hasDrugProblem(String radio_hasDrugProblem) {
		this.radio_hasDrugProblem = radio_hasDrugProblem;
	}
	public String getRadio_hasHealthProblem() {
		return radio_hasHealthProblem;
	}
	public void setRadio_hasHealthProblem(String radio_hasHealthProblem) {
		this.radio_hasHealthProblem = radio_hasHealthProblem;
	}
	public String getRadio_hasMentalIllness() {
		return radio_hasMentalIllness;
	}
	public void setRadio_hasMentalIllness(String radio_hasMentalIllness) {
		this.radio_hasMentalIllness = radio_hasMentalIllness;
	}
	public String getRadio_healthIssue() {
		return radio_healthIssue;
	}
	public void setRadio_healthIssue(String radio_healthIssue) {
		this.radio_healthIssue = radio_healthIssue;
	}
	public String getRadio_helpObtainMedication() {
		return radio_helpObtainMedication;
	}
	public void setRadio_helpObtainMedication(String radio_helpObtainMedication) {
		this.radio_helpObtainMedication = radio_helpObtainMedication;
	}
	public String getRadio_mentalHealthConcerns() {
		return radio_mentalHealthConcerns;
	}
	public void setRadio_mentalHealthConcerns(String radio_mentalHealthConcerns) {
		this.radio_mentalHealthConcerns = radio_mentalHealthConcerns;
	}
	public String getRadio_needRegularDoctor() {
		return radio_needRegularDoctor;
	}
	public void setRadio_needRegularDoctor(String radio_needRegularDoctor) {
		this.radio_needRegularDoctor = radio_needRegularDoctor;
	}
	public String getRadio_needSeatonService() {
		return radio_needSeatonService;
	}
	public void setRadio_needSeatonService(String radio_needSeatonService) {
		this.radio_needSeatonService = radio_needSeatonService;
	}
	public String getRadio_objectToRegularDoctorIn4Wks() {
		return radio_objectToRegularDoctorIn4Wks;
	}
	public void setRadio_objectToRegularDoctorIn4Wks(
			String radio_objectToRegularDoctorIn4Wks) {
		this.radio_objectToRegularDoctorIn4Wks = radio_objectToRegularDoctorIn4Wks;
	}
	public String getRadio_onlineCheck() {
		return radio_onlineCheck;
	}
	public void setRadio_onlineCheck(String radio_onlineCheck) {
		this.radio_onlineCheck = radio_onlineCheck;
	}
	public String getRadio_otherHealthConcern() {
		return radio_otherHealthConcern;
	}
	public void setRadio_otherHealthConcern(String radio_otherHealthConcern) {
		this.radio_otherHealthConcern = radio_otherHealthConcern;
	}
	public String getRadio_pamphletIssued() {
		return radio_pamphletIssued;
	}
	public void setRadio_pamphletIssued(String radio_pamphletIssued) {
		this.radio_pamphletIssued = radio_pamphletIssued;
	}
	public String getRadio_rateOverallHealth() {
		return radio_rateOverallHealth;
	}
	public void setRadio_rateOverallHealth(String radio_rateOverallHealth) {
		this.radio_rateOverallHealth = radio_rateOverallHealth;
	}
	public String getRadio_seatonTour() {
		return radio_seatonTour;
	}
	public void setRadio_seatonTour(String radio_seatonTour) {
		this.radio_seatonTour = radio_seatonTour;
	}
	public String getRadio_seeDoctor() {
		return radio_seeDoctor;
	}
	public void setRadio_seeDoctor(String radio_seeDoctor) {
		this.radio_seeDoctor = radio_seeDoctor;
	}
	public String getRadio_seeSameDoctor() {
		return radio_seeSameDoctor;
	}
	public void setRadio_seeSameDoctor(String radio_seeSameDoctor) {
		this.radio_seeSameDoctor = radio_seeSameDoctor;
	}
	public String getRadio_speakToResearcher() {
		return radio_speakToResearcher;
	}
	public void setRadio_speakToResearcher(String radio_speakToResearcher) {
		this.radio_speakToResearcher = radio_speakToResearcher;
	}
	public String getRadio_takeMedication() {
		return radio_takeMedication;
	}
	public void setRadio_takeMedication(String radio_takeMedication) {
		this.radio_takeMedication = radio_takeMedication;
	}
	public String getReactionToMedication() {
		return reactionToMedication;
	}
	public void setReactionToMedication(String reactionToMedication) {
		this.reactionToMedication = reactionToMedication;
	}
	public String getReasonToSeaton() {
		return reasonToSeaton;
	}
	public void setReasonToSeaton(String reasonToSeaton) {
		this.reasonToSeaton = reasonToSeaton;
	}
	public String getRefugeeNum() {
		return refugeeNum;
	}
	public void setRefugeeNum(String refugeeNum) {
		this.refugeeNum = refugeeNum;
	}
	public String getSeatonNotToured() {
		return seatonNotToured;
	}
	public void setSeatonNotToured(String seatonNotToured) {
		this.seatonNotToured = seatonNotToured;
	}
	public String getSinNum() {
		return sinNum;
	}
	public void setSinNum(String sinNum) {
		this.sinNum = sinNum;
	}
	public String getSpeakOther() {
		return speakOther;
	}
	public void setSpeakOther(String speakOther) {
		this.speakOther = speakOther;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTreatOtherReasons() {
		return treatOtherReasons;
	}
	public void setTreatOtherReasons(String treatOtherReasons) {
		this.treatOtherReasons = treatOtherReasons;
	}
	public String getWorkerNum() {
		return workerNum;
	}
	public void setWorkerNum(String workerNum) {
		this.workerNum = workerNum;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCbox_sexFemale() {
		return cbox_sexFemale;
	}
	public void setCbox_sexFemale(String cbox_sexFemale) {
		this.cbox_sexFemale = cbox_sexFemale;
	}
	public String getCbox_sexMale() {
		return cbox_sexMale;
	}
	public void setCbox_sexMale(String cbox_sexMale) {
		this.cbox_sexMale = cbox_sexMale;
	}
	public String getCbox_sexTransgendered() {
		return cbox_sexTransgendered;
	}
	public void setCbox_sexTransgendered(String cbox_sexTransgendered) {
		this.cbox_sexTransgendered = cbox_sexTransgendered;
	}

//##########################################################################
/*	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = super.validate(mapping, request);
		
		if(clientFirstName.equals(""))
		{
			errors.add("clientFirstName", new ActionError("errors.required"));
		}
		return errors;
	}
*/	
//##########################################################################	
}
