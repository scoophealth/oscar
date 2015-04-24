--
-- Table structure for table `access_type`
--

CREATE TABLE `access_type` (
  `access_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  PRIMARY KEY  (`access_id`)
);

--
-- Table structure for table `admission`
--

CREATE TABLE `admission` (
  `am_id` bigint(11) NOT NULL auto_increment,
  `client_id` bigint(11) NOT NULL default '0',
  `program_id` bigint(11) NOT NULL default '0',
  `provider_no` varchar(6) NOT NULL default '0',
  `admission_date` datetime default NULL,
	admission_from_transfer tinyint(1) not null,
	`admission_notes` text default NULL,
  `temp_admission` char(1) default NULL,
  `discharge_date` datetime default NULL,
	discharge_from_transfer tinyint(1) not null,
  `discharge_notes` text default NULL,
  `temp_admit_discharge` char(1) default NULL,
  `admission_status` varchar(24) default NULL,
  `team_id` int(10) default NULL,
  `temporary_admission_flag` tinyint(1) default NULL,
  `radioDischargeReason` varchar(10) default '0',
  `clientstatus_id` bigint(20) DEFAULT NULL,
  `automatic_discharge` tinyint(1) default 0,
  lastUpdateDate datetime not null,
  PRIMARY KEY  (`am_id`),
  KEY `FK1A21809DAA8624B` (`team_id`),
	index (program_id),
	index (client_id)
);

--
-- Table structure for table `agency`
--
CREATE TABLE `agency` (
  `id` bigint(20) NOT NULL default '0',  
  `intake_quick` integer unsigned NOT NULL DEFAULT 1,
  `intake_quick_state` char(3) NOT NULL,
  `intake_indepth` integer unsigned DEFAULT 2,
  `intake_indepth_state` CHAR(3) NOT NULL,
  PRIMARY KEY  (`id`)
);

--
-- Table structure for table `bed`
--

CREATE TABLE `bed` (
  `bed_id` int(10) unsigned NOT NULL auto_increment,
  `bed_type_id` int(10) unsigned NOT NULL default '1',
  `room_id` int(10) unsigned default NULL,
  `facility_id` int unsigned NOT NULL default 0,
  `room_start` date NOT NULL,
  `team_id` int(10) unsigned default NULL,
  `name` varchar(45) NOT NULL,
  `active` tinyint(1) unsigned NOT NULL default '1',
  PRIMARY KEY  (`bed_id`)
);


--
-- Table structure for table `bed_check_time`
--
CREATE TABLE `bed_check_time` (
  `bed_check_time_id` int(10) unsigned NOT NULL auto_increment,
  `program_id` int(10) unsigned NOT NULL,
  `bed_check_time` time NOT NULL,
  PRIMARY KEY  (`bed_check_time_id`),
  UNIQUE KEY `idx_program_time` USING BTREE (`program_id`,`bed_check_time`)
);

--
-- Table structure for table `bed_demographic`
--

CREATE TABLE  `bed_demographic` (
  `bed_id` int(10) unsigned NOT NULL,
  `demographic_no` int(10) unsigned NOT NULL,
  `bed_demographic_status_id` int(10) unsigned NOT NULL default '1',
  `provider_no` varchar(6) NOT NULL,
  `late_pass` tinyint(1) unsigned NOT NULL default '0',
  `reservation_start` date NOT NULL,
  `reservation_end` date NOT NULL,
  PRIMARY KEY  (`bed_id`,`demographic_no`),
  UNIQUE KEY `idx_bed` (`bed_id`),
  UNIQUE KEY `idx_demographic` (`demographic_no`)
);

--
-- Table structure for table `bed_demographic_historical`
--

CREATE TABLE `bed_demographic_historical` (
  `bed_id` int(10) unsigned NOT NULL,
  `demographic_no` int(10) unsigned NOT NULL,
  `usage_start` date NOT NULL,
  `usage_end` date NOT NULL,
  PRIMARY KEY  (`bed_id`,`demographic_no`,`usage_start`)
);

--
-- Table structure for table `bed_demographic_status`
--

CREATE TABLE `bed_demographic_status` (
  `bed_demographic_status_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL,
  `duration` int(10) unsigned NOT NULL default '0',
  `dflt` tinyint(1) unsigned NOT NULL default '0',
  PRIMARY KEY  (`bed_demographic_status_id`)
);

--
-- Table structure for table `bed_type`
--

CREATE TABLE `bed_type` (
  `bed_type_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL,
  `dflt` tinyint(1) unsigned NOT NULL default '0',
  PRIMARY KEY  (`bed_type_id`)
);

--
-- Table structure for table `caisi_form`
--

CREATE TABLE `caisi_form` (
  `form_id` bigint(20) NOT NULL auto_increment,
  facilityId int,
  `description` varchar(255) default NULL,
  `surveyData` text,
  `status` smallint(6) default NULL,
  `version` bigint(20) default '0',
  PRIMARY KEY  (`form_id`)
);

--
-- Table structure for table `caisi_form_data`
--

CREATE TABLE `caisi_form_data` (
  `id` bigint(20) NOT NULL auto_increment,
  `instance_id` bigint(20) default NULL,
  `page_number` bigint(20) default NULL,
  `section_id` bigint(20) default NULL,
  `question_id` bigint(20) default NULL,
  `value` text default NULL,
  `data_key` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKC253B2E74497F4E` (`instance_id`)
);

--
-- Table structure for table `caisi_form_instance`
--

CREATE TABLE `caisi_form_instance` (
  `id` bigint(20) NOT NULL auto_increment,
  `form_id` bigint(20) default NULL,
  `description` varchar(255) default NULL,
  `date_created` datetime default NULL,
  `user_id` bigint(20) default NULL,
  `username` varchar(255) default NULL,
  `client_id` bigint(20) default NULL,
  program_id int,
  PRIMARY KEY  (`id`)
);


CREATE TABLE `caisi_form_data_tmpsave` (
  `tmp_form_data_id` bigint(20) NOT NULL auto_increment,
  `tmp_instance_id` bigint(20) default NULL,
  `page_number` bigint(20) default NULL,
  `section_id` bigint(20) default NULL,
  `question_id` bigint(20) default NULL,
  `value` text default NULL,
  `data_key` varchar(255) default NULL,
  PRIMARY KEY  (`tmp_form_data_id`),
  KEY `caisi_form_data_tmpsave_key1` (`tmp_instance_id`)
--  KEY `FKC253B2E74497F4F` (`instance_id`)
);

--
-- Table structure for table `caisi_form_instance`
--
CREATE TABLE `caisi_form_instance_tmpsave` (
  `tmp_instance_id` bigint(20) NOT NULL auto_increment,
  `instance_id` bigint(20) NOT NULL,
  `form_id` bigint(20) default NULL,
  `description` varchar(255) default NULL,
  `date_created` datetime default NULL,
  `user_id` bigint(20) default NULL,
  `username` varchar(255) default NULL,
  `client_id` bigint(20) default NULL,
  program_id int,
  PRIMARY KEY  (`tmp_instance_id`)
);


CREATE TABLE `caisi_form_question` (
  `id` bigint(20) NOT NULL auto_increment,
  `page` bigint(20),
  `section` bigint(20),
  `question` bigint(20),
  `description` varchar(255) default NULL,
  `form_id` bigint(20),
  `form_question_id` bigint(20),
  `type` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
);


--
-- Table structure for table `caisi_role`
--

CREATE TABLE `caisi_role` (
  `role_id` int(10) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  userDefined tinyint not null,
  `oscar_name` varchar(255) NOT NULL default '',
  `update_date` datetime NOT NULL,
  PRIMARY KEY  (`role_id`)
);

--
-- Table structure for table `casemgmt_cpp`
--

CREATE TABLE `casemgmt_cpp` (
  `id` int(10) NOT NULL auto_increment,
  `demographic_no` varchar(10) NOT NULL default '',
  `provider_no` varchar(6) NOT NULL,
  `socialHistory` text,
  `familyHistory` text,
  `medicalHistory` text,
  `ongoingConcerns` text,
  `reminders` text,
  `update_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `primaryPhysician` varchar(255) default NULL,
  `primaryCounsellor` varchar(255) default NULL,
  `otherFileNumber` varchar(100) default null,
  `otherSupportSystems` text default null,
  `pastMedications` text default null,  
  PRIMARY KEY  (`id`)
);

--
-- Table structure for table `casemgmt_issue`
--

CREATE TABLE `casemgmt_issue` (
  `id` int(10) NOT NULL auto_increment,
  `demographic_no` varchar(20) NOT NULL default '',
  `issue_id` int(10) NOT NULL default '0',
  `acute` tinyint(1) NOT NULL default '0',
  `certain` tinyint(1) NOT NULL default '0',
  `major` tinyint(1) NOT NULL default '0',
  `resolved` tinyint(1) NOT NULL default '0',
  program_id int,
  `type` varchar(100) NOT NULL default '',
  `update_date` datetime NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK718D130B49CFB32F` (`issue_id`)
);

--
-- Table structure for table `casemgmt_issue_notes`
--

CREATE TABLE `casemgmt_issue_notes` (
  `id` int(10) NOT NULL default '0',
  `note_id` int(10) NOT NULL default '0',
  index(`id`),
  index(`note_id`)
);

--
-- Table structure for table `casemgmt_note`
--

