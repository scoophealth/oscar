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

import org.apache.commons.lang.StringUtils;

/**
 * This is the object class that relates to the formintakec table.
 * Any customizations belong here.
 */
public class Formintakec implements Serializable {

    private int hashCode = Integer.MIN_VALUE;// primary key

    private Long _id;// fields
    private String _cboxReferralByPolice;
    private String _radioGender;
    private String _cboxSocialServiceIssueOther;
    private String _cboxBase2ndIncomeNone;
    private String _cboxCurrHasSIn;
    private String _cboxReferralByFredVictorCentreOther;
    private String _cboxReferralByDetentionCenter;
    private String _currDescriptionOfHousing;
    private String _cboxBaseUseHealthBus;
    private String _dateOfHospitalization;
    private String _cboxHousingIssue;
    private String _cboxBaseHasCommunity;
    private String _cboxBaseHasFriends;
    private String _cboxRelocationExit;
    private String _cbox2ndPersonalityDisorder;
    private String _cboxBase2ndIncomeSocialAssistance;
    private String _cbox2ndMedicalMentalDisorder;
    private String _cboxReferralByCriminalJusticeSystem;
    private String _cboxThreatIssue;
    private String _cboxSubstanceAnxietyDisorder;
    private String _referralComment;
    private String _cboxBase2ndIncomeEi;
    private String _cboxCurrHasSupportUnknown;
    private String _cboxReferralBySelf;
    private String _cboxPhysicalIssueOther;
    private String _cboxCurrLivingWithSpouse;
    private String _cboxReferralByOtherInstitution;
    private String _cboxPhysicalHospitalization;
    private String _currWhyClientDoNotAccessSocialServices;
    private String _cboxFinancialIssue;
    private String _cboxIsolationIssueOther;
    private String _cboxCurrLivingWithUnknown;
    private String _cbox2ndIncomeOther;
    private String _cboxCurrHasUnknown;
    private String _cboxReferralByPhysician;
    private String _cbox2ndDisassociativeDisorder;
    private String _cboxImmigrationIssueOther;
    private String _entryDate;
    private String _admissionDate;
    private String _cboxCurrHasCommunity;
    private String _cboxDailyActivityIssue;
    private String _cboxCurrLivingWithParents;
    private String _cboxIdentificationIssueOther;
    private String _radioBaseEmploymentStatus;
    private String _radioCanadianBorn;
    private String _cboxBaseLivingWithNonrelatives;
    private String _cboxCurrIncomeMgmentDoNotNeedTrustee;
    private String _cboxBase2ndIncomeFamily;
    private String _radioCurrEmploymentStatus;
    private String _cboxSuicideExit;
    private String _cboxReferralByStreetNurseOther;
    private String _dayOfBirth;
    private String _cboxDualDisorder;
    private String _cbox2ndIncomeODSp;
    private String _cboxPsychiatricHospitalization;
    private String _cbox2ndSomatoformDisorder;
    private String _cboxPreAdmission;
    private String _cboxReferralByStreetHealthReceptionOther;
    private String _cbox2ndAnxietyDisorder;
    private String _radioCurrParticipateInEduction;
    private String _cbox2ndChildhoodDisorder;
    private String _cboxCurrIncomeMgmentHasTrustee;
    private String _radioCountryOfOrigin;
    private String _cboxOtherChronicIllness;
    private String _cboxMOHLTCDisorder;
    private String _cboxPTSd;
    private String _cboxBase2ndIncomeDisabilityAssistance;
    private String _lengthOfHospitalization;
    private String _radioCurrPrimaryIncomeSource;
    private String _baseWhyClientDoNotAccessSocialServices;
    private String _cbox2ndIncomeInformalOther;
    private String _cbox2ndIncomeEi;
    private String _cbox2ndGenderIdentityDisorder;
    private String _cboxBaseDoNotAccessHealthCare;
    private String _currSocialServiceClientAccesses;
    private String _cbox2ndIncomeUnknown;
    private String _radioBaseHealthCareAccess;
    private String _cboxBase2ndIncomeODSp;
    private String _cboxCurrUseWalkinClinic;
    private String _cboxReferralByPsychiatrists;
    private String _cboxReferralByMentalOrg;
    private String _radioBasePrimaryResidenceType;
    private String _cbox2ndIncomeDisabilityAssistance;
    private String _cboxAddictionIssue;
    private String _cboxBase2ndIncomePanhandlingOther;
    private String _cboxCurrIncomeMgmentUnknown;
    private String _cboxLegalIssue;
    private String _radioResistTreatment;
    private String _cbox2ndSchizophrenia;
    private String _radioBaseSocialServiceAccess;
    private String _radioCurrSocialServiceAccess;
    private String _radioBaseResidenceStatus;
    private String _cbox2ndIncomeEmployment;
    private String _cbox2ndAnxietyDisorderFromSubstance;
    private String _radioCurrPrimaryResidenceType;
    private String _radioCurrPrimaryIncomeSourceOther;
    private String _cboxReferralByProbation;
    private String _radioCurrLegalStatus;
    private String _cboxProblemsWithPolice2;
    private java.util.Date _formCreated;
    private String _cbox2ndIncomeSocialAssistance;
    private String _cboxBaseUseHospitalEmergency;
    private String _cboxReferralByPublic;
    private String _cboxReferralByHospital;
    private String _cboxBaseLivingWithSpousePlus;
    private String _cboxWithdrawalExit;
    private String _cboxCurrLivingWithSelf;
    private String _cbox2ndCognitiveDisorder;
    private String _cboxBaseIncomeMgmentUnknown;
    private String _cboxBaseHasHealthCard;
    private String _radioCurrHighestEductionLevel;
    private String _cboxReferralByPsychiatricHospital;
    private String _cboxCurrLivingWithNonrelatives;
    private String _cboxFamilyLawIssues1;
    private String _cboxProblemsWithPolice1;
    private String _cboxBaseHasRegularHealthProvider;
    private String _cboxFamilyLawIssues2;
    private String _cboxCurrUseShelterClinic;
    private String _clientNum;
    private String _cbox2ndImpulsiveDisorder;
    private String _radioIsAboriginal;
    private String _baseSocialServiceClientAccesses;
    private String _cboxBase2ndIncomeOther;
    private String _cboxBaseIncomeMgmentNeedsTrusteeButDoNotWant;
    private String _cbox2ndSleepDisorder;
    private String _cboxBaseHasNonStatus;
    private String _cboxHealthCareIssueOther;
    private String _radioCurrHealthCareAccess;
    private String _cboxBaseHasCertificate;
    private String _cboxBaseLivingWithSelf;
    private String _cboxCurrIncomeMgmentNeedsTrustee;
    private String _cboxBase2ndIncomeUnknown;
    private String _radioLanguageEnglish;
    private String _yearArrivedInCanada;
    private String _cboxDateOfBirthUnknown;
    private String _cboxOtherIssue;
    private String _cboxCurrHasHealthCard;
    private String _cboxCurrHasSomeone;
    private String _cboxCurrHasRegularHealthProvider;
    private String _cbox2ndDevelopmentalDisorder;
    private String _cboxBase2ndIncomeInformalOther;
    private String _cboxOCd;
    private String _cboxCurrUseHospitalEmergency;
    private String _cboxBaseLivingWithUnknown;
    private String _cbox2ndFactitiousDisorder;
    private String _cboxBaseHasSomeone;
    private String _cboxRelationalIssue;
    private String _cboxBaseHasNativeCard;
    private String _radioBaseLegalStatus;
    private String _cbox2ndIncomeFamily;
    private String _cboxCurrHasRelatives;
    private String _cboxCurrHasCertificate;
    private String _monthOfBirth;
    private String _cboxBaseHasSIn;
    private String _clientSurname;
    private String _cbox2ndAnxietyDisorderOCd;
    private String _staffName;
    private String _cboxReferralByStreetIDWorkerOther;
    private String _cbox2ndSubstanceDisorder;
    private String _radioCurrNeedSocialServices;
    private String _radioBaseHighestEductionLevel;
    private String _cboxBaseHasSupportUnknown;
    private String _radioCurrResidenceStatus;
    private String _cboxBaseIncomeMgmentDoNotNeedTrustee;
    private String _countryOfOrigin;
    private String _cboxCurrHasFriends;
    private String _radioBasePrimaryIncomeSource;
    private String _cbox2ndAnxietyDisorderOther;
    private String _cboxEducationalIssue;
    private String _radioBasePrimaryIncomeSourceOther;
    private String _radioBaseParticipateInEduction;
    private String _cboxCurrLivingWithRelatives;
    private String _baseDescriptionOfHousing;
    private String _cboxReferralByCourt;
    private String _cbox2ndIncomePanhandlingOther;
    private String _cboxReferralByOtherAgency;
    private String _cboxCaseFile;
    private String _cboxCurrLivingWithChildren;
    private String _cboxReferralByMentalHealthWorker;
    private String _cboxCurrIncomeMgmentNeedsTrusteeButDoNotWant;
    private String _clientFirstName;
    private String _cboxBankingIssueOther;
    private String _cboxBase2ndIncomeEmployment;
    private String _cboxNAExit;
    private String _cbox2ndIncomeNone;
    private String _cboxNa;
    private String _cboxBaseHasRelatives;
    private String _cboxCurrHasNonStatus;
    private String _cboxEmploymentIssue;
    private String _yearOfBirth;
    private String _cboxBase2ndIncomePension;
    private String _cboxCurrLivingWithSpousePlus;
    private String _cboxPreferredLanguageUnknown;
    private String _cbox2ndAnxietyDisorderPSd;
    private String _cboxSexualAbuseIssue;
    private java.util.Date _formEdited;
    private String _cboxBaseUseShelterClinic;
    private String _cboxCurrAccessHealthCareUnknown;
    private String _cbox2ndIncomePension;
    private Long _providerNo;
    private String _cboxCurrDoNotAccessHealthCare;
    private String _cboxCurrUseHealthBus;
    private String _cboxBaseLivingWithParents;
    private String _cboxBaseHasUnknown;
    private String _radioBaseNeedSocialServices;
    private String _cboxNoneListedIssue;
    private String _cbox2ndUnknown;
    private String _cboxReferralBySafeBeds;
    private String _cbox2ndAdjustDisorder;
    private String _cboxHospitalizationUnknown;
    private String _cboxConcurrentDisorder;
    private String _cbox2ndEatingDisorder;
    private String _cboxCompleteWithReferral;
    private String _cboxBaseUseWalkinClinic;
    private String _monthlyProgressReport;
    private String _cboxOtherAnxietyDisorder;
    private String _cboxBaseIncomeMgmentHasTrustee;
    private Long _demographicNo;
    private String _cboxBaseIncomeMgmentNeedsTrustee;
    private String _radioRaceCaucasian;
    private String _cboxMentalIssue;
    private String _preferredLanguage;
    private String _cbox2ndMoodDisorder;
    private String _radioPrimaryDiagnosis;
    private String _cboxCurrHasNativeCard;
    private String _cboxBaseLivingWithChildren;
    private String _cboxBaseLivingWithSpouse;
    private String _radioTreatmentOrders;
    private String _cboxReferralByOtherPeople;
    private String _cboxBaseAccessHealthCareUnknown;
    private String _cboxCompleteWithoutReferral;
    private String _cboxDeathExit;
    private String _cboxBaseLivingWithRelatives;
    private String _currAddress;
    private String _currPhone;
    private String _pastAddresses;
    private String _contactsInfo;
    private String _ids;
    private String _hospitalizations;

    // constructors
    public Formintakec () {
        initialize();
    }

    /**
     * Constructor for primary key
     */
    public Formintakec (Long _id) {
        this.setId(_id);
        initialize();
    }

