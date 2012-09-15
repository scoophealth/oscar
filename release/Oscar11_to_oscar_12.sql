-- oscar-mcmaster11-1.1alpha.deb is up to date to update-2012-04-01.sql 
-- Updates Oscar database from version 11 to 12 --

-- =========START OF FEATURES THAT MADE THE 12 RELEASE========= --

-- UPDATE-2011-12-07.sql plus UPDATE-2012-01-06.sql --
CREATE TABLE IF NOT EXISTS `SentToPHRTracking` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `demographicNo` int(10) NOT NULL,
  `objectName` varchar(60) NOT NULL,
  `lastObjectId` int(10) NOT NULL,
  `sentDatetime` datetime NOT NULL,
  `sentToServer` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  unique key (demographicNo,objectName,sentToServer)
);

-- UPDATE-2011-12-31.sql renders obsolete optimize.sql
-- ALTER IGNORE TABLE `issue` ADD INDEX `description_index`(`description`(20));
-- ALTER IGNORE TABLE `hl7TextInfo` ADD INDEX `labno_index`(`lab_no`);
-- ALTER IGNORE TABLE `providerLabRouting` ADD INDEX `labno_index`(`lab_no`);

-- UPDATE-2012-01-11.sql ***PHC START***
ALTER IGNORE TABLE prescription add column lastUPDATEDate datetime;
UPDATE prescription set lastUPDATEDate=now();
ALTER IGNORE TABLE prescription change column lastUPDATEDate lastUPDATEDate datetime not null;

-- UPDATE-2012-01-13.sql 
ALTER IGNORE TABLE drugs add column lastUPDATEDate datetime;
UPDATE drugs set lastUPDATEDate=now();
ALTER IGNORE TABLE drugs change column lastUPDATEDate lastUPDATEDate datetime not null;

UPDATE drugs set customName=null where customName='null';
UPDATE drugs set special_instruction=null where special_instruction='null';
UPDATE drugs set unitName=null where unitName='null';
UPDATE drugs set eTreatmentType=null where eTreatmentType='null';
UPDATE drugs set rxStatus=null where rxStatus='null';

-- UPDATE-2012-01-16.sql 
ALTER IGNORE TABLE allergies add column lastUPDATEDate datetime;
UPDATE allergies set lastUPDATEDate=now();
ALTER IGNORE TABLE allergies change column lastUPDATEDate lastUPDATEDate datetime not null;

ALTER IGNORE TABLE allergies add column providerNo varchar(6);

-- UPDATE-2012-01-17.sql 
ALTER IGNORE TABLE preventions add column lastUPDATEDate datetime not null;
UPDATE preventions set lastUPDATEDate=now();

ALTER IGNORE TABLE SentToPHRTracking drop column lastObjectId;

-- UPDATE-2012-01-19.sql 
ALTER IGNORE TABLE measurementType add column createDate datetime;
UPDATE measurementType set createDate=now();
ALTER IGNORE TABLE measurementType change column createDate createDate datetime not null;

-- UPDATE-2012-01-19.1.sql 
ALTER IGNORE TABLE measurementsDeleted add column originalId int(10) unsigned not null;
UPDATE measurementsDeleted set originalId = 0;

-- UPDATE-2012-02-06.sql 
UPDATE drugs set past_med = false where past_med is null;

-- UPDATE-2012-02-07.sql 
ALTER IGNORE TABLE OcanStaffForm add consent varchar(50);
ALTER IGNORE TABLE drugs modify `past_med` boolean not null;

-- UPDATE-2012-02-15.sql 
ALTER IGNORE TABLE EyeformConsultationReport change concurrentProblems concurrentProblems text;
ALTER IGNORE TABLE EyeformConsultationReport change currentMeds currentMeds text;
ALTER IGNORE TABLE EyeformConsultationReport change allergies allergies text;
ALTER IGNORE TABLE EyeformConsultationReport change plan plan text;
ALTER IGNORE TABLE EyeformConsultationReport change impression impression text;
ALTER IGNORE TABLE EyeformConsultationReport change clinicalInfo clinicalInfo text;


alter table Contact add deleted tinyint;
alter table Contact add UPDATEDate timestamp not null;