CREATE TABLE `casemgmt_note` (
  `note_id` int(10) NOT NULL auto_increment,
  `update_date` datetime NOT NULL default '0000-00-00 00:00:00',
  `observation_date` datetime NOT NULL default '0000-00-00 00:00:00',
  `demographic_no` int(10) NOT NULL default '0',
  `provider_no` varchar(20) NOT NULL default '',
  `note` mediumtext NOT NULL,
  `signed` tinyint(1) NOT NULL default '0',
  `include_issue_innote` tinyint(1) NOT NULL default '0',
  `signing_provider_no` varchar(20) NOT NULL default '',
  `encounter_type` varchar(100) NOT NULL default '',
  `billing_code` varchar(100) NOT NULL default '',
  `program_no` varchar(20) NOT NULL default '',
  `reporter_caisi_role` varchar(20) NOT NULL default '',
  `reporter_program_team` varchar(20) NOT NULL default '',
  `history` mediumtext NOT NULL,
  `password` varchar(255) default NULL,
  `locked` char(1) default NULL,
  `archived` boolean default false,
  `position` int(10) default 0,
  `uuid` char(36) default NULL,
  `appointmentNo` int(10),
  `hourOfEncounterTime` int,
  `minuteOfEncounterTime` int,
  `hourOfEncTransportationTime` int,
  `minuteOfEncTransportationTime` int,
  PRIMARY KEY  (`note_id`),
  KEY `FKA8D537806CCA0FC` (`provider_no`),
	index(demographic_no),
	index(uuid),
	index(program_no),
	index(observation_date) 
);

--
-- Table structure for table `casemgmt_tmpsave`
--

CREATE TABLE `casemgmt_tmpsave` (
  `id` bigint(20) NOT NULL auto_increment,
  `demographic_no` bigint(20) default NULL,
  `provider_no` varchar(255) default NULL,
  `program_id` bigint(20) default NULL,
  `note` text,
  `update_date` datetime default NULL,
  `note_id` int(10),
  PRIMARY KEY  (`id`)
);

--
-- Table structure for table `client_image`
--

CREATE TABLE `client_image` (
  `image_id` bigint(20) NOT NULL auto_increment,
  `demographic_no` int(11) default NULL,
  `image_type` varchar(255) default NULL,
  `image_data` longtext,
  `update_date` datetime default NULL,
  `contents` longblob,
  PRIMARY KEY  (`image_id`)
);

--
-- Table structure for table `client_referral`
--

CREATE TABLE `client_referral` (
  `referral_id` bigint(20) NOT NULL auto_increment,
  `client_id` bigint(20) NOT NULL default '0',
  `facility_id` int NOT NULL default '0',
  `referral_date` datetime default NULL,
  `provider_no` varchar(6) NOT NULL,
  `notes` text default NULL,
  `select_vacancy` varchar(255), 
  `vacancy_id` int(11),
  `program_id` bigint(20) NOT NULL default '0',
  `status` varchar(30) default NULL,
  `completion_notes` text default NULL,
  `temporary_admission_flag` tinyint(1) default NULL,
  `completion_date` datetime default NULL,
  `present_problems` text default NULL,
  `radioRejectionReason` varchar(10) default '0',
  PRIMARY KEY  (`referral_id`)
);


CREATE TABLE `cr_cert` (
  `cert_id` varchar(37) NOT NULL default '',
  `user_specific` tinyint(1) default NULL,
  `static_ip` tinyint(1) default NULL,
  `unassigned` tinyint(1) default NULL,
  `ip` varchar(15) default NULL,
  `user_id` varchar(64) default NULL,
  `machine_id` varchar(37) default NULL,
  `verification_needed` tinyint(1) default NULL,
  `usage_times_before_reverify` int(11) default NULL,
  `policy_id` varchar(37) default NULL,
  `created_timestamp` datetime default NULL,
  `last_changed` datetime default NULL,
  `signature` bigint(20) default NULL,
  PRIMARY KEY  (`cert_id`)
);

--
-- Table structure for table `cr_machine`
--

CREATE TABLE `cr_machine` (
  `machine_id` varchar(37) NOT NULL default '',
  `ip` varchar(15) default NULL,
  `machine_name` varchar(255) default NULL,
  PRIMARY KEY  (`machine_id`)
);

--
-- Table structure for table `cr_policy`
--

CREATE TABLE `cr_policy` (
  `policy_id` varchar(37) NOT NULL default '',
  `static_ip` tinyint(1) default NULL,
  `ip` varchar(15) default NULL,
  `remote_access` tinyint(1) default NULL,
  `generate_super_certs` tinyint(1) default NULL,
  `administrate_policies` tinyint(1) default NULL,
  `administrate_questions` tinyint(1) default NULL,
  `remove_bans` tinyint(1) default NULL,
  `user_id` varchar(64) default NULL,
  `role_id` varchar(37) default NULL,
  `priority` int(11) default NULL,
  `usage_times_before_reverify` int(11) default NULL,
  `max_time_between_usage` int(11) default NULL,
  `expire_cookie` int(11) default NULL,
  `ip_filter` varchar(128) default NULL,
  `certs_max` int(11) default NULL,
  `certs_current` int(11) default NULL,
  `default_answer` varchar(16) default NULL,
  PRIMARY KEY  (`policy_id`)
);

--
-- Table structure for table `cr_securityquestion`
--

CREATE TABLE `cr_securityquestion` (
  `question_id` varchar(37) NOT NULL default '',
  `user_id` varchar(128) default NULL,
  `question` varchar(255) default NULL,
  `answer` varchar(255) default NULL,
  PRIMARY KEY  (`question_id`)
);

--
-- Table structure for table `cr_user`
--

CREATE TABLE `cr_user` (
  `user_id` varchar(64) NOT NULL default '',
  `password_digest` varchar(128) default NULL,
  `disabled` tinyint(1) default NULL,
  `lockedout` tinyint(1) default NULL,
  `password_expired` tinyint(1) default NULL,
  PRIMARY KEY  (`user_id`)
);

--
-- Table structure for table `cr_userrole`
--

CREATE TABLE `cr_userrole` (
  `user_id` varchar(64) NOT NULL default '',
  `user_role` varchar(64) NOT NULL default '',
  PRIMARY KEY  (`user_id`,`user_role`)
);

--
-- Table structure for table `custom_filter`
--

CREATE TABLE `custom_filter` (
  `id` int(10) NOT NULL auto_increment,
  `provider_no` varchar(6) NOT NULL default '',
  `start_date` date NOT NULL default '0000-00-00',
  `end_date` date NOT NULL default '0000-00-00',
  `status` char(1) NOT NULL default '',
  `priority` varchar(20) NOT NULL default '',
  `demographic_no` varchar(20) NOT NULL default '',
  `programId` varchar(10) default '',
  `name` varchar(255) NOT NULL default '',
  `shortcut` tinyint(1) default '0',
  `message` varchar(255),
  PRIMARY KEY  (`id`)
);

--
-- Table structure for table `custom_filter_assignees`
--

CREATE TABLE `custom_filter_assignees` (
  `filter_id` int(10) NOT NULL default '0',
  `provider_no` varchar(6) NOT NULL default ''
);

--
-- Table structure for table `custom_filter_providers`
--

CREATE TABLE `custom_filter_providers` (
  `filter_id` int(10) NOT NULL default '0',
  `provider_no` varchar(6) NOT NULL default ''
);

--
-- Table structure for table `default_role_access`
--