    /**
     * Constructor for required fields
     */
    public Formintakec (
            Long _id,
            Long _demographicNo) {

        this.setId(_id);
        this.setDemographicNo(_demographicNo);
        initialize();
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
     * Return the value associated with the column: cboxReferralByPolice
     */
    public String getCboxReferralByPolice () {
        return _cboxReferralByPolice;
    }

    /**
     * Set the value related to the column: cboxReferralByPolice
     * @param _cboxReferralByPolice the cboxReferralByPolice value
     */
    public void setCboxReferralByPolice (String _cboxReferralByPolice) {
        this._cboxReferralByPolice = _cboxReferralByPolice;
    }

    /**
     * Return the value associated with the column: radioGender
     */
    public String getRadioGender () {
        if (_radioGender!=""&&_radioGender!=null)
            return _radioGender;
        else return "4";
    }

    /**
     * Set the value related to the column: radioGender
     * @param _radioGender the radioGender value
     */
    public void setRadioGender (String _radioGender) {
        this._radioGender = _radioGender;
    }

    /**
     * Return the value associated with the column: cboxSocialServiceIssueOther
     */
    public String getCboxSocialServiceIssueOther () {
        return _cboxSocialServiceIssueOther;
    }

    /**
     * Set the value related to the column: cboxSocialServiceIssueOther
     * @param _cboxSocialServiceIssueOther the cboxSocialServiceIssueOther value
     */
    public void setCboxSocialServiceIssueOther (String _cboxSocialServiceIssueOther) {
        this._cboxSocialServiceIssueOther = _cboxSocialServiceIssueOther;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomeNone
     */
    public String getCboxBase2ndIncomeNone () {
        return _cboxBase2ndIncomeNone;
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomeNone
     * @param _cboxBase2ndIncomeNone the cboxBase2ndIncomeNone value
     */
    public void setCboxBase2ndIncomeNone (String _cboxBase2ndIncomeNone) {
        this._cboxBase2ndIncomeNone = _cboxBase2ndIncomeNone;
    }

    /**
     * Return the value associated with the column: cboxCurrHasSIn
     */
    public String getCboxCurrHasSIn () {
        return _cboxCurrHasSIn;
    }

    /**
     * Set the value related to the column: cboxCurrHasSIn
     * @param _cboxCurrHasSIn the cboxCurrHasSIn value
     */
    public void setCboxCurrHasSIn (String _cboxCurrHasSIn) {
        this._cboxCurrHasSIn = _cboxCurrHasSIn;
    }

    /**
     * Return the value associated with the column: cboxReferralByFredVictorCentreOther
     */
    public String getCboxReferralByFredVictorCentreOther () {
        return _cboxReferralByFredVictorCentreOther;
    }

    /**
     * Set the value related to the column: cboxReferralByFredVictorCentreOther
     * @param _cboxReferralByFredVictorCentreOther the cboxReferralByFredVictorCentreOther value
     */
    public void setCboxReferralByFredVictorCentreOther (String _cboxReferralByFredVictorCentreOther) {
        this._cboxReferralByFredVictorCentreOther = _cboxReferralByFredVictorCentreOther;
    }

    /**
     * Return the value associated with the column: cboxReferralByDetentionCenter
     */
    public String getCboxReferralByDetentionCenter () {
        return _cboxReferralByDetentionCenter;
    }

    /**
     * Set the value related to the column: cboxReferralByDetentionCenter
     * @param _cboxReferralByDetentionCenter the cboxReferralByDetentionCenter value
     */
    public void setCboxReferralByDetentionCenter (String _cboxReferralByDetentionCenter) {
        this._cboxReferralByDetentionCenter = _cboxReferralByDetentionCenter;
    }

    /**
     * Return the value associated with the column: currDescriptionOfHousing
     */
    public String getCurrDescriptionOfHousing () {
        return _currDescriptionOfHousing;
    }

    /**
     * Set the value related to the column: currDescriptionOfHousing
     * @param _currDescriptionOfHousing the currDescriptionOfHousing value
     */
    public void setCurrDescriptionOfHousing (String _currDescriptionOfHousing) {
        this._currDescriptionOfHousing = _currDescriptionOfHousing;
    }

    /**
     * Return the value associated with the column: cboxBaseUseHealthBus
     */
    public String getCboxBaseUseHealthBus () {
        return _cboxBaseUseHealthBus;
    }

    /**
     * Set the value related to the column: cboxBaseUseHealthBus
     * @param _cboxBaseUseHealthBus the cboxBaseUseHealthBus value
     */
    public void setCboxBaseUseHealthBus (String _cboxBaseUseHealthBus) {
        this._cboxBaseUseHealthBus = _cboxBaseUseHealthBus;
    }

    /**
     * Return the value associated with the column: dateOfHospitalization
     */
    public String getDateOfHospitalization () {
        return _dateOfHospitalization;
    }

    /**
     * Set the value related to the column: dateOfHospitalization
     * @param _dateOfHospitalization the dateOfHospitalization value
     */
    public void setDateOfHospitalization (String _dateOfHospitalization) {
        this._dateOfHospitalization = _dateOfHospitalization;
    }

    /**
     * Return the value associated with the column: cboxHousingIssue
     */
    public String getCboxHousingIssue () {
        return _cboxHousingIssue;
    }

    /**
     * Set the value related to the column: cboxHousingIssue
     * @param _cboxHousingIssue the cboxHousingIssue value
     */
    public void setCboxHousingIssue (String _cboxHousingIssue) {
        this._cboxHousingIssue = _cboxHousingIssue;
    }

    /**
     * Return the value associated with the column: cboxBaseHasCommunity
     */
    public String getCboxBaseHasCommunity () {
        return _cboxBaseHasCommunity;
    }

    /**
     * Set the value related to the column: cboxBaseHasCommunity
     * @param _cboxBaseHasCommunity the cboxBaseHasCommunity value
     */
    public void setCboxBaseHasCommunity (String _cboxBaseHasCommunity) {
        this._cboxBaseHasCommunity = _cboxBaseHasCommunity;
    }

    /**
     * Return the value associated with the column: cboxBaseHasFriends
     */
    public String getCboxBaseHasFriends () {
        return _cboxBaseHasFriends;
    }

    /**
     * Set the value related to the column: cboxBaseHasFriends
     * @param _cboxBaseHasFriends the cboxBaseHasFriends value
     */
    public void setCboxBaseHasFriends (String _cboxBaseHasFriends) {
        this._cboxBaseHasFriends = _cboxBaseHasFriends;
    }

    /**
     * Return the value associated with the column: cboxRelocationExit
     */
    public String getCboxRelocationExit () {
        return _cboxRelocationExit;
    }

    /**
     * Set the value related to the column: cboxRelocationExit
     * @param _cboxRelocationExit the cboxRelocationExit value
     */
    public void setCboxRelocationExit (String _cboxRelocationExit) {
        this._cboxRelocationExit = _cboxRelocationExit;
    }

    /**
     * Return the value associated with the column: cbox2ndPersonalityDisorder
     */
    public String getCbox2ndPersonalityDisorder () {
        return _cbox2ndPersonalityDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndPersonalityDisorder
     * @param _cbox2ndPersonalityDisorder the cbox2ndPersonalityDisorder value
     */
    public void setCbox2ndPersonalityDisorder (String _cbox2ndPersonalityDisorder) {
        this._cbox2ndPersonalityDisorder = _cbox2ndPersonalityDisorder;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomeSocialAssistance
     */
    public String getCboxBase2ndIncomeSocialAssistance () {
        return _cboxBase2ndIncomeSocialAssistance;
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomeSocialAssistance
     * @param _cboxBase2ndIncomeSocialAssistance the cboxBase2ndIncomeSocialAssistance value
     */
    public void setCboxBase2ndIncomeSocialAssistance (String _cboxBase2ndIncomeSocialAssistance) {
        this._cboxBase2ndIncomeSocialAssistance = _cboxBase2ndIncomeSocialAssistance;
    }

    /**
     * Return the value associated with the column: cbox2ndMedicalMentalDisorder
     */
    public String getCbox2ndMedicalMentalDisorder () {
        return _cbox2ndMedicalMentalDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndMedicalMentalDisorder
     * @param _cbox2ndMedicalMentalDisorder the cbox2ndMedicalMentalDisorder value
     */
    public void setCbox2ndMedicalMentalDisorder (String _cbox2ndMedicalMentalDisorder) {
        this._cbox2ndMedicalMentalDisorder = _cbox2ndMedicalMentalDisorder;
    }

    /**
     * Return the value associated with the column: cboxReferralByCriminalJusticeSystem
     */
    public String getCboxReferralByCriminalJusticeSystem () {
        return _cboxReferralByCriminalJusticeSystem;
    }

    /**
     * Set the value related to the column: cboxReferralByCriminalJusticeSystem
     * @param _cboxReferralByCriminalJusticeSystem the cboxReferralByCriminalJusticeSystem value
     */
    public void setCboxReferralByCriminalJusticeSystem (String _cboxReferralByCriminalJusticeSystem) {
        this._cboxReferralByCriminalJusticeSystem = _cboxReferralByCriminalJusticeSystem;
    }

    /**
     * Return the value associated with the column: cboxThreatIssue
     */
    public String getCboxThreatIssue () {
        return _cboxThreatIssue;
    }

    /**
     * Set the value related to the column: cboxThreatIssue
     * @param _cboxThreatIssue the cboxThreatIssue value
     */
    public void setCboxThreatIssue (String _cboxThreatIssue) {
        this._cboxThreatIssue = _cboxThreatIssue;
    }

    /**
     * Return the value associated with the column: cboxSubstanceAnxietyDisorder
     */
    public String getCboxSubstanceAnxietyDisorder () {
        return _cboxSubstanceAnxietyDisorder;
    }

    /**
     * Set the value related to the column: cboxSubstanceAnxietyDisorder
     * @param _cboxSubstanceAnxietyDisorder the cboxSubstanceAnxietyDisorder value
     */
    public void setCboxSubstanceAnxietyDisorder (String _cboxSubstanceAnxietyDisorder) {
        this._cboxSubstanceAnxietyDisorder = _cboxSubstanceAnxietyDisorder;
    }

    /**
     * Return the value associated with the column: referralComment
     */
    public String getReferralComment () {
        return _referralComment;
    }

    /**
     * Set the value related to the column: referralComment
     * @param _referralComment the referralComment value
     */
    public void setReferralComment (String _referralComment) {
        this._referralComment = _referralComment;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomeEi
     */
    public String getCboxBase2ndIncomeEi () {
        return _cboxBase2ndIncomeEi;
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomeEi
     * @param _cboxBase2ndIncomeEi the cboxBase2ndIncomeEi value
     */
    public void setCboxBase2ndIncomeEi (String _cboxBase2ndIncomeEi) {
        this._cboxBase2ndIncomeEi = _cboxBase2ndIncomeEi;
    }

    /**
     * Return the value associated with the column: cboxCurrHasSupportUnknown
     */
    public String getCboxCurrHasSupportUnknown () {
        if(_cboxCurrHasSupportUnknown!=""&&_cboxCurrHasSupportUnknown!=null)
            return _cboxCurrHasSupportUnknown;
        else return "true";
    }

    /**
     * Set the value related to the column: cboxCurrHasSupportUnknown
     * @param _cboxCurrHasSupportUnknown the cboxCurrHasSupportUnknown value
     */
    public void setCboxCurrHasSupportUnknown (String _cboxCurrHasSupportUnknown) {
        this._cboxCurrHasSupportUnknown = _cboxCurrHasSupportUnknown;
    }

    /**
     * Return the value associated with the column: cboxReferralBySelf
     */
    public String getCboxReferralBySelf () {
        return _cboxReferralBySelf;
    }

    /**
     * Set the value related to the column: cboxReferralBySelf
     * @param _cboxReferralBySelf the cboxReferralBySelf value
     */
    public void setCboxReferralBySelf (String _cboxReferralBySelf) {
        this._cboxReferralBySelf = _cboxReferralBySelf;
    }

    /**
     * Return the value associated with the column: cboxPhysicalIssueOther
     */
    public String getCboxPhysicalIssueOther () {
        return _cboxPhysicalIssueOther;
    }

    /**
     * Set the value related to the column: cboxPhysicalIssueOther
     * @param _cboxPhysicalIssueOther the cboxPhysicalIssueOther value
     */
    public void setCboxPhysicalIssueOther (String _cboxPhysicalIssueOther) {
        this._cboxPhysicalIssueOther = _cboxPhysicalIssueOther;
    }

    /**
     * Return the value associated with the column: cboxCurrLivingWithSpouse
     */
    public String getCboxCurrLivingWithSpouse () {
        return _cboxCurrLivingWithSpouse;
    }

    /**
     * Set the value related to the column: cboxCurrLivingWithSpouse
     * @param _cboxCurrLivingWithSpouse the cboxCurrLivingWithSpouse value
     */
    public void setCboxCurrLivingWithSpouse (String _cboxCurrLivingWithSpouse) {
        this._cboxCurrLivingWithSpouse = _cboxCurrLivingWithSpouse;
    }

    /**
     * Return the value associated with the column: cboxReferralByOtherInstitution
     */
    public String getCboxReferralByOtherInstitution () {
        return _cboxReferralByOtherInstitution;
    }

    /**
     * Set the value related to the column: cboxReferralByOtherInstitution
     * @param _cboxReferralByOtherInstitution the cboxReferralByOtherInstitution value
     */
    public void setCboxReferralByOtherInstitution (String _cboxReferralByOtherInstitution) {
        this._cboxReferralByOtherInstitution = _cboxReferralByOtherInstitution;
    }

    /**
     * Return the value associated with the column: cboxPhysicalHospitalization
     */
    public String getCboxPhysicalHospitalization () {
        return _cboxPhysicalHospitalization;
    }

    /**
     * Set the value related to the column: cboxPhysicalHospitalization
     * @param _cboxPhysicalHospitalization the cboxPhysicalHospitalization value
     */
    public void setCboxPhysicalHospitalization (String _cboxPhysicalHospitalization) {
        this._cboxPhysicalHospitalization = _cboxPhysicalHospitalization;
    }

    /**
     * Return the value associated with the column: currWhyClientDoNotAccessSocialServices
     */
    public String getCurrWhyClientDoNotAccessSocialServices () {
        return _currWhyClientDoNotAccessSocialServices;
    }

    /**
     * Set the value related to the column: currWhyClientDoNotAccessSocialServices
     * @param _currWhyClientDoNotAccessSocialServices the currWhyClientDoNotAccessSocialServices value
     */
    public void setCurrWhyClientDoNotAccessSocialServices (String _currWhyClientDoNotAccessSocialServices) {
        this._currWhyClientDoNotAccessSocialServices = _currWhyClientDoNotAccessSocialServices;
    }

    /**
     * Return the value associated with the column: cboxFinancialIssue
     */
    public String getCboxFinancialIssue () {
        return _cboxFinancialIssue;
    }

    /**
     * Set the value related to the column: cboxFinancialIssue
     * @param _cboxFinancialIssue the cboxFinancialIssue value
     */
    public void setCboxFinancialIssue (String _cboxFinancialIssue) {
        this._cboxFinancialIssue = _cboxFinancialIssue;
    }

    /**
     * Return the value associated with the column: cboxIsolationIssueOther
     */
    public String getCboxIsolationIssueOther () {
        return _cboxIsolationIssueOther;
    }

    /**
     * Set the value related to the column: cboxIsolationIssueOther
     * @param _cboxIsolationIssueOther the cboxIsolationIssueOther value
     */
    public void setCboxIsolationIssueOther (String _cboxIsolationIssueOther) {
        this._cboxIsolationIssueOther = _cboxIsolationIssueOther;
    }

    /**
     * Return the value associated with the column: cboxCurrLivingWithUnknown
     */
    public String getCboxCurrLivingWithUnknown () {
        if(_cboxCurrLivingWithUnknown!=""&&_cboxCurrLivingWithUnknown!=null)
            return _cboxCurrLivingWithUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxCurrLivingWithUnknown
     * @param _cboxCurrLivingWithUnknown the cboxCurrLivingWithUnknown value
     */
    public void setCboxCurrLivingWithUnknown (String _cboxCurrLivingWithUnknown) {
        this._cboxCurrLivingWithUnknown = _cboxCurrLivingWithUnknown;
    }

    /**
     * Return the value associated with the column: cbox2ndIncomeOther
     */
    public String getCbox2ndIncomeOther () {
        return _cbox2ndIncomeOther;
    }

    /**
     * Set the value related to the column: cbox2ndIncomeOther
     * @param _cbox2ndIncomeOther the cbox2ndIncomeOther value
     */
    public void setCbox2ndIncomeOther (String _cbox2ndIncomeOther) {
        this._cbox2ndIncomeOther = _cbox2ndIncomeOther;
    }

    /**
     * Return the value associated with the column: cboxCurrHasUnknown
     */
    public String getCboxCurrHasUnknown () {
        if(_cboxCurrHasUnknown!=""&&_cboxCurrHasUnknown!=null)
            return _cboxCurrHasUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxCurrHasUnknown
     * @param _cboxCurrHasUnknown the cboxCurrHasUnknown value
     */
    public void setCboxCurrHasUnknown (String _cboxCurrHasUnknown) {
        this._cboxCurrHasUnknown = _cboxCurrHasUnknown;
    }

    /**
     * Return the value associated with the column: cboxReferralByPhysician
     */
    public String getCboxReferralByPhysician () {
        return _cboxReferralByPhysician;
    }

    /**
     * Set the value related to the column: cboxReferralByPhysician
     * @param _cboxReferralByPhysician the cboxReferralByPhysician value
     */
    public void setCboxReferralByPhysician (String _cboxReferralByPhysician) {
        this._cboxReferralByPhysician = _cboxReferralByPhysician;
    }

    /**
     * Return the value associated with the column: cbox2ndDisassociativeDisorder
     */
    public String getCbox2ndDisassociativeDisorder () {
        return _cbox2ndDisassociativeDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndDisassociativeDisorder
     * @param _cbox2ndDisassociativeDisorder the cbox2ndDisassociativeDisorder value
     */
    public void setCbox2ndDisassociativeDisorder (String _cbox2ndDisassociativeDisorder) {
        this._cbox2ndDisassociativeDisorder = _cbox2ndDisassociativeDisorder;
    }

    /**
     * Return the value associated with the column: cboxImmigrationIssueOther
     */
    public String getCboxImmigrationIssueOther () {
        return _cboxImmigrationIssueOther;
    }

    /**
     * Set the value related to the column: cboxImmigrationIssueOther
     * @param _cboxImmigrationIssueOther the cboxImmigrationIssueOther value
     */
    public void setCboxImmigrationIssueOther (String _cboxImmigrationIssueOther) {
        this._cboxImmigrationIssueOther = _cboxImmigrationIssueOther;
    }

    /**
     * Return the value associated with the column: entryDate
     */
    public String getEntryDate () {
        return _entryDate;
    }

    /**
     * Set the value related to the column: entryDate
     * @param _entryDate the entryDate value
     */
    public void setEntryDate (String _entryDate) {
        this._entryDate = _entryDate;
    }

    /**
     * Return the value associated with the column: admissionDate
     */
    public String getAdmissionDate () {
        return _admissionDate;
    }

    /**
     * Set the value related to the column: admissionDate
     * @param _admissionDate the admissionDate value
     */
    public void setAdmissionDate (String _admissionDate) {
        this._admissionDate = _admissionDate;
    }

    /**
     * Return the value associated with the column: cboxCurrHasCommunity
     */
    public String getCboxCurrHasCommunity () {
        return _cboxCurrHasCommunity;
    }

    /**
     * Set the value related to the column: cboxCurrHasCommunity
     * @param _cboxCurrHasCommunity the cboxCurrHasCommunity value
     */
    public void setCboxCurrHasCommunity (String _cboxCurrHasCommunity) {
        this._cboxCurrHasCommunity = _cboxCurrHasCommunity;
    }

    /**
     * Return the value associated with the column: cboxDailyActivityIssue
     */
    public String getCboxDailyActivityIssue () {
        return _cboxDailyActivityIssue;
    }

    /**
     * Set the value related to the column: cboxDailyActivityIssue
     * @param _cboxDailyActivityIssue the cboxDailyActivityIssue value
     */
    public void setCboxDailyActivityIssue (String _cboxDailyActivityIssue) {
        this._cboxDailyActivityIssue = _cboxDailyActivityIssue;
    }

    /**
     * Return the value associated with the column: cboxCurrLivingWithParents
     */
    public String getCboxCurrLivingWithParents () {
        return _cboxCurrLivingWithParents;
    }

    /**
     * Set the value related to the column: cboxCurrLivingWithParents
     * @param _cboxCurrLivingWithParents the cboxCurrLivingWithParents value
     */
    public void setCboxCurrLivingWithParents (String _cboxCurrLivingWithParents) {
        this._cboxCurrLivingWithParents = _cboxCurrLivingWithParents;
    }

    /**
     * Return the value associated with the column: cboxIdentificationIssueOther
     */
    public String getCboxIdentificationIssueOther () {
        return _cboxIdentificationIssueOther;
    }

    /**
     * Set the value related to the column: cboxIdentificationIssueOther
     * @param _cboxIdentificationIssueOther the cboxIdentificationIssueOther value
     */
    public void setCboxIdentificationIssueOther (String _cboxIdentificationIssueOther) {
        this._cboxIdentificationIssueOther = _cboxIdentificationIssueOther;
    }

    /**
     * Return the value associated with the column: radioBaseEmploymentStatus
     */
    public String getRadioBaseEmploymentStatus () {
        if(_radioBaseEmploymentStatus!=""&&_radioBaseEmploymentStatus!=null)
            return _radioBaseEmploymentStatus;
        else return "10";
    }

    /**
     * Set the value related to the column: radioBaseEmploymentStatus
     * @param _radioBaseEmploymentStatus the radioBaseEmploymentStatus value
     */
    public void setRadioBaseEmploymentStatus (String _radioBaseEmploymentStatus) {
        this._radioBaseEmploymentStatus = _radioBaseEmploymentStatus;
    }

    /**
     * Return the value associated with the column: radioCanadianBorn
     */
    public String getRadioCanadianBorn () {
        if (_radioCanadianBorn!=""&&_radioCanadianBorn!=null)
            return _radioCanadianBorn;
        else return "2";
    }

    /**
     * Set the value related to the column: radioCanadianBorn
     * @param _radioCanadianBorn the radioCanadianBorn value
     */
    public void setRadioCanadianBorn (String _radioCanadianBorn) {
        this._radioCanadianBorn = _radioCanadianBorn;
    }

    /**
     * Return the value associated with the column: cboxBaseLivingWithNonrelatives
     */
    public String getCboxBaseLivingWithNonrelatives () {
        return _cboxBaseLivingWithNonrelatives;
    }

    /**
     * Set the value related to the column: cboxBaseLivingWithNonrelatives
     * @param _cboxBaseLivingWithNonrelatives the cboxBaseLivingWithNonrelatives value
     */
    public void setCboxBaseLivingWithNonrelatives (String _cboxBaseLivingWithNonrelatives) {
        this._cboxBaseLivingWithNonrelatives = _cboxBaseLivingWithNonrelatives;
    }

    /**
     * Return the value associated with the column: cboxCurrIncomeMgmentDoNotNeedTrustee
     */
    public String getCboxCurrIncomeMgmentDoNotNeedTrustee () {
        return _cboxCurrIncomeMgmentDoNotNeedTrustee;
    }

    /**
     * Set the value related to the column: cboxCurrIncomeMgmentDoNotNeedTrustee
     * @param _cboxCurrIncomeMgmentDoNotNeedTrustee the cboxCurrIncomeMgmentDoNotNeedTrustee value
     */
    public void setCboxCurrIncomeMgmentDoNotNeedTrustee (String _cboxCurrIncomeMgmentDoNotNeedTrustee) {
        this._cboxCurrIncomeMgmentDoNotNeedTrustee = _cboxCurrIncomeMgmentDoNotNeedTrustee;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomeFamily
     */
    public String getCboxBase2ndIncomeFamily () {
        return _cboxBase2ndIncomeFamily;
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomeFamily
     * @param _cboxBase2ndIncomeFamily the cboxBase2ndIncomeFamily value
     */
    public void setCboxBase2ndIncomeFamily (String _cboxBase2ndIncomeFamily) {
        this._cboxBase2ndIncomeFamily = _cboxBase2ndIncomeFamily;
    }

    /**
     * Return the value associated with the column: radioCurrEmploymentStatus
     */
    public String getRadioCurrEmploymentStatus () {
        if(_radioCurrEmploymentStatus!=""&&_radioCurrEmploymentStatus!=null)
            return _radioCurrEmploymentStatus;
        else return "10";
    }

    /**
     * Set the value related to the column: radioCurrEmploymentStatus
     * @param _radioCurrEmploymentStatus the radioCurrEmploymentStatus value
     */
    public void setRadioCurrEmploymentStatus (String _radioCurrEmploymentStatus) {
        this._radioCurrEmploymentStatus = _radioCurrEmploymentStatus;
    }

    /**
     * Return the value associated with the column: cboxSuicideExit
     */
    public String getCboxSuicideExit () {
        return _cboxSuicideExit;
    }

    /**
     * Set the value related to the column: cboxSuicideExit
     * @param _cboxSuicideExit the cboxSuicideExit value
     */
    public void setCboxSuicideExit (String _cboxSuicideExit) {
        this._cboxSuicideExit = _cboxSuicideExit;
    }

    /**
     * Return the value associated with the column: cboxReferralByStreetNurseOther
     */
    public String getCboxReferralByStreetNurseOther () {
        return _cboxReferralByStreetNurseOther;
    }

    /**
     * Set the value related to the column: cboxReferralByStreetNurseOther
     * @param _cboxReferralByStreetNurseOther the cboxReferralByStreetNurseOther value
     */
    public void setCboxReferralByStreetNurseOther (String _cboxReferralByStreetNurseOther) {
        this._cboxReferralByStreetNurseOther = _cboxReferralByStreetNurseOther;
    }

    /**
     * Return the value associated with the column: dayOfBirth
     */
    public String getDayOfBirth () {
        return(StringUtils.trimToEmpty(_dayOfBirth));
    }

    /**
     * Set the value related to the column: dayOfBirth
     * @param _dayOfBirth the dayOfBirth value
     */
    public void setDayOfBirth (String _dayOfBirth) {
        this._dayOfBirth = _dayOfBirth;
    }

    /**
     * Return the value associated with the column: cboxDualDisorder
     */
    public String getCboxDualDisorder () {
        return _cboxDualDisorder;
    }

    /**
     * Set the value related to the column: cboxDualDisorder
     * @param _cboxDualDisorder the cboxDualDisorder value
     */
    public void setCboxDualDisorder (String _cboxDualDisorder) {
        this._cboxDualDisorder = _cboxDualDisorder;
    }

    /**
     * Return the value associated with the column: cbox2ndIncomeODSp
     */
    public String getCbox2ndIncomeODSp () {
        return _cbox2ndIncomeODSp;
    }

    /**
     * Set the value related to the column: cbox2ndIncomeODSp
     * @param _cbox2ndIncomeODSp the cbox2ndIncomeODSp value
     */
    public void setCbox2ndIncomeODSp (String _cbox2ndIncomeODSp) {
        this._cbox2ndIncomeODSp = _cbox2ndIncomeODSp;
    }

    /**
     * Return the value associated with the column: cboxPsychiatricHospitalization
     */
    public String getCboxPsychiatricHospitalization () {
        return _cboxPsychiatricHospitalization;
    }

    /**
     * Set the value related to the column: cboxPsychiatricHospitalization
     * @param _cboxPsychiatricHospitalization the cboxPsychiatricHospitalization value
     */
    public void setCboxPsychiatricHospitalization (String _cboxPsychiatricHospitalization) {
        this._cboxPsychiatricHospitalization = _cboxPsychiatricHospitalization;
    }

    /**
     * Return the value associated with the column: cbox2ndSomatoformDisorder
     */
    public String getCbox2ndSomatoformDisorder () {
        return _cbox2ndSomatoformDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndSomatoformDisorder
     * @param _cbox2ndSomatoformDisorder the cbox2ndSomatoformDisorder value
     */
    public void setCbox2ndSomatoformDisorder (String _cbox2ndSomatoformDisorder) {
        this._cbox2ndSomatoformDisorder = _cbox2ndSomatoformDisorder;
    }

    /**
     * Return the value associated with the column: cboxPreAdmission
     */
    public String getCboxPreAdmission () {
        return _cboxPreAdmission;
    }

    /**
     * Set the value related to the column: cboxPreAdmission
     * @param _cboxPreAdmission the cboxPreAdmission value
     */
    public void setCboxPreAdmission (String _cboxPreAdmission) {
        this._cboxPreAdmission = _cboxPreAdmission;
    }

    /**
     * Return the value associated with the column: cboxReferralByStreetHealthReceptionOther
     */
    public String getCboxReferralByStreetHealthReceptionOther () {
        return _cboxReferralByStreetHealthReceptionOther;
    }

    /**
     * Set the value related to the column: cboxReferralByStreetHealthReceptionOther
     * @param _cboxReferralByStreetHealthReceptionOther the cboxReferralByStreetHealthReceptionOther value
     */
    public void setCboxReferralByStreetHealthReceptionOther (String _cboxReferralByStreetHealthReceptionOther) {
        this._cboxReferralByStreetHealthReceptionOther = _cboxReferralByStreetHealthReceptionOther;
    }

    /**
     * Return the value associated with the column: cbox2ndAnxietyDisorder
     */
    public String getCbox2ndAnxietyDisorder () {
        return _cbox2ndAnxietyDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndAnxietyDisorder
     * @param _cbox2ndAnxietyDisorder the cbox2ndAnxietyDisorder value
     */
    public void setCbox2ndAnxietyDisorder (String _cbox2ndAnxietyDisorder) {
        this._cbox2ndAnxietyDisorder = _cbox2ndAnxietyDisorder;
    }

    /**
     * Return the value associated with the column: radioCurrParticipateInEduction
     */
    public String getRadioCurrParticipateInEduction () {
        if(_radioCurrParticipateInEduction!=""&&_radioCurrParticipateInEduction!=null)
            return _radioCurrParticipateInEduction;
        else return "10";
    }

    /**
     * Set the value related to the column: radioCurrParticipateInEduction
     * @param _radioCurrParticipateInEduction the radioCurrParticipateInEduction value
     */
    public void setRadioCurrParticipateInEduction (String _radioCurrParticipateInEduction) {
        this._radioCurrParticipateInEduction = _radioCurrParticipateInEduction;
    }

    /**
     * Return the value associated with the column: cbox2ndChildhoodDisorder
     */
    public String getCbox2ndChildhoodDisorder () {
        return _cbox2ndChildhoodDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndChildhoodDisorder
     * @param _cbox2ndChildhoodDisorder the cbox2ndChildhoodDisorder value
     */
    public void setCbox2ndChildhoodDisorder (String _cbox2ndChildhoodDisorder) {
        this._cbox2ndChildhoodDisorder = _cbox2ndChildhoodDisorder;
    }

    /**
     * Return the value associated with the column: cboxCurrIncomeMgmentHasTrustee
     */
    public String getCboxCurrIncomeMgmentHasTrustee () {
        return _cboxCurrIncomeMgmentHasTrustee;
    }

    /**
     * Set the value related to the column: cboxCurrIncomeMgmentHasTrustee
     * @param _cboxCurrIncomeMgmentHasTrustee the cboxCurrIncomeMgmentHasTrustee value
     */
    public void setCboxCurrIncomeMgmentHasTrustee (String _cboxCurrIncomeMgmentHasTrustee) {
        this._cboxCurrIncomeMgmentHasTrustee = _cboxCurrIncomeMgmentHasTrustee;
    }

    /**
     * Return the value associated with the column: radioCountryOfOrigin
     */
    public String getRadioCountryOfOrigin () {
        if (_radioCountryOfOrigin!=""&&_radioCountryOfOrigin!=null)
            return _radioCountryOfOrigin;
        else return "3";
    }

    /**
     * Set the value related to the column: radioCountryOfOrigin
     * @param _radioCountryOfOrigin the radioCountryOfOrigin value
     */
    public void setRadioCountryOfOrigin (String _radioCountryOfOrigin) {
        this._radioCountryOfOrigin = _radioCountryOfOrigin;
    }

    /**
     * Return the value associated with the column: cboxOtherChronicIllness
     */
    public String getCboxOtherChronicIllness () {
        return _cboxOtherChronicIllness;
    }

    /**
     * Set the value related to the column: cboxOtherChronicIllness
     * @param _cboxOtherChronicIllness the cboxOtherChronicIllness value
     */
    public void setCboxOtherChronicIllness (String _cboxOtherChronicIllness) {
        this._cboxOtherChronicIllness = _cboxOtherChronicIllness;
    }

    /**
     * Return the value associated with the column: cboxMOHLTCDisorder
     */
    public String getCboxMOHLTCDisorder () {
        return _cboxMOHLTCDisorder;
    }

    /**
     * Set the value related to the column: cboxMOHLTCDisorder
     * @param _cboxMOHLTCDisorder the cboxMOHLTCDisorder value
     */
    public void setCboxMOHLTCDisorder (String _cboxMOHLTCDisorder) {
        this._cboxMOHLTCDisorder = _cboxMOHLTCDisorder;
    }

    /**
     * Return the value associated with the column: cboxPTSd
     */
    public String getCboxPTSd () {
        return _cboxPTSd;
    }

    /**
     * Set the value related to the column: cboxPTSd
     * @param _cboxPTSd the cboxPTSd value
     */
    public void setCboxPTSd (String _cboxPTSd) {
        this._cboxPTSd = _cboxPTSd;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomeDisabilityAssistance
     */
    public String getCboxBase2ndIncomeDisabilityAssistance () {
        return _cboxBase2ndIncomeDisabilityAssistance;
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomeDisabilityAssistance
     * @param _cboxBase2ndIncomeDisabilityAssistance the cboxBase2ndIncomeDisabilityAssistance value
     */
    public void setCboxBase2ndIncomeDisabilityAssistance (String _cboxBase2ndIncomeDisabilityAssistance) {
        this._cboxBase2ndIncomeDisabilityAssistance = _cboxBase2ndIncomeDisabilityAssistance;
    }

    /**
     * Return the value associated with the column: lengthOfHospitalization
     */
    public String getLengthOfHospitalization () {
        return _lengthOfHospitalization;
    }

    /**
     * Set the value related to the column: lengthOfHospitalization
     * @param _lengthOfHospitalization the lengthOfHospitalization value
     */
    public void setLengthOfHospitalization (String _lengthOfHospitalization) {
        this._lengthOfHospitalization = _lengthOfHospitalization;
    }

    /**
     * Return the value associated with the column: radioCurrPrimaryIncomeSource
     */
    public String getRadioCurrPrimaryIncomeSource () {
        if(_radioCurrPrimaryIncomeSource!=""&&_radioCurrPrimaryIncomeSource!=null)
            return _radioCurrPrimaryIncomeSource;
        else return "10";
    }

    /**
     * Set the value related to the column: radioCurrPrimaryIncomeSource
     * @param _radioCurrPrimaryIncomeSource the radioCurrPrimaryIncomeSource value
     */
    public void setRadioCurrPrimaryIncomeSource (String _radioCurrPrimaryIncomeSource) {
        this._radioCurrPrimaryIncomeSource = _radioCurrPrimaryIncomeSource;
    }

    /**
     * Return the value associated with the column: baseWhyClientDoNotAccessSocialServices
     */
    public String getBaseWhyClientDoNotAccessSocialServices () {
        return _baseWhyClientDoNotAccessSocialServices;
    }

    /**
     * Set the value related to the column: baseWhyClientDoNotAccessSocialServices
     * @param _baseWhyClientDoNotAccessSocialServices the baseWhyClientDoNotAccessSocialServices value
     */
    public void setBaseWhyClientDoNotAccessSocialServices (String _baseWhyClientDoNotAccessSocialServices) {
        this._baseWhyClientDoNotAccessSocialServices = _baseWhyClientDoNotAccessSocialServices;
    }

    /**
     * Return the value associated with the column: cbox2ndIncomeInformalOther
     */
    public String getCbox2ndIncomeInformalOther () {
        return _cbox2ndIncomeInformalOther;
    }

    /**
     * Set the value related to the column: cbox2ndIncomeInformalOther
     * @param _cbox2ndIncomeInformalOther the cbox2ndIncomeInformalOther value
     */
    public void setCbox2ndIncomeInformalOther (String _cbox2ndIncomeInformalOther) {
        this._cbox2ndIncomeInformalOther = _cbox2ndIncomeInformalOther;
    }

    /**
     * Return the value associated with the column: cbox2ndIncomeEi
     */
    public String getCbox2ndIncomeEi () {
        return _cbox2ndIncomeEi;
    }

    /**
     * Set the value related to the column: cbox2ndIncomeEi
     * @param _cbox2ndIncomeEi the cbox2ndIncomeEi value
     */
    public void setCbox2ndIncomeEi (String _cbox2ndIncomeEi) {
        this._cbox2ndIncomeEi = _cbox2ndIncomeEi;
    }

    /**
     * Return the value associated with the column: cbox2ndGenderIdentityDisorder
     */
    public String getCbox2ndGenderIdentityDisorder () {
        return _cbox2ndGenderIdentityDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndGenderIdentityDisorder
     * @param _cbox2ndGenderIdentityDisorder the cbox2ndGenderIdentityDisorder value
     */
    public void setCbox2ndGenderIdentityDisorder (String _cbox2ndGenderIdentityDisorder) {
        this._cbox2ndGenderIdentityDisorder = _cbox2ndGenderIdentityDisorder;
    }

    /**
     * Return the value associated with the column: cboxBaseDoNotAccessHealthCare
     */
    public String getCboxBaseDoNotAccessHealthCare () {
        return _cboxBaseDoNotAccessHealthCare;
    }

    /**
     * Set the value related to the column: cboxBaseDoNotAccessHealthCare
     * @param _cboxBaseDoNotAccessHealthCare the cboxBaseDoNotAccessHealthCare value
     */
    public void setCboxBaseDoNotAccessHealthCare (String _cboxBaseDoNotAccessHealthCare) {
        this._cboxBaseDoNotAccessHealthCare = _cboxBaseDoNotAccessHealthCare;
    }

    /**
     * Return the value associated with the column: currSocialServiceClientAccesses
     */
    public String getCurrSocialServiceClientAccesses () {
        return _currSocialServiceClientAccesses;
    }

    /**
     * Set the value related to the column: currSocialServiceClientAccesses
     * @param _currSocialServiceClientAccesses the currSocialServiceClientAccesses value
     */
    public void setCurrSocialServiceClientAccesses (String _currSocialServiceClientAccesses) {
        this._currSocialServiceClientAccesses = _currSocialServiceClientAccesses;
    }

    /**
     * Return the value associated with the column: cbox2ndIncomeUnknown
     */
    public String getCbox2ndIncomeUnknown () {
        if(_cbox2ndIncomeUnknown!=""&_cbox2ndIncomeUnknown!=null)
            return _cbox2ndIncomeUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cbox2ndIncomeUnknown
     * @param _cbox2ndIncomeUnknown the cbox2ndIncomeUnknown value
     */
    public void setCbox2ndIncomeUnknown (String _cbox2ndIncomeUnknown) {
        this._cbox2ndIncomeUnknown = _cbox2ndIncomeUnknown;
    }

    /**
     * Return the value associated with the column: radioBaseHealthCareAccess
     */
    public String getRadioBaseHealthCareAccess () {
        if(_radioBaseHealthCareAccess!=""&&_radioBaseHealthCareAccess!=null)
            return _radioBaseHealthCareAccess;
        else return "3";
    }

    /**
     * Set the value related to the column: radioBaseHealthCareAccess
     * @param _radioBaseHealthCareAccess the radioBaseHealthCareAccess value
     */
    public void setRadioBaseHealthCareAccess (String _radioBaseHealthCareAccess) {
        this._radioBaseHealthCareAccess = _radioBaseHealthCareAccess;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomeODSp
     */
    public String getCboxBase2ndIncomeODSp () {
        return _cboxBase2ndIncomeODSp;
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomeODSp
     * @param _cboxBase2ndIncomeODSp the cboxBase2ndIncomeODSp value
     */
    public void setCboxBase2ndIncomeODSp (String _cboxBase2ndIncomeODSp) {
        this._cboxBase2ndIncomeODSp = _cboxBase2ndIncomeODSp;
    }

    /**
     * Return the value associated with the column: cboxCurrUseWalkinClinic
     */
    public String getCboxCurrUseWalkinClinic () {
        return _cboxCurrUseWalkinClinic;
    }

    /**
     * Set the value related to the column: cboxCurrUseWalkinClinic
     * @param _cboxCurrUseWalkinClinic the cboxCurrUseWalkinClinic value
     */
    public void setCboxCurrUseWalkinClinic (String _cboxCurrUseWalkinClinic) {
        this._cboxCurrUseWalkinClinic = _cboxCurrUseWalkinClinic;
    }

    /**
     * Return the value associated with the column: cboxReferralByPsychiatrists
     */
    public String getCboxReferralByPsychiatrists () {
        return _cboxReferralByPsychiatrists;
    }

    /**
     * Set the value related to the column: cboxReferralByPsychiatrists
     * @param _cboxReferralByPsychiatrists the cboxReferralByPsychiatrists value
     */
    public void setCboxReferralByPsychiatrists (String _cboxReferralByPsychiatrists) {
        this._cboxReferralByPsychiatrists = _cboxReferralByPsychiatrists;
    }

    /**
     * Return the value associated with the column: cboxReferralByMentalOrg
     */
    public String getCboxReferralByMentalOrg () {
        return _cboxReferralByMentalOrg;
    }

    /**
     * Set the value related to the column: cboxReferralByMentalOrg
     * @param _cboxReferralByMentalOrg the cboxReferralByMentalOrg value
     */
    public void setCboxReferralByMentalOrg (String _cboxReferralByMentalOrg) {
        this._cboxReferralByMentalOrg = _cboxReferralByMentalOrg;
    }

    /**
     * Return the value associated with the column: radioBasePrimaryResidenceType
     */
    public String getRadioBasePrimaryResidenceType () {
        if(_radioBasePrimaryResidenceType!=""&&_radioBasePrimaryResidenceType!=null)
            return _radioBasePrimaryResidenceType;
        else return "17";
    }

    /**
     * Set the value related to the column: radioBasePrimaryResidenceType
     * @param _radioBasePrimaryResidenceType the radioBasePrimaryResidenceType value
     */
    public void setRadioBasePrimaryResidenceType (String _radioBasePrimaryResidenceType) {
        this._radioBasePrimaryResidenceType = _radioBasePrimaryResidenceType;
    }

    /**
     * Return the value associated with the column: cbox2ndIncomeDisabilityAssistance
     */
    public String getCbox2ndIncomeDisabilityAssistance () {
        return _cbox2ndIncomeDisabilityAssistance;
    }

    /**
     * Set the value related to the column: cbox2ndIncomeDisabilityAssistance
     * @param _cbox2ndIncomeDisabilityAssistance the cbox2ndIncomeDisabilityAssistance value
     */
    public void setCbox2ndIncomeDisabilityAssistance (String _cbox2ndIncomeDisabilityAssistance) {
        this._cbox2ndIncomeDisabilityAssistance = _cbox2ndIncomeDisabilityAssistance;
    }

    /**
     * Return the value associated with the column: cboxAddictionIssue
     */
    public String getCboxAddictionIssue () {
        return _cboxAddictionIssue;
    }

    /**
     * Set the value related to the column: cboxAddictionIssue
     * @param _cboxAddictionIssue the cboxAddictionIssue value
     */
    public void setCboxAddictionIssue (String _cboxAddictionIssue) {
        this._cboxAddictionIssue = _cboxAddictionIssue;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomePanhandlingOther
     */
    public String getCboxBase2ndIncomePanhandlingOther () {
        return _cboxBase2ndIncomePanhandlingOther;
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomePanhandlingOther
     * @param _cboxBase2ndIncomePanhandlingOther the cboxBase2ndIncomePanhandlingOther value
     */
    public void setCboxBase2ndIncomePanhandlingOther (String _cboxBase2ndIncomePanhandlingOther) {
        this._cboxBase2ndIncomePanhandlingOther = _cboxBase2ndIncomePanhandlingOther;
    }

    /**
     * Return the value associated with the column: cboxCurrIncomeMgmentUnknown
     */
    public String getCboxCurrIncomeMgmentUnknown () {
        if(_cboxCurrIncomeMgmentUnknown!=""&&_cboxCurrIncomeMgmentUnknown!=null)
            return _cboxCurrIncomeMgmentUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxCurrIncomeMgmentUnknown
     * @param _cboxCurrIncomeMgmentUnknown the cboxCurrIncomeMgmentUnknown value
     */
    public void setCboxCurrIncomeMgmentUnknown (String _cboxCurrIncomeMgmentUnknown) {
        this._cboxCurrIncomeMgmentUnknown = _cboxCurrIncomeMgmentUnknown;
    }

    /**
     * Return the value associated with the column: cboxLegalIssue
     */
    public String getCboxLegalIssue () {
        return _cboxLegalIssue;
    }

    /**
     * Set the value related to the column: cboxLegalIssue
     * @param _cboxLegalIssue the cboxLegalIssue value
     */
    public void setCboxLegalIssue (String _cboxLegalIssue) {
        this._cboxLegalIssue = _cboxLegalIssue;
    }

    /**
     * Return the value associated with the column: radioResistTreatment
     */
    public String getRadioResistTreatment () {
        if(_radioResistTreatment!=""&&_radioResistTreatment!=null)
            return _radioResistTreatment;
        else return "3";
    }

    /**
     * Set the value related to the column: radioResistTreatment
     * @param _radioResistTreatment the radioResistTreatment value
     */
    public void setRadioResistTreatment (String _radioResistTreatment) {
        this._radioResistTreatment = _radioResistTreatment;
    }

    /**
     * Return the value associated with the column: cbox2ndSchizophrenia
     */
    public String getCbox2ndSchizophrenia () {
        return _cbox2ndSchizophrenia;
    }

    /**
     * Set the value related to the column: cbox2ndSchizophrenia
     * @param _cbox2ndSchizophrenia the cbox2ndSchizophrenia value
     */
    public void setCbox2ndSchizophrenia (String _cbox2ndSchizophrenia) {
        this._cbox2ndSchizophrenia = _cbox2ndSchizophrenia;
    }

    /**
     * Return the value associated with the column: radioBaseSocialServiceAccess
     */
    public String getRadioBaseSocialServiceAccess () {

        if(_radioBaseSocialServiceAccess!=""&&_radioBaseSocialServiceAccess!=null)
            return _radioBaseSocialServiceAccess;
        else return "3";
    }

    /**
     * Set the value related to the column: radioBaseSocialServiceAccess
     * @param _radioBaseSocialServiceAccess the radioBaseSocialServiceAccess value
     */
    public void setRadioBaseSocialServiceAccess (String _radioBaseSocialServiceAccess) {
        this._radioBaseSocialServiceAccess = _radioBaseSocialServiceAccess;
    }

    /**
     * Return the value associated with the column: radioCurrSocialServiceAccess
     */
    public String getRadioCurrSocialServiceAccess () {
        if(_radioCurrSocialServiceAccess!=""&&_radioCurrSocialServiceAccess!=null)
            return _radioCurrSocialServiceAccess;
        else return "3";
    }

    /**
     * Set the value related to the column: radioCurrSocialServiceAccess
     * @param _radioCurrSocialServiceAccess the radioCurrSocialServiceAccess value
     */
    public void setRadioCurrSocialServiceAccess (String _radioCurrSocialServiceAccess) {
        this._radioCurrSocialServiceAccess = _radioCurrSocialServiceAccess;
    }

    /**
     * Return the value associated with the column: radioBaseResidenceStatus
     */
    public String getRadioBaseResidenceStatus () {
        if(_radioBaseResidenceStatus!=""&&_radioBaseResidenceStatus!=null)
            return _radioBaseResidenceStatus;
        else return "4";
    }

    /**
     * Set the value related to the column: radioBaseResidenceStatus
     * @param _radioBaseResidenceStatus the radioBaseResidenceStatus value
     */
    public void setRadioBaseResidenceStatus (String _radioBaseResidenceStatus) {
        this._radioBaseResidenceStatus = _radioBaseResidenceStatus;
    }

    /**
     * Return the value associated with the column: cbox2ndIncomeEmployment
     */
    public String getCbox2ndIncomeEmployment () {
        return _cbox2ndIncomeEmployment;
    }

    /**
     * Set the value related to the column: cbox2ndIncomeEmployment
     * @param _cbox2ndIncomeEmployment the cbox2ndIncomeEmployment value
     */
    public void setCbox2ndIncomeEmployment (String _cbox2ndIncomeEmployment) {
        this._cbox2ndIncomeEmployment = _cbox2ndIncomeEmployment;
    }

    /**
     * Return the value associated with the column: cbox2ndAnxietyDisorderFromSubstance
     */
    public String getCbox2ndAnxietyDisorderFromSubstance () {
        return _cbox2ndAnxietyDisorderFromSubstance;
    }

    /**
     * Set the value related to the column: cbox2ndAnxietyDisorderFromSubstance
     * @param _cbox2ndAnxietyDisorderFromSubstance the cbox2ndAnxietyDisorderFromSubstance value
     */
    public void setCbox2ndAnxietyDisorderFromSubstance (String _cbox2ndAnxietyDisorderFromSubstance) {
        this._cbox2ndAnxietyDisorderFromSubstance = _cbox2ndAnxietyDisorderFromSubstance;
    }

    /**
     * Return the value associated with the column: radioCurrPrimaryResidenceType
     */
    public String getRadioCurrPrimaryResidenceType () {
        if(_radioCurrPrimaryResidenceType!=""&&_radioCurrPrimaryResidenceType!=null)
            return _radioCurrPrimaryResidenceType;
        else return "17";
    }

    /**
     * Set the value related to the column: radioCurrPrimaryResidenceType
     * @param _radioCurrPrimaryResidenceType the radioCurrPrimaryResidenceType value
     */
    public void setRadioCurrPrimaryResidenceType (String _radioCurrPrimaryResidenceType) {
        this._radioCurrPrimaryResidenceType = _radioCurrPrimaryResidenceType;
    }

    /**
     * Return the value associated with the column: radioCurrPrimaryIncomeSourceOther
     */
    public String getRadioCurrPrimaryIncomeSourceOther () {
        if(_radioCurrPrimaryIncomeSourceOther!=""&&_radioCurrPrimaryIncomeSourceOther!=null)
            return _radioCurrPrimaryIncomeSourceOther;
        else return "10";
    }

    /**
     * Set the value related to the column: radioCurrPrimaryIncomeSourceOther
     * @param _radioCurrPrimaryIncomeSourceOther the radioCurrPrimaryIncomeSourceOther value
     */
    public void setRadioCurrPrimaryIncomeSourceOther (String _radioCurrPrimaryIncomeSourceOther) {
        this._radioCurrPrimaryIncomeSourceOther = _radioCurrPrimaryIncomeSourceOther;
    }

    /**
     * Return the value associated with the column: cboxReferralByProbation
     */
    public String getCboxReferralByProbation () {
        return _cboxReferralByProbation;
    }

    /**
     * Set the value related to the column: cboxReferralByProbation
     * @param _cboxReferralByProbation the cboxReferralByProbation value
     */
    public void setCboxReferralByProbation (String _cboxReferralByProbation) {
        this._cboxReferralByProbation = _cboxReferralByProbation;
    }

    /**
     * Return the value associated with the column: radioCurrLegalStatus
     */
    public String getRadioCurrLegalStatus () {
        if(_radioCurrLegalStatus!=""&&_radioCurrLegalStatus!=null)
            return _radioCurrLegalStatus;
        else return "13";
    }

    /**
     * Set the value related to the column: radioCurrLegalStatus
     * @param _radioCurrLegalStatus the radioCurrLegalStatus value
     */
    public void setRadioCurrLegalStatus (String _radioCurrLegalStatus) {
        this._radioCurrLegalStatus = _radioCurrLegalStatus;
    }

    /**
     * Return the value associated with the column: cboxProblemsWithPolice2
     */
    public String getCboxProblemsWithPolice2 () {
        return _cboxProblemsWithPolice2;
    }

    /**
     * Set the value related to the column: cboxProblemsWithPolice2
     * @param _cboxProblemsWithPolice2 the cboxProblemsWithPolice2 value
     */
    public void setCboxProblemsWithPolice2 (String _cboxProblemsWithPolice2) {
        this._cboxProblemsWithPolice2 = _cboxProblemsWithPolice2;
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
     * Return the value associated with the column: cbox2ndIncomeSocialAssistance
     */
    public String getCbox2ndIncomeSocialAssistance () {
        return _cbox2ndIncomeSocialAssistance;
    }

    /**
     * Set the value related to the column: cbox2ndIncomeSocialAssistance
     * @param _cbox2ndIncomeSocialAssistance the cbox2ndIncomeSocialAssistance value
     */
    public void setCbox2ndIncomeSocialAssistance (String _cbox2ndIncomeSocialAssistance) {
        this._cbox2ndIncomeSocialAssistance = _cbox2ndIncomeSocialAssistance;
    }

    /**
     * Return the value associated with the column: cboxBaseUseHospitalEmergency
     */
    public String getCboxBaseUseHospitalEmergency () {
        return _cboxBaseUseHospitalEmergency;
    }

    /**
     * Set the value related to the column: cboxBaseUseHospitalEmergency
     * @param _cboxBaseUseHospitalEmergency the cboxBaseUseHospitalEmergency value
     */
    public void setCboxBaseUseHospitalEmergency (String _cboxBaseUseHospitalEmergency) {
        this._cboxBaseUseHospitalEmergency = _cboxBaseUseHospitalEmergency;
    }

    /**
     * Return the value associated with the column: cboxReferralByPublic
     */
    public String getCboxReferralByPublic () {
        return _cboxReferralByPublic;
    }

    /**
     * Set the value related to the column: cboxReferralByPublic
     * @param _cboxReferralByPublic the cboxReferralByPublic value
     */
    public void setCboxReferralByPublic (String _cboxReferralByPublic) {
        this._cboxReferralByPublic = _cboxReferralByPublic;
    }

    /**
     * Return the value associated with the column: cboxReferralByHospital
     */
    public String getCboxReferralByHospital () {
        return _cboxReferralByHospital;
    }

    /**
     * Set the value related to the column: cboxReferralByHospital
     * @param _cboxReferralByHospital the cboxReferralByHospital value
     */
    public void setCboxReferralByHospital (String _cboxReferralByHospital) {
        this._cboxReferralByHospital = _cboxReferralByHospital;
    }

    /**
     * Return the value associated with the column: cboxBaseLivingWithSpousePlus
     */
    public String getCboxBaseLivingWithSpousePlus () {
        return _cboxBaseLivingWithSpousePlus;
    }

    /**
     * Set the value related to the column: cboxBaseLivingWithSpousePlus
     * @param _cboxBaseLivingWithSpousePlus the cboxBaseLivingWithSpousePlus value
     */
    public void setCboxBaseLivingWithSpousePlus (String _cboxBaseLivingWithSpousePlus) {
        this._cboxBaseLivingWithSpousePlus = _cboxBaseLivingWithSpousePlus;
    }

    /**
     * Return the value associated with the column: cboxWithdrawalExit
     */
    public String getCboxWithdrawalExit () {
        return _cboxWithdrawalExit;
    }

    /**
     * Set the value related to the column: cboxWithdrawalExit
     * @param _cboxWithdrawalExit the cboxWithdrawalExit value
     */
    public void setCboxWithdrawalExit (String _cboxWithdrawalExit) {
        this._cboxWithdrawalExit = _cboxWithdrawalExit;
    }

    /**
     * Return the value associated with the column: cboxCurrLivingWithSelf
     */
    public String getCboxCurrLivingWithSelf () {
        return _cboxCurrLivingWithSelf;
    }

    /**
     * Set the value related to the column: cboxCurrLivingWithSelf
     * @param _cboxCurrLivingWithSelf the cboxCurrLivingWithSelf value
     */
    public void setCboxCurrLivingWithSelf (String _cboxCurrLivingWithSelf) {
        this._cboxCurrLivingWithSelf = _cboxCurrLivingWithSelf;
    }

    /**
     * Return the value associated with the column: cbox2ndCognitiveDisorder
     */
    public String getCbox2ndCognitiveDisorder () {
        return _cbox2ndCognitiveDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndCognitiveDisorder
     * @param _cbox2ndCognitiveDisorder the cbox2ndCognitiveDisorder value
     */
    public void setCbox2ndCognitiveDisorder (String _cbox2ndCognitiveDisorder) {
        this._cbox2ndCognitiveDisorder = _cbox2ndCognitiveDisorder;
    }

    /**
     * Return the value associated with the column: cboxBaseIncomeMgmentUnknown
     */
    public String getCboxBaseIncomeMgmentUnknown () {
        if(_cboxBaseIncomeMgmentUnknown!=""&&_cboxBaseIncomeMgmentUnknown!=null)
            return _cboxBaseIncomeMgmentUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxBaseIncomeMgmentUnknown
     * @param _cboxBaseIncomeMgmentUnknown the cboxBaseIncomeMgmentUnknown value
     */
    public void setCboxBaseIncomeMgmentUnknown (String _cboxBaseIncomeMgmentUnknown) {
        this._cboxBaseIncomeMgmentUnknown = _cboxBaseIncomeMgmentUnknown;
    }

    /**
     * Return the value associated with the column: cboxBaseHasHealthCard
     */
    public String getCboxBaseHasHealthCard () {
        return _cboxBaseHasHealthCard;
    }

    /**
     * Set the value related to the column: cboxBaseHasHealthCard
     * @param _cboxBaseHasHealthCard the cboxBaseHasHealthCard value
     */
    public void setCboxBaseHasHealthCard (String _cboxBaseHasHealthCard) {
        this._cboxBaseHasHealthCard = _cboxBaseHasHealthCard;
    }

    /**
     * Return the value associated with the column: radioCurrHighestEductionLevel
     */
    public String getRadioCurrHighestEductionLevel () {
        if(_radioCurrHighestEductionLevel!=""&&_radioCurrHighestEductionLevel!=null)
            return _radioCurrHighestEductionLevel;
        else return "10";
    }

    /**
     * Set the value related to the column: radioCurrHighestEductionLevel
     * @param _radioCurrHighestEductionLevel the radioCurrHighestEductionLevel value
     */
    public void setRadioCurrHighestEductionLevel (String _radioCurrHighestEductionLevel) {
        this._radioCurrHighestEductionLevel = _radioCurrHighestEductionLevel;
    }

    /**
     * Return the value associated with the column: cboxReferralByPsychiatricHospital
     */
    public String getCboxReferralByPsychiatricHospital () {
        return _cboxReferralByPsychiatricHospital;
    }

    /**
     * Set the value related to the column: cboxReferralByPsychiatricHospital
     * @param _cboxReferralByPsychiatricHospital the cboxReferralByPsychiatricHospital value
     */
    public void setCboxReferralByPsychiatricHospital (String _cboxReferralByPsychiatricHospital) {
        this._cboxReferralByPsychiatricHospital = _cboxReferralByPsychiatricHospital;
    }

    /**
     * Return the value associated with the column: cboxCurrLivingWithNonrelatives
     */
    public String getCboxCurrLivingWithNonrelatives () {
        return _cboxCurrLivingWithNonrelatives;
    }

    /**
     * Set the value related to the column: cboxCurrLivingWithNonrelatives
     * @param _cboxCurrLivingWithNonrelatives the cboxCurrLivingWithNonrelatives value
     */
    public void setCboxCurrLivingWithNonrelatives (String _cboxCurrLivingWithNonrelatives) {
        this._cboxCurrLivingWithNonrelatives = _cboxCurrLivingWithNonrelatives;
    }

    /**
     * Return the value associated with the column: cboxFamilyLawIssues1
     */
    public String getCboxFamilyLawIssues1 () {
        return _cboxFamilyLawIssues1;
    }

    /**
     * Set the value related to the column: cboxFamilyLawIssues1
     * @param _cboxFamilyLawIssues1 the cboxFamilyLawIssues1 value
     */
    public void setCboxFamilyLawIssues1 (String _cboxFamilyLawIssues1) {
        this._cboxFamilyLawIssues1 = _cboxFamilyLawIssues1;
    }

    /**
     * Return the value associated with the column: cboxProblemsWithPolice1
     */
    public String getCboxProblemsWithPolice1 () {
        return _cboxProblemsWithPolice1;
    }

    /**
     * Set the value related to the column: cboxProblemsWithPolice1
     * @param _cboxProblemsWithPolice1 the cboxProblemsWithPolice1 value
     */
    public void setCboxProblemsWithPolice1 (String _cboxProblemsWithPolice1) {
        this._cboxProblemsWithPolice1 = _cboxProblemsWithPolice1;
    }

    /**
     * Return the value associated with the column: cboxBaseHasRegularHealthProvider
     */
    public String getCboxBaseHasRegularHealthProvider () {
        return _cboxBaseHasRegularHealthProvider;
    }

    /**
     * Set the value related to the column: cboxBaseHasRegularHealthProvider
     * @param _cboxBaseHasRegularHealthProvider the cboxBaseHasRegularHealthProvider value
     */
    public void setCboxBaseHasRegularHealthProvider (String _cboxBaseHasRegularHealthProvider) {
        this._cboxBaseHasRegularHealthProvider = _cboxBaseHasRegularHealthProvider;
    }

    /**
     * Return the value associated with the column: cboxFamilyLawIssues2
     */
    public String getCboxFamilyLawIssues2 () {
        return _cboxFamilyLawIssues2;
    }

    /**
     * Set the value related to the column: cboxFamilyLawIssues2
     * @param _cboxFamilyLawIssues2 the cboxFamilyLawIssues2 value
     */
    public void setCboxFamilyLawIssues2 (String _cboxFamilyLawIssues2) {
        this._cboxFamilyLawIssues2 = _cboxFamilyLawIssues2;
    }

    /**
     * Return the value associated with the column: cboxCurrUseShelterClinic
     */
    public String getCboxCurrUseShelterClinic () {
        return _cboxCurrUseShelterClinic;
    }

    /**
     * Set the value related to the column: cboxCurrUseShelterClinic
     * @param _cboxCurrUseShelterClinic the cboxCurrUseShelterClinic value
     */
    public void setCboxCurrUseShelterClinic (String _cboxCurrUseShelterClinic) {
        this._cboxCurrUseShelterClinic = _cboxCurrUseShelterClinic;
    }

    /**
     * Return the value associated with the column: clientNum
     */
    public String getClientNum () {
        return _clientNum;
    }

    /**
     * Set the value related to the column: clientNum
     * @param _clientNum the clientNum value
     */
    public void setClientNum (String _clientNum) {
        this._clientNum = _clientNum;
    }

    /**
     * Return the value associated with the column: cbox2ndImpulsiveDisorder
     */
    public String getCbox2ndImpulsiveDisorder () {
        return _cbox2ndImpulsiveDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndImpulsiveDisorder
     * @param _cbox2ndImpulsiveDisorder the cbox2ndImpulsiveDisorder value
     */
    public void setCbox2ndImpulsiveDisorder (String _cbox2ndImpulsiveDisorder) {
        this._cbox2ndImpulsiveDisorder = _cbox2ndImpulsiveDisorder;
    }

    /**
     * Return the value associated with the column: radioIsAboriginal
     */
    public String getRadioIsAboriginal () {
        if(_radioIsAboriginal!=""&&_radioIsAboriginal!=null)
            return _radioIsAboriginal;
        else return "3";
    }

    /**
     * Set the value related to the column: radioIsAboriginal
     * @param _radioIsAboriginal the radioIsAboriginal value
     */
    public void setRadioIsAboriginal (String _radioIsAboriginal) {
        this._radioIsAboriginal = _radioIsAboriginal;
    }

    /**
     * Return the value associated with the column: baseSocialServiceClientAccesses
     */
    public String getBaseSocialServiceClientAccesses () {
        return _baseSocialServiceClientAccesses;
    }

    /**
     * Set the value related to the column: baseSocialServiceClientAccesses
     * @param _baseSocialServiceClientAccesses the baseSocialServiceClientAccesses value
     */
    public void setBaseSocialServiceClientAccesses (String _baseSocialServiceClientAccesses) {
        this._baseSocialServiceClientAccesses = _baseSocialServiceClientAccesses;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomeOther
     */
    public String getCboxBase2ndIncomeOther () {
        return _cboxBase2ndIncomeOther;
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomeOther
     * @param _cboxBase2ndIncomeOther the cboxBase2ndIncomeOther value
     */
    public void setCboxBase2ndIncomeOther (String _cboxBase2ndIncomeOther) {
        this._cboxBase2ndIncomeOther = _cboxBase2ndIncomeOther;
    }

    /**
     * Return the value associated with the column: cboxBaseIncomeMgmentNeedsTrusteeButDoNotWant
     */
    public String getCboxBaseIncomeMgmentNeedsTrusteeButDoNotWant () {
        return _cboxBaseIncomeMgmentNeedsTrusteeButDoNotWant;
    }

    /**
     * Set the value related to the column: cboxBaseIncomeMgmentNeedsTrusteeButDoNotWant
     * @param _cboxBaseIncomeMgmentNeedsTrusteeButDoNotWant the cboxBaseIncomeMgmentNeedsTrusteeButDoNotWant value
     */
    public void setCboxBaseIncomeMgmentNeedsTrusteeButDoNotWant (String _cboxBaseIncomeMgmentNeedsTrusteeButDoNotWant) {
        this._cboxBaseIncomeMgmentNeedsTrusteeButDoNotWant = _cboxBaseIncomeMgmentNeedsTrusteeButDoNotWant;
    }

    /**
     * Return the value associated with the column: cbox2ndSleepDisorder
     */
    public String getCbox2ndSleepDisorder () {
        return _cbox2ndSleepDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndSleepDisorder
     * @param _cbox2ndSleepDisorder the cbox2ndSleepDisorder value
     */
    public void setCbox2ndSleepDisorder (String _cbox2ndSleepDisorder) {
        this._cbox2ndSleepDisorder = _cbox2ndSleepDisorder;
    }

    /**
     * Return the value associated with the column: cboxBaseHasNonStatus
     */
    public String getCboxBaseHasNonStatus () {
        return _cboxBaseHasNonStatus;
    }

    /**
     * Set the value related to the column: cboxBaseHasNonStatus
     * @param _cboxBaseHasNonStatus the cboxBaseHasNonStatus value
     */
    public void setCboxBaseHasNonStatus (String _cboxBaseHasNonStatus) {
        this._cboxBaseHasNonStatus = _cboxBaseHasNonStatus;
    }

    /**
     * Return the value associated with the column: cboxHealthCareIssueOther
     */
    public String getCboxHealthCareIssueOther () {
        return _cboxHealthCareIssueOther;
    }

    /**
     * Set the value related to the column: cboxHealthCareIssueOther
     * @param _cboxHealthCareIssueOther the cboxHealthCareIssueOther value
     */
    public void setCboxHealthCareIssueOther (String _cboxHealthCareIssueOther) {
        this._cboxHealthCareIssueOther = _cboxHealthCareIssueOther;
    }

    /**
     * Return the value associated with the column: radioCurrHealthCareAccess
     */
    public String getRadioCurrHealthCareAccess () {

        if(_radioCurrHealthCareAccess!=""&&_radioCurrHealthCareAccess!=null)
            return _radioCurrHealthCareAccess;
        else return "3";
    }

    /**
     * Set the value related to the column: radioCurrHealthCareAccess
     * @param _radioCurrHealthCareAccess the radioCurrHealthCareAccess value
     */
    public void setRadioCurrHealthCareAccess (String _radioCurrHealthCareAccess) {
        this._radioCurrHealthCareAccess = _radioCurrHealthCareAccess;
    }

    /**
     * Return the value associated with the column: cboxBaseHasCertificate
     */
    public String getCboxBaseHasCertificate () {
        return _cboxBaseHasCertificate;
    }

    /**
     * Set the value related to the column: cboxBaseHasCertificate
     * @param _cboxBaseHasCertificate the cboxBaseHasCertificate value
     */
    public void setCboxBaseHasCertificate (String _cboxBaseHasCertificate) {
        this._cboxBaseHasCertificate = _cboxBaseHasCertificate;
    }

    /**
     * Return the value associated with the column: cboxBaseLivingWithSelf
     */
    public String getCboxBaseLivingWithSelf () {
        return _cboxBaseLivingWithSelf;
    }

    /**
     * Set the value related to the column: cboxBaseLivingWithSelf
     * @param _cboxBaseLivingWithSelf the cboxBaseLivingWithSelf value
     */
    public void setCboxBaseLivingWithSelf (String _cboxBaseLivingWithSelf) {
        this._cboxBaseLivingWithSelf = _cboxBaseLivingWithSelf;
    }

    /**
     * Return the value associated with the column: cboxCurrIncomeMgmentNeedsTrustee
     */
    public String getCboxCurrIncomeMgmentNeedsTrustee () {
        return _cboxCurrIncomeMgmentNeedsTrustee;
    }

    /**
     * Set the value related to the column: cboxCurrIncomeMgmentNeedsTrustee
     * @param _cboxCurrIncomeMgmentNeedsTrustee the cboxCurrIncomeMgmentNeedsTrustee value
     */
    public void setCboxCurrIncomeMgmentNeedsTrustee (String _cboxCurrIncomeMgmentNeedsTrustee) {
        this._cboxCurrIncomeMgmentNeedsTrustee = _cboxCurrIncomeMgmentNeedsTrustee;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomeUnknown
     */
    public String getCboxBase2ndIncomeUnknown () {
        if(_cboxBase2ndIncomeUnknown!=""&&_cboxBase2ndIncomeUnknown!=null)
            return _cboxBase2ndIncomeUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomeUnknown
     * @param _cboxBase2ndIncomeUnknown the cboxBase2ndIncomeUnknown value
     */
    public void setCboxBase2ndIncomeUnknown (String _cboxBase2ndIncomeUnknown) {
        this._cboxBase2ndIncomeUnknown = _cboxBase2ndIncomeUnknown;
    }

    /**
     * Return the value associated with the column: radioLanguageEnglish
     */
    public String getRadioLanguageEnglish () {
        if(_radioLanguageEnglish!=""&&_radioLanguageEnglish!=null)
            return _radioLanguageEnglish;
        else return "3";
    }

    /**
     * Set the value related to the column: radioLanguageEnglish
     * @param _radioLanguageEnglish the radioLanguageEnglish value
     */
    public void setRadioLanguageEnglish (String _radioLanguageEnglish) {
        this._radioLanguageEnglish = _radioLanguageEnglish;
    }

    /**
     * Return the value associated with the column: yearArrivedInCanada
     */
    public String getYearArrivedInCanada () {
        return _yearArrivedInCanada;
    }

    /**
     * Set the value related to the column: yearArrivedInCanada
     * @param _yearArrivedInCanada the yearArrivedInCanada value
     */
    public void setYearArrivedInCanada (String _yearArrivedInCanada) {
        this._yearArrivedInCanada = _yearArrivedInCanada;
    }

    /**
     * Return the value associated with the column: cboxDateOfBirthUnknown
     */
    public String getCboxDateOfBirthUnknown () {

        if(_cboxDateOfBirthUnknown!=""&&_cboxDateOfBirthUnknown!=null)
            return _cboxDateOfBirthUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxDateOfBirthUnknown
     * @param _cboxDateOfBirthUnknown the cboxDateOfBirthUnknown value
     */
    public void setCboxDateOfBirthUnknown (String _cboxDateOfBirthUnknown) {
        this._cboxDateOfBirthUnknown = _cboxDateOfBirthUnknown;
    }

    /**
     * Return the value associated with the column: cboxOtherIssue
     */
    public String getCboxOtherIssue () {
        return _cboxOtherIssue;
    }

    /**
     * Set the value related to the column: cboxOtherIssue
     * @param _cboxOtherIssue the cboxOtherIssue value
     */
    public void setCboxOtherIssue (String _cboxOtherIssue) {
        this._cboxOtherIssue = _cboxOtherIssue;
    }

    /**
     * Return the value associated with the column: cboxCurrHasHealthCard
     */
    public String getCboxCurrHasHealthCard () {
        return _cboxCurrHasHealthCard;
    }

    /**
     * Set the value related to the column: cboxCurrHasHealthCard
     * @param _cboxCurrHasHealthCard the cboxCurrHasHealthCard value
     */
    public void setCboxCurrHasHealthCard (String _cboxCurrHasHealthCard) {
        this._cboxCurrHasHealthCard = _cboxCurrHasHealthCard;
    }

    /**
     * Return the value associated with the column: cboxCurrHasSomeone
     */
    public String getCboxCurrHasSomeone () {
        return _cboxCurrHasSomeone;
    }

    /**
     * Set the value related to the column: cboxCurrHasSomeone
     * @param _cboxCurrHasSomeone the cboxCurrHasSomeone value
     */
    public void setCboxCurrHasSomeone (String _cboxCurrHasSomeone) {
        this._cboxCurrHasSomeone = _cboxCurrHasSomeone;
    }

    /**
     * Return the value associated with the column: cboxCurrHasRegularHealthProvider
     */
    public String getCboxCurrHasRegularHealthProvider () {
        return _cboxCurrHasRegularHealthProvider;
    }

    /**
     * Set the value related to the column: cboxCurrHasRegularHealthProvider
     * @param _cboxCurrHasRegularHealthProvider the cboxCurrHasRegularHealthProvider value
     */
    public void setCboxCurrHasRegularHealthProvider (String _cboxCurrHasRegularHealthProvider) {
        this._cboxCurrHasRegularHealthProvider = _cboxCurrHasRegularHealthProvider;
    }

    /**
     * Return the value associated with the column: cbox2ndDevelopmentalDisorder
     */
    public String getCbox2ndDevelopmentalDisorder () {
        return _cbox2ndDevelopmentalDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndDevelopmentalDisorder
     * @param _cbox2ndDevelopmentalDisorder the cbox2ndDevelopmentalDisorder value
     */
    public void setCbox2ndDevelopmentalDisorder (String _cbox2ndDevelopmentalDisorder) {
        this._cbox2ndDevelopmentalDisorder = _cbox2ndDevelopmentalDisorder;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomeInformalOther
     */
    public String getCboxBase2ndIncomeInformalOther () {
        return _cboxBase2ndIncomeInformalOther;
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomeInformalOther
     * @param _cboxBase2ndIncomeInformalOther the cboxBase2ndIncomeInformalOther value
     */
    public void setCboxBase2ndIncomeInformalOther (String _cboxBase2ndIncomeInformalOther) {
        this._cboxBase2ndIncomeInformalOther = _cboxBase2ndIncomeInformalOther;
    }

    /**
     * Return the value associated with the column: cboxOCd
     */
    public String getCboxOCd () {
        return _cboxOCd;
    }

    /**
     * Set the value related to the column: cboxOCd
     * @param _cboxOCd the cboxOCd value
     */
    public void setCboxOCd (String _cboxOCd) {
        this._cboxOCd = _cboxOCd;
    }

    /**
     * Return the value associated with the column: cboxCurrUseHospitalEmergency
     */
    public String getCboxCurrUseHospitalEmergency () {
        return _cboxCurrUseHospitalEmergency;
    }

    /**
     * Set the value related to the column: cboxCurrUseHospitalEmergency
     * @param _cboxCurrUseHospitalEmergency the cboxCurrUseHospitalEmergency value
     */
    public void setCboxCurrUseHospitalEmergency (String _cboxCurrUseHospitalEmergency) {
        this._cboxCurrUseHospitalEmergency = _cboxCurrUseHospitalEmergency;
    }

    /**
     * Return the value associated with the column: cboxBaseLivingWithUnknown
     */
    public String getCboxBaseLivingWithUnknown () {
        if(_cboxBaseLivingWithUnknown!=""&&_cboxBaseLivingWithUnknown!=null)
            return _cboxBaseLivingWithUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxBaseLivingWithUnknown
     * @param _cboxBaseLivingWithUnknown the cboxBaseLivingWithUnknown value
     */
    public void setCboxBaseLivingWithUnknown (String _cboxBaseLivingWithUnknown) {
        this._cboxBaseLivingWithUnknown = _cboxBaseLivingWithUnknown;
    }

    /**
     * Return the value associated with the column: cbox2ndFactitiousDisorder
     */
    public String getCbox2ndFactitiousDisorder () {
        return _cbox2ndFactitiousDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndFactitiousDisorder
     * @param _cbox2ndFactitiousDisorder the cbox2ndFactitiousDisorder value
     */
    public void setCbox2ndFactitiousDisorder (String _cbox2ndFactitiousDisorder) {
        this._cbox2ndFactitiousDisorder = _cbox2ndFactitiousDisorder;
    }

    /**
     * Return the value associated with the column: cboxBaseHasSomeone
     */
    public String getCboxBaseHasSomeone () {
        return _cboxBaseHasSomeone;
    }

    /**
     * Set the value related to the column: cboxBaseHasSomeone
     * @param _cboxBaseHasSomeone the cboxBaseHasSomeone value
     */
    public void setCboxBaseHasSomeone (String _cboxBaseHasSomeone) {
        this._cboxBaseHasSomeone = _cboxBaseHasSomeone;
    }

    /**
     * Return the value associated with the column: cboxRelationalIssue
     */
    public String getCboxRelationalIssue () {
        return _cboxRelationalIssue;
    }

    /**
     * Set the value related to the column: cboxRelationalIssue
     * @param _cboxRelationalIssue the cboxRelationalIssue value
     */
    public void setCboxRelationalIssue (String _cboxRelationalIssue) {
        this._cboxRelationalIssue = _cboxRelationalIssue;
    }

    /**
     * Return the value associated with the column: cboxBaseHasNativeCard
     */
    public String getCboxBaseHasNativeCard () {
        return _cboxBaseHasNativeCard;
    }

    /**
     * Set the value related to the column: cboxBaseHasNativeCard
     * @param _cboxBaseHasNativeCard the cboxBaseHasNativeCard value
     */
    public void setCboxBaseHasNativeCard (String _cboxBaseHasNativeCard) {
        this._cboxBaseHasNativeCard = _cboxBaseHasNativeCard;
    }

    /**
     * Return the value associated with the column: radioBaseLegalStatus
     */
    public String getRadioBaseLegalStatus () {
        if(_radioBaseLegalStatus!=""&&_radioBaseLegalStatus!=null)
            return _radioBaseLegalStatus;
        else return "13";
    }

    /**
     * Set the value related to the column: radioBaseLegalStatus
     * @param _radioBaseLegalStatus the radioBaseLegalStatus value
     */
    public void setRadioBaseLegalStatus (String _radioBaseLegalStatus) {
        this._radioBaseLegalStatus = _radioBaseLegalStatus;
    }

    /**
     * Return the value associated with the column: cbox2ndIncomeFamily
     */
    public String getCbox2ndIncomeFamily () {
        return _cbox2ndIncomeFamily;
    }

    /**
     * Set the value related to the column: cbox2ndIncomeFamily
     * @param _cbox2ndIncomeFamily the cbox2ndIncomeFamily value
     */
    public void setCbox2ndIncomeFamily (String _cbox2ndIncomeFamily) {
        this._cbox2ndIncomeFamily = _cbox2ndIncomeFamily;
    }

    /**
     * Return the value associated with the column: cboxCurrHasRelatives
     */
    public String getCboxCurrHasRelatives () {
        return _cboxCurrHasRelatives;
    }

    /**
     * Set the value related to the column: cboxCurrHasRelatives
     * @param _cboxCurrHasRelatives the cboxCurrHasRelatives value
     */
    public void setCboxCurrHasRelatives (String _cboxCurrHasRelatives) {
        this._cboxCurrHasRelatives = _cboxCurrHasRelatives;
    }

    /**
     * Return the value associated with the column: cboxCurrHasCertificate
     */
    public String getCboxCurrHasCertificate () {
        return _cboxCurrHasCertificate;
    }

    /**
     * Set the value related to the column: cboxCurrHasCertificate
     * @param _cboxCurrHasCertificate the cboxCurrHasCertificate value
     */
    public void setCboxCurrHasCertificate (String _cboxCurrHasCertificate) {
        this._cboxCurrHasCertificate = _cboxCurrHasCertificate;
    }

    /**
     * Return the value associated with the column: monthOfBirth
     */
    public String getMonthOfBirth () {
    	return(StringUtils.trimToEmpty(_monthOfBirth));
    }

    /**
     * Set the value related to the column: monthOfBirth
     * @param _monthOfBirth the monthOfBirth value
     */
    public void setMonthOfBirth (String _monthOfBirth) {
        this._monthOfBirth = _monthOfBirth;
    }

    /**
     * Return the value associated with the column: cboxBaseHasSIn
     */
    public String getCboxBaseHasSIn () {
        return _cboxBaseHasSIn;
    }

    /**
     * Set the value related to the column: cboxBaseHasSIn
     * @param _cboxBaseHasSIn the cboxBaseHasSIn value
     */
    public void setCboxBaseHasSIn (String _cboxBaseHasSIn) {
        this._cboxBaseHasSIn = _cboxBaseHasSIn;
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
     * Return the value associated with the column: cbox2ndAnxietyDisorderOCd
     */
    public String getCbox2ndAnxietyDisorderOCd () {
        return _cbox2ndAnxietyDisorderOCd;
    }

    /**
     * Set the value related to the column: cbox2ndAnxietyDisorderOCd
     * @param _cbox2ndAnxietyDisorderOCd the cbox2ndAnxietyDisorderOCd value
     */
    public void setCbox2ndAnxietyDisorderOCd (String _cbox2ndAnxietyDisorderOCd) {
        this._cbox2ndAnxietyDisorderOCd = _cbox2ndAnxietyDisorderOCd;
    }

    /**
     * Return the value associated with the column: staffName
     */
    public String getStaffName () {
        return _staffName;
    }

    /**
     * Set the value related to the column: staffName
     * @param _staffName the staffName value
     */
    public void setStaffName (String _staffName) {
        this._staffName = _staffName;
    }

    /**
     * Return the value associated with the column: cboxReferralByStreetIDWorkerOther
     */
    public String getCboxReferralByStreetIDWorkerOther () {
        return _cboxReferralByStreetIDWorkerOther;
    }

    /**
     * Set the value related to the column: cboxReferralByStreetIDWorkerOther
     * @param _cboxReferralByStreetIDWorkerOther the cboxReferralByStreetIDWorkerOther value
     */
    public void setCboxReferralByStreetIDWorkerOther (String _cboxReferralByStreetIDWorkerOther) {
        this._cboxReferralByStreetIDWorkerOther = _cboxReferralByStreetIDWorkerOther;
    }

    /**
     * Return the value associated with the column: cbox2ndSubstanceDisorder
     */
    public String getCbox2ndSubstanceDisorder () {
        return _cbox2ndSubstanceDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndSubstanceDisorder
     * @param _cbox2ndSubstanceDisorder the cbox2ndSubstanceDisorder value
     */
    public void setCbox2ndSubstanceDisorder (String _cbox2ndSubstanceDisorder) {
        this._cbox2ndSubstanceDisorder = _cbox2ndSubstanceDisorder;
    }

    /**
     * Return the value associated with the column: radioCurrNeedSocialServices
     */
    public String getRadioCurrNeedSocialServices () {
        if(_radioCurrNeedSocialServices!=""&&_radioCurrNeedSocialServices!=null)
            return _radioCurrNeedSocialServices;
        else return "3";
    }

    /**
     * Set the value related to the column: radioCurrNeedSocialServices
     * @param _radioCurrNeedSocialServices the radioCurrNeedSocialServices value
     */
    public void setRadioCurrNeedSocialServices (String _radioCurrNeedSocialServices) {
        this._radioCurrNeedSocialServices = _radioCurrNeedSocialServices;
    }

    /**
     * Return the value associated with the column: radioBaseHighestEductionLevel
     */
    public String getRadioBaseHighestEductionLevel () {
        if(_radioBaseHighestEductionLevel!=""&&_radioBaseHighestEductionLevel!=null)
            return _radioBaseHighestEductionLevel;
        else return "10";
    }

    /**
     * Set the value related to the column: radioBaseHighestEductionLevel
     * @param _radioBaseHighestEductionLevel the radioBaseHighestEductionLevel value
     */
    public void setRadioBaseHighestEductionLevel (String _radioBaseHighestEductionLevel) {
        this._radioBaseHighestEductionLevel = _radioBaseHighestEductionLevel;
    }

    /**
     * Return the value associated with the column: cboxBaseHasSupportUnknown
     */
    public String getCboxBaseHasSupportUnknown () {
        if(_cboxBaseHasSupportUnknown!=""&&_cboxBaseHasSupportUnknown!=null)
            return _cboxBaseHasSupportUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxBaseHasSupportUnknown
     * @param _cboxBaseHasSupportUnknown the cboxBaseHasSupportUnknown value
     */
    public void setCboxBaseHasSupportUnknown (String _cboxBaseHasSupportUnknown) {
        this._cboxBaseHasSupportUnknown = _cboxBaseHasSupportUnknown;
    }

    /**
     * Return the value associated with the column: radioCurrResidenceStatus
     */
    public String getRadioCurrResidenceStatus () {

        if(_radioCurrResidenceStatus!=""&&_radioCurrResidenceStatus!=null)
            return _radioCurrResidenceStatus;
        else return "4";
    }

    /**
     * Set the value related to the column: radioCurrResidenceStatus
     * @param _radioCurrResidenceStatus the radioCurrResidenceStatus value
     */
    public void setRadioCurrResidenceStatus (String _radioCurrResidenceStatus) {
        this._radioCurrResidenceStatus = _radioCurrResidenceStatus;
    }

    /**
     * Return the value associated with the column: cboxBaseIncomeMgmentDoNotNeedTrustee
     */
    public String getCboxBaseIncomeMgmentDoNotNeedTrustee () {
        return _cboxBaseIncomeMgmentDoNotNeedTrustee;
    }

    /**
     * Set the value related to the column: cboxBaseIncomeMgmentDoNotNeedTrustee
     * @param _cboxBaseIncomeMgmentDoNotNeedTrustee the cboxBaseIncomeMgmentDoNotNeedTrustee value
     */
    public void setCboxBaseIncomeMgmentDoNotNeedTrustee (String _cboxBaseIncomeMgmentDoNotNeedTrustee) {
        this._cboxBaseIncomeMgmentDoNotNeedTrustee = _cboxBaseIncomeMgmentDoNotNeedTrustee;
    }

    /**
     * Return the value associated with the column: countryOfOrigin
     */
    public String getCountryOfOrigin () {
        return _countryOfOrigin;
    }

    /**
     * Set the value related to the column: countryOfOrigin
     * @param _countryOfOrigin the countryOfOrigin value
     */
    public void setCountryOfOrigin (String _countryOfOrigin) {
        this._countryOfOrigin = _countryOfOrigin;
    }

    /**
     * Return the value associated with the column: cboxCurrHasFriends
     */
    public String getCboxCurrHasFriends () {
        return _cboxCurrHasFriends;
    }

    /**
     * Set the value related to the column: cboxCurrHasFriends
     * @param _cboxCurrHasFriends the cboxCurrHasFriends value
     */
    public void setCboxCurrHasFriends (String _cboxCurrHasFriends) {
        this._cboxCurrHasFriends = _cboxCurrHasFriends;
    }

    /**
     * Return the value associated with the column: radioBasePrimaryIncomeSource
     */
    public String getRadioBasePrimaryIncomeSource () {

        if(_radioBasePrimaryIncomeSource!=""&&_radioBasePrimaryIncomeSource!=null)
            return _radioBasePrimaryIncomeSource;
        else return "10";
    }

    /**
     * Set the value related to the column: radioBasePrimaryIncomeSource
     * @param _radioBasePrimaryIncomeSource the radioBasePrimaryIncomeSource value
     */
    public void setRadioBasePrimaryIncomeSource (String _radioBasePrimaryIncomeSource) {
        this._radioBasePrimaryIncomeSource = _radioBasePrimaryIncomeSource;
    }

    /**
     * Return the value associated with the column: cbox2ndAnxietyDisorderOther
     */
    public String getCbox2ndAnxietyDisorderOther () {
        return _cbox2ndAnxietyDisorderOther;
    }

    /**
     * Set the value related to the column: cbox2ndAnxietyDisorderOther
     * @param _cbox2ndAnxietyDisorderOther the cbox2ndAnxietyDisorderOther value
     */
    public void setCbox2ndAnxietyDisorderOther (String _cbox2ndAnxietyDisorderOther) {
        this._cbox2ndAnxietyDisorderOther = _cbox2ndAnxietyDisorderOther;
    }

    /**
     * Return the value associated with the column: cboxEducationalIssue
     */
    public String getCboxEducationalIssue () {
        return _cboxEducationalIssue;
    }

    /**
     * Set the value related to the column: cboxEducationalIssue
     * @param _cboxEducationalIssue the cboxEducationalIssue value
     */
    public void setCboxEducationalIssue (String _cboxEducationalIssue) {
        this._cboxEducationalIssue = _cboxEducationalIssue;
    }

    /**
     * Return the value associated with the column: radioBasePrimaryIncomeSourceOther
     */
    public String getRadioBasePrimaryIncomeSourceOther () {
        return _radioBasePrimaryIncomeSourceOther;
    }

    /**
     * Set the value related to the column: radioBasePrimaryIncomeSourceOther
     * @param _radioBasePrimaryIncomeSourceOther the radioBasePrimaryIncomeSourceOther value
     */
    public void setRadioBasePrimaryIncomeSourceOther (String _radioBasePrimaryIncomeSourceOther) {
        this._radioBasePrimaryIncomeSourceOther = _radioBasePrimaryIncomeSourceOther;
    }

    /**
     * Return the value associated with the column: radioBaseParticipateInEduction
     */
    public String getRadioBaseParticipateInEduction () {
        if(_radioBaseParticipateInEduction!=""&&_radioBaseParticipateInEduction!=null)
            return _radioBaseParticipateInEduction;
        else return "10";
    }

    /**
     * Set the value related to the column: radioBaseParticipateInEduction
     * @param _radioBaseParticipateInEduction the radioBaseParticipateInEduction value
     */
    public void setRadioBaseParticipateInEduction (String _radioBaseParticipateInEduction) {
        this._radioBaseParticipateInEduction = _radioBaseParticipateInEduction;
    }

    /**
     * Return the value associated with the column: cboxCurrLivingWithRelatives
     */
    public String getCboxCurrLivingWithRelatives () {
        return _cboxCurrLivingWithRelatives;
    }

    /**
     * Set the value related to the column: cboxCurrLivingWithRelatives
     * @param _cboxCurrLivingWithRelatives the cboxCurrLivingWithRelatives value
     */
    public void setCboxCurrLivingWithRelatives (String _cboxCurrLivingWithRelatives) {
        this._cboxCurrLivingWithRelatives = _cboxCurrLivingWithRelatives;
    }

    /**
     * Return the value associated with the column: baseDescriptionOfHousing
     */
    public String getBaseDescriptionOfHousing () {
        return _baseDescriptionOfHousing;
    }

    /**
     * Set the value related to the column: baseDescriptionOfHousing
     * @param _baseDescriptionOfHousing the baseDescriptionOfHousing value
     */
    public void setBaseDescriptionOfHousing (String _baseDescriptionOfHousing) {
        this._baseDescriptionOfHousing = _baseDescriptionOfHousing;
    }

    /**
     * Return the value associated with the column: cboxReferralByCourt
     */
    public String getCboxReferralByCourt () {
        return _cboxReferralByCourt;
    }

    /**
     * Set the value related to the column: cboxReferralByCourt
     * @param _cboxReferralByCourt the cboxReferralByCourt value
     */
    public void setCboxReferralByCourt (String _cboxReferralByCourt) {
        this._cboxReferralByCourt = _cboxReferralByCourt;
    }

    /**
     * Return the value associated with the column: cbox2ndIncomePanhandlingOther
     */
    public String getCbox2ndIncomePanhandlingOther () {
        return _cbox2ndIncomePanhandlingOther;
    }

    /**
     * Set the value related to the column: cbox2ndIncomePanhandlingOther
     * @param _cbox2ndIncomePanhandlingOther the cbox2ndIncomePanhandlingOther value
     */
    public void setCbox2ndIncomePanhandlingOther (String _cbox2ndIncomePanhandlingOther) {
        this._cbox2ndIncomePanhandlingOther = _cbox2ndIncomePanhandlingOther;
    }

    /**
     * Return the value associated with the column: cboxReferralByOtherAgency
     */
    public String getCboxReferralByOtherAgency () {
        return _cboxReferralByOtherAgency;
    }

    /**
     * Set the value related to the column: cboxReferralByOtherAgency
     * @param _cboxReferralByOtherAgency the cboxReferralByOtherAgency value
     */
    public void setCboxReferralByOtherAgency (String _cboxReferralByOtherAgency) {
        this._cboxReferralByOtherAgency = _cboxReferralByOtherAgency;
    }

    /**
     * Return the value associated with the column: cboxCaseFile
     */
    public String getCboxCaseFile () {
        return _cboxCaseFile;
    }

    /**
     * Set the value related to the column: cboxCaseFile
     * @param _cboxCaseFile the cboxCaseFile value
     */
    public void setCboxCaseFile (String _cboxCaseFile) {
        this._cboxCaseFile = _cboxCaseFile;
    }

    /**
     * Return the value associated with the column: cboxCurrLivingWithChildren
     */
    public String getCboxCurrLivingWithChildren () {
        return _cboxCurrLivingWithChildren;
    }

    /**
     * Set the value related to the column: cboxCurrLivingWithChildren
     * @param _cboxCurrLivingWithChildren the cboxCurrLivingWithChildren value
     */
    public void setCboxCurrLivingWithChildren (String _cboxCurrLivingWithChildren) {
        this._cboxCurrLivingWithChildren = _cboxCurrLivingWithChildren;
    }

    /**
     * Return the value associated with the column: cboxReferralByMentalHealthWorker
     */
    public String getCboxReferralByMentalHealthWorker () {
        return _cboxReferralByMentalHealthWorker;
    }

    /**
     * Set the value related to the column: cboxReferralByMentalHealthWorker
     * @param _cboxReferralByMentalHealthWorker the cboxReferralByMentalHealthWorker value
     */
    public void setCboxReferralByMentalHealthWorker (String _cboxReferralByMentalHealthWorker) {
        this._cboxReferralByMentalHealthWorker = _cboxReferralByMentalHealthWorker;
    }

    /**
     * Return the value associated with the column: cboxCurrIncomeMgmentNeedsTrusteeButDoNotWant
     */
    public String getCboxCurrIncomeMgmentNeedsTrusteeButDoNotWant () {
        return _cboxCurrIncomeMgmentNeedsTrusteeButDoNotWant;
    }

    /**
     * Set the value related to the column: cboxCurrIncomeMgmentNeedsTrusteeButDoNotWant
     * @param _cboxCurrIncomeMgmentNeedsTrusteeButDoNotWant the cboxCurrIncomeMgmentNeedsTrusteeButDoNotWant value
     */
    public void setCboxCurrIncomeMgmentNeedsTrusteeButDoNotWant (String _cboxCurrIncomeMgmentNeedsTrusteeButDoNotWant) {
        this._cboxCurrIncomeMgmentNeedsTrusteeButDoNotWant = _cboxCurrIncomeMgmentNeedsTrusteeButDoNotWant;
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
     * Return the value associated with the column: cboxBankingIssueOther
     */
    public String getCboxBankingIssueOther () {
        return _cboxBankingIssueOther;
    }

    /**
     * Set the value related to the column: cboxBankingIssueOther
     * @param _cboxBankingIssueOther the cboxBankingIssueOther value
     */
    public void setCboxBankingIssueOther (String _cboxBankingIssueOther) {
        this._cboxBankingIssueOther = _cboxBankingIssueOther;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomeEmployment
     */
    public String getCboxBase2ndIncomeEmployment () {
        return _cboxBase2ndIncomeEmployment;
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomeEmployment
     * @param _cboxBase2ndIncomeEmployment the cboxBase2ndIncomeEmployment value
     */
    public void setCboxBase2ndIncomeEmployment (String _cboxBase2ndIncomeEmployment) {
        this._cboxBase2ndIncomeEmployment = _cboxBase2ndIncomeEmployment;
    }

    /**
     * Return the value associated with the column: cboxNAExit
     */
    public String getCboxNAExit () {
        if(_cboxNAExit!=""&&_cboxNAExit!=null)
            return _cboxNAExit;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxNAExit
     * @param _cboxNAExit the cboxNAExit value
     */
    public void setCboxNAExit (String _cboxNAExit) {
        this._cboxNAExit = _cboxNAExit;
    }

    /**
     * Return the value associated with the column: cbox2ndIncomeNone
     */
    public String getCbox2ndIncomeNone () {
        return _cbox2ndIncomeNone;
    }

    /**
     * Set the value related to the column: cbox2ndIncomeNone
     * @param _cbox2ndIncomeNone the cbox2ndIncomeNone value
     */
    public void setCbox2ndIncomeNone (String _cbox2ndIncomeNone) {
        this._cbox2ndIncomeNone = _cbox2ndIncomeNone;
    }

    /**
     * Return the value associated with the column: cboxNa
     */
    public String getCboxNa () {
        if(_cboxNa!=""&&_cboxNa!=null)
            return _cboxNa;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxNa
     * @param _cboxNa the cboxNa value
     */
    public void setCboxNa (String _cboxNa) {
        this._cboxNa = _cboxNa;
    }

    /**
     * Return the value associated with the column: cboxBaseHasRelatives
     */
    public String getCboxBaseHasRelatives () {
        return _cboxBaseHasRelatives;
    }

    /**
     * Set the value related to the column: cboxBaseHasRelatives
     * @param _cboxBaseHasRelatives the cboxBaseHasRelatives value
     */
    public void setCboxBaseHasRelatives (String _cboxBaseHasRelatives) {
        this._cboxBaseHasRelatives = _cboxBaseHasRelatives;
    }

    /**
     * Return the value associated with the column: cboxCurrHasNonStatus
     */
    public String getCboxCurrHasNonStatus () {
        return _cboxCurrHasNonStatus;
    }

    /**
     * Set the value related to the column: cboxCurrHasNonStatus
     * @param _cboxCurrHasNonStatus the cboxCurrHasNonStatus value
     */
    public void setCboxCurrHasNonStatus (String _cboxCurrHasNonStatus) {
        this._cboxCurrHasNonStatus = _cboxCurrHasNonStatus;
    }

    /**
     * Return the value associated with the column: cboxEmploymentIssue
     */
    public String getCboxEmploymentIssue () {
        return _cboxEmploymentIssue;
    }

    /**
     * Set the value related to the column: cboxEmploymentIssue
     * @param _cboxEmploymentIssue the cboxEmploymentIssue value
     */
    public void setCboxEmploymentIssue (String _cboxEmploymentIssue) {
        this._cboxEmploymentIssue = _cboxEmploymentIssue;
    }

    /**
     * Return the value associated with the column: yearOfBirth
     */
    public String getYearOfBirth () {
    	return(StringUtils.trimToEmpty(_yearOfBirth));
    }

    /**
     * Set the value related to the column: yearOfBirth
     * @param _yearOfBirth the yearOfBirth value
     */
    public void setYearOfBirth (String _yearOfBirth) {
        this._yearOfBirth = _yearOfBirth;
    }

    /**
     * Return the value associated with the column: cboxBase2ndIncomePension
     */
    public String getCboxBase2ndIncomePension () {
        return _cboxBase2ndIncomePension;
    }

    /**
     * Set the value related to the column: cboxBase2ndIncomePension
     * @param _cboxBase2ndIncomePension the cboxBase2ndIncomePension value
     */
    public void setCboxBase2ndIncomePension (String _cboxBase2ndIncomePension) {
        this._cboxBase2ndIncomePension = _cboxBase2ndIncomePension;
    }

    /**
     * Return the value associated with the column: cboxCurrLivingWithSpousePlus
     */
    public String getCboxCurrLivingWithSpousePlus () {
        return _cboxCurrLivingWithSpousePlus;
    }

    /**
     * Set the value related to the column: cboxCurrLivingWithSpousePlus
     * @param _cboxCurrLivingWithSpousePlus the cboxCurrLivingWithSpousePlus value
     */
    public void setCboxCurrLivingWithSpousePlus (String _cboxCurrLivingWithSpousePlus) {
        this._cboxCurrLivingWithSpousePlus = _cboxCurrLivingWithSpousePlus;
    }

    /**
     * Return the value associated with the column: cboxPreferredLanguageUnknown
     */
    public String getCboxPreferredLanguageUnknown () {
        if (_cboxPreferredLanguageUnknown!=""&&_cboxPreferredLanguageUnknown!=null)
            return _cboxPreferredLanguageUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxPreferredLanguageUnknown
     * @param _cboxPreferredLanguageUnknown the cboxPreferredLanguageUnknown value
     */
    public void setCboxPreferredLanguageUnknown (String _cboxPreferredLanguageUnknown) {
        this._cboxPreferredLanguageUnknown = _cboxPreferredLanguageUnknown;
    }

    /**
     * Return the value associated with the column: cbox2ndAnxietyDisorderPSd
     */
    public String getCbox2ndAnxietyDisorderPSd () {
        return _cbox2ndAnxietyDisorderPSd;
    }

    /**
     * Set the value related to the column: cbox2ndAnxietyDisorderPSd
     * @param _cbox2ndAnxietyDisorderPSd the cbox2ndAnxietyDisorderPSd value
     */
    public void setCbox2ndAnxietyDisorderPSd (String _cbox2ndAnxietyDisorderPSd) {
        this._cbox2ndAnxietyDisorderPSd = _cbox2ndAnxietyDisorderPSd;
    }

    /**
     * Return the value associated with the column: cboxSexualAbuseIssue
     */
    public String getCboxSexualAbuseIssue () {
        return _cboxSexualAbuseIssue;
    }

    /**
     * Set the value related to the column: cboxSexualAbuseIssue
     * @param _cboxSexualAbuseIssue the cboxSexualAbuseIssue value
     */
    public void setCboxSexualAbuseIssue (String _cboxSexualAbuseIssue) {
        this._cboxSexualAbuseIssue = _cboxSexualAbuseIssue;
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
     * Return the value associated with the column: cboxBaseUseShelterClinic
     */
    public String getCboxBaseUseShelterClinic () {
        return _cboxBaseUseShelterClinic;
    }

    /**
     * Set the value related to the column: cboxBaseUseShelterClinic
     * @param _cboxBaseUseShelterClinic the cboxBaseUseShelterClinic value
     */
    public void setCboxBaseUseShelterClinic (String _cboxBaseUseShelterClinic) {
        this._cboxBaseUseShelterClinic = _cboxBaseUseShelterClinic;
    }

    /**
     * Return the value associated with the column: cboxCurrAccessHealthCareUnknown
     */
    public String getCboxCurrAccessHealthCareUnknown () {
        if(_cboxCurrAccessHealthCareUnknown!=""&&_cboxCurrAccessHealthCareUnknown!=null)
            return _cboxCurrAccessHealthCareUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxCurrAccessHealthCareUnknown
     * @param _cboxCurrAccessHealthCareUnknown the cboxCurrAccessHealthCareUnknown value
     */
    public void setCboxCurrAccessHealthCareUnknown (String _cboxCurrAccessHealthCareUnknown) {
        this._cboxCurrAccessHealthCareUnknown = _cboxCurrAccessHealthCareUnknown;
    }

    /**
     * Return the value associated with the column: cbox2ndIncomePension
     */
    public String getCbox2ndIncomePension () {
        return _cbox2ndIncomePension;
    }

    /**
     * Set the value related to the column: cbox2ndIncomePension
     * @param _cbox2ndIncomePension the cbox2ndIncomePension value
     */
    public void setCbox2ndIncomePension (String _cbox2ndIncomePension) {
        this._cbox2ndIncomePension = _cbox2ndIncomePension;
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
     * Return the value associated with the column: cboxCurrDoNotAccessHealthCare
     */
    public String getCboxCurrDoNotAccessHealthCare () {
        return _cboxCurrDoNotAccessHealthCare;
    }

    /**
     * Set the value related to the column: cboxCurrDoNotAccessHealthCare
     * @param _cboxCurrDoNotAccessHealthCare the cboxCurrDoNotAccessHealthCare value
     */
    public void setCboxCurrDoNotAccessHealthCare (String _cboxCurrDoNotAccessHealthCare) {
        this._cboxCurrDoNotAccessHealthCare = _cboxCurrDoNotAccessHealthCare;
    }

    /**
     * Return the value associated with the column: cboxCurrUseHealthBus
     */
    public String getCboxCurrUseHealthBus () {
        return _cboxCurrUseHealthBus;
    }

    /**
     * Set the value related to the column: cboxCurrUseHealthBus
     * @param _cboxCurrUseHealthBus the cboxCurrUseHealthBus value
     */
    public void setCboxCurrUseHealthBus (String _cboxCurrUseHealthBus) {
        this._cboxCurrUseHealthBus = _cboxCurrUseHealthBus;
    }

    /**
     * Return the value associated with the column: cboxBaseLivingWithParents
     */
    public String getCboxBaseLivingWithParents () {
        return _cboxBaseLivingWithParents;
    }

    /**
     * Set the value related to the column: cboxBaseLivingWithParents
     * @param _cboxBaseLivingWithParents the cboxBaseLivingWithParents value
     */
    public void setCboxBaseLivingWithParents (String _cboxBaseLivingWithParents) {
        this._cboxBaseLivingWithParents = _cboxBaseLivingWithParents;
    }

    /**
     * Return the value associated with the column: cboxBaseHasUnknown
     */
    public String getCboxBaseHasUnknown () {
        if(_cboxBaseHasUnknown!=""&&_cboxBaseHasUnknown!=null)
            return _cboxBaseHasUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxBaseHasUnknown
     * @param _cboxBaseHasUnknown the cboxBaseHasUnknown value
     */
    public void setCboxBaseHasUnknown (String _cboxBaseHasUnknown) {
        this._cboxBaseHasUnknown = _cboxBaseHasUnknown;
    }

    /**
     * Return the value associated with the column: radioBaseNeedSocialServices
     */
    public String getRadioBaseNeedSocialServices () {
        if(_radioBaseNeedSocialServices!=""&&_radioBaseNeedSocialServices!=null)
            return _radioBaseNeedSocialServices;
        else return "3";
    }

    /**
     * Set the value related to the column: radioBaseNeedSocialServices
     * @param _radioBaseNeedSocialServices the radioBaseNeedSocialServices value
     */
    public void setRadioBaseNeedSocialServices (String _radioBaseNeedSocialServices) {
        this._radioBaseNeedSocialServices = _radioBaseNeedSocialServices;
    }

    /**
     * Return the value associated with the column: cboxNoneListedIssue
     */
    public String getCboxNoneListedIssue () {


        if(_cboxNoneListedIssue!=""&&_cboxNoneListedIssue!=null)
            return _cboxNoneListedIssue;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxNoneListedIssue
     * @param _cboxNoneListedIssue the cboxNoneListedIssue value
     */
    public void setCboxNoneListedIssue (String _cboxNoneListedIssue) {

        this._cboxNoneListedIssue = _cboxNoneListedIssue;
    }

    /**
     * Return the value associated with the column: cbox2ndUnknown
     */
    public String getCbox2ndUnknown () {
        if(_cbox2ndUnknown!=""&&_cbox2ndUnknown!=null)
            return _cbox2ndUnknown;
        else return"tr";
    }

    /**
     * Set the value related to the column: cbox2ndUnknown
     * @param _cbox2ndUnknown the cbox2ndUnknown value
     */
    public void setCbox2ndUnknown (String _cbox2ndUnknown) {
        this._cbox2ndUnknown = _cbox2ndUnknown;
    }

    /**
     * Return the value associated with the column: cboxReferralBySafeBeds
     */
    public String getCboxReferralBySafeBeds () {
        return _cboxReferralBySafeBeds;
    }

    /**
     * Set the value related to the column: cboxReferralBySafeBeds
     * @param _cboxReferralBySafeBeds the cboxReferralBySafeBeds value
     */
    public void setCboxReferralBySafeBeds (String _cboxReferralBySafeBeds) {
        this._cboxReferralBySafeBeds = _cboxReferralBySafeBeds;
    }

    /**
     * Return the value associated with the column: cbox2ndAdjustDisorder
     */
    public String getCbox2ndAdjustDisorder () {
        return _cbox2ndAdjustDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndAdjustDisorder
     * @param _cbox2ndAdjustDisorder the cbox2ndAdjustDisorder value
     */
    public void setCbox2ndAdjustDisorder (String _cbox2ndAdjustDisorder) {
        this._cbox2ndAdjustDisorder = _cbox2ndAdjustDisorder;
    }

    /**
     * Return the value associated with the column: cboxHospitalizationUnknown
     */
    public String getCboxHospitalizationUnknown () {
        return _cboxHospitalizationUnknown;
    }

    /**
     * Set the value related to the column: cboxHospitalizationUnknown
     * @param _cboxHospitalizationUnknown the cboxHospitalizationUnknown value
     */
    public void setCboxHospitalizationUnknown (String _cboxHospitalizationUnknown) {
        this._cboxHospitalizationUnknown = _cboxHospitalizationUnknown;
    }

    /**
     * Return the value associated with the column: cboxConcurrentDisorder
     */
    public String getCboxConcurrentDisorder () {
        return _cboxConcurrentDisorder;
    }

    /**
     * Set the value related to the column: cboxConcurrentDisorder
     * @param _cboxConcurrentDisorder the cboxConcurrentDisorder value
     */
    public void setCboxConcurrentDisorder (String _cboxConcurrentDisorder) {
        this._cboxConcurrentDisorder = _cboxConcurrentDisorder;
    }

    /**
     * Return the value associated with the column: cbox2ndEatingDisorder
     */
    public String getCbox2ndEatingDisorder () {
        return _cbox2ndEatingDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndEatingDisorder
     * @param _cbox2ndEatingDisorder the cbox2ndEatingDisorder value
     */
    public void setCbox2ndEatingDisorder (String _cbox2ndEatingDisorder) {
        this._cbox2ndEatingDisorder = _cbox2ndEatingDisorder;
    }

    /**
     * Return the value associated with the column: cboxCompleteWithReferral
     */
    public String getCboxCompleteWithReferral () {
        return _cboxCompleteWithReferral;
    }

    /**
     * Set the value related to the column: cboxCompleteWithReferral
     * @param _cboxCompleteWithReferral the cboxCompleteWithReferral value
     */
    public void setCboxCompleteWithReferral (String _cboxCompleteWithReferral) {
        this._cboxCompleteWithReferral = _cboxCompleteWithReferral;
    }

    /**
     * Return the value associated with the column: cboxBaseUseWalkinClinic
     */
    public String getCboxBaseUseWalkinClinic () {
        return _cboxBaseUseWalkinClinic;
    }

    /**
     * Set the value related to the column: cboxBaseUseWalkinClinic
     * @param _cboxBaseUseWalkinClinic the cboxBaseUseWalkinClinic value
     */
    public void setCboxBaseUseWalkinClinic (String _cboxBaseUseWalkinClinic) {
        this._cboxBaseUseWalkinClinic = _cboxBaseUseWalkinClinic;
    }

    /**
     * Return the value associated with the column: monthlyProgressReport
     */
    public String getMonthlyProgressReport () {
        return _monthlyProgressReport;
    }

    /**
     * Set the value related to the column: monthlyProgressReport
     * @param _monthlyProgressReport the monthlyProgressReport value
     */
    public void setMonthlyProgressReport (String _monthlyProgressReport) {
        this._monthlyProgressReport = _monthlyProgressReport;
    }

    /**
     * Return the value associated with the column: cboxOtherAnxietyDisorder
     */
    public String getCboxOtherAnxietyDisorder () {
        return _cboxOtherAnxietyDisorder;
    }

    /**
     * Set the value related to the column: cboxOtherAnxietyDisorder
     * @param _cboxOtherAnxietyDisorder the cboxOtherAnxietyDisorder value
     */
    public void setCboxOtherAnxietyDisorder (String _cboxOtherAnxietyDisorder) {
        this._cboxOtherAnxietyDisorder = _cboxOtherAnxietyDisorder;
    }

    /**
     * Return the value associated with the column: cboxBaseIncomeMgmentHasTrustee
     */
    public String getCboxBaseIncomeMgmentHasTrustee () {
        return _cboxBaseIncomeMgmentHasTrustee;
    }

    /**
     * Set the value related to the column: cboxBaseIncomeMgmentHasTrustee
     * @param _cboxBaseIncomeMgmentHasTrustee the cboxBaseIncomeMgmentHasTrustee value
     */
    public void setCboxBaseIncomeMgmentHasTrustee (String _cboxBaseIncomeMgmentHasTrustee) {
        this._cboxBaseIncomeMgmentHasTrustee = _cboxBaseIncomeMgmentHasTrustee;
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
     * Return the value associated with the column: cboxBaseIncomeMgmentNeedsTrustee
     */
    public String getCboxBaseIncomeMgmentNeedsTrustee () {
        return _cboxBaseIncomeMgmentNeedsTrustee;
    }

    /**
     * Set the value related to the column: cboxBaseIncomeMgmentNeedsTrustee
     * @param _cboxBaseIncomeMgmentNeedsTrustee the cboxBaseIncomeMgmentNeedsTrustee value
     */
    public void setCboxBaseIncomeMgmentNeedsTrustee (String _cboxBaseIncomeMgmentNeedsTrustee) {
        this._cboxBaseIncomeMgmentNeedsTrustee = _cboxBaseIncomeMgmentNeedsTrustee;
    }

    /**
     * Return the value associated with the column: radioRaceCaucasian
     */
    public String getRadioRaceCaucasian () {
        return _radioRaceCaucasian;
    }

    /**
     * Set the value related to the column: radioRaceCaucasian
     * @param _radioRaceCaucasian the radioRaceCaucasian value
     */
    public void setRadioRaceCaucasian (String _radioRaceCaucasian) {
        this._radioRaceCaucasian = _radioRaceCaucasian;
    }

    /**
     * Return the value associated with the column: cboxMentalIssue
     */
    public String getCboxMentalIssue () {
        return _cboxMentalIssue;
    }

    /**
     * Set the value related to the column: cboxMentalIssue
     * @param _cboxMentalIssue the cboxMentalIssue value
     */
    public void setCboxMentalIssue (String _cboxMentalIssue) {
        this._cboxMentalIssue = _cboxMentalIssue;
    }

    /**
     * Return the value associated with the column: preferredLanguage
     */
    public String getPreferredLanguage () {
        return _preferredLanguage;
    }

    /**
     * Set the value related to the column: preferredLanguage
     * @param _preferredLanguage the preferredLanguage value
     */
    public void setPreferredLanguage (String _preferredLanguage) {
        this._preferredLanguage = _preferredLanguage;
    }

    /**
     * Return the value associated with the column: cbox2ndMoodDisorder
     */
    public String getCbox2ndMoodDisorder () {
        return _cbox2ndMoodDisorder;
    }

    /**
     * Set the value related to the column: cbox2ndMoodDisorder
     * @param _cbox2ndMoodDisorder the cbox2ndMoodDisorder value
     */
    public void setCbox2ndMoodDisorder (String _cbox2ndMoodDisorder) {
        this._cbox2ndMoodDisorder = _cbox2ndMoodDisorder;
    }

    /**
     * Return the value associated with the column: radioPrimaryDiagnosis
     */
    public String getRadioPrimaryDiagnosis () {
        if(_radioPrimaryDiagnosis!=""&&_radioPrimaryDiagnosis!=null)
            return _radioPrimaryDiagnosis;
        else return "18";
    }

    /**
     * Set the value related to the column: radioPrimaryDiagnosis
     * @param _radioPrimaryDiagnosis the radioPrimaryDiagnosis value
     */
    public void setRadioPrimaryDiagnosis (String _radioPrimaryDiagnosis) {
        this._radioPrimaryDiagnosis = _radioPrimaryDiagnosis;
    }

    /**
     * Return the value associated with the column: cboxCurrHasNativeCard
     */
    public String getCboxCurrHasNativeCard () {
        return _cboxCurrHasNativeCard;
    }

    /**
     * Set the value related to the column: cboxCurrHasNativeCard
     * @param _cboxCurrHasNativeCard the cboxCurrHasNativeCard value
     */
    public void setCboxCurrHasNativeCard (String _cboxCurrHasNativeCard) {
        this._cboxCurrHasNativeCard = _cboxCurrHasNativeCard;
    }

    /**
     * Return the value associated with the column: cboxBaseLivingWithChildren
     */
    public String getCboxBaseLivingWithChildren () {
        return _cboxBaseLivingWithChildren;
    }

    /**
     * Set the value related to the column: cboxBaseLivingWithChildren
     * @param _cboxBaseLivingWithChildren the cboxBaseLivingWithChildren value
     */
    public void setCboxBaseLivingWithChildren (String _cboxBaseLivingWithChildren) {
        this._cboxBaseLivingWithChildren = _cboxBaseLivingWithChildren;
    }

    /**
     * Return the value associated with the column: cboxBaseLivingWithSpouse
     */
    public String getCboxBaseLivingWithSpouse () {
        return _cboxBaseLivingWithSpouse;
    }

    /**
     * Set the value related to the column: cboxBaseLivingWithSpouse
     * @param _cboxBaseLivingWithSpouse the cboxBaseLivingWithSpouse value
     */
    public void setCboxBaseLivingWithSpouse (String _cboxBaseLivingWithSpouse) {
        this._cboxBaseLivingWithSpouse = _cboxBaseLivingWithSpouse;
    }

    /**
     * Return the value associated with the column: radioTreatmentOrders
     */
    public String getRadioTreatmentOrders () {
        if(_radioTreatmentOrders!=""&&_radioTreatmentOrders!=null)
            return _radioTreatmentOrders;
        else return "3";
    }

    /**
     * Set the value related to the column: radioTreatmentOrders
     * @param _radioTreatmentOrders the radioTreatmentOrders value
     */
    public void setRadioTreatmentOrders (String _radioTreatmentOrders) {
        this._radioTreatmentOrders = _radioTreatmentOrders;
    }

    /**
     * Return the value associated with the column: cboxReferralByOtherPeople
     */
    public String getCboxReferralByOtherPeople () {
        if(_cboxReferralByOtherPeople!=""&&_cboxReferralByOtherPeople!=null)
            return _cboxReferralByOtherPeople;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxReferralByOtherPeople
     * @param _cboxReferralByOtherPeople the cboxReferralByOtherPeople value
     */
    public void setCboxReferralByOtherPeople (String _cboxReferralByOtherPeople) {
        this._cboxReferralByOtherPeople = _cboxReferralByOtherPeople;
    }

    /**
     * Return the value associated with the column: cboxBaseAccessHealthCareUnknown
     */
    public String getCboxBaseAccessHealthCareUnknown () {
        if(_cboxBaseAccessHealthCareUnknown!=""&&_cboxBaseAccessHealthCareUnknown!=null)
            return _cboxBaseAccessHealthCareUnknown;
        else return "tr";
    }

    /**
     * Set the value related to the column: cboxBaseAccessHealthCareUnknown
     * @param _cboxBaseAccessHealthCareUnknown the cboxBaseAccessHealthCareUnknown value
     */
    public void setCboxBaseAccessHealthCareUnknown (String _cboxBaseAccessHealthCareUnknown) {
        this._cboxBaseAccessHealthCareUnknown = _cboxBaseAccessHealthCareUnknown;
    }

    /**
     * Return the value associated with the column: cboxCompleteWithoutReferral
     */
    public String getCboxCompleteWithoutReferral () {
        return _cboxCompleteWithoutReferral;
    }

    /**
     * Set the value related to the column: cboxCompleteWithoutReferral
     * @param _cboxCompleteWithoutReferral the cboxCompleteWithoutReferral value
     */
    public void setCboxCompleteWithoutReferral (String _cboxCompleteWithoutReferral) {
        this._cboxCompleteWithoutReferral = _cboxCompleteWithoutReferral;
    }

    /**
     * Return the value associated with the column: cboxDeathExit
     */
    public String getCboxDeathExit () {
        return _cboxDeathExit;
    }

    /**
     * Set the value related to the column: cboxDeathExit
     * @param _cboxDeathExit the cboxDeathExit value
     */
    public void setCboxDeathExit (String _cboxDeathExit) {
        this._cboxDeathExit = _cboxDeathExit;
    }

    /**
     * Return the value associated with the column: cboxBaseLivingWithRelatives
     */
    public String getCboxBaseLivingWithRelatives () {
        return _cboxBaseLivingWithRelatives;
    }

    /**
     * Set the value related to the column: cboxBaseLivingWithRelatives
     * @param _cboxBaseLivingWithRelatives the cboxBaseLivingWithRelatives value
     */
    public void setCboxBaseLivingWithRelatives (String _cboxBaseLivingWithRelatives) {
        this._cboxBaseLivingWithRelatives = _cboxBaseLivingWithRelatives;
    }

    /**
     * Return the value associated with the column: currAddress
     */
    public String getCurrAddress () {
        return _currAddress;
    }

    /**
     * Set the value related to the column: currAddress
     * @param _currAddress the currAddress value
     */
    public void setCurrAddress (String _currAddress) {
        this._currAddress = _currAddress;
    }

    /**
     * Return the value associated with the column: currPhone
     */
    public String getCurrPhone () {
        return _currPhone;
    }

    /**
     * Set the value related to the column: currPhone
     * @param _currPhone the currPhone value
     */
    public void setCurrPhone (String _currPhone) {
        this._currPhone = _currPhone;
    }

    /**
     * Return the value associated with the column: pastAddresses
     */
    public String getPastAddresses () {
        return _pastAddresses;
    }

    /**
     * Set the value related to the column: pastAddresses
     * @param _pastAddresses the pastAddresses value
     */
    public void setPastAddresses (String _pastAddresses) {
        this._pastAddresses = _pastAddresses;
    }

    /**
     * Return the value associated with the column: contactsInfo
     */
    public String getContactsInfo () {
        return _contactsInfo;
    }

    /**
     * Set the value related to the column: contactsInfo
     * @param _contactsInfo the contactsInfo value
     */
    public void setContactsInfo (String _contactsInfo) {
        this._contactsInfo = _contactsInfo;
    }

    /**
     * Return the value associated with the column: ids
     */
    public String getIds () {
        return _ids;
    }

    /**
     * Set the value related to the column: ids
     * @param _ids the ids value
     */
    public void setIds (String _ids) {
        this._ids = _ids;
    }

    /**
     * Return the value associated with the column: hospitalizations
     */
    public String getHospitalizations () {
        return _hospitalizations;
    }

    /**
     * Set the value related to the column: hospitalizations
     * @param _hospitalizations the hospitalizations value
     */
    public void setHospitalizations (String _hospitalizations) {
        this._hospitalizations = _hospitalizations;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Formintakec)) return false;
        else {
            Formintakec mObj = (Formintakec) obj;
            if (null == this.getId() && null == mObj.getId()) return false;
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