create table RemoteReferral
(
	id int primary key auto_increment,
	facilityId int not null,
	index(facilityId),
	demographicId int not null,
	index(demographicId),
	referringProviderNo varchar(6) not null,
	referredToFacilityName varchar(255) not null,
	referredToProgramName varchar(255) not null,
	referalDate datetime not null,
	reasonForReferral varchar(255),
	presentingProblem varchar(255),
	createDate datetime not null
);


alter table billactivity add column id int(10) NOT NULL auto_increment primary key;
alter table billactivity add column sentdate datetime;

alter table demographicSets add `id` int(10) not null auto_increment primary key;
alter table demographicPharmacy add `id` int(10) NOT NULL auto_increment primary key;
alter table groupMembers_tbl add `id` int(10) NOT NULL auto_increment primary key;

alter table measurementGroup add `id` int(10) not null auto_increment primary key;
alter table messagelisttbl add `id` int(10) not null auto_increment primary key;

alter table providerArchive add `id` int(10) not null auto_increment primary key;
alter table quickList add `id` int(10) not null auto_increment primary key;
alter table quickListUser add `id` int(10) not null auto_increment primary key;

alter table recycle_bin add `id` int(10) not null auto_increment primary key;
alter table remoteAttachments add `id` int(10) not null auto_increment primary key;
alter table reportagesex add `id` int(10) not null auto_increment primary key;
alter table reportprovider add `id` int(10) not null auto_increment primary key;

alter table specialistsJavascript add `id` int(10) not null auto_increment primary key;

alter table clinic_location add id int(10) not null auto_increment primary key;
alter table ctl_billingservice_premium add id int(10) not null auto_increment primary key;
alter table ctl_diagcode add id int(10) not null auto_increment primary key;
alter table ctl_doctype add id int(10) not null auto_increment primary key;

-- 2012-03-05

alter table hl7TextInfo add 
(filler_order_num varchar(50), 
sending_facility varchar(50));

create table HL7HandlerMSHMapping (id int(50) NOT NULL AUTO_INCREMENT, hospital_site varchar(255), facility varchar(100), facility_name varchar(255), notes varchar(255), PRIMARY KEY (id));


INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health",".","Lakeridge Health Oshawa");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","MHB","Lakeridge Health Bowmanville");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","NDP","Lakeridge Health Port Perry");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","OE.LHC","Lakeridge Health ");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","RAD.OSG","Lakeridge Health");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","RVA","Rouge Valley Ajax and Pickering");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","RVC","Rouge Valley Centenary");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","RAD.APG","Rouge Valley");
	
INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","PRH","Peterborough Regional Health Centre");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","NHC","Northumberland Hills Hospital");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","CMH","Campbellford Memorial Hospital");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","RAD.PRH","Peterborough/Northumberland/Campbellford");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("The Scarborough Hospitals","GRA","The Scarborough Hospital - Birchmount Campus");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("The Scarborough Hospitals","SCS","The Scarborough Hospital - General Campus");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("The Scarborough Hospitals","RAD.SCS","The Scarborough Hospital");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Ontario Shores","WHA","Ontario Shores");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","APG","Rouge Valley Ajax and Pickering");


alter table hl7TextInfo add label varchar(255);


alter table log add column securityId int;