CREATE TABLE `default_role_access` (
  `id` bigint(20) NOT NULL auto_increment,
  `role_id` int(11) NOT NULL default '0',
  `access_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`id`)
);

--
-- Table structure for table `formfollowup`
--

CREATE TABLE `formfollowup` (
  `ID` bigint(20) NOT NULL auto_increment,
  `cbox_assistWithHealthCard` char(1) default NULL,
  `commentsOnEducation` varchar(255) default NULL,
  `typeOfIncome` varchar(120) default NULL,
  `historyOfJail` varchar(50) default NULL,
  `dateEnteredSeaton` varchar(24) default NULL,
  `commentsOnStreetDrugs` varchar(255) default NULL,
  `whenMadeAppforOtherIncome` varchar(24) default NULL,
  `cbox_ODSP` char(1) default NULL,
  `cbox_fortYork` char(1) default NULL,
  `cbox_oNeillHouse` char(1) default NULL,
  `cbox_other` char(1) default NULL,
  `cbox_haveOther1` char(1) default NULL,
  `radio_drinkThese` varchar(24) default NULL,
  `cbox_OAS` char(1) default NULL,
  `whySponsorshipBreakdown` varchar(255) default NULL,
  `radio_everBeenJailed` varchar(24) default NULL,
  `doctor2Phone` varchar(24) default NULL,
  `radio_caredForDepression` varchar(24) default NULL,
  `radio_caredForOther` varchar(24) default NULL,
  `cbox_getMoreMedication` char(1) default NULL,
  `radio_drugUse` varchar(24) default NULL,
  `radio_haveMentalProblem` varchar(24) default NULL,
  `radio_requireReferralToESL` varchar(24) default NULL,
  `radio_haveHealthCoverage` varchar(24) default NULL,
  `day` char(2) default NULL,
  `cbox_speakFrench` char(1) default NULL,
  `commentsOnLegalIssues` varchar(255) default NULL,
  `radio_howMuchDrink` varchar(24) default NULL,
  `radio_needAssistInLegal` varchar(24) default NULL,
  `commentsOnHousing` varchar(255) default NULL,
  `cbox_downsviewDells` char(1) default NULL,
  `cbox_haveUniversity` char(1) default NULL,
  `publicTrusteeInfo` varchar(255) default NULL,
  `cbox_longTermProgram` char(1) default NULL,
  `dateAssessment` varchar(24) default NULL,
  `cbox_assistWithSINCard` char(1) default NULL,
  `drinksPerDay` char(3) default NULL,
  `speakOther` varchar(50) default NULL,
  `cbox_rememberToTakeMedication` char(1) default NULL,
  `dateLastContact3` varchar(24) default NULL,
  `usualOccupation` varchar(70) default NULL,
  `radio_wantAppmt` varchar(24) default NULL,
  `radio_hasIDInFile` varchar(24) default NULL,
  `drinksPerMonth` varchar(5) default NULL,
  `sponsorName` varchar(50) default NULL,
  `cbox_pamphletIssued` char(1) default NULL,
  `dateLastContact1` varchar(24) default NULL,
  `cbox_OW` char(1) default NULL,
  `assessCompleteTime` varchar(50) default NULL,
  `cbox_birchmountResidence` char(1) default NULL,
  `contact4Name` varchar(50) default NULL,
  `clientLastAddressPayRent` varchar(255) default NULL,
  `dateLastContact2` varchar(24) default NULL,
  `contact2Name` varchar(50) default NULL,
  `radio_currentlyEmployed` varchar(24) default NULL,
  `commentsOnFinance` varchar(255) default NULL,
  `cbox_haveODSP` char(1) default NULL,
  `agency3Name` varchar(70) default NULL,
  `amtOwing` varchar(16) default NULL,
  `cbox_rotaryClub` char(1) default NULL,
  `clientSurname` varchar(50) default NULL,
  `completedBy1` varchar(50) default NULL,
  `commentsOnEmployment` varchar(255) default NULL,
  `radio_entitledToOtherIncome` varchar(24) default NULL,
  `dateLastContact4` varchar(24) default NULL,
  `whereBeforeSeaton` varchar(255) default NULL,
  `doctor2NameAddr` varchar(255) default NULL,
  `radio_sponsorshipBreakdown` varchar(24) default NULL,
  `cbox_assistWithBirthCert` char(1) default NULL,
  `howHearAboutSeaton` varchar(255) default NULL,
  `howMuchYouReceive` varchar(12) default NULL,
  `everMadeAppforOtherIncome` varchar(120) default NULL,
  `contact4Phone` varchar(24) default NULL,
  `assistProvided1` varchar(120) default NULL,
  `cbox_employment` char(1) default NULL,
  `yourCanadianStatus` varchar(50) default NULL,
  `radio_everMadeAppforOtherIncome` varchar(24) default NULL,
  `cbox_haveSchizophrenia` char(1) default NULL,
  `commentsOnID` varchar(255) default NULL,
  `cbox_haveOther2` char(1) default NULL,
  `radio_needAssistWithMedication` varchar(24) default NULL,
  `assessStartTime` varchar(24) default NULL,
  `cbox_haveDepression` char(1) default NULL,
  `cbox_assistWithImmigrant` char(1) default NULL,
  `clientLastAddress` varchar(255) default NULL,
  `mainSourceOfIncome` varchar(70) default NULL,
  `cbox_assistWithNone` char(1) default NULL,
  `dateExitedSeaton` varchar(24) default NULL,
  `cbox_haveOHIP` char(1) default NULL,
  `housingInterested` varchar(70) default NULL,
  `assistWithOther` varchar(50) default NULL,
  `needHelpWithImmigration` varchar(50) default NULL,
  `cbox_speakEnglish` char(1) default NULL,
  `cbox_assistWithOther` char(1) default NULL,
  `contact2Phone` varchar(24) default NULL,
  `cbox_haveCollege` char(1) default NULL,
  `dateLivedThere` varchar(24) default NULL,
  `radio_involvedOtherAgencies` varchar(24) default NULL,
  `assistProvided2` varchar(120) default NULL,
  `radio_wantHelpQuitDrug` varchar(24) default NULL,
  `contact1Phone` varchar(24) default NULL,
  `radio_need60DaysSeatonServices` varchar(24) default NULL,
  `completedBy2` varchar(50) default NULL,
  `radio_interestedInTraining` varchar(24) default NULL,
  `contact3Phone` varchar(24) default NULL,
  `followupAppmts` varchar(120) default NULL,
  `cbox_haveAnxiety` char(1) default NULL,
  `radio_interestBackToSchool` varchar(24) default NULL,
  `cbox_assistWithCitizenCard` char(1) default NULL,
  `cbox_haveHighSchool` char(1) default NULL,
  `cbox_haveOther3` char(1) default NULL,
  `radio_citizen` varchar(24) default NULL,
  `clientFirstName` varchar(50) default NULL,
  `month` char(2) default NULL,
  `cbox_CPP` char(1) default NULL,
  `radio_seenDoctorRegAlcohol` varchar(24) default NULL,
  `radio_wantHelpQuit` varchar(24) default NULL,
  `radio_owedRent` varchar(24) default NULL,
  `whereOweRent` varchar(255) default NULL,
  `howLongUnemployed` char(3) default NULL,
  `cbox_haveODB` char(1) default NULL,
  `howLongEmployed` char(3) default NULL,
  `cbox_annexHarm` char(1) default NULL,
  `formEdited` datetime default NULL,
  `dateLastDoctor2Contact` varchar(24) default NULL,
  `yearsOfEducation` varchar(4) default NULL,
  `radio_yourCanadianStatus` varchar(24) default NULL,
  `radio_drugUseFrequency` varchar(24) default NULL,
  `provider_no` bigint(20) default NULL,
  `doctor1Phone` varchar(24) default NULL,
  `agency1Name` varchar(70) default NULL,
  `cbox_hostel` char(1) default NULL,
  `commentsOnAlcohol` varchar(255) default NULL,
  `radio_mentalIllness` varchar(24) default NULL,
  `cbox_speakSpanish` char(1) default NULL,
  `drinksPerWeek` varchar(4) default NULL,
  `commentsOnImmigration` varchar(255) default NULL,
  `radio_behaviorProblem` varchar(24) default NULL,
  `agency2Name` varchar(70) default NULL,
  `commentsOnNeedHelp` varchar(255) default NULL,
  `radio_havePublicTrustee` varchar(24) default NULL,
  `cbox_speakOther` char(1) default NULL,
  `cbox_takePrescribedMedication` char(1) default NULL,
  `needAssistInLegal` varchar(255) default NULL,
  `radio_caredForSchizophrenia` varchar(24) default NULL,
  `formCreated` date default NULL,
  `demographic_no` bigint(20) NOT NULL default '0',
  `radio_doYouDrink` varchar(24) default NULL,
  `radio_drinking` varchar(24) default NULL,
  `radio_healthProblem` varchar(24) default NULL,
  `cbox_UI` char(1) default NULL,
  `livedWithWhom` varchar(120) default NULL,
  `assistProvided3` varchar(120) default NULL,
  `cbox_haveManic` char(1) default NULL,
  `contact1Name` varchar(50) default NULL,
  `year` varchar(4) default NULL,
  `radio_caredForManic` varchar(24) default NULL,
  `cbox_assistWithRefugee` char(1) default NULL,
  `radio_livedInSubsidized` varchar(24) default NULL,
  `radio_useDrugs` varchar(24) default NULL,
  `cbox_needHelpInOther` char(1) default NULL,
  `assistProvided4` varchar(120) default NULL,
  `doctor1NameAddr` varchar(255) default NULL,
  `contact3Name` varchar(50) default NULL,
  `haveOther` varchar(70) default NULL,
  `cbox_storeMedication` char(1) default NULL,
  `agency4Name` varchar(70) default NULL,
  `radio_caredForAnxiety` varchar(24) default NULL,
  `dateLastDoctor1Contact` varchar(24) default NULL,
  PRIMARY KEY  (`ID`)
);

--
-- Table structure for table `formreceptionassessment`
--

CREATE TABLE `formreceptionassessment` (
  `ID` int(10) NOT NULL auto_increment,
  `demographic_no` int(10) NOT NULL default '0',
  `provider_no` int(10) default NULL,
  `formCreated` date default NULL,
  `formEdited` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `assessDate` varchar(24) default NULL,
  `assessStartTime` varchar(24) default NULL,
  `enterSeatonDate` varchar(24) default NULL,
  `cbox_newClient` char(1) default NULL,
  `cbox_dateOfReadmission` char(1) default NULL,
  `dateOfReadmission` varchar(24) default NULL,
  `clientSurname` varchar(50) default NULL,
  `clientFirstName` varchar(50) default NULL,
  `month` char(2) default NULL,
  `day` char(2) default NULL,
  `year` varchar(4) default NULL,
  `cbox_speakEnglish` char(1) default NULL,
  `cbox_speakFrench` char(1) default NULL,
  `cbox_speakOther` char(1) default NULL,
  `speakOther` varchar(36) default NULL,
  `reasonToSeaton` varchar(255) default NULL,
  `datesAtSeaton` varchar(70) default NULL,
  `cbox_assistInHealth` char(1) default NULL,
  `cbox_assistInIdentification` char(1) default NULL,
  `cbox_assistInAddictions` char(1) default NULL,
  `cbox_assistInHousing` char(1) default NULL,
  `cbox_assistInEducation` char(1) default NULL,
  `cbox_assistInEmployment` char(1) default NULL,
  `cbox_assistInFinance` char(1) default NULL,
  `cbox_assistInLegal` char(1) default NULL,
  `cbox_assistInImmigration` char(1) default NULL,
  `cbox_noID` char(1) default NULL,
  `cbox_sinCard` char(1) default NULL,
  `cbox_healthCard` char(1) default NULL,
  `healthCardNum` varchar(24) default NULL,
  `cbox_birthCertificate` char(1) default NULL,
  `cbox_citzenshipCard` char(1) default NULL,
  `cbox_immigrant` char(1) default NULL,
  `cbox_refugee` char(1) default NULL,
  `cbox_otherID` char(1) default NULL,
  `otherIdentification` varchar(70) default NULL,
  `cbox_idFiled` char(1) default NULL,
  `cbox_idNone` char(1) default NULL,
  `commentsOnID` varchar(255) default NULL,
  `cbox_OW` char(1) default NULL,
  `cbox_ODSP` char(1) default NULL,
  `cbox_WSIB` char(1) default NULL,
  `cbox_Employment` char(1) default NULL,
  `cbox_EI` char(1) default NULL,
  `cbox_OAS` char(1) default NULL,
  `cbox_CPP` char(1) default NULL,
  `cbox_OtherIncome` char(1) default NULL,
  `radio_onlineCheck` varchar(36) default NULL,
  `radio_active` varchar(36) default NULL,
  `cbox_noRecord` char(1) default NULL,
  `lastIssueDate` varchar(24) default NULL,
  `office` varchar(50) default NULL,
  `workerNum` varchar(36) default NULL,
  `amtReceived` varchar(9) default NULL,
  `radio_hasDoctor` varchar(36) default NULL,
  `doctorName` varchar(50) default NULL,
  `doctorPhone` varchar(24) default NULL,
  `doctorPhoneExt` varchar(8) default NULL,
  `doctorAddress` varchar(120) default NULL,
  `radio_seeDoctor` varchar(36) default NULL,
  `radio_healthIssue` varchar(36) default NULL,
  `healthIssueDetails` varchar(255) default NULL,
  `cbox_hasDiabetes` char(1) default NULL,
  `cbox_insulin` char(1) default NULL,
  `cbox_epilepsy` char(1) default NULL,
  `cbox_bleeding` char(1) default NULL,
  `cbox_hearingImpair` char(1) default NULL,
  `cbox_visualImpair` char(1) default NULL,
  `cbox_mobilityImpair` char(1) default NULL,
  `mobilityImpair` varchar(255) default NULL,
  `radio_otherHealthConcern` varchar(36) default NULL,
  `otherHealthConerns` varchar(255) default NULL,
  `radio_takeMedication` varchar(36) default NULL,
  `namesOfMedication` varchar(120) default NULL,
  `radio_helpObtainMedication` varchar(36) default NULL,
  `helpObtainMedication` varchar(255) default NULL,
  `radio_allergicToMedication` varchar(36) default NULL,
  `allergicToMedicationName` varchar(255) default NULL,
  `reactionToMedication` varchar(255) default NULL,
  `radio_mentalHealthConcerns` varchar(36) default NULL,
  `mentalHealthConcerns` varchar(255) default NULL,
  `frequencyOfSeeingDoctor` varchar(8) default NULL,
  `cbox_visitWalkInClinic` char(1) default NULL,
  `cbox_visitHealthCenter` char(1) default NULL,
  `cbox_visitEmergencyRoom` char(1) default NULL,
  `cbox_visitOthers` char(1) default NULL,
  `cbox_visitHealthOffice` char(1) default NULL,
  `radio_seeSameDoctor` varchar(36) default NULL,
  `frequencyOfSeeingEmergencyRoomDoctor` varchar(8) default NULL,
  `radio_didNotReceiveHealthCare` varchar(36) default NULL,
  `cbox_treatPhysicalHealth` char(1) default NULL,
  `cbox_treatMentalHealth` char(1) default NULL,
  `cbox_regularCheckup` char(1) default NULL,
  `cbox_treatOtherReasons` char(1) default NULL,
  `treatOtherReasons` varchar(255) default NULL,
  `cbox_treatInjury` char(1) default NULL,
  `cbox_goToWalkInClinic` char(1) default NULL,
  `cbox_goToHealthCenter` char(1) default NULL,
  `cbox_goToEmergencyRoom` char(1) default NULL,
  `cbox_goToOthers` char(1) default NULL,
  `goToOthers` varchar(255) default NULL,
  `cbox_HealthOffice` char(1) default NULL,
  `radio_appmtSeeDoctorIn3Mths` varchar(36) default NULL,
  `radio_needRegularDoctor` varchar(36) default NULL,
  `radio_objectToRegularDoctorIn4Wks` varchar(36) default NULL,
  `radio_rateOverallHealth` varchar(36) default NULL,
  `radio_speakToResearcher` varchar(36) default NULL,
  `contactName` varchar(70) default NULL,
  `contactPhone` varchar(24) default NULL,
  `contactAddress` varchar(255) default NULL,
  `contactRelationship` varchar(120) default NULL,
  `radio_hasMentalIllness` varchar(36) default NULL,
  `radio_hasDrinkingProblem` varchar(36) default NULL,
  `radio_hasDrugProblem` varchar(36) default NULL,
  `radio_hasHealthProblem` varchar(36) default NULL,
  `radio_hasBehaviorProblem` varchar(36) default NULL,
  `radio_needSeatonService` varchar(36) default NULL,
  `radio_seatonTour` varchar(36) default NULL,
  `seatonNotToured` varchar(255) default NULL,
  `radio_pamphletIssued` varchar(36) default NULL,
  `pamphletNotIssued` varchar(255) default NULL,
  `summary` varchar(255) default NULL,
  `completedBy` varchar(120) default NULL,
  `assessCompleteTime` varchar(36) default NULL,
  `cbox_pamphletIssued` char(1) default NULL,
  `cbox_hostel` char(1) default NULL,
  `cbox_rotaryClub` char(1) default NULL,
  `cbox_annexHarm` char(1) default NULL,
  `cbox_longTermProgram` char(1) default NULL,
  `cbox_birchmountResidence` char(1) default NULL,
  `cbox_oNeillHouse` char(1) default NULL,
  `cbox_fortYork` char(1) default NULL,
  `cbox_downsviewDells` char(1) default NULL,
  PRIMARY KEY  (`ID`)
);

--
-- Table structure for table `functional_user_type`
--
CREATE TABLE `functional_user_type` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `intake_node_js` (
        `id` integer not null auto_increment,
        `question_id` varchar(255) not null,
        `location` varchar(255) not null,
        primary key(id)
);

--
-- Table structure for table `intake_node_label`
--
CREATE TABLE `intake_node_label` (
  `intake_node_label_id` int(10) unsigned NOT NULL auto_increment,
  `lbl` text NOT NULL,
  PRIMARY KEY  (`intake_node_label_id`)
);

--
-- Table structure for table `intake_node_type`
--
CREATE TABLE `intake_node_type` (
  `intake_node_type_id` int(10) unsigned NOT NULL auto_increment,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY  (`intake_node_type_id`)
);

--
-- Table structure for table `intake_node_template`
--
CREATE TABLE `intake_node_template` (
  `intake_node_template_id` int(10) unsigned NOT NULL auto_increment,
  `remote_intake_node_template_id` int(10) unsigned default NULL,
  `intake_node_type_id` int(10) unsigned NOT NULL,
  `intake_node_label_id` int(10) unsigned default NULL,
  PRIMARY KEY  (`intake_node_template_id`),
  KEY `IDX_intake_node_template_intake_node_type` (`intake_node_type_id`),
  KEY `IDX_intake_node_template_intake_node_label` (`intake_node_label_id`),
  CONSTRAINT `FK_intake_node_template_intake_node_label` FOREIGN KEY (`intake_node_label_id`) REFERENCES `intake_node_label` (`intake_node_label_id`),
  CONSTRAINT `FK_intake_node_template_intake_node_type` FOREIGN KEY (`intake_node_type_id`) REFERENCES `intake_node_type` (`intake_node_type_id`)
);

--
-- Table structure for table `intake_answer_validation`
--
CREATE TABLE `intake_answer_validation` (
  `intake_answer_validation_id` int(10) unsigned NOT NULL auto_increment,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY  (`intake_answer_validation_id`)
);

--
-- Table structure for table `intake_answer_element`
--
CREATE TABLE `intake_answer_element` (
  `intake_answer_element_id` int(10) unsigned NOT NULL auto_increment,
  `intake_node_template_id` int(10) unsigned NOT NULL,
  `intake_answer_validation_id` int(10) unsigned default NULL,
  `dflt` tinyint(1) default '0',
  `element` varchar(255) NOT NULL,
  `label` varchar(255),
  PRIMARY KEY  (`intake_answer_element_id`),
  KEY `IDX_intake_answer_element_intake_node_template` (`intake_node_template_id`),
  KEY `IDX_intake_answer_element_intake_answer_validation` (`intake_answer_validation_id`),
  CONSTRAINT `FK_intake_answer_element_intake_node_template` FOREIGN KEY (`intake_node_template_id`) REFERENCES `intake_node_template` (`intake_node_template_id`),
  CONSTRAINT `FK_intake_answer_element_intake_answer_validation` FOREIGN KEY (`intake_answer_validation_id`) REFERENCES `intake_answer_validation` (`intake_answer_validation_id`)
);

--
-- Table structure for table `intake_node`
--
CREATE TABLE `intake_node` (
  `intake_node_id` int(10) unsigned NOT NULL auto_increment,
  `intake_node_template_id` int(10) unsigned NOT NULL,
  `intake_node_label_id` int(10) unsigned default NULL,
  `pos` int(10) unsigned default '0',  
  `parent_intake_node_id` int(10) unsigned default NULL,
  `mandatory` boolean NOT NULL default false,
  `repeating` boolean NOT NULL default false,
  `common_list` boolean NOT NULL default false,
  `eq_to_id` int(10) default NULL,
  `form_version` int(10) unsigned default NULL,
  `publish_date` date default NULL,
  `publish_by` varchar(60) default NULL,
  `form_type` integer default 0,
  `question_id` varchar(255) default NULL,
  `validations` varchar(255) default NULL,
  PRIMARY KEY  (`intake_node_id`),
  KEY `IDX_intake_node_intake_node_template` (`intake_node_template_id`),
  KEY `IDX_intake_node_intake_node_label` (`intake_node_label_id`),
  KEY `IDX_intake_node_intake_node` (`parent_intake_node_id`),
  CONSTRAINT `FK_intake_node_intake_node_template` FOREIGN KEY (`intake_node_template_id`) REFERENCES `intake_node_template` (`intake_node_template_id`),
  CONSTRAINT `FK_intake_node_intake_node_label` FOREIGN KEY (`intake_node_label_id`) REFERENCES `intake_node_label` (`intake_node_label_id`),
  CONSTRAINT `FK_intake_node_intake_node` FOREIGN KEY (`parent_intake_node_id`) REFERENCES `intake_node` (`intake_node_id`)
);

--
-- Table structure for table `intake`
--
CREATE TABLE `intake` (
  `intake_id` int(10) unsigned NOT NULL auto_increment,
  `intake_node_id` int(10) unsigned NOT NULL,
  `client_id` int(10) unsigned NOT NULL,
  `staff_id` varchar(6) NOT NULL,
  `creation_date` datetime NOT NULL,
  `intake_status` varchar(10) not null default 'Signed',
  `intake_location` int(10) default 0,
  `facility_id` int,
  `program_id` int(10) default NULL,
  `lastUpdateDate` date default NULL,
  `end_date` date default NULL,
  PRIMARY KEY  (`intake_id`),
  KEY `IDX_intake_intake_node` (`intake_node_id`),
  KEY `IDX_intake_client_creation_date` (`client_id`,`creation_date`),
  CONSTRAINT `FK_intake_intake_node` FOREIGN KEY (`intake_node_id`) REFERENCES `intake_node` (`intake_node_id`)
);

--
-- Table structure for table `intake_answer`
--
CREATE TABLE `intake_answer` (
  `intake_answer_id` int(10) unsigned NOT NULL auto_increment,
  `intake_id` int(10) unsigned NOT NULL,
  `idx` int(10) not null default 0,
  `intake_node_id` int(10) unsigned NOT NULL,
  `val` text NOT NULL,
  PRIMARY KEY  (`intake_answer_id`),
  KEY `IDX_intake_answer_intake` (`intake_id`),
  KEY `IDX_intake_answer_intake_node` (`intake_node_id`),
  CONSTRAINT `FK_intake_answer_intake` FOREIGN KEY (`intake_id`) REFERENCES `intake` (`intake_id`),
  CONSTRAINT `FK_intake_answer_intake_node` FOREIGN KEY (`intake_node_id`) REFERENCES `intake_node` (`intake_node_id`)
);

--
-- Table structure for table `issue`
--


--
-- Table structure for table `program_access`
--

CREATE TABLE `program_access` (
  `id` bigint(20) NOT NULL auto_increment,
  `program_id` bigint(20) default NULL,
  `access_type_id` varchar(255) default NULL,
  `all_roles` tinyint(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKC385F77F9BCAF076` (`access_type_id`)
);

