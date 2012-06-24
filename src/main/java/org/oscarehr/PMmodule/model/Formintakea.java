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

package org.oscarehr.PMmodule.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * This is the object class that relates to the formintakea table.
 * Any customizations belong here.
 */
public class Formintakea implements Serializable {

    private int hashCode = Integer.MIN_VALUE;// primary key

    private Long _id;// fields
    private String _radioSex;
    private String _effDate;
    private String _cboxSharing;
    private String _namesOfMedication;
    private String _radioHasdoctor;
    private String _healthIssueDetails;
    private String _cboxOdsp;
    private String _cboxFortyork;
    private String _cboxIdnone;
    private String _reactionToMedication;
    private String _seatonNotToured;
    private String _radioOtherhealthconcern;
    private String _cboxOneillhouse;
    private String _contactName;
    private String _radioDidnotreceivehealthcare;
    private String _cboxTreatphysicalhealth;
    private String _cboxIdfiled;
    private String _cboxTreatinjury;
    private String _cboxOas;
    private String _cboxOtherincome;
    private String _cboxNewclient;
    private String _radioSeedoctor;
    private String _radioMentalhealthconcerns;
    private String _radioObjecttoregulardoctorin4wks;
    private String _helpObtainMedication;
    private String _summary;
    private String _day;
    private String _cboxSpeakfrench;
    private String _cboxIsstatement6read;
    private String _hasWhatID;
    private String _doctorPhone;
    private String _radioHasdrinkingproblem;
    private String _cboxDownsviewdells;
    private String _cboxLongtermprogram;
    private String _cboxGotoothers;
    private String _speakOther;
    private String _lastIssueDate;
    private String _refugeeNum;
    private String _radioHasbehaviorproblem;
    private String _radioNeedregulardoctor;
    private String _goToOthers;
    private String _radioHelpobtainmedication;
    private String _cboxAssistinhealth;
    private String _cboxGotowalkinclinic;
    private String _cboxNoid;
    private String _cboxPamphletissued;
    private String _cboxIsstatementread;
    private String _cboxNorecord;
    private String _cboxOw;
    private String _assessCompleteTime;
    private String _assessDate;
    private String _cboxHearingimpair;
    private String _cboxVisualimpair;
    private String _cboxBirchmountresidence;
    private String _cboxEi;
    private String _radioHasdrugproblem;
    private String _cboxVisithealthcenter;
    private String _citzenshipCardNum;
    private String _cboxRotaryclub;
    private String _cboxHealthcard;
    private String _clientSurname;
    private String _treatOtherReasons;
    private String _mobilityImpair;
    private String _radioSeatontour;
    private String _radioHealthissue;
    private String _cboxAssistinhousing;
    private String _amtReceived;
    private String _cboxVisithealthoffice;
    private String _completedBy;
    private String _contactPhone;
    private String _cboxImmigrant;
    private String _healthCardVer;
    private String _doctorName2;
    private String _cboxDateofreadmission;
    private String _cboxTreatotherreasons;
    private String _cboxVisitothers;
    private String _commentsOnID;
    private String _assessStartTime;
    private String _immigrantNum;
    private String _cboxInsulin;
    private String _radioSeesamedoctor;
    private String _radioSpeaktoresearcher;
    private String _cboxAssistinidentification;
    private String _cboxSpeakenglish;
    private String _contactRelationship;
    private String _cboxTreatmentalhealth;
    private String _datesAtSeaton;
    private String _sinNum;
    private String _radioRateoverallhealth;
    private String _healthCardNum;
    private String _office;
    private String _radioNeedseatonservice;
    private String _cboxBleeding;
    private String _cboxRegularcheckup;
    private String _workerNum;
    private String _cboxVisitwalkinclinic;
    private String _cboxHasdiabetes;
    private String _birthCertificateNum;
    private String _clientFirstName;
    private String _month;
    private String _radioAllergictomedication;
    private String _cboxCpp;
    private String _cboxHostelFusionCare;
    private String _frequencyOfSeeingDoctor;
    private String _cboxOtherid;
    private String _cboxAssistineducation;
    private String _cboxAnnexharm;
    private String _cboxVisitemergencyroom;
    private String _cboxMobilityimpair;
    private String _cboxHealthoffice;
    private String _cboxEpilepsy;
    private String _radioPamphletissued;
    private Date _formEdited;
    private String _otherSpecify;
    private String _cboxCitzenshipcard;
    private String _cboxAssistinaddictions;
    private String _cboxAssistinfinance;
    private String _radioActive;
    private Long _providerNo;
    private String _cboxEmployment;
    private String _mentalHealthConcerns;
    private String _radioTakemedication;
    private String _cboxHostel;
    private String _doctorAddress;
    private String _doctorName;
    private String _cboxGotohealthcenter;
    private String _radioAppmtseedoctorin3mths;
    private String _cboxAssistinlegal;
    private String _everAtSeatonBefore;
    private String _cboxSincard;
    private String _radioHasmentalillness;
    private String _otherIdentification;
    private String _cboxBirthcertificate;
    private String _cboxAssistinimmigration;
    private String _contactAddress;
    private String _cboxSpeakother;
    private String _pamphletNotIssued;
    private String _radioHashealthproblem;
    private String _dateOfReadmission;
    private String _radioOnlinecheck;
    private String _allergicToMedicationName;
    private Date _formCreated;
    private Long _demographicNo;
    private String _cboxGotoemergencyroom;
    private String _frequencyOfSeeingEmergencyRoomDoctor;
    private String _cboxWsib;
    private String _cboxRefugee;
    private String _doctorPhoneExt;
    private String _year;
    private String _cboxAssistinemployment;
    private String _otherHealthConerns;
    private String _enterSeatonDate;
    private String _reasonToSeaton;