ALTER TABLE billingservice ADD COLUMN sliFlag TINYINT(1) NOT NULL AFTER termination_date;
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'E450B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'E450C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'E451B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'E451C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G104A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G105A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G111A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G112A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G120A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G121A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G138A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G139A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G140A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G141A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G142A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G143A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G144A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G145A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G146A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G147A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G148A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G149A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G150A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G151A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G152A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G166A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G167A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G174A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G180A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G181A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G197A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G209A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G251A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G252A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G253A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G283A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G284A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G307A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G308A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G310A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G311A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G313A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G315A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G317A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G319A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G320A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G321A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G343A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G346A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G350A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G351A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G353A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G354A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G414A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G415A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G416A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G418A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G425A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G428A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G432A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G433A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G436A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G437A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G438A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G439A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G440A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G441A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G442A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G443A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G444A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G448A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G450A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G451A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G455A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G456A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G457A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G459A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G466A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G469A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G477A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G516A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G518A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G519A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G525A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G526A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G529A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G530A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G533A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G540A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G542A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G544A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G545A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G546A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G554A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G555A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G560A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G560A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G561A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G562A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G566A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G567A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G568A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G570A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G571A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G572A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G574A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G575A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G577A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G578A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G581A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G650A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G651A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G652A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G653A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G654A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G655A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G656A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G657A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G658A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G659A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G660A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G661A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G682A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G683A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G684A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G685A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G686A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G687A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G688A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G689A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G690A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G692A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G693A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G815A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G816A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G850A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G851A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G852A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G853A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G854A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G855A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G856A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G857A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'G858A';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J102B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J103B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J103C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J105B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J105C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J106B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J106C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J107B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J107C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J108B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J108C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J122B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J122C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J125B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J125C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J127B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J127C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J128B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J128C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J135B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J135C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J138B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J138C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J149B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J149C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J151C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J157B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J157C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J158B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J158C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J159B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J159C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J160B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J160C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J161B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J161C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J162B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J162C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J163B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J163C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J164B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J164C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J165B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J165C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J180B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J180C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J182B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J182C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J183B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J183C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J189C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J190B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J190C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J191B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J191C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J192B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J192C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J193B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J193C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J194B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J194C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J195B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J195C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J196B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J196C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J197B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J197C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J198B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J198C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J200B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J200C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J201B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J201C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J202B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J202C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J203B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J203C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J204B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J204C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J205B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J206B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J206C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J207B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J207C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J290C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J301B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J301C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J303B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J303C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J304B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J304C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J305B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J305C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J306B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J306C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J307B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J307C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J308B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J308C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J310B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J310C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J311B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J311C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J313B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J313C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J315B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J315C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J316B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J316C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J318B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J319B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J320B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J320C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J322B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J322C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J323B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J324B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J324C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J327B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J327C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J330B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J330C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J331B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J331C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J332B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J332C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J333B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J333C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J334B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J334C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J335B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J335C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J340B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J340C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J402B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J402C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J403B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J403C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J405B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J405C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J406B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J406C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J407B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J407C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J408B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J408C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J422B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J422C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J425B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J425C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J427B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J427C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J428B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J428C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J435B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J435C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J438B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J438C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J457B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J457C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J458B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J458C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J459B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J459C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J460B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J460C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J461B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J461C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J462B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J463B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J463C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J464B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J464C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J476B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J476C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J480B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J480C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J482B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J482C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J483B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J483C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J489C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J490B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J490C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J491B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J491C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J492B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J492C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J493B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J493C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J494B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J494C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J495B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J495C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J496B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J496C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J497B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J497C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J498B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J498C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J500B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J500C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J501B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J501C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J502B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J502C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J503B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J503C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J504B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J504C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J505B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J505C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J506B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J506C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J507B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J507C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J602B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J602C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J604B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J604C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J606B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J606C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J607B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J607C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J608B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J608C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J609B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J609C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J610B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J610C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J611B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J611C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J612B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J612C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J613B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J613C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J614B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J614C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J615B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J615C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J616B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J616C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J617B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J617C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J618B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J618C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J619B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J619C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J620B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J620C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J621B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J621C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J623B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J623C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J624B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J624C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J625B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J625C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J626B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J626C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J627B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J627C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J629B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J629C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J630B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J630C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J631B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J631C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J632B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J632C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J633B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J633C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J634B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J634C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J635B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J635C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J636B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J636C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J637B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J637C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J638B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J638C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J639B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J639C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J640B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J640C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J641B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J641C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J643B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J643C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J647B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J647C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J648B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J648C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J649B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J649C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J650B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J650C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J651B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J651C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J652B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J652C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J653B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J653C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J654B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J654C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J655B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J655C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J656B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J656C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J657B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J657C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J658B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J658C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J659B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J659C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J660B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J660C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J661B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J661C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J662B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J662C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J663B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J663C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J664B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J664C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J665B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J665C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J666B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J666C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J667B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J667C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J668B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J668C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J669B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J669C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J670B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J670C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J671B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J671C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J672B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J672C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J673B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J673C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J674B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J674C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J675B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J675C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J676B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J676C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J677B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J677C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J678B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J678C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J679B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J679C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J680B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J680C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J681B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J681C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J682B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J682C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J683B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J683C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J684B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J684C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J685B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J685C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J686B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J686C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J687B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J687C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J688B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J688C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J689B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J689C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J690B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J690C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J691B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J691C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J802B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J802C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J804B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J804C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J806B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J806C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J807B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J807C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J808B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J808C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J809B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J809C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J810B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J810C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J811B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J811C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J812B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J812C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J813B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J813C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J814B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J814C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J815B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J815C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J816C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J817C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J818C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J819B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J819C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J820B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J820C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J821B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J821C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J823B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J823C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J824B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J824C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J825B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J825C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J826B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J826C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J827B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J827C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J829B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J829C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J830B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J830C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J831B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J831C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J832B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J832C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J833B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J833C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J834B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J834C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J835B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J835C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J836B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J836C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J837B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J837C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J838B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J838C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J839B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J839C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J840B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J840C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J841B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J841C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J843B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J843C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J847B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J847C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J848B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J848C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J849B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J849C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J850B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J850C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J851B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J851C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J852B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J852C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J853B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J853C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J854B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J854C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J855B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J855C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J856B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J856C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J857B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J857C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J858B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J858C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J859B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J859C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J860B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J860C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J861B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J861C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J862B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J862C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J863B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J863C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J864B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J864C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J865B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J865C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J866B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J866C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J867C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J868C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J869C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J870B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J870C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J871B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J871C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J872B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J872C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J873B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J873C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J874B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J874C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J875B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J875C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J876B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J876C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J877B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J877C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J878B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J878C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J879B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J879C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J880B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J880C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J881B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J881C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J882B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J882C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J883B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J883C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J884B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J884C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J885B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J885C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J886B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J886C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J887B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J887C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J888B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J888C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J889B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J889C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J890B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J890C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J891B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J891C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J893B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J893C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J894B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'J894C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X001B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X001C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X003B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X003C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X004B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X004C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X005B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X005C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X006B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X006C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X007B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X007C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X008B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X008C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X009B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X009C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X010B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X010C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X011B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X011C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X012B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X012C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X016B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X016C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X017B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X017C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X018B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X018C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X019B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X019C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X020B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X020C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X025B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X025C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X027B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X027C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X028C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X031C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X032C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X033B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X033C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X034B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X034C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X035B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X035C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X036B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X036C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X037B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X037C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X038B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X038C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X039B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X039C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X040B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X040C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X045B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X045C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X046B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X046C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X047B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X047C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X048B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X048C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X049B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X049C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X050B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X050C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X051B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X051C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X052B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X052C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X053B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X053C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X054B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X054C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X055B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X055C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X056B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X056C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X057B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X057C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X058B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X058C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X060B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X060C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X063B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X063C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X064B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X064C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X065B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X065C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X066B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X066C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X067B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X067C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X068B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X068C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X069B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X069C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X072B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X072C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X080B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X080C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X081B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X081C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X090B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X090C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X091B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X091C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X092B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X092C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X096B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X096C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X100B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X100C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X101B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X101C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X103B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X103C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X104B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X104C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X105B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X105C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X106C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X107C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X108C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X109B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X109C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X110B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X110C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X111B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X111C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X112B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X112C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X113B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X113C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X114B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X114C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X116B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X116C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X117B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X117C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X120B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X120C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X121B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X121C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X122B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X122C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X123B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X123C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X124C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X125C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X126C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X127C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X128C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X129B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X129C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X130B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X130C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X131B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X131C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X132B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X132C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X133B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X133C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X134B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X134C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X135B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X135C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X136B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X136C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X137B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X137C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X138B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X138C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X139B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X139C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X140B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X140C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X141B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X141C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X143B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X143C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X144B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X144C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X147B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X147C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X149B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X149C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X150B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X150C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X151B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X151C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X152B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X152C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X153B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X153C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X154B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X154C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X155B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X155C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X156B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X156C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X157B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X157C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X158B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X158C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X159B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X159C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X160B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X160C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X161B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X161C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X162B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X162C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X163C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X164C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X165C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X167C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X168C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X169B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X169C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X170B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X170C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X171B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X171C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X173B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X173C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X174B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X174C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X175B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X175C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X176B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X176C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X177B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X177C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X179B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X179C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X180B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X180C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X181B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X181C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X182B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X182C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X183B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X183C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X184B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X184C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X185B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X185C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X186B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X186C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X187B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X187C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X188C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X189B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X189C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X190B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X190C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X191B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X191C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X192B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X192C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X193B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X193C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X194B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X194C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X195B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X195C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X196B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X196C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X197B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X197C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X198B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X198C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X199B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X199C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X200B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X200C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X201B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X201C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X202B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X202C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X203B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X203C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X204B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X204C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X205B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X205C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X206B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X206C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X207B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X207C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X208B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X208C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X209B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X209C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X210B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X210C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X211B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X211C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X212B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X212C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X213B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X213C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X214B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X214C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X215B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X215C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X216B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X216C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X217B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X217C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X218B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X218C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X219B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X219C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X220B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X220C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X221B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X221C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X223B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X223C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X224B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X224C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X225B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X225C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X226B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X226C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X227B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X227C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X228B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X228C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X229B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X229C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X230B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X230C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X231C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X232C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X233C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X400C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X401C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X402C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X403C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X404C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X405C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X406C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X407C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X408C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X409C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X410C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X412C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X413C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X415C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X416C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X417C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X421C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X425C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X431C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X435C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X441C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X445C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X451C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X455C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X461C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X465C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X471C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X475C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X486C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X487C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X488C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X489C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X490C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X492C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X493C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X495C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X496C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X498C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'X499C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y602B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y602C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y604B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y604C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y606B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y606C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y607B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y607C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y608B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y608C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y609C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y610B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y610C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y611B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y611C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y612B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y612C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y613B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y613C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y614B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y614C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y615B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y615C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y616B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y616C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y617B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y617C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y618B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y618C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y620B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y620C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y621B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y621C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y623B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y623C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y624B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y624C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y625B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y625C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y626B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y626C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y627B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y627C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y628B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y629B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y629C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y630B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y630C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y631B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y631C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y632B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y632C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y633B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y633C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y634B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y634C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y635B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y635C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y636B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y636C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y637B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y637C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y638B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y638C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y639B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y639C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y640B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y640C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y641B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y641C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y643B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y643C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y647B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y647C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y648B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y648C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y649B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y649C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y650B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y650C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y651B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y651C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y652B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y652C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y653B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y653C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y654B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y654C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y655B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y655C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y656B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y656C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y657B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y657C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y658B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y658C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y659B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y659C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y660B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y660C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y661B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y661C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y662B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y662C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y663B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y663C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y664B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y664C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y665B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y665C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y667B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y667C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y668B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y668C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y669B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y669C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y670B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y670C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y671B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y671C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y672B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y672C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y673B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y673C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y674B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y674C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y675B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y675C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y676B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y676C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y677B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y677C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y678B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y678C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y679B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y679C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y680B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y680C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y681B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y681C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y682B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y682C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y683B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y683C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y684B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y684C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y685B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y685C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y686B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y686C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y687B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y687C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y688B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y688C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y802B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y802C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y804B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y804C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y806B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y806C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y807B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y807C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y808B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y808C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y809C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y810B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y810C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y811B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y811C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y812B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y812C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y813B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y813C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y814B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y814C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y815B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y815C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y816B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y816C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y817B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y817C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y818B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y818C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y820B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y820C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y821B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y821C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y823B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y823C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y824B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y824C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y825B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y825C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y826B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y826C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y827B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y827C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y829B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y829C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y830B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y830C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y831C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y832C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y833B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y833C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y834B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y834C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y835B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y835C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y836B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y836C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y837B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y837C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y838B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y838C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y839B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y839C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y840B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y840C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y841B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y841C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y843B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y843C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y847B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y847C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y848B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y848C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y849B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y849C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y850B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y850C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y851B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y851C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y852B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y852C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y853B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y853C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y854B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y854C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y855B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y855C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y856B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y856C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y857B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y857C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y858B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y858C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y859B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y859C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y860B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y860C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y861B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y861C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y862B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y862C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y863B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y863C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y864B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y864C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y865B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y865C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y867B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y867C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y868B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y868C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y869B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y869C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y870B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y870C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y871B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y871C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y872B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y872C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y873B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y873C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y874B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y874C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y875B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y875C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y876B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y876C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y877B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y877C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y878B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y878C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y879B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y879C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y880B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y880C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y881B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y881C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y882B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y882C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y883B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y883C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y884B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y884C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y885B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y885C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y886B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y886C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y887B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y887C';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y888B';
UPDATE IGNORE billingservice SET sliFlag = TRUE where service_code = 'Y888C';

