-- Change 12_1 data to master Feb 10, 2015 mildly debuged.

-- master updated on Feb 1, 2016
-- updates appended to update-2016-01-25.sql


-- update-2012-06-29  modified to allow for the drop not to fail

DROP TABLE IF EXISTS Vacancy;

CREATE TABLE IF NOT EXISTS `vacancy` (
  `id` int(11) primary key NOT NULL AUTO_INCREMENT,
  `vacancyName` varchar(255) NOT NULL,
  `templateId` int(11) NOT NULL,
  `status` varchar(24) NOT NULL,
  `dateClosed` timestamp NULL,
  `reasonClosed` varchar(255),
  `wlProgramId` int(11) NOT NULL,
  `dateCreated` datetime NOT NULL,
  `emailNotificationAddressesCsv` varchar(255),
   `statusUpdateUser` varchar(25),
   `statusUpdateDate` datetime
);

-- update-2012-07-06.sql  already applied
-- alter table mygroup add default_billing_form varchar(10);

-- update-2012-07-11.sql  already applied
-- delete from PageMonitor;
-- alter table PageMonitor add pageId varchar(255) NOT NULL;


-- update 2012-08-09
INSERT INTO encounterForm VALUES ('Student Intake Hx','../form/formIntakeHx.jsp?demographic_no=','formIntakeHx', '0');

CREATE TABLE IF NOT EXISTS `formIntakeHx` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(10) NOT NULL,
  `provider_no` int(10),
  `formCreated` date,
  `formEdited` timestamp,
  `student_number` varchar(50),
  `student_surname` varchar(50),
  `student_firstname` varchar(50),
  `student_dob` varchar(50),
  `student_sex` varchar(50),
  `student_ercontact_name` varchar(50),
  `student_ercontact_phone` varchar(50),
  `student_ercontact_address` varchar(50),
  `student_ercontact_address2` varchar(50),
  `student_physician_name` varchar(50),
  `student_physician_address` varchar(50),
  `student_physician_address2` varchar(50),
  `student_physician_phone` varchar(50),
  `student_faculty_phone` varchar(50),
  `academic_year` varchar(50),
  `pt_ft` varchar(50),
  `AllergicYesNo` varchar(50),
  `AllergicNonDrugYesNo` varchar(50),
  `DrugAllergy1` varchar(50),
  `DrugAllergyRS1` varchar(50),
  `DrugAllergy2` varchar(50),
  `DrugAllergyRS2` varchar(50),
  `DrugAllergy3` varchar(50),
  `DrugAllergyRS3` varchar(50),
  `DrugAllergy4` varchar(50),
  `DrugAllergyRS4` varchar(50),
  `DrugAllergy5` varchar(50),
  `DrugAllergyRS5` varchar(50),
  `DrugAllergy6` varchar(50),
  `DrugAllergyRS6` varchar(50),
  `DrugAllergy7` varchar(50),
  `DrugAllergyRS7` varchar(50),
  `DrugAllergy8` varchar(50),
  `DrugAllergyRS8` varchar(50),
  `AllergicShotsYesNo` varchar(50),
  `allergicbee` tinyint(1),
  `allergicragweed` tinyint(1),
  `allergicOtherPollens` tinyint(1),
  `allergicgrasses` tinyint(1),
  `allergicdust` tinyint(1),
  `allergicanimal` tinyint(1),
  `allergicother` tinyint(1),
  `allergicfood` tinyint(1),
  `TakeOtherDrugs` varchar(50),
  `CurrentDrug1` varchar(50),
  `CurrentDrug2` varchar(50),
  `CurrentDrug3` varchar(50),
  `CurrentDrug4` varchar(50),
  `CurrentDrug5` varchar(50),
  `CurrentDrug6` varchar(50),
  `CurrentDrug7` varchar(50),
  `SeriousIllness` varchar(50),
  `PastIllness1` varchar(50),
  `IllnessAge1` varchar(50),
  `PastIllness2` varchar(50),
  `IllnessAge2` varchar(50),
  `PastIllness3` varchar(50),
  `IllnessAge3` varchar(50),
  `PastIllness4` varchar(50),
  `IllnessAge4` varchar(50),
  `PastIllness5` varchar(50),
  `IllnessAge5` varchar(50),
  `PastIllness6` varchar(50),
  `IllnessAge6` varchar(50),
  `operations` varchar(50),
  `NameofOperation1` varchar(50),
  `NameofOperationAge1` varchar(50),
  `NameofOperation2` varchar(50),
  `NameofOperationAge2` varchar(50),
  `NameofOperation3` varchar(50),
  `NameofOperationAge3` varchar(50),
  `NameofOperation4` varchar(50),
  `NameofOperationAge4` varchar(50),
  `NameofOperation5` varchar(50),
  `NameofOperationAge5` varchar(50),
  `NameofOperation6` varchar(50),
  `NameofOperationAge6` varchar(50),
  `Conditionsbrokenbones` varchar(50),
  `ConditionsDescbrokenbones` varchar(50),
  `Conditionsmigraine` varchar(50),
  `ConditionsDescmigraine` varchar(50),
  `Conditionsneurologicdisorder` varchar(50),
  `ConditionsDescneurologicdisorder` varchar(50),
  `Conditionsasthma` varchar(50),
  `ConditionsDescasthma` varchar(50),
  `Conditionspneumonia` varchar(50),
  `ConditionsDescpneumonia` varchar(50),
  `Conditionslungdisease` varchar(50),
  `ConditionsDesclungdisease` varchar(50),
  `Conditionsheartdisease` varchar(50),
  `ConditionsDescheartdisease` varchar(50),
  `Conditionsulcer` varchar(50),
  `ConditionsDesculcer` varchar(50),
  `Conditionsboweldisease` varchar(50),
  `ConditionsDescboweldisease` varchar(50),
  `Conditionshepatitis` varchar(50),
  `ConditionsDeschepatitis` varchar(50),
  `ConditionsHIV` varchar(50),
  `ConditionsDescHIV` varchar(50),
  `Conditionsthyroid` varchar(50),
  `ConditionsDescthyroid` varchar(50),
  `Conditionsblooddisorder` varchar(50),
  `ConditionsDescblooddisorder` varchar(50),
  `Conditionsdiabetes` varchar(50),
  `ConditionsDescdiabetes` varchar(50),
  `Conditionsbloodtransfusion` varchar(50),
  `ConditionsDescbloodtransfusion` varchar(50),
  `Conditionscancerorleukemia` varchar(50),
  `ConditionsDesccancerorleukemia` varchar(50),
  `Conditionssexualdisease` varchar(50),
  `ConditionsDescsexualdisease` varchar(50),
  `ConditionsURI` varchar(50),
  `ConditionsDescURI` varchar(50),
  `Conditionsemotional` varchar(50),
  `ConditionsDescemotional` varchar(50),
  `Conditionsarthritis` varchar(50),
  `ConditionsDescarthritis` varchar(50),
  `Conditionseatingdisorder` varchar(50),
  `ConditionsDesceatingdisorder` varchar(50),
  `Conditionsosteoporosis` varchar(50),
  `ConditionsDescosteoporosis` varchar(50),
  `Conditionsskin` varchar(50),
  `ConditionsDescskin` varchar(50),
  `ConditionsHighbloodpressure` varchar(50),
  `ConditionsDescHighbloodpressure` varchar(50),
  `Conditionslearningdisability` varchar(50),
  `ConditionsDesclearningdisability` varchar(50),
  `Conditionsschizophrenia` varchar(50),
  `ConditionsDescschizophrenia` varchar(50),
  `Conditionsalcohol` varchar(50),
  `ConditionsDescalcohol` varchar(50),
  `ConditionsMS` varchar(50),
  `ConditionsDescMS` varchar(50),
  `Conditionsstroke` varchar(50),
  `ConditionsDescstroke` varchar(50),
  `ConditionsHighcholesterol` varchar(50),
  `ConditionsDescHighcholesterol` varchar(50),
  `Conditionsdepression` varchar(50),
  `ConditionsDescdepression` varchar(50),
  `ConditionsDrugdependency` varchar(50),
  `ConditionsDescDrugdependency` varchar(50),
  `ConditionsOtherdisease` text,
  `ConditionsDescOtherdisease` varchar(50),
  `ImmunizationHepatitisB` varchar(50),
  `ImmunizationYearHepatitisB` varchar(50),
  `ImmunizationHadTetanus` varchar(50),
  `ImmunizationYearTetanus` varchar(50),
  `ImmunizationHadPolio` varchar(50),
  `ImmunizationYearPolio` varchar(50),
  `ImmunizationHadMMR` varchar(50),
  `ImmunizationYearMMR` varchar(50),
  `ImmunizationHadTB` varchar(50),
  `ImmunizationYearTB` varchar(50),
  `ImmunizationHadRubella` varchar(50),
  `ImmunizationYearRubella` varchar(50),
  `ImmunizationHadVaricella` varchar(50),
  `ImmunizationYearVaricella` varchar(50),
  `ImmunizationHadMeningitis` varchar(50),
  `ImmunizationYearMeningitis` varchar(50),
  `ImmunizationHadPneumococcus` varchar(50),
  `ImmunizationYearPneumococcus` varchar(50),
  `HaveImmunizationCard` varchar(50),
  `SeatBelt` varchar(50),
  `smoker` varchar(50),
  `HowMuchSmoke` varchar(50),
  `smokeInPast` varchar(50),
  `UseDrugs` varchar(50),
  `Alcohol` varchar(50),
  `HowManyDrinks` varchar(50),
  `HowManyDrinksWeek` varchar(50),
  `exercise` varchar(50),
  `biologicalmigraine` varchar(50),
  `biologicalDescmigraine` varchar(50),
  `biologicalneurologic` varchar(50),
  `biologicalDescneurologic` varchar(50),
  `biologicalasthma` varchar(50),
  `biologicalDescasthma` varchar(50),
  `biologicalpneumonia` varchar(50),
  `biologicalDescpneumonia` varchar(50),
  `biologicallungdisease` varchar(50),
  `biologicalDesclungdisease` varchar(50),
  `biologicalheartdisease` varchar(50),
  `biologicalDescheartdisease` varchar(50),
  `biologicalulcer` varchar(50),
  `biologicalDesculcer` varchar(50),
  `biologicalboweldisease` varchar(50),
  `biologicalDescboweldisease` varchar(50),
  `biologicalhepatitis` varchar(50),
  `biologicalDeschepatitis` varchar(50),
  `biologicalthyroid` varchar(50),
  `biologicalDescthyroid` varchar(50),
  `biologicalblooddisorder` varchar(50),
  `biologicalDescblooddisorder` varchar(50),
  `biologicaldiabetes` varchar(50),
  `biologicalDescdiabetes` varchar(50),
  `biologicalbloodtransfusion` varchar(50),
  `biologicalDescbloodtransfusion` varchar(50),
  `biologicalcancerorleukemia` varchar(50),
  `biologicalDesccancerorleukemia` varchar(50),
  `biologicalURI` varchar(50),
  `biologicalDescURI` varchar(50),
  `biologicalemotional` varchar(50),
  `biologicalDescemotional` varchar(50),
  `biologicalarthritis` varchar(50),
  `biologicalDescarthritis` varchar(50),
  `biologicalosteoporosis` varchar(50),
  `biologicalDescosteoporosis` varchar(50),
  `biologicalskin` varchar(50),
  `biologicalDescskin` varchar(50),
  `biologicalHBP` varchar(50),
  `biologicalDescHBP` varchar(50),
  `biologicallearningdisability` varchar(50),
  `biologicalDesclearningdisability` varchar(50),
  `biologicalschizophrenia` varchar(50),
  `biologicalDescschizophrenia` varchar(50),
  `biologicalalcohol` varchar(50),
  `biologicalDescalcohol` varchar(50),
  `biologicalMS` varchar(50),
  `biologicalDescMS` varchar(50),
  `biologicalstroke` varchar(50),
  `biologicalDescstroke` varchar(50),
  `biologicalhighcholesterol` varchar(50),
  `biologicalDeschighcholesterol` varchar(50),
  `biologicaldepression` varchar(50),
  `biologicalDescdepression` varchar(50),
  `biologicaldrug` varchar(50),
  `biologicalDescdrug` varchar(50),
  `General` varchar(250),
  `Nervous` varchar(250),
  `HEENT` varchar(250),
  `Neck` varchar(250),
  `Chest` varchar(250),
  `Heart` varchar(250),
  `Gastrointestinal` varchar(50),
  `GenitalsUrinary` varchar(50),
  `GeneralPsychiatric` varchar(50),
  `firstPeriod` varchar(50),
  `monthlyPeriod` text,
  `periodLength` text,
  `severeCramps` text,
  `unusualBleeding` text,
  `PID` text,
  `ovarianCyst` text,
  `breastCancer` text,
  `hadBreastLump` text,
  `BeenPregnant` text,
  `TherapeuticAbortion` text,
  `AgeHadAbortion` text,
  `HadPap` text,
  `AbnormalPap` text,
  `LastPap` text,
  `usedBirthControl` text,
  `birthcontrolUsed1` varchar(50),
  `birthcontrolUsed2` varchar(50),
  `birthcontrolUsed3` varchar(50),
  `birthcontrolUsed4` varchar(50),
  `onbirthcontrol` varchar(50),
  `problemsBirthControl` varchar(50),
  `monthlyBreastSelfExam` varchar(50),
  `hadSexualIntercourse` varchar(50),
  `sexWithMale` varchar(50),
  `sexWithFemale` varchar(50),
  `ageHadSex` varchar(50),
  `partnersLastYear` varchar(50),
  `HowOftenUseCondoms` varchar(50),
  `hadSTD` varchar(50),
  `hadHPV` varchar(50),
  `hadchlamydia` varchar(50),
  `hadgonorrhea` varchar(50),
  `hadHSV2` varchar(50),
  `hadsyphilis` varchar(50),
  `immunizationDiseasePneumococcus` text,
  `immunizationDiseaseYearPneumococcus` text,
  `immunizationDiseaseMeningitis` text,
  `immunizationDiseaseYearMeningitis` text,
  `immunizationDiseaseVaricella` text,
  `immunizationDiseaseYearVaricella` text,
  `immunizationDiseaseTb` text,
  `immunizationDiseaseYearTb` text,
  `immunizationDiseaseRubella` text,
  `immunizationDiseaseYearRubella` text,
  `immunizationDiseaseMMR` text,
  `immunizationDiseaseYearMMR` text,
  `immunizationDiseasePolio` text,
  `immunizationDiseaseYearPolio` text,
  `immunizationDiseaseTetanus` text,
  `immunizationDiseaseYearTetanus` text,
  `immunizationDiseaseYearHepatitisB` text,
  `immunizationDiseaseHepatitisB` text,
   PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- update-2012-08-23
