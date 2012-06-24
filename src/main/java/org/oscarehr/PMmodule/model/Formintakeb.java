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

/**
 * This is the object class that relates to the formintakeb table.
 * Any customizations belong here.
 */
public class Formintakeb implements Serializable {

    private int hashCode = Integer.MIN_VALUE;// primary key

    private Long _id;// fields
    private String _cboxAssistwithhealthcard;
    private String _commentsOnEducation;
    private String _typeOfIncome;
    private String _historyOfJail;
    private String _dateEnteredSeaton;
    private String _commentsOnStreetDrugs;
    private String _whenMadeAppforOtherIncome;
    private String _cboxOdsp;
    private String _cboxFortyork;
    private String _cboxOneillhouse;
    private String _cboxOther;
    private String _cboxHaveother1;
    private String _radioDrinkthese;
    private String _cboxOas;
    private String _whySponsorshipBreakdown;
    private String _radioEverbeenjailed;
    private String _doctor2Phone;
    private String _radioCaredfordepression;
    private String _radioCaredforother;
    private String _cboxGetmoremedication;
    private String _radioDruguse;
    private String _radioHavementalproblem;
    private String _radioRequirereferraltoesl;
    private String _radioHavehealthcoverage;
    private String _day;
    private String _cboxSpeakfrench;
    private String _commentsOnLegalIssues;
    private String _radioHowmuchdrink;
    private String _radioNeedassistinlegal;
    private String _commentsOnHousing;
    private String _cboxDownsviewdells;
    private String _cboxHaveuniversity;
    private String _publicTrusteeInfo;
    private String _cboxLongtermprogram;
    private String _dateAssessment;
    private String _cboxAssistwithsincard;
    private String _drinksPerDay;
    private String _speakOther;
    private String _cboxRemembertotakemedication;
    private String _dateLastContact3;
    private String _usualOccupation;
    private String _radioWantappmt;
    private String _radioHasidinfile;
    private String _drinksPerMonth;
    private String _sponsorName;
    private String _cboxPamphletissued;
    private String _dateLastContact1;
    private String _cboxOw;
    private String _assessCompleteTime;
    private String _cboxBirchmountresidence;
    private String _contact4Name;
    private String _clientLastAddressPayRent;
    private String _dateLastContact2;
    private String _contact2Name;
    private String _radioCurrentlyemployed;
    private String _commentsOnFinance;
    private String _cboxHaveodsp;
    private String _agency3Name;
    private String _amtOwing;
    private String _cboxRotaryclub;
    private String _clientSurname;
    private String _completedBy1;
    private String _commentsOnEmployment;
    private String _radioEntitledtootherincome;
    private String _dateLastContact4;
    private String _whereBeforeSeaton;
    private String _doctor2NameAddr;
    private String _radioSponsorshipbreakdown;
    private String _cboxAssistwithbirthcert;
    private String _howHearAboutSeaton;
    private String _howMuchYouReceive;
    private String _everMadeAppforOtherIncome;
    private String _contact4Phone;
    private String _assistProvided1;
    private String _cboxEmployment;
    private String _yourCanadianStatus;
    private String _radioEvermadeappforotherincome;
    private String _cboxHaveschizophrenia;
    private String _commentsOnID;
    private String _cboxHaveother2;
    private String _radioNeedassistwithmedication;
    private String _assessStartTime;
    private String _cboxHavedepression;
    private String _cboxAssistwithimmigrant;
    private String _clientLastAddress;
    private String _mainSourceOfIncome;
    private String _cboxAssistwithnone;
    private String _dateExitedSeaton;
    private String _cboxHaveohip;
    private String _housingInterested;
    private String _assistWithOther;
    private String _needHelpWithImmigration;
    private String _cboxSpeakenglish;
    private String _cboxAssistwithother;
    private String _contact2Phone;
    private String _cboxHavecollege;
    private String _dateLivedThere;
    private String _radioInvolvedotheragencies;
    private String _assistProvided2;
    private String _radioWanthelpquitdrug;
    private String _contact1Phone;
    private String _radioNeed60daysseatonservices;
    private String _completedBy2;
    private String _radioInterestedintraining;
    private String _contact3Phone;
    private String _followupAppmts;
    private String _cboxHaveanxiety;
    private String _radioInterestbacktoschool;
    private String _cboxAssistwithcitizencard;
    private String _cboxHavehighschool;
    private String _cboxHaveother3;
    private String _radioCitizen;
    private String _clientFirstName;
    private String _month;
    private String _cboxCpp;
    private String _radioSeendoctorregalcohol;
    private String _radioWanthelpquit;
    private String _radioOwedrent;
    private String _whereOweRent;
    private String _howLongUnemployed;
    private String _cboxHaveodb;
    private String _howLongEmployed;
    private String _cboxAnnexharm;
    private java.util.Date _formEdited;
    private String _dateLastDoctor2Contact;
    private String _yearsOfEducation;
    private String _radioYourcanadianstatus;
    private String _radioDrugusefrequency;
    private Long _providerNo;
    private String _doctor1Phone;
    private String _agency1Name;
    private String _cboxHostel;
    private String _commentsOnAlcohol;
    private String _radioMentalillness;
    private String _cboxSpeakspanish;
    private String _drinksPerWeek;
    private String _commentsOnImmigration;
    private String _radioBehaviorproblem;
    private String _agency2Name;
    private String _commentsOnNeedHelp;
    private String _radioHavepublictrustee;
    private String _cboxSpeakother;
    private String _cboxTakeprescribedmedication;
    private String _needAssistInLegal;
    private String _radioCaredforschizophrenia;
    private java.util.Date _formCreated;
    private Long _demographicNo;
    private String _radioDoyoudrink;
    private String _radioDrinking;
    private String _radioHealthproblem;
    private String _cboxUi;
    private String _livedWithWhom;
    private String _assistProvided3;
    private String _cboxHavemanic;
    private String _contact1Name;
    private String _year;
    private String _radioCaredformanic;
    private String _cboxAssistwithrefugee;
    private String _radioLivedinsubsidized;
    private String _radioUsedrugs;
    private String _cboxNeedhelpinother;
    private String _assistProvided4;
    private String _doctor1NameAddr;
    private String _contact3Name;
    private String _haveOther;
    private String _cboxStoremedication;
    private String _agency4Name;
    private String _radioCaredforanxiety;
    private String _dateLastDoctor1Contact;

    // constructors
    public Formintakeb () {
        initialize();
    }

    /**
     * Constructor for primary key
     */
    public Formintakeb (Long _id) {
        this.setId(_id);
        initialize();
    }

    /**
     * Constructor for required fields
     */
    public Formintakeb (
            Long _id,
            Long _demographicNo) {

        this.setId(_id);
        this.setDemographicNo(_demographicNo);
        initialize();
    }


    /*[CONSTRUCTOR MARKER END]*/
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
     * Return the value associated with the column: cbox_assistWithHealthCard
     */
    public String getCboxAssistwithhealthcard () {
        return _cboxAssistwithhealthcard;
    }

    /**
     * Set the value related to the column: cbox_assistWithHealthCard
     * @param _cboxAssistwithhealthcard the cbox_assistWithHealthCard value
     */
    public void setCboxAssistwithhealthcard (String _cboxAssistwithhealthcard) {
        this._cboxAssistwithhealthcard = _cboxAssistwithhealthcard;
    }

    /**
     * Return the value associated with the column: commentsOnEducation
     */
    public String getCommentsOnEducation () {
        return _commentsOnEducation;
    }

    /**
     * Set the value related to the column: commentsOnEducation
     * @param _commentsOnEducation the commentsOnEducation value
     */
    public void setCommentsOnEducation (String _commentsOnEducation) {
        this._commentsOnEducation = _commentsOnEducation;
    }

    /**
     * Return the value associated with the column: typeOfIncome
     */
    public String getTypeOfIncome () {
        return _typeOfIncome;
    }

    /**
     * Set the value related to the column: typeOfIncome
     * @param _typeOfIncome the typeOfIncome value
     */
    public void setTypeOfIncome (String _typeOfIncome) {
        this._typeOfIncome = _typeOfIncome;
    }

    /**
     * Return the value associated with the column: historyOfJail
     */
    public String getHistoryOfJail () {
        return _historyOfJail;
    }

    /**
     * Set the value related to the column: historyOfJail
     * @param _historyOfJail the historyOfJail value
     */
    public void setHistoryOfJail (String _historyOfJail) {
        this._historyOfJail = _historyOfJail;
    }

