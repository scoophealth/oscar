-- Updates Oscar database from version 10_12 to 11 --
-- less OLIS which is huge and seperate --

-- =========START OF FEATURES THAT MADE THE 11 RELEASE========= --

-- CAISI Patches from 2011-01-12.sql --

alter table OcanStaffForm add clientStartDate date after startDate;
alter table OcanStaffForm add clientCompletionDate date after completionDate;
alter table OcanStaffForm add clientDateOfBirth varchar(10) after dateOfBirth;
alter table OcanStaffForm add ocanType varchar(20) not null after ocanFormVersion;
alter table OcanStaffForm modify gender varchar(10) null;

update OcanStaffForm set assessmentStatus="Completed" where assessmentStatus="Complete";
update OcanStaffForm set assessmentStatus="In Progress" where assessmentStatus="Active";

update OcanStaffForm set assessmentStatus="In Progress" where assessmentStatus is null;
update OcanStaffForm set assessmentStatus="In Progress" where assessmentStatus="";
 
update OcanStaffForm set ocanType="FULL";
 
INSERT INTO `OcanFormOption` VALUES (1213,'1.2','OCAN Assessment Status','In Progress','In Progress');
INSERT INTO `OcanFormOption` VALUES (1214,'1.2','OCAN Assessment Status','Completed','Completed');
INSERT INTO `OcanFormOption` VALUES (1215,'1.2','OCAN Assessment Status','Cancelled','Cancelled');

--
alter table OcanStaffForm add assessmentId int after id;
alter table OcanStaffForm add clientFormCreated datetime after created;
alter table OcanStaffForm add clientFormProviderNo varchar(6) after providerNo;
alter table OcanStaffForm add clientFormProviderName varchar(100) after providerName;

alter table OcanStaffForm modify providerNo varchar(6);
alter table OcanStaffForm modify created datetime;

delete from OcanFormOption where id=704 or id=709;
--
create table OcanSubmissionLog (
submissionId int primary key auto_increment,
submitDateTime timestamp,
result varchar(255),
transactionId varchar(100),
resultMessage text,
submissionData longtext
);


alter table OcanStaffForm add submissionId integer;

-- patch-2011-03-01.sql --
update OcanStaffForm set submissionId=0;

INSERT INTO `OcanFormOption` VALUES (1216,'1.2','Year of First Entry Date','1985','1985');
INSERT INTO `OcanFormOption` VALUES (1217,'1.2','Year of First Entry Date','1986','1986');
INSERT INTO `OcanFormOption` VALUES (1218,'1.2','Year of First Entry Date','1987','1987');
INSERT INTO `OcanFormOption` VALUES (1219,'1.2','Year of First Entry Date','1988','1988');
INSERT INTO `OcanFormOption` VALUES (1220,'1.2','Year of First Entry Date','1989','1989');
INSERT INTO `OcanFormOption` VALUES (1221,'1.2','Year of First Entry Date','1990','1990');
INSERT INTO `OcanFormOption` VALUES (1222,'1.2','Year of First Entry Date','1991','1991');
INSERT INTO `OcanFormOption` VALUES (1223,'1.2','Year of First Entry Date','1992','1992');
INSERT INTO `OcanFormOption` VALUES (1224,'1.2','Year of First Entry Date','1993','1993');
INSERT INTO `OcanFormOption` VALUES (1225,'1.2','Year of First Entry Date','1994','1994');
INSERT INTO `OcanFormOption` VALUES (1226,'1.2','Year of First Entry Date','1995','1995');
INSERT INTO `OcanFormOption` VALUES (1227,'1.2','Year of First Entry Date','1996','1996');
INSERT INTO `OcanFormOption` VALUES (1228,'1.2','Year of First Entry Date','1997','1997');
INSERT INTO `OcanFormOption` VALUES (1229,'1.2','Year of First Entry Date','1998','1998');
INSERT INTO `OcanFormOption` VALUES (1230,'1.2','Year of First Entry Date','1999','1999');

-- end CAISI patches --


-- UPDATES --


insert into issue (`code`,`description`,`role`,`update_date`) Values('CurrentHistory','Current History', 'nurse', now());

alter table eyeform_macro add discharge varchar(20);
alter table eyeform_macro add stat varchar(20);
alter table eyeform_macro add opt varchar(20);
alter table measurements modify type varchar(50);
alter table measurements modify dataField varchar(50);
update consultationRequests c left outer join professionalSpecialists p on p.specId = c.specId set c.specId = NULL where p.specId is null;
update consultationRequests set patientWillBook = false where patientWillBook != true and patientWillBook != false;create table consultationRequestExt(
 id int(10) NOT NULL auto_increment, 
 requestId int(10) NOT NULL, 
 name varchar(100) NOT NULL,
 value varchar(100) NOT NULL,
 dateCreated date not null,
 primary key(id),
 key(requestId)
);
alter table other_id modify table_id varchar(30);
alter table casemgmt_note add appointmentNo integer;
update casemgmt_note set appointmentNo=0;

alter table  measurements add appointmentNo int(10) not null;
update measurements set appointmentNo=0;


create table EyeformConsultationReport (
 id int(10) NOT NULL auto_increment,
 `date` date,
 referralId integer,
 greeting integer,
 appointmentNo integer,
 appointmentDate date,
 appointmentTime time,
 demographicNo int,
 reason varchar(255),
 type varchar(255),
 cc varchar(255),
 memo varchar(255),
 clinicalInfo varchar(255),
 currentMeds varchar(255),
 allergies varchar(255),
 providerNo varchar(20),
 status varchar(255),
 sendTo varchar(255),
 examination varchar(255),
 concurrentProblems varchar(255),
 impression varchar(255),
 plan varchar(255),
 urgency varchar(100),
 patientWillBook integer,
 primary key(id)
);

drop table if exists EyeformConsultationReport;
drop table if exists EyeformConsulationReport;

create table EyeformConsultationReport (
 id int(10) NOT NULL auto_increment,
 `date` date,
 referralId integer,
 greeting integer,
 appointmentNo integer,
 appointmentDate date,
 appointmentTime time,
 demographicNo int,
 reason varchar(255),
 type varchar(255),
 cc varchar(255),
 memo varchar(255),
 clinicalInfo varchar(255),
 currentMeds varchar(255),
 allergies varchar(255),
 providerNo varchar(20),
 status varchar(255),
 sendTo varchar(255),
 examination varchar(255),
 concurrentProblems varchar(255),
 impression varchar(255),
 plan varchar(255),
 urgency varchar(100),
 patientWillBook integer,
 primary key(id)
);

alter table ProviderPreference add defaultDoNotDeleteBilling  tinyint(1) ;
alter table ProviderPreference add defaultDxCode varchar(4) ;
update ProviderPreference set defaultDoNotDeleteBilling=0 where defaultDoNotDeleteBilling is NULL;
alter table EyeformConsultationReport change `date` `date` datetime;
DROP TABLE IF EXISTS `eyeform_followup`;
DROP TABLE IF EXISTS `EyeformFollowUp`;
CREATE TABLE `EyeformFollowUp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_no` int(11)  ,
  `demographic_no` int(11) ,
  `timespan` int(11) ,
  `timeframe` varchar(25) ,
  `followup_provider` varchar(100) ,
  `date` timestamp ,
  `type` varchar(25),
  `urgency` varchar(50),
  `comment` text,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `procedurebook`;
DROP TABLE IF EXISTS `EyeformProcedureBook`;
CREATE TABLE `EyeformProcedureBook` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eyeform_id` bigint(20) ,
  `demographic_no` int(11) ,
  `appointment_no` int(11) ,
  `provider` varchar(60) ,
  `procedure_name` varchar(30) ,
  `eye` varchar(20) ,
  `location` varchar(50) ,
  `urgency` varchar(10) ,
  `comment` text,
  `date` datetime ,
  `status` varchar(2) ,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `testbookrecord`;
DROP TABLE IF EXISTS `EyeformTestBook`;
CREATE TABLE `EyeformTestBook` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `testname` varchar(60) ,
  `appointment_no` int(11) ,
  `demographic_no` int(11) ,
  `provider` varchar(60) ,
  `eyeform_id` bigint(20) ,
  `eye` varchar(20) ,
  `urgency` varchar(30) ,
  `comment` varchar(100) ,
  `date` datetime ,
  `status` varchar(2) ,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `eyeform`;
DROP TABLE IF EXISTS `Eyeform`;
CREATE TABLE `Eyeform` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_no` int(11)  ,
  `discharge` varchar(20),
  `stat` varchar(20),
  `opt` varchar(20),
  `date` timestamp ,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `ocularprocedurehis`;