--
-- Table structure for table `program_access_roles`
--

CREATE TABLE `program_access_roles` (
  `id` bigint(20) NOT NULL default '0',
  `role_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`id`,`role_id`),
  KEY `FK55BF0DFDBC1003F0` (`id`),
  KEY `FK55BF0DFD60605436` (`role_id`)
);

--
-- Table structure for table `program_functional_user`
--

CREATE TABLE `program_functional_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `program_id` bigint(20) default NULL,
  `user_type_id` bigint(20) default NULL,
  `provider_no` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK13B70CCFE0B12E7` (`user_type_id`),
  KEY `FK13B70CCE6DF0AD3` (`provider_no`)
);

--
-- Table structure for table `program_provider`
--

CREATE TABLE `program_provider` (
  `id` bigint(20) NOT NULL auto_increment,
  `program_id` bigint(20) default NULL,
  `provider_no` varchar(6) NOT NULL,
  `role_id` bigint(20) default NULL,
  `team_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK3EFA2D4C60605436` (`role_id`),
  KEY `FK3EFA2D4CE6DF0AD3` (`provider_no`),
  KEY `FK3EFA2D4CDAA8624B` (`team_id`)
);

--
-- Table structure for table `program_provider_team`
--

CREATE TABLE `program_provider_team` (
  `id` bigint(20) NOT NULL default '0',
  `elt` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`id`,`elt`),
  KEY `FK5E7B3D703D01BC3D` (`id`),
  KEY `FK5E7B3D703073B4BB` (`elt`)
);

--
-- Table structure for table `program_queue`
--

CREATE TABLE `program_queue` (
  `queue_id` bigint(20) NOT NULL auto_increment,
  `client_id` bigint(20) NOT NULL default '0',
  `referral_date` datetime default NULL,
  `provider_no` bigint(20) NOT NULL default '0',
  `notes` varchar(255) default NULL,
  `program_id` bigint(20) NOT NULL default '0',
  `status` varchar(30) default NULL,
  `referral_id` bigint(20) default NULL,
  `temporary_admission_flag` tinyint(1) default NULL,
  `present_problems` varchar(255) default NULL,
  `intake_id` int(10) default NULL,
  PRIMARY KEY  (`queue_id`)
);

--
-- Table structure for table `program_team`
--

CREATE TABLE `program_team` (
  `team_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `program_id` bigint(20) default NULL,
  PRIMARY KEY  (`team_id`)
);