CREATE TABLE IF NOT EXISTS `billing_on_premium` (
	premium_id int (10) NOT NULL auto_increment primary key, 
	raheader_no int(6) NOT NULL, 
	provider_no varchar(6), 
	providerohip_no varchar(6) NOT NULL, 
	pay_date date NOT NULL, 
	amount_pay varchar(10) NOT NULL, 
	status tinyint(1) NOT NULL, 
	create_date timestamp NOT NULL, 
	creator varchar(6) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- update-2012-08-30
insert into secRole (role_name,description) values ("Field Note Admin","Field Note Admin");
insert into secObjPrivilege values ("Field Note Admin","_admin.fieldnote","x",0,"999998");

-- update-2012-09-14-ocan
insert into `secObjectName` (objectName) values ('_pmm_clientForms.updateCompletedOcan');
insert into `secObjPrivilege` values('doctor','_pmm_clientForms.updateCompletedOcan','x',0,'999998');

-- update-2012-09-18
CREATE TABLE IF NOT EXISTS `formCounseling` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `provider_no` int(10),    
  `doc_name` varchar(60),
  `cl_name` varchar(60),
  `cl_address1` varchar(170),
  `cl_address2` varchar(170),
  `cl_phone` varchar(16),
  `cl_fax` varchar(16),
  `billingreferral_no` int(10),
  `t_name` varchar(60),
  `t_name2` varchar(60),
  `t_address1` varchar(170),
  `t_address2` varchar(170),
  `t_phone` varchar(16),
  `t_fax` varchar(16),
  `demographic_no` int(10) NOT NULL,  
  `p_name` varchar(60),
  `p_address1` varchar(170),
  `p_address2` varchar(170),
  `p_phone` varchar(16),
  `p_birthdate` varchar(30),
  `p_healthcard` varchar(20),  
  `comments` text,
  `formCreated` date,
  `formEdited` timestamp,
  `consultTime` date,  
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


-- update-2012-09-28.sql 
CREATE TABLE IF NOT EXISTS `formNoShowPolicy` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `provider_no` int(10),
  `demographic_no` int(10) NOT NULL,
  `formCreated` date,
  `formEdited` timestamp,
  `formVersion` varchar(10),
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

alter table quickList modify createdByProvider varchar(20);

-- update-2012-09-30.sql moved to procedure
-- ALTER TABLE billing_on_eareport MODIFY admitted_date date;


-- update-2012-10-01.sql 
ALTER TABLE studydata add column keyname varchar(32);
CREATE TABLE IF NOT EXISTS `providerstudy` (
	study_no int(10), 
	provider_no varchar(6), 
	creator varchar(6), 
	`timestamp` timestamp
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- update-2012-10-16.sql  moved to procedure
-- alter table billing_on_ext change payment_id payment_id int(10);

-- update-2012-10-17.sql
CREATE TABLE IF NOT EXISTS `workflow` (
  ID int(10) NOT NULL AUTO_INCREMENT,
  workflow_type varchar(100),
  provider_no varchar(20),
  demographic_no int(10),
  completion_date date,
  current_state varchar(50),
  create_date_time datetime, 
  PRIMARY KEY(`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- update-2012-10-19.sql
--
-- Table structure for table `formSelfAssessment`
--

CREATE TABLE IF NOT EXISTS `formSelfAssessment` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(10),
  `provider_no` int(10),
  `formCreated` date,
  `formEdited` timestamp,
  `name` varchar(255),
  `p_birthdate` varchar(255),
  `sex` varchar(255),
  `faculty` varchar(255),
  `AcademicYear` varchar(255),
  `PTFT` varchar(255),
  `Job` varchar(255),
  `Hours` varchar(255),
  `Residence` mediumtext,
  `Campus` mediumtext,
  `Home` mediumtext,
  `Roommates` mediumtext,
  `LivingSituationOther` mediumtext,
  `Depression` mediumtext,
  `helplessness` mediumtext,
  `ADHD` mediumtext,
  `Obsessions` mediumtext,
  `Bipolar` mediumtext,
  `Anxiety` mediumtext,
  `Esteem` mediumtext,
  `Relationship` mediumtext,
  `Eating` mediumtext,
  `Sexual` mediumtext,
  `Suicidal` mediumtext,
  `Psychosis` mediumtext,
  `Mania` mediumtext,
  `Grief` mediumtext,
  `Substance` mediumtext,
  `TraumaEmotional` mediumtext,
  `TraumaPhysical` mediumtext,
  `TraumaSexual` mediumtext,
  `Academic` mediumtext,
  `ReasonsOther` mediumtext,
  `Hospitalizations` mediumtext,
  `Surgery` mediumtext,
  `Medicalillnesses` mediumtext,
  `CurrentMedications` mediumtext,
  `CurrentMedicationsList` mediumtext,
  `psychiatricMedications` mediumtext,
  `psychiatricMedicationsList` mediumtext,
  `HospitalizationsOther` mediumtext,
  `PastSubstance` mediumtext,
  `PastAlcohol` mediumtext,
  `PastPrescribedDrugs` mediumtext,
  `PastCounterMedications` mediumtext,
  `PastStreetDrugs` mediumtext,
  `PastTobacco` mediumtext,
  `PastPSYCHIATRICTraumaEmotional` mediumtext,
  `PastPSYCHIATRICTraumaPhysical` mediumtext,
  `PastPSYCHIATRICTraumaSexual` mediumtext,
  `PastLegal` mediumtext,
  `PastGambling` mediumtext,
  `PastReactionsMedication` mediumtext,
  `PastReactionsMedicationList` mediumtext,
  `PastSuicideAttempts` mediumtext,
  `PastSuicideMany` mediumtext,
  `PastSuicideWhen` mediumtext,
  `PastCutting` mediumtext,
  `ptsd` mediumtext,
  `PastPASTPSYCHIATRICOther` mediumtext,
  `AgesMother` mediumtext,
  `AgesFather` mediumtext,
  `AgesSiblings` mediumtext,
  `AgesOthers` mediumtext,
  `Adopted` mediumtext,
  `FamilyDepression` mediumtext,
  `FamilyAnxiety` mediumtext,
  `FamilySubstance` mediumtext,
  `FamilyAlcohol` mediumtext,
  `FamilyDrugs` mediumtext,
  `FamilyEmotional` mediumtext,
  `FamilyPhysical` mediumtext,
  `FamilySexual` mediumtext,
  `FamilySuicide` mediumtext,
  `FamilyEating` mediumtext,
  `FamilyBipolar` mediumtext,
  `FamilyPsychosis` mediumtext,
  `FamilySchizophrenia` mediumtext,
  `FamilyADHD` mediumtext,
  `FamilyPsychiatricOther` mediumtext,
  `Smoker` mediumtext,
  `SmokeQty` mediumtext,
  `StreetDrugs` mediumtext,
  `DrinkAlcohol` mediumtext,
  `DrinkAlcoholMany` mediumtext,
  `DrinkAlcoholWeekly` mediumtext,
  `Exercise` mediumtext,
  `Meals` mediumtext,
  `InRelationship` mediumtext,
  `AcademicPerformance` mediumtext,
  `SexualOrientation` mediumtext,
  `ReligiousAffiliation` mediumtext,
  `GeneralOther` mediumtext,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;



-- update-2012-10-22.sql
CREATE TABLE IF NOT EXISTS `frm_labreq_preset` (
preset_id int (10) NOT NULL auto_increment primary key,
lab_type varchar(255)  NOT NULL,
prop_name varchar(255) NOT NULL,
prop_value varchar(255) NOT NULL,
status int (1) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_glucose_fasting","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_hba1c","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_uricAcid","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_lipidAssessment","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","aci","\n Fast for 12 hours",'1');


insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","b_urinalysis","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","i_rubella","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","i_prenatal","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","m_urine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","o_otherTests1","Hep B s Ag",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","o_otherTests2","VDRL",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","o_otherTests3","HIV",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","o_otherTests4","Varicella titre",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","o_otherTests5","Ferritin",'1');


insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","b_urinalysis","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","h_bloodFilmExam","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","h_hemoglobin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","h_wcbCount","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","h_hematocrit","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","i_heterophile","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","i_rubella","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","i_prenatal","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","i_prenatalHepatitisB","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","i_vdrl","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","m_urine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","otherTest","HIV \n\nVaricella titre",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Acne","../form/formlabreq07.jsp?labType=Acne&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Acne","../form/formlabreq10.jsp?labType=Acne&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","b_bilirubin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","b_lipidAssessment","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","h_cbc","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 AHEf","../form/formlabreq07.jsp?labType=AHEf&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 AHEf","../form/formlabreq10.jsp?labType=AHEf&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","i_rubella","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","o_otherTests1","pap",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","m_chlamydia","pap",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","m_chlamydiaSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","m_gcSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","m_vaginal","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 AHEm","../form/formlabreq07.jsp?labType=AHEm&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 AHEm","../form/formlabreq10.jsp?labType=AHEm&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEm","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEm","h_cbc","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Anemia","../form/formlabreq07.jsp?labType=Anemia&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Anemia","../form/formlabreq10.jsp?labType=Anemia&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Anemia","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Anemia","b_ferritin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Anemia","o_otherTests1","reticulocyte ct",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Bp","../form/formlabreq07.jsp?labType=Bp&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Bp","../form/formlabreq10.jsp?labType=Bp&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_sodium","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_potassium","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_chloride","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_lipidAssessment","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","o_otherTests1","urine, routine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","o_otherTests2","urine, micro",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Chol","../form/formlabreq07.jsp?labType=Chol&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Chol","../form/formlabreq10.jsp?labType=Chol&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_bilirubin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_lipidAssessment","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","h_cbc","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Ed","../form/formlabreq07.jsp?labType=Ed&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Ed","../form/formlabreq10.jsp?labType=Ed&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_bilirubin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","o_otherTests1","lh",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","o_otherTests2","fsh",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","o_otherTests3","prolactin",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","o_otherTests4","testosterone",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","o_otherTests5","testosterone,free",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Fatigue","../form/formlabreq07.jsp?labType=Fatigue&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Fatigue","../form/formlabreq10.jsp?labType=Fatigue&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_bilirubin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_vitaminB12","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_ferritin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","i_mononucleosisScreen","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","o_otherTests1","urine, routine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","o_otherTests1","urine, micro",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Fulstdf","../form/formlabreq07.jsp?labType=Fulstdf&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Fulstdf","../form/formlabreq10.jsp?labType=Fulstdf&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","m_vaginal","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","m_chlamydiaSource","cervical",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","m_gcSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","v_immuneStatus","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","v_immune_HepatitisB","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","v_immune_HepatitisC","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","o_otherTests1","hiv-ab",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","o_otherTests2","hep b surf antigen",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","o_otherTests3","VDRL",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Fulstdm","../form/formlabreq07.jsp?labType=Fulstdm&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Fulstdm","../form/formlabreq10.jsp?labType=Fulstdm&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","m_chlamydiaSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","m_gcSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","v_immuneStatus","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","v_immune_HepatitisB","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","v_immune_HepatitisC","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","o_otherTests1","hiv-ab",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","o_otherTests2","hep b surf antigen",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","o_otherTests3","vdrl",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Gastro","../form/formlabreq07.jsp?labType=Gastro&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Gastro","../form/formlabreq10.jsp?labType=Gastro&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gastro","o_otherTests1","stool c+s x1",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gastro","o_otherTests2","stool c+p x1",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Gstdm","../form/formlabreq07.jsp?labType=Gstdm&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Gstdm","../form/formlabreq10.jsp?labType=Gstdm&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","m_chlamydiaSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","m_gcSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","v_immuneStatus","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","v_immune_HepatitisA","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","v_immune_HepatitisB","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","v_immune_HepatitisC","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","o_otherTests1","hiv-ab",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","o_otherTests2","vdrl",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","o_otherTests3","rectal - gc/chlam",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Gyne","../form/formlabreq07.jsp?labType=Gyne&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Gyne","../form/formlabreq10.jsp?labType=Gyne&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","o_otherTests1","pap",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","m_chlamydiaSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","m_gcSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","m_vaginal","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 HIV","../form/formlabreq07.jsp?labType=HIV&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 HIV","../form/formlabreq10.jsp?labType=HIV&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("HIV","o_otherTests1","hiv-ab",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Ibs","../form/formlabreq07.jsp?labType=Ibs&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Ibs","../form/formlabreq10.jsp?labType=Ibs&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ibs","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ibs","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ibs","o_otherTests1","stool c+s x1",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ibs","o_otherTests2","stool c+s x1",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Mono","../form/formlabreq07.jsp?labType=Mono&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Mono","../form/formlabreq10.jsp?labType=Mono&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Mono","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Mono","i_mononucleosisScreen","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Pcod","../form/formlabreq07.jsp?labType=Pcod&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Pcod","../form/formlabreq10.jsp?labType=Pcod&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","o_otherTests1","prolactin",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","o_otherTests2","lh",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","o_otherTests3","fsh",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","o_otherTests4","17-hydroxy-progest",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","o_otherTests5","testosterone",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Stdf","../form/formlabreq07.jsp?labType=Stdf&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Stdf","../form/formlabreq10.jsp?labType=Stdf&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdf","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdf","m_chlamydiaSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdf","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdf","m_gcSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdf","m_vaginal","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Stdms","../form/formlabreq07.jsp?labType=Stdms&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Stdms","../form/formlabreq10.jsp?labType=Stdms&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","m_chlamydiaSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","m_gcSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","m_urine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","o_otherTests1","urine - microscopic",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Stdma","../form/formlabreq07.jsp?labType=Stdma&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Stdma","../form/formlabreq10.jsp?labType=Stdma&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdma","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdma","m_chlamydiaSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdma","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdma","m_gcSource","urine",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Thyroid","../form/formlabreq07.jsp?labType=Thyroid&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Thyroid","../form/formlabreq10.jsp?labType=Thyroid&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Thyroid","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Thyroid","o_otherTests1","free t4",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Urticaria","../form/formlabreq07.jsp?labType=Urticaria&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Urticaria","../form/formlabreq10.jsp?labType=Urticaria&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","i_mononucleosisScreen","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","i_rubella","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","v_acuteHepatitis","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","o_otherTests1","Urinalysis (routine)",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Uti","../form/formlabreq07.jsp?labType=Uti&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Uti","../form/formlabreq10.jsp?labType=Uti&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Uti","m_urine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Uti","o_otherTests1","urine, routine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Uti","o_otherTests2","urine, micro",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Vaginitis","../form/formlabreq07.jsp?labType=Vaginitis&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Vaginitis","../form/formlabreq10.jsp?labType=Vaginitis&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Vaginitis","o_otherTests1","swab vaginal c+s",'1');

insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 AntiPsychotic","../form/formlabreq07.jsp?labType=AntiPsychotic&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 AntiPsychotic","../form/formlabreq10.jsp?labType=AntiPsychotic&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_sodium","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_potassium","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_lipidAssessment","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","o_otherTests1","AST",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","o_otherTests2","Prolactin",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","o_otherTests3","ECG",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 PreLithium","../form/formlabreq07.jsp?labType=PreLithium&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 PreLithium","../form/formlabreq10.jsp?labType=PreLithium&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_bilirubin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_albumin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_urinalysis","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","o_otherTests1","GGT",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","o_otherTests2","ECG",'1');

-- update-2012-10-23.sql moved to procedure
-- alter table formLabReq10 add patientChartNo varchar(20);
-- alter table formLabReq07 add patientChartNo varchar(20);

-- update-2012-10-25.sql
CREATE TABLE IF NOT EXISTS `formPositionHazard` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(11),
  `formCreated` date,
  `formEdited`  timestamp NOT NULL,
  `supervisor` varchar(100),
  `formCreatedBy` varchar(6),
  `Acrylonitrile` varchar(1),
  `JobReclassify` tinyint(1),
  `NewHire` tinyint(1),
  `ProcedureChange` tinyint(1),
  `staffName` varchar(500),
  `staffPosition` varchar(500),
  `staffPhone` varchar(500),
  `staffJobSite` varchar(500),
  `staffDept` varchar(500),
  `staffEmail` varchar(500),
  `supervisorName` varchar(500),
  `supervisorEmail` varchar(500),
  `supervisorCampusAddress` varchar(500),
  `supervisorPhone` varchar(500),
  `staffFaculty` varchar(500),
  `Asbestos` varchar(1),
  `EthyleneOxide` varchar(1),
  `Lead` varchar(1),
  `Silica` varchar(1),
  `Arsenic` varchar(1),
  `Benzene` varchar(1),
  `Isocyanates` varchar(1),
  `Mercury` varchar(1),
  `VinylChloride` varchar(1),
  `CompressedGases` varchar(1),
  `MaterialsSeriousEffects` varchar(1),
  `ReactiveMaterials` varchar(1),
  `FlammableCombustible` varchar(1),
  `CorrosiveMaterials` varchar(1),
  `MaterialsOtherToxicEffects` varchar(1),
  `OxidizingMaterials` varchar(1),
  `AgricultureChemicals` varchar(1),
  `ChemicalNames` varchar(500),
  `ChemicalHowItsUsed` varchar(500),
  `ContainmentLevel1` varchar(1),
  `ContainmentLevel2` varchar(1),
  `ContainmentLevel3` varchar(1),
  `AnimalCareFacility` varchar(1),
  `SheepContainmentUnit` varchar(1),
  `NonHumanPrimate` varchar(1),
  `ContainmentLevel3Area` varchar(1),
  `PrimaryCulture` varchar(1),
  `Cadavers` varchar(1),
  `BloodProducts` varchar(1),
  `PrimaryPatientCare` varchar(1),
  `OtherHumanBiohazard` text,
  `Primates` varchar(1),
  `DogsCats` varchar(1),
  `Sheep` varchar(1),
  `WildMammals` varchar(1),
  `Rodents` varchar(1),
  `OtherAnimalBiohazard` varchar(500),
  `OtherBiohazard` varchar(500),
  `PathogenicParasites` varchar(500),
  `UltravioletEmitter` varchar(1),
  `InfraredEmitter` varchar(1),
  `Irradiators` varchar(1),
  `Vibration` varchar(1),
  `ExtremeHeat` varchar(1),
  `RadioactiveSubstance` varchar(1),
  `MicrowaveEmittingDevice` varchar(1),
  `XrayEmittingDevice` varchar(1),
  `RadioFrequency` varchar(1),
  `ExtremeCold` varchar(1),
  `Ultrasound` varchar(1),
  `Infrasound` varchar(1),
  `Gamma` varchar(1),
  `HighNoiseLevels` varchar(1),
  `Laser3B` varchar(1),
  `Laser4` varchar(1),
  `MagneticField` varchar(1),
  `Nanotechnology` text,
  `Driving` varchar(1),
  `HighVoltage` varchar(1),
  `ConfinedSpaceEntry` varchar(1),
  `Heights` varchar(1),
  `ComputerWork` varchar(1),
  `RepetitiveWork` varchar(1),
  `AwkwardPositions` varchar(1),
  `WasteManagement` tinyint(1),
  `RadiationSafety` tinyint(1),
  `EmployeeSafety` tinyint(1),
  `XrayTraining` tinyint(1),
  `AnimalHandling` tinyint(1),
  `BiosafetyTraining` tinyint(1),
  `FormCompletedBy` varchar(500),
  `AdditionalNotes` text,
  `WHIMS` tinyint(1),
  `OtherCode` varchar(500),
  `ProcedureName` text,
  `SubstanceForm` text,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

insert into encounterForm values ('Position Hazard', '../form/formPositionHazard.jsp?demographic_no=', 'formPositionHazard', '0');

-- update-2012-10-30.sql
alter table billcenter add primary key billcenter_code(billcenter_code);

-- update-2012-10-31.sql
alter table validations change `maxValue` `maxValue1` double;

-- update-2012-11-03.sql PHC moved to procedure
-- alter table formONAREnhanced change provider_no provider_no varchar(10) default NULL;

-- update-2012-11-05.sql 
alter table admission change clientstatus_id clientstatus_id bigint(20) default NULL;


-- update-2012-11-06.sql 
update admission set clientstatus_id=NULL where clientstatus_id=0;
update admission set team_id=NULL where team_id=0;
alter table quickListUser change providerNo providerNo varchar(20) not null;
alter table secUserRole change role_name role_name varchar(60) not null;



-- update-2012-11-09.sql  ...in part
CREATE TABLE IF NOT EXISTS `vacancy_client_match` (
  `match_id` int NOT NULL AUTO_INCREMENT,
  `vacancy_id` int(11),
  `client_id` int(11),
  `contact_attempts` int(11),
  `last_contact_date` datetime,
  `status` varchar(30),
  `rejection_reason` text,
  `form_id` int(10),
  `match_percent` double,
  PRIMARY KEY (`match_id`),
 UNIQUE KEY `vacancy_id` (`vacancy_id`,`client_id`,`form_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


-- update-2012-11-11.sql 
alter table vacancy_template change name name varchar(100) not null;
insert into `secObjectName` (`objectName`) values ('_pmm_editProgram.vacancies');
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.vacancies','x',0,'999998');

-- update-2012-11-14.sql 
CREATE TABLE IF NOT EXISTS `OscarCode` (
      id int(10) NOT NULL auto_increment primary key,
      OscarCode varchar(25) not null,
      description varchar(255)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


-- update-2012-11-21.sql 
delete from OcanFormOption where id=1276;
insert into OcanConnexOption values (558, '7','Toronto Central','Fred Victor Centre','4292','Fred Victor Centre','6006');

-- update-2012-11-27.sql 
INSERT INTO `OscarCode` VALUES (1,'CKDSCREEN','Ckd Screening');

-- update-2012-12-01.sql 
alter table DemographicContact add consentToContact tinyint(1);
alter table DemographicContact add active tinyint(1);
update DemographicContact set consentToContact=1 where consentToContact is null;
update DemographicContact set active=1 where active is null;

-- update-2012-12-05.sql 
update measurements set type='ACR' where type='UACR';

-- update-2012-12-07.sql 
alter table billinginr change provider_no provider_no varchar(10);
update program set defaultServiceRestrictionDays=0 where defaultServiceRestrictionDays is null;

-- update-2012-12-16.sql 
CREATE TABLE IF NOT EXISTS `Institution` (
	id int PRIMARY KEY NOT NULL auto_increment,
	name varchar(255) not null,
	address varchar(255),
	city varchar(100),
	province varchar(100),
	postal varchar(10),
	country varchar(25),
	phone varchar(25),
	fax varchar(25),
	website varchar(100),
	email varchar(50),
	annotation text
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `Department` (
        id int PRIMARY KEY NOT NULL auto_increment,
        name varchar(255) not null,
	annotation text
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


CREATE TABLE IF NOT EXISTS `InstitutionDepartment` (
	institutionId int not null,
	departmentId int not null
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- update-2012-12-18.sql 
CREATE TABLE IF NOT EXISTS `casemgmt_note_lock` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `session_id` varchar(255),
  `ip_address` varchar(15),
  `provider_no` varchar(6),  
  `note_id` int(10),
  `demographic_no` int(10),
  `lock_acquired` datetime,
  PRIMARY KEY (`id`),
  KEY `casemgmt_note_lock_providerNo` (`provider_no`),
  KEY `casemgmt_note_lock_note_id` (`note_id`),
  KEY `casemgmt_note_lock_providerNo_noteId` (`provider_no`,`note_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- update-2012-12-23.sql 
alter table Facility add enablePhoneEncounter tinyint(1) unsigned NOT NULL;


-- update-2012-12-27.sql 

INSERT INTO secObjectName VALUES('_caseload.DisplayMode',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Age',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Sex',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.LastAppt',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.NextAppt',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.ApptsLYTD',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Lab',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Doc',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Tickler',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Msg',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.BMI',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.BP',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.WT',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.SMK',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.A1C',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.ACR',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.SCR',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.LDL',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.HDL',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.TCHD',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.EGFR',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.EYEE',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.LastEncounterDate',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.LastEncounterType',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.CashAdmissionDate',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Access1AdmissionDate',NULL,0);

INSERT INTO secObjPrivilege VALUES('doctor','_caseload.DisplayMode','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Age','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Sex','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.LastAppt','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.NextAppt','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.ApptsLYTD','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Lab','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Doc','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Tickler','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Msg','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.BMI','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.BP','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.WT','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.SMK','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.A1C','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.ACR','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.LDL','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.HDL','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.TCHD','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.EGFR','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.EYEE','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.LastEncounterDate','o',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.LastEncounterType','o',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.CashAdmissionDate','o',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Access1AdmissionDate','o',0,'999998');

-- update-2012-12-31.sql 
alter table client_referral add `select_vacancy` varchar(255);

-- update-2013-01-10.sql 
alter table professionalSpecialists add institutionId int(10) not null;
alter table professionalSpecialists add departmentId int(10) not null;

-- update-2013-01-13.sql already changed in the table def
-- alter table vacancy change dateCreated dateCreated datetime not null;

-- update-2013-01-14.sql
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Correctional/Probational Facility','Correctional/Probational Facility','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Domiciliary Hospital','Domiciliary Hospital','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'General Hospital','General Hospital','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Psychiatric Hospital','Psychiatric Hospital','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Other Specialty Hospital','Other Specialty Hospital','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'No fixed address','No fixed address','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Hostel/Shelter','Hostel/Shelter','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Long-Term Care Facility/Nursing Home','Long-Term Care Facility/Nursing Home','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Municipal Non-Profit Housing','Municipal Non-Profit Housing','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Private House/Apt.- Client Owned/Market','Private House/Apt.- Client Owned/Market','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Rent','Rent','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Private House/Apt.- Other/Subsidized','Private House/Apt.- Other/Subsidized','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Retirement Home/Seniors\' Residence','Retirement Home/Seniors\' Residence','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Rooming/Boarding House','Rooming/Boarding House','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Supportive Housing - Congregate Living','Supportive Housing - Congregate Living','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Supportive Housing - Assisted Living (RTF 24 Hr Home and Group Homes)','Supportive Housing - Assisted Living (RTF 24 Hr Home and Group Homes)','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Private Non-Profit Housing','Private Non-Profit Housing','community',99999,0,0,0,0,'active',1,0,0);
INSERT INTO `program` (facilityId,name,description,type,maxAllowed,holdingTank,allowBatchAdmission,allowBatchDischarge,hic,programStatus,userDefined, enableEncounterTime, enableEncounterTransportationTime) VALUES (1,'Approved Homes & Homes for Special Care','Approved Homes & Homes for Special Care','community',99999,0,0,0,0,'active',1,0,0);

-- update-2013-01-15.1.sql
alter table Facility add registrationIntake int(8);

-- update-2013-01-15.sql
alter table client_referral add vacancy_id int(11);

-- update-2013-01-24.sql
-- PHC deletion of duplicate add registrationIntake  --

-- update-2013-01-28.sql 
alter table criteria modify CAN_BE_ADHOC int(1) not null;

-- update-2013-01-29.sql 
alter table vacancy_template drop column PROGRAM_ID;
alter table Facility add displayAllVacancies int(1) not null;


-- update-2013-01-30.1.sql 
alter table vacancy_client_match add proportion varchar(8);

-- update-2013-01-30.sql 
CREATE TABLE IF NOT EXISTS `default_issue` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `assigned_time` datetime NOT NULL,
  `issue_ids` text,
  `provider_no` varchar(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


-- measurements 

-- update-2013-02-02.sql moved into procedure
-- alter table formLabReq10 add letterhead varchar(25);

-- update-2013-02-05.1.sql 
alter table demographic change month_of_birth month_of_birth varchar(2);
alter table demographic change date_of_birth date_of_birth varchar(2);

-- update-2013-02-05.sql 
alter table measurementsDeleted change comments comments varchar(255);
alter table measurementsDeleted change dateObserved dateObserved datetime;

alter table measurements change appointmentNo appointmentNo int(10);

-- update-2013-02-06.sql 
update hl7TextInfo set priority = null where length(priority) = 0;
update hl7TextInfo set result_status = null where length(result_status) = 0;


-- update-2013-02-10.sql 
-- PHC ish, as the table doesn't grep create it whole cloth

CREATE TABLE IF NOT EXISTS `table_modification` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(10) DEFAULT NULL,
  `provider_no` varchar(6) NOT NULL DEFAULT '',
  `modification_date` datetime DEFAULT NULL,
  `modification_type` varchar(20) DEFAULT NULL,
  `table_name` varchar(255) DEFAULT NULL,
  `row_id` varchar(20) DEFAULT NULL,
  `resultSet` text,
  PRIMARY KEY (`id`),
  KEY `table_modification_demographic_no` (`demographic_no`),
  KEY `table_modification_provider_no` (`provider_no`),
  KEY `table_modification_modification_type` (`modification_type`(10))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

alter table table_modification change demographic_no demographic_no int(10);


-- update-2013-02-11
update messagetbl set pdfattachment = null where pdfattachment = 'null';


-- update-2013-02-13.sql
update messagetbl set pdfattachment = null where pdfattachment = 'null';

-- update-2013-02-14.sql
alter table measurementType change type type varchar(50) not null;
alter table measurementTypeDeleted change type type varchar(50) not null;
alter table billingservice change sliFlag sliFlag TINYINT(1) NOT NULL default 0;

--  PHC skipping billingnote as its BC only --
-- alter table billingnote change note note text;


-- update-2013-02-22.sql
alter table Facility add assignNewVacancyTicklerProvider varchar(25);
alter table Facility add assignNewVacancyTicklerDemographic int(10);

-- update-2013-02-23.sql
alter table Facility add assignRejectedVacancyApplicant varchar(25);

-- update-2013-02-25.sql 
alter table Facility add vacancyWithdrawnTicklerProvider varchar(25);
alter table Facility add vacancyWithdrawnTicklerDemographic int(10);

alter table vacancy add statusUpdateUser varchar(25);
alter table vacancy add statusUpdateDate datetime;

-- update-2013-03-06.sql -
ALTER TABLE default_issue ADD update_time DATETIME NOT NULL;


-- update-2013-03-09.sql --
update OcanFormOption set ocanDataCategoryValue='C&amp;T' where ocanDataCategoryValue='C&T';

-- update-2013-04-19.sql --
-- PHC fix we have gstFlag in deb 12.1.1 so delete it --
-- alter table billingservice add gstFlag tinyint(1) NOT NULL default 0;

-- update-2013-04-23.sql --
update allergies set archived='0' where archived is null;
update allergies set archived='0' where archived='n';
update allergies set archived='0' where archived='N';
update allergies set archived='0' where archived='f';
update allergies set archived='0' where archived='F';
update allergies set archived='1' where archived<>'0';
alter table allergies change archived archived tinyint(1) not null;

-- update-2013-04-29.sql --
ALTER TABLE demographicQueryFavourites ADD demoIds text;

-- update-2013-05-01.sql --
-- PHC ??? we already have table demographicExtArchive

CREATE TABLE IF NOT EXISTS `demographicExtArchive` (
  `id` int(10) NOT NULL auto_increment,
  `archiveId` bigint(20),
  `demographic_no` int(10),
  `provider_no` varchar(6),
  `key_val` varchar(64),
  `value` text,
  `date_time` datetime,
  `hidden` char(1),
  PRIMARY KEY  (`id`),
  INDEX (demographic_no)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- update-2013-05-02-bc.sql --
-- PHC we already have table labPatientPhysicianInfo add to bc
CREATE TABLE IF NOT EXISTS `labPatientPhysicianInfo` (
  id int(10) NOT NULL auto_increment primary key,
  labReportInfo_id int(10),
  accession_num varchar(64),
  physician_account_num varchar(30),
  service_date varchar(10),
  patient_first_name varchar(100),
  patient_last_name varchar(100),
  patient_sex char(1),
  patient_health_num varchar(20),
  patient_dob varchar(15),
  lab_status char(1),
  doc_num varchar(50),
  doc_name varchar(100),
  doc_addr1 varchar(100),
  doc_addr2 varchar(100),
  doc_addr3 varchar(100),
  doc_postal varchar(15),
  doc_route varchar(50),
  comment1 text,
  comment2 text,
  patient_phone varchar(20),
  doc_phone varchar(20),
  collection_date varchar(20)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


-- Migrate to InnoDB storage engine
-- InnoDB is necessary for sharing center that uses foreign keys
-- PHC amended for schema 15

DROP PROCEDURE IF EXISTS MIGRATE_TO_INNODB;
DELIMITER //
CREATE PROCEDURE MIGRATE_TO_INNODB(in_schema varchar(20))
BEGIN
	DECLARE done INT DEFAULT FALSE;  
	DECLARE tblName varchar(50);  
	DECLARE cur CURSOR FOR SELECT table_name FROM information_schema.tables WHERE table_schema=in_schema and Table_TYPE='BASE TABLE';  
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	OPEN cur;  
	read_loop: LOOP  
	FETCH cur INTO tblName;  
	IF done THEN  
		LEAVE read_loop;  
	END IF;
	IF tblName NOT IN ('formRourke2009','formONANEnhanced') THEN  
		set @stmt = Concat( 'ALTER table `' , in_schema, '`.`' , tblName, '` engine=INNODB');  
		prepare stmt1 from @stmt;   
		execute stmt1;  
	END IF; 
	END LOOP;  
	CLOSE cur;  
END//
DELIMITER ;
CALL MIGRATE_TO_INNODB("tempo");
DROP PROCEDURE MIGRATE_TO_INNODB;


-- update-2013-05-02.sql --
-- Move archive records from demographicExt to demographicExtArchive table

-- PHC probably ok
-- but may fail due to Thread stack overrun
-- for 64bit use tread_stack=256K in /etc/mysql/my.cnf

DROP PROCEDURE IF EXISTS MIGRATE_DEMOGRAPHIC_EXT;

DELIMITER //

CREATE PROCEDURE MIGRATE_DEMOGRAPHIC_EXT(keepRecord BOOLEAN)
BEGIN
    DECLARE demographicExtId int;
    DECLARE no_more_result int;

    -- Cursor for archiving
    DECLARE  curArchive CURSOR FOR
        SELECT  ext.id
        FROM    demographicExt ext,
                (SELECT demographic_no,
                        provider_no   ,
                        key_val       ,
                        max(date_time) date_time
                 FROM   demographicExt
-- PHC mod, sometimes the provider varies
--                 GROUP  BY demographic_no, provider_no, key_val) latest
                 GROUP  BY demographic_no, key_val) latest
        WHERE    ext.demographic_no = latest.demographic_no
-- PHC mod, sometimes the provider varies
--        AND      ext.provider_no    = latest.provider_no
        AND      ext.key_val        = latest.key_val
        AND      ext.date_time      < latest.date_time;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more_result = 1;

    -- Archive records (Move from DemographicExt to DemographicExtArchive)
    SET no_more_result = 0;
    OPEN curArchive;
    REPEAT
        FETCH curArchive INTO demographicExtId;
        IF no_more_result = 0 THEN
            INSERT INTO demographicExtArchive (
                    demographic_no,
                    provider_no   ,
                    key_val       ,
                    value         ,
                    date_time     ,
                    hidden        )
            SELECT  demographic_no,
                    provider_no   ,
                    key_val       ,
                    value         ,
                    date_time     ,
                    hidden
            FROM    demographicExt
            WHERE   id = demographicExtId;
        END IF;
    UNTIL no_more_result
    END REPEAT;
    CLOSE curArchive;

    -- Delete archived records
    IF keepRecord = FALSE THEN
	    SET no_more_result = 0;
	    OPEN curArchive;
	    REPEAT
	        FETCH curArchive INTO demographicExtId;
	        IF no_more_result = 0 THEN
	            DELETE  FROM demographicExt
	            WHERE   id = demographicExtId;
	        END IF;
	    UNTIL no_more_result
	    END REPEAT;
	    CLOSE curArchive;
    END IF;


END//

DELIMITER ;

CALL MIGRATE_DEMOGRAPHIC_EXT(FALSE);

DROP PROCEDURE MIGRATE_DEMOGRAPHIC_EXT;

ALTER TABLE demographicExt ADD CONSTRAINT uk_demo_ext UNIQUE (demographic_no, key_val);

--  PHC the key uk_demo_ext fails if you have two entries with different providers
--  The procedure changed to allow for that
--  also it fails if there is the same providers but different date_time
--  eg demographic 10162 duplicate rxInteractionWarningLevel with same date_time!!
--  alas -- 
--  TODO -- fix Done

-- update-2013-05-04 moved to procedure

-- update-2013-05-06.sql --
update measurementType set validation='4' where type='EGFR';


-- update-2013-05-07.sql --
INSERT INTO `validations` (`name`, `regularExp`, `maxValue1`, `minValue`, `maxLength`, `minLength`, `isNumeric`, `isTrue`, `isDate`) VALUES
('Yes/No/Maybe', 'YES|yes|Yes|Y|NO|no|No|N|MAYBE|maybe|Maybe', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

insert into encounterForm (`form_name`, `form_value`, `form_table`, `hidden`) values('Patient Encounter Worksheet','../form/patientEncounterWorksheet.jsp?demographic_no=','',0);

-- update-2013-05-08.sql --
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'KEEL', 'Keele Score', 'Keele Score', 'null', '2', '2013-05-07 13:00:00'),
( 'PSQS', 'PSQ3 Sleep Score', 'PSQ3 Sleep Score', 'null', '5', '2013-05-07 00:00:00'),
( 'PTSD', 'PC PTSD Trauma Score', 'PC PTSD Trauma Score', 'null', '2', '2013-05-07 00:00:00'),
( 'PHQS', 'PHQ4 Depression Anxiety Score', 'PHQ4 Depression Anxiety Score', 'null', '3', '2013-05-07 00:00:00'),
( 'DNFS', 'DN4 Questionnaire', 'DN4 Questionnaire', 'null', '2', '2013-05-07 00:00:00'),
( 'NERF', 'Neuropathic Features?', 'Neuropathic Features?', 'null', '15', '2013-05-07 00:00:00'),
( 'BPI', 'Brief Pain Index -Short Form', 'Brief Pain Index -Short Form', 'null', '5', '2013-05-07 00:00:00');


DELIMITER $$

DROP PROCEDURE IF EXISTS upgrade_ON_and_BC $$
CREATE PROCEDURE upgrade_ON_and_BC()
BEGIN

IF EXISTS(SELECT 'formONAREnhanced' 
            FROM information_schema.TABLES
           WHERE table_schema = 'oscar_15'
             AND table_name LIKE 'formONAREnhanced')
THEN
     ALTER TABLE formONAREnhanced change provider_no provider_no varchar(10) default NULL;
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments1 pg2_comments1 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments2 pg2_comments2 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments3 pg2_comments3 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments4 pg2_comments4 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments5 pg2_comments5 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments6 pg2_comments6 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments7 pg2_comments7 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments8 pg2_comments8 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments9 pg2_comments9 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments10 pg2_comments10 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments11 pg2_comments11 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments12 pg2_comments12 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments13 pg2_comments13 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments14 pg2_comments14 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments15 pg2_comments15 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments16 pg2_comments16 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments17 pg2_comments17 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments18 pg2_comments18 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments19 pg2_comments19 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments20 pg2_comments20 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments21 pg2_comments21 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments22 pg2_comments22 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments23 pg2_comments23 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments24 pg2_comments24 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments25 pg2_comments25 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments26 pg2_comments26 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments27 pg2_comments27 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments28 pg2_comments28 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments29 pg2_comments29 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments30 pg2_comments30 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments31 pg2_comments31 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments32 pg2_comments32 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments33 pg2_comments33 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments34 pg2_comments34 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments35 pg2_comments35 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments36 pg2_comments36 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments37 pg2_comments37 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments38 pg2_comments38 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments39 pg2_comments39 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments40 pg2_comments40 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments41 pg2_comments41 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments42 pg2_comments42 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments43 pg2_comments43 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments44 pg2_comments44 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments45 pg2_comments45 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments46 pg2_comments46 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments47 pg2_comments47 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments48 pg2_comments48 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments49 pg2_comments49 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments50 pg2_comments50 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments51 pg2_comments51 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments52 pg2_comments52 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments53 pg2_comments53 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments54 pg2_comments54 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments55 pg2_comments55 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments56 pg2_comments56 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments57 pg2_comments57 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments58 pg2_comments58 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments59 pg2_comments59 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments60 pg2_comments60 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments61 pg2_comments61 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments62 pg2_comments62 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments63 pg2_comments63 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments64 pg2_comments64 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments65 pg2_comments65 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments66 pg2_comments66 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments67 pg2_comments67 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments68 pg2_comments68 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments69 pg2_comments69 varchar(80);
     ALTER TABLE formONAREnhanced CHANGE  pg2_comments70 pg2_comments70 varchar(88);
-- update-2013-10-07.sql and sundry ON hacks--
ALTER TABLE formONAREnhanced add pg1_geneticA_riskLevel varchar(25);
ALTER TABLE formONAREnhanced add pg1_geneticB_riskLevel varchar(25);
ALTER TABLE formONAREnhanced add pg1_geneticC_riskLevel varchar(25);
ALTER TABLE formONAREnhanced add pg1_labCustom3Result_riskLevel varchar(25);
END IF;

IF EXISTS(SELECT 'billing_on_eareport' 
            FROM information_schema.TABLES
           WHERE table_schema = 'oscar_15'
             AND table_name LIKE 'billing_on_eareport')
THEN
ALTER TABLE billing_on_eareport MODIFY admitted_date date;
END IF;

IF EXISTS(SELECT 'billing_on_ext' 
            FROM information_schema.TABLES
           WHERE table_schema = 'oscar_15'
             AND table_name LIKE 'billing_on_ext')
THEN
ALTER TABLE billing_on_ext ADD column payment_id int(10);
END IF;

IF EXISTS(SELECT 'formLabReq10' 
            FROM information_schema.TABLES
           WHERE table_schema = 'oscar_15'
             AND table_name LIKE 'formLabReq10')
THEN
ALTER TABLE formLabReq10 ADD letterhead varchar(25);
ALTER TABLE formLabReq10 add patientChartNo varchar(20);
END IF;

IF EXISTS(SELECT 'formLabReq07' 
            FROM information_schema.TABLES
           WHERE table_schema = 'oscar_15'
             AND table_name LIKE 'formLabReq07')
THEN
ALTER TABLE formLabReq07 add patientChartNo varchar(20);
END IF;

IF EXISTS(SELECT 'billing_on_errorCode' 
            FROM information_schema.TABLES
           WHERE table_schema = 'oscar_15'
             AND table_name LIKE 'billing_on_errorCode')
THEN
INSERT INTO billing_on_errorCode (code, description) VALUES ('ARF', 'Technical service code requires referring physician') ON DUPLICATE KEY UPDATE description='Technical service code requires referring physician';
END IF;

IF EXISTS(SELECT 'formBCNewBorn2008' 
            FROM information_schema.TABLES
           WHERE table_schema = 'oscar_15'
             AND table_name LIKE 'formBCNewBorn2008')
THEN
   ALTER TABLE formBCNewBorn2008 CHANGE `MothersName` `MothersName` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `MothersAge` `MothersAge` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `LastName` `LastName` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `MothersHospitalID` `MothersHospitalID` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `SurNewBorn` `SurNewBorn` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `FathersName` `FathersName` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `FathersAge` `FathersAge` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `G` `G` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `T` `T` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `P` `P` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `A` `A` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `L` `L` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `EDD` `EDD` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `BloodGroup` `BloodGroup` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Rh` `Rh` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Antibodies` `Antibodies` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `ExposuretoSubstancesOtherText` `ExposuretoSubstancesOtherText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `ExposuretoSubstancesOtherRisk` `ExposuretoSubstancesOtherRisk` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `HeartRate1min` `HeartRate1min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `HeartRate5min` `HeartRate5min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `HeartRate10min` `HeartRate10min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Resp1min` `Resp1min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Resp5min` `Resp5min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Resp10min` `Resp10min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `MuscleTone1min` `MuscleTone1min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `MuscleTone5min` `MuscleTone5min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `MuscleTone10min` `MuscleTone10min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `ResptoStim1min` `ResptoStim1min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `ResptoStim5min` `ResptoStim5min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `ResptoStim10min` `ResptoStim10min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Colour1min` `Colour1min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Colour5min` `Colour5min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Colour10min` `Colour10min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Total1min` `Total1min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Total5min` `Total5min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Total10min` `Total10min` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `PositionedOtherText` `PositionedOtherText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `OxygenStart` `OxygenStart` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `OxygenStop` `OxygenStop` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `OxygenIPPVStart` `OxygenIPPVStart` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `OxygenIPPVStop` `OxygenIPPVStop` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section3Tempeature` `Section3Tempeature` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section3HeartRate` `Section3HeartRate` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section3HeartRateMin` `Section3HeartRateMin` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section3HeartRateSec` `Section3HeartRateSec` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section3SigRMRN1` `Section3SigRMRN1` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section3SigRMRN2` `Section3SigRMRN2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section3SigMD` `Section3SigMD` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section3Respirations` `Section3Respirations` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section3MinTimetoSpontaneousBrathingMin` `Section3MinTimetoSpontaneousBrathingMin` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section3MinTimetoSpontaneousBrathingSec` `Section3MinTimetoSpontaneousBrathingSec` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section4Birthdate` `Section4Birthdate` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section4time` `Section4time` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section4NewBornHospital` `Section4NewBornHospital` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section4DeliveryType` `Section4DeliveryType` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section4Sig1` `Section4Sig1` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section4Sig2` `Section4Sig2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section5EyeProphylaxisOtherText` `Section5EyeProphylaxisOtherText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section5EyeProphylaxisTime` `Section5EyeProphylaxisTime` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section5sig1` `Section5sig1` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section5VitaminKDosage` `Section5VitaminKDosage` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section5VitaminKSite` `Section5VitaminKSite` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section5VitaminKTime` `Section5VitaminKTime` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section5VitaminKSig2` `Section5VitaminKSig2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section6BirthweightG` `Section6BirthweightG` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section6BirthweightPercent` `Section6BirthweightPercent` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section6LengthCM` `Section6LengthCM` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section6LengthPercent` `Section6LengthPercent` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section6HeadCircumferenceCM` `Section6HeadCircumferenceCM` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section6HeadCircumferencePercent` `Section6HeadCircumferencePercent` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section7ObviousAnomalyText` `Section7ObviousAnomalyText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section7umbilicalCM` `Section7umbilicalCM` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8GestationalAgeAntenatalHistory` `Section8GestationalAgeAntenatalHistory` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8GestationalAgeByExam` `Section8GestationalAgeByExam` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8GeneralApperanceComments` `Section8GeneralApperanceComments` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8SkinText` `Section8SkinText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8HeadText` `Section8HeadText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8EENTText` `Section8EENTText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8RespiratoryText` `Section8RespiratoryText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8CVSText` `Section8CVSText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8AbdomenText` `Section8AbdomenText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8UmbilicalCordText` `Section8UmbilicalCordText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8GenitorectalText` `Section8GenitorectalText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8MusculoSkeletaltext` `Section8MusculoSkeletaltext` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8NeurologicalText` `Section8NeurologicalText` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section12Other` `Section12Other` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8DATE` `Section8DATE` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8TIME` `Section8TIME` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section8SIGNATURE` `Section8SIGNATURE` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9Date` `Section9Date` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9Comment` `Section9Comment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9OtherComment` `Section9OtherComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9PrintName` `Section9PrintName` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9Signature` `Section9Signature` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10Date` `Section10Date` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningAge` `Section10MetabolicScreeningAge` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningComment` `Section10MetabolicScreeningComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningBilirubinScreeningAge` `Section10MetabolicScreeningBilirubinScreeningAge` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningBilirubinScreeningComment` `Section10MetabolicScreeningBilirubinScreeningComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section11Date` `Section11Date` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section11Comment` `Section11Comment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section12Date` `Section12Date` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section12Comment` `Section12Comment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section12Supplementation` `Section12Supplementation` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section13Note` `Section13Note` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section13Note2` `Section13Note2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section13Note3` `Section13Note3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date1` `Section14Date1` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date2` `Section14Date2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date3` `Section14Date3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date4` `Section14Date4` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date5` `Section14Date5` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date6` `Section14Date6` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date7` `Section14Date7` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date8` `Section14Date8` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes1` `Section14ProgressNotes1` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes2` `Section14ProgressNotes2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes3` `Section14ProgressNotes3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes4` `Section14ProgressNotes4` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes5` `Section14ProgressNotes5` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes6` `Section14ProgressNotes6` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes7` `Section14ProgressNotes7` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes8` `Section14ProgressNotes8` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15HC` `Section15HC` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15Weight` `Section15Weight` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15WeightLoss` `Section15WeightLoss` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15GeneralComment` `Section15GeneralComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15SkinComment` `Section15SkinComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15HeadComment` `Section15HeadComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15EENTComment` `Section15EENTComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15RespiratoryComment` `Section15RespiratoryComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15CVSComment` `Section15CVSComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15AbdomenComment` `Section15AbdomenComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15UnbilicalCordComment` `Section15UnbilicalCordComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15GenitorectalComment` `Section15GenitorectalComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15MusculoskeletalComment` `Section15MusculoskeletalComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15NeurologicalComment` `Section15NeurologicalComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15OtherComment` `Section15OtherComment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Date` `Date` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `SIGNATURE` `SIGNATURE` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section16Comment` `Section16Comment` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `ProblemsrequiringFollowup` `ProblemsrequiringFollowup` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section17OtherHospitalSpecify` `Section17OtherHospitalSpecify` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18FamilyPhsicianDate` `Section18FamilyPhsicianDate` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18MidwifeDate` `Section18MidwifeDate` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18PediatricianDate` `Section18PediatricianDate` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18OtherConsultantDate` `Section18OtherConsultantDate` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18PublicHealthNurseDate` `Section18PublicHealthNurseDate` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18MinistryforChildernDate` `Section18MinistryforChildernDate` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `c_hospitalName` `c_hospitalName` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9DatePg2` `Section9DatePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9CommentPg2` `Section9CommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9OtherCommentPg2` `Section9OtherCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9PrintNamePg2` `Section9PrintNamePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9SignaturePg2` `Section9SignaturePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10DatePg2` `Section10DatePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningAgePg2` `Section10MetabolicScreeningAgePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningCommentPg2` `Section10MetabolicScreeningCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningBilirubinScreeningAgePg2` `Section10MetabolicScreeningBilirubinScreeningAgePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningBilirubinScreeningCommentPg2` `Section10MetabolicScreeningBilirubinScreeningCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section11DatePg2` `Section11DatePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section11CommentPg2` `Section11CommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section12DatePg2` `Section12DatePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section12CommentPg2` `Section12CommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section12SupplementationPg2` `Section12SupplementationPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section13NotePg2` `Section13NotePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section13Note2Pg2` `Section13Note2Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section13Note3Pg2` `Section13Note3Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date1Pg2` `Section14Date1Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date2Pg2` `Section14Date2Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date3Pg2` `Section14Date3Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date4Pg2` `Section14Date4Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date5Pg2` `Section14Date5Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date6Pg2` `Section14Date6Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date7Pg2` `Section14Date7Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date8Pg2` `Section14Date8Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes1Pg2` `Section14ProgressNotes1Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes2Pg2` `Section14ProgressNotes2Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes3Pg2` `Section14ProgressNotes3Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes4Pg2` `Section14ProgressNotes4Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes5Pg2` `Section14ProgressNotes5Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes6Pg2` `Section14ProgressNotes6Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes7Pg2` `Section14ProgressNotes7Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes8Pg2` `Section14ProgressNotes8Pg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15HCPg2` `Section15HCPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15WeightPg2` `Section15WeightPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15WeightLossPg2` `Section15WeightLossPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15GeneralCommentPg2` `Section15GeneralCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15SkinCommentPg2` `Section15SkinCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15HeadCommentPg2` `Section15HeadCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15EENTCommentPg2` `Section15EENTCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15RespiratoryCommentPg2` `Section15RespiratoryCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15CVSCommentPg2` `Section15CVSCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15AbdomenCommentPg2` `Section15AbdomenCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15UnbilicalCordCommentPg2` `Section15UnbilicalCordCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15GenitorectalCommentPg2` `Section15GenitorectalCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15MusculoskeletalCommentPg2` `Section15MusculoskeletalCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15NeurologicalCommentPg2` `Section15NeurologicalCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15OtherCommentPg2` `Section15OtherCommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `DatePg2` `DatePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `SIGNATUREPg2` `SIGNATUREPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section16CommentPg2` `Section16CommentPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `ProblemsrequiringFollowupPg2` `ProblemsrequiringFollowupPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section17OtherHospitalSpecifyPg2` `Section17OtherHospitalSpecifyPg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18FamilyPhsicianDatePg2` `Section18FamilyPhsicianDatePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18MidwifeDatePg2` `Section18MidwifeDatePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18PediatricianDatePg2` `Section18PediatricianDatePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18OtherConsultantDatePg2` `Section18OtherConsultantDatePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18PublicHealthNurseDatePg2` `Section18PublicHealthNurseDatePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18MinistryforChildernDatePg2` `Section18MinistryforChildernDatePg2` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9DatePg3` `Section9DatePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9CommentPg3` `Section9CommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9OtherCommentPg3` `Section9OtherCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9PrintNamePg3` `Section9PrintNamePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section9SignaturePg3` `Section9SignaturePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10DatePg3` `Section10DatePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningAgePg3` `Section10MetabolicScreeningAgePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningCommentPg3` `Section10MetabolicScreeningCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningBilirubinScreeningAgePg3` `Section10MetabolicScreeningBilirubinScreeningAgePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section10MetabolicScreeningBilirubinScreeningCommentPg3` `Section10MetabolicScreeningBilirubinScreeningCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section11DatePg3` `Section11DatePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section11CommentPg3` `Section11CommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section12DatePg3` `Section12DatePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section12CommentPg3` `Section12CommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section12SupplementationPg3` `Section12SupplementationPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section13NotePg3` `Section13NotePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section13Note2Pg3` `Section13Note2Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section13Note3Pg3` `Section13Note3Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date1Pg3` `Section14Date1Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date2Pg3` `Section14Date2Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date3Pg3` `Section14Date3Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date4Pg3` `Section14Date4Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date5Pg3` `Section14Date5Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date6Pg3` `Section14Date6Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date7Pg3` `Section14Date7Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14Date8Pg3` `Section14Date8Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes1Pg3` `Section14ProgressNotes1Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes2Pg3` `Section14ProgressNotes2Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes3Pg3` `Section14ProgressNotes3Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes4Pg3` `Section14ProgressNotes4Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes5Pg3` `Section14ProgressNotes5Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes6Pg3` `Section14ProgressNotes6Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes7Pg3` `Section14ProgressNotes7Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section14ProgressNotes8Pg3` `Section14ProgressNotes8Pg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15HCPg3` `Section15HCPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15WeightPg3` `Section15WeightPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15WeightLossPg3` `Section15WeightLossPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15GeneralCommentPg3` `Section15GeneralCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15SkinCommentPg3` `Section15SkinCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15HeadCommentPg3` `Section15HeadCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15EENTCommentPg3` `Section15EENTCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15RespiratoryCommentPg3` `Section15RespiratoryCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15CVSCommentPg3` `Section15CVSCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15AbdomenCommentPg3` `Section15AbdomenCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15UnbilicalCordCommentPg3` `Section15UnbilicalCordCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15GenitorectalCommentPg3` `Section15GenitorectalCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15MusculoskeletalCommentPg3` `Section15MusculoskeletalCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15NeurologicalCommentPg3` `Section15NeurologicalCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section15OtherCommentPg3` `Section15OtherCommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `DatePg3` `DatePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `SIGNATUREPg3` `SIGNATUREPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section16CommentPg3` `Section16CommentPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `ProblemsrequiringFollowupPg3` `ProblemsrequiringFollowupPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section17OtherHospitalSpecifyPg3` `Section17OtherHospitalSpecifyPg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18FamilyPhsicianDatePg3` `Section18FamilyPhsicianDatePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18MidwifeDatePg3` `Section18MidwifeDatePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18PediatricianDatePg3` `Section18PediatricianDatePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18OtherConsultantDatePg3` `Section18OtherConsultantDatePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18PublicHealthNurseDatePg3` `Section18PublicHealthNurseDatePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Section18MinistryforChildernDatePg3` `Section18MinistryforChildernDatePg3` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `Pg2_formDate` `Pg2_formDate` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `c_surname` `c_surname` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `c_givenName` `c_givenName` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `c_address` `c_address` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `c_city` `c_city` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `c_province` `c_province` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `c_postal` `c_postal` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `c_phone` `c_phone` varchar(70);
   ALTER TABLE formBCNewBorn2008 CHANGE `c_phyMid` `c_phyMid` varchar(70);
-- from UPDATE 2013 08 23 BC Only
   INSERT INTO billingstatus_types VALUES ('I', 'ICBC', 'ICBC', 17);
END IF;

END $$

CALL upgrade_ON_and_BC() $$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `CreateIndex` $$
CREATE PROCEDURE `CreateIndex`
(
    given_database VARCHAR(64),
    given_table    VARCHAR(64),
    given_index    VARCHAR(64),
    given_columns  VARCHAR(64)
)
theStart:BEGIN

    DECLARE TableIsThere INTEGER;
    DECLARE IndexIsThere INTEGER;

    SELECT COUNT(1) INTO TableIsThere
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE table_schema = given_database
    AND   table_name   = given_table;

    IF TableIsThere = 0 THEN
        SELECT CONCAT(given_database,'.',given_table, 
	' does not exist.  Unable to add ', given_index) CreateIndexMessage;
	LEAVE theStart;
    ELSE

	    SELECT COUNT(1) INTO IndexIsThere
	    FROM INFORMATION_SCHEMA.STATISTICS
	    WHERE table_schema = given_database
	    AND   table_name   = given_table
	    AND   index_name   = given_index;

	    IF IndexIsThere = 0 THEN
		SET @sqlstmt = CONCAT('CREATE INDEX ',given_index,' ON ',
		given_database,'.',given_table,' (',given_columns,')');
		PREPARE st FROM @sqlstmt;
		EXECUTE st;
		DEALLOCATE PREPARE st;
	    ELSE
		SELECT CONCAT('Index ',given_index,' Already Exists ON Table ',
		given_database,'.',given_table) CreateIndexMessage;
	    END IF;

	END IF;

END $$

DELIMITER ;

-- INSERT DEFERED INDICES from the following updates

-- update-2013-05-14.sql 
CALL CreateIndex('oscar_15', 'scheduledate', 'scheduledate_sdate', 'sdate');
CALL CreateIndex('oscar_15', 'scheduledate', 'scheduledate_pno', 'provider_no');
CALL CreateIndex('oscar_15', 'scheduledate', 'scheduledate_status', 'status');
CALL CreateIndex('oscar_15', 'scheduledate', 'scheduledate_key1', 'sdate,provider_no,hour,status');
-- update-2014-12-29.sql
CALL CreateIndex('oscar_15', 'providerLabRouting', 'provider_lab_status_index', "`provider_no` ( 3 ) , `status`");

-- END INSERT DEFERED INDICES

-- update-2013-05-15.sql --
alter table `demographicQueryFavourites` change demoIds demoIds text;

-- update-2013-05-16.sql --
INSERT INTO icd9 (id, icd9, description) VALUES (15362,'338','PAIN NOT ELSEWHERE CLASSIFIED')
ON DUPLICATE KEY UPDATE description='PAIN NOT ELSEWHERE CLASSIFIED';

INSERT INTO icd9 (id, icd9, description) VALUES (15363,'338.2','CHRONIC PAIN')
ON DUPLICATE KEY UPDATE description='CHRONIC PAIN';

-- update-2013-05-23.sql --
ALTER TABLE document ADD contentdatetime datetime;
update document set contentdatetime=updatedatetime;

-- update-2013-06-03.sql --
alter table custom_filter add message varchar(255);

-- update-2013-06-10.sql --

CREATE TABLE IF NOT EXISTS `LookupList` (
	id int(11) NOT NULL AUTO_INCREMENT,
	name varchar(50) NOT NULL,
	description varchar(255),
	categoryId int,
	`active` tinyint(1) not null,
	createdBy varchar(8) not null,
	dateCreated timestamp not null,
	primary key(id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `LookupListItem` (
	id int(11) NOT NULL AUTO_INCREMENT,
	lookupListId int(11) not null,
	value varchar(50) NOT NULL,
	label varchar(255),
	displayOrder int not null,
	`active` tinyint(1) not null,
	createdBy varchar(8) not null,
	dateCreated timestamp not null,
	primary key(id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


-- update-2013-06-20.sql --  see the procedure that does this
-- INSERT INTO billing_on_errorCode (code, description) VALUES ('ARF', 'Technical service code requires referring physician') ON DUPLICATE KEY UPDATE description='Technical service code requires referring physician';

-- update-2013-06-26.sql --
CREATE TABLE IF NOT EXISTS `CtlRelationships` (
        id int(11) NOT NULL AUTO_INCREMENT,
        value varchar(50) NOT NULL,
        label varchar(255),
        `active` tinyint(1) not null,
	maleInverse varchar(50),
	femaleInverse varchar(50),
        primary key(id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Mother','Mother',true,'Son','Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Father','Father',true,'Son','Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Parent','Parent',true,'Son','Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Wife','Wife',true,'Husband','Partner');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Husband','Husband',true,'Partner','Wife');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Partner','Partner',true,'Partner','Partner');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Brother','Brother',true,'Brother','Sister');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Sister','Sister',true,'Brother','Sister');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Son','Son',true,'Father','Mother');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Daughter','Daughter',true,'Father','Mother');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Aunt','Aunt',true,'Nephew','Niece');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Uncle','Uncle',true,'Nephew','Niece');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Nephew','Nephew',true,'Uncle','Aunt');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Niece','Niece',true,'Uncle','Aunt');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('GrandFather','GrandFather',true,'GrandSon','GrandDaughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('GrandMother','GrandMother',true,'GrandSon','GrandDaughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Foster Parent','Foster Parent',true,'Foster Son','Foster Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Foster Son','Foster Son',true,'Foster Parent','Foster Parent');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Foster Daughter','Foster Daughter',true,'Foster Parent','Foster Parent');
insert into CtlRelationships (value,label,active) values ('Guardian','Guardian',true);
insert into CtlRelationships (value,label,active) values ('Next of Kin','Next of kin',true);
insert into CtlRelationships (value,label,active) values ('Administrative Staff','Administrative Staff',true);
insert into CtlRelationships (value,label,active) values ('Care Giver','Care Giver',true);
insert into CtlRelationships (value,label,active) values ('Power of Attorney','Power of Attorney',true);
insert into CtlRelationships (value,label,active) values ('Insurance','Insurance',true);
insert into CtlRelationships (value,label,active) values ('Guarantor','Guarantor',true);
insert into CtlRelationships (value,label,active) values ('Other','Other',true);



-- update-2013-07-04.sql --
alter table eform add column showLatestFormOnly boolean not null;
alter table eform modify patient_independent boolean not null;

alter table eform_data add column showLatestFormOnly boolean not null;


-- update-2013-07-08.sql --
/*This is an update to resolve duplicate measurement type="WT" with measuringInstruction="in BMI" for those users where this data exists

Before deleting the duplicate WT (in BMI) we should record it was deleted in measurementTypeDeleted. To do this here we look for more than one measurement type "WT" then look for the one with measuringInstruction equals "in BMI" and insert that record into measurementTypeDeleted */
INSERT INTO measurementTypeDeleted(type, typeDisplayName, typeDescription, measuringInstruction, validation, dateDeleted) SELECT mt.type, mt.typeDisplayName, mt.typeDescription, mt.measuringInstruction, mt.validation, NOW() FROM measurementType AS mt WHERE mt.type="WT" && measuringInstruction="in BMI" && exists(select * from measurementType where type='WT' GROUP BY type HAVING (COUNT(type) > 1));

/*to make sure we do not simply delete the only measurement type "WT" we again look for more than one "WT" and delete the one where the 
measuringInstruction = 'in BMI' */
delete from measurementType where type='WT' && measuringInstruction = 'in BMI' && exists(
	SELECT * FROM (
		select * from measurementType where type='WT' GROUP BY type HAVING (COUNT(type) > 1)
	) AS find
);


-- update-2013-07-25.sql --
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BPII', 'BPI Pain Severity', 'BPI Pain Severity', 'null', '2', '2013-07-25 13:00:00'),
( 'BPIS', 'BPI Pain Interference', 'BPI Pain Interference', 'null', '2', '2013-07-25 00:00:00');



-- update-2013-07-30.sql --
ALTER TABLE appointment ADD COLUMN reasonCode INT(11) NULL;

-- PHC modified to reflect top 10 diagnosis in general family practice
INSERT INTO LookupList(name, description, categoryId, active, createdBy, dateCreated) VALUES('reasonCode', 'Reason Code', null, 1, 'oscar', CURRENT_TIMESTAMP);

SET @lookupListId:=LAST_INSERT_ID();

INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Skin'                 , 'Skin'                 , 1 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Arthritis'                   , 'Arthritis'                   , 2 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Back'                           , 'Back'                           , 3 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Cholesterol'                     , 'Cholesterol'                     , 4 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'URTI'       , 'URTI'       , 5 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Mental Health'                   , 'Mental Health'                   , 6 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Neurolgic'                  , 'Neurologic'                  , 7 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Hypertension'                   , 'Hypertension'                   , 8 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Headache'               , 'Headache'               , 9 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Diabetes'                      , 'Diabetes'                      , 10, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Immunisation'                      , 'Immunisation'                      , 11, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Results'                       , 'Results'                       , 12, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Prescription'                       , 'Prescription Renewal'                       , 13, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Others'				 , 'Others'			   , 99, 1, 'oscar', CURRENT_TIMESTAMP);

COMMIT;


insert into FunctionalCentre values ('725 51 76 11','Peer/Self Help Initiatives');

-- update-2013-08-06.sql --
ALTER TABLE studydata ADD COLUMN deleted tinyint not null;
update studydata set deleted = 0;

alter table agency modify intake_indepth_state char(3) NOT NULL;
alter table agency modify intake_quick_state char(3) NOT NULL;

update agency set intake_indepth_state='HSC';
update agency set intake_quick_state='HSC';

-- update-2013-08-14.sql --
ALTER TABLE Facility ADD COLUMN enableCbiForm tinyint(1) not null;
ALTER TABLE OcanStaffForm ADD COLUMN lastNameAtBirth varchar(100);
ALTER TABLE OcanStaffForm ADD COLUMN estimatedAge varchar(3);

-- update-2013-08-15.sql --
CREATE TABLE  documentDescriptionTemplate (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  doctype varchar(60) NOT NULL,
  description varchar(255) NOT NULL,
  descriptionShortcut varchar(20) NOT NULL,
  provider_no varchar(6),
  lastUpdated timestamp NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('lab','Hematology','Hema',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('lab','Biochemistry','Bio',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('lab','ECG','ECG',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','Ultrasound','US',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','MRI','MRI',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','CT-SCAN','Scan',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','X-Ray','XRay',NULL);

-- update-2013-08-20.sql --
-- dupe? ALTER TABLE OcanStaffForm ADD COLUMN estimatedAge varchar(3);
insert into issue (code,description,role,update_date,type) values ('TicklerNote','Tickler Note', 'nurse',now(),'system');

-- update-2013-08-22.sql --
alter table appointment modify notes VARCHAR(255) ; 

-- update-2013-08-26.sql --
ALTER TABLE document CHANGE COLUMN doctype doctype VARCHAR(60);


alter table security add forcePasswordReset tinyint(1);

-- update-2013-09-03.sql

UPDATE `security` SET `forcePasswordReset`='1'  WHERE `user_name`='oscardoc';

-- PHC somehow missed
CREATE TABLE IF NOT EXISTS `billing_on_payment` (
    `payment_id` int (10) NOT NULL auto_increment primary key, 
    `billing_no` int(6) NOT NULL, 
    `pay_date` timestamp NOT NULL
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


-- update-2013-09-19.sql --
ALTER TABLE eform_data MODIFY form_data mediumtext;



-- add tracability security object and permissions based ON UPDATE 2013 10 15

INSERT INTO`secObjPrivilege` VALUES('doctor','_admin.traceability','x',0,'999998') ON duplicate key UPDATE provider_no='999998';
INSERT INTO`secObjPrivilege` VALUES('admin','_admin.traceability','x',0,'999998') ON duplicate key UPDATE provider_no='999998';
INSERT INTO`secObjectName`  (`objectName`,`description`,`orgapplicable`) VALUES ('_admin.traceability', 'Right to generate trace and run traceability report',0) ON duplicate key UPDATE description='Rights to generate trace and run report';

-- update-2013-10-25.sql --
/* Updates for PHV */ 
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'IBPL', 'Income below poverty line', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'FAS', 'Folic Acid supplementation', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'COGA', 'Cognitive Assessment', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'HPNP', 'Hearing protection/Noise control programs', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'SBLT', 'Seat belts', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'SDET', 'Smoke detector that works', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'PWC', 'Parents with children', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'SUNP', 'Sun protection', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'BTFP', 'Brush teeth with fluoride toothpaste', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'FLOS', 'Floss', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'DILY', 'Dentist in the last year', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00');

UPDATE `issue` SET type = 'icd9' WHERE length(type) = 0 or type is null;
 
UPDATE `issue` SET type = 'system' WHERE code = 'OMeds' or code = 'SocHistory' or code = 'MedHistory' or code = 'Concerns' or code = 'Reminders' or code = 'FamHistory';


-- update-2013-10-31.sql --
ALTER TABLE OcanSubmissionLog ADD COLUMN submissionType varchar(30) not null;
ALTER TABLE OcanStaffForm ADD COLUMN referralDate date;	
ALTER TABLE OcanStaffForm ADD COLUMN admissionDate date;
ALTER TABLE OcanStaffForm ADD COLUMN serviceInitDate date;
ALTER TABLE OcanStaffForm ADD COLUMN dischargeDate date;

-- use procedure to prevent error caused by previously applied index
CALL CreateIndex('oscar_15', 'OcanSubmissionLog', 'submitDateIndex', 'submitDateTime');
CALL CreateIndex('oscar_15', 'OcanSubmissionLog', 'submissionTypeIndex', 'submissionType');

-- create index submitDateIndex on OcanSubmissionLog (submitDateTime);
-- create index submissionTypeIndex on OcanSubmissionLog (submissionType);


UPDATE OcanSubmissionLog set submissionType="OCAN" where submissionType is null;
insert into IssueGroupIssues select (select id from IssueGroup where name='Physical Health'),issue.issue_id from issue where issue.code='OCAN10060'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='Personal ID'),issue.issue_id from issue where issue.code='OCAN10002'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='Legal'),issue.issue_id from issue where issue.code='OCAN10241'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='Education'),issue.issue_id from issue where issue.code='OCAN10200'; 

insert into IssueGroup (name) values ("CARE COORDINATION");
insert into IssueGroup (name) values ("ACCOMMODATION");
insert into IssueGroup (name) values ("FOOD");
insert into IssueGroup (name) values ("LOOKING AFTER THE HOME");
insert into IssueGroup (name) values ("SELF-CARE");
insert into IssueGroup (name) values ("DAYTIME ACTIVITIES");
insert into IssueGroup (name) values ("PSYCHOTIC SYMPTOMS");
insert into IssueGroup (name) values ("MENTAL HEALTH NON-PSYCHOTIC");
insert into IssueGroup (name) values ("INFORMATION ON CONDITION AND TREATMENT");
insert into IssueGroup (name) values ("PSYCHOLOGICAL DISTRESS");
insert into IssueGroup (name) values ("SAFETY TO SELF");
insert into IssueGroup (name) values ("SAFETY TO OTHERS");
insert into IssueGroup (name) values ("ALCOHOL");
insert into IssueGroup (name) values ("DRUGS");
insert into IssueGroup (name) values ("OTHER ADDICTIONS");
insert into IssueGroup (name) values ("COMPANY");
insert into IssueGroup (name) values ("INTIMATE RELATIONSHIPS");
insert into IssueGroup (name) values ("SEXUAL EXPRESSION");
insert into IssueGroup (name) values ("CHILD CARE");
insert into IssueGroup (name) values ("OTHER DEPENDENTS");
insert into IssueGroup (name) values ("TELEPHONE");
insert into IssueGroup (name) values ("TRANSPORT");
insert into IssueGroup (name) values ("MONEY");
insert into IssueGroup (name) values ("BENEFITS");

insert into IssueGroupIssues select (select id from IssueGroup where name='CARE COORDINATION'),issue.issue_id from issue where issue.code='OCAN10001'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='ACCOMMODATION'),issue.issue_id from issue where issue.code='OCAN10010'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='FOOD'),issue.issue_id from issue where issue.code='OCAN10020'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='LOOKING AFTER THE HOME'),issue.issue_id from issue where issue.code='OCAN10030'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='SELF-CARE'),issue.issue_id from issue where issue.code='OCAN10040'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='DAYTIME ACTIVITIES'),issue.issue_id from issue where issue.code='OCAN10050'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='PSYCHOTIC SYMPTOMS'),issue.issue_id from issue where issue.code='OCAN10070'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='MENTAL HEALTH NON-PSYCHOTIC'),issue.issue_id from issue where issue.code='OCAN10072'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='INFORMATION ON CONDITION AND TREATMENT'),issue.issue_id from issue where issue.code='OCAN10080'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='PSYCHOLOGICAL DISTRESS'),issue.issue_id from issue where issue.code='OCAN10090'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='SAFETY TO SELF'),issue.issue_id from issue where issue.code='OCAN10100'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='SAFETY TO OTHERS'),issue.issue_id from issue where issue.code='OCAN10110'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='ALCOHOL'),issue.issue_id from issue where issue.code='OCAN10120'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='DRUGS'),issue.issue_id from issue where issue.code='OCAN10130'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='OTHER ADDICTIONS'),issue.issue_id from issue where issue.code='OCAN10140'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='COMPANY'),issue.issue_id from issue where issue.code='OCAN10150'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='INTIMATE RELATIONSHIPS'),issue.issue_id from issue where issue.code='OCAN10160'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='SEXUAL EXPRESSION'),issue.issue_id from issue where issue.code='OCAN10170'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='CHILD CARE'),issue.issue_id from issue where issue.code='OCAN10180'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='OTHER DEPENDENTS'),issue.issue_id from issue where issue.code='OCAN10190'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='TELEPHONE'),issue.issue_id from issue where issue.code='OCAN10210'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='TRANSPORT'),issue.issue_id from issue where issue.code='OCAN10220'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='MONEY'),issue.issue_id from issue where issue.code='OCAN10230'; 
insert into IssueGroupIssues select (select id from IssueGroup where name='BENEFITS'),issue.issue_id from issue where issue.code='OCAN10240'; 