DROP TABLE IF EXISTS `EyeformOcularProcedure`;
CREATE TABLE `EyeformOcularProcedure` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographicNo` int(11) NOT NULL ,
  `provider` varchar(60) ,
  `date` date NOT NULL ,
  `eye` varchar(2) NOT NULL ,
  `procedureName` varchar(100) NOT NULL ,
  `procedureType` varchar(30) ,
  `procedureNote` text,
  `doctor` varchar(30) ,
  `location` varchar(30) ,
  `updateTime` datetime ,
  `status` varchar(2) ,
  `appointmentNo` int(11) ,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `specshis`;
DROP TABLE IF EXISTS `EyeformSpecsHistory`;
CREATE TABLE `EyeformSpecsHistory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographicNo` int(11) NOT NULL ,
  `provider` varchar(60) ,
  `date` date ,
  `doctor` varchar(30) ,
  `type` varchar(30) ,
  `odSph` varchar(10),
  `odCyl` varchar(10) ,
  `odAxis` varchar(10) ,
  `odAdd` varchar(10) ,
  `odPrism` varchar(10) ,
  `osSph` varchar(10) ,
  `osCyl` varchar(10) ,
  `osAxis` varchar(10) ,
  `osAdd` varchar(10) ,
  `osPrism` varchar(10) ,
  `updateTime` datetime ,
  `status` varchar(2) ,
  `appointmentNo` int(11) ,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `eyeform_macro`;
DROP TABLE IF EXISTS `EyeformMacro`;
CREATE TABLE `EyeformMacro` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `label` varchar(255) NOT NULL,
  `displayOrder` tinyint(4) NOT NULL,
  `impression` text,
  `followupNo` smallint(5),
  `followupUnit` varchar(50),
  `followupDoctor` varchar(6),
  `followupReason` varchar(255) ,
  `ticklerStaff` varchar(6) ,
  `billingVisitType` varchar(50) ,
  `billingVisitLocation` varchar(50) ,
  `billingCodes` text,
  `billingDxcode` varchar(50),
  `billingTotal` varchar(50) ,
  `billingComment` varchar(255),
  `billingBilltype` varchar(50)  ,
  `billingPayMethod` varchar(50) ,
  `billingBillto` varchar(50) ,
  `billingRemitto` varchar(50) ,
  `billingGstBilledTotal` varchar(50)  ,
  `billingPayment` varchar(50),
  `billingRefund` varchar(50),
  `billingGst` varchar(50),
  `testRecords` text,
  `dischargeFlag` varchar(20),
  `statFlag` varchar(20),
  `optFlag` varchar(20),
  PRIMARY KEY (`id`)
);

create table RemoteDataRetrievalLog
(
	id bigint primary key auto_increment,
	providerNo varchar(255) not null,
	retrievalDate datetime not null,
	remoteDocumentId varchar(255) not null,
	remoteDocumentContents blob not null
);

DROP TABLE if exists DemographicContact;
create table DemographicContact (
id integer not null auto_increment primary key,
created timestamp not null,
updateDate timestamp not null,
deleted tinyint,
demographicNo integer,
contactId varchar(100),
role varchar(100),
type integer,
sdm varchar(25),
ec varchar(25),
category varchar(100),
KEY (`demographicNo`)
);

DROP TABLE if exists Contact;
create table Contact (
id integer not null auto_increment primary key,
type varchar(20),
updateDate timestamp not null,
lastName varchar(100),
firstName varchar(100),
address varchar(255),
address2 varchar(255),
city varchar(100),
province varchar(25),
country varchar(25),
postal varchar(25),
residencePhone varchar(30),
cellPhone varchar(30),
workPhone varchar(30),
workPhoneExtension varchar(10),
email varchar(50),
fax varchar(30),
note text,
specialty varchar(255),
cpso varchar(10),
systemId varchar(30),
deleted tinyint(1),
KEY (`type`),
KEY (`cpso`),
KEY (`systemId`)
);
alter table EyeformConsultationReport change examination examination text;

alter table measurements change dataField dataField varchar(255);

insert into queue values(1,'default');
insert into secObjectName values('_queue.1','default',0);alter table ctl_billingservice add id int(10) not null auto_increment primary key;

alter table ProviderPreference change defaultDoNotDeleteBilling  defaultDoNotDeleteBilling tinyint(1) not null;
-- this is because on older versions at least (if not current versions still as well as many databases like postgres/oracle, altering a constraint after data exists will leave data that violates the constraint in violation in the database.
update ProviderPreference set defaultDoNotDeleteBilling=0 where defaultDoNotDeleteBilling is null;
create table ProviderPreferenceAppointmentScreenQuickLink(providerNo varchar(6) not null, name varchar(64) not null, url varchar(255) not null);
alter table ProviderPreference change appointmentScreenFormNameDisplayLength appointmentScreenLinkNameDisplayLength int not null;alter table scheduletemplatecode add id integer not null auto_increment primary key;
alter table EyeformMacro change displayOrder displayOrder int(10) not null;
alter table appointment add urgency varchar(30);
alter table appointmentArchive add urgency varchar(30);
alter table drugs add non_authoritative boolean;

update secObjectName set objectName='_pmm.editor' where objectName='_pmm.eidtor';
update secObjPrivilege set objectName='_pmm.editor' where objectName='_pmm.eidtor';

alter table OcanStaffFormData modify answer text not null;alter table allergies add column life_stage char(1);alter table drugs add pickup_datetime datetime;
alter table pharmacyInfo add column serviceLocationIdentifier varchar(255);
alter table billingreferral modify phone varchar(25);
ALTER TABLE appointment MODIFY type VARCHAR(50) NULL;

DROP TABLE IF EXISTS `appointmentType`;
CREATE TABLE `appointmentType` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NULL,
  `notes` varchar(80) NULL,
  `reason` varchar(80) NULL,
  `location` varchar(30) NULL,
  `resources` varchar(10) NULL,
  `duration` int(12),
  PRIMARY KEY (`id`)
);
CREATE TABLE drugReason (
  id int(10) NOT NULL auto_increment,
  drugId int(10) NOT NULL,
  codingSystem varchar(255),
  code varchar(255),
  comments text,
  primaryReasonFlag tinyint(1) NOT NULL,
  archivedFlag tinyint(1) NOT NULL,
  archivedReason text,
  demographicNo int(10) NOT NULL,
  providerNo varchar(6) NOT NULL,
  dateCoded date,
  PRIMARY KEY  (id),
  KEY (drugId),
  KEY (archivedFlag),
  KEY (codingSystem,code)
);
alter table drugs add column eTreatmentType varchar(20);

alter table formRourke2009
change p1_breastFeeding1wNo p1_breastFeeding1wOkConcerns tinyint(1),
add p1_breastFeeding1wNo tinyint(1),
add p1_breastFeeding1wNotDiscussed tinyint(1),
change  p1_formulaFeeding1wNo p1_formulaFeeding1wOkConcerns tinyint(1),
add p1_formulaFeeding1wNo tinyint(1),
add p1_formulaFeeding1wNotDiscussed tinyint(1),
change p1_stoolUrine1wNo p1_stoolUrine1wOkConcerns tinyint(1),
add p1_stoolUrine1wNotDiscussed tinyint(1),
change p1_breastFeeding2wNo p1_breastFeeding2wOkConcerns tinyint(1),
add p1_breastFeeding2wNo tinyint(1),
add p1_breastFeeding2wNotDiscussed tinyint(1),
change p1_formulaFeeding2wNo p1_formulaFeeding2wOkConcerns tinyint(1),
add p1_formulaFeeding2wNo tinyint(1),
add p1_formulaFeeding2wNotDiscussed tinyint(1),
change p1_stoolUrine2wNo p1_stoolUrine2wOkConcerns tinyint(1),
add p1_stoolUrine2wNotDiscussed tinyint(1),
change p1_breastFeeding1mNo p1_breastFeeding1mOkConcerns tinyint(1),
add p1_breastFeeding1mNo tinyint(1),
add p1_breastFeeding1mNotDiscussed tinyint(1),
change p1_formulaFeeding1mNo p1_formulaFeeding1mOkConcerns tinyint(1),
add p1_formulaFeeding1mNo tinyint(1),
add p1_formulaFeeding1mNotDiscussed tinyint(1),
change p1_stoolUrine1mNo p1_stoolUrine1mOkConcerns tinyint(1),
add p1_stoolUrine1mNotDiscussed tinyint(1),
change p1_carSeatNo p1_carSeatOkConcerns tinyint(1),
add p1_carSeatNotDiscussed tinyint(1),
change p1_sleepPosNo p1_sleepPosOkConcerns tinyint(1),
add p1_sleepPosNotDiscussed tinyint(1),
change p1_cribSafetyNo p1_cribSafetyOkConcerns tinyint(1),
add p1_cribSafetyNotDiscussed tinyint(1),
change p1_firearmSafetyNo p1_firearmSafetyOkConcerns tinyint(1),
add p1_firearmSafetyNotDiscussed tinyint(1),
change p1_smokeSafetyNo p1_smokeSafetyOkConcerns tinyint(1),
add p1_smokeSafetyNotDiscussed tinyint(1),
change p1_hotWaterNo p1_hotWaterOkConcerns tinyint(1),
add p1_hotWaterNotDiscussed tinyint(1),
change p1_safeToysNo p1_safeToysOkConcerns tinyint(1),
add p1_safeToysNotDiscussed tinyint(1),
change p1_sleepCryNo p1_sleepCryOkConcerns tinyint(1),
add p1_sleepCryNotDiscussed tinyint(1),
change p1_soothabilityNo p1_soothabilityOkConcerns tinyint(1),
add p1_soothabilityNotDiscussed tinyint(1),
change p1_bondingNo p1_bondingOkConcerns tinyint(1),
add p1_bondingNotDiscussed tinyint(1),
change p1_pFatigueNo p1_pFatigueOkConcerns tinyint(1),
add p1_pFatigueNotDiscussed tinyint(1),
change p1_famConflictNo p1_famConflictOkConcerns tinyint(1),
add p1_famConflictNotDiscussed tinyint(1),
change p1_siblingsNo p1_siblingsOkConcerns tinyint(1),
add p1_siblingsNotDiscussed tinyint(1),
change p1_homeVisitNo p1_homeVisitOkConcerns tinyint(1),
add p1_homeVisitNotDiscussed tinyint(1),
change p1_2ndSmokeNo p1_2ndSmokeOkConcerns tinyint(1),
add p1_2ndSmokeNotDiscussed tinyint(1),
change p1_altMedNo p1_altMedOkConcerns tinyint(1),
add p1_altMedNotDiscussed tinyint(1),
change p1_pacifierNo p1_pacifierOkConcerns tinyint(1),
add p1_pacifierNotDiscussed tinyint(1),
change p1_feverNo p1_feverOkConcerns tinyint(1),
add p1_feverNotDiscussed tinyint(1),
change p1_tmpControlNo p1_tmpControlOkConcerns tinyint(1),
add p1_tmpControlNotDiscussed tinyint(1),
change p1_sunExposureNo p1_sunExposureOkConcerns tinyint(1),
add p1_sunExposureNotDiscussed tinyint(1),
change p1_noCoughMedNo p1_noCoughMedOkConcerns tinyint(1),
add p1_noCoughMedNotDiscussed tinyint(1),
add p1_sucks2wNotDiscussed tinyint(1),
add p1_noParentsConcerns2wNotDiscussed tinyint(1),
add p1_focusGaze1mNotDiscussed tinyint(1),
add p1_startles1mNotDiscussed tinyint(1),
add p1_calms1mNotDiscussed tinyint(1),
add p1_sucks1mNotDiscussed tinyint(1),
add p1_noParentsConcerns1mNotDiscussed tinyint(1),
add p1_skin1wNotDiscussed tinyint(1),
add p1_fontanelles1wNotDiscussed tinyint(1),
add p1_eyes1wNotDiscussed tinyint(1),
add p1_ears1wNotDiscussed tinyint(1),
add p1_heartLungs1wNotDiscussed tinyint(1),
add p1_umbilicus1wNotDiscussed tinyint(1),
add p1_femoralPulses1wNotDiscussed tinyint(1),
add p1_hips1wNotDiscussed tinyint(1),
add p1_muscleTone1wNotDiscussed tinyint(1),
add p1_testicles1wNotDiscussed tinyint(1),
add p1_maleUrinary1wNotDiscussed tinyint(1),
add p1_skin2wNotDiscussed tinyint(1),
add p1_fontanelles2wNotDiscussed tinyint(1),
add p1_eyes2wNotDiscussed tinyint(1),
add p1_ears2wNotDiscussed tinyint(1),
add p1_heartLungs2wNotDiscussed tinyint(1),
add p1_umbilicus2wNotDiscussed tinyint(1),
add p1_femoralPulses2wNotDiscussed tinyint(1),
add p1_hips2wNotDiscussed tinyint(1),
add p1_muscleTone2wNotDiscussed tinyint(1),
add p1_testicles2wNotDiscussed tinyint(1),
add p1_maleUrinary2wNotDiscussed tinyint(1),
add p1_skin1mNotDiscussed tinyint(1),
add p1_fontanelles1mNotDiscussed tinyint(1),
add p1_eyes1mNotDiscussed tinyint(1),
add p1_corneal1mNotDiscussed tinyint(1),
add p1_hearing1mNotDiscussed tinyint(1),
add p1_heart1mNotDiscussed tinyint(1),
add p1_hips1mNotDiscussed tinyint(1),
add p1_muscleTone1mNotDiscussed tinyint(1),
change p1_pkuThyroid1w p1_pkuThyroid1wOkConcerns tinyint(1),
add p1_pkuThyroid1wOk tinyint(1),
add p1_pkuThyroid1wNotDiscussed tinyint(1),
change p1_hemoScreen1w p1_hemoScreen1wOkConcerns tinyint(1),
add p1_hemoScreen1wOk tinyint(1),
add p1_hemoScreen1wNotDiscussed tinyint(1),
change p1_hepatitisVaccine1w p1_hepatitisVaccine1wOk tinyint(1),
add p1_hepatitisVaccine1wNo tinyint(1),
change p1_hepatitisVaccine1m p1_hepatitisVaccine1mOk tinyint(1),
add p1_hepatitisVaccine1mNo tinyint(1),
change p2_breastFeeding2mNo p2_breastFeeding2mOkConcerns tinyint(1),
add p2_breastFeeding2mNo tinyint(1),
add p2_breastFeeding2mNotDiscussed tinyint(1),
change p2_formulaFeeding2mNo p2_formulaFeeding2mOkConcerns tinyint(1),
add p2_formulaFeeding2mNo tinyint(1),
add p2_formulaFeeding2mNotDiscussed tinyint(1),
change p2_breastFeeding4mNo p2_breastFeeding4mOkConcerns tinyint(1),
add p2_breastFeeding4mNo tinyint(1),
add p2_breastFeeding4mNotDiscussed tinyint(1),
change p2_formulaFeeding4mNo p2_formulaFeeding4mOkConcerns tinyint(1),
add p2_formulaFeeding4mNo tinyint(1),
add p2_formulaFeeding4mNotDiscussed tinyint(1),
change p2_breastFeeding6mNo p2_breastFeeding6mOkConcerns tinyint(1),
add p2_breastFeeding6mNo tinyint(1),
add p2_breastFeeding6mNotDiscussed tinyint(1),
change p2_formulaFeeding6mNo p2_formulaFeeding6mOkConcerns tinyint(1),
add p2_formulaFeeding6mNo tinyint(1),
add p2_formulaFeeding6mNotDiscussed tinyint(1),
change p2_bottle6mNo p2_bottle6mOkConcerns tinyint(1),
add p2_bottle6mNotDiscussed tinyint(1),
change p2_liquids6mNo p2_liquids6mOkConcerns tinyint(1),
add p2_liquids6mNo tinyint(1),
add p2_liquids6mNotDiscussed tinyint(1),
change p2_iron6mNo p2_iron6mOkConcerns tinyint(1),
add p2_iron6mNotDiscussed tinyint(1),
change p2_vegFruit6mNo p2_vegFruit6mOkConcerns tinyint(1),
add p2_vegFruit6mNotDiscussed tinyint(1),
change p2_egg6mNo p2_egg6mOkConcerns tinyint(1),
add p2_egg6mNotDiscussed tinyint(1),
change p2_choking6mNo p2_choking6mOkConcerns tinyint(1),
add p2_choking6mNotDiscussed tinyint(1),
change p2_carSeatNo p2_carSeatOkConcerns tinyint(1),
add p2_carSeatNotDiscussed tinyint(1),
change p2_sleepPosNo p2_sleepPosOkConcerns tinyint(1),
add p2_sleepPosNotDiscussed tinyint(1),
change p2_poisonsNo p2_poisonsOkConcerns tinyint(1),
add p2_poisonsNotDiscussed tinyint(1),
change p2_firearmSafetyNo p2_firearmSafetyOkConcerns tinyint(1),
add p2_firearmSafetyNotDiscussed tinyint(1),
change p2_electricNo p2_electricOkConcerns tinyint(1),
add p2_electricNotDiscussed tinyint(1),
change p2_smokeSafetyNo p2_smokeSafetyOkConcerns tinyint(1),
add p2_smokeSafetyNotDiscussed tinyint(1),
change p2_hotWaterNo p2_hotWaterOkConcerns tinyint(1),
add p2_hotWaterNotDiscussed tinyint(1),
change p2_fallsNo p2_fallsp2_sleepCryOkConcernsOkConcerns tinyint(1),
add p2_fallsNotDiscussed tinyint(1),
change p2_safeToysNo p2_safeToysOkConcerns tinyint(1),
add p2_safeToysNotDiscussed tinyint(1),
change p2_sleepCryNo p2_sleepCryOkConcerns tinyint(1),
add p2_sleepCryNotDiscussed tinyint(1),
change p2_soothabilityNo p2_soothabilityOkConcerns tinyint(1),
add p2_soothabilityNotDiscussed tinyint(1),
change p2_homeVisitNo p2_homeVisitOkConcerns tinyint(1),
add p2_homeVisitNotDiscussed tinyint(1),
change p2_bondingNo p2_bondingOkConcerns tinyint(1),
add p2_bondingNotDiscussed tinyint(1),
change p2_pFatigueNo p2_pFatigueOkConcerns tinyint(1),
add p2_pFatigueNotDiscussed tinyint(1),
change p2_famConflictNo p2_famConflictOkConcerns tinyint(1),
add p2_famConflictNotDiscussed tinyint(1),
change p2_siblingsNo p2_siblingsOkConcerns tinyint(1),
add p2_siblingsNotDiscussed tinyint(1),
change p2_childCareNo p2_childCareOkConcerns tinyint(1),
add p2_childCareNotDiscussed tinyint(1),
change p2_2ndSmokeNo p2_2ndSmokeOkConcerns tinyint(1),
add p2_2ndSmokeNotDiscussed tinyint(1),
change p2_teethingNo p2_teethingOkConcerns tinyint(1),
add p2_teethingNotDiscussed tinyint(1),
change p2_altMedNo p2_altMedOkConcerns tinyint(1),
add p2_altMedNotDiscussed tinyint(1),
change p2_pacifierNo p2_pacifierOkConcerns tinyint(1),
add p2_pacifierNotDiscussed tinyint(1),
change p2_tmpControlNo p2_tmpControlOkConcerns tinyint(1),
add p2_tmpControlNotDiscussed tinyint(1),
change p2_feverNo p2_feverOkConcerns tinyint(1),
add p2_feverNotDiscussed tinyint(1),
change p2_sunExposureNo p2_sunExposureOkConcerns tinyint(1),
add p2_sunExposureNotDiscussed tinyint(1),
change p2_pesticidesNo p2_pesticidesOkConcerns tinyint(1),
add p2_pesticidesNotDiscussed tinyint(1),
change p2_readingNo p2_readingOkConcerns tinyint(1),
add p2_readingNotDiscussed tinyint(1),
change p2_noCoughMedNo p2_noCoughMedOkConcerns tinyint(1),
add p2_noCoughMedNotDiscussed tinyint(1),
change p2_eyesNo p2_eyesOkConcerns tinyint(1),
add p2_eyesNotDiscussed tinyint(1),
change p2_coosNo p2_coosOkConcerns tinyint(1),
add p2_coosNotDiscussed tinyint(1),
change p2_headUpTummyNo p2_headUpTummyOkConcerns tinyint(1),
add p2_headUpTummyNotDiscussed tinyint(1),
change p2_cuddledNo p2_cuddledOkConcerns tinyint(1),
add p2_cuddledNotDiscussed tinyint(1),
change p2_2sucksNo p2_2sucksOkConcerns tinyint(1),
add p2_2sucksNotDiscussed tinyint(1),
change p2_smilesNo p2_smilesOkConcerns tinyint(1),
add p2_smilesNotDiscussed tinyint(1),
change p2_noParentsConcerns2mNo p2_noParentsConcerns2mOkConcerns tinyint(1),
add p2_noParentsConcerns2mNotDiscussed tinyint(1),
change p2_movingObjNo p2_movingObjOkConcerns tinyint(1),
add p2_movingObjNotDiscussed tinyint(1),
change p2_respondsNo p2_respondsOkConcerns tinyint(1),
add p2_respondsNotDiscussed tinyint(1),
change p2_headSteadyNo p2_headSteadyOkConcerns tinyint(1),
add p2_headSteadyNotDiscussed tinyint(1),
change p2_holdsObjNo p2_holdsObjOkConcerns tinyint(1),
add p2_holdsObjNotDiscussed tinyint(1),
change p2_laughsNo p2_laughsOkConcerns tinyint(1),
add p2_laughsNotDiscussed tinyint(1),
change p2_noParentsConcerns4mNo p2_noParentsConcerns4mOkConcerns tinyint(1),
add p2_noParentsConcerns4mNotDiscussed tinyint(1),
change p2_turnsHeadNo p2_turnsHeadOkConcerns tinyint(1),
add p2_turnsHeadNotDiscussed tinyint(1),
change p2_makesSoundNo p2_makesSoundOkConcerns tinyint(1),
add p2_makesSoundNotDiscussed tinyint(1),
change p2_vocalizesNo p2_vocalizesOkConcerns tinyint(1),
add p2_vocalizesNotDiscussed tinyint(1),
change p2_rollsNo p2_rollsOkConcerns tinyint(1),
add p2_rollsNotDiscussed tinyint(1),
change p2_sitsNo p2_sitsOkConcerns tinyint(1),
add p2_sitsNotDiscussed tinyint(1),
change p2_reachesGraspsNo p2_reachesGraspsOkConcerns tinyint(1),
add p2_reachesGraspsNotDiscussed tinyint(1),
change p2_noParentsConcerns6mNo p2_noParentsConcerns6mOkConcerns tinyint(1),
add p2_noParentsConcerns6mNotDiscussed tinyint(1),
change p2_fontanelles2mNo p2_fontanelles2mOkConcerns tinyint(1),
add p2_fontanelles2mNotDiscussed tinyint(1),
change p2_eyes2mNo p2_eyes2mOkConcerns tinyint(1),
add p2_eyes2mNotDiscussed tinyint(1),
change p2_corneal2mNo p2_corneal2mOkConcerns tinyint(1),
add p2_corneal2mNotDiscussed tinyint(1),
change p2_hearing2mNo p2_hearing2mOkConcerns tinyint(1),
add p2_hearing2mNotDiscussed tinyint(1),
change p2_heart2mNo p2_heart2mOkConcerns tinyint(1),
add p2_heart2mNotDiscussed tinyint(1),
change p2_hips2mNo p2_hips2mOkConcerns tinyint(1),
add p2_hips2mNotDiscussed tinyint(1),
change p2_muscleTone2mNo p2_muscleTone2mOkConcerns tinyint(1),
add p2_muscleTone2mNotDiscussed tinyint(1),
change p2_fontanelles4mNo p2_fontanelles4mOkConcerns tinyint(1),
add p2_fontanelles4mNotDiscussed tinyint(1),
change p2_eyes4mNo p2_eyes4mOkConcerns tinyint(1),
add p2_eyes4mNotDiscussed tinyint(1),
change p2_corneal4mNo p2_corneal4mOkConcerns tinyint(1),
add p2_corneal4mNotDiscussed tinyint(1),
change p2_hearing4mNo p2_hearing4mOkConcerns tinyint(1),
add p2_hearing4mNotDiscussed tinyint(1),
change p2_hips4mNo p2_hips4mOkConcerns tinyint(1),
add p2_hips4mNotDiscussed tinyint(1),
change p2_muscleTone4mNo p2_muscleTone4mOkConcerns tinyint(1),
add p2_muscleTone4mNotDiscussed tinyint(1),
change p2_fontanelles6mNo p2_fontanelles6mOkConcerns tinyint(1),
add p2_fontanelles6mNotDiscussed tinyint(1),
change p2_eyes6mNo p2_eyes6mOkConcerns tinyint(1),
add p2_eyes6mNotDiscussed tinyint(1),
change p2_corneal6mNo p2_corneal6mOkConcerns tinyint(1),
add p2_corneal6mNotDiscussed tinyint(1),
change p2_hearing6mNo p2_hearing6mOkConcerns tinyint(1),
add p2_hearing6mNotDiscussed tinyint(1),
change p2_hips6mNo p2_hips6mOkConcerns tinyint(1),
add p2_hips6mNotDiscussed tinyint(1),
change p2_muscleTone6mNo p2_muscleTone6mOkConcerns tinyint(1),
add p2_muscleTone6mNotDiscussed tinyint(1),
change p2_hepatitisVaccine6m p2_hepatitisVaccine6mOk tinyint(1),
add p2_hepatitisVaccine6mNo tinyint(1),
change p2_tb6m p2_tb6mOkConcerns tinyint(1),
add p2_tb6mOk tinyint(1),
add p2_tb6mNotDiscussed tinyint(1),
change p3_breastFeeding9mNo p3_breastFeeding9mOkConcerns tinyint(1),
add p3_breastFeeding9mNo tinyint(1),
add p3_breastFeeding9mNotDiscussed tinyint(1),
change p3_formulaFeeding9mNo p3_formulaFeeding9mOkConcerns tinyint(1),
add p3_formulaFeeding9mNo tinyint(1),
add p3_formulaFeeding9mNotDiscussed tinyint(1),
change p3_bottle9mNo p3_bottle9mOkConcerns tinyint(1),
add p3_bottle9mNotDiscussed tinyint(1),
change p3_liquids9mNo p3_liquids9mOkConcerns tinyint(1),
add p3_liquids9mNotDiscussed tinyint(1),
change p3_cereal9mNo p3_cereal9mOkConcerns tinyint(1),
add p3_cereal9mNotDiscussed tinyint(1),
change p3_introCowMilk9mNo p3_introCowMilk9mOkConcerns tinyint(1),
add p3_introCowMilk9mNotDiscussed tinyint(1),
change p3_egg9mNo p3_egg9mOkConcerns tinyint(1),
add p3_egg9mNotDiscussed tinyint(1),
change p3_choking9mNo p3_choking9mOkConcerns tinyint(1),
add p3_choking9mNotDiscussed tinyint(1),
change p3_breastFeeding12mNo p3_breastFeeding12mOkConcerns tinyint(1),
add p3_breastFeeding12mNo tinyint(1),
add p3_breastFeeding12mNotDiscussed tinyint(1),
change p3_homoMilk12mNo p3_homoMilk12mOkConcerns tinyint(1),
add p3_homoMilk12mNo tinyint(1),
add p3_homoMilk12mNotDiscussed tinyint(1),
change p3_cup12mNo p3_cup12mOkConcerns tinyint(1),
add p3_cup12mNotDiscussed tinyint(1),
change p3_appetite12mNo p3_appetite12mOkConcerns tinyint(1),
add p3_appetite12mNotDiscussed tinyint(1),
change p3_choking12mNo p3_choking12mOkConcerns tinyint(1),
add p3_choking12mNotDiscussed tinyint(1),
change p3_breastFeeding15mNo p3_breastFeeding15mOkConcerns tinyint(1),
add p3_breastFeeding15mNo tinyint(1),
add p3_breastFeeding15mNotDiscussed tinyint(1),
change p3_homoMilk15mNo p3_homoMilk15mOkConcerns tinyint(1),
add p3_homoMilk15mNo tinyint(1),
add p3_homoMilk15mNotDiscussed tinyint(1),
change p3_choking15mNo p3_choking15mOkConcerns tinyint(1),
add p3_choking15mNotDiscussed tinyint(1),
change p3_cup15mNo p3_cup15mOkConcerns tinyint(1),
add p3_cup15mNotDiscussed tinyint(1),
change p3_poisonsNo p3_poisonsOkConcerns tinyint(1),
add p3_poisonsNotDiscussed tinyint(1),
change p3_carSeatNo p3_carSeatOkConcerns tinyint(1),
add p3_carSeatNotDiscussed tinyint(1),
change p3_firearmSafetyNo p3_firearmSafetyOkConcerns tinyint(1),
add p3_firearmSafetyNotDiscussed tinyint(1),
change p3_smokeSafetyNo p3_smokeSafetyOkConcerns tinyint(1),
add p3_smokeSafetyNotDiscussed tinyint(1),
change p3_hotWaterNo p3_hotWaterOkConcerns tinyint(1),
add p3_hotWaterNotDiscussed tinyint(1),
change p3_electricNo p3_electricOkConcerns tinyint(1),
add p3_electricNotDiscussed tinyint(1),
change p3_fallsNo p3_fallsOkConcerns tinyint(1),
add p3_fallsNotDiscussed tinyint(1),
change p3_safeToysNo p3_safeToysOkConcerns tinyint(1),
add p3_safeToysNotDiscussed tinyint(1),
change p3_sleepCryNo p3_sleepCryOkConcerns tinyint(1),
add p3_sleepCryNotDiscussed tinyint(1),
change p3_soothabilityNo p3_soothabilityOkConcerns tinyint(1),
add p3_soothabilityNotDiscussed tinyint(1),
change p3_homeVisitNo p3_homeVisitOkConcerns tinyint(1),
add p3_homeVisitNotDiscussed tinyint(1),
change p3_parentingNo p3_parentingOkConcerns tinyint(1),
add p3_parentingNotDiscussed tinyint(1),
change p3_pFatigueNo p3_pFatigueOkConcerns tinyint(1),
add p3_pFatigueNotDiscussed tinyint(1),
change p3_famConflictNo p3_famConflictOkConcerns tinyint(1),
add p3_famConflictNotDiscussed tinyint(1),
change p3_siblingsNo p3_siblingsOkConcerns tinyint(1),
add p3_siblingsNotDiscussed tinyint(1),
change p3_childCareNo p3_childCareOkConcerns tinyint(1),
add p3_childCareNotDiscussed tinyint(1),
change p3_2ndSmokeNo p3_2ndSmokeOkConcerns tinyint(1),
add p3_2ndSmokeNotDiscussed tinyint(1),
change p3_teethingNo p3_teethingOkConcerns tinyint(1),
add p3_teethingNotDiscussed tinyint(1),
change p3_altMedNo p3_altMedOkConcerns tinyint(1),
add p3_altMedNotDiscussed tinyint(1),
change p3_pacifierNo p3_pacifierOkConcerns tinyint(1),
add p3_pacifierNotDiscussed tinyint(1),
change p3_feverNo p3_feverOkConcerns tinyint(1),
add p3_feverNotDiscussed tinyint(1),
change p3_activeNo p3_activeOkConcerns tinyint(1),
add p3_activeNotDiscussed tinyint(1),
change p3_readingNo p3_readingOkConcerns tinyint(1),
add p3_readingNotDiscussed tinyint(1),
change p3_footwearNo p3_footwearOkConcerns tinyint(1),
add p3_footwearNotDiscussed tinyint(1),
change p3_coughMedNo p3_coughMedOkConcerns tinyint(1),
add p3_coughMedNotDiscussed tinyint(1),
change p3_sunExposureNo p3_sunExposureOkConcerns tinyint(1),
add p3_sunExposureNotDiscussed tinyint(1),
change p3_checkSerumNo p3_checkSerumOkConcerns tinyint(1),
add p3_checkSerumNotDiscussed tinyint(1),
change p3_pesticidesNo p3_pesticidesOkConcerns tinyint(1),
add p3_pesticidesNotDiscussed tinyint(1),
change p3_hiddenToyNo p3_hiddenToyOkConcerns tinyint(1),
add p3_hiddenToyNotDiscussed tinyint(1),
change p3_soundsNo p3_soundsOkConcerns tinyint(1),
add p3_soundsNotDiscussed tinyint(1),
change p3_responds2peopleNo p3_responds2peopleOkConcerns tinyint(1),
add p3_responds2peopleNotDiscussed tinyint(1),
change p3_makeSoundsNo p3_makeSoundsOkConcerns tinyint(1),
add p3_makeSoundsNotDiscussed tinyint(1),
change p3_sitsNo p3_sitsOkConcerns tinyint(1),
add p3_sitsNotDiscussed tinyint(1),
change p3_standsNo p3_standsOkConcerns tinyint(1),
add p3_standsNotDiscussed tinyint(1),
change p3_thumbNo p3_thumbOkConcerns tinyint(1),
add p3_thumbNotDiscussed tinyint(1),
change p3_playGamesNo p3_playGamesOkConcerns tinyint(1),
add p3_playGamesNotDiscussed tinyint(1),
change p3_attention9mNo p3_attention9mOkConcerns tinyint(1),
add p3_attention9mNotDiscussed tinyint(1),
change p3_noParentsConcerns9mNo p3_noParentsConcerns9mOkConcerns tinyint(1),
add p3_noParentsConcerns9mNotDiscussed tinyint(1),
change p3_respondsNo p3_respondsOkConcerns tinyint(1),
add p3_respondsNotDiscussed tinyint(1),
change p3_simpleRequestsNo p3_simpleRequestsOkConcerns tinyint(1),
add p3_simpleRequestsNotDiscussed tinyint(1),
change p3_consonantNo p3_consonantOkConcerns tinyint(1),
add p3_consonantNotDiscussed tinyint(1),
change p3_says3wordsNo p3_says3wordsOkConcerns tinyint(1),
add p3_says3wordsNotDiscussed tinyint(1),
change p3_shufflesNo p3_shufflesOkConcerns tinyint(1),
add p3_shufflesNotDiscussed tinyint(1),
change p3_pull2standNo p3_pull2standOkConcerns tinyint(1),
change p3_showDistressNo p3_showDistressOkConcerns tinyint(1),
add p3_showDistressNotDiscussed tinyint(1),
add p3_pull2standNotDiscussed tinyint(1),
change p3_followGazeNo p3_followGazeOkConcerns tinyint(1),
add p3_followGazeNotDiscussed tinyint(1),
change p3_noParentsConcerns12mNo p3_noParentsConcerns12mOkConcerns tinyint(1),
add p3_noParentsConcerns12mNotDiscussed tinyint(1),
change p3_says5wordsNo p3_says5wordsOkConcerns tinyint(1),
add p3_says5wordsNotDiscussed tinyint(1),
change p3_fingerFoodsNo p3_fingerFoodsOkConcerns tinyint(1),
add p3_fingerFoodsNotDiscussed tinyint(1),
change p3_walksSidewaysNo p3_walksSidewaysOkConcerns tinyint(1),
add p3_walksSidewaysNotDiscussed tinyint(1),
change p3_showsFearStrangersNo p3_showsFearStrangersOkConcerns tinyint(1),
add p3_showsFearStrangersNotDiscussed tinyint(1),
change p3_crawlsStairsNo p3_crawlsStairsOkConcerns tinyint(1),
add p3_crawlsStairsNotDiscussed tinyint(1),
change p3_squatsNo p3_squatsOkConcerns tinyint(1),
add p3_squatsNotDiscussed tinyint(1),
change p3_noParentsConcerns15mNo p3_noParentsConcerns15mOkConcerns tinyint(1),
add p3_noParentsConcerns15mNotDiscussed tinyint(1),
change p3_fontanelles9mNo p3_fontanelles9mOkConcerns tinyint(1),
add p3_fontanelles9mNotDiscussed tinyint(1),
change p3_eyes9mNo p3_eyes9mOkConcerns tinyint(1),
add p3_eyes9mNotDiscussed tinyint(1),
change p3_corneal9mNo p3_corneal9mOkConcerns tinyint(1),
add p3_corneal9mNotDiscussed tinyint(1),
change p3_hearing9mNo p3_hearing9mOkConcerns tinyint(1),
add p3_hearing9mNotDiscussed tinyint(1),
change p3_hips9mNo p3_hips9mOkConcerns tinyint(1),
add p3_hips9mNotDiscussed tinyint(1),
change p3_fontanelles12mNo p3_fontanelles12mOkConcerns tinyint(1),
add p3_fontanelles12mNotDiscussed tinyint(1),
change p3_eyes12mNo p3_eyes12mOkConcerns tinyint(1),
add p3_eyes12mNotDiscussed tinyint(1),
change p3_corneal12mNo p3_corneal12mOkConcerns tinyint(1),
add p3_corneal12mNotDiscussed tinyint(1),
change p3_hearing12mNo p3_hearing12mOkConcerns tinyint(1),
add p3_hearing12mNotDiscussed tinyint(1),
change p3_tonsil12mNo p3_tonsil12mOkConcerns tinyint(1),
add p3_tonsil12mNotDiscussed tinyint(1),
change p3_hips12mNo p3_hips12mOkConcerns tinyint(1),
add p3_hips12mNotDiscussed tinyint(1),
change p3_fontanelles15mNo p3_fontanelles15mOkConcerns tinyint(1),
add p3_fontanelles15mNotDiscussed tinyint(1),
change p3_eyes15mNo p3_eyes15mOkConcerns tinyint(1),
add p3_eyes15mNotDiscussed tinyint(1),
change p3_corneal15mNo p3_corneal15mOkConcerns tinyint(1),
add p3_corneal15mNotDiscussed tinyint(1),
change p3_hearing15mNo p3_hearing15mOkConcerns tinyint(1),
add p3_hearing15mNotDiscussed tinyint(1),
change p3_tonsil15mNo p3_tonsil15mOkConcerns tinyint(1),
add p3_tonsil15mNotDiscussed tinyint(1),
change p3_hips15mNo p3_hips15mOkConcerns tinyint(1),
add p3_hips15mNotDiscussed tinyint(1),
change p3_antiHB9m p3_antiHB9mOkConcerns tinyint(1),
add p3_antiHB9mNotDiscussed tinyint(1),
add p3_antiHB9mOk tinyint(1),
change p3_hemoglobin9m p3_hemoglobin9mOkConcerns tinyint(1),
add p3_hemoglobin9mOk tinyint(1),
add p3_hemoglobin9mNotDiscussed tinyint(1),
change p3_hemoglobin12m p3_hemoglobin12mOkConcerns tinyint(1),
add p3_hemoglobin12mOk tinyint(1),
add p3_hemoglobin12mNotDiscussed tinyint(1),
change p4_breastFeeding18mNo p4_breastFeeding18mOkConcerns tinyint(1),
add p4_breastFeeding18mNo tinyint(1),
add p4_breastFeeding18mNotDiscussed tinyint(1),
change p4_homoMilk18mNo p4_homoMilk18mOkConcerns tinyint(1),
add p4_homoMilk18mNo tinyint(1),
add p4_homoMilk18mNotDiscussed tinyint(1),
change p4_bottle18mNo p4_bottle18mOkConcerns tinyint(1),
add p4_bottle18mNotDiscussed tinyint(1),
change p4_homo2percent24mNo p4_homo2percent24mOkConcerns tinyint(1),
add p4_homo2percent24mNo tinyint(1),
add p4_homo2percent24mNotDiscussed tinyint(1),
change p4_lowerfatdiet24mNo p4_lowerfatdiet24mOkConcerns tinyint(1),
add p4_lowerfatdiet24mNotDiscussed tinyint(1),
add p4_foodguide24mNotDiscussed tinyint(1),
change p4_2pMilk48mNo p4_2pMilk48mOkConcerns tinyint(1),
add p4_2pMilk48mNo tinyint(1),
add p4_2pMilk48mNotDiscussed tinyint(1),
add p4_foodguide48mNotDiscussed tinyint(1),
change p4_carSeat18mNo p4_carSeat18mOkConcerns tinyint(1),
add p4_carSeat18mNotDiscussed tinyint(1),
change  p4_bathSafetyNo p4_bathSafetyOkConcerns tinyint(1),
add p4_bathSafetyNotDiscussed tinyint(1),
change p4_safeToysNo p4_safeToysOkConcerns tinyint(1),
add p4_safeToysNotDiscussed tinyint(1),
change p4_parentChild18mNo p4_parentChild18mOkConcerns tinyint(1),
add p4_parentChild18mNotDiscussed tinyint(1),
change p4_discipline18mNo p4_discipline18mOkConcerns tinyint(1),
add p4_discipline18mNotDiscussed tinyint(1),
change p4_pFatigue18mNo p4_pFatigue18mOkConcerns tinyint(1),
add p4_pFatigue18mNotDiscussed tinyint(1),
change p4_highRisk18mNo p4_highRisk18mOkConcerns tinyint(1),
add p4_highRisk18mNotDiscussed tinyint(1),
change p4_socializing18mNo p4_socializing18mOkConcerns tinyint(1),
add p4_socializing18mNotDiscussed tinyint(1),
change p4_weanPacifier18mNo p4_weanPacifier18mOkConcerns tinyint(1),
add p4_weanPacifier18mNotDiscussed tinyint(1),
change p4_dentalCareNo p4_dentalCareOkConcerns tinyint(1),
add p4_dentalCareNotDiscussed tinyint(1),
change p4_toiletLearning18mNo p4_toiletLearning18mOkConcerns tinyint(1),
add p4_toiletLearning18mNotDiscussed tinyint(1),
change p4_encourageReading18mNo p4_encourageReading18mOkConcerns tinyint(1),
add p4_encourageReading18mNotDiscussed tinyint(1),
change p4_carSeat24mNo p4_carSeat24mOkConcerns tinyint(1),
add p4_carSeat24mNotDiscussed tinyint(1),
change p4_bikeHelmetsNo p4_bikeHelmetsOkConcerns tinyint(1),
add p4_bikeHelmetsNotDiscussed tinyint(1),
change p4_firearmSafetyNo p4_firearmSafetyOkConcerns tinyint(1),
add p4_firearmSafetyNotDiscussed tinyint(1),
change p4_smokeSafetyNo p4_smokeSafetyOkConcerns tinyint(1),
add p4_smokeSafetyNotDiscussed tinyint(1),
change p4_matchesNo p4_matchesOkConcerns tinyint(1),
add p4_matchesNotDiscussed tinyint(1),
change p4_waterSafetyNo p4_waterSafetyOkConcerns tinyint(1),
add p4_waterSafetyNotDiscussed tinyint(1),
change p4_parentChild24mNo p4_parentChild24mOkConcerns tinyint(1),
add p4_parentChild24mNotDiscussed tinyint(1),
change p4_discipline24mNo p4_discipline24mOkConcerns tinyint(1),
add p4_discipline24mNotDiscussed tinyint(1),
change p4_highRisk24mNo p4_highRisk24mOkConcerns tinyint(1),
add p4_highRisk24mNotDiscussed tinyint(1),
change p4_pFatigue24mNo p4_pFatigue24mOkConcerns tinyint(1),
add p4_pFatigue24mNotDiscussed tinyint(1),
change p4_famConflictNo p4_famConflictOkConcerns tinyint(1),
add p4_famConflictNotDiscussed tinyint(1),
change p4_siblingsNo p4_siblingsOkConcerns tinyint(1),
add p4_siblingsNotDiscussed tinyint(1),
change p4_2ndSmokeNo p4_2ndSmokeOkConcerns tinyint(1),
add p4_2ndSmokeNotDiscussed tinyint(1),
change p4_dentalCleaningNo p4_dentalCleaningOkConcerns tinyint(1),
add p4_dentalCleaningNotDiscussed tinyint(1),
change p4_noPacifier24mNo p4_noPacifier24mOkConcerns tinyint(1),
add p4_noPacifier24mNotDiscussed tinyint(1),
change p4_altMedNo p4_altMedOkConcerns tinyint(1),
add p4_altMedNotDiscussed tinyint(1),
change p4_toiletLearning24mNo p4_toiletLearning24mOkConcerns tinyint(1),
add p4_toiletLearning24mNotDiscussed tinyint(1),
change p4_noCough24mNo p4_noCough24mOkConcerns tinyint(1),
add p4_noCough24mNotDiscussed tinyint(1),
change p4_activeNo p4_activeOkConcerns tinyint(1),
add p4_activeNotDiscussed tinyint(1),
change p4_socializing24mNo p4_socializing24mOkConcerns tinyint(1),
add p4_socializing24mNotDiscussed tinyint(1),
change p4_readingNo p4_readingOkConcerns tinyint(1),
add p4_readingNotDiscussed tinyint(1),
change p4_dayCareNo p4_dayCareOkConcerns tinyint(1),
add p4_dayCareNotDiscussed tinyint(1),
change p4_sunExposureNo p4_sunExposureOkConcerns tinyint(1),
add p4_sunExposureNotDiscussed tinyint(1),
change p4_pesticidesNo p4_pesticidesOkConcerns tinyint(1),
add p4_pesticidesNotDiscussed tinyint(1),
change p4_checkSerumNo p4_checkSerumOkConcerns tinyint(1),
add p4_checkSerumNotDiscussed tinyint(1),
change p4_manageableNo p4_manageableOkConcerns tinyint(1),
add p4_manageableNotDiscussed tinyint(1),
change p4_otherChildrenNo p4_otherChildrenOkConcerns tinyint(1),
add p4_otherChildrenNotDiscussed tinyint(1),
change p4_soothabilityNo p4_soothabilityOkConcerns tinyint(1),
add p4_soothabilityNotDiscussed tinyint(1),
change p4_comfortNo p4_comfortOkConcerns tinyint(1),
add p4_comfortNotDiscussed tinyint(1),
change p4_pointsNo p4_pointsOkConcerns tinyint(1),
add p4_pointsNotDiscussed tinyint(1),
change p4_getAttnNo p4_getAttnOkConcerns tinyint(1),
add p4_getAttnNotDiscussed tinyint(1),
change p4_recsNameNo p4_recsNameOkConcerns tinyint(1),
add p4_recsNameNotDiscussed tinyint(1),
change p4_points2wantNo p4_points2wantOkConcerns tinyint(1),
add p4_points2wantNotDiscussed tinyint(1),
change p4_looks4toyNo p4_looks4toyOkConcerns tinyint(1),
add p4_looks4toyNotDiscussed tinyint(1),
change p4_initSpeechNo p4_initSpeechOkConcerns tinyint(1),
add p4_initSpeechNotDiscussed tinyint(1),
change p4_says20wordsNo p4_says20wordsOkConcerns tinyint(1),
add p4_says20wordsNotDiscussed tinyint(1),
change p4_4consonantsNo p4_4consonantsOkConcerns tinyint(1),
add p4_4consonantsNotDiscussed tinyint(1),
change p4_walksbackNo p4_walksbackOkConcerns tinyint(1),
add p4_walksbackNotDiscussed tinyint(1),
change p4_feedsSelfNo p4_feedsSelfOkConcerns tinyint(1),
add p4_feedsSelfNotDiscussed tinyint(1),
change p4_removesHatNo p4_removesHatOkConcerns tinyint(1),
add p4_removesHatNotDiscussed tinyint(1),
change p4_noParentsConcerns18mNo p4_noParentsConcerns18mOkConcerns tinyint(1),
add p4_noParentsConcerns18mNotDiscussed tinyint(1),
change p4_2wSentenceNo p4_2wSentenceOkConcerns tinyint(1),
add p4_2wSentenceNotDiscussed tinyint(1),
change p4_one2stepdirectionsNo p4_one2stepdirectionsOkConcerns tinyint(1),
add p4_one2stepdirectionsNotDiscussed tinyint(1),
change p4_walksbackwardNo p4_walksbackwardOkConcerns tinyint(1),
add p4_walksbackwardNotDiscussed tinyint(1),
change p4_runsNo p4_runsOkConcerns tinyint(1),
add p4_runsNotDiscussed tinyint(1),
change p4_smallContainerNo p4_smallContainerOkConcerns tinyint(1),
add p4_smallContainerNotDiscussed tinyint(1),
change p4_pretendsPlayNo p4_pretendsPlayOkConcerns tinyint(1),
add p4_pretendsPlayNotDiscussed tinyint(1),
change p4_newSkillsNo p4_newSkillsOkConcerns tinyint(1),
add p4_newSkillsNotDiscussed tinyint(1),
change p4_noParentsConcerns24mNo p4_noParentsConcerns24mOkConcerns tinyint(1),
add p4_noParentsConcerns24mNotDiscussed tinyint(1),
change p4_3directionsNo p4_3directionsOkConcerns tinyint(1),
add p4_3directionsNotDiscussed tinyint(1),
change p4_asksQuestionsNo p4_asksQuestionsOkConcerns tinyint(1),
add p4_asksQuestionsNotDiscussed tinyint(1),
change p4_upDownStairsNo p4_upDownStairsOkConcerns tinyint(1),
add p4_upDownStairsNotDiscussed tinyint(1),
change p4_undoesZippersNo p4_undoesZippersOkConcerns tinyint(1),
add p4_undoesZippersNotDiscussed tinyint(1),
change p4_tries2comfortNo p4_tries2comfortOkConcerns tinyint(1),
add p4_tries2comfortNotDiscussed tinyint(1),
change p4_noParentsConcerns48mNo p4_noParentsConcerns48mOkConcerns tinyint(1),
add p4_noParentsConcerns48mNotDiscussed tinyint(1),
change p4_2directionsNo p4_2directionsOkConcerns tinyint(1),
add p4_2directionsNotDiscussed tinyint(1),
change p4_5ormoreWordsNo p4_5ormoreWordsOkConcerns tinyint(1),
add p4_5ormoreWordsNotDiscussed tinyint(1),
change p4_walksUpStairsNo p4_walksUpStairsOkConcerns tinyint(1),
add p4_walksUpStairsNotDiscussed tinyint(1),
change p4_twistslidsNo p4_twistslidsOkConcerns tinyint(1),
add p4_twistslidsNotDiscussed tinyint(1),
change p4_sharesSometimeNo p4_sharesSometimeOkConcerns tinyint(1),
add p4_sharesSometimeNotDiscussed tinyint(1),
change p4_playMakeBelieveNo p4_playMakeBelieveOkConcerns tinyint(1),
add p4_playMakeBelieveNotDiscussed tinyint(1),
change p4_turnsPagesNo p4_turnsPagesOkConcerns tinyint(1),
add p4_turnsPagesNotDiscussed tinyint(1),
change p4_listenMusikNo p4_listenMusikOkConcerns tinyint(1),
add p4_listenMusikNotDiscussed tinyint(1),
change p4_noParentsConcerns36mNo p4_noParentsConcerns36mOkConcerns tinyint(1),
add p4_noParentsConcerns36mNotDiscussed tinyint(1),
change p4_countsOutloudNo p4_countsOutloudOkConcerns tinyint(1),
add p4_countsOutloudNotDiscussed tinyint(1),
change p4_speaksClearlyNo p4_speaksClearlyOkConcerns tinyint(1),
add p4_speaksClearlyNotDiscussed tinyint(1),
change p4_throwsCatchesNo p4_throwsCatchesOkConcerns tinyint(1),
add p4_throwsCatchesNotDiscussed tinyint(1),
change p4_hops1footNo p4_hops1footOkConcerns tinyint(1),
add p4_hops1footNotDiscussed tinyint(1),
change p4_dressesUndressesNo p4_dressesUndressesOkConcerns tinyint(1),
add p4_dressesUndressesNotDiscussed tinyint(1),
change p4_obeysAdultNo p4_obeysAdultOkConcerns tinyint(1),
add p4_obeysAdultNotDiscussed tinyint(1),
change p4_retellsStoryNo p4_retellsStoryOkConcerns tinyint(1),
add p4_retellsStoryNotDiscussed tinyint(1),
change p4_separatesNo p4_separatesOkConcerns tinyint(1),
add p4_separatesNotDiscussed tinyint(1),
change p4_noParentsConcerns60mNo p4_noParentsConcerns60mOkConcerns tinyint(1),
add p4_noParentsConcerns60mNotDiscussed tinyint(1),
change p4_fontanellesClosedNo p4_fontanellesClosedOkConcerns tinyint(1),
add p4_fontanellesClosedNotDiscussed tinyint(1),
change p4_eyes18mNo p4_eyes18mOkConcerns tinyint(1),
add p4_eyes18mNotDiscussed tinyint(1),
change p4_corneal18mNo p4_corneal18mOkConcerns tinyint(1),
add p4_corneal18mNotDiscussed tinyint(1),
change p4_hearing18mNo p4_hearing18mOkConcerns tinyint(1),
add p4_hearing18mNotDiscussed tinyint(1),
change p4_tonsil18mNo p4_tonsil18mOkConcerns tinyint(1),
add p4_tonsil18mNotDiscussed tinyint(1),
change p4_bloodpressure24mNo p4_bloodpressure24mOkConcerns tinyint(1),
add p4_bloodpressure24mNotDiscussed tinyint(1),
change p4_eyes24mNo p4_eyes24mOkConcerns tinyint(1),
add p4_eyes24mNotDiscussed tinyint(1),
change p4_corneal24mNo p4_corneal24mOkConcerns tinyint(1),
add p4_corneal24mNotDiscussed tinyint(1),
change p4_hearing24mNo p4_hearing24mOkConcerns tinyint(1),
add p4_hearing24mNotDiscussed tinyint(1),
change p4_tonsil24mNo p4_tonsil24mOkConcerns tinyint(1),
add p4_tonsil24mNotDiscussed tinyint(1),
change p4_bloodpressure48mNo p4_bloodpressure48mOkConcerns tinyint(1),
add p4_bloodpressure48mNotDiscussed tinyint(1),
change p4_eyes48mNo p4_eyes48mOkConcerns tinyint(1),
add p4_eyes48mNotDiscussed tinyint(1),
change p4_corneal48mNo p4_corneal48mOkConcerns tinyint(1),
add p4_corneal48mNotDiscussed tinyint(1),
change p4_hearing48mNo p4_hearing48mOkConcerns tinyint(1),
add p4_hearing48mNotDiscussed tinyint(1),
change p4_tonsil48mNo p4_tonsil48mOkConcerns tinyint(1),
add p4_tonsil48mNotDiscussed tinyint(1),
add p1_pNutrition1w text,
add p1_pNutrition2w text,
add p1_pNutrition1m text,
add p1_pEducation text,
add p1_pPhysical1w text,
add p1_pPhysical2w text,
add p1_pPhysical1m text,
add p1_immunization1w text,
add p1_immunization2w text,
add p1_immunization1m text,
add p2_nutrition6m text,
add p2_education text,
add p2_physical2m text,
add p2_physical4m text,
add p2_physical6m text,
add p2_immunization6m text,
add p3_nutrition9m text,
add p3_education text,
add p3_physical9m text,
add p3_physical12m text,
add p3_physical15m text,
add p4_nutrition18m text,
add p4_nutrition24m text,
add p4_nutrition48m text,
add p4_education18m text,
add p4_education48m text,
add p4_development18m text,
add p4_development24m text,
add p4_development48m text,
add p4_development36m text,
add p4_development60m text,
add p4_physical18m text,
add p4_physical24m text,
add p4_physical48m text,
add p4_nippisingattained text;
alter table Facility add column enableEncounterTime tinyint(1) not null;
alter table Facility add column enableEncounterTransportationTime tinyint(1) not null;
alter table casemgmt_note add column hourOfEncounterTime int;
alter table casemgmt_note add column minuteOfEncounterTime int;
alter table casemgmt_note add column hourOfEncTransportationTime int;
alter table casemgmt_note add column minuteOfEncTransportationTime int;CREATE TABLE SecurityToken (
  id int(10) NOT NULL auto_increment,
  token varchar(100) not null,
  created timestamp not null,
  expiry datetime not null,
  data varchar(255),
  providerNo varchar(10),
  PRIMARY KEY  (id),
  KEY (token)
);

alter table drugs add dispense_interval int(10) not null;
alter table drugs add refill_quantity int(10) not null;
alter table drugs add refill_duration int(10) not null;

CREATE TABLE `HRMCategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `HRMDocument` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timeReceived` datetime ,
  `reportType` varchar(50),
  `reportHash` varchar(64),
  `reportLessTransactionInfoHash` varchar(64) ,
  `reportStatus` varchar(10) ,
  `reportFile` varchar(255) ,
  `unmatchedProviders` varchar(255),
  `numDuplicatesReceived` int(11),
  `reportDate` datetime ,
  `parentReport` int(11) ,
  `reportLessDemographicInfoHash` varchar(64) ,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `HRMDocumentSubClass` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hrmDocumentId` int(11) ,
  `subClass` varchar(50) ,
  `subClassMnemonic` varchar(50) ,
  `subClassDescription` varchar(255) ,
  `subClassDateTime` date ,
  `isActive` tinyint(4) NOT NULL ,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `HRMDocumentToDemographic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographicNo` varchar(20) ,
  `hrmDocumentId` varchar(20) ,
  `timeAssigned` datetime ,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `HRMDocumentToProvider` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `providerNo` varchar(20) ,
  `hrmDocumentId` varchar(20) ,
  `signedOff` int(11) ,
  `signedOffTimestamp` datetime ,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `HRMSubClass` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `className` varchar(255) ,
  `subClassName` varchar(255) ,
  `subClassMnemonic` varchar(255) ,
  `subClassDescription` varchar(255) ,
  `sendingFacilityId` varchar(50) NOT NULL,
  `hrmCategoryId` int(11) ,
  PRIMARY KEY (`id`)
);