--
-- Table structure for table `provider_default_program`
--

CREATE TABLE `provider_default_program` (
  `id` int(10) NOT NULL auto_increment,
  `provider_no` varchar(6) NOT NULL default '',
  `program_id` int(10) NOT NULL default '0',
  `signnote` tinyint(1) default '0',
  PRIMARY KEY  (`id`)
);

--
-- Table structure for table `room`
--

CREATE TABLE `room` (
  `room_id` int(10) unsigned NOT NULL auto_increment,
  `room_type_id` int(10) unsigned NOT NULL default '1',
  `program_id` int(10) unsigned default NULL,
  `name` varchar(45) NOT NULL,
  `floor` varchar(45) default NULL,
  `active` tinyint(1) NOT NULL default '1',
  `facility_id` int NOT NULL,
  `assigned_bed`  tinyint(1)  NOT NULL default '1',
  `occupancy`  int(10) NULL default '0',
  CONSTRAINT `FK_room_facility` FOREIGN KEY (`facility_id`) REFERENCES `Facility` (`id`),
  PRIMARY KEY  (`room_id`)
);

--
-- Table structure for table `room_bed_historical`
--

CREATE TABLE `room_bed_historical` (
  `room_id` int(10) unsigned NOT NULL,
  `bed_id` int(10) unsigned NOT NULL,
  `contain_start` date NOT NULL,
  `contain_end` date NOT NULL,
  PRIMARY KEY  (`room_id`,`bed_id`,`contain_start`)
);

--
-- Table structure for table `room_type`
--

CREATE TABLE `room_type` (
  `room_type_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL,
  `dflt` tinyint(1) unsigned NOT NULL default '0',
  PRIMARY KEY  (`room_type_id`)
);


CREATE TABLE `room_demographic` (
  `room_id` int(10) unsigned NOT NULL default '0',
  `demographic_no` int(10) unsigned NOT NULL default '0',
  `provider_no` varchar(6) NOT NULL default '',
  `assign_start` date,
  `assign_end` date ,
  `comments` varchar(50)  NULL default '',

  PRIMARY KEY  (`room_id`,`demographic_no`)
);


CREATE TABLE `room_bed` (
  `room_id` int(10) unsigned NOT NULL default '0',
  `bed_id` int(10) unsigned NOT NULL default '0',
  `assign_start` date,
  `assign_end` date,
  `comments` varchar(50)  NULL default '',

  PRIMARY KEY  (`room_id`,`bed_id`)
);

--
-- Table structure for table `survey`
--

CREATE TABLE `survey` (
  `surveyid` bigint(20) NOT NULL auto_increment,
  `description` varchar(255) default NULL,
  `surveyData` text,
  `status` smallint(6) default NULL,
  `userId` bigint(20) default NULL,
  facilityId int,
  `dateCreated` datetime default NULL,
  `dateLaunched` datetime default NULL,
  `dateClosed` datetime default NULL,
  `launched_instance_id` bigint(20) default NULL,
  `version` bigint(20) default '0',
  PRIMARY KEY  (`surveyid`)
);

--
-- Table structure for table `surveyData`
-- surveyData is moved to oscarinit.sql.

-- CREATE TABLE IF NOT EXISTS `surveyData` (
--   `surveyDataId` int(10) NOT NULL auto_increment,
--   `surveyId` varchar(5) default NULL,
--   `demographic_no` int(10) default NULL,
--   `provider_no` varchar(6) default NULL,
--   `status` char(2) default NULL,
--   `survey_date` date default NULL,
--   `answer` varchar(10) default NULL,
--   `processed` int(10) default NULL,
--   PRIMARY KEY  (`surveyDataId`),
--   KEY `surveyId_index` (`surveyId`),
--   KEY `demographic_no_index` (`demographic_no`),
--   KEY `provider_no_index` (`provider_no`),
--   KEY `status_index` (`status`),
--   KEY `survey_date_index` (`survey_date`),
--   KEY `answer_index` (`answer`),
--   KEY `processed_index` (`processed`)
-- );

--
-- Table structure for table `survey_test_data`
--

CREATE TABLE `survey_test_data` (
  `id` bigint(20) NOT NULL auto_increment,
  `instance_id` bigint(20) default NULL,
  `page_number` bigint(20) default NULL,
  `section_id` bigint(20) default NULL,
  `question_id` bigint(20) default NULL,
  `value` text default NULL,
  `data_key` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKB5082892A9437DC8` (`instance_id`)
);