DROP TABLE IF EXISTS `clinic_nbr`;

CREATE TABLE `clinic_nbr` (
  `nbr_id` int NOT NULL AUTO_INCREMENT,
  `nbr_value` varchar(11) NOT NULL,
  `nbr_string` text,
  `nbr_status` varchar(1),
  PRIMARY KEY (`nbr_id`)
);

LOCK TABLES `clinic_nbr` WRITE;
/*!40000 ALTER TABLE `clinic_nbr` DISABLE KEYS */;

INSERT INTO `clinic_nbr` 
VALUES
	(NULL, '22','R .  M .  A .', 'A'),
	(NULL, '33','AFP Ham Surgery RMA', 'A'),
	(NULL, '98','Bill Directs', 'A');

/*!40000 ALTER TABLE `clinic_nbr` ENABLE KEYS */;
UNLOCK TABLES;


insert into issue (code,description,role,UPDATE_date,priority,type,sortOrderId) values ('Misc','Misc','nurse',now(),NULL,'system',0);

CREATE TABLE `cssStyles` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255),
  `style` text,
  `status` char(1) NOT NULL,
  PRIMARY KEY (`id`)
);

alter table billingservice add column `displaystyle` int(10);

insert into `secObjectName` (`objectName`) values ('_resource');
insert into `secObjectName` (`objectName`) values ('_search');
insert into `secObjectName` (`objectName`) values ('_report');
insert into `secObjectName` (`objectName`) values ('_msg');
insert into `secObjectName` (`objectName`) values ('_con');
insert into `secObjectName` (`objectName`) values ('_pmm_agencyList');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.apptHistory');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.doctorName');