CREATE TABLE `dataExport` (
  `id` int(11) primary key auto_increment,
  `file` varchar(255),
  `daterun` timestamp,
  `user` varchar(255),
  `type` varchar(255),
  `contactLName` varchar(255),
  `contactFName` varchar(255),
  `contactPhone` varchar(255),
  `contactEmail` varchar(255)
);
alter table demographic add patient_status_date date;
alter table demographicArchive add patient_status_date date;
alter table drugs add rxStatus varchar(20);

alter table drugs add hide_cpp tinyint(1);
update drugs set hide_cpp=0;

alter table demographic add roster_termination_date date;
alter table demographicArchive add roster_termination_date date;

alter table Facility add rxInteractionWarningLevel int(10) not null;
update Facility set rxInteractionWarningLevel=0;

create table document_storage (
	id int(10)  NOT NULL auto_increment primary key,
	documentNo int(10),
	fileContents mediumblob,
	uploadDate Date
);

alter table demographicArchive add id bigint auto_increment primary key;

alter table drugs add position int(10) not null;

alter table document add docClass varchar(60);
alter table document add docSubClass varchar(60);

create table ctl_doc_class (
  id integer auto_increment primary key,
  reportclass varchar(60) not null,
subclass varchar(60) not null);

insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Abdomen X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Barium Enema");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Bone Densitometry");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Bone Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Brain Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Carotid Angiography");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Carotid Doppler Ultrasound");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Cervical Spine X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Chest X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Coronary Angiography");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","CT Scan Body");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","CT Scan Head");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Echocardiogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","ERCP X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Hysterosalpingogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","IVP");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Liver-Spleen Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Lumbar Spine X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Lung Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Mammogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Misc. CT Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Misc. MRI Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Misc. Nuclear Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Misc. Ultrasound");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Misc. X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","MRI Scan Body");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","MRI Scan Head");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Myelogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Myoview)");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Other Angiography");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Retinal Angiography");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Retinal Tomograph");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Sestamibi");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Sonohistogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Stress Heart Scan (Thallium");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","UGI with Small Bowel");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Ultrasound Abdomen");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Ultrasound Breast");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Ultrasound Obstetrical");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Ultrasound Pelvis");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Ultrasound Thyroid");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Upper GI Series");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Venous Doppler Ultrasound");

insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Ambulatory BP Monitoring");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Arterial Segmental Pressures (ABI)");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Audiogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Bronchoscopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Colonoscopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Colposcopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Cystoscopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Dobutamine)");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","ECG");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","EEG");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","EGD-oscopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","EMG");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Holter Monitor");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Loop Recorder");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Mantoux Test");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Misc. Diagnostic Test");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Pap Test Report");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Persantine");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Pulmonary Function Testing");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Sigmoidoscopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Sleep Study");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Stress Test (Exercise");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Reports","Urodynamic Testing");

insert into ctl_doc_class (reportclass,subclass) values ("Cardio Respiratory Report","Echocardiography Bubble Study");
insert into ctl_doc_class (reportclass,subclass) values ("Cardio Respiratory Report","Pericardiocentesis");
insert into ctl_doc_class (reportclass,subclass) values ("Cardio Respiratory Report","Echocardiography Esophageal");

insert into ctl_doc_class (reportclass,subclass) values ("Other Letters","Authorization from Patient");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letters","Consent from Patient");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letters","Disability Report");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letters","Letter from Insurance Company");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letters","Letter from Lawyer");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letters","Letter from Patient");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letters","Letter from WSIB");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letters","Living Will");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letters","Miscellaneous Letter");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letters","Power of Attorney for Health Care");

insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Allergy & Immunology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Anaesthesiology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Audiology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Cardiology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Cardiovascular Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Chiropody / Podiatry");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Chiropractic");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Clinical Biochemistry");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Dentistry");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Dermatology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Diagnostic Radiology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Dietitian");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Emergency Medicine");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Emergency Physician");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Endocrinology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Family Practice");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Gastroenterology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","General Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Genetics");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Geriatrics");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Hematology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Hospitalis");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Infectious Disease");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Internal Medicine");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Kinesiology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Microbiology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Midwifery");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Naturopathy");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Neonatology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Nephrology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Neurology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Neurosurgery");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Nuclear Medicine");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Nurse Practitioner");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Nursing");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Obstetrics & Gynecology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Occupational Therapy");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","On-Call Nurse");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","On-Call Physician");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Oncology / Chemotherapy");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Ophthalmology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Optometry");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Oral Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Orthopedic Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Osteopathy");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Other Consultant");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Other Therapy");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Otolaryngology (ENT)");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Paediatrics");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Palliative Care");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Pathology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Pharmacology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Physical Medicine");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Physiotherapy");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Plastic Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Psychiatry");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Psychology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Respiratory Technology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Respirology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Rheumatology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Social Work");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Speech Therapy");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Sports Medicine");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Therapeutic Radiology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Thoracic Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Urgent Care/Walk-In Clinic Physician");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Uro-Gynecology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Urology");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultA","Vascular Surgery");

