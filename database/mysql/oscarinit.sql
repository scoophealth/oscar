-- MySQL dump 9.09
--
-- Host: localhost    Database: oscar_mcmaster
-- ------------------------------------------------------
-- Server version	4.0.16-standard

--
-- Table structure for table `FaxClientLog`
--

CREATE TABLE FaxClientLog (
  faxLogId int(9) NOT NULL auto_increment,
  provider_no varchar(6) default NULL,
  startTime datetime default NULL,
  endTime datetime default NULL,
  result varchar(255) default NULL,
  requestId varchar(10) default NULL,
  faxId varchar(10) default NULL,
  PRIMARY KEY  (faxLogId)
) TYPE=MyISAM;

--
-- Table structure for table `allergies`
--

CREATE TABLE allergies (
  allergyid int(10) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  entry_date date default NULL,
  DESCRIPTION varchar(50) NOT NULL default '',
  HICL_SEQNO int(6) default NULL,
  HIC_SEQNO int(6) default NULL,
  AGCSP int(6) default NULL,
  AGCCS int(6) default NULL,
  TYPECODE tinyint(4) NOT NULL default '0',
  reaction text,
  drugref_id varchar(100) default NULL,
  PRIMARY KEY  (allergyid)
) TYPE=MyISAM;

--
-- Table structure for table `appointment`
--

CREATE TABLE appointment (
  appointment_no int(12) NOT NULL auto_increment,
  provider_no varchar(6) NOT NULL default '',
  appointment_date date NOT NULL default '0001-01-01',
  start_time time NOT NULL default '00:00:00',
  end_time time NOT NULL default '00:00:00',
  name varchar(50) default NULL,
  demographic_no int(10) default NULL,
  notes varchar(80) default NULL,
  reason varchar(80) default NULL,
  location varchar(30) default NULL,
  resources varchar(10) default NULL,
  type varchar(10) default NULL,
  style varchar(10) default NULL,
  billing varchar(10) default NULL,
  status char(2) default NULL,
  createdatetime datetime default NULL,
  creator varchar(50) default NULL,
  remarks varchar(50) default NULL,
  PRIMARY KEY  (appointment_no),
  KEY appointment_date (appointment_date,start_time,demographic_no),
  KEY demographic_no (demographic_no)
) TYPE=MyISAM;

--
-- Table structure for table `billactivity`
--