--
-- Table structure for table `survey_test_instance`
--

CREATE TABLE `survey_test_instance` (
  `id` bigint(20) NOT NULL auto_increment,
  `survey_id` bigint(20) default NULL,
  `date_created` datetime default NULL,
  `user_id` bigint(20) default NULL,
  `client_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
);

create table SystemMessage 
(
  id int primary key auto_increment,
  message text NOT NULL,
  creationDate datetime NOT NULL,
  expiryDate datetime NOT NULL
);


CREATE TABLE `facility_message` (
  `id` int(10) NOT NULL auto_increment,
  `message` text NOT NULL,
  `creation_date` datetime NOT NULL default '1900-01-01 00:00:00',
  `expiry_date` datetime NOT NULL default '1900-01-01 00:00:00',
  `facility_id` int,
  `facility_name` varchar(32),
  `programId` int,
  PRIMARY KEY  (`id`)
);


create table pmm_log (
    id bigint not null auto_increment,
    provider_no varchar(255),
    dateTime datetime,
    action varchar(20),
    contentId varchar(80),
    content varchar(80) not null,
    ip varchar(30),
    primary key (id)
);


--
-- Table structure for table `program_clientstatus`
--
CREATE TABLE `program_clientstatus` (
  `clientstatus_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `program_id` bigint(20) default NULL,
  PRIMARY KEY  (`clientstatus_id`)
);

create table tickler_update (
	id int(10) not null auto_increment,
	tickler_no int(10) not null,
	status char(1),
        assignedTo varchar(6),
        serviceDate datetime,
        priority varchar(6),
	provider_no varchar(6) not null,
	update_date datetime not null,
	primary key(id)
);

create table tickler_comments (
	id int(10) not null auto_increment,
	tickler_no int(10) not null,
	message text,
	provider_no varchar(6) not null,
	update_date datetime not null,
	primary key(id)
);

CREATE TABLE `formDischargeSummary` (
  `id` bigint(11) NOT NULL auto_increment,
  `demographic_no` bigint(11) NOT NULL default '0',  
  `formCreated` date default NULL,
  `formEdited` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `clientName` varchar(60) default NULL,
  `birthDate` date default NULL,
  `ohip` varchar(25) default NULL,
  `admitDate` date default NULL,
  `dischargeDate` date default NULL,
  `programName` varchar(70) default NULL,
  `allergies` text default NULL,
  `admissionNotes` text default NULL,
  `currentIssues` text default NULL,
  `briefSummary` text default NULL,
  `dischargePlan` text default NULL,
  `followUpAppointment` char(1) default NULL,
  `doctor1` varchar(30) default NULL,
  `phoneNumber1` varchar(30) default NULL,
  `date1` varchar(20) default NULL,
  `location1` varchar(100) default NULL,
  `doctor2` varchar(30) default NULL,
  `phoneNumber2` varchar(30) default NULL,
  `date2` varchar(20) default NULL,
  `location2` varchar(100) default NULL,
  `doctor3` varchar(30) default NULL,
  `phoneNumber3` varchar(30) default NULL,
  `date3` varchar(20) default NULL,
  `location3` varchar(100) default NULL,
  `doctor4` varchar(30) default NULL,
  `phoneNumber4` varchar(30) default NULL,
  `date4` varchar(20) default NULL,
  `location4` varchar(100) default NULL,
  `prescriptionSummary` text default NULL,
  `prescriptionProvided` char(1) default NULL,  
  `medicationProvided` char(1) default NULL,  
  `ODBFormReqired` char(1) default NULL,
  `changeMedications` varchar(255) default NULL,
  `referralProgram1` varchar(255) default NULL,
  `referralMade1` varchar(255) default NULL,
  `referralOutcome1` varchar(255) default NULL, 
  `referralProgram2` varchar(255) default NULL,
  `referralMade2` varchar(255) default NULL,
  `referralOutcome2` varchar(255) default NULL, 
  `referralProgram3` varchar(255) default NULL,
  `referralMade3` varchar(255) default NULL,
  `referralOutcome3` varchar(255) default NULL, 
  `referralProgram4` varchar(255) default NULL,
  `referralMade4` varchar(255) default NULL,
  `referralOutcome4` varchar(255) default NULL, 
  `referralProgram5` varchar(255) default NULL,
  `referralMade5` varchar(255) default NULL,
  `referralOutcome5` varchar(255) default NULL,  
  `provider_no` bigint(11) NOT NULL default '0',
  `providerName` varchar(60) default NULL,
  `signature` varchar(60) default NULL,
  `signatureDate` varchar(20) default NULL,
  `notes` text default NULL,
  PRIMARY KEY  (`id`)  
);

create table programSignature (
	`id` int(10) NOT NULL auto_increment,
  	`programId` int(10) NOT NULL default '0',   	 
  	`programName` varchar(70) NOT NULL default '',
  	`providerId` varchar(6) NOT NULL default '0',
  	`providerName` varchar(60) NOT NULL default '',
  	`caisiRoleName` varchar(255) NOT NULL default '',
  	`updateDate` datetime,
  	PRIMARY KEY (`id`)
);


-- program restriction based on gender
create table `program_client_restriction` (
    `id` bigint(22) NOT NULL auto_increment,
    `program_id` int(10) NOT NULL,
    `demographic_no` int(10) NOT NULL,
    `provider_no` varchar(6) NOT NULL,
    `comments` varchar(255) default null,
    `is_enabled` tinyint(1) NOT NULL default TRUE,
    `start_date` datetime not null,
    `end_date` datetime not null,
    early_termination_provider varchar(6),
	PRIMARY KEY (`id`),    
    CONSTRAINT `FK_pcr_program` FOREIGN KEY (`program_id`) REFERENCES `program` (`id`),
    CONSTRAINT `FK_pcr_provider` FOREIGN KEY (`provider_no`) REFERENCES `provider` (`provider_no`),
    CONSTRAINT `FK_pcr_demographic` FOREIGN KEY (`demographic_no`) REFERENCES `demographic` (`demographic_no`)
);

CREATE TABLE `joint_admissions` (
  `id` bigint(11) NOT NULL auto_increment,
  `client_id` bigint(11) NOT NULL default '0',
  `type_id` bigint(3) NOT NULL default '0',
  `provider_no` varchar(6) NOT NULL default '0',
  `admission_date` datetime default NULL,
  `head_client_id` bigint(11) NOT NULL default '0',
  `archived` tinyint(1) default '0',
  `archiving_provider_no` varchar(6) default NULL,
  PRIMARY KEY  (`id`),
  KEY `client_id` (`client_id`),
  KEY `head_client_id` (`head_client_id`)
);

create table health_safety
(
  id             	bigint(20) not null auto_increment,
  demographic_no 	bigint(20) default '0' not null,
  message        	text,
  username       	varchar(128),
  updatedate    	datetime,
  primary key (id)
);


-- Tables for quatro group's report runner
create table lst_gender
(
	code char(1) NOT NULL,
 	description varchar(80),
 	isactive tinyint(1),
 	displayorder int(10),
 	PRIMARY KEY (code)
);

create table lst_sector
(
	id int(10) NOT NULL auto_increment,
 	description varchar(80),
 	isactive tinyint(1),
 	displayorder int(10),
 	PRIMARY KEY (id)
);

create table lst_organization
(
	id int(10) NOT NULL auto_increment,
 	description varchar(80),
 	isactive tinyint(1),
 	displayorder int(10),
 	PRIMARY KEY (id)
);

create table lst_discharge_reason
(
  id   int(10) not null auto_increment,
  description 	varchar(80),
  needsecondary tinyint(1),  
  isactive    	tinyint(1),
  displayorder 	int(10),
  primary key (id)
);

create table lst_field_category
(
 id int(10) NOT NULL auto_increment,
 description varchar(80),
 isactive tinyint(1),
 displayorder int(10),
 PRIMARY KEY (id)
);

create table lst_service_restriction
(
 id int(10) NOT NULL auto_increment,
 description varchar(80),
 isactive tinyint(1),
 displayorder int(10),
 PRIMARY KEY (id)
);

create table lst_admission_status
(
  code          varchar(20) not null,
  description   varchar(80),
  isactive      tinyint(1),
  displayorder  int,
  primary key (code)
);

create table lst_program_type
(
  code         varchar(20) not null,
  description  varchar(80),
  isactive     tinyint(1),
  displayorder int,
  primary key (code)
);

-- Tables for quatro group's report runner:
create table report
(
  reportno     	bigint(10) 	NOT NULL auto_increment,
  title         varchar(80) default NULL,
  description	varchar(255) default NULL,
  orgapplicable tinyint(1) 	default 0,
  reporttype	char(3) 	default NULL,
  dateoption	char(1)	default NULL,
  datepart		char(1)	default NULL,
  reportgroup	int(10)		default 0,
  notes			text 		default NULL,
  tablename		varchar(30)	default NULL,
  updatedby		varchar(20)	default NULL,
  updateddate	datetime 	default NULL,
  sptorun		varchar(32) default NULL,
  PRIMARY KEY  (reportno)
);