insert into ctl_doc_class (reportclass,subclass) values ("ConsultB","Admission History");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultB","Consultation");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultB","Discharge Summary");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultB","Encounter Report");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultB","Operative Report");
insert into ctl_doc_class (reportclass,subclass) values ("ConsultB","Progress Report");

alter table allergies add position int(10) not null;

insert into `secRole` (role_name, description) values('Counselling Intern', 'Counselling Intern');

insert into `secObjPrivilege` values('Counselling Intern','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_tasks','x',0,'999998');

insert into access_type (name, type) values("read ticklers assigned to a Counselling Intern","access");
insert into access_type (name, type) values("write Counselling Intern issues","access");
insert into access_type (name, type) values("read Counselling Intern issues","access");
insert into access_type (name, type) values("read Counselling Intern notes","access");

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='Counselling Intern'),(select access_id from access_type where name='read ticklers assigned to a Counselling Intern'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='Counselling Intern'),(select access_id from access_type where name='read Counselling Intern issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='Counselling Intern'),(select access_id from access_type where name='write Counselling Intern issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='Counselling Intern'),(select access_id from access_type where name='read Counselling Intern notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read ticklers assigned to a Counselling Intern'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read Counselling Intern issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='write Counselling Intern issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read Counselling Intern notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read ticklers assigned to a Clinical Assistant'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read Clinical Assistant issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='write Clinical Assistant issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read Clinical Assistant notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read ticklers assigned to a Medical Secretary'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read Medical Secretary issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='write Medical Secretary issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read Medical Secretary notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read ticklers assigned to a secretary'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read secretary issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='write secretary issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read secretary notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='Write Ticklers'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='oscarcomm'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='medical form'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='measurements'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='eform'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='master file'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='read ticklers'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='Perform program registration intake'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='perform registration intake'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='perform admissions'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='perform discharges'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='perform bed assignments'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Counselling Intern'),(select access_id from access_type where name='print bed rosters and reports'));


insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Case Manager'),(select access_id from access_type where name='read Counselling Intern notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='counsellor'),(select access_id from access_type where name='read Counselling Intern notes'));
alter table HRMDocumentToProvider add column viewed int default 0;

CREATE TABLE `HRMDocumentComment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `providerNo` varchar(20) DEFAULT NULL,
  `hrmDocumentId` int(11) DEFAULT NULL,
  `comment` text,
  `commentTime` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `HRMProviderConfidentialityStatement` (
  `providerNo` varchar(20) NOT NULL DEFAULT '',
  `statement` text,
  PRIMARY KEY (`providerNo`)
);
alter table casemgmt_note_link add other_id varchar(25);
create table FlowSheetUserCreated(
  id int(10) auto_increment primary key,
  name varchar(4),
  dxcodeTriggers varchar(255),	
  displayName varchar(255),
  warningColour varchar(20),
  recommendationColour varchar(20),
  topHTML text,
  archived tinyint(1),
  createdDate date,
  KEY FlowSheetUserCreated_archived (archived)
);
alter table HRMDocument add column hrmCategoryId int;

drop table HRMCategory;

CREATE TABLE `HRMCategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(255) NOT NULL,
  subClassNameMnemonic varchar(255) not null,
  PRIMARY KEY (`id`)
);

insert into HRMCategory values (null, 'General Oscar Lab', 'DEFAULT');
insert into HRMCategory values (null, 'Oscar HRM Category CT:ABDW' ,'CT:ABDW');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:CSP5' ,'RAD:CSP5');
insert into HRMCategory values (null, 'Oscar HRM Category NM:THYSAN' ,'NM:THYSAN');
insert into HRMCategory values (null, 'Oscar HRM Category NM:BLDPOL' ,'NM:BLDPOL');
insert into HRMCategory values (null, 'Oscar HRM Category US:ABDC' ,'US:ABDC');
insert into HRMCategory values (null, 'Oscar HRM Category US:PELVLT' ,'US:PELVLT');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ABD' ,'RAD:ABD');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:CXR2' ,'RAD:CXR2');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ABD2' ,'RAD:ABD2');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ANKB' ,'RAD:ANKB');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:CSP' ,'RAD:CSP');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:TSP' ,'RAD:TSP');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:LSP4ER' ,'RAD:LSP4ER');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:DIGB' ,'RAD:DIGB');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ELBB' ,'RAD:ELBB');
insert into HRMCategory values (null, 'Oscar HRM Category MAM:MAMMOB' ,'MAM:MAMMOB');
insert into HRMCategory values (null, 'Oscar HRM Category ECHO:ECHO' ,'ECHO:ECHO');
insert into HRMCategory values (null, 'Oscar HRM Category ECHOWL:ECH0520' ,'ECHOWL:ECH0520');
insert into HRMCategory values (null, 'Oscar HRM Category ECHO:MDAB' ,'ECHO:MDAB');


alter table billing_on_cheader1 modify demographic_name varchar(60);
alter table ctl_billingservice modify service_group_name varchar(30);
alter table ctl_billingservice modify service_group varchar(30);
alter table issue add sortOrderId int;
alter table HRMDocument add sourceFacility varchar(120);

create table providerLabRoutingFavorites ( 
 id int auto_increment primary key, 
 provider_no varchar(6), 
 index(provider_no), 
 route_to_provider_no varchar(6) 
);
CREATE TABLE `partial_date` (
        id int PRIMARY KEY AUTO_INCREMENT,
        table_name int,
        table_id int,
        field_name varchar(50),
        format varchar(10)
);


alter table demographic change pin myOscarUserName varchar(255);
update demographic set myOscarUserName=null where  myOscarUserName='@myoscar.org';
update demographic set myOscarUserName=null where  myOscarUserName='';
alter table demographic add unique(myOscarUserName);

update document set appointment_no = 0 where appointment_no is null;
drop table RemoteDataRetrievalLog;

create table RemoteDataLog
(
	id bigint primary key auto_increment,
	providerNo varchar(255) not null,
	actionDate datetime not null,
	action varchar(32) not null,
	documentId varchar(255) not null,
	documentContents blob not null
);
update property set value=replace(value, '@myoscar.org', '') where name='MyOscarId';
update demographic set myOscarUserName=replace(myOscarUserName, '@myoscar.org', '');

alter table demographicArchive change pin myOscarUserName varchar(255);

create table Flowsheet (
  id int(10) auto_increment primary key,
  name varchar(25),
  content text,
  enabled tinyint(1),
  external tinyint(1),
  createdDate date
);

alter table drugs add home_med boolean;
alter table drugs add comment varchar(255);
alter table drugs add start_date_unknown boolean;

update drugs set home_med=0;
update drugs set start_date_unknown=0;
update drugs set comment='';
update app_lookuptable set readonly=0;
alter table consultationRequestExt change value value text not null;
alter table partial_date modify field_name integer;

alter table drugs drop home_med;
UPDATE drugs d set d.start_date_unknown="0";
update demographic set myOscarUserName=null where myOscarUserName='';alter table DemographicContact add note varchar(200);
alter table labRequestReportLink modify request_date datetime;

alter table demographic add roster_termination_reason varchar(2) after roster_termination_date;
alter table demographicArchive add roster_termination_reason varchar(2) after roster_termination_date;

