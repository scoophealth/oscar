-- MySQL dump 10.9
--
-- Host: localhost    Database: oscar_mcmaster
-- ------------------------------------------------------
-- Server version	4.1.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `access_type`
--

DROP TABLE IF EXISTS `access_type`;
CREATE TABLE `access_type` (
  `access_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  PRIMARY KEY  (`access_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `admission`
--

DROP TABLE IF EXISTS `admission`;
CREATE TABLE `admission` (
  `am_id` bigint(11) NOT NULL auto_increment,
  `client_id` bigint(11) NOT NULL default '0',
  `program_id` bigint(11) NOT NULL default '0',
  `provider_no` bigint(11) NOT NULL default '0',
  `admission_date` datetime default NULL,
  `admission_notes` varchar(255) default NULL,
  `temp_admission` char(1) default NULL,
  `discharge_date` datetime default NULL,
  `discharge_notes` varchar(255) default NULL,
  `temp_admit_discharge` char(1) default NULL,
  `admission_status` varchar(24) default NULL,
  `team_id` int(10) default NULL,
  PRIMARY KEY  (`am_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `agency`
--

DROP TABLE IF EXISTS `agency`;
CREATE TABLE `agency` (
  `id` bigint(20) NOT NULL default '0',
  `name` varchar(50) NOT NULL default '',
  `description` varchar(255) default NULL,
  `contact_name` varchar(255) default NULL,
  `contact_email` varchar(255) default NULL,
  `contact_phone` varchar(255) default NULL,
  `local` tinyint(1) NOT NULL default '0',
  `integrator_enabled` tinyint(1) NOT NULL default '0',
  `integrator_url` varchar(255) default NULL,
  `integrator_jms` varchar(255) default NULL,
  `integrator_username` varchar(255) default NULL,
  `integrator_password` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `bed_log`
--

DROP TABLE IF EXISTS `bed_log`;
CREATE TABLE `bed_log` (
  `id` bigint(20) NOT NULL auto_increment,
  `program_id` bigint(20) NOT NULL default '0',
  `provider_no` varchar(11) NOT NULL default '',
  `sheet_id` bigint(20) NOT NULL default '0',
  `demographic_no` bigint(20) NOT NULL default '0',
  `time` varchar(50) NOT NULL default '',
  `status` varchar(50) NOT NULL default '',
  `date_created` datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`id`),
  KEY `FKF22A268694786124` (`sheet_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `bed_log_sheet`
--

DROP TABLE IF EXISTS `bed_log_sheet`;
CREATE TABLE `bed_log_sheet` (
  `id` bigint(20) NOT NULL auto_increment,
  `date_created` datetime NOT NULL default '0000-00-00 00:00:00',
  `program_id` bigint(20) default NULL,
  `closed` tinyint(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `client_referral`
--

DROP TABLE IF EXISTS `client_referral`;
CREATE TABLE `client_referral` (
  `referral_id` bigint(20) NOT NULL auto_increment,
  `agency_id` bigint(20) NOT NULL default '0',
  `client_id` bigint(20) NOT NULL default '0',
  `referral_date` datetime default NULL,
  `provider_no` bigint(20) NOT NULL default '0',
  `notes` varchar(255) default NULL,
  `program_id` bigint(20) NOT NULL default '0',
  `status` varchar(30) default NULL,
  PRIMARY KEY  (`referral_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `formintakea`
--

DROP TABLE IF EXISTS `formintakea`;
CREATE TABLE `formintakea` (
  `ID` bigint(11) NOT NULL auto_increment,
  `demographic_no` bigint(11) NOT NULL default '0',
  `provider_no` bigint(11) default '0',
  `formCreated` date default '0000-00-00',
  `formEdited` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `assessDate` varchar(24) default '',
  `assessStartTime` varchar(24) default '',
  `enterSeatonDate` varchar(24) default '',
  `cbox_newClient` char(1) default '',
  `cbox_dateOfReadmission` char(1) default '',
  `dateOfReadmission` varchar(24) default '',
  `cbox_isStatementRead` char(1) default '',
  `clientSurname` varchar(50) default '',
  `clientFirstName` varchar(50) default '',
  `month` char(2) default '',
  `day` char(2) default '',
  `year` varchar(4) default '',
  `cbox_speakEnglish` char(1) default '',
  `cbox_speakFrench` char(1) default '',
  `cbox_speakOther` char(1) default '',
  `speakOther` varchar(36) default '',
  `reasonToSeaton` varchar(255) default '',
  `everAtSeatonBefore` varchar(24) default NULL,
  `datesAtSeaton` varchar(70) default '',
  `cbox_assistInHealth` char(1) default '',
  `cbox_assistInIdentification` char(1) default '',
  `cbox_assistInAddictions` char(1) default '',
  `cbox_assistInHousing` char(1) default '',
  `cbox_assistInEducation` char(1) default '',
  `cbox_assistInEmployment` char(1) default '',
  `cbox_assistInFinance` char(1) default '',
  `cbox_assistInLegal` char(1) default '',
  `cbox_assistInImmigration` char(1) default '',
  `hasWhatID` varchar(120) default '',
  `cbox_noID` char(1) default '',
  `cbox_sinCard` char(1) default '',
  `sinNum` varchar(24) default '',
  `cbox_healthCard` char(1) default '',
  `healthCardNum` varchar(24) default '',
  `healthCardVer` char(2) default '',
  `cbox_birthCertificate` char(1) default '',
  `birthCertificateNum` varchar(24) default '',
  `cbox_citzenshipCard` char(1) default '',
  `citzenshipCardNum` varchar(24) default '',
  `cbox_immigrant` char(1) default '',
  `immigrantNum` varchar(24) default '',
  `cbox_refugee` char(1) default '',
  `refugeeNum` varchar(24) default '',
  `cbox_otherID` char(1) default '',
  `otherIdentification` varchar(70) default '',
  `cbox_idFiled` char(1) default '',
  `cbox_idNone` char(1) default '',
  `commentsOnID` varchar(255) default '',
  `cbox_OW` char(1) default '',
  `cbox_ODSP` char(1) default '',
  `cbox_WSIB` char(1) default '',
  `cbox_Employment` char(1) default '',
  `cbox_EI` char(1) default '',
  `cbox_OAS` char(1) default '',
  `cbox_CPP` char(1) default '',
  `cbox_OtherIncome` char(1) default '',
  `radio_onlineCheck` varchar(36) default '',
  `radio_active` varchar(36) default '',
  `cbox_noRecord` char(1) default '',
  `lastIssueDate` varchar(24) default '',
  `office` varchar(50) default '',
  `workerNum` varchar(36) default '',
  `amtReceived` varchar(9) default '',
  `radio_hasDoctor` varchar(36) default '',
  `doctorName` varchar(50) default '',
  `doctorName2` varchar(50) default '',
  `doctorPhone` varchar(24) default '',
  `doctorPhoneExt` varchar(8) default '',
  `doctorAddress` varchar(120) default '',
  `radio_seeDoctor` varchar(36) default '',
  `radio_healthIssue` varchar(36) default '',
  `healthIssueDetails` varchar(255) default '',
  `cbox_hasDiabetes` char(1) default '',
  `cbox_insulin` char(1) default '',
  `cbox_epilepsy` char(1) default '',
  `cbox_bleeding` char(1) default '',
  `cbox_hearingImpair` char(1) default '',
  `cbox_visualImpair` char(1) default '',
  `cbox_mobilityImpair` char(1) default '',
  `mobilityImpair` varchar(255) default '',
  `radio_otherHealthConcern` varchar(36) default '',
  `otherHealthConerns` varchar(255) default '',
  `radio_takeMedication` varchar(36) default '',
  `namesOfMedication` varchar(120) default '',
  `radio_helpObtainMedication` varchar(36) default '',
  `helpObtainMedication` varchar(255) default '',
  `radio_allergicToMedication` varchar(36) default '',
  `allergicToMedicationName` varchar(255) default '',
  `reactionToMedication` varchar(255) default '',
  `radio_mentalHealthConcerns` varchar(36) default '',
  `mentalHealthConcerns` varchar(255) default '',
  `cbox_isStatement6Read` char(1) default '',
  `frequencyOfSeeingDoctor` varchar(8) default '',
  `cbox_visitWalkInClinic` char(1) default '',
  `cbox_visitHealthCenter` char(1) default '',
  `cbox_visitEmergencyRoom` char(1) default '',
  `cbox_visitOthers` char(1) default '',
  `otherSpecify` varchar(120) default '',
  `cbox_visitHealthOffice` char(1) default '',
  `radio_seeSameDoctor` varchar(36) default '',
  `frequencyOfSeeingEmergencyRoomDoctor` varchar(8) default '',
  `radio_didNotReceiveHealthCare` varchar(36) default '',
  `cbox_treatPhysicalHealth` char(1) default '',
  `cbox_treatMentalHealth` char(1) default '',
  `cbox_regularCheckup` char(1) default '',
  `cbox_treatOtherReasons` char(1) default '',
  `treatOtherReasons` varchar(255) default '',
  `cbox_treatInjury` char(1) default '',
  `cbox_goToWalkInClinic` char(1) default '',
  `cbox_goToHealthCenter` char(1) default '',
  `cbox_goToEmergencyRoom` char(1) default '',
  `cbox_goToOthers` char(1) default '',
  `goToOthers` varchar(255) default '',
  `cbox_HealthOffice` char(1) default '',
  `radio_appmtSeeDoctorIn3Mths` varchar(36) default '',
  `radio_needRegularDoctor` varchar(36) default '',
  `radio_objectToRegularDoctorIn4Wks` varchar(36) default '',
  `radio_rateOverallHealth` varchar(36) default '',
  `radio_speakToResearcher` varchar(36) default '',
  `contactName` varchar(70) default '',
  `contactPhone` varchar(24) default '',
  `contactAddress` varchar(255) default '',
  `contactRelationship` varchar(120) default '',
  `radio_hasMentalIllness` varchar(36) default '',
  `radio_hasDrinkingProblem` varchar(36) default '',
  `radio_hasDrugProblem` varchar(36) default '',
  `radio_hasHealthProblem` varchar(36) default '',
  `radio_hasBehaviorProblem` varchar(36) default '',
  `radio_needSeatonService` varchar(36) default '',
  `radio_seatonTour` varchar(36) default '',
  `seatonNotToured` varchar(255) default '',
  `radio_pamphletIssued` varchar(36) default '',
  `pamphletNotIssued` varchar(255) default '',
  `summary` varchar(255) default '',
  `completedBy` varchar(120) default '',
  `assessCompleteTime` varchar(36) default '',
  `cbox_pamphletIssued` char(1) default '',
  `cbox_hostel` char(1) default '',
  `cbox_hostel_fusion_care` char(1) default '',
  `cbox_rotaryClub` char(1) default '',
  `cbox_annexHarm` char(1) default '',
  `cbox_longTermProgram` char(1) default '',
  `cbox_birchmountResidence` char(1) default '',
  `cbox_oNeillHouse` char(1) default '',
  `cbox_fortYork` char(1) default '',
  `cbox_downsviewDells` char(1) default '',
  `cbox_sharing` char(1) default '',
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `formintakeb`
--

DROP TABLE IF EXISTS `formintakeb`;
CREATE TABLE `formintakeb` (
  `ID` bigint(11) NOT NULL auto_increment,
  `demographic_no` bigint(11) NOT NULL default '0',
  `provider_no` bigint(11) default '0',
  `formCreated` date default '0001-01-01',
  `formEdited` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `dateAssessment` varchar(24) default '',
  `assessStartTime` varchar(24) default '',
  `dateEnteredSeaton` varchar(24) default '',
  `dateExitedSeaton` varchar(24) default '',
  `clientSurname` varchar(50) default '',
  `clientFirstName` varchar(50) default '',
  `month` char(2) default '',
  `day` char(2) default '',
  `year` varchar(4) default '',
  `cbox_speakEnglish` char(1) default '',
  `cbox_speakFrench` char(1) default '',
  `cbox_speakSpanish` char(1) default '',
  `cbox_speakOther` char(1) default '',
  `speakOther` varchar(50) default '',
  `howHearAboutSeaton` varchar(255) default '',
  `whereBeforeSeaton` varchar(255) default '',
  `radio_hasIDInFile` varchar(24) default '',
  `cbox_assistWithSINCard` char(1) default '',
  `cbox_assistWithImmigrant` char(1) default '',
  `cbox_assistWithHealthCard` char(1) default '',
  `cbox_assistWithRefugee` char(1) default '',
  `cbox_assistWithBirthCert` char(1) default '',
  `cbox_assistWithNone` char(1) default '',
  `cbox_assistWithCitizenCard` char(1) default '',
  `cbox_assistWithOther` char(1) default '',
  `assistWithOther` varchar(50) default '',
  `commentsOnID` varchar(255) default '',
  `radio_haveHealthCoverage` varchar(24) default '',
  `cbox_haveOHIP` char(1) default '',
  `cbox_haveODSP` char(1) default '',
  `cbox_haveODB` char(1) default '',
  `cbox_haveOther1` char(1) default '',
  `haveOther` varchar(70) default '',
  `radio_haveMentalProblem` varchar(24) default '',
  `cbox_haveSchizophrenia` char(1) default '',
  `radio_caredForSchizophrenia` varchar(24) default '',
  `cbox_haveManic` char(1) default '',
  `radio_caredForManic` varchar(24) default '',
  `cbox_haveDepression` char(1) default '',
  `radio_caredForDepression` varchar(24) default '',
  `cbox_haveAnxiety` char(1) default '',
  `radio_caredForAnxiety` varchar(24) default '',
  `cbox_haveOther2` char(1) default '',
  `radio_caredForOther` varchar(24) default '',
  `doctor1NameAddr` varchar(255) default '',
  `doctor1Phone` varchar(24) default '',
  `dateLastDoctor1Contact` varchar(24) default '',
  `doctor2NameAddr` varchar(255) default '',
  `doctor2Phone` varchar(24) default '',
  `dateLastDoctor2Contact` varchar(24) default '',
  `radio_needAssistWithMedication` varchar(24) default '',
  `cbox_rememberToTakeMedication` char(1) default '',
  `cbox_getMoreMedication` char(1) default '',
  `cbox_storeMedication` char(1) default '',
  `cbox_needHelpInOther` char(1) default '',
  `cbox_takePrescribedMedication` char(1) default '',
  `commentsOnNeedHelp` varchar(255) default '',
  `radio_doYouDrink` varchar(24) default '',
  `drinksPerDay` char(3) default '',
  `drinksPerWeek` varchar(4) default '',
  `drinksPerMonth` varchar(5) default '',
  `radio_howMuchDrink` varchar(24) default '',
  `radio_drinkThese` varchar(24) default '',
  `radio_seenDoctorRegAlcohol` varchar(24) default '',
  `radio_wantHelpQuit` varchar(24) default '',
  `commentsOnAlcohol` varchar(255) default '',
  `radio_useDrugs` varchar(24) default '',
  `radio_drugUseFrequency` varchar(24) default '',
  `radio_wantHelpQuitDrug` varchar(24) default '',
  `commentsOnStreetDrugs` varchar(255) default '',
  `housingInterested` varchar(70) default '',
  `radio_wantAppmt` varchar(24) default '',
  `clientLastAddress` varchar(255) default '',
  `clientLastAddressPayRent` varchar(255) default '',
  `dateLivedThere` varchar(24) default '',
  `livedWithWhom` varchar(120) default '',
  `radio_livedInSubsidized` varchar(24) default '',
  `radio_owedRent` varchar(24) default '',
  `whereOweRent` varchar(255) default '',
  `amtOwing` varchar(16) default '',
  `commentsOnHousing` varchar(255) default '',
  `yearsOfEducation` varchar(4) default '',
  `cbox_haveHighSchool` char(1) default '',
  `cbox_haveCollege` char(1) default '',
  `cbox_haveUniversity` char(1) default '',
  `cbox_haveOther3` char(1) default '',
  `radio_interestBackToSchool` varchar(24) default '',
  `radio_requireReferralToESL` varchar(24) default '',
  `commentsOnEducation` varchar(255) default '',
  `radio_currentlyEmployed` varchar(24) default '',
  `howLongEmployed` char(3) default '',
  `howLongUnemployed` char(3) default '',
  `usualOccupation` varchar(70) default '',
  `radio_interestedInTraining` varchar(24) default '',
  `commentsOnEmployment` varchar(255) default '',
  `mainSourceOfIncome` varchar(70) default '',
  `cbox_OW` char(1) default '',
  `cbox_ODSP` char(1) default '',
  `cbox_employment` char(1) default '',
  `cbox_UI` char(1) default '',
  `cbox_OAS` char(1) default '',
  `cbox_CPP` char(1) default '',
  `cbox_other` char(1) default '',
  `howMuchYouReceive` varchar(12) default '',
  `radio_havePublicTrustee` varchar(24) default '',
  `publicTrusteeInfo` varchar(255) default '',
  `radio_entitledToOtherIncome` varchar(24) default '',
  `typeOfIncome` varchar(120) default '',
  `radio_everMadeAppforOtherIncome` varchar(24) default '',
  `everMadeAppforOtherIncome` varchar(120) default '',
  `whenMadeAppforOtherIncome` varchar(24) default '',
  `commentsOnFinance` varchar(255) default '',
  `radio_everBeenJailed` varchar(24) default '',
  `historyOfJail` varchar(50) default '',
  `radio_needAssistInLegal` varchar(24) default '',
  `needAssistInLegal` varchar(255) default '',
  `commentsOnLegalIssues` varchar(255) default '',
  `radio_citizen` varchar(24) default '',
  `radio_yourCanadianStatus` varchar(24) default '',
  `yourCanadianStatus` varchar(50) default '',
  `radio_sponsorshipBreakdown` varchar(24) default '',
  `whySponsorshipBreakdown` varchar(255) default '',
  `sponsorName` varchar(50) default '',
  `needHelpWithImmigration` varchar(50) default '',
  `commentsOnImmigration` varchar(255) default '',
  `radio_involvedOtherAgencies` varchar(24) default '',
  `agency1Name` varchar(70) default '',
  `contact1Name` varchar(50) default '',
  `contact1Phone` varchar(24) default '',
  `assistProvided1` varchar(120) default '',
  `dateLastContact1` varchar(24) default '',
  `agency2Name` varchar(70) default '',
  `contact2Name` varchar(50) default '',
  `contact2Phone` varchar(24) default '',
  `assistProvided2` varchar(120) default '',
  `dateLastContact2` varchar(24) default '',
  `agency3Name` varchar(70) default '',
  `contact3Name` varchar(50) default '',
  `contact3Phone` varchar(24) default '',
  `assistProvided3` varchar(120) default '',
  `dateLastContact3` varchar(24) default '',
  `agency4Name` varchar(70) default '',
  `contact4Name` varchar(50) default '',
  `contact4Phone` varchar(24) default '',
  `assistProvided4` varchar(120) default '',
  `dateLastContact4` varchar(24) default '',
  `radio_mentalIllness` varchar(24) default '',
  `radio_drinking` varchar(24) default '',
  `radio_drugUse` varchar(24) default '',
  `radio_healthProblem` varchar(24) default '',
  `radio_behaviorProblem` varchar(24) default '',
  `radio_need60DaysSeatonServices` varchar(24) default '',
  `completedBy1` varchar(50) default '',
  `completedBy2` varchar(50) default '',
  `assessCompleteTime` varchar(50) default '',
  `followupAppmts` varchar(120) default '',
  `cbox_pamphletIssued` char(1) default '',
  `cbox_hostel` char(1) default '',
  `cbox_rotaryClub` char(1) default '',
  `cbox_annexHarm` char(1) default '',
  `cbox_longTermProgram` char(1) default '',
  `cbox_birchmountResidence` char(1) default '',
  `cbox_oNeillHouse` char(1) default '',
  `cbox_fortYork` char(1) default '',
  `cbox_downsviewDells` char(1) default '',
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `formintakec`
--

DROP TABLE IF EXISTS `formintakec`;
CREATE TABLE `formintakec` (
  `ID` bigint(20) NOT NULL auto_increment,
  `cboxReferralByPolice` char(2) default NULL,
  `radioGender` varchar(16) default NULL,
  `cboxSocialServiceIssueOther` char(2) default NULL,
  `cboxBase2ndIncomeNone` char(2) default NULL,
  `cboxCurrHasSIn` char(2) default NULL,
  `cboxReferralByFredVictorCentreOther` char(2) default NULL,
  `cboxReferralByDetentionCenter` char(2) default NULL,
  `currDescriptionOfHousing` varchar(255) default NULL,
  `cboxBaseUseHealthBus` char(2) default NULL,
  `dateOfHospitalization` varchar(10) default NULL,
  `cboxHousingIssue` char(2) default NULL,
  `cboxBaseHasCommunity` char(2) default NULL,
  `cboxBaseHasFriends` char(2) default NULL,
  `cboxRelocationExit` char(2) default NULL,
  `cbox2ndPersonalityDisorder` char(2) default NULL,
  `cboxBase2ndIncomeSocialAssistance` char(2) default NULL,
  `cbox2ndMedicalMentalDisorder` char(2) default NULL,
  `cboxReferralByCriminalJusticeSystem` char(2) default NULL,
  `cboxThreatIssue` char(2) default NULL,
  `cboxSubstanceAnxietyDisorder` char(2) default NULL,
  `referralComment` varchar(255) default NULL,
  `cboxBase2ndIncomeEi` char(2) default NULL,
  `cboxCurrHasSupportUnknown` char(2) default NULL,
  `cboxReferralBySelf` char(2) default NULL,
  `cboxPhysicalIssueOther` char(2) default NULL,
  `cboxCurrLivingWithSpouse` char(2) default NULL,
  `cboxReferralByOtherInstitution` char(2) default NULL,
  `cboxPhysicalHospitalization` char(2) default NULL,
  `currWhyClientDoNotAccessSocialServices` varchar(255) default NULL,
  `cboxFinancialIssue` char(2) default NULL,
  `cboxIsolationIssueOther` char(2) default NULL,
  `cboxCurrLivingWithUnknown` char(2) default NULL,
  `cbox2ndIncomeOther` char(2) default NULL,
  `cboxCurrHasUnknown` char(2) default NULL,
  `cboxReferralByPhysician` char(2) default NULL,
  `cbox2ndDisassociativeDisorder` char(2) default NULL,
  `cboxImmigrationIssueOther` char(2) default NULL,
  `entryDate` varchar(10) default NULL,
  `admissionDate` varchar(10) default NULL,
  `cboxCurrHasCommunity` char(2) default NULL,
  `cboxDailyActivityIssue` char(2) default NULL,
  `cboxCurrLivingWithParents` char(2) default NULL,
  `cboxIdentificationIssueOther` char(2) default NULL,
  `radioBaseEmploymentStatus` varchar(16) default NULL,
  `radioCanadianBorn` varchar(16) default NULL,
  `cboxBaseLivingWithNonrelatives` char(2) default NULL,
  `cboxCurrIncomeMgmentDoNotNeedTrustee` char(2) default NULL,
  `cboxBase2ndIncomeFamily` char(2) default NULL,
  `radioCurrEmploymentStatus` varchar(16) default NULL,
  `cboxSuicideExit` char(2) default NULL,
  `cboxReferralByStreetNurseOther` char(2) default NULL,
  `dayOfBirth` char(2) default NULL,
  `cboxDualDisorder` char(2) default NULL,
  `cbox2ndIncomeODSp` char(2) default NULL,
  `cboxPsychiatricHospitalization` char(2) default NULL,
  `cbox2ndSomatoformDisorder` char(2) default NULL,
  `cboxPreAdmission` char(2) default NULL,
  `cboxReferralByStreetHealthReceptionOther` char(2) default NULL,
  `cbox2ndAnxietyDisorder` char(2) default NULL,
  `radioCurrParticipateInEduction` varchar(16) default NULL,
  `cbox2ndChildhoodDisorder` char(2) default NULL,
  `cboxCurrIncomeMgmentHasTrustee` char(2) default NULL,
  `radioCountryOfOrigin` varchar(16) default NULL,
  `cboxOtherChronicIllness` char(2) default NULL,
  `cboxMOHLTCDisorder` char(2) default NULL,
  `cboxPTSd` char(2) default NULL,
  `cboxBase2ndIncomeDisabilityAssistance` char(2) default NULL,
  `lengthOfHospitalization` varchar(25) default NULL,
  `radioCurrPrimaryIncomeSource` varchar(16) default NULL,
  `baseWhyClientDoNotAccessSocialServices` varchar(255) default NULL,
  `cbox2ndIncomeInformalOther` char(2) default NULL,
  `cbox2ndIncomeEi` char(2) default NULL,
  `cbox2ndGenderIdentityDisorder` char(2) default NULL,
  `cboxBaseDoNotAccessHealthCare` char(2) default NULL,
  `currSocialServiceClientAccesses` varchar(255) default NULL,
  `cbox2ndIncomeUnknown` char(2) default NULL,
  `radioBaseHealthCareAccess` varchar(16) default NULL,
  `cboxBase2ndIncomeODSp` char(2) default NULL,
  `cboxCurrUseWalkinClinic` char(2) default NULL,
  `cboxReferralByPsychiatrists` char(2) default NULL,
  `cboxReferralByMentalOrg` char(2) default NULL,
  `radioBasePrimaryResidenceType` varchar(16) default NULL,
  `cbox2ndIncomeDisabilityAssistance` char(2) default NULL,
  `cboxAddictionIssue` char(2) default NULL,
  `cboxBase2ndIncomePanhandlingOther` char(2) default NULL,
  `cboxCurrIncomeMgmentUnknown` char(2) default NULL,
  `cboxLegalIssue` char(2) default NULL,
  `radioResistTreatment` varchar(16) default NULL,
  `cbox2ndSchizophrenia` char(2) default NULL,
  `radioBaseSocialServiceAccess` varchar(16) default NULL,
  `radioCurrSocialServiceAccess` varchar(16) default NULL,
  `radioBaseResidenceStatus` varchar(16) default NULL,
  `cbox2ndIncomeEmployment` char(2) default NULL,
  `cbox2ndAnxietyDisorderFromSubstance` char(2) default NULL,
  `radioCurrPrimaryResidenceType` varchar(16) default NULL,
  `radioCurrPrimaryIncomeSourceOther` varchar(16) default NULL,
  `cboxReferralByProbation` char(2) default NULL,
  `radioCurrLegalStatus` varchar(16) default NULL,
  `cboxProblemsWithPolice2` char(2) default NULL,
  `formCreated` date default NULL,
  `cbox2ndIncomeSocialAssistance` char(2) default NULL,
  `cboxBaseUseHospitalEmergency` char(2) default NULL,
  `cboxReferralByPublic` char(2) default NULL,
  `cboxReferralByHospital` char(2) default NULL,
  `cboxBaseLivingWithSpousePlus` char(2) default NULL,
  `cboxWithdrawalExit` char(2) default NULL,
  `cboxCurrLivingWithSelf` char(2) default NULL,
  `cbox2ndCognitiveDisorder` char(2) default NULL,
  `cboxBaseIncomeMgmentUnknown` char(2) default NULL,
  `cboxBaseHasHealthCard` char(2) default NULL,
  `radioCurrHighestEductionLevel` varchar(16) default NULL,
  `cboxReferralByPsychiatricHospital` char(2) default NULL,
  `cboxCurrLivingWithNonrelatives` char(2) default NULL,
  `cboxFamilyLawIssues1` char(2) default NULL,
  `cboxProblemsWithPolice1` char(2) default NULL,
  `cboxBaseHasRegularHealthProvider` char(2) default NULL,
  `cboxFamilyLawIssues2` char(2) default NULL,
  `cboxCurrUseShelterClinic` char(2) default NULL,
  `clientNum` varchar(16) default NULL,
  `cbox2ndImpulsiveDisorder` char(2) default NULL,
  `radioIsAboriginal` varchar(16) default NULL,
  `baseSocialServiceClientAccesses` varchar(255) default NULL,
  `cboxBase2ndIncomeOther` char(2) default NULL,
  `cboxBaseIncomeMgmentNeedsTrusteeButDoNotWant` char(2) default NULL,
  `cbox2ndSleepDisorder` char(2) default NULL,
  `cboxBaseHasNonStatus` char(2) default NULL,
  `cboxHealthCareIssueOther` char(2) default NULL,
  `radioCurrHealthCareAccess` varchar(16) default NULL,
  `cboxBaseHasCertificate` char(2) default NULL,
  `cboxBaseLivingWithSelf` char(2) default NULL,
  `cboxCurrIncomeMgmentNeedsTrustee` char(2) default NULL,
  `cboxBase2ndIncomeUnknown` char(2) default NULL,
  `radioLanguageEnglish` varchar(16) default NULL,
  `yearArrivedInCanada` varchar(16) default NULL,
  `cboxDateOfBirthUnknown` char(2) default NULL,
  `cboxOtherIssue` char(2) default NULL,
  `cboxCurrHasHealthCard` char(2) default NULL,
  `cboxCurrHasSomeone` char(2) default NULL,
  `cboxCurrHasRegularHealthProvider` char(2) default NULL,
  `cbox2ndDevelopmentalDisorder` char(2) default NULL,
  `cboxBase2ndIncomeInformalOther` char(2) default NULL,
  `cboxOCd` char(2) default NULL,
  `cboxCurrUseHospitalEmergency` char(2) default NULL,
  `cboxBaseLivingWithUnknown` char(2) default NULL,
  `cbox2ndFactitiousDisorder` char(2) default NULL,
  `cboxBaseHasSomeone` char(2) default NULL,
  `cboxRelationalIssue` char(2) default NULL,
  `cboxBaseHasNativeCard` char(2) default NULL,
  `radioBaseLegalStatus` varchar(16) default NULL,
  `cbox2ndIncomeFamily` char(2) default NULL,
  `cboxCurrHasRelatives` char(2) default NULL,
  `cboxCurrHasCertificate` char(2) default NULL,
  `monthOfBirth` char(2) default NULL,
  `cboxBaseHasSIn` char(2) default NULL,
  `clientSurname` varchar(40) default NULL,
  `cbox2ndAnxietyDisorderOCd` char(2) default NULL,
  `staffName` varchar(60) default NULL,
  `cboxReferralByStreetIDWorkerOther` char(2) default NULL,
  `cbox2ndSubstanceDisorder` char(2) default NULL,
  `radioCurrNeedSocialServices` varchar(16) default NULL,
  `radioBaseHighestEductionLevel` varchar(16) default NULL,
  `cboxBaseHasSupportUnknown` char(2) default NULL,
  `radioCurrResidenceStatus` varchar(16) default NULL,
  `cboxBaseIncomeMgmentDoNotNeedTrustee` char(2) default NULL,
  `countryOfOrigin` varchar(60) default NULL,
  `cboxCurrHasFriends` char(2) default NULL,
  `radioBasePrimaryIncomeSource` varchar(16) default NULL,
  `cbox2ndAnxietyDisorderOther` char(2) default NULL,
  `cboxEducationalIssue` char(2) default NULL,
  `radioBasePrimaryIncomeSourceOther` varchar(16) default NULL,
  `radioBaseParticipateInEduction` varchar(16) default NULL,
  `cboxCurrLivingWithRelatives` char(2) default NULL,
  `baseDescriptionOfHousing` varchar(255) default NULL,
  `cboxReferralByCourt` char(2) default NULL,
  `cbox2ndIncomePanhandlingOther` char(2) default NULL,
  `cboxReferralByOtherAgency` char(2) default NULL,
  `cboxCaseFile` char(2) default NULL,
  `cboxCurrLivingWithChildren` char(2) default NULL,
  `cboxReferralByMentalHealthWorker` char(2) default NULL,
  `cboxCurrIncomeMgmentNeedsTrusteeButDoNotWant` char(2) default NULL,
  `clientFirstName` varchar(40) default NULL,
  `cboxBankingIssueOther` char(2) default NULL,
  `cboxBase2ndIncomeEmployment` char(2) default NULL,
  `cboxNAExit` char(2) default NULL,
  `cbox2ndIncomeNone` char(2) default NULL,
  `cboxNa` char(2) default NULL,
  `cboxBaseHasRelatives` char(2) default NULL,
  `cboxCurrHasNonStatus` char(2) default NULL,
  `cboxEmploymentIssue` char(2) default NULL,
  `yearOfBirth` varchar(4) default NULL,
  `cboxBase2ndIncomePension` char(2) default NULL,
  `cboxCurrLivingWithSpousePlus` char(2) default NULL,
  `cboxPreferredLanguageUnknown` char(2) default NULL,
  `cbox2ndAnxietyDisorderPSd` char(2) default NULL,
  `cboxSexualAbuseIssue` char(2) default NULL,
  `formEdited` datetime default NULL,
  `cboxBaseUseShelterClinic` char(2) default NULL,
  `cboxCurrAccessHealthCareUnknown` char(2) default NULL,
  `cbox2ndIncomePension` char(2) default NULL,
  `provider_no` bigint(20) default NULL,
  `cboxCurrDoNotAccessHealthCare` char(2) default NULL,
  `cboxCurrUseHealthBus` char(2) default NULL,
  `cboxBaseLivingWithParents` char(2) default NULL,
  `cboxBaseHasUnknown` char(2) default NULL,
  `radioBaseNeedSocialServices` varchar(16) default NULL,
  `cboxNoneListedIssue` char(2) default NULL,
  `cbox2ndUnknown` char(2) default NULL,
  `cboxReferralBySafeBeds` char(2) default NULL,
  `cbox2ndAdjustDisorder` char(2) default NULL,
  `cboxHospitalizationUnknown` char(2) default NULL,
  `cboxConcurrentDisorder` char(2) default NULL,
  `cbox2ndEatingDisorder` char(2) default NULL,
  `cboxCompleteWithReferral` char(2) default NULL,
  `cboxBaseUseWalkinClinic` char(2) default NULL,
  `monthlyProgressReport` varchar(255) default NULL,
  `cboxOtherAnxietyDisorder` char(2) default NULL,
  `cboxBaseIncomeMgmentHasTrustee` char(2) default NULL,
  `demographic_no` bigint(20) NOT NULL default '0',
  `cboxBaseIncomeMgmentNeedsTrustee` char(2) default NULL,
  `radioRaceCaucasian` varchar(16) default NULL,
  `cboxMentalIssue` char(2) default NULL,
  `preferredLanguage` varchar(30) default NULL,
  `cbox2ndMoodDisorder` char(2) default NULL,
  `radioPrimaryDiagnosis` varchar(16) default NULL,
  `cboxCurrHasNativeCard` char(2) default NULL,
  `cboxBaseLivingWithChildren` char(2) default NULL,
  `cboxBaseLivingWithSpouse` char(2) default NULL,
  `radioTreatmentOrders` varchar(16) default NULL,
  `cboxReferralByOtherPeople` char(2) default NULL,
  `cboxBaseAccessHealthCareUnknown` char(2) default NULL,
  `cboxCompleteWithoutReferral` char(2) default NULL,
  `cboxDeathExit` char(2) default NULL,
  `cboxBaseLivingWithRelatives` char(2) default NULL,
  `currAddress` varchar(255) default NULL,
  `currPhone` varchar(255) default NULL,
  `pastAddresses` text,
  `contactsInfo` text,
  `ids` text,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `functional_user_type`
--

DROP TABLE IF EXISTS `functional_user_type`;
CREATE TABLE `functional_user_type` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `pmm_log`
--

DROP TABLE IF EXISTS `pmm_log`;
CREATE TABLE `pmm_log` (
  `id` bigint(20) NOT NULL auto_increment,
  `provider_no` varchar(255) default NULL,
  `dateTime` datetime default NULL,
  `action` varchar(20) default NULL,
  `contentId` varchar(80) default NULL,
  `content` varchar(80) NOT NULL default '',
  `ip` varchar(30) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `program`
--

DROP TABLE IF EXISTS `program`;
CREATE TABLE `program` (
  `program_id` int(10) NOT NULL auto_increment,
  `agency_id` bigint(11) NOT NULL default '0',
  `name` varchar(70) NOT NULL default '',
  `descr` varchar(255) default NULL,
  `address` varchar(255) NOT NULL default '',
  `phone` varchar(255) NOT NULL default '',
  `fax` varchar(255) NOT NULL default '',
  `url` varchar(255) NOT NULL default '',
  `email` varchar(255) NOT NULL default '',
  `emergency_number` varchar(255) NOT NULL default '',
  `type` varchar(50) default NULL,
  `location` varchar(70) default NULL,
  `max_allowed` int(6) NOT NULL default '0',
  `num_of_members` int(6) NOT NULL default '0',
  `holding_tank` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`program_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `program_access`
--

DROP TABLE IF EXISTS `program_access`;
CREATE TABLE `program_access` (
  `id` bigint(20) NOT NULL auto_increment,
  `program_id` bigint(20) default NULL,
  `access_type_id` varchar(255) default NULL,
  `all_roles` tinyint(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKC385F77F9BCAF076` (`access_type_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `program_access_roles`
--

DROP TABLE IF EXISTS `program_access_roles`;
CREATE TABLE `program_access_roles` (
  `id` bigint(20) NOT NULL default '0',
  `role_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`id`,`role_id`),
  KEY `FK55BF0DFDBC1003F0` (`id`),
  KEY `FK55BF0DFD60605436` (`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `program_bedlog`
--

DROP TABLE IF EXISTS `program_bedlog`;
CREATE TABLE `program_bedlog` (
  `bedlog_id` bigint(20) NOT NULL auto_increment,
  `program_id` bigint(20) default NULL,
  `enabled` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`bedlog_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `program_bedlog_times`
--

DROP TABLE IF EXISTS `program_bedlog_times`;
CREATE TABLE `program_bedlog_times` (
  `bedlog_id` bigint(20) NOT NULL default '0',
  `check_time` varchar(255) default NULL,
  `position` int(11) default NULL,
  KEY `FK6F2C9105E471442B` (`bedlog_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `program_bedlog_statuses`
--

DROP TABLE IF EXISTS `program_bedlog_statuses`;
CREATE TABLE `program_bedlog_statuses` (
  `bedlog_id` bigint(20) NOT NULL default '0',
  `status` varchar(255) default NULL,
  `position` int(11) default NULL,
  KEY `FK3D9F5201E471442B` (`bedlog_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `program_functional_user`
--

DROP TABLE IF EXISTS `program_functional_user`;
CREATE TABLE `program_functional_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `program_id` bigint(20) default NULL,
  `user_type_id` bigint(20) default NULL,
  `provider_no` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK13B70CCFE0B12E7` (`user_type_id`),
  KEY `FK13B70CCE6DF0AD3` (`provider_no`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `program_provider`
--

DROP TABLE IF EXISTS `program_provider`;
CREATE TABLE `program_provider` (
  `id` bigint(20) NOT NULL auto_increment,
  `program_id` bigint(20) default NULL,
  `provider_no` bigint(20) default NULL,
  `role_id` bigint(20) default NULL,
  `team_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK3EFA2D4C60605436` (`role_id`),
  KEY `FK3EFA2D4CE6DF0AD3` (`provider_no`),
  KEY `FK3EFA2D4CDAA8624B` (`team_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `program_provider_team`
--

DROP TABLE IF EXISTS `program_provider_team`;
CREATE TABLE `program_provider_team` (
  `id` bigint(20) NOT NULL default '0',
  `elt` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`id`,`elt`),
  KEY `FK5E7B3D703D01BC3D` (`id`),
  KEY `FK5E7B3D703073B4BB` (`elt`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `program_queue`
--

DROP TABLE IF EXISTS `program_queue`;
CREATE TABLE `program_queue` (
  `queue_id` bigint(20) NOT NULL auto_increment,
  `agency_id` bigint(20) NOT NULL default '0',
  `client_id` bigint(20) NOT NULL default '0',
  `referral_date` datetime default NULL,
  `provider_no` bigint(20) NOT NULL default '0',
  `notes` varchar(255) default NULL,
  `program_id` bigint(20) NOT NULL default '0',
  `status` varchar(30) default NULL,
  `referral_id` bigint(20) default NULL,
  PRIMARY KEY  (`queue_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `program_team`
--

DROP TABLE IF EXISTS `program_team`;
CREATE TABLE `program_team` (
  `team_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `program_id` bigint(20) default NULL,
  PRIMARY KEY  (`team_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `rate_page`
--

DROP TABLE IF EXISTS `rate_page`;
CREATE TABLE `rate_page` (
  `id` int(11) NOT NULL auto_increment,
  `score` int(11) NOT NULL default '0',
  `visitors` int(11) NOT NULL default '0',
  `page_name` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


INSERT INTO `agency` VALUES (1,'Default Agency','Default Agency','Marc Dumontier','marc@mdumontier.com','416-837-5078',1,1,'http://localhost:8081/integrator',NULL,'marc','1234');