insert into `secObjPrivilege` values('doctor','_resource','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_search','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_report','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_msg','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_con','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_pmm_agencyList','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.apptHistory','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.doctorName','x',0,'999998');


alter table appointmentArchive add `id` int(10) not null auto_increment primary key;

alter table billingreferral change column referral_no referral_no varchar(6) not null;
alter table billingreferral drop index referral_no_2;

CREATE TABLE `RemoteIntegratedDataCopy` (
  id int(11) NOT NULL auto_increment,
  demographic_no int(10) ,
  datatype varchar(255) ,
  data longtext,
  lastUPDATEDate datetime ,
  signature varchar(255) ,
  facilityId int(11) ,
  provider_no varchar(6) ,
  archived tinyint(1) ,
  PRIMARY KEY  (`id`),
  KEY RIDopy_demo_dataT_fac_arch (`demographic_no`,`datatype`,`facilityId`,`archived`), 
  KEY RIDopy_demo_dataT_sig_fac_arch (`demographic_no`,`datatype`,`signature`,`facilityId`,`archived`)
);

alter table appointment add column creatorSecurityId int;

alter table program add column enableEncounterTime tinyint(1);
alter table program add column enableEncounterTransportationTime tinyint(1);

UPDATE program set enableEncounterTime = 0;
UPDATE program set enableEncounterTransportationTime = 0;