CREATE TABLE `formLabReq10` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `formEdited` timestamp NOT NULL,
  `b_glucose` tinyint(1),
  `b_glucose_random` tinyint(1),
  `b_glucose_fasting` tinyint(1),
  `b_hba1c` tinyint(1),
  `b_creatinine` tinyint(1),
  `b_uricAcid` tinyint(1),
  `b_sodium` tinyint(1),
  `b_potassium` tinyint(1),
  `b_chloride` tinyint(1),
  `b_ck` tinyint(1),
  `b_alt` tinyint(1),
  `b_alkPhosphatase` tinyint(1),
  `ohip` tinyint(1),
  `formCreated` date,
  `patientFirstName` varchar(40),
  `b_timeNextDose2` varchar(20),
  `clinicCity` varchar(40),
  `b_timeNextDose1` varchar(20),
  `b_timeLastDose2` varchar(20),
  `b_timeLastDose1` varchar(20),
  `provider_no` int(11),
  `m_vaginalRectal` tinyint(1),
  `m_vaginal` tinyint(1),
  `b_acRatioUrine` tinyint(1),
  `healthNumber` varchar(20),
  `patientCity` varchar(40),
  `b_patientsTelNo` varchar(40),
  `clinicianContactUrgent` varchar(40),
  `b_childsAgeDays` varchar(20),
  `b_tsh` tinyint(1),
  `b_timeCollected2` varchar(20),
  `b_timeCollected1` varchar(20),
  `b_therapeuticDrugMonitoring` tinyint(1),
  `i_rubella` tinyint(1),
  `m_cervical` tinyint(1),
  `patientAddress` varchar(60),
  `b_vitaminB12` tinyint(1),
  `m_blank` tinyint(1),
  `reqProvName` varchar(60),
  `v_chronicHepatitis` tinyint(1),
  `version` varchar(6),
  `oprn` varchar(40),
  `b_albumin` tinyint(1),
  `aci` text,
  `patientPC` varchar(10),
  `b_urinalysis` tinyint(1),
  `h_cbc` tinyint(1),
  `form_class` tinyint(1),
  `sex` varchar(8),
  `i_pregnancyTest` tinyint(1),
  `submit` tinyint(1),
  `v_acuteHepatitis` tinyint(1),
  `birthDate` date,
  `patientBirthMth` varchar(25),
  `phoneNumber` varchar(20),
  `m_otherSwabsSource` varchar(60),
  `m_otherSwabsPus` tinyint(1),
  `clinicAddress` varchar(100),
  `m_gcSource` varchar(60),
  `b_childsAgeHours` varchar(20),
  `m_throat` tinyint(1),
  `patientLastName` varchar(40),
  `m_sputum` tinyint(1),
  `b_neonatalBilirubin` tinyint(1),
  `m_fecalOccultBlood` tinyint(1),
  `m_urine` tinyint(1),
  `m_blankText` varchar(100),
  `b_nameDrug2` varchar(100),
  `m_stoolOvaParasites` tinyint(1),
  `b_nameDrug1` varchar(100),
  `m_gc` tinyint(1),
  `i_prenatal` tinyint(1),
  `h_prothrombinTime` tinyint(1),
  `patientBirthYear` varchar(10),
  `m_chlamydiaSource` varchar(60),
  `i_repeatPrenatalAntibodies` tinyint(1),
  `b_ferritin` tinyint(1),
  `wcb` tinyint(1),
  `v_immuneStatus` tinyint(1),
  `m_stoolCulture` tinyint(1),
  `province` varchar(20),
  `m_woundSource` varchar(60),
  `patientBirthDay` varchar(30),
  `demographic_no` int(11),
  `i_mononucleosisScreen` tinyint(1),
  `b_lipidAssessment` tinyint(1),
  `m_wound` tinyint(1),
  `provName` varchar(60),
  `thirdParty` tinyint(1),
  `clinicName` varchar(60),
  `b_bilirubin` tinyint(1),
  `m_specimenCollectionTime` varchar(15),
  `o_otherTests16` varchar(150),
  `o_otherTests15` varchar(150),
  `o_otherTests14` varchar(150),
  `o_otherTests13` varchar(150),
  `o_otherTests12` varchar(150),
  `o_otherTests11` varchar(150),
  `o_otherTests10` varchar(150),
  `o_otherTests9` varchar(150),
  `o_otherTests8` varchar(150),
  `o_otherTests7` varchar(150),
  `o_otherTests6` varchar(150),
  `o_specimenCollectionDate` date,
  `o_otherTests5` varchar(150),
  `clinicPC` varchar(10),
  `o_otherTests4` varchar(150),
  `o_otherTests3` varchar(150),
  `o_otherTests2` varchar(150),
  `b_cliniciansTelNo` varchar(40),
  `o_otherTests1` varchar(150),
  `patientName` varchar(40),
  `b_dateSigned` date,
  `practitionerNo` varchar(20),
  `m_chlamydia` tinyint(1),
  `v_immune_HepatitisC` tinyint(1),
  `v_immune_HepatitisB` tinyint(1),
  `v_immune_HepatitisA` tinyint(1),
  `fobt_nonCCC` tinyint(1),
  `fobt_CCC` tinyint(1),
  `psa_total` tinyint(1),
  `psa_free` tinyint(1),
  `psa_insured` tinyint(1),
  `psa_uninsured` tinyint(1),
  `copy2clinician` tinyint(1),
  `copyLname` varchar(32),
  `copyFname` varchar(32),
  `copyAddress` varchar(100),
  `vitd_uninsured` tinyint(1),
  `vitd_insured` tinyint(1),
  `hcType` varchar(4),
  `male` tinyint(1),
  `female` tinyint(1),
  PRIMARY KEY (`ID`)
);

insert into encounterForm (form_name,form_value,form_table,hidden) Values('Lab Req 2010','../form/formlabreq10.jsp?demographic_no=','formLabReq10',0);
create table PHRVerification(
	id int(10) auto_increment primary key,
	demographicNo int(10),
	phrUserName varchar(255),
	verificationLevel varchar(100),
	verificationDate datetime,
	verificationBy varchar(6),
	photoId tinyint(1),
	parentGuardian tinyint(1),
	comments text,
	createdDate datetime,
	archived tinyint(1),
	KEY `PHRVerification_archived` (`archived`)
);

alter table labPatientPhysicianInfo modify collection_date varchar(20);
alter table document add sourceFacility varchar(120);

update ctl_doc_class set reportclass="Diagnostic Test Report" where reportclass="Diagnostic Test Reports";
update ctl_doc_class set reportclass="Other Letter" where reportclass="Other Letters";
update ctl_doc_class set reportclass="Consultant ReportA" where reportclass="ConsultA";
update ctl_doc_class set reportclass="Consultant ReportB" where reportclass="ConsultB";
insert into ctl_doc_class (reportclass,subclass) values ("Medical Record Report","");
insert into ctl_doc_class (reportclass,subclass) values ("Lab Report","");

update document set docClass="Diagnostic Test Report" where docClass="Diagnostic Test Reports";
update document set docClass="Other Letter" where docClass="Other Letters";
update document set docClass="Consultant Report" where reportclass="Consult";


-- CAISI Patches from 2011-01-12.sql --

alter table OcanStaffForm add clientStartDate date after startDate;
alter table OcanStaffForm add clientCompletionDate date after completionDate;
alter table OcanStaffForm add clientDateOfBirth varchar(10) after dateOfBirth;
alter table OcanStaffForm add ocanType varchar(20) not null after ocanFormVersion;
alter table OcanStaffForm modify gender varchar(10) null;

update OcanStaffForm set assessmentStatus="Completed" where assessmentStatus="Complete";
update OcanStaffForm set assessmentStatus="In Progress" where assessmentStatus="Active";

update OcanStaffForm set assessmentStatus="In Progress" where assessmentStatus is null;
update OcanStaffForm set assessmentStatus="In Progress" where assessmentStatus="";
 
update OcanStaffForm set ocanType="FULL";
 
INSERT INTO `OcanFormOption` VALUES (1213,'1.2','OCAN Assessment Status','In Progress','In Progress');
INSERT INTO `OcanFormOption` VALUES (1214,'1.2','OCAN Assessment Status','Completed','Completed');
INSERT INTO `OcanFormOption` VALUES (1215,'1.2','OCAN Assessment Status','Cancelled','Cancelled');

--
alter table OcanStaffForm add assessmentId int after id;
alter table OcanStaffForm add clientFormCreated datetime after created;
alter table OcanStaffForm add clientFormProviderNo varchar(6) after providerNo;
alter table OcanStaffForm add clientFormProviderName varchar(100) after providerName;

alter table OcanStaffForm modify providerNo varchar(6);
alter table OcanStaffForm modify created datetime;

delete from OcanFormOption where id=704 or id=709;
--
create table OcanSubmissionLog (
submissionId int primary key auto_increment,
submitDateTime timestamp,
result varchar(255),
transactionId varchar(100),
resultMessage text,
submissionData longtext
);


alter table OcanStaffForm add submissionId integer;


-- update-2011-10-24.sql ---
alter table appointment add imported_status varchar(20);
alter table appointmentArchive add imported_status varchar(20);

-- PHC end --

-- Part of update-2011-11-30.sql
ALTER table formRourke2009 change `p2_fallsp2_sleepCryOkConcernsOkConcerns` `p2_fallsOkConcerns` tinyint(1);

-- update-2011-12-07.sql --
CREATE TABLE `SentToPHRTracking` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `demographicNo` int(10) NOT NULL,
  `objectName` varchar(60) NOT NULL,
  `lastObjectId` int(10) NOT NULL,
  `sentDatetime` datetime NOT NULL,
  `sentToServer` varchar(60) NOT NULL,
  PRIMARY KEY (`id`)
);

-- update-2011-12-31.sql renders obsolete optimize.sql
ALTER TABLE `issue` ADD INDEX `description_index`(`description`(20));
ALTER TABLE `hl7TextInfo` ADD INDEX `labno_index`(`lab_no`);
ALTER TABLE `providerLabRouting` ADD INDEX `labno_index`(`lab_no`);

-- update-2012-01-06.sql 
alter table SentToPHRTracking add unique key (demographicNo,objectName,sentToServer);

-- update-2012-01-11.sql 
alter table prescription add column lastUpdateDate datetime;
update prescription set lastUpdateDate=now();
alter table prescription change column lastUpdateDate lastUpdateDate datetime not null;

-- update-2012-01-13.sql 
alter table drugs add column lastUpdateDate datetime;
update drugs set lastUpdateDate=now();
alter table drugs change column lastUpdateDate lastUpdateDate datetime not null;

update drugs set customName=null where customName='null';
update drugs set special_instruction=null where special_instruction='null';
update drugs set unitName=null where unitName='null';
update drugs set eTreatmentType=null where eTreatmentType='null';
update drugs set rxStatus=null where rxStatus='null';

-- update-2012-01-16.sql 
alter table allergies add column lastUpdateDate datetime;
update allergies set lastUpdateDate=now();
alter table allergies change column lastUpdateDate lastUpdateDate datetime not null;

alter table allergies add column providerNo varchar(6);

-- update-2012-01-17.sql 
alter table preventions add column lastUpdateDate datetime not null;
update preventions set lastUpdateDate=now();

alter table SentToPHRTracking drop column lastObjectId;

-- update-2012-01-19.sql 
alter table measurementType add column createDate datetime;
update measurementType set createDate=now();
alter table measurementType change column createDate createDate datetime not null;

-- update-2012-01-19.1.sql 
alter table measurementsDeleted add column originalId int(10) unsigned not null;
update measurementsDeleted set originalId = 0;

-- update-2012-02-06.sql 
update drugs set past_med = false where past_med is null;

-- update-2012-02-07.sql 
alter table OcanStaffForm add consent varchar(50);
alter table drugs modify `past_med` boolean not null;

-- update-2012-02-15.sql 
alter table EyeformConsultationReport change concurrentProblems concurrentProblems text;
alter table EyeformConsultationReport change currentMeds currentMeds text;
alter table EyeformConsultationReport change allergies allergies text;
alter table EyeformConsultationReport change plan plan text;
alter table EyeformConsultationReport change impression impression text;
alter table EyeformConsultationReport change clinicalInfo clinicalInfo text;

-- =========END OF FEATURES THAT MADE THE 11 RELEASE========= --

--
-- This schema is up to date as of update-2012-02-15.sql
--


