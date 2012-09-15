-- Updates Oscar database from version 12 to 12_1 --
-- Good to update-2012-07-26.sql

-- =========START OF FEATURES THAT MADE THE 12_1 RELEASE========= --

-- UPDATE-2012-03-29.sql 

ALTER TABLE consultationRequests
ADD COLUMN `signature_img` VARCHAR(20),
ADD COLUMN letterheadName VARCHAR(20),
ADD COLUMN letterheadAddress TEXT,
ADD COLUMN letterheadPhone VARCHAR(50),
ADD COLUMN letterheadFax VARCHAR(50);


-- -------------------------


alter table batch_billing change billing_provider_no billing_provider_no varchar(6);
alter table batch_billing change creator creator varchar(6);

INSERT IGNORE INTO `encounterForm`(`form_name`,`form_value`,`form_table`,`hidden`) VALUES ('Health Passport', '../form/formbchp.jsp?demographic_no=', 'formBCHP', 0);

CREATE TABLE IF NOT EXISTS formBCHP (
  ID int(10) AUTO_INCREMENT,
  demographic_no int(10),
  provider_no int(10),
  formCreated date,
  formEdited timestamp ON UPDATE CURRENT_TIMESTAMP,
  c_lastVisited char(3),
  pg1_formDate date,
  pg1_patientName varchar(40),
  pg1_phn varchar(20),
  pg1_phone varchar(15),
  pg1_emergContact varchar(40),
  pg1_emergContactPhone varchar(15),
  pg1_allergies varchar(90),
  pg1_md varchar(40),
  pg1_msp varchar(9),
  pg1_mdPhone varchar(15),
  pg1_livingWillY tinyint(1),
  pg1_livingWillN tinyint(1),
  pg1_diabetes tinyint(1),
  pg1_atrialFib tinyint(1),
  pg1_coronary tinyint(1),
  pg1_highBP tinyint(1),
  pg1_chf tinyint(1),
  pg1_stroke tinyint(1),
  pg1_kidneyDisease tinyint(1),
  pg1_lowGFR varchar(10),
  pg1_asthma tinyint(1),
  pg1_copd tinyint(1),
  pg1_co2retainer tinyint(1),
  pg1_cancer tinyint(1),
  pg1_cancerSpec varchar(40),
  pg1_other tinyint(1),
  pg1_otherSpec varchar(90),
  pg1_majorSurg tinyint(1),
  pg1_majorSurgSpec varchar(80),
  pg1_date1 date,
  pg1_medName1 varchar(30),
  pg1_dose1 varchar(10),
  pg1_howOften1 varchar(10),
  pg1_reason1 varchar(20),
  pg1_date2 date,
  pg1_medName2 varchar(30),
  pg1_dose2 varchar(10),
  pg1_howOften2 varchar(10),
  pg1_reason2 varchar(20),
  pg1_date3 date,
  pg1_medName3 varchar(30),
  pg1_dose3 varchar(10),
  pg1_howOften3 varchar(10),
  pg1_reason3 varchar(20),
  pg1_date4 date,
  pg1_medName4 varchar(30),
  pg1_dose4 varchar(10),
  pg1_howOften4 varchar(10),
  pg1_reason4 varchar(20),
  pg1_date5 date,
  pg1_medName5 varchar(30),
  pg1_dose5 varchar(10),
  pg1_howOften5 varchar(10),
  pg1_reason5 varchar(20),
  pg1_date6 date,
  pg1_medName6 varchar(30),
  pg1_dose6 varchar(10),
  pg1_howOften6 varchar(10),
  pg1_reason6 varchar(20),
  pg1_date7 date,
  pg1_medName7 varchar(30),
  pg1_dose7 varchar(10),
  pg1_howOften7 varchar(10),
  pg1_reason7 varchar(20),
  pg1_date8 date,
  pg1_medName8 varchar(30),
  pg1_dose8 varchar(10),
  pg1_howOften8 varchar(10),
  pg1_reason8 varchar(20),
  pg1_date9 date,
  pg1_medName9 varchar(30),
  pg1_dose9 varchar(10),
  pg1_howOften9 varchar(10),
  pg1_reason9 varchar(20),
  pg1_date10 date,
  pg1_medName10 varchar(30),
  pg1_dose10 varchar(10),
  pg1_howOften10 varchar(10),
  pg1_reason10 varchar(20),
  pg1_date11 date,
  pg1_medName11 varchar(30),
  pg1_dose11 varchar(10),
  pg1_howOften11 varchar(10),
  pg1_reason11 varchar(20),
  pg1_date12 date,
  pg1_medName12 varchar(30),
  pg1_dose12 varchar(10),
  pg1_howOften12 varchar(10),
  pg1_reason12 varchar(20),
  pg1_date13 date,
  pg1_medName13 varchar(30),
  pg1_dose13 varchar(10),
  pg1_howOften13 varchar(10),
  pg1_reason13 varchar(20),
  pg1_date14 date,
  pg1_medName14 varchar(30),
  pg1_dose14 varchar(10),
  pg1_howOften14 varchar(10),
  pg1_reason14 varchar(20),
  pg1_fluDate1 date,
  pg1_fluDate2 date,
  pg1_fluDate3 date,
  pg1_fluDate4 date,
  pg1_fluDate5 date,
  pg1_fluDate6 date,
  pg1_fluDate7 date,
  pg1_pneumoVaccDate1 date,
  pg1_pneumoVaccDate2 date,
  pg1_pneumoVaccDate3 date,
  pg1_pneumoVaccDate4 date,
  pg1_pneumoVaccDate5 date,
  pg1_pneumoVaccDate6 date,
  pg1_pneumoVaccDate7 date,
  pg1_tdDate1 date,
  pg1_tdDate2 date,
  pg1_tdDate3 date,
  pg1_tdDate4 date,
  pg1_tdDate5 date,
  pg1_tdDate6 date,
  pg1_tdDate7 date,
  pg1_hepaDate1 date,
  pg1_hepaDate2 date,
  pg1_hepaDate3 date,
  pg1_hepaDate4 date,
  pg1_hepaDate5 date,
  pg1_hepaDate6 date,
  pg1_hepaDate7 date,
  pg1_hepbDate1 date,
  pg1_hepbDate2 date,
  pg1_hepbDate3 date,
  pg1_hepbDate4 date,
  pg1_hepbDate5 date,
  pg1_hepbDate6 date,
  pg1_hepbDate7 date,
  pg1_otherVac varchar(30),
  pg1_otherDate1 date,
  pg1_otherDate2 date,
  pg1_otherDate3 date,
  pg1_otherDate4 date,
  pg1_otherDate5 date,
  pg1_otherDate6 date,
  pg1_otherDate7 date,
  pg2_date1 date,
  pg2_medName1 varchar(30),
  pg2_dose1 varchar(10),
  pg2_howOften1 varchar(10),
  pg2_reason1 varchar(20),
  pg2_date2 date,
  pg2_medName2 varchar(30),
  pg2_dose2 varchar(10),
  pg2_howOften2 varchar(10),
  pg2_reason2 varchar(20),
  pg2_date3 date,
  pg2_medName3 varchar(30),
  pg2_dose3 varchar(10),
  pg2_howOften3 varchar(10),
  pg2_reason3 varchar(20),
  pg2_date4 date,
  pg2_medName4 varchar(30),
  pg2_dose4 varchar(10),
  pg2_howOften4 varchar(10),
  pg2_reason4 varchar(20),
  pg2_date5 date,
  pg2_medName5 varchar(30),
  pg2_dose5 varchar(10),
  pg2_howOften5 varchar(10),
  pg2_reason5 varchar(20),
  pg2_date6 date,
  pg2_medName6 varchar(30),
  pg2_dose6 varchar(10),
  pg2_howOften6 varchar(10),
  pg2_reason6 varchar(20),
  pg2_date7 date,
  pg2_medName7 varchar(30),
  pg2_dose7 varchar(10),
  pg2_howOften7 varchar(10),
  pg2_reason7 varchar(20),
  pg2_date8 date,
  pg2_medName8 varchar(30),
  pg2_dose8 varchar(10),
  pg2_howOften8 varchar(10),
  pg2_reason8 varchar(20),
  pg2_date9 date,
  pg2_medName9 varchar(30),
  pg2_dose9 varchar(10),
  pg2_howOften9 varchar(10),
  pg2_reason9 varchar(20),
  pg2_date10 date,
  pg2_medName10 varchar(30),
  pg2_dose10 varchar(10),
  pg2_howOften10 varchar(10),
  pg2_reason10 varchar(20),
  pg2_date11 date,
  pg2_medName11 varchar(30),
  pg2_dose11 varchar(10),
  pg2_howOften11 varchar(10),
  pg2_reason11 varchar(20),
  pg2_date12 date,
  pg2_medName12 varchar(30),
  pg2_dose12 varchar(10),
  pg2_howOften12 varchar(10),
  pg2_reason12 varchar(20),
  pg2_date13 date,
  pg2_medName13 varchar(30),
  pg2_dose13 varchar(10),
  pg2_howOften13 varchar(10),
  pg2_reason13 varchar(20),
  pg2_date14 date,
  pg2_medName14 varchar(30),
  pg2_dose14 varchar(10),
  pg2_howOften14 varchar(10),
  pg2_reason14 varchar(20),
  pg2_fluDate1 date,
  pg2_fluDate2 date,
  pg2_fluDate3 date,
  pg2_fluDate4 date,
  pg2_fluDate5 date,
  pg2_fluDate6 date,
  pg2_fluDate7 date,
  pg2_otherVac1 varchar(30),
  pg2_other1Date1 date,
  pg2_other1Date2 date,
  pg2_other1Date3 date,
  pg2_other1Date4 date,
  pg2_other1Date5 date,
  pg2_other1Date6 date,
  pg2_other1Date7 date,
  pg2_otherVac2 varchar(30),
  pg2_other2Date1 date,
  pg2_other2Date2 date,
  pg2_other2Date3 date,
  pg2_other2Date4 date,
  pg2_other2Date5 date,
  pg2_other2Date6 date,
  pg2_other2Date7 date,
  pg2_otherVac3 varchar(30),
  pg2_other3Date1 date,
  pg2_other3Date2 date,
  pg2_other3Date3 date,
  pg2_other3Date4 date,
  pg2_other3Date5 date,
  pg2_other3Date6 date,
  pg2_other3Date7 date,
  pg2_otherVac4 varchar(30),
  pg2_other4Date1 date,
  pg2_other4Date2 date,
  pg2_other4Date3 date,
  pg2_other4Date4 date,
  pg2_other4Date5 date,
  pg2_other4Date6 date,
  pg2_other4Date7 date,
  pg2_otherVac5 varchar(30),
  pg2_other5Date1 date,
  pg2_other5Date2 date,
  pg2_other5Date3 date,
  pg2_other5Date4 date,
  pg2_other5Date5 date,
  pg2_other5Date6 date,
  pg2_other5Date7 date,
  PRIMARY KEY (ID)
);
alter table provider change signed_confidentiality signed_confidentiality date;

alter table billing add column billingtype varchar(4) default 'MSP';
insert into OcanFormOption (ocanFormVersion, ocanDataCategory, ocanDataCategoryValue, ocanDataCategoryName) VALUES ('1.2','Practioner List','SIGOTHR','Significant Other');

alter table program add column emailNotificationAddressesCsv varchar(255);
alter table program add column lastReferralNotification datetime;


CREATE TABLE `vacancy_template` (
  `TEMPLATE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROGRAM_ID` int(11) NOT NULL,
  `NAME` varchar(32) NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL,
  PRIMARY KEY (`TEMPLATE_ID`)
);

insert into `secObjectName` (`objectName`) values ('_day');
insert into `secObjectName` (`objectName`) values ('_month');
insert into `secObjectName` (`objectName`) values ('_pref');
insert into `secObjectName` (`objectName`) values ('_edoc');
insert into `secObjectName` (`objectName`) values ('_tickler');
insert into `secObjectName` (`objectName`) values ('_pmm_client.BedRoomReservation');

insert into `secObjPrivilege` values('doctor','_day','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_month','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_pref','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_edoc','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_tickler','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_pmm_client.BedRoomReservation','x',0,'999998');

-- Doesn't seem to be present so commenting it out
-- alter table IntegratorControl drop foreign key IntegratorControl_ibfk_1;

CREATE TABLE `criteria_type` (
  `CRITERIA_TYPE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIELD_NAME` varchar(128) NOT NULL,
  `FIELD_TYPE` varchar(128) NOT NULL,
  `DEFAULT_VALUE` varchar(255),
  `ACTIVE` tinyint(1) NOT NULL,
  `WL_PROGRAM_ID` int(11),
  `CAN_BE_ADHOC` tinyint(1) NOT NULL,
  PRIMARY KEY (`CRITERIA_TYPE_ID`)
);

CREATE TABLE `criteria_type_option` (
  `OPTION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CRITERIA_TYPE_ID` int(11) NOT NULL,
  `DISPLAY_ORDER_NUMBER` int(11) NOT NULL,
  `OPTION_LABEL` varchar(128) NOT NULL,
  `OPTION_VALUE` varchar(255),
  `RANGE_START_VALUE` int(11),
  `RANGE_END_VALUE` int(11),
  PRIMARY KEY (`OPTION_ID`)
);

CREATE TABLE `criteria` (
  `CRITERIA_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CRITERIA_TYPE_ID` int(11) NOT NULL,
  `CRITERIA_VALUE` varchar(255),
  `RANGE_START_VALUE` int(11),
  `RANGE_END_VALUE` int(11),
  `TEMPLATE_ID` int(11),
  `VACANCY_ID` int(11),
  `MATCH_SCORE_WEIGHT` double NOT NULL,
  `CAN_BE_ADHOC` tinyint(1) NOT NULL,
  PRIMARY KEY (`CRITERIA_ID`)
);

CREATE TABLE `criteria_selection_option` (
  `SELECT_OPTION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CRITERIA_ID` int(11) NOT NULL,
  `OPTION_VALUE` varchar(255),
  PRIMARY KEY (`SELECT_OPTION_ID`)
);

CREATE TABLE `vacancy` (
  `VACANCY_ID` int(11) NOT NULL AUTO_INCREMENT,
  `TEMPLATE_ID` int(11) NOT NULL,
  `STATUS` varchar(24) NOT NULL,
  `DATE_CLOSED` timestamp NULL,
  `REASON_CLOSED` varchar(255),
  PRIMARY KEY (`VACANCY_ID`)
);



INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (1,'Agency','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (2,'Age','select_one',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (3,'Area','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (4,'Serious and Persistent Mental Illness','select_one',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (5,'Serious and Persistent Mental Illness Diagnosis','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (6,'Serious and Persistent Mental Illness Hospitalization','number','0',1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (7,'Type of Program','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (8,'Referral Source','select_one',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (9,'Legal History','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (10,'Residence','select_one',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (11,'Other Health Issues','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (12,'Language','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (13,'Gender','select_one',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (14,'Gender','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (15,'Homeless','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (16,'Mental health diagnosis','select_multiple',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (17,'Housing type','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (18,'Referral source','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (19,'Support level','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (20,'Geographic location','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (21,'Age category','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (22,'Current involvement with Criminal Justice system','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (23,'SHPPSU criteria','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (24,'Accessible unit','select_one',NULL,1,2,0);


INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (1,2,1,'Youth – 14 – 22',NULL,14,22);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (2,2,2,'Youth  - 16-24',NULL,16,24);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (3,3,1,'North York','North York',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (4,3,2,'Scarborough','Scarborough',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (5,3,3,'East York','East York',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (6,3,4,'Old City of York','Old City of York',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (7,3,5,'North Etobicoke','North Etobicoke',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (8,3,6,'South Etobicoke','South Etobicoke',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (9,3,7,'Downtown Toronto','Downtown Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (10,3,8,'East of Yonge','East of Yonge',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (11,3,9,'West of Yonge','West of Yonge',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (12,3,10,'Toronto','Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (13,2,3,'16 Years of age or older',NULL,16,120);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (14,2,4,'18 years of age or older',NULL,18,120);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (15,4,1,'Formal Diagnosis','Formal Diagnosis',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (16,4,2,'No formal Diagnosis','No formal Diagnosis',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (19,5,1,'Test diagnosis 1','Test diagnosis 1',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (20,5,2,'Test diagnosis 2','Test diagnosis 2',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (21,7,1,'Long-Term Case Management','Long-Term Case Management',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (22,7,2,'Short-term case management','Short-term case management',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (23,7,3,'Emergency Department Diversion Program','Emergency Department Diversion Program',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (24,7,4,'Assertive Community Treatment Team','Assertive Community Treatment Team',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (25,7,5,'Mental Health Outreach Program','Mental Health Outreach Program',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (26,7,6,'Language Specific Service (Across Boundaries, CRCT, WRAP, Pathways, Passages)','Language Specific Service',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (27,7,7,'Youth Programs','Youth Programs',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (28,7,8,'Early Intervention Programs','Early Intervention Programs',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (29,7,9,'Mental Health Prevention Program (short-term case management)','Mental Health Prevention Program (short-term case management)',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (30,7,10,'Seniors Case Management','Seniors Case Management',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (31,7,11,'TCAT (Addictions case management)','TCAT (Addictions case management)',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (32,7,12,'CATCH','CATCH',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (33,7,13,'CATCH - ED','CATCH - ED',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (34,1,1,'Across Boundaries','Across Boundaries',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (35,1,2,'Bayview Community Services','Bayview Community Services',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (36,8,1,'Organizational Referral Source','Organizational Referral Source',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (37,8,2,'Accredited Professional (i.e. private psychiatrist, family doctor etc)','Accredited Professional',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (38,8,3,' Self',' Self',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (39,8,4,'Family/Friend','Family/Friend',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (40,8,5,'Hospital (List of all hospitals)','Hospital',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (41,8,6,'Ontario Review Board','Ontario Review Board',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (42,8,7,'Alternative Access Route (i.e. internal referral, pre-existing agreement, alternate access route)','Alternative Access Route',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (43,9,1,'Test legal history 1','Test legal history 1',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (44,9,2,'Test legal history 2','Test legal history 2',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (45,10,1,'Housed','Housed',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (46,10,2,'Homeless','Homeless',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (47,10,3,'Transitional','Transitional',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (48,11,1,'Concurrent Disorder','Concurrent Disorder',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (49,11,2,'Dual Diagnosis','Dual Diagnosis',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (50,11,3,'Acquired brain injury','Acquired brain injury',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (51,11,4,'Psycho-geriatric  issues','Psycho-geriatric  issues',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (52,12,1,'English','English',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (53,12,2,'French','French',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (54,12,3,'Other','Other',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (55,13,1,'Male','Male',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (56,13,2,'Female','Female',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (57,14,1,'Male','Male',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (58,14,2,'Female','Female',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (59,15,1,'Homeless','Homeless',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (60,15,2,'At risk','At risk',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (61,15,3,'Housed','Housed',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (62,16,1,'Formal Diagnosis','Formal Diagnosis',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (63,16,2,'No formal Diagnosis','No formal Diagnosis',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (65,17,1,'Shared','Shared',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (66,17,2,'Independent','Independent',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (67,18,1,'Organizational Referral Source','Organizational Referral Source',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (68,18,2,'Accredited Professional (i.e. private psychiatrist, family doctor etc)','Accredited Professional',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (69,19,1,'Test level 1','Test level 1',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (70,19,2,'Test level 2','Test level 2',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (75,22,1,'Test involvement 1','Test involvement 1',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (76,22,2,'Test involvement 2','Test involvement 2',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (77,23,1,'Test SHPPSU criteria 1','Test SHPPSU criteria 1',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (78,23,2,'Test SHPPSU criteria 2','Test SHPPSU criteria 2',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (79,24,1,'Accessible unit required','Accessible unit required',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (80,24,2,'Accessible unit not required','Accessible unit not required',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (82,1,3,'CMHA (Toronto East)','CMHA (Toronto East)',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (83,1,4,'CMHA (Toronto West)','CMHA (Toronto West)',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (84,1,5,'COTA Health','COTA Health',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (85,1,6,'Community Resource Connections of Toronto','Community Resource Connections of Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (86,1,7,'Griffin Centre & Community Support Network','Griffin Centre & Community Support Network',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (87,1,8,'North York General Hospital','North York General Hospital',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (88,1,9,'Reconnect Mental Health Services','Reconnect Mental Health Services',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (89,1,10,'Saint Elizabeth Health Care','Saint Elizabeth Health Care',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (90,1,11,'Scarborough Hospital','Scarborough Hospital',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (91,1,12,'Sunnybrook Hospital','Sunnybrook Hospital',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (92,1,13,'Toronto North Support Services','Toronto North Support Services',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (93,13,3,'Transgender','Transgender',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (94,13,4,'Transsexual','Transsexual',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (95,13,5,'Other','Other',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (96,14,3,'Transgender','Transgender',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (97,14,4,'Transsexual','Transsexual',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (98,14,5,'Other','Other',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (99,18,3,' Self',' Self',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (100,18,4,'Family/Friend','Family/Friend',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (101,18,5,'Hospital (List of all hospitals)','Hospital',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (102,18,6,'Ontario Review Board','Ontario Review Board',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (103,18,7,'Alternative Access Route (i.e. internal referral, pre-existing agreement, alternate access route)','Alternative Access Route',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (104,20,1,'West End of Toronto (Bathurst to Islington, Lawrence to Lakeshore) ','West End of Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (105,20,1,'East End of Toronto (Don Valley to Victoria Park, Lawrence to Lakeshore)','East End of Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (106,20,1,'Downtown Core of Toronto (Bathurst to Don Valley, Lawrence to Lakeshore)','Downtown Core of Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (107,20,1,'North York East (North of Lawrence, East of Yonge to Victoria Park) ','North York East',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (108,20,1,'North York West (North of Lawrence, West of Yonge to Islington)','North York West',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (109,20,1,'Etobicoke (West of Islington) ','Etobicoke',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (110,20,1,'Scarborough (East of Victoria Park)','Scarborough',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (111,21,1,'Youth – 14 – 22',NULL,14,22);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (112,21,2,'Youth  - 16-24',NULL,16,24);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (113,21,3,'16 Years of age or older',NULL,16,120);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (114,21,4,'18 years of age or older',NULL,18,120);

create table MyGroupAccessRestriction (
        id int not null auto_increment,
        myGroupNo varchar(50) not null,
        providerNo varchar(20) not null,
        lastUpdateUser varchar(20),
        lastUpdateDate datetime,
        key(myGroupNo),
        key(myGroupNo,providerNo),
        primary key(id)
);

update program set transgender=0 where transgender is null;
alter table program change transgender transgender tinyint(1) not null;

update program set firstNation=0 where firstNation is null;
alter table program change firstNation firstNation tinyint(1) not null;

update program set bedProgramAffiliated=0 where bedProgramAffiliated is null;
alter table program change bedProgramAffiliated bedProgramAffiliated tinyint(1) not null;

update program set alcohol=0 where alcohol is null;
alter table program change alcohol alcohol tinyint(1) not null;

update program set physicalHealth=0 where physicalHealth is null;
alter table program change physicalHealth physicalHealth tinyint(1) not null;

update program set mentalHealth=0 where mentalHealth is null;
alter table program change mentalHealth mentalHealth tinyint(1) not null;

update program set housing=0 where housing is null;
alter table program change housing housing tinyint(1) not null;

update program set ageMin=1 where ageMin is null;
alter table program change ageMin ageMin int not null;

update program set ageMax=200 where ageMax is null;
alter table program change ageMax ageMax int not null;
alter table vacancy_template add `WL_PROGRAM_ID` int(11) NOT NULL;
update vacancy_template set WL_PROGRAM_ID=0;

alter table tickler_update modify status char(1);
alter table tickler_update add column assignedTo varchar(6);
alter table tickler_update add column serviceDate datetime;
alter table tickler_update add column priority varchar(6);
create table tickler_text_suggest (id int(10) not null AUTO_INCREMENT, creator varchar(6) not null, suggested_text varchar(255) not null, create_date timestamp not null, active tinyint(1) not null, PRIMARY KEY (id));

insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised Test Results", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC see INFO", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC see MD", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC for Rx", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC for Lab Work", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC for immunization", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Declined treatment", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Dont call", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Letter sent", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Msg on ans. mach. to call clinic", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Msg with roomate to call clinic", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Phone - No Answer", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Notified", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Notified. Patient is asymptomatic.", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Prescription given", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Prescription phoned in to:", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Referral Booked", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Re-Booked for followup", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Returned for Lab Work", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Telephone Busy", now(), "1");

ALTER TABLE professionalSpecialists ADD COLUMN referralNo VARCHAR(6);

INSERT INTO professionalSpecialists (specId, fName, lName, phone, fax, specType, lastUpdated, address, referralNo)
(SELECT billingreferral_no+900000 AS specId, last_name AS lName, first_name AS fName, phone AS phone, fax AS fax, specialty AS specType, NOW() AS lastUpdated,
CONCAT(
IFNULL(CONCAT(address1, "\n"), ""),
IFNULL(CONCAT(address2, "\n"), ""),
IFNULL(CONCAT(city, " "), ""),
IFNULL(province, ""),
IFNULL(CONCAT("\n", postal), ""),
IFNULL(CONCAT("\n", country), "")
) AS address,
referral_no AS referralNo
 FROM billingreferral);

UPDATE EyeformConsultationReport SET referralId=(referralId+900000);

# ALTER TABLE billingreferral add country varchar(30) after province;
# update billingreferral set country='CA';

create table Episode (
        id int not null auto_increment primary key,
        demographicNo int not null,
        startDate date not null,
        endDate date,
        code varchar(50),
        codingSystem varchar(50),
        description varchar(255),
        status varchar(25),
        lastUpdateUser varchar(25) not null,
        lastUpdateTime timestamp not null
);

insert into OcanFormOption values (1280,"1.2","Years in Canada","51","51");
insert into OcanFormOption values (1281,"1.2","Years in Canada","52","52");
insert into OcanFormOption values (1282,"1.2","Years in Canada","53","53");
insert into OcanFormOption values (1283,"1.2","Years in Canada","54","54");
insert into OcanFormOption values (1284,"1.2","Years in Canada","55","55");
insert into OcanFormOption values (1285,"1.2","Years in Canada","56","56");
insert into OcanFormOption values (1286,"1.2","Years in Canada","57","57");
insert into OcanFormOption values (1287,"1.2","Years in Canada","58","58");
insert into OcanFormOption values (1288,"1.2","Years in Canada","59","59");
insert into OcanFormOption values (1289,"1.2","Years in Canada","60","60");
insert into OcanFormOption values (1290,"1.2","Years in Canada","61","61");
insert into OcanFormOption values (1291,"1.2","Years in Canada","62","62");
insert into OcanFormOption values (1292,"1.2","Years in Canada","63","63");
insert into OcanFormOption values (1293,"1.2","Years in Canada","64","64");
insert into OcanFormOption values (1294,"1.2","Years in Canada","65","65");
insert into OcanFormOption values (1295,"1.2","Years in Canada","66","66");
insert into OcanFormOption values (1296,"1.2","Years in Canada","67","67");
insert into OcanFormOption values (1297,"1.2","Years in Canada","68","68");
insert into OcanFormOption values (1298,"1.2","Years in Canada","69","69");
insert into OcanFormOption values (1299,"1.2","Years in Canada","70","70");
insert into OcanFormOption values (1300,"1.2","Years in Canada","71","71");
insert into OcanFormOption values (1301,"1.2","Years in Canada","72","72");
insert into OcanFormOption values (1302,"1.2","Years in Canada","73","73");
insert into OcanFormOption values (1303,"1.2","Years in Canada","74","74");
insert into OcanFormOption values (1304,"1.2","Years in Canada","75","75");
insert into OcanFormOption values (1305,"1.2","Years in Canada","76","76");
insert into OcanFormOption values (1306,"1.2","Years in Canada","77","77");
insert into OcanFormOption values (1307,"1.2","Years in Canada","78","78");
insert into OcanFormOption values (1308,"1.2","Years in Canada","79","79");
insert into OcanFormOption values (1309,"1.2","Years in Canada","80","80");
insert into OcanFormOption values (1310,"1.2","Years in Canada","81","81");
insert into OcanFormOption values (1311,"1.2","Years in Canada","82","82");
insert into OcanFormOption values (1312,"1.2","Years in Canada","83","83");
insert into OcanFormOption values (1313,"1.2","Years in Canada","84","84");
insert into OcanFormOption values (1314,"1.2","Years in Canada","85","85");
insert into OcanFormOption values (1315,"1.2","Years in Canada","86","86");
insert into OcanFormOption values (1316,"1.2","Years in Canada","87","87");
insert into OcanFormOption values (1317,"1.2","Years in Canada","88","88");
insert into OcanFormOption values (1318,"1.2","Years in Canada","89","89");
insert into OcanFormOption values (1319,"1.2","Years in Canada","90","90");
insert into OcanFormOption values (1320,"1.2","Years in Canada","91","91");
insert into OcanFormOption values (1321,"1.2","Years in Canada","92","92");
insert into OcanFormOption values (1322,"1.2","Years in Canada","93","93");
insert into OcanFormOption values (1323,"1.2","Years in Canada","94","94");
insert into OcanFormOption values (1324,"1.2","Years in Canada","95","95");
insert into OcanFormOption values (1325,"1.2","Years in Canada","96","96");
insert into OcanFormOption values (1326,"1.2","Years in Canada","97","97");
insert into OcanFormOption values (1327,"1.2","Years in Canada","98","98");
insert into OcanFormOption values (1328,"1.2","Years in Canada","99","99");
insert into OcanFormOption values (1329,"1.2","Years in Canada","100","100");

alter table vacancy add `WL_PROGRAM_ID` int(11) NOT NULL;
update vacancy set WL_PROGRAM_ID=0;
alter table vacancy add `DATE_CREATE` date NOT NULL;
update vacancy set DATE_CREATE=now();


--
-- Table structure for table `formONAREnhanced`
--

CREATE TABLE IF NOT EXISTS `formONAREnhanced` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `episodeId` int(10) DEFAULT NULL,
  `sent_to_born` tinyint(1) DEFAULT '0',
  `obxhx_num` varchar(10) DEFAULT '0',
  `rf_num` varchar(10) DEFAULT '0',
  `sv_num` varchar(10) DEFAULT '0',
  `us_num` varchar(10) DEFAULT '0',
  `demographic_no` int(10) NOT NULL DEFAULT '0',
  `provider_no` int(10) DEFAULT NULL,
  `formCreated` date DEFAULT NULL,
  `formEdited` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `c_lastName` varchar(80) DEFAULT NULL,
  `c_firstName` varchar(80) DEFAULT NULL,
  `c_address` varchar(80) DEFAULT NULL,
  `c_apt` varchar(20) DEFAULT NULL,
  `c_city` varchar(80) DEFAULT NULL,
  `c_province` varchar(80) DEFAULT NULL,
  `c_postal` varchar(10) DEFAULT NULL,
  `c_partnerLastName` varchar(80) DEFAULT NULL,
  `c_partnerFirstName` varchar(80) DEFAULT NULL,
  `pg1_homePhone` varchar(20) DEFAULT NULL,
  `pg1_workPhone` varchar(20) DEFAULT NULL,
  `pg1_language` varchar(25) DEFAULT NULL,
  `pg1_partnerOccupation` varchar(25) DEFAULT NULL,
  `c_partnerOccupationOther` varchar(255) DEFAULT NULL,
  `pg1_partnerEduLevel` varchar(25) DEFAULT NULL,
  `pg1_partnerAge` varchar(5) DEFAULT NULL,
  `pg1_dateOfBirth` date DEFAULT NULL,
  `pg1_age` varchar(10) DEFAULT NULL,
  `pg1_occupation` varchar(25) DEFAULT NULL,
  `pg1_occupationOther` varchar(255) DEFAULT NULL,
  `pg1_eduLevel` varchar(25) DEFAULT NULL,
  `pg1_ethnicBgMother` varchar(200) DEFAULT NULL,
  `pg1_ethnicBgFather` varchar(200) DEFAULT NULL,
  `c_hin` varchar(20) DEFAULT NULL,
  `c_hinType` varchar(20) DEFAULT NULL,
  `c_fileNo` varchar(20) DEFAULT NULL,
  `pg1_maritalStatus` varchar(20) DEFAULT NULL,
  `pg1_msSingle` tinyint(1) DEFAULT NULL,
  `pg1_msCommonLaw` tinyint(1) DEFAULT NULL,
  `pg1_msMarried` tinyint(1) DEFAULT NULL,
  `pg1_baObs` tinyint(1) DEFAULT NULL,
  `pg1_baFP` tinyint(1) DEFAULT NULL,
  `pg1_baMidwife` tinyint(1) DEFAULT NULL,
  `c_ba` varchar(25) DEFAULT NULL,
  `pg1_ncPed` tinyint(1) DEFAULT NULL,
  `pg1_ncFP` tinyint(1) DEFAULT NULL,
  `pg1_ncMidwife` tinyint(1) DEFAULT NULL,
  `c_nc` varchar(25) DEFAULT NULL,
  `c_famPhys` varchar(80) DEFAULT NULL,
  `c_allergies` text,
  `c_meds` text,
  `pg1_menLMP` date DEFAULT NULL,
  `pg1_psCertY` tinyint(1) DEFAULT NULL,
  `pg1_psCertN` tinyint(1) DEFAULT NULL,
  `pg1_menCycle` varchar(7) DEFAULT NULL,
  `pg1_menReg` tinyint(1) DEFAULT NULL,
  `pg1_menRegN` tinyint(1) DEFAULT NULL,
  `pg1_contracep` varchar(25) DEFAULT NULL,
  `pg1_lastUsed` date DEFAULT NULL,
  `pg1_menEDB` date DEFAULT NULL,
  `c_finalEDB` date DEFAULT NULL,
  `pg1_edbByDate` tinyint(1) DEFAULT NULL,
  `pg1_edbByT1` tinyint(1) DEFAULT NULL,
  `pg1_edbByT2` tinyint(1) DEFAULT NULL,
  `pg1_edbByART` tinyint(1) DEFAULT NULL,
  `c_gravida` varchar(3) DEFAULT NULL,
  `c_term` varchar(3) DEFAULT NULL,
  `c_prem` varchar(3) DEFAULT NULL,
  `c_abort` varchar(3) DEFAULT NULL,
  `c_living` varchar(3) DEFAULT NULL,
  `pg1_year1` varchar(10) DEFAULT NULL,
  `pg1_sex1` char(1) DEFAULT NULL,
  `pg1_oh_gest1` varchar(5) DEFAULT NULL,
  `pg1_weight1` varchar(6) DEFAULT NULL,
  `pg1_length1` varchar(6) DEFAULT NULL,
  `pg1_place1` varchar(20) DEFAULT NULL,
  `pg1_svb1` tinyint(1) DEFAULT NULL,
  `pg1_cs1` tinyint(1) DEFAULT NULL,
  `pg1_ass1` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments1` varchar(80) DEFAULT NULL,
  `pg1_year2` varchar(10) DEFAULT NULL,
  `pg1_sex2` char(1) DEFAULT NULL,
  `pg1_oh_gest2` varchar(5) DEFAULT NULL,
  `pg1_weight2` varchar(6) DEFAULT NULL,
  `pg1_length2` varchar(6) DEFAULT NULL,
  `pg1_place2` varchar(20) DEFAULT NULL,
  `pg1_svb2` tinyint(1) DEFAULT NULL,
  `pg1_cs2` tinyint(1) DEFAULT NULL,
  `pg1_ass2` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments2` varchar(80) DEFAULT NULL,
  `pg1_year3` varchar(10) DEFAULT NULL,
  `pg1_sex3` char(1) DEFAULT NULL,
  `pg1_oh_gest3` varchar(5) DEFAULT NULL,
  `pg1_weight3` varchar(6) DEFAULT NULL,
  `pg1_length3` varchar(6) DEFAULT NULL,
  `pg1_place3` varchar(20) DEFAULT NULL,
  `pg1_svb3` tinyint(1) DEFAULT NULL,
  `pg1_cs3` tinyint(1) DEFAULT NULL,
  `pg1_ass3` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments3` varchar(80) DEFAULT NULL,
  `pg1_year4` varchar(10) DEFAULT NULL,
  `pg1_sex4` char(1) DEFAULT NULL,
  `pg1_oh_gest4` varchar(5) DEFAULT NULL,
  `pg1_weight4` varchar(6) DEFAULT NULL,
  `pg1_length4` varchar(6) DEFAULT NULL,
  `pg1_place4` varchar(20) DEFAULT NULL,
  `pg1_svb4` tinyint(1) DEFAULT NULL,
  `pg1_cs4` tinyint(1) DEFAULT NULL,
  `pg1_ass4` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments4` varchar(80) DEFAULT NULL,
  `pg1_year5` varchar(10) DEFAULT NULL,
  `pg1_sex5` char(1) DEFAULT NULL,
  `pg1_oh_gest5` varchar(5) DEFAULT NULL,
  `pg1_weight5` varchar(6) DEFAULT NULL,
  `pg1_length5` varchar(6) DEFAULT NULL,
  `pg1_place5` varchar(20) DEFAULT NULL,
  `pg1_svb5` tinyint(1) DEFAULT NULL,
  `pg1_cs5` tinyint(1) DEFAULT NULL,
  `pg1_ass5` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments5` varchar(80) DEFAULT NULL,
  `pg1_year6` varchar(10) DEFAULT NULL,
  `pg1_sex6` char(1) DEFAULT NULL,
  `pg1_oh_gest6` varchar(5) DEFAULT NULL,
  `pg1_weight6` varchar(6) DEFAULT NULL,
  `pg1_length6` varchar(6) DEFAULT NULL,
  `pg1_place6` varchar(20) DEFAULT NULL,
  `pg1_svb6` tinyint(1) DEFAULT NULL,
  `pg1_cs6` tinyint(1) DEFAULT NULL,
  `pg1_ass6` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments6` varchar(80) DEFAULT NULL,
  `pg1_year7` varchar(10) DEFAULT NULL,
  `pg1_sex7` char(1) DEFAULT NULL,
  `pg1_oh_gest7` varchar(5) DEFAULT NULL,
  `pg1_weight7` varchar(6) DEFAULT NULL,
  `pg1_length7` varchar(6) DEFAULT NULL,
  `pg1_place7` varchar(20) DEFAULT NULL,
  `pg1_svb7` tinyint(1) DEFAULT NULL,
  `pg1_cs7` tinyint(1) DEFAULT NULL,
  `pg1_ass7` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments7` varchar(80) DEFAULT NULL,
  `pg1_year8` varchar(10) DEFAULT NULL,
  `pg1_sex8` char(1) DEFAULT NULL,
  `pg1_oh_gest8` varchar(5) DEFAULT NULL,
  `pg1_weight8` varchar(6) DEFAULT NULL,
  `pg1_length8` varchar(6) DEFAULT NULL,
  `pg1_place8` varchar(20) DEFAULT NULL,
  `pg1_svb8` tinyint(1) DEFAULT NULL,
  `pg1_cs8` tinyint(1) DEFAULT NULL,
  `pg1_ass8` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments8` varchar(80) DEFAULT NULL,
  `pg1_year9` varchar(10) DEFAULT NULL,
  `pg1_sex9` char(1) DEFAULT NULL,
  `pg1_oh_gest9` varchar(5) DEFAULT NULL,
  `pg1_weight9` varchar(6) DEFAULT NULL,
  `pg1_length9` varchar(6) DEFAULT NULL,
  `pg1_place9` varchar(20) DEFAULT NULL,
  `pg1_svb9` tinyint(1) DEFAULT NULL,
  `pg1_cs9` tinyint(1) DEFAULT NULL,
  `pg1_ass9` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments9` varchar(80) DEFAULT NULL,
  `pg1_year10` varchar(10) DEFAULT NULL,
  `pg1_sex10` char(1) DEFAULT NULL,
  `pg1_oh_gest10` varchar(5) DEFAULT NULL,
  `pg1_weight10` varchar(6) DEFAULT NULL,
  `pg1_length10` varchar(6) DEFAULT NULL,
  `pg1_place10` varchar(20) DEFAULT NULL,
  `pg1_svb10` tinyint(1) DEFAULT NULL,
  `pg1_cs10` tinyint(1) DEFAULT NULL,
  `pg1_ass10` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments10` varchar(80) DEFAULT NULL,
  `pg1_year11` varchar(10) DEFAULT NULL,
  `pg1_sex11` char(1) DEFAULT NULL,
  `pg1_oh_gest11` varchar(5) DEFAULT NULL,
  `pg1_weight11` varchar(6) DEFAULT NULL,
  `pg1_length11` varchar(6) DEFAULT NULL,
  `pg1_place11` varchar(20) DEFAULT NULL,
  `pg1_svb11` tinyint(1) DEFAULT NULL,
  `pg1_cs11` tinyint(1) DEFAULT NULL,
  `pg1_ass11` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments11` varchar(80) DEFAULT NULL,
  `pg1_year12` varchar(10) DEFAULT NULL,
  `pg1_sex12` char(1) DEFAULT NULL,
  `pg1_oh_gest12` varchar(5) DEFAULT NULL,
  `pg1_weight12` varchar(6) DEFAULT NULL,
  `pg1_length12` varchar(6) DEFAULT NULL,
  `pg1_place12` varchar(20) DEFAULT NULL,
  `pg1_svb12` tinyint(1) DEFAULT NULL,
  `pg1_cs12` tinyint(1) DEFAULT NULL,
  `pg1_ass12` tinyint(1) DEFAULT NULL,
  `pg1_oh_comments12` varchar(80) DEFAULT NULL,
  `pg1_cp1` tinyint(1) DEFAULT NULL,
  `pg1_cp1N` tinyint(1) DEFAULT NULL,
  `pg1_cp2` tinyint(1) DEFAULT NULL,
  `pg1_cp2N` tinyint(1) DEFAULT NULL,
  `pg1_box3` varchar(10) DEFAULT NULL,
  `pg1_cp3` tinyint(1) DEFAULT NULL,
  `pg1_cp3N` tinyint(1) DEFAULT NULL,
  `pg1_cp4` tinyint(1) DEFAULT NULL,
  `pg1_cp4N` tinyint(1) DEFAULT NULL,
  `pg1_cp8` tinyint(1) DEFAULT NULL,
  `pg1_cp8N` tinyint(1) DEFAULT NULL,
  `pg1_naDiet` tinyint(1) DEFAULT NULL,
  `pg1_naDietN` tinyint(1) DEFAULT NULL,
  `pg1_naMilk` tinyint(1) DEFAULT NULL,
  `pg1_naMilkN` tinyint(1) DEFAULT NULL,
  `pg1_naFolic` tinyint(1) DEFAULT NULL,
  `pg1_naFolicN` tinyint(1) DEFAULT NULL,
  `pg1_yes9` tinyint(1) DEFAULT NULL,
  `pg1_no9` tinyint(1) DEFAULT NULL,
  `pg1_yes10` tinyint(1) DEFAULT NULL,
  `pg1_no10` tinyint(1) DEFAULT NULL,
  `pg1_yes12` tinyint(1) DEFAULT NULL,
  `pg1_no12` tinyint(1) DEFAULT NULL,
  `pg1_yes13` tinyint(1) DEFAULT NULL,
  `pg1_no13` tinyint(1) DEFAULT NULL,
  `pg1_yes14` tinyint(1) DEFAULT NULL,
  `pg1_no14` tinyint(1) DEFAULT NULL,
  `pg1_yes17` tinyint(1) DEFAULT NULL,
  `pg1_no17` tinyint(1) DEFAULT NULL,
  `pg1_yes22` tinyint(1) DEFAULT NULL,
  `pg1_no22` tinyint(1) DEFAULT NULL,
  `pg1_yes20` tinyint(1) DEFAULT NULL,
  `pg1_no20` tinyint(1) DEFAULT NULL,
  `pg1_bloodTranY` tinyint(1) DEFAULT NULL,
  `pg1_bloodTranN` tinyint(1) DEFAULT NULL,
  `pg1_yes21` tinyint(1) DEFAULT NULL,
  `pg1_no21` tinyint(1) DEFAULT NULL,
  `pg1_yes24` tinyint(1) DEFAULT NULL,
  `pg1_no24` tinyint(1) DEFAULT NULL,
  `pg1_yes15` tinyint(1) DEFAULT NULL,
  `pg1_no15` tinyint(1) DEFAULT NULL,
  `pg1_box25` varchar(25) DEFAULT NULL,
  `pg1_yes25` tinyint(1) DEFAULT NULL,
  `pg1_no25` tinyint(1) DEFAULT NULL,
  `pg1_yes27` tinyint(1) DEFAULT NULL,
  `pg1_no27` tinyint(1) DEFAULT NULL,
  `pg1_yes31` tinyint(1) DEFAULT NULL,
  `pg1_no31` tinyint(1) DEFAULT NULL,
  `pg1_yes32` tinyint(1) DEFAULT NULL,
  `pg1_no32` tinyint(1) DEFAULT NULL,
  `pg1_yes34` tinyint(1) DEFAULT NULL,
  `pg1_no34` tinyint(1) DEFAULT NULL,
  `pg1_yes35` tinyint(1) DEFAULT NULL,
  `pg1_no35` tinyint(1) DEFAULT NULL,
  `pg1_idt40` tinyint(1) DEFAULT NULL,
  `pg1_idt40N` tinyint(1) DEFAULT NULL,
  `pg1_idt38` tinyint(1) DEFAULT NULL,
  `pg1_idt38N` tinyint(1) DEFAULT NULL,
  `pg1_idt42` tinyint(1) DEFAULT NULL,
  `pg1_idt42N` tinyint(1) DEFAULT NULL,
  `pg1_infectDisOther` varchar(20) DEFAULT NULL,
  `pg1_infectDisOtherY` tinyint(1) DEFAULT NULL,
  `pg1_infectDisOtherN` tinyint(1) DEFAULT NULL,
  `pg1_pdt43` tinyint(1) DEFAULT NULL,
  `pg1_pdt43N` tinyint(1) DEFAULT NULL,
  `pg1_pdt44` tinyint(1) DEFAULT NULL,
  `pg1_pdt44N` tinyint(1) DEFAULT NULL,
  `pg1_pdt45` tinyint(1) DEFAULT NULL,
  `pg1_pdt45N` tinyint(1) DEFAULT NULL,
  `pg1_pdt46` tinyint(1) DEFAULT NULL,
  `pg1_pdt46N` tinyint(1) DEFAULT NULL,
  `pg1_pdt47` tinyint(1) DEFAULT NULL,
  `pg1_pdt47N` tinyint(1) DEFAULT NULL,
  `pg1_pdt48` tinyint(1) DEFAULT NULL,
  `pg1_pdt48N` tinyint(1) DEFAULT NULL,
  `pg1_reliCultY` tinyint(1) DEFAULT NULL,
  `pg1_reliCultN` tinyint(1) DEFAULT NULL,
  `pg1_fhRiskY` tinyint(1) DEFAULT NULL,
  `pg1_fhRiskN` tinyint(1) DEFAULT NULL,
  `pg1_ht` varchar(6) DEFAULT NULL,
  `pg1_wt` varchar(6) DEFAULT NULL,
  `c_bmi` varchar(6) DEFAULT NULL,
  `pg1_BP` varchar(10) DEFAULT NULL,
  `pg1_thyroid` tinyint(1) DEFAULT NULL,
  `pg1_thyroidA` tinyint(1) DEFAULT NULL,
  `pg1_chest` tinyint(1) DEFAULT NULL,
  `pg1_chestA` tinyint(1) DEFAULT NULL,
  `pg1_breasts` tinyint(1) DEFAULT NULL,
  `pg1_breastsA` tinyint(1) DEFAULT NULL,
  `pg1_cardio` tinyint(1) DEFAULT NULL,
  `pg1_cardioA` tinyint(1) DEFAULT NULL,
  `pg1_abdomen` tinyint(1) DEFAULT NULL,
  `pg1_abdomenA` tinyint(1) DEFAULT NULL,
  `pg1_vari` tinyint(1) DEFAULT NULL,
  `pg1_variA` tinyint(1) DEFAULT NULL,
  `pg1_extGen` tinyint(1) DEFAULT NULL,
  `pg1_extGenA` tinyint(1) DEFAULT NULL,
  `pg1_cervix` tinyint(1) DEFAULT NULL,
  `pg1_cervixA` tinyint(1) DEFAULT NULL,
  `pg1_uterus` tinyint(1) DEFAULT NULL,
  `pg1_uterusA` tinyint(1) DEFAULT NULL,
  `pg1_uterusBox` varchar(3) DEFAULT NULL,
  `pg1_adnexa` tinyint(1) DEFAULT NULL,
  `pg1_adnexaA` tinyint(1) DEFAULT NULL,
  `pg1_pExOtherDesc` varchar(20) DEFAULT NULL,
  `pg1_pExOther` tinyint(1) DEFAULT NULL,
  `pg1_pExOtherA` tinyint(1) DEFAULT NULL,
  `pg1_labHb` varchar(20) DEFAULT NULL,
  `pg1_labHIV` varchar(20) DEFAULT NULL,
  `pg1_labMCV` varchar(20) DEFAULT NULL,
  `pg1_labHIVCounsel` tinyint(1) DEFAULT NULL,
  `pg1_labABO` varchar(20) DEFAULT NULL,
  `pg1_labLastPapDate` varchar(10) DEFAULT NULL,
  `pg1_labLastPap` varchar(20) DEFAULT NULL,
  `pg1_labCustom1Label` varchar(40) DEFAULT NULL,
  `pg1_labCustom1Result` varchar(40) DEFAULT NULL,
  `pg1_labCustom2Label` varchar(40) DEFAULT NULL,
  `pg1_labCustom2Result` varchar(40) DEFAULT NULL,
  `pg1_labCustom3Label` varchar(40) DEFAULT NULL,
  `pg1_labCustom3Result` varchar(40) DEFAULT NULL,
  `pg1_labCustom4Label` varchar(40) DEFAULT NULL,
  `pg1_labCustom4Result` varchar(40) DEFAULT NULL,
  `pg1_labRh` varchar(20) DEFAULT NULL,
  `pg1_labAntiScr` varchar(20) DEFAULT NULL,
  `pg1_labGC` varchar(20) DEFAULT NULL,
  `pg1_labChlamydia` varchar(20) DEFAULT NULL,
  `pg1_labRubella` varchar(20) DEFAULT NULL,
  `pg1_labUrine` varchar(20) DEFAULT NULL,
  `pg1_labHBsAg` varchar(20) DEFAULT NULL,
  `pg1_labVDRL` varchar(20) DEFAULT NULL,
  `pg1_labSickle` varchar(20) DEFAULT NULL,
  `pg1_geneticA` varchar(20) DEFAULT NULL,
  `pg1_geneticB` varchar(20) DEFAULT NULL,
  `pg1_geneticC` varchar(20) DEFAULT NULL,
  `pg1_geneticD` varchar(20) DEFAULT NULL,
  `pg1_geneticD1` tinyint(1) DEFAULT NULL,
  `pg1_geneticD2` tinyint(1) DEFAULT NULL,
  `pg1_commentsAR1` text,
  `pg1_comments2AR1` text,
  `pg1_signature` varchar(50) DEFAULT NULL,
  `pg1_formDate` date DEFAULT NULL,
  `pg1_signature2` varchar(50) DEFAULT NULL,
  `pg1_formDate2` date DEFAULT NULL,
  `c_riskFactors1` varchar(50) DEFAULT NULL,
  `c_planManage1` varchar(100) DEFAULT NULL,
  `c_riskFactors2` varchar(50) DEFAULT NULL,
  `c_planManage2` varchar(100) DEFAULT NULL,
  `c_riskFactors3` varchar(50) DEFAULT NULL,
  `c_planManage3` varchar(100) DEFAULT NULL,
  `c_riskFactors4` varchar(50) DEFAULT NULL,
  `c_planManage4` varchar(100) DEFAULT NULL,
  `c_riskFactors5` varchar(50) DEFAULT NULL,
  `c_planManage5` varchar(100) DEFAULT NULL,
  `c_riskFactors6` varchar(50) DEFAULT NULL,
  `c_planManage6` varchar(100) DEFAULT NULL,
  `c_riskFactors7` varchar(50) DEFAULT NULL,
  `c_planManage7` varchar(100) DEFAULT NULL,
  `c_riskFactors8` varchar(50) DEFAULT NULL,
  `c_planManage8` varchar(100) DEFAULT NULL,
  `c_riskFactors9` varchar(50) DEFAULT NULL,
  `c_planManage9` varchar(100) DEFAULT NULL,
  `c_riskFactors10` varchar(50) DEFAULT NULL,
  `c_planManage10` varchar(100) DEFAULT NULL,
  `c_riskFactors11` varchar(50) DEFAULT NULL,
  `c_planManage11` varchar(100) DEFAULT NULL,
  `c_riskFactors12` varchar(50) DEFAULT NULL,
  `c_planManage12` varchar(100) DEFAULT NULL,
  `c_riskFactors13` varchar(50) DEFAULT NULL,
  `c_planManage13` varchar(100) DEFAULT NULL,
  `c_riskFactors14` varchar(50) DEFAULT NULL,
  `c_planManage14` varchar(100) DEFAULT NULL,
  `c_riskFactors15` varchar(50) DEFAULT NULL,
  `c_planManage15` varchar(100) DEFAULT NULL,
  `c_riskFactors16` varchar(50) DEFAULT NULL,
  `c_planManage16` varchar(100) DEFAULT NULL,
  `c_riskFactors17` varchar(50) DEFAULT NULL,
  `c_planManage17` varchar(100) DEFAULT NULL,
  `c_riskFactors18` varchar(50) DEFAULT NULL,
  `c_planManage18` varchar(100) DEFAULT NULL,
  `c_riskFactors19` varchar(50) DEFAULT NULL,
  `c_planManage19` varchar(100) DEFAULT NULL,
  `c_riskFactors20` varchar(50) DEFAULT NULL,
  `c_planManage20` varchar(100) DEFAULT NULL,
  `ar2_rhNeg` tinyint(1) DEFAULT NULL,
  `ar2_rhIG` varchar(10) DEFAULT NULL,
  `ar2_rubella` tinyint(1) DEFAULT NULL,
  `ar2_hepBIG` tinyint(1) DEFAULT NULL,
  `ar2_hepBVac` tinyint(1) DEFAULT NULL,
  `pg2_date1` date DEFAULT NULL,
  `pg2_gest1` varchar(6) DEFAULT NULL,
  `pg2_wt1` varchar(6) DEFAULT NULL,
  `pg2_BP1` varchar(8) DEFAULT NULL,
  `pg2_urinePr1` char(3) DEFAULT NULL,
  `pg2_urineGl1` char(3) DEFAULT NULL,
  `pg2_ht1` varchar(6) DEFAULT NULL,
  `pg2_presn1` varchar(6) DEFAULT NULL,
  `pg2_FHR1` varchar(6) DEFAULT NULL,
  `pg2_comments1` varchar(255) DEFAULT NULL,
  `pg2_date2` date DEFAULT NULL,
  `pg2_gest2` varchar(6) DEFAULT NULL,
  `pg2_ht2` varchar(6) DEFAULT NULL,
  `pg2_wt2` varchar(6) DEFAULT NULL,
  `pg2_presn2` varchar(6) DEFAULT NULL,
  `pg2_FHR2` varchar(6) DEFAULT NULL,
  `pg2_urinePr2` char(3) DEFAULT NULL,
  `pg2_urineGl2` char(3) DEFAULT NULL,
  `pg2_BP2` varchar(8) DEFAULT NULL,
  `pg2_comments2` varchar(255) DEFAULT NULL,
  `pg2_date3` date DEFAULT NULL,
  `pg2_gest3` varchar(6) DEFAULT NULL,
  `pg2_ht3` varchar(6) DEFAULT NULL,
  `pg2_wt3` varchar(6) DEFAULT NULL,
  `pg2_presn3` varchar(6) DEFAULT NULL,
  `pg2_FHR3` varchar(6) DEFAULT NULL,
  `pg2_urinePr3` char(3) DEFAULT NULL,
  `pg2_urineGl3` char(3) DEFAULT NULL,
  `pg2_BP3` varchar(8) DEFAULT NULL,
  `pg2_comments3` varchar(255) DEFAULT NULL,
  `pg2_date4` date DEFAULT NULL,
  `pg2_gest4` varchar(6) DEFAULT NULL,
  `pg2_ht4` varchar(6) DEFAULT NULL,
  `pg2_wt4` varchar(6) DEFAULT NULL,
  `pg2_presn4` varchar(6) DEFAULT NULL,
  `pg2_FHR4` varchar(6) DEFAULT NULL,
  `pg2_urinePr4` char(3) DEFAULT NULL,
  `pg2_urineGl4` char(3) DEFAULT NULL,
  `pg2_BP4` varchar(8) DEFAULT NULL,
  `pg2_comments4` varchar(255) DEFAULT NULL,
  `pg2_date5` date DEFAULT NULL,
  `pg2_gest5` varchar(6) DEFAULT NULL,
  `pg2_ht5` varchar(6) DEFAULT NULL,
  `pg2_wt5` varchar(6) DEFAULT NULL,
  `pg2_presn5` varchar(6) DEFAULT NULL,
  `pg2_FHR5` varchar(6) DEFAULT NULL,
  `pg2_urinePr5` char(3) DEFAULT NULL,
  `pg2_urineGl5` char(3) DEFAULT NULL,
  `pg2_BP5` varchar(8) DEFAULT NULL,
  `pg2_comments5` varchar(255) DEFAULT NULL,
  `pg2_date6` date DEFAULT NULL,
  `pg2_gest6` varchar(6) DEFAULT NULL,
  `pg2_ht6` varchar(6) DEFAULT NULL,
  `pg2_wt6` varchar(6) DEFAULT NULL,
  `pg2_presn6` varchar(6) DEFAULT NULL,
  `pg2_FHR6` varchar(6) DEFAULT NULL,
  `pg2_urinePr6` char(3) DEFAULT NULL,
  `pg2_urineGl6` char(3) DEFAULT NULL,
  `pg2_BP6` varchar(8) DEFAULT NULL,
  `pg2_comments6` varchar(255) DEFAULT NULL,
  `pg2_date7` date DEFAULT NULL,
  `pg2_gest7` varchar(6) DEFAULT NULL,
  `pg2_ht7` varchar(6) DEFAULT NULL,
  `pg2_wt7` varchar(6) DEFAULT NULL,
  `pg2_presn7` varchar(6) DEFAULT NULL,
  `pg2_FHR7` varchar(6) DEFAULT NULL,
  `pg2_urinePr7` char(3) DEFAULT NULL,
  `pg2_urineGl7` char(3) DEFAULT NULL,
  `pg2_BP7` varchar(8) DEFAULT NULL,
  `pg2_comments7` varchar(255) DEFAULT NULL,
  `pg2_date8` date DEFAULT NULL,
  `pg2_gest8` varchar(6) DEFAULT NULL,
  `pg2_ht8` varchar(6) DEFAULT NULL,
  `pg2_wt8` varchar(6) DEFAULT NULL,
  `pg2_presn8` varchar(6) DEFAULT NULL,
  `pg2_FHR8` varchar(6) DEFAULT NULL,
  `pg2_urinePr8` char(3) DEFAULT NULL,
  `pg2_urineGl8` char(3) DEFAULT NULL,
  `pg2_BP8` varchar(8) DEFAULT NULL,
  `pg2_comments8` varchar(255) DEFAULT NULL,
  `pg2_date9` date DEFAULT NULL,
  `pg2_gest9` varchar(6) DEFAULT NULL,
  `pg2_ht9` varchar(6) DEFAULT NULL,
  `pg2_wt9` varchar(6) DEFAULT NULL,
  `pg2_presn9` varchar(6) DEFAULT NULL,
  `pg2_FHR9` varchar(6) DEFAULT NULL,
  `pg2_urinePr9` char(3) DEFAULT NULL,
  `pg2_urineGl9` char(3) DEFAULT NULL,
  `pg2_BP9` varchar(8) DEFAULT NULL,
  `pg2_comments9` varchar(255) DEFAULT NULL,
  `pg2_date10` date DEFAULT NULL,
  `pg2_gest10` varchar(6) DEFAULT NULL,
  `pg2_ht10` varchar(6) DEFAULT NULL,
  `pg2_wt10` varchar(6) DEFAULT NULL,
  `pg2_presn10` varchar(6) DEFAULT NULL,
  `pg2_FHR10` varchar(6) DEFAULT NULL,
  `pg2_urinePr10` char(3) DEFAULT NULL,
  `pg2_urineGl10` char(3) DEFAULT NULL,
  `pg2_BP10` varchar(8) DEFAULT NULL,
  `pg2_comments10` varchar(255) DEFAULT NULL,
  `pg2_date11` date DEFAULT NULL,
  `pg2_gest11` varchar(6) DEFAULT NULL,
  `pg2_ht11` varchar(6) DEFAULT NULL,
  `pg2_wt11` varchar(6) DEFAULT NULL,
  `pg2_presn11` varchar(6) DEFAULT NULL,
  `pg2_FHR11` varchar(6) DEFAULT NULL,
  `pg2_urinePr11` char(3) DEFAULT NULL,
  `pg2_urineGl11` char(3) DEFAULT NULL,
  `pg2_BP11` varchar(8) DEFAULT NULL,
  `pg2_comments11` varchar(255) DEFAULT NULL,
  `pg2_date12` date DEFAULT NULL,
  `pg2_gest12` varchar(6) DEFAULT NULL,
  `pg2_ht12` varchar(6) DEFAULT NULL,
  `pg2_wt12` varchar(6) DEFAULT NULL,
  `pg2_presn12` varchar(6) DEFAULT NULL,
  `pg2_FHR12` varchar(6) DEFAULT NULL,
  `pg2_urinePr12` char(3) DEFAULT NULL,
  `pg2_urineGl12` char(3) DEFAULT NULL,
  `pg2_BP12` varchar(8) DEFAULT NULL,
  `pg2_comments12` varchar(255) DEFAULT NULL,
  `pg2_date13` date DEFAULT NULL,
  `pg2_gest13` varchar(6) DEFAULT NULL,
  `pg2_ht13` varchar(6) DEFAULT NULL,
  `pg2_wt13` varchar(6) DEFAULT NULL,
  `pg2_presn13` varchar(6) DEFAULT NULL,
  `pg2_FHR13` varchar(6) DEFAULT NULL,
  `pg2_urinePr13` char(3) DEFAULT NULL,
  `pg2_urineGl13` char(3) DEFAULT NULL,
  `pg2_BP13` varchar(8) DEFAULT NULL,
  `pg2_comments13` varchar(255) DEFAULT NULL,
  `pg2_date14` date DEFAULT NULL,
  `pg2_gest14` varchar(6) DEFAULT NULL,
  `pg2_ht14` varchar(6) DEFAULT NULL,
  `pg2_wt14` varchar(6) DEFAULT NULL,
  `pg2_presn14` varchar(6) DEFAULT NULL,
  `pg2_FHR14` varchar(6) DEFAULT NULL,
  `pg2_urinePr14` char(3) DEFAULT NULL,
  `pg2_urineGl14` char(3) DEFAULT NULL,
  `pg2_BP14` varchar(8) DEFAULT NULL,
  `pg2_comments14` varchar(255) DEFAULT NULL,
  `pg2_date15` date DEFAULT NULL,
  `pg2_gest15` varchar(6) DEFAULT NULL,
  `pg2_ht15` varchar(6) DEFAULT NULL,
  `pg2_wt15` varchar(6) DEFAULT NULL,
  `pg2_presn15` varchar(6) DEFAULT NULL,
  `pg2_FHR15` varchar(6) DEFAULT NULL,
  `pg2_urinePr15` char(3) DEFAULT NULL,
  `pg2_urineGl15` char(3) DEFAULT NULL,
  `pg2_BP15` varchar(8) DEFAULT NULL,
  `pg2_comments15` varchar(255) DEFAULT NULL,
  `pg2_date16` date DEFAULT NULL,
  `pg2_gest16` varchar(6) DEFAULT NULL,
  `pg2_ht16` varchar(6) DEFAULT NULL,
  `pg2_wt16` varchar(6) DEFAULT NULL,
  `pg2_presn16` varchar(6) DEFAULT NULL,
  `pg2_FHR16` varchar(6) DEFAULT NULL,
  `pg2_urinePr16` char(3) DEFAULT NULL,
  `pg2_urineGl16` char(3) DEFAULT NULL,
  `pg2_BP16` varchar(8) DEFAULT NULL,
  `pg2_comments16` varchar(255) DEFAULT NULL,
  `pg2_date17` date DEFAULT NULL,
  `pg2_gest17` varchar(6) DEFAULT NULL,
  `pg2_ht17` varchar(6) DEFAULT NULL,
  `pg2_wt17` varchar(6) DEFAULT NULL,
  `pg2_presn17` varchar(6) DEFAULT NULL,
  `pg2_FHR17` varchar(6) DEFAULT NULL,
  `pg2_urinePr17` char(3) DEFAULT NULL,
  `pg2_urineGl17` char(3) DEFAULT NULL,
  `pg2_BP17` varchar(8) DEFAULT NULL,
  `pg2_comments17` varchar(255) DEFAULT NULL,
  `pg2_date18` date DEFAULT NULL,
  `pg2_gest18` varchar(6) DEFAULT NULL,
  `pg2_ht18` varchar(6) DEFAULT NULL,
  `pg2_wt18` varchar(6) DEFAULT NULL,
  `pg2_presn18` varchar(6) DEFAULT NULL,
  `pg2_FHR18` varchar(6) DEFAULT NULL,
  `pg2_urinePr18` char(3) DEFAULT NULL,
  `pg2_urineGl18` char(3) DEFAULT NULL,
  `pg2_BP18` varchar(8) DEFAULT NULL,
  `pg2_comments18` varchar(255) DEFAULT NULL,
  `pg2_date19` date DEFAULT NULL,
  `pg2_gest19` varchar(6) DEFAULT NULL,
  `pg2_ht19` varchar(6) DEFAULT NULL,
  `pg2_wt19` varchar(6) DEFAULT NULL,
  `pg2_presn19` varchar(6) DEFAULT NULL,
  `pg2_FHR19` varchar(6) DEFAULT NULL,
  `pg2_urinePr19` char(3) DEFAULT NULL,
  `pg2_urineGl19` char(3) DEFAULT NULL,
  `pg2_BP19` varchar(8) DEFAULT NULL,
  `pg2_comments19` varchar(255) DEFAULT NULL,
  `pg2_date20` date DEFAULT NULL,
  `pg2_gest20` varchar(6) DEFAULT NULL,
  `pg2_ht20` varchar(6) DEFAULT NULL,
  `pg2_wt20` varchar(6) DEFAULT NULL,
  `pg2_presn20` varchar(6) DEFAULT NULL,
  `pg2_FHR20` varchar(6) DEFAULT NULL,
  `pg2_urinePr20` char(3) DEFAULT NULL,
  `pg2_urineGl20` char(3) DEFAULT NULL,
  `pg2_BP20` varchar(8) DEFAULT NULL,
  `pg2_comments20` varchar(255) DEFAULT NULL,
  `pg2_date21` date DEFAULT NULL,
  `pg2_gest21` varchar(6) DEFAULT NULL,
  `pg2_ht21` varchar(6) DEFAULT NULL,
  `pg2_wt21` varchar(6) DEFAULT NULL,
  `pg2_presn21` varchar(6) DEFAULT NULL,
  `pg2_FHR21` varchar(6) DEFAULT NULL,
  `pg2_urinePr21` char(3) DEFAULT NULL,
  `pg2_urineGl21` char(3) DEFAULT NULL,
  `pg2_BP21` varchar(8) DEFAULT NULL,
  `pg2_comments21` varchar(255) DEFAULT NULL,
  `pg2_date22` date DEFAULT NULL,
  `pg2_gest22` varchar(6) DEFAULT NULL,
  `pg2_ht22` varchar(6) DEFAULT NULL,
  `pg2_wt22` varchar(6) DEFAULT NULL,
  `pg2_presn22` varchar(6) DEFAULT NULL,
  `pg2_FHR22` varchar(6) DEFAULT NULL,
  `pg2_urinePr22` char(3) DEFAULT NULL,
  `pg2_urineGl22` char(3) DEFAULT NULL,
  `pg2_BP22` varchar(8) DEFAULT NULL,
  `pg2_comments22` varchar(255) DEFAULT NULL,
  `pg2_date23` date DEFAULT NULL,
  `pg2_gest23` varchar(6) DEFAULT NULL,
  `pg2_ht23` varchar(6) DEFAULT NULL,
  `pg2_wt23` varchar(6) DEFAULT NULL,
  `pg2_presn23` varchar(6) DEFAULT NULL,
  `pg2_FHR23` varchar(6) DEFAULT NULL,
  `pg2_urinePr23` char(3) DEFAULT NULL,
  `pg2_urineGl23` char(3) DEFAULT NULL,
  `pg2_BP23` varchar(8) DEFAULT NULL,
  `pg2_comments23` varchar(255) DEFAULT NULL,
  `pg2_date24` date DEFAULT NULL,
  `pg2_gest24` varchar(6) DEFAULT NULL,
  `pg2_ht24` varchar(6) DEFAULT NULL,
  `pg2_wt24` varchar(6) DEFAULT NULL,
  `pg2_presn24` varchar(6) DEFAULT NULL,
  `pg2_FHR24` varchar(6) DEFAULT NULL,
  `pg2_urinePr24` char(3) DEFAULT NULL,
  `pg2_urineGl24` char(3) DEFAULT NULL,
  `pg2_BP24` varchar(8) DEFAULT NULL,
  `pg2_comments24` varchar(255) DEFAULT NULL,
  `pg2_date25` date DEFAULT NULL,
  `pg2_gest25` varchar(6) DEFAULT NULL,
  `pg2_ht25` varchar(6) DEFAULT NULL,
  `pg2_wt25` varchar(6) DEFAULT NULL,
  `pg2_presn25` varchar(6) DEFAULT NULL,
  `pg2_FHR25` varchar(6) DEFAULT NULL,
  `pg2_urinePr25` char(3) DEFAULT NULL,
  `pg2_urineGl25` char(3) DEFAULT NULL,
  `pg2_BP25` varchar(8) DEFAULT NULL,
  `pg2_comments25` varchar(255) DEFAULT NULL,
  `pg2_date26` date DEFAULT NULL,
  `pg2_gest26` varchar(6) DEFAULT NULL,
  `pg2_ht26` varchar(6) DEFAULT NULL,
  `pg2_wt26` varchar(6) DEFAULT NULL,
  `pg2_presn26` varchar(6) DEFAULT NULL,
  `pg2_FHR26` varchar(6) DEFAULT NULL,
  `pg2_urinePr26` char(3) DEFAULT NULL,
  `pg2_urineGl26` char(3) DEFAULT NULL,
  `pg2_BP26` varchar(8) DEFAULT NULL,
  `pg2_comments26` varchar(255) DEFAULT NULL,
  `pg2_date27` date DEFAULT NULL,
  `pg2_gest27` varchar(6) DEFAULT NULL,
  `pg2_ht27` varchar(6) DEFAULT NULL,
  `pg2_wt27` varchar(6) DEFAULT NULL,
  `pg2_presn27` varchar(6) DEFAULT NULL,
  `pg2_FHR27` varchar(6) DEFAULT NULL,
  `pg2_urinePr27` char(3) DEFAULT NULL,
  `pg2_urineGl27` char(3) DEFAULT NULL,
  `pg2_BP27` varchar(8) DEFAULT NULL,
  `pg2_comments27` varchar(255) DEFAULT NULL,
  `pg2_date28` date DEFAULT NULL,
  `pg2_gest28` varchar(6) DEFAULT NULL,
  `pg2_ht28` varchar(6) DEFAULT NULL,
  `pg2_wt28` varchar(6) DEFAULT NULL,
  `pg2_presn28` varchar(6) DEFAULT NULL,
  `pg2_FHR28` varchar(6) DEFAULT NULL,
  `pg2_urinePr28` char(3) DEFAULT NULL,
  `pg2_urineGl28` char(3) DEFAULT NULL,
  `pg2_BP28` varchar(8) DEFAULT NULL,
  `pg2_comments28` varchar(255) DEFAULT NULL,
  `pg2_date29` date DEFAULT NULL,
  `pg2_gest29` varchar(6) DEFAULT NULL,
  `pg2_ht29` varchar(6) DEFAULT NULL,
  `pg2_wt29` varchar(6) DEFAULT NULL,
  `pg2_presn29` varchar(6) DEFAULT NULL,
  `pg2_FHR29` varchar(6) DEFAULT NULL,
  `pg2_urinePr29` char(3) DEFAULT NULL,
  `pg2_urineGl29` char(3) DEFAULT NULL,
  `pg2_BP29` varchar(8) DEFAULT NULL,
  `pg2_comments29` varchar(255) DEFAULT NULL,
  `pg2_date30` date DEFAULT NULL,
  `pg2_gest30` varchar(6) DEFAULT NULL,
  `pg2_ht30` varchar(6) DEFAULT NULL,
  `pg2_wt30` varchar(6) DEFAULT NULL,
  `pg2_presn30` varchar(6) DEFAULT NULL,
  `pg2_FHR30` varchar(6) DEFAULT NULL,
  `pg2_urinePr30` char(3) DEFAULT NULL,
  `pg2_urineGl30` char(3) DEFAULT NULL,
  `pg2_BP30` varchar(8) DEFAULT NULL,
  `pg2_comments30` varchar(255) DEFAULT NULL,
  `pg2_date31` date DEFAULT NULL,
  `pg2_gest31` varchar(6) DEFAULT NULL,
  `pg2_ht31` varchar(6) DEFAULT NULL,
  `pg2_wt31` varchar(6) DEFAULT NULL,
  `pg2_presn31` varchar(6) DEFAULT NULL,
  `pg2_FHR31` varchar(6) DEFAULT NULL,
  `pg2_urinePr31` char(3) DEFAULT NULL,
  `pg2_urineGl31` char(3) DEFAULT NULL,
  `pg2_BP31` varchar(8) DEFAULT NULL,
  `pg2_comments31` varchar(255) DEFAULT NULL,
  `pg2_date32` date DEFAULT NULL,
  `pg2_gest32` varchar(6) DEFAULT NULL,
  `pg2_ht32` varchar(6) DEFAULT NULL,
  `pg2_wt32` varchar(6) DEFAULT NULL,
  `pg2_presn32` varchar(6) DEFAULT NULL,
  `pg2_FHR32` varchar(6) DEFAULT NULL,
  `pg2_urinePr32` char(3) DEFAULT NULL,
  `pg2_urineGl32` char(3) DEFAULT NULL,
  `pg2_BP32` varchar(8) DEFAULT NULL,
  `pg2_comments32` varchar(255) DEFAULT NULL,
  `pg2_date33` date DEFAULT NULL,
  `pg2_gest33` varchar(6) DEFAULT NULL,
  `pg2_ht33` varchar(6) DEFAULT NULL,
  `pg2_wt33` varchar(6) DEFAULT NULL,
  `pg2_presn33` varchar(6) DEFAULT NULL,
  `pg2_FHR33` varchar(6) DEFAULT NULL,
  `pg2_urinePr33` char(3) DEFAULT NULL,
  `pg2_urineGl33` char(3) DEFAULT NULL,
  `pg2_BP33` varchar(8) DEFAULT NULL,
  `pg2_comments33` varchar(255) DEFAULT NULL,
  `pg2_date34` date DEFAULT NULL,
  `pg2_gest34` varchar(6) DEFAULT NULL,
  `pg2_ht34` varchar(6) DEFAULT NULL,
  `pg2_wt34` varchar(6) DEFAULT NULL,
  `pg2_presn34` varchar(6) DEFAULT NULL,
  `pg2_FHR34` varchar(6) DEFAULT NULL,
  `pg2_urinePr34` char(3) DEFAULT NULL,
  `pg2_urineGl34` char(3) DEFAULT NULL,
  `pg2_BP34` varchar(8) DEFAULT NULL,
  `pg2_comments34` varchar(255) DEFAULT NULL,
  `pg2_date35` date DEFAULT NULL,
  `pg2_gest35` varchar(6) DEFAULT NULL,
  `pg2_ht35` varchar(6) DEFAULT NULL,
  `pg2_wt35` varchar(6) DEFAULT NULL,
  `pg2_presn35` varchar(6) DEFAULT NULL,
  `pg2_FHR35` varchar(6) DEFAULT NULL,
  `pg2_urinePr35` char(3) DEFAULT NULL,
  `pg2_urineGl35` char(3) DEFAULT NULL,
  `pg2_BP35` varchar(8) DEFAULT NULL,
  `pg2_comments35` varchar(255) DEFAULT NULL,
  `pg2_date36` date DEFAULT NULL,
  `pg2_gest36` varchar(6) DEFAULT NULL,
  `pg2_ht36` varchar(6) DEFAULT NULL,
  `pg2_wt36` varchar(6) DEFAULT NULL,
  `pg2_presn36` varchar(6) DEFAULT NULL,
  `pg2_FHR36` varchar(6) DEFAULT NULL,
  `pg2_urinePr36` char(3) DEFAULT NULL,
  `pg2_urineGl36` char(3) DEFAULT NULL,
  `pg2_BP36` varchar(8) DEFAULT NULL,
  `pg2_comments36` varchar(255) DEFAULT NULL,
  `pg2_date37` date DEFAULT NULL,
  `pg2_gest37` varchar(6) DEFAULT NULL,
  `pg2_ht37` varchar(6) DEFAULT NULL,
  `pg2_wt37` varchar(6) DEFAULT NULL,
  `pg2_presn37` varchar(6) DEFAULT NULL,
  `pg2_FHR37` varchar(6) DEFAULT NULL,
  `pg2_urinePr37` char(3) DEFAULT NULL,
  `pg2_urineGl37` char(3) DEFAULT NULL,
  `pg2_BP37` varchar(8) DEFAULT NULL,
  `pg2_comments37` varchar(255) DEFAULT NULL,
  `pg2_date38` date DEFAULT NULL,
  `pg2_gest38` varchar(6) DEFAULT NULL,
  `pg2_ht38` varchar(6) DEFAULT NULL,
  `pg2_wt38` varchar(6) DEFAULT NULL,
  `pg2_presn38` varchar(6) DEFAULT NULL,
  `pg2_FHR38` varchar(6) DEFAULT NULL,
  `pg2_urinePr38` char(3) DEFAULT NULL,
  `pg2_urineGl38` char(3) DEFAULT NULL,
  `pg2_BP38` varchar(8) DEFAULT NULL,
  `pg2_comments38` varchar(255) DEFAULT NULL,
  `pg2_date39` date DEFAULT NULL,
  `pg2_gest39` varchar(6) DEFAULT NULL,
  `pg2_ht39` varchar(6) DEFAULT NULL,
  `pg2_wt39` varchar(6) DEFAULT NULL,
  `pg2_presn39` varchar(6) DEFAULT NULL,
  `pg2_FHR39` varchar(6) DEFAULT NULL,
  `pg2_urinePr39` char(3) DEFAULT NULL,
  `pg2_urineGl39` char(3) DEFAULT NULL,
  `pg2_BP39` varchar(8) DEFAULT NULL,
  `pg2_comments39` varchar(255) DEFAULT NULL,
  `pg2_date40` date DEFAULT NULL,
  `pg2_gest40` varchar(6) DEFAULT NULL,
  `pg2_ht40` varchar(6) DEFAULT NULL,
  `pg2_wt40` varchar(6) DEFAULT NULL,
  `pg2_presn40` varchar(6) DEFAULT NULL,
  `pg2_FHR40` varchar(6) DEFAULT NULL,
  `pg2_urinePr40` char(3) DEFAULT NULL,
  `pg2_urineGl40` char(3) DEFAULT NULL,
  `pg2_BP40` varchar(8) DEFAULT NULL,
  `pg2_comments40` varchar(255) DEFAULT NULL,
  `pg2_date41` date DEFAULT NULL,
  `pg2_gest41` varchar(6) DEFAULT NULL,
  `pg2_ht41` varchar(6) DEFAULT NULL,
  `pg2_wt41` varchar(6) DEFAULT NULL,
  `pg2_presn41` varchar(6) DEFAULT NULL,
  `pg2_FHR41` varchar(6) DEFAULT NULL,
  `pg2_urinePr41` char(3) DEFAULT NULL,
  `pg2_urineGl41` char(3) DEFAULT NULL,
  `pg2_BP41` varchar(8) DEFAULT NULL,
  `pg2_comments41` varchar(255) DEFAULT NULL,
  `pg2_date42` date DEFAULT NULL,
  `pg2_gest42` varchar(6) DEFAULT NULL,
  `pg2_ht42` varchar(6) DEFAULT NULL,
  `pg2_wt42` varchar(6) DEFAULT NULL,
  `pg2_presn42` varchar(6) DEFAULT NULL,
  `pg2_FHR42` varchar(6) DEFAULT NULL,
  `pg2_urinePr42` char(3) DEFAULT NULL,
  `pg2_urineGl42` char(3) DEFAULT NULL,
  `pg2_BP42` varchar(8) DEFAULT NULL,
  `pg2_comments42` varchar(255) DEFAULT NULL,
  `pg2_date43` date DEFAULT NULL,
  `pg2_gest43` varchar(6) DEFAULT NULL,
  `pg2_ht43` varchar(6) DEFAULT NULL,
  `pg2_wt43` varchar(6) DEFAULT NULL,
  `pg2_presn43` varchar(6) DEFAULT NULL,
  `pg2_FHR43` varchar(6) DEFAULT NULL,
  `pg2_urinePr43` char(3) DEFAULT NULL,
  `pg2_urineGl43` char(3) DEFAULT NULL,
  `pg2_BP43` varchar(8) DEFAULT NULL,
  `pg2_comments43` varchar(255) DEFAULT NULL,
  `pg2_date44` date DEFAULT NULL,
  `pg2_gest44` varchar(6) DEFAULT NULL,
  `pg2_ht44` varchar(6) DEFAULT NULL,
  `pg2_wt44` varchar(6) DEFAULT NULL,
  `pg2_presn44` varchar(6) DEFAULT NULL,
  `pg2_FHR44` varchar(6) DEFAULT NULL,
  `pg2_urinePr44` char(3) DEFAULT NULL,
  `pg2_urineGl44` char(3) DEFAULT NULL,
  `pg2_BP44` varchar(8) DEFAULT NULL,
  `pg2_comments44` varchar(255) DEFAULT NULL,
  `pg2_date45` date DEFAULT NULL,
  `pg2_gest45` varchar(6) DEFAULT NULL,
  `pg2_ht45` varchar(6) DEFAULT NULL,
  `pg2_wt45` varchar(6) DEFAULT NULL,
  `pg2_presn45` varchar(6) DEFAULT NULL,
  `pg2_FHR45` varchar(6) DEFAULT NULL,
  `pg2_urinePr45` char(3) DEFAULT NULL,
  `pg2_urineGl45` char(3) DEFAULT NULL,
  `pg2_BP45` varchar(8) DEFAULT NULL,
  `pg2_comments45` varchar(255) DEFAULT NULL,
  `pg2_date46` date DEFAULT NULL,
  `pg2_gest46` varchar(6) DEFAULT NULL,
  `pg2_ht46` varchar(6) DEFAULT NULL,
  `pg2_wt46` varchar(6) DEFAULT NULL,
  `pg2_presn46` varchar(6) DEFAULT NULL,
  `pg2_FHR46` varchar(6) DEFAULT NULL,
  `pg2_urinePr46` char(3) DEFAULT NULL,
  `pg2_urineGl46` char(3) DEFAULT NULL,
  `pg2_BP46` varchar(8) DEFAULT NULL,
  `pg2_comments46` varchar(255) DEFAULT NULL,
  `pg2_date47` date DEFAULT NULL,
  `pg2_gest47` varchar(6) DEFAULT NULL,
  `pg2_ht47` varchar(6) DEFAULT NULL,
  `pg2_wt47` varchar(6) DEFAULT NULL,
  `pg2_presn47` varchar(6) DEFAULT NULL,
  `pg2_FHR47` varchar(6) DEFAULT NULL,
  `pg2_urinePr47` char(3) DEFAULT NULL,
  `pg2_urineGl47` char(3) DEFAULT NULL,
  `pg2_BP47` varchar(8) DEFAULT NULL,
  `pg2_comments47` varchar(255) DEFAULT NULL,
  `pg2_date48` date DEFAULT NULL,
  `pg2_gest48` varchar(6) DEFAULT NULL,
  `pg2_ht48` varchar(6) DEFAULT NULL,
  `pg2_wt48` varchar(6) DEFAULT NULL,
  `pg2_presn48` varchar(6) DEFAULT NULL,
  `pg2_FHR48` varchar(6) DEFAULT NULL,
  `pg2_urinePr48` char(3) DEFAULT NULL,
  `pg2_urineGl48` char(3) DEFAULT NULL,
  `pg2_BP48` varchar(8) DEFAULT NULL,
  `pg2_comments48` varchar(255) DEFAULT NULL,
  `pg2_date49` date DEFAULT NULL,
  `pg2_gest49` varchar(6) DEFAULT NULL,
  `pg2_ht49` varchar(6) DEFAULT NULL,
  `pg2_wt49` varchar(6) DEFAULT NULL,
  `pg2_presn49` varchar(6) DEFAULT NULL,
  `pg2_FHR49` varchar(6) DEFAULT NULL,
  `pg2_urinePr49` char(3) DEFAULT NULL,
  `pg2_urineGl49` char(3) DEFAULT NULL,
  `pg2_BP49` varchar(8) DEFAULT NULL,
  `pg2_comments49` varchar(255) DEFAULT NULL,
  `pg2_date50` date DEFAULT NULL,
  `pg2_gest50` varchar(6) DEFAULT NULL,
  `pg2_ht50` varchar(6) DEFAULT NULL,
  `pg2_wt50` varchar(6) DEFAULT NULL,
  `pg2_presn50` varchar(6) DEFAULT NULL,
  `pg2_FHR50` varchar(6) DEFAULT NULL,
  `pg2_urinePr50` char(3) DEFAULT NULL,
  `pg2_urineGl50` char(3) DEFAULT NULL,
  `pg2_BP50` varchar(8) DEFAULT NULL,
  `pg2_comments50` varchar(255) DEFAULT NULL,
  `pg2_date51` date DEFAULT NULL,
  `pg2_gest51` varchar(6) DEFAULT NULL,
  `pg2_ht51` varchar(6) DEFAULT NULL,
  `pg2_wt51` varchar(6) DEFAULT NULL,
  `pg2_presn51` varchar(6) DEFAULT NULL,
  `pg2_FHR51` varchar(6) DEFAULT NULL,
  `pg2_urinePr51` char(3) DEFAULT NULL,
  `pg2_urineGl51` char(3) DEFAULT NULL,
  `pg2_BP51` varchar(8) DEFAULT NULL,
  `pg2_comments51` varchar(255) DEFAULT NULL,
  `pg2_date52` date DEFAULT NULL,
  `pg2_gest52` varchar(6) DEFAULT NULL,
  `pg2_ht52` varchar(6) DEFAULT NULL,
  `pg2_wt52` varchar(6) DEFAULT NULL,
  `pg2_presn52` varchar(6) DEFAULT NULL,
  `pg2_FHR52` varchar(6) DEFAULT NULL,
  `pg2_urinePr52` char(3) DEFAULT NULL,
  `pg2_urineGl52` char(3) DEFAULT NULL,
  `pg2_BP52` varchar(8) DEFAULT NULL,
  `pg2_comments52` varchar(255) DEFAULT NULL,
  `pg2_date53` date DEFAULT NULL,
  `pg2_gest53` varchar(6) DEFAULT NULL,
  `pg2_ht53` varchar(6) DEFAULT NULL,
  `pg2_wt53` varchar(6) DEFAULT NULL,
  `pg2_presn53` varchar(6) DEFAULT NULL,
  `pg2_FHR53` varchar(6) DEFAULT NULL,
  `pg2_urinePr53` char(3) DEFAULT NULL,
  `pg2_urineGl53` char(3) DEFAULT NULL,
  `pg2_BP53` varchar(8) DEFAULT NULL,
  `pg2_comments53` varchar(255) DEFAULT NULL,
  `pg2_date54` date DEFAULT NULL,
  `pg2_gest54` varchar(6) DEFAULT NULL,
  `pg2_ht54` varchar(6) DEFAULT NULL,
  `pg2_wt54` varchar(6) DEFAULT NULL,
  `pg2_presn54` varchar(6) DEFAULT NULL,
  `pg2_FHR54` varchar(6) DEFAULT NULL,
  `pg2_urinePr54` char(3) DEFAULT NULL,
  `pg2_urineGl54` char(3) DEFAULT NULL,
  `pg2_BP54` varchar(8) DEFAULT NULL,
  `pg2_comments54` varchar(255) DEFAULT NULL,
  `pg2_date55` date DEFAULT NULL,
  `pg2_gest55` varchar(6) DEFAULT NULL,
  `pg2_ht55` varchar(6) DEFAULT NULL,
  `pg2_wt55` varchar(6) DEFAULT NULL,
  `pg2_presn55` varchar(6) DEFAULT NULL,
  `pg2_FHR55` varchar(6) DEFAULT NULL,
  `pg2_urinePr55` char(3) DEFAULT NULL,
  `pg2_urineGl55` char(3) DEFAULT NULL,
  `pg2_BP55` varchar(8) DEFAULT NULL,
  `pg2_comments55` varchar(255) DEFAULT NULL,
  `pg2_date56` date DEFAULT NULL,
  `pg2_gest56` varchar(6) DEFAULT NULL,
  `pg2_ht56` varchar(6) DEFAULT NULL,
  `pg2_wt56` varchar(6) DEFAULT NULL,
  `pg2_presn56` varchar(6) DEFAULT NULL,
  `pg2_FHR56` varchar(6) DEFAULT NULL,
  `pg2_urinePr56` char(3) DEFAULT NULL,
  `pg2_urineGl56` char(3) DEFAULT NULL,
  `pg2_BP56` varchar(8) DEFAULT NULL,
  `pg2_comments56` varchar(255) DEFAULT NULL,
  `pg2_date57` date DEFAULT NULL,
  `pg2_gest57` varchar(6) DEFAULT NULL,
  `pg2_ht57` varchar(6) DEFAULT NULL,
  `pg2_wt57` varchar(6) DEFAULT NULL,
  `pg2_presn57` varchar(6) DEFAULT NULL,
  `pg2_FHR57` varchar(6) DEFAULT NULL,
  `pg2_urinePr57` char(3) DEFAULT NULL,
  `pg2_urineGl57` char(3) DEFAULT NULL,
  `pg2_BP57` varchar(8) DEFAULT NULL,
  `pg2_comments57` varchar(255) DEFAULT NULL,
  `pg2_date58` date DEFAULT NULL,
  `pg2_gest58` varchar(6) DEFAULT NULL,
  `pg2_ht58` varchar(6) DEFAULT NULL,
  `pg2_wt58` varchar(6) DEFAULT NULL,
  `pg2_presn58` varchar(6) DEFAULT NULL,
  `pg2_FHR58` varchar(6) DEFAULT NULL,
  `pg2_urinePr58` char(3) DEFAULT NULL,
  `pg2_urineGl58` char(3) DEFAULT NULL,
  `pg2_BP58` varchar(8) DEFAULT NULL,
  `pg2_comments58` varchar(255) DEFAULT NULL,
  `pg2_date59` date DEFAULT NULL,
  `pg2_gest59` varchar(6) DEFAULT NULL,
  `pg2_ht59` varchar(6) DEFAULT NULL,
  `pg2_wt59` varchar(6) DEFAULT NULL,
  `pg2_presn59` varchar(6) DEFAULT NULL,
  `pg2_FHR59` varchar(6) DEFAULT NULL,
  `pg2_urinePr59` char(3) DEFAULT NULL,
  `pg2_urineGl59` char(3) DEFAULT NULL,
  `pg2_BP59` varchar(8) DEFAULT NULL,
  `pg2_comments59` varchar(255) DEFAULT NULL,
  `pg2_date60` date DEFAULT NULL,
  `pg2_gest60` varchar(6) DEFAULT NULL,
  `pg2_ht60` varchar(6) DEFAULT NULL,
  `pg2_wt60` varchar(6) DEFAULT NULL,
  `pg2_presn60` varchar(6) DEFAULT NULL,
  `pg2_FHR60` varchar(6) DEFAULT NULL,
  `pg2_urinePr60` char(3) DEFAULT NULL,
  `pg2_urineGl60` char(3) DEFAULT NULL,
  `pg2_BP60` varchar(8) DEFAULT NULL,
  `pg2_comments60` varchar(255) DEFAULT NULL,
  `pg2_date61` date DEFAULT NULL,
  `pg2_gest61` varchar(6) DEFAULT NULL,
  `pg2_ht61` varchar(6) DEFAULT NULL,
  `pg2_wt61` varchar(6) DEFAULT NULL,
  `pg2_presn61` varchar(6) DEFAULT NULL,
  `pg2_FHR61` varchar(6) DEFAULT NULL,
  `pg2_urinePr61` char(3) DEFAULT NULL,
  `pg2_urineGl61` char(3) DEFAULT NULL,
  `pg2_BP61` varchar(8) DEFAULT NULL,
  `pg2_comments61` varchar(255) DEFAULT NULL,
  `pg2_date62` date DEFAULT NULL,
  `pg2_gest62` varchar(6) DEFAULT NULL,
  `pg2_ht62` varchar(6) DEFAULT NULL,
  `pg2_wt62` varchar(6) DEFAULT NULL,
  `pg2_presn62` varchar(6) DEFAULT NULL,
  `pg2_FHR62` varchar(6) DEFAULT NULL,
  `pg2_urinePr62` char(3) DEFAULT NULL,
  `pg2_urineGl62` char(3) DEFAULT NULL,
  `pg2_BP62` varchar(8) DEFAULT NULL,
  `pg2_comments62` varchar(255) DEFAULT NULL,
  `pg2_date63` date DEFAULT NULL,
  `pg2_gest63` varchar(6) DEFAULT NULL,
  `pg2_ht63` varchar(6) DEFAULT NULL,
  `pg2_wt63` varchar(6) DEFAULT NULL,
  `pg2_presn63` varchar(6) DEFAULT NULL,
  `pg2_FHR63` varchar(6) DEFAULT NULL,
  `pg2_urinePr63` char(3) DEFAULT NULL,
  `pg2_urineGl63` char(3) DEFAULT NULL,
  `pg2_BP63` varchar(8) DEFAULT NULL,
  `pg2_comments63` varchar(255) DEFAULT NULL,
  `pg2_date64` date DEFAULT NULL,
  `pg2_gest64` varchar(6) DEFAULT NULL,
  `pg2_ht64` varchar(6) DEFAULT NULL,
  `pg2_wt64` varchar(6) DEFAULT NULL,
  `pg2_presn64` varchar(6) DEFAULT NULL,
  `pg2_FHR64` varchar(6) DEFAULT NULL,
  `pg2_urinePr64` char(3) DEFAULT NULL,
  `pg2_urineGl64` char(3) DEFAULT NULL,
  `pg2_BP64` varchar(8) DEFAULT NULL,
  `pg2_comments64` varchar(255) DEFAULT NULL,
  `pg2_date65` date DEFAULT NULL,
  `pg2_gest65` varchar(6) DEFAULT NULL,
  `pg2_ht65` varchar(6) DEFAULT NULL,
  `pg2_wt65` varchar(6) DEFAULT NULL,
  `pg2_presn65` varchar(6) DEFAULT NULL,
  `pg2_FHR65` varchar(6) DEFAULT NULL,
  `pg2_urinePr65` char(3) DEFAULT NULL,
  `pg2_urineGl65` char(3) DEFAULT NULL,
  `pg2_BP65` varchar(8) DEFAULT NULL,
  `pg2_comments65` varchar(255) DEFAULT NULL,
  `pg2_date66` date DEFAULT NULL,
  `pg2_gest66` varchar(6) DEFAULT NULL,
  `pg2_ht66` varchar(6) DEFAULT NULL,
  `pg2_wt66` varchar(6) DEFAULT NULL,
  `pg2_presn66` varchar(6) DEFAULT NULL,
  `pg2_FHR66` varchar(6) DEFAULT NULL,
  `pg2_urinePr66` char(3) DEFAULT NULL,
  `pg2_urineGl66` char(3) DEFAULT NULL,
  `pg2_BP66` varchar(8) DEFAULT NULL,
  `pg2_comments66` varchar(255) DEFAULT NULL,
  `pg2_date67` date DEFAULT NULL,
  `pg2_gest67` varchar(6) DEFAULT NULL,
  `pg2_ht67` varchar(6) DEFAULT NULL,
  `pg2_wt67` varchar(6) DEFAULT NULL,
  `pg2_presn67` varchar(6) DEFAULT NULL,
  `pg2_FHR67` varchar(6) DEFAULT NULL,
  `pg2_urinePr67` char(3) DEFAULT NULL,
  `pg2_urineGl67` char(3) DEFAULT NULL,
  `pg2_BP67` varchar(8) DEFAULT NULL,
  `pg2_comments67` varchar(255) DEFAULT NULL,
  `pg2_date68` date DEFAULT NULL,
  `pg2_gest68` varchar(6) DEFAULT NULL,
  `pg2_ht68` varchar(6) DEFAULT NULL,
  `pg2_wt68` varchar(6) DEFAULT NULL,
  `pg2_presn68` varchar(6) DEFAULT NULL,
  `pg2_FHR68` varchar(6) DEFAULT NULL,
  `pg2_urinePr68` char(3) DEFAULT NULL,
  `pg2_urineGl68` char(3) DEFAULT NULL,
  `pg2_BP68` varchar(8) DEFAULT NULL,
  `pg2_comments68` varchar(255) DEFAULT NULL,
  `pg2_date69` date DEFAULT NULL,
  `pg2_gest69` varchar(6) DEFAULT NULL,
  `pg2_ht69` varchar(6) DEFAULT NULL,
  `pg2_wt69` varchar(6) DEFAULT NULL,
  `pg2_presn69` varchar(6) DEFAULT NULL,
  `pg2_FHR69` varchar(6) DEFAULT NULL,
  `pg2_urinePr69` char(3) DEFAULT NULL,
  `pg2_urineGl69` char(3) DEFAULT NULL,
  `pg2_BP69` varchar(8) DEFAULT NULL,
  `pg2_comments69` varchar(255) DEFAULT NULL,
  `pg2_date70` date DEFAULT NULL,
  `pg2_gest70` varchar(6) DEFAULT NULL,
  `pg2_ht70` varchar(6) DEFAULT NULL,
  `pg2_wt70` varchar(6) DEFAULT NULL,
  `pg2_presn70` varchar(6) DEFAULT NULL,
  `pg2_FHR70` varchar(6) DEFAULT NULL,
  `pg2_urinePr70` char(3) DEFAULT NULL,
  `pg2_urineGl70` char(3) DEFAULT NULL,
  `pg2_BP70` varchar(8) DEFAULT NULL,
  `pg2_comments70` varchar(255) DEFAULT NULL,
  `ar2_uDate1` date DEFAULT NULL,
  `ar2_uGA1` varchar(10) DEFAULT NULL,
  `ar2_uResults1` varchar(50) DEFAULT NULL,
  `ar2_uDate2` date DEFAULT NULL,
  `ar2_uGA2` varchar(10) DEFAULT NULL,
  `ar2_uResults2` varchar(50) DEFAULT NULL,
  `ar2_uDate3` date DEFAULT NULL,
  `ar2_uGA3` varchar(10) DEFAULT NULL,
  `ar2_uResults3` varchar(50) DEFAULT NULL,
  `ar2_uDate4` date DEFAULT NULL,
  `ar2_uGA4` varchar(10) DEFAULT NULL,
  `ar2_uResults4` varchar(50) DEFAULT NULL,
  `ar2_uDate5` date DEFAULT NULL,
  `ar2_uGA5` varchar(10) DEFAULT NULL,
  `ar2_uResults5` varchar(50) DEFAULT NULL,
  `ar2_uDate6` date DEFAULT NULL,
  `ar2_uGA6` varchar(10) DEFAULT NULL,
  `ar2_uResults6` varchar(50) DEFAULT NULL,
  `ar2_uDate7` date DEFAULT NULL,
  `ar2_uGA7` varchar(10) DEFAULT NULL,
  `ar2_uResults7` varchar(50) DEFAULT NULL,
  `ar2_uDate8` date DEFAULT NULL,
  `ar2_uGA8` varchar(10) DEFAULT NULL,
  `ar2_uResults8` varchar(50) DEFAULT NULL,
  `ar2_uDate9` date DEFAULT NULL,
  `ar2_uGA9` varchar(10) DEFAULT NULL,
  `ar2_uResults9` varchar(50) DEFAULT NULL,
  `ar2_uDate10` date DEFAULT NULL,
  `ar2_uGA10` varchar(10) DEFAULT NULL,
  `ar2_uResults10` varchar(50) DEFAULT NULL,
  `ar2_uDate11` date DEFAULT NULL,
  `ar2_uGA11` varchar(10) DEFAULT NULL,
  `ar2_uResults11` varchar(50) DEFAULT NULL,
  `ar2_uDate12` date DEFAULT NULL,
  `ar2_uGA12` varchar(10) DEFAULT NULL,
  `ar2_uResults12` varchar(50) DEFAULT NULL,
  `ar2_hb` varchar(10) DEFAULT NULL,
  `ar2_bloodGroup` varchar(6) DEFAULT NULL,
  `ar2_rh` varchar(6) DEFAULT NULL,
  `ar2_labABS` varchar(10) DEFAULT NULL,
  `ar2_lab1GCT` varchar(10) DEFAULT NULL,
  `ar2_lab2GTT` varchar(14) DEFAULT NULL,
  `ar2_strep` varchar(10) DEFAULT NULL,
  `ar2_labCustom1Label` varchar(40) DEFAULT NULL,
  `ar2_labCustom1Result` varchar(40) DEFAULT NULL,
  `ar2_labCustom2Label` varchar(40) DEFAULT NULL,
  `ar2_labCustom2Result` varchar(40) DEFAULT NULL,
  `ar2_labCustom3Label` varchar(40) DEFAULT NULL,
  `ar2_labCustom3Result` varchar(40) DEFAULT NULL,
  `ar2_labCustom4Label` varchar(40) DEFAULT NULL,
  `ar2_labCustom4Result` varchar(40) DEFAULT NULL,
  `ar2_exercise` tinyint(1) DEFAULT NULL,
  `ar2_workPlan` tinyint(1) DEFAULT NULL,
  `ar2_intercourse` tinyint(1) DEFAULT NULL,
  `ar2_travel` tinyint(1) DEFAULT NULL,
  `ar2_prenatal` tinyint(1) DEFAULT NULL,
  `ar2_birth` tinyint(1) DEFAULT NULL,
  `ar2_onCall` tinyint(1) DEFAULT NULL,
  `ar2_preterm` tinyint(1) DEFAULT NULL,
  `ar2_prom` tinyint(1) DEFAULT NULL,
  `ar2_aph` tinyint(1) DEFAULT NULL,
  `ar2_fetal` tinyint(1) DEFAULT NULL,
  `ar2_admission` tinyint(1) DEFAULT NULL,
  `ar2_pain` tinyint(1) DEFAULT NULL,
  `ar2_labour` tinyint(1) DEFAULT NULL,
  `ar2_breast` tinyint(1) DEFAULT NULL,
  `ar2_circumcision` tinyint(1) DEFAULT NULL,
  `ar2_dischargePlan` tinyint(1) DEFAULT NULL,
  `ar2_car` tinyint(1) DEFAULT NULL,
  `ar2_depression` tinyint(1) DEFAULT NULL,
  `ar2_contraception` tinyint(1) DEFAULT NULL,
  `ar2_postpartumCare` tinyint(1) DEFAULT NULL,
  `pg2_signature` varchar(50) DEFAULT NULL,
  `pg2_formDate` date DEFAULT NULL,
  `pg2_signature2` varchar(50) DEFAULT NULL,
  `pg2_formDate2` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


insert into encounterForm values ('ON AR Enhanced','../form/formonarenhanced.jsp?demographic_no=','formONAREnhanced',0);

drop table vacancy;

CREATE TABLE `Vacancy` (
  `id` int(11) primary key NOT NULL AUTO_INCREMENT,
  `templateId` int(11) NOT NULL,
  `status` varchar(24) NOT NULL,
  `dateClosed` timestamp NULL,
  `reasonClosed` varchar(255),
  `emailNotificationAddressesCsv` varchar(255)
);

drop table if exists form_hsfo2_visit;
CREATE TABLE form_hsfo2_visit (
 	ID int(10) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL,
  provider_no varchar(10) NOT NULL ,
  formCreated date ,
  formEdited timestamp NOT NULL,
  Patient_Id varchar(255) NOT NULL,
  VisitDate_Id date NOT Null,
  Drugcoverage enum('yes', 'no', 'null'),
  SBP int(3) ,
  SBP_goal int(3) ,
  DBP int(3) ,
  DBP_goal int(3) ,
  Height double(4, 1) NOT NULL,
  Height_unit enum('cm', 'inch') NOT NULL,
  Bptru_used enum('yes', 'no', 'null'),
  Weight double(4, 1) ,
  Weight_unit enum('kg', 'lb', 'null'),
  Waist double(4, 1) ,
  Waist_unit enum('cm', 'inch', 'null'),
  TC_HDL double(3, 1) ,
  LDL double(3, 1) ,
  HDL double(2, 1) ,
  Triglycerides double(3,1),
  Nextvisit enum('Under1Mo', '1to2Mo', '3to6Mo', 'Over6Mo', 'null'),
  Bpactionplan bool NOT NULL,
  PressureOff bool NOT NULL,
  PatientProvider bool NOT NULL,
  ABPM bool NOT NULL,
  Home bool NOT NULL,
  CommunityRes bool NOT NULL,
  ProRefer bool NOT NULL,
  HtnDxType enum('PrimaryHtn', 'ElevatedBpReadings', 'null'),
  Dyslipid bool NOT NULL,
  Diabetes bool NOT NULL,
  KidneyDis bool NOT NULL,
  Obesity bool NOT NULL,
  CHD bool NOT NULL,
  Stroke_TIA bool NOT NULL,
  Risk_weight bool,
  Risk_activity bool,
  Risk_diet bool,
  Risk_smoking bool,
  Risk_alcohol bool,
  Risk_stress bool,
  PtView enum('Uninterested', 'Thinking', 'Deciding', 'TakingAction', 'Maintaining', 'Relapsing', 'null'),
  Change_importance int(2),
  Change_confidence int(2),
  exercise_minPerWk int(3),
  smoking_cigsPerDay int(2),
  alcohol_drinksPerWk int(2),
  sel_DashDiet enum('Always', 'Often', 'Sometimes', 'Never', 'null'),
  sel_HighSaltFood enum('Always', 'Often', 'Sometimes', 'Never', 'null'),
  sel_Stressed enum('Always', 'Often', 'Sometimes', 'Never', 'null'),
  LifeGoal enum('Goal_weight', 'Goal_activity', 'Goal_dietDash', 'Goal_dietSalt', 'Goal_smoking', 'Goal_alcohol', 'Goal_stress', 'null'),
  FamHx_Htn bool NOT NULL,
  FamHx_Dyslipid bool NOT NULL,
  FamHx_Diabetes bool NOT NULL,
  FamHx_KidneyDis bool NOT NULL,
  FamHx_Obesity bool NOT NULL,
  FamHx_CHD bool NOT NULL,
  FamHx_Stroke_TIA bool NOT NULL,
  Diuret_rx bool NOT NULL,
  Diuret_SideEffects bool NOT NULL,
  Diuret_RxDecToday enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch', 'null') ,
  Ace_rx bool NOT NULL,
  Ace_SideEffects bool NOT NULL,
  Ace_RxDecToday enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch', 'null') ,
  Arecept_rx bool NOT NULL,
  Arecept_SideEffects bool NOT NULL,
  Arecept_RxDecToday enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch', 'null') ,
  Beta_rx bool NOT NULL,
  Beta_SideEffects bool NOT NULL,
  Beta_RxDecToday enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch', 'null') ,
  Calc_rx bool NOT NULL,
  Calc_SideEffects bool NOT NULL,
  Calc_RxDecToday enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch', 'null') ,
  Anti_rx bool NOT NULL,
  Anti_SideEffects bool NOT NULL,
  Anti_RxDecToday enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch', 'null') ,
  Statin_rx bool NOT NULL,
  Statin_SideEffects bool NOT NULL,
  Statin_RxDecToday enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch', 'null') ,
  Lipid_rx bool NOT NULL,
  Lipid_SideEffects bool NOT NULL,
  Lipid_RxDecToday enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch', 'null') ,
  Hypo_rx bool NOT NULL,
  Hypo_SideEffects bool NOT NULL,
  Hypo_RxDecToday enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch', 'null') ,
  Insul_rx bool NOT NULL,
  Insul_SideEffects bool NOT NULL,
  Insul_RxDecToday enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch', 'null') ,
  Often_miss int(2) ,
  Herbal enum('yes', 'no', 'null') ,
  TC_HDL_LabresultsDate date,
  LDL_LabresultsDate date,
  HDL_LabresultsDate date,
  A1C_LabresultsDate date,
  Locked bool,
  
  depression bool,
  famHx_depression bool,
  assessActivity int(3),
  assessSmoking int(3),
  assessAlcohol int(3),
  nextVisitInMonths int(3),
  nextVisitInWeeks int(3),
  
  monitor bool,
  egfrDate date,
  egfr int(3),
  acr double(5, 1),  
  
  lastBaseLineRecord bool NOT NULL,
  A1C double(3, 3) ,
  fbs double(3, 1) ,
  ASA_rx bool NOT NULL,
  ASA_SideEffects bool NOT NULL,
  ASA_RxDecToday enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch', 'null') ,
 
  PRIMARY KEY  (ID)
) ;

drop table if exists hsfo2_patient;
CREATE TABLE hsfo2_patient (
  ID int(10) NOT NULL auto_increment,
  SiteCode varchar(10) Not null,
  Patient_Id varchar(255) NOT NULL,
  FName text NOT NULL,
  LName text NOT NULL,
  BirthDate date NOT NULL,
  Sex enum('m', 'f') NOT NULL,
  PostalCode varchar(7) NOT NULL,
  
  Ethnic_White bool NOT NULL,
  Ethnic_Black bool NOT NULL,
  Ethnic_EIndian bool NOT NULL,
  Ethnic_Pakistani bool NOT NULL,
  Ethnic_SriLankan bool NOT NULL,
  Ethnic_Bangladeshi bool NOT NULL,
  Ethnic_Chinese bool NOT NULL,
  Ethnic_Japanese bool NOT NULL,
  Ethnic_Korean bool NOT NULL,
  Ethnic_Hispanic bool NOT NULL,
  Ethnic_FirstNation bool NOT NULL,
  Ethnic_Other bool NOT NULL,
  Ethnic_Refused bool NOT NULL,
  Ethnic_Unknown bool NOT NULL,
  PharmacyName text,
  PharmacyLocation text,
  sel_TimeAgoDx enum('AtLeast1YrAgo', 'Under1YrAgo', 'NA', 'null'),
  EmrHCPId text,
  ConsentDate date not null,
  
  statusInHmp enum('enrolled', 'notEnrolled' ) ,
  dateOfHmpStatus date ,
  registrationId varchar(64),
  submitted bool NOT NULL,
  
  
  PRIMARY KEY  (ID)
) ;

drop table if exists hsfo2_system ;
CREATE TABLE hsfo2_system (
  ID int(10) NOT NULL auto_increment,
  LastUploadDate date NOT NULL,
  PRIMARY KEY  (ID)
) ;


-- for hsfo schedule
drop table if exists hsfo_recommit_schedule;
CREATE TABLE `hsfo_recommit_schedule` (   
  `id` int(11) NOT NULL auto_increment,   
  `status` varchar(2) ,       
  `memo` text,                            
  `schedule_time` datetime ,  
  `user_no` varchar(6) ,      
  `check_flag` tinyint(1) ,   
  PRIMARY KEY  (`id`)                     
) ; 
                        
-- hsfo1
-- INSERT INTO `encounterForm` (`form_name`, `form_value`, `form_table`, `hidden`) VALUES
-- ('HMI form','../form/HSFOform.do?demographic_no=','form_hsfo_visit',1);

-- hsfo2

delete from encounterForm where form_name='HSFO2';
INSERT INTO `encounterForm` (`form_name`, `form_value`, `form_table`, `hidden`) VALUES
('HMP Form','../form/HSFOForm2.do?demographic_no=','form_hsfo2_visit',1);

-- update-2012-06-07.sql
CREATE TABLE `PageMonitor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  pageName varchar(100) NOT NULL,
  session varchar(100),
  remoteAddr varchar(20),
  locked tinyint(1),
  updateDate timestamp not null,
  timeout int(10),
  providerNo varchar(10),
  providerName varchar(100),
  PRIMARY KEY (`id`)
);

-- bits and pieces
insert into secObjPrivilege values ('doctor','_pregnancy','o',0,'999998');
alter table Episode add notes text;

-- update-2012-06-22.sql
create table BornTransmissionLog(
	id integer not null auto_increment,
	submitDateTime timestamp not null,
	success tinyint(1) default 0,
	filename varchar(100) not null,
	primary key(id)
);

-- update-2012-06-13.sql update-2012-06-14.sql update-2012-06-19.sql update-2011-06-23.sql  update-2012-07-10.sql
-- have been squashed into the definition of table `formONAREnhanced` defined earlier in this script


-- update-2011-06-26.sql
alter table ProviderPreference add eRxEnabled tinyint(1) not null;
alter table ProviderPreference add eRx_SSO_URL varchar(128);
alter table ProviderPreference add eRxUsername varchar(32);
alter table ProviderPreference add eRxPassword varchar(64);
alter table ProviderPreference add eRxFacility varchar(32);
alter table ProviderPreference add eRxTrainingMode tinyint(1) not null;


-- update-2012-06-29.sql
drop table Vacancy;

CREATE TABLE `vacancy` (
  `id` int(11) primary key NOT NULL AUTO_INCREMENT,
  `templateId` int(11) NOT NULL,
  `status` varchar(24) NOT NULL,
  `dateClosed` timestamp NULL,
  `reasonClosed` varchar(255),
  `wlProgramId` int(11) NOT NULL,
  `dateCreated` date NOT NULL,
  `emailNotificationAddressesCsv` varchar(255)
);


-- update-2011-07-02.sql
insert into access_type (name,type) values('read receptionist notes','access');
insert into access_type (name,type) values('write receptionist notes','access');
insert into access_type (name,type) values('write receptionist issues','access');
insert into access_type (name,type) values('read receptionist issues','access');
insert into access_type (name,type) values('read receptionist ticklers','access');

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='read receptionist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='read receptionist notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='write receptionist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='write receptionist notes'));


-- update-2012-07-06.sql
alter table mygroup add default_billing_form varchar(10);

-- update-2012-07-10.sql
-- squashed into formONAREnhanced table definintion above


-- update-2012-07-11.sql
delete from PageMonitor;
alter table PageMonitor add pageId varchar(255) NOT NULL;

-- update-2012-07-12.sql

INSERT INTO `eform` (`form_name`, `subject`, `form_date`, `form_time`, `status`, `form_html`, `patient_independent`) 
VALUES 
('Rich Text Letter','Rich Text Letter Generator','2012-06-01','10:00:00','0','<html><head>\n<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n\n<title>Rich Text Letter</title>\n<style type=\"text/css\">\n.butn {width: 140px;}\n</style>\n\n<style type=\"text/css\" media=\"print\">\n.DoNotPrint {display: none;}\n\n</style>\n<script language=\"javascript\" type=\"text/javascript\" src=\"${oscar_javascript_path}jquery/jquery-1.4.2.js\"></script>\n\n<script language=\"javascript\">\nvar needToConfirm = false;\n\n//keypress events trigger dirty flag for the iFrame and the subject line\ndocument.onkeyup=setDirtyFlag\n\n\nfunction setDirtyFlag() {\n	needToConfirm = true; \n}\n\nfunction releaseDirtyFlag() {\n	needToConfirm = false; //Call this function if dosent requires an alert.\n	//this could be called when save button is clicked\n}\n\n\nwindow.onbeforeunload = confirmExit;\n\nfunction confirmExit() {\n	if (needToConfirm)\n	return \"You have attempted to leave this page. If you have made any changes without clicking the Submit button, your changes will be lost. Are you sure you want to exit this page?\";\n}\n\n</script>\n\n\n\n</head><body bgcolor=\"FFFFFF\" onload=\"Start();\">\n\n\n<!-- START OF EDITCONTROL CODE --> \n\n<script language=\"javascript\" type=\"text/javascript\" src=\"${oscar_javascript_path}eforms/editControl.js\"></script>\n<script language=\"javascript\" type=\"text/javascript\" src=\"${oscar_javascript_path}eforms/APCache.js\"></script>\n<script language=\"javascript\" type=\"text/javascript\" src=\"${oscar_javascript_path}eforms/imageControl.js\"></script>\n<script language=\"javascript\" type=\"text/javascript\" src=\"${oscar_javascript_path}eforms/faxControl.js\"></script>\n<script language=\"javascript\" type=\"text/javascript\" src=\"${oscar_javascript_path}eforms/signatureControl.jsp\"></script>\n<script language=\"javascript\" type=\"text/javascript\" src=\"${oscar_javascript_path}eforms/printControl.js\"></script>\n\n<script language=\"javascript\">\n	//put any of the optional configuration variables that you want here\n	cfg_width = \'840\'; //editor control width in pixels\n	cfg_height = \'520\'; //editor control height in pixels\n	cfg_editorname = \'edit\'; //the handle for the editor                  \n	cfg_isrc = \'../eform/displayImage.do?imagefile=\'; //location of the button icon files\n	cfg_filesrc = \'../eform/displayImage.do?imagefile=\'; //location of the html files\n	cfg_template = \'blank.rtl\'; //default style and content template\n	cfg_formattemplate = \'<option value=\"\"> loading... </option></select>\';\n	//cfg_layout = \'[all]\';             //adjust the format of the buttons here\n	cfg_layout = \'<table style=\"background-color:ccccff; width:840px\"><tr id=control1><td>[bold][italic][underlined][strike][subscript][superscript]|[left][center][full][right]|[unordered][ordered][rule]|[undo][redo]|[indent][outdent][select-all][clean]|[table]</td></tr><tr id=control2><td>[select-block][select-face][select-size][select-template]|[image][clock][date][spell][help]</td></tr></table>[edit-area]\';\n	insertEditControl(); // Initialise the edit control and sets it at this point in the webpage\n\n	\n	function gup(name, url)\n	{\n		if (url == null) { url = window.location.href; }\n		name = name.replace(/[\\[]/,\"\\\\\\[\").replace(/[\\]]/,\"\\\\\\]\");\n		var regexS = \"[\\\\?&]\"+name+\"=([^&#]*)\";\n		var regex = new RegExp(regexS);\n		var results = regex.exec(url);\n		if (results == null) { return \"\"; }\n		else { return results[1]; }\n	}\n	var demographicNo =\"\";\n\n	jQuery(document).ready(function(){\n		demographicNo = gup(\"demographic_no\");\n		if (demographicNo == \"\") { demographicNo = gup(\"efmdemographic_no\", jQuery(\"form\").attr(\'action\')); }\n		if (typeof signatureControl != \"undefined\") {\n			signatureControl.initialize({\n				sigHTML:\"../signature_pad/tabletSignature.jsp?inWindow=true&saveToDB=true&demographicNo=\",\n				demographicNo:demographicNo,\n				refreshImage: function (e) {\n					var html = \"<img src=\'\"+e.storedImageUrl+\"&r=\"+ Math.floor(Math.random()*1001) +\"\'></img>\";\n					doHtml(html);		\n				},\n				signatureInput: \"#signatureInput\"	\n			});\n		}		\n	});\n		\n	var cache = createCache({\n		defaultCacheResponseHandler: function(type) {\n			if (checkKeyResponse(type)) {\n				doHtml(cache.get(type));\n			}			\n			\n		},\n		cacheResponseErrorHandler: function(xhr, error) {\n			alert(\"Please contact an administrator, an error has occurred.\");			\n			\n		}\n	});	\n	\n	function checkKeyResponse(response) {		\n		if (cache.isEmpty(response)) {\n			alert(\"The requested value has no content.\");\n			return false;\n		}\n		return true;\n	}\n	\n	function printKey (key) {\n		var value = cache.lookup(key); \n		if (value != null && checkKeyResponse(key)) { doHtml(cache.get(key)); } 		  \n	}\n	\n	function submitFaxButton() {\n		document.getElementById(\'faxEForm\').value=true;\n		needToConfirm=false;\n		document.getElementById(\'Letter\').value=editControlContents(\'edit\');\n		setTimeout(\'document.RichTextLetter.submit()\',1000);\n	}\n	\n	cache.addMapping({\n		name: \"_SocialFamilyHistory\",\n		values: [\"social_family_history\"],\n		storeInCacheHandler: function(key,value) {\n			cache.put(this.name, cache.get(\"social_family_history\").replace(/(<br>)+/g,\"<br>\"));\n		},\n		cacheResponseHandler:function () {\n			if (checkKeyResponse(this.name)) {				\n				doHtml(cache.get(this.name));\n			}	\n		}\n	});\n	\n	\n	cache.addMapping({name: \"template\", cacheResponseHandler: populateTemplate});	\n	\n	cache.addMapping({\n		name: \"_ClosingSalutation\", \n		values: [\"provider_name_first_init\"],	\n		storeInCacheHandler: function (key,value) {\n			if (!cache.isEmpty(\"provider_name_first_init\")) {\n				cache.put(this.name, \"<p>Yours Sincerely<p>&nbsp;<p>\" + cache.get(\"provider_name_first_init\") + \", MD\");\n			}\n		},\n		cacheResponseHandler:function () {\n			if (checkKeyResponse(this.name)) {				\n				doHtml(cache.get(this.name));\n			}	\n		}\n	});\n	\n	cache.addMapping({\n		name: \"_ReferringBlock\", \n		values: [\"referral_name\", \"referral_address\", \"referral_phone\", \"referral_fax\"], 	\n		storeInCacheHandler: function (key, value) {\n			var text = \n				(!cache.isEmpty(\"referral_name\") ? cache.get(\"referral_name\") + \"<br>\" : \"\") \n			  + (!cache.isEmpty(\"referral_address\") ? cache.get(\"referral_address\") + \"<br>\" : \"\")\n			  + (!cache.isEmpty(\"referral_phone\") ? \"Tel: \" + cache.get(\"referral_phone\") + \"<br>\" : \"\")\n			  + (!cache.isEmpty(\"referral_fax\") ? \"Fax: \" + cache.get(\"referral_fax\") + \"<br>\" : \"\");						  						 \n			cache.put(this.name, text)\n		},\n		cacheResponseHandler: function () {\n			if (checkKeyResponse(this.name)) {\n				doHtml(cache.get(this.name));\n			}\n		}\n	});\n	\n	cache.addMapping({\n		name: \"letterhead\", \n		values: [\"clinic_name\", \"clinic_fax\", \"clinic_phone\", \"clinic_addressLineFull\", \"doctor\", \"doctor_contact_phone\", \"doctor_contact_fax\", \"doctor_contact_addr\"], \n		storeInCacheHandler: function (key, value) {\n			var text = genericLetterhead();\n			cache.put(\"letterhead\", text);\n		},\n		cacheResponseHandler: function () {\n			if (checkKeyResponse(this.name)) {\n				doHtml(cache.get(this.name));\n			}\n		}\n	});\n	\n	cache.addMapping({\n		name: \"referral_nameL\", \n		values: [\"referral_name\"], \n		storeInCacheHandler: function(_key,_val) { \n		if (!cache.isEmpty(\"referral_name\")) {\n				var mySplitResult =  cache.get(\"referral_name\").toString().split(\",\");\n				cache.put(\"referral_nameL\", mySplitResult[0]);\n			} \n		}\n	});\n	\n	cache.addMapping({\n		name: \"complexAge\", \n		values: [\"complexAge\"], \n		cacheResponseHandler: function() {\n			if (cache.isEmpty(\"complexAge\")) { \n				printKey(\"age\"); \n			}\n			else {\n				if (checkKeyResponse(this.name)) {\n					doHtml(cache.get(this.name));\n				}\n			}\n		}\n	});\n	\n	// Setting up many to one mapping for derived gender keys.\n	var genderKeys = [\"he_she\", \"his_her\", \"gender\"];	\n	var genderIndex;\n	for (genderIndex in genderKeys) {\n		cache.addMapping({ name: genderKeys[genderIndex], values: [\"sex\"]});\n	}\n	cache.addMapping({name: \"sex\", values: [\"sex\"], storeInCacheHandler: populateGenderInfo});\n	\n	function isGenderLookup(key) {\n		var y;\n		for (y in genderKeys) { if (genderKeys[y] == key) { return true; } }\n		return false;\n	}\n	\n	function populateGenderInfo(key, val){\n		if (val == \'F\') {\n			cache.put(\"sex\", \"F\");\n			cache.put(\"he_she\", \"she\");\n			cache.put(\"his_her\", \"her\");\n			cache.put(\"gender\", \"female\");				\n		}\n		else {\n			cache.put(\"sex\", \"M\");\n			cache.put(\"he_she\", \"he\");\n			cache.put(\"his_her\", \"him\");\n			cache.put(\"gender\", \"male\");				\n		}\n	}\n	\n	function Start() {\n		\n			$.ajax({\n				url : \"efmformrtl_templates.jsp\",\n				success : function(data) {\n					$(\"#template\").html(data);\n					loadDefaultTemplate();\n				}\n			});\n	\n			$(\".cacheInit\").each(function() { \n				cache.put($(this).attr(\'name\'), $(this).val());\n				$(this).remove();				\n			});\n			\n			// set eventlistener for the iframe to flag changes in the text displayed \n			var agent = navigator.userAgent.toLowerCase(); //for non IE browsers\n			if ((agent.indexOf(\"msie\") == -1) || (agent.indexOf(\"opera\") != -1)) {\n				document.getElementById(cfg_editorname).contentWindow\n						.addEventListener(\'keypress\', setDirtyFlag, true);\n			}\n				\n			// set the HTML contents of this edit control from the value saved in Oscar (if any)\n			var contents = document.getElementById(\'Letter\').value\n			if (contents.length == 0) {\n				parseTemplate();\n			} else {\n				seteditControlContents(cfg_editorname, contents);\n			}\n	}\n\n	function htmlLine(text) {\n		return text.replace(/\\r?\\n/g,\"<br>\");\n	}\n\n	function genericLetterhead() {\n		// set the HTML contents of the letterhead\n		var address = \'<table border=0><tbody><tr><td><font size=6>\'\n				+ cache.get(\'clinic_name\')\n				+ \'</font></td></tr><tr><td><font size=2>\'\n				+ cache.get(\'doctor_contact_addr\')\n				+ \' Fax: \' + cache.get(\'doctor_contact_fax\')\n				+ \' Phone: \' + cache.get(\'doctor_contact_phone\')\n				+ \'</font><hr></td></tr></tbody></table><br>\';\n		\n		return address;\n	}\n\n	function fhtLetterhead() {\n		// set the HTML contents of the letterhead using FHT colours\n		var address = cache.get(\'clinic_addressLineFull\')\n				+ \'<br>Fax:\' + cache.get(\'clinic_fax\')\n				+ \' Phone:\' + cache.get(\'clinic_phone\');\n		if (cache.contains(\"doctor\") && cache.get(\'doctor\').indexOf(\'zapski\') > 0) {\n			address = \'293 Meridian Avenue, Haileybury, ON P0J 1K0<br> Tel 705-672-2442 Fax 705-672-2384\';\n		}\n		address = \'<table style=\\\'text-align: right;\\\' border=\\\'0\\\'><tbody><tr style=\\\'font-style: italic; color: rgb(71, 127, 128);\\\'><td><font size=\\\'+2\\\'>\'\n				+ cache.get(\'clinic_name\')\n				+ \'</font> <hr style=\\\'width: 100%; height: 3px; color: rgb(212, 118, 0); background-color: rgb(212, 118, 0);\\\'></td> </tr> <tr style=\\\'color: rgb(71, 127, 128);\\\'> <td><font size=\\\'+1\\\'>Family Health Team<br> &Eacute;quipe Sant&eacute; Familiale</font></td> </tr> <tr style=\\\'color: rgb(212, 118, 0); \\\'> <td><small>\'\n				+ address + \'</small></td> </tr> </tbody> </table>\';\n		return address;\n	}\n\n	var formIsRTL = true;\n\n</script>\n\n<!-- END OF EDITCONTROL CODE -->\n\n\n<form method=\"post\" action=\"\" name=\"RichTextLetter\" >\n\n<textarea name=\"Letter\" id=\"Letter\" style=\"width:600px; display: none;\"></textarea>\n\n<div class=\"DoNotPrint\" id=\"control3\" style=\"position:absolute; top:20px; left: 860px;\">\n\n<!-- Letter Head -->\n<input type=\"button\" class=\"butn\" name=\"AddLetterhead\" id=\"AddLetterhead\" value=\"Letterhead\" onclick=\"printKey(\'letterhead\');\">\n<br>\n\n<!-- Referring Block -->\n<input type=\"button\" class=\"butn\" name=\"AddReferral\" id=\"AddReferral\" value=\"Referring Block\" onclick=\"printKey(\'_ReferringBlock\');\">\n<br>\n\n<!-- Patient Block -->\n<input type=\"button\" class=\"butn\" name=\"AddLabel\" id=\"AddLabel\" value=\"Patient Block\" onclick=\"printKey(\'label\');\">\n<br>\n<br> \n\n<!-- Social History -->\n<input type=\"button\" class=\"butn\" name=\"AddSocialFamilyHistory\" value=\"Social History\" onclick=\"var hist=\'_SocialFamilyHistory\';printKey(hist);\">\n<br>\n\n<!--  Medical History -->\n<input type=\"button\"  class=\"butn\" name=\"AddMedicalHistory\" value=\"Medical History\" width=30 onclick=\"printKey(\'medical_history\'); \">\n<br>\n\n<!--  Ongoing Concerns -->\n\n<input type=\"button\" class=\"butn\" name=\"AddOngoingConcerns\" value=\"Ongoing Concerns\" onclick=\"var hist=\'ongoingconcerns\'; printKey(hist);\">\n<br>\n\n<!-- Reminders -->\n<input type=\"button\" class=\"butn\" name=\"AddReminders\" value=\"Reminders\"\n	onclick=\"var hist=\'reminders\'; printKey(hist);\">\n<br>\n\n<!-- Allergies -->\n<input type=\"button\" class=\"butn\" name=\"Allergies\" id=\"Allergies\" value=\"Allergies\" onclick=\"printKey(\'allergies_des\');\">\n<br>\n\n<!-- Prescriptions -->\n<input type=\"button\" class=\"butn\" name=\"Medlist\" id=\"Medlist\" value=\"Prescriptions\"	onclick=\"printKey(\'druglist_trade\');\">\n<br>\n\n<!-- Other Medications -->\n<input type=\"button\" class=\"butn\" name=\"OtherMedicationsHistory\" value=\"Other Medications\" onclick=\"printKey(\'other_medications_history\'); \">\n\n<br>\n\n<!-- Risk Factors -->\n<input type=\"button\" class=\"butn\" name=\"RiskFactors\" value=\"Risk Factors\" onclick=\"printKey(\'riskfactors\'); \">\n<br>\n\n<!-- Family History -->\n<input type=\"button\" class=\"butn\" name=\"FamilyHistory\" value=\"Family History\" onclick=\"printKey(\'family_history\'); \">\n<br>\n<br>\n\n<!-- Patient Name --> \n<input type=\"button\" class=\"butn\" name=\"Patient\" value=\"Patient Name\" onclick=\"printKey(\'first_last_name\');\">\n<br>\n\n<!-- Patient Age -->\n<input type=\"button\" class=\"butn\" name=\"PatientAge\" value=\"Patient Age\" onclick=\"var hist=\'ageComplex\'; printKey(hist);\">\n\n<br>\n\n<!-- Patient Label -->\n<input type=\"button\" class=\"butn\" name=\"label\" value=\"Patient Label\" onclick=\"hist=\'label\';printKey(hist);\">\n<br>\n\n<input type=\"button\" class=\"butn\" name=\"PatientSex\" value=\"Patient Gender\" onclick=\"printKey(\'sex\');\">\n<br>\n<br>\n\n<!-- Closing Salutation -->\n<input type=\"button\" class=\"butn\" name=\"Closing\" value=\"Closing Salutation\" onclick=\"printKey(\'_ClosingSalutation\');\">\n<br>\n\n<!--  Current User -->\n<input type=\"button\" class=\"butn\" name=\"User\" value=\"Current User\" onclick=\"var hist=\'current_user\'; printKey(hist);\">\n<br>\n\n<!-- Attending Doctor -->\n<input type=\"button\" class=\"butn\" name=\"Doctor\" value=\"Doctor (MRP)\" onclick=\"var hist=\'doctor\'; printKey(hist);\">\n<br>\n<br>\n\n</div>\n\n\n<div class=\"DoNotPrint\" >\n<input onclick=\"viewsource(this.checked)\" type=\"checkbox\">\nHTML Source\n<input onclick=\"usecss(this.checked)\" type=\"checkbox\">\nUse CSS\n	<table><tr><td>\n		 Subject: <input name=\"subject\" id=\"subject\" size=\"40\" type=\"text\">		 \n	 </td></tr></table>\n\n \n <div id=\"signatureInput\">&nbsp;</div>\n\n <div id=\"faxControl\">&nbsp;</div>\n \n<br>\n\n<input value=\"Submit\" name=\"SubmitButton\" type=\"submit\" onclick=\"needToConfirm=false;document.getElementById(\'Letter\').value=editControlContents(\'edit\');  document.RichTextLetter.submit()\">\n<input value=\"Print\" name=\"PrintSaveButton\" type=\"button\" onclick=\"document.getElementById(\'edit\').contentWindow.print();needToConfirm=false;document.getElementById(\'Letter\').value=editControlContents(\'edit\');  setTimeout(\'document.RichTextLetter.submit()\',1000);\">\n<input value=\"Reset\" name=\"ResetButton\" type=\"reset\">\n<input value=\"Print\" name=\"PrintButton\" type=\"button\" onclick=\"document.getElementById(\'edit\').contentWindow.print();\">\n\n\n    	</div>\n\n</form>\n\n</body></html>','0');


-- update-2012-07-18.sql
alter table ProviderPreference add column encryptedMyOscarPassword varbinary(255);


-- update-2012-07-19.sql

CREATE TABLE `PrintResourceLog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  resourceName varchar(100) NOT NULL,
  resourceId varchar(50) NOT NULL,
  dateTime timestamp not null,
  providerNo varchar(10),
  externalLocation varchar(200),
  externalMethod varchar(100),
  PRIMARY KEY (`id`)
);

ALTER TABLE `RemoteIntegratedDataCopy` DROP KEY `RIDopy_demo_dataT_sig_fac_arch`, ADD KEY `RIDopy_demo_dataT_sig_fac_arch` (`demographic_no`,`datatype`(165),`signature`(165),`facilityId`,`archived`);

ALTER TABLE `drugReason` DROP KEY `codingSystem`, ADD KEY `codingSystem` (`codingSystem`(30),`code`(30));

-- update-2012-07-26.sql

CREATE TABLE `eyeform_macro_def` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `macroName` varchar(255),
  `lastUpdated` datetime,
  `copyFromLastImpression` tinyint(1),
  `impressionText` text,
  `planText` text,
  PRIMARY KEY (`id`)
);

CREATE TABLE `eyeform_macro_billing` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `macroId` int(11),
  `billingServiceCode` varchar(50),
  `multiplier` double,
  PRIMARY KEY (`id`)
);

INSERT INTO `issue` (`code`, `description`, `role`, `update_date`, `priority`, `type`)
VALUES
	('eyeformFollowUp', 'Follow-Up Item for Eyeform', 'nurse', NOW(), NULL, 'system'),
	('eyeformCurrentIssue', 'Current Presenting Issue Item for Eyeform', 'nurse', NOW(), NULL, 'system'),
	('eyeformPlan', 'Plan Item for Eyeform', 'nurse', NOW(), NULL, 'system'),
	('eyeformImpression', 'Impression History Item for Eyeform', 'nurse', NOW(), NULL, 'system'),
	('eyeformProblem', 'Problem List Item for Eyeform', 'nurse', NOW(), NULL, 'system');

-- =========END OF FEATURES THAT MADE THE 12_1 RELEASE========= --

-- the following late additions didn't make it into the official release 
-- they have been trimmed off from an earlier version of this file
-- update-2012-08-09.sql intakehHx form and tables
-- update-2012-08-23.sql billing_on_premium

-- PHC
update program set defaultServiceRestrictionDays=30 where defaultServiceRestrictionDays IS NULL; 