-- update-2013-11-05.sql --
create table ORNPreImplementationReportLog (
  id int(10) NOT NULL auto_increment,
  providerNo varchar(10) not null,
  reportData text not null,
  lastUpdateDate datetime not null,
  primary key(id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- update-2013-11-06.sql --
update `measurementType` set `typeDisplayName`= 'BPI Pain Interference', `typeDescription`= 'BPI Pain Interference' where type='BPII';
update `measurementType` set `typeDisplayName`= 'BPI Pain Severity', `typeDescription`= 'BPI Pain Severity' where type='BPIS';

-- update-2013-11-14.sql --
alter table demographic change city city varchar(50);


-- update-2013-11-25.sql --
CREATE TABLE `ServiceRequestToken` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clientId` int(11) DEFAULT NULL,
  `tokenId` varchar(255) NOT NULL,
  `tokenSecret` varchar(255) NOT NULL,
  `callback` varchar(255) NOT NULL,
  `verifier` varchar(255) DEFAULT NULL,
  `providerNo` varchar(25) DEFAULT NULL,
  `scopes` varchar(255) DEFAULT NULL,
  `dateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE `ServiceAccessToken` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clientId` int(11) DEFAULT NULL,
  `tokenId` varchar(255) NOT NULL,
  `tokenSecret` varchar(255) NOT NULL,
  `lifetime` int(10) NOT NULL,
  `issued` int(10) NOT NULL,
  `providerNo` varchar(25) DEFAULT NULL,
  `scopes` varchar(255) DEFAULT NULL,
  `dateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE `ServiceClient` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `clientKey` varchar(255) NOT NULL,
  `clientSecret` varchar(255) NOT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `dateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- update-2013-12-11.sql --
ALTER TABLE document MODIFY number_of_pages int(6);

-- update-2013-12-17.sql --
create table PreventionsLotNrs(
  id int(10) NOT NULL AUTO_INCREMENT, 
  creationDate datetime,
  providerNo varchar(6) NOT NULL,
  preventionType varchar(20) NOT NULL,
  lotNr text NOT NULL,
  deleted boolean NOT NULL, 
  lastUpdateDate datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- update-2013-12-20.sql --
/* Updates for PHV */ 
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ECHK', 'Do you have your eyes regularly checked?', 'NULL', 'Yes/No', '7', '2013-12-20 13:00:00'),
( 'HCON', 'Do you have any hearing concerns?', 'NULL', 'Yes/No', '7', '2013-12-20 13:00:00'),
( 'FAMR', 'Family/Relationships', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'WKED', 'Work/Education', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'SDUS', 'Street Drug Use', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'SHAB', 'Sleep Habits', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'SEXH', 'Sexual History', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'PXAM', 'Physical Exam', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'FUPP', 'Assessment/Follow-up plans', 'NULL', 'Review', '16', '2013-12-30 13:00:00');

/*this was added by PHV and requested to delete by PHV*/
delete from measurementType where type='FLOS';

update measurementType set type='BTFT' where type='BTFP';
update measurementType set typeDisplayName = 'Brush teeth with fluoride toothpaste and floss' where type = 'BTFT';

INSERT INTO `validations` (`name`, `regularExp`, `maxLength` , `isNumeric`, `isDate` ) VALUES ('Review', 'REVIEWED|reviewed|Reviewed', 0, 0, 0);

-- update-2013-12-27.sql --
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ORSK', 'Opioid Risk', 'NULL', 'Score 0-26', '3', '2013-12-27 13:00:00');

-- update-2014-01-14.sql --
INSERT INTO `secRole` (`role_name`, `description` ) VALUES ('student', 'Student (OSCAR Learning)'),('moderator', 'Moderator (OSCAR Learning)');

-- update-2014-01-23.sql --
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CAVD', 'Calcium and Vitamin D', 'NULL', 'Review', '16', '2014-01-23 13:00:00'),
( 'STIS', 'STI Screening', 'Sexual Transmitted Infections', 'Review', '16', '2014-01-23 13:00:00'),
( 'SSXC', 'Safe Sex Counselling', 'NULL', 'Review', '16', '2014-01-23 13:00:00');

/*this was added by PHV and requested to delete by PHV*/
delete from measurementType where type='PCPR';
delete from measurementType where type='NFSW';
delete from measurementType where type='HOTW';
delete from measurementType where type='PWC';

-- update-2014-02-01
UPDATE `eform` 
SET `form_date`='2014-02-01', `form_html`= '\n<html><head>\r\n<meta http-equiv="content-type" content="text/html; charset=UTF-8">\r\n\r\n<title>Rich Text Letter</title>\r\n<style type="text/css">\r\n.butn {width: 140px;}\r\n</style>\r\n\r\n<style type="text/css" media="print">\r\n.DoNotPrint {display: none;}\r\n\r\n</style>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}jquery/jquery-1.4.2.js"></script>\r\n\r\n<script language="javascript">\r\nvar needToConfirm = false;\r\n\r\n//keypress events trigger dirty flag for the iFrame and the subject line\r\ndocument.onkeyup=setDirtyFlag\r\n\r\n\r\nfunction setDirtyFlag() {\r\n	needToConfirm = true; \r\n}\r\n\r\nfunction releaseDirtyFlag() {\r\n	needToConfirm = false; //Call this function if dosent requires an alert.\r\n	//this could be called when save button is clicked\r\n}\r\n\r\n\r\nwindow.onbeforeunload = confirmExit;\r\n\r\nfunction confirmExit() {\r\n	if (needToConfirm){\r\n	return "You have attempted to leave this page. If you have made any changes without clicking the Submit button, your changes will be lost. Are you sure you want to exit this page?";\r\n	}\r\n}\r\n\r\n\r\nvar loads=true;\r\n\r\nfunction maximize() {\r\n	window.resizeTo(1030, 865) ;\r\n	loads=false;\r\n}\r\n\r\nfunction saveRTL() {\r\n	needToConfirm=false;\r\n	var theRTL=editControlContents(''edit'');\r\n	var myNewString = theRTL.replace(/"/g, ''&quot;'');\r\n	document.getElementById(''Letter'').value=myNewString.replace(/''/g, "&#39;");\r\n}\r\n</script>\r\n\r\n<!-- START OF EDITCONTROL CODE --> \r\n\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/editControl.js"></script>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/APCache.js"></script>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/imageControl.js"></script>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/faxControl.js"></script>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/signatureControl.jsp"></script>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/printControl.js"></script>\r\n\r\n<script language="javascript">\r\n	//put any of the optional configuration variables that you want here\r\n	cfg_width = ''840''; //editor control width in pixels\r\n	cfg_height = ''520''; //editor control height in pixels\r\n	cfg_editorname = ''edit''; //the handle for the editor                  \r\n	cfg_isrc = ''../eform/displayImage.do?imagefile=''; //location of the button icon files\r\n	cfg_filesrc = ''../eform/displayImage.do?imagefile=''; //location of the html files\r\n	cfg_template = ''blank.rtl''; //default style and content template\r\n	cfg_formattemplate = ''<option value=""> loading... </option></select>'';\r\n	//cfg_layout = ''[all]'';             //adjust the format of the buttons here\r\n	//cfg_layout = ''<table style="background-color:ccccff; width:840px"><tr id=control1><td>[bold][italic][underlined][strike][subscript][superscript]|[left][center][full][right]|[unordered][ordered][rule]|[undo][redo]|[indent][outdent][select-all][clean]|[table]</td></tr><tr id=control2><td>[select-block][select-face][select-size][select-template]|[image][clock][date][spell][help]</td></tr></table>[edit-area]'';\r\n	cfg_layout = ''<table style="background-color:ccccff; width:840px"><tr id=control1><td align=center>[bold][italic][underlined][strike][subscript][superscript]|[left][center][full][right]|[unordered][ordered][rule]|[undo][redo]|[indent][outdent][select-all][clean]|[table]\\[text-colour][hilight]</td></tr><tr id=control2><td align=center>[select-block][select-face][select-size][select-template]|[image][link]|[clock][date][spell][cut][copy][paste][help]</td></tr></table>[edit-area]'';\r\n	insertEditControl(); // Initialise the edit control and sets it at this point in the webpage\r\n\r\n	\r\n	function gup(name, url)\r\n	{\r\n		if (url == null) { url = window.location.href; }\r\n		name = name.replace(/[\\[]/,"\\\\\\[").replace(/[\\]]/,"\\\\\\]");\r\n		var regexS = "[\\\\?&]"+name+"=([^&#]*)";\r\n		var regex = new RegExp(regexS);\r\n		var results = regex.exec(url);\r\n		if (results == null) { return ""; }\r\n		else { return results[1]; }\r\n	}\r\n	var demographicNo ="";\r\n\r\n	jQuery(document).ready(function(){\r\n		demographicNo = gup("demographic_no");\r\n		if (demographicNo == "") { demographicNo = gup("efmdemographic_no", jQuery("form").attr(''action'')); }\r\n		if (typeof signatureControl != "undefined") {\r\n			signatureControl.initialize({\r\n				sigHTML:"../signature_pad/tabletSignature.jsp?inWindow=true&saveToDB=true&demographicNo=",\r\n				demographicNo:demographicNo,\r\n				refreshImage: function (e) {\r\n					var html = "<img src=''"+e.storedImageUrl+"&r="+ Math.floor(Math.random()*1001) +"''></img>";\r\n					doHtml(html);		\r\n				},\r\n				signatureInput: "#signatureInput"	\r\n			});\r\n		}		\r\n	});\r\n		\r\n	var cache = createCache({\r\n		defaultCacheResponseHandler: function(type) {\r\n			if (checkKeyResponse(type)) {\r\n				doHtml(cache.get(type));\r\n			}			\r\n			\r\n		},\r\n		cacheResponseErrorHandler: function(xhr, error) {\r\n			alert("Please contact an administrator, an error has occurred.");			\r\n			\r\n		}\r\n	});	\r\n	\r\n	function checkKeyResponse(response) {		\r\n		if (cache.isEmpty(response)) {\r\n			alert("The requested value has no content.");\r\n			return false;\r\n		}\r\n		return true;\r\n	}\r\n	\r\n	function printKey (key) {\r\n		var value = cache.lookup(key); \r\n		if (value != null && checkKeyResponse(key)) { doHtml(cache.get(key)); } 		  \r\n	}\r\n	\r\n	function submitFaxButton() {\r\n		document.getElementById(''faxEForm'').value=true;\r\n		saveRTL();\r\n		setTimeout(''document.RichTextLetter.submit()'',1000);\r\n	}\r\n	\r\n	cache.addMapping({\r\n		name: "_SocialFamilyHistory",\r\n		values: ["social_family_history"],\r\n		storeInCacheHandler: function(key,value) {\r\n			cache.put(this.name, cache.get("social_family_history").replace(/(<br>)+/g,"<br>"));\r\n		},\r\n		cacheResponseHandler:function () {\r\n			if (checkKeyResponse(this.name)) {				\r\n				doHtml(cache.get(this.name));\r\n			}	\r\n		}\r\n	});\r\n	\r\n	\r\n	cache.addMapping({name: "template", cacheResponseHandler: populateTemplate});	\r\n	\r\n	cache.addMapping({\r\n		name: "_ClosingSalutation", \r\n		values: ["provider_name_first_init"],	\r\n		storeInCacheHandler: function (key,value) {\r\n			if (!cache.isEmpty("provider_name_first_init")) {\r\n				cache.put(this.name, "<p>Yours Sincerely<p>&nbsp;<p>" + cache.get("provider_name_first_init") + ", MD");\r\n			}\r\n		},\r\n		cacheResponseHandler:function () {\r\n			if (checkKeyResponse(this.name)) {				\r\n				doHtml(cache.get(this.name));\r\n			}	\r\n		}\r\n	});\r\n	\r\n	cache.addMapping({\r\n		name: "_ReferringBlock", \r\n		values: ["referral_name", "referral_address", "referral_phone", "referral_fax"], 	\r\n		storeInCacheHandler: function (key, value) {\r\n			var text = \r\n				(!cache.isEmpty("referral_name") ? cache.get("referral_name") + "<br>" : "") \r\n			  + (!cache.isEmpty("referral_address") ? cache.get("referral_address") + "<br>" : "")\r\n			  + (!cache.isEmpty("referral_phone") ? "Tel: " + cache.get("referral_phone") + "<br>" : "")\r\n			  + (!cache.isEmpty("referral_fax") ? "Fax: " + cache.get("referral_fax") + "<br>" : "");\r\n			if (text == "") {\r\n				text = \r\n					(!cache.isEmpty("bc_referral_name") ? cache.get("bc_referral_name") + "<br>" : "") \r\n				  + (!cache.isEmpty("bc_referral_address") ? cache.get("bc_referral_address") + "<br>" : "")\r\n				  + (!cache.isEmpty("bc_referral_phone") ? "Tel: " + cache.get("bc_referral_phone") + "<br>" : "")\r\n				  + (!cache.isEmpty("bc_referral_fax") ? "Fax: " + cache.get("bc_referral_fax") + "<br>" : "");\r\n			}						 \r\n			cache.put(this.name, text)\r\n		},\r\n		cacheResponseHandler: function () {\r\n			if (checkKeyResponse(this.name)) {\r\n				doHtml(cache.get(this.name));\r\n			}\r\n		}\r\n	});\r\n	\r\n	cache.addMapping({\r\n		name: "letterhead", \r\n		values: ["clinic_name", "clinic_fax", "clinic_phone", "clinic_addressLineFull", "doctor", "doctor_contact_phone", "doctor_contact_fax", "doctor_contact_addr"], \r\n		storeInCacheHandler: function (key, value) {\r\n			var text = genericLetterhead();\r\n			cache.put("letterhead", text);\r\n		},\r\n		cacheResponseHandler: function () {\r\n			if (checkKeyResponse(this.name)) {\r\n				doHtml(cache.get(this.name));\r\n			}\r\n		}\r\n	});\r\n	\r\n	cache.addMapping({\r\n		name: "referral_nameL", \r\n		values: ["referral_name"], \r\n		storeInCacheHandler: function(_key,_val) { \r\n		if (!cache.isEmpty("referral_name")) {\r\n				var mySplitResult =  cache.get("referral_name").toString().split(",");\r\n				cache.put("referral_nameL", mySplitResult[0]);\r\n			} \r\n		}\r\n	});\r\n\r\n	cache.addMapping({\r\n		name: "medical_historyS", \r\n		values: ["medical_history"], \r\n		storeInCacheHandler: function(_key,_val) { \r\n		if (!cache.isEmpty("medical_history")) {\r\n				var mySplitResult =  cache.get("medical_history").toString().split("]]-----");\r\n				cache.put("medical_historyS", mySplitResult.pop());\r\n			} \r\n		}\r\n	});\r\n\r\n	cache.addMapping({\r\n		name: "stamp", \r\n		values: ["stamp_name", "doctor"], \r\n		storeInCacheHandler: function(_key,_val) { \r\n				var imgsrc=pickStamp();\r\n				cache.put("stamp",imgsrc);\r\n		}\r\n	});\r\n\r\n	\r\n	cache.addMapping({\r\n		name: "complexAge", \r\n		values: ["complexAge"], \r\n		cacheResponseHandler: function() {\r\n			if (cache.isEmpty("complexAge")) { \r\n				printKey("age"); \r\n			}\r\n			else {\r\n				if (checkKeyResponse(this.name)) {\r\n					doHtml(cache.get(this.name));\r\n				}\r\n			}\r\n		}\r\n	});\r\n	\r\n	// Setting up many to one mapping for derived gender keys.\r\n	var genderKeys = ["he_she", "his_her", "gender"];	\r\n	var genderIndex;\r\n	for (genderIndex in genderKeys) {\r\n		cache.addMapping({ name: genderKeys[genderIndex], values: ["sex"]});\r\n	}\r\n	cache.addMapping({name: "sex", values: ["sex"], storeInCacheHandler: populateGenderInfo});\r\n	\r\n	function isGenderLookup(key) {\r\n		var y;\r\n		for (y in genderKeys) { if (genderKeys[y] == key) { return true; } }\r\n		return false;\r\n	}\r\n	\r\n	function populateGenderInfo(key, val){\r\n		if (val == ''F'') {\r\n			cache.put("sex", "F");\r\n			cache.put("he_she", "she");\r\n			cache.put("his_her", "her");\r\n			cache.put("gender", "female");				\r\n		}\r\n		else {\r\n			cache.put("sex", "M");\r\n			cache.put("he_she", "he");\r\n			cache.put("his_her", "him");\r\n			cache.put("gender", "male");				\r\n		}\r\n	}\r\n	\r\n	function Start() {\r\n		\r\n			$.ajax({\r\n				url : "efmformrtl_templates.jsp",\r\n				success : function(data) {\r\n					$("#template").html(data);\r\n					loadDefaultTemplate();\r\n				}\r\n			});\r\n	\r\n			$(".cacheInit").each(function() { \r\n				cache.put($(this).attr(''name''), $(this).val());\r\n				$(this).remove();				\r\n			});\r\n			\r\n			// set eventlistener for the iframe to flag changes in the text displayed \r\n			var agent = navigator.userAgent.toLowerCase(); //for non IE browsers\r\n			if ((agent.indexOf("msie") == -1) || (agent.indexOf("opera") != -1)) {\r\n				document.getElementById(cfg_editorname).contentWindow\r\n						.addEventListener(''keypress'', setDirtyFlag, true);\r\n			}\r\n				\r\n			// set the HTML contents of this edit control from the value saved in Oscar (if any)\r\n			var contents = document.getElementById(''Letter'').value\r\n			if (contents.length == 0) {\r\n				parseTemplate();\r\n			} else {\r\n				seteditControlContents(cfg_editorname, contents);\r\n				document.getElementById(cfg_editorname).contentWindow.document.designMode = ''on'';\r\n			}\r\n			maximize();\r\n	}\r\n\r\n	function htmlLine(text) {\r\n		return text.replace(/\\r?\\n/g,"<br>");\r\n	}\r\n\r\n	function genericLetterhead() {\r\n		// set the HTML contents of the letterhead\r\n		var address = ''<table border=0><tbody><tr><td><font size=6>''\r\n				+ cache.get(''clinic_name'')\r\n				+ ''</font></td></tr><tr><td><font size=2>''\r\n				+ cache.get(''doctor_contact_addr'')\r\n				+ '' Fax: '' + cache.get(''doctor_contact_fax'')\r\n				+ '' Phone: '' + cache.get(''doctor_contact_phone'')\r\n				+ ''</font><hr></td></tr></tbody></table><br>'';\r\n		if ( (cache.get(''clinic_name'').toLowerCase()).indexOf(''amily health team'',0)>-1){\r\n		address=fhtLetterhead(); }\r\n		if ( (cache.get(''clinic_name'').toLowerCase()).indexOf(''fht'',0)>-1){\r\n		address=fhtLetterhead(); }\r\n		return address;\r\n	}\r\n\r\n	function fhtLetterhead() {\r\n		// set the HTML contents of the letterhead using FHT colours\r\n		var address = cache.get(''clinic_addressLineFull'')\r\n				+ ''<br>Fax:'' + cache.get(''clinic_fax'')\r\n				+ '' Phone:'' + cache.get(''clinic_phone'');\r\n		if (cache.contains("doctor") && cache.get(''doctor'').indexOf(''zapski'') > 0) {\r\n			address = ''293 Meridian Avenue, Haileybury, ON P0J 1K0<br> Tel 705-672-2442&nbsp;&nbsp; Fax 866-945-5725'';\r\n		}\r\n		address = ''<table style=\\''text-align: right;\\'' border=\\''0\\''><tbody><tr style=\\''font-style: italic; color: rgb(71, 127, 128);\\''><td><font size=\\''+2\\''>''\r\n				+ cache.get(''clinic_name'')\r\n				+ ''</font> <hr style=\\''width: 100%; height: 3px; color: rgb(212, 118, 0); background-color: rgb(212, 118, 0);\\''></td> </tr> <tr style=\\''color: rgb(71, 127, 128);\\''> <td><font size=\\''+1\\''>Family Health Team<br>Equipe Sante Familiale</font></td> </tr> <tr style=\\''color: rgb(212, 118, 0); \\''> <td><small>''\r\n				+ address + ''</small></td> </tr> </tbody> </table>'';\r\n		return address;\r\n	}\r\n\r\n	function pickStamp() {\r\n		// set the HTML contents of the signature stamp\r\n		var mystamp =''<img src="../eform/displayImage.do?imagefile=stamp.png">'';\r\n		if (cache.contains("doctor")) {\r\n			if (cache.get(''doctor'').indexOf(''zapski'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=PHC.png" width="200" height="100" />'';\r\n				}\r\n			if (cache.get(''doctor'').indexOf(''hurman'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=MCH.png" width="200" height="100" />'';\r\n				}\r\n			if (cache.get(''doctor'').indexOf(''mith'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=PJS.png" width="200" height="100" />'';\r\n				}\r\n			if (cache.get(''doctor'').indexOf(''loko'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=FAO.png" width="200" height="100" />'';\r\n				}\r\n			if (cache.get(''doctor'').indexOf(''urrie'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=LNC.png" width="200" height="100" />'';\r\n				}\r\n			if (cache.get(''doctor'').indexOf(''cdermot'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=TMD.png" width="200" height="100" />'';\r\n				}\r\n		}\r\n		return mystamp;\r\n	}\r\n	var formIsRTL = true;\r\n\r\n</script>\r\n<!-- END OF EDITCONTROL CODE -->\r\n</head><body bgcolor="FFFFFF" onload="Start();">\r\n<form method="post" action="" name="RichTextLetter" ><textarea name="Letter" id="Letter" style="width:600px; display: none;"></textarea>\r\n\r\n<div class="DoNotPrint" id="control3" style="position:absolute; top:20px; left: 860px;">\r\n\r\n<!-- Letter Head -->\r\n<input type="button" class="butn" name="AddLetterhead" id="AddLetterhead" value="Letterhead" onclick="printKey(''letterhead'');">\r\n<br>\r\n\r\n<!-- Referring Block -->\r\n<input type="button" class="butn" name="AddReferral" id="AddReferral" value="Referring Block" onclick="printKey(''_ReferringBlock'');">\r\n<br>\r\n\r\n<!-- Patient Block -->\r\n<input type="button" class="butn" name="AddLabel" id="AddLabel" value="Patient Block" onclick="printKey(''label'');">\r\n<br>\r\n<br> \r\n\r\n<!-- Social History -->\r\n<input type="button" class="butn" name="AddSocialFamilyHistory" value="Social History" onclick="var hist=''_SocialFamilyHistory'';printKey(hist);">\r\n<br>\r\n\r\n<!--  Medical History -->\r\n<input type="button"  class="butn" name="AddMedicalHistory" value="Medical History" width=30 onclick="var hist=''medical_historyS'';printKey(hist);">\r\n<br>\r\n\r\n<!--  Ongoing Concerns -->\r\n<input type="button" class="butn" name="AddOngoingConcerns" value="Ongoing Concerns" onclick="var hist=''ongoingconcerns''; printKey(hist);">\r\n<br>\r\n\r\n<!-- Reminders -->\r\n<input type="button" class="butn" name="AddReminders" value="Reminders"\r\n	onclick="var hist=''reminders''; printKey(hist);">\r\n<br>\r\n\r\n<!-- Allergies -->\r\n<input type="button" class="butn" name="Allergies" id="Allergies" value="Allergies" onclick="printKey(''allergies_des'');">\r\n<br>\r\n\r\n<!-- Prescriptions -->\r\n<input type="button" class="butn" name="Medlist" id="Medlist" value="Prescriptions"	onclick="printKey(''druglist_trade'');">\r\n<br>\r\n\r\n<!-- Other Medications -->\r\n<input type="button" class="butn" name="OtherMedicationsHistory" value="Other Medications" onclick="printKey(''other_medications_history''); ">\r\n\r\n<br>\r\n\r\n<!-- Risk Factors -->\r\n<input type="button" class="butn" name="RiskFactors" value="Risk Factors" onclick="printKey(''riskfactors''); ">\r\n<br>\r\n\r\n<!-- Family History -->\r\n<input type="button" class="butn" name="FamilyHistory" value="Family History" onclick="printKey(''family_history''); ">\r\n<br>\r\n<br>\r\n\r\n<!-- Patient Name --> \r\n<input type="button" class="butn" name="Patient" value="Patient Name" onclick="printKey(''first_last_name'');">\r\n<br>\r\n\r\n<!-- Patient Age -->\r\n<input type="button" class="butn" name="PatientAge" value="Patient Age" onclick="var hist=''ageComplex''; printKey(hist);">\r\n\r\n<br>\r\n\r\n<!-- Patient Label -->\r\n<input type="button" class="butn" name="label" value="Patient Label" onclick="hist=''label'';printKey(hist);">\r\n<br>\r\n\r\n<input type="button" class="butn" name="PatientSex" value="Patient Gender" onclick="printKey(''gender'');">\r\n<br>\r\n<br>\r\n\r\n<!-- Closing Salutation -->\r\n<input type="button" class="butn" name="Closing" value="Closing Salutation" onclick="printKey(''_ClosingSalutation'');">\r\n<br>\r\n\r\n<!-- Signature Stamp -->\r\n<input type="button" class="butn" name="stamp" value="Stamp" onclick="printKey(''stamp'');">\r\n<br>\r\n<!--  Current User -->\r\n<input type="button" class="butn" name="User" value="Current User" onclick="var hist=''current_user''; printKey(hist);">\r\n<br>\r\n\r\n<!-- Attending Doctor -->\r\n<input type="button" class="butn" name="Doctor" value="Doctor (MRP)" onclick="var hist=''doctor''; printKey(hist);">\r\n<br>\r\n<br>\r\n\r\n</div>\r\n\r\n\r\n<div class="DoNotPrint" >\r\n<input onclick="viewsource(this.checked)" type="checkbox">\r\nHTML Source\r\n<input onclick="usecss(this.checked)" type="checkbox">\r\nUse CSS	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Subject: <input name="subject" id="subject" size="40" type="text">		 \r\n\r\n<table><tr id=sig>\r\n<td> <div id="signatureInput">&nbsp;</div></td>\r\n<td> <div id="faxControl">&nbsp;</div></td>\r\n</tr></table>\r\n \r\n \r\n<input value="Submit" name="SubmitButton" type="submit" onclick="saveRTL();  document.RichTextLetter.submit()">\r\n<input value="Print" name="PrintSaveButton" type="button" onclick="document.getElementById(''edit'').contentWindow.print();saveRTL();  setTimeout(''document.RichTextLetter.submit()'',1000);">\r\n<input value="Reset" name="ResetButton" type="reset">\r\n<input value="Print" name="PrintButton" type="button" onclick="document.getElementById(''edit'').contentWindow.print();">\r\n\r\n\r\n    	</div>\r\n\r\n</form>\r\n\r\n</body></html>'
WHERE `fid`='2' 
AND `form_name`='Rich Text Letter'
AND `form_date`='2012-06-01';

-- from update-2014-02-06.sql --
alter table billing_on_item modify ser_num char(5); 

-- from update-2014-02-12.sql --
ALTER TABLE `secRole` ADD UNIQUE INDEX `secRoleTemp`(`role_name`, `description`);
INSERT INTO `secRole` (`role_name`, `description`) VALUES('midwife', 'midwife') ON DUPLICATE KEY UPDATE `role_name`='midwife';
ALTER TABLE `secRole` DROP INDEX `secRoleTemp`;

-- from update-2014-03-03.sql --
/* Sharing Center Tables */
CREATE TABLE IF NOT EXISTS `sharing_affinity_domain` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `oid` VARCHAR(255),
    `permission` VARCHAR(10),
    `name` VARCHAR(255)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_actor` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `oid` VARCHAR(255),
    `name` VARCHAR(255),
    `actor_type` VARCHAR(255),
    `secure` TINYINT(1),
    `endpoint` VARCHAR(255),
    `id_facility_name` VARCHAR(255),
    `id_application_id` VARCHAR(255),
    `domain_fk` INT,
    CONSTRAINT `fk_sharing_domain` FOREIGN KEY (`domain_fk`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_policy_definition` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `display_name` VARCHAR(255),
    `code` VARCHAR(255),
    `code_system` VARCHAR(255),
    `policy_doc_url` VARCHAR(255),
    `ack_duration` DOUBLE,
    `domain_fk` INT,
    CONSTRAINT `fk_sharing_pd_domain` FOREIGN KEY (`domain_fk`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_acl_definition` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `role` VARCHAR(255),
    `permission` VARCHAR(255),
    `action_outcome` VARCHAR(255),
    `policy_fk` INT,
    CONSTRAINT `fk_sharing_policy` FOREIGN KEY (`policy_fk`) REFERENCES `sharing_policy_definition` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_value_set` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `value_set_id` VARCHAR(255),
    `description` VARCHAR(255),
    `attribute` VARCHAR(255),
    `domain_fk` INT,
    CONSTRAINT `fk_sharing_vs_domain` FOREIGN KEY (`domain_fk`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_code_mapping` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `attribute` VARCHAR(255),
    `description` VARCHAR(255),
    `domain_fk` INT,
    CONSTRAINT `fk_sharing_cm_domain` FOREIGN KEY (`domain_fk`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_code_value` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `code_system` VARCHAR(255),
    `code` VARCHAR(255),
    `display_name` VARCHAR(255),
    `code_system_name` VARCHAR(255),
    `mapping_fk` INT,
    CONSTRAINT `fk_sharing_mapping` FOREIGN KEY (`mapping_fk`) REFERENCES `sharing_code_mapping` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_clinic_info` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `org_oid` VARCHAR(255),
    `name` VARCHAR(255),
    `application_name` VARCHAR(255),
    `facility_name` VARCHAR(255),
    `universal_id` VARCHAR(255),
    `namespace` VARCHAR(255),
    `source_id` VARCHAR(255)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;
# Ensure there is a record in the sharing_clinic_info table
INSERT INTO sharing_clinic_info (org_oid, name, application_name, facility_name, universal_id, namespace, source_id)
SELECT * FROM (SELECT '0.1.2.3.4', 'Clinic Name', 'OSCAR_App', 'OSCAR_Facility', '1.0.1', 'OSCAR', '1.0.2') AS temp_set
WHERE NOT EXISTS (SELECT * FROM sharing_clinic_info);

CREATE TABLE IF NOT EXISTS `sharing_infrastructure` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `alias` VARCHAR(255),
    `common_name` VARCHAR(255),
    `organizational_unit` VARCHAR(255),
    `organization` VARCHAR(255),
    `locality` VARCHAR(255),
    `state` VARCHAR(255),
    `country` VARCHAR(255),
    `public_key` TEXT,
    `private_key` TEXT
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_patient_document` (
  `patientDocumentId` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `demographic_no` INT,
  `uniqueDocumentId` VARCHAR(255),
  `repositoryUniqueId` VARCHAR(45),
  `creationTime` DATETIME,
  `isDownloaded` TINYINT(1),
  `affinityDomain` VARCHAR(255),
  `title` VARCHAR(255),
  `mimetype` VARCHAR(255),
  `author` VARCHAR(255),
  `affinityDomain_fk` INT,
  CONSTRAINT `fk_sharing_pat_doc_affinityDomain` FOREIGN KEY (`affinityDomain_fk`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_patient_network` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `demographic_no` INT,
    `affinity_domain` INT,
    `sharing_enabled` TINYINT(1),
    `sharing_key` VARCHAR(255),
    `date_enabled` Date,
    CONSTRAINT `fk_sharing_pat_netwk_affinity_domain` FOREIGN KEY (`affinity_domain`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_mapping_code` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `attribute` VARCHAR(255),
    `code` VARCHAR(255),
    `code_system` VARCHAR(255),
    `code_system_name` VARCHAR(255),
    `display_name` VARCHAR(255)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_mapping_site` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `affinity_domain` INT NOT NULL,
    `source` VARCHAR(255) NOT NULL,
    `facility_type_code` INT,
    `practice_setting_code` INT,
    CONSTRAINT `fk_sharing_mapping_site_facility_type_code` FOREIGN KEY (`facility_type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_site_practice_setting_code` FOREIGN KEY (`practice_setting_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_mapping_edoc` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `affinity_domain` INT NOT NULL,
    `doc_type` VARCHAR(255) NOT NULL,
    `source` VARCHAR(255) NOT NULL,
    `class_code` INT,
    `type_code` INT,
    `format_code` INT,
    `content_type_code` INT,
    `event_code_list` INT,
    `folder_code_list` INT,
    CONSTRAINT `fk_sharing_mapping_edoc_class_code` FOREIGN KEY (`class_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_edoc_type_code` FOREIGN KEY (`type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_edoc_format_code` FOREIGN KEY (`format_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_edoc_content_type_code` FOREIGN KEY (`content_type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_edoc_event_code_list` FOREIGN KEY (`event_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_edoc_folder_code_list` FOREIGN KEY (`folder_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_mapping_eform` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `affinity_domain` INT NOT NULL,
    `eform_id` INT NOT NULL,
    `source` VARCHAR(255) NOT NULL,
    `class_code` INT,
    `type_code` INT,
    `format_code` INT,
    `content_type_code` INT,
    `event_code_list` INT,
    `folder_code_list` INT,
    CONSTRAINT `fk_sharing_mapping_eform_class_code` FOREIGN KEY (`class_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_eform_type_code` FOREIGN KEY (`type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_eform_format_code` FOREIGN KEY (`format_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_eform_content_type_code` FOREIGN KEY (`content_type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_eform_event_code_list` FOREIGN KEY (`event_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_eform_folder_code_list` FOREIGN KEY (`folder_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_mapping_misc` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `affinity_domain` INT NOT NULL,
    `type` VARCHAR(255) NOT NULL,
    `source` VARCHAR(255) NOT NULL,
    `class_code` INT,
    `type_code` INT,
    `format_code` INT,
    `content_type_code` INT,
    `event_code_list` INT,
    `folder_code_list` INT,
    CONSTRAINT `fk_sharing_mapping_misc_class_code` FOREIGN KEY (`class_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_misc_type_code` FOREIGN KEY (`type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_misc_format_code` FOREIGN KEY (`format_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_misc_content_type_code` FOREIGN KEY (`content_type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_misc_event_code_list` FOREIGN KEY (`event_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_misc_folder_code_list` FOREIGN KEY (`folder_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_document_export` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `document_type` VARCHAR(10) NOT NULL,
    `document` BLOB NOT NULL,
    `demographic_no` INT NOT NUlL,
    CONSTRAINT `fk_sharing_document_export_demographic_no` FOREIGN KEY (`demographic_no`) REFERENCES `demographic` (`demographic_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_patient_policy_consent` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `demographic_no` INT NOT NULL,
  `affinity_domain_id` INT NOT NULL,
  `consent_date` DATETIME NOT NULL,
  `policy_id` INT NOT NULL,
  CONSTRAINT `fk_sharing_consent_policy_definition` FOREIGN KEY (`policy_id`) REFERENCES `sharing_policy_definition` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_sharing_consent_affinity_domain` FOREIGN KEY (`affinity_domain_id`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE IF NOT EXISTS `sharing_exported_doc` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `affinity_domain` INT NOT NULL,
  `demographic_no` INT NOT NULL,
  `local_doc_id` INT,
  `document_type` VARCHAR(10) NOT NULL,
  `document_uid` VARCHAR(255) NOT NULL,
  `document_uuid` VARCHAR(255) NOT NULL,
  `date_exported` DATETIME NOT NULL,
  CONSTRAINT `fk_sharing_exported_doc_domain` FOREIGN KEY (`affinity_domain`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- from update-2014-03-14.sql -
ALTER TABLE `messagetbl` CHANGE `sentto` `sentto` text;

-- from update-2014-03-18.sql -
insert into billing_on_errorCode values ('51','Fee allowed according to the appropriate item in the current Ministry Schedule of Benefits') ON DUPLICATE KEY UPDATE description='Fee allowed according to the appropriate item in the current Ministry Schedule of Benefits';

-- from update-2014-04-08.sql -
alter table patientLabRouting add dateModified datetime;

-- update-2014-4-15
ALTER TABLE `raheader` CHANGE `filename` `filename` VARCHAR( 30 )NOT NULL;

-- update-2014-4-16
alter table program add `enableOCAN` tinyint(1) not null;

-- from update-2014-05-09.sql - TODO measurements check
ALTER TABLE `measurementType` ADD UNIQUE INDEX `type_instruction`(`type`, `measuringInstruction`);


INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ABO', 'Blood Group', 'ABO RhD blood type group', 'Blood Type', '11', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'AFP', 'AFP', 'Alpha Fetoprotein', 'ug/L Range under 7', '5', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ALB', 'Albumin', 'Serum Albumin', 'g/L Range 35-50', '5', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ALP', 'ALP', 'Alkaline Phosphatase', 'U/L Range 50-300', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ANA', 'ANA', 'Antinuclear Antibodies', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'APOB', 'APO B', 'Apolipoprotein B', 'g/L Range 0.5-1.2', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BILI', 'Bilirubin', 'Total Bilirubin', 'umol/L Range under 20', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BUN', 'BUN', 'Blood Urea Nitrogen', 'mmol/L Range 2-9', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Clpl', 'Chloride', 'Chloride', 'mmol/L Range 98-106', '5', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CK', 'CK', 'Creatinine Kinase', 'U/L', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CRP', 'CRP', 'C reactive protein', 'mg/L', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CA', 'Calcium', 'Calcium', 'mmol/L', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'C125', 'CA 125', 'CA 125', 'kU/L Range under 36', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'C153', 'CA 15-3', 'CA 15-3', 'kU/L Range under 23', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'C199', 'CA 19-9', 'CA 19-9', 'kU/L Range under 27', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CEA', 'CEA', 'CEA', 'umol/L', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CHLM', 'CHLM', 'Chlamydia', 'test result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'C3', 'C3', 'Complement component 3', 'umol/L', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CMBS', 'Coombs', 'Coombs', 'test result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DIG', 'Digoxin', 'Digoxin Level', 'nmol/L Range 1-2.6', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DIL', 'Dilantin', 'Dilantin (Phenytoin) level', 'umol/L Range 40-80', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ENA', 'ENA', 'Extractable Nuclear Antigens', 'result', '11', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ESR', 'ESR', 'Erythrocyte sedimentation rate', 'mm/h Range under 20', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Fer', 'Ferritin', 'Ferritin', 'ug/L Range 15-180', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FIT', 'FIT', 'Fecal Immunochemical Test', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FOBT', 'FOBT', 'Fecal Occult Blood', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FT3', 'FT3', 'Free T3', 'pmol/L Range 4-8', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FT4', 'FT4', 'Free T4', 'pmol/L Range 11-22', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GBS', 'GBS', 'Group B Strep', 'test result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GC', 'Gonococcus', 'Gonococcus', 'test result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GGT', 'GGT', 'Gamma-glutamyl transferase', 'U/L Range 10-58', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GCT', '50g Glucose Challenge', '1h 50g Glucose Challenge', 'mmol/L Range under 7.8', '4', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GT1', '75g OGTT 1h', '1h 75g Glucose Tolerance Test', 'mmol/L Range under 10.6', '4', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GT2', '75g OGTT 2h', '2h 75g Glucose Tolerance Test', 'mmol/L Range under 9', '4', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HCO3', 'Bicarbonate', 'Bicarbonate', 'mmol/L Range 20-29', '4', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HBEB', 'AntiHBeAg', 'AntiHBeAg', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HBEG', 'HBeAg', 'HBeAg', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HBVD', 'HBV DNA', 'HBV DNA', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HPYL', 'H Pylori', 'H Pylori', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'LITH', 'Lithium', 'Lithium', 'mmol/L Range 0.6-0.8', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MG', 'Mg', 'Magnesium', 'mmol/L Range 0.7-1.2', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MCV', 'MCV', 'Mean corpuscular volume', 'fL Range 82-98', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PB19', 'Parvovirus', 'Parvovirus B19', 'result', '11', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PHOS', 'Phosphate', 'Phosphate', 'mmol/L Range 0.8-1.4', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PLT', 'Platelets', 'Platelets', 'x10 9/L Range 150-400', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PROT', 'Protein', 'Total Protein Serum', 'g/L Range 60-80', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PSA', 'PSA', 'Prostatic specific antigen', 'ug/L Range under 5', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'iPTH', 'iPTH', 'intact Parathyroid Hormone', 'pmol/L Range 1-6', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'RF', 'RF', 'Rheumatoid Factor', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Rh', 'Rh', 'RhD blood type group', 'result', '11', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'RUB', 'Rubella', 'Rubella titre', 'titre', '11', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'TSAT', 'Transferrin Saturation', 'Transferrin Saturation', 'percent Range 20-50', '4', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'URIC', 'Uric Acid', 'Uric Acid', 'umol/L Range 230-530', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'VZV', 'Zoster', 'Varicella Zoster', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'WBC', 'WBC', 'White Cell Count', 'x10 9/L Range 4-11', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;


ALTER TABLE `measurementType` DROP INDEX `type_instruction`;

INSERT INTO`validations` (`id`, `name`, `regularExp`) VALUES 
(17,'pos or neg', 'pos|neg|positive|negative') ON DUPLICATE KEY UPDATE `name`='pos or neg', `regularExp`= 'pos|neg|positive|negative';

-- update-2014-5-15
insert into billing_on_errorCode values ('CNA', 'Counselling NOT Allowed') ON DUPLICATE KEY UPDATE description='Counselling NOT Allowed';
insert into billing_on_errorCode values ('DF', 'Corresponding fee code has not been claimed or was approved at zero') ON DUPLICATE KEY UPDATE description='Corresponding fee code has not been claimed or was approved at zero';
insert into billing_on_errorCode values ('AD1', 'Corresponding procedure not claimed by same or different physician') ON DUPLICATE KEY UPDATE description='Corresponding procedure not claimed by same or different physician';

-- update-2014-5-26
CREATE TABLE IF NOT EXISTS `OscarJob` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255),
    `description` VARCHAR(255),
    `oscarJobTypeId` INTEGER,
    `cronExpression` VARCHAR(255),
    `providerNo` VARCHAR(10),
    `enabled` TINYINT(1) NOT NULL,
    `updated` DATETIME NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


CREATE TABLE IF NOT EXISTS `OscarJobType` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255),
    `description` VARCHAR(255),
    `className` VARCHAR(255),
    `enabled` TINYINT(1) NOT NULL,
    `updated` DATETIME NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- update-2014-06-02-vietname omitted

-- update-2014-6-12
alter table drugs add dispenseInternal tinyint(1);
alter table favorites add dispenseInternal tinyint(1) not null;

create table DrugProduct(
	id int(9) NOT NULL auto_increment, 
	name varchar(255),
	code varchar(255),
	lotNumber varchar(255),
	dispensingEvent int(9),
	amount int not null,
	expiryDate date,
	location int,
	primary key (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

create table DrugDispensing (
	id int(9) not null auto_increment,
	drugId int(9),
	dateCreated datetime,
	productId int(9),
	quantity int(9),
	unit varchar(20),
	dispensingProviderNo varchar(20),
	providerNo varchar(20),
	paidFor tinyint(1),
	notes text,
	programNo int,
	primary key(id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

insert into `secObjectName` (`objectName`) values('_rx.dispense');
insert into `secObjPrivilege` values('doctor','_rx.dispense','x',0,'999998');


create table DrugDispensingMapping (
        id int(9) not null auto_increment,
        din varchar(50),
	duration varchar(255),
	durUnit char(1),
	freqCode varchar(6),
	quantity varchar(20),
	takeMin float,
	takeMax float,
        productCode varchar(255),
        dateCreated datetime,
        primary key(id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


-- update-2014-6-20
ALTER TABLE `appointment_status` 
	ADD COLUMN `short_letter_colour` INT(11) NULL COMMENT 'The colour of the short letters in the system'  AFTER `editable` , 
	ADD COLUMN `short_letters` VARCHAR(5) NULL COMMENT 'The short letter representation of the appointment status'  AFTER `short_letter_colour` ;


-- update-2014-6-24
create table ORNCkdScreeningReportLog (
  id int(10) NOT NULL auto_increment,
  providerNo varchar(10) not null,
  reportData text not null,
  lastUpdateDate datetime not null,
  primary key(id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


-- update-2014-7-7
ALTER TABLE property MODIFY value VARCHAR(2000);


-- update-2014-7-25
update drugs set dispenseInternal =0 where dispenseInternal is null;
alter table drugs modify dispenseInternal tinyint(1) not null;


-- update-2014-8-1
CREATE TABLE demographiccustArchive (
  id int(10) AUTO_INCREMENT,
  demographic_no int(10) NOT NULL,
  cust1 varchar(255),
  cust2 varchar(255),
  cust3 varchar(255),
  cust4 varchar(255),
  content text,
  PRIMARY KEY  (id)
)  ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;



-- update-2014-8-20
alter table hl7TextInfo modify accessionNum varchar(255);


-- update-2014-8-25
CREATE TABLE ProductLocation (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255),
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;


-- update-2014-9-05
DROP TABLE IF EXISTS `faxes`;
CREATE TABLE `faxes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(255),
  `faxline` varchar(11),
  `destination` varchar(11),
  `status` varchar(32),
  `document` text,
  `numPages` int(11),
  `stamp` datetime,
  `user` varchar(255),
  `jobId` int(11),
  `oscarUser` varchar(6),
  PRIMARY KEY (`id`),
  KEY `faxline` (`faxline`),
  KEY `faxstatus` (`status`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

CREATE TABLE `fax_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255),
  `siteUser` varchar(255),
  `passwd` varchar(255),
  `faxUser` varchar(255),
  `faxPasswd` varchar(255),
  `queue` varchar(255),
  `active` tinyint(1),
  `faxNumber` varchar(10),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;




-- update-2014-9-29
alter table reportTemplates add sequence tinyint(1);




-- from update-2014-10-10.sql

UPDATE demographicPharmacy, pharmacyInfo SET demographicPharmacy.pharmacyID = pharmacyInfo.recordID WHERE demographicPharmacy.pharmacyID = pharmacyInfo.ID;	
ALTER TABLE pharmacyInfo DROP column ID;
ALTER TABLE demographicPharmacy ADD column preferredOrder int(10);
UPDATE demographicPharmacy set preferredOrder = 1; 


-- update-2014-11-24.sql
alter table preventions modify prevention_date datetime ;

-- update-2014-11-27.sql
alter table casemgmt_note_lock modify `ip_address` varchar(64);
alter table log modify `ip` varchar(64);

-- update-2014-11-28-pain-opiod-mtypes.sql
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'OPAE', 'Opioid Adverse Effects', 'Opioid Adverse Effects', 'null', '17', '2014-11-27 13:00:00'),
( 'OPAB', 'Opioid Aberrant Behaviour', 'Opioid Aberrant Behaviour', 'null', '17', '2014-11-27 13:00:00'),
( 'OPUS', 'Opioid Urine Drug Screen', 'Opioid Urine Drug Screen', 'null', '17', '2014-11-27 13:00:00'),
( 'DMOE', 'Daily Morphine Equivalent', 'Daily Morphine Equivalent', 'null', '11', '2014-11-27 13:00:00');

-- update-2014-11-28.sql
CREATE TABLE formONAREnhancedRecord (
  ID int(10) NOT NULL  auto_increment,
  episodeId int(10),
  sent_to_born tinyint(1) default 0,
  obxhx_num varchar(10) default '0',
  rf_num varchar(10) default '0',
  sv_num varchar(10) default '0',
  us_num varchar(10) default '0',
  demographic_no int(10) NOT NULL default '0',
  provider_no varchar(10)  default NULL,
  formCreated date  default NULL,
  formEdited timestamp,
  c_lastName varchar(80),
  c_firstName varchar(80),
  c_address varchar(80),
  c_apt varchar(20),
  c_city varchar(80),
  c_province varchar(80),
  c_postal varchar(10),
  c_partnerLastName varchar(80),
  c_partnerFirstName varchar(80),
  pg1_homePhone varchar(20),
  pg1_workPhone varchar(20),
  pg1_language varchar(25),
  pg1_partnerOccupation varchar(25),
  c_partnerOccupationOther varchar(255),
  pg1_partnerEduLevel varchar(25),
  pg1_partnerAge varchar(5),
  pg1_dateOfBirth date,
  pg1_age varchar(10),
  pg1_occupation varchar(25),
  pg1_occupationOther varchar(255),
  pg1_eduLevel varchar(25),
  pg1_ethnicBgMother varchar(200),
  pg1_ethnicBgFather varchar(200),
  c_hin varchar(20),
  c_hinType varchar(20),
  c_fileNo varchar(20),
  pg1_maritalStatus varchar(20) default NULL,
  pg1_msSingle tinyint(1) default NULL,
  pg1_msCommonLaw tinyint(1) default NULL,
  pg1_msMarried tinyint(1) default NULL,
  pg1_baObs tinyint(1) default NULL,
  pg1_baFP tinyint(1) default NULL,
  pg1_baMidwife tinyint(1) default NULL,
  c_ba varchar(25) default NULL,
  pg1_ncPed tinyint(1) default NULL,
  pg1_ncFP tinyint(1) default NULL,
  pg1_ncMidwife tinyint(1) default NULL,
  c_nc varchar(25) default NULL,
  c_famPhys varchar(80) default NULL,
  c_allergies text,
  c_meds text,
  pg1_menLMP date,
  pg1_psCertY tinyint(1),
  pg1_psCertN tinyint(1),
  pg1_menCycle varchar(7),
  pg1_menReg tinyint(1),
  pg1_menRegN tinyint(1),
  pg1_contracep varchar(25),
  pg1_lastUsed date,
  pg1_menEDB date,
  c_finalEDB date,
  pg1_edbByDate tinyint(1),
  pg1_edbByT1 tinyint(1),
  pg1_edbByT2 tinyint(1),
  pg1_edbByART tinyint(1),
  c_gravida varchar(3) default NULL,
  c_term varchar(3) default NULL,
  c_prem varchar(3) default NULL,
  c_abort varchar(3) default NULL,
  c_living varchar(3) default NULL,

  pg1_year1 varchar(10) default NULL,
  pg1_sex1 char(1) default NULL,
  pg1_oh_gest1 varchar(5) default NULL,
  pg1_weight1 varchar(6) default NULL,
  pg1_length1 varchar(6) default NULL,
  pg1_place1 varchar(20) default NULL,
  pg1_svb1 tinyint(1) default NULL,
  pg1_cs1 tinyint(1) default NULL,
  pg1_ass1 tinyint(1) default NULL,
  pg1_oh_comments1 varchar(80) default NULL,
  pg1_year2 varchar(10) default NULL,
  pg1_sex2 char(1) default NULL,
  pg1_oh_gest2 varchar(5) default NULL,
  pg1_weight2 varchar(6) default NULL,
  pg1_length2 varchar(6) default NULL,
  pg1_place2 varchar(20) default NULL,
  pg1_svb2 tinyint(1) default NULL,
  pg1_cs2 tinyint(1) default NULL,
  pg1_ass2 tinyint(1) default NULL,
  pg1_oh_comments2 varchar(80) default NULL,
  pg1_year3 varchar(10) default NULL,
  pg1_sex3 char(1) default NULL,
  pg1_oh_gest3 varchar(5) default NULL,
  pg1_weight3 varchar(6) default NULL,
  pg1_length3 varchar(6) default NULL,
  pg1_place3 varchar(20) default NULL,
  pg1_svb3 tinyint(1) default NULL,
  pg1_cs3 tinyint(1) default NULL,
  pg1_ass3 tinyint(1) default NULL,
  pg1_oh_comments3 varchar(80) default NULL,
  pg1_year4 varchar(10) default NULL,
  pg1_sex4 char(1) default NULL,
  pg1_oh_gest4 varchar(5) default NULL,
  pg1_weight4 varchar(6) default NULL,
  pg1_length4 varchar(6) default NULL,
  pg1_place4 varchar(20) default NULL,
  pg1_svb4 tinyint(1) default NULL,
  pg1_cs4 tinyint(1) default NULL,
  pg1_ass4 tinyint(1) default NULL,
  pg1_oh_comments4 varchar(80) default NULL,
  pg1_year5 varchar(10) default NULL,
  pg1_sex5 char(1) default NULL,
  pg1_oh_gest5 varchar(5) default NULL,
  pg1_weight5 varchar(6) default NULL,
  pg1_length5 varchar(6) default NULL,
  pg1_place5 varchar(20) default NULL,
  pg1_svb5 tinyint(1) default NULL,
  pg1_cs5 tinyint(1) default NULL,
  pg1_ass5 tinyint(1) default NULL,
  pg1_oh_comments5 varchar(80) default NULL,
  pg1_year6 varchar(10) default NULL,
  pg1_sex6 char(1) default NULL,
  pg1_oh_gest6 varchar(5) default NULL,
  pg1_weight6 varchar(6) default NULL,
  pg1_length6 varchar(6) default NULL,
  pg1_place6 varchar(20) default NULL,
  pg1_svb6 tinyint(1) default NULL,
  pg1_cs6 tinyint(1) default NULL,
  pg1_ass6 tinyint(1) default NULL,
  pg1_oh_comments6 varchar(80) default NULL,
  pg1_year7 varchar(10) default NULL,
  pg1_sex7 char(1) default NULL,
  pg1_oh_gest7 varchar(5) default NULL,
  pg1_weight7 varchar(6) default NULL,
  pg1_length7 varchar(6) default NULL,
  pg1_place7 varchar(20) default NULL,
  pg1_svb7 tinyint(1) default NULL,
  pg1_cs7 tinyint(1) default NULL,
  pg1_ass7 tinyint(1) default NULL,
  pg1_oh_comments7 varchar(80) default NULL,
  pg1_year8 varchar(10) default NULL,
  pg1_sex8 char(1) default NULL,
  pg1_oh_gest8 varchar(5) default NULL,
  pg1_weight8 varchar(6) default NULL,
  pg1_length8 varchar(6) default NULL,
  pg1_place8 varchar(20) default NULL,
  pg1_svb8 tinyint(1) default NULL,
  pg1_cs8 tinyint(1) default NULL,
  pg1_ass8 tinyint(1) default NULL,
  pg1_oh_comments8 varchar(80) default NULL,

  pg1_year9 varchar(10) default NULL,
  pg1_sex9 char(1) default NULL,
  pg1_oh_gest9 varchar(5) default NULL,
  pg1_weight9 varchar(6) default NULL,
  pg1_length9 varchar(6) default NULL,
  pg1_place9 varchar(20) default NULL,
  pg1_svb9 tinyint(1) default NULL,
  pg1_cs9 tinyint(1) default NULL,
  pg1_ass9 tinyint(1) default NULL,
  pg1_oh_comments9 varchar(80) default NULL,

  pg1_year10 varchar(10) default NULL,
  pg1_sex10 char(1) default NULL,
  pg1_oh_gest10 varchar(5) default NULL,
  pg1_weight10 varchar(6) default NULL,
  pg1_length10 varchar(6) default NULL,
  pg1_place10 varchar(20) default NULL,
  pg1_svb10 tinyint(1) default NULL,
  pg1_cs10 tinyint(1) default NULL,
  pg1_ass10 tinyint(1) default NULL,
  pg1_oh_comments10 varchar(80) default NULL,

  pg1_year11 varchar(10) default NULL,
  pg1_sex11 char(1) default NULL,
  pg1_oh_gest11 varchar(5) default NULL,
  pg1_weight11 varchar(6) default NULL,
  pg1_length11 varchar(6) default NULL,
  pg1_place11 varchar(20) default NULL,
  pg1_svb11 tinyint(1) default NULL,
  pg1_cs11 tinyint(1) default NULL,
  pg1_ass11 tinyint(1) default NULL,
  pg1_oh_comments11 varchar(80) default NULL,

  pg1_year12 varchar(10) default NULL,
  pg1_sex12 char(1) default NULL,
  pg1_oh_gest12 varchar(5) default NULL,
  pg1_weight12 varchar(6) default NULL,
  pg1_length12 varchar(6) default NULL,
  pg1_place12 varchar(20) default NULL,
  pg1_svb12 tinyint(1) default NULL,
  pg1_cs12 tinyint(1) default NULL,
  pg1_ass12 tinyint(1) default NULL,
  pg1_oh_comments12 varchar(80) default NULL,

  pg1_cp1 tinyint(1) default NULL,
  pg1_cp1N tinyint(1) default NULL,
  pg1_cp2 tinyint(1) default NULL,
  pg1_cp2N tinyint(1) default NULL,
  pg1_box3 varchar(10) default NULL,
  pg1_cp3 tinyint(1) default NULL,
  pg1_cp3N tinyint(1) default NULL,
  pg1_cp4 tinyint(1) default NULL,
  pg1_cp4N tinyint(1) default NULL,
  pg1_cp8 tinyint(1) default NULL,
  pg1_cp8N tinyint(1) default NULL,
  pg1_naDiet tinyint(1) default NULL,
  pg1_naDietN tinyint(1) default NULL,
  pg1_naMilk tinyint(1) default NULL,
  pg1_naMilkN tinyint(1) default NULL,
  pg1_naFolic tinyint(1) default NULL,
  pg1_naFolicN tinyint(1) default NULL,
  pg1_yes9 tinyint(1) default NULL,
  pg1_no9 tinyint(1) default NULL,
  pg1_yes10 tinyint(1) default NULL,
  pg1_no10 tinyint(1) default NULL,
  pg1_yes12 tinyint(1) default NULL,
  pg1_no12 tinyint(1) default NULL,
  pg1_yes13 tinyint(1) default NULL,
  pg1_no13 tinyint(1) default NULL,
  pg1_yes14 tinyint(1) default NULL,
  pg1_no14 tinyint(1) default NULL,
  pg1_yes17 tinyint(1) default NULL,
  pg1_no17 tinyint(1) default NULL,
  pg1_yes22 tinyint(1) default NULL,
  pg1_no22 tinyint(1) default NULL,
  pg1_yes20 tinyint(1) default NULL,
  pg1_no20 tinyint(1) default NULL,
  pg1_bloodTranY tinyint(1) default NULL,
  pg1_bloodTranN tinyint(1) default NULL,
  pg1_yes21 tinyint(1) default NULL,
  pg1_no21 tinyint(1) default NULL,
  pg1_yes24 tinyint(1) default NULL,
  pg1_no24 tinyint(1) default NULL,
  pg1_yes15 tinyint(1) default NULL,
  pg1_no15 tinyint(1) default NULL,
  pg1_box25  varchar(25) default NULL,
  pg1_yes25 tinyint(1) default NULL,
  pg1_no25 tinyint(1) default NULL,
  pg1_yes27 tinyint(1) default NULL,
  pg1_no27 tinyint(1) default NULL,
  pg1_yes31 tinyint(1) default NULL,
  pg1_no31 tinyint(1) default NULL,
  pg1_yes32 tinyint(1) default NULL,
  pg1_no32 tinyint(1) default NULL,
  pg1_yes34 tinyint(1) default NULL,
  pg1_no34 tinyint(1) default NULL,
  pg1_yes35 tinyint(1) default NULL,
  pg1_no35 tinyint(1) default NULL,
  pg1_idt40 tinyint(1) default NULL,
  pg1_idt40N tinyint(1) default NULL,
  pg1_idt38 tinyint(1) default NULL,
  pg1_idt38N tinyint(1) default NULL,
  pg1_idt42 tinyint(1) default NULL,
  pg1_idt42N tinyint(1) default NULL,
  pg1_infectDisOther varchar(20) default NULL,
  pg1_infectDisOtherY tinyint(1) default NULL,
  pg1_infectDisOtherN tinyint(1) default NULL,
  pg1_pdt43 tinyint(1) default NULL,
  pg1_pdt43N tinyint(1) default NULL,
  pg1_pdt44 tinyint(1) default NULL,
  pg1_pdt44N tinyint(1) default NULL,
  pg1_pdt45 tinyint(1) default NULL,
  pg1_pdt45N tinyint(1) default NULL,
  pg1_pdt46 tinyint(1) default NULL,
  pg1_pdt46N tinyint(1) default NULL,
  pg1_pdt47 tinyint(1) default NULL,
  pg1_pdt47N tinyint(1) default NULL,
  pg1_pdt48 tinyint(1) default NULL,
  pg1_pdt48N tinyint(1) default NULL,
  pg1_reliCultY tinyint(1) default NULL,
  pg1_reliCultN tinyint(1) default NULL,
  pg1_fhRiskY tinyint(1) default NULL,
  pg1_fhRiskN tinyint(1) default NULL,
  pg1_ht varchar(6) default NULL,
  pg1_wt varchar(6) default NULL,
  c_bmi varchar(6) default NULL,
  pg1_BP varchar(10) default NULL,
  pg1_thyroid tinyint(1) default NULL,
  pg1_thyroidA tinyint(1) default NULL,
  pg1_chest tinyint(1) default NULL,
  pg1_chestA tinyint(1) default NULL,
  pg1_breasts tinyint(1) default NULL,
  pg1_breastsA tinyint(1) default NULL,
  pg1_cardio tinyint(1) default NULL,
  pg1_cardioA tinyint(1) default NULL,
  pg1_abdomen tinyint(1) default NULL,
  pg1_abdomenA tinyint(1) default NULL,
  pg1_vari tinyint(1) default NULL,
  pg1_variA tinyint(1) default NULL,
  pg1_extGen tinyint(1) default NULL,
  pg1_extGenA tinyint(1) default NULL,
  pg1_cervix tinyint(1) default NULL,
  pg1_cervixA tinyint(1) default NULL,
  pg1_uterus tinyint(1) default NULL,
  pg1_uterusA tinyint(1) default NULL,
  pg1_uterusBox varchar(3) default NULL,
  pg1_adnexa tinyint(1) default NULL,
  pg1_adnexaA tinyint(1) default NULL,
  pg1_pExOtherDesc varchar(20) default NULL,
  pg1_pExOther tinyint(1) default NULL,
  pg1_pExOtherA tinyint(1) default NULL,

  pg1_labHb varchar(20) default NULL,
  pg1_labHIV varchar(20) default NULL,
  pg1_labMCV varchar(20) default NULL,
  pg1_labHIVCounsel tinyint(1) default NULL,
  pg1_labABO varchar(20) default NULL,
  pg1_labLastPapDate varchar(10) default NULL,
  pg1_labLastPap varchar(20) default NULL,

  pg1_labCustom1Label varchar(40) default NULL,
  pg1_labCustom1Result varchar(40) default NULL,
  pg1_labCustom2Label varchar(40) default NULL,
  pg1_labCustom2Result varchar(40) default NULL,
  pg1_labCustom3Label varchar(40) default NULL,
  pg1_labCustom3Result varchar(40) default NULL,
  pg1_labCustom4Label varchar(40) default NULL,
  pg1_labCustom4Result varchar(40) default NULL,

  pg1_labRh varchar(20) default NULL,
  pg1_labAntiScr varchar(20) default NULL,
  pg1_labGC varchar(20) default NULL,
  pg1_labChlamydia varchar(20) default NULL,
  pg1_labRubella varchar(20) default NULL,
  pg1_labUrine varchar(20) default NULL,
  pg1_labHBsAg varchar(20) default NULL,
  pg1_labVDRL varchar(20) default NULL,
  pg1_labSickle varchar(20) default NULL,
  pg1_geneticA varchar(20) default NULL,
  pg1_geneticB varchar(20) default NULL,
  pg1_geneticC varchar(20) default NULL,
  pg1_geneticD varchar(20) default NULL,
  pg1_geneticD1 tinyint(1) default NULL,
  pg1_geneticD2 tinyint(1) default NULL,
  pg1_commentsAR1 text,
  pg1_comments2AR1 text,
  pg1_signature varchar(50) default NULL,
  pg1_formDate date default NULL,
  pg1_signature2 varchar(50) default NULL,
  pg1_formDate2 date default NULL,

  c_riskFactors1 varchar(50) default NULL,
  c_planManage1 varchar(100) default NULL,
  c_riskFactors2 varchar(50) default NULL,
  c_planManage2 varchar(100) default NULL,
  c_riskFactors3 varchar(50) default NULL,
  c_planManage3 varchar(100) default NULL,
  c_riskFactors4 varchar(50) default NULL,
  c_planManage4 varchar(100) default NULL,
  c_riskFactors5 varchar(50) default NULL,
  c_planManage5 varchar(100) default NULL,
  c_riskFactors6 varchar(50) default NULL,
  c_planManage6 varchar(100) default NULL,
  c_riskFactors7 varchar(50) default NULL,
  c_planManage7 varchar(100) default NULL,


  c_riskFactors8 varchar(50) default NULL,
  c_planManage8 varchar(100) default NULL,
  c_riskFactors9 varchar(50) default NULL,
  c_planManage9 varchar(100) default NULL,
  c_riskFactors10 varchar(50) default NULL,
  c_planManage10 varchar(100) default NULL,
  c_riskFactors11 varchar(50) default NULL,
  c_planManage11 varchar(100) default NULL,
  c_riskFactors12 varchar(50) default NULL,
  c_planManage12 varchar(100) default NULL,
  c_riskFactors13 varchar(50) default NULL,
  c_planManage13 varchar(100) default NULL,
  c_riskFactors14 varchar(50) default NULL,
  c_planManage14 varchar(100) default NULL,
  c_riskFactors15 varchar(50) default NULL,
  c_planManage15 varchar(100) default NULL,
  c_riskFactors16 varchar(50) default NULL,
  c_planManage16 varchar(100) default NULL,
  c_riskFactors17 varchar(50) default NULL,
  c_planManage17 varchar(100) default NULL,
  c_riskFactors18 varchar(50) default NULL,
  c_planManage18 varchar(100) default NULL,
  c_riskFactors19 varchar(50) default NULL,
  c_planManage19 varchar(100) default NULL,
  c_riskFactors20 varchar(50) default NULL,
  c_planManage20 varchar(100) default NULL,
  PRIMARY KEY (ID)
);


CREATE TABLE formONAREnhancedRecordExt1 (
  ID int(10) NOT NULL ,
  ar2_rhNeg tinyint(1) default NULL,
  ar2_rhIG varchar(10) default NULL,
  ar2_rubella tinyint(1) default NULL,
  ar2_hepBIG tinyint(1) default NULL,
  ar2_hepBVac tinyint(1) default NULL,
  `pg2_date1` date default NULL,
  `pg2_gest1` varchar(6),
  `pg2_wt1` varchar(6),
  `pg2_BP1` varchar(8),
  `pg2_urinePr1` char(3),
  `pg2_urineGl1` char(3),
  `pg2_ht1` varchar(6),
  `pg2_presn1` varchar(6),
  `pg2_FHR1` varchar(6),
  `pg2_comments1` varchar(80),
  `pg2_date2` date,
  `pg2_gest2` varchar(6),
  `pg2_ht2` varchar(6),
  `pg2_wt2` varchar(6),
  `pg2_presn2` varchar(6),
  `pg2_FHR2` varchar(6),
  `pg2_urinePr2` char(3),
  `pg2_urineGl2` char(3),
  `pg2_BP2` varchar(8),
  `pg2_comments2` varchar(80),
  `pg2_date3` date,
  `pg2_gest3` varchar(6),
  `pg2_ht3` varchar(6),
  `pg2_wt3` varchar(6),
  `pg2_presn3` varchar(6),
  `pg2_FHR3` varchar(6),
  `pg2_urinePr3` char(3),
  `pg2_urineGl3` char(3),
  `pg2_BP3` varchar(8),
  `pg2_comments3` varchar(80),
  `pg2_date4` date,
  `pg2_gest4` varchar(6),
  `pg2_ht4` varchar(6),
  `pg2_wt4` varchar(6),
  `pg2_presn4` varchar(6),
  `pg2_FHR4` varchar(6),
  `pg2_urinePr4` char(3),
  `pg2_urineGl4` char(3),
  `pg2_BP4` varchar(8),
  `pg2_comments4` varchar(80),
  `pg2_date5` date,
  `pg2_gest5` varchar(6),
  `pg2_ht5` varchar(6),
  `pg2_wt5` varchar(6),
  `pg2_presn5` varchar(6),
  `pg2_FHR5` varchar(6),
  `pg2_urinePr5` char(3),
  `pg2_urineGl5` char(3),
  `pg2_BP5` varchar(8),
  `pg2_comments5` varchar(80),
  `pg2_date6` date,
  `pg2_gest6` varchar(6),
  `pg2_ht6` varchar(6),
  `pg2_wt6` varchar(6),
  `pg2_presn6` varchar(6),
  `pg2_FHR6` varchar(6),
  `pg2_urinePr6` char(3),
  `pg2_urineGl6` char(3),
  `pg2_BP6` varchar(8),
  `pg2_comments6` varchar(80),
  `pg2_date7` date,
  `pg2_gest7` varchar(6),
  `pg2_ht7` varchar(6),
  `pg2_wt7` varchar(6),
  `pg2_presn7` varchar(6),
  `pg2_FHR7` varchar(6),
  `pg2_urinePr7` char(3),
  `pg2_urineGl7` char(3),
  `pg2_BP7` varchar(8),
  `pg2_comments7` varchar(80),
  `pg2_date8` date,
  `pg2_gest8` varchar(6),
  `pg2_ht8` varchar(6),
  `pg2_wt8` varchar(6),
  `pg2_presn8` varchar(6),
  `pg2_FHR8` varchar(6),
  `pg2_urinePr8` char(3),
  `pg2_urineGl8` char(3),
  `pg2_BP8` varchar(8),
  `pg2_comments8` varchar(80),
  `pg2_date9` date,
  `pg2_gest9` varchar(6),
  `pg2_ht9` varchar(6),
  `pg2_wt9` varchar(6),
  `pg2_presn9` varchar(6),
  `pg2_FHR9` varchar(6),
  `pg2_urinePr9` char(3),
  `pg2_urineGl9` char(3),
  `pg2_BP9` varchar(8),
  `pg2_comments9` varchar(80),
  `pg2_date10` date,
  `pg2_gest10` varchar(6),
  `pg2_ht10` varchar(6),
  `pg2_wt10` varchar(6),
  `pg2_presn10` varchar(6),
  `pg2_FHR10` varchar(6),
  `pg2_urinePr10` char(3),
  `pg2_urineGl10` char(3),
  `pg2_BP10` varchar(8),
  `pg2_comments10` varchar(80),
  `pg2_date11` date,
  `pg2_gest11` varchar(6),
  `pg2_ht11` varchar(6),
  `pg2_wt11` varchar(6),
  `pg2_presn11` varchar(6),
  `pg2_FHR11` varchar(6),
  `pg2_urinePr11` char(3),
  `pg2_urineGl11` char(3),
  `pg2_BP11` varchar(8),
  `pg2_comments11` varchar(80),
  `pg2_date12` date,
  `pg2_gest12` varchar(6),
  `pg2_ht12` varchar(6),
  `pg2_wt12` varchar(6),
  `pg2_presn12` varchar(6),
  `pg2_FHR12` varchar(6),
  `pg2_urinePr12` char(3),
  `pg2_urineGl12` char(3),
  `pg2_BP12` varchar(8),
  `pg2_comments12` varchar(80),
  `pg2_date13` date,
  `pg2_gest13` varchar(6),
  `pg2_ht13` varchar(6),
  `pg2_wt13` varchar(6),
  `pg2_presn13` varchar(6),
  `pg2_FHR13` varchar(6),
  `pg2_urinePr13` char(3),
  `pg2_urineGl13` char(3),
  `pg2_BP13` varchar(8),
  `pg2_comments13` varchar(80),
  `pg2_date14` date,
  `pg2_gest14` varchar(6),
  `pg2_ht14` varchar(6),
  `pg2_wt14` varchar(6),
  `pg2_presn14` varchar(6),
  `pg2_FHR14` varchar(6),
  `pg2_urinePr14` char(3),
  `pg2_urineGl14` char(3),
  `pg2_BP14` varchar(8),
  `pg2_comments14` varchar(80),
  `pg2_date15` date,
  `pg2_gest15` varchar(6),
  `pg2_ht15` varchar(6),
  `pg2_wt15` varchar(6),
  `pg2_presn15` varchar(6),
  `pg2_FHR15` varchar(6),
  `pg2_urinePr15` char(3),
  `pg2_urineGl15` char(3),
  `pg2_BP15` varchar(8),
  `pg2_comments15` varchar(80),
  `pg2_date16` date,
  `pg2_gest16` varchar(6),
  `pg2_ht16` varchar(6),
  `pg2_wt16` varchar(6),
  `pg2_presn16` varchar(6),
  `pg2_FHR16` varchar(6),
  `pg2_urinePr16` char(3),
  `pg2_urineGl16` char(3),
  `pg2_BP16` varchar(8),
  `pg2_comments16` varchar(80),
  `pg2_date17` date,
  `pg2_gest17` varchar(6),
  `pg2_ht17` varchar(6),
  `pg2_wt17` varchar(6),
  `pg2_presn17` varchar(6),
  `pg2_FHR17` varchar(6),
  `pg2_urinePr17` char(3),
  `pg2_urineGl17` char(3),
  `pg2_BP17` varchar(8),
  `pg2_comments17` varchar(80),
  `pg2_date18` date,
  `pg2_gest18` varchar(6),
  `pg2_ht18` varchar(6),
  `pg2_wt18` varchar(6),
  `pg2_presn18` varchar(6),
  `pg2_FHR18` varchar(6),
  `pg2_urinePr18` char(3),
  `pg2_urineGl18` char(3),
  `pg2_BP18` varchar(8),
  `pg2_comments18` varchar(80),
  `pg2_date19` date,
  `pg2_gest19` varchar(6),
  `pg2_ht19` varchar(6),
  `pg2_wt19` varchar(6),
  `pg2_presn19` varchar(6),
  `pg2_FHR19` varchar(6),
  `pg2_urinePr19` char(3),
  `pg2_urineGl19` char(3),
  `pg2_BP19` varchar(8),
  `pg2_comments19` varchar(80),
  `pg2_date20` date,
  `pg2_gest20` varchar(6),
  `pg2_ht20` varchar(6),
  `pg2_wt20` varchar(6),
  `pg2_presn20` varchar(6),
  `pg2_FHR20` varchar(6),
  `pg2_urinePr20` char(3),
  `pg2_urineGl20` char(3),
  `pg2_BP20` varchar(8),
  `pg2_comments20` varchar(80),
  `pg2_date21` date,
  `pg2_gest21` varchar(6),
  `pg2_ht21` varchar(6),
  `pg2_wt21` varchar(6),
  `pg2_presn21` varchar(6),
  `pg2_FHR21` varchar(6),
  `pg2_urinePr21` char(3),
  `pg2_urineGl21` char(3),
  `pg2_BP21` varchar(8),
  `pg2_comments21` varchar(80),
  `pg2_date22` date,
  `pg2_gest22` varchar(6),
  `pg2_ht22` varchar(6),
  `pg2_wt22` varchar(6),
  `pg2_presn22` varchar(6),
  `pg2_FHR22` varchar(6),
  `pg2_urinePr22` char(3),
  `pg2_urineGl22` char(3),
  `pg2_BP22` varchar(8),
  `pg2_comments22` varchar(80),
  `pg2_date23` date,
  `pg2_gest23` varchar(6),
  `pg2_ht23` varchar(6),
  `pg2_wt23` varchar(6),
  `pg2_presn23` varchar(6),
  `pg2_FHR23` varchar(6),
  `pg2_urinePr23` char(3),
  `pg2_urineGl23` char(3),
  `pg2_BP23` varchar(8),
  `pg2_comments23` varchar(80),
  `pg2_date24` date,
  `pg2_gest24` varchar(6),
  `pg2_ht24` varchar(6),
  `pg2_wt24` varchar(6),
  `pg2_presn24` varchar(6),
  `pg2_FHR24` varchar(6),
  `pg2_urinePr24` char(3),
  `pg2_urineGl24` char(3),
  `pg2_BP24` varchar(8),
  `pg2_comments24` varchar(80),
  `pg2_date25` date,
  `pg2_gest25` varchar(6),
  `pg2_ht25` varchar(6),
  `pg2_wt25` varchar(6),
  `pg2_presn25` varchar(6),
  `pg2_FHR25` varchar(6),
  `pg2_urinePr25` char(3),
  `pg2_urineGl25` char(3),
  `pg2_BP25` varchar(8),
  `pg2_comments25` varchar(80),
  `pg2_date26` date,
  `pg2_gest26` varchar(6),
  `pg2_ht26` varchar(6),
  `pg2_wt26` varchar(6),
  `pg2_presn26` varchar(6),
  `pg2_FHR26` varchar(6),
  `pg2_urinePr26` char(3),
  `pg2_urineGl26` char(3),
  `pg2_BP26` varchar(8),
  `pg2_comments26` varchar(80),
  `pg2_date27` date,
  `pg2_gest27` varchar(6),
  `pg2_ht27` varchar(6),
  `pg2_wt27` varchar(6),
  `pg2_presn27` varchar(6),
  `pg2_FHR27` varchar(6),
  `pg2_urinePr27` char(3),
  `pg2_urineGl27` char(3),
  `pg2_BP27` varchar(8),
  `pg2_comments27` varchar(80),
  `pg2_date28` date,
  `pg2_gest28` varchar(6),
  `pg2_ht28` varchar(6),
  `pg2_wt28` varchar(6),
  `pg2_presn28` varchar(6),
  `pg2_FHR28` varchar(6),
  `pg2_urinePr28` char(3),
  `pg2_urineGl28` char(3),
  `pg2_BP28` varchar(8),
  `pg2_comments28` varchar(80),
  `pg2_date29` date,
  `pg2_gest29` varchar(6),
  `pg2_ht29` varchar(6),
  `pg2_wt29` varchar(6),
  `pg2_presn29` varchar(6),
  `pg2_FHR29` varchar(6),
  `pg2_urinePr29` char(3),
  `pg2_urineGl29` char(3),
  `pg2_BP29` varchar(8),
  `pg2_comments29` varchar(80),
  `pg2_date30` date,
  `pg2_gest30` varchar(6),
  `pg2_ht30` varchar(6),
  `pg2_wt30` varchar(6),
  `pg2_presn30` varchar(6),
  `pg2_FHR30` varchar(6),
  `pg2_urinePr30` char(3),
  `pg2_urineGl30` char(3),
  `pg2_BP30` varchar(8),
  `pg2_comments30` varchar(80),
  `pg2_date31` date,
  `pg2_gest31` varchar(6),
  `pg2_ht31` varchar(6),
  `pg2_wt31` varchar(6),
  `pg2_presn31` varchar(6),
  `pg2_FHR31` varchar(6),
  `pg2_urinePr31` char(3),
  `pg2_urineGl31` char(3),
  `pg2_BP31` varchar(8),
  `pg2_comments31` varchar(80),
  `pg2_date32` date,
  `pg2_gest32` varchar(6),
  `pg2_ht32` varchar(6),
  `pg2_wt32` varchar(6),
  `pg2_presn32` varchar(6),
  `pg2_FHR32` varchar(6),
  `pg2_urinePr32` char(3),
  `pg2_urineGl32` char(3),
  `pg2_BP32` varchar(8),
  `pg2_comments32` varchar(80),
  `pg2_date33` date,
  `pg2_gest33` varchar(6),
  `pg2_ht33` varchar(6),
  `pg2_wt33` varchar(6),
  `pg2_presn33` varchar(6),
  `pg2_FHR33` varchar(6),
  `pg2_urinePr33` char(3),
  `pg2_urineGl33` char(3),
  `pg2_BP33` varchar(8),
  `pg2_comments33` varchar(80),
  `pg2_date34` date,
  `pg2_gest34` varchar(6),
  `pg2_ht34` varchar(6),
  `pg2_wt34` varchar(6),
  `pg2_presn34` varchar(6),
  `pg2_FHR34` varchar(6),
  `pg2_urinePr34` char(3),
  `pg2_urineGl34` char(3),
  `pg2_BP34` varchar(8),
  `pg2_comments34` varchar(80),
  `pg2_date35` date,
  `pg2_gest35` varchar(6),
  `pg2_ht35` varchar(6),
  `pg2_wt35` varchar(6),
  `pg2_presn35` varchar(6),
  `pg2_FHR35` varchar(6),
  `pg2_urinePr35` char(3),
  `pg2_urineGl35` char(3),
  `pg2_BP35` varchar(8),
  `pg2_comments35` varchar(80),
  `pg2_date36` date,
  `pg2_gest36` varchar(6),
  `pg2_ht36` varchar(6),
  `pg2_wt36` varchar(6),
  `pg2_presn36` varchar(6),
  `pg2_FHR36` varchar(6),
  `pg2_urinePr36` char(3),
  `pg2_urineGl36` char(3),
  `pg2_BP36` varchar(8),
  `pg2_comments36` varchar(80),
  `pg2_date37` date,
  `pg2_gest37` varchar(6),
  `pg2_ht37` varchar(6),
  `pg2_wt37` varchar(6),
  `pg2_presn37` varchar(6),
  `pg2_FHR37` varchar(6),
  `pg2_urinePr37` char(3),
  `pg2_urineGl37` char(3),
  `pg2_BP37` varchar(8),
  `pg2_comments37` varchar(80),
  `pg2_date38` date,
  `pg2_gest38` varchar(6),
  `pg2_ht38` varchar(6),
  `pg2_wt38` varchar(6),
  `pg2_presn38` varchar(6),
  `pg2_FHR38` varchar(6),
  `pg2_urinePr38` char(3),
  `pg2_urineGl38` char(3),
  `pg2_BP38` varchar(8),
  `pg2_comments38` varchar(80),
  `pg2_date39` date,
  `pg2_gest39` varchar(6),
  `pg2_ht39` varchar(6),
  `pg2_wt39` varchar(6),
  `pg2_presn39` varchar(6),
  `pg2_FHR39` varchar(6),
  `pg2_urinePr39` char(3),
  `pg2_urineGl39` char(3),
  `pg2_BP39` varchar(8),
  `pg2_comments39` varchar(80),
  `pg2_date40` date,
  `pg2_gest40` varchar(6),
  `pg2_ht40` varchar(6),
  `pg2_wt40` varchar(6),
  `pg2_presn40` varchar(6),
  `pg2_FHR40` varchar(6),
  `pg2_urinePr40` char(3),
  `pg2_urineGl40` char(3),
  `pg2_BP40` varchar(8),
  `pg2_comments40` varchar(80)
);

CREATE TABLE formONAREnhancedRecordExt2 (
  ID int(10) NOT NULL ,
  `pg2_date41` date,
  `pg2_gest41` varchar(6),
  `pg2_ht41` varchar(6),
  `pg2_wt41` varchar(6),
  `pg2_presn41` varchar(6),
  `pg2_FHR41` varchar(6),
  `pg2_urinePr41` char(3),
  `pg2_urineGl41` char(3),
  `pg2_BP41` varchar(8),
  `pg2_comments41` varchar(80),
  `pg2_date42` date,
  `pg2_gest42` varchar(6),
  `pg2_ht42` varchar(6),
  `pg2_wt42` varchar(6),
  `pg2_presn42` varchar(6),
  `pg2_FHR42` varchar(6),
  `pg2_urinePr42` char(3),
  `pg2_urineGl42` char(3),
  `pg2_BP42` varchar(8),
  `pg2_comments42` varchar(80),
  `pg2_date43` date,
  `pg2_gest43` varchar(6),
  `pg2_ht43` varchar(6),
  `pg2_wt43` varchar(6),
  `pg2_presn43` varchar(6),
  `pg2_FHR43` varchar(6),
  `pg2_urinePr43` char(3),
  `pg2_urineGl43` char(3),
  `pg2_BP43` varchar(8),
  `pg2_comments43` varchar(80),
  `pg2_date44` date,
  `pg2_gest44` varchar(6),
  `pg2_ht44` varchar(6),
  `pg2_wt44` varchar(6),
  `pg2_presn44` varchar(6),
  `pg2_FHR44` varchar(6),
  `pg2_urinePr44` char(3),
  `pg2_urineGl44` char(3),
  `pg2_BP44` varchar(8),
  `pg2_comments44` varchar(80),
  `pg2_date45` date,
  `pg2_gest45` varchar(6),
  `pg2_ht45` varchar(6),
  `pg2_wt45` varchar(6),
  `pg2_presn45` varchar(6),
  `pg2_FHR45` varchar(6),
  `pg2_urinePr45` char(3),
  `pg2_urineGl45` char(3),
  `pg2_BP45` varchar(8),
  `pg2_comments45` varchar(80),
  `pg2_date46` date,
  `pg2_gest46` varchar(6),
  `pg2_ht46` varchar(6),
  `pg2_wt46` varchar(6),
  `pg2_presn46` varchar(6),
  `pg2_FHR46` varchar(6),
  `pg2_urinePr46` char(3),
  `pg2_urineGl46` char(3),
  `pg2_BP46` varchar(8),
  `pg2_comments46` varchar(80),
  `pg2_date47` date,
  `pg2_gest47` varchar(6),
  `pg2_ht47` varchar(6),
  `pg2_wt47` varchar(6),
  `pg2_presn47` varchar(6),
  `pg2_FHR47` varchar(6),
  `pg2_urinePr47` char(3),
  `pg2_urineGl47` char(3),
  `pg2_BP47` varchar(8),
  `pg2_comments47` varchar(80),
  `pg2_date48` date,
  `pg2_gest48` varchar(6),
  `pg2_ht48` varchar(6),
  `pg2_wt48` varchar(6),
  `pg2_presn48` varchar(6),
  `pg2_FHR48` varchar(6),
  `pg2_urinePr48` char(3),
  `pg2_urineGl48` char(3),
  `pg2_BP48` varchar(8),
  `pg2_comments48` varchar(80),
  `pg2_date49` date,
  `pg2_gest49` varchar(6),
  `pg2_ht49` varchar(6),
  `pg2_wt49` varchar(6),
  `pg2_presn49` varchar(6),
  `pg2_FHR49` varchar(6),
  `pg2_urinePr49` char(3),
  `pg2_urineGl49` char(3),
  `pg2_BP49` varchar(8),
  `pg2_comments49` varchar(80),
  `pg2_date50` date,
  `pg2_gest50` varchar(6),
  `pg2_ht50` varchar(6),
  `pg2_wt50` varchar(6),
  `pg2_presn50` varchar(6),
  `pg2_FHR50` varchar(6),
  `pg2_urinePr50` char(3),
  `pg2_urineGl50` char(3),
  `pg2_BP50` varchar(8),
  `pg2_comments50` varchar(80),
  `pg2_date51` date,
  `pg2_gest51` varchar(6),
  `pg2_ht51` varchar(6),
  `pg2_wt51` varchar(6),
  `pg2_presn51` varchar(6),
  `pg2_FHR51` varchar(6),
  `pg2_urinePr51` char(3),
  `pg2_urineGl51` char(3),
  `pg2_BP51` varchar(8),
  `pg2_comments51` varchar(80),
  `pg2_date52` date,
  `pg2_gest52` varchar(6),
  `pg2_ht52` varchar(6),
  `pg2_wt52` varchar(6),
  `pg2_presn52` varchar(6),
  `pg2_FHR52` varchar(6),
  `pg2_urinePr52` char(3),
  `pg2_urineGl52` char(3),
  `pg2_BP52` varchar(8),
  `pg2_comments52` varchar(80),
  `pg2_date53` date,
  `pg2_gest53` varchar(6),
  `pg2_ht53` varchar(6),
  `pg2_wt53` varchar(6),
  `pg2_presn53` varchar(6),
  `pg2_FHR53` varchar(6),
  `pg2_urinePr53` char(3),
  `pg2_urineGl53` char(3),
  `pg2_BP53` varchar(8),
  `pg2_comments53` varchar(80),
  `pg2_date54` date,
  `pg2_gest54` varchar(6),
  `pg2_ht54` varchar(6),
  `pg2_wt54` varchar(6),
  `pg2_presn54` varchar(6),
  `pg2_FHR54` varchar(6),
  `pg2_urinePr54` char(3),
  `pg2_urineGl54` char(3),
  `pg2_BP54` varchar(8),
  `pg2_comments54` varchar(80),
  `pg2_date55` date,
  `pg2_gest55` varchar(6),
  `pg2_ht55` varchar(6),
  `pg2_wt55` varchar(6),
  `pg2_presn55` varchar(6),
  `pg2_FHR55` varchar(6),
  `pg2_urinePr55` char(3),
  `pg2_urineGl55` char(3),
  `pg2_BP55` varchar(8),
  `pg2_comments55` varchar(80),
  `pg2_date56` date,
  `pg2_gest56` varchar(6),
  `pg2_ht56` varchar(6),
  `pg2_wt56` varchar(6),
  `pg2_presn56` varchar(6),
  `pg2_FHR56` varchar(6),
  `pg2_urinePr56` char(3),
  `pg2_urineGl56` char(3),
  `pg2_BP56` varchar(8),
  `pg2_comments56` varchar(80),
  `pg2_date57` date,
  `pg2_gest57` varchar(6),
  `pg2_ht57` varchar(6),
  `pg2_wt57` varchar(6),
  `pg2_presn57` varchar(6),
  `pg2_FHR57` varchar(6),
  `pg2_urinePr57` char(3),
  `pg2_urineGl57` char(3),
  `pg2_BP57` varchar(8),
  `pg2_comments57` varchar(80),
  `pg2_date58` date,
  `pg2_gest58` varchar(6),
  `pg2_ht58` varchar(6),
  `pg2_wt58` varchar(6),
  `pg2_presn58` varchar(6),
  `pg2_FHR58` varchar(6),
  `pg2_urinePr58` char(3),
  `pg2_urineGl58` char(3),
  `pg2_BP58` varchar(8),
  `pg2_comments58` varchar(80),
  `pg2_date59` date,
  `pg2_gest59` varchar(6),
  `pg2_ht59` varchar(6),
  `pg2_wt59` varchar(6),
  `pg2_presn59` varchar(6),
  `pg2_FHR59` varchar(6),
  `pg2_urinePr59` char(3),
  `pg2_urineGl59` char(3),
  `pg2_BP59` varchar(8),
  `pg2_comments59` varchar(80),
  `pg2_date60` date,
  `pg2_gest60` varchar(6),
  `pg2_ht60` varchar(6),
  `pg2_wt60` varchar(6),
  `pg2_presn60` varchar(6),
  `pg2_FHR60` varchar(6),
  `pg2_urinePr60` char(3),
  `pg2_urineGl60` char(3),
  `pg2_BP60` varchar(8),
  `pg2_comments60` varchar(80),
  `pg2_date61` date,
  `pg2_gest61` varchar(6),
  `pg2_ht61` varchar(6),
  `pg2_wt61` varchar(6),
  `pg2_presn61` varchar(6),
  `pg2_FHR61` varchar(6),
  `pg2_urinePr61` char(3),
  `pg2_urineGl61` char(3),
  `pg2_BP61` varchar(8),
  `pg2_comments61` varchar(80),
  `pg2_date62` date,
  `pg2_gest62` varchar(6),
  `pg2_ht62` varchar(6),
  `pg2_wt62` varchar(6),
  `pg2_presn62` varchar(6),
  `pg2_FHR62` varchar(6),
  `pg2_urinePr62` char(3),
  `pg2_urineGl62` char(3),
  `pg2_BP62` varchar(8),
  `pg2_comments62` varchar(80),
  `pg2_date63` date,
  `pg2_gest63` varchar(6),
  `pg2_ht63` varchar(6),
  `pg2_wt63` varchar(6),
  `pg2_presn63` varchar(6),
  `pg2_FHR63` varchar(6),
  `pg2_urinePr63` char(3),
  `pg2_urineGl63` char(3),
  `pg2_BP63` varchar(8),
  `pg2_comments63` varchar(80),
  `pg2_date64` date,
  `pg2_gest64` varchar(6),
  `pg2_ht64` varchar(6),
  `pg2_wt64` varchar(6),
  `pg2_presn64` varchar(6),
  `pg2_FHR64` varchar(6),
  `pg2_urinePr64` char(3),
  `pg2_urineGl64` char(3),
  `pg2_BP64` varchar(8),
  `pg2_comments64` varchar(80),
  `pg2_date65` date,
  `pg2_gest65` varchar(6),
  `pg2_ht65` varchar(6),
  `pg2_wt65` varchar(6),
  `pg2_presn65` varchar(6),
  `pg2_FHR65` varchar(6),
  `pg2_urinePr65` char(3),
  `pg2_urineGl65` char(3),
  `pg2_BP65` varchar(8),
  `pg2_comments65` varchar(80),
  `pg2_date66` date,
  `pg2_gest66` varchar(6),
  `pg2_ht66` varchar(6),
  `pg2_wt66` varchar(6),
  `pg2_presn66` varchar(6),
  `pg2_FHR66` varchar(6),
  `pg2_urinePr66` char(3),
  `pg2_urineGl66` char(3),
  `pg2_BP66` varchar(8),
  `pg2_comments66` varchar(80),
  `pg2_date67` date,
  `pg2_gest67` varchar(6),
  `pg2_ht67` varchar(6),
  `pg2_wt67` varchar(6),
  `pg2_presn67` varchar(6),
  `pg2_FHR67` varchar(6),
  `pg2_urinePr67` char(3),
  `pg2_urineGl67` char(3),
  `pg2_BP67` varchar(8),
  `pg2_comments67` varchar(80),
  `pg2_date68` date,
  `pg2_gest68` varchar(6),
  `pg2_ht68` varchar(6),
  `pg2_wt68` varchar(6),
  `pg2_presn68` varchar(6),
  `pg2_FHR68` varchar(6),
  `pg2_urinePr68` char(3),
  `pg2_urineGl68` char(3),
  `pg2_BP68` varchar(8),
  `pg2_comments68` varchar(80),
  `pg2_date69` date,
  `pg2_gest69` varchar(6),
  `pg2_ht69` varchar(6),
  `pg2_wt69` varchar(6),
  `pg2_presn69` varchar(6),
  `pg2_FHR69` varchar(6),
  `pg2_urinePr69` char(3),
  `pg2_urineGl69` char(3),
  `pg2_BP69` varchar(8),
  `pg2_comments69` varchar(80),
  `pg2_date70` date,
  `pg2_gest70` varchar(6),
  `pg2_ht70` varchar(6),
  `pg2_wt70` varchar(6),
  `pg2_presn70` varchar(6),
  `pg2_FHR70` varchar(6),
  `pg2_urinePr70` char(3),
  `pg2_urineGl70` char(3),
  `pg2_BP70` varchar(8),
  `pg2_comments70` varchar(80),

  ar2_uDate1 date,
  ar2_uGA1 varchar(10) default NULL,
  ar2_uResults1 varchar(50) default NULL,
  ar2_uDate2 date,
  ar2_uGA2 varchar(10) default NULL,
  ar2_uResults2 varchar(50) default NULL,
  ar2_uDate3 date,
  ar2_uGA3 varchar(10) default NULL,
  ar2_uResults3 varchar(50) default NULL,
  ar2_uDate4 date,
  ar2_uGA4 varchar(10) default NULL,
  ar2_uResults4 varchar(50) default NULL,

  ar2_uDate5 date,
  ar2_uGA5 varchar(10) default NULL,
  ar2_uResults5 varchar(50) default NULL, 
  ar2_uDate6 date,
  ar2_uGA6 varchar(10) default NULL,
  ar2_uResults6 varchar(50) default NULL,
  ar2_uDate7 date,
  ar2_uGA7 varchar(10) default NULL,
  ar2_uResults7 varchar(50) default NULL,
  ar2_uDate8 date,
  ar2_uGA8 varchar(10) default NULL,
  ar2_uResults8 varchar(50) default NULL,
  ar2_uDate9 date,
  ar2_uGA9 varchar(10) default NULL,
  ar2_uResults9 varchar(50) default NULL,
  ar2_uDate10 date,
  ar2_uGA10 varchar(10) default NULL,
  ar2_uResults10 varchar(50) default NULL,
  ar2_uDate11 date,
  ar2_uGA11 varchar(10) default NULL,
  ar2_uResults11 varchar(50) default NULL,
  ar2_uDate12 date,
  ar2_uGA12 varchar(10) default NULL,
  ar2_uResults12 varchar(50) default NULL,

  ar2_hb varchar(10) default NULL,
  ar2_bloodGroup varchar(6) default NULL,
  ar2_rh varchar(6) default NULL,
  ar2_labABS varchar(10) default NULL,
  ar2_lab1GCT varchar(10) default NULL,
  ar2_lab2GTT varchar(14) default NULL,
  ar2_strep varchar(10) default NULL,

  ar2_labCustom1Label varchar(40) default NULL,
  ar2_labCustom1Result varchar(40) default NULL,
  ar2_labCustom2Label varchar(40) default NULL,
  ar2_labCustom2Result varchar(40) default NULL,
  ar2_labCustom3Label varchar(40) default NULL,
  ar2_labCustom3Result varchar(40) default NULL,
  ar2_labCustom4Label varchar(40) default NULL,
  ar2_labCustom4Result varchar(40) default NULL,

  ar2_exercise tinyint(1) default NULL,
  ar2_workPlan tinyint(1) default NULL,
  ar2_intercourse tinyint(1) default NULL,
  ar2_travel tinyint(1) default NULL,
  ar2_prenatal tinyint(1) default NULL,
  ar2_birth tinyint(1) default NULL,
  ar2_onCall tinyint(1) default NULL,
  ar2_preterm tinyint(1) default NULL,
  ar2_prom tinyint(1) default NULL,
  ar2_aph tinyint(1) default NULL,
  ar2_fetal tinyint(1) default NULL,
  ar2_admission tinyint(1) default NULL,
  ar2_pain tinyint(1) default NULL,
  ar2_labour tinyint(1) default NULL,
  ar2_breast tinyint(1) default NULL,
  ar2_circumcision tinyint(1) default NULL,
  ar2_dischargePlan tinyint(1) default NULL,
  ar2_car tinyint(1) default NULL,
  ar2_depression tinyint(1) default NULL,
  ar2_contraception tinyint(1) default NULL,
  ar2_postpartumCare tinyint(1) default NULL,
  pg2_signature varchar(50) default NULL,
  pg2_formDate date default NULL,
  pg2_signature2 varchar(50) default NULL,
  pg2_formDate2 date default NULL,
  pg1_geneticA_riskLevel varchar(25),
  pg1_geneticB_riskLevel varchar(25),
  pg1_geneticC_riskLevel varchar(25),
  pg1_labCustom3Result_riskLevel varchar(25)
);


update encounterForm set  form_table='formONAREnhancedRecord' where form_name='ON AR Enhanced';

-- update-2014-12-05.sql
alter table faxes add column statusString varchar(255);
alter table faxes add column demographicNo int(11);

-- update-2014-12-09.sql
create table DrugProductTemplate (
        id int NOT NULL auto_increment,
        name varchar(255),
        code varchar(255),
        amount int not null,
        primary key (id)
);

-- update-2014-12-12.sql
insert into secObjectName Values('_admin.fax','Configure & Manage Faxes',0);
insert into secObjPrivilege Values('admin','_admin.fax','x',0,'999998');

-- update-2014-12-17.sql
alter table facility_message add `programId` int;

-- update-2014-12-29.sql
--  moved to procedure 
-- ALTER TABLE `providerLabRouting` ADD INDEX `provider_lab_status_index` ( `provider_no` ( 3 ) , `status`);

-- update-2015-01-19.sql
CREATE TABLE `IntegratorProgress` (
 `id` int(11) NOT NULL auto_increment,
 `dateCreated` timestamp,
 `status` varchar(50),
 `errorMessage` varchar(255),
 PRIMARY KEY  (`id`),
 KEY `idx_status` (`status`)
);



CREATE TABLE `IntegratorProgressItem` (
 `id` int(11) NOT NULL auto_increment,
  `demographicNo` int not null,
  `integratorProgressId` int not null,
 `dateUpdated` timestamp,
 `status` varchar(50),
 PRIMARY KEY  (`id`),
 KEY `idx_id` (`integratorProgressId`),
 KEY `idx_status` (`status`)
);

-- update-2015-02-06.sql
CREATE TABLE `AppDefinition` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(255),
  `appType` varchar(255),
  `config` text,
  `active` tinyint(1),
  `addedBy` varchar(8),
  `added` datetime,
  PRIMARY KEY (`id`)
);


CREATE TABLE `AppUser` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `providerNo` varchar(8),
  `appId` int(9),
  `authenticationData` text,
  `added` datetime,
  PRIMARY KEY (`id`)
);

-- update-2015-02-12.sql
CREATE TABLE consultationResponse (
  responseId int(10) NOT NULL auto_increment,
  responseDate date,
  referralDate date,
  referringDocId int(10),
  appointmentDate date,
  appointmentTime time,
  appointmentNote text,
  referralReason text,
  examination text,
  impression text,
  plan text,
  clinicalInfo text,
  currentMeds text,
  allergies text,
  providerNo varchar(6),
  demographicNo int(10),
  status char(2),
  sendTo varchar(20),
  concurrentProblems text,
  urgency char(2),
  followUpDate date,
  signatureImg VARCHAR(20),
  letterheadName VARCHAR(20),
  letterheadAddress TEXT,
  letterheadPhone VARCHAR(50),
  letterheadFax VARCHAR(50),
  PRIMARY KEY  (responseId)
) ;

INSERT INTO consultationServices (serviceDesc, active) VALUES ('Referring Doctor', '02');

CREATE TABLE `consultResponseDoc` (
  `id` int(10) NOT NULL auto_increment PRIMARY KEY,
  `responseId` int(10) NOT NULL,
  `documentNo` int(10) NOT NULL,
  `docType` char(1) NOT NULL,
  `deleted` char(1),
  `attachDate` date,
  `providerNo` varchar(6) NOT NULL
);

-- update-2015-02-13.sql  -- TODO check why this is commented out in the OSCAR-EMR upgrade script
alter table professionalSpecialists add privatePhoneNumber varchar(30);
alter table professionalSpecialists add cellPhoneNumber varchar(30);
alter table professionalSpecialists add pagerNumber varchar(30);
alter table professionalSpecialists add salutation varchar(10);
update professionalSpecialists set privatePhoneNumber='';
update professionalSpecialists set cellPhoneNumber='';
update professionalSpecialists set pagerNumber='';
update professionalSpecialists set salutation='';

-- update-2015-03-08.sql
--
-- Table structure for table 'contactspecialty'
--
CREATE TABLE `ContactSpecialty` (
  `id` int(11) NOT NULL,
  `specialty` varchar(50) NOT NULL,
  `description` varchar(140),
  PRIMARY KEY (`id`)
);
--
-- Insert into Specialty Table.
--
--
-- ContactSpecialty Data
--
INSERT INTO `ContactSpecialty` VALUES ('0', 'FAMILY PHYSICIAN', null), ('1', 'DERMATOLOGY', null), ('2', 'NEUROLOGY', null), ('3', 'PSYCHIATRY', null), ('5', 'OBSTETRICS & GYNAECOLOGY', null), ('6', 'OPHTHALMOLOGY', null), ('7', 'OTOLARYNGOLOGY', null), ('8', 'GENERAL SURGERY', null), ('9', 'NEUROSURGERY', null), ('10', 'ORTHOPAEDICS', null), ('11', 'PLASTIC SURGERY', null), ('12', 'CARDIO & THORACIC', null), ('13', 'UROLOGY', null), ('14', 'PAEDIATRICS', null), ('15', 'INTERNAL MEDICINE', null), ('16', 'RADIOLOGY', null), ('17', 'LABORATORY PROCEDURES', null), ('18', 'ANAESTHESIA', null), ('19', 'PAEDIATRIC CARDIOLOGY', null), ('20', 'PHYSICAL MEDICINE AND  REHABILITATION', null), ('21', 'PUBLIC HEALTH', null), ('22', 'PHARMACIST', null), ('23', 'OCCUPATIONAL MEDICINE', null), ('24', 'GERIATRIC MEDICINE', null), ('25', 'UNKNOWN', null), ('26', 'PROCEDURAL CARDIOLOGIST', null), ('28', 'EMERGENCY MEDICINE', null), ('29', 'MEDICAL MICROBIOLOGY', null), ('30', 'CHIROPRACTORS', null), ('31', 'NATUROPATHS', null), ('32', 'PHYSICAL THERAPISTS', null), ('33', 'NUCLEAR MEDICINE', null), ('34', 'OSTEOPATHY', null), ('35', 'ORTHOPTIC', null), ('37', 'ORAL SURGEONS', null), ('38', 'PODIATRISTS', null), ('39', 'OPTOMETRIST', null), ('40', 'DENTAL SURGEONS', null), ('41', 'ORAL MEDICINE', null), ('42', 'ORTHODONTISTS', null), ('43', 'MASSAGE PRACTITIONER', null), ('44', 'RHEUMATOLOGY', null), ('45', 'CLINICAL IMMUNIZATION AND ALLERGY', null), ('46', 'MEDICAL GENETICS', null), ('47', 'VASCULAR SURGERY', null), ('48', 'THORACIC SURGERY', null);

-- update-2015-03-17.sql
alter table DrugDispensing add archived tinyint(1) not null;
update DrugDispensing set archived=0;

-- update-2015-03-21.sql
alter table appointmentArchive change type type varchar(50);

-- update-2015-03-23.sql
create table EFormReportTool (
  `id` int(11) NOT NULL auto_increment,
  `tableName` varchar(255) not null,
  `eformId` int not null,
  `expiryDate` datetime,
  `dateCreated` timestamp,
  `providerNo` varchar(6),
  `name` varchar(255),
  `dateLastPopulated` timestamp null,
  `latestMarked` tinyint(1) not null,
  PRIMARY KEY  (`id`)
);

-- update-2015-03-30.sql


-- ----------------------------
--  Table structure for `Icd9Synonym`
-- ----------------------------
CREATE TABLE `Icd9Synonym` (
  `dxCode` varchar(10) NOT NULL,
  `patientFriendly` varchar(250) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
);



INSERT INTO `Icd9Synonym` VALUES ('172', 'Skin Cancer', '1'), ('173', 'basal cell carcinoma', '2'), ('2429', 'Hyperthyroid', '3'), ('2449', 'Hypothyroid', '4'), ('2564', 'polycystic ovarian synd
rome', '5'), ('2720', 'Hypercholesterolemia', '6'), ('2722', 'Mixed hyperlipidemia', '7'), ('2724', 'Cholesterol', '8'), ('274', 'Gout', '9'), ('2768', 'hypokalemia', '10'), ('2778', 'Retinitis pigment
osa', '11'), ('2901', 'Dementia', '12'), ('2963', 'Depression/Mood', '13'), ('2967', 'Bipolar', '14'), ('3000', 'Anxiety', '15'), ('3003', 'OCD', '16'), ('30981', 'PTSD', '17'), ('3339', 'Restless leg 
syndrome', '18'), ('3540', 'carpal tunnel syndrome', '19'), ('356', 'Neuropathy/Neuropathic pain', '20'), ('401', 'Hypertension', '21'), ('4140', 'CAD', '22'), ('4273', 'Atrial Fibrilation', '23'), ('4
53', 'Deep vein thrombosis', '24'), ('4781', 'Nasal congestion', '25'), ('4912', 'COPD', '26'), ('530', 'Barret\'s esophagus', '27'), ('53081', 'GERD/Reflux', '28'), ('555', 'Cholitis/Crohn\'s', '29'),
 ('5718', 'Fatty liver', '30'), ('59651', 'Overactive bladder', '31'), ('600', 'Enlarged prostate', '32'), ('607', 'ED/Libido', '33'), ('627', 'Menopause', '34'), ('6929', 'Dermatitis/Eczema', '35'), (
'6960', 'Psoriatic arthritis', '36'), ('715', 'Arthritis/Osteoarthritis', '37'), ('722', 'degenerative disc disorder', '38'), ('7245', 'Back Pain', '39'), ('72885', 'Muscle Spasms', '40'), ('7291', 'Fi
bromyalgia', '41'), ('73390', 'osteopenia', '42'), ('7506', 'Hiatis Hernia', '43'), ('7804', 'Dizziness', '44'), ('7805', 'sleep', '45'), ('78051', 'Sleep apnea', '46'), ('78052', 'insomnia', '47'), ('
78605', 'Difficulty breathing', '48'), ('7865', 'Chest pain', '49'), ('78841', 'Frequent Urination', '50'), ('8470', 'whiplash', '51'), ('O54', 'Herpes', '52'), ('V433', 'Aortic valve replacement', '53
'), ('V450', 'Cardiac pace maker', '54');

-- update 2015-04-07.sql
alter table document add restrictToProgram tinyint(1);

-- update 2015-04-09.bc.sql  skipped in OSCAR-EMR update
CREATE TABLE IF NOT EXISTS `formBPMH` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(10) NOT NULL,
  `provider_no` int(10) NOT NULL,
  `formCreated` date,
  `formEdited` date,
  `familyDrName` varchar(55),
  `familyDrPhone` varchar(15),
  `familyDrFax` varchar(15),
  `note` varchar(255),
  `allergies` blob,
  `drugs` blob,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `dxCodeTranslations` (
  `dxCode` varchar(10) NOT NULL,
  `patientFriendly` varchar(250) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
);



INSERT INTO `dxCodeTranslations` VALUES ('172', 'Skin Cancer', '1'), ('173', 'basal cell carcinoma', '2'), ('2429', 'Hyperthyroid', '3'), ('2449', 'Hypothyroid', '4'), ('2564', 'polycystic ovarian synd
rome', '5'), ('2720', 'Hypercholesterolemia', '6'), ('2722', 'Mixed hyperlipidemia', '7'), ('2724', 'Cholesterol', '8'), ('274', 'Gout', '9'), ('2768', 'hypokalemia', '10'), ('2778', 'Retinitis pigment
osa', '11'), ('2901', 'Dementia', '12'), ('2963', 'Depression/Mood', '13'), ('2967', 'Bipolar', '14'), ('3000', 'Anxiety', '15'), ('3003', 'OCD', '16'), ('30981', 'PTSD', '17'), ('3339', 'Restless leg 
syndrome', '18'), ('3540', 'carpal tunnel syndrome', '19'), ('356', 'Neuropathy/Neuropathic pain', '20'), ('401', 'Hypertension', '21'), ('4140', 'CAD', '22'), ('4273', 'Atrial Fibrilation', '23'), ('4
53', 'Deep vein thrombosis', '24'), ('4781', 'Nasal congestion', '25'), ('4912', 'COPD', '26'), ('530', 'Barret\'s esophagus', '27'), ('53081', 'GERD/Reflux', '28'), ('555', 'Cholitis/Crohn\'s', '29'),
 ('5718', 'Fatty liver', '30'), ('59651', 'Overactive bladder', '31'), ('600', 'Enlarged prostate', '32'), ('607', 'ED/Libido', '33'), ('627', 'Menopause', '34'), ('6929', 'Dermatitis/Eczema', '35'), (
'6960', 'Psoriatic arthritis', '36'), ('715', 'Arthritis/Osteoarthritis', '37'), ('722', 'degenerative disc disorder', '38'), ('7245', 'Back Pain', '39'), ('72885', 'Muscle Spasms', '40'), ('7291', 'Fi
bromyalgia', '41'), ('73390', 'osteopenia', '42'), ('7506', 'Hiatis Hernia', '43'), ('7804', 'Dizziness', '44'), ('7805', 'sleep', '45'), ('78051', 'Sleep apnea', '46'), ('78052', 'insomnia', '47'), ('
78605', 'Difficulty breathing', '48'), ('7865', 'Chest pain', '49'), ('78841', 'Frequent Urination', '50'), ('8470', 'whiplash', '51'), ('O54', 'Herpes', '52'), ('V433', 'Aortic valve replacement', '53
'), ('V450', 'Cardiac pace maker', '54');


insert into encounterForm values ('BPMH', '../formBPMH.do?demographic_no=', 'formBPMH', 0);

-- update-2015-04-15.sql
insert into `secObjectName` (`objectName`) values ('_hrm');
insert into `secObjPrivilege` values('doctor','_hrm','x',0,'999998');

-- update-2015-04-22.sql
insert into `secObjectName` (`objectName`) values ('_eform');
insert into `secObjPrivilege` values('doctor','_eform','x',0,'999998');

insert into `secObjectName` (`objectName`) values ('_form');
insert into `secObjPrivilege` values('doctor','_form','x',0,'999998');

-- update-2015-04-22.1.sql
insert into `secObjectName` (`objectName`) values ('_measurement');
insert into `secObjPrivilege` values('doctor','_measurement','x',0,'999998');

-- update-2015-04-23.sql
insert into `secObjectName` (`objectName`) values ('_lab');
insert into `secObjPrivilege` values('doctor','_lab','x',0,'999998');

-- update-2015-04-23.1.sql
insert into `secObjectName` (`objectName`) values ('_prevention');
insert into `secObjPrivilege` values('doctor','_prevention','x',0,'999998');

-- update-2015-04-23.2.sql
insert into `secObjectName` (`objectName`) values ('_dxresearch');
insert into `secObjPrivilege` values('doctor','_dxresearch','x',0,'999998');

-- update-2015-04-24.sql 
alter table mdsMSH change dateTime dateTime datetime NOT NULL;
alter table waitingListName change `create_date` `create_date` datetime NOT NULL;
alter table waitingList change `onListSince` `onListSince` datetime NOT NULL;
alter table fileUploadCheck change `date_time` `date_time` datetime NOT NULL;
alter table caisi_role change `update_date` update_date datetime NOT NULL;
alter table casemgmt_issue change `update_date` update_date datetime NOT NULL;
alter table room_demographic change assign_start assign_start date;
alter table room_demographic change assign_end assign_end date;
alter table room_bed change assign_start assign_start date;
alter table room_bed change assign_end assign_end date;
alter table programSignature change updateDate updateDate datetime;

-- update-2015-04-25.sql
insert into `secObjectName` (`objectName`) values ('_allergy');
insert into `secObjPrivilege` values('doctor','_allergy','x',0,'999998');
insert into `secObjectName` (`objectName`) values ('_eyeform');
insert into `secObjPrivilege` values('doctor','_eyeform','x',0,'999998');

-- update-2015-04-26.sql

-- INSERT INTO billing_payment_type (id, payment_type) VALUES (8,'ALTERNATE');

CREATE TABLE IF NOT EXISTS `billing_on_item_payment`(
	`id` INT(12) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`ch1_id` INT(12) NOT NULL,
	`billing_on_payment_id` INT(12) NOT NULL,
	`billing_on_item_id` INT(12) NOT NULL,
	`payment_timestamp` TIMESTAMP,
	`paid` DECIMAL(10,2) NOT NULL,
	`refund` DECIMAL(10,2) NOT NULL,
	`discount` DECIMAL(10,2) NOT NULL,
	`credit` DECIMAL(10,2) NOT NULL,
	KEY(`ch1_id`),
	KEY(`billing_on_payment_id`),
	KEY(`billing_on_item_id`)
);

CREATE TABLE IF NOT EXISTS `billing_on_transaction` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `ch1_id` int(12) NOT NULL,
  `payment_id` int(12) NOT NULL,
  `billing_on_item_payment_id` int(12) NOT NULL,
  `demographic_no` int(10) NOT NULL,
  `update_provider_no` varchar(6) NOT NULL,
  `update_datetime` timestamp NOT NULL,
  `payment_date` date,
  `ref_num` varchar(6),
  `province` char(2),
  `man_review` char(1),
  `billing_date` date,
  `status` char(1),
  `pay_program` char(3),
  `facility_num` char(4),
  `clinic` varchar(30),
  `provider_no` varchar(6),
  `creator` varchar(30),
  `visittype` char(2),
  `admission_date` date,
  `sli_code` varchar(10),
  `service_code` varchar(10),
  `service_code_num` char(2),
  `service_code_invoiced` varchar(64),
  `service_code_paid` decimal(10,2),
  `service_code_refund` decimal(10,2),
  `service_code_discount` decimal(10,2),
  `dx_code` varchar(3),
  `billing_notes` varchar(255),
  `action_type` char(1),
  `payment_type_id` int(2),
  `service_code_credit` DECIMAL(10,2),
  PRIMARY KEY (`id`),
  KEY `ch1_id` (`ch1_id`),
  KEY `payment_id` (`payment_id`),
  KEY `service_code` (`service_code`),
  KEY `dx_code` (`dx_code`),
  KEY `payment_type_id` (`payment_type_id`),
  KEY `demographic_no`(`demographic_no`),
  KEY `provider_no`(`provider_no`),
  KEY `creator`(`creator`),
  KEY `pay_program`(`pay_program`)
);

-- CREATE UNIQUE INDEX `payment_type` ON billing_payment_type(payment_type);
-- use procedure to prevent error caused by previously applied index
CALL CreateIndex('oscar_15', 'billing_payment_type', 'payment_type', 'payment_type');

ALTER TABLE `billing_on_cheader1` modify `total` decimal(10,2);
ALTER TABLE `billing_on_cheader1` modify `paid` decimal(10,2);
ALTER TABLE `billing_on_payment` add `paymentTypeId` int(10);
ALTER TABLE `billing_on_payment` ADD COLUMN `creator` varchar(30);
ALTER TABLE `billing_on_payment` ADD COLUMN `total_payment` decimal(10,2) not null;
ALTER TABLE `billing_on_payment` ADD COLUMN `total_discount` decimal(10,2) not null;
ALTER TABLE `billing_on_payment` ADD COLUMN `total_refund` decimal(10,2) not null;
ALTER TABLE `billing_on_payment` ADD COLUMN `total_credit` DECIMAL(10, 2) NOT NULL;

ALTER TABLE `billing_on_cheader1` modify `comment1` text;


INSERT INTO `ctl_doctype` (`module`, `doctype`, `status`, `id`) VALUES('provider','invoice letterhead','A',null);

ALTER TABLE `site` add `siteLogoId` int(11);
ALTER TABLE `site` add `siteUrl` varchar(50);

-- update-2015-04-27.sql
alter table eform add programNo int(10);
alter table eform add restrictToProgram tinyint(1);
update eform set restrictToProgram = 0;

-- update-2015-04-27.1.sql
alter table preventions add restrictToProgram tinyint(1);
alter table preventions add programNo int;

-- update-2015-04-29.sql
insert into `secObjectName` (`objectName`) values ('_appDefinition');
insert into `secObjPrivilege` values('admin','_appDefinition','x',0,'999998');

-- includes changes of update-2015-05-15.sql

-- update-2015-06-10.sql
INSERT INTO secObjPrivilege values ('doctor','_phr','x','0','999998');
INSERT INTO secObjPrivilege values ('admin','_phr','x','0','999998');

-- update-2015-06-25.sql
alter table professionalSpecialists add hideFromView tinyint(1) not null;

-- update-2015-07-09.sql
alter table dxresearch add providerNo varchar(6);

-- update-2015-08-03.sql
insert into `secObjPrivilege` values('admin','_demographicExport','x',0,'999998');

-- update-2015-08-14
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.measurements', 'access to customize measurements',0);
insert into `secObjPrivilege` values('admin','_admin.measurements','x',0,'999998');

-- update-2015-08-20
ALTER TABLE DemographicContact ADD mrp tinyint(1);
UPDATE DemographicContact SET mrp = 0 WHERE mrp IS NULL OR mrp > "";

-- update-2015-09-01
alter table appointment change status status char(2) BINARY;


-- update-2015-09-10.sql
alter table billing_on_cheader1 add programNo int(10);

-- update-2015-09-17
alter table DrugProduct add dateCreated TIMESTAMP;
alter table DrugProduct add lastUpdateDate TIMESTAMP;
alter table DrugProduct add lastUpdateUser varchar(10);

-- update-2015-10-26
delete from secObjPrivilege where roleUserGroup = 'doctor' and objectName='_phr' and privilege = 'x';
insert into `secObjectName` (`objectName`) values ('_phr');
insert into `secObjPrivilege` values('doctor','_phr','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_phr','x',0,'999998');


-- update-2015-10-26.1
insert into `secObjectName` (`objectName`) values ('_admin.pmm');
insert into `secObjectName` (`objectName`) values ('_pmm');
insert into `secObjPrivilege` values('doctor','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.pmm','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('RN','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm','x',0,'999998');


-- update-2015-11-10
CREATE INDEX drugs_demographic_no ON drugs(demographic_no);

-- update-2015-11-24.sql
CREATE TABLE `ResourceStorage` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `resourceType` varchar(100),
  `resourceName` varchar(100),
  `uuid` varchar(40),
  `fileContents` mediumblob,
  `uploadDate` datetime,
  `active` tinyint(1),
  PRIMARY KEY (`id`),
  KEY `ResourceStorage_resourceType_active` (`resourceType`(10),`active`)
);

-- update-2015-11-30.sql
insert into `secObjectName` (`objectName`) values ('_admin.document');
insert into `secObjPrivilege` values('doctor','_admin.document','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.document','x',0,'999998');

-- update-2015-11-30.1.sql
alter table document modify contenttype varchar(255);
update document set contenttype='application/vnd.openxmlformats-officedocument.wordprocessingml.document' where contenttype='application/vnd.openxmlformats-officedocument.wordprocessing';
update document set contenttype='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' where contenttype='application/vnd.openxmlformats-officedocument.spreadsheetml.';

-- update-2015-11-30.2.sql
insert into `secObjectName` (`objectName`) values ('_admin.consult');
insert into `secObjPrivilege` values('admin','_admin.consult','x',0,'999998');

-- update-2015-11-30.3.sql
alter table appointmentArchive modify notes varchar(255);

-- update-2015-12-08.sql
alter table reportTemplates add uuid varchar(60);

-- update-2015-12-09.sql
alter table RemoteDataLog modify documentContents mediumblob not null;

-- update-2016-01-11.sql
ALTER TABLE scratch_pad ADD COLUMN status tinyint(1);
UPDATE scratch_pad set status=1;

-- update-2016-01-25.sql
insert into ProductLocation (name) values ('Default');

-- phc hack
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.pregnancy');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.episode');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.photo');
insert into `secObjectName` (`objectName`) values ('_demographicExport');

insert into `secObjPrivilege` values('doctor','_newCasemgmt.pregnancy','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.episode','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.photo','x',0,'999998');






