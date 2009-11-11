------------------------------------------------------------------
-- My2Pg 1.27 translated dump
--
------------------------------------------------------------------

BEGIN;




--
-- Sequences for table FAXCLIENTLOG
--

CREATE SEQUENCE faxclientlog_numeric_seq;

-- MySQL dump 9.07
--
-- Host: localhost    Database: oscar_mcmaster
---------------------------------------------------------
-- Server version	4.0.12-standard

--
-- Table structure for table 'FaxClientLog'
--

CREATE TABLE FaxClientLog (
  faxLogId numeric(9) DEFAULT nextval('faxclientlog_numeric_seq'),
  provider_no varchar(6) DEFAULT NULL,
  startTime TIMESTAMP DEFAULT NULL,
  endTime TIMESTAMP DEFAULT NULL,
  result varchar(255) DEFAULT NULL,
  requestId varchar(10) DEFAULT NULL,
  faxId varchar(10) DEFAULT NULL,
  PRIMARY KEY (faxLogId)

);

--
-- Table structure for table 'allergies'
--



--
-- Sequences for table ALLERGIES
--

CREATE SEQUENCE allergies_numeric_seq;

CREATE TABLE allergies (
  allergyid numeric(10) DEFAULT nextval('allergies_numeric_seq'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  entry_date DATE DEFAULT NULL,
  DESCRIPTION char(50) NOT NULL DEFAULT '',
  HICL_SEQNO numeric(6) DEFAULT NULL,
  HIC_SEQNO numeric(6) DEFAULT NULL,
  AGCSP numeric(6) DEFAULT NULL,
  AGCCS numeric(6) DEFAULT NULL,
  TYPECODE INT2 NOT NULL DEFAULT '0',
  reaction text,
  drugref_id varchar(100) default NULL,
  PRIMARY KEY (allergyid)

);

--
-- Table structure for table 'appointment'
--



--
-- Sequences for table APPOINTMENT
--

CREATE SEQUENCE appointment_numeric_seq;

CREATE TABLE appointment (
  appointment_no numeric(12) DEFAULT nextval('appointment_numeric_seq'),
  provider_no varchar(6) NOT NULL DEFAULT '',
  appointment_date DATE NOT NULL DEFAULT '0001-01-01',
  start_time time NOT NULL DEFAULT '00:00:00',
  end_time time NOT NULL DEFAULT '00:00:00',
  name varchar(50) DEFAULT NULL,
  demographic_no numeric(10) DEFAULT NULL,
  notes varchar(80) DEFAULT NULL,
  reason varchar(80) DEFAULT NULL,
  location varchar(10) DEFAULT NULL,
  resources varchar(255) DEFAULT NULL,
  type varchar(10) DEFAULT NULL,
  style varchar(10) DEFAULT NULL,
  billing varchar(10) DEFAULT NULL,
  status char(2) DEFAULT NULL,
  createdatetime TIMESTAMP DEFAULT NULL,
  creator varchar(50) DEFAULT NULL,
  remarks varchar(50) DEFAULT NULL,
  PRIMARY KEY (appointment_no)

);

--
-- Table structure for table batchEligibility
--
CREATE TABLE batchEligibility(
  responseCode numeric(9) NOT NULL ,
  MOHResponse varchar(100) NOT NULL,
  reason varchar(100) NOT NULL,
  PRIMARY KEY  (responseCode)
);

--
-- Table structure for table 'billactivity'
--

CREATE TABLE billactivity (
  monthCode char(1) DEFAULT NULL,
  batchcount numeric(3) DEFAULT NULL,
  htmlfilename varchar(50) DEFAULT NULL,
  ohipfilename varchar(50) DEFAULT NULL,
  providerohipno varchar(6) DEFAULT NULL,
  groupno varchar(4) DEFAULT NULL,
  creator varchar(6) DEFAULT NULL,
  htmlcontext text,
  ohipcontext text,
  claimrecord varchar(10) DEFAULT NULL,
  updatedatetime TIMESTAMP DEFAULT NULL,
  status char(1) DEFAULT NULL,
  total varchar(20) DEFAULT NULL
);

--
-- Table structure for table 'billcenter'
--

CREATE TABLE billcenter (
  billcenter_code char(2) NOT NULL DEFAULT '',
  billcenter_desc varchar(20) DEFAULT NULL
);


--
-- Sequences for table BILLING
--

CREATE SEQUENCE billing_numeric_seq;

--
-- Table structure for table 'billing'
--

CREATE TABLE billing (
  billing_no numeric(10) DEFAULT nextval('billing_numeric_seq'),
  clinic_no numeric(10) NOT NULL DEFAULT '0',
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  provider_no varchar(6) NOT NULL DEFAULT '',
  appointment_no numeric(12) DEFAULT NULL,
  organization_spec_code varchar(6) DEFAULT NULL,
  demographic_name varchar(60) DEFAULT NULL,
  hin varchar(12) DEFAULT NULL,
  update_date DATE DEFAULT NULL,
  update_time time DEFAULT NULL,
  billing_date DATE DEFAULT NULL,
  billing_time time DEFAULT NULL,
  clinic_ref_code varchar(10) DEFAULT NULL,
  content text,
  total varchar(6) DEFAULT NULL,
  status char(1) DEFAULT NULL,
  dob varchar(8) DEFAULT NULL,
  visitdate DATE DEFAULT NULL,
  visittype char(2) DEFAULT NULL,
  provider_ohip_no varchar(20) DEFAULT NULL,
  provider_rma_no varchar(20) DEFAULT NULL,
  apptProvider_no varchar(6) DEFAULT NULL,
  asstProvider_no varchar(6) DEFAULT NULL,
  creator varchar(6) DEFAULT NULL,
  PRIMARY KEY (billing_no)

);

--
-- Table structure for table 'billingdetail'
--



--
-- Sequences for table BILLINGDETAIL
--

CREATE SEQUENCE billingdetail_numeric_seq;

CREATE TABLE billingdetail (
  billing_dt_no numeric(10) DEFAULT nextval('billingdetail_numeric_seq'),
  billing_no numeric(10) NOT NULL DEFAULT '0',
  service_code varchar(5) DEFAULT NULL,
  service_desc varchar(255) DEFAULT NULL,
  billing_amount varchar(6) DEFAULT NULL,
  diagnostic_code char(3) DEFAULT NULL,
  appointment_date DATE DEFAULT NULL,
  status char(1) DEFAULT NULL,
  billingunit char(1) DEFAULT NULL,
  PRIMARY KEY (billing_dt_no)

);

--
-- Table structure for table 'billinginr'
--



--
-- Sequences for table BILLINGINR
--

CREATE SEQUENCE billinginr_numeric_seq;

CREATE TABLE billinginr (
  billinginr_no numeric(10) DEFAULT nextval('billinginr_numeric_seq'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  demographic_name varchar(60) NOT NULL DEFAULT '',
  hin varchar(12) DEFAULT NULL,
  dob varchar(8) DEFAULT NULL,
  provider_no numeric(10) DEFAULT NULL,
  provider_ohip_no varchar(20) DEFAULT NULL,
  provider_rma_no varchar(20) DEFAULT NULL,
  creator varchar(6) DEFAULT NULL,
  diagnostic_code char(3) DEFAULT NULL,
  service_code varchar(6) DEFAULT NULL,
  service_desc varchar(255) DEFAULT NULL,
  billing_amount varchar(6) DEFAULT NULL,
  billing_unit char(1) DEFAULT NULL,
  createdatetime TIMESTAMP NOT NULL DEFAULT '0001-01-01 00:00:00',
  status char(1) DEFAULT NULL,
  PRIMARY KEY (billinginr_no)

);

--
-- Table structure for table 'billingservice'
--



--
-- Sequences for table BILLINGSERVICE
--

CREATE SEQUENCE billingservice_numeric_seq;

CREATE TABLE billingservice (
  billingservice_no numeric(10) DEFAULT nextval('billingservice_numeric_seq'),
  service_compositecode varchar(30) DEFAULT NULL,
  service_code varchar(10) DEFAULT NULL,
  description text,
  value varchar(8) DEFAULT NULL,
  percentage varchar(8) DEFAULT NULL,
  billingservice_date DATE DEFAULT NULL,
  specialty varchar(15) default NULL,
  region varchar(5) default NULL,
  anaesthesia char(2) default NULL,
  PRIMARY KEY (billingservice_no)

);

--
-- Table structure for table 'clinic'
--



--
-- Sequences for table CLINIC
--

CREATE SEQUENCE clinic_numeric_seq;

CREATE TABLE clinic (
  clinic_no numeric(10) DEFAULT nextval('clinic_numeric_seq'),
  clinic_name varchar(50) DEFAULT NULL,
  clinic_address varchar(60) DEFAULT '',
  clinic_city varchar(40) DEFAULT '',
  clinic_postal varchar(15) DEFAULT '',
  clinic_phone varchar(50) DEFAULT NULL,
  clinic_fax varchar(20) DEFAULT '',
  clinic_location_code varchar(10) DEFAULT NULL,
  status char(1) DEFAULT NULL,
  clinic_province varchar(40) DEFAULT NULL,
  clinic_delim_phone text,
  clinic_delim_fax text,
  PRIMARY KEY (clinic_no)

);

--
-- Table structure for table 'clinic_location'
--

CREATE TABLE clinic_location (
  clinic_location_no varchar(15) NOT NULL DEFAULT '',
  clinic_no numeric(10) NOT NULL DEFAULT '0',
  clinic_location_name varchar(40) DEFAULT NULL
);

--
-- Table structure for table 'config_Immunization'
--



--
-- Sequences for table CONFIG_IMMUNIZATION
--

CREATE SEQUENCE config_immunization_numeric_;

CREATE TABLE config_Immunization (
  setId numeric(10) DEFAULT nextval('config_immunization_numeric_'),
  setName varchar(255) DEFAULT NULL,
  setXmlDoc text,
  createDate DATE DEFAULT NULL,
  providerNo varchar(6) DEFAULT NULL,
  archived INT2 NOT NULL DEFAULT '0',
  PRIMARY KEY (setId)

);

--
-- Table structure for table 'consultationRequests'
--



--
-- Sequences for table CONSULTATIONREQUESTS
--

CREATE SEQUENCE consultationrequests_numeric;

CREATE TABLE consultationRequests (
  referalDate DATE DEFAULT NULL,
  serviceId numeric(10) DEFAULT NULL,
  specId numeric(10) DEFAULT NULL,
  appointmentDate DATE DEFAULT NULL,
  appointmentTime time DEFAULT NULL,
  reason text,
  clinicalInfo text,
  currentMeds text,
  allergies text,
  providerNo varchar(6) DEFAULT NULL,
  demographicNo numeric(10) DEFAULT NULL,
  status char(2) DEFAULT NULL,
  statusText text,
  sendTo varchar(20) DEFAULT NULL,
  requestId numeric(10) DEFAULT nextval('consultationrequests_numeric'),
  concurrentProblems text,
  urgency char(2) DEFAULT NULL,
  PRIMARY KEY (requestId)

);

--
-- Table structure for table 'consultationServices'
--



--
-- Sequences for table CONSULTATIONSERVICES
--

CREATE SEQUENCE consultationservices_numeric;

CREATE TABLE consultationServices (
  serviceId numeric(10) DEFAULT nextval('consultationservices_numeric'),
  serviceDesc varchar(255) DEFAULT NULL,
  active char(2) DEFAULT NULL,
  PRIMARY KEY (serviceId)

);

--
-- Table structure for table 'ctl_billingservice'
--

CREATE TABLE ctl_billingservice (
  servicetype_name varchar(150) DEFAULT NULL,
  servicetype varchar(10) DEFAULT NULL,
  service_code varchar(10) DEFAULT NULL,
  service_group_name varchar(20) DEFAULT NULL,
  service_group varchar(20) DEFAULT NULL,
  status char(1) DEFAULT NULL,
  service_order numeric(4) DEFAULT NULL
);

--
-- Table structure for table 'ctl_billingservice_premium'
--

CREATE TABLE ctl_billingservice_premium (
  servicetype_name varchar(150) DEFAULT '',
  service_code varchar(10) DEFAULT '',
  status char(1) DEFAULT '',
  update_date DATE DEFAULT NULL
);

--
-- Table structure for table 'ctl_diagcode'
--

CREATE TABLE ctl_diagcode (
  servicetype varchar(10) DEFAULT NULL,
  diagnostic_code varchar(5) DEFAULT NULL,
  status char(1) DEFAULT NULL
);

--
-- Table structure for table 'ctl_doctype'
--

CREATE TABLE ctl_doctype (
  module varchar(30) NOT NULL DEFAULT '',
  doctype varchar(60) NOT NULL DEFAULT '',
  status char(1) DEFAULT NULL
);

--
-- Table structure for table 'ctl_document'
--

CREATE TABLE ctl_document (
  module varchar(30) NOT NULL DEFAULT '',
  module_id numeric(12) NOT NULL DEFAULT '0',
  document_no numeric(6) NOT NULL DEFAULT '0',
  status char(1) DEFAULT NULL
);

--
-- Table structure for table 'ctl_frequency'
--



--
-- Sequences for table CTL_FREQUENCY
--

CREATE SEQUENCE ctl_frequency_int_seq;

CREATE TABLE ctl_frequency (
  freqid INT2 DEFAULT nextval('ctl_frequency_int_seq'),
  freqcode char(6) NOT NULL DEFAULT '',
  dailymin INT2 NOT NULL DEFAULT '0',
  dailymax INT2 NOT NULL DEFAULT '0',
  PRIMARY KEY (freqid)

);

--
-- Table structure for table 'ctl_provider'
--

CREATE TABLE ctl_provider (
  clinic_no numeric(10) NOT NULL DEFAULT '0',
  provider_no numeric(10) NOT NULL DEFAULT '0',
  status char(1) DEFAULT NULL
);


--
-- Table structure for table 'ctl_specialinstructions'
--



--
-- Sequences for table CTL_SPECIALINSTRUCTIONS
--

CREATE SEQUENCE ctl_specialinstructions_int_;

CREATE TABLE ctl_specialinstructions (
  id INT2 DEFAULT nextval('ctl_specialinstructions_int_'),
  description varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (id)

);


--
-- Table structure for table 'demographic'
--



--
-- Sequences for table DEMOGRAPHIC
--

CREATE SEQUENCE demographic_numeric_seq;

CREATE TABLE demographic (
  demographic_no numeric(10) DEFAULT nextval('demographic_numeric_seq'),
  last_name varchar(30) NOT NULL DEFAULT '',
  first_name varchar(30) NOT NULL DEFAULT '',
  address varchar(60) DEFAULT NULL,
  city varchar(20) DEFAULT NULL,
  province varchar(20) DEFAULT NULL,
  postal varchar(9) DEFAULT NULL,
  phone varchar(20) DEFAULT NULL,
  phone2 varchar(20) DEFAULT NULL,
  email varchar(100) DEFAULT NULL,
  pin varchar(255) DEFAULT NULL,
  year_of_birth varchar(4) DEFAULT NULL,
  month_of_birth char(2) DEFAULT NULL,
  date_of_birth char(2) DEFAULT NULL,
  hin varchar(20) DEFAULT NULL,
  ver char(3) DEFAULT NULL,
  roster_status varchar(20) DEFAULT NULL,
  patient_status varchar(20) DEFAULT NULL,
  date_joined DATE DEFAULT NULL,
  chart_no varchar(10) DEFAULT NULL,
  provider_no varchar(250) DEFAULT NULL,
  sex char(1) NOT NULL DEFAULT '',
  end_date DATE DEFAULT NULL,
  eff_date DATE DEFAULT NULL,
  pcn_indicator varchar(20) DEFAULT NULL,
  hc_type varchar(20) DEFAULT NULL,
  hc_renew_date DATE DEFAULT NULL,
  family_doctor varchar(80) DEFAULT NULL,
  PRIMARY KEY (demographic_no)

);

--
-- Table structure for table 'demographicaccessory'
--

CREATE TABLE demographicaccessory (
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  content text,
  PRIMARY KEY (demographic_no)

);

--
-- Table structure for table 'demographiccust'
--

CREATE TABLE demographiccust (
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  cust1 varchar(255) DEFAULT NULL,
  cust2 varchar(255) DEFAULT NULL,
  cust3 varchar(255) DEFAULT NULL,
  cust4 varchar(255) DEFAULT NULL,
  content text,
  PRIMARY KEY (demographic_no)

);

--
-- Table structure for table 'demographicstudy'
--

CREATE TABLE demographicstudy (
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  study_no numeric(3) NOT NULL DEFAULT '0',
  provider_no varchar(6) NOT NULL DEFAULT '',
  timestamp timestamp(6) NOT NULL,
  PRIMARY KEY (demographic_no,study_no)

);

--
-- Table structure for table 'desannualreviewplan'
--



--
-- Sequences for table DESANNUALREVIEWPLAN
--

CREATE SEQUENCE desannualreviewplan_numeric_;

CREATE TABLE desannualreviewplan (
  des_no numeric(10) DEFAULT nextval('desannualreviewplan_numeric_'),
  des_date DATE NOT NULL DEFAULT '0001-01-01',
  des_time time NOT NULL DEFAULT '00:00:00',
  demographic_no numeric(10) DEFAULT '0',
  form_no numeric(10) DEFAULT '0',
  provider_no varchar(6) NOT NULL DEFAULT '',
  risk_content text,
  checklist_content text,
  PRIMARY KEY (des_no)

);

--
-- Table structure for table 'desaprisk'
--



--
-- Sequences for table DESAPRISK
--

CREATE SEQUENCE desaprisk_numeric_seq;

CREATE TABLE desaprisk (
  desaprisk_no numeric(10) DEFAULT nextval('desaprisk_numeric_seq'),
  desaprisk_date DATE NOT NULL DEFAULT '0001-01-01',
  desaprisk_time time NOT NULL DEFAULT '00:00:00',
  demographic_no numeric(10) DEFAULT '0',
  form_no numeric(10) DEFAULT '0',
  provider_no varchar(6) NOT NULL DEFAULT '',
  risk_content text,
  checklist_content text,
  PRIMARY KEY (desaprisk_no)

);

--
-- Table structure for table 'diagnosticcode'
--



--
-- Sequences for table DIAGNOSTICCODE
--

CREATE SEQUENCE diagnosticcode_numeric_seq;

CREATE TABLE diagnosticcode (
  diagnosticcode_no numeric(5) DEFAULT nextval('diagnosticcode_numeric_seq'),
  diagnostic_code varchar(5) NOT NULL DEFAULT '',
  description text,
  status char(1) DEFAULT NULL,
  region varchar(5) default NULL,
  PRIMARY KEY (diagnosticcode_no)

);

--
-- Table structure for table 'diseases'
--



--
-- Sequences for table DISEASES
--

CREATE SEQUENCE diseases_numeric_seq;

CREATE TABLE diseases (
  diseaseid numeric(10) DEFAULT nextval('diseases_numeric_seq'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  ICD9_E char(6) NOT NULL DEFAULT '',
  entry_date DATE DEFAULT NULL,
  PRIMARY KEY (diseaseid)

);

--
-- Table structure for table 'document'
--



--
-- Sequences for table DOCUMENT
--

CREATE SEQUENCE document_numeric_seq;

CREATE TABLE document (
  document_no numeric(6) DEFAULT nextval('document_numeric_seq'),
  doctype varchar(20) DEFAULT NULL,
  docdesc varchar(50) NOT NULL DEFAULT '',
  docxml text,
  docfilename varchar(50) NOT NULL DEFAULT '',
  doccreator varchar(30) NOT NULL DEFAULT '',
  updatedatetime TIMESTAMP DEFAULT NULL,
  status char(1) NOT NULL DEFAULT '',
  contenttype varchar(60) NOT NULL DEFAULT '',
  public int(1) NOT NULL DEFAULT '0',
  observationdate DATE DEFAULT NULL,
  PRIMARY KEY (document_no)

);


--
-- Table structure for table `reportTemplates`
--
CREATE SEQUENCE reportTemplates_numeric_seq;

CREATE TABLE reportTemplates (
  templateid numeric(11) DEFAULT nextval('reportTemplates_numeric_seq'),
  templatetitle varchar(80) NOT NULL DEFAULT '',
  templatedescription text NOT NULL DEFAULT '',
  templatesql text NOT NULL DEFAULT '',
  templatexml text NOT NULL DEFAULT '',
  active tinyint NOT NULL DEFAULT 1,
  PRIMARY KEY (templateid)
);

--
-- Table structure for table 'drugs'
--



--
-- Sequences for table DRUGS
--

CREATE SEQUENCE drugs_numeric_seq;

CREATE TABLE drugs (
  drugid numeric(10) DEFAULT nextval('drugs_numeric_seq'),
  provider_no varchar(6) NOT NULL DEFAULT '',
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  rx_date DATE NOT NULL DEFAULT '0001-01-01',
  end_date DATE NOT NULL DEFAULT '0001-01-01',
  BN varchar(30) DEFAULT NULL,
  GCN_SEQNO decimal(10,0) NOT NULL DEFAULT '0',
  customName varchar(60) DEFAULT NULL,
  takemin float DEFAULT NULL,
  takemax float DEFAULT NULL,
  freqcode varchar(6) DEFAULT NULL,
  duration varchar(4) DEFAULT NULL,
  durunit char(1) DEFAULT NULL,
  quantity varchar(20) DEFAULT NULL,
  repeat INT2 DEFAULT NULL,
  nosubs INT2 NOT NULL DEFAULT '0',
  prn INT2 NOT NULL DEFAULT '0',
  special varchar(255) NOT NULL DEFAULT '',
  archived INT2 NOT NULL DEFAULT '0',
  GN varchar(255) default NULL,
  ATC varchar(20) default NULL,
  script_no numeric(10) default NULL,
  regional_identifier varchar(100) default NULL,
  PRIMARY KEY (drugid)

);

--
-- Table structure for table 'dxresearch'
--



--
-- Sequences for table DXRESEARCH
--

CREATE SEQUENCE dxresearch_numeric_seq;

CREATE TABLE dxresearch (
  dxresearch_no numeric(10) DEFAULT nextval('dxresearch_numeric_seq'),
  demographic_no numeric(10) DEFAULT '0',
  start_date DATE DEFAULT '0001-01-01',
  update_date DATE DEFAULT '0001-01-01',
  status char(1) DEFAULT 'A',
  dxresearch_code varchar(10) DEFAULT '',
  PRIMARY KEY (dxresearch_no)

);

--
-- Table structure for table 'eChart'
--



--
-- Sequences for table ECHART
--

CREATE SEQUENCE echart_numeric_seq;

CREATE TABLE eChart (
  eChartId numeric(15) DEFAULT nextval('echart_numeric_seq'),
  timeStamp timestamp(6) NOT NULL,
  demographicNo numeric(10) NOT NULL DEFAULT '0',
  providerNo varchar(6) NOT NULL DEFAULT '',
  subject varchar(128) DEFAULT NULL,
  socialHistory text,
  familyHistory text,
  medicalHistory text,
  ongoingConcerns text,
  reminders text,
  encounter text,
  PRIMARY KEY (eChartId)

);

--
-- Table structure for table 'eform'
--



--
-- Sequences for table EFORM
--

CREATE SEQUENCE eform_numeric_seq;

CREATE TABLE eform (
  fid numeric(8) DEFAULT nextval('eform_numeric_seq'),
  form_name varchar(255) DEFAULT NULL,
  file_name varchar(255) DEFAULT NULL,
  subject varchar(255) DEFAULT NULL,
  form_date DATE DEFAULT NULL,
  form_time time DEFAULT NULL,
  form_creator varchar(255) DEFAULT NULL,
  status INT2 NOT NULL DEFAULT '1',
  form_html text,
  PRIMARY KEY (fid)

);

--
-- Table structure for table 'eform_data'
--



--
-- Sequences for table EFORM_DATA
--

CREATE SEQUENCE eform_data_numeric_seq;

CREATE TABLE eform_data (
  fdid numeric(8) DEFAULT nextval('eform_data_numeric_seq'),
  fid numeric(8) NOT NULL DEFAULT '0',
  form_name varchar(255) DEFAULT NULL,
  subject varchar(255) DEFAULT NULL,
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  status INT2 NOT NULL DEFAULT '1',
  form_date DATE DEFAULT NULL,
  form_time time DEFAULT NULL,
  form_provider varchar(255) DEFAULT NULL,
  form_data text,
  PRIMARY KEY (fdid)

);

--
-- Table structure for table 'eforms'
--



--
-- Sequences for table EFORMS
--

CREATE SEQUENCE eforms_numeric_seq;

CREATE TABLE eforms (
  fid numeric(8) DEFAULT nextval('eforms_numeric_seq'),
  form_name text,
  file_name text,
  subject text,
  form_date DATE DEFAULT NULL,
  form_time time DEFAULT NULL,
  form_creator text,
  status numeric(1) NOT NULL DEFAULT '0',
  form_html text,
  PRIMARY KEY (fid)

);

--
-- Table structure for table 'eforms_data'
--



--
-- Sequences for table EFORMS_DATA
--

CREATE SEQUENCE eforms_data_numeric_seq;

CREATE TABLE eforms_data (
  fdid numeric(8) DEFAULT nextval('eforms_data_numeric_seq'),
  fid numeric(8) NOT NULL DEFAULT '0',
  form_name text,
  subject text,
  demographic_no numeric(8) NOT NULL DEFAULT '0',
  status numeric(1) NOT NULL DEFAULT '0',
  form_date DATE DEFAULT NULL,
  form_time time DEFAULT NULL,
  form_provider text,
  form_data text,
  form_fields text,
  PRIMARY KEY (fdid)

);

--
-- Table structure for table 'encounter'
--



--
-- Sequences for table ENCOUNTER
--

CREATE SEQUENCE encounter_numeric_seq;

CREATE TABLE encounter (
  encounter_no numeric(12) DEFAULT nextval('encounter_numeric_seq'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  encounter_date DATE NOT NULL DEFAULT '0001-01-01',
  encounter_time time NOT NULL DEFAULT '00:00:00',
  provider_no varchar(6) NOT NULL DEFAULT '',
  subject varchar(100) DEFAULT NULL,
  content text,
  encounterattachment text,
  PRIMARY KEY (encounter_no)

);

--
-- Table structure for table 'encounterForm'
--

CREATE TABLE encounterForm (
  form_name varchar(30) NOT NULL DEFAULT '',
  form_value varchar(255) NOT NULL DEFAULT '',
  form_table varchar(50) NOT NULL DEFAULT '',
  hidden char(2) default '1',
  PRIMARY KEY (form_value)

);

--
-- Table structure for table 'encountertemplate'
--

CREATE TABLE encountertemplate (
  encountertemplate_name varchar(50) NOT NULL DEFAULT '',
  createdatetime TIMESTAMP DEFAULT NULL,
  encountertemplate_value text,
  creator varchar(6) DEFAULT NULL,
  PRIMARY KEY (encountertemplate_name)

);

--
-- Table structure for table `encounterWindow`
--
                                                                                                                                                             
CREATE TABLE encounterWindow (
  provider_no varchar(6) NOT NULL default '',
  rowOneSize numeric(10) NOT NULL default '60',
  rowTwoSize numeric(10) NOT NULL default '60',
  presBoxSize numeric(10) NOT NULL default '30',
  rowThreeSize numeric(10) NOT NULL default '378',
  PRIMARY KEY  (provider_no)
);

--
-- Sequences for table FAVORITES
--

CREATE SEQUENCE favorites_numeric_seq;

--
-- Table structure for table 'favorites'
--

CREATE TABLE favorites (
  favoriteid numeric(10) DEFAULT nextval('favorites_numeric_seq'),
  provider_no varchar(6) NOT NULL DEFAULT '',
  favoritename varchar(50) NOT NULL DEFAULT '',
  BN varchar(30) DEFAULT NULL,
  GCN_SEQNO decimal(10,0) NOT NULL DEFAULT '0',
  customName varchar(60) DEFAULT NULL,
  takemin float DEFAULT NULL,
  takemax float DEFAULT NULL,
  freqcode varchar(6) DEFAULT NULL,
  duration varchar(4) DEFAULT NULL,
  durunit char(1) DEFAULT NULL,
  quantity varchar(20) DEFAULT NULL,
  repeat INT2 DEFAULT NULL,
  nosubs INT2 NOT NULL DEFAULT '0',
  prn INT2 NOT NULL DEFAULT '0',
  special varchar(255) NOT NULL DEFAULT '',
  GN varchar(255) default NULL,
  ATC varchar(255) default NULL,
  regional_identifier varchar(100) default NULL,
  PRIMARY KEY (favoriteid)

);

--
-- Table structure for table 'form'
--



--
-- Sequences for table FORM
--

CREATE SEQUENCE form_numeric_seq;

CREATE TABLE form (
  form_no numeric(12) DEFAULT nextval('form_numeric_seq'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  provider_no varchar(6) NOT NULL DEFAULT '',
  form_date DATE NOT NULL DEFAULT '0001-01-01',
  form_time time NOT NULL DEFAULT '00:00:00',
  form_name varchar(50) DEFAULT NULL,
  content text,
  PRIMARY KEY (form_no)

);

--
-- Table structure for table 'formAR'
--



--
-- Sequences for table FORMAR
--

CREATE SEQUENCE formar_numeric_seq;

CREATE TABLE formAR (
  ID numeric(10) DEFAULT nextval('formar_numeric_seq'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  provider_no numeric(10) DEFAULT NULL,
  formCreated DATE DEFAULT NULL,
  formEdited timestamp(6) NOT NULL,
  c_lastVisited char(3) DEFAULT NULL,
  c_pName varchar(60) DEFAULT NULL,
  c_address varchar(80) DEFAULT NULL,
  pg1_dateOfBirth DATE DEFAULT NULL,
  pg1_age char(2) DEFAULT NULL,
  pg1_msSingle INT2 DEFAULT NULL,
  pg1_msCommonLaw INT2 DEFAULT NULL,
  pg1_msMarried INT2 DEFAULT NULL,
  pg1_eduLevel varchar(25) DEFAULT NULL,
  pg1_occupation varchar(25) DEFAULT NULL,
  pg1_language varchar(25) DEFAULT NULL,
  pg1_homePhone varchar(20) DEFAULT NULL,
  pg1_workPhone varchar(20) DEFAULT NULL,
  pg1_partnerName varchar(50) DEFAULT NULL,
  pg1_partnerAge char(2) DEFAULT NULL,
  pg1_partnerOccupation varchar(25) DEFAULT NULL,
  pg1_baObs INT2 DEFAULT NULL,
  pg1_baFP INT2 DEFAULT NULL,
  pg1_baMidwife INT2 DEFAULT NULL,
  c_ba varchar(25) DEFAULT NULL,
  pg1_famPhys varchar(100) DEFAULT NULL,
  pg1_ncPed INT2 DEFAULT NULL,
  pg1_ncFP INT2 DEFAULT NULL,
  pg1_ncMidwife INT2 DEFAULT NULL,
  c_nc varchar(25) DEFAULT NULL,
  pg1_ethnicBg varchar(100) DEFAULT NULL,
  pg1_vbac INT2 DEFAULT NULL,
  pg1_repeatCS INT2 DEFAULT NULL,
  c_allergies text,
  c_meds text,
  pg1_menLMP varchar(10) DEFAULT NULL,
  pg1_menCycle varchar(7) DEFAULT NULL,
  pg1_menReg INT2 DEFAULT NULL,
  pg1_menEDB varchar(10) DEFAULT NULL,
  pg1_iud INT2 DEFAULT NULL,
  pg1_hormone INT2 DEFAULT NULL,
  pg1_hormoneType varchar(25) DEFAULT NULL,
  pg1_otherAR1 INT2 DEFAULT NULL,
  pg1_otherAR1Name varchar(25) DEFAULT NULL,
  pg1_lastUsed varchar(10) DEFAULT NULL,
  c_finalEDB DATE DEFAULT NULL,
  c_gravida varchar(5) DEFAULT NULL,
  c_term varchar(5) DEFAULT NULL,
  c_prem varchar(5) DEFAULT NULL,
  pg1_ectopic INT2 DEFAULT NULL,
  pg1_ectopicBox char(2) DEFAULT NULL,
  pg1_termination INT2 DEFAULT NULL,
  pg1_terminationBox char(2) DEFAULT NULL,
  pg1_spontaneous INT2 DEFAULT NULL,
  pg1_spontaneousBox char(2) DEFAULT NULL,
  pg1_stillborn INT2 DEFAULT NULL,
  pg1_stillbornBox char(2) DEFAULT NULL,
  c_living varchar(10) DEFAULT NULL,
  pg1_multi varchar(10) DEFAULT NULL,
  pg1_year1 varchar(10) DEFAULT NULL,
  pg1_sex1 char(1) DEFAULT NULL,
  pg1_oh_gest1 varchar(5) DEFAULT NULL,
  pg1_weight1 varchar(6) DEFAULT NULL,
  pg1_length1 varchar(6) DEFAULT NULL,
  pg1_place1 varchar(20) DEFAULT NULL,
  pg1_svb1 INT2 DEFAULT NULL,
  pg1_cs1 INT2 DEFAULT NULL,
  pg1_ass1 INT2 DEFAULT NULL,
  pg1_oh_comments1 varchar(80) DEFAULT NULL,
  pg1_year2 varchar(10) DEFAULT NULL,
  pg1_sex2 char(1) DEFAULT NULL,
  pg1_oh_gest2 varchar(5) DEFAULT NULL,
  pg1_weight2 varchar(6) DEFAULT NULL,
  pg1_length2 varchar(6) DEFAULT NULL,
  pg1_place2 varchar(20) DEFAULT NULL,
  pg1_svb2 INT2 DEFAULT NULL,
  pg1_cs2 INT2 DEFAULT NULL,
  pg1_ass2 INT2 DEFAULT NULL,
  pg1_oh_comments2 varchar(80) DEFAULT NULL,
  pg1_year3 varchar(10) DEFAULT NULL,
  pg1_sex3 char(1) DEFAULT NULL,
  pg1_oh_gest3 varchar(5) DEFAULT NULL,
  pg1_weight3 varchar(6) DEFAULT NULL,
  pg1_length3 varchar(6) DEFAULT NULL,
  pg1_place3 varchar(20) DEFAULT NULL,
  pg1_svb3 INT2 DEFAULT NULL,
  pg1_cs3 INT2 DEFAULT NULL,
  pg1_ass3 INT2 DEFAULT NULL,
  pg1_oh_comments3 varchar(80) DEFAULT NULL,
  pg1_year4 varchar(10) DEFAULT NULL,
  pg1_sex4 char(1) DEFAULT NULL,
  pg1_oh_gest4 varchar(5) DEFAULT NULL,
  pg1_weight4 varchar(6) DEFAULT NULL,
  pg1_length4 varchar(6) DEFAULT NULL,
  pg1_place4 varchar(20) DEFAULT NULL,
  pg1_svb4 INT2 DEFAULT NULL,
  pg1_cs4 INT2 DEFAULT NULL,
  pg1_ass4 INT2 DEFAULT NULL,
  pg1_oh_comments4 varchar(80) DEFAULT NULL,
  pg1_year5 varchar(10) DEFAULT NULL,
  pg1_sex5 char(1) DEFAULT NULL,
  pg1_oh_gest5 varchar(5) DEFAULT NULL,
  pg1_weight5 varchar(6) DEFAULT NULL,
  pg1_length5 varchar(6) DEFAULT NULL,
  pg1_place5 varchar(20) DEFAULT NULL,
  pg1_svb5 INT2 DEFAULT NULL,
  pg1_cs5 INT2 DEFAULT NULL,
  pg1_ass5 INT2 DEFAULT NULL,
  pg1_oh_comments5 varchar(80) DEFAULT NULL,
  pg1_year6 varchar(10) DEFAULT NULL,
  pg1_sex6 char(1) DEFAULT NULL,
  pg1_oh_gest6 varchar(5) DEFAULT NULL,
  pg1_weight6 varchar(6) DEFAULT NULL,
  pg1_length6 varchar(6) DEFAULT NULL,
  pg1_place6 varchar(20) DEFAULT NULL,
  pg1_svb6 INT2 DEFAULT NULL,
  pg1_cs6 INT2 DEFAULT NULL,
  pg1_ass6 INT2 DEFAULT NULL,
  pg1_oh_comments6 varchar(80) DEFAULT NULL,
  pg1_cp1 INT2 DEFAULT NULL,
  pg1_cp2 INT2 DEFAULT NULL,
  pg1_cp3 INT2 DEFAULT NULL,
  pg1_box3 char(3) DEFAULT NULL,
  pg1_cp4 INT2 DEFAULT NULL,
  pg1_cp5 INT2 DEFAULT NULL,
  pg1_box5 char(3) DEFAULT NULL,
  pg1_cp6 INT2 DEFAULT NULL,
  pg1_cp7 INT2 DEFAULT NULL,
  pg1_cp8 INT2 DEFAULT NULL,
  pg1_naFolic INT2 DEFAULT NULL,
  pg1_naMilk INT2 DEFAULT NULL,
  pg1_naDietBal INT2 DEFAULT NULL,
  pg1_naDietRes INT2 DEFAULT NULL,
  pg1_naRef INT2 DEFAULT NULL,
  pg1_yes9 INT2 DEFAULT NULL,
  pg1_no9 INT2 DEFAULT NULL,
  pg1_yes10 INT2 DEFAULT NULL,
  pg1_no10 INT2 DEFAULT NULL,
  pg1_yes11 INT2 DEFAULT NULL,
  pg1_no11 INT2 DEFAULT NULL,
  pg1_yes12 INT2 DEFAULT NULL,
  pg1_no12 INT2 DEFAULT NULL,
  pg1_yes13 INT2 DEFAULT NULL,
  pg1_no13 INT2 DEFAULT NULL,
  pg1_yes14 INT2 DEFAULT NULL,
  pg1_no14 INT2 DEFAULT NULL,
  pg1_yes15 INT2 DEFAULT NULL,
  pg1_no15 INT2 DEFAULT NULL,
  pg1_yes16 INT2 DEFAULT NULL,
  pg1_no16 INT2 DEFAULT NULL,
  pg1_yes17 INT2 DEFAULT NULL,
  pg1_no17 INT2 DEFAULT NULL,
  pg1_yes18 INT2 DEFAULT NULL,
  pg1_no18 INT2 DEFAULT NULL,
  pg1_yes19 INT2 DEFAULT NULL,
  pg1_no19 INT2 DEFAULT NULL,
  pg1_yes20 INT2 DEFAULT NULL,
  pg1_no20 INT2 DEFAULT NULL,
  pg1_yes21 INT2 DEFAULT NULL,
  pg1_no21 INT2 DEFAULT NULL,
  pg1_yes22 INT2 DEFAULT NULL,
  pg1_no22 INT2 DEFAULT NULL,
  pg1_yes23 INT2 DEFAULT NULL,
  pg1_no23 INT2 DEFAULT NULL,
  pg1_yes24 INT2 DEFAULT NULL,
  pg1_no24 INT2 DEFAULT NULL,
  pg1_yes25 INT2 DEFAULT NULL,
  pg1_no25 INT2 DEFAULT NULL,
  pg1_box25 varchar(25) DEFAULT NULL,
  pg1_yes26 INT2 DEFAULT NULL,
  pg1_no26 INT2 DEFAULT NULL,
  pg1_yes27 INT2 DEFAULT NULL,
  pg1_no27 INT2 DEFAULT NULL,
  pg1_yes28 INT2 DEFAULT NULL,
  pg1_no28 INT2 DEFAULT NULL,
  pg1_yes29 INT2 DEFAULT NULL,
  pg1_no29 INT2 DEFAULT NULL,
  pg1_yes30 INT2 DEFAULT NULL,
  pg1_no30 INT2 DEFAULT NULL,
  pg1_yes31 INT2 DEFAULT NULL,
  pg1_no31 INT2 DEFAULT NULL,
  pg1_yes32 INT2 DEFAULT NULL,
  pg1_no32 INT2 DEFAULT NULL,
  pg1_yes33 INT2 DEFAULT NULL,
  pg1_no33 INT2 DEFAULT NULL,
  pg1_yes34 INT2 DEFAULT NULL,
  pg1_no34 INT2 DEFAULT NULL,
  pg1_yes35 INT2 DEFAULT NULL,
  pg1_no35 INT2 DEFAULT NULL,
  pg1_yes36 INT2 DEFAULT NULL,
  pg1_no36 INT2 DEFAULT NULL,
  pg1_yes37off INT2 DEFAULT NULL,
  pg1_no37off INT2 DEFAULT NULL,
  pg1_yes37acc INT2 DEFAULT NULL,
  pg1_no37acc INT2 DEFAULT NULL,
  pg1_idt38 INT2 DEFAULT NULL,
  pg1_idt39 INT2 DEFAULT NULL,
  pg1_idt40 INT2 DEFAULT NULL,
  pg1_idt41 INT2 DEFAULT NULL,
  pg1_idt42 INT2 DEFAULT NULL,
  pg1_box42 varchar(20) DEFAULT NULL,
  pg1_pdt43 INT2 DEFAULT NULL,
  pg1_pdt44 INT2 DEFAULT NULL,
  pg1_pdt45 INT2 DEFAULT NULL,
  pg1_pdt46 INT2 DEFAULT NULL,
  pg1_pdt47 INT2 DEFAULT NULL,
  pg1_pdt48 INT2 DEFAULT NULL,
  c_riskFactors text,
  pg1_ht varchar(6) DEFAULT NULL,
  pg1_wt varchar(6) DEFAULT NULL,
  c_ppWt varchar(6) DEFAULT NULL,
  pg1_BP varchar(10) DEFAULT NULL,
  pg1_head INT2 DEFAULT NULL,
  pg1_thyroid INT2 DEFAULT NULL,
  pg1_chest INT2 DEFAULT NULL,
  pg1_breasts INT2 DEFAULT NULL,
  pg1_cardio INT2 DEFAULT NULL,
  pg1_abdomen INT2 DEFAULT NULL,
  pg1_vari INT2 DEFAULT NULL,
  pg1_neuro INT2 DEFAULT NULL,
  pg1_pelvic INT2 DEFAULT NULL,
  pg1_extGen INT2 DEFAULT NULL,
  pg1_cervix INT2 DEFAULT NULL,
  pg1_uterus INT2 DEFAULT NULL,
  pg1_uterusBox char(3) DEFAULT NULL,
  pg1_adnexa INT2 DEFAULT NULL,
  pg1_commentsAR1 text,
  ar2_etss varchar(10) DEFAULT NULL,
  ar2_hb varchar(10) DEFAULT NULL,
  ar2_mcv varchar(10) DEFAULT NULL,
  ar2_mss varchar(10) DEFAULT NULL,
  ar2_rubella varchar(5) DEFAULT NULL,
  ar2_hbs varchar(6) DEFAULT NULL,
  ar2_vdrl varchar(6) DEFAULT NULL,
  ar2_bloodGroup varchar(6) DEFAULT NULL,
  ar2_rh varchar(6) DEFAULT NULL,
  ar2_antibodies varchar(6) DEFAULT NULL,
  ar2_rhIG varchar(6) DEFAULT NULL,
  pg2_date1 DATE DEFAULT NULL,
  pg2_gest1 varchar(6) DEFAULT NULL,
  pg2_ht1 varchar(6) DEFAULT NULL,
  pg2_wt1 varchar(6) DEFAULT NULL,
  pg2_presn1 varchar(6) DEFAULT NULL,
  pg2_FHR1 varchar(6) DEFAULT NULL,
  pg2_urinePr1 char(3) DEFAULT NULL,
  pg2_urineGl1 char(3) DEFAULT NULL,
  pg2_BP1 varchar(8) DEFAULT NULL,
  pg2_comments1 varchar(255) DEFAULT NULL,
  pg2_cig1 char(3) DEFAULT NULL,
  pg2_date2 DATE DEFAULT NULL,
  pg2_gest2 varchar(6) DEFAULT NULL,
  pg2_ht2 varchar(6) DEFAULT NULL,
  pg2_wt2 varchar(6) DEFAULT NULL,
  pg2_presn2 varchar(6) DEFAULT NULL,
  pg2_FHR2 varchar(6) DEFAULT NULL,
  pg2_urinePr2 char(3) DEFAULT NULL,
  pg2_urineGl2 char(3) DEFAULT NULL,
  pg2_BP2 varchar(8) DEFAULT NULL,
  pg2_comments2 varchar(255) DEFAULT NULL,
  pg2_cig2 char(3) DEFAULT NULL,
  pg2_date3 DATE DEFAULT NULL,
  pg2_gest3 varchar(6) DEFAULT NULL,
  pg2_ht3 varchar(6) DEFAULT NULL,
  pg2_wt3 varchar(6) DEFAULT NULL,
  pg2_presn3 varchar(6) DEFAULT NULL,
  pg2_FHR3 varchar(6) DEFAULT NULL,
  pg2_urinePr3 char(3) DEFAULT NULL,
  pg2_urineGl3 char(3) DEFAULT NULL,
  pg2_BP3 varchar(8) DEFAULT NULL,
  pg2_comments3 varchar(255) DEFAULT NULL,
  pg2_cig3 char(3) DEFAULT NULL,
  pg2_date4 DATE DEFAULT NULL,
  pg2_gest4 varchar(6) DEFAULT NULL,
  pg2_ht4 varchar(6) DEFAULT NULL,
  pg2_wt4 varchar(6) DEFAULT NULL,
  pg2_presn4 varchar(6) DEFAULT NULL,
  pg2_FHR4 varchar(6) DEFAULT NULL,
  pg2_urinePr4 char(3) DEFAULT NULL,
  pg2_urineGl4 char(3) DEFAULT NULL,
  pg2_BP4 varchar(8) DEFAULT NULL,
  pg2_comments4 varchar(255) DEFAULT NULL,
  pg2_cig4 char(3) DEFAULT NULL,
  pg2_date5 DATE DEFAULT NULL,
  pg2_gest5 varchar(6) DEFAULT NULL,
  pg2_ht5 varchar(6) DEFAULT NULL,
  pg2_wt5 varchar(6) DEFAULT NULL,
  pg2_presn5 varchar(6) DEFAULT NULL,
  pg2_FHR5 varchar(6) DEFAULT NULL,
  pg2_urinePr5 char(3) DEFAULT NULL,
  pg2_urineGl5 char(3) DEFAULT NULL,
  pg2_BP5 varchar(8) DEFAULT NULL,
  pg2_comments5 varchar(255) DEFAULT NULL,
  pg2_cig5 char(3) DEFAULT NULL,
  pg2_date6 DATE DEFAULT NULL,
  pg2_gest6 varchar(6) DEFAULT NULL,
  pg2_ht6 varchar(6) DEFAULT NULL,
  pg2_wt6 varchar(6) DEFAULT NULL,
  pg2_presn6 varchar(6) DEFAULT NULL,
  pg2_FHR6 varchar(6) DEFAULT NULL,
  pg2_urinePr6 char(3) DEFAULT NULL,
  pg2_urineGl6 char(3) DEFAULT NULL,
  pg2_BP6 varchar(8) DEFAULT NULL,
  pg2_comments6 varchar(255) DEFAULT NULL,
  pg2_cig6 char(3) DEFAULT NULL,
  pg2_date7 DATE DEFAULT NULL,
  pg2_gest7 varchar(6) DEFAULT NULL,
  pg2_ht7 varchar(6) DEFAULT NULL,
  pg2_wt7 varchar(6) DEFAULT NULL,
  pg2_presn7 varchar(6) DEFAULT NULL,
  pg2_FHR7 varchar(6) DEFAULT NULL,
  pg2_urinePr7 char(3) DEFAULT NULL,
  pg2_urineGl7 char(3) DEFAULT NULL,
  pg2_BP7 varchar(8) DEFAULT NULL,
  pg2_comments7 varchar(255) DEFAULT NULL,
  pg2_cig7 char(3) DEFAULT NULL,
  pg2_date8 DATE DEFAULT NULL,
  pg2_gest8 varchar(6) DEFAULT NULL,
  pg2_ht8 varchar(6) DEFAULT NULL,
  pg2_wt8 varchar(6) DEFAULT NULL,
  pg2_presn8 varchar(6) DEFAULT NULL,
  pg2_FHR8 varchar(6) DEFAULT NULL,
  pg2_urinePr8 char(3) DEFAULT NULL,
  pg2_urineGl8 char(3) DEFAULT NULL,
  pg2_BP8 varchar(8) DEFAULT NULL,
  pg2_comments8 varchar(255) DEFAULT NULL,
  pg2_cig8 char(3) DEFAULT NULL,
  pg2_date9 DATE DEFAULT NULL,
  pg2_gest9 varchar(6) DEFAULT NULL,
  pg2_ht9 varchar(6) DEFAULT NULL,
  pg2_wt9 varchar(6) DEFAULT NULL,
  pg2_presn9 varchar(6) DEFAULT NULL,
  pg2_FHR9 varchar(6) DEFAULT NULL,
  pg2_urinePr9 char(3) DEFAULT NULL,
  pg2_urineGl9 char(3) DEFAULT NULL,
  pg2_BP9 varchar(8) DEFAULT NULL,
  pg2_comments9 varchar(255) DEFAULT NULL,
  pg2_cig9 char(3) DEFAULT NULL,
  pg2_date10 DATE DEFAULT NULL,
  pg2_gest10 varchar(6) DEFAULT NULL,
  pg2_ht10 varchar(6) DEFAULT NULL,
  pg2_wt10 varchar(6) DEFAULT NULL,
  pg2_presn10 varchar(6) DEFAULT NULL,
  pg2_FHR10 varchar(6) DEFAULT NULL,
  pg2_urinePr10 char(3) DEFAULT NULL,
  pg2_urineGl10 char(3) DEFAULT NULL,
  pg2_BP10 varchar(8) DEFAULT NULL,
  pg2_comments10 varchar(255) DEFAULT NULL,
  pg2_cig10 char(3) DEFAULT NULL,
  pg2_date11 DATE DEFAULT NULL,
  pg2_gest11 varchar(6) DEFAULT NULL,
  pg2_ht11 varchar(6) DEFAULT NULL,
  pg2_wt11 varchar(6) DEFAULT NULL,
  pg2_presn11 varchar(6) DEFAULT NULL,
  pg2_FHR11 varchar(6) DEFAULT NULL,
  pg2_urinePr11 char(3) DEFAULT NULL,
  pg2_urineGl11 char(3) DEFAULT NULL,
  pg2_BP11 varchar(8) DEFAULT NULL,
  pg2_comments11 varchar(255) DEFAULT NULL,
  pg2_cig11 char(3) DEFAULT NULL,
  pg2_date12 DATE DEFAULT NULL,
  pg2_gest12 varchar(6) DEFAULT NULL,
  pg2_ht12 varchar(6) DEFAULT NULL,
  pg2_wt12 varchar(6) DEFAULT NULL,
  pg2_presn12 varchar(6) DEFAULT NULL,
  pg2_FHR12 varchar(6) DEFAULT NULL,
  pg2_urinePr12 char(3) DEFAULT NULL,
  pg2_urineGl12 char(3) DEFAULT NULL,
  pg2_BP12 varchar(8) DEFAULT NULL,
  pg2_comments12 varchar(255) DEFAULT NULL,
  pg2_cig12 char(3) DEFAULT NULL,
  pg2_date13 DATE DEFAULT NULL,
  pg2_gest13 varchar(6) DEFAULT NULL,
  pg2_ht13 varchar(6) DEFAULT NULL,
  pg2_wt13 varchar(6) DEFAULT NULL,
  pg2_presn13 varchar(6) DEFAULT NULL,
  pg2_FHR13 varchar(6) DEFAULT NULL,
  pg2_urinePr13 char(3) DEFAULT NULL,
  pg2_urineGl13 char(3) DEFAULT NULL,
  pg2_BP13 varchar(8) DEFAULT NULL,
  pg2_comments13 varchar(255) DEFAULT NULL,
  pg2_cig13 char(3) DEFAULT NULL,
  pg2_date14 DATE DEFAULT NULL,
  pg2_gest14 varchar(6) DEFAULT NULL,
  pg2_ht14 varchar(6) DEFAULT NULL,
  pg2_wt14 varchar(6) DEFAULT NULL,
  pg2_presn14 varchar(6) DEFAULT NULL,
  pg2_FHR14 varchar(6) DEFAULT NULL,
  pg2_urinePr14 char(3) DEFAULT NULL,
  pg2_urineGl14 char(3) DEFAULT NULL,
  pg2_BP14 varchar(8) DEFAULT NULL,
  pg2_comments14 varchar(255) DEFAULT NULL,
  pg2_cig14 char(3) DEFAULT NULL,
  pg2_date15 DATE DEFAULT NULL,
  pg2_gest15 varchar(6) DEFAULT NULL,
  pg2_ht15 varchar(6) DEFAULT NULL,
  pg2_wt15 varchar(6) DEFAULT NULL,
  pg2_presn15 varchar(6) DEFAULT NULL,
  pg2_FHR15 varchar(6) DEFAULT NULL,
  pg2_urinePr15 char(3) DEFAULT NULL,
  pg2_urineGl15 char(3) DEFAULT NULL,
  pg2_BP15 varchar(8) DEFAULT NULL,
  pg2_comments15 varchar(255) DEFAULT NULL,
  pg2_cig15 char(3) DEFAULT NULL,
  pg2_date16 DATE DEFAULT NULL,
  pg2_gest16 varchar(6) DEFAULT NULL,
  pg2_ht16 varchar(6) DEFAULT NULL,
  pg2_wt16 varchar(6) DEFAULT NULL,
  pg2_presn16 varchar(6) DEFAULT NULL,
  pg2_FHR16 varchar(6) DEFAULT NULL,
  pg2_urinePr16 char(3) DEFAULT NULL,
  pg2_urineGl16 char(3) DEFAULT NULL,
  pg2_BP16 varchar(8) DEFAULT NULL,
  pg2_comments16 varchar(255) DEFAULT NULL,
  pg2_cig16 char(3) DEFAULT NULL,
  pg2_date17 DATE DEFAULT NULL,
  pg2_gest17 varchar(6) DEFAULT NULL,
  pg2_ht17 varchar(6) DEFAULT NULL,
  pg2_wt17 varchar(6) DEFAULT NULL,
  pg2_presn17 varchar(6) DEFAULT NULL,
  pg2_FHR17 varchar(6) DEFAULT NULL,
  pg2_urinePr17 char(3) DEFAULT NULL,
  pg2_urineGl17 char(3) DEFAULT NULL,
  pg2_BP17 varchar(8) DEFAULT NULL,
  pg2_comments17 varchar(255) DEFAULT NULL,
  pg2_cig17 char(3) DEFAULT NULL,
  pg3_date18 DATE DEFAULT NULL,
  pg3_gest18 varchar(6) DEFAULT NULL,
  pg3_ht18 varchar(6) DEFAULT NULL,
  pg3_wt18 varchar(6) DEFAULT NULL,
  pg3_presn18 varchar(6) DEFAULT NULL,
  pg3_FHR18 varchar(6) DEFAULT NULL,
  pg3_urinePr18 char(3) DEFAULT NULL,
  pg3_urineGl18 char(3) DEFAULT NULL,
  pg3_BP18 varchar(8) DEFAULT NULL,
  pg3_comments18 varchar(255) DEFAULT NULL,
  pg3_cig18 char(3) DEFAULT NULL,
  pg3_date19 DATE DEFAULT NULL,
  pg3_gest19 varchar(6) DEFAULT NULL,
  pg3_ht19 varchar(6) DEFAULT NULL,
  pg3_wt19 varchar(6) DEFAULT NULL,
  pg3_presn19 varchar(6) DEFAULT NULL,
  pg3_FHR19 varchar(6) DEFAULT NULL,
  pg3_urinePr19 char(3) DEFAULT NULL,
  pg3_urineGl19 char(3) DEFAULT NULL,
  pg3_BP19 varchar(8) DEFAULT NULL,
  pg3_comments19 varchar(255) DEFAULT NULL,
  pg3_cig19 char(3) DEFAULT NULL,
  pg3_date20 DATE DEFAULT NULL,
  pg3_gest20 varchar(6) DEFAULT NULL,
  pg3_ht20 varchar(6) DEFAULT NULL,
  pg3_wt20 varchar(6) DEFAULT NULL,
  pg3_presn20 varchar(6) DEFAULT NULL,
  pg3_FHR20 varchar(6) DEFAULT NULL,
  pg3_urinePr20 char(3) DEFAULT NULL,
  pg3_urineGl20 char(3) DEFAULT NULL,
  pg3_BP20 varchar(8) DEFAULT NULL,
  pg3_comments20 varchar(255) DEFAULT NULL,
  pg3_cig20 char(3) DEFAULT NULL,
  pg3_date21 DATE DEFAULT NULL,
  pg3_gest21 varchar(6) DEFAULT NULL,
  pg3_ht21 varchar(6) DEFAULT NULL,
  pg3_wt21 varchar(6) DEFAULT NULL,
  pg3_presn21 varchar(6) DEFAULT NULL,
  pg3_FHR21 varchar(6) DEFAULT NULL,
  pg3_urinePr21 char(3) DEFAULT NULL,
  pg3_urineGl21 char(3) DEFAULT NULL,
  pg3_BP21 varchar(8) DEFAULT NULL,
  pg3_comments21 varchar(255) DEFAULT NULL,
  pg3_cig21 char(3) DEFAULT NULL,
  pg3_date22 DATE DEFAULT NULL,
  pg3_gest22 varchar(6) DEFAULT NULL,
  pg3_ht22 varchar(6) DEFAULT NULL,
  pg3_wt22 varchar(6) DEFAULT NULL,
  pg3_presn22 varchar(6) DEFAULT NULL,
  pg3_FHR22 varchar(6) DEFAULT NULL,
  pg3_urinePr22 char(3) DEFAULT NULL,
  pg3_urineGl22 char(3) DEFAULT NULL,
  pg3_BP22 varchar(8) DEFAULT NULL,
  pg3_comments22 varchar(255) DEFAULT NULL,
  pg3_cig22 char(3) DEFAULT NULL,
  pg3_date23 DATE DEFAULT NULL,
  pg3_gest23 varchar(6) DEFAULT NULL,
  pg3_ht23 varchar(6) DEFAULT NULL,
  pg3_wt23 varchar(6) DEFAULT NULL,
  pg3_presn23 varchar(6) DEFAULT NULL,
  pg3_FHR23 varchar(6) DEFAULT NULL,
  pg3_urinePr23 char(3) DEFAULT NULL,
  pg3_urineGl23 char(3) DEFAULT NULL,
  pg3_BP23 varchar(8) DEFAULT NULL,
  pg3_comments23 varchar(255) DEFAULT NULL,
  pg3_cig23 char(3) DEFAULT NULL,
  pg3_date24 DATE DEFAULT NULL,
  pg3_gest24 varchar(6) DEFAULT NULL,
  pg3_ht24 varchar(6) DEFAULT NULL,
  pg3_wt24 varchar(6) DEFAULT NULL,
  pg3_presn24 varchar(6) DEFAULT NULL,
  pg3_FHR24 varchar(6) DEFAULT NULL,
  pg3_urinePr24 char(3) DEFAULT NULL,
  pg3_urineGl24 char(3) DEFAULT NULL,
  pg3_BP24 varchar(8) DEFAULT NULL,
  pg3_comments24 varchar(255) DEFAULT NULL,
  pg3_cig24 char(3) DEFAULT NULL,
  pg3_date25 DATE DEFAULT NULL,
  pg3_gest25 varchar(6) DEFAULT NULL,
  pg3_ht25 varchar(6) DEFAULT NULL,
  pg3_wt25 varchar(6) DEFAULT NULL,
  pg3_presn25 varchar(6) DEFAULT NULL,
  pg3_FHR25 varchar(6) DEFAULT NULL,
  pg3_urinePr25 char(3) DEFAULT NULL,
  pg3_urineGl25 char(3) DEFAULT NULL,
  pg3_BP25 varchar(8) DEFAULT NULL,
  pg3_comments25 varchar(255) DEFAULT NULL,
  pg3_cig25 char(3) DEFAULT NULL,
  pg3_date26 DATE DEFAULT NULL,
  pg3_gest26 varchar(6) DEFAULT NULL,
  pg3_ht26 varchar(6) DEFAULT NULL,
  pg3_wt26 varchar(6) DEFAULT NULL,
  pg3_presn26 varchar(6) DEFAULT NULL,
  pg3_FHR26 varchar(6) DEFAULT NULL,
  pg3_urinePr26 char(3) DEFAULT NULL,
  pg3_urineGl26 char(3) DEFAULT NULL,
  pg3_BP26 varchar(8) DEFAULT NULL,
  pg3_comments26 varchar(255) DEFAULT NULL,
  pg3_cig26 char(3) DEFAULT NULL,
  pg3_date27 DATE DEFAULT NULL,
  pg3_gest27 varchar(6) DEFAULT NULL,
  pg3_ht27 varchar(6) DEFAULT NULL,
  pg3_wt27 varchar(6) DEFAULT NULL,
  pg3_presn27 varchar(6) DEFAULT NULL,
  pg3_FHR27 varchar(6) DEFAULT NULL,
  pg3_urinePr27 char(3) DEFAULT NULL,
  pg3_urineGl27 char(3) DEFAULT NULL,
  pg3_BP27 varchar(8) DEFAULT NULL,
  pg3_comments27 varchar(255) DEFAULT NULL,
  pg3_cig27 char(3) DEFAULT NULL,
  pg3_date28 DATE DEFAULT NULL,
  pg3_gest28 varchar(6) DEFAULT NULL,
  pg3_ht28 varchar(6) DEFAULT NULL,
  pg3_wt28 varchar(6) DEFAULT NULL,
  pg3_presn28 varchar(6) DEFAULT NULL,
  pg3_FHR28 varchar(6) DEFAULT NULL,
  pg3_urinePr28 char(3) DEFAULT NULL,
  pg3_urineGl28 char(3) DEFAULT NULL,
  pg3_BP28 varchar(8) DEFAULT NULL,
  pg3_comments28 varchar(255) DEFAULT NULL,
  pg3_cig28 char(3) DEFAULT NULL,
  pg3_date29 DATE DEFAULT NULL,
  pg3_gest29 varchar(6) DEFAULT NULL,
  pg3_ht29 varchar(6) DEFAULT NULL,
  pg3_wt29 varchar(6) DEFAULT NULL,
  pg3_presn29 varchar(6) DEFAULT NULL,
  pg3_FHR29 varchar(6) DEFAULT NULL,
  pg3_urinePr29 char(3) DEFAULT NULL,
  pg3_urineGl29 char(3) DEFAULT NULL,
  pg3_BP29 varchar(8) DEFAULT NULL,
  pg3_comments29 varchar(255) DEFAULT NULL,
  pg3_cig29 char(3) DEFAULT NULL,
  pg3_date30 DATE DEFAULT NULL,
  pg3_gest30 varchar(6) DEFAULT NULL,
  pg3_ht30 varchar(6) DEFAULT NULL,
  pg3_wt30 varchar(6) DEFAULT NULL,
  pg3_presn30 varchar(6) DEFAULT NULL,
  pg3_FHR30 varchar(6) DEFAULT NULL,
  pg3_urinePr30 char(3) DEFAULT NULL,
  pg3_urineGl30 char(3) DEFAULT NULL,
  pg3_BP30 varchar(8) DEFAULT NULL,
  pg3_comments30 varchar(255) DEFAULT NULL,
  pg3_cig30 char(3) DEFAULT NULL,
  pg3_date31 DATE DEFAULT NULL,
  pg3_gest31 varchar(6) DEFAULT NULL,
  pg3_ht31 varchar(6) DEFAULT NULL,
  pg3_wt31 varchar(6) DEFAULT NULL,
  pg3_presn31 varchar(6) DEFAULT NULL,
  pg3_FHR31 varchar(6) DEFAULT NULL,
  pg3_urinePr31 char(3) DEFAULT NULL,
  pg3_urineGl31 char(3) DEFAULT NULL,
  pg3_BP31 varchar(8) DEFAULT NULL,
  pg3_comments31 varchar(255) DEFAULT NULL,
  pg3_cig31 char(3) DEFAULT NULL,
  pg3_date32 DATE DEFAULT NULL,
  pg3_gest32 varchar(6) DEFAULT NULL,
  pg3_ht32 varchar(6) DEFAULT NULL,
  pg3_wt32 varchar(6) DEFAULT NULL,
  pg3_presn32 varchar(6) DEFAULT NULL,
  pg3_FHR32 varchar(6) DEFAULT NULL,
  pg3_urinePr32 char(3) DEFAULT NULL,
  pg3_urineGl32 char(3) DEFAULT NULL,
  pg3_BP32 varchar(8) DEFAULT NULL,
  pg3_comments32 varchar(255) DEFAULT NULL,
  pg3_cig32 char(3) DEFAULT NULL,
  pg3_date33 DATE DEFAULT NULL,
  pg3_gest33 varchar(6) DEFAULT NULL,
  pg3_ht33 varchar(6) DEFAULT NULL,
  pg3_wt33 varchar(6) DEFAULT NULL,
  pg3_presn33 varchar(6) DEFAULT NULL,
  pg3_FHR33 varchar(6) DEFAULT NULL,
  pg3_urinePr33 char(3) DEFAULT NULL,
  pg3_urineGl33 char(3) DEFAULT NULL,
  pg3_BP33 varchar(8) DEFAULT NULL,
  pg3_comments33 varchar(255) DEFAULT NULL,
  pg3_cig33 char(3) DEFAULT NULL,
  pg3_date34 DATE DEFAULT NULL,
  pg3_gest34 varchar(6) DEFAULT NULL,
  pg3_ht34 varchar(6) DEFAULT NULL,
  pg3_wt34 varchar(6) DEFAULT NULL,
  pg3_presn34 varchar(6) DEFAULT NULL,
  pg3_FHR34 varchar(6) DEFAULT NULL,
  pg3_urinePr34 char(3) DEFAULT NULL,
  pg3_urineGl34 char(3) DEFAULT NULL,
  pg3_BP34 varchar(8) DEFAULT NULL,
  pg3_comments34 varchar(255) DEFAULT NULL,
  pg3_cig34 char(3) DEFAULT NULL,
  ar2_obstetrician INT2 DEFAULT NULL,
  ar2_pediatrician INT2 DEFAULT NULL,
  ar2_anesthesiologist INT2 DEFAULT NULL,
  ar2_socialWorker INT2 DEFAULT NULL,
  ar2_dietician INT2 DEFAULT NULL,
  ar2_otherAR2 INT2 DEFAULT NULL,
  ar2_otherBox varchar(35) DEFAULT NULL,
  ar2_drugUse INT2 DEFAULT NULL,
  ar2_smoking INT2 DEFAULT NULL,
  ar2_alcohol INT2 DEFAULT NULL,
  ar2_exercise INT2 DEFAULT NULL,
  ar2_workPlan INT2 DEFAULT NULL,
  ar2_intercourse INT2 DEFAULT NULL,
  ar2_dental INT2 DEFAULT NULL,
  ar2_travel INT2 DEFAULT NULL,
  ar2_prenatal INT2 DEFAULT NULL,
  ar2_breast INT2 DEFAULT NULL,
  ar2_birth INT2 DEFAULT NULL,
  ar2_preterm INT2 DEFAULT NULL,
  ar2_prom INT2 DEFAULT NULL,
  ar2_fetal INT2 DEFAULT NULL,
  ar2_admission INT2 DEFAULT NULL,
  ar2_labour INT2 DEFAULT NULL,
  ar2_pain INT2 DEFAULT NULL,
  ar2_depression INT2 DEFAULT NULL,
  ar2_circumcision INT2 DEFAULT NULL,
  ar2_car INT2 DEFAULT NULL,
  ar2_contraception INT2 DEFAULT NULL,
  ar2_onCall INT2 DEFAULT NULL,
  ar2_uDate1 varchar(10) DEFAULT NULL,
  ar2_uGA1 varchar(10) DEFAULT NULL,
  ar2_uResults1 varchar(25) DEFAULT NULL,
  ar2_uDate2 varchar(10) DEFAULT NULL,
  ar2_uGA2 varchar(10) DEFAULT NULL,
  ar2_uResults2 varchar(25) DEFAULT NULL,
  ar2_uDate3 varchar(10) DEFAULT NULL,
  ar2_uGA3 varchar(10) DEFAULT NULL,
  ar2_uResults3 varchar(25) DEFAULT NULL,
  ar2_uDate4 varchar(10) DEFAULT NULL,
  ar2_uGA4 varchar(10) DEFAULT NULL,
  ar2_uResults4 varchar(25) DEFAULT NULL,
  ar2_pap varchar(20) DEFAULT NULL,
  ar2_commentsAR2 text,
  ar2_chlamydia varchar(10) DEFAULT NULL,
  ar2_hiv varchar(10) DEFAULT NULL,
  ar2_vaginosis varchar(10) DEFAULT NULL,
  ar2_strep varchar(10) DEFAULT NULL,
  ar2_urineCulture varchar(10) DEFAULT NULL,
  ar2_sickleDex varchar(10) DEFAULT NULL,
  ar2_electro varchar(10) DEFAULT NULL,
  ar2_amnio varchar(10) DEFAULT NULL,
  ar2_glucose varchar(10) DEFAULT NULL,
  ar2_otherAR2Name varchar(20) DEFAULT NULL,
  ar2_otherResult varchar(10) DEFAULT NULL,
  ar2_psych varchar(25) DEFAULT NULL,
  pg1_signature varchar(50) DEFAULT NULL,
  pg2_signature varchar(50) DEFAULT NULL,
  pg3_signature varchar(50) DEFAULT NULL,
  pg1_formDate DATE DEFAULT NULL,
  pg2_formDate DATE DEFAULT NULL,
  pg3_formDate DATE DEFAULT NULL,
  PRIMARY KEY (ID)

);


--
-- Sequences for table formadf
--
CREATE SEQUENCE formadf_numeric_seq;

--
-- Table structure for table `formAdf`
--

CREATE TABLE formAdf (
  ID numeric(10) NOT NULL default nextval('formadf_numeric_seq'),
  demographic_no numeric(10) NOT NULL default '0',
  provider_no numeric(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(6) NOT NULL,
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
);


--
-- Sequences for table formadfv2
--

CREATE SEQUENCE formadfv2_numeric_seq;

CREATE TABLE formAdfV2(
  ID numeric(10) NOT NULL  default nextval('formadfv2_numeric_seq'),
  demographic_no numeric(10) NOT NULL default '0' ,
  provider_no numeric(10)  default NULL ,
  formCreated date  default NULL ,
  formEdited timestamp   ,
  c_patientname varchar(60),
  sendFacility varchar(80),
  poaSdm varchar(80),
  capTreatDecY smallint,
  capTreatDecN smallint,
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
);


--
-- Sequences for table FORMALPHA
--

CREATE SEQUENCE formalpha_numeric_seq;

--
-- Table structure for table 'formAlpha'
--
CREATE TABLE formAlpha (
  ID numeric(10) DEFAULT nextval('formalpha_numeric_seq'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  provider_no numeric(10) DEFAULT NULL,
  formCreated DATE DEFAULT NULL,
  formEdited timestamp(6) NOT NULL,
  pName varchar(60) DEFAULT NULL,
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
  provCounselling INT2 DEFAULT NULL,
  homecare INT2 DEFAULT NULL,
  assaultedWomen INT2 DEFAULT NULL,
  addAppts INT2 DEFAULT NULL,
  parentingClasses INT2 DEFAULT NULL,
  legalAdvice INT2 DEFAULT NULL,
  postpartumAppts INT2 DEFAULT NULL,
  addictPrograms INT2 DEFAULT NULL,
  cas INT2 DEFAULT NULL,
  babyVisits INT2 DEFAULT NULL,
  quitSmoking INT2 DEFAULT NULL,
  other1 INT2 DEFAULT NULL,
  other1Name varchar(30) DEFAULT NULL,
  publicHealth INT2 DEFAULT NULL,
  socialWorker INT2 DEFAULT NULL,
  other2 INT2 DEFAULT NULL,
  other2Name varchar(30) DEFAULT NULL,
  prenatalEdu INT2 DEFAULT NULL,
  psych INT2 DEFAULT NULL,
  other3 INT2 DEFAULT NULL,
  other3Name varchar(30) DEFAULT NULL,
  nutritionist INT2 DEFAULT NULL,
  therapist INT2 DEFAULT NULL,
  other4 INT2 DEFAULT NULL,
  other4Name varchar(30) DEFAULT NULL,
  resources INT2 DEFAULT NULL,
  comments text,
  PRIMARY KEY (ID)
);

--
-- Table structure for table 'formAnnual'
--



--
-- Sequences for table FORMANNUAL
--

CREATE SEQUENCE formannual_numeric_seq;

CREATE TABLE formAnnual (
  ID numeric(10) DEFAULT nextval('formannual_numeric_seq'),
  demographic_no numeric(10) DEFAULT NULL,
  provider_no numeric(10) DEFAULT NULL,
  formCreated DATE DEFAULT NULL,
  formEdited timestamp(6) NOT NULL,
  pName varchar(60) DEFAULT NULL,
  age char(3) DEFAULT NULL,
  formDate DATE DEFAULT NULL,
  currentConcerns text,
  currentConcernsNo INT2 DEFAULT NULL,
  currentConcernsYes INT2 DEFAULT NULL,
  headN INT2 DEFAULT NULL,
  headAbN INT2 DEFAULT NULL,
  head varchar(30) DEFAULT NULL,
  respN INT2 DEFAULT NULL,
  respAbN INT2 DEFAULT NULL,
  resp varchar(30) DEFAULT NULL,
  cardioN INT2 DEFAULT NULL,
  cardioAbN INT2 DEFAULT NULL,
  cardio varchar(30) DEFAULT NULL,
  giN INT2 DEFAULT NULL,
  giAbN INT2 DEFAULT NULL,
  gi varchar(30) DEFAULT NULL,
  guN INT2 DEFAULT NULL,
  guAbN INT2 DEFAULT NULL,
  gu varchar(30) DEFAULT NULL,
  noGtpalRevisions INT2 DEFAULT NULL,
  yesGtpalRevisions INT2 DEFAULT NULL,
  frontSheet INT2 DEFAULT NULL,
  lmp DATE DEFAULT NULL,
  menopause char(3) DEFAULT NULL,
  papSmearsN INT2 DEFAULT NULL,
  papSmearsAbN INT2 DEFAULT NULL,
  papSmears varchar(30) DEFAULT NULL,
  skinN INT2 DEFAULT NULL,
  skinAbN INT2 DEFAULT NULL,
  skin varchar(30) DEFAULT NULL,
  mskN INT2 DEFAULT NULL,
  mskAbN INT2 DEFAULT NULL,
  msk varchar(30) DEFAULT NULL,
  endocrinN INT2 DEFAULT NULL,
  endocrinAbN INT2 DEFAULT NULL,
  endocrin varchar(30) DEFAULT NULL,
  otherN INT2 DEFAULT NULL,
  otherAbN INT2 DEFAULT NULL,
  other varchar(255) DEFAULT NULL,
  drugs INT2 DEFAULT NULL,
  medSheet INT2 DEFAULT NULL,
  allergies INT2 DEFAULT NULL,
  frontSheet1 INT2 DEFAULT NULL,
  familyHistory INT2 DEFAULT NULL,
  frontSheet2 INT2 DEFAULT NULL,
  smokingNo INT2 DEFAULT NULL,
  smokingYes INT2 DEFAULT NULL,
  smoking varchar(30) DEFAULT NULL,
  sexualityNo INT2 DEFAULT NULL,
  sexualityYes INT2 DEFAULT NULL,
  sexuality varchar(30) DEFAULT NULL,
  alcoholNo INT2 DEFAULT NULL,
  alcoholYes INT2 DEFAULT NULL,
  alcohol varchar(30) DEFAULT NULL,
  occupationalNo INT2 DEFAULT NULL,
  occupationalYes INT2 DEFAULT NULL,
  occupational varchar(30) DEFAULT NULL,
  otcNo INT2 DEFAULT NULL,
  otcYes INT2 DEFAULT NULL,
  otc varchar(30) DEFAULT NULL,
  drivingNo INT2 DEFAULT NULL,
  drivingYes INT2 DEFAULT NULL,
  driving varchar(30) DEFAULT NULL,
  exerciseNo INT2 DEFAULT NULL,
  exerciseYes INT2 DEFAULT NULL,
  exercise varchar(30) DEFAULT NULL,
  travelNo INT2 DEFAULT NULL,
  travelYes INT2 DEFAULT NULL,
  travel varchar(30) DEFAULT NULL,
  nutritionNo INT2 DEFAULT NULL,
  nutritionYes INT2 DEFAULT NULL,
  nutrition varchar(30) DEFAULT NULL,
  otherNo INT2 DEFAULT NULL,
  otherYes INT2 DEFAULT NULL,
  otherLifestyle varchar(255) DEFAULT NULL,
  dentalNo INT2 DEFAULT NULL,
  dentalYes INT2 DEFAULT NULL,
  dental varchar(30) DEFAULT NULL,
  relationshipNo INT2 DEFAULT NULL,
  relationshipYes INT2 DEFAULT NULL,
  relationship varchar(150) DEFAULT NULL,
  mammogram INT2 DEFAULT NULL,
  rectal INT2 DEFAULT NULL,
  breast INT2 DEFAULT NULL,
  maleCardiac INT2 DEFAULT NULL,
  pap INT2 DEFAULT NULL,
  maleImmunization INT2 DEFAULT NULL,
  femaleImmunization INT2 DEFAULT NULL,
  maleOther1c INT2 DEFAULT NULL,
  maleOther1 varchar(30) DEFAULT NULL,
  precontraceptive INT2 DEFAULT NULL,
  maleOther2c INT2 DEFAULT NULL,
  maleOther2 varchar(30) DEFAULT NULL,
  femaleCardiac INT2 DEFAULT NULL,
  osteoporosis INT2 DEFAULT NULL,
  femaleOther1c INT2 DEFAULT NULL,
  femaleOther1 varchar(30) DEFAULT NULL,
  femaleOther2c INT2 DEFAULT NULL,
  femaleOther2 varchar(30) DEFAULT NULL,
  bprTop char(3) DEFAULT NULL,
  bprBottom char(3) DEFAULT NULL,
  pulse varchar(10) DEFAULT NULL,
  height varchar(4) DEFAULT NULL,
  weight varchar(4) DEFAULT NULL,
  bplTop char(3) DEFAULT NULL,
  bplBottom char(3) DEFAULT NULL,
  rhythm varchar(10) DEFAULT NULL,
  urine varchar(30) DEFAULT NULL,
  physicalSigns text,
  assessment text,
  plan text,
  signature varchar(60) DEFAULT NULL,
  PRIMARY KEY (ID)

);

--
-- Sequences for table FORMANNUALv2
--

CREATE SEQUENCE formannualv2_numeric_seq;


--
-- Table structure for table `formAnnualV2`
--

CREATE TABLE formAnnualV2 (
  ID numeric(10) NOT NULL default nextval('formannualv2_numeric_seq'),
  demographic_no numeric(10) default NULL,
  provider_no numeric(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(6) NOT NULL,
  pName varchar(60) default NULL,
  age char(3) default NULL,
  formDate date default NULL,
  pmhxPshxUpdated int2 default NULL,
  famHxUpdated int2 default NULL,
  socHxUpdated int2 default NULL,
  allergiesUpdated int2 default NULL,
  medicationsUpdated int2 default NULL,
  weight varchar(4) default NULL,
  height varchar(4) default NULL,
  waist varchar(4) default NULL,
  lmp date default NULL,
  BP varchar(6) default NULL,
  smokingNo int2 default NULL,
  smokingYes int2 default NULL,
  smoking varchar(100) default NULL,
  etohNo int2 default NULL,
  etohYes int2 default NULL,
  etoh varchar(100) default NULL,
  caffineNo int2 default NULL,
  caffineYes int2 default NULL,
  caffine varchar(100) default NULL,
  otcNo int2 default NULL,
  otcYes int2 default NULL,
  otc varchar(100) default NULL,
  exerciseNo int2 default NULL,
  exerciseYes int2 default NULL,
  exercise varchar(100) default NULL,
  nutritionNo int2 default NULL,
  nutritionYes int2 default NULL,
  nutrition varchar(100) default NULL,
  dentalNo int2 default NULL,
  dentalYes int2 default NULL,
  dental varchar(100) default NULL,
  occupationalNo int2 default NULL,
  occupationalYes int2 default NULL,
  occupational varchar(100) default NULL,
  travelNo int2 default NULL,
  travelYes int2 default NULL,
  travel varchar(100) default NULL,
  sexualityNo int2 default NULL,
  sexualityYes int2 default NULL,
  sexuality varchar(100) default NULL,
  generalN int2 default NULL,
  generalAbN int2 default NULL,
  general varchar(100) default NULL,
  headN int2 default NULL,
  headAbN int2 default NULL,
  head varchar(100) default NULL,
  chestN int2 default NULL,
  chestAbN int2 default NULL,
  chest varchar(100) default NULL,
  cvsN int2 default NULL,
  cvsAbN int2 default NULL,
  cvs varchar(100) default NULL,
  giN int2 default NULL,
  giAbN int2 default NULL,
  gi varchar(100) default NULL,
  guN int2 default NULL,
  guAbN int2 default NULL,
  gu varchar(100) default NULL,
  cnsN int2 default NULL,
  cnsAbN int2 default NULL,
  cns varchar(100) default NULL,
  mskN int2 default NULL,
  mskAbN int2 default NULL,
  msk varchar(100) default NULL,
  skinN int2 default NULL,
  skinAbN int2 default NULL,
  skin varchar(100) default NULL,
  moodN int2 default NULL,
  moodAbN int2 default NULL,
  mood varchar(100) default NULL,
  otherN int2 default NULL,
  otherAbN int2 default NULL,
  other text,
  eyesN int2 default NULL,
  eyesAbN int2 default NULL,
  eyes varchar(100) default NULL,
  earsN int2 default NULL,
  earsAbN int2 default NULL,
  ears varchar(100) default NULL,
  oropharynxN int2 default NULL,
  oropharynxAbN int2 default NULL,
  oropharynx varchar(100) default NULL,
  thyroidN int2 default NULL,
  thyroidAbN int2 default NULL,
  thyroid varchar(100) default NULL,
  lnodesN int2 default NULL,
  lnodesAbN int2 default NULL,
  lnodes varchar(100) default NULL,
  clearN int2 default NULL,
  clearAbN int2 default NULL,
  clear varchar(100) default NULL,
  bilatN int2 default NULL,
  bilatAbN int2 default NULL,
  bilat varchar(100) default NULL,
  wheezesN int2 default NULL,
  wheezesAbN int2 default NULL,
  wheezes varchar(100) default NULL,
  cracklesN int2 default NULL,
  cracklesAbN int2 default NULL,
  crackles varchar(100) default NULL,
  chestOther varchar(100) default NULL,
  s1s2N int2 default NULL,
  s1s2AbN int2 default NULL,
  s1s2 varchar(100) default NULL,
  murmurN int2 default NULL,
  murmurAbN int2 default NULL,
  murmur varchar(100) default NULL,
  periphPulseN int2 default NULL,
  periphPulseAbN int2 default NULL,
  periphPulse varchar(100) default NULL,
  edemaN int2 default NULL,
  edemaAbN int2 default NULL,
  edema varchar(100) default NULL,
  jvpN int2 default NULL,
  jvpAbN int2 default NULL,
  jvp varchar(100) default NULL,
  rhythmN int2 default NULL,
  rhythmAbN int2 default NULL,
  rhythm varchar(100) default NULL,
  chestbpN int2 default NULL,
  chestbpAbN int2 default NULL,
  chestbp varchar(100) default NULL,
  cvsOther varchar(100) default NULL,
  breastLeftN int2 default NULL,
  breastLeftAbN int2 default NULL,
  breastLeft varchar(100) default NULL,
  breastRightN int2 default NULL,
  breastRightAbN int2 default NULL,
  breastRight varchar(100) default NULL,
  softN int2 default NULL,
  softAbN int2 default NULL,
  soft varchar(100) default NULL,
  tenderN int2 default NULL,
  tenderAbN int2 default NULL,
  tender varchar(100) default NULL,
  bsN int2 default NULL,
  bsAbN int2 default NULL,
  bs varchar(100) default NULL,
  hepatomegN int2 default NULL,
  hepatomegAbN int2 default NULL,
  hepatomeg varchar(100) default NULL,
  splenomegN int2 default NULL,
  splenomegAbN int2 default NULL,
  splenomeg varchar(100) default NULL,
  massesN int2 default NULL,
  massesAbN int2 default NULL,
  masses varchar(100) default NULL,
  rectalN int2 default NULL,
  rectalAbN int2 default NULL,
  rectal varchar(100) default NULL,
  cxN int2 default NULL,
  cxAbN int2 default NULL,
  cx varchar(100) default NULL,
  bimanualN int2 default NULL,
  bimanualAbN int2 default NULL,
  bimanual varchar(100) default NULL,
  adnexaN int2 default NULL,
  adnexaAbN int2 default NULL,
  adnexa varchar(100) default NULL,
  papN int2 default NULL,
  papAbN int2 default NULL,
  pap varchar(100) default NULL,
  exammskN int2 default NULL,
  exammskAbN int2 default NULL,
  exammsk varchar(100) default NULL,
  examskinN int2 default NULL,
  examskinAbN int2 default NULL,
  examskin varchar(100) default NULL,
  examcnsN int2 default NULL,
  examcnsAbN int2 default NULL,
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
  immunizationtd int2 default NULL,
  immunizationPneumovax int2 default NULL,
  immunizationFlu int2 default NULL,
  immunizationMenjugate int2 default NULL,
  toDoImmunization text,
  signature varchar(60) default NULL,
  examGenitaliaN int2 default NULL,
  examGenitaliaAbN int2 default NULL,
  examGenitalia varchar(100) default NULL,
  toDoProstateCancer text,
  PRIMARY KEY  (ID)
);

--
-- Sequences for table formfalls
--
CREATE SEQUENCE formfalls_numeric_seq;

--
-- Table structure for table formFall
--
CREATE TABLE formFalls(
  ID numeric(10) NOT NULL  default nextval('formfalls_numeric_seq;'),
  demographic_no numeric(10) NOT NULL,
  provider_no numeric(10),
  formCreated date,
  formEdited timestamp NOT NULL,
  fallenLast12MY int2,
  fallenLast12MN int2,
  fallenLast12MNotRemember int2,
  injuredY int2,
  injuredN int2,
  medAttnY int2,
  medAttnN int2,
  hospitalizedY int2,
  hospitalizedN int2,
  limitActY int2,
  limitActN int2,
  PRIMARY KEY  (ID)
);

--
-- Sequences for table formimmunallergy
--
CREATE SEQUENCE formimmunallergy_numeric_seq;

--
-- Table structure for table `formImmunAllergy`
--

CREATE TABLE formImmunAllergy (
  ID numeric(10) NOT NULL default nextval('formimmunallergy_numeric_seq'),
  demographic_no numeric(10) NOT NULL default '0',
  provider_no numeric(10) default NULL,
  formCreated date default NULL,
  formEdited timestamp(6) NOT NULL,
  c_surname varchar(30) default NULL,
  c_givenName varchar(30) default NULL,
  dateAdmin date default NULL,
  tradeName varchar(50) default NULL,
  manufacturer varchar(50) default NULL,
  lot varchar(50) default NULL,
  expiDate date default NULL,
  doseAdminSC int2 default NULL,
  doseAdminIM int2 default NULL,
  doseAdminml int2 default NULL,
  doseAdminTxtml varchar(10) default NULL,
  locLtDel int2 default NULL,
  locRtDel int2 default NULL,
  locLtDelOUQ int2 default NULL,
  locRtDelOUQ int2 default NULL,
  locOther int2 default NULL,
  InstrStay20 int2 default NULL,
  InstrExpectLoc int2 default NULL,
  InstrFU int2 default NULL,
  disChNoComp int2 default NULL,
  PRIMARY KEY  (ID)
);




--
-- Sequences for table FORMLABREQ
--

CREATE SEQUENCE formlabreq_numeric_seq;

--
-- Table structure for table 'formLabReq'
--
CREATE TABLE formLabReq (
  ID numeric(10) DEFAULT nextval('formlabreq_numeric_seq'),
  demographic_no numeric(10) DEFAULT NULL,
  provider_no numeric(10) DEFAULT NULL,
  formCreated DATE DEFAULT NULL,
  formEdited timestamp(6) NOT NULL,
  provName varchar(60) DEFAULT NULL,
  clinicAddress varchar(30) DEFAULT NULL,
  clinicCity varchar(20) DEFAULT NULL,
  clinicPC varchar(7) DEFAULT NULL,
  practitionerNo varchar(14) DEFAULT NULL,
  ohip INT2 DEFAULT NULL,
  thirdParty INT2 DEFAULT NULL,
  wcb INT2 DEFAULT NULL,
  aci text,
  healthNumber varchar(10) DEFAULT NULL,
  version char(2) DEFAULT NULL,
  birthDate DATE DEFAULT NULL,
  paymentProgram varchar(4) DEFAULT NULL,
  province varchar(15) DEFAULT NULL,
  orn varchar(12) DEFAULT NULL,
  phoneNumber varchar(12) DEFAULT NULL,
  patientName varchar(40) DEFAULT NULL,
  sex varchar(6) DEFAULT NULL,
  patientAddress varchar(20) DEFAULT NULL,
  patientCity varchar(20) DEFAULT NULL,
  patientPC varchar(7) DEFAULT NULL,
  b_glucose INT2 DEFAULT NULL,
  b_creatine INT2 DEFAULT NULL,
  b_uricAcid INT2 DEFAULT NULL,
  b_sodium INT2 DEFAULT NULL,
  b_potassium INT2 DEFAULT NULL,
  b_chloride INT2 DEFAULT NULL,
  b_ast INT2 DEFAULT NULL,
  b_alkPhosphate INT2 DEFAULT NULL,
  b_bilirubin INT2 DEFAULT NULL,
  b_cholesterol INT2 DEFAULT NULL,
  b_triglyceride INT2 DEFAULT NULL,
  b_urinalysis INT2 DEFAULT NULL,
  v_acuteHepatitis INT2 DEFAULT NULL,
  v_chronicHepatitis INT2 DEFAULT NULL,
  v_immune INT2 DEFAULT NULL,
  v_hepA varchar(20) DEFAULT NULL,
  v_hepB varchar(20) DEFAULT NULL,
  h_bloodFilmExam INT2 DEFAULT NULL,
  h_hemoglobin INT2 DEFAULT NULL,
  h_wcbCount INT2 DEFAULT NULL,
  h_hematocrit INT2 DEFAULT NULL,
  h_prothrombTime INT2 DEFAULT NULL,
  i_pregnancyTest INT2 DEFAULT NULL,
  i_heterophile INT2 DEFAULT NULL,
  i_rubella INT2 DEFAULT NULL,
  i_prenatal INT2 DEFAULT NULL,
  i_repeatPrenatal INT2 DEFAULT NULL,
  i_prenatalHepatitisB INT2 DEFAULT NULL,
  i_vdrl INT2 DEFAULT NULL,
  m_cervicalVaginal INT2 DEFAULT NULL,
  m_sputum INT2 DEFAULT NULL,
  m_throat INT2 DEFAULT NULL,
  m_urine INT2 DEFAULT NULL,
  m_stoolCulture INT2 DEFAULT NULL,
  m_other varchar(20) DEFAULT NULL,
  otherTest text,
  formDate DATE DEFAULT NULL,
  signature varchar(60) DEFAULT NULL,
  PRIMARY KEY (ID)

);


--
-- Sequences for table FORMMMSE
--
CREATE SEQUENCE formmmse_numeric_seq;

--
-- Table structure for table 'formMMSE'
--
CREATE TABLE formMMSE (
  ID numeric(10) DEFAULT nextval('formmmse_numeric_seq'),
  demographic_no numeric(10) DEFAULT NULL,
  provider_no numeric(10) DEFAULT NULL,
  formCreated DATE DEFAULT NULL,
  formEdited timestamp(6) NOT NULL,
  pName varchar(50) DEFAULT NULL,
  age char(3) DEFAULT NULL,
  sex char(1) DEFAULT NULL,
  formDate DATE DEFAULT NULL,
  diagnosis text,
  meds text,
  o_date char(1) DEFAULT NULL,
  o_place char(1) DEFAULT NULL,
  r_objects char(1) DEFAULT NULL,
  a_serial char(1) DEFAULT NULL,
  re_name char(1) DEFAULT NULL,
  l_name char(1) DEFAULT NULL,
  l_repeat char(1) DEFAULT NULL,
  l_follow char(1) DEFAULT NULL,
  l_read char(1) DEFAULT NULL,
  l_write char(1) DEFAULT NULL,
  l_copy char(1) DEFAULT NULL,
  total char(2) DEFAULT NULL,
  lc_alert INT2 DEFAULT NULL,
  lc_drowsy INT2 DEFAULT NULL,
  lc_stupor INT2 DEFAULT NULL,
  lc_coma INT2 DEFAULT NULL,
  i_dementia INT2 DEFAULT NULL,
  i_depression INT2 DEFAULT NULL,
  i_normal INT2 DEFAULT NULL,
  PRIMARY KEY (ID)
);

--
-- Sequences for table FORMMENTALHEALTH
--
CREATE SEQUENCE formmentalhealth_numeric_seq;

--
-- Table structure for table 'formMentalHealth'
--
CREATE TABLE formMentalHealth (
  ID numeric(10) DEFAULT nextval('formmentalhealth_numeric_seq'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  provider_no numeric(10) DEFAULT NULL,
  formCreated DATE DEFAULT NULL,
  formEdited timestamp(6) NOT NULL,
  c_lastVisited varchar(15) DEFAULT NULL,
  c_pName varchar(60) DEFAULT NULL,
  c_address varchar(80) DEFAULT NULL,
  c_birthDate DATE DEFAULT NULL,
  c_sex varchar(6) DEFAULT NULL,
  c_homePhone varchar(10) DEFAULT NULL,
  c_referralDate DATE DEFAULT NULL,
  c_referredBy varchar(30) DEFAULT NULL,
  r_rps1 char(2) DEFAULT NULL,
  r_rps2 char(2) DEFAULT NULL,
  r_rps3 char(2) DEFAULT NULL,
  r_rpsOther varchar(30) DEFAULT NULL,
  r_rpi1 char(2) DEFAULT NULL,
  r_rpi2 char(2) DEFAULT NULL,
  r_rpi3 char(2) DEFAULT NULL,
  r_rpiOther varchar(30) DEFAULT NULL,
  r_rmpi1 char(2) DEFAULT NULL,
  r_rmpi2 char(2) DEFAULT NULL,
  r_rmpi3 char(2) DEFAULT NULL,
  r_rmpiOther varchar(30) DEFAULT NULL,
  r_ir1 char(2) DEFAULT NULL,
  r_ir2 char(2) DEFAULT NULL,
  r_ir3 char(2) DEFAULT NULL,
  r_irOther varchar(30) DEFAULT NULL,
  r_arm1 char(2) DEFAULT NULL,
  r_arm2 char(2) DEFAULT NULL,
  r_arm3 char(2) DEFAULT NULL,
  r_armOther varchar(30) DEFAULT NULL,
  r_refComments text,
  a_specialist varchar(30) DEFAULT NULL,
  a_aps1 char(2) DEFAULT NULL,
  a_aps2 char(2) DEFAULT NULL,
  a_aps3 char(2) DEFAULT NULL,
  a_apsOther varchar(30) DEFAULT NULL,
  a_api1 char(2) DEFAULT NULL,
  a_api2 char(2) DEFAULT NULL,
  a_api3 char(2) DEFAULT NULL,
  a_apiOther varchar(30) DEFAULT NULL,
  a_ampi1 char(2) DEFAULT NULL,
  a_ampi2 char(2) DEFAULT NULL,
  a_ampi3 char(2) DEFAULT NULL,
  a_ampiOther varchar(50) DEFAULT NULL,
  a_assComments text,
  a_tp1 char(2) DEFAULT NULL,
  a_tp2 char(2) DEFAULT NULL,
  a_tp3 char(2) DEFAULT NULL,
  a_tpOther varchar(30) DEFAULT NULL,
  o_specialist varchar(30) DEFAULT NULL,
  o_numVisits varchar(5) DEFAULT NULL,
  o_formDate DATE DEFAULT NULL,
  o_sp1 char(2) DEFAULT NULL,
  o_sp2 char(2) DEFAULT NULL,
  o_sp3 char(2) DEFAULT NULL,
  o_spOther varchar(30) DEFAULT NULL,
  o_pe1 char(2) DEFAULT NULL,
  o_pe2 char(2) DEFAULT NULL,
  o_pe3 char(2) DEFAULT NULL,
  o_peOther varchar(30) DEFAULT NULL,
  o_d1 char(2) DEFAULT NULL,
  o_d2 char(2) DEFAULT NULL,
  o_d3 char(2) DEFAULT NULL,
  o_dOther varchar(30) DEFAULT NULL,
  o_pns1 char(2) DEFAULT NULL,
  o_pns2 char(2) DEFAULT NULL,
  o_pns3 char(2) DEFAULT NULL,
  o_pnsOther varchar(30) DEFAULT NULL,
  o_outComments text,
  a_formDate DATE DEFAULT NULL,
  PRIMARY KEY (ID)

);

--
-- Table structure for table 'formPalliativeCare'
--



--
-- Sequences for table FORMPALLIATIVECARE
--

CREATE SEQUENCE formpalliativecare_numeric_s;

CREATE TABLE formPalliativeCare (
  ID numeric(10) DEFAULT nextval('formpalliativecare_numeric_s'),
  demographic_no numeric(10) DEFAULT NULL,
  provider_no numeric(10) DEFAULT NULL,
  formCreated DATE DEFAULT NULL,
  formEdited timestamp(6) NOT NULL,
  pName varchar(60) DEFAULT NULL,
  diagnosis varchar(60) DEFAULT NULL,
  date1 DATE DEFAULT NULL,
  date2 DATE DEFAULT NULL,
  date3 DATE DEFAULT NULL,
  date4 DATE DEFAULT NULL,
  pain1 varchar(255) DEFAULT NULL,
  pain2 varchar(255) DEFAULT NULL,
  pain3 varchar(255) DEFAULT NULL,
  pain4 varchar(255) DEFAULT NULL,
  giBowels1 varchar(255) DEFAULT NULL,
  giBowels2 varchar(255) DEFAULT NULL,
  giBowels3 varchar(255) DEFAULT NULL,
  giBowels4 varchar(255) DEFAULT NULL,
  giNausea1 varchar(255) DEFAULT NULL,
  giNausea2 varchar(255) DEFAULT NULL,
  giNausea3 varchar(255) DEFAULT NULL,
  giNausea4 varchar(255) DEFAULT NULL,
  giDysphagia1 varchar(255) DEFAULT NULL,
  giDysphagia2 varchar(255) DEFAULT NULL,
  giDysphagia3 varchar(255) DEFAULT NULL,
  giDysphagia4 varchar(255) DEFAULT NULL,
  giHiccups1 varchar(255) DEFAULT NULL,
  giHiccups2 varchar(255) DEFAULT NULL,
  giHiccups3 varchar(255) DEFAULT NULL,
  giHiccups4 varchar(255) DEFAULT NULL,
  giMouth1 varchar(255) DEFAULT NULL,
  giMouth2 varchar(255) DEFAULT NULL,
  giMouth3 varchar(255) DEFAULT NULL,
  giMouth4 varchar(255) DEFAULT NULL,
  gu1 varchar(255) DEFAULT NULL,
  gu2 varchar(255) DEFAULT NULL,
  gu3 varchar(255) DEFAULT NULL,
  gu4 varchar(255) DEFAULT NULL,
  skinUlcers1 varchar(255) DEFAULT NULL,
  skinUlcers2 varchar(255) DEFAULT NULL,
  skinUlcers3 varchar(255) DEFAULT NULL,
  skinUlcers4 varchar(255) DEFAULT NULL,
  skinPruritis1 varchar(255) DEFAULT NULL,
  skinPruritis2 varchar(255) DEFAULT NULL,
  skinPruritis3 varchar(255) DEFAULT NULL,
  skinPruritis4 varchar(255) DEFAULT NULL,
  psychAgitation1 varchar(255) DEFAULT NULL,
  psychAgitation2 varchar(255) DEFAULT NULL,
  psychAgitation3 varchar(255) DEFAULT NULL,
  psychAgitation4 varchar(255) DEFAULT NULL,
  psychAnorexia1 varchar(255) DEFAULT NULL,
  psychAnorexia2 varchar(255) DEFAULT NULL,
  psychAnorexia3 varchar(255) DEFAULT NULL,
  psychAnorexia4 varchar(255) DEFAULT NULL,
  psychAnxiety1 varchar(255) DEFAULT NULL,
  psychAnxiety2 varchar(255) DEFAULT NULL,
  psychAnxiety3 varchar(255) DEFAULT NULL,
  psychAnxiety4 varchar(255) DEFAULT NULL,
  psychDepression1 varchar(255) DEFAULT NULL,
  psychDepression2 varchar(255) DEFAULT NULL,
  psychDepression3 varchar(255) DEFAULT NULL,
  psychDepression4 varchar(255) DEFAULT NULL,
  psychFatigue1 varchar(255) DEFAULT NULL,
  psychFatigue2 varchar(255) DEFAULT NULL,
  psychFatigue3 varchar(255) DEFAULT NULL,
  psychFatigue4 varchar(255) DEFAULT NULL,
  psychSomnolence1 varchar(255) DEFAULT NULL,
  psychSomnolence2 varchar(255) DEFAULT NULL,
  psychSomnolence3 varchar(255) DEFAULT NULL,
  psychSomnolence4 varchar(255) DEFAULT NULL,
  respCough1 varchar(255) DEFAULT NULL,
  respCough2 varchar(255) DEFAULT NULL,
  respCough3 varchar(255) DEFAULT NULL,
  respCough4 varchar(255) DEFAULT NULL,
  respDyspnea1 varchar(255) DEFAULT NULL,
  respDyspnea2 varchar(255) DEFAULT NULL,
  respDyspnea3 varchar(255) DEFAULT NULL,
  respDyspnea4 varchar(255) DEFAULT NULL,
  respFever1 varchar(255) DEFAULT NULL,
  respFever2 varchar(255) DEFAULT NULL,
  respFever3 varchar(255) DEFAULT NULL,
  respFever4 varchar(255) DEFAULT NULL,
  respCaregiver1 varchar(255) DEFAULT NULL,
  respCaregiver2 varchar(255) DEFAULT NULL,
  respCaregiver3 varchar(255) DEFAULT NULL,
  respCaregiver4 varchar(255) DEFAULT NULL,
  other1 varchar(255) DEFAULT NULL,
  other2 varchar(255) DEFAULT NULL,
  other3 varchar(255) DEFAULT NULL,
  other4 varchar(255) DEFAULT NULL,
  signature1 varchar(50) DEFAULT NULL,
  signature2 varchar(50) DEFAULT NULL,
  signature3 varchar(50) DEFAULT NULL,
  signature4 varchar(50) DEFAULT NULL,
  PRIMARY KEY (ID)

);

--
-- Table structure for table 'formPeriMenopausal'
--



--
-- Sequences for table FORMPERIMENOPAUSAL
--

CREATE SEQUENCE formperimenopausal_numeric_s;

CREATE TABLE formPeriMenopausal (
  ID numeric(10) DEFAULT nextval('formperimenopausal_numeric_s'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  provider_no numeric(10) DEFAULT NULL,
  formCreated DATE DEFAULT NULL,
  formEdited timestamp(6) NOT NULL,
  lastVisited char(1) DEFAULT NULL,
  pName varchar(60) DEFAULT NULL,
  ageMenopause char(2) DEFAULT NULL,
  age char(3) DEFAULT NULL,
  orf_emYes INT2 DEFAULT NULL,
  orf_emNo INT2 DEFAULT NULL,
  orf_fhoYes INT2 DEFAULT NULL,
  orf_fhoNo INT2 DEFAULT NULL,
  orf_fhofYes INT2 DEFAULT NULL,
  orf_fhofNo INT2 DEFAULT NULL,
  orf_lhYes INT2 DEFAULT NULL,
  orf_lhNo INT2 DEFAULT NULL,
  orf_phfYes INT2 DEFAULT NULL,
  orf_phfNo INT2 DEFAULT NULL,
  orf_warYes INT2 DEFAULT NULL,
  orf_warNo INT2 DEFAULT NULL,
  orf_tsbYes INT2 DEFAULT NULL,
  orf_tsbNo INT2 DEFAULT NULL,
  orf_hipYes INT2 DEFAULT NULL,
  orf_hipNo INT2 DEFAULT NULL,
  orf_ieYes INT2 DEFAULT NULL,
  orf_ieNo INT2 DEFAULT NULL,
  orf_llciYes INT2 DEFAULT NULL,
  orf_llciNo INT2 DEFAULT NULL,
  orf_csYes INT2 DEFAULT NULL,
  orf_csNo INT2 DEFAULT NULL,
  orf_cYes INT2 DEFAULT NULL,
  orf_cNo INT2 DEFAULT NULL,
  orf_aYes INT2 DEFAULT NULL,
  orf_aNo INT2 DEFAULT NULL,
  orf_cptdYes INT2 DEFAULT NULL,
  orf_cptdNo INT2 DEFAULT NULL,
  orf_dcYes INT2 DEFAULT NULL,
  orf_dcNo INT2 DEFAULT NULL,
  orf_adYes INT2 DEFAULT NULL,
  orf_adNo INT2 DEFAULT NULL,
  orf_tmYes INT2 DEFAULT NULL,
  orf_tmNo INT2 DEFAULT NULL,
  orf_comments varchar(255) DEFAULT NULL,
  cs_mcYes INT2 DEFAULT NULL,
  cs_mcNo INT2 DEFAULT NULL,
  cs_mpYes INT2 DEFAULT NULL,
  cs_mpNo INT2 DEFAULT NULL,
  cs_hfYes INT2 DEFAULT NULL,
  cs_hfNo INT2 DEFAULT NULL,
  cs_vdYes INT2 DEFAULT NULL,
  cs_vdNo INT2 DEFAULT NULL,
  cs_dsiYes INT2 DEFAULT NULL,
  cs_dsiNo INT2 DEFAULT NULL,
  cs_lisaYes INT2 DEFAULT NULL,
  cs_lisaNo INT2 DEFAULT NULL,
  cs_lbcYes INT2 DEFAULT NULL,
  cs_lbcNo INT2 DEFAULT NULL,
  cs_hbiYes INT2 DEFAULT NULL,
  cs_hbiNo INT2 DEFAULT NULL,
  cs_comments varchar(255) DEFAULT NULL,
  crf_fhhdYes INT2 DEFAULT NULL,
  crf_fhhdNo INT2 DEFAULT NULL,
  crf_haYes INT2 DEFAULT NULL,
  crf_haNo INT2 DEFAULT NULL,
  crf_hchfYes INT2 DEFAULT NULL,
  crf_hchfNo INT2 DEFAULT NULL,
  crf_hhaYes INT2 DEFAULT NULL,
  crf_hhaNo INT2 DEFAULT NULL,
  crf_hdYes INT2 DEFAULT NULL,
  crf_hdNo INT2 DEFAULT NULL,
  crf_csYes INT2 DEFAULT NULL,
  crf_csNo INT2 DEFAULT NULL,
  crf_hbpYes INT2 DEFAULT NULL,
  crf_hbpNo INT2 DEFAULT NULL,
  crf_lhcYes INT2 DEFAULT NULL,
  crf_lhcNo INT2 DEFAULT NULL,
  crf_htYes INT2 DEFAULT NULL,
  crf_htNo INT2 DEFAULT NULL,
  crf_hlcYes INT2 DEFAULT NULL,
  crf_hlcNo INT2 DEFAULT NULL,
  crf_oYes INT2 DEFAULT NULL,
  crf_oNo INT2 DEFAULT NULL,
  crf_slYes INT2 DEFAULT NULL,
  crf_slNo INT2 DEFAULT NULL,
  crf_comments varchar(255) DEFAULT NULL,
  rh_fhbcYes INT2 DEFAULT NULL,
  rh_fhbcNo INT2 DEFAULT NULL,
  rh_phbcYes INT2 DEFAULT NULL,
  rh_phbcNo INT2 DEFAULT NULL,
  rh_phocYes INT2 DEFAULT NULL,
  rh_phocNo INT2 DEFAULT NULL,
  ageHysterectomy char(2) DEFAULT NULL,
  rh_hYes INT2 DEFAULT NULL,
  rh_hNo INT2 DEFAULT NULL,
  rh_hwroYes INT2 DEFAULT NULL,
  rh_hwroNo INT2 DEFAULT NULL,
  rh_hpcbYes INT2 DEFAULT NULL,
  rh_hpcbNo INT2 DEFAULT NULL,
  rh_fhadYes INT2 DEFAULT NULL,
  rh_fhadNo INT2 DEFAULT NULL,
  rh_fhccYes INT2 DEFAULT NULL,
  rh_fhccNo INT2 DEFAULT NULL,
  rh_other varchar(60) DEFAULT NULL,
  rh_oYes INT2 DEFAULT NULL,
  rh_oNo INT2 DEFAULT NULL,
  rh_comments varchar(255) DEFAULT NULL,
  cm_cs varchar(30) DEFAULT NULL,
  cm_vds varchar(30) DEFAULT NULL,
  cm_other1 varchar(30) DEFAULT NULL,
  cm_o1 varchar(30) DEFAULT NULL,
  cm_other2 varchar(30) DEFAULT NULL,
  cm_o2 varchar(30) DEFAULT NULL,
  cm_comments varchar(255) DEFAULT NULL,
  phrtYes INT2 DEFAULT NULL,
  phrtNo INT2 DEFAULT NULL,
  estrogenYes INT2 DEFAULT NULL,
  estrogenNo INT2 DEFAULT NULL,
  progesteroneYes INT2 DEFAULT NULL,
  progesteroneNo INT2 DEFAULT NULL,
  hrtYes INT2 DEFAULT NULL,
  hrtNo INT2 DEFAULT NULL,
  whenHrt varchar(20) DEFAULT NULL,
  reasonDiscontinued varchar(100) DEFAULT NULL,
  date1 DATE DEFAULT NULL,
  date2 DATE DEFAULT NULL,
  date3 DATE DEFAULT NULL,
  date4 DATE DEFAULT NULL,
  date5 DATE DEFAULT NULL,
  date6 DATE DEFAULT NULL,
  date7 DATE DEFAULT NULL,
  date8 DATE DEFAULT NULL,
  etohUse1 varchar(100) DEFAULT NULL,
  etohUse2 varchar(100) DEFAULT NULL,
  etohUse3 varchar(100) DEFAULT NULL,
  etohUse4 varchar(100) DEFAULT NULL,
  smokingCessation1 varchar(100) DEFAULT NULL,
  smokingCessation2 varchar(100) DEFAULT NULL,
  smokingCessation3 varchar(100) DEFAULT NULL,
  smokingCessation4 varchar(100) DEFAULT NULL,
  exercise1 varchar(100) DEFAULT NULL,
  exercise2 varchar(100) DEFAULT NULL,
  exercise3 varchar(100) DEFAULT NULL,
  exercise4 varchar(100) DEFAULT NULL,
  vision1 varchar(100) DEFAULT NULL,
  vision2 varchar(100) DEFAULT NULL,
  vision3 varchar(100) DEFAULT NULL,
  vision4 varchar(100) DEFAULT NULL,
  lowFat1 varchar(100) DEFAULT NULL,
  lowFat2 varchar(100) DEFAULT NULL,
  lowFat3 varchar(100) DEFAULT NULL,
  lowFat4 varchar(100) DEFAULT NULL,
  tdLast1 varchar(100) DEFAULT NULL,
  tdLast2 varchar(100) DEFAULT NULL,
  tdLast3 varchar(100) DEFAULT NULL,
  tdLast4 varchar(100) DEFAULT NULL,
  calcium1 varchar(100) DEFAULT NULL,
  calcium2 varchar(100) DEFAULT NULL,
  calcium3 varchar(100) DEFAULT NULL,
  calcium4 varchar(100) DEFAULT NULL,
  flu1 varchar(100) DEFAULT NULL,
  flu2 varchar(100) DEFAULT NULL,
  flu3 varchar(100) DEFAULT NULL,
  flu4 varchar(100) DEFAULT NULL,
  vitaminD1 varchar(100) DEFAULT NULL,
  vitaminD2 varchar(100) DEFAULT NULL,
  vitaminD3 varchar(100) DEFAULT NULL,
  vitaminD4 varchar(100) DEFAULT NULL,
  pneumovaxDate DATE DEFAULT NULL,
  pneumovax1 varchar(100) DEFAULT NULL,
  pneumovax2 varchar(100) DEFAULT NULL,
  pneumovax3 varchar(100) DEFAULT NULL,
  pneumovax4 varchar(100) DEFAULT NULL,
  papSmear1 varchar(100) DEFAULT NULL,
  papSmear2 varchar(100) DEFAULT NULL,
  papSmear3 varchar(100) DEFAULT NULL,
  papSmear4 varchar(100) DEFAULT NULL,
  height1 varchar(100) DEFAULT NULL,
  height2 varchar(100) DEFAULT NULL,
  height3 varchar(100) DEFAULT NULL,
  height4 varchar(100) DEFAULT NULL,
  bloodPressure1 varchar(100) DEFAULT NULL,
  bloodPressure2 varchar(100) DEFAULT NULL,
  bloodPressure3 varchar(100) DEFAULT NULL,
  bloodPressure4 varchar(100) DEFAULT NULL,
  weight1 varchar(100) DEFAULT NULL,
  weight2 varchar(100) DEFAULT NULL,
  weight3 varchar(100) DEFAULT NULL,
  weight4 varchar(100) DEFAULT NULL,
  cbe1 varchar(100) DEFAULT NULL,
  cbe2 varchar(100) DEFAULT NULL,
  cbe3 varchar(100) DEFAULT NULL,
  cbe4 varchar(100) DEFAULT NULL,
  bmd1 varchar(100) DEFAULT NULL,
  bmd2 varchar(100) DEFAULT NULL,
  bmd3 varchar(100) DEFAULT NULL,
  bmd4 varchar(100) DEFAULT NULL,
  mammography1 varchar(100) DEFAULT NULL,
  mammography2 varchar(100) DEFAULT NULL,
  mammography3 varchar(100) DEFAULT NULL,
  mammography4 varchar(100) DEFAULT NULL,
  other1 varchar(30) DEFAULT NULL,
  other11 varchar(100) DEFAULT NULL,
  other12 varchar(100) DEFAULT NULL,
  other13 varchar(100) DEFAULT NULL,
  other14 varchar(100) DEFAULT NULL,
  other2 varchar(30) DEFAULT NULL,
  other21 varchar(100) DEFAULT NULL,
  other22 varchar(100) DEFAULT NULL,
  other23 varchar(100) DEFAULT NULL,
  other24 varchar(100) DEFAULT NULL,
  other3 varchar(30) DEFAULT NULL,
  other31 varchar(100) DEFAULT NULL,
  other32 varchar(100) DEFAULT NULL,
  other33 varchar(100) DEFAULT NULL,
  other34 varchar(100) DEFAULT NULL,
  PRIMARY KEY (ID)

);

--
-- Sequences for table FORMROURKE
--
CREATE SEQUENCE formrourke_numeric_seq;

--
-- Table structure for table 'formRourke'
--
CREATE TABLE formRourke (
  ID numeric(10) DEFAULT nextval('formrourke_numeric_seq'),
  demographic_no numeric(10) DEFAULT NULL,
  provider_no numeric(10) DEFAULT NULL,
  formCreated DATE DEFAULT NULL,
  formEdited timestamp(6) NOT NULL,
  c_lastVisited char(1) DEFAULT NULL,
  c_birthRemarks text,
  c_riskFactors text,
  c_pName varchar(60) DEFAULT NULL,
  c_birthDate DATE DEFAULT NULL,
  c_length varchar(6) DEFAULT NULL,
  c_headCirc varchar(6) DEFAULT NULL,
  c_birthWeight varchar(7) DEFAULT NULL,
  c_dischargeWeight varchar(7) DEFAULT NULL,
  p1_date1w DATE DEFAULT NULL,
  p1_date2w DATE DEFAULT NULL,
  p1_date1m DATE DEFAULT NULL,
  p1_date2m DATE DEFAULT NULL,
  p1_ht1w varchar(5) DEFAULT NULL,
  p1_wt1w varchar(5) DEFAULT NULL,
  p1_hc1w varchar(5) DEFAULT NULL,
  p1_ht2w varchar(5) DEFAULT NULL,
  p1_wt2w varchar(5) DEFAULT NULL,
  p1_hc2w varchar(5) DEFAULT NULL,
  p1_ht1m varchar(5) DEFAULT NULL,
  p1_wt1m varchar(5) DEFAULT NULL,
  p1_hc1m varchar(5) DEFAULT NULL,
  p1_ht2m varchar(5) DEFAULT NULL,
  p1_wt2m varchar(5) DEFAULT NULL,
  p1_hc2m varchar(5) DEFAULT NULL,
  p1_pConcern1w text,
  p1_pConcern2w text,
  p1_pConcern1m text,
  p1_pConcern2m text,
  p1_breastFeeding1w INT2 DEFAULT NULL,
  p1_formulaFeeding1w INT2 DEFAULT NULL,
  p1_stoolUrine1w INT2 DEFAULT NULL,
  p1_nutrition1w varchar(50) DEFAULT NULL,
  p1_breastFeeding2w INT2 DEFAULT NULL,
  p1_formulaFeeding2w INT2 DEFAULT NULL,
  p1_stoolUrine2w INT2 DEFAULT NULL,
  p1_nutrition2w varchar(50) DEFAULT NULL,
  p1_breastFeeding1m INT2 DEFAULT NULL,
  p1_formulaFeeding1m INT2 DEFAULT NULL,
  p1_stoolUrine1m INT2 DEFAULT NULL,
  p1_nutrition1m varchar(50) DEFAULT NULL,
  p1_breastFeeding2m INT2 DEFAULT NULL,
  p1_formulaFeeding2m INT2 DEFAULT NULL,
  p1_nutrition2m varchar(50) DEFAULT NULL,
  p1_carSeat1w INT2 DEFAULT NULL,
  p1_cribSafety1w INT2 DEFAULT NULL,
  p1_sleeping1w INT2 DEFAULT NULL,
  p1_sooth1w INT2 DEFAULT NULL,
  p1_bonding1w INT2 DEFAULT NULL,
  p1_fatigue1w INT2 DEFAULT NULL,
  p1_siblings1w INT2 DEFAULT NULL,
  p1_family1w INT2 DEFAULT NULL,
  p1_homeVisit1w INT2 DEFAULT NULL,
  p1_sleepPos1w INT2 DEFAULT NULL,
  p1_temp1w INT2 DEFAULT NULL,
  p1_smoke1w INT2 DEFAULT NULL,
  p1_educationAdvice1w varchar(50) DEFAULT NULL,
  p1_carSeat2w INT2 DEFAULT NULL,
  p1_cribSafety2w INT2 DEFAULT NULL,
  p1_sleeping2w INT2 DEFAULT NULL,
  p1_sooth2w INT2 DEFAULT NULL,
  p1_bonding2w INT2 DEFAULT NULL,
  p1_fatigue2w INT2 DEFAULT NULL,
  p1_family2w INT2 DEFAULT NULL,
  p1_siblings2w INT2 DEFAULT NULL,
  p1_homeVisit2w INT2 DEFAULT NULL,
  p1_sleepPos2w INT2 DEFAULT NULL,
  p1_temp2w INT2 DEFAULT NULL,
  p1_smoke2w INT2 DEFAULT NULL,
  p1_educationAdvice2w varchar(50) DEFAULT NULL,
  p1_carbonMonoxide1m INT2 DEFAULT NULL,
  p1_sleepwear1m INT2 DEFAULT NULL,
  p1_hotWater1m INT2 DEFAULT NULL,
  p1_toys1m INT2 DEFAULT NULL,
  p1_crying1m INT2 DEFAULT NULL,
  p1_sooth1m INT2 DEFAULT NULL,
  p1_interaction1m INT2 DEFAULT NULL,
  p1_supports1m INT2 DEFAULT NULL,
  p1_educationAdvice1m varchar(50) DEFAULT NULL,
  p1_falls2m INT2 DEFAULT NULL,
  p1_toys2m INT2 DEFAULT NULL,
  p1_crying2m INT2 DEFAULT NULL,
  p1_sooth2m INT2 DEFAULT NULL,
  p1_interaction2m INT2 DEFAULT NULL,
  p1_stress2m INT2 DEFAULT NULL,
  p1_fever2m INT2 DEFAULT NULL,
  p1_educationAdvice2m varchar(50) DEFAULT NULL,
  p1_development1w varchar(50) DEFAULT NULL,
  p1_development2w varchar(50) DEFAULT NULL,
  p1_focusGaze1m INT2 DEFAULT NULL,
  p1_startles1m INT2 DEFAULT NULL,
  p1_sucks1m INT2 DEFAULT NULL,
  p1_noParentsConcerns1m INT2 DEFAULT NULL,
  p1_development1m varchar(50) DEFAULT NULL,
  p1_followMoves2m INT2 DEFAULT NULL,
  p1_sounds2m INT2 DEFAULT NULL,
  p1_headUp2m INT2 DEFAULT NULL,
  p1_cuddled2m INT2 DEFAULT NULL,
  p1_noParentConcerns2m INT2 DEFAULT NULL,
  p1_development2m varchar(50) DEFAULT NULL,
  p1_skin1w INT2 DEFAULT NULL,
  p1_fontanelles1w INT2 DEFAULT NULL,
  p1_eyes1w INT2 DEFAULT NULL,
  p1_ears1w INT2 DEFAULT NULL,
  p1_heartLungs1w INT2 DEFAULT NULL,
  p1_umbilicus1w INT2 DEFAULT NULL,
  p1_femoralPulses1w INT2 DEFAULT NULL,
  p1_hips1w INT2 DEFAULT NULL,
  p1_testicles1w INT2 DEFAULT NULL,
  p1_maleUrinary1w INT2 DEFAULT NULL,
  p1_physical1w varchar(50) DEFAULT NULL,
  p1_skin2w INT2 DEFAULT NULL,
  p1_fontanelles2w INT2 DEFAULT NULL,
  p1_eyes2w INT2 DEFAULT NULL,
  p1_ears2w INT2 DEFAULT NULL,
  p1_heartLungs2w INT2 DEFAULT NULL,
  p1_umbilicus2w INT2 DEFAULT NULL,
  p1_femoralPulses2w INT2 DEFAULT NULL,
  p1_hips2w INT2 DEFAULT NULL,
  p1_testicles2w INT2 DEFAULT NULL,
  p1_maleUrinary2w INT2 DEFAULT NULL,
  p1_physical2w varchar(50) DEFAULT NULL,
  p1_fontanelles1m INT2 DEFAULT NULL,
  p1_eyes1m INT2 DEFAULT NULL,
  p1_cover1m INT2 DEFAULT NULL,
  p1_hearing1m INT2 DEFAULT NULL,
  p1_heart1m INT2 DEFAULT NULL,
  p1_hips1m INT2 DEFAULT NULL,
  p1_physical1m varchar(50) DEFAULT NULL,
  p1_fontanelles2m INT2 DEFAULT NULL,
  p1_eyes2m INT2 DEFAULT NULL,
  p1_cover2m INT2 DEFAULT NULL,
  p1_hearing2m INT2 DEFAULT NULL,
  p1_heart2m INT2 DEFAULT NULL,
  p1_hips2m INT2 DEFAULT NULL,
  p1_physical2m varchar(50) DEFAULT NULL,
  p1_pkuThyroid1w INT2 DEFAULT NULL,
  p1_hemoScreen1w INT2 DEFAULT NULL,
  p1_problems1w varchar(50) DEFAULT NULL,
  p1_problems2w varchar(50) DEFAULT NULL,
  p1_problems1m varchar(50) DEFAULT NULL,
  p1_problems2m varchar(50) DEFAULT NULL,
  p1_hepB1w INT2 DEFAULT NULL,
  p1_immunization1w varchar(50) DEFAULT NULL,
  p1_immunization2w varchar(50) DEFAULT NULL,
  p1_immuniz1m INT2 DEFAULT NULL,
  p1_acetaminophen1m INT2 DEFAULT NULL,
  p1_hepB1m INT2 DEFAULT NULL,
  p1_immunization1m varchar(50) DEFAULT NULL,
  p1_acetaminophen2m INT2 DEFAULT NULL,
  p1_hib2m INT2 DEFAULT NULL,
  p1_polio2m INT2 DEFAULT NULL,
  p1_immunization2m varchar(50) DEFAULT NULL,
  p1_signature1w varchar(50) DEFAULT NULL,
  p1_signature2w varchar(50) DEFAULT NULL,
  p1_signature1m varchar(50) DEFAULT NULL,
  p1_signature2m varchar(50) DEFAULT NULL,
  p2_date4m DATE DEFAULT NULL,
  p2_date6m DATE DEFAULT NULL,
  p2_date9m DATE DEFAULT NULL,
  p2_date12m DATE DEFAULT NULL,
  p2_ht4m varchar(5) DEFAULT NULL,
  p2_wt4m varchar(5) DEFAULT NULL,
  p2_hc4m varchar(5) DEFAULT NULL,
  p2_ht6m varchar(5) DEFAULT NULL,
  p2_wt6m varchar(5) DEFAULT NULL,
  p2_hc6m varchar(5) DEFAULT NULL,
  p2_ht9m varchar(5) DEFAULT NULL,
  p2_wt9m varchar(5) DEFAULT NULL,
  p2_hc9m varchar(5) DEFAULT NULL,
  p2_ht12m varchar(5) DEFAULT NULL,
  p2_wt12m varchar(5) DEFAULT NULL,
  p2_hc12m varchar(5) DEFAULT NULL,
  p2_pConcern4m text,
  p2_pConcern6m text,
  p2_pConcern9m text,
  p2_pConcern12m text,
  p2_breastFeeding4m INT2 DEFAULT NULL,
  p2_formulaFeeding4m INT2 DEFAULT NULL,
  p2_cereal4m INT2 DEFAULT NULL,
  p2_nutrition4m varchar(50) DEFAULT NULL,
  p2_breastFeeding6m INT2 DEFAULT NULL,
  p2_formulaFeeding6m INT2 DEFAULT NULL,
  p2_bottle6m INT2 DEFAULT NULL,
  p2_vegFruit6m INT2 DEFAULT NULL,
  p2_egg6m INT2 DEFAULT NULL,
  p2_choking6m INT2 DEFAULT NULL,
  p2_nutrition6m varchar(50) DEFAULT NULL,
  p2_breastFeeding9m INT2 DEFAULT NULL,
  p2_formulaFeeding9m INT2 DEFAULT NULL,
  p2_bottle9m INT2 DEFAULT NULL,
  p2_meat9m INT2 DEFAULT NULL,
  p2_milk9m INT2 DEFAULT NULL,
  p2_egg9m INT2 DEFAULT NULL,
  p2_choking9m INT2 DEFAULT NULL,
  p2_nutrition9m varchar(50) DEFAULT NULL,
  p2_milk12m INT2 DEFAULT NULL,
  p2_bottle12m INT2 DEFAULT NULL,
  p2_appetite12m INT2 DEFAULT NULL,
  p2_nutrition12m varchar(50) DEFAULT NULL,
  p2_carSeat4m INT2 DEFAULT NULL,
  p2_stairs4m INT2 DEFAULT NULL,
  p2_bath4m INT2 DEFAULT NULL,
  p2_sleeping4m INT2 DEFAULT NULL,
  p2_parent4m INT2 DEFAULT NULL,
  p2_childCare4m INT2 DEFAULT NULL,
  p2_family4m INT2 DEFAULT NULL,
  p2_teething4m INT2 DEFAULT NULL,
  p2_educationAdvice4m varchar(50) DEFAULT NULL,
  p2_poison6m INT2 DEFAULT NULL,
  p2_electric6m INT2 DEFAULT NULL,
  p2_sleeping6m INT2 DEFAULT NULL,
  p2_parent6m INT2 DEFAULT NULL,
  p2_childCare6m INT2 DEFAULT NULL,
  p2_educationAdvice6m varchar(50) DEFAULT NULL,
  p2_childProof9m INT2 DEFAULT NULL,
  p2_separation9m INT2 DEFAULT NULL,
  p2_sleeping9m INT2 DEFAULT NULL,
  p2_dayCare9m INT2 DEFAULT NULL,
  p2_homeVisit9m INT2 DEFAULT NULL,
  p2_smoke9m INT2 DEFAULT NULL,
  p2_educationAdvice9m varchar(50) DEFAULT NULL,
  p2_poison12m INT2 DEFAULT NULL,
  p2_electric12m INT2 DEFAULT NULL,
  p2_carbon12m INT2 DEFAULT NULL,
  p2_hotWater12m INT2 DEFAULT NULL,
  p2_sleeping12m INT2 DEFAULT NULL,
  p2_parent12m INT2 DEFAULT NULL,
  p2_teething12m INT2 DEFAULT NULL,
  p2_educationAdvice12m varchar(50) DEFAULT NULL,
  p2_turnHead4m INT2 DEFAULT NULL,
  p2_laugh4m INT2 DEFAULT NULL,
  p2_headSteady4m INT2 DEFAULT NULL,
  p2_grasp4m INT2 DEFAULT NULL,
  p2_concern4m INT2 DEFAULT NULL,
  p2_development4m varchar(50) DEFAULT NULL,
  p2_follow6m INT2 DEFAULT NULL,
  p2_respond6m INT2 DEFAULT NULL,
  p2_babbles6m INT2 DEFAULT NULL,
  p2_rolls6m INT2 DEFAULT NULL,
  p2_sits6m INT2 DEFAULT NULL,
  p2_mouth6m INT2 DEFAULT NULL,
  p2_concern6m INT2 DEFAULT NULL,
  p2_development6m varchar(50) DEFAULT NULL,
  p2_looks9m INT2 DEFAULT NULL,
  p2_babbles9m INT2 DEFAULT NULL,
  p2_sits9m INT2 DEFAULT NULL,
  p2_stands9m INT2 DEFAULT NULL,
  p2_opposes9m INT2 DEFAULT NULL,
  p2_reaches9m INT2 DEFAULT NULL,
  p2_noParentsConcerns9m INT2 DEFAULT NULL,
  p2_development9m varchar(50) DEFAULT NULL,
  p2_understands12m INT2 DEFAULT NULL,
  p2_chatters12m INT2 DEFAULT NULL,
  p2_crawls12m INT2 DEFAULT NULL,
  p2_pulls12m INT2 DEFAULT NULL,
  p2_emotions12m INT2 DEFAULT NULL,
  p2_noParentConcerns12m INT2 DEFAULT NULL,
  p2_development12m varchar(50) DEFAULT NULL,
  p2_eyes4m INT2 DEFAULT NULL,
  p2_cover4m INT2 DEFAULT NULL,
  p2_hearing4m INT2 DEFAULT NULL,
  p2_babbling4m INT2 DEFAULT NULL,
  p2_hips4m INT2 DEFAULT NULL,
  p2_physical4m varchar(50) DEFAULT NULL,
  p2_fontanelles6m INT2 DEFAULT NULL,
  p2_eyes6m INT2 DEFAULT NULL,
  p2_cover6m INT2 DEFAULT NULL,
  p2_hearing6m INT2 DEFAULT NULL,
  p2_hips6m INT2 DEFAULT NULL,
  p2_physical6m varchar(50) DEFAULT NULL,
  p2_eyes9m INT2 DEFAULT NULL,
  p2_cover9m INT2 DEFAULT NULL,
  p2_hearing9m INT2 DEFAULT NULL,
  p2_physical9m varchar(50) DEFAULT NULL,
  p2_eyes12m INT2 DEFAULT NULL,
  p2_cover12m INT2 DEFAULT NULL,
  p2_hearing12m INT2 DEFAULT NULL,
  p2_hips12m INT2 DEFAULT NULL,
  p2_physical12m varchar(50) DEFAULT NULL,
  p2_problems4m varchar(50) DEFAULT NULL,
  p2_tb6m INT2 DEFAULT NULL,
  p2_problems6m varchar(50) DEFAULT NULL,
  p2_antiHbs9m INT2 DEFAULT NULL,
  p2_hgb9m INT2 DEFAULT NULL,
  p2_problems9m varchar(50) DEFAULT NULL,
  p2_hgb12m INT2 DEFAULT NULL,
  p2_serum12m INT2 DEFAULT NULL,
  p2_problems12m varchar(50) DEFAULT NULL,
  p2_hib4m INT2 DEFAULT NULL,
  p2_polio4m INT2 DEFAULT NULL,
  p2_immunization4m varchar(50) DEFAULT NULL,
  p2_hib6m INT2 DEFAULT NULL,
  p2_polio6m INT2 DEFAULT NULL,
  p2_hepB6m INT2 DEFAULT NULL,
  p2_immunization6m varchar(50) DEFAULT NULL,
  p2_tbSkin9m INT2 DEFAULT NULL,
  p2_immunization9m varchar(50) DEFAULT NULL,
  p2_mmr12m INT2 DEFAULT NULL,
  p2_varicella12m INT2 DEFAULT NULL,
  p2_immunization12m varchar(50) DEFAULT NULL,
  p2_signature4m varchar(50) DEFAULT NULL,
  p2_signature6m varchar(50) DEFAULT NULL,
  p2_signature9m varchar(50) DEFAULT NULL,
  p2_signature12m varchar(50) DEFAULT NULL,
  p3_date18m DATE DEFAULT NULL,
  p3_date2y DATE DEFAULT NULL,
  p3_date4y DATE DEFAULT NULL,
  p3_ht18m varchar(5) DEFAULT NULL,
  p3_wt18m varchar(5) DEFAULT NULL,
  p3_hc18m varchar(5) DEFAULT NULL,
  p3_ht2y varchar(5) DEFAULT NULL,
  p3_wt2y varchar(5) DEFAULT NULL,
  p3_ht4y varchar(5) DEFAULT NULL,
  p3_wt4y varchar(5) DEFAULT NULL,
  p3_pConcern18m text,
  p3_pConcern2y text,
  p3_pConcern4y text,
  p3_bottle18m INT2 DEFAULT NULL,
  p3_nutrition18m varchar(50) DEFAULT NULL,
  p3_milk2y INT2 DEFAULT NULL,
  p3_food2y INT2 DEFAULT NULL,
  p3_nutrition2y varchar(50) DEFAULT NULL,
  p3_milk4y INT2 DEFAULT NULL,
  p3_food4y INT2 DEFAULT NULL,
  p3_nutrition4y varchar(50) DEFAULT NULL,
  p3_bath18m INT2 DEFAULT NULL,
  p3_choking18m INT2 DEFAULT NULL,
  p3_temperment18m INT2 DEFAULT NULL,
  p3_limit18m INT2 DEFAULT NULL,
  p3_social18m INT2 DEFAULT NULL,
  p3_dental18m INT2 DEFAULT NULL,
  p3_toilet18m INT2 DEFAULT NULL,
  p3_educationAdvice18m varchar(50) DEFAULT NULL,
  p3_bike2y INT2 DEFAULT NULL,
  p3_matches2y INT2 DEFAULT NULL,
  p3_carbon2y INT2 DEFAULT NULL,
  p3_parent2y INT2 DEFAULT NULL,
  p3_social2y INT2 DEFAULT NULL,
  p3_dayCare2y INT2 DEFAULT NULL,
  p3_dental2y INT2 DEFAULT NULL,
  p3_toilet2y INT2 DEFAULT NULL,
  p3_educationAdvice2y varchar(50) DEFAULT NULL,
  p3_bike4y INT2 DEFAULT NULL,
  p3_matches4y INT2 DEFAULT NULL,
  p3_carbon4y INT2 DEFAULT NULL,
  p3_water4y INT2 DEFAULT NULL,
  p3_social4y INT2 DEFAULT NULL,
  p3_dental4y INT2 DEFAULT NULL,
  p3_school4y INT2 DEFAULT NULL,
  p3_educationAdvice4y varchar(50) DEFAULT NULL,
  p3_points18m INT2 DEFAULT NULL,
  p3_words18m INT2 DEFAULT NULL,
  p3_picks18m INT2 DEFAULT NULL,
  p3_walks18m INT2 DEFAULT NULL,
  p3_stacks18m INT2 DEFAULT NULL,
  p3_affection18m INT2 DEFAULT NULL,
  p3_showParents18m INT2 DEFAULT NULL,
  p3_looks18m INT2 DEFAULT NULL,
  p3_noParentsConcerns18m INT2 DEFAULT NULL,
  p3_development18m varchar(50) DEFAULT NULL,
  p3_word2y INT2 DEFAULT NULL,
  p3_sentence2y INT2 DEFAULT NULL,
  p3_run2y INT2 DEFAULT NULL,
  p3_container2y INT2 DEFAULT NULL,
  p3_copies2y INT2 DEFAULT NULL,
  p3_skills2y INT2 DEFAULT NULL,
  p3_noParentsConcerns2y INT2 DEFAULT NULL,
  p3_development2y varchar(50) DEFAULT NULL,
  p3_understands3y INT2 DEFAULT NULL,
  p3_twists3y INT2 DEFAULT NULL,
  p3_turnPages3y INT2 DEFAULT NULL,
  p3_share3y INT2 DEFAULT NULL,
  p3_listens3y INT2 DEFAULT NULL,
  p3_noParentsConcerns3y INT2 DEFAULT NULL,
  p3_development3y varchar(50) DEFAULT NULL,
  p3_understands4y INT2 DEFAULT NULL,
  p3_questions4y INT2 DEFAULT NULL,
  p3_oneFoot4y INT2 DEFAULT NULL,
  p3_draws4y INT2 DEFAULT NULL,
  p3_toilet4y INT2 DEFAULT NULL,
  p3_comfort4y INT2 DEFAULT NULL,
  p3_noParentsConcerns4y INT2 DEFAULT NULL,
  p3_development4y varchar(50) DEFAULT NULL,
  p3_counts5y INT2 DEFAULT NULL,
  p3_speaks5y INT2 DEFAULT NULL,
  p3_ball5y INT2 DEFAULT NULL,
  p3_hops5y INT2 DEFAULT NULL,
  p3_shares5y INT2 DEFAULT NULL,
  p3_alone5y INT2 DEFAULT NULL,
  p3_separate5y INT2 DEFAULT NULL,
  p3_noParentsConcerns5y INT2 DEFAULT NULL,
  p3_development5y varchar(50) DEFAULT NULL,
  p3_eyes18m INT2 DEFAULT NULL,
  p3_cover18m INT2 DEFAULT NULL,
  p3_hearing18m INT2 DEFAULT NULL,
  p3_physical18m varchar(50) DEFAULT NULL,
  p3_visual2y INT2 DEFAULT NULL,
  p3_cover2y INT2 DEFAULT NULL,
  p3_hearing2y INT2 DEFAULT NULL,
  p3_physical2y varchar(50) DEFAULT NULL,
  p3_visual4y INT2 DEFAULT NULL,
  p3_cover4y INT2 DEFAULT NULL,
  p3_hearing4y INT2 DEFAULT NULL,
  p3_blood4y INT2 DEFAULT NULL,
  p3_physical4y varchar(50) DEFAULT NULL,
  p3_problems18m varchar(50) DEFAULT NULL,
  p3_serum2y INT2 DEFAULT NULL,
  p3_problems2y varchar(50) DEFAULT NULL,
  p3_problems4y varchar(50) DEFAULT NULL,
  p3_hib18m INT2 DEFAULT NULL,
  p3_polio18m INT2 DEFAULT NULL,
  p3_immunization18m varchar(50) DEFAULT NULL,
  p3_immunization2y varchar(50) DEFAULT NULL,
  p3_mmr4y INT2 DEFAULT NULL,
  p3_polio4y INT2 DEFAULT NULL,
  p3_immunization4y varchar(50) DEFAULT NULL,
  p3_signature18m varchar(50) DEFAULT NULL,
  p3_signature2y varchar(50) DEFAULT NULL,
  p3_signature4y varchar(50) DEFAULT NULL,
  PRIMARY KEY (ID)

);

--
-- Sequences for table formSelfAdministered
--
CREATE SEQUENCE formselfadministered_numeric_seq;

--
-- Table structure for table formSelfAdministered
--
CREATE TABLE formSelfAdministered(
  ID numeric(10) NOT NULL  default nextval('formselfadministered_numeric_seq;'),
  demographic_no numeric(10) NOT NULL,
  provider_no numeric(10),
  formCreated date,
  formEdited timestamp NOT NULL,
  sex varchar(1),
  dob date,
  healthEx int2,
  healthVG int2,
  healthG int2,
  healthF int2,
  healthP int2,
  stayInHospNo int2,
  stayInHosp1 int2,
  stayInHosp2Or3 int2,
  stayInHospMore3 int2,
  visitPhyNo int2,
  visitPhy1 int2,
  visitPhy2Or3 int2,
  visitPhyMore3 int2,
  diabetesY int2,
  diabetesN int2,
  heartDiseaseY int2,
  heartDiseaseN int2,
  anginaPectorisY int2,
  anginaPectorisN int2,
  myocardialInfarctionY int2,
  myocardialInfarctionN int2,
  anyHeartAttackY int2,
  anyHeartAttackN int2,
  relativeTakeCareY int2,
  relativeTakeCareN int2,
  PRIMARY KEY  (ID)
);

--
-- Sequences for table FORMTYPE2DIABETES
--
CREATE SEQUENCE formtype2diabetes_numeric_se;

--
-- Table structure for table 'formType2Diabetes'
--
CREATE TABLE formType2Diabetes (
  ID numeric(10) DEFAULT nextval('formtype2diabetes_numeric_se'),
  demographic_no numeric(10) DEFAULT NULL,
  provider_no numeric(10) DEFAULT NULL,
  formCreated DATE DEFAULT NULL,
  formEdited timestamp(6) NOT NULL,
  pName varchar(60) DEFAULT NULL,
  birthDate DATE DEFAULT NULL,
  dateDx DATE DEFAULT NULL,
  height varchar(10) DEFAULT NULL,
  date1 DATE DEFAULT NULL,
  date2 DATE DEFAULT NULL,
  date3 DATE DEFAULT NULL,
  date4 DATE DEFAULT NULL,
  date5 DATE DEFAULT NULL,
  weight1 varchar(20) DEFAULT NULL,
  weight2 varchar(20) DEFAULT NULL,
  weight3 varchar(20) DEFAULT NULL,
  weight4 varchar(20) DEFAULT NULL,
  weight5 varchar(20) DEFAULT NULL,
  bp1 varchar(20) DEFAULT NULL,
  bp2 varchar(20) DEFAULT NULL,
  bp3 varchar(20) DEFAULT NULL,
  bp4 varchar(20) DEFAULT NULL,
  bp5 varchar(20) DEFAULT NULL,
  glucoseA1 varchar(50) DEFAULT NULL,
  glucoseA2 varchar(50) DEFAULT NULL,
  glucoseA3 varchar(50) DEFAULT NULL,
  glucoseA4 varchar(50) DEFAULT NULL,
  glucoseA5 varchar(50) DEFAULT NULL,
  glucoseB1 varchar(50) DEFAULT NULL,
  glucoseB2 varchar(50) DEFAULT NULL,
  glucoseB3 varchar(50) DEFAULT NULL,
  glucoseB4 varchar(50) DEFAULT NULL,
  glucoseB5 varchar(50) DEFAULT NULL,
  glucoseC1 varchar(50) DEFAULT NULL,
  glucoseC2 varchar(50) DEFAULT NULL,
  glucoseC3 varchar(50) DEFAULT NULL,
  glucoseC4 varchar(50) DEFAULT NULL,
  glucoseC5 varchar(50) DEFAULT NULL,
  renal1 varchar(50) DEFAULT NULL,
  renal2 varchar(50) DEFAULT NULL,
  renal3 varchar(50) DEFAULT NULL,
  renal4 varchar(50) DEFAULT NULL,
  renal5 varchar(50) DEFAULT NULL,
  urineRatio1 varchar(50) DEFAULT NULL,
  urineRatio2 varchar(50) DEFAULT NULL,
  urineRatio3 varchar(50) DEFAULT NULL,
  urineRatio4 varchar(50) DEFAULT NULL,
  urineRatio5 varchar(50) DEFAULT NULL,
  urineClearance1 varchar(100) DEFAULT NULL,
  urineClearance2 varchar(100) DEFAULT NULL,
  urineClearance3 varchar(100) DEFAULT NULL,
  urineClearance4 varchar(100) DEFAULT NULL,
  urineClearance5 varchar(100) DEFAULT NULL,
  lipidsA1 varchar(30) DEFAULT NULL,
  lipidsA2 varchar(30) DEFAULT NULL,
  lipidsA3 varchar(30) DEFAULT NULL,
  lipidsA4 varchar(30) DEFAULT NULL,
  lipidsA5 varchar(30) DEFAULT NULL,
  lipidsB1 varchar(30) DEFAULT NULL,
  lipidsB2 varchar(30) DEFAULT NULL,
  lipidsB3 varchar(30) DEFAULT NULL,
  lipidsB4 varchar(30) DEFAULT NULL,
  lipidsB5 varchar(30) DEFAULT NULL,
  lipidsC1 varchar(30) DEFAULT NULL,
  lipidsC2 varchar(30) DEFAULT NULL,
  lipidsC3 varchar(30) DEFAULT NULL,
  lipidsC4 varchar(30) DEFAULT NULL,
  lipidsC5 varchar(30) DEFAULT NULL,
  ophthamologist varchar(50) DEFAULT NULL,
  eyes1 varchar(50) DEFAULT NULL,
  eyes2 varchar(50) DEFAULT NULL,
  eyes3 varchar(50) DEFAULT NULL,
  eyes4 varchar(50) DEFAULT NULL,
  eyes5 varchar(50) DEFAULT NULL,
  feet1 varchar(100) DEFAULT NULL,
  feet2 varchar(100) DEFAULT NULL,
  feet3 varchar(100) DEFAULT NULL,
  feet4 varchar(100) DEFAULT NULL,
  feet5 varchar(100) DEFAULT NULL,
  metformin INT2 DEFAULT NULL,
  aceInhibitor INT2 DEFAULT NULL,
  glyburide INT2 DEFAULT NULL,
  asa INT2 DEFAULT NULL,
  otherOha INT2 DEFAULT NULL,
  otherBox7 varchar(20) DEFAULT NULL,
  insulin INT2 DEFAULT NULL,
  otherBox8 varchar(20) DEFAULT NULL,
  meds1 varchar(100) DEFAULT NULL,
  meds2 varchar(100) DEFAULT NULL,
  meds3 varchar(100) DEFAULT NULL,
  meds4 varchar(100) DEFAULT NULL,
  meds5 varchar(100) DEFAULT NULL,
  lifestyle1 varchar(50) DEFAULT NULL,
  lifestyle2 varchar(50) DEFAULT NULL,
  lifestyle3 varchar(50) DEFAULT NULL,
  lifestyle4 varchar(50) DEFAULT NULL,
  lifestyle5 varchar(50) DEFAULT NULL,
  exercise1 varchar(50) DEFAULT NULL,
  exercise2 varchar(50) DEFAULT NULL,
  exercise3 varchar(50) DEFAULT NULL,
  exercise4 varchar(50) DEFAULT NULL,
  exercise5 varchar(50) DEFAULT NULL,
  alcohol1 varchar(50) DEFAULT NULL,
  alcohol2 varchar(50) DEFAULT NULL,
  alcohol3 varchar(50) DEFAULT NULL,
  alcohol4 varchar(50) DEFAULT NULL,
  alcohol5 varchar(50) DEFAULT NULL,
  sexualFunction1 varchar(50) DEFAULT NULL,
  sexualFunction2 varchar(50) DEFAULT NULL,
  sexualFunction3 varchar(50) DEFAULT NULL,
  sexualFunction4 varchar(50) DEFAULT NULL,
  sexualFunction5 varchar(50) DEFAULT NULL,
  diet1 varchar(50) DEFAULT NULL,
  diet2 varchar(50) DEFAULT NULL,
  diet3 varchar(50) DEFAULT NULL,
  diet4 varchar(50) DEFAULT NULL,
  diet5 varchar(50) DEFAULT NULL,
  otherPlan1 varchar(100) DEFAULT NULL,
  otherPlan2 varchar(100) DEFAULT NULL,
  otherPlan3 varchar(100) DEFAULT NULL,
  otherPlan4 varchar(100) DEFAULT NULL,
  otherPlan5 varchar(100) DEFAULT NULL,
  consultant varchar(30) DEFAULT NULL,
  educator varchar(30) DEFAULT NULL,
  nutritionist varchar(30) DEFAULT NULL,
  cdn1 varchar(100) DEFAULT NULL,
  cdn2 varchar(100) DEFAULT NULL,
  cdn3 varchar(100) DEFAULT NULL,
  cdn4 varchar(100) DEFAULT NULL,
  cdn5 varchar(100) DEFAULT NULL,
  initials1 varchar(30) DEFAULT NULL,
  initials2 varchar(30) DEFAULT NULL,
  initials3 varchar(30) DEFAULT NULL,
  initials4 varchar(30) DEFAULT NULL,
  initials5 varchar(30) DEFAULT NULL,
  resource1 int2 default NULL,
  resource2 int2 default NULL,
  PRIMARY KEY (ID)
);

--
-- Table structure for table 'groupMembers_tbl'
--
CREATE TABLE groupMembers_tbl (
  groupID numeric(10) DEFAULT NULL,
  provider_No varchar(6) DEFAULT NULL
);

--
-- Sequences for table GROUPS_TBL
--
CREATE SEQUENCE groups_tbl_numeric_seq;

--
-- Table structure for table 'groups_tbl'
--
CREATE TABLE groups_tbl (
  groupID numeric(10) DEFAULT nextval('groups_tbl_numeric_seq'),
  parentID numeric(10) DEFAULT NULL,
  groupDesc varchar(50) DEFAULT NULL,
  PRIMARY KEY (groupID)

);

--
-- Table structure for table 'ichppccode'
--

CREATE TABLE ichppccode (
  ichppccode varchar(10) DEFAULT NULL,
  diagnostic_code varchar(10) DEFAULT NULL,
  description varchar(255) DEFAULT NULL
);

--
-- Table structure for table 'immunizations'
--



--
-- Sequences for table IMMUNIZATIONS
--

CREATE SEQUENCE immunizations_numeric_seq;

CREATE TABLE immunizations (
  ID numeric(11) DEFAULT nextval('immunizations_numeric_seq'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  provider_no varchar(6) NOT NULL DEFAULT '',
  immunizations text,
  save_date DATE NOT NULL DEFAULT '0001-01-01',
  archived INT2 NOT NULL DEFAULT '0',
  PRIMARY KEY (ID)

);



CREATE SEQUENCE mdsmsh_numeric_seq;
--
-- Table structure for table `mdsMSH`
--

CREATE TABLE mdsMSH (
  segmentID numeric(10) NOT NULL default nextval('mdsmsh_numeric_seq'),
  sendingApp char(180) default NULL,
  datetime timestamp NOT NULL default current_timestamp,
  type char(7) default NULL,
  messageConID char(20) default NULL,
  processingID char(3) default NULL,
  versionID char(8) default NULL,
  acceptAckType char(2) default NULL,
  appAckType char(2) default NULL,
  demographic_no numeric(10) default '0',
  PRIMARY KEY  (segmentID)
);


CREATE SEQUENCE mdsnte_numeric_seq;
--
-- Table structure for table `mdsNTE`
--

CREATE TABLE mdsNTE (
  segmentID numeric(10) default NULL,
  sourceOfComment varchar(8) default NULL,
  comment varchar(255) default NULL,
  associatedOBX numeric(10) default NULL
);


CREATE SEQUENCE mdsobr_numeric_seq;
--
-- Table structure for table `mdsOBR`
--
CREATE TABLE mdsOBR (
  segmentID numeric(10) default NULL,
  obrID numeric(10) default NULL,
  placerOrderNo char(75) default NULL,
  universalServiceID char(200) default NULL,
  observationTimestamp char(26) default NULL,
  specimenRecTimestamp char(26) default NULL,
  fillerFieldOne char(60) default NULL,
  quantityTiming char(200) default NULL
);


CREATE SEQUENCE mdsobx_numeric_seq;
--
-- Table structure for table `mdsOBX`
--
CREATE TABLE mdsOBX (
  segmentID numeric(10) default NULL,
  obxID numeric(10) default '0',
  valueType char(2) default NULL,
  observationIden varchar(80) default NULL,
  observationSubID varchar(255) default NULL,
  observationValue varchar(255) default NULL,
  abnormalFlags varchar(5) default NULL,
  observationResultStatus char(2) default NULL,
  producersID varchar(60) default NULL,
  associatedOBR numeric(10) default NULL
);


CREATE SEQUENCE mdspid_numeric_seq;
--
-- Table structure for table `mdsPID`
--
CREATE TABLE mdsPID (
  segmentID numeric(10) default NULL,
  intPatientID char(16) default NULL,
  altPatientID char(15) default NULL,
  patientName char(48) default NULL,
  dOB char(26) default NULL,
  sex char(1) default NULL,
  homePhone char(40) default NULL,
  healthNumber char(16) default NULL
);


CREATE SEQUENCE mdspv1_numeric_seq;
--
-- Table structure for table `mdsPV1`
--

CREATE TABLE mdsPV1 (
  segmentID numeric(10) default NULL,
  patientClass char(1) default NULL,
  patientLocation char(80) default NULL,
  refDoctor char(60) default NULL,
  conDoctor char(60) default NULL,
  admDoctor char(60) default NULL,
  vNumber char(20) default NULL,
  accStatus char(2) default NULL,
  admTimestamp char(26) default NULL
);


CREATE SEQUENCE mdszcl_numeric_seq;
--
-- Table structure for table `mdsZCL`
--
CREATE TABLE mdsZCL (
  segmentID numeric(10) default NULL,
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
  clientBakFax char(40) default NULL
);

CREATE SEQUENCE mdszct_numeric_seq;
--
-- Table structure for table `mdsZCT`
--
CREATE TABLE mdsZCT (
  segmentID numeric(10) default NULL,
  barCodeIdentifier char(14) default NULL,
  placerGroupNo char(14) default NULL,
  observationTimestamp char(26) default NULL
);

CREATE SEQUENCE mdszfr_numeric_seq;
--
-- Table structure for table `mdsZFR`
--
CREATE TABLE mdsZFR (
  segmentID numeric(10) default NULL,
  reportForm char(1) default NULL,
  reportFormStatus char(1) default NULL,
  testingLab varchar(5) default NULL,
  medicalDirector varchar(255) default NULL,
  editFlag varchar(255) default NULL,
  abnormalFlag varchar(255) default NULL
);

CREATE SEQUENCE mdszlb_numeric_seq;
--
-- Table structure for table `mdsZLB`
--
CREATE TABLE mdsZLB (
  segmentID numeric(10) default NULL,
  labID varchar(5) default NULL,
  labIDVersion varchar(255) default NULL,
  labAddress varchar(100) default NULL,
  primaryLab varchar(5) default NULL,
  primaryLabVersion varchar(5) default NULL,
  MDSLU varchar(5) default NULL,
  MDSLV varchar(5) default NULL
);

CREATE SEQUENCE mdszmc_numeric_seq;
--
-- Table structure for table `mdsZMC`
--
CREATE TABLE mdsZMC (
  segmentID numeric(10) default NULL,
  setID varchar(10) default NULL,
  messageCodeIdentifier varchar(10) default NULL,
  messageCodeVersion varchar(255) default NULL,
  noMessageCodeDescLines varchar(30) default NULL,
  sigFlag varchar(5) default NULL,
  messageCodeDesc varchar(255) default NULL
);


CREATE SEQUENCE mdszmn_numeric_seq;
--
-- Table structure for table `mdsZMN`
--
CREATE TABLE mdsZMN (
  segmentID numeric(10) default NULL,
  resultMnemonic varchar(20) default NULL,
  resultMnemonicVersion varchar(255) default NULL,
  reportName varchar(255) default NULL,
  units varchar(60) default NULL,
  cumulativeSequence varchar(255) default NULL,
  referenceRange varchar(255) default NULL,
  resultCode varchar(20) default NULL,
  reportForm varchar(255) default NULL,
  reportGroup varchar(10) default NULL,
  reportGroupVersion varchar(255) default NULL
);


CREATE SEQUENCE mdszrg_numeric_seq;
--
-- Table structure for table `mdsZRG`
--
CREATE TABLE mdsZRG (
  segmentID numeric(10) default NULL,
  reportSequence varchar(255) default NULL,
  reportGroupID varchar(10) default NULL,
  reportGroupVersion varchar(10) default NULL,
  reportFlags varchar(255) default NULL,
  reportGroupDesc varchar(30) default NULL,
  MDSIndex varchar(255) default NULL,
  reportGroupHeading varchar(255) default NULL
);


CREATE SEQUENCE measurements_numeric_seq;
--
-- Table structure for table `measurements`
--
CREATE TABLE measurements(
  id int  default nextval('measurements_numeric_seq'),
  type varchar(4) NOT NULL,
  demographicNo numeric(10) NOT NULL default '0', 
  providerNo varchar(6) NOT NULL default '',
  dataField  varchar(10) NOT NULL,
  measuringInstruction varchar(255) NOT NULL,  
  comments varchar(255) NOT NULL, 
  dateObserved timestamp NOT NULL, 
  dateEntered timestamp NOT NULL
);


CREATE SEQUENCE measurementcsslocation_numeric_seq;
--
-- Table structure for table `measurementCSSLocation`
--
CREATE TABLE measurementCSSLocation(
  cssID numeric(9) NOT NULL default nextval('measurementcsslocation_numeric_seq'),
  location varchar(255) NOT NULL,  
  PRIMARY KEY  (cssID) 
);

CREATE SEQUENCE measurementsdeleted_numeric_seq;
--
-- Table structure for table `measurementsDeleted`
--
CREATE TABLE measurementsDeleted(
  id int  default nextval('measurementsdeleted_numeric_seq'),
  type varchar(4) NOT NULL,
  demographicNo numeric(10) NOT NULL default '0', 
  providerNo varchar(6) NOT NULL default '',
  dataField  varchar(10) NOT NULL,
  measuringInstruction varchar(255) NOT NULL,  
  comments varchar(255) NOT NULL, 
  dateObserved timestamp NOT NULL, 
  dateEntered timestamp NOT NULL,
  dateDeleted timestamp NOT NULL,
  PRIMARY KEY(id)
);


CREATE SEQUENCE measurementgroup_numeric_seq;
--
-- Table structure for table `measurementGroup`
--
CREATE TABLE measurementGroup(
  name varchar(100) NOT NULL,
  typeDisplayName varchar(20) 
);


CREATE SEQUENCE measurementgroupstyle_numeric_seq;
--
-- Table structure for table `measurementGroupStyle`
--
CREATE TABLE measurementGroupStyle(
  groupID numeric(9) NOT NULL default nextval('measurementgroupstyle_numeric_seq'),
  groupName varchar(100) NOT NULL,
  cssID numeric(9) NOT NULL,
  PRIMARY KEY  (groupID) 
);



CREATE SEQUENCE measurementtype_numeric_seq;
--
-- Table structure for table `measurementType`
--
CREATE TABLE measurementType (
  id int  default nextval('measurementtype_numeric_seq'),
  type varchar(4) NOT NULL,
  typeDisplayName varchar(255) NOT NULL,
  typeDescription varchar(255) NOT NULL, 
  measuringInstruction varchar(255) NOT NULL, 
  validation varchar(100) NOT NULL,
  PRIMARY KEY(id)
);


CREATE SEQUENCE measurementtypedeleted_numeric_seq;
--
-- Table structure for table `measurementTypeDeleted`
--
CREATE TABLE measurementTypeDeleted (
  id int  default nextval('measurementtypedeleted_numeric_seq'),
  type varchar(4) NOT NULL,
  typeDisplayName varchar(20) NOT NULL,
  typeDescription varchar(255) NOT NULL, 
  measuringInstruction varchar(255) NOT NULL, 
  validation varchar(100) NOT NULL,
  dateDeleted timestamp NOT NULL,
  PRIMARY KEY(id)
);


--
-- Table structure for table 'messagelisttbl'
--

CREATE TABLE messagelisttbl (
  message INT4 DEFAULT NULL,
  provider_no varchar(6) DEFAULT NULL,
  status varchar(10) DEFAULT NULL,
  remoteLocation numeric(10) DEFAULT NULL
);

--
-- Table structure for table 'messagetbl'
--



--
-- Sequences for table MESSAGETBL
--

CREATE SEQUENCE messagetbl_int_seq;

CREATE TABLE messagetbl (
  messageid INT4 DEFAULT nextval('messagetbl_int_seq'),
  thedate DATE DEFAULT NULL,
  theime time DEFAULT NULL,
  themessage text,
  thesubject varchar(128) DEFAULT NULL,
  sentby varchar(62) DEFAULT NULL,
  sentto varchar(255) DEFAULT NULL,
  sentbyNo varchar(6) DEFAULT NULL,
  sentByLocation numeric(10) DEFAULT NULL,
  attachment text,
  actionstatus char(2) DEFAULT NULL,
  PRIMARY KEY (messageid)

);

--
-- Table structure for table 'mygroup'
--

CREATE TABLE mygroup (
  mygroup_no varchar(10) NOT NULL DEFAULT '',
  provider_no varchar(6) NOT NULL DEFAULT '',
  last_name varchar(30) NOT NULL DEFAULT '',
  first_name varchar(30) NOT NULL DEFAULT '',
  vieworder char(2) DEFAULT NULL,
  PRIMARY KEY (mygroup_no,provider_no)

);

--
-- Table structure for table 'oscarcommlocations'
--

CREATE TABLE oscarcommlocations (
  locationId numeric(10) NOT NULL DEFAULT '0',
  locationDesc varchar(50) NOT NULL DEFAULT '',
  locationAuth varchar(30) DEFAULT NULL,
  current INT2 NOT NULL DEFAULT '0',
  addressBook text,
  remoteServerURL varchar(30) DEFAULT NULL,
  PRIMARY KEY (locationId)
);

--
-- Table structure for table `patientLabRouting`
--

CREATE TABLE patientLabRouting (
  demographic_no numeric(10) NOT NULL default '0',
  lab_no numeric(10) NOT NULL default '0',
  PRIMARY KEY  (lab_no)
);


--
-- Sequences for table PREFERENCE
--

CREATE SEQUENCE preference_numeric_seq;

--
-- Table structure for table 'preference'
--

CREATE TABLE preference (
  preference_no numeric(6) DEFAULT nextval('preference_numeric_seq'),
  provider_no varchar(6) NOT NULL DEFAULT '',
  start_hour char(2) DEFAULT NULL,
  end_hour char(2) DEFAULT NULL,
  every_min char(3) DEFAULT NULL,
  mygroup_no varchar(10) DEFAULT NULL,
  color_template varchar(10) DEFAULT NULL,
  PRIMARY KEY (preference_no)
);


--
-- Sequences for table PRESCRIBE
--

CREATE SEQUENCE prescribe_numeric_seq;

--
-- Table structure for table 'prescribe'
--

CREATE TABLE prescribe (
  prescribe_no numeric(12) DEFAULT nextval('prescribe_numeric_seq'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  provider_no varchar(6) NOT NULL DEFAULT '',
  prescribe_date DATE NOT NULL DEFAULT '0001-01-01',
  prescribe_time time NOT NULL DEFAULT '00:00:00',
  content text,
  PRIMARY KEY (prescribe_no)

);


CREATE SEQUENCE prescription_numeric_sq;
--
-- Table structure for table `prescription`
--

CREATE TABLE prescription (
  script_no numeric(10) NOT NULL default nextval('prescription_numeric_sq'),
  provider_no varchar(6) default NULL,
  demographic_no numeric(10) default NULL,
  date_prescribed date default NULL,
  date_printed date default NULL,
  dates_reprinted text,
  textView text,
  PRIMARY KEY  (script_no)
);


--
-- Sequences for table PROFESSIONALSPECIALISTS
--
CREATE SEQUENCE professionalspecialists_nume;

--
-- Table structure for table 'professionalSpecialists'
--
CREATE TABLE professionalSpecialists (
  specId numeric(10) DEFAULT nextval('professionalspecialists_nume'),
  fName varchar(32) DEFAULT NULL,
  lName varchar(32) DEFAULT NULL,
  proLetters varchar(20) DEFAULT NULL,
  address varchar(255) DEFAULT NULL,
  phone varchar(30) DEFAULT NULL,
  fax varchar(30) DEFAULT NULL,
  website varchar(128) DEFAULT NULL,
  email varchar(128) DEFAULT NULL,
  specType varchar(128) DEFAULT NULL,
  PRIMARY KEY (specId)

);

--
-- Table structure for table 'property'
--

CREATE TABLE property (
  name varchar(255) NOT NULL DEFAULT '',
  value varchar(255) DEFAULT NULL,
  PRIMARY KEY (name)

);

--
-- Table structure for table 'provider'
--

CREATE TABLE provider (
  provider_no varchar(6) NOT NULL DEFAULT '',
  last_name varchar(30) NOT NULL DEFAULT '',
  first_name varchar(30) NOT NULL DEFAULT '',
  provider_type varchar(15) NOT NULL DEFAULT '',
  specialty varchar(20) NOT NULL DEFAULT '',
  team varchar(20) DEFAULT '',
  sex char(1) NOT NULL DEFAULT '',
  dob DATE DEFAULT NULL,
  address varchar(40) DEFAULT NULL,
  phone varchar(20) DEFAULT NULL,
  work_phone varchar(50) DEFAULT NULL,
  ohip_no varchar(20) DEFAULT NULL,
  rma_no varchar(20) DEFAULT NULL,
  billing_no varchar(20) DEFAULT NULL,
  hso_no varchar(10) DEFAULT NULL,
  status char(1) DEFAULT NULL,
  comments text,
  provider_activity CHAR(3) DEFAULT NULL,
  PRIMARY KEY (provider_no)

);

--
-- Table structure for table 'providerExt'
--

CREATE TABLE providerExt (
  provider_no varchar(6) DEFAULT NULL,
  signature varchar(255) DEFAULT NULL
);

--
-- Table structure for table `providerLabRouting`
--

CREATE TABLE providerLabRouting (
  provider_no varchar(6) NOT NULL default '',
  lab_no numeric(10) NOT NULL default '0',
  status char(1) default '',
  comment varchar(255) default '',
  timestamp timestamp NOT NULL,
  PRIMARY KEY  (provider_no,lab_no)
);

--
-- Sequences for table RADETAIL
--

CREATE SEQUENCE radetail_numeric_seq;

--
-- Table structure for table 'radetail'
--
CREATE TABLE radetail (
  radetail_no numeric(6) DEFAULT nextval('radetail_numeric_seq'),
  raheader_no numeric(6) NOT NULL DEFAULT '0',
  providerohip_no varchar(12) NOT NULL DEFAULT '',
  billing_no numeric(6) NOT NULL DEFAULT '0',
  service_code varchar(5) NOT NULL DEFAULT '',
  service_count char(2) NOT NULL DEFAULT '',
  hin varchar(12) NOT NULL DEFAULT '',
  amountclaim varchar(8) NOT NULL DEFAULT '',
  amountpay varchar(8) NOT NULL DEFAULT '',
  service_date varchar(12) NOT NULL DEFAULT '',
  error_code char(2) NOT NULL DEFAULT '',
  billtype char(3) NOT NULL DEFAULT '',
  PRIMARY KEY (radetail_no)

);

--
-- Sequences for table RAHEADER
--

CREATE SEQUENCE raheader_numeric_seq;

--
-- Table structure for table 'raheader'
--
CREATE TABLE raheader (
  raheader_no numeric(6) DEFAULT nextval('raheader_numeric_seq'),
  filename varchar(12) NOT NULL DEFAULT '',
  paymentdate varchar(8) NOT NULL DEFAULT '',
  payable varchar(30) NOT NULL DEFAULT '',
  totalamount varchar(10) NOT NULL DEFAULT '',
  records varchar(5) NOT NULL DEFAULT '',
  claims varchar(5) NOT NULL DEFAULT '',
  status char(1) NOT NULL DEFAULT '',
  readdate varchar(12) NOT NULL DEFAULT '',
  content text,
  PRIMARY KEY (raheader_no)

);

--
-- Table structure for table 'recycle_bin'
--

CREATE TABLE recycle_bin (
  provider_no varchar(6) NOT NULL DEFAULT '',
  table_name varchar(30) NOT NULL DEFAULT '',
  table_content text,
  updatedatetime TIMESTAMP DEFAULT NULL
);

--
-- Table structure for table 'recyclebin'
--



--
-- Sequences for table RECYCLEBIN
--

CREATE SEQUENCE recyclebin_numeric_seq;

CREATE TABLE recyclebin (
  recyclebin_no numeric(12) DEFAULT nextval('recyclebin_numeric_seq'),
  provider_no varchar(6) DEFAULT NULL,
  updatedatetime TIMESTAMP DEFAULT NULL,
  table_name varchar(30) DEFAULT NULL,
  keyword varchar(50) DEFAULT NULL,
  table_content text,
  PRIMARY KEY (recyclebin_no)

);

--
-- Table structure for table 'remoteAttachments'
--

CREATE TABLE remoteAttachments (
  demographic_no numeric(10) DEFAULT NULL,
  messageid INT4 DEFAULT NULL,
  savedBy varchar(255) DEFAULT NULL,
  date DATE DEFAULT NULL,
  time time DEFAULT NULL
);

--
-- Table structure for table 'reportagesex'
--

CREATE TABLE reportagesex (
  demographic_no numeric(10) DEFAULT NULL,
  age numeric(4) DEFAULT '0',
  roster varchar(4) DEFAULT '',
  sex char(2) DEFAULT '',
  provider_no varchar(6) DEFAULT NULL,
  reportdate DATE DEFAULT NULL,
  status char(2) DEFAULT '',
  date_joined DATE DEFAULT '0001-01-01'
);


CREATE SEQUENCE reportbyexamples_numeric_sq;
--
-- Table structure for table reportByExamples
--
CREATE TABLE reportByExamples(
  id numeric(9) default nextval('reportbyexamples_numeric_sq') ,
  providerNo varchar(6) NOT NULL,
  query text NOT NULL,
  date timestamp NOT NULL,
  PRIMARY KEY  (id)
);


CREATE SEQUENCE reportbyexamplesfavorite_numeric_sq;
--
-- Table structure for table reportByExamplesFavorite
--
CREATE TABLE reportByExamplesFavorite(
  id numeric(9) default nextval('reportbyexamplesfavorite_numeric_sq') ,
  providerNo varchar(6) NOT NULL,
  query text NOT NULL,  
  name varchar(255) NOT NULL,  
  PRIMARY KEY  (id)
);


--
-- Table structure for table 'reportprovider'
--
CREATE TABLE reportprovider (
  provider_no varchar(10) DEFAULT '',
  team varchar(10) DEFAULT '',
  action varchar(20) DEFAULT '',
  status char(1) DEFAULT ''
);

--
-- Table structure for table 'reporttemp'
--

CREATE TABLE reporttemp (
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  edb DATE NOT NULL DEFAULT '0001-01-01',
  demo_name varchar(60) NOT NULL DEFAULT '',
  provider_no varchar(6) DEFAULT NULL,
  address text,
  creator varchar(10) DEFAULT NULL,
  PRIMARY KEY (demographic_no,edb)

);

--
-- Table structure for table 'rschedule'
--

CREATE TABLE rschedule (
  provider_no varchar(6) NOT NULL DEFAULT '',
  sdate DATE NOT NULL DEFAULT '0001-01-01',
  edate DATE DEFAULT NULL,
  available char(1) NOT NULL DEFAULT '',
  day_of_week varchar(30) DEFAULT NULL,
  avail_hourB varchar(255) DEFAULT NULL,
  avail_hour varchar(230) DEFAULT NULL,
  creator varchar(50) DEFAULT NULL,
  PRIMARY KEY (provider_no,sdate,available)

);

--
-- Table structure for table 'scheduledate'
--

CREATE TABLE scheduledate (
  sdate DATE NOT NULL DEFAULT '0001-01-01',
  provider_no varchar(6) NOT NULL DEFAULT '',
  available char(1) NOT NULL DEFAULT '',
  priority char(1) DEFAULT NULL,
  reason varchar(255) DEFAULT NULL,
  hour varchar(255) DEFAULT NULL,
  creator varchar(50) DEFAULT NULL,
  PRIMARY KEY (sdate,provider_no)

);

--
-- Table structure for table 'scheduledaytemplate'
--

CREATE TABLE scheduledaytemplate (
  provider_no varchar(6) NOT NULL DEFAULT '',
  day DATE NOT NULL DEFAULT '0001-01-01',
  template_name varchar(20) DEFAULT NULL,
  PRIMARY KEY (provider_no,day)

);

--
-- Table structure for table 'scheduleholiday'
--

CREATE TABLE scheduleholiday (
  sdate DATE NOT NULL DEFAULT '0001-01-01',
  holiday_name varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (sdate)

);

--
-- Table structure for table 'scheduletemplate'
--

CREATE TABLE scheduletemplate (
  provider_no varchar(6) NOT NULL DEFAULT '',
  name varchar(20) NOT NULL DEFAULT '',
  summary varchar(80) DEFAULT NULL,
  timecode varchar(255) DEFAULT NULL,
  PRIMARY KEY (provider_no,name)

);

--
-- Table structure for table 'scheduletemplatecode'
--

CREATE TABLE scheduletemplatecode (
  code char(1) NOT NULL DEFAULT '',
  description varchar(80) DEFAULT NULL,
  duration char(3) DEFAULT '',
  color varchar(10) DEFAULT NULL
);

--
-- Table structure for table 'security'
--



--
-- Sequences for table SECURITY
--

CREATE SEQUENCE security_numeric_seq;

CREATE TABLE security (
  security_no numeric(6) DEFAULT nextval('security_numeric_seq'),
  user_name varchar(30) NOT NULL DEFAULT '',
  password varchar(80) NOT NULL DEFAULT '',
  provider_no varchar(6) DEFAULT NULL,
  pin varchar(6) DEFAULT NULL,
  PRIMARY KEY (security_no)

);

--
-- Table structure for table 'serviceSpecialists'
--

CREATE TABLE serviceSpecialists (
  serviceId numeric(10) DEFAULT NULL,
  specId numeric(10) DEFAULT NULL
);

--
-- Table structure for table 'specialistsJavascript'
--

CREATE TABLE specialistsJavascript (
  setId char(1) DEFAULT NULL,
  javascriptString text
);

--
-- Table structure for table 'study'
--



--
-- Sequences for table STUDY
--

CREATE SEQUENCE study_numeric_seq;

CREATE TABLE study (
  study_no numeric(3) DEFAULT nextval('study_numeric_seq'),
  study_name varchar(20) NOT NULL DEFAULT '',
  study_link varchar(255) NOT NULL DEFAULT '',
  description varchar(255) NOT NULL DEFAULT '',
  form_name varchar(30) DEFAULT NULL,
  current INT2 DEFAULT '0',
  remote_serverurl varchar(50) DEFAULT NULL,
  provider_no varchar(6) NOT NULL DEFAULT '',
  timestamp timestamp(6) NOT NULL,
  PRIMARY KEY (study_no)

);

--
-- Table structure for table 'studydata'
--



--
-- Sequences for table STUDYDATA
--

CREATE SEQUENCE studydata_numeric_seq;

CREATE TABLE studydata (
  studydata_no numeric(10) DEFAULT nextval('studydata_numeric_seq'),
  demographic_no numeric(10) NOT NULL DEFAULT '0',
  study_no numeric(3) NOT NULL DEFAULT '0',
  provider_no varchar(6) NOT NULL DEFAULT '',
  timestamp timestamp(6) NOT NULL,
  status varchar(30) DEFAULT NULL,
  content text,
  PRIMARY KEY (studydata_no)

);


CREATE SEQUENCE studylogin_numeric_sq;
--
-- Table structure for table `studylogin`
--
CREATE TABLE studylogin (
  id numeric(6) NOT NULL default nextval('studylogin_numeric_sq'),
  provider_no varchar(6) default NULL,
  study_no numeric(3) default NULL,
  remote_login_url varchar(100) default NULL,
  url_name_username varchar(20) NOT NULL default '',
  url_name_password varchar(20) NOT NULL default '',
  username varchar(30) NOT NULL default '',
  password varchar(100) NOT NULL default '',
  current int2 default NULL,
  creator varchar(6) NOT NULL default '',
  timestamp timestamp(6) NOT NULL,
  PRIMARY KEY  (id)
);


--
-- Sequences for table TICKLER
--
CREATE SEQUENCE tickler_numeric_seq;

--
-- Table structure for table 'tickler'
--
CREATE TABLE tickler (
  tickler_no numeric(10) DEFAULT nextval('tickler_numeric_seq'),
  demographic_no numeric(10) DEFAULT '0',
  message text,
  status char(1) DEFAULT NULL,
  update_date TIMESTAMP DEFAULT '0001-01-01 00:00:00',
  service_date DATE DEFAULT NULL,
  creator varchar(6) DEFAULT NULL,
  priority varchar(6) default 'Normal',
  task_assigned_to varchar(255),
  PRIMARY KEY (tickler_no)
);


--
-- Sequences for table TMPDIAGNOSTICCODE
--
CREATE SEQUENCE tmpdiagnosticcode_numeric_se;

--
-- Table structure for table 'tmpdiagnosticcode'
--
CREATE TABLE tmpdiagnosticcode (
  diagnosticcode_no numeric(5) DEFAULT nextval('tmpdiagnosticcode_numeric_se'),
  diagnostic_code varchar(5) NOT NULL DEFAULT '',
  description text,
  status char(1) DEFAULT NULL,
  PRIMARY KEY (diagnosticcode_no)
);

CREATE SEQUENCE validations_int_sq;
CREATE TABLE validations(
  id int default nextval('validations_int_sq'),
  name varchar(100) NOT NULL,
  regularExp varchar(100),
  maxValue double precision, 
  minValue double precision, 
  maxLength numeric(3), 
  minLength numeric(3), 
  isNumeric bool, 
  isTrue bool,
 PRIMARY KEY(id)
);

----
---- Indexes for table RECYCLEBIN
----
--
--CREATE INDEX keyword_recyclebin_index ON recyclebin (keyword);
--
----
---- Indexes for table EFORMS_DATA
----
--
--CREATE UNIQUE INDEX id_eforms_data_index ON eforms_data (fdid);
--
----
---- Indexes for table APPOINTMENT
----
--
--CREATE INDEX appointment_date_appointment_index ON appointment (appointment_date,start_time,demographic_no);
--CREATE INDEX demographic_no_appointment_index ON appointment (demographic_no);
--
----
---- Indexes for table EFORM_DATA
----
--
--CREATE UNIQUE INDEX id_eform_data_index ON eform_data (fdid);
--
----
---- Indexes for table SECURITY
----
--
--CREATE INDEX user_name_security_index ON security (user_name);
--
----
---- Indexes for table PREFERENCE
----
--
--CREATE INDEX provider_no_preference_index ON preference (provider_no);
--
----
---- Indexes for table SCHEDULETEMPLATECODE
----
--
--CREATE INDEX code_scheduletemplatecode_index ON scheduletemplatecode (code);
--
----
---- Indexes for table ENCOUNTERTEMPLATE
----
--
--CREATE INDEX encountertemplate_url_encountertemplate_index ON encountertemplate (createdatetime);
--
----
---- Indexes for table DEMOGRAPHICCUST
----
--
--CREATE INDEX cust1_demographiccust_index ON demographiccust (cust1);
--CREATE INDEX cust2_demographiccust_index ON demographiccust (cust2);
--CREATE INDEX cust3_demographiccust_index ON demographiccust (cust3);
--CREATE INDEX cust4_demographiccust_index ON demographiccust (cust4);
--
----
---- Indexes for table DEMOGRAPHIC
----
--
--CREATE INDEX hin_demographic_index ON demographic (hin);
--CREATE INDEX name_demographic_index ON demographic (last_name,first_name);
--
----
---- Indexes for table EFORM
----
--
--CREATE UNIQUE INDEX id_eform_index ON eform (fid);
--
----
---- Indexes for table EFORMS
----
--
--CREATE UNIQUE INDEX id_eforms_index ON eforms (fid);
--
----
---- Sequences for table BILLINGDETAIL
----
--
--SELECT SETVAL('billingdetail_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from billingdetail));
--
----
---- Sequences for table CTL_SPECIALINSTRUCTIONS
----
--
--SELECT SETVAL('ctl_specialinstructions_int_',(select case when max(INT)>0 then max(INT)+1 else 1 end from ctl_specialinstructions));
--
----
---- Sequences for table FORMMMSE
----
--
--SELECT SETVAL('formmmse_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from formMMSE));
--
----
---- Sequences for table EFORM_DATA
----
--
--SELECT SETVAL('eform_data_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from eform_data));
--
----
---- Sequences for table FORMAR
----
--
--SELECT SETVAL('formar_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from formAR));
--
----
---- Sequences for table DESANNUALREVIEWPLAN
----
--
--SELECT SETVAL('desannualreviewplan_numeric_',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from desannualreviewplan));
--
----
---- Sequences for table BILLING
----
--
--SELECT SETVAL('billing_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from billing));
--
----
---- Sequences for table FAVORITES
----
--
--SELECT SETVAL('favorites_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from favorites));
--
----
---- Sequences for table CONSULTATIONSERVICES
----
--
--SELECT SETVAL('consultationservices_numeric',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from consultationServices));
--
----
---- Sequences for table FORMROURKE
----
--
--SELECT SETVAL('formrourke_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from formRourke));
--
----
---- Sequences for table FORMPERIMENOPAUSAL
----
--
--SELECT SETVAL('formperimenopausal_numeric_s',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from formPeriMenopausal));
--
----
---- Sequences for table PRESCRIBE
----
--
--SELECT SETVAL('prescribe_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from prescribe));
--
----
---- Sequences for table EFORMS
----
--
--SELECT SETVAL('eforms_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from eforms));
--
----
---- Sequences for table RECYCLEBIN
----
--
--SELECT SETVAL('recyclebin_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from recyclebin));
--
----
---- Sequences for table TMPDIAGNOSTICCODE
----
--
--SELECT SETVAL('tmpdiagnosticcode_numeric_se',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from tmpdiagnosticcode));
--
----
---- Sequences for table TICKLER
----
--
--SELECT SETVAL('tickler_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from tickler));
--
----
---- Sequences for table PROFESSIONALSPECIALISTS
----
--
--SELECT SETVAL('professionalspecialists_nume',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from professionalSpecialists));
--
----
---- Sequences for table SECURITY
----
--
--SELECT SETVAL('security_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from security));
--
----
---- Sequences for table PREFERENCE
----
--
--SELECT SETVAL('preference_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from preference));
--
----
---- Sequences for table ECHART
----
--
--SELECT SETVAL('echart_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from eChart));
--
----
---- Sequences for table DISEASES
----
--
--SELECT SETVAL('diseases_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from diseases));
--
----
---- Sequences for table BILLINGSERVICE
----
--
--SELECT SETVAL('billingservice_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from billingservice));
--
----
---- Sequences for table DEMOGRAPHIC
----
--
--SELECT SETVAL('demographic_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from demographic));
--
----
---- Sequences for table EFORM
----
--
--SELECT SETVAL('eform_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from eform));
--
----
---- Sequences for table FAXCLIENTLOG
----
--
--SELECT SETVAL('faxclientlog_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from FaxClientLog));
--
----
---- Sequences for table STUDY
----
--
--SELECT SETVAL('study_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from study));
--
----
---- Sequences for table APPOINTMENT
----
--
--SELECT SETVAL('appointment_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from appointment));
--
----
---- Sequences for table ENCOUNTER
----
--
--SELECT SETVAL('encounter_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from encounter));
--
----
---- Sequences for table FORM
----
--
--SELECT SETVAL('form_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from form));
--
----
---- Sequences for table IMMUNIZATIONS
----
--
--SELECT SETVAL('immunizations_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from immunizations));
--
----
---- Sequences for table CLINIC
----
--
--SELECT SETVAL('clinic_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from clinic));
--
----
---- Sequences for table RAHEADER
----
--
--SELECT SETVAL('raheader_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from raheader));
--
----
---- Sequences for table ALLERGIES
----
--
--SELECT SETVAL('allergies_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from allergies));
--
----
---- Sequences for table DXRESEARCH
----
--
--SELECT SETVAL('dxresearch_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from dxresearch));
--
----
---- Sequences for table FORMMENTALHEALTH
----
--
--SELECT SETVAL('formmentalhealth_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from formMentalHealth));
--
----
---- Sequences for table FORMALPHA
----
--
--SELECT SETVAL('formalpha_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from formAlpha));
--
----
---- Sequences for table STUDYDATA
----
--
--SELECT SETVAL('studydata_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from studydata));
--
----
---- Sequences for table DRUGS
----
--
--SELECT SETVAL('drugs_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from drugs));
--
----
---- Sequences for table CONFIG_IMMUNIZATION
----
--
--SELECT SETVAL('config_immunization_numeric_',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from config_Immunization));
--
----
---- Sequences for table CTL_FREQUENCY
----
--
--SELECT SETVAL('ctl_frequency_int_seq',(select case when max(INT)>0 then max(INT)+1 else 1 end from ctl_frequency));
--
----
---- Sequences for table DOCUMENT
----
--
--SELECT SETVAL('document_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from document));
--
----
---- Sequences for table EFORMS_DATA
----
--
--SELECT SETVAL('eforms_data_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from eforms_data));
--
----
---- Sequences for table RADETAIL
----
--
--SELECT SETVAL('radetail_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from radetail));
--
----
---- Sequences for table FORMLABREQ
----
--
--SELECT SETVAL('formlabreq_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from formLabReq));
--
----
---- Sequences for table CONSULTATIONREQUESTS
----
--
--SELECT SETVAL('consultationrequests_numeric',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from consultationRequests));
--
----
---- Sequences for table MESSAGETBL
----
--
--SELECT SETVAL('messagetbl_int_seq',(select case when max(INT)>0 then max(INT)+1 else 1 end from messagetbl));
--
----
---- Sequences for table DIAGNOSTICCODE
----
--
--SELECT SETVAL('diagnosticcode_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from diagnosticcode));
--
----
---- Sequences for table DESAPRISK
----
--
--SELECT SETVAL('desaprisk_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from desaprisk));
--
----
---- Sequences for table BILLINGINR
----
--
--SELECT SETVAL('billinginr_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from billinginr));
--
----
---- Sequences for table GROUPS_TBL
----
--
--SELECT SETVAL('groups_tbl_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from groups_tbl));
--
----
---- Sequences for table FORMTYPE2DIABETES
----
--
--SELECT SETVAL('formtype2diabetes_numeric_se',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from formType2Diabetes));
--
----
---- Sequences for table FORMPALLIATIVECARE
----
--
--SELECT SETVAL('formpalliativecare_numeric_s',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from formPalliativeCare));
--
----
---- Sequences for table FORMANNUAL
----
--
--SELECT SETVAL('formannual_numeric_seq',(select case when max(numeric)>0 then max(numeric)+1 else 1 end from formAnnual));
--
COMMIT;

-- This function was created to simplify the compatibility MySQL - POSTGRES

CREATE FUNCTION CONCAT (varchar, varchar, varchar) returns varchar
  AS 'select $1 || $2 || $3;' LANGUAGE 'sql';

CREATE FUNCTION CONCAT (varchar, varchar, varchar, varchar, varchar, varchar, varchar) returns varchar
  AS 'select $1 || $2 || $3 || $4 || $5 || $6 || $7;' LANGUAGE 'sql';
