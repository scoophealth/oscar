

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