    /**
     * Return the value associated with the column: dateEnteredSeaton
     */
    public String getDateEnteredSeaton () {
        return _dateEnteredSeaton;
    }

    /**
     * Set the value related to the column: dateEnteredSeaton
     * @param _dateEnteredSeaton the dateEnteredSeaton value
     */
    public void setDateEnteredSeaton (String _dateEnteredSeaton) {
        this._dateEnteredSeaton = _dateEnteredSeaton;
    }

    /**
     * Return the value associated with the column: commentsOnStreetDrugs
     */
    public String getCommentsOnStreetDrugs () {
        return _commentsOnStreetDrugs;
    }

    /**
     * Set the value related to the column: commentsOnStreetDrugs
     * @param _commentsOnStreetDrugs the commentsOnStreetDrugs value
     */
    public void setCommentsOnStreetDrugs (String _commentsOnStreetDrugs) {
        this._commentsOnStreetDrugs = _commentsOnStreetDrugs;
    }

    /**
     * Return the value associated with the column: whenMadeAppforOtherIncome
     */
    public String getWhenMadeAppforOtherIncome () {
        return _whenMadeAppforOtherIncome;
    }

    /**
     * Set the value related to the column: whenMadeAppforOtherIncome
     * @param _whenMadeAppforOtherIncome the whenMadeAppforOtherIncome value
     */
    public void setWhenMadeAppforOtherIncome (String _whenMadeAppforOtherIncome) {
        this._whenMadeAppforOtherIncome = _whenMadeAppforOtherIncome;
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
     * Return the value associated with the column: cbox_other
     */
    public String getCboxOther () {
        return _cboxOther;
    }

    /**
     * Set the value related to the column: cbox_other
     * @param _cboxOther the cbox_other value
     */
    public void setCboxOther (String _cboxOther) {
        this._cboxOther = _cboxOther;
    }

    /**
     * Return the value associated with the column: cbox_haveOther1
     */
    public String getCboxHaveother1 () {
        return _cboxHaveother1;
    }

    /**
     * Set the value related to the column: cbox_haveOther1
     * @param _cboxHaveother1 the cbox_haveOther1 value
     */
    public void setCboxHaveother1 (String _cboxHaveother1) {
        this._cboxHaveother1 = _cboxHaveother1;
    }

    /**
     * Return the value associated with the column: radio_drinkThese
     */
    public String getRadioDrinkthese () {
        return _radioDrinkthese;
    }

    /**
     * Set the value related to the column: radio_drinkThese
     * @param _radioDrinkthese the radio_drinkThese value
     */
    public void setRadioDrinkthese (String _radioDrinkthese) {
        this._radioDrinkthese = _radioDrinkthese;
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
     * Return the value associated with the column: whySponsorshipBreakdown
     */
    public String getWhySponsorshipBreakdown () {
        return _whySponsorshipBreakdown;
    }

    /**
     * Set the value related to the column: whySponsorshipBreakdown
     * @param _whySponsorshipBreakdown the whySponsorshipBreakdown value
     */
    public void setWhySponsorshipBreakdown (String _whySponsorshipBreakdown) {
        this._whySponsorshipBreakdown = _whySponsorshipBreakdown;
    }

    /**
     * Return the value associated with the column: radio_everBeenJailed
     */
    public String getRadioEverbeenjailed () {
        return _radioEverbeenjailed;
    }

    /**
     * Set the value related to the column: radio_everBeenJailed
     * @param _radioEverbeenjailed the radio_everBeenJailed value
     */
    public void setRadioEverbeenjailed (String _radioEverbeenjailed) {
        this._radioEverbeenjailed = _radioEverbeenjailed;
    }

    /**
     * Return the value associated with the column: doctor2Phone
     */
    public String getDoctor2Phone () {
        return _doctor2Phone;
    }

    /**
     * Set the value related to the column: doctor2Phone
     * @param _doctor2Phone the doctor2Phone value
     */
    public void setDoctor2Phone (String _doctor2Phone) {
        this._doctor2Phone = _doctor2Phone;
    }

    /**
     * Return the value associated with the column: radio_caredForDepression
     */
    public String getRadioCaredfordepression () {
        return _radioCaredfordepression;
    }

    /**
     * Set the value related to the column: radio_caredForDepression
     * @param _radioCaredfordepression the radio_caredForDepression value
     */
    public void setRadioCaredfordepression (String _radioCaredfordepression) {
        this._radioCaredfordepression = _radioCaredfordepression;
    }

    /**
     * Return the value associated with the column: radio_caredForOther
     */
    public String getRadioCaredforother () {
        return _radioCaredforother;
    }

    /**
     * Set the value related to the column: radio_caredForOther
     * @param _radioCaredforother the radio_caredForOther value
     */
    public void setRadioCaredforother (String _radioCaredforother) {
        this._radioCaredforother = _radioCaredforother;
    }

    /**
     * Return the value associated with the column: cbox_getMoreMedication
     */
    public String getCboxGetmoremedication () {
        return _cboxGetmoremedication;
    }

    /**
     * Set the value related to the column: cbox_getMoreMedication
     * @param _cboxGetmoremedication the cbox_getMoreMedication value
     */
    public void setCboxGetmoremedication (String _cboxGetmoremedication) {
        this._cboxGetmoremedication = _cboxGetmoremedication;
    }

    /**
     * Return the value associated with the column: radio_drugUse
     */
    public String getRadioDruguse () {
        return _radioDruguse;
    }

    /**
     * Set the value related to the column: radio_drugUse
     * @param _radioDruguse the radio_drugUse value
     */
    public void setRadioDruguse (String _radioDruguse) {
        this._radioDruguse = _radioDruguse;
    }

    /**
     * Return the value associated with the column: radio_haveMentalProblem
     */
    public String getRadioHavementalproblem () {
        return _radioHavementalproblem;
    }

    /**
     * Set the value related to the column: radio_haveMentalProblem
     * @param _radioHavementalproblem the radio_haveMentalProblem value
     */
    public void setRadioHavementalproblem (String _radioHavementalproblem) {
        this._radioHavementalproblem = _radioHavementalproblem;
    }

    /**
     * Return the value associated with the column: radio_requireReferralToESL
     */
    public String getRadioRequirereferraltoesl () {
        return _radioRequirereferraltoesl;
    }

    /**
     * Set the value related to the column: radio_requireReferralToESL
     * @param _radioRequirereferraltoesl the radio_requireReferralToESL value
     */
    public void setRadioRequirereferraltoesl (String _radioRequirereferraltoesl) {
        this._radioRequirereferraltoesl = _radioRequirereferraltoesl;
    }

    /**
     * Return the value associated with the column: radio_haveHealthCoverage
     */
    public String getRadioHavehealthcoverage () {
        return _radioHavehealthcoverage;
    }

    /**
     * Set the value related to the column: radio_haveHealthCoverage
     * @param _radioHavehealthcoverage the radio_haveHealthCoverage value
     */
    public void setRadioHavehealthcoverage (String _radioHavehealthcoverage) {
        this._radioHavehealthcoverage = _radioHavehealthcoverage;
    }

    /**
     * Return the value associated with the column: day
     */
    public String getDay () {
        return _day;
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
     * Return the value associated with the column: commentsOnLegalIssues
     */
    public String getCommentsOnLegalIssues () {
        return _commentsOnLegalIssues;
    }

    /**
     * Set the value related to the column: commentsOnLegalIssues
     * @param _commentsOnLegalIssues the commentsOnLegalIssues value
     */
    public void setCommentsOnLegalIssues (String _commentsOnLegalIssues) {
        this._commentsOnLegalIssues = _commentsOnLegalIssues;
    }

    /**
     * Return the value associated with the column: radio_howMuchDrink
     */
    public String getRadioHowmuchdrink () {
        return _radioHowmuchdrink;
    }

    /**
     * Set the value related to the column: radio_howMuchDrink
     * @param _radioHowmuchdrink the radio_howMuchDrink value
     */
    public void setRadioHowmuchdrink (String _radioHowmuchdrink) {
        this._radioHowmuchdrink = _radioHowmuchdrink;
    }

    /**
     * Return the value associated with the column: radio_needAssistInLegal
     */
    public String getRadioNeedassistinlegal () {
        return _radioNeedassistinlegal;
    }

    /**
     * Set the value related to the column: radio_needAssistInLegal
     * @param _radioNeedassistinlegal the radio_needAssistInLegal value
     */
    public void setRadioNeedassistinlegal (String _radioNeedassistinlegal) {
        this._radioNeedassistinlegal = _radioNeedassistinlegal;
    }

    /**
     * Return the value associated with the column: commentsOnHousing
     */
    public String getCommentsOnHousing () {
        return _commentsOnHousing;
    }

    /**
     * Set the value related to the column: commentsOnHousing
     * @param _commentsOnHousing the commentsOnHousing value
     */
    public void setCommentsOnHousing (String _commentsOnHousing) {
        this._commentsOnHousing = _commentsOnHousing;
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
     * Return the value associated with the column: cbox_haveUniversity
     */
    public String getCboxHaveuniversity () {
        return _cboxHaveuniversity;
    }

    /**
     * Set the value related to the column: cbox_haveUniversity
     * @param _cboxHaveuniversity the cbox_haveUniversity value
     */
    public void setCboxHaveuniversity (String _cboxHaveuniversity) {
        this._cboxHaveuniversity = _cboxHaveuniversity;
    }

    /**
     * Return the value associated with the column: publicTrusteeInfo
     */
    public String getPublicTrusteeInfo () {
        return _publicTrusteeInfo;
    }

    /**
     * Set the value related to the column: publicTrusteeInfo
     * @param _publicTrusteeInfo the publicTrusteeInfo value
     */
    public void setPublicTrusteeInfo (String _publicTrusteeInfo) {
        this._publicTrusteeInfo = _publicTrusteeInfo;
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
     * Return the value associated with the column: dateAssessment
     */
    public String getDateAssessment () {
        return _dateAssessment;
    }

    /**
     * Set the value related to the column: dateAssessment
     * @param _dateAssessment the dateAssessment value
     */
    public void setDateAssessment (String _dateAssessment) {
        this._dateAssessment = _dateAssessment;
    }

    /**
     * Return the value associated with the column: cbox_assistWithSINCard
     */
    public String getCboxAssistwithsincard () {
        return _cboxAssistwithsincard;
    }

    /**
     * Set the value related to the column: cbox_assistWithSINCard
     * @param _cboxAssistwithsincard the cbox_assistWithSINCard value
     */
    public void setCboxAssistwithsincard (String _cboxAssistwithsincard) {
        this._cboxAssistwithsincard = _cboxAssistwithsincard;
    }

    /**
     * Return the value associated with the column: drinksPerDay
     */
    public String getDrinksPerDay () {
        return _drinksPerDay;
    }

    /**
     * Set the value related to the column: drinksPerDay
     * @param _drinksPerDay the drinksPerDay value
     */
    public void setDrinksPerDay (String _drinksPerDay) {
        this._drinksPerDay = _drinksPerDay;
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
     * Return the value associated with the column: cbox_rememberToTakeMedication
     */
    public String getCboxRemembertotakemedication () {
        return _cboxRemembertotakemedication;
    }

    /**
     * Set the value related to the column: cbox_rememberToTakeMedication
     * @param _cboxRemembertotakemedication the cbox_rememberToTakeMedication value
     */
    public void setCboxRemembertotakemedication (String _cboxRemembertotakemedication) {
        this._cboxRemembertotakemedication = _cboxRemembertotakemedication;
    }

    /**
     * Return the value associated with the column: dateLastContact3
     */
    public String getDateLastContact3 () {
        return _dateLastContact3;
    }

    /**
     * Set the value related to the column: dateLastContact3
     * @param _dateLastContact3 the dateLastContact3 value
     */
    public void setDateLastContact3 (String _dateLastContact3) {
        this._dateLastContact3 = _dateLastContact3;
    }

    /**
     * Return the value associated with the column: usualOccupation
     */
    public String getUsualOccupation () {
        return _usualOccupation;
    }

    /**
     * Set the value related to the column: usualOccupation
     * @param _usualOccupation the usualOccupation value
     */
    public void setUsualOccupation (String _usualOccupation) {
        this._usualOccupation = _usualOccupation;
    }

    /**
     * Return the value associated with the column: radio_wantAppmt
     */
    public String getRadioWantappmt () {
        return _radioWantappmt;
    }

    /**
     * Set the value related to the column: radio_wantAppmt
     * @param _radioWantappmt the radio_wantAppmt value
     */
    public void setRadioWantappmt (String _radioWantappmt) {
        this._radioWantappmt = _radioWantappmt;
    }

    /**
     * Return the value associated with the column: radio_hasIDInFile
     */
    public String getRadioHasidinfile () {
        return _radioHasidinfile;
    }

    /**
     * Set the value related to the column: radio_hasIDInFile
     * @param _radioHasidinfile the radio_hasIDInFile value
     */
    public void setRadioHasidinfile (String _radioHasidinfile) {
        this._radioHasidinfile = _radioHasidinfile;
    }

    /**
     * Return the value associated with the column: drinksPerMonth
     */
    public String getDrinksPerMonth () {
        return _drinksPerMonth;
    }

    /**
     * Set the value related to the column: drinksPerMonth
     * @param _drinksPerMonth the drinksPerMonth value
     */
    public void setDrinksPerMonth (String _drinksPerMonth) {
        this._drinksPerMonth = _drinksPerMonth;
    }

    /**
     * Return the value associated with the column: sponsorName
     */
    public String getSponsorName () {
        return _sponsorName;
    }

    /**
     * Set the value related to the column: sponsorName
     * @param _sponsorName the sponsorName value
     */
    public void setSponsorName (String _sponsorName) {
        this._sponsorName = _sponsorName;
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
     * Return the value associated with the column: dateLastContact1
     */
    public String getDateLastContact1 () {
        return _dateLastContact1;
    }

    /**
     * Set the value related to the column: dateLastContact1
     * @param _dateLastContact1 the dateLastContact1 value
     */
    public void setDateLastContact1 (String _dateLastContact1) {
        this._dateLastContact1 = _dateLastContact1;
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
     * Return the value associated with the column: contact4Name
     */
    public String getContact4Name () {
        return _contact4Name;
    }

    /**
     * Set the value related to the column: contact4Name
     * @param _contact4Name the contact4Name value
     */
    public void setContact4Name (String _contact4Name) {
        this._contact4Name = _contact4Name;
    }

    /**
     * Return the value associated with the column: clientLastAddressPayRent
     */
    public String getClientLastAddressPayRent () {
        return _clientLastAddressPayRent;
    }

    /**
     * Set the value related to the column: clientLastAddressPayRent
     * @param _clientLastAddressPayRent the clientLastAddressPayRent value
     */
    public void setClientLastAddressPayRent (String _clientLastAddressPayRent) {
        this._clientLastAddressPayRent = _clientLastAddressPayRent;
    }

    /**
     * Return the value associated with the column: dateLastContact2
     */
    public String getDateLastContact2 () {
        return _dateLastContact2;
    }

    /**
     * Set the value related to the column: dateLastContact2
     * @param _dateLastContact2 the dateLastContact2 value
     */
    public void setDateLastContact2 (String _dateLastContact2) {
        this._dateLastContact2 = _dateLastContact2;
    }

    /**
     * Return the value associated with the column: contact2Name
     */
    public String getContact2Name () {
        return _contact2Name;
    }

    /**
     * Set the value related to the column: contact2Name
     * @param _contact2Name the contact2Name value
     */
    public void setContact2Name (String _contact2Name) {
        this._contact2Name = _contact2Name;
    }

    /**
     * Return the value associated with the column: radio_currentlyEmployed
     */
    public String getRadioCurrentlyemployed () {
        return _radioCurrentlyemployed;
    }

    /**
     * Set the value related to the column: radio_currentlyEmployed
     * @param _radioCurrentlyemployed the radio_currentlyEmployed value
     */
    public void setRadioCurrentlyemployed (String _radioCurrentlyemployed) {
        this._radioCurrentlyemployed = _radioCurrentlyemployed;
    }

    /**
     * Return the value associated with the column: commentsOnFinance
     */
    public String getCommentsOnFinance () {
        return _commentsOnFinance;
    }

    /**
     * Set the value related to the column: commentsOnFinance
     * @param _commentsOnFinance the commentsOnFinance value
     */
    public void setCommentsOnFinance (String _commentsOnFinance) {
        this._commentsOnFinance = _commentsOnFinance;
    }

    /**
     * Return the value associated with the column: cbox_haveODSP
     */
    public String getCboxHaveodsp () {
        return _cboxHaveodsp;
    }

    /**
     * Set the value related to the column: cbox_haveODSP
     * @param _cboxHaveodsp the cbox_haveODSP value
     */
    public void setCboxHaveodsp (String _cboxHaveodsp) {
        this._cboxHaveodsp = _cboxHaveodsp;
    }

    /**
     * Return the value associated with the column: agency3Name
     */
    public String getAgency3Name () {
        return _agency3Name;
    }

    /**
     * Set the value related to the column: agency3Name
     * @param _agency3Name the agency3Name value
     */
    public void setAgency3Name (String _agency3Name) {
        this._agency3Name = _agency3Name;
    }

    /**
     * Return the value associated with the column: amtOwing
     */
    public String getAmtOwing () {
        return _amtOwing;
    }

    /**
     * Set the value related to the column: amtOwing
     * @param _amtOwing the amtOwing value
     */
    public void setAmtOwing (String _amtOwing) {
        this._amtOwing = _amtOwing;
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
     * Return the value associated with the column: completedBy1
     */
    public String getCompletedBy1 () {
        return _completedBy1;
    }

    /**
     * Set the value related to the column: completedBy1
     * @param _completedBy1 the completedBy1 value
     */
    public void setCompletedBy1 (String _completedBy1) {
        this._completedBy1 = _completedBy1;
    }

    /**
     * Return the value associated with the column: commentsOnEmployment
     */
    public String getCommentsOnEmployment () {
        return _commentsOnEmployment;
    }

    /**
     * Set the value related to the column: commentsOnEmployment
     * @param _commentsOnEmployment the commentsOnEmployment value
     */
    public void setCommentsOnEmployment (String _commentsOnEmployment) {
        this._commentsOnEmployment = _commentsOnEmployment;
    }

    /**
     * Return the value associated with the column: radio_entitledToOtherIncome
     */
    public String getRadioEntitledtootherincome () {
        return _radioEntitledtootherincome;
    }

    /**
     * Set the value related to the column: radio_entitledToOtherIncome
     * @param _radioEntitledtootherincome the radio_entitledToOtherIncome value
     */
    public void setRadioEntitledtootherincome (String _radioEntitledtootherincome) {
        this._radioEntitledtootherincome = _radioEntitledtootherincome;
    }

    /**
     * Return the value associated with the column: dateLastContact4
     */
    public String getDateLastContact4 () {
        return _dateLastContact4;
    }

    /**
     * Set the value related to the column: dateLastContact4
     * @param _dateLastContact4 the dateLastContact4 value
     */
    public void setDateLastContact4 (String _dateLastContact4) {
        this._dateLastContact4 = _dateLastContact4;
    }

    /**
     * Return the value associated with the column: whereBeforeSeaton
     */
    public String getWhereBeforeSeaton () {
        return _whereBeforeSeaton;
    }

    /**
     * Set the value related to the column: whereBeforeSeaton
     * @param _whereBeforeSeaton the whereBeforeSeaton value
     */
    public void setWhereBeforeSeaton (String _whereBeforeSeaton) {
        this._whereBeforeSeaton = _whereBeforeSeaton;
    }

    /**
     * Return the value associated with the column: doctor2NameAddr
     */
    public String getDoctor2NameAddr () {
        return _doctor2NameAddr;
    }

    /**
     * Set the value related to the column: doctor2NameAddr
     * @param _doctor2NameAddr the doctor2NameAddr value
     */
    public void setDoctor2NameAddr (String _doctor2NameAddr) {
        this._doctor2NameAddr = _doctor2NameAddr;
    }

    /**
     * Return the value associated with the column: radio_sponsorshipBreakdown
     */
    public String getRadioSponsorshipbreakdown () {
        return _radioSponsorshipbreakdown;
    }

    /**
     * Set the value related to the column: radio_sponsorshipBreakdown
     * @param _radioSponsorshipbreakdown the radio_sponsorshipBreakdown value
     */
    public void setRadioSponsorshipbreakdown (String _radioSponsorshipbreakdown) {
        this._radioSponsorshipbreakdown = _radioSponsorshipbreakdown;
    }

    /**
     * Return the value associated with the column: cbox_assistWithBirthCert
     */
    public String getCboxAssistwithbirthcert () {
        return _cboxAssistwithbirthcert;
    }

    /**
     * Set the value related to the column: cbox_assistWithBirthCert
     * @param _cboxAssistwithbirthcert the cbox_assistWithBirthCert value
     */
    public void setCboxAssistwithbirthcert (String _cboxAssistwithbirthcert) {
        this._cboxAssistwithbirthcert = _cboxAssistwithbirthcert;
    }

    /**
     * Return the value associated with the column: howHearAboutSeaton
     */
    public String getHowHearAboutSeaton () {
        return _howHearAboutSeaton;
    }

    /**
     * Set the value related to the column: howHearAboutSeaton
     * @param _howHearAboutSeaton the howHearAboutSeaton value
     */
    public void setHowHearAboutSeaton (String _howHearAboutSeaton) {
        this._howHearAboutSeaton = _howHearAboutSeaton;
    }

    /**
     * Return the value associated with the column: howMuchYouReceive
     */
    public String getHowMuchYouReceive () {
        return _howMuchYouReceive;
    }

    /**
     * Set the value related to the column: howMuchYouReceive
     * @param _howMuchYouReceive the howMuchYouReceive value
     */
    public void setHowMuchYouReceive (String _howMuchYouReceive) {
        this._howMuchYouReceive = _howMuchYouReceive;
    }

    /**
     * Return the value associated with the column: everMadeAppforOtherIncome
     */
    public String getEverMadeAppforOtherIncome () {
        return _everMadeAppforOtherIncome;
    }

    /**
     * Set the value related to the column: everMadeAppforOtherIncome
     * @param _everMadeAppforOtherIncome the everMadeAppforOtherIncome value
     */
    public void setEverMadeAppforOtherIncome (String _everMadeAppforOtherIncome) {
        this._everMadeAppforOtherIncome = _everMadeAppforOtherIncome;
    }

    /**
     * Return the value associated with the column: contact4Phone
     */
    public String getContact4Phone () {
        return _contact4Phone;
    }

    /**
     * Set the value related to the column: contact4Phone
     * @param _contact4Phone the contact4Phone value
     */
    public void setContact4Phone (String _contact4Phone) {
        this._contact4Phone = _contact4Phone;
    }

    /**
     * Return the value associated with the column: assistProvided1
     */
    public String getAssistProvided1 () {
        return _assistProvided1;
    }

    /**
     * Set the value related to the column: assistProvided1
     * @param _assistProvided1 the assistProvided1 value
     */
    public void setAssistProvided1 (String _assistProvided1) {
        this._assistProvided1 = _assistProvided1;
    }

    /**
     * Return the value associated with the column: cbox_employment
     */
    public String getCboxEmployment () {
        return _cboxEmployment;
    }

    /**
     * Set the value related to the column: cbox_employment
     * @param _cboxEmployment the cbox_employment value
     */
    public void setCboxEmployment (String _cboxEmployment) {
        this._cboxEmployment = _cboxEmployment;
    }

    /**
     * Return the value associated with the column: yourCanadianStatus
     */
    public String getYourCanadianStatus () {
        return _yourCanadianStatus;
    }

    /**
     * Set the value related to the column: yourCanadianStatus
     * @param _yourCanadianStatus the yourCanadianStatus value
     */
    public void setYourCanadianStatus (String _yourCanadianStatus) {
        this._yourCanadianStatus = _yourCanadianStatus;
    }

    /**
     * Return the value associated with the column: radio_everMadeAppforOtherIncome
     */
    public String getRadioEvermadeappforotherincome () {
        return _radioEvermadeappforotherincome;
    }

    /**
     * Set the value related to the column: radio_everMadeAppforOtherIncome
     * @param _radioEvermadeappforotherincome the radio_everMadeAppforOtherIncome value
     */
    public void setRadioEvermadeappforotherincome (String _radioEvermadeappforotherincome) {
        this._radioEvermadeappforotherincome = _radioEvermadeappforotherincome;
    }

    /**
     * Return the value associated with the column: cbox_haveSchizophrenia
     */
    public String getCboxHaveschizophrenia () {
        return _cboxHaveschizophrenia;
    }

    /**
     * Set the value related to the column: cbox_haveSchizophrenia
     * @param _cboxHaveschizophrenia the cbox_haveSchizophrenia value
     */
    public void setCboxHaveschizophrenia (String _cboxHaveschizophrenia) {
        this._cboxHaveschizophrenia = _cboxHaveschizophrenia;
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
     * Return the value associated with the column: cbox_haveOther2
     */
    public String getCboxHaveother2 () {
        return _cboxHaveother2;
    }

    /**
     * Set the value related to the column: cbox_haveOther2
     * @param _cboxHaveother2 the cbox_haveOther2 value
     */
    public void setCboxHaveother2 (String _cboxHaveother2) {
        this._cboxHaveother2 = _cboxHaveother2;
    }

    /**
     * Return the value associated with the column: radio_needAssistWithMedication
     */
    public String getRadioNeedassistwithmedication () {
        return _radioNeedassistwithmedication;
    }

    /**
     * Set the value related to the column: radio_needAssistWithMedication
     * @param _radioNeedassistwithmedication the radio_needAssistWithMedication value
     */
    public void setRadioNeedassistwithmedication (String _radioNeedassistwithmedication) {
        this._radioNeedassistwithmedication = _radioNeedassistwithmedication;
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
     * Return the value associated with the column: cbox_haveDepression
     */
    public String getCboxHavedepression () {
        return _cboxHavedepression;
    }

    /**
     * Set the value related to the column: cbox_haveDepression
     * @param _cboxHavedepression the cbox_haveDepression value
     */
    public void setCboxHavedepression (String _cboxHavedepression) {
        this._cboxHavedepression = _cboxHavedepression;
    }

    /**
     * Return the value associated with the column: cbox_assistWithImmigrant
     */
    public String getCboxAssistwithimmigrant () {
        return _cboxAssistwithimmigrant;
    }

    /**
     * Set the value related to the column: cbox_assistWithImmigrant
     * @param _cboxAssistwithimmigrant the cbox_assistWithImmigrant value
     */
    public void setCboxAssistwithimmigrant (String _cboxAssistwithimmigrant) {
        this._cboxAssistwithimmigrant = _cboxAssistwithimmigrant;
    }

    /**
     * Return the value associated with the column: clientLastAddress
     */
    public String getClientLastAddress () {
        return _clientLastAddress;
    }

    /**
     * Set the value related to the column: clientLastAddress
     * @param _clientLastAddress the clientLastAddress value
     */
    public void setClientLastAddress (String _clientLastAddress) {
        this._clientLastAddress = _clientLastAddress;
    }

    /**
     * Return the value associated with the column: mainSourceOfIncome
     */
    public String getMainSourceOfIncome () {
        return _mainSourceOfIncome;
    }

    /**
     * Set the value related to the column: mainSourceOfIncome
     * @param _mainSourceOfIncome the mainSourceOfIncome value
     */
    public void setMainSourceOfIncome (String _mainSourceOfIncome) {
        this._mainSourceOfIncome = _mainSourceOfIncome;
    }

    /**
     * Return the value associated with the column: cbox_assistWithNone
     */
    public String getCboxAssistwithnone () {
        return _cboxAssistwithnone;
    }

    /**
     * Set the value related to the column: cbox_assistWithNone
     * @param _cboxAssistwithnone the cbox_assistWithNone value
     */
    public void setCboxAssistwithnone (String _cboxAssistwithnone) {
        this._cboxAssistwithnone = _cboxAssistwithnone;
    }

    /**
     * Return the value associated with the column: dateExitedSeaton
     */
    public String getDateExitedSeaton () {
        return _dateExitedSeaton;
    }

    /**
     * Set the value related to the column: dateExitedSeaton
     * @param _dateExitedSeaton the dateExitedSeaton value
     */
    public void setDateExitedSeaton (String _dateExitedSeaton) {
        this._dateExitedSeaton = _dateExitedSeaton;
    }

    /**
     * Return the value associated with the column: cbox_haveOHIP
     */
    public String getCboxHaveohip () {
        return _cboxHaveohip;
    }

    /**
     * Set the value related to the column: cbox_haveOHIP
     * @param _cboxHaveohip the cbox_haveOHIP value
     */
    public void setCboxHaveohip (String _cboxHaveohip) {
        this._cboxHaveohip = _cboxHaveohip;
    }

    /**
     * Return the value associated with the column: housingInterested
     */
    public String getHousingInterested () {
        return _housingInterested;
    }

    /**
     * Set the value related to the column: housingInterested
     * @param _housingInterested the housingInterested value
     */
    public void setHousingInterested (String _housingInterested) {
        this._housingInterested = _housingInterested;
    }

    /**
     * Return the value associated with the column: assistWithOther
     */
    public String getAssistWithOther () {
        return _assistWithOther;
    }

    /**
     * Set the value related to the column: assistWithOther
     * @param _assistWithOther the assistWithOther value
     */
    public void setAssistWithOther (String _assistWithOther) {
        this._assistWithOther = _assistWithOther;
    }

    /**
     * Return the value associated with the column: needHelpWithImmigration
     */
    public String getNeedHelpWithImmigration () {
        return _needHelpWithImmigration;
    }

    /**
     * Set the value related to the column: needHelpWithImmigration
     * @param _needHelpWithImmigration the needHelpWithImmigration value
     */
    public void setNeedHelpWithImmigration (String _needHelpWithImmigration) {
        this._needHelpWithImmigration = _needHelpWithImmigration;
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
     * Return the value associated with the column: cbox_assistWithOther
     */
    public String getCboxAssistwithother () {
        return _cboxAssistwithother;
    }

    /**
     * Set the value related to the column: cbox_assistWithOther
     * @param _cboxAssistwithother the cbox_assistWithOther value
     */
    public void setCboxAssistwithother (String _cboxAssistwithother) {
        this._cboxAssistwithother = _cboxAssistwithother;
    }

    /**
     * Return the value associated with the column: contact2Phone
     */
    public String getContact2Phone () {
        return _contact2Phone;
    }

    /**
     * Set the value related to the column: contact2Phone
     * @param _contact2Phone the contact2Phone value
     */
    public void setContact2Phone (String _contact2Phone) {
        this._contact2Phone = _contact2Phone;
    }

    /**
     * Return the value associated with the column: cbox_haveCollege
     */
    public String getCboxHavecollege () {
        return _cboxHavecollege;
    }

    /**
     * Set the value related to the column: cbox_haveCollege
     * @param _cboxHavecollege the cbox_haveCollege value
     */
    public void setCboxHavecollege (String _cboxHavecollege) {
        this._cboxHavecollege = _cboxHavecollege;
    }

    /**
     * Return the value associated with the column: dateLivedThere
     */
    public String getDateLivedThere () {
        return _dateLivedThere;
    }

    /**
     * Set the value related to the column: dateLivedThere
     * @param _dateLivedThere the dateLivedThere value
     */
    public void setDateLivedThere (String _dateLivedThere) {
        this._dateLivedThere = _dateLivedThere;
    }

    /**
     * Return the value associated with the column: radio_involvedOtherAgencies
     */
    public String getRadioInvolvedotheragencies () {
        return _radioInvolvedotheragencies;
    }

    /**
     * Set the value related to the column: radio_involvedOtherAgencies
     * @param _radioInvolvedotheragencies the radio_involvedOtherAgencies value
     */
    public void setRadioInvolvedotheragencies (String _radioInvolvedotheragencies) {
        this._radioInvolvedotheragencies = _radioInvolvedotheragencies;
    }

    /**
     * Return the value associated with the column: assistProvided2
     */
    public String getAssistProvided2 () {
        return _assistProvided2;
    }

    /**
     * Set the value related to the column: assistProvided2
     * @param _assistProvided2 the assistProvided2 value
     */
    public void setAssistProvided2 (String _assistProvided2) {
        this._assistProvided2 = _assistProvided2;
    }

    /**
     * Return the value associated with the column: radio_wantHelpQuitDrug
     */
    public String getRadioWanthelpquitdrug () {
        return _radioWanthelpquitdrug;
    }

    /**
     * Set the value related to the column: radio_wantHelpQuitDrug
     * @param _radioWanthelpquitdrug the radio_wantHelpQuitDrug value
     */
    public void setRadioWanthelpquitdrug (String _radioWanthelpquitdrug) {
        this._radioWanthelpquitdrug = _radioWanthelpquitdrug;
    }

    /**
     * Return the value associated with the column: contact1Phone
     */
    public String getContact1Phone () {
        return _contact1Phone;
    }

    /**
     * Set the value related to the column: contact1Phone
     * @param _contact1Phone the contact1Phone value
     */
    public void setContact1Phone (String _contact1Phone) {
        this._contact1Phone = _contact1Phone;
    }

    /**
     * Return the value associated with the column: radio_need60DaysSeatonServices
     */
    public String getRadioNeed60daysseatonservices () {
        return _radioNeed60daysseatonservices;
    }

    /**
     * Set the value related to the column: radio_need60DaysSeatonServices
     * @param _radioNeed60daysseatonservices the radio_need60DaysSeatonServices value
     */
    public void setRadioNeed60daysseatonservices (String _radioNeed60daysseatonservices) {
        this._radioNeed60daysseatonservices = _radioNeed60daysseatonservices;
    }

    /**
     * Return the value associated with the column: completedBy2
     */
    public String getCompletedBy2 () {
        return _completedBy2;
    }

    /**
     * Set the value related to the column: completedBy2
     * @param _completedBy2 the completedBy2 value
     */
    public void setCompletedBy2 (String _completedBy2) {
        this._completedBy2 = _completedBy2;
    }

    /**
     * Return the value associated with the column: radio_interestedInTraining
     */
    public String getRadioInterestedintraining () {
        return _radioInterestedintraining;
    }

    /**
     * Set the value related to the column: radio_interestedInTraining
     * @param _radioInterestedintraining the radio_interestedInTraining value
     */
    public void setRadioInterestedintraining (String _radioInterestedintraining) {
        this._radioInterestedintraining = _radioInterestedintraining;
    }

    /**
     * Return the value associated with the column: contact3Phone
     */
    public String getContact3Phone () {
        return _contact3Phone;
    }

    /**
     * Set the value related to the column: contact3Phone
     * @param _contact3Phone the contact3Phone value
     */
    public void setContact3Phone (String _contact3Phone) {
        this._contact3Phone = _contact3Phone;
    }

    /**
     * Return the value associated with the column: followupAppmts
     */
    public String getFollowupAppmts () {
        return _followupAppmts;
    }

    /**
     * Set the value related to the column: followupAppmts
     * @param _followupAppmts the followupAppmts value
     */
    public void setFollowupAppmts (String _followupAppmts) {
        this._followupAppmts = _followupAppmts;
    }

    /**
     * Return the value associated with the column: cbox_haveAnxiety
     */
    public String getCboxHaveanxiety () {
        return _cboxHaveanxiety;
    }

    /**
     * Set the value related to the column: cbox_haveAnxiety
     * @param _cboxHaveanxiety the cbox_haveAnxiety value
     */
    public void setCboxHaveanxiety (String _cboxHaveanxiety) {
        this._cboxHaveanxiety = _cboxHaveanxiety;
    }

    /**
     * Return the value associated with the column: radio_interestBackToSchool
     */
    public String getRadioInterestbacktoschool () {
        return _radioInterestbacktoschool;
    }

    /**
     * Set the value related to the column: radio_interestBackToSchool
     * @param _radioInterestbacktoschool the radio_interestBackToSchool value
     */
    public void setRadioInterestbacktoschool (String _radioInterestbacktoschool) {
        this._radioInterestbacktoschool = _radioInterestbacktoschool;
    }

    /**
     * Return the value associated with the column: cbox_assistWithCitizenCard
     */
    public String getCboxAssistwithcitizencard () {
        return _cboxAssistwithcitizencard;
    }

    /**
     * Set the value related to the column: cbox_assistWithCitizenCard
     * @param _cboxAssistwithcitizencard the cbox_assistWithCitizenCard value
     */
    public void setCboxAssistwithcitizencard (String _cboxAssistwithcitizencard) {
        this._cboxAssistwithcitizencard = _cboxAssistwithcitizencard;
    }

    /**
     * Return the value associated with the column: cbox_haveHighSchool
     */
    public String getCboxHavehighschool () {
        return _cboxHavehighschool;
    }

    /**
     * Set the value related to the column: cbox_haveHighSchool
     * @param _cboxHavehighschool the cbox_haveHighSchool value
     */
    public void setCboxHavehighschool (String _cboxHavehighschool) {
        this._cboxHavehighschool = _cboxHavehighschool;
    }

    /**
     * Return the value associated with the column: cbox_haveOther3
     */
    public String getCboxHaveother3 () {
        return _cboxHaveother3;
    }

    /**
     * Set the value related to the column: cbox_haveOther3
     * @param _cboxHaveother3 the cbox_haveOther3 value
     */
    public void setCboxHaveother3 (String _cboxHaveother3) {
        this._cboxHaveother3 = _cboxHaveother3;
    }

    /**
     * Return the value associated with the column: radio_citizen
     */
    public String getRadioCitizen () {
        return _radioCitizen;
    }

    /**
     * Set the value related to the column: radio_citizen
     * @param _radioCitizen the radio_citizen value
     */
    public void setRadioCitizen (String _radioCitizen) {
        this._radioCitizen = _radioCitizen;
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
        return _month;
    }

    /**
     * Set the value related to the column: month
     * @param _month the month value
     */
    public void setMonth (String _month) {
        this._month = _month;
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
     * Return the value associated with the column: radio_seenDoctorRegAlcohol
     */
    public String getRadioSeendoctorregalcohol () {
        return _radioSeendoctorregalcohol;
    }

    /**
     * Set the value related to the column: radio_seenDoctorRegAlcohol
     * @param _radioSeendoctorregalcohol the radio_seenDoctorRegAlcohol value
     */
    public void setRadioSeendoctorregalcohol (String _radioSeendoctorregalcohol) {
        this._radioSeendoctorregalcohol = _radioSeendoctorregalcohol;
    }

    /**
     * Return the value associated with the column: radio_wantHelpQuit
     */
    public String getRadioWanthelpquit () {
        return _radioWanthelpquit;
    }

    /**
     * Set the value related to the column: radio_wantHelpQuit
     * @param _radioWanthelpquit the radio_wantHelpQuit value
     */
    public void setRadioWanthelpquit (String _radioWanthelpquit) {
        this._radioWanthelpquit = _radioWanthelpquit;
    }

    /**
     * Return the value associated with the column: radio_owedRent
     */
    public String getRadioOwedrent () {
        return _radioOwedrent;
    }

    /**
     * Set the value related to the column: radio_owedRent
     * @param _radioOwedrent the radio_owedRent value
     */
    public void setRadioOwedrent (String _radioOwedrent) {
        this._radioOwedrent = _radioOwedrent;
    }

    /**
     * Return the value associated with the column: whereOweRent
     */
    public String getWhereOweRent () {
        return _whereOweRent;
    }

    /**
     * Set the value related to the column: whereOweRent
     * @param _whereOweRent the whereOweRent value
     */
    public void setWhereOweRent (String _whereOweRent) {
        this._whereOweRent = _whereOweRent;
    }

    /**
     * Return the value associated with the column: howLongUnemployed
     */
    public String getHowLongUnemployed () {
        return _howLongUnemployed;
    }

    /**
     * Set the value related to the column: howLongUnemployed
     * @param _howLongUnemployed the howLongUnemployed value
     */
    public void setHowLongUnemployed (String _howLongUnemployed) {
        this._howLongUnemployed = _howLongUnemployed;
    }

    /**
     * Return the value associated with the column: cbox_haveODB
     */
    public String getCboxHaveodb () {
        return _cboxHaveodb;
    }

    /**
     * Set the value related to the column: cbox_haveODB
     * @param _cboxHaveodb the cbox_haveODB value
     */
    public void setCboxHaveodb (String _cboxHaveodb) {
        this._cboxHaveodb = _cboxHaveodb;
    }

    /**
     * Return the value associated with the column: howLongEmployed
     */
    public String getHowLongEmployed () {
        return _howLongEmployed;
    }

    /**
     * Set the value related to the column: howLongEmployed
     * @param _howLongEmployed the howLongEmployed value
     */
    public void setHowLongEmployed (String _howLongEmployed) {
        this._howLongEmployed = _howLongEmployed;
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
     * Return the value associated with the column: formEdited
     */
    public java.util.Date getFormEdited () {
        return _formEdited;
    }

    /**
     * Set the value related to the column: formEdited
     * @param _formEdited the formEdited value
     */
    public void setFormEdited (java.util.Date _formEdited) {
        this._formEdited = _formEdited;
    }

    /**
     * Return the value associated with the column: dateLastDoctor2Contact
     */
    public String getDateLastDoctor2Contact () {
        return _dateLastDoctor2Contact;
    }

    /**
     * Set the value related to the column: dateLastDoctor2Contact
     * @param _dateLastDoctor2Contact the dateLastDoctor2Contact value
     */
    public void setDateLastDoctor2Contact (String _dateLastDoctor2Contact) {
        this._dateLastDoctor2Contact = _dateLastDoctor2Contact;
    }

    /**
     * Return the value associated with the column: yearsOfEducation
     */
    public String getYearsOfEducation () {
        return _yearsOfEducation;
    }

    /**
     * Set the value related to the column: yearsOfEducation
     * @param _yearsOfEducation the yearsOfEducation value
     */
    public void setYearsOfEducation (String _yearsOfEducation) {
        this._yearsOfEducation = _yearsOfEducation;
    }

    /**
     * Return the value associated with the column: radio_yourCanadianStatus
     */
    public String getRadioYourcanadianstatus () {
        return _radioYourcanadianstatus;
    }

    /**
     * Set the value related to the column: radio_yourCanadianStatus
     * @param _radioYourcanadianstatus the radio_yourCanadianStatus value
     */
    public void setRadioYourcanadianstatus (String _radioYourcanadianstatus) {
        this._radioYourcanadianstatus = _radioYourcanadianstatus;
    }

    /**
     * Return the value associated with the column: radio_drugUseFrequency
     */
    public String getRadioDrugusefrequency () {
        return _radioDrugusefrequency;
    }

    /**
     * Set the value related to the column: radio_drugUseFrequency
     * @param _radioDrugusefrequency the radio_drugUseFrequency value
     */
    public void setRadioDrugusefrequency (String _radioDrugusefrequency) {
        this._radioDrugusefrequency = _radioDrugusefrequency;
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
     * Return the value associated with the column: doctor1Phone
     */
    public String getDoctor1Phone () {
        return _doctor1Phone;
    }

    /**
     * Set the value related to the column: doctor1Phone
     * @param _doctor1Phone the doctor1Phone value
     */
    public void setDoctor1Phone (String _doctor1Phone) {
        this._doctor1Phone = _doctor1Phone;
    }

    /**
     * Return the value associated with the column: agency1Name
     */
    public String getAgency1Name () {
        return _agency1Name;
    }

    /**
     * Set the value related to the column: agency1Name
     * @param _agency1Name the agency1Name value
     */
    public void setAgency1Name (String _agency1Name) {
        this._agency1Name = _agency1Name;
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
     * Return the value associated with the column: commentsOnAlcohol
     */
    public String getCommentsOnAlcohol () {
        return _commentsOnAlcohol;
    }

    /**
     * Set the value related to the column: commentsOnAlcohol
     * @param _commentsOnAlcohol the commentsOnAlcohol value
     */
    public void setCommentsOnAlcohol (String _commentsOnAlcohol) {
        this._commentsOnAlcohol = _commentsOnAlcohol;
    }

    /**
     * Return the value associated with the column: radio_mentalIllness
     */
    public String getRadioMentalillness () {
        return _radioMentalillness;
    }

    /**
     * Set the value related to the column: radio_mentalIllness
     * @param _radioMentalillness the radio_mentalIllness value
     */
    public void setRadioMentalillness (String _radioMentalillness) {
        this._radioMentalillness = _radioMentalillness;
    }

    /**
     * Return the value associated with the column: cbox_speakSpanish
     */
    public String getCboxSpeakspanish () {
        return _cboxSpeakspanish;
    }

    /**
     * Set the value related to the column: cbox_speakSpanish
     * @param _cboxSpeakspanish the cbox_speakSpanish value
     */
    public void setCboxSpeakspanish (String _cboxSpeakspanish) {
        this._cboxSpeakspanish = _cboxSpeakspanish;
    }

    /**
     * Return the value associated with the column: drinksPerWeek
     */
    public String getDrinksPerWeek () {
        return _drinksPerWeek;
    }

    /**
     * Set the value related to the column: drinksPerWeek
     * @param _drinksPerWeek the drinksPerWeek value
     */
    public void setDrinksPerWeek (String _drinksPerWeek) {
        this._drinksPerWeek = _drinksPerWeek;
    }

    /**
     * Return the value associated with the column: CommentsOnImmigration
     */
    public String getCommentsOnImmigration () {
        return _commentsOnImmigration;
    }

    /**
     * Set the value related to the column: CommentsOnImmigration
     * @param _commentsOnImmigration the CommentsOnImmigration value
     */
    public void setCommentsOnImmigration (String _commentsOnImmigration) {
        this._commentsOnImmigration = _commentsOnImmigration;
    }

    /**
     * Return the value associated with the column: radio_behaviorProblem
     */
    public String getRadioBehaviorproblem () {
        return _radioBehaviorproblem;
    }

    /**
     * Set the value related to the column: radio_behaviorProblem
     * @param _radioBehaviorproblem the radio_behaviorProblem value
     */
    public void setRadioBehaviorproblem (String _radioBehaviorproblem) {
        this._radioBehaviorproblem = _radioBehaviorproblem;
    }

    /**
     * Return the value associated with the column: agency2Name
     */
    public String getAgency2Name () {
        return _agency2Name;
    }

    /**
     * Set the value related to the column: agency2Name
     * @param _agency2Name the agency2Name value
     */
    public void setAgency2Name (String _agency2Name) {
        this._agency2Name = _agency2Name;
    }

    /**
     * Return the value associated with the column: commentsOnNeedHelp
     */
    public String getCommentsOnNeedHelp () {
        return _commentsOnNeedHelp;
    }

    /**
     * Set the value related to the column: commentsOnNeedHelp
     * @param _commentsOnNeedHelp the commentsOnNeedHelp value
     */
    public void setCommentsOnNeedHelp (String _commentsOnNeedHelp) {
        this._commentsOnNeedHelp = _commentsOnNeedHelp;
    }

    /**
     * Return the value associated with the column: radio_havePublicTrustee
     */
    public String getRadioHavepublictrustee () {
        return _radioHavepublictrustee;
    }

    /**
     * Set the value related to the column: radio_havePublicTrustee
     * @param _radioHavepublictrustee the radio_havePublicTrustee value
     */
    public void setRadioHavepublictrustee (String _radioHavepublictrustee) {
        this._radioHavepublictrustee = _radioHavepublictrustee;
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
     * Return the value associated with the column: cbox_takePrescribedMedication
     */
    public String getCboxTakeprescribedmedication () {
        return _cboxTakeprescribedmedication;
    }

    /**
     * Set the value related to the column: cbox_takePrescribedMedication
     * @param _cboxTakeprescribedmedication the cbox_takePrescribedMedication value
     */
    public void setCboxTakeprescribedmedication (String _cboxTakeprescribedmedication) {
        this._cboxTakeprescribedmedication = _cboxTakeprescribedmedication;
    }

    /**
     * Return the value associated with the column: needAssistInLegal
     */
    public String getNeedAssistInLegal () {
        return _needAssistInLegal;
    }

    /**
     * Set the value related to the column: needAssistInLegal
     * @param _needAssistInLegal the needAssistInLegal value
     */
    public void setNeedAssistInLegal (String _needAssistInLegal) {
        this._needAssistInLegal = _needAssistInLegal;
    }

    /**
     * Return the value associated with the column: radio_caredForSchizophrenia
     */
    public String getRadioCaredforschizophrenia () {
        return _radioCaredforschizophrenia;
    }

    /**
     * Set the value related to the column: radio_caredForSchizophrenia
     * @param _radioCaredforschizophrenia the radio_caredForSchizophrenia value
     */
    public void setRadioCaredforschizophrenia (String _radioCaredforschizophrenia) {
        this._radioCaredforschizophrenia = _radioCaredforschizophrenia;
    }

    /**
     * Return the value associated with the column: formCreated
     */
    public java.util.Date getFormCreated () {
        return _formCreated;
    }

    /**
     * Set the value related to the column: formCreated
     * @param _formCreated the formCreated value
     */
    public void setFormCreated (java.util.Date _formCreated) {
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
     * Return the value associated with the column: radio_doYouDrink
     */
    public String getRadioDoyoudrink () {
        return _radioDoyoudrink;
    }

    /**
     * Set the value related to the column: radio_doYouDrink
     * @param _radioDoyoudrink the radio_doYouDrink value
     */
    public void setRadioDoyoudrink (String _radioDoyoudrink) {
        this._radioDoyoudrink = _radioDoyoudrink;
    }

    /**
     * Return the value associated with the column: radio_drinking
     */
    public String getRadioDrinking () {
        return _radioDrinking;
    }

    /**
     * Set the value related to the column: radio_drinking
     * @param _radioDrinking the radio_drinking value
     */
    public void setRadioDrinking (String _radioDrinking) {
        this._radioDrinking = _radioDrinking;
    }

    /**
     * Return the value associated with the column: radio_healthProblem
     */
    public String getRadioHealthproblem () {
        return _radioHealthproblem;
    }

    /**
     * Set the value related to the column: radio_healthProblem
     * @param _radioHealthproblem the radio_healthProblem value
     */
    public void setRadioHealthproblem (String _radioHealthproblem) {
        this._radioHealthproblem = _radioHealthproblem;
    }

    /**
     * Return the value associated with the column: cbox_UI
     */
    public String getCboxUi () {
        return _cboxUi;
    }

    /**
     * Set the value related to the column: cbox_UI
     * @param _cboxUi the cbox_UI value
     */
    public void setCboxUi (String _cboxUi) {
        this._cboxUi = _cboxUi;
    }

    /**
     * Return the value associated with the column: livedWithWhom
     */
    public String getLivedWithWhom () {
        return _livedWithWhom;
    }

    /**
     * Set the value related to the column: livedWithWhom
     * @param _livedWithWhom the livedWithWhom value
     */
    public void setLivedWithWhom (String _livedWithWhom) {
        this._livedWithWhom = _livedWithWhom;
    }

    /**
     * Return the value associated with the column: assistProvided3
     */
    public String getAssistProvided3 () {
        return _assistProvided3;
    }

    /**
     * Set the value related to the column: assistProvided3
     * @param _assistProvided3 the assistProvided3 value
     */
    public void setAssistProvided3 (String _assistProvided3) {
        this._assistProvided3 = _assistProvided3;
    }

    /**
     * Return the value associated with the column: cbox_haveManic
     */
    public String getCboxHavemanic () {
        return _cboxHavemanic;
    }

    /**
     * Set the value related to the column: cbox_haveManic
     * @param _cboxHavemanic the cbox_haveManic value
     */
    public void setCboxHavemanic (String _cboxHavemanic) {
        this._cboxHavemanic = _cboxHavemanic;
    }

    /**
     * Return the value associated with the column: contact1Name
     */
    public String getContact1Name () {
        return _contact1Name;
    }

    /**
     * Set the value related to the column: contact1Name
     * @param _contact1Name the contact1Name value
     */
    public void setContact1Name (String _contact1Name) {
        this._contact1Name = _contact1Name;
    }

    /**
     * Return the value associated with the column: year
     */
    public String getYear () {
        return _year;
    }

    /**
     * Set the value related to the column: year
     * @param _year the year value
     */
    public void setYear (String _year) {
        this._year = _year;
    }

    /**
     * Return the value associated with the column: radio_caredForManic
     */
    public String getRadioCaredformanic () {
        return _radioCaredformanic;
    }

    /**
     * Set the value related to the column: radio_caredForManic
     * @param _radioCaredformanic the radio_caredForManic value
     */
    public void setRadioCaredformanic (String _radioCaredformanic) {
        this._radioCaredformanic = _radioCaredformanic;
    }

    /**
     * Return the value associated with the column: cbox_assistWithRefugee
     */
    public String getCboxAssistwithrefugee () {
        return _cboxAssistwithrefugee;
    }

    /**
     * Set the value related to the column: cbox_assistWithRefugee
     * @param _cboxAssistwithrefugee the cbox_assistWithRefugee value
     */
    public void setCboxAssistwithrefugee (String _cboxAssistwithrefugee) {
        this._cboxAssistwithrefugee = _cboxAssistwithrefugee;
    }

    /**
     * Return the value associated with the column: radio_livedInSubsidized
     */
    public String getRadioLivedinsubsidized () {
        return _radioLivedinsubsidized;
    }

    /**
     * Set the value related to the column: radio_livedInSubsidized
     * @param _radioLivedinsubsidized the radio_livedInSubsidized value
     */
    public void setRadioLivedinsubsidized (String _radioLivedinsubsidized) {
        this._radioLivedinsubsidized = _radioLivedinsubsidized;
    }

    /**
     * Return the value associated with the column: radio_useDrugs
     */
    public String getRadioUsedrugs () {
        return _radioUsedrugs;
    }

    /**
     * Set the value related to the column: radio_useDrugs
     * @param _radioUsedrugs the radio_useDrugs value
     */
    public void setRadioUsedrugs (String _radioUsedrugs) {
        this._radioUsedrugs = _radioUsedrugs;
    }

    /**
     * Return the value associated with the column: cbox_needHelpInOther
     */
    public String getCboxNeedhelpinother () {
        return _cboxNeedhelpinother;
    }

    /**
     * Set the value related to the column: cbox_needHelpInOther
     * @param _cboxNeedhelpinother the cbox_needHelpInOther value
     */
    public void setCboxNeedhelpinother (String _cboxNeedhelpinother) {
        this._cboxNeedhelpinother = _cboxNeedhelpinother;
    }

    /**
     * Return the value associated with the column: assistProvided4
     */
    public String getAssistProvided4 () {
        return _assistProvided4;
    }

    /**
     * Set the value related to the column: assistProvided4
     * @param _assistProvided4 the assistProvided4 value
     */
    public void setAssistProvided4 (String _assistProvided4) {
        this._assistProvided4 = _assistProvided4;
    }

    /**
     * Return the value associated with the column: doctor1NameAddr
     */
    public String getDoctor1NameAddr () {
        return _doctor1NameAddr;
    }

    /**
     * Set the value related to the column: doctor1NameAddr
     * @param _doctor1NameAddr the doctor1NameAddr value
     */
    public void setDoctor1NameAddr (String _doctor1NameAddr) {
        this._doctor1NameAddr = _doctor1NameAddr;
    }

    /**
     * Return the value associated with the column: contact3Name
     */
    public String getContact3Name () {
        return _contact3Name;
    }

    /**
     * Set the value related to the column: contact3Name
     * @param _contact3Name the contact3Name value
     */
    public void setContact3Name (String _contact3Name) {
        this._contact3Name = _contact3Name;
    }

    /**
     * Return the value associated with the column: haveOther
     */
    public String getHaveOther () {
        return _haveOther;
    }

    /**
     * Set the value related to the column: haveOther
     * @param _haveOther the haveOther value
     */
    public void setHaveOther (String _haveOther) {
        this._haveOther = _haveOther;
    }

    /**
     * Return the value associated with the column: cbox_storeMedication
     */
    public String getCboxStoremedication () {
        return _cboxStoremedication;
    }

    /**
     * Set the value related to the column: cbox_storeMedication
     * @param _cboxStoremedication the cbox_storeMedication value
     */
    public void setCboxStoremedication (String _cboxStoremedication) {
        this._cboxStoremedication = _cboxStoremedication;
    }

    /**
     * Return the value associated with the column: agency4Name
     */
    public String getAgency4Name () {
        return _agency4Name;
    }

    /**
     * Set the value related to the column: agency4Name
     * @param _agency4Name the agency4Name value
     */
    public void setAgency4Name (String _agency4Name) {
        this._agency4Name = _agency4Name;
    }

    /**
     * Return the value associated with the column: radio_caredForAnxiety
     */
    public String getRadioCaredforanxiety () {
        return _radioCaredforanxiety;
    }

    /**
     * Set the value related to the column: radio_caredForAnxiety
     * @param _radioCaredforanxiety the radio_caredForAnxiety value
     */
    public void setRadioCaredforanxiety (String _radioCaredforanxiety) {
        this._radioCaredforanxiety = _radioCaredforanxiety;
    }

    /**
     * Return the value associated with the column: dateLastDoctor1Contact
     */
    public String getDateLastDoctor1Contact () {
        return _dateLastDoctor1Contact;
    }

    /**
     * Set the value related to the column: dateLastDoctor1Contact
     * @param _dateLastDoctor1Contact the dateLastDoctor1Contact value
     */
    public void setDateLastDoctor1Contact (String _dateLastDoctor1Contact) {
        this._dateLastDoctor1Contact = _dateLastDoctor1Contact;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Formintakeb)) return false;
        else {
            Formintakeb mObj = (Formintakeb) obj;
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