CREATE TABLE billactivity (
  monthCode char(1) default NULL,
  batchcount int(3) default NULL,
  htmlfilename varchar(50) default NULL,
  ohipfilename varchar(50) default NULL,
  providerohipno varchar(6) default NULL,
  groupno varchar(6) default NULL,
  creator varchar(6) default NULL,
  htmlcontext text,
  ohipcontext text,
  claimrecord varchar(10) default NULL,
  updatedatetime datetime default NULL,
  status char(1) default NULL,
  total varchar(20) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `billcenter`
--

CREATE TABLE billcenter (
  billcenter_code char(2) NOT NULL default '',
  billcenter_desc varchar(20) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `billing`
--

CREATE TABLE billing (
  billing_no int(10) NOT NULL auto_increment,
  clinic_no int(10) NOT NULL default '0',
  demographic_no int(10) NOT NULL default '0',
  provider_no varchar(6) NOT NULL default '',
  appointment_no int(12) default NULL,
  organization_spec_code varchar(6) default NULL,
  demographic_name varchar(60) default NULL,
  hin varchar(12) default NULL,
  update_date date default NULL,
  update_time time default NULL,
  billing_date date default NULL,
  billing_time time default NULL,
  clinic_ref_code varchar(10) default NULL,
  content text,
  total varchar(6) default NULL,
  status char(1) default NULL,
  dob varchar(8) default NULL,
  visitdate date default NULL,
  visittype char(2) default NULL,
  provider_ohip_no varchar(20) default NULL,
  provider_rma_no varchar(20) default NULL,
  apptProvider_no varchar(6) default NULL,
  asstProvider_no varchar(6) default NULL,
  creator varchar(6) default NULL,
  PRIMARY KEY  (billing_no),
  KEY appointment_no (appointment_no,demographic_no),
  KEY demographic_no (demographic_no),
  KEY billing_date (billing_date),
  KEY provider_no (provider_no),
  KEY apptProvider_no (apptProvider_no),
  KEY creator (creator)
) TYPE=MyISAM;

--
-- Table structure for table `billingdetail`
--

CREATE TABLE billingdetail (
  billing_dt_no int(10) NOT NULL auto_increment,
  billing_no int(10) NOT NULL default '0',
  service_code varchar(5) default NULL,
  service_desc varchar(255) default NULL,
  billing_amount varchar(6) default NULL,
  diagnostic_code char(3) default NULL,
  appointment_date date default NULL,
  status char(1) default NULL,
  billingunit char(1) default NULL,
  PRIMARY KEY  (billing_dt_no),
  KEY billingno (billing_no)
) TYPE=MyISAM;

--
-- Table structure for table `billinginr`
--

CREATE TABLE billinginr (
  billinginr_no int(10) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  demographic_name varchar(60) NOT NULL default '',
  hin varchar(12) default NULL,
  dob varchar(8) default NULL,
  provider_no int(10) default NULL,
  provider_ohip_no varchar(20) default NULL,
  provider_rma_no varchar(20) default NULL,
  creator varchar(6) default NULL,
  diagnostic_code char(3) default NULL,
  service_code varchar(6) default NULL,
  service_desc varchar(255) default NULL,
  billing_amount varchar(6) default NULL,
  billing_unit char(1) default NULL,
  createdatetime datetime NOT NULL default '0001-01-01 00:00:00',
  status char(1) default NULL,
  PRIMARY KEY  (billinginr_no)
) TYPE=MyISAM;

--
-- Table structure for table `billingservice`
--

CREATE TABLE billingservice (
  billingservice_no int(10) NOT NULL auto_increment,
  service_compositecode varchar(30) default NULL,
  service_code varchar(10) default NULL,
  description text,
  value varchar(8) default NULL,
  percentage varchar(8) default NULL,
  billingservice_date date default NULL,
  specialty varchar(15) default NULL,
  region varchar(5) default NULL,
  anaesthesia char(2) default NULL,
  PRIMARY KEY  (billingservice_no),
  KEY billingservice_service_code_index (service_code)
) TYPE=MyISAM;

--
-- Table structure for table `clinic`
--

CREATE TABLE clinic (
  clinic_no int(10) NOT NULL auto_increment,
  clinic_name varchar(50) default NULL,
  clinic_address varchar(60) default '',
  clinic_city varchar(40) default '',
  clinic_postal varchar(15) default '',
  clinic_phone varchar(50) default NULL,
  clinic_fax varchar(20) default '',
  clinic_location_code varchar(10) default NULL,
  status char(1) default NULL,
  clinic_province varchar(40) default NULL,
  clinic_delim_phone text,
  clinic_delim_fax text,
  PRIMARY KEY  (clinic_no)
) TYPE=MyISAM;

--
-- Table structure for table `clinic_location`
--

CREATE TABLE clinic_location (
  clinic_location_no varchar(15) NOT NULL default '',
  clinic_no int(10) NOT NULL default '0',
  clinic_location_name varchar(40) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `config_Immunization`
--

CREATE TABLE config_Immunization (
  setId int(10) NOT NULL auto_increment,
  setName varchar(255) default NULL,
  setXmlDoc text,
  createDate date default NULL,
  providerNo varchar(6) default NULL,
  archived tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (setId)
) TYPE=MyISAM;

--
-- Table structure for table `consultationRequests`
--

CREATE TABLE consultationRequests (
  referalDate date default NULL,
  serviceId int(10) default NULL,
  specId int(10) default NULL,
  appointmentDate date default NULL,
  appointmentTime time default NULL,
  reason text,
  clinicalInfo text,
  currentMeds text,
  allergies text,
  providerNo varchar(6) default NULL,
  demographicNo int(10) default NULL,
  status char(2) default NULL,
  statusText text,
  sendTo varchar(20) default NULL,
  requestId int(10) NOT NULL auto_increment,
  concurrentProblems text,
  urgency char(2) default NULL,
  PRIMARY KEY  (requestId)
) TYPE=MyISAM;

--
-- Table structure for table `consultationServices`
--

CREATE TABLE consultationServices (
  serviceId int(10) NOT NULL auto_increment,
  serviceDesc varchar(255) default NULL,
  active char(2) default NULL,
  PRIMARY KEY  (serviceId)
) TYPE=MyISAM;

--
-- Table structure for table `ctl_billingservice`
--

CREATE TABLE ctl_billingservice (
  servicetype_name varchar(150) default NULL,
  servicetype varchar(10) default NULL,
  service_code varchar(10) default NULL,
  service_group_name varchar(20) default NULL,
  service_group varchar(20) default NULL,
  status char(1) default NULL,
  service_order int(4) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `ctl_billingservice_premium`
--

CREATE TABLE ctl_billingservice_premium (
  servicetype_name varchar(150) default '',
  service_code varchar(10) default '',
  status char(1) default '',
  update_date date default NULL
) TYPE=MyISAM;

--
-- Table structure for table `ctl_diagcode`
--

CREATE TABLE ctl_diagcode (
  servicetype varchar(10) default NULL,
  diagnostic_code varchar(5) default NULL,
  status char(1) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `ctl_doctype`
--

CREATE TABLE ctl_doctype (
  module varchar(30) NOT NULL default '',
  doctype varchar(60) NOT NULL default '',
  status char(1) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `ctl_document`
--

CREATE TABLE ctl_document (
  module varchar(30) NOT NULL default '',
  module_id int(6) NOT NULL default '0',
  document_no int(6) NOT NULL default '0',
  status char(1) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `ctl_frequency`
--

CREATE TABLE ctl_frequency (
  freqid tinyint(4) NOT NULL auto_increment,
  freqcode char(6) NOT NULL default '',
  dailymin tinyint(4) NOT NULL default '0',
  dailymax tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (freqid)
) TYPE=MyISAM;

--
-- Table structure for table `ctl_provider`
--

CREATE TABLE ctl_provider (
  clinic_no int(10) NOT NULL default '0',
  provider_no int(10) NOT NULL default '0',
  status char(1) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `ctl_specialinstructions`
--

CREATE TABLE ctl_specialinstructions (
  id tinyint(4) NOT NULL auto_increment,
  description varchar(50) NOT NULL default '',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `demographic`
--

CREATE TABLE demographic (
  demographic_no int(10) NOT NULL auto_increment,
  last_name varchar(30) NOT NULL default '',
  first_name varchar(30) NOT NULL default '',
  address varchar(60) default NULL,
  city varchar(20) default NULL,
  province varchar(20) default NULL,
  postal varchar(9) default NULL,
  phone varchar(20) default NULL,
  phone2 varchar(20) default NULL,
  email varchar(100) default NULL,
  pin varchar(10) default NULL,
  year_of_birth varchar(4) default NULL,
  month_of_birth char(2) default NULL,
  date_of_birth char(2) default NULL,
  hin varchar(20) default NULL,
  ver char(3) default NULL,
  roster_status varchar(20) default NULL,
  patient_status varchar(20) default NULL,
  date_joined date default NULL,
  chart_no varchar(10) default NULL,
  provider_no varchar(250) default NULL,
  sex char(1) NOT NULL default '',
  end_date date default NULL,
  eff_date date default NULL,
  pcn_indicator varchar(20) default NULL,
  hc_type varchar(20) default NULL,
  hc_renew_date date default NULL,
  family_doctor varchar(80) default NULL,
  PRIMARY KEY  (demographic_no),
  KEY hin (hin),
  KEY name (last_name,first_name)
) TYPE=MyISAM;

--
-- Table structure for table `demographicaccessory`
--

CREATE TABLE demographicaccessory (
  demographic_no int(10) NOT NULL default '0',
  content text,
  PRIMARY KEY  (demographic_no)
) TYPE=MyISAM;

--
-- Table structure for table `demographiccust`
--

CREATE TABLE demographiccust (
  demographic_no int(10) NOT NULL default '0',
  cust1 varchar(255) default NULL,
  cust2 varchar(255) default NULL,
  cust3 varchar(255) default NULL,
  cust4 varchar(255) default NULL,
  content text,
  PRIMARY KEY  (demographic_no),
  KEY cust1 (cust1),
  KEY cust2 (cust2),
  KEY cust3 (cust3),
  KEY cust4 (cust4)
) TYPE=MyISAM;

--
-- Table structure for table `demographicstudy`
--

CREATE TABLE demographicstudy (
  demographic_no int(10) NOT NULL default '0',
  study_no int(3) NOT NULL default '0',
  provider_no varchar(6) NOT NULL default '',
  timestamp timestamp(14) NOT NULL,
  PRIMARY KEY  (demographic_no,study_no)
) TYPE=MyISAM;

--
-- Table structure for table `desannualreviewplan`
--

CREATE TABLE desannualreviewplan (
  des_no int(10) NOT NULL auto_increment,
  des_date date NOT NULL default '0001-01-01',
  des_time time NOT NULL default '00:00:00',
  demographic_no int(10) default '0',
  form_no int(10) default '0',
  provider_no varchar(6) NOT NULL default '',
  risk_content text,
  checklist_content text,
  PRIMARY KEY  (des_no)
) TYPE=MyISAM;

--
-- Table structure for table `desaprisk`
--

CREATE TABLE desaprisk (
  desaprisk_no int(10) NOT NULL auto_increment,
  desaprisk_date date NOT NULL default '0001-01-01',
  desaprisk_time time NOT NULL default '00:00:00',
  demographic_no int(10) default '0',
  form_no int(10) default '0',
  provider_no varchar(6) NOT NULL default '',
  risk_content text,
  checklist_content text,
  PRIMARY KEY  (desaprisk_no)
) TYPE=MyISAM;

--
-- Table structure for table `diagnosticcode`
--

CREATE TABLE diagnosticcode (
  diagnosticcode_no int(5) NOT NULL auto_increment,
  diagnostic_code varchar(5) NOT NULL default '',
  description text,
  status char(1) default NULL,
  region varchar(5) default NULL,
  PRIMARY KEY  (diagnosticcode_no),
  KEY diagnosticcode_diagnostic_code_index (diagnostic_code)
) TYPE=MyISAM;

--
-- Table structure for table `diseases`
--

CREATE TABLE diseases (
  diseaseid int(10) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  ICD9_E char(6) NOT NULL default '',
  entry_date date default NULL,
  PRIMARY KEY  (diseaseid)
) TYPE=MyISAM;

--
-- Table structure for table `document`
--

CREATE TABLE document (
  document_no int(6) NOT NULL auto_increment,
  doctype varchar(20) default NULL,
  docdesc varchar(50) NOT NULL default '',
  docxml text,
  docfilename varchar(50) NOT NULL default '',
  doccreator varchar(30) NOT NULL default '',
  updatedatetime datetime default NULL,
  status char(1) NOT NULL default '',
  PRIMARY KEY  (document_no)
) TYPE=MyISAM;

--
-- Table structure for table `drugs`
--

CREATE TABLE drugs (
  drugid int(10) NOT NULL auto_increment,
  provider_no varchar(6) NOT NULL default '',
  demographic_no int(10) NOT NULL default '0',
  rx_date date NOT NULL default '0001-01-01',
  end_date date NOT NULL default '0001-01-01',
  BN varchar(255) default '',
  GCN_SEQNO decimal(10,0) NOT NULL default '0',
  customName varchar(60) default NULL,
  takemin float default NULL,
  takemax float default NULL,
  freqcode varchar(6) default NULL,
  duration varchar(4) default NULL,
  durunit char(1) default NULL,
  quantity varchar(20) default NULL,
  repeat tinyint(4) default NULL,
  nosubs tinyint(1) NOT NULL default '0',
  prn tinyint(1) NOT NULL default '0',
  special text,
  archived tinyint(1) NOT NULL default '0',
  GN varchar(255) default NULL,
  ATC varchar(20) default NULL,
  script_no int(10) default NULL,
  regional_identifier varchar(100) default NULL,
  PRIMARY KEY  (drugid)
) TYPE=MyISAM;

--
-- Table structure for table `dxresearch`
--

CREATE TABLE dxresearch (
  dxresearch_no int(10) NOT NULL auto_increment,
  demographic_no int(10) default '0',
  start_date date default '0001-01-01',
  update_date date default '0001-01-01',
  status char(1) default 'A',
  dxresearch_code varchar(10) default '',
  PRIMARY KEY  (dxresearch_no)
) TYPE=MyISAM;

--
-- Table structure for table `eChart`
--

CREATE TABLE eChart (
  eChartId int(15) NOT NULL auto_increment,
  timeStamp timestamp(14) NOT NULL,
  demographicNo int(10) NOT NULL default '0',
  providerNo varchar(6) NOT NULL default '',
  subject varchar(128) default NULL,
  socialHistory text,
  familyHistory text,
  medicalHistory text,
  ongoingConcerns text,
  reminders text,
  encounter text,
  PRIMARY KEY  (eChartId),
  KEY demographicno (demographicNo)
) TYPE=MyISAM;

--
-- Table structure for table `eform`
--

CREATE TABLE eform (
  fid int(8) NOT NULL auto_increment,
  form_name varchar(255) default NULL,
  file_name varchar(255) default NULL,
  subject varchar(255) default NULL,
  form_date date default NULL,
  form_time time default NULL,
  form_creator varchar(255) default NULL,
  status tinyint(1) NOT NULL default '1',
  form_html text,
  PRIMARY KEY  (fid),
  UNIQUE KEY id (fid)
) TYPE=MyISAM;

--
-- Table structure for table `eform_data`
--

CREATE TABLE eform_data (
  fdid int(8) NOT NULL auto_increment,
  fid int(8) NOT NULL default '0',
  form_name varchar(255) default NULL,
  subject varchar(255) default NULL,
  demographic_no int(10) NOT NULL default '0',
  status tinyint(1) NOT NULL default '1',
  form_date date default NULL,
  form_time time default NULL,
  form_provider varchar(255) default NULL,
  form_data text,
  PRIMARY KEY  (fdid),
  UNIQUE KEY id (fdid)
) TYPE=MyISAM;

--
-- Table structure for table `eforms`
--

CREATE TABLE eforms (
  fid int(8) NOT NULL auto_increment,
  form_name text,
  file_name text,
  subject text,
  form_date date default NULL,
  form_time time default NULL,
  form_creator text,
  status int(1) NOT NULL default '0',
  form_html mediumtext,
  PRIMARY KEY  (fid),
  UNIQUE KEY id (fid)
) TYPE=MyISAM;

--
-- Table structure for table `eforms_data`
--

CREATE TABLE eforms_data (
  fdid int(8) NOT NULL auto_increment,
  fid int(8) NOT NULL default '0',
  form_name text,
  subject text,
  demographic_no int(8) NOT NULL default '0',
  status int(1) NOT NULL default '0',
  form_date date default NULL,
  form_time time default NULL,
  form_provider text,
  form_data mediumtext,
  form_fields mediumtext,
  PRIMARY KEY  (fdid),
  UNIQUE KEY id (fdid)
) TYPE=MyISAM;

--
-- Table structure for table `encounter`
--

CREATE TABLE encounter (
  encounter_no int(12) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  encounter_date date NOT NULL default '0001-01-01',
  encounter_time time NOT NULL default '00:00:00',
  provider_no varchar(6) NOT NULL default '',
  subject varchar(100) default NULL,
  content text,
  encounterattachment text,
  PRIMARY KEY  (encounter_no)
) TYPE=MyISAM;

--
-- Table structure for table `encounterForm`
--

CREATE TABLE encounterForm (
  form_name varchar(30) NOT NULL default '',
  form_value varchar(255) NOT NULL default '',
  form_table varchar(50) NOT NULL default '',
  hidden char(2) default '1',
  PRIMARY KEY  (form_value)
) TYPE=MyISAM;

--
-- Table structure for table `encountertemplate`
--

CREATE TABLE encountertemplate (
  encountertemplate_name varchar(50) NOT NULL default '',
  createdatetime datetime default NULL,
  encountertemplate_value text,
  creator varchar(6) default NULL,
  PRIMARY KEY  (encountertemplate_name),
  KEY encountertemplate_url (createdatetime)
) TYPE=MyISAM;

--
-- Table structure for table `favorites`
--

CREATE TABLE favorites (
  favoriteid int(10) NOT NULL auto_increment,
  provider_no varchar(6) NOT NULL default '',
  favoritename varchar(50) NOT NULL default '',
  BN varchar(30) default NULL,
  GCN_SEQNO decimal(10,0) NOT NULL default '0',
  customName varchar(60) default NULL,
  takemin float default NULL,
  takemax float default NULL,
  freqcode varchar(6) default NULL,
  duration varchar(4) default NULL,
  durunit char(1) default NULL,
  quantity varchar(20) default NULL,
  repeat tinyint(4) default NULL,
  nosubs tinyint(1) NOT NULL default '0',
  prn tinyint(1) NOT NULL default '0',
  special varchar(255) NOT NULL default '',
  GN varchar(255) default NULL,
  ATC varchar(255) default NULL,
  regional_identifier varchar(100) default NULL,
  PRIMARY KEY  (favoriteid)
) TYPE=MyISAM;

--
-- Table structure for table `form`
--

CREATE TABLE form (
  form_no int(12) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  provider_no varchar(6) NOT NULL default '',
  form_date date NOT NULL default '0001-01-01',
  form_time time NOT NULL default '00:00:00',
  form_name varchar(50) default NULL,
  content text,
  PRIMARY KEY  (form_no)
) TYPE=MyISAM;

--
-- Table structure for table `formAR`
--

CREATE TABLE formAR (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  c_lastVisited char(3) default NULL,
  c_pName varchar(60) default NULL,
  c_address varchar(80) default NULL,
  pg1_dateOfBirth date default NULL,
  pg1_age char(2) default NULL,
  pg1_msSingle tinyint(1) default NULL,
  pg1_msCommonLaw tinyint(1) default NULL,
  pg1_msMarried tinyint(1) default NULL,
  pg1_eduLevel varchar(25) default NULL,
  pg1_occupation varchar(25) default NULL,
  pg1_language varchar(25) default NULL,
  pg1_homePhone varchar(20) default NULL,
  pg1_workPhone varchar(20) default NULL,
  pg1_partnerName varchar(50) default NULL,
  pg1_partnerAge char(2) default NULL,
  pg1_partnerOccupation varchar(25) default NULL,
  pg1_baObs tinyint(1) default NULL,
  pg1_baFP tinyint(1) default NULL,
  pg1_baMidwife tinyint(1) default NULL,
  c_ba varchar(25) default NULL,
  pg1_famPhys varchar(100) default NULL,
  pg1_ncPed tinyint(1) default NULL,
  pg1_ncFP tinyint(1) default NULL,
  pg1_ncMidwife tinyint(1) default NULL,
  c_nc varchar(25) default NULL,
  pg1_ethnicBg varchar(100) default NULL,
  pg1_vbac tinyint(1) default NULL,
  pg1_repeatCS tinyint(1) default NULL,
  c_allergies text,
  c_meds text,
  pg1_menLMP varchar(10) default NULL,
  pg1_menCycle varchar(7) default NULL,
  pg1_menReg tinyint(1) default NULL,
  pg1_menEDB varchar(10) default NULL,
  pg1_iud tinyint(1) default NULL,
  pg1_hormone tinyint(1) default NULL,
  pg1_hormoneType varchar(25) default NULL,
  pg1_otherAR1 tinyint(1) default NULL,
  pg1_otherAR1Name varchar(25) default NULL,
  pg1_lastUsed varchar(10) default NULL,
  c_finalEDB date default NULL,
  c_gravida varchar(5) default NULL,
  c_term varchar(5) default NULL,
  c_prem varchar(5) default NULL,
  pg1_ectopic tinyint(1) default NULL,
  pg1_ectopicBox char(2) default NULL,
  pg1_termination tinyint(1) default NULL,
  pg1_terminationBox char(2) default NULL,
  pg1_spontaneous tinyint(1) default NULL,
  pg1_spontaneousBox char(2) default NULL,
  pg1_stillborn tinyint(1) default NULL,
  pg1_stillbornBox char(2) default NULL,
  c_living varchar(10) default NULL,
  pg1_multi varchar(10) default NULL,
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
  pg1_cp1 tinyint(1) default NULL,
  pg1_cp2 tinyint(1) default NULL,
  pg1_cp3 tinyint(1) default NULL,
  pg1_box3 char(3) default NULL,
  pg1_cp4 tinyint(1) default NULL,
  pg1_cp5 tinyint(1) default NULL,
  pg1_box5 char(3) default NULL,
  pg1_cp6 tinyint(1) default NULL,
  pg1_cp7 tinyint(1) default NULL,
  pg1_cp8 tinyint(1) default NULL,
  pg1_naFolic tinyint(1) default NULL,
  pg1_naMilk tinyint(1) default NULL,
  pg1_naDietBal tinyint(1) default NULL,
  pg1_naDietRes tinyint(1) default NULL,
  pg1_naRef tinyint(1) default NULL,
  pg1_yes9 tinyint(1) default NULL,
  pg1_no9 tinyint(1) default NULL,
  pg1_yes10 tinyint(1) default NULL,
  pg1_no10 tinyint(1) default NULL,
  pg1_yes11 tinyint(1) default NULL,
  pg1_no11 tinyint(1) default NULL,
  pg1_yes12 tinyint(1) default NULL,
  pg1_no12 tinyint(1) default NULL,
  pg1_yes13 tinyint(1) default NULL,
  pg1_no13 tinyint(1) default NULL,
  pg1_yes14 tinyint(1) default NULL,
  pg1_no14 tinyint(1) default NULL,
  pg1_yes15 tinyint(1) default NULL,
  pg1_no15 tinyint(1) default NULL,
  pg1_yes16 tinyint(1) default NULL,
  pg1_no16 tinyint(1) default NULL,
  pg1_yes17 tinyint(1) default NULL,
  pg1_no17 tinyint(1) default NULL,
  pg1_yes18 tinyint(1) default NULL,
  pg1_no18 tinyint(1) default NULL,
  pg1_yes19 tinyint(1) default NULL,
  pg1_no19 tinyint(1) default NULL,
  pg1_yes20 tinyint(1) default NULL,
  pg1_no20 tinyint(1) default NULL,
  pg1_yes21 tinyint(1) default NULL,
  pg1_no21 tinyint(1) default NULL,
  pg1_yes22 tinyint(1) default NULL,
  pg1_no22 tinyint(1) default NULL,
  pg1_yes23 tinyint(1) default NULL,
  pg1_no23 tinyint(1) default NULL,
  pg1_yes24 tinyint(1) default NULL,
  pg1_no24 tinyint(1) default NULL,
  pg1_yes25 tinyint(1) default NULL,
  pg1_no25 tinyint(1) default NULL,
  pg1_box25 varchar(25) default NULL,
  pg1_yes26 tinyint(1) default NULL,
  pg1_no26 tinyint(1) default NULL,
  pg1_yes27 tinyint(1) default NULL,
  pg1_no27 tinyint(1) default NULL,
  pg1_yes28 tinyint(1) default NULL,
  pg1_no28 tinyint(1) default NULL,
  pg1_yes29 tinyint(1) default NULL,
  pg1_no29 tinyint(1) default NULL,
  pg1_yes30 tinyint(1) default NULL,
  pg1_no30 tinyint(1) default NULL,
  pg1_yes31 tinyint(1) default NULL,
  pg1_no31 tinyint(1) default NULL,
  pg1_yes32 tinyint(1) default NULL,
  pg1_no32 tinyint(1) default NULL,
  pg1_yes33 tinyint(1) default NULL,
  pg1_no33 tinyint(1) default NULL,
  pg1_yes34 tinyint(1) default NULL,
  pg1_no34 tinyint(1) default NULL,
  pg1_yes35 tinyint(1) default NULL,
  pg1_no35 tinyint(1) default NULL,
  pg1_yes36 tinyint(1) default NULL,
  pg1_no36 tinyint(1) default NULL,
  pg1_yes37off tinyint(1) default NULL,
  pg1_no37off tinyint(1) default NULL,
  pg1_yes37acc tinyint(1) default NULL,
  pg1_no37acc tinyint(1) default NULL,
  pg1_idt38 tinyint(1) default NULL,
  pg1_idt39 tinyint(1) default NULL,
  pg1_idt40 tinyint(1) default NULL,
  pg1_idt41 tinyint(1) default NULL,
  pg1_idt42 tinyint(1) default NULL,
  pg1_box42 varchar(20) default NULL,
  pg1_pdt43 tinyint(1) default NULL,
  pg1_pdt44 tinyint(1) default NULL,
  pg1_pdt45 tinyint(1) default NULL,
  pg1_pdt46 tinyint(1) default NULL,
  pg1_pdt47 tinyint(1) default NULL,
  pg1_pdt48 tinyint(1) default NULL,
  c_riskFactors text,
  pg1_ht varchar(6) default NULL,
  pg1_wt varchar(6) default NULL,
  c_ppWt varchar(6) default NULL,
  pg1_BP varchar(10) default NULL,
  pg1_head tinyint(1) default NULL,
  pg1_thyroid tinyint(1) default NULL,
  pg1_chest tinyint(1) default NULL,
  pg1_breasts tinyint(1) default NULL,
  pg1_cardio tinyint(1) default NULL,
  pg1_abdomen tinyint(1) default NULL,
  pg1_vari tinyint(1) default NULL,
  pg1_neuro tinyint(1) default NULL,
  pg1_pelvic tinyint(1) default NULL,
  pg1_extGen tinyint(1) default NULL,
  pg1_cervix tinyint(1) default NULL,
  pg1_uterus tinyint(1) default NULL,
  pg1_uterusBox char(3) default NULL,
  pg1_adnexa tinyint(1) default NULL,
  pg1_commentsAR1 text,
  ar2_etss varchar(10) default NULL,
  ar2_hb varchar(10) default NULL,
  ar2_mcv varchar(10) default NULL,
  ar2_mss varchar(10) default NULL,
  ar2_rubella varchar(5) default NULL,
  ar2_hbs varchar(6) default NULL,
  ar2_vdrl varchar(6) default NULL,
  ar2_bloodGroup varchar(6) default NULL,
  ar2_rh varchar(6) default NULL,
  ar2_antibodies varchar(6) default NULL,
  ar2_rhIG varchar(6) default NULL,
  pg2_date1 date default NULL,
  pg2_gest1 varchar(6) default NULL,
  pg2_ht1 varchar(6) default NULL,
  pg2_wt1 varchar(6) default NULL,
  pg2_presn1 varchar(6) default NULL,
  pg2_FHR1 varchar(6) default NULL,
  pg2_urinePr1 char(3) default NULL,
  pg2_urineGl1 char(3) default NULL,
  pg2_BP1 varchar(8) default NULL,
  pg2_comments1 varchar(255) default NULL,
  pg2_cig1 char(3) default NULL,
  pg2_date2 date default NULL,
  pg2_gest2 varchar(6) default NULL,
  pg2_ht2 varchar(6) default NULL,
  pg2_wt2 varchar(6) default NULL,
  pg2_presn2 varchar(6) default NULL,
  pg2_FHR2 varchar(6) default NULL,
  pg2_urinePr2 char(3) default NULL,
  pg2_urineGl2 char(3) default NULL,
  pg2_BP2 varchar(8) default NULL,
  pg2_comments2 varchar(255) default NULL,
  pg2_cig2 char(3) default NULL,
  pg2_date3 date default NULL,
  pg2_gest3 varchar(6) default NULL,
  pg2_ht3 varchar(6) default NULL,
  pg2_wt3 varchar(6) default NULL,
  pg2_presn3 varchar(6) default NULL,
  pg2_FHR3 varchar(6) default NULL,
  pg2_urinePr3 char(3) default NULL,
  pg2_urineGl3 char(3) default NULL,
  pg2_BP3 varchar(8) default NULL,
  pg2_comments3 varchar(255) default NULL,
  pg2_cig3 char(3) default NULL,
  pg2_date4 date default NULL,
  pg2_gest4 varchar(6) default NULL,
  pg2_ht4 varchar(6) default NULL,
  pg2_wt4 varchar(6) default NULL,
  pg2_presn4 varchar(6) default NULL,
  pg2_FHR4 varchar(6) default NULL,
  pg2_urinePr4 char(3) default NULL,
  pg2_urineGl4 char(3) default NULL,
  pg2_BP4 varchar(8) default NULL,
  pg2_comments4 varchar(255) default NULL,
  pg2_cig4 char(3) default NULL,
  pg2_date5 date default NULL,
  pg2_gest5 varchar(6) default NULL,
  pg2_ht5 varchar(6) default NULL,
  pg2_wt5 varchar(6) default NULL,
  pg2_presn5 varchar(6) default NULL,
  pg2_FHR5 varchar(6) default NULL,
  pg2_urinePr5 char(3) default NULL,
  pg2_urineGl5 char(3) default NULL,
  pg2_BP5 varchar(8) default NULL,
  pg2_comments5 varchar(255) default NULL,
  pg2_cig5 char(3) default NULL,
  pg2_date6 date default NULL,
  pg2_gest6 varchar(6) default NULL,
  pg2_ht6 varchar(6) default NULL,
  pg2_wt6 varchar(6) default NULL,
  pg2_presn6 varchar(6) default NULL,
  pg2_FHR6 varchar(6) default NULL,
  pg2_urinePr6 char(3) default NULL,
  pg2_urineGl6 char(3) default NULL,
  pg2_BP6 varchar(8) default NULL,
  pg2_comments6 varchar(255) default NULL,
  pg2_cig6 char(3) default NULL,
  pg2_date7 date default NULL,
  pg2_gest7 varchar(6) default NULL,
  pg2_ht7 varchar(6) default NULL,
  pg2_wt7 varchar(6) default NULL,
  pg2_presn7 varchar(6) default NULL,
  pg2_FHR7 varchar(6) default NULL,
  pg2_urinePr7 char(3) default NULL,
  pg2_urineGl7 char(3) default NULL,
  pg2_BP7 varchar(8) default NULL,
  pg2_comments7 varchar(255) default NULL,
  pg2_cig7 char(3) default NULL,
  pg2_date8 date default NULL,
  pg2_gest8 varchar(6) default NULL,
  pg2_ht8 varchar(6) default NULL,
  pg2_wt8 varchar(6) default NULL,
  pg2_presn8 varchar(6) default NULL,
  pg2_FHR8 varchar(6) default NULL,
  pg2_urinePr8 char(3) default NULL,
  pg2_urineGl8 char(3) default NULL,
  pg2_BP8 varchar(8) default NULL,
  pg2_comments8 varchar(255) default NULL,
  pg2_cig8 char(3) default NULL,
  pg2_date9 date default NULL,
  pg2_gest9 varchar(6) default NULL,
  pg2_ht9 varchar(6) default NULL,
  pg2_wt9 varchar(6) default NULL,
  pg2_presn9 varchar(6) default NULL,
  pg2_FHR9 varchar(6) default NULL,
  pg2_urinePr9 char(3) default NULL,
  pg2_urineGl9 char(3) default NULL,
  pg2_BP9 varchar(8) default NULL,
  pg2_comments9 varchar(255) default NULL,
  pg2_cig9 char(3) default NULL,
  pg2_date10 date default NULL,
  pg2_gest10 varchar(6) default NULL,
  pg2_ht10 varchar(6) default NULL,
  pg2_wt10 varchar(6) default NULL,
  pg2_presn10 varchar(6) default NULL,
  pg2_FHR10 varchar(6) default NULL,
  pg2_urinePr10 char(3) default NULL,
  pg2_urineGl10 char(3) default NULL,
  pg2_BP10 varchar(8) default NULL,
  pg2_comments10 varchar(255) default NULL,
  pg2_cig10 char(3) default NULL,
  pg2_date11 date default NULL,
  pg2_gest11 varchar(6) default NULL,
  pg2_ht11 varchar(6) default NULL,
  pg2_wt11 varchar(6) default NULL,
  pg2_presn11 varchar(6) default NULL,
  pg2_FHR11 varchar(6) default NULL,
  pg2_urinePr11 char(3) default NULL,
  pg2_urineGl11 char(3) default NULL,
  pg2_BP11 varchar(8) default NULL,
  pg2_comments11 varchar(255) default NULL,
  pg2_cig11 char(3) default NULL,
  pg2_date12 date default NULL,
  pg2_gest12 varchar(6) default NULL,
  pg2_ht12 varchar(6) default NULL,
  pg2_wt12 varchar(6) default NULL,
  pg2_presn12 varchar(6) default NULL,
  pg2_FHR12 varchar(6) default NULL,
  pg2_urinePr12 char(3) default NULL,
  pg2_urineGl12 char(3) default NULL,
  pg2_BP12 varchar(8) default NULL,
  pg2_comments12 varchar(255) default NULL,
  pg2_cig12 char(3) default NULL,
  pg2_date13 date default NULL,
  pg2_gest13 varchar(6) default NULL,
  pg2_ht13 varchar(6) default NULL,
  pg2_wt13 varchar(6) default NULL,
  pg2_presn13 varchar(6) default NULL,
  pg2_FHR13 varchar(6) default NULL,
  pg2_urinePr13 char(3) default NULL,
  pg2_urineGl13 char(3) default NULL,
  pg2_BP13 varchar(8) default NULL,
  pg2_comments13 varchar(255) default NULL,
  pg2_cig13 char(3) default NULL,
  pg2_date14 date default NULL,
  pg2_gest14 varchar(6) default NULL,
  pg2_ht14 varchar(6) default NULL,
  pg2_wt14 varchar(6) default NULL,
  pg2_presn14 varchar(6) default NULL,
  pg2_FHR14 varchar(6) default NULL,
  pg2_urinePr14 char(3) default NULL,
  pg2_urineGl14 char(3) default NULL,
  pg2_BP14 varchar(8) default NULL,
  pg2_comments14 varchar(255) default NULL,
  pg2_cig14 char(3) default NULL,
  pg2_date15 date default NULL,
  pg2_gest15 varchar(6) default NULL,
  pg2_ht15 varchar(6) default NULL,
  pg2_wt15 varchar(6) default NULL,
  pg2_presn15 varchar(6) default NULL,
  pg2_FHR15 varchar(6) default NULL,
  pg2_urinePr15 char(3) default NULL,
  pg2_urineGl15 char(3) default NULL,
  pg2_BP15 varchar(8) default NULL,
  pg2_comments15 varchar(255) default NULL,
  pg2_cig15 char(3) default NULL,
  pg2_date16 date default NULL,
  pg2_gest16 varchar(6) default NULL,
  pg2_ht16 varchar(6) default NULL,
  pg2_wt16 varchar(6) default NULL,
  pg2_presn16 varchar(6) default NULL,
  pg2_FHR16 varchar(6) default NULL,
  pg2_urinePr16 char(3) default NULL,
  pg2_urineGl16 char(3) default NULL,
  pg2_BP16 varchar(8) default NULL,
  pg2_comments16 varchar(255) default NULL,
  pg2_cig16 char(3) default NULL,
  pg2_date17 date default NULL,
  pg2_gest17 varchar(6) default NULL,
  pg2_ht17 varchar(6) default NULL,
  pg2_wt17 varchar(6) default NULL,
  pg2_presn17 varchar(6) default NULL,
  pg2_FHR17 varchar(6) default NULL,
  pg2_urinePr17 char(3) default NULL,
  pg2_urineGl17 char(3) default NULL,
  pg2_BP17 varchar(8) default NULL,
  pg2_comments17 varchar(255) default NULL,
  pg2_cig17 char(3) default NULL,
  pg3_date18 date default NULL,
  pg3_gest18 varchar(6) default NULL,
  pg3_ht18 varchar(6) default NULL,
  pg3_wt18 varchar(6) default NULL,
  pg3_presn18 varchar(6) default NULL,
  pg3_FHR18 varchar(6) default NULL,
  pg3_urinePr18 char(3) default NULL,
  pg3_urineGl18 char(3) default NULL,
  pg3_BP18 varchar(8) default NULL,
  pg3_comments18 varchar(255) default NULL,
  pg3_cig18 char(3) default NULL,
  pg3_date19 date default NULL,
  pg3_gest19 varchar(6) default NULL,
  pg3_ht19 varchar(6) default NULL,
  pg3_wt19 varchar(6) default NULL,
  pg3_presn19 varchar(6) default NULL,
  pg3_FHR19 varchar(6) default NULL,
  pg3_urinePr19 char(3) default NULL,
  pg3_urineGl19 char(3) default NULL,
  pg3_BP19 varchar(8) default NULL,
  pg3_comments19 varchar(255) default NULL,
  pg3_cig19 char(3) default NULL,
  pg3_date20 date default NULL,
  pg3_gest20 varchar(6) default NULL,
  pg3_ht20 varchar(6) default NULL,
  pg3_wt20 varchar(6) default NULL,
  pg3_presn20 varchar(6) default NULL,
  pg3_FHR20 varchar(6) default NULL,
  pg3_urinePr20 char(3) default NULL,
  pg3_urineGl20 char(3) default NULL,
  pg3_BP20 varchar(8) default NULL,
  pg3_comments20 varchar(255) default NULL,
  pg3_cig20 char(3) default NULL,
  pg3_date21 date default NULL,
  pg3_gest21 varchar(6) default NULL,
  pg3_ht21 varchar(6) default NULL,
  pg3_wt21 varchar(6) default NULL,
  pg3_presn21 varchar(6) default NULL,
  pg3_FHR21 varchar(6) default NULL,
  pg3_urinePr21 char(3) default NULL,
  pg3_urineGl21 char(3) default NULL,
  pg3_BP21 varchar(8) default NULL,
  pg3_comments21 varchar(255) default NULL,
  pg3_cig21 char(3) default NULL,
  pg3_date22 date default NULL,
  pg3_gest22 varchar(6) default NULL,
  pg3_ht22 varchar(6) default NULL,
  pg3_wt22 varchar(6) default NULL,
  pg3_presn22 varchar(6) default NULL,
  pg3_FHR22 varchar(6) default NULL,
  pg3_urinePr22 char(3) default NULL,
  pg3_urineGl22 char(3) default NULL,
  pg3_BP22 varchar(8) default NULL,
  pg3_comments22 varchar(255) default NULL,
  pg3_cig22 char(3) default NULL,
  pg3_date23 date default NULL,
  pg3_gest23 varchar(6) default NULL,
  pg3_ht23 varchar(6) default NULL,
  pg3_wt23 varchar(6) default NULL,
  pg3_presn23 varchar(6) default NULL,
  pg3_FHR23 varchar(6) default NULL,
  pg3_urinePr23 char(3) default NULL,
  pg3_urineGl23 char(3) default NULL,
  pg3_BP23 varchar(8) default NULL,
  pg3_comments23 varchar(255) default NULL,
  pg3_cig23 char(3) default NULL,
  pg3_date24 date default NULL,
  pg3_gest24 varchar(6) default NULL,
  pg3_ht24 varchar(6) default NULL,
  pg3_wt24 varchar(6) default NULL,
  pg3_presn24 varchar(6) default NULL,
  pg3_FHR24 varchar(6) default NULL,
  pg3_urinePr24 char(3) default NULL,
  pg3_urineGl24 char(3) default NULL,
  pg3_BP24 varchar(8) default NULL,
  pg3_comments24 varchar(255) default NULL,
  pg3_cig24 char(3) default NULL,
  pg3_date25 date default NULL,
  pg3_gest25 varchar(6) default NULL,
  pg3_ht25 varchar(6) default NULL,
  pg3_wt25 varchar(6) default NULL,
  pg3_presn25 varchar(6) default NULL,
  pg3_FHR25 varchar(6) default NULL,
  pg3_urinePr25 char(3) default NULL,
  pg3_urineGl25 char(3) default NULL,
  pg3_BP25 varchar(8) default NULL,
  pg3_comments25 varchar(255) default NULL,
  pg3_cig25 char(3) default NULL,
  pg3_date26 date default NULL,
  pg3_gest26 varchar(6) default NULL,
  pg3_ht26 varchar(6) default NULL,
  pg3_wt26 varchar(6) default NULL,
  pg3_presn26 varchar(6) default NULL,
  pg3_FHR26 varchar(6) default NULL,
  pg3_urinePr26 char(3) default NULL,
  pg3_urineGl26 char(3) default NULL,
  pg3_BP26 varchar(8) default NULL,
  pg3_comments26 varchar(255) default NULL,
  pg3_cig26 char(3) default NULL,
  pg3_date27 date default NULL,
  pg3_gest27 varchar(6) default NULL,
  pg3_ht27 varchar(6) default NULL,
  pg3_wt27 varchar(6) default NULL,
  pg3_presn27 varchar(6) default NULL,
  pg3_FHR27 varchar(6) default NULL,
  pg3_urinePr27 char(3) default NULL,
  pg3_urineGl27 char(3) default NULL,
  pg3_BP27 varchar(8) default NULL,
  pg3_comments27 varchar(255) default NULL,
  pg3_cig27 char(3) default NULL,
  pg3_date28 date default NULL,
  pg3_gest28 varchar(6) default NULL,
  pg3_ht28 varchar(6) default NULL,
  pg3_wt28 varchar(6) default NULL,
  pg3_presn28 varchar(6) default NULL,
  pg3_FHR28 varchar(6) default NULL,
  pg3_urinePr28 char(3) default NULL,
  pg3_urineGl28 char(3) default NULL,
  pg3_BP28 varchar(8) default NULL,
  pg3_comments28 varchar(255) default NULL,
  pg3_cig28 char(3) default NULL,
  pg3_date29 date default NULL,
  pg3_gest29 varchar(6) default NULL,
  pg3_ht29 varchar(6) default NULL,
  pg3_wt29 varchar(6) default NULL,
  pg3_presn29 varchar(6) default NULL,
  pg3_FHR29 varchar(6) default NULL,
  pg3_urinePr29 char(3) default NULL,
  pg3_urineGl29 char(3) default NULL,
  pg3_BP29 varchar(8) default NULL,
  pg3_comments29 varchar(255) default NULL,
  pg3_cig29 char(3) default NULL,
  pg3_date30 date default NULL,
  pg3_gest30 varchar(6) default NULL,
  pg3_ht30 varchar(6) default NULL,
  pg3_wt30 varchar(6) default NULL,
  pg3_presn30 varchar(6) default NULL,
  pg3_FHR30 varchar(6) default NULL,
  pg3_urinePr30 char(3) default NULL,
  pg3_urineGl30 char(3) default NULL,
  pg3_BP30 varchar(8) default NULL,
  pg3_comments30 varchar(255) default NULL,
  pg3_cig30 char(3) default NULL,
  pg3_date31 date default NULL,
  pg3_gest31 varchar(6) default NULL,
  pg3_ht31 varchar(6) default NULL,
  pg3_wt31 varchar(6) default NULL,
  pg3_presn31 varchar(6) default NULL,
  pg3_FHR31 varchar(6) default NULL,
  pg3_urinePr31 char(3) default NULL,
  pg3_urineGl31 char(3) default NULL,
  pg3_BP31 varchar(8) default NULL,
  pg3_comments31 varchar(255) default NULL,
  pg3_cig31 char(3) default NULL,
  pg3_date32 date default NULL,
  pg3_gest32 varchar(6) default NULL,
  pg3_ht32 varchar(6) default NULL,
  pg3_wt32 varchar(6) default NULL,
  pg3_presn32 varchar(6) default NULL,
  pg3_FHR32 varchar(6) default NULL,
  pg3_urinePr32 char(3) default NULL,
  pg3_urineGl32 char(3) default NULL,
  pg3_BP32 varchar(8) default NULL,
  pg3_comments32 varchar(255) default NULL,
  pg3_cig32 char(3) default NULL,
  pg3_date33 date default NULL,
  pg3_gest33 varchar(6) default NULL,
  pg3_ht33 varchar(6) default NULL,
  pg3_wt33 varchar(6) default NULL,
  pg3_presn33 varchar(6) default NULL,
  pg3_FHR33 varchar(6) default NULL,
  pg3_urinePr33 char(3) default NULL,
  pg3_urineGl33 char(3) default NULL,
  pg3_BP33 varchar(8) default NULL,
  pg3_comments33 varchar(255) default NULL,
  pg3_cig33 char(3) default NULL,
  pg3_date34 date default NULL,
  pg3_gest34 varchar(6) default NULL,
  pg3_ht34 varchar(6) default NULL,
  pg3_wt34 varchar(6) default NULL,
  pg3_presn34 varchar(6) default NULL,
  pg3_FHR34 varchar(6) default NULL,
  pg3_urinePr34 char(3) default NULL,
  pg3_urineGl34 char(3) default NULL,
  pg3_BP34 varchar(8) default NULL,
  pg3_comments34 varchar(255) default NULL,
  pg3_cig34 char(3) default NULL,
  ar2_obstetrician tinyint(1) default NULL,
  ar2_pediatrician tinyint(1) default NULL,
  ar2_anesthesiologist tinyint(1) default NULL,
  ar2_socialWorker tinyint(1) default NULL,
  ar2_dietician tinyint(1) default NULL,
  ar2_otherAR2 tinyint(1) default NULL,
  ar2_otherBox varchar(35) default NULL,
  ar2_drugUse tinyint(1) default NULL,
  ar2_smoking tinyint(1) default NULL,
  ar2_alcohol tinyint(1) default NULL,
  ar2_exercise tinyint(1) default NULL,
  ar2_workPlan tinyint(1) default NULL,
  ar2_intercourse tinyint(1) default NULL,
  ar2_dental tinyint(1) default NULL,
  ar2_travel tinyint(1) default NULL,
  ar2_prenatal tinyint(1) default NULL,
  ar2_breast tinyint(1) default NULL,
  ar2_birth tinyint(1) default NULL,
  ar2_preterm tinyint(1) default NULL,
  ar2_prom tinyint(1) default NULL,
  ar2_fetal tinyint(1) default NULL,
  ar2_admission tinyint(1) default NULL,
  ar2_labour tinyint(1) default NULL,
  ar2_pain tinyint(1) default NULL,
  ar2_depression tinyint(1) default NULL,
  ar2_circumcision tinyint(1) default NULL,
  ar2_car tinyint(1) default NULL,
  ar2_contraception tinyint(1) default NULL,
  ar2_onCall tinyint(1) default NULL,
  ar2_uDate1 varchar(10) default NULL,
  ar2_uGA1 varchar(10) default NULL,
  ar2_uResults1 varchar(25) default NULL,
  ar2_uDate2 varchar(10) default NULL,
  ar2_uGA2 varchar(10) default NULL,
  ar2_uResults2 varchar(25) default NULL,
  ar2_uDate3 varchar(10) default NULL,
  ar2_uGA3 varchar(10) default NULL,
  ar2_uResults3 varchar(25) default NULL,
  ar2_uDate4 varchar(10) default NULL,
  ar2_uGA4 varchar(10) default NULL,
  ar2_uResults4 varchar(25) default NULL,
  ar2_pap varchar(20) default NULL,
  ar2_commentsAR2 text,
  ar2_chlamydia varchar(10) default NULL,
  ar2_hiv varchar(10) default NULL,
  ar2_vaginosis varchar(10) default NULL,
  ar2_strep varchar(10) default NULL,
  ar2_urineCulture varchar(10) default NULL,
  ar2_sickleDex varchar(10) default NULL,
  ar2_electro varchar(10) default NULL,
  ar2_amnio varchar(10) default NULL,
  ar2_glucose varchar(10) default NULL,
  ar2_otherAR2Name varchar(20) default NULL,
  ar2_otherResult varchar(10) default NULL,
  ar2_psych varchar(25) default NULL,
  pg1_signature varchar(50) default NULL,
  pg2_signature varchar(50) default NULL,
  pg3_signature varchar(50) default NULL,
  pg1_formDate date default NULL,
  pg2_formDate date default NULL,
  pg3_formDate date default NULL,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

--
-- Table structure for table `formAdf`
--

CREATE TABLE formAdf (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  c_patientname varchar(60) default NULL,
  residentno varchar(20) default NULL,
  c_physician varchar(60) default NULL,
  c_address varchar(80) default NULL,
  c_phone varchar(20) default NULL,
  cComplait text,
  histPresIll text,
  childhood varchar(80) default NULL,
  adult varchar(80) default NULL,
  operations varchar(80) default NULL,
  injuries varchar(80) default NULL,
  mentalIll varchar(80) default NULL,
  familyHist text,
  socialHist text,
  general varchar(80) default NULL,
  histSkin varchar(80) default NULL,
  headNeck varchar(80) default NULL,
  respiratory varchar(80) default NULL,
  cardiovascular varchar(80) default NULL,
  gi varchar(80) default NULL,
  gu varchar(80) default NULL,
  cns varchar(80) default NULL,
  histExtremities varchar(80) default NULL,
  allergies text,
  sensitivityDrug text,
  currentMedication text,
  temp varchar(80) default NULL,
  pulse varchar(80) default NULL,
  resp varchar(80) default NULL,
  bp varchar(80) default NULL,
  height varchar(80) default NULL,
  weight varchar(80) default NULL,
  physicalCondition text,
  mentalCondition text,
  skin varchar(80) default NULL,
  eyes varchar(80) default NULL,
  ears varchar(80) default NULL,
  nose varchar(80) default NULL,
  mouthTeeth varchar(80) default NULL,
  throat varchar(80) default NULL,
  neck varchar(80) default NULL,
  chest varchar(80) default NULL,
  heart varchar(80) default NULL,
  abdomen varchar(80) default NULL,
  genitalia varchar(80) default NULL,
  lymphatics varchar(80) default NULL,
  bloodVessels varchar(80) default NULL,
  locomotor varchar(80) default NULL,
  extremities varchar(80) default NULL,
  rectal varchar(80) default NULL,
  vaginal varchar(80) default NULL,
  neurological varchar(80) default NULL,
  behaviorProblem text,
  functionalLimitation text,
  diagnoses text,
  sigDate date default NULL,
  signature varchar(60) default NULL,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

CREATE TABLE formAdfV2(
  ID int(10) NOT NULL  auto_increment,
  demographic_no int(10) NOT NULL default '0' ,
  provider_no int(10)  default NULL ,
  formCreated date  default NULL ,
  formEdited timestamp   ,
  c_patientname varchar(60),
  sendFacility varchar(80),
  poaSdm varchar(80),
  capTreatDecY tinyint(1),
  capTreatDecN tinyint(1),
  advDirective varchar(100),
  actProblem text,
  inactProblem text,
  courseOverYear text,
  medications text,
  allergy varchar(255),
  diet varchar(255),
  influDate date,
  pneuDate date,
  mantDateRes varchar(50),
  immuOthers varchar(50),
  pertLabInvest text,
  communication varchar(100),
  appetDysphWeight varchar(100),
  sleepEnergy varchar(100),
  depreSymptom varchar(100),
  problemBehav varchar(100),
  funcStatus varchar(100),
  fallFracture varchar(100),
  pain varchar(100),
  continence varchar(100),
  sysSkin varchar(100),
  socialSupp varchar(100),
  sysOther varchar(100),
  phyWeight varchar(10),
  phyHeight varchar(10),
  phyBPlying varchar(16),
  phyBPStanding varchar(16),
  phyGenAppear varchar(100),
  phyEyes varchar(50),
  phyEars varchar(50),
  phyOralHygeine varchar(50),
  phyBreast varchar(50),
  phyCardHeartSound varchar(30),
  phyPeriPulse varchar(30),
  phyOther varchar(30),
  phyRespiratory varchar(100),
  phyNeurological varchar(30),
  phyReflexes varchar(30),
  phyBabinski varchar(30),
  phyPower varchar(30),
  phyTone varchar(30),
  phyPowerOther varchar(30),
  phyMMSE varchar(20),
  phyComment varchar(80),
  phySkin varchar(100),
  phyAbdomen varchar(80),
  highRiskProb1 varchar(80),
  investPlanCare1 varchar(80),
  highRiskProb2 varchar(80),
  investPlanCare2 varchar(80),
  highRiskProb3 varchar(80),
  investPlanCare3 varchar(80),
  highRiskProb4 varchar(80),
  investPlanCare4 varchar(80),
  sigDate date,
  signature varchar(60),
  sigName varchar(60),
  PRIMARY KEY (ID)
) TYPE=MyISAM;

--
-- Table structure for table `formAlpha`
--

CREATE TABLE formAlpha (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  pName varchar(60) default NULL,
  socialSupport text,
  lifeEvents text,
  coupleRelationship text,
  prenatalCare text,
  prenatalEducation text,
  feelingsRePregnancy text,
  relationshipParents text,
  selfEsteem text,
  psychHistory text,
  depression text,
  alcoholDrugAbuse text,
  abuse text,
  womanAbuse text,
  childAbuse text,
  childDiscipline text,
  provCounselling tinyint(1) default NULL,
  homecare tinyint(1) default NULL,
  assaultedWomen tinyint(1) default NULL,
  addAppts tinyint(1) default NULL,
  parentingClasses tinyint(1) default NULL,
  legalAdvice tinyint(1) default NULL,
  postpartumAppts tinyint(1) default NULL,
  addictPrograms tinyint(1) default NULL,
  cas tinyint(1) default NULL,
  babyVisits tinyint(1) default NULL,
  quitSmoking tinyint(1) default NULL,
  other1 tinyint(1) default NULL,
  other1Name varchar(30) default NULL,
  publicHealth tinyint(1) default NULL,
  socialWorker tinyint(1) default NULL,
  other2 tinyint(1) default NULL,
  other2Name varchar(30) default NULL,
  prenatalEdu tinyint(1) default NULL,
  psych tinyint(1) default NULL,
  other3 tinyint(1) default NULL,
  other3Name varchar(30) default NULL,
  nutritionist tinyint(1) default NULL,
  therapist tinyint(1) default NULL,
  other4 tinyint(1) default NULL,
  other4Name varchar(30) default NULL,
  resources tinyint(1) default NULL,
  comments text,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

--
-- Table structure for table `formAnnual`
--

CREATE TABLE formAnnual (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) default NULL,
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  pName varchar(60) default NULL,
  age char(3) default NULL,
  formDate date default NULL,
  currentConcerns text,
  currentConcernsNo tinyint(1) default NULL,
  currentConcernsYes tinyint(1) default NULL,
  headN tinyint(1) default NULL,
  headAbN tinyint(1) default NULL,
  head varchar(30) default NULL,
  respN tinyint(1) default NULL,
  respAbN tinyint(1) default NULL,
  resp varchar(30) default NULL,
  cardioN tinyint(1) default NULL,
  cardioAbN tinyint(1) default NULL,
  cardio varchar(30) default NULL,
  giN tinyint(1) default NULL,
  giAbN tinyint(1) default NULL,
  gi varchar(30) default NULL,
  guN tinyint(1) default NULL,
  guAbN tinyint(1) default NULL,
  gu varchar(30) default NULL,
  noGtpalRevisions tinyint(1) default NULL,
  yesGtpalRevisions tinyint(1) default NULL,
  frontSheet tinyint(1) default NULL,
  lmp date default NULL,
  menopause char(3) default NULL,
  papSmearsN tinyint(1) default NULL,
  papSmearsAbN tinyint(1) default NULL,
  papSmears varchar(30) default NULL,
  skinN tinyint(1) default NULL,
  skinAbN tinyint(1) default NULL,
  skin varchar(30) default NULL,
  mskN tinyint(1) default NULL,
  mskAbN tinyint(1) default NULL,
  msk varchar(30) default NULL,
  endocrinN tinyint(1) default NULL,
  endocrinAbN tinyint(1) default NULL,
  endocrin varchar(30) default NULL,
  otherN tinyint(1) default NULL,
  otherAbN tinyint(1) default NULL,
  other varchar(255) default NULL,
  drugs tinyint(1) default NULL,
  medSheet tinyint(1) default NULL,
  allergies tinyint(1) default NULL,
  frontSheet1 tinyint(1) default NULL,
  familyHistory tinyint(1) default NULL,
  frontSheet2 tinyint(1) default NULL,
  smokingNo tinyint(1) default NULL,
  smokingYes tinyint(1) default NULL,
  smoking varchar(30) default NULL,
  sexualityNo tinyint(1) default NULL,
  sexualityYes tinyint(1) default NULL,
  sexuality varchar(30) default NULL,
  alcoholNo tinyint(1) default NULL,
  alcoholYes tinyint(1) default NULL,
  alcohol varchar(30) default NULL,
  occupationalNo tinyint(1) default NULL,
  occupationalYes tinyint(1) default NULL,
  occupational varchar(30) default NULL,
  otcNo tinyint(1) default NULL,
  otcYes tinyint(1) default NULL,
  otc varchar(30) default NULL,
  drivingNo tinyint(1) default NULL,
  drivingYes tinyint(1) default NULL,
  driving varchar(30) default NULL,
  exerciseNo tinyint(1) default NULL,
  exerciseYes tinyint(1) default NULL,
  exercise varchar(30) default NULL,
  travelNo tinyint(1) default NULL,
  travelYes tinyint(1) default NULL,
  travel varchar(30) default NULL,
  nutritionNo tinyint(1) default NULL,
  nutritionYes tinyint(1) default NULL,
  nutrition varchar(30) default NULL,
  otherNo tinyint(1) default NULL,
  otherYes tinyint(1) default NULL,
  otherLifestyle varchar(255) default NULL,
  dentalNo tinyint(1) default NULL,
  dentalYes tinyint(1) default NULL,
  dental varchar(30) default NULL,
  relationshipNo tinyint(1) default NULL,
  relationshipYes tinyint(1) default NULL,
  relationship varchar(150) default NULL,
  mammogram tinyint(1) default NULL,
  rectal tinyint(1) default NULL,
  breast tinyint(1) default NULL,
  maleCardiac tinyint(1) default NULL,
  pap tinyint(1) default NULL,
  maleImmunization tinyint(1) default NULL,
  femaleImmunization tinyint(1) default NULL,
  maleOther1c tinyint(1) default NULL,
  maleOther1 varchar(30) default NULL,
  precontraceptive tinyint(1) default NULL,
  maleOther2c tinyint(1) default NULL,
  maleOther2 varchar(30) default NULL,
  femaleCardiac tinyint(1) default NULL,
  osteoporosis tinyint(1) default NULL,
  femaleOther1c tinyint(1) default NULL,
  femaleOther1 varchar(30) default NULL,
  femaleOther2c tinyint(1) default NULL,
  femaleOther2 varchar(30) default NULL,
  bprTop char(3) default NULL,
  bprBottom char(3) default NULL,
  pulse varchar(10) default NULL,
  height varchar(4) default NULL,
  weight varchar(4) default NULL,
  bplTop char(3) default NULL,
  bplBottom char(3) default NULL,
  rhythm varchar(10) default NULL,
  urine varchar(30) default NULL,
  physicalSigns text,
  assessment text,
  plan text,
  signature varchar(60) default NULL,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

--
-- Table structure for table `formAnnualV2`
--

CREATE TABLE formAnnualV2 (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) default NULL,
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  pName varchar(60) default NULL,
  age char(3) default NULL,
  formDate date default NULL,
  pmhxPshxUpdated tinyint(1) default NULL,
  famHxUpdated tinyint(1) default NULL,
  socHxUpdated tinyint(1) default NULL,
  allergiesUpdated tinyint(1) default NULL,
  medicationsUpdated tinyint(1) default NULL,
  weight varchar(4) default NULL,
  height varchar(4) default NULL,
  waist varchar(4) default NULL,
  lmp date default NULL,
  BP varchar(6) default NULL,
  smokingNo tinyint(1) default NULL,
  smokingYes tinyint(1) default NULL,
  smoking varchar(100) default NULL,
  etohNo tinyint(1) default NULL,
  etohYes tinyint(1) default NULL,
  etoh varchar(100) default NULL,
  caffineNo tinyint(1) default NULL,
  caffineYes tinyint(1) default NULL,
  caffine varchar(100) default NULL,
  otcNo tinyint(1) default NULL,
  otcYes tinyint(1) default NULL,
  otc varchar(100) default NULL,
  exerciseNo tinyint(1) default NULL,
  exerciseYes tinyint(1) default NULL,
  exercise varchar(100) default NULL,
  nutritionNo tinyint(1) default NULL,
  nutritionYes tinyint(1) default NULL,
  nutrition varchar(100) default NULL,
  dentalNo tinyint(1) default NULL,
  dentalYes tinyint(1) default NULL,
  dental varchar(100) default NULL,
  occupationalNo tinyint(1) default NULL,
  occupationalYes tinyint(1) default NULL,
  occupational varchar(100) default NULL,
  travelNo tinyint(1) default NULL,
  travelYes tinyint(1) default NULL,
  travel varchar(100) default NULL,
  sexualityNo tinyint(1) default NULL,
  sexualityYes tinyint(1) default NULL,
  sexuality varchar(100) default NULL,
  generalN tinyint(1) default NULL,
  generalAbN tinyint(1) default NULL,
  general varchar(100) default NULL,
  headN tinyint(1) default NULL,
  headAbN tinyint(1) default NULL,
  head varchar(100) default NULL,
  chestN tinyint(1) default NULL,
  chestAbN tinyint(1) default NULL,
  chest varchar(100) default NULL,
  cvsN tinyint(1) default NULL,
  cvsAbN tinyint(1) default NULL,
  cvs varchar(100) default NULL,
  giN tinyint(1) default NULL,
  giAbN tinyint(1) default NULL,
  gi varchar(100) default NULL,
  guN tinyint(1) default NULL,
  guAbN tinyint(1) default NULL,
  gu varchar(100) default NULL,
  cnsN tinyint(1) default NULL,
  cnsAbN tinyint(1) default NULL,
  cns varchar(100) default NULL,
  mskN tinyint(1) default NULL,
  mskAbN tinyint(1) default NULL,
  msk varchar(100) default NULL,
  skinN tinyint(1) default NULL,
  skinAbN tinyint(1) default NULL,
  skin varchar(100) default NULL,
  moodN tinyint(1) default NULL,
  moodAbN tinyint(1) default NULL,
  mood varchar(100) default NULL,
  otherN tinyint(1) default NULL,
  otherAbN tinyint(1) default NULL,
  other text,
  eyesN tinyint(1) default NULL,
  eyesAbN tinyint(1) default NULL,
  eyes varchar(100) default NULL,
  earsN tinyint(1) default NULL,
  earsAbN tinyint(1) default NULL,
  ears varchar(100) default NULL,
  oropharynxN tinyint(1) default NULL,
  oropharynxAbN tinyint(1) default NULL,
  oropharynx varchar(100) default NULL,
  thyroidN tinyint(1) default NULL,
  thyroidAbN tinyint(1) default NULL,
  thyroid varchar(100) default NULL,
  lnodesN tinyint(1) default NULL,
  lnodesAbN tinyint(1) default NULL,
  lnodes varchar(100) default NULL,
  clearN tinyint(1) default NULL,
  clearAbN tinyint(1) default NULL,
  clear varchar(100) default NULL,
  bilatN tinyint(1) default NULL,
  bilatAbN tinyint(1) default NULL,
  bilat varchar(100) default NULL,
  wheezesN tinyint(1) default NULL,
  wheezesAbN tinyint(1) default NULL,
  wheezes varchar(100) default NULL,
  cracklesN tinyint(1) default NULL,
  cracklesAbN tinyint(1) default NULL,
  crackles varchar(100) default NULL,
  chestOther varchar(100) default NULL,
  s1s2N tinyint(1) default NULL,
  s1s2AbN tinyint(1) default NULL,
  s1s2 varchar(100) default NULL,
  murmurN tinyint(1) default NULL,
  murmurAbN tinyint(1) default NULL,
  murmur varchar(100) default NULL,
  periphPulseN tinyint(1) default NULL,
  periphPulseAbN tinyint(1) default NULL,
  periphPulse varchar(100) default NULL,
  edemaN tinyint(1) default NULL,
  edemaAbN tinyint(1) default NULL,
  edema varchar(100) default NULL,
  jvpN tinyint(1) default NULL,
  jvpAbN tinyint(1) default NULL,
  jvp varchar(100) default NULL,
  rhythmN tinyint(1) default NULL,
  rhythmAbN tinyint(1) default NULL,
  rhythm varchar(100) default NULL,
  chestbpN tinyint(1) default NULL,
  chestbpAbN tinyint(1) default NULL,
  chestbp varchar(100) default NULL,
  cvsOther varchar(100) default NULL,
  breastLeftN tinyint(1) default NULL,
  breastLeftAbN tinyint(1) default NULL,
  breastLeft varchar(100) default NULL,
  breastRightN tinyint(1) default NULL,
  breastRightAbN tinyint(1) default NULL,
  breastRight varchar(100) default NULL,
  softN tinyint(1) default NULL,
  softAbN tinyint(1) default NULL,
  soft varchar(100) default NULL,
  tenderN tinyint(1) default NULL,
  tenderAbN tinyint(1) default NULL,
  tender varchar(100) default NULL,
  bsN tinyint(1) default NULL,
  bsAbN tinyint(1) default NULL,
  bs varchar(100) default NULL,
  hepatomegN tinyint(1) default NULL,
  hepatomegAbN tinyint(1) default NULL,
  hepatomeg varchar(100) default NULL,
  splenomegN tinyint(1) default NULL,
  splenomegAbN tinyint(1) default NULL,
  splenomeg varchar(100) default NULL,
  massesN tinyint(1) default NULL,
  massesAbN tinyint(1) default NULL,
  masses varchar(100) default NULL,
  rectalN tinyint(1) default NULL,
  rectalAbN tinyint(1) default NULL,
  rectal varchar(100) default NULL,
  cxN tinyint(1) default NULL,
  cxAbN tinyint(1) default NULL,
  cx varchar(100) default NULL,
  bimanualN tinyint(1) default NULL,
  bimanualAbN tinyint(1) default NULL,
  bimanual varchar(100) default NULL,
  adnexaN tinyint(1) default NULL,
  adnexaAbN tinyint(1) default NULL,
  adnexa varchar(100) default NULL,
  papN tinyint(1) default NULL,
  papAbN tinyint(1) default NULL,
  pap varchar(100) default NULL,
  exammskN tinyint(1) default NULL,
  exammskAbN tinyint(1) default NULL,
  exammsk varchar(100) default NULL,
  examskinN tinyint(1) default NULL,
  examskinAbN tinyint(1) default NULL,
  examskin varchar(100) default NULL,
  examcnsN tinyint(1) default NULL,
  examcnsAbN tinyint(1) default NULL,
  examcns text,
  impressionPlan text,
  toDoSexualHealth text,
  toDoObesity text,
  toDoCholesterol text,
  toDoOsteoporosis text,
  toDoPAPs text,
  toDoMammogram text,
  toDoColorectal text,
  toDoElderly text,
  immunizationtd tinyint(1) default NULL,
  immunizationPneumovax tinyint(1) default NULL,
  immunizationFlu tinyint(1) default NULL,
  immunizationMenjugate tinyint(1) default NULL,
  toDoImmunization text,
  signature varchar(60) default NULL,
  examGenitaliaN tinyint(1) default NULL,
  examGenitaliaAbN tinyint(1) default NULL,
  examGenitalia varchar(100) default NULL,
  toDoProstateCancer text,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

--
-- Table structure for table `formLabReq`
--

CREATE TABLE formLabReq (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) default NULL,
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  provName varchar(60) default NULL,
  clinicAddress varchar(30) default NULL,
  clinicCity varchar(20) default NULL,
  clinicPC varchar(7) default NULL,
  practitionerNo varchar(14) default NULL,
  ohip tinyint(1) default NULL,
  thirdParty tinyint(1) default NULL,
  wcb tinyint(1) default NULL,
  aci text,
  healthNumber varchar(10) default NULL,
  version char(2) default NULL,
  birthDate date default NULL,
  paymentProgram varchar(4) default NULL,
  province varchar(15) default NULL,
  orn varchar(12) default NULL,
  phoneNumber varchar(12) default NULL,
  patientName varchar(40) default NULL,
  sex varchar(6) default NULL,
  patientAddress varchar(20) default NULL,
  patientCity varchar(20) default NULL,
  patientPC varchar(7) default NULL,
  b_glucose tinyint(1) default NULL,
  b_creatine tinyint(1) default NULL,
  b_uricAcid tinyint(1) default NULL,
  b_sodium tinyint(1) default NULL,
  b_potassium tinyint(1) default NULL,
  b_chloride tinyint(1) default NULL,
  b_ast tinyint(1) default NULL,
  b_alkPhosphate tinyint(1) default NULL,
  b_bilirubin tinyint(1) default NULL,
  b_cholesterol tinyint(1) default NULL,
  b_triglyceride tinyint(1) default NULL,
  b_urinalysis tinyint(1) default NULL,
  v_acuteHepatitis tinyint(1) default NULL,
  v_chronicHepatitis tinyint(1) default NULL,
  v_immune tinyint(1) default NULL,
  v_hepA varchar(20) default NULL,
  v_hepB varchar(20) default NULL,
  h_bloodFilmExam tinyint(1) default NULL,
  h_hemoglobin tinyint(1) default NULL,
  h_wcbCount tinyint(1) default NULL,
  h_hematocrit tinyint(1) default NULL,
  h_prothrombTime tinyint(1) default NULL,
  h_otherC tinyint(1) default NULL,
  h_other varchar(20) default NULL,
  i_pregnancyTest tinyint(1) default NULL,
  i_heterophile tinyint(1) default NULL,
  i_rubella tinyint(1) default NULL,
  i_prenatal tinyint(1) default NULL,
  i_repeatPrenatal tinyint(1) default NULL,
  i_prenatalHepatitisB tinyint(1) default NULL,
  i_vdrl tinyint(1) default NULL,
  i_otherC tinyint(1) default NULL,
  i_other varchar(20) default NULL,
  m_cervicalVaginal tinyint(1) default NULL,
  m_sputum tinyint(1) default NULL,
  m_throat tinyint(1) default NULL,
  m_urine tinyint(1) default NULL,
  m_stoolCulture tinyint(1) default NULL,
  m_otherSwabs tinyint(1) default NULL,
  m_other varchar(20) default NULL,
  otherTest text,
  formDate date default NULL,
  signature varchar(60) default NULL,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

--
-- Table structure for table `formMMSE`
--

CREATE TABLE formMMSE (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) default NULL,
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  pName varchar(50) default NULL,
  age char(3) default NULL,
  sex char(1) default NULL,
  formDate date default NULL,
  diagnosis text,
  meds text,
  o_date char(1) default NULL,
  o_place char(1) default NULL,
  r_objects char(1) default NULL,
  a_serial char(1) default NULL,
  re_name char(1) default NULL,
  l_name char(1) default NULL,
  l_repeat char(1) default NULL,
  l_follow char(1) default NULL,
  l_read char(1) default NULL,
  l_write char(1) default NULL,
  l_copy char(1) default NULL,
  total char(2) default NULL,
  lc_alert tinyint(1) default NULL,
  lc_drowsy tinyint(1) default NULL,
  lc_stupor tinyint(1) default NULL,
  lc_coma tinyint(1) default NULL,
  i_dementia tinyint(1) default NULL,
  i_depression tinyint(1) default NULL,
  i_normal tinyint(1) default NULL,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

--
-- Table structure for table `formMentalHealth`
--

CREATE TABLE formMentalHealth (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  c_lastVisited varchar(15) default NULL,
  c_pName varchar(60) default NULL,
  c_address varchar(80) default NULL,
  c_birthDate date default NULL,
  c_sex varchar(6) default NULL,
  c_homePhone varchar(10) default NULL,
  c_referralDate date default NULL,
  c_referredBy varchar(30) default NULL,
  r_rps1 char(2) default NULL,
  r_rps2 char(2) default NULL,
  r_rps3 char(2) default NULL,
  r_rpsOther varchar(30) default NULL,
  r_rpi1 char(2) default NULL,
  r_rpi2 char(2) default NULL,
  r_rpi3 char(2) default NULL,
  r_rpiOther varchar(30) default NULL,
  r_rmpi1 char(2) default NULL,
  r_rmpi2 char(2) default NULL,
  r_rmpi3 char(2) default NULL,
  r_rmpiOther varchar(30) default NULL,
  r_ir1 char(2) default NULL,
  r_ir2 char(2) default NULL,
  r_ir3 char(2) default NULL,
  r_irOther varchar(30) default NULL,
  r_arm1 char(2) default NULL,
  r_arm2 char(2) default NULL,
  r_arm3 char(2) default NULL,
  r_armOther varchar(30) default NULL,
  r_refComments text,
  a_specialist varchar(30) default NULL,
  a_aps1 char(2) default NULL,
  a_aps2 char(2) default NULL,
  a_aps3 char(2) default NULL,
  a_apsOther varchar(30) default NULL,
  a_api1 char(2) default NULL,
  a_api2 char(2) default NULL,
  a_api3 char(2) default NULL,
  a_apiOther varchar(30) default NULL,
  a_ampi1 char(2) default NULL,
  a_ampi2 char(2) default NULL,
  a_ampi3 char(2) default NULL,
  a_ampiOther varchar(50) default NULL,
  a_assComments text,
  a_tp1 char(2) default NULL,
  a_tp2 char(2) default NULL,
  a_tp3 char(2) default NULL,
  a_tpOther varchar(30) default NULL,
  o_specialist varchar(30) default NULL,
  o_numVisits varchar(5) default NULL,
  o_formDate date default NULL,
  o_sp1 char(2) default NULL,
  o_sp2 char(2) default NULL,
  o_sp3 char(2) default NULL,
  o_spOther varchar(30) default NULL,
  o_pe1 char(2) default NULL,
  o_pe2 char(2) default NULL,
  o_pe3 char(2) default NULL,
  o_peOther varchar(30) default NULL,
  o_d1 char(2) default NULL,
  o_d2 char(2) default NULL,
  o_d3 char(2) default NULL,
  o_dOther varchar(30) default NULL,
  o_pns1 char(2) default NULL,
  o_pns2 char(2) default NULL,
  o_pns3 char(2) default NULL,
  o_pnsOther varchar(30) default NULL,
  o_outComments text,
  a_formDate date default NULL,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

--
-- Table structure for table `formPalliativeCare`
--

CREATE TABLE formPalliativeCare (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) default NULL,
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  pName varchar(60) default NULL,
  diagnosis varchar(60) default NULL,
  date1 date default NULL,
  date2 date default NULL,
  date3 date default NULL,
  date4 date default NULL,
  pain1 varchar(255) default NULL,
  pain2 varchar(255) default NULL,
  pain3 varchar(255) default NULL,
  pain4 varchar(255) default NULL,
  giBowels1 varchar(255) default NULL,
  giBowels2 varchar(255) default NULL,
  giBowels3 varchar(255) default NULL,
  giBowels4 varchar(255) default NULL,
  giNausea1 varchar(255) default NULL,
  giNausea2 varchar(255) default NULL,
  giNausea3 varchar(255) default NULL,
  giNausea4 varchar(255) default NULL,
  giDysphagia1 varchar(255) default NULL,
  giDysphagia2 varchar(255) default NULL,
  giDysphagia3 varchar(255) default NULL,
  giDysphagia4 varchar(255) default NULL,
  giHiccups1 varchar(255) default NULL,
  giHiccups2 varchar(255) default NULL,
  giHiccups3 varchar(255) default NULL,
  giHiccups4 varchar(255) default NULL,
  giMouth1 varchar(255) default NULL,
  giMouth2 varchar(255) default NULL,
  giMouth3 varchar(255) default NULL,
  giMouth4 varchar(255) default NULL,
  gu1 varchar(255) default NULL,
  gu2 varchar(255) default NULL,
  gu3 varchar(255) default NULL,
  gu4 varchar(255) default NULL,
  skinUlcers1 varchar(255) default NULL,
  skinUlcers2 varchar(255) default NULL,
  skinUlcers3 varchar(255) default NULL,
  skinUlcers4 varchar(255) default NULL,
  skinPruritis1 varchar(255) default NULL,
  skinPruritis2 varchar(255) default NULL,
  skinPruritis3 varchar(255) default NULL,
  skinPruritis4 varchar(255) default NULL,
  psychAgitation1 varchar(255) default NULL,
  psychAgitation2 varchar(255) default NULL,
  psychAgitation3 varchar(255) default NULL,
  psychAgitation4 varchar(255) default NULL,
  psychAnorexia1 varchar(255) default NULL,
  psychAnorexia2 varchar(255) default NULL,
  psychAnorexia3 varchar(255) default NULL,
  psychAnorexia4 varchar(255) default NULL,
  psychAnxiety1 varchar(255) default NULL,
  psychAnxiety2 varchar(255) default NULL,
  psychAnxiety3 varchar(255) default NULL,
  psychAnxiety4 varchar(255) default NULL,
  psychDepression1 varchar(255) default NULL,
  psychDepression2 varchar(255) default NULL,
  psychDepression3 varchar(255) default NULL,
  psychDepression4 varchar(255) default NULL,
  psychFatigue1 varchar(255) default NULL,
  psychFatigue2 varchar(255) default NULL,
  psychFatigue3 varchar(255) default NULL,
  psychFatigue4 varchar(255) default NULL,
  psychSomnolence1 varchar(255) default NULL,
  psychSomnolence2 varchar(255) default NULL,
  psychSomnolence3 varchar(255) default NULL,
  psychSomnolence4 varchar(255) default NULL,
  respCough1 varchar(255) default NULL,
  respCough2 varchar(255) default NULL,
  respCough3 varchar(255) default NULL,
  respCough4 varchar(255) default NULL,
  respDyspnea1 varchar(255) default NULL,
  respDyspnea2 varchar(255) default NULL,
  respDyspnea3 varchar(255) default NULL,
  respDyspnea4 varchar(255) default NULL,
  respFever1 varchar(255) default NULL,
  respFever2 varchar(255) default NULL,
  respFever3 varchar(255) default NULL,
  respFever4 varchar(255) default NULL,
  respCaregiver1 varchar(255) default NULL,
  respCaregiver2 varchar(255) default NULL,
  respCaregiver3 varchar(255) default NULL,
  respCaregiver4 varchar(255) default NULL,
  other1 varchar(255) default NULL,
  other2 varchar(255) default NULL,
  other3 varchar(255) default NULL,
  other4 varchar(255) default NULL,
  signature1 varchar(50) default NULL,
  signature2 varchar(50) default NULL,
  signature3 varchar(50) default NULL,
  signature4 varchar(50) default NULL,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

--
-- Table structure for table `formPeriMenopausal`
--

CREATE TABLE formPeriMenopausal (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  lastVisited char(1) default NULL,
  pName varchar(60) default NULL,
  ageMenopause char(2) default NULL,
  age char(3) default NULL,
  orf_emYes tinyint(1) default NULL,
  orf_emNo tinyint(1) default NULL,
  orf_fhoYes tinyint(1) default NULL,
  orf_fhoNo tinyint(1) default NULL,
  orf_fhofYes tinyint(1) default NULL,
  orf_fhofNo tinyint(1) default NULL,
  orf_lhYes tinyint(1) default NULL,
  orf_lhNo tinyint(1) default NULL,
  orf_phfYes tinyint(1) default NULL,
  orf_phfNo tinyint(1) default NULL,
  orf_warYes tinyint(1) default NULL,
  orf_warNo tinyint(1) default NULL,
  orf_tsbYes tinyint(1) default NULL,
  orf_tsbNo tinyint(1) default NULL,
  orf_hipYes tinyint(1) default NULL,
  orf_hipNo tinyint(1) default NULL,
  orf_ieYes tinyint(1) default NULL,
  orf_ieNo tinyint(1) default NULL,
  orf_llciYes tinyint(1) default NULL,
  orf_llciNo tinyint(1) default NULL,
  orf_csYes tinyint(1) default NULL,
  orf_csNo tinyint(1) default NULL,
  orf_cYes tinyint(1) default NULL,
  orf_cNo tinyint(1) default NULL,
  orf_aYes tinyint(1) default NULL,
  orf_aNo tinyint(1) default NULL,
  orf_cptdYes tinyint(1) default NULL,
  orf_cptdNo tinyint(1) default NULL,
  orf_dcYes tinyint(1) default NULL,
  orf_dcNo tinyint(1) default NULL,
  orf_adYes tinyint(1) default NULL,
  orf_adNo tinyint(1) default NULL,
  orf_tmYes tinyint(1) default NULL,
  orf_tmNo tinyint(1) default NULL,
  orf_comments varchar(255) default NULL,
  cs_mcYes tinyint(1) default NULL,
  cs_mcNo tinyint(1) default NULL,
  cs_mpYes tinyint(1) default NULL,
  cs_mpNo tinyint(1) default NULL,
  cs_hfYes tinyint(1) default NULL,
  cs_hfNo tinyint(1) default NULL,
  cs_vdYes tinyint(1) default NULL,
  cs_vdNo tinyint(1) default NULL,
  cs_dsiYes tinyint(1) default NULL,
  cs_dsiNo tinyint(1) default NULL,
  cs_lisaYes tinyint(1) default NULL,
  cs_lisaNo tinyint(1) default NULL,
  cs_lbcYes tinyint(1) default NULL,
  cs_lbcNo tinyint(1) default NULL,
  cs_hbiYes tinyint(1) default NULL,
  cs_hbiNo tinyint(1) default NULL,
  cs_comments varchar(255) default NULL,
  crf_fhhdYes tinyint(1) default NULL,
  crf_fhhdNo tinyint(1) default NULL,
  crf_haYes tinyint(1) default NULL,
  crf_haNo tinyint(1) default NULL,
  crf_hchfYes tinyint(1) default NULL,
  crf_hchfNo tinyint(1) default NULL,
  crf_hhaYes tinyint(1) default NULL,
  crf_hhaNo tinyint(1) default NULL,
  crf_hdYes tinyint(1) default NULL,
  crf_hdNo tinyint(1) default NULL,
  crf_csYes tinyint(1) default NULL,
  crf_csNo tinyint(1) default NULL,
  crf_hbpYes tinyint(1) default NULL,
  crf_hbpNo tinyint(1) default NULL,
  crf_lhcYes tinyint(1) default NULL,
  crf_lhcNo tinyint(1) default NULL,
  crf_htYes tinyint(1) default NULL,
  crf_htNo tinyint(1) default NULL,
  crf_hlcYes tinyint(1) default NULL,
  crf_hlcNo tinyint(1) default NULL,
  crf_oYes tinyint(1) default NULL,
  crf_oNo tinyint(1) default NULL,
  crf_slYes tinyint(1) default NULL,
  crf_slNo tinyint(1) default NULL,
  crf_comments varchar(255) default NULL,
  rh_fhbcYes tinyint(1) default NULL,
  rh_fhbcNo tinyint(1) default NULL,
  rh_phbcYes tinyint(1) default NULL,
  rh_phbcNo tinyint(1) default NULL,
  rh_phocYes tinyint(1) default NULL,
  rh_phocNo tinyint(1) default NULL,
  ageHysterectomy char(2) default NULL,
  rh_hYes tinyint(1) default NULL,
  rh_hNo tinyint(1) default NULL,
  rh_hwroYes tinyint(1) default NULL,
  rh_hwroNo tinyint(1) default NULL,
  rh_hpcbYes tinyint(1) default NULL,
  rh_hpcbNo tinyint(1) default NULL,
  rh_fhadYes tinyint(1) default NULL,
  rh_fhadNo tinyint(1) default NULL,
  rh_fhccYes tinyint(1) default NULL,
  rh_fhccNo tinyint(1) default NULL,
  rh_other varchar(60) default NULL,
  rh_oYes tinyint(1) default NULL,
  rh_oNo tinyint(1) default NULL,
  rh_comments varchar(255) default NULL,
  cm_cs varchar(30) default NULL,
  cm_vds varchar(30) default NULL,
  cm_other1 varchar(30) default NULL,
  cm_o1 varchar(30) default NULL,
  cm_other2 varchar(30) default NULL,
  cm_o2 varchar(30) default NULL,
  cm_comments varchar(255) default NULL,
  phrtYes tinyint(1) default NULL,
  phrtNo tinyint(1) default NULL,
  estrogenYes tinyint(1) default NULL,
  estrogenNo tinyint(1) default NULL,
  progesteroneYes tinyint(1) default NULL,
  progesteroneNo tinyint(1) default NULL,
  hrtYes tinyint(1) default NULL,
  hrtNo tinyint(1) default NULL,
  whenHrt varchar(20) default NULL,
  reasonDiscontinued varchar(100) default NULL,
  date1 date default NULL,
  date2 date default NULL,
  date3 date default NULL,
  date4 date default NULL,
  date5 date default NULL,
  date6 date default NULL,
  date7 date default NULL,
  date8 date default NULL,
  etohUse1 varchar(100) default NULL,
  etohUse2 varchar(100) default NULL,
  etohUse3 varchar(100) default NULL,
  etohUse4 varchar(100) default NULL,
  smokingCessation1 varchar(100) default NULL,
  smokingCessation2 varchar(100) default NULL,
  smokingCessation3 varchar(100) default NULL,
  smokingCessation4 varchar(100) default NULL,
  exercise1 varchar(100) default NULL,
  exercise2 varchar(100) default NULL,
  exercise3 varchar(100) default NULL,
  exercise4 varchar(100) default NULL,
  vision1 varchar(100) default NULL,
  vision2 varchar(100) default NULL,
  vision3 varchar(100) default NULL,
  vision4 varchar(100) default NULL,
  lowFat1 varchar(100) default NULL,
  lowFat2 varchar(100) default NULL,
  lowFat3 varchar(100) default NULL,
  lowFat4 varchar(100) default NULL,
  tdLast1 varchar(100) default NULL,
  tdLast2 varchar(100) default NULL,
  tdLast3 varchar(100) default NULL,
  tdLast4 varchar(100) default NULL,
  calcium1 varchar(100) default NULL,
  calcium2 varchar(100) default NULL,
  calcium3 varchar(100) default NULL,
  calcium4 varchar(100) default NULL,
  flu1 varchar(100) default NULL,
  flu2 varchar(100) default NULL,
  flu3 varchar(100) default NULL,
  flu4 varchar(100) default NULL,
  vitaminD1 varchar(100) default NULL,
  vitaminD2 varchar(100) default NULL,
  vitaminD3 varchar(100) default NULL,
  vitaminD4 varchar(100) default NULL,
  pneumovaxDate date default NULL,
  pneumovax1 varchar(100) default NULL,
  pneumovax2 varchar(100) default NULL,
  pneumovax3 varchar(100) default NULL,
  pneumovax4 varchar(100) default NULL,
  papSmear1 varchar(100) default NULL,
  papSmear2 varchar(100) default NULL,
  papSmear3 varchar(100) default NULL,
  papSmear4 varchar(100) default NULL,
  height1 varchar(100) default NULL,
  height2 varchar(100) default NULL,
  height3 varchar(100) default NULL,
  height4 varchar(100) default NULL,
  bloodPressure1 varchar(100) default NULL,
  bloodPressure2 varchar(100) default NULL,
  bloodPressure3 varchar(100) default NULL,
  bloodPressure4 varchar(100) default NULL,
  weight1 varchar(100) default NULL,
  weight2 varchar(100) default NULL,
  weight3 varchar(100) default NULL,
  weight4 varchar(100) default NULL,
  cbe1 varchar(100) default NULL,
  cbe2 varchar(100) default NULL,
  cbe3 varchar(100) default NULL,
  cbe4 varchar(100) default NULL,
  bmd1 varchar(100) default NULL,
  bmd2 varchar(100) default NULL,
  bmd3 varchar(100) default NULL,
  bmd4 varchar(100) default NULL,
  mammography1 varchar(100) default NULL,
  mammography2 varchar(100) default NULL,
  mammography3 varchar(100) default NULL,
  mammography4 varchar(100) default NULL,
  other1 varchar(30) default NULL,
  other11 varchar(100) default NULL,
  other12 varchar(100) default NULL,
  other13 varchar(100) default NULL,
  other14 varchar(100) default NULL,
  other2 varchar(30) default NULL,
  other21 varchar(100) default NULL,
  other22 varchar(100) default NULL,
  other23 varchar(100) default NULL,
  other24 varchar(100) default NULL,
  other3 varchar(30) default NULL,
  other31 varchar(100) default NULL,
  other32 varchar(100) default NULL,
  other33 varchar(100) default NULL,
  other34 varchar(100) default NULL,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

--
-- Table structure for table `formRourke`
--

CREATE TABLE formRourke (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) default NULL,
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  c_lastVisited char(3) default NULL,
  c_birthRemarks text,
  c_riskFactors text,
  c_pName varchar(60) default NULL,
  c_birthDate date default NULL,
  c_length varchar(6) default NULL,
  c_headCirc varchar(6) default NULL,
  c_birthWeight varchar(7) default NULL,
  c_dischargeWeight varchar(7) default NULL,
  p1_date1w date default NULL,
  p1_date2w date default NULL,
  p1_date1m date default NULL,
  p1_date2m date default NULL,
  p1_ht1w varchar(5) default NULL,
  p1_wt1w varchar(5) default NULL,
  p1_hc1w varchar(5) default NULL,
  p1_ht2w varchar(5) default NULL,
  p1_wt2w varchar(5) default NULL,
  p1_hc2w varchar(5) default NULL,
  p1_ht1m varchar(5) default NULL,
  p1_wt1m varchar(5) default NULL,
  p1_hc1m varchar(5) default NULL,
  p1_ht2m varchar(5) default NULL,
  p1_wt2m varchar(5) default NULL,
  p1_hc2m varchar(5) default NULL,
  p1_pConcern1w text,
  p1_pConcern2w text,
  p1_pConcern1m text,
  p1_pConcern2m text,
  p1_breastFeeding1w tinyint(1) default NULL,
  p1_formulaFeeding1w tinyint(1) default NULL,
  p1_stoolUrine1w tinyint(1) default NULL,
  p1_nutrition1w varchar(50) default NULL,
  p1_breastFeeding2w tinyint(1) default NULL,
  p1_formulaFeeding2w tinyint(1) default NULL,
  p1_stoolUrine2w tinyint(1) default NULL,
  p1_nutrition2w varchar(50) default NULL,
  p1_breastFeeding1m tinyint(1) default NULL,
  p1_formulaFeeding1m tinyint(1) default NULL,
  p1_stoolUrine1m tinyint(1) default NULL,
  p1_nutrition1m varchar(50) default NULL,
  p1_breastFeeding2m tinyint(1) default NULL,
  p1_formulaFeeding2m tinyint(1) default NULL,
  p1_nutrition2m varchar(50) default NULL,
  p1_carSeat1w tinyint(1) default NULL,
  p1_cribSafety1w tinyint(1) default NULL,
  p1_sleeping1w tinyint(1) default NULL,
  p1_sooth1w tinyint(1) default NULL,
  p1_bonding1w tinyint(1) default NULL,
  p1_fatigue1w tinyint(1) default NULL,
  p1_siblings1w tinyint(1) default NULL,
  p1_family1w tinyint(1) default NULL,
  p1_homeVisit1w tinyint(1) default NULL,
  p1_sleepPos1w tinyint(1) default NULL,
  p1_temp1w tinyint(1) default NULL,
  p1_smoke1w tinyint(1) default NULL,
  p1_educationAdvice1w varchar(50) default NULL,
  p1_carSeat2w tinyint(1) default NULL,
  p1_cribSafety2w tinyint(1) default NULL,
  p1_sleeping2w tinyint(1) default NULL,
  p1_sooth2w tinyint(1) default NULL,
  p1_bonding2w tinyint(1) default NULL,
  p1_fatigue2w tinyint(1) default NULL,
  p1_family2w tinyint(1) default NULL,
  p1_siblings2w tinyint(1) default NULL,
  p1_homeVisit2w tinyint(1) default NULL,
  p1_sleepPos2w tinyint(1) default NULL,
  p1_temp2w tinyint(1) default NULL,
  p1_smoke2w tinyint(1) default NULL,
  p1_educationAdvice2w varchar(50) default NULL,
  p1_carbonMonoxide1m tinyint(1) default NULL,
  p1_sleepwear1m tinyint(1) default NULL,
  p1_hotWater1m tinyint(1) default NULL,
  p1_toys1m tinyint(1) default NULL,
  p1_crying1m tinyint(1) default NULL,
  p1_sooth1m tinyint(1) default NULL,
  p1_interaction1m tinyint(1) default NULL,
  p1_supports1m tinyint(1) default NULL,
  p1_educationAdvice1m varchar(50) default NULL,
  p1_falls2m tinyint(1) default NULL,
  p1_toys2m tinyint(1) default NULL,
  p1_crying2m tinyint(1) default NULL,
  p1_sooth2m tinyint(1) default NULL,
  p1_interaction2m tinyint(1) default NULL,
  p1_stress2m tinyint(1) default NULL,
  p1_fever2m tinyint(1) default NULL,
  p1_educationAdvice2m varchar(50) default NULL,
  p1_development1w varchar(50) default NULL,
  p1_development2w varchar(50) default NULL,
  p1_focusGaze1m tinyint(1) default NULL,
  p1_startles1m tinyint(1) default NULL,
  p1_sucks1m tinyint(1) default NULL,
  p1_noParentsConcerns1m tinyint(1) default NULL,
  p1_development1m varchar(50) default NULL,
  p1_followMoves2m tinyint(1) default NULL,
  p1_sounds2m tinyint(1) default NULL,
  p1_headUp2m tinyint(1) default NULL,
  p1_cuddled2m tinyint(1) default NULL,
  p1_noParentConcerns2m tinyint(1) default NULL,
  p1_development2m varchar(50) default NULL,
  p1_skin1w tinyint(1) default NULL,
  p1_fontanelles1w tinyint(1) default NULL,
  p1_eyes1w tinyint(1) default NULL,
  p1_ears1w tinyint(1) default NULL,
  p1_heartLungs1w tinyint(1) default NULL,
  p1_umbilicus1w tinyint(1) default NULL,
  p1_femoralPulses1w tinyint(1) default NULL,
  p1_hips1w tinyint(1) default NULL,
  p1_testicles1w tinyint(1) default NULL,
  p1_maleUrinary1w tinyint(1) default NULL,
  p1_physical1w varchar(50) default NULL,
  p1_skin2w tinyint(1) default NULL,
  p1_fontanelles2w tinyint(1) default NULL,
  p1_eyes2w tinyint(1) default NULL,
  p1_ears2w tinyint(1) default NULL,
  p1_heartLungs2w tinyint(1) default NULL,
  p1_umbilicus2w tinyint(1) default NULL,
  p1_femoralPulses2w tinyint(1) default NULL,
  p1_hips2w tinyint(1) default NULL,
  p1_testicles2w tinyint(1) default NULL,
  p1_maleUrinary2w tinyint(1) default NULL,
  p1_physical2w varchar(50) default NULL,
  p1_fontanelles1m tinyint(1) default NULL,
  p1_eyes1m tinyint(1) default NULL,
  p1_cover1m tinyint(1) default NULL,
  p1_hearing1m tinyint(1) default NULL,
  p1_heart1m tinyint(1) default NULL,
  p1_hips1m tinyint(1) default NULL,
  p1_physical1m varchar(50) default NULL,
  p1_fontanelles2m tinyint(1) default NULL,
  p1_eyes2m tinyint(1) default NULL,
  p1_cover2m tinyint(1) default NULL,
  p1_hearing2m tinyint(1) default NULL,
  p1_heart2m tinyint(1) default NULL,
  p1_hips2m tinyint(1) default NULL,
  p1_physical2m varchar(50) default NULL,
  p1_pkuThyroid1w tinyint(1) default NULL,
  p1_hemoScreen1w tinyint(1) default NULL,
  p1_problems1w varchar(50) default NULL,
  p1_problems2w varchar(50) default NULL,
  p1_problems1m varchar(50) default NULL,
  p1_problems2m varchar(50) default NULL,
  p1_hepB1w tinyint(1) default NULL,
  p1_immunization1w varchar(50) default NULL,
  p1_immunization2w varchar(50) default NULL,
  p1_immuniz1m tinyint(1) default NULL,
  p1_acetaminophen1m tinyint(1) default NULL,
  p1_hepB1m tinyint(1) default NULL,
  p1_immunization1m varchar(50) default NULL,
  p1_acetaminophen2m tinyint(1) default NULL,
  p1_hib2m tinyint(1) default NULL,
  p1_polio2m tinyint(1) default NULL,
  p1_immunization2m varchar(50) default NULL,
  p1_signature1w varchar(50) default NULL,
  p1_signature2w varchar(50) default NULL,
  p1_signature1m varchar(50) default NULL,
  p1_signature2m varchar(50) default NULL,
  p2_date4m date default NULL,
  p2_date6m date default NULL,
  p2_date9m date default NULL,
  p2_date12m date default NULL,
  p2_ht4m varchar(5) default NULL,
  p2_wt4m varchar(5) default NULL,
  p2_hc4m varchar(5) default NULL,
  p2_ht6m varchar(5) default NULL,
  p2_wt6m varchar(5) default NULL,
  p2_hc6m varchar(5) default NULL,
  p2_ht9m varchar(5) default NULL,
  p2_wt9m varchar(5) default NULL,
  p2_hc9m varchar(5) default NULL,
  p2_ht12m varchar(5) default NULL,
  p2_wt12m varchar(5) default NULL,
  p2_hc12m varchar(5) default NULL,
  p2_pConcern4m text,
  p2_pConcern6m text,
  p2_pConcern9m text,
  p2_pConcern12m text,
  p2_breastFeeding4m tinyint(1) default NULL,
  p2_formulaFeeding4m tinyint(1) default NULL,
  p2_cereal4m tinyint(1) default NULL,
  p2_nutrition4m varchar(50) default NULL,
  p2_breastFeeding6m tinyint(1) default NULL,
  p2_formulaFeeding6m tinyint(1) default NULL,
  p2_bottle6m tinyint(1) default NULL,
  p2_vegFruit6m tinyint(1) default NULL,
  p2_egg6m tinyint(1) default NULL,
  p2_choking6m tinyint(1) default NULL,
  p2_nutrition6m varchar(50) default NULL,
  p2_breastFeeding9m tinyint(1) default NULL,
  p2_formulaFeeding9m tinyint(1) default NULL,
  p2_bottle9m tinyint(1) default NULL,
  p2_meat9m tinyint(1) default NULL,
  p2_milk9m tinyint(1) default NULL,
  p2_egg9m tinyint(1) default NULL,
  p2_choking9m tinyint(1) default NULL,
  p2_nutrition9m varchar(50) default NULL,
  p2_milk12m tinyint(1) default NULL,
  p2_bottle12m tinyint(1) default NULL,
  p2_appetite12m tinyint(1) default NULL,
  p2_nutrition12m varchar(50) default NULL,
  p2_carSeat4m tinyint(1) default NULL,
  p2_stairs4m tinyint(1) default NULL,
  p2_bath4m tinyint(1) default NULL,
  p2_sleeping4m tinyint(1) default NULL,
  p2_parent4m tinyint(1) default NULL,
  p2_childCare4m tinyint(1) default NULL,
  p2_family4m tinyint(1) default NULL,
  p2_teething4m tinyint(1) default NULL,
  p2_educationAdvice4m varchar(50) default NULL,
  p2_poison6m tinyint(1) default NULL,
  p2_electric6m tinyint(1) default NULL,
  p2_sleeping6m tinyint(1) default NULL,
  p2_parent6m tinyint(1) default NULL,
  p2_childCare6m tinyint(1) default NULL,
  p2_educationAdvice6m varchar(50) default NULL,
  p2_childProof9m tinyint(1) default NULL,
  p2_separation9m tinyint(1) default NULL,
  p2_sleeping9m tinyint(1) default NULL,
  p2_dayCare9m tinyint(1) default NULL,
  p2_homeVisit9m tinyint(1) default NULL,
  p2_smoke9m tinyint(1) default NULL,
  p2_educationAdvice9m varchar(50) default NULL,
  p2_poison12m tinyint(1) default NULL,
  p2_electric12m tinyint(1) default NULL,
  p2_carbon12m tinyint(1) default NULL,
  p2_hotWater12m tinyint(1) default NULL,
  p2_sleeping12m tinyint(1) default NULL,
  p2_parent12m tinyint(1) default NULL,
  p2_teething12m tinyint(1) default NULL,
  p2_educationAdvice12m varchar(50) default NULL,
  p2_turnHead4m tinyint(1) default NULL,
  p2_laugh4m tinyint(1) default NULL,
  p2_headSteady4m tinyint(1) default NULL,
  p2_grasp4m tinyint(1) default NULL,
  p2_concern4m tinyint(1) default NULL,
  p2_development4m varchar(50) default NULL,
  p2_follow6m tinyint(1) default NULL,
  p2_respond6m tinyint(1) default NULL,
  p2_babbles6m tinyint(1) default NULL,
  p2_rolls6m tinyint(1) default NULL,
  p2_sits6m tinyint(1) default NULL,
  p2_mouth6m tinyint(1) default NULL,
  p2_concern6m tinyint(1) default NULL,
  p2_development6m varchar(50) default NULL,
  p2_looks9m tinyint(1) default NULL,
  p2_babbles9m tinyint(1) default NULL,
  p2_sits9m tinyint(1) default NULL,
  p2_stands9m tinyint(1) default NULL,
  p2_opposes9m tinyint(1) default NULL,
  p2_reaches9m tinyint(1) default NULL,
  p2_noParentsConcerns9m tinyint(1) default NULL,
  p2_development9m varchar(50) default NULL,
  p2_understands12m tinyint(1) default NULL,
  p2_chatters12m tinyint(1) default NULL,
  p2_crawls12m tinyint(1) default NULL,
  p2_pulls12m tinyint(1) default NULL,
  p2_emotions12m tinyint(1) default NULL,
  p2_noParentConcerns12m tinyint(1) default NULL,
  p2_development12m varchar(50) default NULL,
  p2_eyes4m tinyint(1) default NULL,
  p2_cover4m tinyint(1) default NULL,
  p2_hearing4m tinyint(1) default NULL,
  p2_babbling4m tinyint(1) default NULL,
  p2_hips4m tinyint(1) default NULL,
  p2_physical4m varchar(50) default NULL,
  p2_fontanelles6m tinyint(1) default NULL,
  p2_eyes6m tinyint(1) default NULL,
  p2_cover6m tinyint(1) default NULL,
  p2_hearing6m tinyint(1) default NULL,
  p2_hips6m tinyint(1) default NULL,
  p2_physical6m varchar(50) default NULL,
  p2_eyes9m tinyint(1) default NULL,
  p2_cover9m tinyint(1) default NULL,
  p2_hearing9m tinyint(1) default NULL,
  p2_physical9m varchar(50) default NULL,
  p2_eyes12m tinyint(1) default NULL,
  p2_cover12m tinyint(1) default NULL,
  p2_hearing12m tinyint(1) default NULL,
  p2_hips12m tinyint(1) default NULL,
  p2_physical12m varchar(50) default NULL,
  p2_problems4m varchar(50) default NULL,
  p2_tb6m tinyint(1) default NULL,
  p2_problems6m varchar(50) default NULL,
  p2_antiHbs9m tinyint(1) default NULL,
  p2_hgb9m tinyint(1) default NULL,
  p2_problems9m varchar(50) default NULL,
  p2_hgb12m tinyint(1) default NULL,
  p2_serum12m tinyint(1) default NULL,
  p2_problems12m varchar(50) default NULL,
  p2_hib4m tinyint(1) default NULL,
  p2_polio4m tinyint(1) default NULL,
  p2_immunization4m varchar(50) default NULL,
  p2_hib6m tinyint(1) default NULL,
  p2_polio6m tinyint(1) default NULL,
  p2_hepB6m tinyint(1) default NULL,
  p2_immunization6m varchar(50) default NULL,
  p2_tbSkin9m tinyint(1) default NULL,
  p2_immunization9m varchar(50) default NULL,
  p2_mmr12m tinyint(1) default NULL,
  p2_varicella12m tinyint(1) default NULL,
  p2_immunization12m varchar(50) default NULL,
  p2_signature4m varchar(50) default NULL,
  p2_signature6m varchar(50) default NULL,
  p2_signature9m varchar(50) default NULL,
  p2_signature12m varchar(50) default NULL,
  p3_date18m date default NULL,
  p3_date2y date default NULL,
  p3_date4y date default NULL,
  p3_ht18m varchar(5) default NULL,
  p3_wt18m varchar(5) default NULL,
  p3_hc18m varchar(5) default NULL,
  p3_ht2y varchar(5) default NULL,
  p3_wt2y varchar(5) default NULL,
  p3_ht4y varchar(5) default NULL,
  p3_wt4y varchar(5) default NULL,
  p3_pConcern18m text,
  p3_pConcern2y text,
  p3_pConcern4y text,
  p3_bottle18m tinyint(1) default NULL,
  p3_nutrition18m varchar(50) default NULL,
  p3_milk2y tinyint(1) default NULL,
  p3_food2y tinyint(1) default NULL,
  p3_nutrition2y varchar(50) default NULL,
  p3_milk4y tinyint(1) default NULL,
  p3_food4y tinyint(1) default NULL,
  p3_nutrition4y varchar(50) default NULL,
  p3_bath18m tinyint(1) default NULL,
  p3_choking18m tinyint(1) default NULL,
  p3_temperment18m tinyint(1) default NULL,
  p3_limit18m tinyint(1) default NULL,
  p3_social18m tinyint(1) default NULL,
  p3_dental18m tinyint(1) default NULL,
  p3_toilet18m tinyint(1) default NULL,
  p3_educationAdvice18m varchar(50) default NULL,
  p3_bike2y tinyint(1) default NULL,
  p3_matches2y tinyint(1) default NULL,
  p3_carbon2y tinyint(1) default NULL,
  p3_parent2y tinyint(1) default NULL,
  p3_social2y tinyint(1) default NULL,
  p3_dayCare2y tinyint(1) default NULL,
  p3_dental2y tinyint(1) default NULL,
  p3_toilet2y tinyint(1) default NULL,
  p3_educationAdvice2y varchar(50) default NULL,
  p3_bike4y tinyint(1) default NULL,
  p3_matches4y tinyint(1) default NULL,
  p3_carbon4y tinyint(1) default NULL,
  p3_water4y tinyint(1) default NULL,
  p3_social4y tinyint(1) default NULL,
  p3_dental4y tinyint(1) default NULL,
  p3_school4y tinyint(1) default NULL,
  p3_educationAdvice4y varchar(50) default NULL,
  p3_points18m tinyint(1) default NULL,
  p3_words18m tinyint(1) default NULL,
  p3_picks18m tinyint(1) default NULL,
  p3_walks18m tinyint(1) default NULL,
  p3_stacks18m tinyint(1) default NULL,
  p3_affection18m tinyint(1) default NULL,
  p3_showParents18m tinyint(1) default NULL,
  p3_looks18m tinyint(1) default NULL,
  p3_noParentsConcerns18m tinyint(1) default NULL,
  p3_development18m varchar(50) default NULL,
  p3_word2y tinyint(1) default NULL,
  p3_sentence2y tinyint(1) default NULL,
  p3_run2y tinyint(1) default NULL,
  p3_container2y tinyint(1) default NULL,
  p3_copies2y tinyint(1) default NULL,
  p3_skills2y tinyint(1) default NULL,
  p3_noParentsConcerns2y tinyint(1) default NULL,
  p3_development2y varchar(50) default NULL,
  p3_understands3y tinyint(1) default NULL,
  p3_twists3y tinyint(1) default NULL,
  p3_turnPages3y tinyint(1) default NULL,
  p3_share3y tinyint(1) default NULL,
  p3_listens3y tinyint(1) default NULL,
  p3_noParentsConcerns3y tinyint(1) default NULL,
  p3_development3y varchar(50) default NULL,
  p3_understands4y tinyint(1) default NULL,
  p3_questions4y tinyint(1) default NULL,
  p3_oneFoot4y tinyint(1) default NULL,
  p3_draws4y tinyint(1) default NULL,
  p3_toilet4y tinyint(1) default NULL,
  p3_comfort4y tinyint(1) default NULL,
  p3_noParentsConcerns4y tinyint(1) default NULL,
  p3_development4y varchar(50) default NULL,
  p3_counts5y tinyint(1) default NULL,
  p3_speaks5y tinyint(1) default NULL,
  p3_ball5y tinyint(1) default NULL,
  p3_hops5y tinyint(1) default NULL,
  p3_shares5y tinyint(1) default NULL,
  p3_alone5y tinyint(1) default NULL,
  p3_separate5y tinyint(1) default NULL,
  p3_noParentsConcerns5y tinyint(1) default NULL,
  p3_development5y varchar(50) default NULL,
  p3_eyes18m tinyint(1) default NULL,
  p3_cover18m tinyint(1) default NULL,
  p3_hearing18m tinyint(1) default NULL,
  p3_physical18m varchar(50) default NULL,
  p3_visual2y tinyint(1) default NULL,
  p3_cover2y tinyint(1) default NULL,
  p3_hearing2y tinyint(1) default NULL,
  p3_physical2y varchar(50) default NULL,
  p3_visual4y tinyint(1) default NULL,
  p3_cover4y tinyint(1) default NULL,
  p3_hearing4y tinyint(1) default NULL,
  p3_blood4y tinyint(1) default NULL,
  p3_physical4y varchar(50) default NULL,
  p3_problems18m varchar(50) default NULL,
  p3_serum2y tinyint(1) default NULL,
  p3_problems2y varchar(50) default NULL,
  p3_problems4y varchar(50) default NULL,
  p3_hib18m tinyint(1) default NULL,
  p3_polio18m tinyint(1) default NULL,
  p3_immunization18m varchar(50) default NULL,
  p3_immunization2y varchar(50) default NULL,
  p3_mmr4y tinyint(1) default NULL,
  p3_polio4y tinyint(1) default NULL,
  p3_immunization4y varchar(50) default NULL,
  p3_signature18m varchar(50) default NULL,
  p3_signature2y varchar(50) default NULL,
  p3_signature4y varchar(50) default NULL,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

--
-- Table structure for table `formType2Diabetes`
--

CREATE TABLE formType2Diabetes (
  ID int(10) NOT NULL auto_increment,
  demographic_no int(10) default NULL,
  provider_no int(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(14) NOT NULL,
  pName varchar(60) default NULL,
  birthDate date default NULL,
  dateDx date default NULL,
  height varchar(10) default NULL,
  date1 date default NULL,
  date2 date default NULL,
  date3 date default NULL,
  date4 date default NULL,
  date5 date default NULL,
  weight1 varchar(20) default NULL,
  weight2 varchar(20) default NULL,
  weight3 varchar(20) default NULL,
  weight4 varchar(20) default NULL,
  weight5 varchar(20) default NULL,
  bp1 varchar(20) default NULL,
  bp2 varchar(20) default NULL,
  bp3 varchar(20) default NULL,
  bp4 varchar(20) default NULL,
  bp5 varchar(20) default NULL,
  glucoseA1 varchar(50) default NULL,
  glucoseA2 varchar(50) default NULL,
  glucoseA3 varchar(50) default NULL,
  glucoseA4 varchar(50) default NULL,
  glucoseA5 varchar(50) default NULL,
  glucoseB1 varchar(50) default NULL,
  glucoseB2 varchar(50) default NULL,
  glucoseB3 varchar(50) default NULL,
  glucoseB4 varchar(50) default NULL,
  glucoseB5 varchar(50) default NULL,
  glucoseC1 varchar(50) default NULL,
  glucoseC2 varchar(50) default NULL,
  glucoseC3 varchar(50) default NULL,
  glucoseC4 varchar(50) default NULL,
  glucoseC5 varchar(50) default NULL,
  renal1 varchar(50) default NULL,
  renal2 varchar(50) default NULL,
  renal3 varchar(50) default NULL,
  renal4 varchar(50) default NULL,
  renal5 varchar(50) default NULL,
  urineRatio1 varchar(50) default NULL,
  urineRatio2 varchar(50) default NULL,
  urineRatio3 varchar(50) default NULL,
  urineRatio4 varchar(50) default NULL,
  urineRatio5 varchar(50) default NULL,
  urineClearance1 varchar(100) default NULL,
  urineClearance2 varchar(100) default NULL,
  urineClearance3 varchar(100) default NULL,
  urineClearance4 varchar(100) default NULL,
  urineClearance5 varchar(100) default NULL,
  lipidsA1 varchar(30) default NULL,
  lipidsA2 varchar(30) default NULL,
  lipidsA3 varchar(30) default NULL,
  lipidsA4 varchar(30) default NULL,
  lipidsA5 varchar(30) default NULL,
  lipidsB1 varchar(30) default NULL,
  lipidsB2 varchar(30) default NULL,
  lipidsB3 varchar(30) default NULL,
  lipidsB4 varchar(30) default NULL,
  lipidsB5 varchar(30) default NULL,
  lipidsC1 varchar(30) default NULL,
  lipidsC2 varchar(30) default NULL,
  lipidsC3 varchar(30) default NULL,
  lipidsC4 varchar(30) default NULL,
  lipidsC5 varchar(30) default NULL,
  ophthalmologist varchar(50) default NULL,
  eyes1 varchar(50) default NULL,
  eyes2 varchar(50) default NULL,
  eyes3 varchar(50) default NULL,
  eyes4 varchar(50) default NULL,
  eyes5 varchar(50) default NULL,
  feet1 varchar(100) default NULL,
  feet2 varchar(100) default NULL,
  feet3 varchar(100) default NULL,
  feet4 varchar(100) default NULL,
  feet5 varchar(100) default NULL,
  metformin tinyint(1) default NULL,
  aceInhibitor tinyint(1) default NULL,
  glyburide tinyint(1) default NULL,
  asa tinyint(1) default NULL,
  otherOha tinyint(1) default NULL,
  otherBox7 varchar(20) default NULL,
  insulin tinyint(1) default NULL,
  otherBox8 varchar(20) default NULL,
  meds1 varchar(100) default NULL,
  meds2 varchar(100) default NULL,
  meds3 varchar(100) default NULL,
  meds4 varchar(100) default NULL,
  meds5 varchar(100) default NULL,
  lifestyle1 varchar(50) default NULL,
  lifestyle2 varchar(50) default NULL,
  lifestyle3 varchar(50) default NULL,
  lifestyle4 varchar(50) default NULL,
  lifestyle5 varchar(50) default NULL,
  exercise1 varchar(50) default NULL,
  exercise2 varchar(50) default NULL,
  exercise3 varchar(50) default NULL,
  exercise4 varchar(50) default NULL,
  exercise5 varchar(50) default NULL,
  alcohol1 varchar(50) default NULL,
  alcohol2 varchar(50) default NULL,
  alcohol3 varchar(50) default NULL,
  alcohol4 varchar(50) default NULL,
  alcohol5 varchar(50) default NULL,
  sexualFunction1 varchar(50) default NULL,
  sexualFunction2 varchar(50) default NULL,
  sexualFunction3 varchar(50) default NULL,
  sexualFunction4 varchar(50) default NULL,
  sexualFunction5 varchar(50) default NULL,
  diet1 varchar(50) default NULL,
  diet2 varchar(50) default NULL,
  diet3 varchar(50) default NULL,
  diet4 varchar(50) default NULL,
  diet5 varchar(50) default NULL,
  otherPlan1 varchar(100) default NULL,
  otherPlan2 varchar(100) default NULL,
  otherPlan3 varchar(100) default NULL,
  otherPlan4 varchar(100) default NULL,
  otherPlan5 varchar(100) default NULL,
  consultant varchar(30) default NULL,
  educator varchar(30) default NULL,
  nutritionist varchar(30) default NULL,
  cdn1 varchar(100) default NULL,
  cdn2 varchar(100) default NULL,
  cdn3 varchar(100) default NULL,
  cdn4 varchar(100) default NULL,
  cdn5 varchar(100) default NULL,
  initials1 varchar(30) default NULL,
  initials2 varchar(30) default NULL,
  initials3 varchar(30) default NULL,
  initials4 varchar(30) default NULL,
  initials5 varchar(30) default NULL,
  resource1 tinyint(1) default NULL,
  resource2 tinyint(1) default NULL,
  PRIMARY KEY  (ID)
) TYPE=MyISAM;

--
-- Table structure for table `groupMembers_tbl`
--

CREATE TABLE groupMembers_tbl (
  groupID int(10) default NULL,
  provider_No varchar(6) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `groups_tbl`
--

CREATE TABLE groups_tbl (
  groupID int(10) NOT NULL auto_increment,
  parentID int(10) default NULL,
  groupDesc varchar(50) default NULL,
  PRIMARY KEY  (groupID)
) TYPE=MyISAM;

--
-- Table structure for table `ichppccode`
--

CREATE TABLE ichppccode (
  ichppccode varchar(10) default NULL,
  diagnostic_code varchar(10) default NULL,
  description varchar(255) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `immunizations`
--

CREATE TABLE immunizations (
  ID int(11) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  provider_no varchar(6) NOT NULL default '',
  immunizations text,
  save_date date NOT NULL default '0001-01-01',
  archived tinyint(1) NOT NULL default '0',
  PRIMARY KEY (ID),
  KEY demographic_no (demographic_no)
) TYPE=MyISAM;

--
-- Table structure for table `mdsMSH`
--

CREATE TABLE mdsMSH (
  segmentID int(10) NOT NULL auto_increment,
  sendingApp char(180) default NULL,
  dateTime datetime NOT NULL default '0000-00-00 00:00:00',
  type char(7) default NULL,
  messageConID char(20) default NULL,
  processingID char(3) default NULL,
  versionID char(8) default NULL,
  acceptAckType char(2) default NULL,
  appAckType char(2) default NULL,
  demographic_no int(10) default '0',
  PRIMARY KEY  (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsNTE`
--

CREATE TABLE mdsNTE (
  segmentID int(10) default NULL,
  sourceOfComment varchar(8) default NULL,
  comment varchar(255) default NULL,
  associatedOBX int(10) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsOBR`
--

CREATE TABLE mdsOBR (
  segmentID int(10) default NULL,
  obrID int(10) default NULL,
  placerOrderNo char(75) default NULL,
  universalServiceID char(200) default NULL,
  observationDateTime char(26) default NULL,
  specimenRecDateTime char(26) default NULL,
  fillerFieldOne char(60) default NULL,
  quantityTiming char(200) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsOBX`
--

CREATE TABLE mdsOBX (
  segmentID int(10) default NULL,
  obxID int(10) default '0',
  valueType char(2) default NULL,
  observationIden varchar(80) default NULL,
  observationSubID varchar(255) default NULL,
  observationValue varchar(255) default NULL,
  abnormalFlags varchar(5) default NULL,
  observationResultStatus char(2) default NULL,
  producersID varchar(60) default NULL,
  associatedOBR int(10) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsPID`
--

CREATE TABLE mdsPID (
  segmentID int(10) default NULL,
  intPatientID char(16) default NULL,
  altPatientID char(15) default NULL,
  patientName char(48) default NULL,
  dOB char(26) default NULL,
  sex char(1) default NULL,
  homePhone char(40) default NULL,
  healthNumber char(16) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsPV1`
--

CREATE TABLE mdsPV1 (
  segmentID int(10) default NULL,
  patientClass char(1) default NULL,
  patientLocation char(80) default NULL,
  refDoctor char(60) default NULL,
  conDoctor char(60) default NULL,
  admDoctor char(60) default NULL,
  vNumber char(20) default NULL,
  accStatus char(2) default NULL,
  admDateTime char(26) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsZCL`
--

CREATE TABLE mdsZCL (
  segmentID int(10) default NULL,
  setID char(4) default NULL,
  consultDoc char(60) default NULL,
  clientAddress char(106) default NULL,
  route char(6) default NULL,
  stop char(6) default NULL,
  area char(2) default NULL,
  reportSet char(1) default NULL,
  clientType char(5) default NULL,
  clientModemPool char(2) default NULL,
  clientFaxNumber char(40) default NULL,
  clientBakFax char(40) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsZCT`
--

CREATE TABLE mdsZCT (
  segmentID int(10) default NULL,
  barCodeIdentifier char(14) default NULL,
  placerGroupNo char(14) default NULL,
  observationDateTime char(26) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsZFR`
--

CREATE TABLE mdsZFR (
  segmentID int(10) default NULL,
  reportForm char(1) default NULL,
  reportFormStatus char(1) default NULL,
  testingLab varchar(5) default NULL,
  medicalDirector varchar(255) default NULL,
  editFlag varchar(255) default NULL,
  abnormalFlag varchar(255) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsZLB`
--

CREATE TABLE mdsZLB (
  segmentID int(10) default NULL,
  labID varchar(5) default NULL,
  labIDVersion varchar(255) default NULL,
  labAddress varchar(100) default NULL,
  primaryLab varchar(5) default NULL,
  primaryLabVersion varchar(5) default NULL,
  MDSLU varchar(5) default NULL,
  MDSLV varchar(5) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsZMC`
--

CREATE TABLE mdsZMC (
  segmentID int(10) default NULL,
  setID varchar(10) default NULL,
  messageCodeIdentifier varchar(10) default NULL,
  messageCodeVersion varchar(255) default NULL,
  noMessageCodeDescLines varchar(30) default NULL,
  sigFlag varchar(5) default NULL,
  messageCodeDesc varchar(255) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsZMN`
--

CREATE TABLE mdsZMN (
  segmentID int(10) default NULL,
  resultMnemonic varchar(20) default NULL,
  resultMnemonicVersion varchar(255) default NULL,
  reportName varchar(255) default NULL,
  units varchar(60) default NULL,
  cumulativeSequence varchar(255) default NULL,
  referenceRange varchar(255) default NULL,
  resultCode varchar(20) default NULL,
  reportForm varchar(255) default NULL,
  reportGroup varchar(10) default NULL,
  reportGroupVersion varchar(255) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;

--
-- Table structure for table `mdsZRG`
--

CREATE TABLE mdsZRG (
  segmentID int(10) default NULL,
  reportSequence varchar(255) default NULL,
  reportGroupID varchar(10) default NULL,
  reportGroupVersion varchar(10) default NULL,
  reportFlags varchar(255) default NULL,
  reportGroupDesc varchar(30) default NULL,
  MDSIndex varchar(255) default NULL,
  reportGroupHeading varchar(255) default NULL,
  KEY segmentID (segmentID)
) TYPE=MyISAM;


--
-- Table structure for table `measurements`
--
CREATE TABLE measurements(
  id int UNSIGNED AUTO_INCREMENT,
  type varchar(4) NOT NULL,
  demographicNo int(10) NOT NULL default '0', 
  providerNo varchar(6) NOT NULL default '',
  dataField  varchar(10) NOT NULL,
  measuringInstruction varchar(255) NOT NULL,  
  comments varchar(255) NOT NULL, 
  dateObserved datetime NOT NULL, 
  dateEntered datetime NOT NULL,
  PRIMARY KEY(id)
) TYPE=MYISAM;

--
-- Table structure for table `measurementCSSLocation`
--
DROP TABLE IF EXISTS measurementCSSLocation;
CREATE TABLE measurementCSSLocation(
  cssID int(9) NOT NULL auto_increment,
  location varchar(255) NOT NULL,  
  PRIMARY KEY  (cssID) 
) TYPE=MyISAM;

--
-- Table structure for table `measurementsDeleted`
--
CREATE TABLE measurementsDeleted(
  id int UNSIGNED AUTO_INCREMENT,
  type varchar(4) NOT NULL,
  demographicNo int(10) NOT NULL default '0', 
  providerNo varchar(6) NOT NULL default '',
  dataField  varchar(10) NOT NULL,
  measuringInstruction varchar(255) NOT NULL,  
  comments varchar(255) NOT NULL, 
  dateObserved datetime NOT NULL, 
  dateEntered datetime NOT NULL,
  dateDeleted datetime NOT NULL,
  PRIMARY KEY(id)
) TYPE=MYISAM;

--
-- Table structure for table `measurementGroup`
--
DROP TABLE IF EXISTS measurementGroup;
CREATE TABLE measurementGroup(
  name varchar(100) NOT NULL,
  typeDisplayName varchar(20) 
) TYPE =MyISAM;

--
-- Table structure for table `measurementGroupStyle`
--
CREATE TABLE measurementGroupStyle(
  groupID int(9) NOT NULL auto_increment,
  groupName varchar(100) NOT NULL,
  cssID int(9) NOT NULL,
  PRIMARY KEY  (groupID) 
) TYPE=MyISAM;



--
-- Table structure for table `measurementType`
--
DROP TABLE IF EXISTS measurementType;
CREATE TABLE measurementType (
  id int UNSIGNED AUTO_INCREMENT,
  type varchar(4) NOT NULL,
  typeDisplayName varchar(20) NOT NULL,
  typeDescription varchar(255) NOT NULL, 
  measuringInstruction varchar(255) NOT NULL, 
  validation varchar(100) NOT NULL,
  PRIMARY KEY(id)
) TYPE =MyISAM;

--
-- Table structure for table `measurementTypeDeleted`
--
CREATE TABLE measurementTypeDeleted (
  id int UNSIGNED AUTO_INCREMENT,
  type varchar(4) NOT NULL,
  typeDisplayName varchar(20) NOT NULL,
  typeDescription varchar(255) NOT NULL, 
  measuringInstruction varchar(255) NOT NULL, 
  validation varchar(100) NOT NULL,
  dateDeleted datetime NOT NULL,
  PRIMARY KEY(id)
) TYPE =MyISAM;
--
-- Table structure for table `messagelisttbl`
--

CREATE TABLE messagelisttbl (
  message mediumint(9) default NULL,
  provider_no varchar(6) default NULL,
  status varchar(10) default NULL,
  remoteLocation int(10) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `messagetbl`
--

CREATE TABLE messagetbl (
  messageid mediumint(9) NOT NULL auto_increment,
  thedate date default NULL,
  theime time default NULL,
  themessage text,
  thesubject varchar(128) default NULL,
  sentby varchar(62) default NULL,
  sentto varchar(255) default NULL,
  sentbyNo varchar(6) default NULL,
  sentByLocation int(10) default NULL,
  attachment text,
  actionstatus char(2) default NULL,
  PRIMARY KEY  (messageid)
) TYPE=MyISAM;

--
-- Table structure for table `mygroup`
--

CREATE TABLE mygroup (
  mygroup_no varchar(10) NOT NULL default '',
  provider_no varchar(6) NOT NULL default '',
  last_name varchar(30) NOT NULL default '',
  first_name varchar(30) NOT NULL default '',
  vieworder char(2) default NULL,
  PRIMARY KEY  (mygroup_no,provider_no)
) TYPE=MyISAM;

--
-- Table structure for table `oscarcommlocations`
--

CREATE TABLE oscarcommlocations (
  locationId int(10) NOT NULL default '0',
  locationDesc varchar(50) NOT NULL default '',
  locationAuth varchar(30) default NULL,
  current tinyint(1) NOT NULL default '0',
  addressBook text,
  remoteServerURL varchar(30) default NULL,
  PRIMARY KEY  (locationId)
) TYPE=MyISAM;

--
-- Table structure for table `patientLabRouting`
--

CREATE TABLE patientLabRouting (
  demographic_no int(10) NOT NULL default '0',
  lab_no int(10) NOT NULL default '0',
  PRIMARY KEY  (lab_no),
  KEY demographic (demographic_no)
) TYPE=MyISAM;

--
-- Table structure for table `preference`
--

CREATE TABLE preference (
  preference_no int(6) NOT NULL auto_increment,
  provider_no varchar(6) NOT NULL default '',
  start_hour char(2) default NULL,
  end_hour char(2) default NULL,
  every_min char(3) default NULL,
  mygroup_no varchar(10) default NULL,
  color_template varchar(10) default NULL,
  PRIMARY KEY  (preference_no),
  KEY provider_no (provider_no)
) TYPE=MyISAM;

--
-- Table structure for table `prescribe`
--

CREATE TABLE prescribe (
  prescribe_no int(12) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  provider_no varchar(6) NOT NULL default '',
  prescribe_date date NOT NULL default '0001-01-01',
  prescribe_time time NOT NULL default '00:00:00',
  content text,
  PRIMARY KEY  (prescribe_no)
) TYPE=MyISAM;

--
-- Table structure for table `prescription`
--

CREATE TABLE prescription (
  script_no int(10) NOT NULL auto_increment,
  provider_no varchar(6) default NULL,
  demographic_no int(10) default NULL,
  date_prescribed date default NULL,
  date_printed date default NULL,
  dates_reprinted text,
  textView text,
  PRIMARY KEY  (script_no)
) TYPE=MyISAM;

--
-- Table structure for table `professionalSpecialists`
--

CREATE TABLE professionalSpecialists (
  specId int(10) NOT NULL auto_increment,
  fName varchar(32) default NULL,
  lName varchar(32) default NULL,
  proLetters varchar(20) default NULL,
  address varchar(255) default NULL,
  phone varchar(30) default NULL,
  fax varchar(30) default NULL,
  website varchar(128) default NULL,
  email varchar(128) default NULL,
  specType varchar(128) default NULL,
  PRIMARY KEY  (specId)
) TYPE=MyISAM;

--
-- Table structure for table `property`
--

CREATE TABLE property (
  name varchar(255) NOT NULL default '',
  value varchar(255) default NULL,
  PRIMARY KEY  (name)
) TYPE=MyISAM;

--
-- Table structure for table `provider`
--

CREATE TABLE provider (
  provider_no varchar(6) NOT NULL default '',
  last_name varchar(30) NOT NULL default '',
  first_name varchar(30) NOT NULL default '',
  provider_type varchar(15) NOT NULL default '',
  specialty varchar(20) NOT NULL default '',
  team varchar(20) default '',
  sex char(1) NOT NULL default '',
  dob date default NULL,
  address varchar(40) default NULL,
  phone varchar(20) default NULL,
  work_phone varchar(50) default NULL,
  ohip_no varchar(20) default NULL,
  rma_no varchar(20) default NULL,
  billing_no varchar(20) default NULL,
  hso_no varchar(10) default NULL,
  status char(1) default NULL,
  comments text,
  provider_activity char(3) default NULL,
  PRIMARY KEY  (provider_no)
) TYPE=MyISAM;

--
-- Table structure for table `providerExt`
--

CREATE TABLE providerExt (
  provider_no varchar(6) default NULL,
  signature varchar(255) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `providerLabRouting`
--

CREATE TABLE providerLabRouting (
  provider_no varchar(6) NOT NULL default '',
  lab_no int(10) NOT NULL default '0',
  status char(1) default '',
  comment varchar(255) default '',
  timestamp timestamp(14) NOT NULL,
  PRIMARY KEY  (provider_no,lab_no)
) TYPE=MyISAM;

--
-- Table structure for table `radetail`
--

CREATE TABLE radetail (
  radetail_no int(6) NOT NULL auto_increment,
  raheader_no int(6) NOT NULL default '0',
  providerohip_no varchar(12) NOT NULL default '',
  billing_no int(6) NOT NULL default '0',
  service_code varchar(5) NOT NULL default '',
  service_count char(2) NOT NULL default '',
  hin varchar(12) NOT NULL default '',
  amountclaim varchar(8) NOT NULL default '',
  amountpay varchar(8) NOT NULL default '',
  service_date varchar(12) NOT NULL default '',
  error_code char(2) NOT NULL default '',
  billtype char(3) NOT NULL default '',
  PRIMARY KEY  (radetail_no)
) TYPE=MyISAM;

--
-- Table structure for table `raheader`
--

CREATE TABLE raheader (
  raheader_no int(6) NOT NULL auto_increment,
  filename varchar(12) NOT NULL default '',
  paymentdate varchar(8) NOT NULL default '',
  payable varchar(30) NOT NULL default '',
  totalamount varchar(10) NOT NULL default '',
  records varchar(5) NOT NULL default '',
  claims varchar(5) NOT NULL default '',
  status char(1) NOT NULL default '',
  readdate varchar(12) NOT NULL default '',
  content text,
  PRIMARY KEY  (raheader_no)
) TYPE=MyISAM;

--
-- Table structure for table `recycle_bin`
--

CREATE TABLE recycle_bin (
  provider_no varchar(6) NOT NULL default '',
  table_name varchar(30) NOT NULL default '',
  table_content text,
  updatedatetime datetime default NULL
) TYPE=MyISAM;

--
-- Table structure for table `recyclebin`
--

CREATE TABLE recyclebin (
  recyclebin_no int(12) NOT NULL auto_increment,
  provider_no varchar(6) default NULL,
  updatedatetime datetime default NULL,
  table_name varchar(30) default NULL,
  keyword varchar(50) default NULL,
  table_content text,
  PRIMARY KEY  (recyclebin_no),
  KEY keyword (keyword)
) TYPE=MyISAM;

--
-- Table structure for table `remoteAttachments`
--

CREATE TABLE remoteAttachments (
  demographic_no int(10) default NULL,
  messageid mediumint(9) default NULL,
  savedBy varchar(255) default NULL,
  date date default NULL,
  time time default NULL
) TYPE=MyISAM;

--
-- Table structure for table `reportagesex`
--

CREATE TABLE reportagesex (
  demographic_no int(10) default NULL,
  age int(4) default '0',
  roster varchar(4) default '',
  sex char(2) default '',
  provider_no varchar(6) default NULL,
  reportdate date default NULL,
  status char(2) default '',
  date_joined date default '0001-01-01'
) TYPE=MyISAM;

--
-- Table structure for table `reportprovider`
--

CREATE TABLE reportprovider (
  provider_no varchar(10) default '',
  team varchar(10) default '',
  action varchar(20) default '',
  status char(1) default ''
) TYPE=MyISAM;

--
-- Table structure for table `reporttemp`
--

CREATE TABLE reporttemp (
  demographic_no int(10) NOT NULL default '0',
  edb date NOT NULL default '0001-01-01',
  demo_name varchar(60) NOT NULL default '',
  provider_no varchar(6) default NULL,
  address text,
  creator varchar(10) default NULL,
  PRIMARY KEY  (demographic_no,edb)
) TYPE=MyISAM;

--
-- Table structure for table `rschedule`
--

CREATE TABLE rschedule (
  provider_no varchar(6) NOT NULL default '',
  sdate date NOT NULL default '0001-01-01',
  edate date default NULL,
  available char(1) NOT NULL default '',
  day_of_week varchar(30) default NULL,
  avail_hourB varchar(255) default NULL,
  avail_hour varchar(230) default NULL,
  creator varchar(50) default NULL,
  PRIMARY KEY  (provider_no,sdate,available)
) TYPE=MyISAM;

--
-- Table structure for table `scheduledate`
--

CREATE TABLE scheduledate (
  sdate date NOT NULL default '0001-01-01',
  provider_no varchar(6) NOT NULL default '',
  available char(1) NOT NULL default '',
  priority char(1) default NULL,
  reason varchar(255) default NULL,
  hour varchar(255) default NULL,
  creator varchar(50) default NULL,
  PRIMARY KEY  (sdate,provider_no)
) TYPE=MyISAM;

--
-- Table structure for table `scheduledaytemplate`
--

CREATE TABLE scheduledaytemplate (
  provider_no varchar(6) NOT NULL default '',
  day date NOT NULL default '0001-01-01',
  template_name varchar(20) default NULL,
  PRIMARY KEY  (provider_no,day)
) TYPE=MyISAM;

--
-- Table structure for table `scheduleholiday`
--

CREATE TABLE scheduleholiday (
  sdate date NOT NULL default '0001-01-01',
  holiday_name varchar(100) NOT NULL default '',
  PRIMARY KEY  (sdate)
) TYPE=MyISAM;

--
-- Table structure for table `scheduletemplate`
--

CREATE TABLE scheduletemplate (
  provider_no varchar(6) NOT NULL default '',
  name varchar(20) NOT NULL default '',
  summary varchar(80) default NULL,
  timecode varchar(255) default NULL,
  PRIMARY KEY  (provider_no,name)
) TYPE=MyISAM;

--
-- Table structure for table `scheduletemplatecode`
--

CREATE TABLE scheduletemplatecode (
  code char(1) NOT NULL default '',
  description varchar(80) default NULL,
  duration char(3) default '',
  color varchar(10) default NULL,
  KEY code (code)
) TYPE=MyISAM;

--
-- Table structure for table `security`
--

CREATE TABLE security (
  security_no int(6) NOT NULL auto_increment,
  user_name varchar(30) NOT NULL default '',
  password varchar(80) NOT NULL default '',
  provider_no varchar(6) default NULL,
  pin varchar(6) default NULL,
  PRIMARY KEY  (security_no),
  KEY user_name (user_name)
) TYPE=MyISAM;

--
-- Table structure for table `serviceSpecialists`
--

CREATE TABLE serviceSpecialists (
  serviceId int(10) default NULL,
  specId int(10) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `specialistsJavascript`
--

CREATE TABLE specialistsJavascript (
  setId char(1) default NULL,
  javascriptString text
) TYPE=MyISAM;

--
-- Table structure for table `study`
--

CREATE TABLE study (
  study_no int(3) NOT NULL auto_increment,
  study_name varchar(20) NOT NULL default '',
  study_link varchar(255) NOT NULL default '',
  description varchar(255) NOT NULL default '',
  form_name varchar(30) default NULL,
  current tinyint(1) default '0',
  remote_serverurl varchar(50) default NULL,
  provider_no varchar(6) NOT NULL default '',
  timestamp timestamp(14) NOT NULL,
  PRIMARY KEY  (study_no)
) TYPE=MyISAM;

--
-- Table structure for table `studydata`
--

CREATE TABLE studydata (
  studydata_no int(10) NOT NULL auto_increment,
  demographic_no int(10) NOT NULL default '0',
  study_no int(3) NOT NULL default '0',
  provider_no varchar(6) NOT NULL default '',
  timestamp timestamp(14) NOT NULL,
  status varchar(30) default NULL,
  content text,
  PRIMARY KEY  (studydata_no)
) TYPE=MyISAM;

--
-- Table structure for table `studylogin`
--

CREATE TABLE studylogin (
  id int(6) NOT NULL auto_increment,
  provider_no varchar(6) default NULL,
  study_no int(3) default NULL,
  remote_login_url varchar(100) default NULL,
  url_name_username varchar(20) NOT NULL default '',
  url_name_password varchar(20) NOT NULL default '',
  username varchar(30) NOT NULL default '',
  password varchar(100) NOT NULL default '',
  current tinyint(1) default NULL,
  creator varchar(6) NOT NULL default '',
  timestamp timestamp(14) NOT NULL,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `tickler`
--

CREATE TABLE tickler (
  tickler_no int(10) NOT NULL auto_increment,
  demographic_no int(10) default '0',
  message text,
  status char(1) default NULL,
  update_date datetime default '0001-01-01 00:00:00',
  service_date date default NULL,
  creator varchar(6) default NULL,
  PRIMARY KEY  (tickler_no)
) TYPE=MyISAM;

--
-- Table structure for table `tmpdiagnosticcode`
--

CREATE TABLE tmpdiagnosticcode (
  diagnosticcode_no int(5) NOT NULL auto_increment,
  diagnostic_code varchar(5) NOT NULL default '',
  description text,
  status char(1) default NULL,
  PRIMARY KEY  (diagnosticcode_no)
) TYPE=MyISAM;

--
-- Table structure for table `validations`
--
DROP TABLE IF EXISTS validations;
CREATE TABLE validations(
  id int UNSIGNED AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  regularExp varchar(100) ,
  maxValue double, 
  minValue double, 
  maxLength int(3), 
  minLength int(3), 
  isNumeric bool, 
  isTrue bool,
 PRIMARY KEY(id)
) TYPE =MyISAM;