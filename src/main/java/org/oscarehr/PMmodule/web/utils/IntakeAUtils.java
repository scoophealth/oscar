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

package org.oscarehr.PMmodule.web.utils;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.struts.validator.LazyValidatorForm;
import org.oscarehr.PMmodule.model.Formintakea;
import org.oscarehr.PMmodule.utility.Utility;
import org.oscarehr.common.model.Demographic;

public class IntakeAUtils {

	public static void setHealthCardInfoForIntakeA(Formintakea newIntakeA, Demographic clientObj) 
	{
		if(clientObj != null) {
			if(clientObj.getHin()!= null && clientObj.getHin().length() > 0)
			{
				newIntakeA.setHealthCardNum( clientObj.getHin() );
				newIntakeA.setHealthCardVer( clientObj.getVer() );
				newIntakeA.setCboxHealthcard("Y");
			}
		}
	}

	public static void setDateOfBirthForIntakeA(Formintakea newIntakeA, Demographic clientObj) 
	{

		newIntakeA.setYear(  clientObj.getYearOfBirth() );
		newIntakeA.setMonth( clientObj.getMonthOfBirth() );
		newIntakeA.setDay(  clientObj.getDateOfBirth() );

	}
	
	public static Formintakea updateBirthDateOfIntakeAFromDemographicIfDifferent(
			Formintakea intakeAClientInfo, Demographic clientObj)
	{	
			if(intakeAClientInfo != null  &&  clientObj !=  null)
			{//if IntakeA exists, compare and replace birthdate with demographic's
					
				if( !intakeAClientInfo.getYear().equals( clientObj.getYearOfBirth() )  &&
					!intakeAClientInfo.getMonth().equals( clientObj.getMonthOfBirth() )  &&
					!intakeAClientInfo.getDay().equals( clientObj.getDateOfBirth() ) &&
				    !clientObj.getYearOfBirth().equals("")  &&
				    !clientObj.getYearOfBirth().equals("")  &&
				 	!clientObj.getYearOfBirth().equals("")
				    )
				{
					intakeAClientInfo.setYear( clientObj.getYearOfBirth() );
					intakeAClientInfo.setMonth( clientObj.getMonthOfBirth() );
					intakeAClientInfo.setDay( clientObj.getDateOfBirth() );
				}
			}
		
		return intakeAClientInfo;
	}	
	