   // constructors
	public Formintakea () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Formintakea (Long _id) {
		this.setId(_id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public Formintakea (
		Long _id,
		Long _demographicNo) {

		this.setId(_id);
		this.setDemographicNo(_demographicNo);
		initialize();
	}
	
	public void setDOB(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setDay(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		setMonth(String.valueOf(cal.get(Calendar.MONTH)+1));
		setYear(String.valueOf(cal.get(Calendar.YEAR)));
	}

    protected void initialize () {}

    /**
	 * Return the unique identifier of this class
* @hibernate.id
*  generator-class="native"
*  column="ID"
*/
    public Long getId () {
        return _id;
    }

    /**
	 * Set the unique identifier of this class
     * @param _id the new ID
     */
    public void setId (Long _id) {
        this._id = _id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: radio_sex
     */
    public String getRadioSex () {
        return _radioSex;
    }

    /**
	 * Set the value related to the column: radio_sex
     * @param _radioSex the radio_sex value
     */
    public void setRadioSex (String _radioSex) {
        this._radioSex = _radioSex;
    }

    /**
	 * Return the value associated with the column: effDate
     */
    public String getEffDate () {
        return _effDate;
    }

    /**
	 * Set the value related to the column: effDate
     * @param _effDate the effDate value
     */
    public void setEffDate (String _effDate) {
        this._effDate = _effDate;
    }

    /**
	 * Return the value associated with the column: cbox_sharing
     */
    public String getCboxSharing () {
        return _cboxSharing;
    }

    /**
	 * Set the value related to the column: cbox_sharing
     * @param _cboxSharing the cbox_sharing value
     */
    public void setCboxSharing (String _cboxSharing) {
        this._cboxSharing = _cboxSharing;
    }

    /**
	 * Return the value associated with the column: namesOfMedication
     */
    public String getNamesOfMedication () {
        return _namesOfMedication;
    }

    /**
	 * Set the value related to the column: namesOfMedication
     * @param _namesOfMedication the namesOfMedication value
     */
    public void setNamesOfMedication (String _namesOfMedication) {
        this._namesOfMedication = _namesOfMedication;
    }

    /**
	 * Return the value associated with the column: radio_hasDoctor
     */
    public String getRadioHasdoctor () {
        return _radioHasdoctor;
    }

    /**
	 * Set the value related to the column: radio_hasDoctor
     * @param _radioHasdoctor the radio_hasDoctor value
     */
    public void setRadioHasdoctor (String _radioHasdoctor) {
        this._radioHasdoctor = _radioHasdoctor;
    }

    /**
	 * Return the value associated with the column: healthIssueDetails
     */
    public String getHealthIssueDetails () {
        return _healthIssueDetails;
    }

    /**
	 * Set the value related to the column: healthIssueDetails
     * @param _healthIssueDetails the healthIssueDetails value
     */
    public void setHealthIssueDetails (String _healthIssueDetails) {
        this._healthIssueDetails = _healthIssueDetails;
    }

    /**
	 * Return the value associated with the column: cbox_ODSP
     */
    public String getCboxOdsp () {
        return _cboxOdsp;
    }

    /**
	 * Set the value related to the column: cbox_ODSP
     * @param _cboxOdsp the cbox_ODSP value
     */
    public void setCboxOdsp (String _cboxOdsp) {
        this._cboxOdsp = _cboxOdsp;
    }

    /**
	 * Return the value associated with the column: cbox_fortYork
     */
    public String getCboxFortyork () {
        return _cboxFortyork;
    }

    /**
	 * Set the value related to the column: cbox_fortYork
     * @param _cboxFortyork the cbox_fortYork value
     */
    public void setCboxFortyork (String _cboxFortyork) {
        this._cboxFortyork = _cboxFortyork;
    }

    /**
	 * Return the value associated with the column: cbox_idNone
     */
    public String getCboxIdnone () {
        return _cboxIdnone;
    }

    /**
	 * Set the value related to the column: cbox_idNone
     * @param _cboxIdnone the cbox_idNone value
     */
    public void setCboxIdnone (String _cboxIdnone) {
        this._cboxIdnone = _cboxIdnone;
    }

    /**
	 * Return the value associated with the column: reactionToMedication
     */
    public String getReactionToMedication () {
        return _reactionToMedication;
    }

    /**
	 * Set the value related to the column: reactionToMedication
     * @param _reactionToMedication the reactionToMedication value
     */
    public void setReactionToMedication (String _reactionToMedication) {
        this._reactionToMedication = _reactionToMedication;
    }

    /**
	 * Return the value associated with the column: seatonNotToured
     */
    public String getSeatonNotToured () {
        return _seatonNotToured;
    }

    /**
	 * Set the value related to the column: seatonNotToured
     * @param _seatonNotToured the seatonNotToured value
     */
    public void setSeatonNotToured (String _seatonNotToured) {
        this._seatonNotToured = _seatonNotToured;
    }

    /**
	 * Return the value associated with the column: radio_otherHealthConcern
     */
    public String getRadioOtherhealthconcern () {
        return _radioOtherhealthconcern;
    }

    /**
	 * Set the value related to the column: radio_otherHealthConcern
     * @param _radioOtherhealthconcern the radio_otherHealthConcern value
     */
    public void setRadioOtherhealthconcern (String _radioOtherhealthconcern) {
        this._radioOtherhealthconcern = _radioOtherhealthconcern;
    }

    /**
	 * Return the value associated with the column: cbox_oNeillHouse
     */
    public String getCboxOneillhouse () {
        return _cboxOneillhouse;
    }

    /**
	 * Set the value related to the column: cbox_oNeillHouse
     * @param _cboxOneillhouse the cbox_oNeillHouse value
     */
    public void setCboxOneillhouse (String _cboxOneillhouse) {
        this._cboxOneillhouse = _cboxOneillhouse;
    }

    /**
	 * Return the value associated with the column: contactName
     */
    public String getContactName () {
        return _contactName;
    }

    /**
	 * Set the value related to the column: contactName
     * @param _contactName the contactName value
     */
    public void setContactName (String _contactName) {
        this._contactName = _contactName;
    }

    /**
	 * Return the value associated with the column: radio_didNotReceiveHealthCare
     */
    public String getRadioDidnotreceivehealthcare () {
        return _radioDidnotreceivehealthcare;
    }

    /**
	 * Set the value related to the column: radio_didNotReceiveHealthCare
     * @param _radioDidnotreceivehealthcare the radio_didNotReceiveHealthCare value
     */
    public void setRadioDidnotreceivehealthcare (String _radioDidnotreceivehealthcare) {
        this._radioDidnotreceivehealthcare = _radioDidnotreceivehealthcare;
    }

    /**
	 * Return the value associated with the column: cbox_treatPhysicalHealth
     */
    public String getCboxTreatphysicalhealth () {
        return _cboxTreatphysicalhealth;
    }

    /**
	 * Set the value related to the column: cbox_treatPhysicalHealth
     * @param _cboxTreatphysicalhealth the cbox_treatPhysicalHealth value
     */
    public void setCboxTreatphysicalhealth (String _cboxTreatphysicalhealth) {
        this._cboxTreatphysicalhealth = _cboxTreatphysicalhealth;
    }

    /**
	 * Return the value associated with the column: cbox_idFiled
     */
    public String getCboxIdfiled () {
        return _cboxIdfiled;
    }

    /**
	 * Set the value related to the column: cbox_idFiled
     * @param _cboxIdfiled the cbox_idFiled value
     */
    public void setCboxIdfiled (String _cboxIdfiled) {
        this._cboxIdfiled = _cboxIdfiled;
    }

    /**
	 * Return the value associated with the column: cbox_treatInjury
     */
    public String getCboxTreatinjury () {
        return _cboxTreatinjury;
    }

    /**
	 * Set the value related to the column: cbox_treatInjury
     * @param _cboxTreatinjury the cbox_treatInjury value
     */
    public void setCboxTreatinjury (String _cboxTreatinjury) {
        this._cboxTreatinjury = _cboxTreatinjury;
    }

    /**
	 * Return the value associated with the column: cbox_OAS
     */
    public String getCboxOas () {
        return _cboxOas;
    }

    /**
	 * Set the value related to the column: cbox_OAS
     * @param _cboxOas the cbox_OAS value
     */
    public void setCboxOas (String _cboxOas) {
        this._cboxOas = _cboxOas;
    }

    /**
	 * Return the value associated with the column: cbox_OtherIncome
     */
    public String getCboxOtherincome () {
        return _cboxOtherincome;
    }

    /**
	 * Set the value related to the column: cbox_OtherIncome
     * @param _cboxOtherincome the cbox_OtherIncome value
     */
    public void setCboxOtherincome (String _cboxOtherincome) {
        this._cboxOtherincome = _cboxOtherincome;
    }

    /**
	 * Return the value associated with the column: cbox_newClient
     */
    public String getCboxNewclient () {
        return _cboxNewclient;
    }

    /**
	 * Set the value related to the column: cbox_newClient
     * @param _cboxNewclient the cbox_newClient value
     */
    public void setCboxNewclient (String _cboxNewclient) {
        this._cboxNewclient = _cboxNewclient;
    }

    /**
	 * Return the value associated with the column: radio_seeDoctor
     */
    public String getRadioSeedoctor () {
        return _radioSeedoctor;
    }

    /**
	 * Set the value related to the column: radio_seeDoctor
     * @param _radioSeedoctor the radio_seeDoctor value
     */
    public void setRadioSeedoctor (String _radioSeedoctor) {
        this._radioSeedoctor = _radioSeedoctor;
    }

    /**
	 * Return the value associated with the column: radio_mentalHealthConcerns
     */
    public String getRadioMentalhealthconcerns () {
        return _radioMentalhealthconcerns;
    }

    /**
	 * Set the value related to the column: radio_mentalHealthConcerns
     * @param _radioMentalhealthconcerns the radio_mentalHealthConcerns value
     */
    public void setRadioMentalhealthconcerns (String _radioMentalhealthconcerns) {
        this._radioMentalhealthconcerns = _radioMentalhealthconcerns;
    }

    /**
	 * Return the value associated with the column: radio_objectToRegularDoctorIn4Wks
     */
    public String getRadioObjecttoregulardoctorin4wks () {
        return _radioObjecttoregulardoctorin4wks;
    }

    /**
	 * Set the value related to the column: radio_objectToRegularDoctorIn4Wks
     * @param _radioObjecttoregulardoctorin4wks the radio_objectToRegularDoctorIn4Wks value
     */
    public void setRadioObjecttoregulardoctorin4wks (String _radioObjecttoregulardoctorin4wks) {
        this._radioObjecttoregulardoctorin4wks = _radioObjecttoregulardoctorin4wks;
    }

    /**
	 * Return the value associated with the column: helpObtainMedication
     */
    public String getHelpObtainMedication () {
        return _helpObtainMedication;
    }

    /**
	 * Set the value related to the column: helpObtainMedication
     * @param _helpObtainMedication the helpObtainMedication value
     */
    public void setHelpObtainMedication (String _helpObtainMedication) {
        this._helpObtainMedication = _helpObtainMedication;
    }

    /**
	 * Return the value associated with the column: summary
     */
    public String getSummary () {
        return _summary;
    }

    /**
	 * Set the value related to the column: summary
     * @param _summary the summary value
     */
    public void setSummary (String _summary) {
        this._summary = _summary;
    }

    /**
	 * Return the value associated with the column: day
     */
    public String getDay () {
        return(StringUtils.trimToEmpty(_day));
    }

    /**
	 * Set the value related to the column: day
     * @param _day the day value
     */
    public void setDay (String _day) {
        this._day = _day;
    }

    /**
	 * Return the value associated with the column: cbox_speakFrench
     */
    public String getCboxSpeakfrench () {
        return _cboxSpeakfrench;
    }

    /**
	 * Set the value related to the column: cbox_speakFrench
     * @param _cboxSpeakfrench the cbox_speakFrench value
     */
    public void setCboxSpeakfrench (String _cboxSpeakfrench) {
        this._cboxSpeakfrench = _cboxSpeakfrench;
    }

    /**
	 * Return the value associated with the column: cbox_isStatement6Read
     */
    public String getCboxIsstatement6read () {
        return _cboxIsstatement6read;
    }

    /**
	 * Set the value related to the column: cbox_isStatement6Read
     * @param _cboxIsstatement6read the cbox_isStatement6Read value
     */
    public void setCboxIsstatement6read (String _cboxIsstatement6read) {
        this._cboxIsstatement6read = _cboxIsstatement6read;
    }

    /**
	 * Return the value associated with the column: hasWhatID
     */
    public String getHasWhatID () {
        return _hasWhatID;
    }

    /**
	 * Set the value related to the column: hasWhatID
     * @param _hasWhatID the hasWhatID value
     */
    public void setHasWhatID (String _hasWhatID) {
        this._hasWhatID = _hasWhatID;
    }

    /**
	 * Return the value associated with the column: doctorPhone
     */
    public String getDoctorPhone () {
        return _doctorPhone;
    }

    /**
	 * Set the value related to the column: doctorPhone
     * @param _doctorPhone the doctorPhone value
     */
    public void setDoctorPhone (String _doctorPhone) {
        this._doctorPhone = _doctorPhone;
    }

    /**
	 * Return the value associated with the column: radio_hasDrinkingProblem
     */
    public String getRadioHasdrinkingproblem () {
        return _radioHasdrinkingproblem;
    }

    /**
	 * Set the value related to the column: radio_hasDrinkingProblem
     * @param _radioHasdrinkingproblem the radio_hasDrinkingProblem value
     */
    public void setRadioHasdrinkingproblem (String _radioHasdrinkingproblem) {
        this._radioHasdrinkingproblem = _radioHasdrinkingproblem;
    }

    /**
	 * Return the value associated with the column: cbox_downsviewDells
     */
    public String getCboxDownsviewdells () {
        return _cboxDownsviewdells;
    }

    /**
	 * Set the value related to the column: cbox_downsviewDells
     * @param _cboxDownsviewdells the cbox_downsviewDells value
     */
    public void setCboxDownsviewdells (String _cboxDownsviewdells) {
        this._cboxDownsviewdells = _cboxDownsviewdells;
    }

    /**
	 * Return the value associated with the column: cbox_longTermProgram
     */
    public String getCboxLongtermprogram () {
        return _cboxLongtermprogram;
    }

    /**
	 * Set the value related to the column: cbox_longTermProgram
     * @param _cboxLongtermprogram the cbox_longTermProgram value
     */
    public void setCboxLongtermprogram (String _cboxLongtermprogram) {
        this._cboxLongtermprogram = _cboxLongtermprogram;
    }

    /**
	 * Return the value associated with the column: cbox_goToOthers
     */
    public String getCboxGotoothers () {
        return _cboxGotoothers;
    }

    /**
	 * Set the value related to the column: cbox_goToOthers
     * @param _cboxGotoothers the cbox_goToOthers value
     */
    public void setCboxGotoothers (String _cboxGotoothers) {
        this._cboxGotoothers = _cboxGotoothers;
    }

    /**
	 * Return the value associated with the column: speakOther
     */
    public String getSpeakOther () {
        return _speakOther;
    }

    /**
	 * Set the value related to the column: speakOther
     * @param _speakOther the speakOther value
     */
    public void setSpeakOther (String _speakOther) {
        this._speakOther = _speakOther;
    }

    /**
	 * Return the value associated with the column: lastIssueDate
     */
    public String getLastIssueDate () {
        return _lastIssueDate;
    }

    /**
	 * Set the value related to the column: lastIssueDate
     * @param _lastIssueDate the lastIssueDate value
     */
    public void setLastIssueDate (String _lastIssueDate) {
        this._lastIssueDate = _lastIssueDate;
    }

    /**
	 * Return the value associated with the column: refugeeNum
     */
    public String getRefugeeNum () {
        return _refugeeNum;
    }

    /**
	 * Set the value related to the column: refugeeNum
     * @param _refugeeNum the refugeeNum value
     */
    public void setRefugeeNum (String _refugeeNum) {
        this._refugeeNum = _refugeeNum;
    }

    /**
	 * Return the value associated with the column: radio_hasBehaviorProblem
     */
    public String getRadioHasbehaviorproblem () {
        return _radioHasbehaviorproblem;
    }

    /**
	 * Set the value related to the column: radio_hasBehaviorProblem
     * @param _radioHasbehaviorproblem the radio_hasBehaviorProblem value
     */
    public void setRadioHasbehaviorproblem (String _radioHasbehaviorproblem) {
        this._radioHasbehaviorproblem = _radioHasbehaviorproblem;
    }

    /**
	 * Return the value associated with the column: radio_needRegularDoctor
     */
    public String getRadioNeedregulardoctor () {
        return _radioNeedregulardoctor;
    }

    /**
	 * Set the value related to the column: radio_needRegularDoctor
     * @param _radioNeedregulardoctor the radio_needRegularDoctor value
     */
    public void setRadioNeedregulardoctor (String _radioNeedregulardoctor) {
        this._radioNeedregulardoctor = _radioNeedregulardoctor;
    }

    /**
	 * Return the value associated with the column: goToOthers
     */
    public String getGoToOthers () {
        return _goToOthers;
    }

    /**
	 * Set the value related to the column: goToOthers
     * @param _goToOthers the goToOthers value
     */
    public void setGoToOthers (String _goToOthers) {
        this._goToOthers = _goToOthers;
    }

    /**
	 * Return the value associated with the column: radio_helpObtainMedication
     */
    public String getRadioHelpobtainmedication () {
        return _radioHelpobtainmedication;
    }

    /**
	 * Set the value related to the column: radio_helpObtainMedication
     * @param _radioHelpobtainmedication the radio_helpObtainMedication value
     */
    public void setRadioHelpobtainmedication (String _radioHelpobtainmedication) {
        this._radioHelpobtainmedication = _radioHelpobtainmedication;
    }

    /**
	 * Return the value associated with the column: cbox_assistInHealth
     */
    public String getCboxAssistinhealth () {
        return _cboxAssistinhealth;
    }

    /**
	 * Set the value related to the column: cbox_assistInHealth
     * @param _cboxAssistinhealth the cbox_assistInHealth value
     */
    public void setCboxAssistinhealth (String _cboxAssistinhealth) {
        this._cboxAssistinhealth = _cboxAssistinhealth;
    }

    /**
	 * Return the value associated with the column: cbox_goToWalkInClinic
     */
    public String getCboxGotowalkinclinic () {
        return _cboxGotowalkinclinic;
    }

    /**
	 * Set the value related to the column: cbox_goToWalkInClinic
     * @param _cboxGotowalkinclinic the cbox_goToWalkInClinic value
     */
    public void setCboxGotowalkinclinic (String _cboxGotowalkinclinic) {
        this._cboxGotowalkinclinic = _cboxGotowalkinclinic;
    }

    /**
	 * Return the value associated with the column: cbox_noID
     */
    public String getCboxNoid () {
        return _cboxNoid;
    }

    /**
	 * Set the value related to the column: cbox_noID
     * @param _cboxNoid the cbox_noID value
     */
    public void setCboxNoid (String _cboxNoid) {
        this._cboxNoid = _cboxNoid;
    }

    /**
	 * Return the value associated with the column: cbox_pamphletIssued
     */
    public String getCboxPamphletissued () {
        return _cboxPamphletissued;
    }

    /**
	 * Set the value related to the column: cbox_pamphletIssued
     * @param _cboxPamphletissued the cbox_pamphletIssued value
     */
    public void setCboxPamphletissued (String _cboxPamphletissued) {
        this._cboxPamphletissued = _cboxPamphletissued;
    }

    /**
	 * Return the value associated with the column: cbox_isStatementRead
     */
    public String getCboxIsstatementread () {
        return _cboxIsstatementread;
    }

    /**
	 * Set the value related to the column: cbox_isStatementRead
     * @param _cboxIsstatementread the cbox_isStatementRead value
     */
    public void setCboxIsstatementread (String _cboxIsstatementread) {
        this._cboxIsstatementread = _cboxIsstatementread;
    }

    /**
	 * Return the value associated with the column: cbox_noRecord
     */
    public String getCboxNorecord () {
        return _cboxNorecord;
    }

    /**
	 * Set the value related to the column: cbox_noRecord
     * @param _cboxNorecord the cbox_noRecord value
     */
    public void setCboxNorecord (String _cboxNorecord) {
        this._cboxNorecord = _cboxNorecord;
    }

    /**
	 * Return the value associated with the column: cbox_OW
     */
    public String getCboxOw () {
        return _cboxOw;
    }

    /**
	 * Set the value related to the column: cbox_OW
     * @param _cboxOw the cbox_OW value
     */
    public void setCboxOw (String _cboxOw) {
        this._cboxOw = _cboxOw;
    }

    /**
	 * Return the value associated with the column: assessCompleteTime
     */
    public String getAssessCompleteTime () {
        return _assessCompleteTime;
    }

    /**
	 * Set the value related to the column: assessCompleteTime
     * @param _assessCompleteTime the assessCompleteTime value
     */
    public void setAssessCompleteTime (String _assessCompleteTime) {
        this._assessCompleteTime = _assessCompleteTime;
    }

    /**
	 * Return the value associated with the column: assessDate
     */
    public String getAssessDate () {
        return _assessDate;
    }

    /**
	 * Set the value related to the column: assessDate
     * @param _assessDate the assessDate value
     */
    public void setAssessDate (String _assessDate) {
        this._assessDate = _assessDate;
    }

    /**
	 * Return the value associated with the column: cbox_hearingImpair
     */
    public String getCboxHearingimpair () {
        return _cboxHearingimpair;
    }

    /**
	 * Set the value related to the column: cbox_hearingImpair
     * @param _cboxHearingimpair the cbox_hearingImpair value
     */
    public void setCboxHearingimpair (String _cboxHearingimpair) {
        this._cboxHearingimpair = _cboxHearingimpair;
    }

    /**
	 * Return the value associated with the column: cbox_visualImpair
     */
    public String getCboxVisualimpair () {
        return _cboxVisualimpair;
    }

    /**
	 * Set the value related to the column: cbox_visualImpair
     * @param _cboxVisualimpair the cbox_visualImpair value
     */
    public void setCboxVisualimpair (String _cboxVisualimpair) {
        this._cboxVisualimpair = _cboxVisualimpair;
    }

    /**
	 * Return the value associated with the column: cbox_birchmountResidence
     */
    public String getCboxBirchmountresidence () {
        return _cboxBirchmountresidence;
    }

    /**
	 * Set the value related to the column: cbox_birchmountResidence
     * @param _cboxBirchmountresidence the cbox_birchmountResidence value
     */
    public void setCboxBirchmountresidence (String _cboxBirchmountresidence) {
        this._cboxBirchmountresidence = _cboxBirchmountresidence;
    }

    /**
	 * Return the value associated with the column: cbox_EI
     */
    public String getCboxEi () {
        return _cboxEi;
    }

    /**
	 * Set the value related to the column: cbox_EI
     * @param _cboxEi the cbox_EI value
     */
    public void setCboxEi (String _cboxEi) {
        this._cboxEi = _cboxEi;
    }

    /**
	 * Return the value associated with the column: radio_hasDrugProblem
     */
    public String getRadioHasdrugproblem () {
        return _radioHasdrugproblem;
    }

    /**
	 * Set the value related to the column: radio_hasDrugProblem
     * @param _radioHasdrugproblem the radio_hasDrugProblem value
     */
    public void setRadioHasdrugproblem (String _radioHasdrugproblem) {
        this._radioHasdrugproblem = _radioHasdrugproblem;
    }

    /**
	 * Return the value associated with the column: cbox_visitHealthCenter
     */
    public String getCboxVisithealthcenter () {
        return _cboxVisithealthcenter;
    }

    /**
	 * Set the value related to the column: cbox_visitHealthCenter
     * @param _cboxVisithealthcenter the cbox_visitHealthCenter value
     */
    public void setCboxVisithealthcenter (String _cboxVisithealthcenter) {
        this._cboxVisithealthcenter = _cboxVisithealthcenter;
    }

    /**
	 * Return the value associated with the column: citzenshipCardNum
     */
    public String getCitzenshipCardNum () {
        return _citzenshipCardNum;
    }

    /**
	 * Set the value related to the column: citzenshipCardNum
     * @param _citzenshipCardNum the citzenshipCardNum value
     */
    public void setCitzenshipCardNum (String _citzenshipCardNum) {
        this._citzenshipCardNum = _citzenshipCardNum;
    }

    /**
	 * Return the value associated with the column: cbox_rotaryClub
     */
    public String getCboxRotaryclub () {
        return _cboxRotaryclub;
    }

    /**
	 * Set the value related to the column: cbox_rotaryClub
     * @param _cboxRotaryclub the cbox_rotaryClub value
     */
    public void setCboxRotaryclub (String _cboxRotaryclub) {
        this._cboxRotaryclub = _cboxRotaryclub;
    }

    /**
	 * Return the value associated with the column: cbox_healthCard
     */
    public String getCboxHealthcard () {
        return _cboxHealthcard;
    }

    /**
	 * Set the value related to the column: cbox_healthCard
     * @param _cboxHealthcard the cbox_healthCard value
     */
    public void setCboxHealthcard (String _cboxHealthcard) {
        this._cboxHealthcard = _cboxHealthcard;
    }

    /**
	 * Return the value associated with the column: clientSurname
     */
    public String getClientSurname () {
        return _clientSurname;
    }

    /**
	 * Set the value related to the column: clientSurname
     * @param _clientSurname the clientSurname value
     */
    public void setClientSurname (String _clientSurname) {
        this._clientSurname = _clientSurname;
    }

    /**
	 * Return the value associated with the column: treatOtherReasons
     */
    public String getTreatOtherReasons () {
        return _treatOtherReasons;
    }

    /**
	 * Set the value related to the column: treatOtherReasons
     * @param _treatOtherReasons the treatOtherReasons value
     */
    public void setTreatOtherReasons (String _treatOtherReasons) {
        this._treatOtherReasons = _treatOtherReasons;
    }

    /**
	 * Return the value associated with the column: mobilityImpair
     */
    public String getMobilityImpair () {
        return _mobilityImpair;
    }

    /**
	 * Set the value related to the column: mobilityImpair
     * @param _mobilityImpair the mobilityImpair value
     */
    public void setMobilityImpair (String _mobilityImpair) {
        this._mobilityImpair = _mobilityImpair;
    }

    /**
	 * Return the value associated with the column: radio_seatonTour
     */
    public String getRadioSeatontour () {
        return _radioSeatontour;
    }

    /**
	 * Set the value related to the column: radio_seatonTour
     * @param _radioSeatontour the radio_seatonTour value
     */
    public void setRadioSeatontour (String _radioSeatontour) {
        this._radioSeatontour = _radioSeatontour;
    }

    /**
	 * Return the value associated with the column: radio_healthIssue
     */
    public String getRadioHealthissue () {
        return _radioHealthissue;
    }

    /**
	 * Set the value related to the column: radio_healthIssue
     * @param _radioHealthissue the radio_healthIssue value
     */
    public void setRadioHealthissue (String _radioHealthissue) {
        this._radioHealthissue = _radioHealthissue;
    }

    /**
	 * Return the value associated with the column: cbox_assistInHousing
     */
    public String getCboxAssistinhousing () {
        return _cboxAssistinhousing;
    }

    /**
	 * Set the value related to the column: cbox_assistInHousing
     * @param _cboxAssistinhousing the cbox_assistInHousing value
     */
    public void setCboxAssistinhousing (String _cboxAssistinhousing) {
        this._cboxAssistinhousing = _cboxAssistinhousing;
    }

    /**
	 * Return the value associated with the column: amtReceived
     */
    public String getAmtReceived () {
        return _amtReceived;
    }

    /**
	 * Set the value related to the column: amtReceived
     * @param _amtReceived the amtReceived value
     */
    public void setAmtReceived (String _amtReceived) {
        this._amtReceived = _amtReceived;
    }

    /**
	 * Return the value associated with the column: cbox_visitHealthOffice
     */
    public String getCboxVisithealthoffice () {
        return _cboxVisithealthoffice;
    }

    /**
	 * Set the value related to the column: cbox_visitHealthOffice
     * @param _cboxVisithealthoffice the cbox_visitHealthOffice value
     */
    public void setCboxVisithealthoffice (String _cboxVisithealthoffice) {
        this._cboxVisithealthoffice = _cboxVisithealthoffice;
    }

    /**
	 * Return the value associated with the column: completedBy
     */
    public String getCompletedBy () {
        return _completedBy;
    }

    /**
	 * Set the value related to the column: completedBy
     * @param _completedBy the completedBy value
     */
    public void setCompletedBy (String _completedBy) {
        this._completedBy = _completedBy;
    }

    /**
	 * Return the value associated with the column: contactPhone
     */
    public String getContactPhone () {
        return _contactPhone;
    }

    /**
	 * Set the value related to the column: contactPhone
     * @param _contactPhone the contactPhone value
     */
    public void setContactPhone (String _contactPhone) {
        this._contactPhone = _contactPhone;
    }

    /**
	 * Return the value associated with the column: cbox_immigrant
     */
    public String getCboxImmigrant () {
        return _cboxImmigrant;
    }

    /**
	 * Set the value related to the column: cbox_immigrant
     * @param _cboxImmigrant the cbox_immigrant value
     */
    public void setCboxImmigrant (String _cboxImmigrant) {
        this._cboxImmigrant = _cboxImmigrant;
    }

    /**
	 * Return the value associated with the column: healthCardVer
     */
    public String getHealthCardVer () {
        return _healthCardVer;
    }

    /**
	 * Set the value related to the column: healthCardVer
     * @param _healthCardVer the healthCardVer value
     */
    public void setHealthCardVer (String _healthCardVer) {
        this._healthCardVer = _healthCardVer;
    }

    /**
	 * Return the value associated with the column: doctorName2
     */
    public String getDoctorName2 () {
        return _doctorName2;
    }

    /**
	 * Set the value related to the column: doctorName2
     * @param _doctorName2 the doctorName2 value
     */
    public void setDoctorName2 (String _doctorName2) {
        this._doctorName2 = _doctorName2;
    }

    /**
	 * Return the value associated with the column: cbox_dateOfReadmission
     */
    public String getCboxDateofreadmission () {
        return _cboxDateofreadmission;
    }

    /**
	 * Set the value related to the column: cbox_dateOfReadmission
     * @param _cboxDateofreadmission the cbox_dateOfReadmission value
     */
    public void setCboxDateofreadmission (String _cboxDateofreadmission) {
        this._cboxDateofreadmission = _cboxDateofreadmission;
    }

    /**
	 * Return the value associated with the column: cbox_treatOtherReasons
     */
    public String getCboxTreatotherreasons () {
        return _cboxTreatotherreasons;
    }

    /**
	 * Set the value related to the column: cbox_treatOtherReasons
     * @param _cboxTreatotherreasons the cbox_treatOtherReasons value
     */
    public void setCboxTreatotherreasons (String _cboxTreatotherreasons) {
        this._cboxTreatotherreasons = _cboxTreatotherreasons;
    }

    /**
	 * Return the value associated with the column: cbox_visitOthers
     */
    public String getCboxVisitothers () {
        return _cboxVisitothers;
    }

    /**
	 * Set the value related to the column: cbox_visitOthers
     * @param _cboxVisitothers the cbox_visitOthers value
     */
    public void setCboxVisitothers (String _cboxVisitothers) {
        this._cboxVisitothers = _cboxVisitothers;
    }

    /**
	 * Return the value associated with the column: commentsOnID
     */
    public String getCommentsOnID () {
        return _commentsOnID;
    }

    /**
	 * Set the value related to the column: commentsOnID
     * @param _commentsOnID the commentsOnID value
     */
    public void setCommentsOnID (String _commentsOnID) {
        this._commentsOnID = _commentsOnID;
    }

    /**
	 * Return the value associated with the column: assessStartTime
     */
    public String getAssessStartTime () {
        return _assessStartTime;
    }

    /**
	 * Set the value related to the column: assessStartTime
     * @param _assessStartTime the assessStartTime value
     */
    public void setAssessStartTime (String _assessStartTime) {
        this._assessStartTime = _assessStartTime;
    }

    /**
	 * Return the value associated with the column: immigrantNum
     */
    public String getImmigrantNum () {
        return _immigrantNum;
    }

    /**
	 * Set the value related to the column: immigrantNum
     * @param _immigrantNum the immigrantNum value
     */
    public void setImmigrantNum (String _immigrantNum) {
        this._immigrantNum = _immigrantNum;
    }

    /**
	 * Return the value associated with the column: cbox_insulin
     */
    public String getCboxInsulin () {
        return _cboxInsulin;
    }

    /**
	 * Set the value related to the column: cbox_insulin
     * @param _cboxInsulin the cbox_insulin value
     */
    public void setCboxInsulin (String _cboxInsulin) {
        this._cboxInsulin = _cboxInsulin;
    }

    /**
	 * Return the value associated with the column: radio_seeSameDoctor
     */
    public String getRadioSeesamedoctor () {
        return _radioSeesamedoctor;
    }

    /**
	 * Set the value related to the column: radio_seeSameDoctor
     * @param _radioSeesamedoctor the radio_seeSameDoctor value
     */
    public void setRadioSeesamedoctor (String _radioSeesamedoctor) {
        this._radioSeesamedoctor = _radioSeesamedoctor;
    }

    /**
	 * Return the value associated with the column: radio_speakToResearcher
     */
    public String getRadioSpeaktoresearcher () {
        return _radioSpeaktoresearcher;
    }

    /**
	 * Set the value related to the column: radio_speakToResearcher
     * @param _radioSpeaktoresearcher the radio_speakToResearcher value
     */
    public void setRadioSpeaktoresearcher (String _radioSpeaktoresearcher) {
        this._radioSpeaktoresearcher = _radioSpeaktoresearcher;
    }

    /**
	 * Return the value associated with the column: cbox_assistInIdentification
     */
    public String getCboxAssistinidentification () {
        return _cboxAssistinidentification;
    }

    /**
	 * Set the value related to the column: cbox_assistInIdentification
     * @param _cboxAssistinidentification the cbox_assistInIdentification value
     */
    public void setCboxAssistinidentification (String _cboxAssistinidentification) {
        this._cboxAssistinidentification = _cboxAssistinidentification;
    }

    /**
	 * Return the value associated with the column: cbox_speakEnglish
     */
    public String getCboxSpeakenglish () {
        return _cboxSpeakenglish;
    }

    /**
	 * Set the value related to the column: cbox_speakEnglish
     * @param _cboxSpeakenglish the cbox_speakEnglish value
     */
    public void setCboxSpeakenglish (String _cboxSpeakenglish) {
        this._cboxSpeakenglish = _cboxSpeakenglish;
    }

    /**
	 * Return the value associated with the column: contactRelationship
     */
    public String getContactRelationship () {
        return _contactRelationship;
    }

    /**
	 * Set the value related to the column: contactRelationship
     * @param _contactRelationship the contactRelationship value
     */
    public void setContactRelationship (String _contactRelationship) {
        this._contactRelationship = _contactRelationship;
    }

    /**
	 * Return the value associated with the column: cbox_treatMentalHealth
     */
    public String getCboxTreatmentalhealth () {
        return _cboxTreatmentalhealth;
    }

    /**
	 * Set the value related to the column: cbox_treatMentalHealth
     * @param _cboxTreatmentalhealth the cbox_treatMentalHealth value
     */
    public void setCboxTreatmentalhealth (String _cboxTreatmentalhealth) {
        this._cboxTreatmentalhealth = _cboxTreatmentalhealth;
    }

    /**
	 * Return the value associated with the column: datesAtSeaton
     */
    public String getDatesAtSeaton () {
        return _datesAtSeaton;
    }

    /**
	 * Set the value related to the column: datesAtSeaton
     * @param _datesAtSeaton the datesAtSeaton value
     */
    public void setDatesAtSeaton (String _datesAtSeaton) {
        this._datesAtSeaton = _datesAtSeaton;
    }

    /**
	 * Return the value associated with the column: sinNum
     */
    public String getSinNum () {
        return _sinNum;
    }

    /**
	 * Set the value related to the column: sinNum
     * @param _sinNum the sinNum value
     */
    public void setSinNum (String _sinNum) {
        this._sinNum = _sinNum;
    }

    /**
	 * Return the value associated with the column: radio_rateOverallHealth
     */
    public String getRadioRateoverallhealth () {
        return _radioRateoverallhealth;
    }

    /**
	 * Set the value related to the column: radio_rateOverallHealth
     * @param _radioRateoverallhealth the radio_rateOverallHealth value
     */
    public void setRadioRateoverallhealth (String _radioRateoverallhealth) {
        this._radioRateoverallhealth = _radioRateoverallhealth;
    }

    /**
	 * Return the value associated with the column: healthCardNum
     */
    public String getHealthCardNum () {
        return _healthCardNum;
    }

    /**
	 * Set the value related to the column: healthCardNum
     * @param _healthCardNum the healthCardNum value
     */
    public void setHealthCardNum (String _healthCardNum) {
        this._healthCardNum = _healthCardNum;
    }

    /**
	 * Return the value associated with the column: office
     */
    public String getOffice () {
        return _office;
    }

    /**
	 * Set the value related to the column: office
     * @param _office the office value
     */
    public void setOffice (String _office) {
        this._office = _office;
    }

    /**
	 * Return the value associated with the column: radio_needSeatonService
     */
    public String getRadioNeedseatonservice () {
        return _radioNeedseatonservice;
    }

    /**
	 * Set the value related to the column: radio_needSeatonService
     * @param _radioNeedseatonservice the radio_needSeatonService value
     */
    public void setRadioNeedseatonservice (String _radioNeedseatonservice) {
        this._radioNeedseatonservice = _radioNeedseatonservice;
    }

    /**
	 * Return the value associated with the column: cbox_bleeding
     */
    public String getCboxBleeding () {
        return _cboxBleeding;
    }

    /**
	 * Set the value related to the column: cbox_bleeding
     * @param _cboxBleeding the cbox_bleeding value
     */
    public void setCboxBleeding (String _cboxBleeding) {
        this._cboxBleeding = _cboxBleeding;
    }

    /**
	 * Return the value associated with the column: cbox_regularCheckup
     */
    public String getCboxRegularcheckup () {
        return _cboxRegularcheckup;
    }

    /**
	 * Set the value related to the column: cbox_regularCheckup
     * @param _cboxRegularcheckup the cbox_regularCheckup value
     */
    public void setCboxRegularcheckup (String _cboxRegularcheckup) {
        this._cboxRegularcheckup = _cboxRegularcheckup;
    }

    /**
	 * Return the value associated with the column: workerNum
     */
    public String getWorkerNum () {
        return _workerNum;
    }

    /**
	 * Set the value related to the column: workerNum
     * @param _workerNum the workerNum value
     */
    public void setWorkerNum (String _workerNum) {
        this._workerNum = _workerNum;
    }

    /**
	 * Return the value associated with the column: cbox_visitWalkInClinic
     */
    public String getCboxVisitwalkinclinic () {
        return _cboxVisitwalkinclinic;
    }

    /**
	 * Set the value related to the column: cbox_visitWalkInClinic
     * @param _cboxVisitwalkinclinic the cbox_visitWalkInClinic value
     */
    public void setCboxVisitwalkinclinic (String _cboxVisitwalkinclinic) {
        this._cboxVisitwalkinclinic = _cboxVisitwalkinclinic;
    }

    /**
	 * Return the value associated with the column: cbox_hasDiabetes
     */
    public String getCboxHasdiabetes () {
        return _cboxHasdiabetes;
    }

    /**
	 * Set the value related to the column: cbox_hasDiabetes
     * @param _cboxHasdiabetes the cbox_hasDiabetes value
     */
    public void setCboxHasdiabetes (String _cboxHasdiabetes) {
        this._cboxHasdiabetes = _cboxHasdiabetes;
    }

    /**
	 * Return the value associated with the column: birthCertificateNum
     */
    public String getBirthCertificateNum () {
        return _birthCertificateNum;
    }

    /**
	 * Set the value related to the column: birthCertificateNum
     * @param _birthCertificateNum the birthCertificateNum value
     */
    public void setBirthCertificateNum (String _birthCertificateNum) {
        this._birthCertificateNum = _birthCertificateNum;
    }

    /**
	 * Return the value associated with the column: clientFirstName
     */
    public String getClientFirstName () {
        return _clientFirstName;
    }

    /**
	 * Set the value related to the column: clientFirstName
     * @param _clientFirstName the clientFirstName value
     */
    public void setClientFirstName (String _clientFirstName) {
        this._clientFirstName = _clientFirstName;
    }

    /**
	 * Return the value associated with the column: month
     */
    public String getMonth () {
        return(StringUtils.trimToEmpty(_month));
    }

    /**
	 * Set the value related to the column: month
     * @param _month the month value
     */
    public void setMonth (String _month) {
        this._month = _month;
    }

    /**
	 * Return the value associated with the column: radio_allergicToMedication
     */
    public String getRadioAllergictomedication () {
        return _radioAllergictomedication;
    }

    /**
	 * Set the value related to the column: radio_allergicToMedication
     * @param _radioAllergictomedication the radio_allergicToMedication value
     */
    public void setRadioAllergictomedication (String _radioAllergictomedication) {
        this._radioAllergictomedication = _radioAllergictomedication;
    }

    /**
	 * Return the value associated with the column: cbox_CPP
     */
    public String getCboxCpp () {
        return _cboxCpp;
    }

    /**
	 * Set the value related to the column: cbox_CPP
     * @param _cboxCpp the cbox_CPP value
     */
    public void setCboxCpp (String _cboxCpp) {
        this._cboxCpp = _cboxCpp;
    }

    /**
	 * Return the value associated with the column: cbox_hostel_fusion_care
     */
    public String getCboxHostelFusionCare () {
        return _cboxHostelFusionCare;
    }

    /**
	 * Set the value related to the column: cbox_hostel_fusion_care
     * @param _cboxHostelFusionCare the cbox_hostel_fusion_care value
     */
    public void setCboxHostelFusionCare (String _cboxHostelFusionCare) {
        this._cboxHostelFusionCare = _cboxHostelFusionCare;
    }

    /**
	 * Return the value associated with the column: frequencyOfSeeingDoctor
     */
    public String getFrequencyOfSeeingDoctor () {
        return _frequencyOfSeeingDoctor;
    }

    /**
	 * Set the value related to the column: frequencyOfSeeingDoctor
     * @param _frequencyOfSeeingDoctor the frequencyOfSeeingDoctor value
     */
    public void setFrequencyOfSeeingDoctor (String _frequencyOfSeeingDoctor) {
        this._frequencyOfSeeingDoctor = _frequencyOfSeeingDoctor;
    }

    /**
	 * Return the value associated with the column: cbox_otherID
     */
    public String getCboxOtherid () {
        return _cboxOtherid;
    }

    /**
	 * Set the value related to the column: cbox_otherID
     * @param _cboxOtherid the cbox_otherID value
     */
    public void setCboxOtherid (String _cboxOtherid) {
        this._cboxOtherid = _cboxOtherid;
    }

    /**
	 * Return the value associated with the column: cbox_assistInEducation
     */
    public String getCboxAssistineducation () {
        return _cboxAssistineducation;
    }

    /**
	 * Set the value related to the column: cbox_assistInEducation
     * @param _cboxAssistineducation the cbox_assistInEducation value
     */
    public void setCboxAssistineducation (String _cboxAssistineducation) {
        this._cboxAssistineducation = _cboxAssistineducation;
    }

    /**
	 * Return the value associated with the column: cbox_annexHarm
     */
    public String getCboxAnnexharm () {
        return _cboxAnnexharm;
    }

    /**
	 * Set the value related to the column: cbox_annexHarm
     * @param _cboxAnnexharm the cbox_annexHarm value
     */
    public void setCboxAnnexharm (String _cboxAnnexharm) {
        this._cboxAnnexharm = _cboxAnnexharm;
    }

    /**
	 * Return the value associated with the column: cbox_visitEmergencyRoom
     */
    public String getCboxVisitemergencyroom () {
        return _cboxVisitemergencyroom;
    }

    /**
	 * Set the value related to the column: cbox_visitEmergencyRoom
     * @param _cboxVisitemergencyroom the cbox_visitEmergencyRoom value
     */
    public void setCboxVisitemergencyroom (String _cboxVisitemergencyroom) {
        this._cboxVisitemergencyroom = _cboxVisitemergencyroom;
    }

    /**
	 * Return the value associated with the column: cbox_mobilityImpair
     */
    public String getCboxMobilityimpair () {
        return _cboxMobilityimpair;
    }

    /**
	 * Set the value related to the column: cbox_mobilityImpair
     * @param _cboxMobilityimpair the cbox_mobilityImpair value
     */
    public void setCboxMobilityimpair (String _cboxMobilityimpair) {
        this._cboxMobilityimpair = _cboxMobilityimpair;
    }

    /**
	 * Return the value associated with the column: cbox_HealthOffice
     */
    public String getCboxHealthoffice () {
        return _cboxHealthoffice;
    }

    /**
	 * Set the value related to the column: cbox_HealthOffice
     * @param _cboxHealthoffice the cbox_HealthOffice value
     */
    public void setCboxHealthoffice (String _cboxHealthoffice) {
        this._cboxHealthoffice = _cboxHealthoffice;
    }

    /**
	 * Return the value associated with the column: cbox_epilepsy
     */
    public String getCboxEpilepsy () {
        return _cboxEpilepsy;
    }

    /**
	 * Set the value related to the column: cbox_epilepsy
     * @param _cboxEpilepsy the cbox_epilepsy value
     */
    public void setCboxEpilepsy (String _cboxEpilepsy) {
        this._cboxEpilepsy = _cboxEpilepsy;
    }

    /**
	 * Return the value associated with the column: radio_pamphletIssued
     */
    public String getRadioPamphletissued () {
        return _radioPamphletissued;
    }

    /**
	 * Set the value related to the column: radio_pamphletIssued
     * @param _radioPamphletissued the radio_pamphletIssued value
     */
    public void setRadioPamphletissued (String _radioPamphletissued) {
        this._radioPamphletissued = _radioPamphletissued;
    }

    /**
	 * Return the value associated with the column: formEdited
     */
    public Date getFormEdited () {
        return _formEdited;
    }

    /**
	 * Set the value related to the column: formEdited
     * @param _formEdited the formEdited value
     */
    public void setFormEdited (Date _formEdited) {
        this._formEdited = _formEdited;
    }

    /**
	 * Return the value associated with the column: otherSpecify
     */
    public String getOtherSpecify () {
        return _otherSpecify;
    }

    /**
	 * Set the value related to the column: otherSpecify
     * @param _otherSpecify the otherSpecify value
     */
    public void setOtherSpecify (String _otherSpecify) {
        this._otherSpecify = _otherSpecify;
    }

    /**
	 * Return the value associated with the column: cbox_citzenshipCard
     */
    public String getCboxCitzenshipcard () {
        return _cboxCitzenshipcard;
    }

    /**
	 * Set the value related to the column: cbox_citzenshipCard
     * @param _cboxCitzenshipcard the cbox_citzenshipCard value
     */
    public void setCboxCitzenshipcard (String _cboxCitzenshipcard) {
        this._cboxCitzenshipcard = _cboxCitzenshipcard;
    }

    /**
	 * Return the value associated with the column: cbox_assistInAddictions
     */
    public String getCboxAssistinaddictions () {
        return _cboxAssistinaddictions;
    }

    /**
	 * Set the value related to the column: cbox_assistInAddictions
     * @param _cboxAssistinaddictions the cbox_assistInAddictions value
     */
    public void setCboxAssistinaddictions (String _cboxAssistinaddictions) {
        this._cboxAssistinaddictions = _cboxAssistinaddictions;
    }

    /**
	 * Return the value associated with the column: cbox_assistInFinance
     */
    public String getCboxAssistinfinance () {
        return _cboxAssistinfinance;
    }

    /**
	 * Set the value related to the column: cbox_assistInFinance
     * @param _cboxAssistinfinance the cbox_assistInFinance value
     */
    public void setCboxAssistinfinance (String _cboxAssistinfinance) {
        this._cboxAssistinfinance = _cboxAssistinfinance;
    }

    /**
	 * Return the value associated with the column: radio_active
     */
    public String getRadioActive () {
        return _radioActive;
    }

    /**
	 * Set the value related to the column: radio_active
     * @param _radioActive the radio_active value
     */
    public void setRadioActive (String _radioActive) {
        this._radioActive = _radioActive;
    }

    /**
	 * Return the value associated with the column: provider_no
     */
    public Long getProviderNo () {
        return _providerNo;
    }

    /**
	 * Set the value related to the column: provider_no
     * @param _providerNo the provider_no value
     */
    public void setProviderNo (Long _providerNo) {
        this._providerNo = _providerNo;
    }

    /**
	 * Return the value associated with the column: cbox_Employment
     */
    public String getCboxEmployment () {
        return _cboxEmployment;
    }

    /**
	 * Set the value related to the column: cbox_Employment
     * @param _cboxEmployment the cbox_Employment value
     */
    public void setCboxEmployment (String _cboxEmployment) {
        this._cboxEmployment = _cboxEmployment;
    }

    /**
	 * Return the value associated with the column: mentalHealthConcerns
     */
    public String getMentalHealthConcerns () {
        return _mentalHealthConcerns;
    }

    /**
	 * Set the value related to the column: mentalHealthConcerns
     * @param _mentalHealthConcerns the mentalHealthConcerns value
     */
    public void setMentalHealthConcerns (String _mentalHealthConcerns) {
        this._mentalHealthConcerns = _mentalHealthConcerns;
    }

    /**
	 * Return the value associated with the column: radio_takeMedication
     */
    public String getRadioTakemedication () {
        return _radioTakemedication;
    }

    /**
	 * Set the value related to the column: radio_takeMedication
     * @param _radioTakemedication the radio_takeMedication value
     */
    public void setRadioTakemedication (String _radioTakemedication) {
        this._radioTakemedication = _radioTakemedication;
    }

    /**
	 * Return the value associated with the column: cbox_hostel
     */
    public String getCboxHostel () {
        return _cboxHostel;
    }

    /**
	 * Set the value related to the column: cbox_hostel
     * @param _cboxHostel the cbox_hostel value
     */
    public void setCboxHostel (String _cboxHostel) {
        this._cboxHostel = _cboxHostel;
    }

    /**
	 * Return the value associated with the column: doctorAddress
     */
    public String getDoctorAddress () {
        return _doctorAddress;
    }

    /**
	 * Set the value related to the column: doctorAddress
     * @param _doctorAddress the doctorAddress value
     */
    public void setDoctorAddress (String _doctorAddress) {
        this._doctorAddress = _doctorAddress;
    }

    /**
	 * Return the value associated with the column: doctorName
     */
    public String getDoctorName () {
        return _doctorName;
    }

    /**
	 * Set the value related to the column: doctorName
     * @param _doctorName the doctorName value
     */
    public void setDoctorName (String _doctorName) {
        this._doctorName = _doctorName;
    }

    /**
	 * Return the value associated with the column: cbox_goToHealthCenter
     */
    public String getCboxGotohealthcenter () {
        return _cboxGotohealthcenter;
    }

    /**
	 * Set the value related to the column: cbox_goToHealthCenter
     * @param _cboxGotohealthcenter the cbox_goToHealthCenter value
     */
    public void setCboxGotohealthcenter (String _cboxGotohealthcenter) {
        this._cboxGotohealthcenter = _cboxGotohealthcenter;
    }

    /**
	 * Return the value associated with the column: radio_appmtSeeDoctorIn3Mths
     */
    public String getRadioAppmtseedoctorin3mths () {
        return _radioAppmtseedoctorin3mths;
    }

    /**
	 * Set the value related to the column: radio_appmtSeeDoctorIn3Mths
     * @param _radioAppmtseedoctorin3mths the radio_appmtSeeDoctorIn3Mths value
     */
    public void setRadioAppmtseedoctorin3mths (String _radioAppmtseedoctorin3mths) {
        this._radioAppmtseedoctorin3mths = _radioAppmtseedoctorin3mths;
    }

    /**
	 * Return the value associated with the column: cbox_assistInLegal
     */
    public String getCboxAssistinlegal () {
        return _cboxAssistinlegal;
    }

    /**
	 * Set the value related to the column: cbox_assistInLegal
     * @param _cboxAssistinlegal the cbox_assistInLegal value
     */
    public void setCboxAssistinlegal (String _cboxAssistinlegal) {
        this._cboxAssistinlegal = _cboxAssistinlegal;
    }

    /**
	 * Return the value associated with the column: everAtSeatonBefore
     */
    public String getEverAtSeatonBefore () {
        return _everAtSeatonBefore;
    }

    /**
	 * Set the value related to the column: everAtSeatonBefore
     * @param _everAtSeatonBefore the everAtSeatonBefore value
     */
    public void setEverAtSeatonBefore (String _everAtSeatonBefore) {
        this._everAtSeatonBefore = _everAtSeatonBefore;
    }

    /**
	 * Return the value associated with the column: cbox_sinCard
     */
    public String getCboxSincard () {
        return _cboxSincard;
    }

    /**
	 * Set the value related to the column: cbox_sinCard
     * @param _cboxSincard the cbox_sinCard value
     */
    public void setCboxSincard (String _cboxSincard) {
        this._cboxSincard = _cboxSincard;
    }

    /**
	 * Return the value associated with the column: radio_hasMentalIllness
     */
    public String getRadioHasmentalillness () {
        return _radioHasmentalillness;
    }

    /**
	 * Set the value related to the column: radio_hasMentalIllness
     * @param _radioHasmentalillness the radio_hasMentalIllness value
     */
    public void setRadioHasmentalillness (String _radioHasmentalillness) {
        this._radioHasmentalillness = _radioHasmentalillness;
    }

    /**
	 * Return the value associated with the column: otherIdentification
     */
    public String getOtherIdentification () {
        return _otherIdentification;
    }

    /**
	 * Set the value related to the column: otherIdentification
     * @param _otherIdentification the otherIdentification value
     */
    public void setOtherIdentification (String _otherIdentification) {
        this._otherIdentification = _otherIdentification;
    }

    /**
	 * Return the value associated with the column: cbox_birthCertificate
     */
    public String getCboxBirthcertificate () {
        return _cboxBirthcertificate;
    }

    /**
	 * Set the value related to the column: cbox_birthCertificate
     * @param _cboxBirthcertificate the cbox_birthCertificate value
     */
    public void setCboxBirthcertificate (String _cboxBirthcertificate) {
        this._cboxBirthcertificate = _cboxBirthcertificate;
    }

    /**
	 * Return the value associated with the column: cbox_assistInImmigration
     */
    public String getCboxAssistinimmigration () {
        return _cboxAssistinimmigration;
    }

    /**
	 * Set the value related to the column: cbox_assistInImmigration
     * @param _cboxAssistinimmigration the cbox_assistInImmigration value
     */
    public void setCboxAssistinimmigration (String _cboxAssistinimmigration) {
        this._cboxAssistinimmigration = _cboxAssistinimmigration;
    }

    /**
	 * Return the value associated with the column: contactAddress
     */
    public String getContactAddress () {
        return _contactAddress;
    }

    /**
	 * Set the value related to the column: contactAddress
     * @param _contactAddress the contactAddress value
     */
    public void setContactAddress (String _contactAddress) {
        this._contactAddress = _contactAddress;
    }

    /**
	 * Return the value associated with the column: cbox_speakOther
     */
    public String getCboxSpeakother () {
        return _cboxSpeakother;
    }

    /**
	 * Set the value related to the column: cbox_speakOther
     * @param _cboxSpeakother the cbox_speakOther value
     */
    public void setCboxSpeakother (String _cboxSpeakother) {
        this._cboxSpeakother = _cboxSpeakother;
    }

    /**
	 * Return the value associated with the column: pamphletNotIssued
     */
    public String getPamphletNotIssued () {
        return _pamphletNotIssued;
    }

    /**
	 * Set the value related to the column: pamphletNotIssued
     * @param _pamphletNotIssued the pamphletNotIssued value
     */
    public void setPamphletNotIssued (String _pamphletNotIssued) {
        this._pamphletNotIssued = _pamphletNotIssued;
    }

    /**
	 * Return the value associated with the column: radio_hasHealthProblem
     */
    public String getRadioHashealthproblem () {
        return _radioHashealthproblem;
    }

    /**
	 * Set the value related to the column: radio_hasHealthProblem
     * @param _radioHashealthproblem the radio_hasHealthProblem value
     */
    public void setRadioHashealthproblem (String _radioHashealthproblem) {
        this._radioHashealthproblem = _radioHashealthproblem;
    }

    /**
	 * Return the value associated with the column: dateOfReadmission
     */
    public String getDateOfReadmission () {
        return _dateOfReadmission;
    }

    /**
	 * Set the value related to the column: dateOfReadmission
     * @param _dateOfReadmission the dateOfReadmission value
     */
    public void setDateOfReadmission (String _dateOfReadmission) {
        this._dateOfReadmission = _dateOfReadmission;
    }

    /**
	 * Return the value associated with the column: radio_onlineCheck
     */
    public String getRadioOnlinecheck () {
        return _radioOnlinecheck;
    }

    /**
	 * Set the value related to the column: radio_onlineCheck
     * @param _radioOnlinecheck the radio_onlineCheck value
     */
    public void setRadioOnlinecheck (String _radioOnlinecheck) {
        this._radioOnlinecheck = _radioOnlinecheck;
    }

    /**
	 * Return the value associated with the column: allergicToMedicationName
     */
    public String getAllergicToMedicationName () {
        return _allergicToMedicationName;
    }

    /**
	 * Set the value related to the column: allergicToMedicationName
     * @param _allergicToMedicationName the allergicToMedicationName value
     */
    public void setAllergicToMedicationName (String _allergicToMedicationName) {
        this._allergicToMedicationName = _allergicToMedicationName;
    }

    /**
	 * Return the value associated with the column: formCreated
     */
    public Date getFormCreated () {
        return _formCreated;
    }

    /**
	 * Set the value related to the column: formCreated
     * @param _formCreated the formCreated value
     */
    public void setFormCreated (Date _formCreated) {
        this._formCreated = _formCreated;
    }

    /**
	 * Return the value associated with the column: demographic_no
     */
    public Long getDemographicNo () {
        return _demographicNo;
    }

    /**
	 * Set the value related to the column: demographic_no
     * @param _demographicNo the demographic_no value
     */
    public void setDemographicNo (Long _demographicNo) {
        this._demographicNo = _demographicNo;
    }

    /**
	 * Return the value associated with the column: cbox_goToEmergencyRoom
     */
    public String getCboxGotoemergencyroom () {
        return _cboxGotoemergencyroom;
    }

    /**
	 * Set the value related to the column: cbox_goToEmergencyRoom
     * @param _cboxGotoemergencyroom the cbox_goToEmergencyRoom value
     */
    public void setCboxGotoemergencyroom (String _cboxGotoemergencyroom) {
        this._cboxGotoemergencyroom = _cboxGotoemergencyroom;
    }

    /**
	 * Return the value associated with the column: frequencyOfSeeingEmergencyRoomDoctor
     */
    public String getFrequencyOfSeeingEmergencyRoomDoctor () {
        return _frequencyOfSeeingEmergencyRoomDoctor;
    }

    /**
	 * Set the value related to the column: frequencyOfSeeingEmergencyRoomDoctor
     * @param _frequencyOfSeeingEmergencyRoomDoctor the frequencyOfSeeingEmergencyRoomDoctor value
     */
    public void setFrequencyOfSeeingEmergencyRoomDoctor (String _frequencyOfSeeingEmergencyRoomDoctor) {
        this._frequencyOfSeeingEmergencyRoomDoctor = _frequencyOfSeeingEmergencyRoomDoctor;
    }

    /**
	 * Return the value associated with the column: cbox_WSIB
     */
    public String getCboxWsib () {
        return _cboxWsib;
    }

    /**
	 * Set the value related to the column: cbox_WSIB
     * @param _cboxWsib the cbox_WSIB value
     */
    public void setCboxWsib (String _cboxWsib) {
        this._cboxWsib = _cboxWsib;
    }

    /**
	 * Return the value associated with the column: cbox_refugee
     */
    public String getCboxRefugee () {
        return _cboxRefugee;
    }

    /**
	 * Set the value related to the column: cbox_refugee
     * @param _cboxRefugee the cbox_refugee value
     */
    public void setCboxRefugee (String _cboxRefugee) {
        this._cboxRefugee = _cboxRefugee;
    }

    /**
	 * Return the value associated with the column: doctorPhoneExt
     */
    public String getDoctorPhoneExt () {
        return _doctorPhoneExt;
    }

    /**
	 * Set the value related to the column: doctorPhoneExt
     * @param _doctorPhoneExt the doctorPhoneExt value
     */
    public void setDoctorPhoneExt (String _doctorPhoneExt) {
        this._doctorPhoneExt = _doctorPhoneExt;
    }

    /**
	 * Return the value associated with the column: year
     */
    public String getYear () {
        return(StringUtils.trimToEmpty(_year));
    }

    /**
	 * Set the value related to the column: year
     * @param _year the year value
     */
    public void setYear (String _year) {
        this._year = _year;
    }

    /**
	 * Return the value associated with the column: cbox_assistInEmployment
     */
    public String getCboxAssistinemployment () {
        return _cboxAssistinemployment;
    }

    /**
	 * Set the value related to the column: cbox_assistInEmployment
     * @param _cboxAssistinemployment the cbox_assistInEmployment value
     */
    public void setCboxAssistinemployment (String _cboxAssistinemployment) {
        this._cboxAssistinemployment = _cboxAssistinemployment;
    }

    /**
	 * Return the value associated with the column: otherHealthConerns
     */
    public String getOtherHealthConerns () {
        return _otherHealthConerns;
    }

    /**
	 * Set the value related to the column: otherHealthConerns
     * @param _otherHealthConerns the otherHealthConerns value
     */
    public void setOtherHealthConerns (String _otherHealthConerns) {
        this._otherHealthConerns = _otherHealthConerns;
    }

    /**
	 * Return the value associated with the column: enterSeatonDate
     */
    public String getEnterSeatonDate () {
        return _enterSeatonDate;
    }

    /**
	 * Set the value related to the column: enterSeatonDate
     * @param _enterSeatonDate the enterSeatonDate value
     */
    public void setEnterSeatonDate (String _enterSeatonDate) {
        this._enterSeatonDate = _enterSeatonDate;
    }

    /**
	 * Return the value associated with the column: reasonToSeaton
     */
    public String getReasonToSeaton () {
        return _reasonToSeaton;
    }

    /**
	 * Set the value related to the column: reasonToSeaton
     * @param _reasonToSeaton the reasonToSeaton value
     */
    public void setReasonToSeaton (String _reasonToSeaton) {
        this._reasonToSeaton = _reasonToSeaton;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Formintakea)) return false;
        else {
            Formintakea mObj = (Formintakea) obj;
            if (null == this.getId() || null == mObj.getId()) return false;
            else return (this.getId().equals(mObj.getId()));
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString () {
        return super.toString();
    }
}