alter table EyeformMacro add `sliCode` varchar(10);

alter table appointmentArchive add column creatorSecurityId int;

CREATE TABLE `batch_billing` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(10),
  `billing_provider_no` int(10),
  `service_code` varchar(10),
  `dxcode` varchar(5),
  `billing_amount` varchar(10),
  `lastbilled_date` date,
  `create_date` timestamp,
  `creator` int(10),
  PRIMARY KEY (`id`)
);

-- 2012-03-27

alter table appointment add column bookingSource varchar(32);
alter table appointmentArchive add column bookingSource varchar(32);

-- 2012-03-30

alter table DemographicContact add facilityId int not null;
alter table DemographicContact add creator varchar(20) not null;
UPDATE DemographicContact set facilityId=1;

-- 2012-04-01
alter table batch_billing change billing_provider_no billing_provider_no varchar(6);
alter table batch_billing change creator creator varchar(6);

-- update-2011-06-29.1.sql

CREATE TABLE `HRMDocumentComment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `providerNo` varchar(20),
  `hrmDocumentId` int(11),
  `comment` text,
  `commentTime` datetime,
  `deleted` tinyint(1),
  PRIMARY KEY (`id`)
);

CREATE TABLE `HRMProviderConfidentialityStatement` (
  `providerNo` varchar(20),
  `statement` text,
  PRIMARY KEY (`providerNo`)
);

-- =========END OF FEATURES THAT MADE THE 12 RELEASE========= --