	public static Formintakea setIntakeA(
			Formintakea intakeA, 
			LazyValidatorForm dynaForm, String actionType)
	{
		try
		{
			if(dynaForm == null  ||  intakeA == null)
			{
				return null;
			}
	 
			intakeA.setProviderNo( Long.valueOf(Utility.convertToRelacementStrIfNull((String)dynaForm.get("provider_no"), "-1")));

			java.util.Date formCreated = Calendar.getInstance().getTime();
			
			if((actionType != null  &&  !actionType.equals("update")) )
			{
				formCreated = Calendar.getInstance().getTime(); 
			}
		    
		    intakeA.setFormCreated( Utility.escapeNull(formCreated));
		    Timestamp formEdited = new Timestamp(Calendar.getInstance().getTimeInMillis());
		    intakeA.setFormEdited(formEdited);
		    
	    	intakeA.setClientFirstName(Utility.filterSQLFromHacking((String)dynaForm.get("clientFirstName")));
	    	intakeA.setClientSurname(Utility.filterSQLFromHacking((String)dynaForm.get("clientSurname")));

		    intakeA.setNamesOfMedication(Utility.escapeNull((String)dynaForm.get("namesOfMedication")));
		    intakeA.setRadioHasdoctor(Utility.escapeNull((String)dynaForm.get("radio_hasDoctor")));
		    intakeA.setHealthIssueDetails(Utility.escapeNull((String)dynaForm.get("healthIssueDetails")));
		    intakeA.setCboxOdsp(Utility.escapeNull((String)dynaForm.get("cbox_ODSP")));
		    intakeA.setCboxFortyork(Utility.escapeNull((String)dynaForm.get("cbox_fortYork")));
		    intakeA.setCboxIdnone(Utility.escapeNull((String)dynaForm.get("cbox_idNone")));
		    intakeA.setReactionToMedication(Utility.escapeNull((String)dynaForm.get("reactionToMedication")));
		    intakeA.setSeatonNotToured(Utility.escapeNull((String)dynaForm.get("seatonNotToured")));
		    intakeA.setRadioOtherhealthconcern(Utility.escapeNull((String)dynaForm.get("radio_otherHealthConcern")));
		    intakeA.setCboxOneillhouse(Utility.escapeNull((String)dynaForm.get("cbox_oNeillHouse")));
		    intakeA.setContactName(Utility.escapeNull((String)dynaForm.get("contactName")));
		    intakeA.setRadioDidnotreceivehealthcare(Utility.escapeNull((String)dynaForm.get("radio_didNotReceiveHealthCare")));
		    intakeA.setCboxTreatphysicalhealth(Utility.escapeNull((String)dynaForm.get("cbox_treatPhysicalHealth")));
		    intakeA.setCboxIdfiled(Utility.escapeNull((String)dynaForm.get("cbox_idFiled")));
		    intakeA.setCboxTreatinjury(Utility.escapeNull((String)dynaForm.get("cbox_treatInjury")));
		    intakeA.setCboxOas(Utility.escapeNull((String)dynaForm.get("cbox_OAS")));
		    intakeA.setCboxOtherincome(Utility.escapeNull((String)dynaForm.get("cbox_OtherIncome")));
		    intakeA.setCboxNewclient(Utility.escapeNull((String)dynaForm.get("cbox_newClient")));
		    intakeA.setRadioSeedoctor(Utility.escapeNull((String)dynaForm.get("radio_seeDoctor")));
		    intakeA.setRadioMentalhealthconcerns(Utility.escapeNull((String)dynaForm.get("radio_mentalHealthConcerns")));
		    intakeA.setRadioObjecttoregulardoctorin4wks(Utility.escapeNull((String)dynaForm.get("radio_objectToRegularDoctorIn4Wks")));
		    intakeA.setHelpObtainMedication(Utility.escapeNull((String)dynaForm.get("helpObtainMedication")));
		    intakeA.setSummary(Utility.escapeNull((String)dynaForm.get("summary")));
		    intakeA.setDay(Utility.escapeNull((String)dynaForm.get("day")));
		    intakeA.setCboxSpeakfrench(Utility.escapeNull((String)dynaForm.get("cbox_speakFrench")));
		    intakeA.setDoctorPhone(Utility.escapeNull((String)dynaForm.get("doctorPhone")));
		    intakeA.setRadioHasdrinkingproblem(Utility.escapeNull((String)dynaForm.get("radio_hasDrinkingProblem")));
		    intakeA.setCboxDownsviewdells(Utility.escapeNull((String)dynaForm.get("cbox_downsviewDells")));
		    intakeA.setCboxLongtermprogram(Utility.escapeNull((String)dynaForm.get("cbox_longTermProgram")));
		    intakeA.setCboxGotoothers(Utility.escapeNull((String)dynaForm.get("cbox_goToOthers")));
		    intakeA.setSpeakOther(Utility.escapeNull((String)dynaForm.get("speakOther")));
		    intakeA.setLastIssueDate(Utility.escapeNull((String)dynaForm.get("lastIssueDate")));
		    intakeA.setRadioHasbehaviorproblem(Utility.escapeNull((String)dynaForm.get("radio_hasBehaviorProblem")));
		    intakeA.setRadioNeedregulardoctor(Utility.escapeNull((String)dynaForm.get("radio_needRegularDoctor")));
		    intakeA.setGoToOthers(Utility.escapeNull((String)dynaForm.get("goToOthers")));
		    intakeA.setRadioHelpobtainmedication(Utility.escapeNull((String)dynaForm.get("radio_helpObtainMedication")));
		    intakeA.setCboxAssistinhealth(Utility.escapeNull((String)dynaForm.get("cbox_assistInHealth")));
		    intakeA.setCboxGotowalkinclinic(Utility.escapeNull((String)dynaForm.get("cbox_goToWalkInClinic")));
		    intakeA.setCboxNoid(Utility.escapeNull((String)dynaForm.get("cbox_noID")));
		    intakeA.setCboxPamphletissued(Utility.escapeNull((String)dynaForm.get("cbox_pamphletIssued")));
		    intakeA.setCboxNorecord(Utility.escapeNull((String)dynaForm.get("cbox_noRecord")));
		    intakeA.setCboxOw(Utility.escapeNull((String)dynaForm.get("cbox_OW")));
		    intakeA.setAssessCompleteTime(Utility.escapeNull((String)dynaForm.get("assessCompleteTime")));
		    intakeA.setAssessDate(Utility.escapeNull((String)dynaForm.get("assessDate")));
		    intakeA.setCboxHearingimpair(Utility.escapeNull((String)dynaForm.get("cbox_hearingImpair")));
		    intakeA.setCboxVisualimpair(Utility.escapeNull((String)dynaForm.get("cbox_visualImpair")));
		    intakeA.setCboxBirchmountresidence(Utility.escapeNull((String)dynaForm.get("cbox_birchmountResidence")));
		    intakeA.setCboxEi(Utility.escapeNull((String)dynaForm.get("cbox_EI")));
		    intakeA.setRadioHasdrugproblem(Utility.escapeNull((String)dynaForm.get("radio_hasDrugProblem")));
		    intakeA.setCboxVisithealthcenter(Utility.escapeNull((String)dynaForm.get("cbox_visitHealthCenter")));
		    intakeA.setCboxRotaryclub(Utility.escapeNull((String)dynaForm.get("cbox_rotaryClub")));
		    intakeA.setCboxHealthcard(Utility.escapeNull((String)dynaForm.get("cbox_healthCard")));
		    intakeA.setTreatOtherReasons(Utility.escapeNull((String)dynaForm.get("treatOtherReasons")));
		    intakeA.setMobilityImpair(Utility.escapeNull((String)dynaForm.get("mobilityImpair")));
		    intakeA.setRadioSeatontour(Utility.escapeNull((String)dynaForm.get("radio_seatonTour")));
		    intakeA.setRadioHealthissue(Utility.escapeNull((String)dynaForm.get("radio_healthIssue")));
		    intakeA.setCboxAssistinhousing(Utility.escapeNull((String)dynaForm.get("cbox_assistInHousing")));
		    intakeA.setAmtReceived(Utility.escapeNull((String)dynaForm.get("amtReceived")));
		    intakeA.setCboxVisithealthoffice(Utility.escapeNull((String)dynaForm.get("cbox_visitHealthOffice")));
		    intakeA.setCompletedBy(Utility.escapeNull((String)dynaForm.get("completedBy")));
		    intakeA.setContactPhone(Utility.escapeNull((String)dynaForm.get("contactPhone")));
		    intakeA.setCboxImmigrant(Utility.escapeNull((String)dynaForm.get("cbox_immigrant")));
		    intakeA.setCboxDateofreadmission(Utility.escapeNull((String)dynaForm.get("cbox_dateOfReadmission")));
		    intakeA.setCboxIsstatementread(Utility.escapeNull((String)dynaForm.get("cbox_isStatementRead")));
		    intakeA.setCboxTreatotherreasons(Utility.escapeNull((String)dynaForm.get("cbox_treatOtherReasons")));
		    intakeA.setCommentsOnID(Utility.escapeNull((String)dynaForm.get("commentsOnID")));
		    intakeA.setCboxVisitothers(Utility.escapeNull((String)dynaForm.get("cbox_visitOthers")));
		    intakeA.setAssessStartTime(Utility.escapeNull((String)dynaForm.get("assessStartTime")));
		    intakeA.setCboxInsulin(Utility.escapeNull((String)dynaForm.get("cbox_insulin")));
		    intakeA.setRadioSeesamedoctor(Utility.escapeNull((String)dynaForm.get("radio_seeSameDoctor")));
		    intakeA.setRadioSpeaktoresearcher(Utility.escapeNull((String)dynaForm.get("radio_speakToResearcher")));
		    intakeA.setCboxSpeakenglish(Utility.escapeNull((String)dynaForm.get("cbox_speakEnglish")));
		    intakeA.setCboxAssistinidentification(Utility.escapeNull((String)dynaForm.get("cbox_assistInIdentification")));
		    intakeA.setContactRelationship(Utility.escapeNull((String)dynaForm.get("contactRelationship")));
		    intakeA.setCboxTreatmentalhealth(Utility.escapeNull((String)dynaForm.get("cbox_treatMentalHealth")));
		    intakeA.setDatesAtSeaton(Utility.escapeNull((String)dynaForm.get("datesAtSeaton")));
		    intakeA.setRadioRateoverallhealth(Utility.escapeNull((String)dynaForm.get("radio_rateOverallHealth")));
		    intakeA.setHealthCardNum(Utility.escapeNull((String)dynaForm.get("healthCardNum")));
		    intakeA.setHealthCardVer(Utility.escapeNull((String)dynaForm.get("healthCardVer")));

		    intakeA.setOffice(Utility.escapeNull((String)dynaForm.get("office")));
		    intakeA.setRadioNeedseatonservice(Utility.escapeNull((String)dynaForm.get("radio_needSeatonService")));
		    intakeA.setCboxBleeding(Utility.escapeNull((String)dynaForm.get("cbox_bleeding")));
		    intakeA.setCboxRegularcheckup(Utility.escapeNull((String)dynaForm.get("cbox_regularCheckup")));
		    intakeA.setWorkerNum(Utility.escapeNull((String)dynaForm.get("workerNum")));
		    intakeA.setCboxHasdiabetes(Utility.escapeNull((String)dynaForm.get("cbox_hasDiabetes")));
		    intakeA.setCboxVisitwalkinclinic(Utility.escapeNull((String)dynaForm.get("cbox_visitWalkInClinic")));
		    intakeA.setMonth(Utility.escapeNull((String)dynaForm.get("month")));
		    intakeA.setRadioAllergictomedication(Utility.escapeNull((String)dynaForm.get("radio_allergicToMedication")));
		    intakeA.setCboxCpp(Utility.escapeNull((String)dynaForm.get("cbox_CPP")));
		    intakeA.setFrequencyOfSeeingDoctor(Utility.escapeNull((String)dynaForm.get("frequencyOfSeeingDoctor")));
		    intakeA.setCboxOtherid(Utility.escapeNull((String)dynaForm.get("cbox_otherID")));
		    intakeA.setCboxAssistineducation(Utility.escapeNull((String)dynaForm.get("cbox_assistInEducation")));
		    intakeA.setCboxAnnexharm(Utility.escapeNull((String)dynaForm.get("cbox_annexHarm")));
		    intakeA.setCboxVisitemergencyroom(Utility.escapeNull((String)dynaForm.get("cbox_visitEmergencyRoom")));
		    intakeA.setCboxMobilityimpair(Utility.escapeNull((String)dynaForm.get("cbox_mobilityImpair")));
		    intakeA.setCboxHealthoffice(Utility.escapeNull((String)dynaForm.get("cbox_HealthOffice")));
		    intakeA.setCboxEpilepsy(Utility.escapeNull((String)dynaForm.get("cbox_epilepsy")));
		    intakeA.setRadioPamphletissued(Utility.escapeNull((String)dynaForm.get("radio_pamphletIssued")));
		    intakeA.setCboxCitzenshipcard(Utility.escapeNull((String)dynaForm.get("cbox_citzenshipCard")));
		    intakeA.setCboxAssistinaddictions(Utility.escapeNull((String)dynaForm.get("cbox_assistInAddictions")));
		    intakeA.setCboxAssistinfinance(Utility.escapeNull((String)dynaForm.get("cbox_assistInFinance")));
		    intakeA.setRadioActive(Utility.escapeNull((String)dynaForm.get("radio_active")));
		    intakeA.setCboxEmployment(Utility.escapeNull((String)dynaForm.get("cbox_Employment")));
		    intakeA.setMentalHealthConcerns(Utility.escapeNull((String)dynaForm.get("mentalHealthConcerns")));
		    intakeA.setRadioTakemedication(Utility.escapeNull((String)dynaForm.get("radio_takeMedication")));
		    intakeA.setCboxHostel(Utility.escapeNull((String)dynaForm.get("cbox_hostel")));
		    intakeA.setDoctorAddress(Utility.escapeNull((String)dynaForm.get("doctorAddress")));
		    intakeA.setDoctorName(Utility.escapeNull((String)dynaForm.get("doctorName")));
		    intakeA.setCboxGotohealthcenter(Utility.escapeNull((String)dynaForm.get("cbox_goToHealthCenter")));
		    intakeA.setRadioAppmtseedoctorin3mths(Utility.escapeNull((String)dynaForm.get("radio_appmtSeeDoctorIn3Mths")));
		    intakeA.setCboxAssistinlegal(Utility.escapeNull((String)dynaForm.get("cbox_assistInLegal")));
		    intakeA.setCboxSincard(Utility.escapeNull((String)dynaForm.get("cbox_sinCard")));
		    intakeA.setRadioHasmentalillness(Utility.escapeNull((String)dynaForm.get("radio_hasMentalIllness")));
		    intakeA.setOtherIdentification(Utility.escapeNull((String)dynaForm.get("otherIdentification")));
		    intakeA.setCboxBirthcertificate(Utility.escapeNull((String)dynaForm.get("cbox_birthCertificate")));
		    intakeA.setCboxAssistinimmigration(Utility.escapeNull((String)dynaForm.get("cbox_assistInImmigration")));
		    intakeA.setContactAddress(Utility.escapeNull((String)dynaForm.get("contactAddress")));
		    intakeA.setCboxSpeakother(Utility.escapeNull((String)dynaForm.get("cbox_speakOther")));
		    
		    intakeA.setPamphletNotIssued(Utility.escapeNull((String)dynaForm.get("pamphletNotIssued")));
		    intakeA.setRadioHashealthproblem(Utility.escapeNull((String)dynaForm.get("radio_hasHealthProblem")));
		    intakeA.setDateOfReadmission(Utility.escapeNull((String)dynaForm.get("dateOfReadmission")));
		    intakeA.setRadioOnlinecheck(Utility.escapeNull((String)dynaForm.get("radio_onlineCheck")));
		    intakeA.setAllergicToMedicationName(Utility.escapeNull((String)dynaForm.get("allergicToMedicationName")));
		    intakeA.setCboxGotoemergencyroom(Utility.escapeNull((String)dynaForm.get("cbox_goToEmergencyRoom")));
		    intakeA.setFrequencyOfSeeingEmergencyRoomDoctor(Utility.escapeNull((String)dynaForm.get("frequencyOfSeeingEmergencyRoomDoctor")));
		    intakeA.setCboxWsib(Utility.escapeNull((String)dynaForm.get("cbox_WSIB")));
		    intakeA.setCboxRefugee(Utility.escapeNull((String)dynaForm.get("cbox_refugee")));
		    intakeA.setDoctorPhoneExt(Utility.escapeNull((String)dynaForm.get("doctorPhoneExt")));
		    intakeA.setYear(Utility.escapeNull((String)dynaForm.get("year")));
		    intakeA.setCboxAssistinemployment(Utility.escapeNull((String)dynaForm.get("cbox_assistInEmployment")));
		    intakeA.setOtherHealthConerns(Utility.escapeNull((String)dynaForm.get("otherHealthConerns")));
		    intakeA.setEnterSeatonDate(Utility.escapeNull((String)dynaForm.get("enterSeatonDate")));
		    intakeA.setReasonToSeaton(Utility.escapeNull((String)dynaForm.get("reasonToSeaton")));

		    intakeA.setSinNum(Utility.escapeNull((String)dynaForm.get("sinNum")));
		    intakeA.setBirthCertificateNum(Utility.escapeNull((String)dynaForm.get("birthCertificateNum")));
		    intakeA.setCitzenshipCardNum(Utility.escapeNull((String)dynaForm.get("citzenshipCardNum")));
		    intakeA.setImmigrantNum(Utility.escapeNull((String)dynaForm.get("immigrantNum")));
		    intakeA.setRefugeeNum(Utility.escapeNull((String)dynaForm.get("refugeeNum")));
		    intakeA.setCboxIsstatement6read(Utility.escapeNull((String)dynaForm.get("cbox_isStatement6Read")));

		    intakeA.setCboxSharing(Utility.escapeNull((String)dynaForm.get("cbox_sharing")));
		    intakeA.setRadioSex(Utility.escapeNull((String)dynaForm.get("radio_sex")));
		    
		    intakeA.setEffDate(Utility.escapeNull((String)dynaForm.get("effDate")));
		    
		}
		catch(NumberFormatException numEx)
		{
			return null;
		}
		return intakeA;
	}
}