create table report_date
(
  sessionid		varchar(32) NOT NULL ,
  startdate   	date default NULL,
  enddate		date default NULL,
  asofdate		date default NULL,
  startdate_s 	varchar(8) 	default NULL,
  enddate_s		varchar(8)	default NULL,   
  asofdate_s	varchar(8)	default NULL,
  PRIMARY KEY  (sessionid)
);

create table report_date_sp
(
  reportno		int(10)    NOT NULL auto_increment,
  startdate		date default NULL , 
  enddate		date default NULL,
  asofdate		date default NULL,
  startdate_s	varchar(8) default NULL,
  enddate_s		varchar(8) default NULL,
  asofdate_s	varchar(8) default NULL,
  sptorun		varchar(32) default NULL,
  PRIMARY KEY  (reportno)
);

create table report_doctext
(
  docid       int(10) NOT NULL auto_increment,
  docdata     text,
  revdatetime TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (docid)
);

create table report_document
(
  docid          int(10) NOT NULL auto_increment,
  subject        varchar(256) default NULL,
  privacycd      varchar(3) default NULL,
  ownerid        varchar(12) default NULL,
  checkoutyn     char(1) default NULL,
  checkoutuserid	varchar(12) default NULL,
  checkoutdate	datetime 	default NULL,
  doctype		varchar(3) 	default NULL,
  filename		varchar(128) default NULL,
  moduleid		varchar(6) 	default NULL,
  refno			varchar(12) default NULL,
  filetype		varchar(5) 	default NULL,
  viewid		varchar(36) default NULL,
  viewrefno		varchar(12)	default NULL,
  revdatetime   datetime default NULL,
  PRIMARY KEY  (docid)
);

create table report_filter
(  
  fieldno           int(10) NOT NULL auto_increment,
  reportno          int(10) default 0,
  fieldname         varchar(32) default NULL,
  fielddesc         varchar(80)	default NULL,
  fieldtype         varchar(10)	default NULL,
  lookup_table      varchar(30) default NULL,
  iscrosstabheaders char(1)	default NULL,
  operator			varchar(10)	default NULL,
  lookup_tree       char(1)	default NULL,
  fieldsql          varchar(32)	default NULL,
  lookup_script     varchar(50)	default NULL,
  note              varchar(128)	default NULL,
  valueformat       varchar(32)	default NULL,
  PRIMARY KEY  (fieldno)
);

create table report_lk_reportgroup
(
  id           int(10) NOT NULL auto_increment,
  description  varchar(40)	default NULL,
  shortdesc    varchar(10)	default NULL,
  activeyn     char(1)	default NULL,
  orderbyindex int(10)	default 0,
  note         varchar(128)	default NULL,
  PRIMARY KEY  (id)
);

create table report_option
(  
  reportoptionid int(10) NOT NULL auto_increment,
  reportno       int(10) not null,
  optiontitle    varchar(120)	default NULL,
  longdesc       varchar(120)	default NULL,
  activeyn       tinyint(1)	default 0,
  defaultyn      tinyint(1)	default 0,
  datefield      varchar(32)	default NULL,
  datefielddesc  varchar(32)	default NULL,
  sqlwhere       text	default NULL,
  sqlorderby     text	default NULL,
  rptfilename    varchar(128)	default NULL,
  rptfileno      int(10)	default 0,
  rptversion     datetime default NULL,
  datefieldtype  varchar(3)	default NULL,
  PRIMARY KEY  (reportoptionid)
);

create table report_qgviewfield
(
  qgviewno      int(10) NOT NULL auto_increment,  
  fieldno       int(10) not null,
  fieldname     varchar(32) not null,
  description   varchar(80)	default NULL,
  fieldtypecode varchar(8)	default NULL,
  numbermask    varchar(16)	default NULL,
  fieldlength   int(10) 	default 0,
  sourcetxt     text	default NULL,
  note          varchar(255) default NULL,
  grouprank     int(10)		default 0,
  lookuptable   varchar(6)	default NULL,
  PRIMARY KEY  (qgviewno,fieldno)
);

create table report_qgviewsummary
(
  qgviewno    int(10) NOT NULL auto_increment,
  qgviewcode  varchar(30) 	not null ,
  description varchar(80) 	default NULL,
  groupcode   varchar(10)	default NULL,
  mastertype  char(1)	default NULL,
  updatedby   varchar(12)	default NULL,
  updateddate datetime	default NULL,
  note        text	default NULL,
  activeyn    char(1)	default NULL,
  secureyn    char(1)	default NULL,
  dbentity    varchar(40)	default NULL,
  refviews    varchar(512)	default NULL,
  relations   text	default NULL,
  filters     text	default NULL,
  object_type varchar(5)	default NULL,
  distinctyn  char(1)	default NULL,
  PRIMARY KEY  (qgviewno)
);

create table report_role
(
  reportno    	int(10) NOT NULL default 0,
  rolecode		varchar(20) not null ,
  access_type	char(1) 	default NULL,
  PRIMARY KEY  (reportno,rolecode)
);

create table report_template
(
  templateno     int(10) NOT NULL auto_increment,
  reportno       int(10) not null	default 0,
  reportoptionid int(10)	default 0,
  description    varchar(120)	default NULL,
  startdate      datetime	default NULL,
  enddate        datetime	default NULL,
  startpayperiod varchar(10)	default NULL,
  endpayperiod   varchar(10)	default NULL,
  loginid        varchar(20)	default NULL,
  updatedate     datetime	default NULL,
  privateyn      char(1)	default NULL,
  PRIMARY KEY  (templateno)
);

create table report_template_criteria 
(
  counter    int(10) not null	default 0,
  templateno int(10) not null	default 0,
  relation   varchar(10)	default NULL,
  fieldno    int(10)	default 0,
  operator   varchar(10)	default NULL,
  operators  varchar(10)	default NULL,
  val        varchar(40)	default NULL,
  valdesc    varchar(120)	default NULL,
  required   char(1)	default NULL,
  PRIMARY KEY  (templateno,counter)
);

create table report_template_org
(
  counter    int(10) not null,
  templateno int(10) not null,
  orgcd      varchar(72),
  PRIMARY KEY  (templateno,counter)
);

create table app_lookuptable
(
  tableid         varchar(32) not null,
  moduleid        varchar(5),
  table_name      varchar(32),
  description     varchar(80),
  istree          tinyint(1),
  treecode_length int(10),
  activeyn        tinyint(1),
  readonly	  tinyint(1),
  PRIMARY KEY  (tableid)
);

create table app_lookuptable_fields
(
  tableid     VARCHAR(10) not null,
  fieldname   VARCHAR(30) not null,
  fielddesc   VARCHAR(40),
  edityn      VARCHAR(1),
  fieldtype   VARCHAR(1),
  lookuptable VARCHAR(6),
  fieldsql    VARCHAR(32),
  fieldindex  int(10),
  uniqueyn    int(10),
  genericidx  int(10),
  autoyn      tinyint(1),
  fieldlength int(10),
  primary key (tableid, fieldname)
);

create table app_module
(
  module_id   int(10) not null,
  description varchar(128) not null,
  primary key (module_id)
);

create table IntegratorConsentComplexExitInterview
(
	facilityId int not null, foreign key (facilityId) references Facility(id),
	demographicId int not null, foreign key (demographicId) references demographic(demographic_no),
	primary key (facilityId,demographicId),

	spokenLanguage varchar(64),
	readLanguage varchar(64),
	education varchar(64),
	timeToReviewConsent varchar(16),
	timeToReviewConsentComments varchar(255),
	pressured varchar(16),
	pressuredComments varchar(255),
	moreInfo varchar(16),
	moreInfoComments varchar(255),
	reAskConsent varchar(32),
	reAskConsentComments varchar(255),
	additionalComments varchar(255)
);



create table formMentalHealthForm1(
	id int primary key auto_increment,
	demographic_no bigint(11) NOT NULL default 0,  
  	formCreated date default NULL,
  	formEdited timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,  
	physicianName varchar(60),
	physicianAddress varchar(255),
	telephoneNumber varchar(20),
	faxNumber varchar(20),
	onDate varchar(10),	
	clientName varchar(60) default NULL,
	clientAddress varchar(250) default NULL,	
	threatened tinyint(1) default 0,
	attempted tinyint(1) default 0,
	behaved tinyint(1) default 0,
	caused tinyint(1) default 0,
	shown tinyint(1) default 0,	
	observation text default NULL,
	facts text default NULL,
	harmHimself tinyint(1) default 0,
	harmOthers tinyint(1) default 0,
	impairment tinyint(1) default 0,
	observation2 text default NULL,
	facts2 text default NULL,	
	harmHimselfB tinyint(1) default 0,
	harmOthersB tinyint(1) default 0,
	deteriorationB tinyint(1) default 0,
	impairmentB tinyint(1) default 0,	
	harmHimselfB2 tinyint(1) default 0,
	harmOthersB2 tinyint(1) default 0,
	deteriorationB2 tinyint(1) default 0,
	impairmentB2 tinyint(1) default 0,	
	observationB text default NULL,
	factsB text default NULL,
	todayDate varchar(20) default NULL,
	todayTime varchar(8) default NULL,
	signature varchar(60) default NULL,
	datetimeOfDetention varchar(20) default NULL,
	signature1 varchar(60) default NULL,
	datetimeOfDelivered varchar(20) default NULL,
	signature2 varchar(60) default NULL
);


create table formMentalHealthForm14 (
	id int primary key auto_increment,
	demographic_no bigint(11) NOT NULL default 0,  
  	formCreated date default NULL,
  	formEdited timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,  
	name varchar(60),
	address varchar(255),
	physicianName varchar(60),
	nameOfFacility varchar(100),
	clientName varchar(60),
	clientDOB varchar(10),
	witness varchar(60),
	signature varchar(60),
	relationship varchar(20),
	signatureDate varchar(20)
);

create table formMentalHealthForm42(
	id int primary key auto_increment,
	demographic_no bigint(11) NOT NULL default 0,  
  	formCreated date default NULL,
  	formEdited timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,  
  	
  	
  	name varchar(60) default null,
	homeAddress varchar(255) default null,
	physician varchar(60) default null,
	dateOfExamination varchar(10) default null,
	
	chkThreatenedA tinyint(1) default 0,
	chkBehavedA tinyint(1) default 0,
	chkCompetenceA tinyint(1) default 0,
	chkHarmYourselfA tinyint(1) default 0,
	chkHarmAnotherA tinyint(1) default 0,
	chkImpairmentA tinyint(1) default 0,
	
	chkHarmYourselfB tinyint(1) default 0,
	chkHarmAnotherB tinyint(1) default 0,
	chkDeteriorationB tinyint(1) default 0,
	chkImpairmentB tinyint(1) default 0,
	
	chkHarmYourselfB2 tinyint(1) default 0,
	chkHarmAnotherB2 tinyint(1) default 0,
	chkDeteriorationB2 tinyint(1) default 0,
	chkImpairmentB2 tinyint(1) default 0,
	
	dateOfSign varchar(10) default 0,
	signPhysician varchar(60) default null,
	
	name2 varchar(60) default null,
	homeAddress2 varchar(255) default null,
	nameOfMinisterHealth varchar(255) default null,
	
	chkHarmYourself2 tinyint(1) default 0,
	chkHarmAnother2 tinyint(1) default 0,
	dateOfOrder varchar(10) default null,
	dateOfSign2 varchar(10) default null,
	signPhysician2 varchar(60) default null

);




create table ClientLink
(
	id int primary key auto_increment,
	facilityId int not null, index(facilityId), foreign key (facilityId) references Facility(id),
	clientId int not null, index(clientId), foreign key (clientId) references demographic(demographic_no),
	linkType varchar(32) not null,
	remoteLinkId int not null,
	linkDate datetime not null,
	linkProviderNo varchar(6) not null,  foreign key (linkProviderNo) references provider(provider_no),
	unlinkDate datetime,
	unlinkProviderNo varchar(6),  foreign key (unlinkProviderNo) references provider(provider_no)
);

create table HnrDataValidation
(
	id int primary key auto_increment,
	facilityId int not null, index(facilityId), foreign key (facilityId) references Facility(id),
	clientId int not null, index(clientId), foreign key (clientId) references demographic(demographic_no),
	created datetime not null,
	validatorProviderNo varchar(6) not null, foreign key (validatorProviderNo) references provider(provider_no),
	valid tinyint(1) not null,
	validationType varchar(32) not null, index(validationType),
	validationCrc bigint not null
);

create table DigitalSignature
(
	id int primary key auto_increment,
	facilityId int not null, foreign key (facilityId) references Facility(id),
	providerNo varchar(6) not null, foreign key (providerNo) references provider(provider_no),
	demographicId int not null, foreign key (demographicId) references demographic(demographic_no),
	dateSigned datetime not null,
	signatureImage blob not null
);

create table IntegratorConsent
(
	id int primary key auto_increment,

	facilityId int not null, foreign key (facilityId) references Facility(id),
	demographicId int not null, foreign key (demographicId) references demographic(demographic_no),
    providerNo varchar(6) not null, foreign key (providerNo) references provider(provider_no),
	createdDate datetime not null, index(createdDate),
	
	excludeMentalHealthData tinyint(1) NOT NULL,
	clientConsentStatus varchar(32) NOT NULL,
	signatureStatus varchar(10) not NULL,
	expiry date,

	digitalSignatureId int, foreign key (digitalSignatureId) references DigitalSignature(id)
);

create table IntegratorConsentShareDataMap
(
	IntegratorConsent_id int not null,
	mapkey int not null,
	element tinyint(1) not null
);

create table CdsFormOption
(
	id int primary key auto_increment,
	cdsFormVersion varchar(16) not null,
	cdsDataCategory varchar(16) not null,
	cdsDataCategoryName varchar(255) not null
);

create table CdsClientForm
(
	id int primary key auto_increment,
	cdsFormVersion varchar(16) not null,
	index(cdsFormVersion),
	providerNo varchar(6) not null,
	signed tinyint not null,
	index(signed),
	created datetime not null,
	facilityId int not null,
	clientId int not null,
	index(facilityId, clientId),
	admissionId int,
	index(admissionId),
	initialContactDate date,
	assessmentDate date
);

create table CdsClientFormData
(
	id int primary key auto_increment,
	cdsClientFormId int not null,
	index(cdsClientFormId),
	question varchar(64) not null,
	index(question),
	answer varchar(16) not null
);


create table OcanFormOption
(
        id int primary key auto_increment,
        ocanFormVersion varchar(16) not null,
        ocanDataCategory varchar(100) not null,
        ocanDataCategoryValue varchar(100) not null,
        ocanDataCategoryName varchar(255) not null
);


create table OcanStaffForm
(
        id int primary key auto_increment,
        assessmentId int,
        submissionId int,
        ocanFormVersion varchar(16) not null,
        ocanType varchar(20) not null,
        index(ocanFormVersion),
        providerNo varchar(6),
        clientFormProviderNo varchar(6),
        signed tinyint not null,
        index(signed),
        created datetime,
        clientFormCreated datetime,
        facilityId int not null,
        clientId int not null,
        index(facilityId, clientId),
        admissionId int,
        index(admissionId),
        clientAge int,
        index(clientAge),
        lastName varchar(100),
        lastNameAtBirth varchar(100),
        firstName varchar(100),
        addressLine1 varchar(100),
        addressLine2 varchar(100),
        city varchar(100),
        province varchar(10),
        postalCode varchar(100),
        phoneNumber varchar(100),
        email varchar(100),
        hcNumber varchar(100),
        hcVersion varchar(100),
        dateOfBirth varchar(100),
        estimatedAge varchar(3),
        clientDateOfBirth varchar(10),
        reasonForAssessment varchar(100),
	assessmentStatus varchar(40),
	index(assessmentStatus),
	startDate date,
	clientStartDate date,
	completionDate date,
	clientCompletionDate date,
	gender varchar(10),
	providerName varchar(100),
	clientFormProviderName varchar(100),	
	consent varchar(50),
	referralDate date,
	admissionDate date,
	serviceInitDate date,
	dischargeDate date,
	index(startDate),
	index(completionDate)
);

create table OcanStaffFormData
(
        id int primary key auto_increment,
        ocanStaffFormId int not null,
        index(ocanStaffFormId),
        question varchar(64) not null,
        index(question),
        answer text not null
);




DROP TABLE IF EXISTS `oncall_questionnaire`;
CREATE TABLE `oncall_questionnaire` (
        `id` int(10) unsigned NOT NULL auto_increment,
        `providerNo` varchar(40) NOT NULL,
        `type` varchar(250) NOT NULL,
        `health_type` varchar(50) NOT NULL,
        `nurse_involved` varchar(50) NOT NULL,
        `course_of_action` varchar(50) NOT NULL,
        `physician_consultation_reqd` varchar(50) NOT NULL,
        `call_time` datetime NOT NULL,
        PRIMARY KEY  (`id`)
);

create table CdsHospitalisationDays
(
	id int primary key auto_increment,
	clientId int not null,
	admitted date not null,
	discharged date
);

create table FunctionalCentre
(
	accountId varchar(64) primary key,
	description varchar(255) not null
);

#create table group_note_link (
#        id int primary key auto_increment,
#        created timestamp,
#        noteId int(10) not null,
#        demographicNo int(10) not null,
#        key(noteId),
#        key(demographicNo)
#);


CREATE TABLE IntegratorControl (
        id int auto_increment,
        facilityId int not null,
        control varchar(80),
        execute boolean,
        PRIMARY KEY (id)
);


CREATE TABLE `GroupNoteLink` (
  `id` int(11) NOT NULL auto_increment,
  `created` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `noteId` int(10) NOT NULL,
  `demographicNo` int(10) NOT NULL,
  `anonymous` tinyint(1) default NULL,
  `active` tinyint(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `noteId` (`noteId`),
  KEY `demographicNo` (`demographicNo`),
  KEY `anonymous` (`anonymous`),
  KEY `active` (`active`)
);


create table OcanConnexOption (
	id int primary key auto_increment,
	LHINCode varchar(3) NOT NULL,
	orgLHIN varchar(100) NOT NULL,
	orgName varchar(100) NOT NULL,
	orgNumber varchar(5) NOT NULL,
	programName varchar(100) NOT NULL,
	programNumber varchar(5) NOT NULL
);


create table OcanSubmissionLog (
submissionId int primary key auto_increment,
submitDateTime timestamp,
result varchar(255),
transactionId varchar(100),
resultMessage text,
submissionData longtext,
submissionType varchar(30),
KEY `submitDateIndex` (`submitDateTime`),
KEY `submissionTypeIndex` (`submissionType`)
);
