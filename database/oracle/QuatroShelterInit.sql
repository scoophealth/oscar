-----------------------------------------------
-- Export file for user QUATROSHELTER        --
-- Created by Tony on 2/28/2008, 11:03:06 AM --
-----------------------------------------------

spool allobjects.log

prompt
prompt Creating table ACCESS_TYPE
prompt ==========================
prompt
create table ACCESS_TYPE
(
  ACCESS_ID NUMBER(20) not null,
  NAME      VARCHAR2(255),
  TYPE      VARCHAR2(255)
)
;
alter table ACCESS_TYPE
  add primary key (ACCESS_ID);

prompt
prompt Creating table ADMISSION
prompt ========================
prompt
create table ADMISSION
(
  AM_ID                    NUMBER(11) not null,
  CLIENT_ID                NUMBER(11) default '0' not null,
  PROGRAM_ID               NUMBER(11) default '0' not null,
  PROVIDER_NO              VARCHAR2(6) default '0' not null,
  ADMISSION_DATE           DATE,
  ADMISSION_NOTES          VARCHAR2(4000),
  TEMP_ADMISSION           VARCHAR2(1),
  DISCHARGE_DATE           DATE,
  DISCHARGE_NOTES          VARCHAR2(4000),
  TEMP_ADMIT_DISCHARGE     VARCHAR2(1),
  ADMISSION_STATUS         VARCHAR2(24),
  TEAM_ID                  NUMBER(10),
  TEMPORARY_ADMISSION_FLAG NUMBER(1),
  RADIODISCHARGEREASON     VARCHAR2(10) default '0',
  CLIENTSTATUS_ID          NUMBER(20) default 0
)
;
alter table ADMISSION
  add primary key (AM_ID);
create index IDX_ADMISSION_1 on ADMISSION (TEAM_ID);

prompt
prompt Creating table AGENCY
prompt =====================
prompt
create table AGENCY
(
  ID                   NUMBER(20) default '0' not null,
  INTAKE_QUICK         NUMBER default 1 not null,
  INTAKE_QUICK_STATE   VARCHAR2(2) default 'HS' not null,
  INTAKE_INDEPTH       NUMBER default 2,
  INTAKE_INDEPTH_STATE VARCHAR2(2) default 'HS' not null,
  NAME                 VARCHAR2(50) default '' not null,
  DESCRIPTION          VARCHAR2(255),
  CONTACT_NAME         VARCHAR2(255),
  CONTACT_EMAIL        VARCHAR2(255),
  CONTACT_PHONE        VARCHAR2(255),
  LOCAL                NUMBER(1) default '0' not null,
  INTEGRATOR_ENABLED   NUMBER(1) default '0' not null,
  INTEGRATOR_URL       VARCHAR2(255),
  INTEGRATOR_JMS       VARCHAR2(255),
  INTEGRATOR_USERNAME  VARCHAR2(255),
  INTEGRATOR_PASSWORD  VARCHAR2(255),
  HIC                  NUMBER(1),
  SHARE_NOTES          NUMBER(1) default 0 not null
)
;
alter table AGENCY
  add primary key (ID);

prompt
prompt Creating table ALLERGIES
prompt ========================
prompt
create table ALLERGIES
(
  ALLERGYID            NUMBER(10) not null,
  DEMOGRAPHIC_NO       NUMBER(10) default '0' not null,
  ENTRY_DATE           DATE,
  DESCRIPTION          VARCHAR2(50) default '' not null,
  HICL_SEQNO           NUMBER(6),
  HIC_SEQNO            NUMBER(6),
  AGCSP                NUMBER(6),
  AGCCS                NUMBER(6),
  TYPECODE             NUMBER(4) default '0' not null,
  REACTION             VARCHAR2(4000),
  DRUGREF_ID           VARCHAR2(100),
  ARCHIVED             VARCHAR2(1) default '0',
  AGE_OF_ONSET         VARCHAR2(4) default '0',
  SEVERITY_OF_REACTION VARCHAR2(1) default '0',
  ONSET_OF_REACTION    VARCHAR2(1) default '0'
)
;
alter table ALLERGIES
  add primary key (ALLERGYID);

prompt
prompt Creating table APPOINTMENT
prompt ==========================
prompt
create table APPOINTMENT
(
  APPOINTMENT_NO   NUMBER(12) not null,
  PROVIDER_NO      VARCHAR2(6),
  APPOINTMENT_DATE DATE default '01-JAN-1900' not null,
  START_TIME       DATE default '01-JAN-1900' not null,
  END_TIME         DATE default '01-JAN-1900' not null,
  NAME             VARCHAR2(50),
  DEMOGRAPHIC_NO   NUMBER(10),
  NOTES            VARCHAR2(80),
  REASON           VARCHAR2(80),
  LOCATION         VARCHAR2(30),
  RESOURCES        VARCHAR2(10),
  TYPE             VARCHAR2(10),
  STYLE            VARCHAR2(10),
  BILLING          VARCHAR2(10),
  STATUS           VARCHAR2(2),
  CREATEDATETIME   DATE,
  UPDATEDATETIME   DATE,
  CREATOR          VARCHAR2(50),
  REMARKS          VARCHAR2(50)
)
;
alter table APPOINTMENT
  add primary key (APPOINTMENT_NO);
create index IDX_APPOINTMENT_1 on APPOINTMENT (APPOINTMENT_DATE, START_TIME, DEMOGRAPHIC_NO);
create index IDX_APPOINTMENT_2 on APPOINTMENT (DEMOGRAPHIC_NO);

prompt
prompt Creating table APP_LOOKUPTABLE
prompt ==============================
prompt
create table APP_LOOKUPTABLE
(
  TABLEID         VARCHAR2(32) not null,
  MODULEID        VARCHAR2(5),
  TABLE_NAME      VARCHAR2(32),
  DESCRIPTION     VARCHAR2(80),
  ISTREE          NUMBER(1),
  TREECODE_LENGTH NUMBER,
  ACTIVEYN        NUMBER(1)
)
;
alter table APP_LOOKUPTABLE
  add constraint PK_APP_LOOKUPTABLE primary key (TABLEID)
  deferrable;

prompt
prompt Creating table APP_LOOKUPTABLE_FIELDS
prompt =====================================
prompt
create table APP_LOOKUPTABLE_FIELDS
(
  TABLEID     VARCHAR2(10) not null,
  FIELDNAME   VARCHAR2(20) not null,
  FIELDDESC   VARCHAR2(40),
  EDITYN      VARCHAR2(1),
  FIELDTYPE   VARCHAR2(1),
  LOOKUPTABLE VARCHAR2(6),
  FIELDSQL    VARCHAR2(32),
  FIELDINDEX  NUMBER,
  UNIQUEYN    NUMBER
  genericidx  NUMBER,
  autoyn      NUMBER(1),
)
;
alter table APP_LOOKUPTABLE_FIELDS
  add constraint PRI_APP_LOOKUPTABLE_FIELDS primary key (TABLEID, FIELDNAME);

prompt
prompt Creating table APP_MODULE
prompt =========================
prompt
create table APP_MODULE
(
  MODULE_ID   NUMBER(10) not null,
  DESCRIPTION VARCHAR2(128) not null
)
;
alter table APP_MODULE
  add constraint PK_APP_MODULE primary key (MODULE_ID);

prompt
prompt Creating table BATCHELIGIBILITY
prompt ===============================
prompt
create table BATCHELIGIBILITY
(
  RESPONSECODE NUMBER(9) not null,
  MOHRESPONSE  VARCHAR2(100) not null,
  REASON       VARCHAR2(255) not null
)
;
alter table BATCHELIGIBILITY
  add primary key (RESPONSECODE);

prompt
prompt Creating table BED
prompt ==================
prompt
create table BED
(
  BED_ID      NUMBER(10) not null,
  BED_TYPE_ID NUMBER(10) default '1' not null,
  ROOM_ID     NUMBER(10),
  FACILITY_ID NUMBER(10) default 0 not null,
  ROOM_START  DATE not null,
  TEAM_ID     NUMBER(10),
  NAME        VARCHAR2(45),
  ACTIVE      NUMBER(1) default '1' not null
)
;
alter table BED
  add primary key (BED_ID);

prompt
prompt Creating table BED_CHECK_TIME
prompt =============================
prompt
create table BED_CHECK_TIME
(
  BED_CHECK_TIME_ID NUMBER(10) not null,
  PROGRAM_ID        NUMBER(10) not null,
  BED_CHECK_TIME    DATE not null
)
;
alter table BED_CHECK_TIME
  add primary key (BED_CHECK_TIME_ID);
create unique index IDX_BED_CHECK_TIME_1 on BED_CHECK_TIME (PROGRAM_ID, BED_CHECK_TIME);

prompt
prompt Creating table BED_DEMOGRAPHIC
prompt ==============================
prompt
create table BED_DEMOGRAPHIC
(
  BED_ID                    NUMBER(10) not null,
  DEMOGRAPHIC_NO            NUMBER(10) not null,
  BED_DEMOGRAPHIC_STATUS_ID NUMBER(10) default '1' not null,
  PROVIDER_NO               VARCHAR2(6) not null,
  LATE_PASS                 NUMBER(1) default '0' not null,
  RESERVATION_START         DATE not null,
  RESERVATION_END           DATE not null
)
;
alter table BED_DEMOGRAPHIC
  add primary key (BED_ID, DEMOGRAPHIC_NO);
create unique index IDX_BED_DEMOGRAPHIC_1 on BED_DEMOGRAPHIC (BED_ID);
create unique index IDX_BED_DEMOGRAPHIC_2 on BED_DEMOGRAPHIC (DEMOGRAPHIC_NO);

prompt
prompt Creating table BED_DEMOGRAPHIC_HISTORICAL
prompt =========================================
prompt
create table BED_DEMOGRAPHIC_HISTORICAL
(
  BED_ID         NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null,
  USAGE_START    DATE not null,
  USAGE_END      DATE not null
)
;
alter table BED_DEMOGRAPHIC_HISTORICAL
  add primary key (BED_ID, DEMOGRAPHIC_NO, USAGE_START);

prompt
prompt Creating table BED_DEMOGRAPHIC_STATUS
prompt =====================================
prompt
create table BED_DEMOGRAPHIC_STATUS
(
  BED_DEMOGRAPHIC_STATUS_ID NUMBER(10) not null,
  NAME                      VARCHAR2(45) not null,
  DURATION                  NUMBER(10) default '0' not null,
  DFLT                      NUMBER(1) default '0' not null
)
;
alter table BED_DEMOGRAPHIC_STATUS
  add primary key (BED_DEMOGRAPHIC_STATUS_ID);

prompt
prompt Creating table BED_TYPE
prompt =======================
prompt
create table BED_TYPE
(
  BED_TYPE_ID NUMBER(10) not null,
  NAME        VARCHAR2(45) not null,
  DFLT        NUMBER(1) default '0' not null
)
;
alter table BED_TYPE
  add primary key (BED_TYPE_ID);

prompt
prompt Creating table BILLACTIVITY
prompt ===========================
prompt
create table BILLACTIVITY
(
  MONTHCODE      VARCHAR2(1),
  BATCHCOUNT     NUMBER(3),
  HTMLFILENAME   VARCHAR2(50),
  OHIPFILENAME   VARCHAR2(50),
  PROVIDEROHIPNO VARCHAR2(6),
  GROUPNO        VARCHAR2(6),
  CREATOR        VARCHAR2(6),
  HTMLCONTEXT    VARCHAR2(4000),
  OHIPCONTEXT    VARCHAR2(4000),
  CLAIMRECORD    VARCHAR2(10),
  UPDATEDATETIME DATE,
  STATUS         VARCHAR2(1),
  TOTAL          VARCHAR2(20)
)
;

prompt
prompt Creating table BILLCENTER
prompt =========================
prompt
create table BILLCENTER
(
  BILLCENTER_CODE VARCHAR2(2) default '' not null,
  BILLCENTER_DESC VARCHAR2(20)
)
;

prompt
prompt Creating table BILLING
prompt ======================
prompt
create table BILLING
(
  BILLING_NO             NUMBER(10) not null,
  CLINIC_NO              NUMBER(10) default '0' not null,
  DEMOGRAPHIC_NO         NUMBER(10) default '0' not null,
  PROVIDER_NO            VARCHAR2(6),
  APPOINTMENT_NO         NUMBER(12),
  ORGANIZATION_SPEC_CODE VARCHAR2(6),
  DEMOGRAPHIC_NAME       VARCHAR2(60),
  HIN                    VARCHAR2(12),
  UPDATE_DATE            DATE,
  UPDATE_TIME            DATE,
  BILLING_DATE           DATE,
  BILLING_TIME           DATE,
  CLINIC_REF_CODE        VARCHAR2(10),
  CONTENT                VARCHAR2(4000),
  TOTAL                  VARCHAR2(6),
  STATUS                 VARCHAR2(1),
  DOB                    VARCHAR2(8),
  VISITDATE              DATE,
  VISITTYPE              VARCHAR2(2),
  PROVIDER_OHIP_NO       VARCHAR2(20),
  PROVIDER_RMA_NO        VARCHAR2(20),
  APPTPROVIDER_NO        VARCHAR2(6),
  ASSTPROVIDER_NO        VARCHAR2(6),
  CREATOR                VARCHAR2(6)
)
;
alter table BILLING
  add primary key (BILLING_NO);
create index IDX_BILLING_1 on BILLING (APPOINTMENT_NO, DEMOGRAPHIC_NO);
create index IDX_BILLING_2 on BILLING (DEMOGRAPHIC_NO);
create index IDX_BILLING_3 on BILLING (BILLING_DATE);
create index IDX_BILLING_4 on BILLING (PROVIDER_NO);
create index IDX_BILLING_5 on BILLING (PROVIDER_OHIP_NO);
create index IDX_BILLING_6 on BILLING (APPTPROVIDER_NO);
create index IDX_BILLING_7 on BILLING (CREATOR);
create index IDX_BILLING_8 on BILLING (STATUS);

prompt
prompt Creating table BILLINGDETAIL
prompt ============================
prompt
create table BILLINGDETAIL
(
  BILLING_DT_NO    NUMBER(10) not null,
  BILLING_NO       NUMBER(10) default '0' not null,
  SERVICE_CODE     VARCHAR2(5),
  SERVICE_DESC     VARCHAR2(255),
  BILLING_AMOUNT   VARCHAR2(6),
  DIAGNOSTIC_CODE  VARCHAR2(3),
  APPOINTMENT_DATE DATE,
  STATUS           VARCHAR2(1),
  BILLINGUNIT      VARCHAR2(2)
)
;
alter table BILLINGDETAIL
  add primary key (BILLING_DT_NO);
create index IDX_BILLINGDETAIL_1 on BILLINGDETAIL (BILLING_NO);

prompt
prompt Creating table BILLINGINR
prompt =========================
prompt
create table BILLINGINR
(
  BILLINGINR_NO    NUMBER(10) not null,
  DEMOGRAPHIC_NO   NUMBER(10) default '0' not null,
  DEMOGRAPHIC_NAME VARCHAR2(60) not null,
  HIN              VARCHAR2(12),
  DOB              VARCHAR2(8),
  PROVIDER_NO      NUMBER(10),
  PROVIDER_OHIP_NO VARCHAR2(20),
  PROVIDER_RMA_NO  VARCHAR2(20),
  CREATOR          VARCHAR2(6),
  DIAGNOSTIC_CODE  VARCHAR2(3),
  SERVICE_CODE     VARCHAR2(6),
  SERVICE_DESC     VARCHAR2(255),
  BILLING_AMOUNT   VARCHAR2(6),
  BILLING_UNIT     VARCHAR2(1),
  CREATEDATETIME   DATE default '01-JAN-1900' not null,
  STATUS           VARCHAR2(1)
)
;
alter table BILLINGINR
  add primary key (BILLINGINR_NO);

prompt
prompt Creating table BILLINGPERCLIMIT
prompt ===============================
prompt
create table BILLINGPERCLIMIT
(
  SERVICE_CODE VARCHAR2(10) not null,
  MIN          VARCHAR2(8) default '0',
  MAX          VARCHAR2(8) default '0'
)
;
alter table BILLINGPERCLIMIT
  add primary key (SERVICE_CODE);

prompt
prompt Creating table BILLINGREFERRAL
prompt ==============================
prompt
create table BILLINGREFERRAL
(
  BILLINGREFERRAL_NO NUMBER(10) not null,
  REFERRAL_NO        VARCHAR2(6) default '000000' not null,
  LAST_NAME          VARCHAR2(30),
  FIRST_NAME         VARCHAR2(30),
  SPECIALTY          VARCHAR2(30),
  ADDRESS1           VARCHAR2(50),
  ADDRESS2           VARCHAR2(50),
  CITY               VARCHAR2(30),
  PROVINCE           VARCHAR2(30),
  COUNTRY            VARCHAR2(30),
  POSTAL             VARCHAR2(10),
  PHONE              VARCHAR2(16),
  FAX                VARCHAR2(16)
)
;
alter table BILLINGREFERRAL
  add primary key (BILLINGREFERRAL_NO);
create unique index IDX_BILLINGREFERRAL_1 on BILLINGREFERRAL (REFERRAL_NO);

prompt
prompt Creating table BILLINGSERVICE
prompt =============================
prompt
create table BILLINGSERVICE
(
  BILLINGSERVICE_NO     NUMBER(10) not null,
  SERVICE_COMPOSITECODE VARCHAR2(30),
  SERVICE_CODE          VARCHAR2(10),
  DESCRIPTION           VARCHAR2(4000),
  VALUE                 VARCHAR2(8),
  PERCENTAGE            VARCHAR2(8),
  BILLINGSERVICE_DATE   DATE,
  SPECIALTY             VARCHAR2(15),
  REGION                VARCHAR2(5),
  ANAESTHESIA           VARCHAR2(2)
)
;
alter table BILLINGSERVICE
  add primary key (BILLINGSERVICE_NO);
create index IDX_BILLINGSERVICE_1 on BILLINGSERVICE (SERVICE_CODE);

prompt
prompt Creating table BILLING_ON_3RDPARTYADDRESS
prompt =========================================
prompt
create table BILLING_ON_3RDPARTYADDRESS
(
  ID           NUMBER(6) not null,
  ATTENTION    VARCHAR2(100) not null,
  COMPANY_NAME VARCHAR2(100) not null,
  ADDRESS      VARCHAR2(200) default '' not null,
  CITY         VARCHAR2(200) default '' not null,
  PROVINCE     VARCHAR2(10) default '' not null,
  POSTCODE     VARCHAR2(10) default '' not null,
  TELEPHONE    VARCHAR2(15) default '' not null,
  FAX          VARCHAR2(15) default '' not null
)
;
alter table BILLING_ON_3RDPARTYADDRESS
  add primary key (ID);

prompt
prompt Creating table BILLING_ON_CHEADER1
prompt ==================================
prompt
create table BILLING_ON_CHEADER1
(
  ID               NUMBER(12) not null,
  HEADER_ID        NUMBER(11) not null,
  TRANSC_ID        VARCHAR2(2) default 'HE',
  REC_ID           VARCHAR2(1) default 'H',
  HIN              VARCHAR2(10),
  VER              VARCHAR2(2) default '  ',
  DOB              VARCHAR2(8),
  PAY_PROGRAM      VARCHAR2(3) default 'HCP',
  PAYEE            VARCHAR2(1) default 'P',
  REF_NUM          VARCHAR2(6),
  FACILTY_NUM      VARCHAR2(4),
  ADMISSION_DATE   VARCHAR2(10),
  REF_LAB_NUM      VARCHAR2(4),
  MAN_REVIEW       VARCHAR2(1),
  LOCATION         VARCHAR2(4),
  DEMOGRAPHIC_NO   NUMBER(10) default '0' not null,
  PROVIDER_NO      VARCHAR2(6),
  APPOINTMENT_NO   NUMBER(12),
  DEMOGRAPHIC_NAME VARCHAR2(20),
  SEX              VARCHAR2(1) default '1',
  PROVINCE         VARCHAR2(2) default 'ON',
  BILLING_DATE     DATE,
  BILLING_TIME     DATE,
  TOTAL            VARCHAR2(7),
  PAID             VARCHAR2(7),
  STATUS           VARCHAR2(1),
  COMMENT1         VARCHAR2(255),
  VISITTYPE        VARCHAR2(2),
  PROVIDER_OHIP_NO VARCHAR2(20),
  PROVIDER_RMA_NO  VARCHAR2(20),
  APPTPROVIDER_NO  VARCHAR2(6),
  ASSTPROVIDER_NO  VARCHAR2(6),
  CREATOR          VARCHAR2(6),
  TIMESTAMP1       TIMESTAMP(6)
)
;
alter table BILLING_ON_CHEADER1
  add primary key (ID);
create index IDX_BILLING_ON_CHEADER1_1 on BILLING_ON_CHEADER1 (APPOINTMENT_NO, DEMOGRAPHIC_NO);

prompt
prompt Creating table BILLING_ON_CHEADER2
prompt ==================================
prompt
create table BILLING_ON_CHEADER2
(
  ID         NUMBER(12) not null,
  CH1_ID     NUMBER(12) not null,
  TRANSC_ID  VARCHAR2(2) default 'HE',
  REC_ID     VARCHAR2(1) default 'R',
  HIN        VARCHAR2(12),
  LAST_NAME  VARCHAR2(9) default '  ',
  FIRST_NAME VARCHAR2(5),
  SEX        VARCHAR2(1) default '1',
  PROVINCE   VARCHAR2(2) default 'AB',
  TIMESTAMP  TIMESTAMP(6)
)
;
alter table BILLING_ON_CHEADER2
  add primary key (ID);
create index IDX_BILLING_ON_CHEADER2_1 on BILLING_ON_CHEADER2 (CH1_ID);

prompt
prompt Creating table BILLING_ON_DISKNAME
prompt ==================================
prompt
create table BILLING_ON_DISKNAME
(
  ID             NUMBER(11) not null,
  MONTHCODE      VARCHAR2(1),
  BATCHCOUNT     NUMBER(3),
  OHIPFILENAME   VARCHAR2(50),
  GROUPNO        VARCHAR2(6),
  CREATOR        VARCHAR2(6),
  CLAIMRECORD    VARCHAR2(10),
  CREATEDATETIME DATE,
  STATUS         VARCHAR2(1),
  TOTAL          VARCHAR2(20),
  TIMESTAMP      TIMESTAMP(6)
)
;
alter table BILLING_ON_DISKNAME
  add primary key (ID);

prompt
prompt Creating table BILLING_ON_ERRORCODE
prompt ===================================
prompt
create table BILLING_ON_ERRORCODE
(
  CODE        VARCHAR2(5) not null,
  DESCRIPTION VARCHAR2(255) not null
)
;
alter table BILLING_ON_ERRORCODE
  add primary key (CODE);

prompt
prompt Creating table BILLING_ON_EXT
prompt =============================
prompt
create table BILLING_ON_EXT
(
  ID             NUMBER(12) not null,
  BILLING_NO     NUMBER(6),
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  KEY_VAL        VARCHAR2(50),
  VALUE          VARCHAR2(255),
  DATE_TIME      DATE,
  STATUS         VARCHAR2(1) default '1'
)
;
alter table BILLING_ON_EXT
  add primary key (ID);
create index IDX_BILLING_ON_EXT_1 on BILLING_ON_EXT (KEY_VAL);
create index IDX_BILLING_ON_EXT_2 on BILLING_ON_EXT (BILLING_NO);

prompt
prompt Creating table BILLING_ON_FAVOURITE
prompt ===================================
prompt
create table BILLING_ON_FAVOURITE
(
  ID          NUMBER(12) not null,
  NAME        VARCHAR2(60),
  SERVICE_DX  VARCHAR2(255),
  PROVIDER_NO VARCHAR2(6),
  TIMESTAMP   TIMESTAMP(6),
  DELETED     VARCHAR2(1) default 'N' not null
)
;
alter table BILLING_ON_FAVOURITE
  add primary key (ID);

prompt
prompt Creating table BILLING_ON_FILENAME
prompt ==================================
prompt
create table BILLING_ON_FILENAME
(
  ID             NUMBER(11) not null,
  DISK_ID        NUMBER(11) not null,
  HTMLFILENAME   VARCHAR2(50),
  PROVIDEROHIPNO VARCHAR2(6),
  PROVIDERNO     VARCHAR2(6) not null,
  CLAIMRECORD    VARCHAR2(10),
  STATUS         VARCHAR2(1),
  TOTAL          VARCHAR2(20),
  TIMESTAMP      TIMESTAMP(6)
)
;
alter table BILLING_ON_FILENAME
  add primary key (ID);

prompt
prompt Creating table BILLING_ON_ITEM
prompt ==============================
prompt
create table BILLING_ON_ITEM
(
  ID           NUMBER(12) not null,
  CH1_ID       NUMBER(12) not null,
  TRANSC_ID    VARCHAR2(2) default 'HE',
  REC_ID       VARCHAR2(1) default 'T',
  SERVICE_CODE VARCHAR2(20),
  FEE          VARCHAR2(7),
  SER_NUM      VARCHAR2(2) default '01',
  SERVICE_DATE DATE,
  DX           VARCHAR2(4),
  DX1          VARCHAR2(4),
  DX2          VARCHAR2(4),
  STATUS       VARCHAR2(1),
  TIMESTAMP    TIMESTAMP(6)
)
;
alter table BILLING_ON_ITEM
  add primary key (ID);
create index IDX_BILLING_ON_ITEM_1 on BILLING_ON_ITEM (CH1_ID);

prompt
prompt Creating table BILLING_ON_REPO
prompt ==============================
prompt
create table BILLING_ON_REPO
(
  ID             NUMBER(11) not null,
  H_ID           NUMBER(11) not null,
  CATEGORY       VARCHAR2(60),
  CONTENT        VARCHAR2(4000),
  CREATEDATETIME DATE
)
;
alter table BILLING_ON_REPO
  add primary key (ID);

prompt
prompt Creating table BILLING_PAYMENT_TYPE
prompt ===================================
prompt
create table BILLING_PAYMENT_TYPE
(
  ID           NUMBER(11) not null,
  PAYMENT_TYPE VARCHAR2(25) not null
)
;
alter table BILLING_PAYMENT_TYPE
  add primary key (ID);

prompt
prompt Creating table BILLING_PRIVATE_SERVICE
prompt ======================================
prompt
create table BILLING_PRIVATE_SERVICE
(
  ID           NUMBER(6) not null,
  SERVICE_CODE VARCHAR2(10),
  DESCRIPTION  VARCHAR2(255),
  VALUE        VARCHAR2(8),
  PERCENTAGE   VARCHAR2(8),
  UPDATE_DATE  DATE
)
;
alter table BILLING_PRIVATE_SERVICE
  add primary key (ID);

prompt
prompt Creating table CAISI_EDITOR
prompt ===========================
prompt
create table CAISI_EDITOR
(
  ID         NUMBER(11) not null,
  CATEGORY   VARCHAR2(50),
  LABEL      VARCHAR2(255),
  TYPE       VARCHAR2(20),
  LABELVALUE VARCHAR2(255),
  LABELCODE  VARCHAR2(50),
  HORIZONTAL VARCHAR2(3),
  ISACTIVE   VARCHAR2(3)
)
;
alter table CAISI_EDITOR
  add primary key (ID);

prompt
prompt Creating table CAISI_FORM
prompt =========================
prompt
create table CAISI_FORM
(
  FORM_ID     NUMBER(20) not null,
  DESCRIPTION VARCHAR2(255),
  SURVEYDATA  CLOB,
  STATUS      NUMBER(6),
  VERSION     NUMBER(20) default '0'
)
;
alter table CAISI_FORM
  add primary key (FORM_ID);

prompt
prompt Creating table CAISI_FORM_DATA
prompt ==============================
prompt
create table CAISI_FORM_DATA
(
  ID          NUMBER(20) not null,
  INSTANCE_ID NUMBER(20),
  PAGE_NUMBER NUMBER(20),
  SECTION_ID  NUMBER(20),
  QUESTION_ID NUMBER(20),
  VALUE       VARCHAR2(4000),
  DATA_KEY    VARCHAR2(255)
)
;
alter table CAISI_FORM_DATA
  add primary key (ID);
create index IDX_CAISI_FORM_DATA_1 on CAISI_FORM_DATA (INSTANCE_ID);

prompt
prompt Creating table CAISI_FORM_DATA_TMPSAVE
prompt ======================================
prompt
create table CAISI_FORM_DATA_TMPSAVE
(
  TMP_FORM_DATA_ID NUMBER(20) not null,
  TMP_INSTANCE_ID  NUMBER(20),
  PAGE_NUMBER      NUMBER(20),
  SECTION_ID       NUMBER(20),
  QUESTION_ID      NUMBER(20),
  VALUE            CLOB,
  DATA_KEY         VARCHAR2(255)
)
;
alter table CAISI_FORM_DATA_TMPSAVE
  add primary key (TMP_FORM_DATA_ID);
create index IDX_CAISI_FORM_DATA_TMPSAVE_1 on CAISI_FORM_DATA_TMPSAVE (TMP_INSTANCE_ID);

prompt
prompt Creating table CAISI_FORM_INSTANCE
prompt ==================================
prompt
create table CAISI_FORM_INSTANCE
(
  ID           NUMBER(20) not null,
  FORM_ID      NUMBER(20),
  DESCRIPTION  VARCHAR2(255),
  DATE_CREATED DATE,
  USER_ID      NUMBER(20),
  USERNAME     VARCHAR2(255),
  CLIENT_ID    NUMBER(20)
)
;
alter table CAISI_FORM_INSTANCE
  add primary key (ID);

prompt
prompt Creating table CAISI_FORM_INSTANCE_TMPSAVE
prompt ==========================================
prompt
create table CAISI_FORM_INSTANCE_TMPSAVE
(
  TMP_INSTANCE_ID NUMBER(20) not null,
  INSTANCE_ID     NUMBER(20) not null,
  FORM_ID         NUMBER(20),
  DESCRIPTION     VARCHAR2(255),
  DATE_CREATED    DATE,
  USER_ID         NUMBER(20),
  USERNAME        VARCHAR2(255),
  CLIENT_ID       NUMBER(20)
)
;
alter table CAISI_FORM_INSTANCE_TMPSAVE
  add primary key (TMP_INSTANCE_ID);

prompt
prompt Creating table CAISI_FORM_QUESTION
prompt ==================================
prompt
create table CAISI_FORM_QUESTION
(
  ID               NUMBER(20) not null,
  PAGE             NUMBER(20),
  SECTION          NUMBER(20),
  QUESTION         NUMBER(20),
  DESCRIPTION      VARCHAR2(255),
  FORM_ID          NUMBER(20),
  FORM_QUESTION_ID NUMBER(20),
  TYPE             VARCHAR2(255)
)
;
alter table CAISI_FORM_QUESTION
  add primary key (ID);

prompt
prompt Creating table CAISI_ROLE
prompt =========================
prompt
create table CAISI_ROLE
(
  ROLE_ID     NUMBER(10) not null,
  NAME        VARCHAR2(255),
  OSCAR_NAME  VARCHAR2(255),
  UPDATE_DATE DATE default '01-JAN-1900' not null,
  USERDEFINED NUMBER(1) not null
)
;
alter table CAISI_ROLE
  add primary key (ROLE_ID);

prompt
prompt Creating table CASEMGMT_CPP
prompt ===========================
prompt
create table CASEMGMT_CPP
(
  ID                  NUMBER(10) not null,
  DEMOGRAPHIC_NO      VARCHAR2(10) not null,
  PROVIDER_NO         VARCHAR2(6) not null,
  SOCIALHISTORY       VARCHAR2(4000),
  FAMILYHISTORY       VARCHAR2(4000),
  MEDICALHISTORY      VARCHAR2(4000),
  ONGOINGCONCERNS     VARCHAR2(4000),
  REMINDERS           VARCHAR2(4000),
  UPDATE_DATE         TIMESTAMP(6) default CURRENT_TIMESTAMP not null,
  PRIMARYPHYSICIAN    VARCHAR2(255),
  PRIMARYCOUNSELLOR   VARCHAR2(255),
  OTHERFILENUMBER     VARCHAR2(100),
  OTHERSUPPORTSYSTEMS VARCHAR2(4000),
  PASTMEDICATIONS     VARCHAR2(4000)
)
;
alter table CASEMGMT_CPP
  add primary key (ID);

prompt
prompt Creating table CASEMGMT_ISSUE
prompt =============================
prompt
create table CASEMGMT_ISSUE
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO VARCHAR2(20),
  ISSUE_ID       NUMBER(10) default '0' not null,
  ACUTE          NUMBER(1) default '0' not null,
  CERTAIN        NUMBER(1) default '0' not null,
  MAJOR          NUMBER(1) default '0' not null,
  RESOLVED       NUMBER(1) default '0' not null,
  TYPE           VARCHAR2(100),
  UPDATE_DATE    DATE default '01-JAN-1900' not null
)
;
alter table CASEMGMT_ISSUE
  add primary key (ID);
create index IDX_CASEMGMT_ISSUE_1 on CASEMGMT_ISSUE (ISSUE_ID);

prompt
prompt Creating table CASEMGMT_ISSUE_NOTES
prompt ===================================
prompt
create table CASEMGMT_ISSUE_NOTES
(
  ID      NUMBER(10) default '0' not null,
  NOTE_ID NUMBER(10) default '0' not null
)
;
alter table CASEMGMT_ISSUE_NOTES
  add primary key (ID, NOTE_ID);
create index IDX_CASEMGMT_ISSUE_NOTES_1 on CASEMGMT_ISSUE_NOTES (ID);
create index IDX_CASEMGMT_ISSUE_NOTES_2 on CASEMGMT_ISSUE_NOTES (NOTE_ID);

prompt
prompt Creating table CASEMGMT_NOTE
prompt ============================
prompt
create table CASEMGMT_NOTE
(
  NOTE_ID               NUMBER(10) not null,
  UPDATE_DATE           DATE default '01-JAN-1900' not null,
  OBSERVATION_DATE      DATE default '01-JAN-1900' not null,
  DEMOGRAPHIC_NO        VARCHAR2(20),
  PROVIDER_NO           VARCHAR2(20),
  NOTE                  VARCHAR2(4000) not null,
  SIGNED                NUMBER(1) default '0' not null,
  INCLUDE_ISSUE_INNOTE  NUMBER(1) default '0' not null,
  SIGNING_PROVIDER_NO   VARCHAR2(20),
  ENCOUNTER_TYPE        VARCHAR2(100),
  BILLING_CODE          VARCHAR2(100),
  PROGRAM_NO            VARCHAR2(20),
  REPORTER_CAISI_ROLE   VARCHAR2(20),
  REPORTER_PROGRAM_TEAM VARCHAR2(20),
  HISTORY               VARCHAR2(4000) not null,
  PASSWORD              VARCHAR2(255),
  LOCKED                VARCHAR2(1),
  UUID                  VARCHAR2(36)
)
;
alter table CASEMGMT_NOTE
  add primary key (NOTE_ID);
create index IDX_CASEMGMT_NOTE_1 on CASEMGMT_NOTE (PROVIDER_NO);
create index IDX_CASEMGMT_NOTE_2 on CASEMGMT_NOTE (DEMOGRAPHIC_NO);
create index IDX_CASEMGMT_NOTE_3 on CASEMGMT_NOTE (UUID);

prompt
prompt Creating table CASEMGMT_TMPSAVE
prompt ===============================
prompt
create table CASEMGMT_TMPSAVE
(
  ID             NUMBER(20) not null,
  DEMOGRAPHIC_NO NUMBER(20),
  PROVIDER_NO    VARCHAR2(255),
  PROGRAM_ID     NUMBER(20),
  NOTE           VARCHAR2(4000),
  UPDATE_DATE    DATE,
  NOTE_ID        NUMBER(10)
)
;
alter table CASEMGMT_TMPSAVE
  add primary key (ID);

prompt
prompt Creating table CLIENT_IMAGE
prompt ===========================
prompt
create table CLIENT_IMAGE
(
  IMAGE_ID       NUMBER(20) not null,
  DEMOGRAPHIC_NO NUMBER(11),
  IMAGE_TYPE     VARCHAR2(255),
  IMAGE_DATA     VARCHAR2(4000),
  UPDATE_DATE    DATE
)
;
alter table CLIENT_IMAGE
  add primary key (IMAGE_ID);

prompt
prompt Creating table CLIENT_REFERRAL
prompt ==============================
prompt
create table CLIENT_REFERRAL
(
  REFERRAL_ID              NUMBER(20) not null,
  CLIENT_ID                NUMBER(20) default '0' not null,
  REFERRAL_DATE            DATE,
  PROVIDER_NO              NUMBER(20) default '0' not null,
  NOTES                    VARCHAR2(4000),
  PROGRAM_ID               NUMBER(20) default '0' not null,
  STATUS                   VARCHAR2(30),
  COMPLETION_NOTES         VARCHAR2(4000),
  TEMPORARY_ADMISSION_FLAG NUMBER(1),
  COMPLETION_DATE          DATE,
  PRESENT_PROBLEMS         VARCHAR2(4000),
  RADIOREJECTIONREASON     VARCHAR2(10) default '0'
)
;
alter table CLIENT_REFERRAL
  add primary key (REFERRAL_ID);

prompt
prompt Creating table CLINIC
prompt =====================
prompt
create table CLINIC
(
  CLINIC_NO            NUMBER(10) not null,
  CLINIC_NAME          VARCHAR2(50),
  CLINIC_ADDRESS       VARCHAR2(60),
  CLINIC_CITY          VARCHAR2(40),
  CLINIC_POSTAL        VARCHAR2(15),
  CLINIC_PHONE         VARCHAR2(50),
  CLINIC_FAX           VARCHAR2(20),
  CLINIC_LOCATION_CODE VARCHAR2(10),
  STATUS               VARCHAR2(1),
  CLINIC_PROVINCE      VARCHAR2(40),
  CLINIC_DELIM_PHONE   VARCHAR2(4000),
  CLINIC_DELIM_FAX     VARCHAR2(4000)
)
;
alter table CLINIC
  add primary key (CLINIC_NO);

prompt
prompt Creating table CLINIC_LOCATION
prompt ==============================
prompt
create table CLINIC_LOCATION
(
  CLINIC_LOCATION_NO   VARCHAR2(15) not null,
  CLINIC_NO            NUMBER(10) default '0' not null,
  CLINIC_LOCATION_NAME VARCHAR2(40)
)
;

prompt
prompt Creating table CONFIG_IMMUNIZATION
prompt ==================================
prompt
create table CONFIG_IMMUNIZATION
(
  SETID      NUMBER(10) not null,
  SETNAME    VARCHAR2(255),
  SETXMLDOC  VARCHAR2(4000),
  CREATEDATE DATE,
  PROVIDERNO VARCHAR2(6),
  ARCHIVED   NUMBER(1) default '0' not null
)
;
alter table CONFIG_IMMUNIZATION
  add primary key (SETID);

prompt
prompt Creating table CONSENT
prompt ======================
prompt
create table CONSENT
(
  ID                    NUMBER(20) not null,
  DEMOGRAPHIC_NO        NUMBER(20) default '0' not null,
  PROVIDER_NO           VARCHAR2(255) not null,
  PROVIDER_NAME         VARCHAR2(255),
  DATE_SIGNED           DATE,
  ANSWER_1              NUMBER(11),
  ANSWER_2              NUMBER(11),
  ANSWER_3              NUMBER(11),
  STATUS                VARCHAR2(255),
  HARDCOPY              NUMBER(1),
  LOCATION              VARCHAR2(255),
  FORM_NAME             VARCHAR2(255),
  FORM_VERSION          VARCHAR2(255),
  SIGNATURE_DECLARATION NUMBER(1),
  EXCLUSIONS            VARCHAR2(255),
  REFUSED               NUMBER(1)
)
;
alter table CONSENT
  add primary key (ID);

prompt
prompt Creating table CONSENT_INTERVIEW
prompt ================================
prompt
create table CONSENT_INTERVIEW
(
  ID                  NUMBER(20) not null,
  CONSENT_ID          NUMBER(20) default '0' not null,
  DEMOGRAPHIC_NO      NUMBER(20) default '0' not null,
  PROVIDER_NO         VARCHAR2(255) not null,
  FORM_NAME           VARCHAR2(255),
  FORM_VERSION        VARCHAR2(255),
  LANGUAGE            VARCHAR2(255),
  LANGUAGE_OTHER      VARCHAR2(255),
  LANGUAGE_READ       VARCHAR2(255),
  LANGUAGE_READ_OTHER VARCHAR2(255),
  EDUCATION           VARCHAR2(255),
  REVIEW              VARCHAR2(255),
  REVIEW_OTHER        VARCHAR2(255),
  PRESSURE            VARCHAR2(255),
  PRESSURE_OTHER      VARCHAR2(255),
  INFORMATION         VARCHAR2(255),
  INFORMATION_OTHER   VARCHAR2(255),
  FOLLOWUP            VARCHAR2(255),
  FOLLOWUP_OTHER      VARCHAR2(255),
  COMMENTS            VARCHAR2(255),
  COMMENTS_OTHER      VARCHAR2(255)
)
;
alter table CONSENT_INTERVIEW
  add primary key (ID);

prompt
prompt Creating table CONSULTATIONREQUESTS
prompt ===================================
prompt
create table CONSULTATIONREQUESTS
(
  REFERALDATE        DATE,
  SERVICEID          NUMBER(10),
  SPECID             NUMBER(10),
  APPOINTMENTDATE    DATE,
  APPOINTMENTTIME    DATE,
  REASON             VARCHAR2(4000),
  CLINICALINFO       VARCHAR2(4000),
  CURRENTMEDS        VARCHAR2(4000),
  ALLERGIES          VARCHAR2(4000),
  PROVIDERNO         VARCHAR2(6),
  DEMOGRAPHICNO      NUMBER(10),
  STATUS             VARCHAR2(2),
  STATUSTEXT         VARCHAR2(4000),
  SENDTO             VARCHAR2(20),
  REQUESTID          NUMBER(10) not null,
  CONCURRENTPROBLEMS VARCHAR2(4000),
  URGENCY            VARCHAR2(2),
  PATIENTWILLBOOK    NUMBER(1)
)
;
alter table CONSULTATIONREQUESTS
  add primary key (REQUESTID);

prompt
prompt Creating table CONSULTATIONSERVICES
prompt ===================================
prompt
create table CONSULTATIONSERVICES
(
  SERVICEID   NUMBER(10) not null,
  SERVICEDESC VARCHAR2(255),
  ACTIVE      VARCHAR2(2)
)
;
alter table CONSULTATIONSERVICES
  add primary key (SERVICEID);

prompt
prompt Creating table CONSULTDOCS
prompt ==========================
prompt
create table CONSULTDOCS
(
  ID          NUMBER(10) not null,
  REQUESTID   NUMBER(10) not null,
  DOCUMENT_NO NUMBER(10) not null,
  DOCTYPE     VARCHAR2(1) not null,
  DELETED     VARCHAR2(1),
  ATTACH_DATE DATE,
  PROVIDER_NO VARCHAR2(6) not null
)
;
alter table CONSULTDOCS
  add primary key (ID);

prompt
prompt Creating table CR_CERT
prompt ======================
prompt
create table CR_CERT
(
  CERT_ID                     VARCHAR2(37) default ' ' not null,
  USER_SPECIFIC               NUMBER(1),
  STATIC_IP                   NUMBER(1),
  UNASSIGNED                  NUMBER(1),
  IP                          VARCHAR2(15),
  USER_ID                     VARCHAR2(64),
  MACHINE_ID                  VARCHAR2(37),
  VERIFICATION_NEEDED         NUMBER(1),
  USAGE_TIMES_BEFORE_REVERIFY NUMBER(11),
  POLICY_ID                   VARCHAR2(37),
  CREATED_TIMESTAMP           DATE,
  LAST_CHANGED                DATE,
  SIGNATURE                   NUMBER(20)
)
;
alter table CR_CERT
  add primary key (CERT_ID);

prompt
prompt Creating table CR_MACHINE
prompt =========================
prompt
create table CR_MACHINE
(
  MACHINE_ID   VARCHAR2(37) default ' ' not null,
  IP           VARCHAR2(15),
  MACHINE_NAME VARCHAR2(255)
)
;
alter table CR_MACHINE
  add primary key (MACHINE_ID);

prompt
prompt Creating table CR_POLICY
prompt ========================
prompt
create table CR_POLICY
(
  POLICY_ID                   VARCHAR2(37) default ' ' not null,
  STATIC_IP                   NUMBER(1),
  IP                          VARCHAR2(15),
  REMOTE_ACCESS               NUMBER(1),
  GENERATE_SUPER_CERTS        NUMBER(1),
  ADMINISTRATE_POLICIES       NUMBER(1),
  ADMINISTRATE_QUESTIONS      NUMBER(1),
  REMOVE_BANS                 NUMBER(1),
  USER_ID                     VARCHAR2(64),
  ROLE_ID                     VARCHAR2(37),
  PRIORITY                    NUMBER(11),
  USAGE_TIMES_BEFORE_REVERIFY NUMBER(11),
  MAX_TIME_BETWEEN_USAGE      NUMBER(11),
  EXPIRE_COOKIE               NUMBER(11),
  IP_FILTER                   VARCHAR2(128),
  CERTS_MAX                   NUMBER(11),
  CERTS_CURRENT               NUMBER(11),
  DEFAULT_ANSWER              VARCHAR2(16)
)
;
alter table CR_POLICY
  add primary key (POLICY_ID);

prompt
prompt Creating table CR_SECURITYQUESTION
prompt ==================================
prompt
create table CR_SECURITYQUESTION
(
  QUESTION_ID VARCHAR2(37) default ' ' not null,
  USER_ID     VARCHAR2(128),
  QUESTION    VARCHAR2(255),
  ANSWER      VARCHAR2(255)
)
;
alter table CR_SECURITYQUESTION
  add primary key (QUESTION_ID);

prompt
prompt Creating table CR_USER
prompt ======================
prompt
create table CR_USER
(
  USER_ID          VARCHAR2(64) default ' ' not null,
  PASSWORD_DIGEST  VARCHAR2(128),
  DISABLED         NUMBER(1),
  LOCKEDOUT        NUMBER(1),
  PASSWORD_EXPIRED NUMBER(1)
)
;
alter table CR_USER
  add primary key (USER_ID);

prompt
prompt Creating table CR_USERROLE
prompt ==========================
prompt
create table CR_USERROLE
(
  USER_ID   VARCHAR2(64) default ' ' not null,
  USER_ROLE VARCHAR2(64) default ' ' not null
)
;
alter table CR_USERROLE
  add primary key (USER_ID, USER_ROLE);

prompt
prompt Creating table CTL_BILLINGSERVICE
prompt =================================
prompt
create table CTL_BILLINGSERVICE
(
  SERVICETYPE_NAME   VARCHAR2(150),
  SERVICETYPE        VARCHAR2(10),
  SERVICE_CODE       VARCHAR2(10),
  SERVICE_GROUP_NAME VARCHAR2(20),
  SERVICE_GROUP      VARCHAR2(20),
  STATUS             VARCHAR2(1),
  SERVICE_ORDER      NUMBER(4)
)
;

prompt
prompt Creating table CTL_BILLINGSERVICE_PREMIUM
prompt =========================================
prompt
create table CTL_BILLINGSERVICE_PREMIUM
(
  SERVICETYPE_NAME VARCHAR2(150),
  SERVICE_CODE     VARCHAR2(10),
  STATUS           VARCHAR2(1),
  UPDATE_DATE      DATE
)
;

prompt
prompt Creating table CTL_BILLINGTYPE
prompt ==============================
prompt
create table CTL_BILLINGTYPE
(
  SERVICETYPE VARCHAR2(10) not null,
  BILLTYPE    VARCHAR2(5) not null
)
;
alter table CTL_BILLINGTYPE
  add primary key (SERVICETYPE);

prompt
prompt Creating table CTL_DIAGCODE
prompt ===========================
prompt
create table CTL_DIAGCODE
(
  SERVICETYPE     VARCHAR2(10),
  DIAGNOSTIC_CODE VARCHAR2(5),
  STATUS          VARCHAR2(1)
)
;

prompt
prompt Creating table CTL_DOCTYPE
prompt ==========================
prompt
create table CTL_DOCTYPE
(
  MODULE  VARCHAR2(30),
  DOCTYPE VARCHAR2(60),
  STATUS  VARCHAR2(1)
)
;

prompt
prompt Creating table CTL_DOCUMENT
prompt ===========================
prompt
create table CTL_DOCUMENT
(
  MODULE      VARCHAR2(30),
  MODULE_ID   NUMBER(12) default '0' not null,
  DOCUMENT_NO NUMBER(6) default '0' not null,
  STATUS      VARCHAR2(1)
)
;

prompt
prompt Creating table CTL_FREQUENCY
prompt ============================
prompt
create table CTL_FREQUENCY
(
  FREQID   NUMBER(4) not null,
  FREQCODE VARCHAR2(8),
  DAILYMIN VARCHAR2(5) default '0' not null,
  DAILYMAX VARCHAR2(5) default '0' not null
)
;
alter table CTL_FREQUENCY
  add primary key (FREQID);

prompt
prompt Creating table CTL_PROVIDER
prompt ===========================
prompt
create table CTL_PROVIDER
(
  CLINIC_NO   NUMBER(10) default '0' not null,
  PROVIDER_NO NUMBER(10) default '0' not null,
  STATUS      VARCHAR2(1)
)
;

prompt
prompt Creating table CTL_SPECIALINSTRUCTIONS
prompt ======================================
prompt
create table CTL_SPECIALINSTRUCTIONS
(
  ID          NUMBER(4) not null,
  DESCRIPTION VARCHAR2(50)
)
;
alter table CTL_SPECIALINSTRUCTIONS
  add primary key (ID);

prompt
prompt Creating table CUSTOM_FILTER
prompt ============================
prompt
create table CUSTOM_FILTER
(
  ID             NUMBER(10) not null,
  PROVIDER_NO    VARCHAR2(6) default ' ' not null,
  START_DATE     DATE default '01-JAN-1900' not null,
  END_DATE       DATE default '01-JAN-1900' not null,
  STATUS         VARCHAR2(1) default ' ',
  PRIORITY       VARCHAR2(20) default ' ',
  DEMOGRAPHIC_NO VARCHAR2(20) default ' ',
  NAME           VARCHAR2(255) default ' ',
  SHORTCUT       NUMBER default 0
)
;
alter table CUSTOM_FILTER
  add primary key (ID);

prompt
prompt Creating table CUSTOM_FILTER_ASSIGNEES
prompt ======================================
prompt
create table CUSTOM_FILTER_ASSIGNEES
(
  FILTER_ID   NUMBER(10) default '0' not null,
  PROVIDER_NO VARCHAR2(6) default ' ' not null
)
;

prompt
prompt Creating table CUSTOM_FILTER_PROVIDERS
prompt ======================================
prompt
create table CUSTOM_FILTER_PROVIDERS
(
  FILTER_ID   NUMBER(10) default '0' not null,
  PROVIDER_NO VARCHAR2(6) default ' ' not null
)
;

prompt
prompt Creating table DEFAULT_ROLE_ACCESS
prompt ==================================
prompt
create table DEFAULT_ROLE_ACCESS
(
  ID        NUMBER(20) not null,
  ROLE_ID   NUMBER(11) default '0' not null,
  ACCESS_ID NUMBER(20) default '0' not null
)
;
alter table DEFAULT_ROLE_ACCESS
  add primary key (ID);

prompt
prompt Creating table DEMOGRAPHIC
prompt ==========================
prompt
create table DEMOGRAPHIC
(
  DEMOGRAPHIC_NO  NUMBER(10) not null,
  LAST_NAME       VARCHAR2(30),
  FIRST_NAME      VARCHAR2(30),
  ADDRESS         VARCHAR2(60),
  CITY            VARCHAR2(20),
  PROVINCE        VARCHAR2(20),
  POSTAL          VARCHAR2(9),
  PHONE           VARCHAR2(20),
  PHONE2          VARCHAR2(20),
  EMAIL           VARCHAR2(100),
  PIN             VARCHAR2(255),
  YEAR_OF_BIRTH   VARCHAR2(4),
  MONTH_OF_BIRTH  VARCHAR2(2),
  DATE_OF_BIRTH   VARCHAR2(2),
  HIN             VARCHAR2(20),
  VER             VARCHAR2(3),
  ROSTER_STATUS   VARCHAR2(20),
  PATIENT_STATUS  VARCHAR2(20),
  DATE_JOINED     DATE,
  CHART_NO        VARCHAR2(10),
  PROVIDER_NO     VARCHAR2(250),
  SEX             VARCHAR2(1),
  END_DATE        DATE,
  EFF_DATE        DATE,
  PCN_INDICATOR   VARCHAR2(20),
  HC_TYPE         VARCHAR2(20),
  HC_RENEW_DATE   DATE,
  FAMILY_DOCTOR   VARCHAR2(80),
  ALIAS           VARCHAR2(70),
  PREVIOUSADDRESS VARCHAR2(255),
  CHILDREN        VARCHAR2(255),
  SOURCEOFINCOME  VARCHAR2(255),
  CITIZENSHIP     VARCHAR2(40),
  SIN             VARCHAR2(15)
)
;
alter table DEMOGRAPHIC
  add primary key (DEMOGRAPHIC_NO);
create index IDX_DEMOGRAPHIC_1 on DEMOGRAPHIC (HIN);
create index IDX_DEMOGRAPHIC_2 on DEMOGRAPHIC (LAST_NAME, FIRST_NAME);

prompt
prompt Creating table DEMOGRAPHICACCESSORY
prompt ===================================
prompt
create table DEMOGRAPHICACCESSORY
(
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  CONTENT        CLOB
)
;
alter table DEMOGRAPHICACCESSORY
  add primary key (DEMOGRAPHIC_NO);

prompt
prompt Creating table DEMOGRAPHICCUST
prompt ==============================
prompt
create table DEMOGRAPHICCUST
(
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  CUST1          VARCHAR2(255),
  CUST2          VARCHAR2(255),
  CUST3          VARCHAR2(255),
  CUST4          VARCHAR2(255),
  CONTENT        CLOB
)
;
alter table DEMOGRAPHICCUST
  add primary key (DEMOGRAPHIC_NO);
create index IDX_DEMOGRAPHICCUST_1 on DEMOGRAPHICCUST (CUST1);
create index IDX_DEMOGRAPHICCUST_2 on DEMOGRAPHICCUST (CUST2);
create index IDX_DEMOGRAPHICCUST_3 on DEMOGRAPHICCUST (CUST3);
create index IDX_DEMOGRAPHICCUST_4 on DEMOGRAPHICCUST (CUST4);

prompt
prompt Creating table DEMOGRAPHICEXT
prompt =============================
prompt
create table DEMOGRAPHICEXT
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10),
  PROVIDER_NO    VARCHAR2(6),
  KEY_VAL        VARCHAR2(64),
  VALUE          CLOB,
  DATE_TIME      DATE,
  HIDDEN         VARCHAR2(1) default '0'
)
;
alter table DEMOGRAPHICEXT
  add primary key (ID);
create index IDX_DEMOGRAPHICEXT_1 on DEMOGRAPHICEXT (DEMOGRAPHIC_NO);

prompt
prompt Creating table DEMOGRAPHICPHARMACY
prompt ==================================
prompt
create table DEMOGRAPHICPHARMACY
(
  PHARMACYID     NUMBER(10),
  DEMOGRAPHIC_NO NUMBER(10),
  STATUS         VARCHAR2(1) default '1',
  ADDDATE        TIMESTAMP(6)
)
;

prompt
prompt Creating table DEMOGRAPHICQUERYFAVOURITES
prompt =========================================
prompt
create table DEMOGRAPHICQUERYFAVOURITES
(
  FAVID         NUMBER(9) not null,
  SELECTS       CLOB,
  AGE           VARCHAR2(255),
  STARTYEAR     VARCHAR2(8),
  ENDYEAR       VARCHAR2(8),
  FIRSTNAME     VARCHAR2(255),
  LASTNAME      VARCHAR2(255),
  ROSTERSTATUS  CLOB,
  SEX           VARCHAR2(10),
  PROVIDERNO    CLOB,
  PATIENTSTATUS CLOB,
  QUERYNAME     VARCHAR2(255),
  ARCHIVED      VARCHAR2(1)
)
;
alter table DEMOGRAPHICQUERYFAVOURITES
  add primary key (FAVID);

prompt
prompt Creating table DEMOGRAPHICSETS
prompt ==============================
prompt
create table DEMOGRAPHICSETS
(
  DEMOGRAPHIC_NO NUMBER(10),
  SET_NAME       VARCHAR2(20),
  ELIGIBILITY    VARCHAR2(1)
)
;

prompt
prompt Creating table DEMOGRAPHICSTUDY
prompt ===============================
prompt
create table DEMOGRAPHICSTUDY
(
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  STUDY_NO       NUMBER(3) default '0' not null,
  PROVIDER_NO    VARCHAR2(6),
  TIMESTAMP      TIMESTAMP(6) not null
)
;
alter table DEMOGRAPHICSTUDY
  add primary key (DEMOGRAPHIC_NO, STUDY_NO);

prompt
prompt Creating table DEMOGRAPHIC_MERGED
prompt =================================
prompt
create table DEMOGRAPHIC_MERGED
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null,
  MERGED_TO      NUMBER(10) not null,
  DELETED        NUMBER(1) default 0 not null
)
;
alter table DEMOGRAPHIC_MERGED
  add primary key (ID);
create index IDX_DEMOGRAPHIC_MERGED_1 on DEMOGRAPHIC_MERGED (DEMOGRAPHIC_NO, MERGED_TO, DELETED);
create index IDX_DEMOGRAPHIC_MERGED_2 on DEMOGRAPHIC_MERGED (DEMOGRAPHIC_NO, DELETED);
create index IDX_DEMOGRAPHIC_MERGED_3 on DEMOGRAPHIC_MERGED (MERGED_TO, DELETED);

prompt
prompt Creating table DESANNUALREVIEWPLAN
prompt ==================================
prompt
create table DESANNUALREVIEWPLAN
(
  DES_NO            NUMBER(10) not null,
  DES_DATE          DATE default '01-JAN-1900' not null,
  DES_TIME          DATE default '01-JAN-1900' not null,
  DEMOGRAPHIC_NO    NUMBER(10) default '0',
  FORM_NO           NUMBER(10) default '0',
  PROVIDER_NO       VARCHAR2(6),
  RISK_CONTENT      CLOB,
  CHECKLIST_CONTENT CLOB
)
;
alter table DESANNUALREVIEWPLAN
  add primary key (DES_NO);

prompt
prompt Creating table DESAPRISK
prompt ========================
prompt
create table DESAPRISK
(
  DESAPRISK_NO      NUMBER(10) not null,
  DESAPRISK_DATE    DATE default '01-JAN-1900' not null,
  DESAPRISK_TIME    DATE default '01-JAN-1900' not null,
  DEMOGRAPHIC_NO    NUMBER(10) default '0',
  FORM_NO           NUMBER(10) default '0',
  PROVIDER_NO       VARCHAR2(6),
  RISK_CONTENT      CLOB,
  CHECKLIST_CONTENT CLOB
)
;
alter table DESAPRISK
  add primary key (DESAPRISK_NO);

prompt
prompt Creating table DIAGNOSTICCODE
prompt =============================
prompt
create table DIAGNOSTICCODE
(
  DIAGNOSTICCODE_NO NUMBER(5) not null,
  DIAGNOSTIC_CODE   VARCHAR2(5),
  DESCRIPTION       CLOB,
  STATUS            VARCHAR2(1),
  REGION            VARCHAR2(5)
)
;
alter table DIAGNOSTICCODE
  add primary key (DIAGNOSTICCODE_NO);
create index IDX_DIAGNOSTICCODE_1 on DIAGNOSTICCODE (DIAGNOSTIC_CODE);

prompt
prompt Creating table DISEASES
prompt =======================
prompt
create table DISEASES
(
  DISEASEID      NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  ICD9_E         VARCHAR2(6),
  ENTRY_DATE     DATE
)
;
alter table DISEASES
  add primary key (DISEASEID);

prompt
prompt Creating table DOCUMENT
prompt =======================
prompt
create table DOCUMENT
(
  DOCUMENT_NO     NUMBER(6) not null,
  DOCTYPE         VARCHAR2(20),
  DOCDESC         VARCHAR2(255),
  DOCXML          VARCHAR2(4000),
  DOCFILENAME     VARCHAR2(255),
  DOCCREATOR      VARCHAR2(30),
  UPDATEDATETIME  DATE,
  STATUS          VARCHAR2(1),
  CONTENTTYPE     VARCHAR2(60),
  PUBLIC1         NUMBER(1) default '0' not null,
  OBSERVATIONDATE DATE
)
;
alter table DOCUMENT
  add primary key (DOCUMENT_NO);

prompt
prompt Creating table DRUGS
prompt ====================
prompt
create table DRUGS
(
  DRUGID              NUMBER(10) not null,
  PROVIDER_NO         VARCHAR2(6),
  DEMOGRAPHIC_NO      NUMBER(10) default '0' not null,
  RX_DATE             DATE default '01-JAN-1900' not null,
  END_DATE            DATE default '01-JAN-1900' not null,
  BN                  VARCHAR2(255),
  GCN_SEQNO           NUMBER default '0' not null,
  CUSTOMNAME          VARCHAR2(60),
  TAKEMIN             NUMBER(12,2),
  TAKEMAX             NUMBER(12,2),
  FREQCODE            VARCHAR2(6),
  DURATION            VARCHAR2(4),
  DURUNIT             VARCHAR2(1),
  QUANTITY            VARCHAR2(20),
  REPEAT              NUMBER(4),
  NOSUBS              NUMBER(1) default '0' not null,
  PRN                 NUMBER(1) default '0' not null,
  SPECIAL             VARCHAR2(4000),
  ARCHIVED            NUMBER(1) default '0' not null,
  GN                  VARCHAR2(255),
  ATC                 VARCHAR2(20),
  SCRIPT_NO           NUMBER(10),
  REGIONAL_IDENTIFIER VARCHAR2(100),
  UNIT                VARCHAR2(5) default 'tab',
  METHOD              VARCHAR2(5) default 'Take',
  ROUTE               VARCHAR2(5) default 'PO',
  CREATE_DATE         DATE,
  DOSAGE              VARCHAR2(4000),
  CUSTOM_INSTRUCTIONS VARCHAR2(1) default 'N'
)
;
alter table DRUGS
  add primary key (DRUGID);

prompt
prompt Creating table DXRESEARCH
prompt =========================
prompt
create table DXRESEARCH
(
  DXRESEARCH_NO   NUMBER(10) not null,
  DEMOGRAPHIC_NO  NUMBER(10) default '0',
  START_DATE      DATE default '01-JAN-1900',
  UPDATE_DATE     DATE default '01-JAN-1900',
  STATUS          VARCHAR2(1) default 'A',
  DXRESEARCH_CODE VARCHAR2(10),
  CODING_SYSTEM   VARCHAR2(20)
)
;
alter table DXRESEARCH
  add primary key (DXRESEARCH_NO);

prompt
prompt Creating table ECHART
prompt =====================
prompt
create table ECHART
(
  ECHARTID        NUMBER(15) not null,
  TIMESTAMP       TIMESTAMP(6) not null,
  DEMOGRAPHICNO   NUMBER(10) default '0' not null,
  PROVIDERNO      VARCHAR2(6),
  SUBJECT         VARCHAR2(128),
  SOCIALHISTORY   VARCHAR2(4000),
  FAMILYHISTORY   VARCHAR2(4000),
  MEDICALHISTORY  VARCHAR2(4000),
  ONGOINGCONCERNS VARCHAR2(4000),
  REMINDERS       VARCHAR2(4000),
  ENCOUNTER       VARCHAR2(4000)
)
;
alter table ECHART
  add primary key (ECHARTID);
create index IDX_ECHART_1 on ECHART (DEMOGRAPHICNO);

prompt
prompt Creating table EFORM
prompt ====================
prompt
create table EFORM
(
  FID          NUMBER(8) not null,
  FORM_NAME    VARCHAR2(255),
  FILE_NAME    VARCHAR2(255),
  SUBJECT      VARCHAR2(255),
  FORM_DATE    DATE,
  FORM_TIME    DATE,
  FORM_CREATOR VARCHAR2(255),
  STATUS       NUMBER(1) default '1' not null,
  FORM_HTML    VARCHAR2(4000)
)
;
alter table EFORM
  add primary key (FID);

prompt
prompt Creating table EFORMS
prompt =====================
prompt
create table EFORMS
(
  FID          NUMBER(8) not null,
  FORM_NAME    VARCHAR2(4000),
  FILE_NAME    VARCHAR2(4000),
  SUBJECT      VARCHAR2(4000),
  FORM_DATE    DATE,
  FORM_TIME    DATE,
  FORM_CREATOR VARCHAR2(4000),
  STATUS       NUMBER(1) default '0' not null,
  FORM_HTML    VARCHAR2(4000)
)
;
alter table EFORMS
  add primary key (FID);

prompt
prompt Creating table EFORMS_DATA
prompt ==========================
prompt
create table EFORMS_DATA
(
  FDID           NUMBER(8) not null,
  FID            NUMBER(8) default '0' not null,
  FORM_NAME      VARCHAR2(4000),
  SUBJECT        VARCHAR2(4000),
  DEMOGRAPHIC_NO NUMBER(8) default '0' not null,
  STATUS         NUMBER(1) default '0' not null,
  FORM_DATE      DATE,
  FORM_TIME      DATE,
  FORM_PROVIDER  VARCHAR2(4000),
  FORM_DATA      VARCHAR2(4000),
  FORM_FIELDS    VARCHAR2(4000)
)
;
alter table EFORMS_DATA
  add primary key (FDID);

prompt
prompt Creating table EFORM_DATA
prompt =========================
prompt
create table EFORM_DATA
(
  FDID           NUMBER(8) not null,
  FID            NUMBER(8) default '0' not null,
  FORM_NAME      VARCHAR2(255),
  SUBJECT        VARCHAR2(255),
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  STATUS         NUMBER(1) default '1' not null,
  FORM_DATE      DATE,
  FORM_TIME      DATE,
  FORM_PROVIDER  VARCHAR2(255),
  FORM_DATA      VARCHAR2(4000)
)
;
alter table EFORM_DATA
  add primary key (FDID);
create index IDX_EFORM_DATA_1 on EFORM_DATA (DEMOGRAPHIC_NO);
create index IDX_EFORM_DATA_2 on EFORM_DATA (STATUS);
create index IDX_EFORM_DATA_3 on EFORM_DATA (FORM_DATE);
create index IDX_EFORM_DATA_4 on EFORM_DATA (FORM_NAME);
create index IDX_EFORM_DATA_5 on EFORM_DATA (SUBJECT);
create index IDX_EFORM_DATA_6 on EFORM_DATA (FID);
create index IDX_EFORM_DATA_7 on EFORM_DATA (FORM_PROVIDER);

prompt
prompt Creating table EFORM_GROUPS
prompt ===========================
prompt
create table EFORM_GROUPS
(
  ID         NUMBER(10) not null,
  FID        NUMBER(8) default 0 not null,
  GROUP_NAME VARCHAR2(20)
)
;
alter table EFORM_GROUPS
  add primary key (ID);

prompt
prompt Creating table EFORM_VALUES
prompt ===========================
prompt
create table EFORM_VALUES
(
  ID             NUMBER(16) not null,
  FDID           NUMBER(8),
  FID            NUMBER(8),
  DEMOGRAPHIC_NO NUMBER(10) default '0',
  VAR_NAME       VARCHAR2(30),
  VAR_VALUE      VARCHAR2(4000)
)
;
alter table EFORM_VALUES
  add primary key (ID);

prompt
prompt Creating table ENCOUNTER
prompt ========================
prompt
create table ENCOUNTER
(
  ENCOUNTER_NO        NUMBER(12) not null,
  DEMOGRAPHIC_NO      NUMBER(10) default '0' not null,
  ENCOUNTER_DATE      DATE default '01-JAN-1900' not null,
  ENCOUNTER_TIME      DATE default '01-JAN-1900' not null,
  PROVIDER_NO         VARCHAR2(6),
  SUBJECT             VARCHAR2(100),
  CONTENT             VARCHAR2(4000),
  ENCOUNTERATTACHMENT VARCHAR2(4000)
)
;
alter table ENCOUNTER
  add primary key (ENCOUNTER_NO);

prompt
prompt Creating table ENCOUNTERFORM
prompt ============================
prompt
create table ENCOUNTERFORM
(
  FORM_NAME  VARCHAR2(30),
  FORM_VALUE VARCHAR2(255) not null,
  FORM_TABLE VARCHAR2(50),
  HIDDEN     NUMBER(5) default '0' not null
)
;
alter table ENCOUNTERFORM
  add primary key (FORM_VALUE);

prompt
prompt Creating table ENCOUNTERTEMPLATE
prompt ================================
prompt
create table ENCOUNTERTEMPLATE
(
  ENCOUNTERTEMPLATE_NAME  VARCHAR2(50) not null,
  CREATEDATETIME          DATE,
  ENCOUNTERTEMPLATE_VALUE VARCHAR2(4000),
  CREATOR                 VARCHAR2(6)
)
;
alter table ENCOUNTERTEMPLATE
  add primary key (ENCOUNTERTEMPLATE_NAME);
create index IDX_ENCOUNTERTEMPLATE_1 on ENCOUNTERTEMPLATE (CREATEDATETIME);

prompt
prompt Creating table ENCOUNTERWINDOW
prompt ==============================
prompt
create table ENCOUNTERWINDOW
(
  PROVIDER_NO  VARCHAR2(6) not null,
  ROWONESIZE   NUMBER(10) default '60' not null,
  ROWTWOSIZE   NUMBER(10) default '60' not null,
  PRESBOXSIZE  NUMBER(10) default '30' not null,
  ROWTHREESIZE NUMBER(10) default '378' not null
)
;
alter table ENCOUNTERWINDOW
  add primary key (PROVIDER_NO);

prompt
prompt Creating table FACILITY
prompt =======================
prompt
create table FACILITY
(
  ID            NUMBER(22) not null,
  NAME          VARCHAR2(32) default ' ' not null,
  DESCRIPTION   VARCHAR2(70) default ' ' not null,
  CONTACT_NAME  VARCHAR2(255),
  CONTACT_EMAIL VARCHAR2(255),
  CONTACT_PHONE VARCHAR2(255),
  HIC           VARCHAR2(1) default 'N' not null,
  DISABLED      NUMBER(1) default '0' not null,
  ORG_ID        NUMBER(10) not null,
  SECTOR_ID     NUMBER(10) not null
)
;
alter table FACILITY
  add primary key (ID);
create unique index IDX_FACILITY_1 on FACILITY (NAME);

prompt
prompt Creating table FACILITY_MESSAGE
prompt ===============================
prompt
create table FACILITY_MESSAGE
(
  ID            NUMBER(22) not null,
  MESSAGE       VARCHAR2(4000),
  CREATION_DATE DATE,
  EXPIRY_DATE   DATE,
  FACILITY_ID   NUMBER(22),
  FACILITY_NAME VARCHAR2(32)
)
;
alter table FACILITY_MESSAGE
  add primary key (ID);

prompt
prompt Creating table FAVORITES
prompt ========================
prompt
create table FAVORITES
(
  FAVORITEID          NUMBER(10) not null,
  PROVIDER_NO         VARCHAR2(6),
  FAVORITENAME        VARCHAR2(50),
  BN                  VARCHAR2(30),
  GCN_SEQNO           NUMBER default '0' not null,
  CUSTOMNAME          VARCHAR2(60),
  TAKEMIN             NUMBER(12,2),
  TAKEMAX             NUMBER(12,2),
  FREQCODE            VARCHAR2(6),
  DURATION            VARCHAR2(4),
  DURUNIT             VARCHAR2(1),
  QUANTITY            VARCHAR2(20),
  REPEAT              NUMBER(4),
  NOSUBS              NUMBER(1) default '0' not null,
  PRN                 NUMBER(1) default '0' not null,
  SPECIAL             VARCHAR2(255),
  GN                  VARCHAR2(255),
  ATC                 VARCHAR2(255),
  REGIONAL_IDENTIFIER VARCHAR2(100),
  UNIT                VARCHAR2(5) default 'tab',
  METHOD              VARCHAR2(5) default 'Take',
  ROUTE               VARCHAR2(5) default 'PO',
  DOSAGE              VARCHAR2(4000),
  CUSTOM_INSTRUCTIONS VARCHAR2(1) default 'N'
)
;
alter table FAVORITES
  add primary key (FAVORITEID);

prompt
prompt Creating table FAXCLIENTLOG
prompt ===========================
prompt
create table FAXCLIENTLOG
(
  FAXLOGID    NUMBER(9) not null,
  PROVIDER_NO VARCHAR2(6),
  STARTTIME   DATE,
  ENDTIME     DATE,
  RESULT      VARCHAR2(255),
  REQUESTID   VARCHAR2(10),
  FAXID       VARCHAR2(10)
)
;
alter table FAXCLIENTLOG
  add primary key (FAXLOGID);

prompt
prompt Creating table FILEUPLOADCHECK
prompt ==============================
prompt
create table FILEUPLOADCHECK
(
  ID          NUMBER(10) not null,
  PROVIDER_NO VARCHAR2(6),
  FILENAME    VARCHAR2(255),
  MD5SUM      VARCHAR2(255),
  DATE_TIME   DATE default '01-JAN-1900' not null
)
;
alter table FILEUPLOADCHECK
  add primary key (ID);

prompt
prompt Creating table FORM
prompt ===================
prompt
create table FORM
(
  FORM_NO        NUMBER(12) not null,
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  PROVIDER_NO    VARCHAR2(6),
  FORM_DATE      DATE default '01-JAN-1900' not null,
  FORM_TIME      DATE default '01-JAN-1900' not null,
  FORM_NAME      VARCHAR2(50),
  CONTENT        VARCHAR2(4000)
)
;
alter table FORM
  add primary key (FORM_NO);
create index IDX_FORM_1 on FORM (DEMOGRAPHIC_NO, FORM_NAME);

prompt
prompt Creating table FORM2MINWALK
prompt ===========================
prompt
create table FORM2MINWALK
(
  ID               NUMBER(10) not null,
  DEMOGRAPHIC_NO   NUMBER(10) not null,
  PROVIDER_NO      NUMBER(10),
  FORMCREATED      DATE,
  FORMEDITED       TIMESTAMP(6) not null,
  STUDYID          VARCHAR2(20) default 'N/A' not null,
  DISTANCE         VARCHAR2(255),
  Q1TRIED          NUMBER(1),
  Q1FULLTANDEM3TO9 NUMBER(1),
  Q1SIDEBYSIDE10   NUMBER(1),
  Q1FULLTANDEM10   NUMBER(1),
  Q1SEMITANDEM10   NUMBER(1),
  Q1CMT            VARCHAR2(255),
  Q2TIME1          VARCHAR2(255),
  Q2TIME2          VARCHAR2(255),
  Q2CMT            VARCHAR2(255),
  Q3UNABLE         NUMBER(1),
  Q3FROM11TO13S    NUMBER(1),
  Q3LESSTHAN16S    NUMBER(1),
  Q3LESSTHAN11S    NUMBER(1),
  Q3FROM13TO16S    NUMBER(1),
  Q3CMT            VARCHAR2(255)
)
;
alter table FORM2MINWALK
  add primary key (ID);

prompt
prompt Creating table FORMADF
prompt ======================
prompt
create table FORMADF
(
  ID                   NUMBER(10) not null,
  DEMOGRAPHIC_NO       NUMBER(10) default '0' not null,
  PROVIDER_NO          NUMBER(10),
  FORMCREATED          DATE,
  FORMEDITED           TIMESTAMP(6) not null,
  C_PATIENTNAME        VARCHAR2(60),
  RESIDENTNO           VARCHAR2(20),
  C_PHYSICIAN          VARCHAR2(60),
  C_ADDRESS            VARCHAR2(80),
  C_PHONE              VARCHAR2(20),
  CCOMPLAIT            VARCHAR2(4000),
  HISTPRESILL          VARCHAR2(4000),
  CHILDHOOD            VARCHAR2(80),
  ADULT                VARCHAR2(80),
  OPERATIONS           VARCHAR2(80),
  INJURIES             VARCHAR2(80),
  MENTALILL            VARCHAR2(80),
  FAMILYHIST           VARCHAR2(4000),
  SOCIALHIST           VARCHAR2(4000),
  GENERAL              VARCHAR2(80),
  HISTSKIN             VARCHAR2(80),
  HEADNECK             VARCHAR2(80),
  RESPIRATORY          VARCHAR2(80),
  CARDIOVASCULAR       VARCHAR2(80),
  GI                   VARCHAR2(80),
  GU                   VARCHAR2(80),
  CNS                  VARCHAR2(80),
  HISTEXTREMITIES      VARCHAR2(80),
  ALLERGIES            VARCHAR2(4000),
  SENSITIVITYDRUG      VARCHAR2(4000),
  CURRENTMEDICATION    VARCHAR2(4000),
  TEMP                 VARCHAR2(80),
  PULSE                VARCHAR2(80),
  RESP                 VARCHAR2(80),
  BP                   VARCHAR2(80),
  HEIGHT               VARCHAR2(80),
  WEIGHT               VARCHAR2(80),
  PHYSICALCONDITION    VARCHAR2(4000),
  MENTALCONDITION      VARCHAR2(4000),
  SKIN                 VARCHAR2(80),
  EYES                 VARCHAR2(80),
  EARS                 VARCHAR2(80),
  NOSE                 VARCHAR2(80),
  MOUTHTEETH           VARCHAR2(80),
  THROAT               VARCHAR2(80),
  NECK                 VARCHAR2(80),
  CHEST                VARCHAR2(80),
  HEART                VARCHAR2(80),
  ABDOMEN              VARCHAR2(80),
  GENITALIA            VARCHAR2(80),
  LYMPHATICS           VARCHAR2(80),
  BLOODVESSELS         VARCHAR2(80),
  LOCOMOTOR            VARCHAR2(80),
  EXTREMITIES          VARCHAR2(80),
  RECTAL               VARCHAR2(80),
  VAGINAL              VARCHAR2(80),
  NEUROLOGICAL         VARCHAR2(80),
  BEHAVIORPROBLEM      VARCHAR2(4000),
  FUNCTIONALLIMITATION VARCHAR2(4000),
  DIAGNOSES            VARCHAR2(4000),
  SIGDATE              DATE,
  SIGNATURE            VARCHAR2(60)
)
;
alter table FORMADF
  add primary key (ID);

prompt
prompt Creating table FORMADFV2
prompt ========================
prompt
create table FORMADFV2
(
  ID                NUMBER(10) not null,
  DEMOGRAPHIC_NO    NUMBER(10) default '0' not null,
  PROVIDER_NO       NUMBER(10),
  FORMCREATED       DATE,
  FORMEDITED        TIMESTAMP(6),
  C_PATIENTNAME     VARCHAR2(60),
  SENDFACILITY      VARCHAR2(80),
  POASDM            VARCHAR2(80),
  CAPTREATDECY      NUMBER(1),
  CAPTREATDECN      NUMBER(1),
  ADVDIRECTIVE      VARCHAR2(100),
  ACTPROBLEM        VARCHAR2(4000),
  INACTPROBLEM      VARCHAR2(4000),
  COURSEOVERYEAR    VARCHAR2(4000),
  MEDICATIONS       VARCHAR2(4000),
  ALLERGY           VARCHAR2(255),
  DIET              VARCHAR2(255),
  INFLUDATE         DATE,
  PNEUDATE          DATE,
  MANTDATERES       VARCHAR2(50),
  IMMUOTHERS        VARCHAR2(50),
  PERTLABINVEST     VARCHAR2(4000),
  COMMUNICATION     VARCHAR2(100),
  APPETDYSPHWEIGHT  VARCHAR2(100),
  SLEEPENERGY       VARCHAR2(100),
  DEPRESYMPTOM      VARCHAR2(100),
  PROBLEMBEHAV      VARCHAR2(100),
  FUNCSTATUS        VARCHAR2(100),
  FALLFRACTURE      VARCHAR2(100),
  PAIN              VARCHAR2(100),
  CONTINENCE        VARCHAR2(100),
  SYSSKIN           VARCHAR2(100),
  SOCIALSUPP        VARCHAR2(100),
  SYSOTHER          VARCHAR2(100),
  PHYWEIGHT         VARCHAR2(10),
  PHYHEIGHT         VARCHAR2(10),
  PHYBPLYING        VARCHAR2(16),
  PHYBPSTANDING     VARCHAR2(16),
  PHYGENAPPEAR      VARCHAR2(100),
  PHYEYES           VARCHAR2(50),
  PHYEARS           VARCHAR2(50),
  PHYORALHYGEINE    VARCHAR2(50),
  PHYBREAST         VARCHAR2(50),
  PHYCARDHEARTSOUND VARCHAR2(30),
  PHYPERIPULSE      VARCHAR2(30),
  PHYOTHER          VARCHAR2(30),
  PHYRESPIRATORY    VARCHAR2(100),
  PHYNEUROLOGICAL   VARCHAR2(30),
  PHYREFLEXES       VARCHAR2(30),
  PHYBABINSKI       VARCHAR2(30),
  PHYPOWER          VARCHAR2(30),
  PHYTONE           VARCHAR2(30),
  PHYPOWEROTHER     VARCHAR2(30),
  PHYMMSE           VARCHAR2(20),
  PHYCOMMENT        VARCHAR2(80),
  PHYSKIN           VARCHAR2(100),
  PHYABDOMEN        VARCHAR2(80),
  HIGHRISKPROB1     VARCHAR2(80),
  INVESTPLANCARE1   VARCHAR2(80),
  HIGHRISKPROB2     VARCHAR2(80),
  INVESTPLANCARE2   VARCHAR2(80),
  HIGHRISKPROB3     VARCHAR2(80),
  INVESTPLANCARE3   VARCHAR2(80),
  HIGHRISKPROB4     VARCHAR2(80),
  INVESTPLANCARE4   VARCHAR2(80),
  SIGDATE           DATE,
  SIGNATURE         VARCHAR2(60),
  SIGNAME           VARCHAR2(60)
)
;
alter table FORMADFV2
  add primary key (ID);

prompt
prompt Creating table FORMALPHA
prompt ========================
prompt
create table FORMALPHA
(
  ID                  NUMBER(10) not null,
  DEMOGRAPHIC_NO      NUMBER(10) default '0' not null,
  PROVIDER_NO         NUMBER(10),
  FORMCREATED         DATE,
  FORMEDITED          TIMESTAMP(6) not null,
  PNAME               VARCHAR2(60),
  SOCIALSUPPORT       VARCHAR2(4000),
  LIFEEVENTS          VARCHAR2(4000),
  COUPLERELATIONSHIP  VARCHAR2(4000),
  PRENATALCARE        VARCHAR2(4000),
  PRENATALEDUCATION   VARCHAR2(4000),
  FEELINGSREPREGNANCY VARCHAR2(4000),
  RELATIONSHIPPARENTS VARCHAR2(4000),
  SELFESTEEM          VARCHAR2(4000),
  PSYCHHISTORY        VARCHAR2(4000),
  DEPRESSION          VARCHAR2(4000),
  ALCOHOLDRUGABUSE    VARCHAR2(4000),
  ABUSE               VARCHAR2(4000),
  WOMANABUSE          VARCHAR2(4000),
  CHILDABUSE          VARCHAR2(4000),
  CHILDDISCIPLINE     VARCHAR2(4000),
  PROVCOUNSELLING     NUMBER(1),
  HOMECARE            NUMBER(1),
  ASSAULTEDWOMEN      NUMBER(1),
  ADDAPPTS            NUMBER(1),
  PARENTINGCLASSES    NUMBER(1),
  LEGALADVICE         NUMBER(1),
  POSTPARTUMAPPTS     NUMBER(1),
  ADDICTPROGRAMS      NUMBER(1),
  CAS                 NUMBER(1),
  BABYVISITS          NUMBER(1),
  QUITSMOKING         NUMBER(1),
  OTHER1              NUMBER(1),
  OTHER1NAME          VARCHAR2(30),
  PUBLICHEALTH        NUMBER(1),
  SOCIALWORKER        NUMBER(1),
  OTHER2              NUMBER(1),
  OTHER2NAME          VARCHAR2(30),
  PRENATALEDU         NUMBER(1),
  PSYCH               NUMBER(1),
  OTHER3              NUMBER(1),
  OTHER3NAME          VARCHAR2(30),
  NUTRITIONIST        NUMBER(1),
  THERAPIST           NUMBER(1),
  OTHER4              NUMBER(1),
  OTHER4NAME          VARCHAR2(30),
  RESOURCES           NUMBER(1),
  COMMENTS            VARCHAR2(4000)
)
;
alter table FORMALPHA
  add primary key (ID);

prompt
prompt Creating table FORMANNUAL
prompt =========================
prompt
create table FORMANNUAL
(
  ID                 NUMBER(10) not null,
  DEMOGRAPHIC_NO     NUMBER(10),
  PROVIDER_NO        NUMBER(10),
  FORMCREATED        DATE,
  FORMEDITED         TIMESTAMP(6) not null,
  PNAME              VARCHAR2(60),
  AGE                VARCHAR2(3),
  FORMDATE           DATE,
  CURRENTCONCERNS    VARCHAR2(4000),
  CURRENTCONCERNSNO  NUMBER(1),
  CURRENTCONCERNSYES NUMBER(1),
  HEADN              NUMBER(1),
  HEADABN            NUMBER(1),
  HEAD               VARCHAR2(30),
  RESPN              NUMBER(1),
  RESPABN            NUMBER(1),
  RESP               VARCHAR2(30),
  CARDION            NUMBER(1),
  CARDIOABN          NUMBER(1),
  CARDIO             VARCHAR2(30),
  GIN                NUMBER(1),
  GIABN              NUMBER(1),
  GI                 VARCHAR2(30),
  GUN                NUMBER(1),
  GUABN              NUMBER(1),
  GU                 VARCHAR2(30),
  NOGTPALREVISIONS   NUMBER(1),
  YESGTPALREVISIONS  NUMBER(1),
  FRONTSHEET         NUMBER(1),
  LMP                DATE,
  MENOPAUSE          VARCHAR2(3),
  PAPSMEARSN         NUMBER(1),
  PAPSMEARSABN       NUMBER(1),
  PAPSMEARS          VARCHAR2(30),
  SKINN              NUMBER(1),
  SKINABN            NUMBER(1),
  SKIN               VARCHAR2(30),
  MSKN               NUMBER(1),
  MSKABN             NUMBER(1),
  MSK                VARCHAR2(30),
  ENDOCRINN          NUMBER(1),
  ENDOCRINABN        NUMBER(1),
  ENDOCRIN           VARCHAR2(30),
  OTHERN             NUMBER(1),
  OTHERABN           NUMBER(1),
  OTHER              VARCHAR2(255),
  DRUGS              NUMBER(1),
  MEDSHEET           NUMBER(1),
  ALLERGIES          NUMBER(1),
  FRONTSHEET1        NUMBER(1),
  FAMILYHISTORY      NUMBER(1),
  FRONTSHEET2        NUMBER(1),
  SMOKINGNO          NUMBER(1),
  SMOKINGYES         NUMBER(1),
  SMOKING            VARCHAR2(30),
  SEXUALITYNO        NUMBER(1),
  SEXUALITYYES       NUMBER(1),
  SEXUALITY          VARCHAR2(30),
  ALCOHOLNO          NUMBER(1),
  ALCOHOLYES         NUMBER(1),
  ALCOHOL            VARCHAR2(30),
  OCCUPATIONALNO     NUMBER(1),
  OCCUPATIONALYES    NUMBER(1),
  OCCUPATIONAL       VARCHAR2(30),
  OTCNO              NUMBER(1),
  OTCYES             NUMBER(1),
  OTC                VARCHAR2(30),
  DRIVINGNO          NUMBER(1),
  DRIVINGYES         NUMBER(1),
  DRIVING            VARCHAR2(30),
  EXERCISENO         NUMBER(1),
  EXERCISEYES        NUMBER(1),
  EXERCISE           VARCHAR2(30),
  TRAVELNO           NUMBER(1),
  TRAVELYES          NUMBER(1),
  TRAVEL             VARCHAR2(30),
  NUTRITIONNO        NUMBER(1),
  NUTRITIONYES       NUMBER(1),
  NUTRITION          VARCHAR2(30),
  OTHERNO            NUMBER(1),
  OTHERYES           NUMBER(1),
  OTHERLIFESTYLE     VARCHAR2(255),
  DENTALNO           NUMBER(1),
  DENTALYES          NUMBER(1),
  DENTAL             VARCHAR2(30),
  RELATIONSHIPNO     NUMBER(1),
  RELATIONSHIPYES    NUMBER(1),
  RELATIONSHIP       VARCHAR2(150),
  MAMMOGRAM          NUMBER(1),
  RECTAL             NUMBER(1),
  BREAST             NUMBER(1),
  MALECARDIAC        NUMBER(1),
  PAP                NUMBER(1),
  MALEIMMUNIZATION   NUMBER(1),
  FEMALEIMMUNIZATION NUMBER(1),
  MALEOTHER1C        NUMBER(1),
  MALEOTHER1         VARCHAR2(30),
  PRECONTRACEPTIVE   NUMBER(1),
  MALEOTHER2C        NUMBER(1),
  MALEOTHER2         VARCHAR2(30),
  FEMALECARDIAC      NUMBER(1),
  OSTEOPOROSIS       NUMBER(1),
  FEMALEOTHER1C      NUMBER(1),
  FEMALEOTHER1       VARCHAR2(30),
  FEMALEOTHER2C      NUMBER(1),
  FEMALEOTHER2       VARCHAR2(30),
  BPRTOP             VARCHAR2(3),
  BPRBOTTOM          VARCHAR2(3),
  PULSE              VARCHAR2(10),
  HEIGHT             VARCHAR2(4),
  WEIGHT             VARCHAR2(4),
  BPLTOP             VARCHAR2(3),
  BPLBOTTOM          VARCHAR2(3),
  RHYTHM             VARCHAR2(10),
  URINE              VARCHAR2(30),
  PHYSICALSIGNS      VARCHAR2(4000),
  ASSESSMENT         VARCHAR2(4000),
  PLAN               VARCHAR2(4000),
  SIGNATURE          VARCHAR2(60)
)
;
alter table FORMANNUAL
  add primary key (ID);

prompt
prompt Creating table FORMANNUALV2
prompt ===========================
prompt
create table FORMANNUALV2
(
  ID                    NUMBER(10) not null,
  DEMOGRAPHIC_NO        NUMBER(10),
  PROVIDER_NO           NUMBER(10),
  FORMCREATED           DATE,
  FORMEDITED            TIMESTAMP(6) not null,
  PNAME                 VARCHAR2(60),
  AGE                   VARCHAR2(3),
  FORMDATE              DATE,
  PMHXPSHXUPDATED       NUMBER(1),
  FAMHXUPDATED          NUMBER(1),
  SOCHXUPDATED          NUMBER(1),
  ALLERGIESUPDATED      NUMBER(1),
  MEDICATIONSUPDATED    NUMBER(1),
  WEIGHT                VARCHAR2(4),
  HEIGHT                VARCHAR2(4),
  WAIST                 VARCHAR2(4),
  LMP                   DATE,
  BP                    VARCHAR2(7),
  SMOKINGNO             NUMBER(1),
  SMOKINGYES            NUMBER(1),
  SMOKING               VARCHAR2(100),
  ETOHNO                NUMBER(1),
  ETOHYES               NUMBER(1),
  ETOH                  VARCHAR2(100),
  CAFFINENO             NUMBER(1),
  CAFFINEYES            NUMBER(1),
  CAFFINE               VARCHAR2(100),
  OTCNO                 NUMBER(1),
  OTCYES                NUMBER(1),
  OTC                   VARCHAR2(100),
  EXERCISENO            NUMBER(1),
  EXERCISEYES           NUMBER(1),
  EXERCISE              VARCHAR2(100),
  NUTRITIONNO           NUMBER(1),
  NUTRITIONYES          NUMBER(1),
  NUTRITION             VARCHAR2(100),
  DENTALNO              NUMBER(1),
  DENTALYES             NUMBER(1),
  DENTAL                VARCHAR2(100),
  OCCUPATIONALNO        NUMBER(1),
  OCCUPATIONALYES       NUMBER(1),
  OCCUPATIONAL          VARCHAR2(100),
  TRAVELNO              NUMBER(1),
  TRAVELYES             NUMBER(1),
  TRAVEL                VARCHAR2(100),
  SEXUALITYNO           NUMBER(1),
  SEXUALITYYES          NUMBER(1),
  SEXUALITY             VARCHAR2(100),
  GENERALN              NUMBER(1),
  GENERALABN            NUMBER(1),
  GENERAL               VARCHAR2(100),
  HEADN                 NUMBER(1),
  HEADABN               NUMBER(1),
  HEAD                  VARCHAR2(100),
  CHESTN                NUMBER(1),
  CHESTABN              NUMBER(1),
  CHEST                 VARCHAR2(100),
  CVSN                  NUMBER(1),
  CVSABN                NUMBER(1),
  CVS                   VARCHAR2(100),
  GIN                   NUMBER(1),
  GIABN                 NUMBER(1),
  GI                    VARCHAR2(100),
  GUN                   NUMBER(1),
  GUABN                 NUMBER(1),
  GU                    VARCHAR2(100),
  CNSN                  NUMBER(1),
  CNSABN                NUMBER(1),
  CNS                   VARCHAR2(100),
  MSKN                  NUMBER(1),
  MSKABN                NUMBER(1),
  MSK                   VARCHAR2(100),
  SKINN                 NUMBER(1),
  SKINABN               NUMBER(1),
  SKIN                  VARCHAR2(100),
  MOODN                 NUMBER(1),
  MOODABN               NUMBER(1),
  MOOD                  VARCHAR2(100),
  OTHERN                NUMBER(1),
  OTHERABN              NUMBER(1),
  OTHER                 VARCHAR2(4000),
  EYESN                 NUMBER(1),
  EYESABN               NUMBER(1),
  EYES                  VARCHAR2(100),
  EARSN                 NUMBER(1),
  EARSABN               NUMBER(1),
  EARS                  VARCHAR2(100),
  OROPHARYNXN           NUMBER(1),
  OROPHARYNXABN         NUMBER(1),
  OROPHARYNX            VARCHAR2(100),
  THYROIDN              NUMBER(1),
  THYROIDABN            NUMBER(1),
  THYROID               VARCHAR2(100),
  LNODESN               NUMBER(1),
  LNODESABN             NUMBER(1),
  LNODES                VARCHAR2(100),
  CLEARN                NUMBER(1),
  CLEARABN              NUMBER(1),
  CLEAR                 VARCHAR2(100),
  BILATN                NUMBER(1),
  BILATABN              NUMBER(1),
  BILAT                 VARCHAR2(100),
  WHEEZESN              NUMBER(1),
  WHEEZESABN            NUMBER(1),
  WHEEZES               VARCHAR2(100),
  CRACKLESN             NUMBER(1),
  CRACKLESABN           NUMBER(1),
  CRACKLES              VARCHAR2(100),
  CHESTOTHER            VARCHAR2(100),
  S1S2N                 NUMBER(1),
  S1S2ABN               NUMBER(1),
  S1S2                  VARCHAR2(100),
  MURMURN               NUMBER(1),
  MURMURABN             NUMBER(1),
  MURMUR                VARCHAR2(100),
  PERIPHPULSEN          NUMBER(1),
  PERIPHPULSEABN        NUMBER(1),
  PERIPHPULSE           VARCHAR2(100),
  EDEMAN                NUMBER(1),
  EDEMAABN              NUMBER(1),
  EDEMA                 VARCHAR2(100),
  JVPN                  NUMBER(1),
  JVPABN                NUMBER(1),
  JVP                   VARCHAR2(100),
  RHYTHMN               NUMBER(1),
  RHYTHMABN             NUMBER(1),
  RHYTHM                VARCHAR2(100),
  CHESTBPN              NUMBER(1),
  CHESTBPABN            NUMBER(1),
  CHESTBP               VARCHAR2(100),
  CVSOTHER              VARCHAR2(100),
  BREASTLEFTN           NUMBER(1),
  BREASTLEFTABN         NUMBER(1),
  BREASTLEFT            VARCHAR2(100),
  BREASTRIGHTN          NUMBER(1),
  BREASTRIGHTABN        NUMBER(1),
  BREASTRIGHT           VARCHAR2(100),
  SOFTN                 NUMBER(1),
  SOFTABN               NUMBER(1),
  SOFT                  VARCHAR2(100),
  TENDERN               NUMBER(1),
  TENDERABN             NUMBER(1),
  TENDER                VARCHAR2(100),
  BSN                   NUMBER(1),
  BSABN                 NUMBER(1),
  BS                    VARCHAR2(100),
  HEPATOMEGN            NUMBER(1),
  HEPATOMEGABN          NUMBER(1),
  HEPATOMEG             VARCHAR2(100),
  SPLENOMEGN            NUMBER(1),
  SPLENOMEGABN          NUMBER(1),
  SPLENOMEG             VARCHAR2(100),
  MASSESN               NUMBER(1),
  MASSESABN             NUMBER(1),
  MASSES                VARCHAR2(100),
  RECTALN               NUMBER(1),
  RECTALABN             NUMBER(1),
  RECTAL                VARCHAR2(100),
  CXN                   NUMBER(1),
  CXABN                 NUMBER(1),
  CX                    VARCHAR2(100),
  BIMANUALN             NUMBER(1),
  BIMANUALABN           NUMBER(1),
  BIMANUAL              VARCHAR2(100),
  ADNEXAN               NUMBER(1),
  ADNEXAABN             NUMBER(1),
  ADNEXA                VARCHAR2(100),
  PAPN                  NUMBER(1),
  PAPABN                NUMBER(1),
  PAP                   VARCHAR2(100),
  EXAMMSKN              NUMBER(1),
  EXAMMSKABN            NUMBER(1),
  EXAMMSK               VARCHAR2(100),
  EXAMSKINN             NUMBER(1),
  EXAMSKINABN           NUMBER(1),
  EXAMSKIN              VARCHAR2(100),
  EXAMCNSN              NUMBER(1),
  EXAMCNSABN            NUMBER(1),
  EXAMCNS               VARCHAR2(4000),
  IMPRESSIONPLAN        VARCHAR2(4000),
  TODOSEXUALHEALTH      VARCHAR2(4000),
  TODOOBESITY           VARCHAR2(4000),
  TODOCHOLESTEROL       VARCHAR2(4000),
  TODOOSTEOPOROSIS      VARCHAR2(4000),
  TODOPAPS              VARCHAR2(4000),
  TODOMAMMOGRAM         VARCHAR2(4000),
  TODOCOLORECTAL        VARCHAR2(4000),
  TODOELDERLY           VARCHAR2(4000),
  IMMUNIZATIONTD        NUMBER(1),
  IMMUNIZATIONPNEUMOVAX NUMBER(1),
  IMMUNIZATIONFLU       NUMBER(1),
  IMMUNIZATIONMENJUGATE NUMBER(1),
  TODOIMMUNIZATION      VARCHAR2(4000),
  SIGNATURE             VARCHAR2(60),
  EXAMGENITALIAN        NUMBER(1),
  EXAMGENITALIAABN      NUMBER(1),
  EXAMGENITALIA         VARCHAR2(100),
  TODOPROSTATECANCER    VARCHAR2(4000)
)
;
alter table FORMANNUALV2
  add primary key (ID);

prompt
prompt Creating table FORMAR
prompt =====================
prompt
create table FORMAR
(
  ID                    NUMBER(10) not null,
  DEMOGRAPHIC_NO        NUMBER(10) default '0' not null,
  PROVIDER_NO           VARCHAR2(6),
  FORMCREATED           DATE,
  FORMEDITED            TIMESTAMP(6) not null,
  C_LASTVISITED         VARCHAR2(3),
  C_PNAME               VARCHAR2(60),
  C_ADDRESS             VARCHAR2(80),
  PG1_DATEOFBIRTH       DATE,
  PG1_AGE               VARCHAR2(2),
  PG1_MSSINGLE          NUMBER(1),
  PG1_MSCOMMONLAW       NUMBER(1),
  PG1_MSMARRIED         NUMBER(1),
  PG1_EDULEVEL          VARCHAR2(25),
  PG1_OCCUPATION        VARCHAR2(25),
  PG1_LANGUAGE          VARCHAR2(25),
  PG1_HOMEPHONE         VARCHAR2(20),
  PG1_WORKPHONE         VARCHAR2(20),
  PG1_PARTNERNAME       VARCHAR2(50),
  PG1_PARTNERAGE        VARCHAR2(2),
  PG1_PARTNEROCCUPATION VARCHAR2(25),
  PG1_BAOBS             NUMBER(1),
  PG1_BAFP              NUMBER(1),
  PG1_BAMIDWIFE         NUMBER(1),
  C_BA                  VARCHAR2(25),
  PG1_FAMPHYS           VARCHAR2(100),
  PG1_NCPED             NUMBER(1),
  PG1_NCFP              NUMBER(1),
  PG1_NCMIDWIFE         NUMBER(1),
  C_NC                  VARCHAR2(25),
  PG1_ETHNICBG          VARCHAR2(100),
  PG1_VBAC              NUMBER(1),
  PG1_REPEATCS          NUMBER(1),
  C_ALLERGIES           VARCHAR2(4000),
  C_MEDS                VARCHAR2(4000),
  PG1_MENLMP            VARCHAR2(10),
  PG1_MENCYCLE          VARCHAR2(7),
  PG1_MENREG            NUMBER(1),
  PG1_MENEDB            VARCHAR2(10),
  PG1_IUD               NUMBER(1),
  PG1_HORMONE           NUMBER(1),
  PG1_HORMONETYPE       VARCHAR2(25),
  PG1_OTHERAR1          NUMBER(1),
  PG1_OTHERAR1NAME      VARCHAR2(25),
  PG1_LASTUSED          VARCHAR2(10),
  C_FINALEDB            DATE,
  C_GRAVIDA             VARCHAR2(5),
  C_TERM                VARCHAR2(5),
  C_PREM                VARCHAR2(5),
  PG1_ECTOPIC           NUMBER(1),
  PG1_ECTOPICBOX        VARCHAR2(2),
  PG1_TERMINATION       NUMBER(1),
  PG1_TERMINATIONBOX    VARCHAR2(2),
  PG1_SPONTANEOUS       NUMBER(1),
  PG1_SPONTANEOUSBOX    VARCHAR2(2),
  PG1_STILLBORN         NUMBER(1),
  PG1_STILLBORNBOX      VARCHAR2(2),
  C_LIVING              VARCHAR2(10),
  PG1_MULTI             VARCHAR2(10),
  PG1_YEAR1             VARCHAR2(10),
  PG1_SEX1              VARCHAR2(1),
  PG1_OH_GEST1          VARCHAR2(5),
  PG1_WEIGHT1           VARCHAR2(6),
  PG1_LENGTH1           VARCHAR2(6),
  PG1_PLACE1            VARCHAR2(20),
  PG1_SVB1              NUMBER(1),
  PG1_CS1               NUMBER(1),
  PG1_ASS1              NUMBER(1),
  PG1_OH_COMMENTS1      VARCHAR2(80),
  PG1_YEAR2             VARCHAR2(10),
  PG1_SEX2              VARCHAR2(1),
  PG1_OH_GEST2          VARCHAR2(5),
  PG1_WEIGHT2           VARCHAR2(6),
  PG1_LENGTH2           VARCHAR2(6),
  PG1_PLACE2            VARCHAR2(20),
  PG1_SVB2              NUMBER(1),
  PG1_CS2               NUMBER(1),
  PG1_ASS2              NUMBER(1),
  PG1_OH_COMMENTS2      VARCHAR2(80),
  PG1_YEAR3             VARCHAR2(10),
  PG1_SEX3              VARCHAR2(1),
  PG1_OH_GEST3          VARCHAR2(5),
  PG1_WEIGHT3           VARCHAR2(6),
  PG1_LENGTH3           VARCHAR2(6),
  PG1_PLACE3            VARCHAR2(20),
  PG1_SVB3              NUMBER(1),
  PG1_CS3               NUMBER(1),
  PG1_ASS3              NUMBER(1),
  PG1_OH_COMMENTS3      VARCHAR2(80),
  PG1_YEAR4             VARCHAR2(10),
  PG1_SEX4              VARCHAR2(1),
  PG1_OH_GEST4          VARCHAR2(5),
  PG1_WEIGHT4           VARCHAR2(6),
  PG1_LENGTH4           VARCHAR2(6),
  PG1_PLACE4            VARCHAR2(20),
  PG1_SVB4              NUMBER(1),
  PG1_CS4               NUMBER(1),
  PG1_ASS4              NUMBER(1),
  PG1_OH_COMMENTS4      VARCHAR2(80),
  PG1_YEAR5             VARCHAR2(10),
  PG1_SEX5              VARCHAR2(1),
  PG1_OH_GEST5          VARCHAR2(5),
  PG1_WEIGHT5           VARCHAR2(6),
  PG1_LENGTH5           VARCHAR2(6),
  PG1_PLACE5            VARCHAR2(20),
  PG1_SVB5              NUMBER(1),
  PG1_CS5               NUMBER(1),
  PG1_ASS5              NUMBER(1),
  PG1_OH_COMMENTS5      VARCHAR2(80),
  PG1_YEAR6             VARCHAR2(10),
  PG1_SEX6              VARCHAR2(1),
  PG1_OH_GEST6          VARCHAR2(5),
  PG1_WEIGHT6           VARCHAR2(6),
  PG1_LENGTH6           VARCHAR2(6),
  PG1_PLACE6            VARCHAR2(20),
  PG1_SVB6              NUMBER(1),
  PG1_CS6               NUMBER(1),
  PG1_ASS6              NUMBER(1),
  PG1_OH_COMMENTS6      VARCHAR2(80),
  PG1_CP1               NUMBER(1),
  PG1_CP2               NUMBER(1),
  PG1_CP3               NUMBER(1),
  PG1_BOX3              VARCHAR2(3),
  PG1_CP4               NUMBER(1),
  PG1_CP5               NUMBER(1),
  PG1_BOX5              VARCHAR2(3),
  PG1_CP6               NUMBER(1),
  PG1_CP7               NUMBER(1),
  PG1_CP8               NUMBER(1),
  PG1_NAFOLIC           NUMBER(1),
  PG1_NAMILK            NUMBER(1),
  PG1_NADIETBAL         NUMBER(1),
  PG1_NADIETRES         NUMBER(1),
  PG1_NAREF             NUMBER(1),
  PG1_YES9              NUMBER(1),
  PG1_NO9               NUMBER(1),
  PG1_YES10             NUMBER(1),
  PG1_NO10              NUMBER(1),
  PG1_YES11             NUMBER(1),
  PG1_NO11              NUMBER(1),
  PG1_YES12             NUMBER(1),
  PG1_NO12              NUMBER(1),
  PG1_YES13             NUMBER(1),
  PG1_NO13              NUMBER(1),
  PG1_YES14             NUMBER(1),
  PG1_NO14              NUMBER(1),
  PG1_YES15             NUMBER(1),
  PG1_NO15              NUMBER(1),
  PG1_YES16             NUMBER(1),
  PG1_NO16              NUMBER(1),
  PG1_YES17             NUMBER(1),
  PG1_NO17              NUMBER(1),
  PG1_YES18             NUMBER(1),
  PG1_NO18              NUMBER(1),
  PG1_YES19             NUMBER(1),
  PG1_NO19              NUMBER(1),
  PG1_YES20             NUMBER(1),
  PG1_NO20              NUMBER(1),
  PG1_YES21             NUMBER(1),
  PG1_NO21              NUMBER(1),
  PG1_YES22             NUMBER(1),
  PG1_NO22              NUMBER(1),
  PG1_YES23             NUMBER(1),
  PG1_NO23              NUMBER(1),
  PG1_YES24             NUMBER(1),
  PG1_NO24              NUMBER(1),
  PG1_YES25             NUMBER(1),
  PG1_NO25              NUMBER(1),
  PG1_BOX25             VARCHAR2(25),
  PG1_YES26             NUMBER(1),
  PG1_NO26              NUMBER(1),
  PG1_YES27             NUMBER(1),
  PG1_NO27              NUMBER(1),
  PG1_YES28             NUMBER(1),
  PG1_NO28              NUMBER(1),
  PG1_YES29             NUMBER(1),
  PG1_NO29              NUMBER(1),
  PG1_YES30             NUMBER(1),
  PG1_NO30              NUMBER(1),
  PG1_YES31             NUMBER(1),
  PG1_NO31              NUMBER(1),
  PG1_YES32             NUMBER(1),
  PG1_NO32              NUMBER(1),
  PG1_YES33             NUMBER(1),
  PG1_NO33              NUMBER(1),
  PG1_YES34             NUMBER(1),
  PG1_NO34              NUMBER(1),
  PG1_YES35             NUMBER(1),
  PG1_NO35              NUMBER(1),
  PG1_YES36             NUMBER(1),
  PG1_NO36              NUMBER(1),
  PG1_YES37OFF          NUMBER(1),
  PG1_NO37OFF           NUMBER(1),
  PG1_YES37ACC          NUMBER(1),
  PG1_NO37ACC           NUMBER(1),
  PG1_IDT38             NUMBER(1),
  PG1_IDT39             NUMBER(1),
  PG1_IDT40             NUMBER(1),
  PG1_IDT41             NUMBER(1),
  PG1_IDT42             NUMBER(1),
  PG1_BOX42             VARCHAR2(20),
  PG1_PDT43             NUMBER(1),
  PG1_PDT44             NUMBER(1),
  PG1_PDT45             NUMBER(1),
  PG1_PDT46             NUMBER(1),
  PG1_PDT47             NUMBER(1),
  PG1_PDT48             NUMBER(1),
  C_RISKFACTORS         VARCHAR2(4000),
  PG1_HT                VARCHAR2(6),
  PG1_WT                VARCHAR2(6),
  C_PPWT                VARCHAR2(6),
  PG1_BP                VARCHAR2(10),
  PG1_HEAD              NUMBER(1),
  PG1_THYROID           NUMBER(1),
  PG1_CHEST             NUMBER(1),
  PG1_BREASTS           NUMBER(1),
  PG1_CARDIO            NUMBER(1),
  PG1_ABDOMEN           NUMBER(1),
  PG1_VARI              NUMBER(1),
  PG1_NEURO             NUMBER(1),
  PG1_PELVIC            NUMBER(1),
  PG1_EXTGEN            NUMBER(1),
  PG1_CERVIX            NUMBER(1),
  PG1_UTERUS            NUMBER(1),
  PG1_UTERUSBOX         VARCHAR2(3),
  PG1_ADNEXA            NUMBER(1),
  PG1_COMMENTSAR1       VARCHAR2(4000),
  AR2_ETSS              VARCHAR2(10),
  AR2_HB                VARCHAR2(10),
  AR2_MCV               VARCHAR2(10),
  AR2_MSS               VARCHAR2(10),
  AR2_RUBELLA           VARCHAR2(5),
  AR2_HBS               VARCHAR2(6),
  AR2_VDRL              VARCHAR2(6),
  AR2_BLOODGROUP        VARCHAR2(6),
  AR2_RH                VARCHAR2(6),
  AR2_ANTIBODIES        VARCHAR2(6),
  AR2_RHIG              VARCHAR2(6),
  PG2_DATE1             DATE,
  PG2_GEST1             VARCHAR2(6),
  PG2_HT1               VARCHAR2(6),
  PG2_WT1               VARCHAR2(6),
  PG2_PRESN1            VARCHAR2(6),
  PG2_FHR1              VARCHAR2(6),
  PG2_URINEPR1          VARCHAR2(3),
  PG2_URINEGL1          VARCHAR2(3),
  PG2_BP1               VARCHAR2(8),
  PG2_COMMENTS1         VARCHAR2(255),
  PG2_CIG1              VARCHAR2(3),
  PG2_DATE2             DATE,
  PG2_GEST2             VARCHAR2(6),
  PG2_HT2               VARCHAR2(6),
  PG2_WT2               VARCHAR2(6),
  PG2_PRESN2            VARCHAR2(6),
  PG2_FHR2              VARCHAR2(6),
  PG2_URINEPR2          VARCHAR2(3),
  PG2_URINEGL2          VARCHAR2(3),
  PG2_BP2               VARCHAR2(8),
  PG2_COMMENTS2         VARCHAR2(255),
  PG2_CIG2              VARCHAR2(3),
  PG2_DATE3             DATE,
  PG2_GEST3             VARCHAR2(6),
  PG2_HT3               VARCHAR2(6),
  PG2_WT3               VARCHAR2(6),
  PG2_PRESN3            VARCHAR2(6),
  PG2_FHR3              VARCHAR2(6),
  PG2_URINEPR3          VARCHAR2(3),
  PG2_URINEGL3          VARCHAR2(3),
  PG2_BP3               VARCHAR2(8),
  PG2_COMMENTS3         VARCHAR2(255),
  PG2_CIG3              VARCHAR2(3),
  PG2_DATE4             DATE,
  PG2_GEST4             VARCHAR2(6),
  PG2_HT4               VARCHAR2(6),
  PG2_WT4               VARCHAR2(6),
  PG2_PRESN4            VARCHAR2(6),
  PG2_FHR4              VARCHAR2(6),
  PG2_URINEPR4          VARCHAR2(3),
  PG2_URINEGL4          VARCHAR2(3),
  PG2_BP4               VARCHAR2(8),
  PG2_COMMENTS4         VARCHAR2(255),
  PG2_CIG4              VARCHAR2(3),
  PG2_DATE5             DATE,
  PG2_GEST5             VARCHAR2(6),
  PG2_HT5               VARCHAR2(6),
  PG2_WT5               VARCHAR2(6),
  PG2_PRESN5            VARCHAR2(6),
  PG2_FHR5              VARCHAR2(6),
  PG2_URINEPR5          VARCHAR2(3),
  PG2_URINEGL5          VARCHAR2(3),
  PG2_BP5               VARCHAR2(8),
  PG2_COMMENTS5         VARCHAR2(255),
  PG2_CIG5              VARCHAR2(3),
  PG2_DATE6             DATE,
  PG2_GEST6             VARCHAR2(6),
  PG2_HT6               VARCHAR2(6),
  PG2_WT6               VARCHAR2(6),
  PG2_PRESN6            VARCHAR2(6),
  PG2_FHR6              VARCHAR2(6),
  PG2_URINEPR6          VARCHAR2(3),
  PG2_URINEGL6          VARCHAR2(3),
  PG2_BP6               VARCHAR2(8),
  PG2_COMMENTS6         VARCHAR2(255),
  PG2_CIG6              VARCHAR2(3),
  PG2_DATE7             DATE,
  PG2_GEST7             VARCHAR2(6),
  PG2_HT7               VARCHAR2(6),
  PG2_WT7               VARCHAR2(6),
  PG2_PRESN7            VARCHAR2(6),
  PG2_FHR7              VARCHAR2(6),
  PG2_URINEPR7          VARCHAR2(3),
  PG2_URINEGL7          VARCHAR2(3),
  PG2_BP7               VARCHAR2(8),
  PG2_COMMENTS7         VARCHAR2(255),
  PG2_CIG7              VARCHAR2(3),
  PG2_DATE8             DATE,
  PG2_GEST8             VARCHAR2(6),
  PG2_HT8               VARCHAR2(6),
  PG2_WT8               VARCHAR2(6),
  PG2_PRESN8            VARCHAR2(6),
  PG2_FHR8              VARCHAR2(6),
  PG2_URINEPR8          VARCHAR2(3),
  PG2_URINEGL8          VARCHAR2(3),
  PG2_BP8               VARCHAR2(8),
  PG2_COMMENTS8         VARCHAR2(255),
  PG2_CIG8              VARCHAR2(3),
  PG2_DATE9             DATE,
  PG2_GEST9             VARCHAR2(6),
  PG2_HT9               VARCHAR2(6),
  PG2_WT9               VARCHAR2(6),
  PG2_PRESN9            VARCHAR2(6),
  PG2_FHR9              VARCHAR2(6),
  PG2_URINEPR9          VARCHAR2(3),
  PG2_URINEGL9          VARCHAR2(3),
  PG2_BP9               VARCHAR2(8),
  PG2_COMMENTS9         VARCHAR2(255),
  PG2_CIG9              VARCHAR2(3),
  PG2_DATE10            DATE,
  PG2_GEST10            VARCHAR2(6),
  PG2_HT10              VARCHAR2(6),
  PG2_WT10              VARCHAR2(6),
  PG2_PRESN10           VARCHAR2(6),
  PG2_FHR10             VARCHAR2(6),
  PG2_URINEPR10         VARCHAR2(3),
  PG2_URINEGL10         VARCHAR2(3),
  PG2_BP10              VARCHAR2(8),
  PG2_COMMENTS10        VARCHAR2(255),
  PG2_CIG10             VARCHAR2(3),
  PG2_DATE11            DATE,
  PG2_GEST11            VARCHAR2(6),
  PG2_HT11              VARCHAR2(6),
  PG2_WT11              VARCHAR2(6),
  PG2_PRESN11           VARCHAR2(6),
  PG2_FHR11             VARCHAR2(6),
  PG2_URINEPR11         VARCHAR2(3),
  PG2_URINEGL11         VARCHAR2(3),
  PG2_BP11              VARCHAR2(8),
  PG2_COMMENTS11        VARCHAR2(255),
  PG2_CIG11             VARCHAR2(3),
  PG2_DATE12            DATE,
  PG2_GEST12            VARCHAR2(6),
  PG2_HT12              VARCHAR2(6),
  PG2_WT12              VARCHAR2(6),
  PG2_PRESN12           VARCHAR2(6),
  PG2_FHR12             VARCHAR2(6),
  PG2_URINEPR12         VARCHAR2(3),
  PG2_URINEGL12         VARCHAR2(3),
  PG2_BP12              VARCHAR2(8),
  PG2_COMMENTS12        VARCHAR2(255),
  PG2_CIG12             VARCHAR2(3),
  PG2_DATE13            DATE,
  PG2_GEST13            VARCHAR2(6),
  PG2_HT13              VARCHAR2(6),
  PG2_WT13              VARCHAR2(6),
  PG2_PRESN13           VARCHAR2(6),
  PG2_FHR13             VARCHAR2(6),
  PG2_URINEPR13         VARCHAR2(3),
  PG2_URINEGL13         VARCHAR2(3),
  PG2_BP13              VARCHAR2(8),
  PG2_COMMENTS13        VARCHAR2(255),
  PG2_CIG13             VARCHAR2(3),
  PG2_DATE14            DATE,
  PG2_GEST14            VARCHAR2(6),
  PG2_HT14              VARCHAR2(6),
  PG2_WT14              VARCHAR2(6),
  PG2_PRESN14           VARCHAR2(6),
  PG2_FHR14             VARCHAR2(6),
  PG2_URINEPR14         VARCHAR2(3),
  PG2_URINEGL14         VARCHAR2(3),
  PG2_BP14              VARCHAR2(8),
  PG2_COMMENTS14        VARCHAR2(255),
  PG2_CIG14             VARCHAR2(3),
  PG2_DATE15            DATE,
  PG2_GEST15            VARCHAR2(6),
  PG2_HT15              VARCHAR2(6),
  PG2_WT15              VARCHAR2(6),
  PG2_PRESN15           VARCHAR2(6),
  PG2_FHR15             VARCHAR2(6),
  PG2_URINEPR15         VARCHAR2(3),
  PG2_URINEGL15         VARCHAR2(3),
  PG2_BP15              VARCHAR2(8),
  PG2_COMMENTS15        VARCHAR2(255),
  PG2_CIG15             VARCHAR2(3),
  PG2_DATE16            DATE,
  PG2_GEST16            VARCHAR2(6),
  PG2_HT16              VARCHAR2(6),
  PG2_WT16              VARCHAR2(6),
  PG2_PRESN16           VARCHAR2(6),
  PG2_FHR16             VARCHAR2(6),
  PG2_URINEPR16         VARCHAR2(3),
  PG2_URINEGL16         VARCHAR2(3),
  PG2_BP16              VARCHAR2(8),
  PG2_COMMENTS16        VARCHAR2(255),
  PG2_CIG16             VARCHAR2(3),
  PG2_DATE17            DATE,
  PG2_GEST17            VARCHAR2(6),
  PG2_HT17              VARCHAR2(6),
  PG2_WT17              VARCHAR2(6),
  PG2_PRESN17           VARCHAR2(6),
  PG2_FHR17             VARCHAR2(6),
  PG2_URINEPR17         VARCHAR2(3),
  PG2_URINEGL17         VARCHAR2(3),
  PG2_BP17              VARCHAR2(8),
  PG2_COMMENTS17        VARCHAR2(255),
  PG2_CIG17             VARCHAR2(3),
  PG3_DATE18            DATE,
  PG3_GEST18            VARCHAR2(6),
  PG3_HT18              VARCHAR2(6),
  PG3_WT18              VARCHAR2(6),
  PG3_PRESN18           VARCHAR2(6),
  PG3_FHR18             VARCHAR2(6),
  PG3_URINEPR18         VARCHAR2(3),
  PG3_URINEGL18         VARCHAR2(3),
  PG3_BP18              VARCHAR2(8),
  PG3_COMMENTS18        VARCHAR2(255),
  PG3_CIG18             VARCHAR2(3),
  PG3_DATE19            DATE,
  PG3_GEST19            VARCHAR2(6),
  PG3_HT19              VARCHAR2(6),
  PG3_WT19              VARCHAR2(6),
  PG3_PRESN19           VARCHAR2(6),
  PG3_FHR19             VARCHAR2(6),
  PG3_URINEPR19         VARCHAR2(3),
  PG3_URINEGL19         VARCHAR2(3),
  PG3_BP19              VARCHAR2(8),
  PG3_COMMENTS19        VARCHAR2(255),
  PG3_CIG19             VARCHAR2(3),
  PG3_DATE20            DATE,
  PG3_GEST20            VARCHAR2(6),
  PG3_HT20              VARCHAR2(6),
  PG3_WT20              VARCHAR2(6),
  PG3_PRESN20           VARCHAR2(6),
  PG3_FHR20             VARCHAR2(6),
  PG3_URINEPR20         VARCHAR2(3),
  PG3_URINEGL20         VARCHAR2(3),
  PG3_BP20              VARCHAR2(8),
  PG3_COMMENTS20        VARCHAR2(255),
  PG3_CIG20             VARCHAR2(3),
  PG3_DATE21            DATE,
  PG3_GEST21            VARCHAR2(6),
  PG3_HT21              VARCHAR2(6),
  PG3_WT21              VARCHAR2(6),
  PG3_PRESN21           VARCHAR2(6),
  PG3_FHR21             VARCHAR2(6),
  PG3_URINEPR21         VARCHAR2(3),
  PG3_URINEGL21         VARCHAR2(3),
  PG3_BP21              VARCHAR2(8),
  PG3_COMMENTS21        VARCHAR2(255),
  PG3_CIG21             VARCHAR2(3),
  PG3_DATE22            DATE,
  PG3_GEST22            VARCHAR2(6),
  PG3_HT22              VARCHAR2(6),
  PG3_WT22              VARCHAR2(6),
  PG3_PRESN22           VARCHAR2(6),
  PG3_FHR22             VARCHAR2(6),
  PG3_URINEPR22         VARCHAR2(3),
  PG3_URINEGL22         VARCHAR2(3),
  PG3_BP22              VARCHAR2(8),
  PG3_COMMENTS22        VARCHAR2(255),
  PG3_CIG22             VARCHAR2(3),
  PG3_DATE23            DATE,
  PG3_GEST23            VARCHAR2(6),
  PG3_HT23              VARCHAR2(6),
  PG3_WT23              VARCHAR2(6),
  PG3_PRESN23           VARCHAR2(6),
  PG3_FHR23             VARCHAR2(6),
  PG3_URINEPR23         VARCHAR2(3),
  PG3_URINEGL23         VARCHAR2(3),
  PG3_BP23              VARCHAR2(8),
  PG3_COMMENTS23        VARCHAR2(255),
  PG3_CIG23             VARCHAR2(3),
  PG3_DATE24            DATE,
  PG3_GEST24            VARCHAR2(6),
  PG3_HT24              VARCHAR2(6),
  PG3_WT24              VARCHAR2(6),
  PG3_PRESN24           VARCHAR2(6),
  PG3_FHR24             VARCHAR2(6),
  PG3_URINEPR24         VARCHAR2(3),
  PG3_URINEGL24         VARCHAR2(3),
  PG3_BP24              VARCHAR2(8),
  PG3_COMMENTS24        VARCHAR2(255),
  PG3_CIG24             VARCHAR2(3),
  PG3_DATE25            DATE,
  PG3_GEST25            VARCHAR2(6),
  PG3_HT25              VARCHAR2(6),
  PG3_WT25              VARCHAR2(6),
  PG3_PRESN25           VARCHAR2(6),
  PG3_FHR25             VARCHAR2(6),
  PG3_URINEPR25         VARCHAR2(3),
  PG3_URINEGL25         VARCHAR2(3),
  PG3_BP25              VARCHAR2(8),
  PG3_COMMENTS25        VARCHAR2(255),
  PG3_CIG25             VARCHAR2(3),
  PG3_DATE26            DATE,
  PG3_GEST26            VARCHAR2(6),
  PG3_HT26              VARCHAR2(6),
  PG3_WT26              VARCHAR2(6),
  PG3_PRESN26           VARCHAR2(6),
  PG3_FHR26             VARCHAR2(6),
  PG3_URINEPR26         VARCHAR2(3),
  PG3_URINEGL26         VARCHAR2(3),
  PG3_BP26              VARCHAR2(8),
  PG3_COMMENTS26        VARCHAR2(255),
  PG3_CIG26             VARCHAR2(3),
  PG3_DATE27            DATE,
  PG3_GEST27            VARCHAR2(6),
  PG3_HT27              VARCHAR2(6),
  PG3_WT27              VARCHAR2(6),
  PG3_PRESN27           VARCHAR2(6),
  PG3_FHR27             VARCHAR2(6),
  PG3_URINEPR27         VARCHAR2(3),
  PG3_URINEGL27         VARCHAR2(3),
  PG3_BP27              VARCHAR2(8),
  PG3_COMMENTS27        VARCHAR2(255),
  PG3_CIG27             VARCHAR2(3),
  PG3_DATE28            DATE,
  PG3_GEST28            VARCHAR2(6),
  PG3_HT28              VARCHAR2(6),
  PG3_WT28              VARCHAR2(6),
  PG3_PRESN28           VARCHAR2(6),
  PG3_FHR28             VARCHAR2(6),
  PG3_URINEPR28         VARCHAR2(3),
  PG3_URINEGL28         VARCHAR2(3),
  PG3_BP28              VARCHAR2(8),
  PG3_COMMENTS28        VARCHAR2(255),
  PG3_CIG28             VARCHAR2(3),
  PG3_DATE29            DATE,
  PG3_GEST29            VARCHAR2(6),
  PG3_HT29              VARCHAR2(6),
  PG3_WT29              VARCHAR2(6),
  PG3_PRESN29           VARCHAR2(6),
  PG3_FHR29             VARCHAR2(6),
  PG3_URINEPR29         VARCHAR2(3),
  PG3_URINEGL29         VARCHAR2(3),
  PG3_BP29              VARCHAR2(8),
  PG3_COMMENTS29        VARCHAR2(255),
  PG3_CIG29             VARCHAR2(3),
  PG3_DATE30            DATE,
  PG3_GEST30            VARCHAR2(6),
  PG3_HT30              VARCHAR2(6),
  PG3_WT30              VARCHAR2(6),
  PG3_PRESN30           VARCHAR2(6),
  PG3_FHR30             VARCHAR2(6),
  PG3_URINEPR30         VARCHAR2(3),
  PG3_URINEGL30         VARCHAR2(3),
  PG3_BP30              VARCHAR2(8),
  PG3_COMMENTS30        VARCHAR2(255),
  PG3_CIG30             VARCHAR2(3),
  PG3_DATE31            DATE,
  PG3_GEST31            VARCHAR2(6),
  PG3_HT31              VARCHAR2(6),
  PG3_WT31              VARCHAR2(6),
  PG3_PRESN31           VARCHAR2(6),
  PG3_FHR31             VARCHAR2(6),
  PG3_URINEPR31         VARCHAR2(3),
  PG3_URINEGL31         VARCHAR2(3),
  PG3_BP31              VARCHAR2(8),
  PG3_COMMENTS31        VARCHAR2(255),
  PG3_CIG31             VARCHAR2(3),
  PG3_DATE32            DATE,
  PG3_GEST32            VARCHAR2(6),
  PG3_HT32              VARCHAR2(6),
  PG3_WT32              VARCHAR2(6),
  PG3_PRESN32           VARCHAR2(6),
  PG3_FHR32             VARCHAR2(6),
  PG3_URINEPR32         VARCHAR2(3),
  PG3_URINEGL32         VARCHAR2(3),
  PG3_BP32              VARCHAR2(8),
  PG3_COMMENTS32        VARCHAR2(255),
  PG3_CIG32             VARCHAR2(3),
  PG3_DATE33            DATE,
  PG3_GEST33            VARCHAR2(6),
  PG3_HT33              VARCHAR2(6),
  PG3_WT33              VARCHAR2(6),
  PG3_PRESN33           VARCHAR2(6),
  PG3_FHR33             VARCHAR2(6),
  PG3_URINEPR33         VARCHAR2(3),
  PG3_URINEGL33         VARCHAR2(3),
  PG3_BP33              VARCHAR2(8),
  PG3_COMMENTS33        VARCHAR2(255),
  PG3_CIG33             VARCHAR2(3),
  PG3_DATE34            DATE,
  PG3_GEST34            VARCHAR2(6),
  PG3_HT34              VARCHAR2(6),
  PG3_WT34              VARCHAR2(6),
  PG3_PRESN34           VARCHAR2(6),
  PG3_FHR34             VARCHAR2(6),
  PG3_URINEPR34         VARCHAR2(3),
  PG3_URINEGL34         VARCHAR2(3),
  PG3_BP34              VARCHAR2(8),
  PG3_COMMENTS34        VARCHAR2(255),
  PG3_CIG34             VARCHAR2(3),
  AR2_OBSTETRICIAN      NUMBER(1),
  AR2_PEDIATRICIAN      NUMBER(1),
  AR2_ANESTHESIOLOGIST  NUMBER(1),
  AR2_SOCIALWORKER      NUMBER(1),
  AR2_DIETICIAN         NUMBER(1),
  AR2_OTHERAR2          NUMBER(1),
  AR2_OTHERBOX          VARCHAR2(35),
  AR2_DRUGUSE           NUMBER(1),
  AR2_SMOKING           NUMBER(1),
  AR2_ALCOHOL           NUMBER(1),
  AR2_EXERCISE          NUMBER(1),
  AR2_WORKPLAN          NUMBER(1),
  AR2_INTERCOURSE       NUMBER(1),
  AR2_DENTAL            NUMBER(1),
  AR2_TRAVEL            NUMBER(1),
  AR2_PRENATAL          NUMBER(1),
  AR2_BREAST            NUMBER(1),
  AR2_BIRTH             NUMBER(1),
  AR2_PRETERM           NUMBER(1),
  AR2_PROM              NUMBER(1),
  AR2_FETAL             NUMBER(1),
  AR2_ADMISSION         NUMBER(1),
  AR2_LABOUR            NUMBER(1),
  AR2_PAIN              NUMBER(1),
  AR2_DEPRESSION        NUMBER(1),
  AR2_CIRCUMCISION      NUMBER(1),
  AR2_CAR               NUMBER(1),
  AR2_CONTRACEPTION     NUMBER(1),
  AR2_ONCALL            NUMBER(1),
  AR2_UDATE1            VARCHAR2(10),
  AR2_UGA1              VARCHAR2(10),
  AR2_URESULTS1         VARCHAR2(25),
  AR2_UDATE2            VARCHAR2(10),
  AR2_UGA2              VARCHAR2(10),
  AR2_URESULTS2         VARCHAR2(25),
  AR2_UDATE3            VARCHAR2(10),
  AR2_UGA3              VARCHAR2(10),
  AR2_URESULTS3         VARCHAR2(25),
  AR2_UDATE4            VARCHAR2(10),
  AR2_UGA4              VARCHAR2(10),
  AR2_URESULTS4         VARCHAR2(25),
  AR2_PAP               VARCHAR2(20),
  AR2_COMMENTSAR2       VARCHAR2(4000),
  AR2_CHLAMYDIA         VARCHAR2(10),
  AR2_HIV               VARCHAR2(10),
  AR2_VAGINOSIS         VARCHAR2(10),
  AR2_STREP             VARCHAR2(10),
  AR2_URINECULTURE      VARCHAR2(10),
  AR2_SICKLEDEX         VARCHAR2(10),
  AR2_ELECTRO           VARCHAR2(10),
  AR2_AMNIO             VARCHAR2(10),
  AR2_GLUCOSE           VARCHAR2(10),
  AR2_OTHERAR2NAME      VARCHAR2(20),
  AR2_OTHERRESULT       VARCHAR2(10),
  AR2_PSYCH             VARCHAR2(25),
  PG1_SIGNATURE         VARCHAR2(50),
  PG2_SIGNATURE         VARCHAR2(50),
  PG3_SIGNATURE         VARCHAR2(50),
  PG1_FORMDATE          DATE,
  PG2_FORMDATE          DATE,
  PG3_FORMDATE          DATE
)
;
alter table FORMAR
  add primary key (ID);

prompt
prompt Creating table FORMBCAR2007
prompt ===========================
prompt
create table FORMBCAR2007
(
  ID                       NUMBER(10) not null,
  DEMOGRAPHIC_NO           NUMBER(10) not null,
  PROVIDER_NO              NUMBER(10),
  FORMCREATED              DATE,
  FORMEDITED               TIMESTAMP(6) default CURRENT_TIMESTAMP,
  C_LASTVISITED            CHAR(3),
  C_HOSPITAL               VARCHAR2(60),
  PG1_PRICARE              VARCHAR2(60),
  PG1_FAMPHY               VARCHAR2(60),
  PG1_MONAME               VARCHAR2(60),
  PG1_DATEOFBIRTH          DATE,
  PG1_AGEATEDD             VARCHAR2(2),
  PG1_MAIDENNAME           VARCHAR2(60),
  PG1_ETHORIG              VARCHAR2(60),
  PG1_LANGPREF             VARCHAR2(60),
  PG1_PARTNERNAME          VARCHAR2(60),
  PG1_PARTNERAGE           VARCHAR2(2),
  PG1_FAETHORIG            VARCHAR2(50),
  C_SURNAME                VARCHAR2(30),
  C_GIVENNAME              VARCHAR2(30),
  C_ADDRESS                VARCHAR2(60),
  C_CITY                   VARCHAR2(60),
  C_PROVINCE               VARCHAR2(50),
  C_POSTAL                 VARCHAR2(8),
  C_PHONE                  VARCHAR2(60),
  C_PHONEALT1              VARCHAR2(60),
  C_PHONEALT2              VARCHAR2(60),
  C_PHN                    VARCHAR2(20),
  PG1_GRAVIDA              VARCHAR2(4),
  PG1_TERM                 VARCHAR2(4),
  PG1_PRETERM              VARCHAR2(4),
  PG1_ABORTION             VARCHAR2(3),
  PG1_INDUCED              VARCHAR2(3),
  PG1_SPONTANEOUS          VARCHAR2(3),
  PG1_LIVING               VARCHAR2(3),
  PG1_OBHISTDATE1          VARCHAR2(10),
  PG1_BIRTHORABORT1        VARCHAR2(20),
  PG1_DELIWEEK1            VARCHAR2(8),
  PG1_LABOHR1              VARCHAR2(8),
  PG1_DELITYPE1            VARCHAR2(30),
  PG1_PERICOMP1            VARCHAR2(80),
  PG1_OBHISTSEX1           VARCHAR2(1),
  PG1_BIRTHWEIT1           VARCHAR2(8),
  PG1_BIRTHWEITUNITS1      VARCHAR2(3),
  PG1_PRESHEALTH1          VARCHAR2(8),
  PG1_OBHISTDATE2          VARCHAR2(10),
  PG1_BIRTHORABORT2        VARCHAR2(20),
  PG1_DELIWEEK2            VARCHAR2(8),
  PG1_LABOHR2              VARCHAR2(8),
  PG1_DELITYPE2            VARCHAR2(30),
  PG1_PERICOMP2            VARCHAR2(80),
  PG1_OBHISTSEX2           VARCHAR2(1),
  PG1_BIRTHWEIT2           VARCHAR2(8),
  PG1_BIRTHWEITUNITS2      VARCHAR2(3),
  PG1_PRESHEALTH2          VARCHAR2(8),
  PG1_OBHISTDATE3          VARCHAR2(10),
  PG1_BIRTHORABORT3        VARCHAR2(20),
  PG1_DELIWEEK3            VARCHAR2(8),
  PG1_LABOHR3              VARCHAR2(8),
  PG1_DELITYPE3            VARCHAR2(30),
  PG1_PERICOMP3            VARCHAR2(80),
  PG1_OBHISTSEX3           VARCHAR2(1),
  PG1_BIRTHWEIT3           VARCHAR2(8),
  PG1_BIRTHWEITUNITS3      VARCHAR2(3),
  PG1_PRESHEALTH3          VARCHAR2(8),
  PG1_OBHISTDATE4          VARCHAR2(10),
  PG1_BIRTHORABORT4        VARCHAR2(20),
  PG1_DELIWEEK4            VARCHAR2(8),
  PG1_LABOHR4              VARCHAR2(8),
  PG1_DELITYPE4            VARCHAR2(30),
  PG1_PERICOMP4            VARCHAR2(80),
  PG1_OBHISTSEX4           VARCHAR2(1),
  PG1_BIRTHWEIT4           VARCHAR2(8),
  PG1_BIRTHWEITUNITS4      VARCHAR2(3),
  PG1_PRESHEALTH4          VARCHAR2(8),
  PG1_OBHISTDATE5          VARCHAR2(10),
  PG1_BIRTHORABORT5        VARCHAR2(20),
  PG1_DELIWEEK5            VARCHAR2(8),
  PG1_LABOHR5              VARCHAR2(8),
  PG1_DELITYPE5            VARCHAR2(30),
  PG1_PERICOMP5            VARCHAR2(80),
  PG1_OBHISTSEX5           VARCHAR2(1),
  PG1_BIRTHWEIT5           VARCHAR2(8),
  PG1_BIRTHWEITUNITS5      VARCHAR2(3),
  PG1_PRESHEALTH5          VARCHAR2(8),
  PG1_LMP                  DATE,
  PG1_MENSCYCLE            VARCHAR2(8),
  PG1_EDDBYDATE            DATE,
  PG1_CONTRMETHOD          VARCHAR2(15),
  PG1_STOPDATE             VARCHAR2(10),
  PG1_EDDBYUS              DATE,
  PG1_EDDBYUSPERF          NUMBER(1),
  PG1_EDDBYUSGESTWKS       VARCHAR2(2),
  PG1_EDDBYUSGESTDAYS      VARCHAR2(2),
  PG1_ALLERGYN             NUMBER(1),
  PG1_ALLERGYY             NUMBER(1),
  PG1_ALLERGY              VARCHAR2(50),
  PG1_CURMEDIC             VARCHAR2(255),
  PG1_BELIPRACT            VARCHAR2(60),
  PG1_IVFPREGNANCY         NUMBER(1),
  PG1_IVFPREGNANCYSPEC     VARCHAR2(30),
  PG1_BLEEDING             NUMBER(1),
  PG1_BLEEDINGSPEC         VARCHAR2(30),
  PG1_NAUSEA               NUMBER(1),
  PG1_NAUSEASPEC           VARCHAR2(30),
  PG1_INFECT               NUMBER(1),
  PG1_INFECTSPEC           VARCHAR2(30),
  PG1_PREPREGOTHER         NUMBER(1),
  PG1_PREPREGOTHERSPEC     VARCHAR2(30),
  PG1_IFVPREGNANCY         NUMBER(1),
  PG1_IFVPREGNANCYSPEC     VARCHAR2(30),
  PG1_HEARTDISE            NUMBER(1),
  PG1_HEARTDISESPEC        VARCHAR2(30),
  PG1_HYPERTS              NUMBER(1),
  PG1_HYPERTSSPEC          VARCHAR2(30),
  PG1_DIABETE              NUMBER(1),
  PG1_DIABETESPEC          VARCHAR2(30),
  PG1_DEPRPSYCHIAT         NUMBER(1),
  PG1_DEPRPSYCHIATSPEC     VARCHAR2(30),
  PG1_ALCOHDRUG            NUMBER(1),
  PG1_ALCOHDRUGSPEC        VARCHAR2(30),
  PG1_THROMCOAG            NUMBER(1),
  PG1_THROMCOAGSPEC        VARCHAR2(30),
  PG1_INHERDISEASE         NUMBER(1),
  PG1_INHERDISEASESPEC     VARCHAR2(30),
  PG1_ETHNIC               NUMBER(1),
  PG1_ETHNICSPEC           VARCHAR2(30),
  PG1_FAMHISTOTHER         NUMBER(1),
  PG1_FAMHISTOTHERSPEC     VARCHAR2(30),
  PG1_OPERATION            NUMBER(1),
  PG1_OPERATIONSPEC        VARCHAR2(255),
  PG1_CVORRESP             NUMBER(1),
  PG1_CVORRESPSPEC         VARCHAR2(255),
  PG1_ANESTHETIC           NUMBER(1),
  PG1_ANESTHETICSPEC       VARCHAR2(40),
  PG1_UTERINECXPROC        NUMBER(1),
  PG1_UTERINECXPROCSPEC    VARCHAR2(40),
  PG1_INFECTSTD            NUMBER(1),
  PG1_INFECTSTDSPEC        VARCHAR2(40),
  PG1_SUSCHIPOX            NUMBER(1),
  PG1_SUSCHIPOXSPEC        VARCHAR2(40),
  PG1_THRCOAG              NUMBER(1),
  PG1_THRCOAGSPEC          VARCHAR2(40),
  PG1_HYPER                NUMBER(1),
  PG1_HYPERSPEC            VARCHAR2(40),
  PG1_PIGI                 NUMBER(1),
  PG1_PIGISPEC             VARCHAR2(40),
  PG1_PIURIN               NUMBER(1),
  PG1_PIURINSPEC           VARCHAR2(40),
  PG1_DBENDOC              NUMBER(1),
  PG1_DBENDOCSPEC          VARCHAR2(40),
  PG1_SEIZNEUR             NUMBER(1),
  PG1_SEIZNEURSPEC         VARCHAR2(40),
  PG1_DEPRPSY              NUMBER(1),
  PG1_DEPRPSYSPEC          VARCHAR2(255),
  PG1_HXANXIETY            NUMBER(1),
  PG1_HXDEPR               NUMBER(1),
  PG1_HXBIPOLAR            NUMBER(1),
  PG1_HXPPDEPR             NUMBER(1),
  PG1_HXUNKNOWN            NUMBER(1),
  PG1_HXOTHER              NUMBER(1),
  PG1_PIOTHER              NUMBER(1),
  PG1_PIOTHERSPEC          VARCHAR2(40),
  PG1_NUTRITION            NUMBER(1),
  PG1_NUTRITIONSPEC        VARCHAR2(40),
  PG1_NUTRITIONREF         NUMBER(1),
  PG1_FOLIACIDREF          NUMBER(1),
  PG1_STPWRKDATE           NUMBER(1),
  PG1_STPWRKDATESPEC       VARCHAR2(30),
  PG1_STPWRKDATEREF        NUMBER(1),
  PG1_FOLIACID             NUMBER(1),
  PG1_FOLIACIDSPEC         VARCHAR2(30),
  PG1_ALCO                 NUMBER(1),
  PG1_ALCONEVER            NUMBER(1),
  PG1_ALCOQUIT             NUMBER(1),
  PG1_ALCOQUITDATE         DATE,
  PG1_ALCOBEF              VARCHAR2(5),
  PG1_ALCOCURR             VARCHAR2(5),
  PG1_ALCOBINGENO          NUMBER(1),
  PG1_ALCOBINGEYES         NUMBER(1),
  PG1_TWEAK                NUMBER(1),
  PG1_TWEAKSCORE           VARCHAR2(15),
  PG1_DRUG                 NUMBER(1),
  PG1_DRUGSPEC             VARCHAR2(30),
  PG1_DRUGREF              NUMBER(1),
  PG1_SUBUSE               NUMBER(1),
  PG1_SUBUSENO             NUMBER(1),
  PG1_SUBUSEYES            NUMBER(1),
  PG1_SUBUSESPEC           VARCHAR2(35),
  PG1_HEROIN               NUMBER(1),
  PG1_COCAINE              NUMBER(1),
  PG1_MARIJUANA            NUMBER(1),
  PG1_METHADONE            NUMBER(1),
  PG1_SOLVENTS             NUMBER(1),
  PG1_SUBUSEOTHER          NUMBER(1),
  PG1_PRESCRIPTION         NUMBER(1),
  PG1_SUBUSEUNKNOWN        NUMBER(1),
  PG1_IPV                  NUMBER(1),
  PG1_IPVSPEC              VARCHAR2(40),
  PG1_IPVREF               NUMBER(1),
  PG1_SMOKEBEF             NUMBER(1),
  PG1_SMOKEBEFSPEC         VARCHAR2(5),
  PG1_SMOKECUR             NUMBER(1),
  PG1_SMOKECURSPEC         VARCHAR2(5),
  PG1_SMOKENEVER           NUMBER(1),
  PG1_SMOKEQUIT            NUMBER(1),
  PG1_SMOKEQUITDATE        DATE,
  PG1_SECSMOKE             NUMBER(1),
  PG1_SECSMOKENO           NUMBER(1),
  PG1_SECSMOKEYES          NUMBER(1),
  PG1_SECSMOKESPEC         VARCHAR2(30),
  PG1_FINAHOUSE            NUMBER(1),
  PG1_FINAHOUSESPEC        VARCHAR2(30),
  PG1_FINAHOUSEREF         NUMBER(1),
  PG1_SUPSYS               NUMBER(1),
  PG1_SUPSYSSPEC           VARCHAR2(30),
  PG1_SUPSYSREF            NUMBER(1),
  PG1_SCHYEAR              VARCHAR2(3),
  PG1_WORK                 VARCHAR2(25),
  PG1_WORKHOURDAY          VARCHAR2(5),
  PG1_PTWORK               VARCHAR2(40),
  PG1_EXAMINATION          DATE,
  PG1_BP                   VARCHAR2(12),
  PG1_HEADNECK             VARCHAR2(40),
  PG1_MUSCSPINE            VARCHAR2(40),
  PG1_BREANIPP             VARCHAR2(40),
  PG1_VARISKIN             VARCHAR2(40),
  PG1_HEARTLUNG            VARCHAR2(40),
  PG1_PELVIC               VARCHAR2(40),
  PG1_ABDOMEN              VARCHAR2(40),
  PG1_SWABSCERV            VARCHAR2(40),
  PG1_DISBESTCH            NUMBER(1),
  PG1_DISPREEDU            NUMBER(1),
  PG1_DISSEXU              NUMBER(1),
  PG1_DISFDSAFETY          NUMBER(1),
  PG1_DISMSS               NUMBER(1),
  PG1_DISMATPAT            NUMBER(1),
  PG1_DISBELTUSE           NUMBER(1),
  PG1_DISBFY               NUMBER(1),
  PG1_DISBFN               NUMBER(1),
  PG1_DISBFM               NUMBER(1),
  PG1_DISGENCOUNS          NUMBER(1),
  PG1_DISHIV               NUMBER(1),
  PG1_SUMMARY              CLOB,
  PG1_SIGNATURE            VARCHAR2(40),
  AR2_RISKNEONDEATH        NUMBER(1),
  AR2_RISKSTILLBIRTH       NUMBER(1),
  AR2_RISKABORTION         NUMBER(1),
  AR2_RISKHABITABORT       NUMBER(1),
  AR2_RISKPRIPRETBIRTH20   NUMBER(1),
  AR2_RISKPRICESBIRTH      NUMBER(1),
  AR2_RISKPRIIUGR          NUMBER(1),
  AR2_RISKPRIMACR          NUMBER(1),
  AR2_RISKRHIMMUY          NUMBER(1),
  AR2_RISKRHIMMUN          NUMBER(1),
  AR2_RISKMAJCONGANOM      NUMBER(1),
  AR2_RISKPPHEMO           NUMBER(1),
  AR2_RISKCONDIET          NUMBER(1),
  AR2_RISKINSDEPEND        NUMBER(1),
  AR2_RISKRETDOC           NUMBER(1),
  AR2_RISKASYMT            NUMBER(1),
  AR2_RISKSYMT             NUMBER(1),
  AR2_RISK14090            NUMBER(1),
  AR2_RISKHYPERDRUG        NUMBER(1),
  AR2_RISKCHRORENALDISEASE NUMBER(1),
  AR2_RISKUNDER18          NUMBER(1),
  AR2_RISKOVER35           NUMBER(1),
  AR2_RISKUNDERWEIGHT      NUMBER(1),
  AR2_RISKOBESITY          NUMBER(1),
  AR2_RISKH152             NUMBER(1),
  AR2_RISKDEPRE            NUMBER(1),
  AR2_RISKALCODRUG         NUMBER(1),
  AR2_RISKSMOKING          NUMBER(1),
  AR2_RISKOTHERMEDICAL     NUMBER(1),
  AR2_RISKABNSERUM         NUMBER(1),
  AR2_RISKALCODRUGCUR      NUMBER(1),
  AR2_RISKDIAGLARGE        NUMBER(1),
  AR2_RISKDIAGSMALL        NUMBER(1),
  AR2_RISKPOLYHYD          NUMBER(1),
  AR2_RISKMULPREG          NUMBER(1),
  AR2_RISKMALPRES          NUMBER(1),
  AR2_RISKMEMRUPT37        NUMBER(1),
  AR2_RISKBLEEDING         NUMBER(1),
  AR2_RISKFETALMOV         NUMBER(1),
  AR2_RISKDEPRECUR         NUMBER(1),
  AR2_RISKPREGINDHYPERT    NUMBER(1),
  AR2_RISKPROTE1           NUMBER(1),
  AR2_RISKGESDIABETE       NUMBER(1),
  AR2_RISKBLOODANTI        NUMBER(1),
  AR2_RISKANEMIA           NUMBER(1),
  AR2_RISKADMPRETERM       NUMBER(1),
  AR2_RISKPREG42W          NUMBER(1),
  AR2_RISKWTLOSS           NUMBER(1),
  AR2_RISKSMOKINGCUR       NUMBER(1),
  AR2_RISKHYPDISORDER      NUMBER(1),
  AR2_RISKPLACABRUPTION    NUMBER(1),
  AR2_INBIRTHPLACE         VARCHAR2(60),
  AR2_INBIRTHPLACEALT      VARCHAR2(60),
  AR2_LABBLOOD             VARCHAR2(12),
  AR2_LABRH                VARCHAR2(12),
  AR2_LABRUBELLA           VARCHAR2(12),
  AR2_LABPPVAC             NUMBER(1),
  AR2_LABHBSAG             VARCHAR2(12),
  AR2_LABHBSAGN            NUMBER(1),
  AR2_LABHBSAGY            NUMBER(1),
  AR2_LABHBSAGDATE         DATE,
  AR2_LABHBSAGCONTACT      NUMBER(1),
  AR2_LABHBSAGVAC          NUMBER(1),
  AR2_LABHEM1ST            VARCHAR2(12),
  AR2_LABHEM3RD            VARCHAR2(12),
  AR2_URINECS              VARCHAR2(3),
  AR2_LABRATDATE1          DATE,
  AR2_LABRATRES1           VARCHAR2(10),
  AR2_LABRATDATE2          DATE,
  AR2_LABRATRES2           VARCHAR2(10),
  AR2_LABRHIGG             DATE,
  AR2_LABRHIGG2            DATE,
  AR2_LABSTS               VARCHAR2(10),
  AR2_LABHIVTESTN          NUMBER(1),
  AR2_LABHIVTESTY          NUMBER(1),
  AR2_LABHIV               VARCHAR2(10),
  AR2_LABOTHERTEST         VARCHAR2(200),
  AR2_LABOTHERHEPC         VARCHAR2(3),
  AR2_LABOTHERTSH          VARCHAR2(3),
  AR2_LABOTHERVAR          VARCHAR2(3),
  AR2_LABSCREEN            VARCHAR2(10),
  AR2_LABSCREENSPEC        VARCHAR2(255),
  AR2_LABGWEEK             VARCHAR2(5),
  AR2_LABDIABDATE          DATE,
  AR2_LABDIABRES           VARCHAR2(10),
  AR2_LABGGTDATE           DATE,
  AR2_LABGGTRES            VARCHAR2(60),
  AR2_LABGBSDATE           DATE,
  AR2_LABGBSCOPY           NUMBER(1),
  AR2_LABGBSTESTN          NUMBER(1),
  AR2_LABGBSTESTY          NUMBER(1),
  AR2_LABGBSRES            VARCHAR2(10),
  AR2_LABEDINSCORE         VARCHAR2(5),
  AR2_LABEDINDATE          DATE,
  AR2_LABEDINN             NUMBER(1),
  AR2_LABEDINY             NUMBER(1),
  AR2_PROPREG              VARCHAR2(50),
  AR2_PROLABOUR            VARCHAR2(50),
  AR2_PROPOSTPARTUM        VARCHAR2(50),
  AR2_PRONEWBORN           VARCHAR2(50),
  AR2_PROLIFE              VARCHAR2(50),
  AR2_AGE                  VARCHAR2(5),
  AR2_1USOUNDDATE          DATE,
  AR2_GESTAGEUS            VARCHAR2(10),
  AR2_AMNIOCUTOFFY         NUMBER(1),
  AR2_AMNIOCUTOFFN         NUMBER(1),
  PG2_PROBCOMMENT          VARCHAR2(255),
  PG2_INVESTIGATION        CLOB,
  PG3_PROBCOMMENT          VARCHAR2(255),
  PG3_INVESTIGATION        CLOB,
  PG2_DOULA                VARCHAR2(100),
  PG2_DOULANO              VARCHAR2(30),
  PG3_DOULA                VARCHAR2(100),
  PG3_DOULANO              VARCHAR2(30),
  AR2_TOPCALL              NUMBER(1),
  AR2_TOPPRETERM           NUMBER(1),
  AR2_TOPHOSP              NUMBER(1),
  AR2_TOPDOULA             NUMBER(1),
  AR2_TOPSLEEP             NUMBER(1),
  AR2_TOPRISKS             NUMBER(1),
  AR2_TOPMOVE              NUMBER(1),
  AR2_TOPPLAN              NUMBER(1),
  AR2_TOPVBAC              NUMBER(1),
  AR2_TOPSEATS             NUMBER(1),
  AR2_TOPFEED              NUMBER(1),
  AR2_TOPPAIN              NUMBER(1),
  AR2_TOPCSEC              NUMBER(1),
  C_PPWT                   VARCHAR2(5),
  C_PPHT                   VARCHAR2(5),
  C_PPBMI                  VARCHAR2(5),
  C_EDD                    DATE,
  PG2_DATE1                DATE,
  PG2_WT1                  VARCHAR2(5),
  PG2_BMI1                 VARCHAR2(5),
  PG2_BP1                  VARCHAR2(8),
  PG2_URINE1               VARCHAR2(8),
  PG2_URINEG1              VARCHAR2(8),
  PG2_GEST1                VARCHAR2(8),
  PG2_HT1                  VARCHAR2(8),
  PG2_FHRACT1              VARCHAR2(8),
  PG2_FM1                  NUMBER(1),
  PG2_POS1                 VARCHAR2(8),
  PG2_COMMENT1             VARCHAR2(80),
  PG2_RETIN1               VARCHAR2(8),
  PG2_DATE2                DATE,
  PG2_WT2                  VARCHAR2(5),
  PG2_BMI2                 VARCHAR2(5),
  PG2_BP2                  VARCHAR2(8),
  PG2_URINE2               VARCHAR2(8),
  PG2_URINEG2              VARCHAR2(8),
  PG2_GEST2                VARCHAR2(8),
  PG2_HT2                  VARCHAR2(8),
  PG2_FHRACT2              VARCHAR2(8),
  PG2_FM2                  NUMBER(1),
  PG2_POS2                 VARCHAR2(8),
  PG2_COMMENT2             VARCHAR2(80),
  PG2_RETIN2               VARCHAR2(8),
  PG2_DATE3                DATE,
  PG2_WT3                  VARCHAR2(5),
  PG2_BMI3                 VARCHAR2(5),
  PG2_BP3                  VARCHAR2(8),
  PG2_URINE3               VARCHAR2(8),
  PG2_URINEG3              VARCHAR2(8),
  PG2_GEST3                VARCHAR2(8),
  PG2_HT3                  VARCHAR2(8),
  PG2_FHRACT3              VARCHAR2(8),
  PG2_FM3                  NUMBER(1),
  PG2_POS3                 VARCHAR2(8),
  PG2_COMMENT3             VARCHAR2(80),
  PG2_RETIN3               VARCHAR2(8),
  PG2_DATE4                DATE,
  PG2_WT4                  VARCHAR2(5),
  PG2_BMI4                 VARCHAR2(5),
  PG2_BP4                  VARCHAR2(8),
  PG2_URINE4               VARCHAR2(8),
  PG2_URINEG4              VARCHAR2(8),
  PG2_GEST4                VARCHAR2(8),
  PG2_HT4                  VARCHAR2(8),
  PG2_FHRACT4              VARCHAR2(8),
  PG2_FM4                  NUMBER(1),
  PG2_POS4                 VARCHAR2(8),
  PG2_SENTHOSP20           NUMBER(1),
  PG2_TOPATIENT20          NUMBER(1),
  PG2_COMMENT4             VARCHAR2(80),
  PG2_RETIN4               VARCHAR2(8),
  PG2_DATE5                DATE,
  PG2_WT5                  VARCHAR2(5),
  PG2_BMI5                 VARCHAR2(5),
  PG2_BP5                  VARCHAR2(8),
  PG2_URINE5               VARCHAR2(8),
  PG2_URINEG5              VARCHAR2(8),
  PG2_GEST5                VARCHAR2(8),
  PG2_HT5                  VARCHAR2(8),
  PG2_FHRACT5              VARCHAR2(8),
  PG2_FM5                  NUMBER(1),
  PG2_POS5                 VARCHAR2(8),
  PG2_COMMENT5             VARCHAR2(80),
  PG2_RETIN5               VARCHAR2(8),
  PG2_DATE6                DATE,
  PG2_WT6                  VARCHAR2(5),
  PG2_BMI6                 VARCHAR2(5),
  PG2_BP6                  VARCHAR2(8),
  PG2_URINE6               VARCHAR2(8),
  PG2_URINEG6              VARCHAR2(8),
  PG2_GEST6                VARCHAR2(8),
  PG2_HT6                  VARCHAR2(8),
  PG2_FHRACT6              VARCHAR2(8),
  PG2_FM6                  NUMBER(1),
  PG2_POS6                 VARCHAR2(8),
  PG2_COMMENT6             VARCHAR2(80),
  PG2_RETIN6               VARCHAR2(8),
  PG2_DATE7                DATE,
  PG2_WT7                  VARCHAR2(5),
  PG2_BMI7                 VARCHAR2(5),
  PG2_BP7                  VARCHAR2(8),
  PG2_URINE7               VARCHAR2(8),
  PG2_URINEG7              VARCHAR2(8),
  PG2_GEST7                VARCHAR2(8),
  PG2_HT7                  VARCHAR2(8),
  PG2_FHRACT7              VARCHAR2(8),
  PG2_FM7                  NUMBER(1),
  PG2_POS7                 VARCHAR2(8),
  PG2_COMMENT7             VARCHAR2(80),
  PG2_RETIN7               VARCHAR2(8),
  PG2_DATE8                DATE,
  PG2_WT8                  VARCHAR2(5),
  PG2_BMI8                 VARCHAR2(5),
  PG2_BP8                  VARCHAR2(8),
  PG2_URINE8               VARCHAR2(8),
  PG2_URINEG8              VARCHAR2(8),
  PG2_GEST8                VARCHAR2(8),
  PG2_HT8                  VARCHAR2(8),
  PG2_FHRACT8              VARCHAR2(8),
  PG2_FM8                  NUMBER(1),
  PG2_POS8                 VARCHAR2(8),
  PG2_COMMENT8             VARCHAR2(80),
  PG2_RETIN8               VARCHAR2(8),
  PG2_DATE9                DATE,
  PG2_WT9                  VARCHAR2(5),
  PG2_BMI9                 VARCHAR2(5),
  PG2_BP9                  VARCHAR2(8),
  PG2_URINE9               VARCHAR2(8),
  PG2_URINEG9              VARCHAR2(8),
  PG2_GEST9                VARCHAR2(8),
  PG2_HT9                  VARCHAR2(8),
  PG2_FHRACT9              VARCHAR2(8),
  PG2_FM9                  NUMBER(1),
  PG2_POS9                 VARCHAR2(8),
  PG2_COMMENT9             VARCHAR2(80),
  PG2_RETIN9               VARCHAR2(8),
  PG2_DATE10               DATE,
  PG2_WT10                 VARCHAR2(5),
  PG2_BMI10                VARCHAR2(5),
  PG2_BP10                 VARCHAR2(8),
  PG2_URINE10              VARCHAR2(8),
  PG2_URINEG10             VARCHAR2(8),
  PG2_GEST10               VARCHAR2(8),
  PG2_HT10                 VARCHAR2(8),
  PG2_FHRACT10             VARCHAR2(8),
  PG2_FM10                 NUMBER(1),
  PG2_POS10                VARCHAR2(8),
  PG2_COMMENT10            VARCHAR2(80),
  PG2_RETIN10              VARCHAR2(8),
  PG2_DATE11               DATE,
  PG2_WT11                 VARCHAR2(5),
  PG2_BMI11                VARCHAR2(5),
  PG2_BP11                 VARCHAR2(8),
  PG2_URINE11              VARCHAR2(8),
  PG2_URINEG11             VARCHAR2(8),
  PG2_GEST11               VARCHAR2(8),
  PG2_HT11                 VARCHAR2(8),
  PG2_FHRACT11             VARCHAR2(8),
  PG2_FM11                 NUMBER(1),
  PG2_POS11                VARCHAR2(8),
  PG2_COMMENT11            VARCHAR2(80),
  PG2_RETIN11              VARCHAR2(8),
  PG2_DATE12               DATE,
  PG2_WT12                 VARCHAR2(5),
  PG2_BMI12                VARCHAR2(5),
  PG2_BP12                 VARCHAR2(8),
  PG2_URINE12              VARCHAR2(8),
  PG2_URINEG12             VARCHAR2(8),
  PG2_GEST12               VARCHAR2(8),
  PG2_HT12                 VARCHAR2(8),
  PG2_FHRACT12             VARCHAR2(8),
  PG2_FM12                 NUMBER(1),
  PG2_POS12                VARCHAR2(8),
  PG2_COMMENT12            VARCHAR2(80),
  PG2_RETIN12              VARCHAR2(8),
  PG2_DATE13               DATE,
  PG2_WT13                 VARCHAR2(5),
  PG2_BMI13                VARCHAR2(5),
  PG2_BP13                 VARCHAR2(8),
  PG2_URINE13              VARCHAR2(8),
  PG2_URINEG13             VARCHAR2(8),
  PG2_GEST13               VARCHAR2(8),
  PG2_HT13                 VARCHAR2(8),
  PG2_FHRACT13             VARCHAR2(8),
  PG2_FM13                 NUMBER(1),
  PG2_POS13                VARCHAR2(8),
  PG2_COMMENT13            VARCHAR2(80),
  PG2_RETIN13              VARCHAR2(8),
  PG2_DATE14               DATE,
  PG2_WT14                 VARCHAR2(5),
  PG2_BMI14                VARCHAR2(5),
  PG2_BP14                 VARCHAR2(8),
  PG2_URINE14              VARCHAR2(8),
  PG2_URINEG14             VARCHAR2(8),
  PG2_GEST14               VARCHAR2(8),
  PG2_HT14                 VARCHAR2(8),
  PG2_FHRACT14             VARCHAR2(8),
  PG2_FM14                 NUMBER(1),
  PG2_POS14                VARCHAR2(8),
  PG2_COMMENT14            VARCHAR2(80),
  PG2_RETIN14              VARCHAR2(8),
  PG2_DATE15               DATE,
  PG2_WT15                 VARCHAR2(5),
  PG2_BMI15                VARCHAR2(5),
  PG2_BP15                 VARCHAR2(8),
  PG2_URINE15              VARCHAR2(8),
  PG2_URINEG15             VARCHAR2(8),
  PG2_GEST15               VARCHAR2(8),
  PG2_HT15                 VARCHAR2(8),
  PG2_FHRACT15             VARCHAR2(8),
  PG2_FM15                 NUMBER(1),
  PG2_POS15                VARCHAR2(8),
  PG2_COMMENT15            VARCHAR2(80),
  PG2_RETIN15              VARCHAR2(8),
  PG2_DATE16               DATE,
  PG2_WT16                 VARCHAR2(5),
  PG2_BMI16                VARCHAR2(5),
  PG2_BP16                 VARCHAR2(8),
  PG2_URINE16              VARCHAR2(8),
  PG2_URINEG16             VARCHAR2(8),
  PG2_GEST16               VARCHAR2(8),
  PG2_HT16                 VARCHAR2(8),
  PG2_FHRACT16             VARCHAR2(8),
  PG2_FM16                 NUMBER(1),
  PG2_POS16                VARCHAR2(8),
  PG2_COMMENT16            VARCHAR2(80),
  PG2_RETIN16              VARCHAR2(8),
  PG2_SIGNATURE            VARCHAR2(60),
  PG3_DATE1                DATE,
  PG3_WT1                  VARCHAR2(5),
  PG3_BMI1                 VARCHAR2(5),
  PG3_BP1                  VARCHAR2(8),
  PG3_URINE1               VARCHAR2(8),
  PG3_URINEG1              VARCHAR2(8),
  PG3_GEST1                VARCHAR2(8),
  PG3_HT1                  VARCHAR2(8),
  PG3_FHRACT1              VARCHAR2(8),
  PG3_FM1                  NUMBER(1),
  PG3_POS1                 VARCHAR2(8),
  PG3_COMMENT1             VARCHAR2(80),
  PG3_RETIN1               VARCHAR2(8),
  PG3_DATE2                DATE,
  PG3_WT2                  VARCHAR2(5),
  PG3_BMI2                 VARCHAR2(5),
  PG3_BP2                  VARCHAR2(8),
  PG3_URINE2               VARCHAR2(8),
  PG3_URINEG2              VARCHAR2(8),
  PG3_GEST2                VARCHAR2(8),
  PG3_HT2                  VARCHAR2(8),
  PG3_FHRACT2              VARCHAR2(8),
  PG3_FM2                  NUMBER(1),
  PG3_POS2                 VARCHAR2(8),
  PG3_COMMENT2             VARCHAR2(80),
  PG3_RETIN2               VARCHAR2(8),
  PG3_DATE3                DATE,
  PG3_WT3                  VARCHAR2(5),
  PG3_BMI3                 VARCHAR2(5),
  PG3_BP3                  VARCHAR2(8),
  PG3_URINE3               VARCHAR2(8),
  PG3_URINEG3              VARCHAR2(8),
  PG3_GEST3                VARCHAR2(8),
  PG3_HT3                  VARCHAR2(8),
  PG3_FHRACT3              VARCHAR2(8),
  PG3_FM3                  NUMBER(1),
  PG3_POS3                 VARCHAR2(8),
  PG3_COMMENT3             VARCHAR2(80),
  PG3_RETIN3               VARCHAR2(8),
  PG3_DATE4                DATE,
  PG3_WT4                  VARCHAR2(5),
  PG3_BMI4                 VARCHAR2(5),
  PG3_BP4                  VARCHAR2(8),
  PG3_URINE4               VARCHAR2(8),
  PG3_URINEG4              VARCHAR2(8),
  PG3_GEST4                VARCHAR2(8),
  PG3_HT4                  VARCHAR2(8),
  PG3_FHRACT4              VARCHAR2(8),
  PG3_FM4                  NUMBER(1),
  PG3_POS4                 VARCHAR2(8),
  PG3_SENTHOSP20           NUMBER(1),
  PG3_TOPATIENT20          NUMBER(1),
  PG3_COMMENT4             VARCHAR2(80),
  PG3_RETIN4               VARCHAR2(8),
  PG3_DATE5                DATE,
  PG3_WT5                  VARCHAR2(5),
  PG3_BMI5                 VARCHAR2(5),
  PG3_BP5                  VARCHAR2(8),
  PG3_URINE5               VARCHAR2(8),
  PG3_URINEG5              VARCHAR2(8),
  PG3_GEST5                VARCHAR2(8),
  PG3_HT5                  VARCHAR2(8),
  PG3_FHRACT5              VARCHAR2(8),
  PG3_FM5                  NUMBER(1),
  PG3_POS5                 VARCHAR2(8),
  PG3_COMMENT5             VARCHAR2(80),
  PG3_RETIN5               VARCHAR2(8),
  PG3_DATE6                DATE,
  PG3_WT6                  VARCHAR2(5),
  PG3_BMI6                 VARCHAR2(5),
  PG3_BP6                  VARCHAR2(8),
  PG3_URINE6               VARCHAR2(8),
  PG3_URINEG6              VARCHAR2(8),
  PG3_GEST6                VARCHAR2(8),
  PG3_HT6                  VARCHAR2(8),
  PG3_FHRACT6              VARCHAR2(8),
  PG3_FM6                  NUMBER(1),
  PG3_POS6                 VARCHAR2(8),
  PG3_COMMENT6             VARCHAR2(80),
  PG3_RETIN6               VARCHAR2(8),
  PG3_DATE7                DATE,
  PG3_WT7                  VARCHAR2(5),
  PG3_BMI7                 VARCHAR2(5),
  PG3_BP7                  VARCHAR2(8),
  PG3_URINE7               VARCHAR2(8),
  PG3_URINEG7              VARCHAR2(8),
  PG3_GEST7                VARCHAR2(8),
  PG3_HT7                  VARCHAR2(8),
  PG3_FHRACT7              VARCHAR2(8),
  PG3_FM7                  NUMBER(1),
  PG3_POS7                 VARCHAR2(8),
  PG3_COMMENT7             VARCHAR2(80),
  PG3_RETIN7               VARCHAR2(8),
  PG3_DATE8                DATE,
  PG3_WT8                  VARCHAR2(5),
  PG3_BMI8                 VARCHAR2(5),
  PG3_BP8                  VARCHAR2(8),
  PG3_URINE8               VARCHAR2(8),
  PG3_URINEG8              VARCHAR2(8),
  PG3_GEST8                VARCHAR2(8),
  PG3_HT8                  VARCHAR2(8),
  PG3_FHRACT8              VARCHAR2(8),
  PG3_FM8                  NUMBER(1),
  PG3_POS8                 VARCHAR2(8),
  PG3_COMMENT8             VARCHAR2(80),
  PG3_RETIN8               VARCHAR2(8),
  PG3_DATE9                DATE,
  PG3_WT9                  VARCHAR2(5),
  PG3_BMI9                 VARCHAR2(5),
  PG3_BP9                  VARCHAR2(8),
  PG3_URINE9               VARCHAR2(8),
  PG3_URINEG9              VARCHAR2(8),
  PG3_GEST9                VARCHAR2(8),
  PG3_HT9                  VARCHAR2(8),
  PG3_FHRACT9              VARCHAR2(8),
  PG3_FM9                  NUMBER(1),
  PG3_POS9                 VARCHAR2(8),
  PG3_COMMENT9             VARCHAR2(80),
  PG3_RETIN9               VARCHAR2(8),
  PG3_DATE10               DATE,
  PG3_WT10                 VARCHAR2(5),
  PG3_BMI10                VARCHAR2(5),
  PG3_BP10                 VARCHAR2(8),
  PG3_URINE10              VARCHAR2(8),
  PG3_URINEG10             VARCHAR2(8),
  PG3_GEST10               VARCHAR2(8),
  PG3_HT10                 VARCHAR2(8),
  PG3_FHRACT10             VARCHAR2(8),
  PG3_FM10                 NUMBER(1),
  PG3_POS10                VARCHAR2(8),
  PG3_COMMENT10            VARCHAR2(80),
  PG3_RETIN10              VARCHAR2(8),
  PG3_DATE11               DATE,
  PG3_WT11                 VARCHAR2(5),
  PG3_BMI11                VARCHAR2(5),
  PG3_BP11                 VARCHAR2(8),
  PG3_URINE11              VARCHAR2(8),
  PG3_URINEG11             VARCHAR2(8),
  PG3_GEST11               VARCHAR2(8),
  PG3_HT11                 VARCHAR2(8),
  PG3_FHRACT11             VARCHAR2(8),
  PG3_FM11                 NUMBER(1),
  PG3_POS11                VARCHAR2(8),
  PG3_COMMENT11            VARCHAR2(80),
  PG3_RETIN11              VARCHAR2(8),
  PG3_DATE12               DATE,
  PG3_WT12                 VARCHAR2(5),
  PG3_BMI12                VARCHAR2(5),
  PG3_BP12                 VARCHAR2(8),
  PG3_URINE12              VARCHAR2(8),
  PG3_URINEG12             VARCHAR2(8),
  PG3_GEST12               VARCHAR2(8),
  PG3_HT12                 VARCHAR2(8),
  PG3_FHRACT12             VARCHAR2(8),
  PG3_FM12                 NUMBER(1),
  PG3_POS12                VARCHAR2(8),
  PG3_COMMENT12            VARCHAR2(80),
  PG3_RETIN12              VARCHAR2(8),
  PG3_DATE13               DATE,
  PG3_WT13                 VARCHAR2(5),
  PG3_BMI13                VARCHAR2(5),
  PG3_BP13                 VARCHAR2(8),
  PG3_URINE13              VARCHAR2(8),
  PG3_URINEG13             VARCHAR2(8),
  PG3_GEST13               VARCHAR2(8),
  PG3_HT13                 VARCHAR2(8),
  PG3_FHRACT13             VARCHAR2(8),
  PG3_FM13                 NUMBER(1),
  PG3_POS13                VARCHAR2(8),
  PG3_COMMENT13            VARCHAR2(80),
  PG3_RETIN13              VARCHAR2(8),
  PG3_DATE14               DATE,
  PG3_WT14                 VARCHAR2(5),
  PG3_BMI14                VARCHAR2(5),
  PG3_BP14                 VARCHAR2(8),
  PG3_URINE14              VARCHAR2(8),
  PG3_URINEG14             VARCHAR2(8),
  PG3_GEST14               VARCHAR2(8),
  PG3_HT14                 VARCHAR2(8),
  PG3_FHRACT14             VARCHAR2(8),
  PG3_FM14                 NUMBER(1),
  PG3_POS14                VARCHAR2(8),
  PG3_COMMENT14            VARCHAR2(80),
  PG3_RETIN14              VARCHAR2(8),
  PG3_DATE15               DATE,
  PG3_WT15                 VARCHAR2(5),
  PG3_BMI15                VARCHAR2(5),
  PG3_BP15                 VARCHAR2(8),
  PG3_URINE15              VARCHAR2(8),
  PG3_URINEG15             VARCHAR2(8),
  PG3_GEST15               VARCHAR2(8),
  PG3_HT15                 VARCHAR2(8),
  PG3_FHRACT15             VARCHAR2(8),
  PG3_FM15                 NUMBER(1),
  PG3_POS15                VARCHAR2(8),
  PG3_COMMENT15            VARCHAR2(80),
  PG3_RETIN15              VARCHAR2(8),
  PG3_DATE16               DATE,
  PG3_WT16                 VARCHAR2(5),
  PG3_BMI16                VARCHAR2(5),
  PG3_BP16                 VARCHAR2(8),
  PG3_URINE16              VARCHAR2(8),
  PG3_URINEG16             VARCHAR2(8),
  PG3_GEST16               VARCHAR2(8),
  PG3_HT16                 VARCHAR2(8),
  PG3_FHRACT16             VARCHAR2(8),
  PG3_FM16                 NUMBER(1),
  PG3_POS16                VARCHAR2(8),
  PG3_COMMENT16            VARCHAR2(80),
  PG3_RETIN16              VARCHAR2(8),
  PG3_SIGNATURE            VARCHAR2(60)
)
;
alter table FORMBCAR2007
  add primary key (ID);

prompt
prompt Creating table FORMCAREGIVER
prompt ============================
prompt
create table FORMCAREGIVER
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null,
  PROVIDER_NO    NUMBER(10),
  STUDYID        VARCHAR2(20) default 'N/A' not null,
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6) not null,
  SEXM           NUMBER(1),
  SEXF           NUMBER(1),
  DOBYEAR        VARCHAR2(4),
  DOBMONTH       VARCHAR2(2),
  DOBDAY         VARCHAR2(2),
  SPOUSEY        NUMBER(1),
  CHILDY         NUMBER(1),
  GRANDCHILDY    NUMBER(1),
  SIBLINGY       NUMBER(1),
  FRIENDY        NUMBER(1),
  OTHERY         NUMBER(1),
  OTHERRELATION  VARCHAR2(255),
  RESIDEY        NUMBER(1),
  RESIDEN        NUMBER(1),
  HEALTHEX       NUMBER(1),
  HEALTHVG       NUMBER(1),
  HEALTHG        NUMBER(1),
  HEALTHF        NUMBER(1),
  HEALTHP        NUMBER(1),
  Q1Y            NUMBER(1),
  Q1N            NUMBER(1),
  Q2Y            NUMBER(1),
  Q2N            NUMBER(1),
  Q3Y            NUMBER(1),
  Q3N            NUMBER(1),
  Q4Y            NUMBER(1),
  Q4N            NUMBER(1),
  Q5Y            NUMBER(1),
  Q5N            NUMBER(1),
  Q6Y            NUMBER(1),
  Q6N            NUMBER(1),
  Q7Y            NUMBER(1),
  Q7N            NUMBER(1),
  Q8Y            NUMBER(1),
  Q8N            NUMBER(1),
  Q9Y            NUMBER(1),
  Q9N            NUMBER(1),
  Q10Y           NUMBER(1),
  Q10N           NUMBER(1),
  Q11Y           NUMBER(1),
  Q11N           NUMBER(1),
  Q12Y           NUMBER(1),
  Q12N           NUMBER(1),
  Q13Y           NUMBER(1),
  Q13N           NUMBER(1),
  SCORE1         NUMBER(2),
  SRBSCORE       NUMBER(3)
)
;
alter table FORMCAREGIVER
  add primary key (ID);

prompt
prompt Creating table FORMCESD
prompt =======================
prompt
create table FORMCESD
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null,
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6) not null,
  STUDYID        VARCHAR2(20) default 'N/A' not null,
  Q1RARE         NUMBER(1),
  Q1SOME         NUMBER(1),
  Q1OCC          NUMBER(1),
  Q1MOST         NUMBER(1),
  Q2RARE         NUMBER(1),
  Q2SOME         NUMBER(1),
  Q2OCC          NUMBER(1),
  Q2MOST         NUMBER(1),
  Q3RARE         NUMBER(1),
  Q3SOME         NUMBER(1),
  Q3OCC          NUMBER(1),
  Q3MOST         NUMBER(1),
  Q4RARE         NUMBER(1),
  Q4SOME         NUMBER(1),
  Q4OCC          NUMBER(1),
  Q4MOST         NUMBER(1),
  Q5RARE         NUMBER(1),
  Q5SOME         NUMBER(1),
  Q5OCC          NUMBER(1),
  Q5MOST         NUMBER(1),
  Q6RARE         NUMBER(1),
  Q6SOME         NUMBER(1),
  Q6OCC          NUMBER(1),
  Q6MOST         NUMBER(1),
  Q7RARE         NUMBER(1),
  Q7SOME         NUMBER(1),
  Q7OCC          NUMBER(1),
  Q7MOST         NUMBER(1),
  Q8RARE         NUMBER(1),
  Q8SOME         NUMBER(1),
  Q8OCC          NUMBER(1),
  Q8MOST         NUMBER(1),
  Q9RARE         NUMBER(1),
  Q9SOME         NUMBER(1),
  Q9OCC          NUMBER(1),
  Q9MOST         NUMBER(1),
  Q10RARE        NUMBER(1),
  Q10SOME        NUMBER(1),
  Q10OCC         NUMBER(1),
  Q10MOST        NUMBER(1),
  Q11RARE        NUMBER(1),
  Q11SOME        NUMBER(1),
  Q11OCC         NUMBER(1),
  Q11MOST        NUMBER(1),
  Q12RARE        NUMBER(1),
  Q12SOME        NUMBER(1),
  Q12OCC         NUMBER(1),
  Q12MOST        NUMBER(1),
  Q13RARE        NUMBER(1),
  Q13SOME        NUMBER(1),
  Q13OCC         NUMBER(1),
  Q13MOST        NUMBER(1),
  Q14RARE        NUMBER(1),
  Q14SOME        NUMBER(1),
  Q14OCC         NUMBER(1),
  Q14MOST        NUMBER(1),
  Q15RARE        NUMBER(1),
  Q15SOME        NUMBER(1),
  Q15OCC         NUMBER(1),
  Q15MOST        NUMBER(1),
  Q16RARE        NUMBER(1),
  Q16SOME        NUMBER(1),
  Q16OCC         NUMBER(1),
  Q16MOST        NUMBER(1),
  Q17RARE        NUMBER(1),
  Q17SOME        NUMBER(1),
  Q17OCC         NUMBER(1),
  Q17MOST        NUMBER(1),
  Q18RARE        NUMBER(1),
  Q18SOME        NUMBER(1),
  Q18OCC         NUMBER(1),
  Q18MOST        NUMBER(1),
  Q19RARE        NUMBER(1),
  Q19SOME        NUMBER(1),
  Q19OCC         NUMBER(1),
  Q19MOST        NUMBER(1),
  Q20RARE        NUMBER(1),
  Q20SOME        NUMBER(1),
  Q20OCC         NUMBER(1),
  Q20MOST        NUMBER(1),
  SCORE          NUMBER(2)
)
;
alter table FORMCESD
  add primary key (ID);

prompt
prompt Creating table FORMCONSULT
prompt ==========================
prompt
create table FORMCONSULT
(
  ID                 NUMBER(10) not null,
  PROVIDER_NO        NUMBER(10),
  DOC_NAME           VARCHAR2(60),
  CL_NAME            VARCHAR2(60),
  CL_ADDRESS1        VARCHAR2(170),
  CL_ADDRESS2        VARCHAR2(170),
  CL_PHONE           VARCHAR2(16),
  CL_FAX             VARCHAR2(16),
  BILLINGREFERRAL_NO NUMBER(10),
  T_NAME             VARCHAR2(60),
  T_NAME2            VARCHAR2(60),
  T_ADDRESS1         VARCHAR2(170),
  T_ADDRESS2         VARCHAR2(170),
  T_PHONE            VARCHAR2(16),
  T_FAX              VARCHAR2(16),
  DEMOGRAPHIC_NO     NUMBER(10) default '0' not null,
  P_NAME             VARCHAR2(60),
  P_ADDRESS1         VARCHAR2(170),
  P_ADDRESS2         VARCHAR2(170),
  P_PHONE            VARCHAR2(16),
  P_BIRTHDATE        VARCHAR2(30),
  P_HEALTHCARD       VARCHAR2(20),
  COMMENTS           VARCHAR2(4000),
  FORMCREATED        DATE,
  FORMEDITED         TIMESTAMP(6) default CURRENT_TIMESTAMP not null,
  CONSULTTIME        DATE
)
;
alter table FORMCONSULT
  add primary key (ID);

prompt
prompt Creating table FORMCOSTQUESTIONNAIRE
prompt ====================================
prompt
create table FORMCOSTQUESTIONNAIRE
(
  ID                   NUMBER(10) not null,
  DEMOGRAPHIC_NO       NUMBER(10) not null,
  PROVIDER_NO          NUMBER(10),
  FORMCREATED          DATE,
  FORMEDITED           TIMESTAMP(6) not null,
  STUDYID              VARCHAR2(20) default 'N/A' not null,
  SEENDOCTORY          NUMBER(1),
  SEENDOCTORN          NUMBER(1),
  FAMILYPHYVISITS      NUMBER(3),
  SPECIALISTVISITS     NUMBER(3),
  OTHERPROVIDERY       NUMBER(1),
  OTHERPROVIDERN       NUMBER(1),
  VISITNURSE           NUMBER(3),
  HOMEMAKER            NUMBER(3),
  PHYSIOTHERAPIST      NUMBER(3),
  THERAPIST            NUMBER(3),
  PSYCHOLOGIST         NUMBER(3),
  SOCIALWORKER         NUMBER(3),
  SUPPORTGROUP         NUMBER(3),
  MEALONWHEELS         NUMBER(3),
  OTHER                NUMBER(3),
  PAIDSERVICE1         VARCHAR2(255),
  PAIDSERVICEHOUR1     NUMBER(5),
  PAIDSERVICECOST1     NUMBER(12,2),
  PAIDSERVICE2         VARCHAR2(255),
  PAIDSERVICEHOUR2     NUMBER(5),
  PAIDSERVICECOST2     NUMBER(12,2),
  PAIDSERVICE3         VARCHAR2(255),
  PAIDSERVICEHOUR3     NUMBER(5),
  PAIDSERVICECOST3     NUMBER(12,2),
  PLANNEDHOSPN         NUMBER(1),
  PLANNEDHOSPY         NUMBER(1),
  PLANNEDHOSPADMITTED  NUMBER(3),
  PLANNEDHOSPDAYS      NUMBER(3),
  NURSINGHOMEN         NUMBER(1),
  NURSINGHOMEY         NUMBER(1),
  NURSINGHOMEDAYS      NUMBER(3),
  EMERGENCYN           NUMBER(1),
  EMERGENCYY           NUMBER(1),
  EMERGENCY911         NUMBER(3),
  EMERGENCY            NUMBER(3),
  EMERGENCYAMBULANCE   NUMBER(3),
  WALKINN              NUMBER(1),
  WALKINY              NUMBER(1),
  WALKIN               NUMBER(3),
  ITEMPURCHASED1       VARCHAR2(255),
  ITEMCOST1            NUMBER(12,2),
  ITEMPURCHASED2       VARCHAR2(255),
  ITEMCOST2            NUMBER(12,2),
  ITEMPURCHASED3       VARCHAR2(255),
  ITEMCOST3            NUMBER(12,2),
  EMPLOYED             NUMBER(1),
  EMPLOYEDFULLTIME     NUMBER(1),
  EMPLOYEDPARTTIME     NUMBER(1),
  SELFEMPLOYED         NUMBER(1),
  SELFEMPLOYEDFULLTIME NUMBER(1),
  SELFEMPLOYEDPARTTIME NUMBER(1),
  UNEMPLOYED           NUMBER(1),
  UNEMPLOYEDABLE       NUMBER(1),
  UNEMPLOYEDUNABLE     NUMBER(1),
  DISABILITY           NUMBER(1),
  DISABILITYSHORTTERM  NUMBER(1),
  DISABILITYLONGTERM   NUMBER(1),
  RETIRED              NUMBER(1),
  HOMEMAKERWITHOUTPAID NUMBER(1)
)
;
alter table FORMCOSTQUESTIONNAIRE
  add primary key (ID);

prompt
prompt Creating table FORMDISCHARGESUMMARY
prompt ===================================
prompt
create table FORMDISCHARGESUMMARY
(
  ID                   NUMBER(11) not null,
  DEMOGRAPHIC_NO       NUMBER(11) default '0' not null,
  FORMCREATED          DATE,
  FORMEDITED           TIMESTAMP(6) default CURRENT_TIMESTAMP not null,
  CLIENTNAME           VARCHAR2(60),
  BIRTHDATE            DATE,
  OHIP                 VARCHAR2(25),
  ADMITDATE            DATE,
  DISCHARGEDATE        DATE,
  PROGRAMNAME          VARCHAR2(70),
  ALLERGIES            VARCHAR2(4000),
  ADMISSIONNOTES       VARCHAR2(4000),
  CURRENTISSUES        VARCHAR2(4000),
  BRIEFSUMMARY         VARCHAR2(4000),
  DISCHARGEPLAN        VARCHAR2(4000),
  FOLLOWUPAPPOINTMENT  VARCHAR2(1),
  DOCTOR1              VARCHAR2(30),
  PHONENUMBER1         VARCHAR2(30),
  DATE1                VARCHAR2(20),
  LOCATION1            VARCHAR2(100),
  DOCTOR2              VARCHAR2(30),
  PHONENUMBER2         VARCHAR2(30),
  DATE2                VARCHAR2(20),
  LOCATION2            VARCHAR2(100),
  DOCTOR3              VARCHAR2(30),
  PHONENUMBER3         VARCHAR2(30),
  DATE3                VARCHAR2(20),
  LOCATION3            VARCHAR2(100),
  PRESCRIPTIONSUMMARY  VARCHAR2(4000),
  PRESCRIPTIONPROVIDED VARCHAR2(1),
  COVEREDBYODB         VARCHAR2(1),
  ODBFORMREQIRED       VARCHAR2(1),
  CHANGEMEDICATIONS    VARCHAR2(255),
  REFERRALPROGRAM1     VARCHAR2(255),
  REFERRALMADE1        VARCHAR2(255),
  REFERRALOUTCOME1     VARCHAR2(255),
  REFERRALPROGRAM2     VARCHAR2(255),
  REFERRALMADE2        VARCHAR2(255),
  REFERRALOUTCOME2     VARCHAR2(255),
  REFERRALPROGRAM3     VARCHAR2(255),
  REFERRALMADE3        VARCHAR2(255),
  REFERRALOUTCOME3     VARCHAR2(255),
  REFERRALPROGRAM4     VARCHAR2(255),
  REFERRALMADE4        VARCHAR2(255),
  REFERRALOUTCOME4     VARCHAR2(255),
  REFERRALPROGRAM5     VARCHAR2(255),
  REFERRALMADE5        VARCHAR2(255),
  REFERRALOUTCOME5     VARCHAR2(255),
  PROVIDER_NO          NUMBER(11) default '0' not null,
  PROVIDERNAME         VARCHAR2(60),
  SIGNATURE            VARCHAR2(60),
  SIGNATUREDATE        VARCHAR2(20)
)
;
alter table FORMDISCHARGESUMMARY
  add primary key (ID);

prompt
prompt Creating table FORMFALLS
prompt ========================
prompt
create table FORMFALLS
(
  ID                       NUMBER(10) not null,
  DEMOGRAPHIC_NO           NUMBER(10) not null,
  PROVIDER_NO              NUMBER(10),
  FORMCREATED              DATE,
  FORMEDITED               TIMESTAMP(6) not null,
  STUDYID                  VARCHAR2(20) default 'N/A' not null,
  FALLENLAST12MY           NUMBER(1),
  FALLENLAST12MN           NUMBER(1),
  FALLENLAST12MNOTREMEMBER NUMBER(1),
  INJUREDY                 NUMBER(1),
  INJUREDN                 NUMBER(1),
  MEDATTNY                 NUMBER(1),
  MEDATTNN                 NUMBER(1),
  HOSPITALIZEDY            NUMBER(1),
  HOSPITALIZEDN            NUMBER(1),
  LIMITACTY                NUMBER(1),
  LIMITACTN                NUMBER(1)
)
;
alter table FORMFALLS
  add primary key (ID);

prompt
prompt Creating table FORMFOLLOWUP
prompt ===========================
prompt
create table FORMFOLLOWUP
(
  ID                             NUMBER(20) not null,
  CBOX_ASSISTWITHHEALTHCARD      VARCHAR2(1),
  COMMENTSONEDUCATION            VARCHAR2(255),
  TYPEOFINCOME                   VARCHAR2(120),
  HISTORYOFJAIL                  VARCHAR2(50),
  DATEENTEREDSEATON              VARCHAR2(24),
  COMMENTSONSTREETDRUGS          VARCHAR2(255),
  WHENMADEAPPFOROTHERINCOME      VARCHAR2(24),
  CBOX_ODSP                      VARCHAR2(1),
  CBOX_FORTYORK                  VARCHAR2(1),
  CBOX_ONEILLHOUSE               VARCHAR2(1),
  CBOX_OTHER                     VARCHAR2(1),
  CBOX_HAVEOTHER1                VARCHAR2(1),
  RADIO_DRINKTHESE               VARCHAR2(24),
  CBOX_OAS                       VARCHAR2(1),
  WHYSPONSORSHIPBREAKDOWN        VARCHAR2(255),
  RADIO_EVERBEENJAILED           VARCHAR2(24),
  DOCTOR2PHONE                   VARCHAR2(24),
  RADIO_CAREDFORDEPRESSION       VARCHAR2(24),
  RADIO_CAREDFOROTHER            VARCHAR2(24),
  CBOX_GETMOREMEDICATION         VARCHAR2(1),
  RADIO_DRUGUSE                  VARCHAR2(24),
  RADIO_HAVEMENTALPROBLEM        VARCHAR2(24),
  RADIO_REQUIREREFERRALTOESL     VARCHAR2(24),
  RADIO_HAVEHEALTHCOVERAGE       VARCHAR2(24),
  DAY                            VARCHAR2(2),
  CBOX_SPEAKFRENCH               VARCHAR2(1),
  COMMENTSONLEGALISSUES          VARCHAR2(255),
  RADIO_HOWMUCHDRINK             VARCHAR2(24),
  RADIO_NEEDASSISTINLEGAL        VARCHAR2(24),
  COMMENTSONHOUSING              VARCHAR2(255),
  CBOX_DOWNSVIEWDELLS            VARCHAR2(1),
  CBOX_HAVEUNIVERSITY            VARCHAR2(1),
  PUBLICTRUSTEEINFO              VARCHAR2(255),
  CBOX_LONGTERMPROGRAM           VARCHAR2(1),
  DATEASSESSMENT                 VARCHAR2(24),
  CBOX_ASSISTWITHSINCARD         VARCHAR2(1),
  DRINKSPERDAY                   VARCHAR2(3),
  SPEAKOTHER                     VARCHAR2(50),
  CBOX_REMEMBERTOTAKEMEDICATION  VARCHAR2(1),
  DATELASTCONTACT3               VARCHAR2(24),
  USUALOCCUPATION                VARCHAR2(70),
  RADIO_WANTAPPMT                VARCHAR2(24),
  RADIO_HASIDINFILE              VARCHAR2(24),
  DRINKSPERMONTH                 VARCHAR2(5),
  SPONSORNAME                    VARCHAR2(50),
  CBOX_PAMPHLETISSUED            VARCHAR2(1),
  DATELASTCONTACT1               VARCHAR2(24),
  CBOX_OW                        VARCHAR2(1),
  ASSESSCOMPLETETIME             VARCHAR2(50),
  CBOX_BIRCHMOUNTRESIDENCE       VARCHAR2(1),
  CONTACT4NAME                   VARCHAR2(50),
  CLIENTLASTADDRESSPAYRENT       VARCHAR2(255),
  DATELASTCONTACT2               VARCHAR2(24),
  CONTACT2NAME                   VARCHAR2(50),
  RADIO_CURRENTLYEMPLOYED        VARCHAR2(24),
  COMMENTSONFINANCE              VARCHAR2(255),
  CBOX_HAVEODSP                  VARCHAR2(1),
  AGENCY3NAME                    VARCHAR2(70),
  AMTOWING                       VARCHAR2(16),
  CBOX_ROTARYCLUB                VARCHAR2(1),
  CLIENTSURNAME                  VARCHAR2(50),
  COMPLETEDBY1                   VARCHAR2(50),
  COMMENTSONEMPLOYMENT           VARCHAR2(255),
  RADIO_ENTITLEDTOOTHERINCOME    VARCHAR2(24),
  DATELASTCONTACT4               VARCHAR2(24),
  WHEREBEFORESEATON              VARCHAR2(255),
  DOCTOR2NAMEADDR                VARCHAR2(255),
  RADIO_SPONSORSHIPBREAKDOWN     VARCHAR2(24),
  CBOX_ASSISTWITHBIRTHCERT       VARCHAR2(1),
  HOWHEARABOUTSEATON             VARCHAR2(255),
  HOWMUCHYOURECEIVE              VARCHAR2(12),
  EVERMADEAPPFOROTHERINCOME      VARCHAR2(120),
  CONTACT4PHONE                  VARCHAR2(24),
  ASSISTPROVIDED1                VARCHAR2(120),
  CBOX_EMPLOYMENT                VARCHAR2(1),
  YOURCANADIANSTATUS             VARCHAR2(50),
  RADIO_EVERMADEAPPFOROTHERINCOM VARCHAR2(24),
  CBOX_HAVESCHIZOPHRENIA         VARCHAR2(1),
  COMMENTSONID                   VARCHAR2(255),
  CBOX_HAVEOTHER2                VARCHAR2(1),
  RADIO_NEEDASSISTWITHMEDICATION VARCHAR2(24),
  ASSESSSTARTTIME                VARCHAR2(24),
  CBOX_HAVEDEPRESSION            VARCHAR2(1),
  CBOX_ASSISTWITHIMMIGRANT       VARCHAR2(1),
  CLIENTLASTADDRESS              VARCHAR2(255),
  MAINSOURCEOFINCOME             VARCHAR2(70),
  CBOX_ASSISTWITHNONE            VARCHAR2(1),
  DATEEXITEDSEATON               VARCHAR2(24),
  CBOX_HAVEOHIP                  VARCHAR2(1),
  HOUSINGINTERESTED              VARCHAR2(70),
  ASSISTWITHOTHER                VARCHAR2(50),
  NEEDHELPWITHIMMIGRATION        VARCHAR2(50),
  CBOX_SPEAKENGLISH              VARCHAR2(1),
  CBOX_ASSISTWITHOTHER           VARCHAR2(1),
  CONTACT2PHONE                  VARCHAR2(24),
  CBOX_HAVECOLLEGE               VARCHAR2(1),
  DATELIVEDTHERE                 VARCHAR2(24),
  RADIO_INVOLVEDOTHERAGENCIES    VARCHAR2(24),
  ASSISTPROVIDED2                VARCHAR2(120),
  RADIO_WANTHELPQUITDRUG         VARCHAR2(24),
  CONTACT1PHONE                  VARCHAR2(24),
  RADIO_NEED60DAYSSEATONSERVICES VARCHAR2(24),
  COMPLETEDBY2                   VARCHAR2(50),
  RADIO_INTERESTEDINTRAINING     VARCHAR2(24),
  CONTACT3PHONE                  VARCHAR2(24),
  FOLLOWUPAPPMTS                 VARCHAR2(120),
  CBOX_HAVEANXIETY               VARCHAR2(1),
  RADIO_INTERESTBACKTOSCHOOL     VARCHAR2(24),
  CBOX_ASSISTWITHCITIZENCARD     VARCHAR2(1),
  CBOX_HAVEHIGHSCHOOL            VARCHAR2(1),
  CBOX_HAVEOTHER3                VARCHAR2(1),
  RADIO_CITIZEN                  VARCHAR2(24),
  CLIENTFIRSTNAME                VARCHAR2(50),
  MONTH                          VARCHAR2(2),
  CBOX_CPP                       VARCHAR2(1),
  RADIO_SEENDOCTORREGALCOHOL     VARCHAR2(24),
  RADIO_WANTHELPQUIT             VARCHAR2(24),
  RADIO_OWEDRENT                 VARCHAR2(24),
  WHEREOWERENT                   VARCHAR2(255),
  HOWLONGUNEMPLOYED              VARCHAR2(3),
  CBOX_HAVEODB                   VARCHAR2(1),
  HOWLONGEMPLOYED                VARCHAR2(3),
  CBOX_ANNEXHARM                 VARCHAR2(1),
  FORMEDITED                     DATE,
  DATELASTDOCTOR2CONTACT         VARCHAR2(24),
  YEARSOFEDUCATION               VARCHAR2(4),
  RADIO_YOURCANADIANSTATUS       VARCHAR2(24),
  RADIO_DRUGUSEFREQUENCY         VARCHAR2(24),
  PROVIDER_NO                    NUMBER(20),
  DOCTOR1PHONE                   VARCHAR2(24),
  AGENCY1NAME                    VARCHAR2(70),
  CBOX_HOSTEL                    VARCHAR2(1),
  COMMENTSONALCOHOL              VARCHAR2(255),
  RADIO_MENTALILLNESS            VARCHAR2(24),
  CBOX_SPEAKSPANISH              VARCHAR2(1),
  DRINKSPERWEEK                  VARCHAR2(4),
  COMMENTSONIMMIGRATION          VARCHAR2(255),
  RADIO_BEHAVIORPROBLEM          VARCHAR2(24),
  AGENCY2NAME                    VARCHAR2(70),
  COMMENTSONNEEDHELP             VARCHAR2(255),
  RADIO_HAVEPUBLICTRUSTEE        VARCHAR2(24),
  CBOX_SPEAKOTHER                VARCHAR2(1),
  CBOX_TAKEPRESCRIBEDMEDICATION  VARCHAR2(1),
  NEEDASSISTINLEGAL              VARCHAR2(255),
  RADIO_CAREDFORSCHIZOPHRENIA    VARCHAR2(24),
  FORMCREATED                    DATE,
  DEMOGRAPHIC_NO                 NUMBER(20) default '0' not null,
  RADIO_DOYOUDRINK               VARCHAR2(24),
  RADIO_DRINKING                 VARCHAR2(24),
  RADIO_HEALTHPROBLEM            VARCHAR2(24),
  CBOX_UI                        VARCHAR2(1),
  LIVEDWITHWHOM                  VARCHAR2(120),
  ASSISTPROVIDED3                VARCHAR2(120),
  CBOX_HAVEMANIC                 VARCHAR2(1),
  CONTACT1NAME                   VARCHAR2(50),
  YEAR                           VARCHAR2(4),
  RADIO_CAREDFORMANIC            VARCHAR2(24),
  CBOX_ASSISTWITHREFUGEE         VARCHAR2(1),
  RADIO_LIVEDINSUBSIDIZED        VARCHAR2(24),
  RADIO_USEDRUGS                 VARCHAR2(24),
  CBOX_NEEDHELPINOTHER           VARCHAR2(1),
  ASSISTPROVIDED4                VARCHAR2(120),
  DOCTOR1NAMEADDR                VARCHAR2(255),
  CONTACT3NAME                   VARCHAR2(50),
  HAVEOTHER                      VARCHAR2(70),
  CBOX_STOREMEDICATION           VARCHAR2(1),
  AGENCY4NAME                    VARCHAR2(70),
  RADIO_CAREDFORANXIETY          VARCHAR2(24),
  DATELASTDOCTOR1CONTACT         VARCHAR2(24)
)
;
alter table FORMFOLLOWUP
  add primary key (ID);

prompt
prompt Creating table FORMGRIPSTRENGTH
prompt ===============================
prompt
create table FORMGRIPSTRENGTH
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null,
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6) not null,
  STUDYID        VARCHAR2(20) default 'N/A' not null,
  DOM1           VARCHAR2(5),
  NONDOM1        VARCHAR2(5),
  DOM2           VARCHAR2(5),
  NONDOM2        VARCHAR2(5),
  DOM3           VARCHAR2(5),
  NONDOM3        VARCHAR2(5),
  DOMAVG         VARCHAR2(5),
  NONDOMAVG      VARCHAR2(5)
)
;
alter table FORMGRIPSTRENGTH
  add primary key (ID);

prompt
prompt Creating table FORMGROWTH0_36
prompt =============================
prompt
create table FORMGROWTH0_36
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6),
  PATIENTNAME    VARCHAR2(80),
  RECORDNO       VARCHAR2(10),
  MOTHERSTATURE  VARCHAR2(80),
  GESTATIONALAGE VARCHAR2(5),
  FATHERSTATURE  VARCHAR2(80),
  EDC            VARCHAR2(10),
  DATE_1         DATE,
  AGE_1          VARCHAR2(5),
  WEIGHT_1       VARCHAR2(6),
  LENGTH_1       VARCHAR2(6),
  HEADCIRC_1     VARCHAR2(6),
  COMMENT_1      VARCHAR2(25),
  DATE_2         DATE,
  AGE_2          VARCHAR2(5),
  WEIGHT_2       VARCHAR2(6),
  LENGTH_2       VARCHAR2(6),
  HEADCIRC_2     VARCHAR2(6),
  COMMENT_2      VARCHAR2(25),
  DATE_3         DATE,
  AGE_3          VARCHAR2(5),
  WEIGHT_3       VARCHAR2(6),
  LENGTH_3       VARCHAR2(6),
  HEADCIRC_3     VARCHAR2(6),
  COMMENT_3      VARCHAR2(25),
  DATE_4         DATE,
  AGE_4          VARCHAR2(5),
  WEIGHT_4       VARCHAR2(6),
  LENGTH_4       VARCHAR2(6),
  HEADCIRC_4     VARCHAR2(6),
  COMMENT_4      VARCHAR2(25),
  DATE_5         DATE,
  AGE_5          VARCHAR2(5),
  WEIGHT_5       VARCHAR2(6),
  LENGTH_5       VARCHAR2(6),
  HEADCIRC_5     VARCHAR2(6),
  COMMENT_5      VARCHAR2(25),
  DATE_6         DATE,
  AGE_6          VARCHAR2(5),
  WEIGHT_6       VARCHAR2(6),
  LENGTH_6       VARCHAR2(6),
  HEADCIRC_6     VARCHAR2(6),
  COMMENT_6      VARCHAR2(25),
  DATE_7         DATE,
  AGE_7          VARCHAR2(5),
  WEIGHT_7       VARCHAR2(6),
  LENGTH_7       VARCHAR2(6),
  HEADCIRC_7     VARCHAR2(6),
  COMMENT_7      VARCHAR2(25),
  DATE_8         DATE,
  AGE_8          VARCHAR2(5),
  WEIGHT_8       VARCHAR2(6),
  LENGTH_8       VARCHAR2(6),
  HEADCIRC_8     VARCHAR2(6),
  COMMENT_8      VARCHAR2(25),
  DATE_9         DATE,
  AGE_9          VARCHAR2(5),
  WEIGHT_9       VARCHAR2(6),
  LENGTH_9       VARCHAR2(6),
  HEADCIRC_9     VARCHAR2(6),
  COMMENT_9      VARCHAR2(25),
  DATE_10        DATE,
  AGE_10         VARCHAR2(5),
  WEIGHT_10      VARCHAR2(6),
  LENGTH_10      VARCHAR2(6),
  HEADCIRC_10    VARCHAR2(6),
  COMMENT_10     VARCHAR2(25),
  DATE_11        DATE,
  AGE_11         VARCHAR2(5),
  WEIGHT_11      VARCHAR2(6),
  LENGTH_11      VARCHAR2(6),
  HEADCIRC_11    VARCHAR2(6),
  COMMENT_11     VARCHAR2(25),
  DATE_12        DATE,
  AGE_12         VARCHAR2(5),
  WEIGHT_12      VARCHAR2(6),
  LENGTH_12      VARCHAR2(6),
  HEADCIRC_12    VARCHAR2(6),
  COMMENT_12     VARCHAR2(25),
  DATE_13        DATE,
  AGE_13         VARCHAR2(5),
  WEIGHT_13      VARCHAR2(6),
  LENGTH_13      VARCHAR2(6),
  HEADCIRC_13    VARCHAR2(6),
  COMMENT_13     VARCHAR2(25),
  DATE_14        DATE,
  AGE_14         VARCHAR2(5),
  WEIGHT_14      VARCHAR2(6),
  LENGTH_14      VARCHAR2(6),
  HEADCIRC_14    VARCHAR2(6),
  COMMENT_14     VARCHAR2(25),
  DATE_15        DATE,
  AGE_15         VARCHAR2(5),
  WEIGHT_15      VARCHAR2(6),
  LENGTH_15      VARCHAR2(6),
  HEADCIRC_15    VARCHAR2(6),
  COMMENT_15     VARCHAR2(25),
  DATE_16        DATE,
  AGE_16         VARCHAR2(5),
  WEIGHT_16      VARCHAR2(6),
  LENGTH_16      VARCHAR2(6),
  HEADCIRC_16    VARCHAR2(6),
  COMMENT_16     VARCHAR2(25),
  DATE_17        DATE,
  AGE_17         VARCHAR2(5),
  WEIGHT_17      VARCHAR2(6),
  LENGTH_17      VARCHAR2(6),
  HEADCIRC_17    VARCHAR2(6),
  COMMENT_17     VARCHAR2(25),
  DATE_18        DATE,
  AGE_18         VARCHAR2(5),
  WEIGHT_18      VARCHAR2(6),
  LENGTH_18      VARCHAR2(6),
  HEADCIRC_18    VARCHAR2(6),
  COMMENT_18     VARCHAR2(25),
  DATE_19        DATE,
  AGE_19         VARCHAR2(5),
  WEIGHT_19      VARCHAR2(6),
  LENGTH_19      VARCHAR2(6),
  HEADCIRC_19    VARCHAR2(6),
  COMMENT_19     VARCHAR2(25),
  DATE_20        DATE,
  AGE_20         VARCHAR2(5),
  WEIGHT_20      VARCHAR2(6),
  LENGTH_20      VARCHAR2(6),
  HEADCIRC_20    VARCHAR2(6),
  COMMENT_20     VARCHAR2(25)
)
;
alter table FORMGROWTH0_36
  add primary key (ID);
create index IDX_FORMGROWTH0_36_1 on FORMGROWTH0_36 (DEMOGRAPHIC_NO);

prompt
prompt Creating table FORMGROWTHCHART
prompt ==============================
prompt
create table FORMGROWTHCHART
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6),
  PATIENTNAME    VARCHAR2(80),
  RECORDNO       VARCHAR2(10),
  MOTHERSTATURE  VARCHAR2(80),
  FATHERSTATURE  VARCHAR2(80),
  DATE_1         DATE,
  AGE_1          VARCHAR2(5),
  STATURE_1      VARCHAR2(6),
  WEIGHT_1       VARCHAR2(6),
  COMMENT_1      VARCHAR2(25),
  BMI_1          VARCHAR2(5),
  DATE_2         DATE,
  AGE_2          VARCHAR2(5),
  STATURE_2      VARCHAR2(6),
  WEIGHT_2       VARCHAR2(6),
  COMMENT_2      VARCHAR2(25),
  BMI_2          VARCHAR2(5),
  DATE_3         DATE,
  AGE_3          VARCHAR2(5),
  STATURE_3      VARCHAR2(6),
  WEIGHT_3       VARCHAR2(6),
  COMMENT_3      VARCHAR2(25),
  BMI_3          VARCHAR2(5),
  DATE_4         DATE,
  AGE_4          VARCHAR2(5),
  STATURE_4      VARCHAR2(6),
  WEIGHT_4       VARCHAR2(6),
  COMMENT_4      VARCHAR2(25),
  BMI_4          VARCHAR2(5),
  DATE_5         DATE,
  AGE_5          VARCHAR2(5),
  STATURE_5      VARCHAR2(6),
  WEIGHT_5       VARCHAR2(6),
  COMMENT_5      VARCHAR2(25),
  BMI_5          VARCHAR2(5),
  DATE_6         DATE,
  AGE_6          VARCHAR2(5),
  STATURE_6      VARCHAR2(6),
  WEIGHT_6       VARCHAR2(6),
  COMMENT_6      VARCHAR2(25),
  BMI_6          VARCHAR2(5),
  DATE_7         DATE,
  AGE_7          VARCHAR2(5),
  STATURE_7      VARCHAR2(6),
  WEIGHT_7       VARCHAR2(6),
  COMMENT_7      VARCHAR2(25),
  BMI_7          VARCHAR2(5)
)
;
alter table FORMGROWTHCHART
  add primary key (ID);

prompt
prompt Creating table FORMHOMEFALLS
prompt ============================
prompt
create table FORMHOMEFALLS
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null,
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6) not null,
  STUDYID        VARCHAR2(20) default 'N/A' not null,
  FLOOR1Y        NUMBER(1),
  FLOOR1N        NUMBER(1),
  FLOOR2Y        NUMBER(1),
  FLOOR2N        NUMBER(1),
  FLOOR3Y        NUMBER(1),
  FLOOR3N        NUMBER(1),
  FLOOR4Y        NUMBER(1),
  FLOOR4N        NUMBER(1),
  FLOOR4NA       NUMBER(1),
  FURNITURE5Y    NUMBER(1),
  FURNITURE5N    NUMBER(1),
  FURNITURE5NA   NUMBER(1),
  FURNITURE6Y    NUMBER(1),
  FURNITURE6N    NUMBER(1),
  FURNITURE6NA   NUMBER(1),
  LIGHTING7Y     NUMBER(1),
  LIGHTING7N     NUMBER(1),
  LIGHTING8Y     NUMBER(1),
  LIGHTING8N     NUMBER(1),
  LIGHTING9Y     NUMBER(1),
  LIGHTING9N     NUMBER(1),
  LIGHTING9NA    NUMBER(1),
  BATHROOM10Y    NUMBER(1),
  BATHROOM10N    NUMBER(1),
  BATHROOM10NA   NUMBER(1),
  BATHROOM11Y    NUMBER(1),
  BATHROOM11N    NUMBER(1),
  BATHROOM11NA   NUMBER(1),
  BATHROOM12Y    NUMBER(1),
  BATHROOM12N    NUMBER(1),
  BATHROOM12NA   NUMBER(1),
  BATHROOM13Y    NUMBER(1),
  BATHROOM13N    NUMBER(1),
  BATHROOM14Y    NUMBER(1),
  BATHROOM14N    NUMBER(1),
  BATHROOM15Y    NUMBER(1),
  BATHROOM15N    NUMBER(1),
  STORAGE16Y     NUMBER(1),
  STORAGE16N     NUMBER(1),
  STORAGE17Y     NUMBER(1),
  STORAGE17N     NUMBER(1),
  STAIRWAY18Y    NUMBER(1),
  STAIRWAY18N    NUMBER(1),
  STAIRWAY18NA   NUMBER(1),
  STAIRWAY19Y    NUMBER(1),
  STAIRWAY19N    NUMBER(1),
  STAIRWAY19NA   NUMBER(1),
  STAIRWAY20Y    NUMBER(1),
  STAIRWAY20N    NUMBER(1),
  STAIRWAY20NA   NUMBER(1),
  STAIRWAY21Y    NUMBER(1),
  STAIRWAY21N    NUMBER(1),
  STAIRWAY21NA   NUMBER(1),
  STAIRWAY22Y    NUMBER(1),
  STAIRWAY22N    NUMBER(1),
  MOBILITY23Y    NUMBER(1),
  MOBILITY23N    NUMBER(1),
  MOBILITY23NA   NUMBER(1),
  MOBILITY24Y    NUMBER(1),
  MOBILITY24N    NUMBER(1),
  MOBILITY25Y    NUMBER(1),
  MOBILITY25N    NUMBER(1),
  MOBILITY25NA   NUMBER(1)
)
;
alter table FORMHOMEFALLS
  add primary key (ID);

prompt
prompt Creating table FORMIMMUNALLERGY
prompt ===============================
prompt
create table FORMIMMUNALLERGY
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6) not null,
  C_SURNAME      VARCHAR2(30),
  C_GIVENNAME    VARCHAR2(30),
  DATEADMIN      DATE,
  TRADENAME      VARCHAR2(50),
  MANUFACTURER   VARCHAR2(50),
  LOT            VARCHAR2(50),
  EXPIDATE       DATE,
  DOSEADMINSC    NUMBER(1),
  DOSEADMINIM    NUMBER(1),
  DOSEADMINML    NUMBER(1),
  DOSEADMINTXTML VARCHAR2(10),
  LOCLTDEL       NUMBER(1),
  LOCRTDEL       NUMBER(1),
  LOCLTDELOUQ    NUMBER(1),
  LOCRTDELOUQ    NUMBER(1),
  LOCOTHER       NUMBER(1),
  INSTRSTAY20    NUMBER(1),
  INSTREXPECTLOC NUMBER(1),
  INSTRFU        NUMBER(1),
  DISCHNOCOMP    NUMBER(1)
)
;
alter table FORMIMMUNALLERGY
  add primary key (ID);

prompt
prompt Creating table FORMINTAKEA
prompt ==========================
prompt
create table FORMINTAKEA
(
  ID                             NUMBER(11) not null,
  DEMOGRAPHIC_NO                 NUMBER(11) default '0' not null,
  PROVIDER_NO                    NUMBER(11) default '0',
  FORMCREATED                    DATE,
  FORMEDITED                     TIMESTAMP(6) default CURRENT_TIMESTAMP not null,
  ASSESSDATE                     VARCHAR2(24) default ' ',
  ASSESSSTARTTIME                VARCHAR2(24) default ' ',
  ENTERSEATONDATE                VARCHAR2(24) default ' ',
  CBOX_NEWCLIENT                 VARCHAR2(1) default ' ',
  CBOX_DATEOFREADMISSION         VARCHAR2(1) default ' ',
  DATEOFREADMISSION              VARCHAR2(24) default ' ',
  CBOX_ISSTATEMENTREAD           VARCHAR2(1) default ' ',
  CLIENTSURNAME                  VARCHAR2(50) default ' ',
  CLIENTFIRSTNAME                VARCHAR2(50) default ' ',
  MONTH                          VARCHAR2(2) default ' ',
  DAY                            VARCHAR2(2) default ' ',
  YEAR                           VARCHAR2(4) default ' ',
  CBOX_SPEAKENGLISH              VARCHAR2(1) default ' ',
  CBOX_SPEAKFRENCH               VARCHAR2(1) default ' ',
  CBOX_SPEAKOTHER                VARCHAR2(1) default ' ',
  SPEAKOTHER                     VARCHAR2(36) default ' ',
  REASONTOSEATON                 VARCHAR2(255) default ' ',
  EVERATSEATONBEFORE             VARCHAR2(24),
  DATESATSEATON                  VARCHAR2(70) default ' ',
  CBOX_ASSISTINHEALTH            VARCHAR2(1) default ' ',
  CBOX_ASSISTINIDENTIFICATION    VARCHAR2(1) default ' ',
  CBOX_ASSISTINADDICTIONS        VARCHAR2(1) default ' ',
  CBOX_ASSISTINHOUSING           VARCHAR2(1) default ' ',
  CBOX_ASSISTINEDUCATION         VARCHAR2(1) default ' ',
  CBOX_ASSISTINEMPLOYMENT        VARCHAR2(1) default ' ',
  CBOX_ASSISTINFINANCE           VARCHAR2(1) default ' ',
  CBOX_ASSISTINLEGAL             VARCHAR2(1) default ' ',
  CBOX_ASSISTINIMMIGRATION       VARCHAR2(1) default ' ',
  HASWHATID                      VARCHAR2(120) default ' ',
  CBOX_NOID                      VARCHAR2(1) default ' ',
  CBOX_SINCARD                   VARCHAR2(1) default ' ',
  SINNUM                         VARCHAR2(24) default ' ',
  CBOX_HEALTHCARD                VARCHAR2(1) default ' ',
  HEALTHCARDNUM                  VARCHAR2(24) default ' ',
  HEALTHCARDVER                  VARCHAR2(2) default ' ',
  CBOX_BIRTHCERTIFICATE          VARCHAR2(1) default ' ',
  BIRTHCERTIFICATENUM            VARCHAR2(24) default ' ',
  CBOX_CITZENSHIPCARD            VARCHAR2(1) default ' ',
  CITZENSHIPCARDNUM              VARCHAR2(24) default ' ',
  CBOX_IMMIGRANT                 VARCHAR2(1) default ' ',
  IMMIGRANTNUM                   VARCHAR2(24) default ' ',
  CBOX_REFUGEE                   VARCHAR2(1) default ' ',
  REFUGEENUM                     VARCHAR2(24) default ' ',
  CBOX_OTHERID                   VARCHAR2(1) default ' ',
  OTHERIDENTIFICATION            VARCHAR2(70) default ' ',
  CBOX_IDFILED                   VARCHAR2(1) default ' ',
  CBOX_IDNONE                    VARCHAR2(1) default ' ',
  COMMENTSONID                   VARCHAR2(255) default ' ',
  CBOX_OW                        VARCHAR2(1) default ' ',
  CBOX_ODSP                      VARCHAR2(1) default ' ',
  CBOX_WSIB                      VARCHAR2(1) default ' ',
  CBOX_EMPLOYMENT                VARCHAR2(1) default ' ',
  CBOX_EI                        VARCHAR2(1) default ' ',
  CBOX_OAS                       VARCHAR2(1) default ' ',
  CBOX_CPP                       VARCHAR2(1) default ' ',
  CBOX_OTHERINCOME               VARCHAR2(1) default ' ',
  RADIO_ONLINECHECK              VARCHAR2(36) default ' ',
  RADIO_ACTIVE                   VARCHAR2(36) default ' ',
  CBOX_NORECORD                  VARCHAR2(1) default ' ',
  LASTISSUEDATE                  VARCHAR2(24) default ' ',
  OFFICE                         VARCHAR2(50) default ' ',
  WORKERNUM                      VARCHAR2(36) default ' ',
  AMTRECEIVED                    VARCHAR2(9) default ' ',
  RADIO_HASDOCTOR                VARCHAR2(36) default ' ',
  DOCTORNAME                     VARCHAR2(50) default ' ',
  DOCTORNAME2                    VARCHAR2(50) default ' ',
  DOCTORPHONE                    VARCHAR2(24) default ' ',
  DOCTORPHONEEXT                 VARCHAR2(8) default ' ',
  DOCTORADDRESS                  VARCHAR2(120) default ' ',
  RADIO_SEEDOCTOR                VARCHAR2(36) default ' ',
  RADIO_HEALTHISSUE              VARCHAR2(36) default ' ',
  HEALTHISSUEDETAILS             VARCHAR2(255) default ' ',
  CBOX_HASDIABETES               VARCHAR2(1) default ' ',
  CBOX_INSULIN                   VARCHAR2(1) default ' ',
  CBOX_EPILEPSY                  VARCHAR2(1) default ' ',
  CBOX_BLEEDING                  VARCHAR2(1) default ' ',
  CBOX_HEARINGIMPAIR             VARCHAR2(1) default ' ',
  CBOX_VISUALIMPAIR              VARCHAR2(1) default ' ',
  CBOX_MOBILITYIMPAIR            VARCHAR2(1) default ' ',
  MOBILITYIMPAIR                 VARCHAR2(255) default ' ',
  RADIO_OTHERHEALTHCONCERN       VARCHAR2(36) default ' ',
  OTHERHEALTHCONERNS             VARCHAR2(255) default ' ',
  RADIO_TAKEMEDICATION           VARCHAR2(36) default ' ',
  NAMESOFMEDICATION              VARCHAR2(120) default ' ',
  RADIO_HELPOBTAINMEDICATION     VARCHAR2(36) default ' ',
  HELPOBTAINMEDICATION           VARCHAR2(255) default ' ',
  RADIO_ALLERGICTOMEDICATION     VARCHAR2(36) default ' ',
  ALLERGICTOMEDICATIONNAME       VARCHAR2(255) default ' ',
  REACTIONTOMEDICATION           VARCHAR2(255) default ' ',
  RADIO_MENTALHEALTHCONCERNS     VARCHAR2(36) default ' ',
  MENTALHEALTHCONCERNS           VARCHAR2(255) default ' ',
  CBOX_ISSTATEMENT6READ          VARCHAR2(1) default ' ',
  FREQUENCYOFSEEINGDOCTOR        VARCHAR2(8) default ' ',
  CBOX_VISITWALKINCLINIC         VARCHAR2(1) default ' ',
  CBOX_VISITHEALTHCENTER         VARCHAR2(1) default ' ',
  CBOX_VISITEMERGENCYROOM        VARCHAR2(1) default ' ',
  CBOX_VISITOTHERS               VARCHAR2(1) default ' ',
  OTHERSPECIFY                   VARCHAR2(120) default ' ',
  CBOX_VISITHEALTHOFFICE         VARCHAR2(1) default ' ',
  RADIO_SEESAMEDOCTOR            VARCHAR2(36) default ' ',
  FREQUENCYOFSEEINGEMGROOMDOCTOR VARCHAR2(8) default ' ',
  RADIO_DIDNOTRECEIVEHEALTHCARE  VARCHAR2(36) default ' ',
  CBOX_TREATPHYSICALHEALTH       VARCHAR2(1) default ' ',
  CBOX_TREATMENTALHEALTH         VARCHAR2(1) default ' ',
  CBOX_REGULARCHECKUP            VARCHAR2(1) default ' ',
  CBOX_TREATOTHERREASONS         VARCHAR2(1) default ' ',
  TREATOTHERREASONS              VARCHAR2(255) default ' ',
  CBOX_TREATINJURY               VARCHAR2(1) default ' ',
  CBOX_GOTOWALKINCLINIC          VARCHAR2(1) default ' ',
  CBOX_GOTOHEALTHCENTER          VARCHAR2(1) default ' ',
  CBOX_GOTOEMERGENCYROOM         VARCHAR2(1) default ' ',
  CBOX_GOTOOTHERS                VARCHAR2(1) default ' ',
  GOTOOTHERS                     VARCHAR2(255) default ' ',
  CBOX_HEALTHOFFICE              VARCHAR2(1) default ' ',
  RADIO_APPMTSEEDOCTORIN3MTHS    VARCHAR2(36) default ' ',
  RADIO_NEEDREGULARDOCTOR        VARCHAR2(36) default ' ',
  RADIO_OBJECTTOREGDOCTORIN4WKS  VARCHAR2(36) default ' ',
  RADIO_RATEOVERALLHEALTH        VARCHAR2(36) default ' ',
  RADIO_SPEAKTORESEARCHER        VARCHAR2(36) default ' ',
  CONTACTNAME                    VARCHAR2(70) default ' ',
  CONTACTPHONE                   VARCHAR2(24) default ' ',
  CONTACTADDRESS                 VARCHAR2(255) default ' ',
  CONTACTRELATIONSHIP            VARCHAR2(120) default ' ',
  RADIO_HASMENTALILLNESS         VARCHAR2(36) default ' ',
  RADIO_HASDRINKINGPROBLEM       VARCHAR2(36) default ' ',
  RADIO_HASDRUGPROBLEM           VARCHAR2(36) default ' ',
  RADIO_HASHEALTHPROBLEM         VARCHAR2(36) default ' ',
  RADIO_HASBEHAVIORPROBLEM       VARCHAR2(36) default ' ',
  RADIO_NEEDSEATONSERVICE        VARCHAR2(36) default ' ',
  RADIO_SEATONTOUR               VARCHAR2(36) default ' ',
  SEATONNOTTOURED                VARCHAR2(255) default ' ',
  RADIO_PAMPHLETISSUED           VARCHAR2(36) default ' ',
  PAMPHLETNOTISSUED              VARCHAR2(255) default ' ',
  SUMMARY                        VARCHAR2(255) default ' ',
  COMPLETEDBY                    VARCHAR2(120) default ' ',
  ASSESSCOMPLETETIME             VARCHAR2(36) default ' ',
  CBOX_PAMPHLETISSUED            VARCHAR2(1) default ' ',
  CBOX_HOSTEL                    VARCHAR2(1) default ' ',
  CBOX_HOSTEL_FUSION_CARE        VARCHAR2(1) default ' ',
  CBOX_ROTARYCLUB                VARCHAR2(1) default ' ',
  CBOX_ANNEXHARM                 VARCHAR2(1) default ' ',
  CBOX_LONGTERMPROGRAM           VARCHAR2(1) default ' ',
  CBOX_BIRCHMOUNTRESIDENCE       VARCHAR2(1) default ' ',
  CBOX_ONEILLHOUSE               VARCHAR2(1) default ' ',
  CBOX_FORTYORK                  VARCHAR2(1) default ' ',
  CBOX_DOWNSVIEWDELLS            VARCHAR2(1) default ' ',
  CBOX_SHARING                   VARCHAR2(1) default ' ',
  RADIO_SEX                      VARCHAR2(36),
  EFFDATE                        VARCHAR2(10)
)
;
alter table FORMINTAKEA
  add primary key (ID);

prompt
prompt Creating table FORMINTAKEB
prompt ==========================
prompt
create table FORMINTAKEB
(
  ID                             NUMBER(11) not null,
  DEMOGRAPHIC_NO                 NUMBER(11) default '0' not null,
  PROVIDER_NO                    NUMBER(11) default '0',
  FORMCREATED                    DATE default '01-JAN-1900',
  FORMEDITED                     TIMESTAMP(6) default CURRENT_TIMESTAMP not null,
  DATEASSESSMENT                 VARCHAR2(24) default ' ',
  ASSESSSTARTTIME                VARCHAR2(24) default ' ',
  DATEENTEREDSEATON              VARCHAR2(24) default ' ',
  DATEEXITEDSEATON               VARCHAR2(24) default ' ',
  CLIENTSURNAME                  VARCHAR2(50) default ' ',
  CLIENTFIRSTNAME                VARCHAR2(50) default ' ',
  MONTH                          VARCHAR2(2) default ' ',
  DAY                            VARCHAR2(2) default ' ',
  YEAR                           VARCHAR2(4) default ' ',
  CBOX_SPEAKENGLISH              VARCHAR2(1) default ' ',
  CBOX_SPEAKFRENCH               VARCHAR2(1) default ' ',
  CBOX_SPEAKSPANISH              VARCHAR2(1) default ' ',
  CBOX_SPEAKOTHER                VARCHAR2(1) default ' ',
  SPEAKOTHER                     VARCHAR2(50) default ' ',
  HOWHEARABOUTSEATON             VARCHAR2(255) default ' ',
  WHEREBEFORESEATON              VARCHAR2(255) default ' ',
  RADIO_HASIDINFILE              VARCHAR2(24) default ' ',
  CBOX_ASSISTWITHSINCARD         VARCHAR2(1) default ' ',
  CBOX_ASSISTWITHIMMIGRANT       VARCHAR2(1) default ' ',
  CBOX_ASSISTWITHHEALTHCARD      VARCHAR2(1) default ' ',
  CBOX_ASSISTWITHREFUGEE         VARCHAR2(1) default ' ',
  CBOX_ASSISTWITHBIRTHCERT       VARCHAR2(1) default ' ',
  CBOX_ASSISTWITHNONE            VARCHAR2(1) default ' ',
  CBOX_ASSISTWITHCITIZENCARD     VARCHAR2(1) default ' ',
  CBOX_ASSISTWITHOTHER           VARCHAR2(1) default ' ',
  ASSISTWITHOTHER                VARCHAR2(50) default ' ',
  COMMENTSONID                   VARCHAR2(255) default ' ',
  RADIO_HAVEHEALTHCOVERAGE       VARCHAR2(24) default ' ',
  CBOX_HAVEOHIP                  VARCHAR2(1) default ' ',
  CBOX_HAVEODSP                  VARCHAR2(1) default ' ',
  CBOX_HAVEODB                   VARCHAR2(1) default ' ',
  CBOX_HAVEOTHER1                VARCHAR2(1) default ' ',
  HAVEOTHER                      VARCHAR2(70) default ' ',
  RADIO_HAVEMENTALPROBLEM        VARCHAR2(24) default ' ',
  CBOX_HAVESCHIZOPHRENIA         VARCHAR2(1) default ' ',
  RADIO_CAREDFORSCHIZOPHRENIA    VARCHAR2(24) default ' ',
  CBOX_HAVEMANIC                 VARCHAR2(1) default ' ',
  RADIO_CAREDFORMANIC            VARCHAR2(24) default ' ',
  CBOX_HAVEDEPRESSION            VARCHAR2(1) default ' ',
  RADIO_CAREDFORDEPRESSION       VARCHAR2(24) default ' ',
  CBOX_HAVEANXIETY               VARCHAR2(1) default ' ',
  RADIO_CAREDFORANXIETY          VARCHAR2(24) default ' ',
  CBOX_HAVEOTHER2                VARCHAR2(1) default ' ',
  RADIO_CAREDFOROTHER            VARCHAR2(24) default ' ',
  DOCTOR1NAMEADDR                VARCHAR2(255) default ' ',
  DOCTOR1PHONE                   VARCHAR2(24) default ' ',
  DATELASTDOCTOR1CONTACT         VARCHAR2(24) default ' ',
  DOCTOR2NAMEADDR                VARCHAR2(255) default ' ',
  DOCTOR2PHONE                   VARCHAR2(24) default ' ',
  DATELASTDOCTOR2CONTACT         VARCHAR2(24) default ' ',
  RADIO_NEEDASSISTWITHMEDICATION VARCHAR2(24) default ' ',
  CBOX_REMEMBERTOTAKEMEDICATION  VARCHAR2(1) default ' ',
  CBOX_GETMOREMEDICATION         VARCHAR2(1) default ' ',
  CBOX_STOREMEDICATION           VARCHAR2(1) default ' ',
  CBOX_NEEDHELPINOTHER           VARCHAR2(1) default ' ',
  CBOX_TAKEPRESCRIBEDMEDICATION  VARCHAR2(1) default ' ',
  COMMENTSONNEEDHELP             VARCHAR2(255) default ' ',
  RADIO_DOYOUDRINK               VARCHAR2(24) default ' ',
  DRINKSPERDAY                   VARCHAR2(3) default ' ',
  DRINKSPERWEEK                  VARCHAR2(4) default ' ',
  DRINKSPERMONTH                 VARCHAR2(5) default ' ',
  RADIO_HOWMUCHDRINK             VARCHAR2(24) default ' ',
  RADIO_DRINKTHESE               VARCHAR2(24) default ' ',
  RADIO_SEENDOCTORREGALCOHOL     VARCHAR2(24) default ' ',
  RADIO_WANTHELPQUIT             VARCHAR2(24) default ' ',
  COMMENTSONALCOHOL              VARCHAR2(255) default ' ',
  RADIO_USEDRUGS                 VARCHAR2(24) default ' ',
  RADIO_DRUGUSEFREQUENCY         VARCHAR2(24) default ' ',
  RADIO_WANTHELPQUITDRUG         VARCHAR2(24) default ' ',
  COMMENTSONSTREETDRUGS          VARCHAR2(255) default ' ',
  HOUSINGINTERESTED              VARCHAR2(70) default ' ',
  RADIO_WANTAPPMT                VARCHAR2(24) default ' ',
  CLIENTLASTADDRESS              VARCHAR2(255) default ' ',
  CLIENTLASTADDRESSPAYRENT       VARCHAR2(255) default ' ',
  DATELIVEDTHERE                 VARCHAR2(24) default ' ',
  LIVEDWITHWHOM                  VARCHAR2(120) default ' ',
  RADIO_LIVEDINSUBSIDIZED        VARCHAR2(24) default ' ',
  RADIO_OWEDRENT                 VARCHAR2(24) default ' ',
  WHEREOWERENT                   VARCHAR2(255) default ' ',
  AMTOWING                       VARCHAR2(16) default ' ',
  COMMENTSONHOUSING              VARCHAR2(255) default ' ',
  YEARSOFEDUCATION               VARCHAR2(4) default ' ',
  CBOX_HAVEHIGHSCHOOL            VARCHAR2(1) default ' ',
  CBOX_HAVECOLLEGE               VARCHAR2(1) default ' ',
  CBOX_HAVEUNIVERSITY            VARCHAR2(1) default ' ',
  CBOX_HAVEOTHER3                VARCHAR2(1) default ' ',
  RADIO_INTERESTBACKTOSCHOOL     VARCHAR2(24) default ' ',
  RADIO_REQUIREREFERRALTOESL     VARCHAR2(24) default ' ',
  COMMENTSONEDUCATION            VARCHAR2(255) default ' ',
  RADIO_CURRENTLYEMPLOYED        VARCHAR2(24) default ' ',
  HOWLONGEMPLOYED                VARCHAR2(3) default ' ',
  HOWLONGUNEMPLOYED              VARCHAR2(3) default ' ',
  USUALOCCUPATION                VARCHAR2(70) default ' ',
  RADIO_INTERESTEDINTRAINING     VARCHAR2(24) default ' ',
  COMMENTSONEMPLOYMENT           VARCHAR2(255) default ' ',
  MAINSOURCEOFINCOME             VARCHAR2(70) default ' ',
  CBOX_OW                        VARCHAR2(1) default ' ',
  CBOX_ODSP                      VARCHAR2(1) default ' ',
  CBOX_EMPLOYMENT                VARCHAR2(1) default ' ',
  CBOX_UI                        VARCHAR2(1) default ' ',
  CBOX_OAS                       VARCHAR2(1) default ' ',
  CBOX_CPP                       VARCHAR2(1) default ' ',
  CBOX_OTHER                     VARCHAR2(1) default ' ',
  HOWMUCHYOURECEIVE              VARCHAR2(12) default ' ',
  RADIO_HAVEPUBLICTRUSTEE        VARCHAR2(24) default ' ',
  PUBLICTRUSTEEINFO              VARCHAR2(255) default ' ',
  RADIO_ENTITLEDTOOTHERINCOME    VARCHAR2(24) default ' ',
  TYPEOFINCOME                   VARCHAR2(120) default ' ',
  RADIO_EVERMADEAPPFOROTHERINCOM VARCHAR2(24) default ' ',
  EVERMADEAPPFOROTHERINCOME      VARCHAR2(120) default ' ',
  WHENMADEAPPFOROTHERINCOME      VARCHAR2(24) default ' ',
  COMMENTSONFINANCE              VARCHAR2(255) default ' ',
  RADIO_EVERBEENJAILED           VARCHAR2(24) default ' ',
  HISTORYOFJAIL                  VARCHAR2(50) default ' ',
  RADIO_NEEDASSISTINLEGAL        VARCHAR2(24) default ' ',
  NEEDASSISTINLEGAL              VARCHAR2(255) default ' ',
  COMMENTSONLEGALISSUES          VARCHAR2(255) default ' ',
  RADIO_CITIZEN                  VARCHAR2(24) default ' ',
  RADIO_YOURCANADIANSTATUS       VARCHAR2(24) default ' ',
  YOURCANADIANSTATUS             VARCHAR2(50) default ' ',
  RADIO_SPONSORSHIPBREAKDOWN     VARCHAR2(24) default ' ',
  WHYSPONSORSHIPBREAKDOWN        VARCHAR2(255) default ' ',
  SPONSORNAME                    VARCHAR2(50) default ' ',
  NEEDHELPWITHIMMIGRATION        VARCHAR2(50) default ' ',
  COMMENTSONIMMIGRATION          VARCHAR2(255) default ' ',
  RADIO_INVOLVEDOTHERAGENCIES    VARCHAR2(24) default ' ',
  AGENCY1NAME                    VARCHAR2(70) default ' ',
  CONTACT1NAME                   VARCHAR2(50) default ' ',
  CONTACT1PHONE                  VARCHAR2(24) default ' ',
  ASSISTPROVIDED1                VARCHAR2(120) default ' ',
  DATELASTCONTACT1               VARCHAR2(24) default ' ',
  AGENCY2NAME                    VARCHAR2(70) default ' ',
  CONTACT2NAME                   VARCHAR2(50) default ' ',
  CONTACT2PHONE                  VARCHAR2(24) default ' ',
  ASSISTPROVIDED2                VARCHAR2(120) default ' ',
  DATELASTCONTACT2               VARCHAR2(24) default ' ',
  AGENCY3NAME                    VARCHAR2(70) default ' ',
  CONTACT3NAME                   VARCHAR2(50) default ' ',
  CONTACT3PHONE                  VARCHAR2(24) default ' ',
  ASSISTPROVIDED3                VARCHAR2(120) default ' ',
  DATELASTCONTACT3               VARCHAR2(24) default ' ',
  AGENCY4NAME                    VARCHAR2(70) default ' ',
  CONTACT4NAME                   VARCHAR2(50) default ' ',
  CONTACT4PHONE                  VARCHAR2(24) default ' ',
  ASSISTPROVIDED4                VARCHAR2(120) default ' ',
  DATELASTCONTACT4               VARCHAR2(24) default ' ',
  RADIO_MENTALILLNESS            VARCHAR2(24) default ' ',
  RADIO_DRINKING                 VARCHAR2(24) default ' ',
  RADIO_DRUGUSE                  VARCHAR2(24) default ' ',
  RADIO_HEALTHPROBLEM            VARCHAR2(24) default ' ',
  RADIO_BEHAVIORPROBLEM          VARCHAR2(24) default ' ',
  RADIO_NEED60DAYSSEATONSERVICES VARCHAR2(24) default ' ',
  COMPLETEDBY1                   VARCHAR2(50) default ' ',
  COMPLETEDBY2                   VARCHAR2(50) default ' ',
  ASSESSCOMPLETETIME             VARCHAR2(50) default ' ',
  FOLLOWUPAPPMTS                 VARCHAR2(120) default ' ',
  CBOX_PAMPHLETISSUED            VARCHAR2(1) default ' ',
  CBOX_HOSTEL                    VARCHAR2(1) default ' ',
  CBOX_ROTARYCLUB                VARCHAR2(1) default ' ',
  CBOX_ANNEXHARM                 VARCHAR2(1) default ' ',
  CBOX_LONGTERMPROGRAM           VARCHAR2(1) default ' ',
  CBOX_BIRCHMOUNTRESIDENCE       VARCHAR2(1) default ' ',
  CBOX_ONEILLHOUSE               VARCHAR2(1) default ' ',
  CBOX_FORTYORK                  VARCHAR2(1) default ' ',
  CBOX_DOWNSVIEWDELLS            VARCHAR2(1) default ' '
)
;
alter table FORMINTAKEB
  add primary key (ID);

prompt
prompt Creating table FORMINTAKEC
prompt ==========================
prompt
create table FORMINTAKEC
(
  ID                             NUMBER(20) not null,
  CBOXREFERRALBYPOLICE           VARCHAR2(2),
  RADIOGENDER                    VARCHAR2(16),
  CBOXSOCIALSERVICEISSUEOTHER    VARCHAR2(2),
  CBOXBASE2NDINCOMENONE          VARCHAR2(2),
  CBOXCURRHASSIN                 VARCHAR2(2),
  CBOXREFBYFREDVICTORCENTREOTHER VARCHAR2(2),
  CBOXREFERRALBYDETENTIONCENTER  VARCHAR2(2),
  CURRDESCRIPTIONOFHOUSING       VARCHAR2(255),
  CBOXBASEUSEHEALTHBUS           VARCHAR2(2),
  DATEOFHOSPITALIZATION          VARCHAR2(10),
  CBOXHOUSINGISSUE               VARCHAR2(2),
  CBOXBASEHASCOMMUNITY           VARCHAR2(2),
  CBOXBASEHASFRIENDS             VARCHAR2(2),
  CBOXRELOCATIONEXIT             VARCHAR2(2),
  CBOX2NDPERSONALITYDISORDER     VARCHAR2(2),
  CBOXBASE2NDINCOMESOCIALASSIST  VARCHAR2(2),
  CBOX2NDMEDICALMENTALDISORDER   VARCHAR2(2),
  CBOXREFBYCRIMINALJUSTICESYSTEM VARCHAR2(2),
  CBOXTHREATISSUE                VARCHAR2(2),
  CBOXSUBSTANCEANXIETYDISORDER   VARCHAR2(2),
  REFERRALCOMMENT                VARCHAR2(255),
  CBOXBASE2NDINCOMEEI            VARCHAR2(2),
  CBOXCURRHASSUPPORTUNKNOWN      VARCHAR2(2),
  CBOXREFERRALBYSELF             VARCHAR2(2),
  CBOXPHYSICALISSUEOTHER         VARCHAR2(2),
  CBOXCURRLIVINGWITHSPOUSE       VARCHAR2(2),
  CBOXREFERRALBYOTHERINSTITUTION VARCHAR2(2),
  CBOXPHYSICALHOSPITALIZATION    VARCHAR2(2),
  CURRWHYCLIENTNOACCSOCSERVICES  VARCHAR2(255),
  CBOXFINANCIALISSUE             VARCHAR2(2),
  CBOXISOLATIONISSUEOTHER        VARCHAR2(2),
  CBOXCURRLIVINGWITHUNKNOWN      VARCHAR2(2),
  CBOX2NDINCOMEOTHER             VARCHAR2(2),
  CBOXCURRHASUNKNOWN             VARCHAR2(2),
  CBOXREFERRALBYPHYSICIAN        VARCHAR2(2),
  CBOX2NDDISASSOCIATIVEDISORDER  VARCHAR2(2),
  CBOXIMMIGRATIONISSUEOTHER      VARCHAR2(2),
  ENTRYDATE                      VARCHAR2(10),
  ADMISSIONDATE                  VARCHAR2(10),
  CBOXCURRHASCOMMUNITY           VARCHAR2(2),
  CBOXDAILYACTIVITYISSUE         VARCHAR2(2),
  CBOXCURRLIVINGWITHPARENTS      VARCHAR2(2),
  CBOXIDENTIFICATIONISSUEOTHER   VARCHAR2(2),
  RADIOBASEEMPLOYMENTSTATUS      VARCHAR2(16),
  RADIOCANADIANBORN              VARCHAR2(16),
  CBOXBASELIVINGWITHNONRELATIVES VARCHAR2(2),
  CBOXCURRINCOMEMGMNONEEDTRUSTEE VARCHAR2(2),
  CBOXBASE2NDINCOMEFAMILY        VARCHAR2(2),
  RADIOCURREMPLOYMENTSTATUS      VARCHAR2(16),
  CBOXSUICIDEEXIT                VARCHAR2(2),
  CBOXREFERRALBYSTREETNURSEOTHER VARCHAR2(2),
  DAYOFBIRTH                     VARCHAR2(2),
  CBOXDUALDISORDER               VARCHAR2(2),
  CBOX2NDINCOMEODSP              VARCHAR2(2),
  CBOXPSYCHIATRICHOSPITALIZATION VARCHAR2(2),
  CBOX2NDSOMATOFORMDISORDER      VARCHAR2(2),
  CBOXPREADMISSION               VARCHAR2(2),
  CBOXREFBYSTHEALTHRECEPTOTHER   VARCHAR2(2),
  CBOX2NDANXIETYDISORDER         VARCHAR2(2),
  RADIOCURRPARTICIPATEINEDUCTION VARCHAR2(16),
  CBOX2NDCHILDHOODDISORDER       VARCHAR2(2),
  CBOXCURRINCOMEMGMENTHASTRUSTEE VARCHAR2(2),
  RADIOCOUNTRYOFORIGIN           VARCHAR2(16),
  CBOXOTHERCHRONICILLNESS        VARCHAR2(2),
  CBOXMOHLTCDISORDER             VARCHAR2(2),
  CBOXPTSD                       VARCHAR2(2),
  CBOXBASE2NDINCOMEDISASSIST     VARCHAR2(2),
  LENGTHOFHOSPITALIZATION        VARCHAR2(25),
  RADIOCURRPRIMARYINCOMESOURCE   VARCHAR2(16),
  BASEWHYCLIENTNOACCSOCSERVICES  VARCHAR2(255),
  CBOX2NDINCOMEINFORMALOTHER     VARCHAR2(2),
  CBOX2NDINCOMEEI                VARCHAR2(2),
  CBOX2NDGENDERIDENTITYDISORDER  VARCHAR2(2),
  CBOXBASEDONOTACCESSHEALTHCARE  VARCHAR2(2),
  CURRSOCSERVICECLIENTACCESSES   VARCHAR2(255),
  CBOX2NDINCOMEUNKNOWN           VARCHAR2(2),
  RADIOBASEHEALTHCAREACCESS      VARCHAR2(16),
  CBOXBASE2NDINCOMEODSP          VARCHAR2(2),
  CBOXCURRUSEWALKINCLINIC        VARCHAR2(2),
  CBOXREFERRALBYPSYCHIATRISTS    VARCHAR2(2),
  CBOXREFERRALBYMENTALORG        VARCHAR2(2),
  RADIOBASEPRIMARYRESIDENCETYPE  VARCHAR2(16),
  CBOX2NDINCOMEDISASSISTANCE     VARCHAR2(2),
  CBOXADDICTIONISSUE             VARCHAR2(2),
  CBOXBASE2NDINCOMEPANHANDLOTHER VARCHAR2(2),
  CBOXCURRINCOMEMGMENTUNKNOWN    VARCHAR2(2),
  CBOXLEGALISSUE                 VARCHAR2(2),
  RADIORESISTTREATMENT           VARCHAR2(16),
  CBOX2NDSCHIZOPHRENIA           VARCHAR2(2),
  RADIOBASESOCIALSERVICEACCESS   VARCHAR2(16),
  RADIOCURRSOCIALSERVICEACCESS   VARCHAR2(16),
  RADIOBASERESIDENCESTATUS       VARCHAR2(16),
  CBOX2NDINCOMEEMPLOYMENT        VARCHAR2(2),
  CBOX2NDANXIETYDISFROMSUBSTANCE VARCHAR2(2),
  RADIOCURRPRIMARYRESIDENCETYPE  VARCHAR2(16),
  CURRPRIMARYINCOMESOURCEOTHER   VARCHAR2(16),
  CBOXREFERRALBYPROBATION        VARCHAR2(2),
  RADIOCURRLEGALSTATUS           VARCHAR2(16),
  CBOXPROBLEMSWITHPOLICE2        VARCHAR2(2),
  FORMCREATED                    DATE,
  CBOX2NDINCOMESOCASSISTANCE     VARCHAR2(2),
  CBOXBASEUSEHOSPITALEMERGENCY   VARCHAR2(2),
  CBOXREFERRALBYPUBLIC           VARCHAR2(2),
  CBOXREFERRALBYHOSPITAL         VARCHAR2(2),
  CBOXBASELIVINGWITHSPOUSEPLUS   VARCHAR2(2),
  CBOXWITHDRAWALEXIT             VARCHAR2(2),
  CBOXCURRLIVINGWITHSELF         VARCHAR2(2),
  CBOX2NDCOGNITIVEDISORDER       VARCHAR2(2),
  CBOXBASEINCOMEMGMENTUNKNOWN    VARCHAR2(2),
  CBOXBASEHASHEALTHCARD          VARCHAR2(2),
  RADIOCURRHIGHESTEDUCTIONLEVEL  VARCHAR2(16),
  CBOXREFBYPSYCHIATRICHOSPITAL   VARCHAR2(2),
  CBOXCURRLIVINGWITHNONRELATIVES VARCHAR2(2),
  CBOXFAMILYLAWISSUES1           VARCHAR2(2),
  CBOXPROBLEMSWITHPOLICE1        VARCHAR2(2),
  CBOXBASEHASREGHEALTHPROVIDER   VARCHAR2(2),
  CBOXFAMILYLAWISSUES2           VARCHAR2(2),
  CBOXCURRUSESHELTERCLINIC       VARCHAR2(2),
  CLIENTNUM                      VARCHAR2(16),
  CBOX2NDIMPULSIVEDISORDER       VARCHAR2(2),
  RADIOISABORIGINAL              VARCHAR2(16),
  BASESOCSERVICECLIENTACCESSES   VARCHAR2(255),
  CBOXBASE2NDINCOMEOTHER         VARCHAR2(2),
  BASEINCOMEMGMNEEDTRUSTEENOWANT VARCHAR2(2),
  CBOX2NDSLEEPDISORDER           VARCHAR2(2),
  CBOXBASEHASNONSTATUS           VARCHAR2(2),
  CBOXHEALTHCAREISSUEOTHER       VARCHAR2(2),
  RADIOCURRHEALTHCAREACCESS      VARCHAR2(16),
  CBOXBASEHASCERTIFICATE         VARCHAR2(2),
  CBOXBASELIVINGWITHSELF         VARCHAR2(2),
  CBOXCURRINCOMEMGMNEEDSTRUSTEE  VARCHAR2(2),
  CBOXBASE2NDINCOMEUNKNOWN       VARCHAR2(2),
  RADIOLANGUAGEENGLISH           VARCHAR2(16),
  YEARARRIVEDINCANADA            VARCHAR2(16),
  CBOXDATEOFBIRTHUNKNOWN         VARCHAR2(2),
  CBOXOTHERISSUE                 VARCHAR2(2),
  CBOXCURRHASHEALTHCARD          VARCHAR2(2),
  CBOXCURRHASSOMEONE             VARCHAR2(2),
  CBOXCURRHASREGHEALTHPROVIDER   VARCHAR2(2),
  CBOX2NDDEVELOPMENTALDISORDER   VARCHAR2(2),
  CBOXBASE2NDINCOMEINFORMALOTHER VARCHAR2(2),
  CBOXOCD                        VARCHAR2(2),
  CBOXCURRUSEHOSPITALEMERGENCY   VARCHAR2(2),
  CBOXBASELIVINGWITHUNKNOWN      VARCHAR2(2),
  CBOX2NDFACTITIOUSDISORDER      VARCHAR2(2),
  CBOXBASEHASSOMEONE             VARCHAR2(2),
  CBOXRELATIONALISSUE            VARCHAR2(2),
  CBOXBASEHASNATIVECARD          VARCHAR2(2),
  RADIOBASELEGALSTATUS           VARCHAR2(16),
  CBOX2NDINCOMEFAMILY            VARCHAR2(2),
  CBOXCURRHASRELATIVES           VARCHAR2(2),
  CBOXCURRHASCERTIFICATE         VARCHAR2(2),
  MONTHOFBIRTH                   VARCHAR2(2),
  CBOXBASEHASSIN                 VARCHAR2(2),
  CLIENTSURNAME                  VARCHAR2(40),
  CBOX2NDANXIETYDISORDEROCD      VARCHAR2(2),
  STAFFNAME                      VARCHAR2(60),
  CBOXREFBYSTREETIDWORKEROTHER   VARCHAR2(2),
  CBOX2NDSUBSTANCEDISORDER       VARCHAR2(2),
  RADIOCURRNEEDSOCIALSERVICES    VARCHAR2(16),
  RADIOBASEHIGHESTEDULEVEL       VARCHAR2(16),
  CBOXBASEHASSUPPORTUNKNOWN      VARCHAR2(2),
  RADIOCURRRESIDENCESTATUS       VARCHAR2(16),
  CBOXBASEINCOMEMGMNONEEDTRUSTEE VARCHAR2(2),
  COUNTRYOFORIGIN                VARCHAR2(60),
  CBOXCURRHASFRIENDS             VARCHAR2(2),
  RADIOBASEPRIMARYINCOMESOURCE   VARCHAR2(16),
  CBOX2NDANXIETYDISORDEROTHER    VARCHAR2(2),
  CBOXEDUCATIONALISSUE           VARCHAR2(2),
  RADIOBASEPRIINCOMESOURCEOTHER  VARCHAR2(16),
  RADIOBASEPARTICIPATEINEDUCTION VARCHAR2(16),
  CBOXCURRLIVINGWITHRELATIVES    VARCHAR2(2),
  BASEDESCRIPTIONOFHOUSING       VARCHAR2(255),
  CBOXREFERRALBYCOURT            VARCHAR2(2),
  CBOX2NDINCOMEPANHANDLINGOTHER  VARCHAR2(2),
  CBOXREFERRALBYOTHERAGENCY      VARCHAR2(2),
  CBOXCASEFILE                   VARCHAR2(2),
  CBOXCURRLIVINGWITHCHILDREN     VARCHAR2(2),
  CBOXREFBYMENTALHEALTHWORKER    VARCHAR2(2),
  CURRINCOMEMGMNEEDTRUSTEENOWANT VARCHAR2(2),
  CLIENTFIRSTNAME                VARCHAR2(40),
  CBOXBANKINGISSUEOTHER          VARCHAR2(2),
  CBOXBASE2NDINCOMEEMPLOYMENT    VARCHAR2(2),
  CBOXNAEXIT                     VARCHAR2(2),
  CBOX2NDINCOMENONE              VARCHAR2(2),
  CBOXNA                         VARCHAR2(2),
  CBOXBASEHASRELATIVES           VARCHAR2(2),
  CBOXCURRHASNONSTATUS           VARCHAR2(2),
  CBOXEMPLOYMENTISSUE            VARCHAR2(2),
  YEAROFBIRTH                    VARCHAR2(4),
  CBOXBASE2NDINCOMEPENSION       VARCHAR2(2),
  CURRLIVINGWITHSPOUSEPLUS       VARCHAR2(2),
  CBOXPREFERREDLANGUAGEUNKNOWN   VARCHAR2(2),
  CBOX2NDANXIETYDISORDERPSD      VARCHAR2(2),
  CBOXSEXUALABUSEISSUE           VARCHAR2(2),
  FORMEDITED                     DATE,
  CBOXBASEUSESHELTERCLINIC       VARCHAR2(2),
  CBOXCURRACCHEALTHCAREUNKNOWN   VARCHAR2(2),
  CBOX2NDINCOMEPENSION           VARCHAR2(2),
  PROVIDER_NO                    NUMBER(20),
  CBOXCURRNOACCESSHEALTHCARE     VARCHAR2(2),
  CBOXCURRUSEHEALTHBUS           VARCHAR2(2),
  CBOXBASELIVINGWITHPARENTS      VARCHAR2(2),
  CBOXBASEHASUNKNOWN             VARCHAR2(2),
  RADIOBASENEEDSOCIALSERVICES    VARCHAR2(16),
  CBOXNONELISTEDISSUE            VARCHAR2(2),
  CBOX2NDUNKNOWN                 VARCHAR2(2),
  CBOXREFERRALBYSAFEBEDS         VARCHAR2(2),
  CBOX2NDADJUSTDISORDER          VARCHAR2(2),
  CBOXHOSPITALIZATIONUNKNOWN     VARCHAR2(2),
  CBOXCONCURRENTDISORDER         VARCHAR2(2),
  CBOX2NDEATINGDISORDER          VARCHAR2(2),
  CBOXCOMPLETEWITHREFERRAL       VARCHAR2(2),
  CBOXBASEUSEWALKINCLINIC        VARCHAR2(2),
  MONTHLYPROGRESSREPORT          VARCHAR2(255),
  CBOXOTHERANXIETYDISORDER       VARCHAR2(2),
  CBOXBASEINCOMEMGMHASTRUSTEE    VARCHAR2(2),
  DEMOGRAPHIC_NO                 NUMBER(20) default '0' not null,
  CBOXBASEINCOMEMGMNEEDSTRUSTEE  VARCHAR2(2),
  RADIORACECAUCASIAN             VARCHAR2(16),
  CBOXMENTALISSUE                VARCHAR2(2),
  PREFERREDLANGUAGE              VARCHAR2(30),
  CBOX2NDMOODDISORDER            VARCHAR2(2),
  RADIOPRIMARYDIAGNOSIS          VARCHAR2(16),
  CBOXCURRHASNATIVECARD          VARCHAR2(2),
  CBOXBASELIVINGWITHCHILDREN     VARCHAR2(2),
  CBOXBASELIVINGWITHSPOUSE       VARCHAR2(2),
  RADIOTREATMENTORDERS           VARCHAR2(16),
  CBOXREFERRALBYOTHERPEOPLE      VARCHAR2(2),
  CBOXBASEACCHEALTHCAREUNKNOWN   VARCHAR2(2),
  CBOXCOMPLETEWITHOUTREFERRAL    VARCHAR2(2),
  CBOXDEATHEXIT                  VARCHAR2(2),
  CBOXBASELIVINGWITHRELATIVES    VARCHAR2(2),
  CURRADDRESS                    VARCHAR2(255),
  CURRPHONE                      VARCHAR2(255),
  PASTADDRESSES                  VARCHAR2(4000),
  CONTACTSINFO                   VARCHAR2(4000),
  IDS                            VARCHAR2(4000),
  HOSPITALIZATIONS               VARCHAR2(4000)
)
;
alter table FORMINTAKEC
  add primary key (ID);

prompt
prompt Creating table FORMINTAKEINFO
prompt =============================
prompt
create table FORMINTAKEINFO
(
  ID                         NUMBER(10) not null,
  DEMOGRAPHIC_NO             NUMBER(10) not null,
  PROVIDER_NO                NUMBER(10),
  FORMCREATED                DATE,
  FORMEDITED                 TIMESTAMP(6) not null,
  STUDYID                    VARCHAR2(20) default 'N/A' not null,
  SEX                        VARCHAR2(1),
  DOB                        DATE,
  PHONE                      VARCHAR2(20),
  CONTACT                    VARCHAR2(20),
  ETHNOCULTURALGR            VARCHAR2(255),
  ACCOMMODATIONHOUSE         NUMBER(1),
  ACCOMMODATIONAPT           NUMBER(1),
  ACCOMMODATIONSEN           NUMBER(1),
  ACCOMMODATIONNUR           NUMBER(1),
  ACCOMMODATIONLTC           NUMBER(1),
  MARITALSTMARRIED           NUMBER(1),
  MARITALSTLIVINGTOGETHER    NUMBER(1),
  MARITALSTSEPARATED         NUMBER(1),
  MARITALSTDIVORCED          NUMBER(1),
  MARITALSTWIDOWED           NUMBER(1),
  MARITALSTREMARRIED         NUMBER(1),
  MARITALSTNEVERMARRIED      NUMBER(1),
  NBPPLINHOUSEHOLD           NUMBER(3),
  HELPY                      NUMBER(1),
  HELPN                      NUMBER(1),
  HELPNONEED                 NUMBER(1),
  HELPRELATIONSHIPSPOUSE     NUMBER(1),
  HELPRELATIONSHIPSIBLING    NUMBER(1),
  HELPRELATIONSHIPFRIEND     NUMBER(1),
  HELPRELATIONSHIPCHILDREN   NUMBER(1),
  HELPRELATIONSHIPGCHILDREN  NUMBER(1),
  OTHERSUPPORTFRIENDS        NUMBER(1),
  OTHERSUPPORTSIBLINGS       NUMBER(1),
  OTHERSUPPORTNEIGHBOUR      NUMBER(1),
  OTHERSUPPORTCHILDREN       NUMBER(1),
  OTHERSUPPORTGCHILDREN      NUMBER(1),
  EDUNOSCHOOL                NUMBER(1),
  EDUSOMECOMMUNITY           NUMBER(1),
  EDUSOMEELEMENTARY          NUMBER(1),
  EDUCOMPLETEDCOMMUNITY      NUMBER(1),
  EDUCOMPLETEDELEMENTARY     NUMBER(1),
  EDUSOMEUNI                 NUMBER(1),
  EDUSOMESEC                 NUMBER(1),
  EDUCOMPLETEDUNI            NUMBER(1),
  EDUCOMPLETEDSEC            NUMBER(1),
  INCOMEBELOW10              NUMBER(1),
  INCOME40TO50               NUMBER(1),
  INCOME10TO20               NUMBER(1),
  INCOMEOVER50               NUMBER(1),
  INCOME20TO30               NUMBER(1),
  INCOMEDONOTKNOW            NUMBER(1),
  INCOME30TO40               NUMBER(1),
  INCOMEREFUSEDTOANS         NUMBER(1),
  FINANCIALDIFFICULT         NUMBER(1),
  FINANCIALENOUGH            NUMBER(1),
  FINANCIALCOMFORTABLE       NUMBER(1),
  ACTPAIDWK                  NUMBER(4),
  ACTUNPAIDWK                NUMBER(4),
  ACTVOLUNTEERING            NUMBER(4),
  ACTCAREGIVING              NUMBER(4),
  ANYHEALTHPB                VARCHAR2(255),
  SMKY                       NUMBER(1),
  SMKN                       NUMBER(1),
  NBCIGARETTES               NUMBER(5),
  HOWLONGSMK                 VARCHAR2(20),
  DIDSMKY                    NUMBER(1),
  DIDSMKN                    NUMBER(1),
  DIDNBCIGARETTES            NUMBER(5),
  DIDHOWLONGSMK              VARCHAR2(20),
  ALCOHOLY                   NUMBER(1),
  ALCOHOLN                   NUMBER(1),
  MORE12DRINKSY              NUMBER(1),
  MORE12DRINKSN              NUMBER(1),
  HEARTATTACKY               NUMBER(1),
  HEARTATTACKREFUSED         NUMBER(1),
  HEARTATTACKN               NUMBER(1),
  HEARTATTACKDONOTKNOW       NUMBER(1),
  ANGINAY                    NUMBER(1),
  ANGINAREFUSED              NUMBER(1),
  ANGINAN                    NUMBER(1),
  ANGINADONOTKNOW            NUMBER(1),
  HEARTFAILUREY              NUMBER(1),
  HEARTFAILUREREFUSED        NUMBER(1),
  HEARTFAILUREN              NUMBER(1),
  HEARTFAILUREDONOTKNOW      NUMBER(1),
  HIGHBPY                    NUMBER(1),
  HIGHBPREFUSED              NUMBER(1),
  HIGHBPN                    NUMBER(1),
  HIGHBPDONOTKNOW            NUMBER(1),
  OTHERHEARTDISEASEY         NUMBER(1),
  OTHERHEARTDISEASEREFUSED   NUMBER(1),
  OTHERHEARTDISEASEN         NUMBER(1),
  OTHERHEARTDISEASEDONOTKNOW NUMBER(1),
  DIABETESY                  NUMBER(1),
  DIABETESREFUSED            NUMBER(1),
  DIABETESN                  NUMBER(1),
  DIABETESDONOTKNOW          NUMBER(1),
  ARTHRITISY                 NUMBER(1),
  ARTHRITISREFUSED           NUMBER(1),
  ARTHRITISN                 NUMBER(1),
  ARTHRITISDONOTKNOW         NUMBER(1),
  STROKEY                    NUMBER(1),
  STROKEREFUSED              NUMBER(1),
  STROKEN                    NUMBER(1),
  STROKEDONOTKNOW            NUMBER(1),
  CANCERY                    NUMBER(1),
  CANCERREFUSED              NUMBER(1),
  CANCERN                    NUMBER(1),
  CANCERDONOTKNOW            NUMBER(1),
  BROKENHIPY                 NUMBER(1),
  BROKENHIPREFUSED           NUMBER(1),
  BROKENHIPN                 NUMBER(1),
  BROKENHIPDONOTKNOW         NUMBER(1),
  PARKINSONY                 NUMBER(1),
  PARKINSONREFUSED           NUMBER(1),
  PARKINSONN                 NUMBER(1),
  PARKINSONDONOTKNOW         NUMBER(1),
  LUNGDISEASEY               NUMBER(1),
  LUNGDISEASEREFUSED         NUMBER(1),
  LUNGDISEASEN               NUMBER(1),
  LUNGDISEASEDONOTKNOW       NUMBER(1),
  HEARINGPBY                 NUMBER(1),
  HEARINGPBREFUSED           NUMBER(1),
  HEARINGPBN                 NUMBER(1),
  HEARINGPBDONOTKNOW         NUMBER(1),
  VISIONPBY                  NUMBER(1),
  VISIONPBREFUSED            NUMBER(1),
  VISIONPBN                  NUMBER(1),
  VISIONPBDONOTKNOW          NUMBER(1),
  OSTEOPOROSISY              NUMBER(1),
  OSTEOPOROSISREFUSED        NUMBER(1),
  OSTEOPOROSISN              NUMBER(1),
  OSTEOPOROSISDONOTKNOW      NUMBER(1),
  FIBROMYALGIAY              NUMBER(1),
  FIBROMYALGIAREFUSED        NUMBER(1),
  FIBROMYALGIAN              NUMBER(1),
  FIBROMYALGIADONOTKNOW      NUMBER(1),
  MULTIPLESCLEROSISY         NUMBER(1),
  MULTIPLESCLEROSISREFUSED   NUMBER(1),
  MULTIPLESCLEROSISN         NUMBER(1),
  MULTIPLESCLEROSISDONOTKNOW NUMBER(1),
  ASTHMAY                    NUMBER(1),
  ASTHMAREFUSED              NUMBER(1),
  ASTHMAN                    NUMBER(1),
  ASTHMADONOTKNOW            NUMBER(1),
  BACKPAINY                  NUMBER(1),
  BACKPAINREFUSED            NUMBER(1),
  BACKPAINN                  NUMBER(1),
  BACKPAINDONOTKNOW          NUMBER(1),
  WEIGHTY                    NUMBER(1),
  WEIGHTREFUSED              NUMBER(1),
  WEIGHTN                    NUMBER(1),
  WEIGHTDONOTKNOW            NUMBER(1),
  WEIGHT                     VARCHAR2(10),
  HEIGHT                     VARCHAR2(10)
)
;
alter table FORMINTAKEINFO
  add primary key (ID);

prompt
prompt Creating table FORMINTERNETACCESS
prompt =================================
prompt
create table FORMINTERNETACCESS
(
  ID              NUMBER(10) not null,
  DEMOGRAPHIC_NO  NUMBER(10) not null,
  PROVIDER_NO     NUMBER(10),
  FORMCREATED     DATE,
  FORMEDITED      TIMESTAMP(6) not null,
  STUDYID         VARCHAR2(20) default 'N/A' not null,
  COMPUTERY       NUMBER(1),
  COMPUTERN       NUMBER(1),
  INTERNETY       NUMBER(1),
  INTERNETN       NUMBER(1),
  INTERNETHOME    NUMBER(1),
  INTERNETWORK    NUMBER(1),
  INTERNETOTHER   NUMBER(1),
  INTERNETOTHERTX VARCHAR2(255),
  TIMEDAILY       VARCHAR2(2),
  TIMEWEEKLY      VARCHAR2(3),
  INFOY           NUMBER(1),
  INFON           NUMBER(1)
)
;
alter table FORMINTERNETACCESS
  add primary key (ID);

prompt
prompt Creating table FORMLABREQ
prompt =========================
prompt
create table FORMLABREQ
(
  ID                   NUMBER(10) not null,
  DEMOGRAPHIC_NO       NUMBER(10),
  PROVIDER_NO          NUMBER(10),
  FORMCREATED          DATE,
  FORMEDITED           TIMESTAMP(6) not null,
  PROVNAME             VARCHAR2(60),
  REQPROVNAME          VARCHAR2(60),
  CLINICADDRESS        VARCHAR2(30),
  CLINICCITY           VARCHAR2(20),
  CLINICPC             VARCHAR2(7),
  PRACTITIONERNO       VARCHAR2(14),
  OHIP                 NUMBER(1),
  THIRDPARTY           NUMBER(1),
  WCB                  NUMBER(1),
  ACI                  VARCHAR2(4000),
  HEALTHNUMBER         VARCHAR2(10),
  VERSION              CHAR(2),
  BIRTHDATE            DATE,
  PAYMENTPROGRAM       VARCHAR2(4),
  PROVINCE             VARCHAR2(15),
  ORN                  VARCHAR2(12),
  PHONENUMBER          VARCHAR2(20),
  PATIENTNAME          VARCHAR2(40),
  SEX                  VARCHAR2(6),
  PATIENTADDRESS       VARCHAR2(20),
  PATIENTCITY          VARCHAR2(20),
  PATIENTPC            VARCHAR2(7),
  B_GLUCOSE            NUMBER(1),
  B_CREATINE           NUMBER(1),
  B_URICACID           NUMBER(1),
  B_SODIUM             NUMBER(1),
  B_POTASSIUM          NUMBER(1),
  B_CHLORIDE           NUMBER(1),
  B_AST                NUMBER(1),
  B_ALKPHOSPHATE       NUMBER(1),
  B_BILIRUBIN          NUMBER(1),
  B_CHOLESTEROL        NUMBER(1),
  B_TRIGLYCERIDE       NUMBER(1),
  B_URINALYSIS         NUMBER(1),
  V_ACUTEHEPATITIS     NUMBER(1),
  V_CHRONICHEPATITIS   NUMBER(1),
  V_IMMUNE             NUMBER(1),
  V_HEPA               VARCHAR2(20),
  V_HEPB               VARCHAR2(20),
  H_BLOODFILMEXAM      NUMBER(1),
  H_HEMOGLOBIN         NUMBER(1),
  H_WCBCOUNT           NUMBER(1),
  H_HEMATOCRIT         NUMBER(1),
  H_PROTHROMBTIME      NUMBER(1),
  H_OTHERC             NUMBER(1),
  H_OTHER              VARCHAR2(20),
  I_PREGNANCYTEST      NUMBER(1),
  I_HETEROPHILE        NUMBER(1),
  I_RUBELLA            NUMBER(1),
  I_PRENATAL           NUMBER(1),
  I_REPEATPRENATAL     NUMBER(1),
  I_PRENATALHEPATITISB NUMBER(1),
  I_VDRL               NUMBER(1),
  I_OTHERC             NUMBER(1),
  I_OTHER              VARCHAR2(20),
  M_CERVICALVAGINAL    NUMBER(1),
  M_SPUTUM             NUMBER(1),
  M_THROAT             NUMBER(1),
  M_URINE              NUMBER(1),
  M_STOOLCULTURE       NUMBER(1),
  M_OTHERSWABS         NUMBER(1),
  M_OTHER              VARCHAR2(20),
  OTHERTEST            VARCHAR2(4000),
  FORMDATE             DATE,
  SIGNATURE            VARCHAR2(60)
)
;
alter table FORMLABREQ
  add primary key (ID);

prompt
prompt Creating table FORMLABREQ07
prompt ===========================
prompt
create table FORMLABREQ07
(
  ID                          NUMBER(10) not null,
  FORMEDITED                  TIMESTAMP(6) default CURRENT_TIMESTAMP not null,
  B_GLUCOSE                   NUMBER(1),
  B_GLUCOSE_RANDOM            NUMBER(1),
  B_GLUCOSE_FASTING           NUMBER(1),
  B_HBA1C                     NUMBER(1),
  B_CREATININE                NUMBER(1),
  B_URICACID                  NUMBER(1),
  B_SODIUM                    NUMBER(1),
  B_POTASSIUM                 NUMBER(1),
  B_CHLORIDE                  NUMBER(1),
  B_CK                        NUMBER(1),
  B_ALT                       NUMBER(1),
  B_ALKPHOSPHATASE            NUMBER(1),
  OHIP                        NUMBER(1),
  FORMCREATED                 DATE,
  PATIENTFIRSTNAME            VARCHAR2(40),
  B_TIMENEXTDOSE2             VARCHAR2(20),
  CLINICCITY                  VARCHAR2(40),
  B_TIMENEXTDOSE1             VARCHAR2(20),
  B_TIMELASTDOSE2             VARCHAR2(20),
  B_TIMELASTDOSE1             VARCHAR2(20),
  PROVIDER_NO                 NUMBER(11),
  M_VAGINALRECTAL             NUMBER(1),
  M_VAGINAL                   NUMBER(1),
  B_ACRATIOURINE              NUMBER(1),
  HEALTHNUMBER                VARCHAR2(20),
  PATIENTCITY                 VARCHAR2(40),
  B_PATIENTSTELNO             VARCHAR2(40),
  CLINICIANCONTACTURGENT      VARCHAR2(40),
  B_CHILDSAGEDAYS             VARCHAR2(20),
  B_TSH                       NUMBER(1),
  B_TIMECOLLECTED2            VARCHAR2(20),
  B_TIMECOLLECTED1            VARCHAR2(20),
  B_THERAPEUTICDRUGMONITORING NUMBER(1),
  I_RUBELLA                   NUMBER(1),
  M_CERVICAL                  NUMBER(1),
  PATIENTADDRESS              VARCHAR2(60),
  B_VITAMINB12                NUMBER(1),
  M_BLANK                     NUMBER(1),
  REQPROVNAME                 VARCHAR2(60),
  V_CHRONICHEPATITIS          NUMBER(1),
  VERSION                     VARCHAR2(6),
  OPRN                        VARCHAR2(40),
  B_ALBUMIN                   NUMBER(1),
  ACI                         VARCHAR2(4000),
  PATIENTPC                   VARCHAR2(10),
  B_URINALYSIS                NUMBER(1),
  H_CBC                       NUMBER(1),
  FORM_CLASS                  NUMBER(1),
  SEX                         VARCHAR2(8),
  I_PREGNANCYTEST             NUMBER(1),
  SUBMIT                      NUMBER(1),
  V_ACUTEHEPATITIS            NUMBER(1),
  BIRTHDATE                   DATE,
  PATIENTBIRTHMTH             VARCHAR2(25),
  PHONENUMBER                 VARCHAR2(20),
  M_OTHERSWABSSOURCE          VARCHAR2(60),
  M_OTHERSWABSPUS             NUMBER(1),
  CLINICADDRESS               VARCHAR2(100),
  M_GCSOURCE                  VARCHAR2(60),
  B_CHILDSAGEHOURS            VARCHAR2(20),
  M_THROAT                    NUMBER(1),
  PATIENTLASTNAME             VARCHAR2(40),
  M_SPUTUM                    NUMBER(1),
  B_NEONATALBILIRUBIN         NUMBER(1),
  M_FECALOCCULTBLOOD          NUMBER(1),
  M_URINE                     NUMBER(1),
  M_BLANKTEXT                 VARCHAR2(100),
  B_NAMEDRUG2                 VARCHAR2(100),
  M_STOOLOVAPARASITES         NUMBER(1),
  B_NAMEDRUG1                 VARCHAR2(100),
  M_GC                        NUMBER(1),
  I_PRENATAL                  NUMBER(1),
  H_PROTHROMBINTIME           NUMBER(1),
  PATIENTBIRTHYEAR            VARCHAR2(10),
  M_CHLAMYDIASOURCE           VARCHAR2(60),
  I_REPEATPRENATALANTIBODIES  NUMBER(1),
  B_FERRITIN                  NUMBER(1),
  WCB                         NUMBER(1),
  V_IMMUNESTATUS              NUMBER(1),
  M_STOOLCULTURE              NUMBER(1),
  PROVINCE                    VARCHAR2(20),
  M_WOUNDSOURCE               VARCHAR2(60),
  PATIENTBIRTHDAY             VARCHAR2(30),
  DEMOGRAPHIC_NO              NUMBER(11),
  I_MONONUCLEOSISSCREEN       NUMBER(1),
  B_LIPIDASSESSMENT           NUMBER(1),
  M_WOUND                     NUMBER(1),
  PROVNAME                    VARCHAR2(60),
  THIRDPARTY                  NUMBER(1),
  CLINICNAME                  VARCHAR2(60),
  B_BILIRUBIN                 NUMBER(1),
  M_SPECIMENCOLLECTIONTIME    VARCHAR2(15),
  O_OTHERTESTS16              VARCHAR2(150),
  O_OTHERTESTS15              VARCHAR2(150),
  O_OTHERTESTS14              VARCHAR2(150),
  O_OTHERTESTS13              VARCHAR2(150),
  O_OTHERTESTS12              VARCHAR2(150),
  O_OTHERTESTS11              VARCHAR2(150),
  O_OTHERTESTS10              VARCHAR2(150),
  O_OTHERTESTS9               VARCHAR2(150),
  O_OTHERTESTS8               VARCHAR2(150),
  O_OTHERTESTS7               VARCHAR2(150),
  O_OTHERTESTS6               VARCHAR2(150),
  O_SPECIMENCOLLECTIONDATE    DATE,
  O_OTHERTESTS5               VARCHAR2(150),
  CLINICPC                    VARCHAR2(10),
  O_OTHERTESTS4               VARCHAR2(150),
  O_OTHERTESTS3               VARCHAR2(150),
  O_OTHERTESTS2               VARCHAR2(150),
  B_CLINICIANSTELNO           VARCHAR2(40),
  O_OTHERTESTS1               VARCHAR2(150),
  PATIENTNAME                 VARCHAR2(40),
  B_DATESIGNED                DATE,
  PRACTITIONERNO              VARCHAR2(20),
  M_CHLAMYDIA                 NUMBER(1),
  V_IMMUNE_HEPATITISC         NUMBER(1),
  V_IMMUNE_HEPATITISB         NUMBER(1),
  V_IMMUNE_HEPATITISA         NUMBER(1)
)
;
alter table FORMLABREQ07
  add primary key (ID);

prompt
prompt Creating table FORMLATELIFEFDIDISABILITY
prompt ========================================
prompt
create table FORMLATELIFEFDIDISABILITY
(
  ID              NUMBER(10) not null,
  DEMOGRAPHIC_NO  NUMBER(10) not null,
  PROVIDER_NO     NUMBER(10),
  FORMCREATED     DATE,
  FORMEDITED      TIMESTAMP(6) not null,
  STUDYID         VARCHAR2(20) default 'N/A' not null,
  D1VERYOFTEN     NUMBER(1),
  D1OFTEN         NUMBER(1),
  D1ONCEINAWHILE  NUMBER(1),
  D1ALMOSTNEVER   NUMBER(1),
  D1NEVER         NUMBER(1),
  D1NOT           NUMBER(1),
  D1LITTLE        NUMBER(1),
  D1SOMEWHAT      NUMBER(1),
  D1ALOT          NUMBER(1),
  D1COMPLETELY    NUMBER(1),
  D2VERYOFTEN     NUMBER(1),
  D2OFTEN         NUMBER(1),
  D2ONCEINAWHILE  NUMBER(1),
  D2ALMOSTNEVER   NUMBER(1),
  D2NEVER         NUMBER(1),
  D2NOT           NUMBER(1),
  D2LITTLE        NUMBER(1),
  D2SOMEWHAT      NUMBER(1),
  D2ALOT          NUMBER(1),
  D2COMPLETELY    NUMBER(1),
  D3VERYOFTEN     NUMBER(1),
  D3OFTEN         NUMBER(1),
  D3ONCEINAWHILE  NUMBER(1),
  D3ALMOSTNEVER   NUMBER(1),
  D3NEVER         NUMBER(1),
  D3NOT           NUMBER(1),
  D3LITTLE        NUMBER(1),
  D3SOMEWHAT      NUMBER(1),
  D3ALOT          NUMBER(1),
  D3COMPLETELY    NUMBER(1),
  D4VERYOFTEN     NUMBER(1),
  D4OFTEN         NUMBER(1),
  D4ONCEINAWHILE  NUMBER(1),
  D4ALMOSTNEVER   NUMBER(1),
  D4NEVER         NUMBER(1),
  D4NOT           NUMBER(1),
  D4LITTLE        NUMBER(1),
  D4SOMEWHAT      NUMBER(1),
  D4ALOT          NUMBER(1),
  D4COMPLETELY    NUMBER(1),
  D5VERYOFTEN     NUMBER(1),
  D5OFTEN         NUMBER(1),
  D5ONCEINAWHILE  NUMBER(1),
  D5ALMOSTNEVER   NUMBER(1),
  D5NEVER         NUMBER(1),
  D5NOT           NUMBER(1),
  D5LITTLE        NUMBER(1),
  D5SOMEWHAT      NUMBER(1),
  D5ALOT          NUMBER(1),
  D5COMPLETELY    NUMBER(1),
  D6VERYOFTEN     NUMBER(1),
  D6OFTEN         NUMBER(1),
  D6ONCEINAWHILE  NUMBER(1),
  D6ALMOSTNEVER   NUMBER(1),
  D6NEVER         NUMBER(1),
  D6NOT           NUMBER(1),
  D6LITTLE        NUMBER(1),
  D6SOMEWHAT      NUMBER(1),
  D6ALOT          NUMBER(1),
  D6COMPLETELY    NUMBER(1),
  D7VERYOFTEN     NUMBER(1),
  D7OFTEN         NUMBER(1),
  D7ONCEINAWHILE  NUMBER(1),
  D7ALMOSTNEVER   NUMBER(1),
  D7NEVER         NUMBER(1),
  D7NOT           NUMBER(1),
  D7LITTLE        NUMBER(1),
  D7SOMEWHAT      NUMBER(1),
  D7ALOT          NUMBER(1),
  D7COMPLETELY    NUMBER(1),
  D8VERYOFTEN     NUMBER(1),
  D8OFTEN         NUMBER(1),
  D8ONCEINAWHILE  NUMBER(1),
  D8ALMOSTNEVER   NUMBER(1),
  D8NEVER         NUMBER(1),
  D8NOT           NUMBER(1),
  D8LITTLE        NUMBER(1),
  D8SOMEWHAT      NUMBER(1),
  D8ALOT          NUMBER(1),
  D8COMPLETELY    NUMBER(1),
  D9VERYOFTEN     NUMBER(1),
  D9OFTEN         NUMBER(1),
  D9ONCEINAWHILE  NUMBER(1),
  D9ALMOSTNEVER   NUMBER(1),
  D9NEVER         NUMBER(1),
  D9NOT           NUMBER(1),
  D9LITTLE        NUMBER(1),
  D9SOMEWHAT      NUMBER(1),
  D9ALOT          NUMBER(1),
  D9COMPLETELY    NUMBER(1),
  D10VERYOFTEN    NUMBER(1),
  D10OFTEN        NUMBER(1),
  D10ONCEINAWHILE NUMBER(1),
  D10ALMOSTNEVER  NUMBER(1),
  D10NEVER        NUMBER(1),
  D10NOT          NUMBER(1),
  D10LITTLE       NUMBER(1),
  D10SOMEWHAT     NUMBER(1),
  D10ALOT         NUMBER(1),
  D10COMPLETELY   NUMBER(1),
  D11VERYOFTEN    NUMBER(1),
  D11OFTEN        NUMBER(1),
  D11ONCEINAWHILE NUMBER(1),
  D11ALMOSTNEVER  NUMBER(1),
  D11NEVER        NUMBER(1),
  D11NOT          NUMBER(1),
  D11LITTLE       NUMBER(1),
  D11SOMEWHAT     NUMBER(1),
  D11ALOT         NUMBER(1),
  D11COMPLETELY   NUMBER(1),
  D12VERYOFTEN    NUMBER(1),
  D12OFTEN        NUMBER(1),
  D12ONCEINAWHILE NUMBER(1),
  D12ALMOSTNEVER  NUMBER(1),
  D12NEVER        NUMBER(1),
  D12NOT          NUMBER(1),
  D12LITTLE       NUMBER(1),
  D12SOMEWHAT     NUMBER(1),
  D12ALOT         NUMBER(1),
  D12COMPLETELY   NUMBER(1),
  D13VERYOFTEN    NUMBER(1),
  D13OFTEN        NUMBER(1),
  D13ONCEINAWHILE NUMBER(1),
  D13ALMOSTNEVER  NUMBER(1),
  D13NEVER        NUMBER(1),
  D13NOT          NUMBER(1),
  D13LITTLE       NUMBER(1),
  D13SOMEWHAT     NUMBER(1),
  D13ALOT         NUMBER(1),
  D13COMPLETELY   NUMBER(1),
  D14VERYOFTEN    NUMBER(1),
  D14OFTEN        NUMBER(1),
  D14ONCEINAWHILE NUMBER(1),
  D14ALMOSTNEVER  NUMBER(1),
  D14NEVER        NUMBER(1),
  D14NOT          NUMBER(1),
  D14LITTLE       NUMBER(1),
  D14SOMEWHAT     NUMBER(1),
  D14ALOT         NUMBER(1),
  D14COMPLETELY   NUMBER(1),
  D15VERYOFTEN    NUMBER(1),
  D15OFTEN        NUMBER(1),
  D15ONCEINAWHILE NUMBER(1),
  D15ALMOSTNEVER  NUMBER(1),
  D15NEVER        NUMBER(1),
  D15NOT          NUMBER(1),
  D15LITTLE       NUMBER(1),
  D15SOMEWHAT     NUMBER(1),
  D15ALOT         NUMBER(1),
  D15COMPLETELY   NUMBER(1),
  D16VERYOFTEN    NUMBER(1),
  D16OFTEN        NUMBER(1),
  D16ONCEINAWHILE NUMBER(1),
  D16ALMOSTNEVER  NUMBER(1),
  D16NEVER        NUMBER(1),
  D16NOT          NUMBER(1),
  D16LITTLE       NUMBER(1),
  D16SOMEWHAT     NUMBER(1),
  D16ALOT         NUMBER(1),
  D16COMPLETELY   NUMBER(1)
)
;
alter table FORMLATELIFEFDIDISABILITY
  add primary key (ID);

prompt
prompt Creating table FORMLATELIFEFDIFUNCTION
prompt ======================================
prompt
create table FORMLATELIFEFDIFUNCTION
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null,
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6) not null,
  STUDYID        VARCHAR2(20) default 'N/A' not null,
  F1NONE         NUMBER(1),
  F1ALITTLE      NUMBER(1),
  F1SOME         NUMBER(1),
  F1ALOT         NUMBER(1),
  F1CANNOT       NUMBER(1),
  F2NONE         NUMBER(1),
  F2ALITTLE      NUMBER(1),
  F2SOME         NUMBER(1),
  F2ALOT         NUMBER(1),
  F2CANNOT       NUMBER(1),
  F3NONE         NUMBER(1),
  F3ALITTLE      NUMBER(1),
  F3SOME         NUMBER(1),
  F3ALOT         NUMBER(1),
  F3CANNOT       NUMBER(1),
  F4NONE         NUMBER(1),
  F4ALITTLE      NUMBER(1),
  F4SOME         NUMBER(1),
  F4ALOT         NUMBER(1),
  F4CANNOT       NUMBER(1),
  F5NONE         NUMBER(1),
  F5ALITTLE      NUMBER(1),
  F5SOME         NUMBER(1),
  F5ALOT         NUMBER(1),
  F5CANNOT       NUMBER(1),
  F6NONE         NUMBER(1),
  F6ALITTLE      NUMBER(1),
  F6SOME         NUMBER(1),
  F6ALOT         NUMBER(1),
  F6CANNOT       NUMBER(1),
  F7NONE         NUMBER(1),
  F7ALITTLE      NUMBER(1),
  F7SOME         NUMBER(1),
  F7ALOT         NUMBER(1),
  F7CANNOT       NUMBER(1),
  F8NONE         NUMBER(1),
  F8ALITTLE      NUMBER(1),
  F8SOME         NUMBER(1),
  F8ALOT         NUMBER(1),
  F8CANNOT       NUMBER(1),
  F9NONE         NUMBER(1),
  F9ALITTLE      NUMBER(1),
  F9SOME         NUMBER(1),
  F9ALOT         NUMBER(1),
  F9CANNOT       NUMBER(1),
  F10NONE        NUMBER(1),
  F10ALITTLE     NUMBER(1),
  F10SOME        NUMBER(1),
  F10ALOT        NUMBER(1),
  F10CANNOT      NUMBER(1),
  F11NONE        NUMBER(1),
  F11ALITTLE     NUMBER(1),
  F11SOME        NUMBER(1),
  F11ALOT        NUMBER(1),
  F11CANNOT      NUMBER(1),
  F12NONE        NUMBER(1),
  F12ALITTLE     NUMBER(1),
  F12SOME        NUMBER(1),
  F12ALOT        NUMBER(1),
  F12CANNOT      NUMBER(1),
  F13NONE        NUMBER(1),
  F13ALITTLE     NUMBER(1),
  F13SOME        NUMBER(1),
  F13ALOT        NUMBER(1),
  F13CANNOT      NUMBER(1),
  F14NONE        NUMBER(1),
  F14ALITTLE     NUMBER(1),
  F14SOME        NUMBER(1),
  F14ALOT        NUMBER(1),
  F14CANNOT      NUMBER(1),
  F15NONE        NUMBER(1),
  F15ALITTLE     NUMBER(1),
  F15SOME        NUMBER(1),
  F15ALOT        NUMBER(1),
  F15CANNOT      NUMBER(1),
  F16NONE        NUMBER(1),
  F16ALITTLE     NUMBER(1),
  F16SOME        NUMBER(1),
  F16ALOT        NUMBER(1),
  F16CANNOT      NUMBER(1),
  F17NONE        NUMBER(1),
  F17ALITTLE     NUMBER(1),
  F17SOME        NUMBER(1),
  F17ALOT        NUMBER(1),
  F17CANNOT      NUMBER(1),
  F18NONE        NUMBER(1),
  F18ALITTLE     NUMBER(1),
  F18SOME        NUMBER(1),
  F18ALOT        NUMBER(1),
  F18CANNOT      NUMBER(1),
  F19NONE        NUMBER(1),
  F19ALITTLE     NUMBER(1),
  F19SOME        NUMBER(1),
  F19ALOT        NUMBER(1),
  F19CANNOT      NUMBER(1),
  F20NONE        NUMBER(1),
  F20ALITTLE     NUMBER(1),
  F20SOME        NUMBER(1),
  F20ALOT        NUMBER(1),
  F20CANNOT      NUMBER(1),
  F21NONE        NUMBER(1),
  F21ALITTLE     NUMBER(1),
  F21SOME        NUMBER(1),
  F21ALOT        NUMBER(1),
  F21CANNOT      NUMBER(1),
  F22NONE        NUMBER(1),
  F22ALITTLE     NUMBER(1),
  F22SOME        NUMBER(1),
  F22ALOT        NUMBER(1),
  F22CANNOT      NUMBER(1),
  F23NONE        NUMBER(1),
  F23ALITTLE     NUMBER(1),
  F23SOME        NUMBER(1),
  F23ALOT        NUMBER(1),
  F23CANNOT      NUMBER(1),
  F24NONE        NUMBER(1),
  F24ALITTLE     NUMBER(1),
  F24SOME        NUMBER(1),
  F24ALOT        NUMBER(1),
  F24CANNOT      NUMBER(1),
  F25NONE        NUMBER(1),
  F25ALITTLE     NUMBER(1),
  F25SOME        NUMBER(1),
  F25ALOT        NUMBER(1),
  F25CANNOT      NUMBER(1),
  F26NONE        NUMBER(1),
  F26ALITTLE     NUMBER(1),
  F26SOME        NUMBER(1),
  F26ALOT        NUMBER(1),
  F26CANNOT      NUMBER(1),
  F27NONE        NUMBER(1),
  F27ALITTLE     NUMBER(1),
  F27SOME        NUMBER(1),
  F27ALOT        NUMBER(1),
  F27CANNOT      NUMBER(1),
  F28NONE        NUMBER(1),
  F28ALITTLE     NUMBER(1),
  F28SOME        NUMBER(1),
  F28ALOT        NUMBER(1),
  F28CANNOT      NUMBER(1),
  F29NONE        NUMBER(1),
  F29ALITTLE     NUMBER(1),
  F29SOME        NUMBER(1),
  F29ALOT        NUMBER(1),
  F29CANNOT      NUMBER(1),
  F30NONE        NUMBER(1),
  F30ALITTLE     NUMBER(1),
  F30SOME        NUMBER(1),
  F30ALOT        NUMBER(1),
  F30CANNOT      NUMBER(1),
  F31NONE        NUMBER(1),
  F31ALITTLE     NUMBER(1),
  F31SOME        NUMBER(1),
  F31ALOT        NUMBER(1),
  F31CANNOT      NUMBER(1),
  F32NONE        NUMBER(1),
  F32ALITTLE     NUMBER(1),
  F32SOME        NUMBER(1),
  F32ALOT        NUMBER(1),
  F32CANNOT      NUMBER(1),
  FD7NONE        NUMBER(1),
  FD7ALITTLE     NUMBER(1),
  FD7SOME        NUMBER(1),
  FD7ALOT        NUMBER(1),
  FD7CANNOT      NUMBER(1),
  FD8NONE        NUMBER(1),
  FD8ALITTLE     NUMBER(1),
  FD8SOME        NUMBER(1),
  FD8ALOT        NUMBER(1),
  FD8CANNOT      NUMBER(1),
  FD14NONE       NUMBER(1),
  FD14ALITTLE    NUMBER(1),
  FD14SOME       NUMBER(1),
  FD14ALOT       NUMBER(1),
  FD14CANNOT     NUMBER(1),
  FD15NONE       NUMBER(1),
  FD15ALITTLE    NUMBER(1),
  FD15SOME       NUMBER(1),
  FD15ALOT       NUMBER(1),
  FD15CANNOT     NUMBER(1),
  FD26NONE       NUMBER(1),
  FD26ALITTLE    NUMBER(1),
  FD26SOME       NUMBER(1),
  FD26ALOT       NUMBER(1),
  FD26CANNOT     NUMBER(1),
  FD29NONE       NUMBER(1),
  FD29ALITTLE    NUMBER(1),
  FD29SOME       NUMBER(1),
  FD29ALOT       NUMBER(1),
  FD29CANNOT     NUMBER(1),
  FD30NONE       NUMBER(1),
  FD30ALITTLE    NUMBER(1),
  FD30SOME       NUMBER(1),
  FD30ALOT       NUMBER(1),
  FD30CANNOT     NUMBER(1),
  FD32NONE       NUMBER(1),
  FD32ALITTLE    NUMBER(1),
  FD32SOME       NUMBER(1),
  FD32ALOT       NUMBER(1),
  FD32CANNOT     NUMBER(1)
)
;
alter table FORMLATELIFEFDIFUNCTION
  add primary key (ID);

prompt
prompt Creating table FORMMENTALHEALTH
prompt ===============================
prompt
create table FORMMENTALHEALTH
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6) not null,
  C_LASTVISITED  VARCHAR2(15),
  C_PNAME        VARCHAR2(60),
  C_ADDRESS      VARCHAR2(80),
  C_BIRTHDATE    DATE,
  C_SEX          VARCHAR2(6),
  C_HOMEPHONE    VARCHAR2(10),
  C_REFERRALDATE DATE,
  C_REFERREDBY   VARCHAR2(30),
  R_RPS1         VARCHAR2(2),
  R_RPS2         VARCHAR2(2),
  R_RPS3         VARCHAR2(2),
  R_RPSOTHER     VARCHAR2(30),
  R_RPI1         VARCHAR2(2),
  R_RPI2         VARCHAR2(2),
  R_RPI3         VARCHAR2(2),
  R_RPIOTHER     VARCHAR2(30),
  R_RMPI1        VARCHAR2(2),
  R_RMPI2        VARCHAR2(2),
  R_RMPI3        VARCHAR2(2),
  R_RMPIOTHER    VARCHAR2(30),
  R_IR1          VARCHAR2(2),
  R_IR2          VARCHAR2(2),
  R_IR3          VARCHAR2(2),
  R_IROTHER      VARCHAR2(30),
  R_ARM1         VARCHAR2(2),
  R_ARM2         VARCHAR2(2),
  R_ARM3         VARCHAR2(2),
  R_ARMOTHER     VARCHAR2(30),
  R_REFCOMMENTS  VARCHAR2(4000),
  A_SPECIALIST   VARCHAR2(30),
  A_APS1         VARCHAR2(2),
  A_APS2         VARCHAR2(2),
  A_APS3         VARCHAR2(2),
  A_APSOTHER     VARCHAR2(30),
  A_API1         VARCHAR2(2),
  A_API2         VARCHAR2(2),
  A_API3         VARCHAR2(2),
  A_APIOTHER     VARCHAR2(30),
  A_AMPI1        VARCHAR2(2),
  A_AMPI2        VARCHAR2(2),
  A_AMPI3        VARCHAR2(2),
  A_AMPIOTHER    VARCHAR2(50),
  A_ASSCOMMENTS  VARCHAR2(4000),
  A_TP1          VARCHAR2(2),
  A_TP2          VARCHAR2(2),
  A_TP3          VARCHAR2(2),
  A_TPOTHER      VARCHAR2(30),
  O_SPECIALIST   VARCHAR2(30),
  O_NUMVISITS    VARCHAR2(5),
  O_FORMDATE     DATE,
  O_SP1          VARCHAR2(2),
  O_SP2          VARCHAR2(2),
  O_SP3          VARCHAR2(2),
  O_SPOTHER      VARCHAR2(30),
  O_PE1          VARCHAR2(2),
  O_PE2          VARCHAR2(2),
  O_PE3          VARCHAR2(2),
  O_PEOTHER      VARCHAR2(30),
  O_D1           VARCHAR2(2),
  O_D2           VARCHAR2(2),
  O_D3           VARCHAR2(2),
  O_DOTHER       VARCHAR2(30),
  O_PNS1         VARCHAR2(2),
  O_PNS2         VARCHAR2(2),
  O_PNS3         VARCHAR2(2),
  O_PNSOTHER     VARCHAR2(30),
  O_OUTCOMMENTS  VARCHAR2(4000),
  A_FORMDATE     DATE
)
;
alter table FORMMENTALHEALTH
  add primary key (ID);

prompt
prompt Creating table FORMMMSE
prompt =======================
prompt
create table FORMMMSE
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10),
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6) not null,
  PNAME          VARCHAR2(50),
  AGE            VARCHAR2(3),
  SEX            VARCHAR2(1),
  FORMDATE       DATE,
  DIAGNOSIS      VARCHAR2(4000),
  MEDS           VARCHAR2(4000),
  O_DATE         VARCHAR2(1),
  O_PLACE        VARCHAR2(1),
  R_OBJECTS      VARCHAR2(1),
  A_SERIAL       VARCHAR2(1),
  RE_NAME        VARCHAR2(1),
  L_NAME         VARCHAR2(1),
  L_REPEAT       VARCHAR2(1),
  L_FOLLOW       VARCHAR2(1),
  L_READ         VARCHAR2(1),
  L_WRITE        VARCHAR2(1),
  L_COPY         VARCHAR2(1),
  TOTAL          VARCHAR2(2),
  LC_ALERT       NUMBER(1),
  LC_DROWSY      NUMBER(1),
  LC_STUPOR      NUMBER(1),
  LC_COMA        NUMBER(1),
  I_DEMENTIA     NUMBER(1),
  I_DEPRESSION   NUMBER(1),
  I_NORMAL       NUMBER(1)
)
;
alter table FORMMMSE
  add primary key (ID);

prompt
prompt Creating table FORMONAR
prompt =======================
prompt
create table FORMONAR
(
  ID                    NUMBER(10) not null,
  DEMOGRAPHIC_NO        NUMBER(10) default '0' not null,
  PROVIDER_NO           NUMBER(10),
  FORMCREATED           DATE,
  FORMEDITED            TIMESTAMP(6),
  C_LASTNAME            VARCHAR2(80),
  C_FIRSTNAME           VARCHAR2(80),
  C_ADDRESS             VARCHAR2(80),
  C_APT                 VARCHAR2(20),
  C_CITY                VARCHAR2(80),
  C_PROVINCE            VARCHAR2(80),
  C_POSTAL              VARCHAR2(10),
  C_PARTNERLASTNAME     VARCHAR2(80),
  C_PARTNERFIRSTNAME    VARCHAR2(80),
  PG1_HOMEPHONE         VARCHAR2(20),
  PG1_WORKPHONE         VARCHAR2(20),
  PG1_LANGUAGE          VARCHAR2(25),
  PG1_PARTNEROCCUPATION VARCHAR2(25),
  PG1_PARTNEREDULEVEL   VARCHAR2(25),
  PG1_PARTNERAGE        VARCHAR2(5),
  PG1_DATEOFBIRTH       DATE,
  PG1_AGE               VARCHAR2(10),
  PG1_OCCUPATION        VARCHAR2(25),
  PG1_EDULEVEL          VARCHAR2(25),
  PG1_ETHNICBG          VARCHAR2(200),
  C_HIN                 VARCHAR2(20),
  C_FILENO              VARCHAR2(20),
  PG1_MSSINGLE          NUMBER(1),
  PG1_MSCOMMONLAW       NUMBER(1),
  PG1_MSMARRIED         NUMBER(1),
  PG1_BAOBS             NUMBER(1),
  PG1_BAFP              NUMBER(1),
  PG1_BAMIDWIFE         NUMBER(1),
  C_BA                  VARCHAR2(25),
  PG1_NCPED             NUMBER(1),
  PG1_NCFP              NUMBER(1),
  PG1_NCMIDWIFE         NUMBER(1),
  C_NC                  VARCHAR2(25),
  C_FAMPHYS             VARCHAR2(80),
  C_ALLERGIES           VARCHAR2(4000),
  C_MEDS                VARCHAR2(4000),
  PG1_MENLMP            DATE,
  PG1_PSCERTY           NUMBER(1),
  PG1_PSCERTN           NUMBER(1),
  PG1_MENCYCLE          VARCHAR2(7),
  PG1_MENREG            NUMBER(1),
  PG1_MENREGN           NUMBER(1),
  PG1_CONTRACEP         VARCHAR2(25),
  PG1_LASTUSED          DATE,
  PG1_MENEDB            DATE,
  C_FINALEDB            DATE,
  PG1_EDBBYDATE         NUMBER(1),
  PG1_EDBBYT1           NUMBER(1),
  PG1_EDBBYT2           NUMBER(1),
  PG1_EDBBYART          NUMBER(1),
  C_GRAVIDA             VARCHAR2(3),
  C_TERM                VARCHAR2(3),
  C_PREM                VARCHAR2(3),
  C_ABORT               VARCHAR2(3),
  C_LIVING              VARCHAR2(3),
  PG1_YEAR1             VARCHAR2(10),
  PG1_SEX1              VARCHAR2(1),
  PG1_OH_GEST1          VARCHAR2(5),
  PG1_WEIGHT1           VARCHAR2(6),
  PG1_LENGTH1           VARCHAR2(6),
  PG1_PLACE1            VARCHAR2(20),
  PG1_SVB1              NUMBER(1),
  PG1_CS1               NUMBER(1),
  PG1_ASS1              NUMBER(1),
  PG1_OH_COMMENTS1      VARCHAR2(80),
  PG1_YEAR2             VARCHAR2(10),
  PG1_SEX2              VARCHAR2(1),
  PG1_OH_GEST2          VARCHAR2(5),
  PG1_WEIGHT2           VARCHAR2(6),
  PG1_LENGTH2           VARCHAR2(6),
  PG1_PLACE2            VARCHAR2(20),
  PG1_SVB2              NUMBER(1),
  PG1_CS2               NUMBER(1),
  PG1_ASS2              NUMBER(1),
  PG1_OH_COMMENTS2      VARCHAR2(80),
  PG1_YEAR3             VARCHAR2(10),
  PG1_SEX3              VARCHAR2(1),
  PG1_OH_GEST3          VARCHAR2(5),
  PG1_WEIGHT3           VARCHAR2(6),
  PG1_LENGTH3           VARCHAR2(6),
  PG1_PLACE3            VARCHAR2(20),
  PG1_SVB3              NUMBER(1),
  PG1_CS3               NUMBER(1),
  PG1_ASS3              NUMBER(1),
  PG1_OH_COMMENTS3      VARCHAR2(80),
  PG1_YEAR4             VARCHAR2(10),
  PG1_SEX4              VARCHAR2(1),
  PG1_OH_GEST4          VARCHAR2(5),
  PG1_WEIGHT4           VARCHAR2(6),
  PG1_LENGTH4           VARCHAR2(6),
  PG1_PLACE4            VARCHAR2(20),
  PG1_SVB4              NUMBER(1),
  PG1_CS4               NUMBER(1),
  PG1_ASS4              NUMBER(1),
  PG1_OH_COMMENTS4      VARCHAR2(80),
  PG1_YEAR5             VARCHAR2(10),
  PG1_SEX5              VARCHAR2(1),
  PG1_OH_GEST5          VARCHAR2(5),
  PG1_WEIGHT5           VARCHAR2(6),
  PG1_LENGTH5           VARCHAR2(6),
  PG1_PLACE5            VARCHAR2(20),
  PG1_SVB5              NUMBER(1),
  PG1_CS5               NUMBER(1),
  PG1_ASS5              NUMBER(1),
  PG1_OH_COMMENTS5      VARCHAR2(80),
  PG1_YEAR6             VARCHAR2(10),
  PG1_SEX6              VARCHAR2(1),
  PG1_OH_GEST6          VARCHAR2(5),
  PG1_WEIGHT6           VARCHAR2(6),
  PG1_LENGTH6           VARCHAR2(6),
  PG1_PLACE6            VARCHAR2(20),
  PG1_SVB6              NUMBER(1),
  PG1_CS6               NUMBER(1),
  PG1_ASS6              NUMBER(1),
  PG1_OH_COMMENTS6      VARCHAR2(80),
  PG1_CP1               NUMBER(1),
  PG1_CP1N              NUMBER(1),
  PG1_CP2               NUMBER(1),
  PG1_CP2N              NUMBER(1),
  PG1_BOX3              VARCHAR2(3),
  PG1_CP3               NUMBER(1),
  PG1_CP3N              NUMBER(1),
  PG1_CP4               NUMBER(1),
  PG1_CP4N              NUMBER(1),
  PG1_CP8               NUMBER(1),
  PG1_CP8N              NUMBER(1),
  PG1_NADIETRES         NUMBER(1),
  PG1_NADIETBAL         NUMBER(1),
  PG1_NAMILK            NUMBER(1),
  PG1_NAMILKN           NUMBER(1),
  PG1_NAFOLIC           NUMBER(1),
  PG1_NAFOLICN          NUMBER(1),
  PG1_YES9              NUMBER(1),
  PG1_NO9               NUMBER(1),
  PG1_YES10             NUMBER(1),
  PG1_NO10              NUMBER(1),
  PG1_YES12             NUMBER(1),
  PG1_NO12              NUMBER(1),
  PG1_YES13             NUMBER(1),
  PG1_NO13              NUMBER(1),
  PG1_YES14             NUMBER(1),
  PG1_NO14              NUMBER(1),
  PG1_YES17             NUMBER(1),
  PG1_NO17              NUMBER(1),
  PG1_YES22             NUMBER(1),
  PG1_NO22              NUMBER(1),
  PG1_YES20             NUMBER(1),
  PG1_NO20              NUMBER(1),
  PG1_BLOODTRANY        NUMBER(1),
  PG1_BLOODTRANN        NUMBER(1),
  PG1_YES21             NUMBER(1),
  PG1_NO21              NUMBER(1),
  PG1_YES24             NUMBER(1),
  PG1_NO24              NUMBER(1),
  PG1_YES15             NUMBER(1),
  PG1_NO15              NUMBER(1),
  PG1_BOX25             VARCHAR2(25),
  PG1_YES25             NUMBER(1),
  PG1_NO25              NUMBER(1),
  PG1_YES27             NUMBER(1),
  PG1_NO27              NUMBER(1),
  PG1_YES31             NUMBER(1),
  PG1_NO31              NUMBER(1),
  PG1_YES32             NUMBER(1),
  PG1_NO32              NUMBER(1),
  PG1_YES34             NUMBER(1),
  PG1_NO34              NUMBER(1),
  PG1_YES35             NUMBER(1),
  PG1_NO35              NUMBER(1),
  PG1_IDT40             NUMBER(1),
  PG1_IDT40N            NUMBER(1),
  PG1_IDT38             NUMBER(1),
  PG1_IDT38N            NUMBER(1),
  PG1_IDT42             NUMBER(1),
  PG1_IDT42N            NUMBER(1),
  PG1_INFECTDISOTHER    VARCHAR2(20),
  PG1_INFECTDISOTHERY   NUMBER(1),
  PG1_INFECTDISOTHERN   NUMBER(1),
  PG1_PDT43             NUMBER(1),
  PG1_PDT43N            NUMBER(1),
  PG1_PDT44             NUMBER(1),
  PG1_PDT44N            NUMBER(1),
  PG1_PDT45             NUMBER(1),
  PG1_PDT45N            NUMBER(1),
  PG1_PDT46             NUMBER(1),
  PG1_PDT46N            NUMBER(1),
  PG1_PDT47             NUMBER(1),
  PG1_PDT47N            NUMBER(1),
  PG1_PDT48             NUMBER(1),
  PG1_PDT48N            NUMBER(1),
  PG1_RELICULTY         NUMBER(1),
  PG1_RELICULTN         NUMBER(1),
  PG1_FHRISKY           NUMBER(1),
  PG1_FHRISKN           NUMBER(1),
  PG1_HT                VARCHAR2(6),
  PG1_WT                VARCHAR2(6),
  C_BMI                 VARCHAR2(6),
  PG1_BP                VARCHAR2(10),
  PG1_THYROID           NUMBER(1),
  PG1_THYROIDA          NUMBER(1),
  PG1_CHEST             NUMBER(1),
  PG1_CHESTA            NUMBER(1),
  PG1_BREASTS           NUMBER(1),
  PG1_BREASTSA          NUMBER(1),
  PG1_CARDIO            NUMBER(1),
  PG1_CARDIOA           NUMBER(1),
  PG1_ABDOMEN           NUMBER(1),
  PG1_ABDOMENA          NUMBER(1),
  PG1_VARI              NUMBER(1),
  PG1_VARIA             NUMBER(1),
  PG1_EXTGEN            NUMBER(1),
  PG1_EXTGENA           NUMBER(1),
  PG1_CERVIX            NUMBER(1),
  PG1_CERVIXA           NUMBER(1),
  PG1_UTERUS            NUMBER(1),
  PG1_UTERUSA           NUMBER(1),
  PG1_UTERUSBOX         VARCHAR2(3),
  PG1_ADNEXA            NUMBER(1),
  PG1_ADNEXAA           NUMBER(1),
  PG1_PEXOTHER          VARCHAR2(20),
  PG1_PEXOTHERN         NUMBER(1),
  PG1_PEXOTHERA         NUMBER(1),
  PG1_LABHB             VARCHAR2(20),
  PG1_LABHIV            VARCHAR2(20),
  PG1_LABMCV            VARCHAR2(20),
  PG1_LABHIVCOUNSEL     NUMBER(1),
  PG1_LABABO            VARCHAR2(20),
  PG1_LABLASTPAPDATE    VARCHAR2(10),
  PG1_LABLASTPAP        VARCHAR2(20),
  PG1_LABRH             VARCHAR2(20),
  PG1_LABANTISCR        VARCHAR2(20),
  PG1_LABGC             VARCHAR2(20),
  PG1_LABRUBELLA        VARCHAR2(20),
  PG1_LABURINE          VARCHAR2(20),
  PG1_LABHBSAG          VARCHAR2(20),
  PG1_LABVDRL           VARCHAR2(20),
  PG1_LABSICKLE         VARCHAR2(20),
  PG1_GENETICA          VARCHAR2(20),
  PG1_GENETICB          VARCHAR2(20),
  PG1_GENETICC          VARCHAR2(20),
  PG1_GENETICD          NUMBER(1),
  PG1_COMMENTSAR1       VARCHAR2(4000),
  PG1_SIGNATURE         VARCHAR2(50),
  PG1_FORMDATE          DATE,
  PG1_SIGNATURE2        VARCHAR2(50),
  PG1_FORMDATE2         DATE,
  C_RISKFACTORS1        VARCHAR2(50),
  C_PLANMANAGE1         VARCHAR2(100),
  C_RISKFACTORS2        VARCHAR2(50),
  C_PLANMANAGE2         VARCHAR2(100),
  C_RISKFACTORS3        VARCHAR2(50),
  C_PLANMANAGE3         VARCHAR2(100),
  C_RISKFACTORS4        VARCHAR2(50),
  C_PLANMANAGE4         VARCHAR2(100),
  C_RISKFACTORS5        VARCHAR2(50),
  C_PLANMANAGE5         VARCHAR2(100),
  C_RISKFACTORS6        VARCHAR2(50),
  C_PLANMANAGE6         VARCHAR2(100),
  C_RISKFACTORS7        VARCHAR2(50),
  C_PLANMANAGE7         VARCHAR2(100),
  AR2_RHNEG             NUMBER(1),
  AR2_RHIG              VARCHAR2(10),
  AR2_RUBELLA           NUMBER(1),
  AR2_HEPBIG            NUMBER(1),
  AR2_HEPBVAC           NUMBER(1),
  PG2_DATE1             DATE,
  PG2_GEST1             VARCHAR2(6),
  PG2_WT1               VARCHAR2(6),
  PG2_BP1               VARCHAR2(8),
  PG2_URINEPR1          VARCHAR2(3),
  PG2_URINEGL1          VARCHAR2(3),
  PG2_HT1               VARCHAR2(6),
  PG2_PRESN1            VARCHAR2(6),
  PG2_FHR1              VARCHAR2(6),
  PG2_COMMENTS1         VARCHAR2(255),
  PG2_DATE2             DATE,
  PG2_GEST2             VARCHAR2(6),
  PG2_HT2               VARCHAR2(6),
  PG2_WT2               VARCHAR2(6),
  PG2_PRESN2            VARCHAR2(6),
  PG2_FHR2              VARCHAR2(6),
  PG2_URINEPR2          VARCHAR2(3),
  PG2_URINEGL2          VARCHAR2(3),
  PG2_BP2               VARCHAR2(8),
  PG2_COMMENTS2         VARCHAR2(255),
  PG2_DATE3             DATE,
  PG2_GEST3             VARCHAR2(6),
  PG2_HT3               VARCHAR2(6),
  PG2_WT3               VARCHAR2(6),
  PG2_PRESN3            VARCHAR2(6),
  PG2_FHR3              VARCHAR2(6),
  PG2_URINEPR3          VARCHAR2(3),
  PG2_URINEGL3          VARCHAR2(3),
  PG2_BP3               VARCHAR2(8),
  PG2_COMMENTS3         VARCHAR2(255),
  PG2_DATE4             DATE,
  PG2_GEST4             VARCHAR2(6),
  PG2_HT4               VARCHAR2(6),
  PG2_WT4               VARCHAR2(6),
  PG2_PRESN4            VARCHAR2(6),
  PG2_FHR4              VARCHAR2(6),
  PG2_URINEPR4          VARCHAR2(3),
  PG2_URINEGL4          VARCHAR2(3),
  PG2_BP4               VARCHAR2(8),
  PG2_COMMENTS4         VARCHAR2(255),
  PG2_DATE5             DATE,
  PG2_GEST5             VARCHAR2(6),
  PG2_HT5               VARCHAR2(6),
  PG2_WT5               VARCHAR2(6),
  PG2_PRESN5            VARCHAR2(6),
  PG2_FHR5              VARCHAR2(6),
  PG2_URINEPR5          VARCHAR2(3),
  PG2_URINEGL5          VARCHAR2(3),
  PG2_BP5               VARCHAR2(8),
  PG2_COMMENTS5         VARCHAR2(255),
  PG2_DATE6             DATE,
  PG2_GEST6             VARCHAR2(6),
  PG2_HT6               VARCHAR2(6),
  PG2_WT6               VARCHAR2(6),
  PG2_PRESN6            VARCHAR2(6),
  PG2_FHR6              VARCHAR2(6),
  PG2_URINEPR6          VARCHAR2(3),
  PG2_URINEGL6          VARCHAR2(3),
  PG2_BP6               VARCHAR2(8),
  PG2_COMMENTS6         VARCHAR2(255),
  PG2_DATE7             DATE,
  PG2_GEST7             VARCHAR2(6),
  PG2_HT7               VARCHAR2(6),
  PG2_WT7               VARCHAR2(6),
  PG2_PRESN7            VARCHAR2(6),
  PG2_FHR7              VARCHAR2(6),
  PG2_URINEPR7          VARCHAR2(3),
  PG2_URINEGL7          VARCHAR2(3),
  PG2_BP7               VARCHAR2(8),
  PG2_COMMENTS7         VARCHAR2(255),
  PG2_DATE8             DATE,
  PG2_GEST8             VARCHAR2(6),
  PG2_HT8               VARCHAR2(6),
  PG2_WT8               VARCHAR2(6),
  PG2_PRESN8            VARCHAR2(6),
  PG2_FHR8              VARCHAR2(6),
  PG2_URINEPR8          VARCHAR2(3),
  PG2_URINEGL8          VARCHAR2(3),
  PG2_BP8               VARCHAR2(8),
  PG2_COMMENTS8         VARCHAR2(255),
  PG2_DATE9             DATE,
  PG2_GEST9             VARCHAR2(6),
  PG2_HT9               VARCHAR2(6),
  PG2_WT9               VARCHAR2(6),
  PG2_PRESN9            VARCHAR2(6),
  PG2_FHR9              VARCHAR2(6),
  PG2_URINEPR9          VARCHAR2(3),
  PG2_URINEGL9          VARCHAR2(3),
  PG2_BP9               VARCHAR2(8),
  PG2_COMMENTS9         VARCHAR2(255),
  PG2_DATE10            DATE,
  PG2_GEST10            VARCHAR2(6),
  PG2_HT10              VARCHAR2(6),
  PG2_WT10              VARCHAR2(6),
  PG2_PRESN10           VARCHAR2(6),
  PG2_FHR10             VARCHAR2(6),
  PG2_URINEPR10         VARCHAR2(3),
  PG2_URINEGL10         VARCHAR2(3),
  PG2_BP10              VARCHAR2(8),
  PG2_COMMENTS10        VARCHAR2(255),
  PG2_DATE11            DATE,
  PG2_GEST11            VARCHAR2(6),
  PG2_HT11              VARCHAR2(6),
  PG2_WT11              VARCHAR2(6),
  PG2_PRESN11           VARCHAR2(6),
  PG2_FHR11             VARCHAR2(6),
  PG2_URINEPR11         VARCHAR2(3),
  PG2_URINEGL11         VARCHAR2(3),
  PG2_BP11              VARCHAR2(8),
  PG2_COMMENTS11        VARCHAR2(255),
  PG2_DATE12            DATE,
  PG2_GEST12            VARCHAR2(6),
  PG2_HT12              VARCHAR2(6),
  PG2_WT12              VARCHAR2(6),
  PG2_PRESN12           VARCHAR2(6),
  PG2_FHR12             VARCHAR2(6),
  PG2_URINEPR12         VARCHAR2(3),
  PG2_URINEGL12         VARCHAR2(3),
  PG2_BP12              VARCHAR2(8),
  PG2_COMMENTS12        VARCHAR2(255),
  PG2_DATE13            DATE,
  PG2_GEST13            VARCHAR2(6),
  PG2_HT13              VARCHAR2(6),
  PG2_WT13              VARCHAR2(6),
  PG2_PRESN13           VARCHAR2(6),
  PG2_FHR13             VARCHAR2(6),
  PG2_URINEPR13         VARCHAR2(3),
  PG2_URINEGL13         VARCHAR2(3),
  PG2_BP13              VARCHAR2(8),
  PG2_COMMENTS13        VARCHAR2(255),
  PG2_DATE14            DATE,
  PG2_GEST14            VARCHAR2(6),
  PG2_HT14              VARCHAR2(6),
  PG2_WT14              VARCHAR2(6),
  PG2_PRESN14           VARCHAR2(6),
  PG2_FHR14             VARCHAR2(6),
  PG2_URINEPR14         VARCHAR2(3),
  PG2_URINEGL14         VARCHAR2(3),
  PG2_BP14              VARCHAR2(8),
  PG2_COMMENTS14        VARCHAR2(255),
  PG2_DATE15            DATE,
  PG2_GEST15            VARCHAR2(6),
  PG2_HT15              VARCHAR2(6),
  PG2_WT15              VARCHAR2(6),
  PG2_PRESN15           VARCHAR2(6),
  PG2_FHR15             VARCHAR2(6),
  PG2_URINEPR15         VARCHAR2(3),
  PG2_URINEGL15         VARCHAR2(3),
  PG2_BP15              VARCHAR2(8),
  PG2_COMMENTS15        VARCHAR2(255),
  PG2_DATE16            DATE,
  PG2_GEST16            VARCHAR2(6),
  PG2_HT16              VARCHAR2(6),
  PG2_WT16              VARCHAR2(6),
  PG2_PRESN16           VARCHAR2(6),
  PG2_FHR16             VARCHAR2(6),
  PG2_URINEPR16         VARCHAR2(3),
  PG2_URINEGL16         VARCHAR2(3),
  PG2_BP16              VARCHAR2(8),
  PG2_COMMENTS16        VARCHAR2(255),
  PG2_DATE17            DATE,
  PG2_GEST17            VARCHAR2(6),
  PG2_HT17              VARCHAR2(6),
  PG2_WT17              VARCHAR2(6),
  PG2_PRESN17           VARCHAR2(6),
  PG2_FHR17             VARCHAR2(6),
  PG2_URINEPR17         VARCHAR2(3),
  PG2_URINEGL17         VARCHAR2(3),
  PG2_BP17              VARCHAR2(8),
  PG2_COMMENTS17        VARCHAR2(255),
  PG2_DATE18            DATE,
  PG2_GEST18            VARCHAR2(6),
  PG2_HT18              VARCHAR2(6),
  PG2_WT18              VARCHAR2(6),
  PG2_PRESN18           VARCHAR2(6),
  PG2_FHR18             VARCHAR2(6),
  PG2_URINEPR18         VARCHAR2(3),
  PG2_URINEGL18         VARCHAR2(3),
  PG2_BP18              VARCHAR2(8),
  PG2_COMMENTS18        VARCHAR2(255),
  PG3_DATE19            DATE,
  PG3_GEST19            VARCHAR2(6),
  PG3_HT19              VARCHAR2(6),
  PG3_WT19              VARCHAR2(6),
  PG3_PRESN19           VARCHAR2(6),
  PG3_FHR19             VARCHAR2(6),
  PG3_URINEPR19         VARCHAR2(3),
  PG3_URINEGL19         VARCHAR2(3),
  PG3_BP19              VARCHAR2(8),
  PG3_COMMENTS19        VARCHAR2(255),
  PG3_DATE20            DATE,
  PG3_GEST20            VARCHAR2(6),
  PG3_HT20              VARCHAR2(6),
  PG3_WT20              VARCHAR2(6),
  PG3_PRESN20           VARCHAR2(6),
  PG3_FHR20             VARCHAR2(6),
  PG3_URINEPR20         VARCHAR2(3),
  PG3_URINEGL20         VARCHAR2(3),
  PG3_BP20              VARCHAR2(8),
  PG3_COMMENTS20        VARCHAR2(255),
  PG3_DATE21            DATE,
  PG3_GEST21            VARCHAR2(6),
  PG3_HT21              VARCHAR2(6),
  PG3_WT21              VARCHAR2(6),
  PG3_PRESN21           VARCHAR2(6),
  PG3_FHR21             VARCHAR2(6),
  PG3_URINEPR21         VARCHAR2(3),
  PG3_URINEGL21         VARCHAR2(3),
  PG3_BP21              VARCHAR2(8),
  PG3_COMMENTS21        VARCHAR2(255),
  PG3_DATE22            DATE,
  PG3_GEST22            VARCHAR2(6),
  PG3_HT22              VARCHAR2(6),
  PG3_WT22              VARCHAR2(6),
  PG3_PRESN22           VARCHAR2(6),
  PG3_FHR22             VARCHAR2(6),
  PG3_URINEPR22         VARCHAR2(3),
  PG3_URINEGL22         VARCHAR2(3),
  PG3_BP22              VARCHAR2(8),
  PG3_COMMENTS22        VARCHAR2(255),
  PG3_DATE23            DATE,
  PG3_GEST23            VARCHAR2(6),
  PG3_HT23              VARCHAR2(6),
  PG3_WT23              VARCHAR2(6),
  PG3_PRESN23           VARCHAR2(6),
  PG3_FHR23             VARCHAR2(6),
  PG3_URINEPR23         VARCHAR2(3),
  PG3_URINEGL23         VARCHAR2(3),
  PG3_BP23              VARCHAR2(8),
  PG3_COMMENTS23        VARCHAR2(255),
  PG3_DATE24            DATE,
  PG3_GEST24            VARCHAR2(6),
  PG3_HT24              VARCHAR2(6),
  PG3_WT24              VARCHAR2(6),
  PG3_PRESN24           VARCHAR2(6),
  PG3_FHR24             VARCHAR2(6),
  PG3_URINEPR24         VARCHAR2(3),
  PG3_URINEGL24         VARCHAR2(3),
  PG3_BP24              VARCHAR2(8),
  PG3_COMMENTS24        VARCHAR2(255),
  PG3_DATE25            DATE,
  PG3_GEST25            VARCHAR2(6),
  PG3_HT25              VARCHAR2(6),
  PG3_WT25              VARCHAR2(6),
  PG3_PRESN25           VARCHAR2(6),
  PG3_FHR25             VARCHAR2(6),
  PG3_URINEPR25         VARCHAR2(3),
  PG3_URINEGL25         VARCHAR2(3),
  PG3_BP25              VARCHAR2(8),
  PG3_COMMENTS25        VARCHAR2(255),
  PG3_DATE26            DATE,
  PG3_GEST26            VARCHAR2(6),
  PG3_HT26              VARCHAR2(6),
  PG3_WT26              VARCHAR2(6),
  PG3_PRESN26           VARCHAR2(6),
  PG3_FHR26             VARCHAR2(6),
  PG3_URINEPR26         VARCHAR2(3),
  PG3_URINEGL26         VARCHAR2(3),
  PG3_BP26              VARCHAR2(8),
  PG3_COMMENTS26        VARCHAR2(255),
  PG3_DATE27            DATE,
  PG3_GEST27            VARCHAR2(6),
  PG3_HT27              VARCHAR2(6),
  PG3_WT27              VARCHAR2(6),
  PG3_PRESN27           VARCHAR2(6),
  PG3_FHR27             VARCHAR2(6),
  PG3_URINEPR27         VARCHAR2(3),
  PG3_URINEGL27         VARCHAR2(3),
  PG3_BP27              VARCHAR2(8),
  PG3_COMMENTS27        VARCHAR2(255),
  PG3_DATE28            DATE,
  PG3_GEST28            VARCHAR2(6),
  PG3_HT28              VARCHAR2(6),
  PG3_WT28              VARCHAR2(6),
  PG3_PRESN28           VARCHAR2(6),
  PG3_FHR28             VARCHAR2(6),
  PG3_URINEPR28         VARCHAR2(3),
  PG3_URINEGL28         VARCHAR2(3),
  PG3_BP28              VARCHAR2(8),
  PG3_COMMENTS28        VARCHAR2(255),
  PG3_DATE29            DATE,
  PG3_GEST29            VARCHAR2(6),
  PG3_HT29              VARCHAR2(6),
  PG3_WT29              VARCHAR2(6),
  PG3_PRESN29           VARCHAR2(6),
  PG3_FHR29             VARCHAR2(6),
  PG3_URINEPR29         VARCHAR2(3),
  PG3_URINEGL29         VARCHAR2(3),
  PG3_BP29              VARCHAR2(8),
  PG3_COMMENTS29        VARCHAR2(255),
  PG3_DATE30            DATE,
  PG3_GEST30            VARCHAR2(6),
  PG3_HT30              VARCHAR2(6),
  PG3_WT30              VARCHAR2(6),
  PG3_PRESN30           VARCHAR2(6),
  PG3_FHR30             VARCHAR2(6),
  PG3_URINEPR30         VARCHAR2(3),
  PG3_URINEGL30         VARCHAR2(3),
  PG3_BP30              VARCHAR2(8),
  PG3_COMMENTS30        VARCHAR2(255),
  PG3_DATE31            DATE,
  PG3_GEST31            VARCHAR2(6),
  PG3_HT31              VARCHAR2(6),
  PG3_WT31              VARCHAR2(6),
  PG3_PRESN31           VARCHAR2(6),
  PG3_FHR31             VARCHAR2(6),
  PG3_URINEPR31         VARCHAR2(3),
  PG3_URINEGL31         VARCHAR2(3),
  PG3_BP31              VARCHAR2(8),
  PG3_COMMENTS31        VARCHAR2(255),
  PG3_DATE32            DATE,
  PG3_GEST32            VARCHAR2(6),
  PG3_HT32              VARCHAR2(6),
  PG3_WT32              VARCHAR2(6),
  PG3_PRESN32           VARCHAR2(6),
  PG3_FHR32             VARCHAR2(6),
  PG3_URINEPR32         VARCHAR2(3),
  PG3_URINEGL32         VARCHAR2(3),
  PG3_BP32              VARCHAR2(8),
  PG3_COMMENTS32        VARCHAR2(255),
  PG3_DATE33            DATE,
  PG3_GEST33            VARCHAR2(6),
  PG3_HT33              VARCHAR2(6),
  PG3_WT33              VARCHAR2(6),
  PG3_PRESN33           VARCHAR2(6),
  PG3_FHR33             VARCHAR2(6),
  PG3_URINEPR33         VARCHAR2(3),
  PG3_URINEGL33         VARCHAR2(3),
  PG3_BP33              VARCHAR2(8),
  PG3_COMMENTS33        VARCHAR2(255),
  PG3_DATE34            DATE,
  PG3_GEST34            VARCHAR2(6),
  PG3_HT34              VARCHAR2(6),
  PG3_WT34              VARCHAR2(6),
  PG3_PRESN34           VARCHAR2(6),
  PG3_FHR34             VARCHAR2(6),
  PG3_URINEPR34         VARCHAR2(3),
  PG3_URINEGL34         VARCHAR2(3),
  PG3_BP34              VARCHAR2(8),
  PG3_COMMENTS34        VARCHAR2(255),
  PG3_DATE35            DATE,
  PG3_GEST35            VARCHAR2(6),
  PG3_HT35              VARCHAR2(6),
  PG3_WT35              VARCHAR2(6),
  PG3_PRESN35           VARCHAR2(6),
  PG3_FHR35             VARCHAR2(6),
  PG3_URINEPR35         VARCHAR2(3),
  PG3_URINEGL35         VARCHAR2(3),
  PG3_BP35              VARCHAR2(8),
  PG3_COMMENTS35        VARCHAR2(255),
  PG3_DATE36            DATE,
  PG3_GEST36            VARCHAR2(6),
  PG3_HT36              VARCHAR2(6),
  PG3_WT36              VARCHAR2(6),
  PG3_PRESN36           VARCHAR2(6),
  PG3_FHR36             VARCHAR2(6),
  PG3_URINEPR36         VARCHAR2(3),
  PG3_URINEGL36         VARCHAR2(3),
  PG3_BP36              VARCHAR2(8),
  PG3_COMMENTS36        VARCHAR2(255),
  PG4_DATE37            DATE,
  PG4_GEST37            VARCHAR2(6),
  PG4_HT37              VARCHAR2(6),
  PG4_WT37              VARCHAR2(6),
  PG4_PRESN37           VARCHAR2(6),
  PG4_FHR37             VARCHAR2(6),
  PG4_URINEPR37         VARCHAR2(3),
  PG4_URINEGL37         VARCHAR2(3),
  PG4_BP37              VARCHAR2(8),
  PG4_COMMENTS37        VARCHAR2(255),
  PG4_DATE38            DATE,
  PG4_GEST38            VARCHAR2(6),
  PG4_HT38              VARCHAR2(6),
  PG4_WT38              VARCHAR2(6),
  PG4_PRESN38           VARCHAR2(6),
  PG4_FHR38             VARCHAR2(6),
  PG4_URINEPR38         VARCHAR2(3),
  PG4_URINEGL38         VARCHAR2(3),
  PG4_BP38              VARCHAR2(8),
  PG4_COMMENTS38        VARCHAR2(255),
  PG4_DATE39            DATE,
  PG4_GEST39            VARCHAR2(6),
  PG4_HT39              VARCHAR2(6),
  PG4_WT39              VARCHAR2(6),
  PG4_PRESN39           VARCHAR2(6),
  PG4_FHR39             VARCHAR2(6),
  PG4_URINEPR39         VARCHAR2(3),
  PG4_URINEGL39         VARCHAR2(3),
  PG4_BP39              VARCHAR2(8),
  PG4_COMMENTS39        VARCHAR2(255),
  PG4_DATE40            DATE,
  PG4_GEST40            VARCHAR2(6),
  PG4_HT40              VARCHAR2(6),
  PG4_WT40              VARCHAR2(6),
  PG4_PRESN40           VARCHAR2(6),
  PG4_FHR40             VARCHAR2(6),
  PG4_URINEPR40         VARCHAR2(3),
  PG4_URINEGL40         VARCHAR2(3),
  PG4_BP40              VARCHAR2(8),
  PG4_COMMENTS40        VARCHAR2(255),
  PG4_DATE41            DATE,
  PG4_GEST41            VARCHAR2(6),
  PG4_HT41              VARCHAR2(6),
  PG4_WT41              VARCHAR2(6),
  PG4_PRESN41           VARCHAR2(6),
  PG4_FHR41             VARCHAR2(6),
  PG4_URINEPR41         VARCHAR2(3),
  PG4_URINEGL41         VARCHAR2(3),
  PG4_BP41              VARCHAR2(8),
  PG4_COMMENTS41        VARCHAR2(255),
  PG4_DATE42            DATE,
  PG4_GEST42            VARCHAR2(6),
  PG4_HT42              VARCHAR2(6),
  PG4_WT42              VARCHAR2(6),
  PG4_PRESN42           VARCHAR2(6),
  PG4_FHR42             VARCHAR2(6),
  PG4_URINEPR42         VARCHAR2(3),
  PG4_URINEGL42         VARCHAR2(3),
  PG4_BP42              VARCHAR2(8),
  PG4_COMMENTS42        VARCHAR2(255),
  PG4_DATE43            DATE,
  PG4_GEST43            VARCHAR2(6),
  PG4_HT43              VARCHAR2(6),
  PG4_WT43              VARCHAR2(6),
  PG4_PRESN43           VARCHAR2(6),
  PG4_FHR43             VARCHAR2(6),
  PG4_URINEPR43         VARCHAR2(3),
  PG4_URINEGL43         VARCHAR2(3),
  PG4_BP43              VARCHAR2(8),
  PG4_COMMENTS43        VARCHAR2(255),
  PG4_DATE44            DATE,
  PG4_GEST44            VARCHAR2(6),
  PG4_HT44              VARCHAR2(6),
  PG4_WT44              VARCHAR2(6),
  PG4_PRESN44           VARCHAR2(6),
  PG4_FHR44             VARCHAR2(6),
  PG4_URINEPR44         VARCHAR2(3),
  PG4_URINEGL44         VARCHAR2(3),
  PG4_BP44              VARCHAR2(8),
  PG4_COMMENTS44        VARCHAR2(255),
  PG4_DATE45            DATE,
  PG4_GEST45            VARCHAR2(6),
  PG4_HT45              VARCHAR2(6),
  PG4_WT45              VARCHAR2(6),
  PG4_PRESN45           VARCHAR2(6),
  PG4_FHR45             VARCHAR2(6),
  PG4_URINEPR45         VARCHAR2(3),
  PG4_URINEGL45         VARCHAR2(3),
  PG4_BP45              VARCHAR2(8),
  PG4_COMMENTS45        VARCHAR2(255),
  PG4_DATE46            DATE,
  PG4_GEST46            VARCHAR2(6),
  PG4_HT46              VARCHAR2(6),
  PG4_WT46              VARCHAR2(6),
  PG4_PRESN46           VARCHAR2(6),
  PG4_FHR46             VARCHAR2(6),
  PG4_URINEPR46         VARCHAR2(3),
  PG4_URINEGL46         VARCHAR2(3),
  PG4_BP46              VARCHAR2(8),
  PG4_COMMENTS46        VARCHAR2(255),
  PG4_DATE47            DATE,
  PG4_GEST47            VARCHAR2(6),
  PG4_HT47              VARCHAR2(6),
  PG4_WT47              VARCHAR2(6),
  PG4_PRESN47           VARCHAR2(6),
  PG4_FHR47             VARCHAR2(6),
  PG4_URINEPR47         VARCHAR2(3),
  PG4_URINEGL47         VARCHAR2(3),
  PG4_BP47              VARCHAR2(8),
  PG4_COMMENTS47        VARCHAR2(255),
  PG4_DATE48            DATE,
  PG4_GEST48            VARCHAR2(6),
  PG4_HT48              VARCHAR2(6),
  PG4_WT48              VARCHAR2(6),
  PG4_PRESN48           VARCHAR2(6),
  PG4_FHR48             VARCHAR2(6),
  PG4_URINEPR48         VARCHAR2(3),
  PG4_URINEGL48         VARCHAR2(3),
  PG4_BP48              VARCHAR2(8),
  PG4_COMMENTS48        VARCHAR2(255),
  PG4_DATE49            DATE,
  PG4_GEST49            VARCHAR2(6),
  PG4_HT49              VARCHAR2(6),
  PG4_WT49              VARCHAR2(6),
  PG4_PRESN49           VARCHAR2(6),
  PG4_FHR49             VARCHAR2(6),
  PG4_URINEPR49         VARCHAR2(3),
  PG4_URINEGL49         VARCHAR2(3),
  PG4_BP49              VARCHAR2(8),
  PG4_COMMENTS49        VARCHAR2(255),
  PG4_DATE50            DATE,
  PG4_GEST50            VARCHAR2(6),
  PG4_HT50              VARCHAR2(6),
  PG4_WT50              VARCHAR2(6),
  PG4_PRESN50           VARCHAR2(6),
  PG4_FHR50             VARCHAR2(6),
  PG4_URINEPR50         VARCHAR2(3),
  PG4_URINEGL50         VARCHAR2(3),
  PG4_BP50              VARCHAR2(8),
  PG4_COMMENTS50        VARCHAR2(255),
  PG4_DATE51            DATE,
  PG4_GEST51            VARCHAR2(6),
  PG4_HT51              VARCHAR2(6),
  PG4_WT51              VARCHAR2(6),
  PG4_PRESN51           VARCHAR2(6),
  PG4_FHR51             VARCHAR2(6),
  PG4_URINEPR51         VARCHAR2(3),
  PG4_URINEGL51         VARCHAR2(3),
  PG4_BP51              VARCHAR2(8),
  PG4_COMMENTS51        VARCHAR2(255),
  PG4_DATE52            DATE,
  PG4_GEST52            VARCHAR2(6),
  PG4_HT52              VARCHAR2(6),
  PG4_WT52              VARCHAR2(6),
  PG4_PRESN52           VARCHAR2(6),
  PG4_FHR52             VARCHAR2(6),
  PG4_URINEPR52         VARCHAR2(3),
  PG4_URINEGL52         VARCHAR2(3),
  PG4_BP52              VARCHAR2(8),
  PG4_COMMENTS52        VARCHAR2(255),
  PG4_DATE53            DATE,
  PG4_GEST53            VARCHAR2(6),
  PG4_HT53              VARCHAR2(6),
  PG4_WT53              VARCHAR2(6),
  PG4_PRESN53           VARCHAR2(6),
  PG4_FHR53             VARCHAR2(6),
  PG4_URINEPR53         VARCHAR2(3),
  PG4_URINEGL53         VARCHAR2(3),
  PG4_BP53              VARCHAR2(8),
  PG4_COMMENTS53        VARCHAR2(255),
  PG4_DATE54            DATE,
  PG4_GEST54            VARCHAR2(6),
  PG4_HT54              VARCHAR2(6),
  PG4_WT54              VARCHAR2(6),
  PG4_PRESN54           VARCHAR2(6),
  PG4_FHR54             VARCHAR2(6),
  PG4_URINEPR54         VARCHAR2(3),
  PG4_URINEGL54         VARCHAR2(3),
  PG4_BP54              VARCHAR2(8),
  PG4_COMMENTS54        VARCHAR2(255),
  AR2_UDATE1            DATE,
  AR2_UGA1              VARCHAR2(10),
  AR2_URESULTS1         VARCHAR2(50),
  AR2_UDATE2            DATE,
  AR2_UGA2              VARCHAR2(10),
  AR2_URESULTS2         VARCHAR2(50),
  AR2_UDATE3            DATE,
  AR2_UGA3              VARCHAR2(10),
  AR2_URESULTS3         VARCHAR2(50),
  AR2_UDATE4            DATE,
  AR2_UGA4              VARCHAR2(10),
  AR2_URESULTS4         VARCHAR2(50),
  AR2_HB                VARCHAR2(10),
  AR2_BLOODGROUP        VARCHAR2(6),
  AR2_RH                VARCHAR2(6),
  AR2_LABABS            VARCHAR2(10),
  AR2_LAB1GCT           VARCHAR2(10),
  AR2_LAB2GTT           VARCHAR2(10),
  AR2_STREP             VARCHAR2(10),
  AR2_EXERCISE          NUMBER(1),
  AR2_WORKPLAN          NUMBER(1),
  AR2_INTERCOURSE       NUMBER(1),
  AR2_TRAVEL            NUMBER(1),
  AR2_PRENATAL          NUMBER(1),
  AR2_BIRTH             NUMBER(1),
  AR2_ONCALL            NUMBER(1),
  AR2_PRETERM           NUMBER(1),
  AR2_PROM              NUMBER(1),
  AR2_APH               NUMBER(1),
  AR2_FETAL             NUMBER(1),
  AR2_ADMISSION         NUMBER(1),
  AR2_PAIN              NUMBER(1),
  AR2_LABOUR            NUMBER(1),
  AR2_BREAST            NUMBER(1),
  AR2_CIRCUMCISION      NUMBER(1),
  AR2_DISCHARGEPLAN     NUMBER(1),
  AR2_CAR               NUMBER(1),
  AR2_DEPRESSION        NUMBER(1),
  AR2_CONTRACEPTION     NUMBER(1),
  AR2_POSTPARTUMCARE    NUMBER(1),
  PG2_SIGNATURE         VARCHAR2(50),
  PG2_FORMDATE          DATE,
  PG2_SIGNATURE2        VARCHAR2(50),
  PG2_FORMDATE2         DATE,
  PG3_SIGNATURE         VARCHAR2(50),
  PG3_FORMDATE          DATE,
  PG3_SIGNATURE2        VARCHAR2(50),
  PG3_FORMDATE2         DATE,
  PG4_SIGNATURE         VARCHAR2(50),
  PG4_FORMDATE          DATE,
  PG4_SIGNATURE2        VARCHAR2(50),
  PG4_FORMDATE2         DATE
)
;
alter table FORMONAR
  add primary key (ID);

prompt
prompt Creating table FORMOVULATION
prompt ============================
prompt
create table FORMOVULATION
(
  ID                 NUMBER(11) not null,
  DEMOGRAPHIC_NO     NUMBER(11) default '0' not null,
  PROVIDER_NO        NUMBER(11) default '0',
  FORMCREATED        DATE default '01-JAN-1900',
  FORMEDITED         TIMESTAMP(6) default CURRENT_TIMESTAMP not null,
  CLIENTFIRSTNAME    VARCHAR2(30),
  CLIENTLASTNAME     VARCHAR2(30),
  DOB                VARCHAR2(16),
  HEALTHNUM          VARCHAR2(24),
  WORKPHONE          VARCHAR2(18),
  HOMEPHONE          VARCHAR2(18),
  OTHERPHONE         VARCHAR2(18),
  DIAG               VARCHAR2(24),
  FEMARA             VARCHAR2(24),
  TAMOXI             VARCHAR2(24),
  CCNOL              VARCHAR2(24),
  GONAD              VARCHAR2(24),
  METFORMIN          VARCHAR2(24),
  PARLODEL           VARCHAR2(24),
  FOLICACID          VARCHAR2(24),
  OVUL               VARCHAR2(24),
  POSTOV             VARCHAR2(24),
  IUI                VARCHAR2(24),
  TDI                VARCHAR2(24),
  SEMENVOLUME        VARCHAR2(24),
  SEMENPMOTILITY     VARCHAR2(24),
  SEMENTMOTILITY     VARCHAR2(24),
  SEMENMORPHOLOGY    VARCHAR2(24),
  SEMENCONCENTRATION VARCHAR2(24),
  SEMENSPERM         VARCHAR2(24),
  SEMENSPERMPH       VARCHAR2(24),
  DIAGNOSIS          VARCHAR2(255),
  SIDEEFFECTS        VARCHAR2(255),
  ABNORMALRESULT     VARCHAR2(255),
  PREVIOUSCYCLES     VARCHAR2(255),
  FSH                VARCHAR2(24),
  COLLECTIONDATE     VARCHAR2(24),
  ABSTINENCEDAYS     VARCHAR2(24),
  COLLECTIONTIME     VARCHAR2(24),
  COLLECTIONAMPM     VARCHAR2(24),
  COLLECTIONMETHOD   VARCHAR2(24),
  BMIAGE             VARCHAR2(3),
  BMIPPWT            VARCHAR2(5),
  BMIPPHT            VARCHAR2(5),
  BMI                VARCHAR2(5),
  PRECOLOR           VARCHAR2(24),
  PREVISCOSITY       VARCHAR2(24),
  PRELIQUEFACTION    VARCHAR2(24),
  PREPH              VARCHAR2(24),
  PREROUNDCELLS      VARCHAR2(24),
  PREPROGRESSION     VARCHAR2(24),
  PREVOLUME          VARCHAR2(24),
  PREDENSITY         VARCHAR2(24),
  PREMOTILITY        VARCHAR2(24),
  PRETMC             VARCHAR2(24),
  PREMORPHOLOGY      VARCHAR2(24),
  PREAGGLUTINATION   VARCHAR2(24),
  PREDEBRIS          VARCHAR2(24),
  POSTPROGRESSION    VARCHAR2(24),
  POSTVOLUME         VARCHAR2(24),
  POSTDENSITY        VARCHAR2(24),
  POSTMOTILITY       VARCHAR2(24),
  POSTTMC            VARCHAR2(24),
  DATE1              VARCHAR2(16),
  DATE2              VARCHAR2(16),
  DATE3              VARCHAR2(16),
  DATE4              VARCHAR2(16),
  DATE5              VARCHAR2(16),
  DATE6              VARCHAR2(16),
  DATE7              VARCHAR2(16),
  DATE8              VARCHAR2(16),
  DATE9              VARCHAR2(16),
  DATE10             VARCHAR2(16),
  DATE11             VARCHAR2(16),
  DATE12             VARCHAR2(16),
  DAY1               VARCHAR2(8),
  DAY2               VARCHAR2(8),
  DAY3               VARCHAR2(8),
  DAY4               VARCHAR2(8),
  DAY5               VARCHAR2(8),
  DAY6               VARCHAR2(8),
  DAY7               VARCHAR2(8),
  DAY8               VARCHAR2(8),
  DAY9               VARCHAR2(8),
  DAY10              VARCHAR2(8),
  DAY11              VARCHAR2(8),
  DAY12              VARCHAR2(8),
  E2LH1A             VARCHAR2(12),
  E2LH1B             VARCHAR2(12),
  E2LH2A             VARCHAR2(12),
  E2LH2B             VARCHAR2(12),
  E2LH3A             VARCHAR2(12),
  E2LH3B             VARCHAR2(12),
  E2LH4A             VARCHAR2(12),
  E2LH4B             VARCHAR2(12),
  E2LH5A             VARCHAR2(12),
  E2LH5B             VARCHAR2(12),
  E2LH6A             VARCHAR2(12),
  E2LH6B             VARCHAR2(12),
  E2LH7A             VARCHAR2(12),
  E2LH7B             VARCHAR2(12),
  E2LH8A             VARCHAR2(12),
  E2LH8B             VARCHAR2(12),
  E2LH9A             VARCHAR2(12),
  E2LH9B             VARCHAR2(12),
  E2LH10A            VARCHAR2(12),
  E2LH10B            VARCHAR2(12),
  E2LH11A            VARCHAR2(12),
  E2LH11B            VARCHAR2(12),
  E2LH12A            VARCHAR2(12),
  E2LH12B            VARCHAR2(12),
  TSHPRL1A           VARCHAR2(12),
  TSHPRL1B           VARCHAR2(12),
  TSHPRL2A           VARCHAR2(12),
  TSHPRL2B           VARCHAR2(12),
  TSHPRL3A           VARCHAR2(12),
  TSHPRL3B           VARCHAR2(12),
  TSHPRL4A           VARCHAR2(12),
  TSHPRL4B           VARCHAR2(12),
  TSHPRL5A           VARCHAR2(12),
  TSHPRL5B           VARCHAR2(12),
  TSHPRL6A           VARCHAR2(12),
  TSHPRL6B           VARCHAR2(12),
  TSHPRL7A           VARCHAR2(12),
  TSHPRL7B           VARCHAR2(12),
  TSHPRL8A           VARCHAR2(12),
  TSHPRL8B           VARCHAR2(12),
  TSHPRL9A           VARCHAR2(12),
  TSHPRL9B           VARCHAR2(12),
  TSHPRL10A          VARCHAR2(12),
  TSHPRL10B          VARCHAR2(12),
  TSHPRL11A          VARCHAR2(12),
  TSHPRL11B          VARCHAR2(12),
  TSHPRL12A          VARCHAR2(12),
  TSHPRL12B          VARCHAR2(12),
  PROGBHOG1A         VARCHAR2(12),
  PROGBHOG1B         VARCHAR2(12),
  PROGBHOG2A         VARCHAR2(12),
  PROGBHOG2B         VARCHAR2(12),
  PROGBHOG3A         VARCHAR2(12),
  PROGBHOG3B         VARCHAR2(12),
  PROGBHOG4A         VARCHAR2(12),
  PROGBHOG4B         VARCHAR2(12),
  PROGBHOG5A         VARCHAR2(12),
  PROGBHOG5B         VARCHAR2(12),
  PROGBHOG6A         VARCHAR2(12),
  PROGBHOG6B         VARCHAR2(12),
  PROGBHOG7A         VARCHAR2(12),
  PROGBHOG7B         VARCHAR2(12),
  PROGBHOG8A         VARCHAR2(12),
  PROGBHOG8B         VARCHAR2(12),
  PROGBHOG9A         VARCHAR2(12),
  PROGBHOG9B         VARCHAR2(12),
  PROGBHOG10A        VARCHAR2(12),
  PROGBHOG10B        VARCHAR2(12),
  PROGBHOG11A        VARCHAR2(12),
  PROGBHOG11B        VARCHAR2(12),
  PROGBHOG12A        VARCHAR2(12),
  PROGBHOG12B        VARCHAR2(12),
  FLAG_E2LH1A        VARCHAR2(8),
  FLAG_E2LH1B        VARCHAR2(8),
  FLAG_E2LH2A        VARCHAR2(8),
  FLAG_E2LH2B        VARCHAR2(8),
  FLAG_E2LH3A        VARCHAR2(8),
  FLAG_E2LH3B        VARCHAR2(8),
  FLAG_E2LH4A        VARCHAR2(8),
  FLAG_E2LH4B        VARCHAR2(8),
  FLAG_E2LH5A        VARCHAR2(8),
  FLAG_E2LH5B        VARCHAR2(8),
  FLAG_E2LH6A        VARCHAR2(8),
  FLAG_E2LH6B        VARCHAR2(8),
  FLAG_E2LH7A        VARCHAR2(8),
  FLAG_E2LH7B        VARCHAR2(8),
  FLAG_E2LH8A        VARCHAR2(8),
  FLAG_E2LH8B        VARCHAR2(8),
  FLAG_E2LH9A        VARCHAR2(8),
  FLAG_E2LH9B        VARCHAR2(8),
  FLAG_E2LH10A       VARCHAR2(8),
  FLAG_E2LH10B       VARCHAR2(8),
  FLAG_E2LH11A       VARCHAR2(8),
  FLAG_E2LH11B       VARCHAR2(8),
  FLAG_E2LH12A       VARCHAR2(8),
  FLAG_E2LH12B       VARCHAR2(8),
  FLAG_TSHPRL1A      VARCHAR2(8),
  FLAG_TSHPRL1B      VARCHAR2(8),
  FLAG_TSHPRL2A      VARCHAR2(8),
  FLAG_TSHPRL2B      VARCHAR2(8),
  FLAG_TSHPRL3A      VARCHAR2(8),
  FLAG_TSHPRL3B      VARCHAR2(8),
  FLAG_TSHPRL4A      VARCHAR2(8),
  FLAG_TSHPRL4B      VARCHAR2(8),
  FLAG_TSHPRL5A      VARCHAR2(8),
  FLAG_TSHPRL5B      VARCHAR2(8),
  FLAG_TSHPRL6A      VARCHAR2(8),
  FLAG_TSHPRL6B      VARCHAR2(8),
  FLAG_TSHPRL7A      VARCHAR2(8),
  FLAG_TSHPRL7B      VARCHAR2(8),
  FLAG_TSHPRL8A      VARCHAR2(8),
  FLAG_TSHPRL8B      VARCHAR2(8),
  FLAG_TSHPRL9A      VARCHAR2(8),
  FLAG_TSHPRL9B      VARCHAR2(8),
  FLAG_TSHPRL10A     VARCHAR2(8),
  FLAG_TSHPRL10B     VARCHAR2(8),
  FLAG_TSHPRL11A     VARCHAR2(8),
  FLAG_TSHPRL11B     VARCHAR2(8),
  FLAG_TSHPRL12A     VARCHAR2(8),
  FLAG_TSHPRL12B     VARCHAR2(8),
  FLAG_PROGBHOG1A    VARCHAR2(8),
  FLAG_PROGBHOG1B    VARCHAR2(8),
  FLAG_PROGBHOG2A    VARCHAR2(8),
  FLAG_PROGBHOG2B    VARCHAR2(8),
  FLAG_PROGBHOG3A    VARCHAR2(8),
  FLAG_PROGBHOG3B    VARCHAR2(8),
  FLAG_PROGBHOG4A    VARCHAR2(8),
  FLAG_PROGBHOG4B    VARCHAR2(8),
  FLAG_PROGBHOG5A    VARCHAR2(8),
  FLAG_PROGBHOG5B    VARCHAR2(8),
  FLAG_PROGBHOG6A    VARCHAR2(8),
  FLAG_PROGBHOG6B    VARCHAR2(8),
  FLAG_PROGBHOG7A    VARCHAR2(8),
  FLAG_PROGBHOG7B    VARCHAR2(8),
  FLAG_PROGBHOG8A    VARCHAR2(8),
  FLAG_PROGBHOG8B    VARCHAR2(8),
  FLAG_PROGBHOG9A    VARCHAR2(8),
  FLAG_PROGBHOG9B    VARCHAR2(8),
  FLAG_PROGBHOG10A   VARCHAR2(8),
  FLAG_PROGBHOG10B   VARCHAR2(8),
  FLAG_PROGBHOG11A   VARCHAR2(8),
  FLAG_PROGBHOG11B   VARCHAR2(8),
  FLAG_PROGBHOG12A   VARCHAR2(8),
  FLAG_PROGBHOG12B   VARCHAR2(8),
  RIGHTCYST1A        VARCHAR2(24),
  RIGHTCYST1B        VARCHAR2(24),
  RIGHTCYST2A        VARCHAR2(24),
  RIGHTCYST2B        VARCHAR2(24),
  RIGHTCYST3A        VARCHAR2(24),
  RIGHTCYST3B        VARCHAR2(24),
  RIGHTCYST4A        VARCHAR2(24),
  RIGHTCYST4B        VARCHAR2(24),
  RIGHTCYST5A        VARCHAR2(24),
  RIGHTCYST5B        VARCHAR2(24),
  RIGHTCYST6A        VARCHAR2(24),
  RIGHTCYST6B        VARCHAR2(24),
  RIGHTCYST7A        VARCHAR2(24),
  RIGHTCYST7B        VARCHAR2(24),
  RIGHTCYST8A        VARCHAR2(24),
  RIGHTCYST8B        VARCHAR2(24),
  RIGHTCYST9A        VARCHAR2(24),
  RIGHTCYST9B        VARCHAR2(24),
  RIGHTCYST10A       VARCHAR2(24),
  RIGHTCYST10B       VARCHAR2(24),
  RIGHTCYST11A       VARCHAR2(24),
  RIGHTCYST11B       VARCHAR2(24),
  RIGHTCYST12A       VARCHAR2(24),
  RIGHTCYST12B       VARCHAR2(24),
  LEFTCYST1A         VARCHAR2(24),
  LEFTCYST1B         VARCHAR2(24),
  LEFTCYST2A         VARCHAR2(24),
  LEFTCYST2B         VARCHAR2(24),
  LEFTCYST3A         VARCHAR2(24),
  LEFTCYST3B         VARCHAR2(24),
  LEFTCYST4A         VARCHAR2(24),
  LEFTCYST4B         VARCHAR2(24),
  LEFTCYST5A         VARCHAR2(24),
  LEFTCYST5B         VARCHAR2(24),
  LEFTCYST6A         VARCHAR2(24),
  LEFTCYST6B         VARCHAR2(24),
  LEFTCYST7A         VARCHAR2(24),
  LEFTCYST7B         VARCHAR2(24),
  LEFTCYST8A         VARCHAR2(24),
  LEFTCYST8B         VARCHAR2(24),
  LEFTCYST9A         VARCHAR2(24),
  LEFTCYST9B         VARCHAR2(24),
  LEFTCYST10A        VARCHAR2(24),
  LEFTCYST10B        VARCHAR2(24),
  LEFTCYST11A        VARCHAR2(24),
  LEFTCYST11B        VARCHAR2(24),
  LEFTCYST12A        VARCHAR2(24),
  LEFTCYST12B        VARCHAR2(24),
  ETTEX1A            VARCHAR2(24),
  ETTEX1B            VARCHAR2(24),
  ETTEX2A            VARCHAR2(24),
  ETTEX2B            VARCHAR2(24),
  ETTEX3A            VARCHAR2(24),
  ETTEX3B            VARCHAR2(24),
  ETTEX4A            VARCHAR2(24),
  ETTEX4B            VARCHAR2(24),
  ETTEX5A            VARCHAR2(24),
  ETTEX5B            VARCHAR2(24),
  ETTEX6A            VARCHAR2(24),
  ETTEX6B            VARCHAR2(24),
  ETTEX7A            VARCHAR2(24),
  ETTEX7B            VARCHAR2(24),
  ETTEX8A            VARCHAR2(24),
  ETTEX8B            VARCHAR2(24),
  ETTEX9A            VARCHAR2(24),
  ETTEX9B            VARCHAR2(24),
  ETTEX10A           VARCHAR2(24),
  ETTEX10B           VARCHAR2(24),
  ETTEX11A           VARCHAR2(24),
  ETTEX11B           VARCHAR2(24),
  ETTEX12A           VARCHAR2(24),
  ETTEX12B           VARCHAR2(24),
  FF1                VARCHAR2(12),
  FF2                VARCHAR2(12),
  FF3                VARCHAR2(12),
  FF4                VARCHAR2(12),
  FF5                VARCHAR2(12),
  FF6                VARCHAR2(12),
  FF7                VARCHAR2(12),
  FF8                VARCHAR2(12),
  FF9                VARCHAR2(12),
  FF10               VARCHAR2(12),
  FF11               VARCHAR2(12),
  FF12               VARCHAR2(12),
  RIGHTLO1           VARCHAR2(255),
  RIGHTLO2           VARCHAR2(255),
  RIGHTLO3           VARCHAR2(255),
  RIGHTLO4           VARCHAR2(255),
  RIGHTLO5           VARCHAR2(255),
  RIGHTLO6           VARCHAR2(255),
  RIGHTLO7           VARCHAR2(255),
  RIGHTLO8           VARCHAR2(255),
  RIGHTLO9           VARCHAR2(255),
  RIGHTLO10          VARCHAR2(255),
  RIGHTLO11          VARCHAR2(255),
  RIGHTLO12          VARCHAR2(255),
  LEFTLO1            VARCHAR2(255),
  LEFTLO2            VARCHAR2(255),
  LEFTLO3            VARCHAR2(255),
  LEFTLO4            VARCHAR2(255),
  LEFTLO5            VARCHAR2(255),
  LEFTLO6            VARCHAR2(255),
  LEFTLO7            VARCHAR2(255),
  LEFTLO8            VARCHAR2(255),
  LEFTLO9            VARCHAR2(255),
  LEFTLO10           VARCHAR2(255),
  LEFTLO11           VARCHAR2(255),
  LEFTLO12           VARCHAR2(255),
  MEDS1              VARCHAR2(255),
  MEDS2              VARCHAR2(255),
  MEDS3              VARCHAR2(255),
  MEDS4              VARCHAR2(255),
  MEDS5              VARCHAR2(255),
  MEDS6              VARCHAR2(255),
  MEDS7              VARCHAR2(255),
  MEDS8              VARCHAR2(255),
  MEDS9              VARCHAR2(255),
  MEDS10             VARCHAR2(255),
  MEDS11             VARCHAR2(255),
  MEDS12             VARCHAR2(255),
  COMMENT1           VARCHAR2(255),
  COMMENT2           VARCHAR2(255),
  COMMENT3           VARCHAR2(255),
  COMMENT4           VARCHAR2(255),
  COMMENT5           VARCHAR2(255),
  COMMENT6           VARCHAR2(255),
  COMMENT7           VARCHAR2(255),
  COMMENT8           VARCHAR2(255),
  COMMENT9           VARCHAR2(255),
  COMMENT10          VARCHAR2(255),
  COMMENT11          VARCHAR2(255),
  COMMENT12          VARCHAR2(255),
  POSTCOMMENT        VARCHAR2(255)
)
;
alter table FORMOVULATION
  add primary key (ID);

prompt
prompt Creating table FORMPALLIATIVECARE
prompt =================================
prompt
create table FORMPALLIATIVECARE
(
  ID               NUMBER(10) not null,
  DEMOGRAPHIC_NO   NUMBER(10),
  PROVIDER_NO      NUMBER(10),
  FORMCREATED      DATE,
  FORMEDITED       TIMESTAMP(6) not null,
  PNAME            VARCHAR2(60),
  DIAGNOSIS        VARCHAR2(60),
  DATE1            DATE,
  DATE2            DATE,
  DATE3            DATE,
  DATE4            DATE,
  PAIN1            VARCHAR2(255),
  PAIN2            VARCHAR2(255),
  PAIN3            VARCHAR2(255),
  PAIN4            VARCHAR2(255),
  GIBOWELS1        VARCHAR2(255),
  GIBOWELS2        VARCHAR2(255),
  GIBOWELS3        VARCHAR2(255),
  GIBOWELS4        VARCHAR2(255),
  GINAUSEA1        VARCHAR2(255),
  GINAUSEA2        VARCHAR2(255),
  GINAUSEA3        VARCHAR2(255),
  GINAUSEA4        VARCHAR2(255),
  GIDYSPHAGIA1     VARCHAR2(255),
  GIDYSPHAGIA2     VARCHAR2(255),
  GIDYSPHAGIA3     VARCHAR2(255),
  GIDYSPHAGIA4     VARCHAR2(255),
  GIHICCUPS1       VARCHAR2(255),
  GIHICCUPS2       VARCHAR2(255),
  GIHICCUPS3       VARCHAR2(255),
  GIHICCUPS4       VARCHAR2(255),
  GIMOUTH1         VARCHAR2(255),
  GIMOUTH2         VARCHAR2(255),
  GIMOUTH3         VARCHAR2(255),
  GIMOUTH4         VARCHAR2(255),
  GU1              VARCHAR2(255),
  GU2              VARCHAR2(255),
  GU3              VARCHAR2(255),
  GU4              VARCHAR2(255),
  SKINULCERS1      VARCHAR2(255),
  SKINULCERS2      VARCHAR2(255),
  SKINULCERS3      VARCHAR2(255),
  SKINULCERS4      VARCHAR2(255),
  SKINPRURITIS1    VARCHAR2(255),
  SKINPRURITIS2    VARCHAR2(255),
  SKINPRURITIS3    VARCHAR2(255),
  SKINPRURITIS4    VARCHAR2(255),
  PSYCHAGITATION1  VARCHAR2(255),
  PSYCHAGITATION2  VARCHAR2(255),
  PSYCHAGITATION3  VARCHAR2(255),
  PSYCHAGITATION4  VARCHAR2(255),
  PSYCHANOREXIA1   VARCHAR2(255),
  PSYCHANOREXIA2   VARCHAR2(255),
  PSYCHANOREXIA3   VARCHAR2(255),
  PSYCHANOREXIA4   VARCHAR2(255),
  PSYCHANXIETY1    VARCHAR2(255),
  PSYCHANXIETY2    VARCHAR2(255),
  PSYCHANXIETY3    VARCHAR2(255),
  PSYCHANXIETY4    VARCHAR2(255),
  PSYCHDEPRESSION1 VARCHAR2(255),
  PSYCHDEPRESSION2 VARCHAR2(255),
  PSYCHDEPRESSION3 VARCHAR2(255),
  PSYCHDEPRESSION4 VARCHAR2(255),
  PSYCHFATIGUE1    VARCHAR2(255),
  PSYCHFATIGUE2    VARCHAR2(255),
  PSYCHFATIGUE3    VARCHAR2(255),
  PSYCHFATIGUE4    VARCHAR2(255),
  PSYCHSOMNOLENCE1 VARCHAR2(255),
  PSYCHSOMNOLENCE2 VARCHAR2(255),
  PSYCHSOMNOLENCE3 VARCHAR2(255),
  PSYCHSOMNOLENCE4 VARCHAR2(255),
  RESPCOUGH1       VARCHAR2(255),
  RESPCOUGH2       VARCHAR2(255),
  RESPCOUGH3       VARCHAR2(255),
  RESPCOUGH4       VARCHAR2(255),
  RESPDYSPNEA1     VARCHAR2(255),
  RESPDYSPNEA2     VARCHAR2(255),
  RESPDYSPNEA3     VARCHAR2(255),
  RESPDYSPNEA4     VARCHAR2(255),
  RESPFEVER1       VARCHAR2(255),
  RESPFEVER2       VARCHAR2(255),
  RESPFEVER3       VARCHAR2(255),
  RESPFEVER4       VARCHAR2(255),
  RESPCAREGIVER1   VARCHAR2(255),
  RESPCAREGIVER2   VARCHAR2(255),
  RESPCAREGIVER3   VARCHAR2(255),
  RESPCAREGIVER4   VARCHAR2(255),
  OTHER1           VARCHAR2(255),
  OTHER2           VARCHAR2(255),
  OTHER3           VARCHAR2(255),
  OTHER4           VARCHAR2(255),
  SIGNATURE1       VARCHAR2(50),
  SIGNATURE2       VARCHAR2(50),
  SIGNATURE3       VARCHAR2(50),
  SIGNATURE4       VARCHAR2(50)
)
;
alter table FORMPALLIATIVECARE
  add primary key (ID);

prompt
prompt Creating table FORMPERIMENOPAUSAL
prompt =================================
prompt
create table FORMPERIMENOPAUSAL
(
  ID                 NUMBER(10) not null,
  DEMOGRAPHIC_NO     NUMBER(10) default '0' not null,
  PROVIDER_NO        NUMBER(10),
  FORMCREATED        DATE,
  FORMEDITED         TIMESTAMP(6) not null,
  LASTVISITED        VARCHAR2(1),
  PNAME              VARCHAR2(60),
  AGEMENOPAUSE       VARCHAR2(2),
  AGE                VARCHAR2(3),
  ORF_EMYES          NUMBER(1),
  ORF_EMNO           NUMBER(1),
  ORF_FHOYES         NUMBER(1),
  ORF_FHONO          NUMBER(1),
  ORF_FHOFYES        NUMBER(1),
  ORF_FHOFNO         NUMBER(1),
  ORF_LHYES          NUMBER(1),
  ORF_LHNO           NUMBER(1),
  ORF_PHFYES         NUMBER(1),
  ORF_PHFNO          NUMBER(1),
  ORF_WARYES         NUMBER(1),
  ORF_WARNO          NUMBER(1),
  ORF_TSBYES         NUMBER(1),
  ORF_TSBNO          NUMBER(1),
  ORF_HIPYES         NUMBER(1),
  ORF_HIPNO          NUMBER(1),
  ORF_IEYES          NUMBER(1),
  ORF_IENO           NUMBER(1),
  ORF_LLCIYES        NUMBER(1),
  ORF_LLCINO         NUMBER(1),
  ORF_CSYES          NUMBER(1),
  ORF_CSNO           NUMBER(1),
  ORF_CYES           NUMBER(1),
  ORF_CNO            NUMBER(1),
  ORF_AYES           NUMBER(1),
  ORF_ANO            NUMBER(1),
  ORF_CPTDYES        NUMBER(1),
  ORF_CPTDNO         NUMBER(1),
  ORF_DCYES          NUMBER(1),
  ORF_DCNO           NUMBER(1),
  ORF_ADYES          NUMBER(1),
  ORF_ADNO           NUMBER(1),
  ORF_TMYES          NUMBER(1),
  ORF_TMNO           NUMBER(1),
  ORF_COMMENTS       VARCHAR2(255),
  CS_MCYES           NUMBER(1),
  CS_MCNO            NUMBER(1),
  CS_MPYES           NUMBER(1),
  CS_MPNO            NUMBER(1),
  CS_HFYES           NUMBER(1),
  CS_HFNO            NUMBER(1),
  CS_VDYES           NUMBER(1),
  CS_VDNO            NUMBER(1),
  CS_DSIYES          NUMBER(1),
  CS_DSINO           NUMBER(1),
  CS_LISAYES         NUMBER(1),
  CS_LISANO          NUMBER(1),
  CS_LBCYES          NUMBER(1),
  CS_LBCNO           NUMBER(1),
  CS_HBIYES          NUMBER(1),
  CS_HBINO           NUMBER(1),
  CS_COMMENTS        VARCHAR2(255),
  CRF_FHHDYES        NUMBER(1),
  CRF_FHHDNO         NUMBER(1),
  CRF_HAYES          NUMBER(1),
  CRF_HANO           NUMBER(1),
  CRF_HCHFYES        NUMBER(1),
  CRF_HCHFNO         NUMBER(1),
  CRF_HHAYES         NUMBER(1),
  CRF_HHANO          NUMBER(1),
  CRF_HDYES          NUMBER(1),
  CRF_HDNO           NUMBER(1),
  CRF_CSYES          NUMBER(1),
  CRF_CSNO           NUMBER(1),
  CRF_HBPYES         NUMBER(1),
  CRF_HBPNO          NUMBER(1),
  CRF_LHCYES         NUMBER(1),
  CRF_LHCNO          NUMBER(1),
  CRF_HTYES          NUMBER(1),
  CRF_HTNO           NUMBER(1),
  CRF_HLCYES         NUMBER(1),
  CRF_HLCNO          NUMBER(1),
  CRF_OYES           NUMBER(1),
  CRF_ONO            NUMBER(1),
  CRF_SLYES          NUMBER(1),
  CRF_SLNO           NUMBER(1),
  CRF_COMMENTS       VARCHAR2(255),
  RH_FHBCYES         NUMBER(1),
  RH_FHBCNO          NUMBER(1),
  RH_PHBCYES         NUMBER(1),
  RH_PHBCNO          NUMBER(1),
  RH_PHOCYES         NUMBER(1),
  RH_PHOCNO          NUMBER(1),
  AGEHYSTERECTOMY    VARCHAR2(2),
  RH_HYES            NUMBER(1),
  RH_HNO             NUMBER(1),
  RH_HWROYES         NUMBER(1),
  RH_HWRONO          NUMBER(1),
  RH_HPCBYES         NUMBER(1),
  RH_HPCBNO          NUMBER(1),
  RH_FHADYES         NUMBER(1),
  RH_FHADNO          NUMBER(1),
  RH_FHCCYES         NUMBER(1),
  RH_FHCCNO          NUMBER(1),
  RH_OTHER           VARCHAR2(60),
  RH_OYES            NUMBER(1),
  RH_ONO             NUMBER(1),
  RH_COMMENTS        VARCHAR2(255),
  CM_CS              VARCHAR2(30),
  CM_VDS             VARCHAR2(30),
  CM_OTHER1          VARCHAR2(30),
  CM_O1              VARCHAR2(30),
  CM_OTHER2          VARCHAR2(30),
  CM_O2              VARCHAR2(30),
  CM_COMMENTS        VARCHAR2(255),
  PHRTYES            NUMBER(1),
  PHRTNO             NUMBER(1),
  ESTROGENYES        NUMBER(1),
  ESTROGENNO         NUMBER(1),
  PROGESTERONEYES    NUMBER(1),
  PROGESTERONENO     NUMBER(1),
  HRTYES             NUMBER(1),
  HRTNO              NUMBER(1),
  WHENHRT            VARCHAR2(20),
  REASONDISCONTINUED VARCHAR2(100),
  DATE1              DATE,
  DATE2              DATE,
  DATE3              DATE,
  DATE4              DATE,
  DATE5              DATE,
  DATE6              DATE,
  DATE7              DATE,
  DATE8              DATE,
  ETOHUSE1           VARCHAR2(100),
  ETOHUSE2           VARCHAR2(100),
  ETOHUSE3           VARCHAR2(100),
  ETOHUSE4           VARCHAR2(100),
  SMOKINGCESSATION1  VARCHAR2(100),
  SMOKINGCESSATION2  VARCHAR2(100),
  SMOKINGCESSATION3  VARCHAR2(100),
  SMOKINGCESSATION4  VARCHAR2(100),
  EXERCISE1          VARCHAR2(100),
  EXERCISE2          VARCHAR2(100),
  EXERCISE3          VARCHAR2(100),
  EXERCISE4          VARCHAR2(100),
  VISION1            VARCHAR2(100),
  VISION2            VARCHAR2(100),
  VISION3            VARCHAR2(100),
  VISION4            VARCHAR2(100),
  LOWFAT1            VARCHAR2(100),
  LOWFAT2            VARCHAR2(100),
  LOWFAT3            VARCHAR2(100),
  LOWFAT4            VARCHAR2(100),
  TDLAST1            VARCHAR2(100),
  TDLAST2            VARCHAR2(100),
  TDLAST3            VARCHAR2(100),
  TDLAST4            VARCHAR2(100),
  CALCIUM1           VARCHAR2(100),
  CALCIUM2           VARCHAR2(100),
  CALCIUM3           VARCHAR2(100),
  CALCIUM4           VARCHAR2(100),
  FLU1               VARCHAR2(100),
  FLU2               VARCHAR2(100),
  FLU3               VARCHAR2(100),
  FLU4               VARCHAR2(100),
  VITAMIND1          VARCHAR2(100),
  VITAMIND2          VARCHAR2(100),
  VITAMIND3          VARCHAR2(100),
  VITAMIND4          VARCHAR2(100),
  PNEUMOVAXDATE      DATE,
  PNEUMOVAX1         VARCHAR2(100),
  PNEUMOVAX2         VARCHAR2(100),
  PNEUMOVAX3         VARCHAR2(100),
  PNEUMOVAX4         VARCHAR2(100),
  PAPSMEAR1          VARCHAR2(100),
  PAPSMEAR2          VARCHAR2(100),
  PAPSMEAR3          VARCHAR2(100),
  PAPSMEAR4          VARCHAR2(100),
  HEIGHT1            VARCHAR2(100),
  HEIGHT2            VARCHAR2(100),
  HEIGHT3            VARCHAR2(100),
  HEIGHT4            VARCHAR2(100),
  BLOODPRESSURE1     VARCHAR2(100),
  BLOODPRESSURE2     VARCHAR2(100),
  BLOODPRESSURE3     VARCHAR2(100),
  BLOODPRESSURE4     VARCHAR2(100),
  WEIGHT1            VARCHAR2(100),
  WEIGHT2            VARCHAR2(100),
  WEIGHT3            VARCHAR2(100),
  WEIGHT4            VARCHAR2(100),
  CBE1               VARCHAR2(100),
  CBE2               VARCHAR2(100),
  CBE3               VARCHAR2(100),
  CBE4               VARCHAR2(100),
  BMD1               VARCHAR2(100),
  BMD2               VARCHAR2(100),
  BMD3               VARCHAR2(100),
  BMD4               VARCHAR2(100),
  MAMMOGRAPHY1       VARCHAR2(100),
  MAMMOGRAPHY2       VARCHAR2(100),
  MAMMOGRAPHY3       VARCHAR2(100),
  MAMMOGRAPHY4       VARCHAR2(100),
  OTHER1             VARCHAR2(30),
  OTHER11            VARCHAR2(100),
  OTHER12            VARCHAR2(100),
  OTHER13            VARCHAR2(100),
  OTHER14            VARCHAR2(100),
  OTHER2             VARCHAR2(30),
  OTHER21            VARCHAR2(100),
  OTHER22            VARCHAR2(100),
  OTHER23            VARCHAR2(100),
  OTHER24            VARCHAR2(100),
  OTHER3             VARCHAR2(30),
  OTHER31            VARCHAR2(100),
  OTHER32            VARCHAR2(100),
  OTHER33            VARCHAR2(100),
  OTHER34            VARCHAR2(100)
)
;
alter table FORMPERIMENOPAUSAL
  add primary key (ID);

prompt
prompt Creating table FORMRECEPTIONASSESSMENT
prompt ======================================
prompt
create table FORMRECEPTIONASSESSMENT
(
  ID                             NUMBER(10) not null,
  DEMOGRAPHIC_NO                 NUMBER(10) default '0' not null,
  PROVIDER_NO                    NUMBER(10),
  FORMCREATED                    DATE,
  FORMEDITED                     TIMESTAMP(6) default CURRENT_TIMESTAMP not null,
  ASSESSDATE                     VARCHAR2(24),
  ASSESSSTARTTIME                VARCHAR2(24),
  ENTERSEATONDATE                VARCHAR2(24),
  CBOX_NEWCLIENT                 VARCHAR2(1),
  CBOX_DATEOFREADMISSION         VARCHAR2(1),
  DATEOFREADMISSION              VARCHAR2(24),
  CLIENTSURNAME                  VARCHAR2(50),
  CLIENTFIRSTNAME                VARCHAR2(50),
  MONTH                          VARCHAR2(2),
  DAY                            VARCHAR2(2),
  YEAR                           VARCHAR2(4),
  CBOX_SPEAKENGLISH              VARCHAR2(1),
  CBOX_SPEAKFRENCH               VARCHAR2(1),
  CBOX_SPEAKOTHER                VARCHAR2(1),
  SPEAKOTHER                     VARCHAR2(36),
  REASONTOSEATON                 VARCHAR2(255),
  DATESATSEATON                  VARCHAR2(70),
  CBOX_ASSISTINHEALTH            VARCHAR2(1),
  CBOX_ASSISTINIDENTIFICATION    VARCHAR2(1),
  CBOX_ASSISTINADDICTIONS        VARCHAR2(1),
  CBOX_ASSISTINHOUSING           VARCHAR2(1),
  CBOX_ASSISTINEDUCATION         VARCHAR2(1),
  CBOX_ASSISTINEMPLOYMENT        VARCHAR2(1),
  CBOX_ASSISTINFINANCE           VARCHAR2(1),
  CBOX_ASSISTINLEGAL             VARCHAR2(1),
  CBOX_ASSISTINIMMIGRATION       VARCHAR2(1),
  CBOX_NOID                      VARCHAR2(1),
  CBOX_SINCARD                   VARCHAR2(1),
  CBOX_HEALTHCARD                VARCHAR2(1),
  HEALTHCARDNUM                  VARCHAR2(24),
  CBOX_BIRTHCERTIFICATE          VARCHAR2(1),
  CBOX_CITZENSHIPCARD            VARCHAR2(1),
  CBOX_IMMIGRANT                 VARCHAR2(1),
  CBOX_REFUGEE                   VARCHAR2(1),
  CBOX_OTHERID                   VARCHAR2(1),
  OTHERIDENTIFICATION            VARCHAR2(70),
  CBOX_IDFILED                   VARCHAR2(1),
  CBOX_IDNONE                    VARCHAR2(1),
  COMMENTSONID                   VARCHAR2(255),
  CBOX_OW                        VARCHAR2(1),
  CBOX_ODSP                      VARCHAR2(1),
  CBOX_WSIB                      VARCHAR2(1),
  CBOX_EMPLOYMENT                VARCHAR2(1),
  CBOX_EI                        VARCHAR2(1),
  CBOX_OAS                       VARCHAR2(1),
  CBOX_CPP                       VARCHAR2(1),
  CBOX_OTHERINCOME               VARCHAR2(1),
  RADIO_ONLINECHECK              VARCHAR2(36),
  RADIO_ACTIVE                   VARCHAR2(36),
  CBOX_NORECORD                  VARCHAR2(1),
  LASTISSUEDATE                  VARCHAR2(24),
  OFFICE                         VARCHAR2(50),
  WORKERNUM                      VARCHAR2(36),
  AMTRECEIVED                    VARCHAR2(9),
  RADIO_HASDOCTOR                VARCHAR2(36),
  DOCTORNAME                     VARCHAR2(50),
  DOCTORPHONE                    VARCHAR2(24),
  DOCTORPHONEEXT                 VARCHAR2(8),
  DOCTORADDRESS                  VARCHAR2(120),
  RADIO_SEEDOCTOR                VARCHAR2(36),
  RADIO_HEALTHISSUE              VARCHAR2(36),
  HEALTHISSUEDETAILS             VARCHAR2(255),
  CBOX_HASDIABETES               VARCHAR2(1),
  CBOX_INSULIN                   VARCHAR2(1),
  CBOX_EPILEPSY                  VARCHAR2(1),
  CBOX_BLEEDING                  VARCHAR2(1),
  CBOX_HEARINGIMPAIR             VARCHAR2(1),
  CBOX_VISUALIMPAIR              VARCHAR2(1),
  CBOX_MOBILITYIMPAIR            VARCHAR2(1),
  MOBILITYIMPAIR                 VARCHAR2(255),
  RADIO_OTHERHEALTHCONCERN       VARCHAR2(36),
  OTHERHEALTHCONERNS             VARCHAR2(255),
  RADIO_TAKEMEDICATION           VARCHAR2(36),
  NAMESOFMEDICATION              VARCHAR2(120),
  RADIO_HELPOBTAINMEDICATION     VARCHAR2(36),
  HELPOBTAINMEDICATION           VARCHAR2(255),
  RADIO_ALLERGICTOMEDICATION     VARCHAR2(36),
  ALLERGICTOMEDICATIONNAME       VARCHAR2(255),
  REACTIONTOMEDICATION           VARCHAR2(255),
  RADIO_MENTALHEALTHCONCERNS     VARCHAR2(36),
  MENTALHEALTHCONCERNS           VARCHAR2(255),
  FREQUENCYOFSEEINGDOCTOR        VARCHAR2(8),
  CBOX_VISITWALKINCLINIC         VARCHAR2(1),
  CBOX_VISITHEALTHCENTER         VARCHAR2(1),
  CBOX_VISITEMERGENCYROOM        VARCHAR2(1),
  CBOX_VISITOTHERS               VARCHAR2(1),
  CBOX_VISITHEALTHOFFICE         VARCHAR2(1),
  RADIO_SEESAMEDOCTOR            VARCHAR2(36),
  FRQOFSEEINGEMERGENCYROOMDOCTOR VARCHAR2(8),
  RADIO_DIDNOTRECEIVEHEALTHCARE  VARCHAR2(36),
  CBOX_TREATPHYSICALHEALTH       VARCHAR2(1),
  CBOX_TREATMENTALHEALTH         VARCHAR2(1),
  CBOX_REGULARCHECKUP            VARCHAR2(1),
  CBOX_TREATOTHERREASONS         VARCHAR2(1),
  TREATOTHERREASONS              VARCHAR2(255),
  CBOX_TREATINJURY               VARCHAR2(1),
  CBOX_GOTOWALKINCLINIC          VARCHAR2(1),
  CBOX_GOTOHEALTHCENTER          VARCHAR2(1),
  CBOX_GOTOEMERGENCYROOM         VARCHAR2(1),
  CBOX_GOTOOTHERS                VARCHAR2(1),
  GOTOOTHERS                     VARCHAR2(255),
  CBOX_HEALTHOFFICE              VARCHAR2(1),
  RADIO_APPMTSEEDOCTORIN3MTHS    VARCHAR2(36),
  RADIO_NEEDREGULARDOCTOR        VARCHAR2(36),
  RADIO_OBJECTTOREGDOCTORIN4WKS  VARCHAR2(36),
  RADIO_RATEOVERALLHEALTH        VARCHAR2(36),
  RADIO_SPEAKTORESEARCHER        VARCHAR2(36),
  CONTACTNAME                    VARCHAR2(70),
  CONTACTPHONE                   VARCHAR2(24),
  CONTACTADDRESS                 VARCHAR2(255),
  CONTACTRELATIONSHIP            VARCHAR2(120),
  RADIO_HASMENTALILLNESS         VARCHAR2(36),
  RADIO_HASDRINKINGPROBLEM       VARCHAR2(36),
  RADIO_HASDRUGPROBLEM           VARCHAR2(36),
  RADIO_HASHEALTHPROBLEM         VARCHAR2(36),
  RADIO_HASBEHAVIORPROBLEM       VARCHAR2(36),
  RADIO_NEEDSEATONSERVICE        VARCHAR2(36),
  RADIO_SEATONTOUR               VARCHAR2(36),
  SEATONNOTTOURED                VARCHAR2(255),
  RADIO_PAMPHLETISSUED           VARCHAR2(36),
  PAMPHLETNOTISSUED              VARCHAR2(255),
  SUMMARY                        VARCHAR2(255),
  COMPLETEDBY                    VARCHAR2(120),
  ASSESSCOMPLETETIME             VARCHAR2(36),
  CBOX_PAMPHLETISSUED            VARCHAR2(1),
  CBOX_HOSTEL                    VARCHAR2(1),
  CBOX_ROTARYCLUB                VARCHAR2(1),
  CBOX_ANNEXHARM                 VARCHAR2(1),
  CBOX_LONGTERMPROGRAM           VARCHAR2(1),
  CBOX_BIRCHMOUNTRESIDENCE       VARCHAR2(1),
  CBOX_ONEILLHOUSE               VARCHAR2(1),
  CBOX_FORTYORK                  VARCHAR2(1),
  CBOX_DOWNSVIEWDELLS            VARCHAR2(1)
)
;
alter table FORMRECEPTIONASSESSMENT
  add primary key (ID);

prompt
prompt Creating table FORMRHIMMUNEGLOBULIN
prompt ===================================
prompt
create table FORMRHIMMUNEGLOBULIN
(
  ID                          NUMBER(10) not null,
  DEMOGRAPHIC_NO              NUMBER(10) default '0' not null,
  PROVIDER_NO                 NUMBER(10),
  FORMCREATED                 DATE,
  FORMEDITED                  TIMESTAMP(6) default CURRENT_TIMESTAMP not null,
  WORKFLOWID                  NUMBER(10),
  STATE                       VARCHAR2(1),
  DATEOFREFERRAL              DATE,
  EDD                         DATE,
  MOTHERSURNAME               VARCHAR2(30),
  MOTHERFIRSTNAME             VARCHAR2(30),
  DOB                         DATE,
  MOTHERHIN                   VARCHAR2(20),
  MOTHERVC                    VARCHAR2(30),
  MOTHERADDRESS               VARCHAR2(60),
  MOTHERCITY                  VARCHAR2(60),
  MOTHERPROVINCE              VARCHAR2(60),
  MOTHERPOSTALCODE            VARCHAR2(10),
  MOTHERABO                   VARCHAR2(3),
  MOTHERRHTYPE                VARCHAR2(4),
  HOSPITALFORDELIVERY         VARCHAR2(255),
  REFPHYSURNAME               VARCHAR2(30),
  REFPHYFIRSTNAME             VARCHAR2(30),
  REFPHYADDRESS               VARCHAR2(60),
  REFPHYCITY                  VARCHAR2(60),
  REFPHYPROVINCE              VARCHAR2(60),
  REFPHYPOSTALCODE            VARCHAR2(10),
  REFPHYPHONE                 VARCHAR2(20),
  REFPHYFAX                   VARCHAR2(20),
  COMMENTS                    VARCHAR2(4000),
  OBSHISG                     VARCHAR2(10),
  OBSHISP                     VARCHAR2(10),
  OBSHIST                     VARCHAR2(10),
  OBSHISA                     VARCHAR2(10),
  OBSHISL                     VARCHAR2(10),
  OBSHISTUBMOLPREGYES         NUMBER(1),
  OBSHISTUBMOLPREGNO          NUMBER(1),
  OBSHISMISABORTIONYES        NUMBER(1),
  OBSHISMISABORTIONNO         NUMBER(1),
  OBSHISRECEIVEANTIDYES       NUMBER(1),
  OBSHISRECEIVEANTIDNO        NUMBER(1),
  MOTHERANTIBODIES            VARCHAR2(4),
  FATHERABO                   VARCHAR2(3),
  FATHERRHTYPE                VARCHAR2(4),
  PMHISBLCLDISORDERSYES       NUMBER(1),
  PMHISBLCLDISORDERSNO        NUMBER(1),
  PMHISBLCLDISORDERSCOMMENT   VARCHAR2(4000),
  PMHISBLPLTRANSFUSYES        NUMBER(1),
  PMHISBLPLTRANSFUSNO         NUMBER(1),
  PMHISBLPLTRANSFUSCOMMENT    VARCHAR2(4000),
  ALLREACTIONSYES             NUMBER(1),
  ALLREACTIONSNO              NUMBER(1),
  ALLREACTIONSCOMMENT         VARCHAR2(4000),
  CURPREGDUEDATECHANGEYES     NUMBER(1),
  CURPREGDUEDATECHANGENO      NUMBER(1),
  CURPREGPROCEDURESYES        NUMBER(1),
  CURPREGPROCEDURESNO         NUMBER(1),
  CURPREGPROCEDURESCOMMENT    VARCHAR2(4000),
  CURPREGBLEEDINGYES          NUMBER(1),
  CURPREGBLEEDINGNO           NUMBER(1),
  CURPREGBLEEDINGCOMMENT      VARCHAR2(4000),
  CURPREGBLEEDINGCONTYES      NUMBER(1),
  CURPREGBLEEDINGCONTNO       NUMBER(1),
  CURPREGTRAUMAYES            NUMBER(1),
  CURPREGTRAUMANO             NUMBER(1),
  CURPREGANTIDYES             NUMBER(1),
  CURPREGANTIDNO              NUMBER(1),
  CURPREGANTIDCOMMENT         VARCHAR2(4000),
  CURPREGANTIDREACTIONYES     NUMBER(1),
  CURPREGANTIDREACTIONNO      NUMBER(1),
  CURPREGBLOODDRAWNYES        NUMBER(1),
  CURPREGBLOODDRAWNNO         NUMBER(1),
  CURPREGDUEDATECHANGECOMMENT VARCHAR2(4000),
  FAMPHYSURNAME               VARCHAR2(30),
  FAMPHYFIRSTNAME             VARCHAR2(30),
  FAMPHYADDRESS               VARCHAR2(60),
  FAMPHYCITY                  VARCHAR2(60),
  FAMPHYPROVINCE              VARCHAR2(60),
  FAMPHYPOSTALCODE            VARCHAR2(10),
  FAMPHYPHONE                 VARCHAR2(20),
  FAMPHYFAX                   VARCHAR2(20)
)
;
alter table FORMRHIMMUNEGLOBULIN
  add primary key (ID);

prompt
prompt Creating table FORMROURKE
prompt =========================
prompt
create table FORMROURKE
(
  ID                      NUMBER(10) not null,
  DEMOGRAPHIC_NO          NUMBER(10),
  PROVIDER_NO             NUMBER(10),
  FORMCREATED             DATE,
  FORMEDITED              TIMESTAMP(6) not null,
  C_LASTVISITED           VARCHAR2(3),
  C_BIRTHREMARKS          VARCHAR2(4000),
  C_RISKFACTORS           VARCHAR2(4000),
  C_PNAME                 VARCHAR2(60),
  C_BIRTHDATE             DATE,
  C_LENGTH                VARCHAR2(6),
  C_HEADCIRC              VARCHAR2(6),
  C_BIRTHWEIGHT           VARCHAR2(7),
  C_DISCHARGEWEIGHT       VARCHAR2(7),
  P1_DATE1W               DATE,
  P1_DATE2W               DATE,
  P1_DATE1M               DATE,
  P1_DATE2M               DATE,
  P1_HT1W                 VARCHAR2(5),
  P1_WT1W                 VARCHAR2(5),
  P1_HC1W                 VARCHAR2(5),
  P1_HT2W                 VARCHAR2(5),
  P1_WT2W                 VARCHAR2(5),
  P1_HC2W                 VARCHAR2(5),
  P1_HT1M                 VARCHAR2(5),
  P1_WT1M                 VARCHAR2(5),
  P1_HC1M                 VARCHAR2(5),
  P1_HT2M                 VARCHAR2(5),
  P1_WT2M                 VARCHAR2(5),
  P1_HC2M                 VARCHAR2(5),
  P1_PCONCERN1W           VARCHAR2(4000),
  P1_PCONCERN2W           VARCHAR2(4000),
  P1_PCONCERN1M           VARCHAR2(4000),
  P1_PCONCERN2M           VARCHAR2(4000),
  P1_BREASTFEEDING1W      NUMBER(1),
  P1_FORMULAFEEDING1W     NUMBER(1),
  P1_STOOLURINE1W         NUMBER(1),
  P1_NUTRITION1W          VARCHAR2(250),
  P1_BREASTFEEDING2W      NUMBER(1),
  P1_FORMULAFEEDING2W     NUMBER(1),
  P1_STOOLURINE2W         NUMBER(1),
  P1_NUTRITION2W          VARCHAR2(250),
  P1_BREASTFEEDING1M      NUMBER(1),
  P1_FORMULAFEEDING1M     NUMBER(1),
  P1_STOOLURINE1M         NUMBER(1),
  P1_NUTRITION1M          VARCHAR2(250),
  P1_BREASTFEEDING2M      NUMBER(1),
  P1_FORMULAFEEDING2M     NUMBER(1),
  P1_NUTRITION2M          VARCHAR2(250),
  P1_CARSEAT1W            NUMBER(1),
  P1_CRIBSAFETY1W         NUMBER(1),
  P1_SLEEPING1W           NUMBER(1),
  P1_SOOTH1W              NUMBER(1),
  P1_BONDING1W            NUMBER(1),
  P1_FATIGUE1W            NUMBER(1),
  P1_SIBLINGS1W           NUMBER(1),
  P1_FAMILY1W             NUMBER(1),
  P1_HOMEVISIT1W          NUMBER(1),
  P1_SLEEPPOS1W           NUMBER(1),
  P1_TEMP1W               NUMBER(1),
  P1_SMOKE1W              NUMBER(1),
  P1_EDUCATIONADVICE1W    VARCHAR2(250),
  P1_CARSEAT2W            NUMBER(1),
  P1_CRIBSAFETY2W         NUMBER(1),
  P1_SLEEPING2W           NUMBER(1),
  P1_SOOTH2W              NUMBER(1),
  P1_BONDING2W            NUMBER(1),
  P1_FATIGUE2W            NUMBER(1),
  P1_FAMILY2W             NUMBER(1),
  P1_SIBLINGS2W           NUMBER(1),
  P1_HOMEVISIT2W          NUMBER(1),
  P1_SLEEPPOS2W           NUMBER(1),
  P1_TEMP2W               NUMBER(1),
  P1_SMOKE2W              NUMBER(1),
  P1_EDUCATIONADVICE2W    VARCHAR2(250),
  P1_CARBONMONOXIDE1M     NUMBER(1),
  P1_SLEEPWEAR1M          NUMBER(1),
  P1_HOTWATER1M           NUMBER(1),
  P1_TOYS1M               NUMBER(1),
  P1_CRYING1M             NUMBER(1),
  P1_SOOTH1M              NUMBER(1),
  P1_INTERACTION1M        NUMBER(1),
  P1_SUPPORTS1M           NUMBER(1),
  P1_EDUCATIONADVICE1M    VARCHAR2(250),
  P1_FALLS2M              NUMBER(1),
  P1_TOYS2M               NUMBER(1),
  P1_CRYING2M             NUMBER(1),
  P1_SOOTH2M              NUMBER(1),
  P1_INTERACTION2M        NUMBER(1),
  P1_STRESS2M             NUMBER(1),
  P1_FEVER2M              NUMBER(1),
  P1_EDUCATIONADVICE2M    VARCHAR2(250),
  P1_DEVELOPMENT1W        VARCHAR2(250),
  P1_DEVELOPMENT2W        VARCHAR2(250),
  P1_FOCUSGAZE1M          NUMBER(1),
  P1_STARTLES1M           NUMBER(1),
  P1_SUCKS1M              NUMBER(1),
  P1_NOPARENTSCONCERNS1M  NUMBER(1),
  P1_DEVELOPMENT1M        VARCHAR2(250),
  P1_FOLLOWMOVES2M        NUMBER(1),
  P1_SOUNDS2M             NUMBER(1),
  P1_HEADUP2M             NUMBER(1),
  P1_CUDDLED2M            NUMBER(1),
  P1_NOPARENTCONCERNS2M   NUMBER(1),
  P1_DEVELOPMENT2M        VARCHAR2(250),
  P1_SKIN1W               NUMBER(1),
  P1_FONTANELLES1W        NUMBER(1),
  P1_EYES1W               NUMBER(1),
  P1_EARS1W               NUMBER(1),
  P1_HEARTLUNGS1W         NUMBER(1),
  P1_UMBILICUS1W          NUMBER(1),
  P1_FEMORALPULSES1W      NUMBER(1),
  P1_HIPS1W               NUMBER(1),
  P1_TESTICLES1W          NUMBER(1),
  P1_MALEURINARY1W        NUMBER(1),
  P1_PHYSICAL1W           VARCHAR2(250),
  P1_SKIN2W               NUMBER(1),
  P1_FONTANELLES2W        NUMBER(1),
  P1_EYES2W               NUMBER(1),
  P1_EARS2W               NUMBER(1),
  P1_HEARTLUNGS2W         NUMBER(1),
  P1_UMBILICUS2W          NUMBER(1),
  P1_FEMORALPULSES2W      NUMBER(1),
  P1_HIPS2W               NUMBER(1),
  P1_TESTICLES2W          NUMBER(1),
  P1_MALEURINARY2W        NUMBER(1),
  P1_PHYSICAL2W           VARCHAR2(250),
  P1_FONTANELLES1M        NUMBER(1),
  P1_EYES1M               NUMBER(1),
  P1_COVER1M              NUMBER(1),
  P1_HEARING1M            NUMBER(1),
  P1_HEART1M              NUMBER(1),
  P1_HIPS1M               NUMBER(1),
  P1_PHYSICAL1M           VARCHAR2(250),
  P1_FONTANELLES2M        NUMBER(1),
  P1_EYES2M               NUMBER(1),
  P1_COVER2M              NUMBER(1),
  P1_HEARING2M            NUMBER(1),
  P1_HEART2M              NUMBER(1),
  P1_HIPS2M               NUMBER(1),
  P1_PHYSICAL2M           VARCHAR2(250),
  P1_PKUTHYROID1W         NUMBER(1),
  P1_HEMOSCREEN1W         NUMBER(1),
  P1_PROBLEMS1W           VARCHAR2(250),
  P1_PROBLEMS2W           VARCHAR2(250),
  P1_PROBLEMS1M           VARCHAR2(250),
  P1_PROBLEMS2M           VARCHAR2(250),
  P1_HEPB1W               NUMBER(1),
  P1_IMMUNIZATION1W       VARCHAR2(250),
  P1_IMMUNIZATION2W       VARCHAR2(250),
  P1_IMMUNIZ1M            NUMBER(1),
  P1_ACETAMINOPHEN1M      NUMBER(1),
  P1_HEPB1M               NUMBER(1),
  P1_IMMUNIZATION1M       VARCHAR2(250),
  P1_ACETAMINOPHEN2M      NUMBER(1),
  P1_HIB2M                NUMBER(1),
  P1_POLIO2M              NUMBER(1),
  P1_IMMUNIZATION2M       VARCHAR2(250),
  P1_SIGNATURE1W          VARCHAR2(250),
  P1_SIGNATURE2W          VARCHAR2(250),
  P1_SIGNATURE1M          VARCHAR2(250),
  P1_SIGNATURE2M          VARCHAR2(250),
  P2_DATE4M               DATE,
  P2_DATE6M               DATE,
  P2_DATE9M               DATE,
  P2_DATE12M              DATE,
  P2_HT4M                 VARCHAR2(5),
  P2_WT4M                 VARCHAR2(5),
  P2_HC4M                 VARCHAR2(5),
  P2_HT6M                 VARCHAR2(5),
  P2_WT6M                 VARCHAR2(5),
  P2_HC6M                 VARCHAR2(5),
  P2_HT9M                 VARCHAR2(5),
  P2_WT9M                 VARCHAR2(5),
  P2_HC9M                 VARCHAR2(5),
  P2_HT12M                VARCHAR2(5),
  P2_WT12M                VARCHAR2(5),
  P2_HC12M                VARCHAR2(5),
  P2_PCONCERN4M           VARCHAR2(4000),
  P2_PCONCERN6M           VARCHAR2(4000),
  P2_PCONCERN9M           VARCHAR2(4000),
  P2_PCONCERN12M          VARCHAR2(4000),
  P2_BREASTFEEDING4M      NUMBER(1),
  P2_FORMULAFEEDING4M     NUMBER(1),
  P2_CEREAL4M             NUMBER(1),
  P2_NUTRITION4M          VARCHAR2(250),
  P2_BREASTFEEDING6M      NUMBER(1),
  P2_FORMULAFEEDING6M     NUMBER(1),
  P2_BOTTLE6M             NUMBER(1),
  P2_VEGFRUIT6M           NUMBER(1),
  P2_EGG6M                NUMBER(1),
  P2_CHOKING6M            NUMBER(1),
  P2_NUTRITION6M          VARCHAR2(250),
  P2_BREASTFEEDING9M      NUMBER(1),
  P2_FORMULAFEEDING9M     NUMBER(1),
  P2_BOTTLE9M             NUMBER(1),
  P2_MEAT9M               NUMBER(1),
  P2_MILK9M               NUMBER(1),
  P2_EGG9M                NUMBER(1),
  P2_CHOKING9M            NUMBER(1),
  P2_NUTRITION9M          VARCHAR2(250),
  P2_MILK12M              NUMBER(1),
  P2_BOTTLE12M            NUMBER(1),
  P2_APPETITE12M          NUMBER(1),
  P2_NUTRITION12M         VARCHAR2(250),
  P2_CARSEAT4M            NUMBER(1),
  P2_STAIRS4M             NUMBER(1),
  P2_BATH4M               NUMBER(1),
  P2_SLEEPING4M           NUMBER(1),
  P2_PARENT4M             NUMBER(1),
  P2_CHILDCARE4M          NUMBER(1),
  P2_FAMILY4M             NUMBER(1),
  P2_TEETHING4M           NUMBER(1),
  P2_EDUCATIONADVICE4M    VARCHAR2(250),
  P2_POISON6M             NUMBER(1),
  P2_ELECTRIC6M           NUMBER(1),
  P2_SLEEPING6M           NUMBER(1),
  P2_PARENT6M             NUMBER(1),
  P2_CHILDCARE6M          NUMBER(1),
  P2_EDUCATIONADVICE6M    VARCHAR2(250),
  P2_CHILDPROOF9M         NUMBER(1),
  P2_SEPARATION9M         NUMBER(1),
  P2_SLEEPING9M           NUMBER(1),
  P2_DAYCARE9M            NUMBER(1),
  P2_HOMEVISIT9M          NUMBER(1),
  P2_SMOKE9M              NUMBER(1),
  P2_EDUCATIONADVICE9M    VARCHAR2(250),
  P2_POISON12M            NUMBER(1),
  P2_ELECTRIC12M          NUMBER(1),
  P2_CARBON12M            NUMBER(1),
  P2_HOTWATER12M          NUMBER(1),
  P2_SLEEPING12M          NUMBER(1),
  P2_PARENT12M            NUMBER(1),
  P2_TEETHING12M          NUMBER(1),
  P2_EDUCATIONADVICE12M   VARCHAR2(250),
  P2_TURNHEAD4M           NUMBER(1),
  P2_LAUGH4M              NUMBER(1),
  P2_HEADSTEADY4M         NUMBER(1),
  P2_GRASP4M              NUMBER(1),
  P2_CONCERN4M            NUMBER(1),
  P2_DEVELOPMENT4M        VARCHAR2(250),
  P2_FOLLOW6M             NUMBER(1),
  P2_RESPOND6M            NUMBER(1),
  P2_BABBLES6M            NUMBER(1),
  P2_ROLLS6M              NUMBER(1),
  P2_SITS6M               NUMBER(1),
  P2_MOUTH6M              NUMBER(1),
  P2_CONCERN6M            NUMBER(1),
  P2_DEVELOPMENT6M        VARCHAR2(250),
  P2_LOOKS9M              NUMBER(1),
  P2_BABBLES9M            NUMBER(1),
  P2_SITS9M               NUMBER(1),
  P2_STANDS9M             NUMBER(1),
  P2_OPPOSES9M            NUMBER(1),
  P2_REACHES9M            NUMBER(1),
  P2_NOPARENTSCONCERNS9M  NUMBER(1),
  P2_DEVELOPMENT9M        VARCHAR2(250),
  P2_UNDERSTANDS12M       NUMBER(1),
  P2_CHATTERS12M          NUMBER(1),
  P2_CRAWLS12M            NUMBER(1),
  P2_PULLS12M             NUMBER(1),
  P2_EMOTIONS12M          NUMBER(1),
  P2_NOPARENTCONCERNS12M  NUMBER(1),
  P2_DEVELOPMENT12M       VARCHAR2(250),
  P2_EYES4M               NUMBER(1),
  P2_COVER4M              NUMBER(1),
  P2_HEARING4M            NUMBER(1),
  P2_BABBLING4M           NUMBER(1),
  P2_HIPS4M               NUMBER(1),
  P2_PHYSICAL4M           VARCHAR2(250),
  P2_FONTANELLES6M        NUMBER(1),
  P2_EYES6M               NUMBER(1),
  P2_COVER6M              NUMBER(1),
  P2_HEARING6M            NUMBER(1),
  P2_HIPS6M               NUMBER(1),
  P2_PHYSICAL6M           VARCHAR2(250),
  P2_EYES9M               NUMBER(1),
  P2_COVER9M              NUMBER(1),
  P2_HEARING9M            NUMBER(1),
  P2_PHYSICAL9M           VARCHAR2(250),
  P2_EYES12M              NUMBER(1),
  P2_COVER12M             NUMBER(1),
  P2_HEARING12M           NUMBER(1),
  P2_HIPS12M              NUMBER(1),
  P2_PHYSICAL12M          VARCHAR2(250),
  P2_PROBLEMS4M           VARCHAR2(250),
  P2_TB6M                 NUMBER(1),
  P2_PROBLEMS6M           VARCHAR2(250),
  P2_ANTIHBS9M            NUMBER(1),
  P2_HGB9M                NUMBER(1),
  P2_PROBLEMS9M           VARCHAR2(250),
  P2_HGB12M               NUMBER(1),
  P2_SERUM12M             NUMBER(1),
  P2_PROBLEMS12M          VARCHAR2(250),
  P2_HIB4M                NUMBER(1),
  P2_POLIO4M              NUMBER(1),
  P2_IMMUNIZATION4M       VARCHAR2(250),
  P2_HIB6M                NUMBER(1),
  P2_POLIO6M              NUMBER(1),
  P2_HEPB6M               NUMBER(1),
  P2_IMMUNIZATION6M       VARCHAR2(250),
  P2_TBSKIN9M             NUMBER(1),
  P2_IMMUNIZATION9M       VARCHAR2(250),
  P2_MMR12M               NUMBER(1),
  P2_VARICELLA12M         NUMBER(1),
  P2_IMMUNIZATION12M      VARCHAR2(250),
  P2_SIGNATURE4M          VARCHAR2(250),
  P2_SIGNATURE6M          VARCHAR2(250),
  P2_SIGNATURE9M          VARCHAR2(250),
  P2_SIGNATURE12M         VARCHAR2(250),
  P3_DATE18M              DATE,
  P3_DATE2Y               DATE,
  P3_DATE4Y               DATE,
  P3_HT18M                VARCHAR2(5),
  P3_WT18M                VARCHAR2(5),
  P3_HC18M                VARCHAR2(5),
  P3_HT2Y                 VARCHAR2(5),
  P3_WT2Y                 VARCHAR2(5),
  P3_HT4Y                 VARCHAR2(5),
  P3_WT4Y                 VARCHAR2(5),
  P3_PCONCERN18M          VARCHAR2(4000),
  P3_PCONCERN2Y           VARCHAR2(4000),
  P3_PCONCERN4Y           VARCHAR2(4000),
  P3_BOTTLE18M            NUMBER(1),
  P3_NUTRITION18M         VARCHAR2(250),
  P3_MILK2Y               NUMBER(1),
  P3_FOOD2Y               NUMBER(1),
  P3_NUTRITION2Y          VARCHAR2(250),
  P3_MILK4Y               NUMBER(1),
  P3_FOOD4Y               NUMBER(1),
  P3_NUTRITION4Y          VARCHAR2(250),
  P3_BATH18M              NUMBER(1),
  P3_CHOKING18M           NUMBER(1),
  P3_TEMPERMENT18M        NUMBER(1),
  P3_LIMIT18M             NUMBER(1),
  P3_SOCIAL18M            NUMBER(1),
  P3_DENTAL18M            NUMBER(1),
  P3_TOILET18M            NUMBER(1),
  P3_EDUCATIONADVICE18M   VARCHAR2(250),
  P3_BIKE2Y               NUMBER(1),
  P3_MATCHES2Y            NUMBER(1),
  P3_CARBON2Y             NUMBER(1),
  P3_PARENT2Y             NUMBER(1),
  P3_SOCIAL2Y             NUMBER(1),
  P3_DAYCARE2Y            NUMBER(1),
  P3_DENTAL2Y             NUMBER(1),
  P3_TOILET2Y             NUMBER(1),
  P3_EDUCATIONADVICE2Y    VARCHAR2(250),
  P3_BIKE4Y               NUMBER(1),
  P3_MATCHES4Y            NUMBER(1),
  P3_CARBON4Y             NUMBER(1),
  P3_WATER4Y              NUMBER(1),
  P3_SOCIAL4Y             NUMBER(1),
  P3_DENTAL4Y             NUMBER(1),
  P3_SCHOOL4Y             NUMBER(1),
  P3_EDUCATIONADVICE4Y    VARCHAR2(250),
  P3_POINTS18M            NUMBER(1),
  P3_WORDS18M             NUMBER(1),
  P3_PICKS18M             NUMBER(1),
  P3_WALKS18M             NUMBER(1),
  P3_STACKS18M            NUMBER(1),
  P3_AFFECTION18M         NUMBER(1),
  P3_SHOWPARENTS18M       NUMBER(1),
  P3_LOOKS18M             NUMBER(1),
  P3_NOPARENTSCONCERNS18M NUMBER(1),
  P3_DEVELOPMENT18M       VARCHAR2(250),
  P3_WORD2Y               NUMBER(1),
  P3_SENTENCE2Y           NUMBER(1),
  P3_RUN2Y                NUMBER(1),
  P3_CONTAINER2Y          NUMBER(1),
  P3_COPIES2Y             NUMBER(1),
  P3_SKILLS2Y             NUMBER(1),
  P3_NOPARENTSCONCERNS2Y  NUMBER(1),
  P3_DEVELOPMENT2Y        VARCHAR2(250),
  P3_UNDERSTANDS3Y        NUMBER(1),
  P3_TWISTS3Y             NUMBER(1),
  P3_TURNPAGES3Y          NUMBER(1),
  P3_SHARE3Y              NUMBER(1),
  P3_LISTENS3Y            NUMBER(1),
  P3_NOPARENTSCONCERNS3Y  NUMBER(1),
  P3_DEVELOPMENT3Y        VARCHAR2(250),
  P3_UNDERSTANDS4Y        NUMBER(1),
  P3_QUESTIONS4Y          NUMBER(1),
  P3_ONEFOOT4Y            NUMBER(1),
  P3_DRAWS4Y              NUMBER(1),
  P3_TOILET4Y             NUMBER(1),
  P3_COMFORT4Y            NUMBER(1),
  P3_NOPARENTSCONCERNS4Y  NUMBER(1),
  P3_DEVELOPMENT4Y        VARCHAR2(250),
  P3_COUNTS5Y             NUMBER(1),
  P3_SPEAKS5Y             NUMBER(1),
  P3_BALL5Y               NUMBER(1),
  P3_HOPS5Y               NUMBER(1),
  P3_SHARES5Y             NUMBER(1),
  P3_ALONE5Y              NUMBER(1),
  P3_SEPARATE5Y           NUMBER(1),
  P3_NOPARENTSCONCERNS5Y  NUMBER(1),
  P3_DEVELOPMENT5Y        VARCHAR2(250),
  P3_EYES18M              NUMBER(1),
  P3_COVER18M             NUMBER(1),
  P3_HEARING18M           NUMBER(1),
  P3_PHYSICAL18M          VARCHAR2(250),
  P3_VISUAL2Y             NUMBER(1),
  P3_COVER2Y              NUMBER(1),
  P3_HEARING2Y            NUMBER(1),
  P3_PHYSICAL2Y           VARCHAR2(250),
  P3_VISUAL4Y             NUMBER(1),
  P3_COVER4Y              NUMBER(1),
  P3_HEARING4Y            NUMBER(1),
  P3_BLOOD4Y              NUMBER(1),
  P3_PHYSICAL4Y           VARCHAR2(250),
  P3_PROBLEMS18M          VARCHAR2(250),
  P3_SERUM2Y              NUMBER(1),
  P3_PROBLEMS2Y           VARCHAR2(250),
  P3_PROBLEMS4Y           VARCHAR2(250),
  P3_HIB18M               NUMBER(1),
  P3_POLIO18M             NUMBER(1),
  P3_IMMUNIZATION18M      VARCHAR2(250),
  P3_IMMUNIZATION2Y       VARCHAR2(250),
  P3_MMR4Y                NUMBER(1),
  P3_POLIO4Y              NUMBER(1),
  P3_IMMUNIZATION4Y       VARCHAR2(250),
  P3_SIGNATURE18M         VARCHAR2(250),
  P3_SIGNATURE2Y          VARCHAR2(50),
  P3_SIGNATURE4Y          VARCHAR2(50)
)
;
alter table FORMROURKE
  add primary key (ID);

prompt
prompt Creating table FORMROURKE2006
prompt =============================
prompt
create table FORMROURKE2006
(
  ID                        NUMBER(10) not null,
  DEMOGRAPHIC_NO            NUMBER(10),
  PROVIDER_NO               NUMBER(10),
  FORMCREATED               DATE,
  FORMEDITED                TIMESTAMP(6) not null,
  C_LASTVISITED             CHAR(3),
  C_BIRTHREMARKS            CLOB,
  C_RISKFACTORS             CLOB,
  C_FAMHISTORY              CLOB,
  C_PNAME                   VARCHAR2(60),
  C_BIRTHDATE               DATE,
  C_LENGTH                  VARCHAR2(6),
  C_HEADCIRC                VARCHAR2(6),
  C_BIRTHWEIGHT             VARCHAR2(7),
  C_DISCHARGEWEIGHT         VARCHAR2(7),
  P1_DATE1W                 DATE,
  P1_DATE2W                 DATE,
  P1_DATE1M                 DATE,
  P1_HT1W                   VARCHAR2(5),
  P1_WT1W                   VARCHAR2(5),
  P1_HC1W                   VARCHAR2(5),
  P1_HT2W                   VARCHAR2(5),
  P1_WT2W                   VARCHAR2(5),
  P1_HC2W                   VARCHAR2(5),
  P1_HT1M                   VARCHAR2(5),
  P1_WT1M                   VARCHAR2(5),
  P1_HC1M                   VARCHAR2(5),
  P1_PCONCERN1W             VARCHAR2(4000),
  P1_PCONCERN2W             VARCHAR2(4000),
  P1_PCONCERN1M             VARCHAR2(4000),
  P1_BREASTFEEDING1W        NUMBER(1),
  P1_FORMULAFEEDING1W       NUMBER(1),
  P1_STOOLURINE1W           NUMBER(1),
  P1_NUTRITION1W            VARCHAR2(250),
  P1_BREASTFEEDING2W        NUMBER(1),
  P1_FORMULAFEEDING2W       NUMBER(1),
  P1_STOOLURINE2W           NUMBER(1),
  P1_NUTRITION2W            VARCHAR2(250),
  P1_BREASTFEEDING1M        NUMBER(1),
  P1_FORMULAFEEDING1M       NUMBER(1),
  P1_STOOLURINE1M           NUMBER(1),
  P1_NUTRITION1M            VARCHAR2(250),
  P1_CARSEATOK              NUMBER(1),
  P1_CARSEATNO              NUMBER(1),
  P1_SLEEPPOSOK             NUMBER(1),
  P1_SLEEPPOSNO             NUMBER(1),
  P1_CRIBSAFETYOK           NUMBER(1),
  P1_CRIBSAFETYNO           NUMBER(1),
  P1_FIREARMSAFETYOK        NUMBER(1),
  P1_FIREARMSAFETYNO        NUMBER(1),
  P1_SMOKESAFETYOK          NUMBER(1),
  P1_SMOKESAFETYNO          NUMBER(1),
  P1_HOTWATEROK             NUMBER(1),
  P1_HOTWATERNO             NUMBER(1),
  P1_SAFETOYSOK             NUMBER(1),
  P1_SAFETOYSNO             NUMBER(1),
  P1_SLEEPCRYOK             NUMBER(1),
  P1_SLEEPCRYNO             NUMBER(1),
  P1_SOOTHABILITYOK         NUMBER(1),
  P1_SOOTHABILITYNO         NUMBER(1),
  P1_HOMEVISITOK            NUMBER(1),
  P1_HOMEVISITNO            NUMBER(1),
  P1_BONDINGOK              NUMBER(1),
  P1_BONDINGNO              NUMBER(1),
  P1_PFATIGUEOK             NUMBER(1),
  P1_PFATIGUENO             NUMBER(1),
  P1_FAMCONFLICTOK          NUMBER(1),
  P1_FAMCONFLICTNO          NUMBER(1),
  P1_SIBLINGSOK             NUMBER(1),
  P1_SIBLINGSNO             NUMBER(1),
  P1_2NDSMOKEOK             NUMBER(1),
  P1_2NDSMOKENO             NUMBER(1),
  P1_ALTMEDOK               NUMBER(1),
  P1_ALTMEDNO               NUMBER(1),
  P1_PACIFIEROK             NUMBER(1),
  P1_PACIFIERNO             NUMBER(1),
  P1_FEVEROK                NUMBER(1),
  P1_FEVERNO                NUMBER(1),
  P1_TMPCONTROLOK           NUMBER(1),
  P1_TMPCONTROLNO           NUMBER(1),
  P1_SUNEXPOSUREOK          NUMBER(1),
  P1_SUNEXPOSURENO          NUMBER(1),
  P1_DEVELOPMENT1W          VARCHAR2(4000),
  P1_DEVELOPMENT2W          VARCHAR2(4000),
  P1_DEVELOPMENT1M          VARCHAR2(4000),
  P1_FOCUSGAZE1MOK          NUMBER(1),
  P1_FOCUSGAZE1MNO          NUMBER(1),
  P1_STARTLES1MOK           NUMBER(1),
  P1_STARTLES1MNO           NUMBER(1),
  P1_SUCKS1MOK              NUMBER(1),
  P1_SUCKS1MNO              NUMBER(1),
  P1_NOPARENTSCONCERNS1MOK  NUMBER(1),
  P1_NOPARENTSCONCERNS1MNO  NUMBER(1),
  P1_SKIN1W                 NUMBER(1),
  P1_FONTANELLES1W          NUMBER(1),
  P1_EYES1W                 NUMBER(1),
  P1_EARS1W                 NUMBER(1),
  P1_HEARTLUNGS1W           NUMBER(1),
  P1_UMBILICUS1W            NUMBER(1),
  P1_FEMORALPULSES1W        NUMBER(1),
  P1_HIPS1W                 NUMBER(1),
  P1_MUSCLETONE1W           NUMBER(1),
  P1_TESTICLES1W            NUMBER(1),
  P1_MALEURINARY1W          NUMBER(1),
  P1_SKIN2W                 NUMBER(1),
  P1_FONTANELLES2W          NUMBER(1),
  P1_EYES2W                 NUMBER(1),
  P1_EARS2W                 NUMBER(1),
  P1_HEARTLUNGS2W           NUMBER(1),
  P1_UMBILICUS2W            NUMBER(1),
  P1_FEMORALPULSES2W        NUMBER(1),
  P1_HIPS2W                 NUMBER(1),
  P1_MUSCLETONE2W           NUMBER(1),
  P1_TESTICLES2W            NUMBER(1),
  P1_MALEURINARY2W          NUMBER(1),
  P1_FONTANELLES1M          NUMBER(1),
  P1_EYES1M                 NUMBER(1),
  P1_CORNEAL1M              NUMBER(1),
  P1_HEARING1M              NUMBER(1),
  P1_HEART1M                NUMBER(1),
  P1_HIPS1M                 NUMBER(1),
  P1_MUSCLETONE1M           NUMBER(1),
  P1_PKUTHYROID1W           NUMBER(1),
  P1_HEMOSCREEN1W           NUMBER(1),
  P1_PROBLEMS1W             VARCHAR2(4000),
  P1_PROBLEMS2W             VARCHAR2(4000),
  P1_PROBLEMS1M             VARCHAR2(4000),
  P1_HEPATITISVACCINE1W     NUMBER(1),
  P1_HEPATITISVACCINE1M     NUMBER(1),
  P1_SIGNATURE2W            VARCHAR2(250),
  P2_DATE2M                 DATE,
  P2_DATE4M                 DATE,
  P2_DATE6M                 DATE,
  P2_HT2M                   VARCHAR2(5),
  P2_WT2M                   VARCHAR2(5),
  P2_HC2M                   VARCHAR2(5),
  P2_HT4M                   VARCHAR2(5),
  P2_WT4M                   VARCHAR2(5),
  P2_HC4M                   VARCHAR2(5),
  P2_HT6M                   VARCHAR2(5),
  P2_WT6M                   VARCHAR2(5),
  P2_HC6M                   VARCHAR2(5),
  P2_PCONCERN2M             VARCHAR2(4000),
  P2_PCONCERN4M             VARCHAR2(4000),
  P2_PCONCERN6M             VARCHAR2(4000),
  P2_NUTRITION2M            VARCHAR2(4000),
  P2_BREASTFEEDING2M        NUMBER(1),
  P2_FORMULAFEEDING2M       NUMBER(1),
  P2_NUTRITION4M            VARCHAR2(4000),
  P2_BREASTFEEDING4M        NUMBER(1),
  P2_FORMULAFEEDING4M       NUMBER(1),
  P2_BREASTFEEDING6M        NUMBER(1),
  P2_FORMULAFEEDING6M       NUMBER(1),
  P2_BOTTLE6M               NUMBER(1),
  P2_LIQUIDS6M              NUMBER(1),
  P2_IRON6M                 NUMBER(1),
  P2_VEGFRUIT6M             NUMBER(1),
  P2_EGG6M                  NUMBER(1),
  P2_CHOKING6M              NUMBER(1),
  P2_CARSEATOK              NUMBER(1),
  P2_CARSEATNO              NUMBER(1),
  P2_SLEEPPOSOK             NUMBER(1),
  P2_SLEEPPOSNO             NUMBER(1),
  P2_POISONSOK              NUMBER(1),
  P2_POISONSNO              NUMBER(1),
  P2_FIREARMSAFETYOK        NUMBER(1),
  P2_FIREARMSAFETYNO        NUMBER(1),
  P2_ELECTRICOK             NUMBER(1),
  P2_ELECTRICNO             NUMBER(1),
  P2_SMOKESAFETYOK          NUMBER(1),
  P2_SMOKESAFETYNO          NUMBER(1),
  P2_HOTWATEROK             NUMBER(1),
  P2_HOTWATERNO             NUMBER(1),
  P2_FALLSOK                NUMBER(1),
  P2_FALLSNO                NUMBER(1),
  P2_SAFETOYSOK             NUMBER(1),
  P2_SAFETOYSNO             NUMBER(1),
  P2_SLEEPCRYOK             NUMBER(1),
  P2_SLEEPCRYNO             NUMBER(1),
  P2_SOOTHABILITYOK         NUMBER(1),
  P2_SOOTHABILITYNO         NUMBER(1),
  P2_HOMEVISITOK            NUMBER(1),
  P2_HOMEVISITNO            NUMBER(1),
  P2_BONDINGOK              NUMBER(1),
  P2_BONDINGNO              NUMBER(1),
  P2_PFATIGUEOK             NUMBER(1),
  P2_PFATIGUENO             NUMBER(1),
  P2_FAMCONFLICTOK          NUMBER(1),
  P2_FAMCONFLICTNO          NUMBER(1),
  P2_SIBLINGSOK             NUMBER(1),
  P2_SIBLINGSNO             NUMBER(1),
  P2_CHILDCAREOK            NUMBER(1),
  P2_CHILDCARENO            NUMBER(1),
  P2_2NDSMOKEOK             NUMBER(1),
  P2_2NDSMOKENO             NUMBER(1),
  P2_TEETHINGOK             NUMBER(1),
  P2_TEETHINGNO             NUMBER(1),
  P2_ALTMEDOK               NUMBER(1),
  P2_ALTMEDNO               NUMBER(1),
  P2_PACIFIEROK             NUMBER(1),
  P2_PACIFIERNO             NUMBER(1),
  P2_TMPCONTROLOK           NUMBER(1),
  P2_TMPCONTROLNO           NUMBER(1),
  P2_FEVEROK                NUMBER(1),
  P2_FEVERNO                NUMBER(1),
  P2_SUNEXPOSUREOK          NUMBER(1),
  P2_SUNEXPOSURENO          NUMBER(1),
  P2_PESTICIDESOK           NUMBER(1),
  P2_PESTICIDESNO           NUMBER(1),
  P2_DEVELOPMENT2M          VARCHAR2(4000),
  P2_EYESOK                 NUMBER(1),
  P2_EYESNO                 NUMBER(1),
  P2_SOUNDSOK               NUMBER(1),
  P2_SOUNDSNO               NUMBER(1),
  P2_HEADUPOK               NUMBER(1),
  P2_HEADUPNO               NUMBER(1),
  P2_CUDDLEDOK              NUMBER(1),
  P2_CUDDLEDNO              NUMBER(1),
  P2_SMILESOK               NUMBER(1),
  P2_SMILESNO               NUMBER(1),
  P2_NOPARENTSCONCERNS2MOK  NUMBER(1),
  P2_NOPARENTSCONCERNS2MNO  NUMBER(1),
  P2_DEVELOPMENT4M          VARCHAR2(4000),
  P2_TURNSHEADOK            NUMBER(1),
  P2_TURNSHEADNO            NUMBER(1),
  P2_LAUGHSOK               NUMBER(1),
  P2_LAUGHSNO               NUMBER(1),
  P2_HEADSTEADYOK           NUMBER(1),
  P2_HEADSTEADYNO           NUMBER(1),
  P2_GRASPOK                NUMBER(1),
  P2_GRASPNO                NUMBER(1),
  P2_NOPARENTSCONCERNS4MOK  NUMBER(1),
  P2_NOPARENTSCONCERNS4MNO  NUMBER(1),
  P2_DEVELOPMENT6M          VARCHAR2(4000),
  P2_MOVINGOBJOK            NUMBER(1),
  P2_MOVINGOBJNO            NUMBER(1),
  P2_LOOKSOK                NUMBER(1),
  P2_LOOKSNO                NUMBER(1),
  P2_BABBLESOK              NUMBER(1),
  P2_BABBLESNO              NUMBER(1),
  P2_ROLLSOK                NUMBER(1),
  P2_ROLLSNO                NUMBER(1),
  P2_SITSOK                 NUMBER(1),
  P2_SITSNO                 NUMBER(1),
  P2_HANDTOMOUTHOK          NUMBER(1),
  P2_HANDTOMOUTHNO          NUMBER(1),
  P2_NOPARENTSCONCERNS6MOK  NUMBER(1),
  P2_NOPARENTSCONCERNS6MNO  NUMBER(1),
  P2_FONTANELLES2M          NUMBER(1),
  P2_EYES2M                 NUMBER(1),
  P2_CORNEAL2M              NUMBER(1),
  P2_HEARING2M              NUMBER(1),
  P2_HEART2M                NUMBER(1),
  P2_HIPS2M                 NUMBER(1),
  P2_MUSCLETONE2M           NUMBER(1),
  P2_EYES4M                 NUMBER(1),
  P2_CORNEAL4M              NUMBER(1),
  P2_HEARING4M              NUMBER(1),
  P2_HIPS4M                 NUMBER(1),
  P2_MUSCLETONE4M           NUMBER(1),
  P2_FONTANELLES6M          NUMBER(1),
  P2_EYES6M                 NUMBER(1),
  P2_CORNEAL6M              NUMBER(1),
  P2_HEARING6M              NUMBER(1),
  P2_HIPS6M                 NUMBER(1),
  P2_MUSCLETONE6M           NUMBER(1),
  P2_PROBLEMS2M             VARCHAR2(4000),
  P2_PROBLEMS4M             VARCHAR2(4000),
  P2_PROBLEMS6M             VARCHAR2(4000),
  P2_TB6M                   NUMBER(1),
  P2_HEPATITISVACCINE6M     NUMBER(1),
  P2_SIGNATURE2M            VARCHAR2(250),
  P2_SIGNATURE4M            VARCHAR2(250),
  P3_DATE9M                 DATE,
  P3_DATE12M                DATE,
  P3_DATE15M                DATE,
  P3_HT9M                   VARCHAR2(5),
  P3_WT9M                   VARCHAR2(5),
  P3_HC9M                   VARCHAR2(5),
  P3_HT12M                  VARCHAR2(5),
  P3_WT12M                  VARCHAR2(5),
  P3_HC12M                  VARCHAR2(5),
  P3_HT15M                  VARCHAR2(5),
  P3_WT15M                  VARCHAR2(5),
  P3_HC15M                  VARCHAR2(5),
  P3_PCONCERN9M             VARCHAR2(4000),
  P3_PCONCERN12M            VARCHAR2(4000),
  P3_PCONCERN15M            VARCHAR2(4000),
  P3_BREASTFEEDING9M        NUMBER(1),
  P3_FORMULAFEEDING9M       NUMBER(1),
  P3_BOTTLE9M               NUMBER(1),
  P3_LIQUIDS9M              NUMBER(1),
  P3_CEREAL9M               NUMBER(1),
  P3_INTROCOWMILK9M         NUMBER(1),
  P3_EGG9M                  NUMBER(1),
  P3_CHOKING9M              NUMBER(1),
  P3_NUTRITION12M           VARCHAR2(4000),
  P3_BREASTFEEDING12M       NUMBER(1),
  P3_HOMOMILK12M            NUMBER(1),
  P3_CUP12M                 NUMBER(1),
  P3_APPETITE12M            NUMBER(1),
  P3_CHOKING12M             NUMBER(1),
  P3_NUTRITION15M           VARCHAR2(4000),
  P3_BREASTFEEDING15M       NUMBER(1),
  P3_HOMOMILK15M            NUMBER(1),
  P3_CHOKING15M             NUMBER(1),
  P3_CUP15M                 NUMBER(1),
  P3_CARSEATOK              NUMBER(1),
  P3_CARSEATNO              NUMBER(1),
  P3_POISONSOK              NUMBER(1),
  P3_POISONSNO              NUMBER(1),
  P3_FIREARMSAFETYOK        NUMBER(1),
  P3_FIREARMSAFETYNO        NUMBER(1),
  P3_SMOKESAFETYOK          NUMBER(1),
  P3_SMOKESAFETYNO          NUMBER(1),
  P3_HOTWATEROK             NUMBER(1),
  P3_HOTWATERNO             NUMBER(1),
  P3_ELECTRICOK             NUMBER(1),
  P3_ELECTRICNO             NUMBER(1),
  P3_FALLSOK                NUMBER(1),
  P3_FALLSNO                NUMBER(1),
  P3_SAFETOYSOK             NUMBER(1),
  P3_SAFETOYSNO             NUMBER(1),
  P3_SLEEPCRYOK             NUMBER(1),
  P3_SLEEPCRYNO             NUMBER(1),
  P3_SOOTHABILITYOK         NUMBER(1),
  P3_SOOTHABILITYNO         NUMBER(1),
  P3_HOMEVISITOK            NUMBER(1),
  P3_HOMEVISITNO            NUMBER(1),
  P3_PARENTINGOK            NUMBER(1),
  P3_PARENTINGNO            NUMBER(1),
  P3_PFATIGUEOK             NUMBER(1),
  P3_PFATIGUENO             NUMBER(1),
  P3_FAMCONFLICTOK          NUMBER(1),
  P3_FAMCONFLICTNO          NUMBER(1),
  P3_SIBLINGSOK             NUMBER(1),
  P3_SIBLINGSNO             NUMBER(1),
  P3_CHILDCAREOK            NUMBER(1),
  P3_CHILDCARENO            NUMBER(1),
  P3_2NDSMOKEOK             NUMBER(1),
  P3_2NDSMOKENO             NUMBER(1),
  P3_TEETHINGOK             NUMBER(1),
  P3_TEETHINGNO             NUMBER(1),
  P3_ALTMEDOK               NUMBER(1),
  P3_ALTMEDNO               NUMBER(1),
  P3_PACIFIEROK             NUMBER(1),
  P3_PACIFIERNO             NUMBER(1),
  P3_FEVEROK                NUMBER(1),
  P3_FEVERNO                NUMBER(1),
  P3_ACTIVEOK               NUMBER(1),
  P3_ACTIVENO               NUMBER(1),
  P3_READINGOK              NUMBER(1),
  P3_READINGNO              NUMBER(1),
  P3_FOOTWEAROK             NUMBER(1),
  P3_FOOTWEARNO             NUMBER(1),
  P3_SUNEXPOSUREOK          NUMBER(1),
  P3_SUNEXPOSURENO          NUMBER(1),
  P3_CHECKSERUMOK           NUMBER(1),
  P3_CHECKSERUMNO           NUMBER(1),
  P3_PESTICIDESOK           NUMBER(1),
  P3_PESTICIDESNO           NUMBER(1),
  P3_DEVELOPMENT9M          VARCHAR2(4000),
  P3_HIDDENTOYOK            NUMBER(1),
  P3_HIDDENTOYNO            NUMBER(1),
  P3_SOUNDSOK               NUMBER(1),
  P3_SOUNDSNO               NUMBER(1),
  P3_MAKESOUNDSOK           NUMBER(1),
  P3_MAKESOUNDSNO           NUMBER(1),
  P3_SITSOK                 NUMBER(1),
  P3_SITSNO                 NUMBER(1),
  P3_STANDSOK               NUMBER(1),
  P3_STANDSNO               NUMBER(1),
  P3_THUMBOK                NUMBER(1),
  P3_THUMBNO                NUMBER(1),
  P3_PICKEDUPOK             NUMBER(1),
  P3_PICKEDUPNO             NUMBER(1),
  P3_NOPARENTSCONCERNS9MOK  NUMBER(1),
  P3_NOPARENTSCONCERNS9MNO  NUMBER(1),
  P3_DEVELOPMENT12M         VARCHAR2(4000),
  P3_RESPONDSOK             NUMBER(1),
  P3_RESPONDSNO             NUMBER(1),
  P3_SIMPLEREQUESTSOK       NUMBER(1),
  P3_SIMPLEREQUESTSNO       NUMBER(1),
  P3_CHATTERSOK             NUMBER(1),
  P3_CHATTERSNO             NUMBER(1),
  P3_SHUFFLESOK             NUMBER(1),
  P3_SHUFFLESNO             NUMBER(1),
  P3_PULL2STANDOK           NUMBER(1),
  P3_PULL2STANDNO           NUMBER(1),
  P3_EMOTIONSOK             NUMBER(1),
  P3_EMOTIONSNO             NUMBER(1),
  P3_NOPARENTSCONCERNS12MOK NUMBER(1),
  P3_NOPARENTSCONCERNS12MNO NUMBER(1),
  P3_SAYS2WORDSOK           NUMBER(1),
  P3_SAYS2WORDSNO           NUMBER(1),
  P3_REACHESOK              NUMBER(1),
  P3_REACHESNO              NUMBER(1),
  P3_FINGERFOODSOK          NUMBER(1),
  P3_FINGERFOODSNO          NUMBER(1),
  P3_CRAWLSSTAIRSOK         NUMBER(1),
  P3_CRAWLSSTAIRSNO         NUMBER(1),
  P3_SQUATSOK               NUMBER(1),
  P3_SQUATSNO               NUMBER(1),
  P3_TIESHOESOK             NUMBER(1),
  P3_TIESHOESNO             NUMBER(1),
  P3_STACKS2BLOCKSOK        NUMBER(1),
  P3_STACKS2BLOCKSNO        NUMBER(1),
  P3_HOWTOREACTOK           NUMBER(1),
  P3_HOWTOREACTNO           NUMBER(1),
  P3_NOPARENTSCONCERNS15MOK NUMBER(1),
  P3_NOPARENTSCONCERNS15MNO NUMBER(1),
  P3_EYES9M                 NUMBER(1),
  P3_CORNEAL9M              NUMBER(1),
  P3_HEARING9M              NUMBER(1),
  P3_HIPS9M                 NUMBER(1),
  P3_EYES12M                NUMBER(1),
  P3_CORNEAL12M             NUMBER(1),
  P3_HEARING12M             NUMBER(1),
  P3_TONSIL12M              NUMBER(1),
  P3_HIPS12M                NUMBER(1),
  P3_EYES15M                NUMBER(1),
  P3_CORNEAL15M             NUMBER(1),
  P3_HEARING15M             NUMBER(1),
  P3_TONSIL15M              NUMBER(1),
  P3_HIPS15M                NUMBER(1),
  P3_PROBLEMS9M             VARCHAR2(4000),
  P3_PROBLEMS12M            VARCHAR2(4000),
  P3_PROBLEMS15M            VARCHAR2(4000),
  P3_ANTIHB9M               NUMBER(1),
  P3_HEMOGLOBIN9M           NUMBER(1),
  P3_HEMOGLOBIN12M          NUMBER(1),
  P3_SIGNATURE9M            VARCHAR2(250),
  P3_SIGNATURE12M           VARCHAR2(250),
  P3_SIGNATURE15M           VARCHAR2(250),
  P4_DATE18M                DATE,
  P4_DATE24M                DATE,
  P4_DATE48M                DATE,
  P4_HT18M                  VARCHAR2(5),
  P4_WT18M                  VARCHAR2(5),
  P4_HC18M                  VARCHAR2(5),
  P4_HT24M                  VARCHAR2(5),
  P4_WT24M                  VARCHAR2(5),
  P4_HC24M                  VARCHAR2(5),
  P4_HT48M                  VARCHAR2(5),
  P4_WT48M                  VARCHAR2(5),
  P4_PCONCERN18M            VARCHAR2(4000),
  P4_PCONCERN24M            VARCHAR2(4000),
  P4_PCONCERN48M            VARCHAR2(4000),
  P4_BREASTFEEDING18M       NUMBER(1),
  P4_HOMOMILK               NUMBER(1),
  P4_BOTTLE18M              NUMBER(1),
  P4_HOMO2PERCENT24M        NUMBER(1),
  P4_LOWERFATDIET24M        NUMBER(1),
  P4_FOODGUIDE24M           NUMBER(1),
  P4_2PMILK48M              NUMBER(1),
  P4_FOODGUIDE48M           NUMBER(1),
  P4_CARSEAT18MOK           NUMBER(1),
  P4_CARSEAT18MNO           NUMBER(1),
  P4_BATHSAFETYOK           NUMBER(1),
  P4_BATHSAFETYNO           NUMBER(1),
  P4_SAFETOYSOK             NUMBER(1),
  P4_SAFETOYSNO             NUMBER(1),
  P4_PARENTCHILD18MOK       NUMBER(1),
  P4_PARENTCHILD18MNO       NUMBER(1),
  P4_DISCIPLINE18MOK        NUMBER(1),
  P4_DISCIPLINE18MNO        NUMBER(1),
  P4_PFATIGUE18MOK          NUMBER(1),
  P4_PFATIGUE18MNO          NUMBER(1),
  P4_HIGHRISK18MOK          NUMBER(1),
  P4_HIGHRISK18MNO          NUMBER(1),
  P4_SOCIALIZING18MOK       NUMBER(1),
  P4_SOCIALIZING18MNO       NUMBER(1),
  P4_DENTALCAREOK           NUMBER(1),
  P4_DENTALCARENO           NUMBER(1),
  P4_TOILETLEARNING18MOK    NUMBER(1),
  P4_TOILETLEARNING18MNO    NUMBER(1),
  P4_CARSEAT24MOK           NUMBER(1),
  P4_CARSEAT24MNO           NUMBER(1),
  P4_BIKEHELMETSOK          NUMBER(1),
  P4_BIKEHELMETSNO          NUMBER(1),
  P4_FIREARMSAFETYOK        NUMBER(1),
  P4_FIREARMSAFETYNO        NUMBER(1),
  P4_SMOKESAFETYOK          NUMBER(1),
  P4_SMOKESAFETYNO          NUMBER(1),
  P4_MATCHESOK              NUMBER(1),
  P4_MATCHESNO              NUMBER(1),
  P4_WATERSAFETYOK          NUMBER(1),
  P4_WATERSAFETYNO          NUMBER(1),
  P4_PARENTCHILD24MOK       NUMBER(1),
  P4_PARENTCHILD24MNO       NUMBER(1),
  P4_DISCIPLINE24MOK        NUMBER(1),
  P4_DISCIPLINE24MNO        NUMBER(1),
  P4_HIGHRISK24MOK          NUMBER(1),
  P4_HIGHRISK24MNO          NUMBER(1),
  P4_PFATIGUE24MOK          NUMBER(1),
  P4_PFATIGUE24MNO          NUMBER(1),
  P4_FAMCONFLICTOK          NUMBER(1),
  P4_FAMCONFLICTNO          NUMBER(1),
  P4_SIBLINGSOK             NUMBER(1),
  P4_SIBLINGSNO             NUMBER(1),
  P4_2NDSMOKEOK             NUMBER(1),
  P4_2NDSMOKENO             NUMBER(1),
  P4_DENTALCLEANINGOK       NUMBER(1),
  P4_DENTALCLEANINGNO       NUMBER(1),
  P4_ALTMEDOK               NUMBER(1),
  P4_ALTMEDNO               NUMBER(1),
  P4_TOILETLEARNING24MOK    NUMBER(1),
  P4_TOILETLEARNING24MNO    NUMBER(1),
  P4_ACTIVEOK               NUMBER(1),
  P4_ACTIVENO               NUMBER(1),
  P4_SOCIALIZING24MOK       NUMBER(1),
  P4_SOCIALIZING24MNO       NUMBER(1),
  P4_READINGOK              NUMBER(1),
  P4_READINGNO              NUMBER(1),
  P4_DAYCAREOK              NUMBER(1),
  P4_DAYCARENO              NUMBER(1),
  P4_SUNEXPOSUREOK          NUMBER(1),
  P4_SUNEXPOSURENO          NUMBER(1),
  P4_PESTICIDESOK           NUMBER(1),
  P4_PESTICIDESNO           NUMBER(1),
  P4_CHECKSERUMOK           NUMBER(1),
  P4_CHECKSERUMNO           NUMBER(1),
  P4_MANAGEABLEOK           NUMBER(1),
  P4_MANAGEABLENO           NUMBER(1),
  P4_SOOTHABILITYOK         NUMBER(1),
  P4_SOOTHABILITYNO         NUMBER(1),
  P4_COMFORTOK              NUMBER(1),
  P4_COMFORTNO              NUMBER(1),
  P4_POINTSOK               NUMBER(1),
  P4_POINTSNO               NUMBER(1),
  P4_GETATTNOK              NUMBER(1),
  P4_GETATTNNO              NUMBER(1),
  P4_PRETENDPLAYOK          NUMBER(1),
  P4_PRETENDPLAYNO          NUMBER(1),
  P4_RECSNAMEOK             NUMBER(1),
  P4_RECSNAMENO             NUMBER(1),
  P4_INITSPEECHOK           NUMBER(1),
  P4_INITSPEECHNO           NUMBER(1),
  P4_3CONSONANTSOK          NUMBER(1),
  P4_3CONSONANTSNO          NUMBER(1),
  P4_WALKSBACKOK            NUMBER(1),
  P4_WALKSBACKNO            NUMBER(1),
  P4_FEEDSSELFOK            NUMBER(1),
  P4_FEEDSSELFNO            NUMBER(1),
  P4_REMOVESHATOK           NUMBER(1),
  P4_REMOVESHATNO           NUMBER(1),
  P4_NOPARENTSCONCERNS18MOK NUMBER(1),
  P4_NOPARENTSCONCERNS18MNO NUMBER(1),
  P4_NEWWORDSOK             NUMBER(1),
  P4_NEWWORDSNO             NUMBER(1),
  P4_2WSENTENCEOK           NUMBER(1),
  P4_2WSENTENCENO           NUMBER(1),
  P4_RUNSOK                 NUMBER(1),
  P4_RUNSNO                 NUMBER(1),
  P4_SMALLCONTAINEROK       NUMBER(1),
  P4_SMALLCONTAINERNO       NUMBER(1),
  P4_COPIESACTIONSOK        NUMBER(1),
  P4_COPIESACTIONSNO        NUMBER(1),
  P4_NEWSKILLSOK            NUMBER(1),
  P4_NEWSKILLSNO            NUMBER(1),
  P4_NOPARENTSCONCERNS24MOK NUMBER(1),
  P4_NOPARENTSCONCERNS24MNO NUMBER(1),
  P4_3DIRECTIONSOK          NUMBER(1),
  P4_3DIRECTIONSNO          NUMBER(1),
  P4_ASKSQUESTIONSOK        NUMBER(1),
  P4_ASKSQUESTIONSNO        NUMBER(1),
  P4_STANDS1FOOTOK          NUMBER(1),
  P4_STANDS1FOOTNO          NUMBER(1),
  P4_DRAWSOK                NUMBER(1),
  P4_DRAWSNO                NUMBER(1),
  P4_TOILETTRAINEDOK        NUMBER(1),
  P4_TOILETTRAINEDNO        NUMBER(1),
  P4_TRIES2COMFORTOK        NUMBER(1),
  P4_TRIES2COMFORTNO        NUMBER(1),
  P4_NOPARENTSCONCERNS48MOK NUMBER(1),
  P4_NOPARENTSCONCERNS48MNO NUMBER(1),
  P4_2DIRECTIONSOK          NUMBER(1),
  P4_2DIRECTIONSNO          NUMBER(1),
  P4_TWISTSLIDSOK           NUMBER(1),
  P4_TWISTSLIDSNO           NUMBER(1),
  P4_TURNSPAGESOK           NUMBER(1),
  P4_TURNSPAGESNO           NUMBER(1),
  P4_SHARESSOMETIMEOK       NUMBER(1),
  P4_SHARESSOMETIMENO       NUMBER(1),
  P4_LISTENMUSIKOK          NUMBER(1),
  P4_LISTENMUSIKNO          NUMBER(1),
  P4_NOPARENTSCONCERNS36MOK NUMBER(1),
  P4_NOPARENTSCONCERNS36MNO NUMBER(1),
  P4_COUNTS2TENOK           NUMBER(1),
  P4_COUNTS2TENNO           NUMBER(1),
  P4_SPEAKSCLEARLYOK        NUMBER(1),
  P4_SPEAKSCLEARLYNO        NUMBER(1),
  P4_THROWSCATCHESOK        NUMBER(1),
  P4_THROWSCATCHESNO        NUMBER(1),
  P4_HOPS1FOOTOK            NUMBER(1),
  P4_HOPS1FOOTNO            NUMBER(1),
  P4_SHARESWILLINGLYOK      NUMBER(1),
  P4_SHARESWILLINGLYNO      NUMBER(1),
  P4_WORKSALONEOK           NUMBER(1),
  P4_WORKSALONENO           NUMBER(1),
  P4_SEPARATESOK            NUMBER(1),
  P4_SEPARATESNO            NUMBER(1),
  P4_NOPARENTSCONCERNS60MOK NUMBER(1),
  P4_NOPARENTSCONCERNS60MNO NUMBER(1),
  P4_EYES18M                NUMBER(1),
  P4_CORNEAL18M             NUMBER(1),
  P4_HEARING18M             NUMBER(1),
  P4_TONSIL18M              NUMBER(1),
  P4_BLOODPRESSURE24M       NUMBER(1),
  P4_EYES24M                NUMBER(1),
  P4_CORNEAL24M             NUMBER(1),
  P4_HEARING24M             NUMBER(1),
  P4_TONSIL24M              NUMBER(1),
  P4_BLOODPRESSURE48M       NUMBER(1),
  P4_EYES48M                NUMBER(1),
  P4_CORNEAL48M             NUMBER(1),
  P4_HEARING48M             NUMBER(1),
  P4_TONSIL48M              NUMBER(1),
  P4_PROBLEMS18M            VARCHAR2(4000),
  P4_PROBLEMS24M            VARCHAR2(4000),
  P4_PROBLEMS48M            VARCHAR2(4000),
  P4_SIGNATURE18M           VARCHAR2(250),
  P4_SIGNATURE24M           VARCHAR2(250),
  P4_SIGNATURE48M           VARCHAR2(250),
  P1_SIGNATURE1W            VARCHAR2(250),
  P1_SIGNATURE1M            VARCHAR2(250),
  P2_SIGNATURE6M            VARCHAR2(250)
)
;
alter table FORMROURKE2006
  add primary key (ID);

prompt
prompt Creating table FORMSATISFACTIONSCALE
prompt ====================================
prompt
create table FORMSATISFACTIONSCALE
(
  ID                        NUMBER(10) not null,
  DEMOGRAPHIC_NO            NUMBER(10) default '0' not null,
  PROVIDER_NO               NUMBER(10),
  FORMCREATED               DATE,
  FORMEDITED                TIMESTAMP(6) not null,
  STUDYID                   VARCHAR2(20) default 'N/A' not null,
  BELIEVE1Y                 NUMBER(1),
  BELIEVE1N                 NUMBER(1),
  RECEIVE2Y                 NUMBER(1),
  RECEIVE2N                 NUMBER(1),
  RECEIVEOT3Y               NUMBER(1),
  RECEIVEP3Y                NUMBER(1),
  RECEIVEB3Y                NUMBER(1),
  OTTREATS4                 VARCHAR2(4),
  PTTREATS4                 VARCHAR2(4),
  EXPLAINING1               VARCHAR2(2),
  EVERYTHINGNEEDED2         VARCHAR2(2),
  PERFECT3                  VARCHAR2(2),
  WONDER4                   VARCHAR2(2),
  CONFIDENT5                VARCHAR2(2),
  CAREFUL6                  VARCHAR2(2),
  AFFORD7                   VARCHAR2(2),
  EASYACCESS8               VARCHAR2(2),
  TOOLONG9                  VARCHAR2(2),
  BUSINESSLIKE10            VARCHAR2(2),
  VERYFRIENDLY11            VARCHAR2(2),
  HURRYTOOMUCH12            VARCHAR2(2),
  IGNORE13                  VARCHAR2(2),
  DOUBTABILITY14            VARCHAR2(2),
  PLENTYOFTIME15            VARCHAR2(2),
  HARDTOGETANAPPOINTMENT16  VARCHAR2(2),
  DISSATISFIED17            VARCHAR2(2),
  ABLETOGETREHABILITATION18 VARCHAR2(2)
)
;
alter table FORMSATISFACTIONSCALE
  add primary key (ID);

prompt
prompt Creating table FORMSELFADMINISTERED
prompt ===================================
prompt
create table FORMSELFADMINISTERED
(
  ID                    NUMBER(10) not null,
  DEMOGRAPHIC_NO        NUMBER(10) not null,
  PROVIDER_NO           NUMBER(10),
  FORMCREATED           DATE,
  FORMEDITED            TIMESTAMP(6) not null,
  STUDYID               VARCHAR2(20) default 'N/A' not null,
  SEX                   VARCHAR2(1),
  DOB                   DATE,
  HEALTHEX              NUMBER(1),
  HEALTHVG              NUMBER(1),
  HEALTHG               NUMBER(1),
  HEALTHF               NUMBER(1),
  HEALTHP               NUMBER(1),
  STAYINHOSPNO          NUMBER(1),
  STAYINHOSP1           NUMBER(1),
  STAYINHOSP2OR3        NUMBER(1),
  STAYINHOSPMORE3       NUMBER(1),
  VISITPHYNO            NUMBER(1),
  VISITPHY1             NUMBER(1),
  VISITPHY2OR3          NUMBER(1),
  VISITPHYMORE3         NUMBER(1),
  DIABETESY             NUMBER(1),
  DIABETESN             NUMBER(1),
  HEARTDISEASEY         NUMBER(1),
  HEARTDISEASEN         NUMBER(1),
  ANGINAPECTORISY       NUMBER(1),
  ANGINAPECTORISN       NUMBER(1),
  MYOCARDIALINFARCTIONY NUMBER(1),
  MYOCARDIALINFARCTIONN NUMBER(1),
  ANYHEARTATTACKY       NUMBER(1),
  ANYHEARTATTACKN       NUMBER(1),
  RELATIVETAKECAREY     NUMBER(1),
  RELATIVETAKECAREN     NUMBER(1)
)
;
alter table FORMSELFADMINISTERED
  add primary key (ID);

prompt
prompt Creating table FORMSELFEFFICACY
prompt ===============================
prompt
create table FORMSELFEFFICACY
(
  ID                  NUMBER(10) not null,
  DEMOGRAPHIC_NO      NUMBER(10) not null,
  PROVIDER_NO         NUMBER(10),
  FORMCREATED         DATE,
  FORMEDITED          TIMESTAMP(6) not null,
  STUDYID             VARCHAR2(20) default 'N/A' not null,
  EX1                 VARCHAR2(2),
  EX2                 VARCHAR2(2),
  EX3                 VARCHAR2(2),
  EXERSCORE           VARCHAR2(5),
  DISEASE1            VARCHAR2(2),
  DISEASESCORE        VARCHAR2(5),
  HELP1               VARCHAR2(2),
  HELP2               VARCHAR2(2),
  HELP3               VARCHAR2(2),
  HELP4               VARCHAR2(2),
  HELPSCORE           VARCHAR2(5),
  COMMUNICATEWITHPHY1 VARCHAR2(2),
  COMMUNICATEWITHPHY2 VARCHAR2(2),
  COMMUNICATEWITHPHY3 VARCHAR2(2),
  COMMSCORE           VARCHAR2(5),
  MANAGEDISEASE1      VARCHAR2(2),
  MANAGEDISEASE2      VARCHAR2(2),
  MANAGEDISEASE3      VARCHAR2(2),
  MANAGEDISEASE4      VARCHAR2(2),
  MANAGEDISEASE5      VARCHAR2(2),
  MANDISEASESCORE     VARCHAR2(5),
  DOCHORE1            VARCHAR2(2),
  DOCHORE2            VARCHAR2(2),
  DOCHORE3            VARCHAR2(2),
  CHORESSCORE         VARCHAR2(5),
  SOCIAL1             VARCHAR2(2),
  SOCIAL2             VARCHAR2(2),
  SOCIALSCORE         VARCHAR2(5),
  SHORTBREATH1        VARCHAR2(2),
  BREATHSCORE         VARCHAR2(5),
  MANAGESYMPTOMS1     VARCHAR2(2),
  MANAGESYMPTOMS2     VARCHAR2(2),
  MANAGESYMPTOMS3     VARCHAR2(2),
  MANAGESYMPTOMS4     VARCHAR2(2),
  MANAGESYMPTOMS5     VARCHAR2(2),
  MANSYMSCORE         VARCHAR2(5),
  CONTROLDEPRESS1     VARCHAR2(2),
  CONTROLDEPRESS2     VARCHAR2(2),
  CONTROLDEPRESS3     VARCHAR2(2),
  CONTROLDEPRESS4     VARCHAR2(2),
  CONTROLDEPRESS5     VARCHAR2(2),
  CONTROLDEPRESS6     VARCHAR2(2),
  MANDPRSCORE         VARCHAR2(5)
)
;
alter table FORMSELFEFFICACY
  add primary key (ID);

prompt
prompt Creating table FORMSELFMANAGEMENT
prompt =================================
prompt
create table FORMSELFMANAGEMENT
(
  ID                     NUMBER(10) not null,
  DEMOGRAPHIC_NO         NUMBER(10) not null,
  PROVIDER_NO            NUMBER(10),
  FORMCREATED            DATE,
  FORMEDITED             TIMESTAMP(6) not null,
  STUDYID                VARCHAR2(20) default 'N/A' not null,
  EX1                    NUMBER(1),
  EX2                    NUMBER(1),
  EX3                    NUMBER(1),
  EX4                    NUMBER(1),
  EX5                    NUMBER(1),
  EX6SPEC                VARCHAR2(255),
  EX6                    NUMBER(1),
  EXTIME1                NUMBER(3),
  EXTIME2TO6             NUMBER(3),
  COG1                   VARCHAR2(1),
  COG2                   VARCHAR2(1),
  COG3                   VARCHAR2(1),
  COG4                   VARCHAR2(1),
  COG5                   VARCHAR2(1),
  COG6                   VARCHAR2(1),
  COGSCORE               VARCHAR2(4),
  MENTALSTRESSTIMES      NUMBER(3),
  MENTALSTRESSTORELAX    VARCHAR2(255),
  MENTALSTRESSSCORE      VARCHAR2(10),
  TANGIBLEHELPHOUSEN     NUMBER(1),
  TANGIBLEHELPHOUSEY     NUMBER(1),
  TANGIBLEHELPYARDN      NUMBER(1),
  TANGIBLEHELPYARDY      NUMBER(1),
  TANGIBLEHELPHOMEN      NUMBER(1),
  TANGIBLEHELPHOMEY      NUMBER(1),
  TANGIBLEHELPMEALN      NUMBER(1),
  TANGIBLEHELPMEALY      NUMBER(1),
  TANGIBLEHELPHYGIENEN   NUMBER(1),
  TANGIBLEHELPHYGIENEY   NUMBER(1),
  TANGIBLEHELPERRANDSN   NUMBER(1),
  TANGIBLEHELPERRANDSY   NUMBER(1),
  TANGIBLEHELPTRANSPORTN NUMBER(1),
  TANGIBLEHELPTRANSPORTY NUMBER(1),
  TANGIBLEHELPSCORE      NUMBER(1),
  EMOTIONALSUPPORTN      NUMBER(1),
  EMOTIONALSUPPORTY      NUMBER(1),
  EMOTIONALSUPPORTSCORE  NUMBER(1),
  HEALTHEDUCATIONN       NUMBER(1),
  HEALTHEDUCATIONY       NUMBER(1),
  HEALTHEDUCATIONHOURS   NUMBER(4),
  HEALTHEDUCATIONSCORE   VARCHAR2(10),
  EXERCISEPRGMN          NUMBER(1),
  EXERCISEPRGMY          NUMBER(1),
  EXERCISEPRGMHOURS      NUMBER(4),
  EXERCISEPRGMSCORE      NUMBER(1),
  COMMUNICATE1           VARCHAR2(1),
  COMMUNICATE2           VARCHAR2(1),
  COMMUNICATE3           VARCHAR2(1),
  COMMUNICATESCORE       VARCHAR2(4)
)
;
alter table FORMSELFMANAGEMENT
  add primary key (ID);

prompt
prompt Creating table FORMSF36
prompt =======================
prompt
create table FORMSF36
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null,
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6) not null,
  STUDYID        VARCHAR2(20) default 'N/A' not null,
  Q1EX           NUMBER(1),
  Q1VG           NUMBER(1),
  Q1G            NUMBER(1),
  Q1F            NUMBER(1),
  Q1P            NUMBER(1),
  Q1CMT          VARCHAR2(255),
  Q2MUCHBETTER   NUMBER(1),
  Q2BETTER       NUMBER(1),
  Q2SAME         NUMBER(1),
  Q2WORSE        NUMBER(1),
  Q2MUCHWORSE    NUMBER(1),
  Q2CMT          VARCHAR2(255),
  Q3AYESLOT      NUMBER(1),
  Q3AYESLITTLE   NUMBER(1),
  Q3ANO          NUMBER(1),
  Q3ACMT         VARCHAR2(255),
  Q3BYESLOT      NUMBER(1),
  Q3BYESLITTLE   NUMBER(1),
  Q3BNO          NUMBER(1),
  Q3BCMT         VARCHAR2(255),
  Q3CYESLOT      NUMBER(1),
  Q3CYESLITTLE   NUMBER(1),
  Q3CNO          NUMBER(1),
  Q3CCMT         VARCHAR2(255),
  Q3DYESLOT      NUMBER(1),
  Q3DYESLITTLE   NUMBER(1),
  Q3DNO          NUMBER(1),
  Q3DCMT         VARCHAR2(255),
  Q3EYESLOT      NUMBER(1),
  Q3EYESLITTLE   NUMBER(1),
  Q3ENO          NUMBER(1),
  Q3ECMT         VARCHAR2(255),
  Q3FYESLOT      NUMBER(1),
  Q3FYESLITTLE   NUMBER(1),
  Q3FNO          NUMBER(1),
  Q3FCMT         VARCHAR2(255),
  Q3GYESLOT      NUMBER(1),
  Q3GYESLITTLE   NUMBER(1),
  Q3GNO          NUMBER(1),
  Q3GCMT         VARCHAR2(255),
  Q3HYESLOT      NUMBER(1),
  Q3HYESLITTLE   NUMBER(1),
  Q3HNO          NUMBER(1),
  Q3HCMT         VARCHAR2(255),
  Q3IYESLOT      NUMBER(1),
  Q3IYESLITTLE   NUMBER(1),
  Q3INO          NUMBER(1),
  Q3ICMT         VARCHAR2(255),
  Q3JYESLOT      NUMBER(1),
  Q3JYESLITTLE   NUMBER(1),
  Q3JNO          NUMBER(1),
  Q3JCMT         VARCHAR2(255),
  Q4AALL         NUMBER(1),
  Q4AMOST        NUMBER(1),
  Q4ASOME        NUMBER(1),
  Q4ALITTLE      NUMBER(1),
  Q4ANONE        NUMBER(1),
  Q4ACMT         VARCHAR2(255),
  Q4BALL         NUMBER(1),
  Q4BMOST        NUMBER(1),
  Q4BSOME        NUMBER(1),
  Q4BLITTLE      NUMBER(1),
  Q4BNONE        NUMBER(1),
  Q4BCMT         VARCHAR2(255),
  Q4CALL         NUMBER(1),
  Q4CMOST        NUMBER(1),
  Q4CSOME        NUMBER(1),
  Q4CLITTLE      NUMBER(1),
  Q4CNONE        NUMBER(1),
  Q4CCMT         VARCHAR2(255),
  Q4DALL         NUMBER(1),
  Q4DMOST        NUMBER(1),
  Q4DSOME        NUMBER(1),
  Q4DLITTLE      NUMBER(1),
  Q4DNONE        NUMBER(1),
  Q4DCMT         VARCHAR2(255),
  Q5AALL         NUMBER(1),
  Q5AMOST        NUMBER(1),
  Q5ASOME        NUMBER(1),
  Q5ALITTLE      NUMBER(1),
  Q5ANONE        NUMBER(1),
  Q5ACMT         VARCHAR2(255),
  Q5BALL         NUMBER(1),
  Q5BMOST        NUMBER(1),
  Q5BSOME        NUMBER(1),
  Q5BLITTLE      NUMBER(1),
  Q5BNONE        NUMBER(1),
  Q5BCMT         VARCHAR2(255),
  Q5CALL         NUMBER(1),
  Q5CMOST        NUMBER(1),
  Q5CSOME        NUMBER(1),
  Q5CLITTLE      NUMBER(1),
  Q5CNONE        NUMBER(1),
  Q5CCMT         VARCHAR2(255),
  Q6NOTATALL     NUMBER(1),
  Q6SLIGHTLY     NUMBER(1),
  Q6MODERATELY   NUMBER(1),
  Q6QUITEABIT    NUMBER(1),
  Q6EXTREMELY    NUMBER(1),
  Q6CMT          VARCHAR2(255),
  Q7NONE         NUMBER(1),
  Q7VERYMILD     NUMBER(1),
  Q7MILD         NUMBER(1),
  Q7MODERATE     NUMBER(1),
  Q7SEVERE       NUMBER(1),
  Q7VERYSEVERE   NUMBER(1),
  Q7CMT          VARCHAR2(255),
  Q8NOTATALL     NUMBER(1),
  Q8SLIGHTLY     NUMBER(1),
  Q8MODERATELY   NUMBER(1),
  Q8QUITEABIT    NUMBER(1),
  Q8EXTREMELY    NUMBER(1),
  Q8CMT          VARCHAR2(255),
  Q9AALL         NUMBER(1),
  Q9AMOST        NUMBER(1),
  Q9ASOME        NUMBER(1),
  Q9ALITTLE      NUMBER(1),
  Q9ANONE        NUMBER(1),
  Q9ACMT         VARCHAR2(255),
  Q9BALL         NUMBER(1),
  Q9BMOST        NUMBER(1),
  Q9BSOME        NUMBER(1),
  Q9BLITTLE      NUMBER(1),
  Q9BNONE        NUMBER(1),
  Q9BCMT         VARCHAR2(255),
  Q9CALL         NUMBER(1),
  Q9CMOST        NUMBER(1),
  Q9CSOME        NUMBER(1),
  Q9CLITTLE      NUMBER(1),
  Q9CNONE        NUMBER(1),
  Q9CCMT         VARCHAR2(255),
  Q9DALL         NUMBER(1),
  Q9DMOST        NUMBER(1),
  Q9DSOME        NUMBER(1),
  Q9DLITTLE      NUMBER(1),
  Q9DNONE        NUMBER(1),
  Q9DCMT         VARCHAR2(255),
  Q9EALL         NUMBER(1),
  Q9EMOST        NUMBER(1),
  Q9ESOME        NUMBER(1),
  Q9ELITTLE      NUMBER(1),
  Q9ENONE        NUMBER(1),
  Q9ECMT         VARCHAR2(255),
  Q9FALL         NUMBER(1),
  Q9FMOST        NUMBER(1),
  Q9FSOME        NUMBER(1),
  Q9FLITTLE      NUMBER(1),
  Q9FNONE        NUMBER(1),
  Q9FCMT         VARCHAR2(255),
  Q9GALL         NUMBER(1),
  Q9GMOST        NUMBER(1),
  Q9GSOME        NUMBER(1),
  Q9GLITTLE      NUMBER(1),
  Q9GNONE        NUMBER(1),
  Q9GCMT         VARCHAR2(255),
  Q9HALL         NUMBER(1),
  Q9HMOST        NUMBER(1),
  Q9HSOME        NUMBER(1),
  Q9HLITTLE      NUMBER(1),
  Q9HNONE        NUMBER(1),
  Q9HCMT         VARCHAR2(255),
  Q9IALL         NUMBER(1),
  Q9IMOST        NUMBER(1),
  Q9ISOME        NUMBER(1),
  Q9ILITTLE      NUMBER(1),
  Q9INONE        NUMBER(1),
  Q9ICMT         VARCHAR2(255),
  Q10ALL         NUMBER(1),
  Q10MOST        NUMBER(1),
  Q10SOME        NUMBER(1),
  Q10LITTLE      NUMBER(1),
  Q10NONE        NUMBER(1),
  Q10CMT         VARCHAR2(255),
  Q11ADEFTRUE    NUMBER(1),
  Q11AMOSTTRUE   NUMBER(1),
  Q11ANOTSURE    NUMBER(1),
  Q11AMOSTFALSE  NUMBER(1),
  Q11ADEFFALSE   NUMBER(1),
  Q11ACMT        VARCHAR2(255),
  Q11BDEFTRUE    NUMBER(1),
  Q11BMOSTTRUE   NUMBER(1),
  Q11BNOTSURE    NUMBER(1),
  Q11BMOSTFALSE  NUMBER(1),
  Q11BDEFFALSE   NUMBER(1),
  Q11BCMT        VARCHAR2(255),
  Q11CDEFTRUE    NUMBER(1),
  Q11CMOSTTRUE   NUMBER(1),
  Q11CNOTSURE    NUMBER(1),
  Q11CMOSTFALSE  NUMBER(1),
  Q11CDEFFALSE   NUMBER(1),
  Q11CCMT        VARCHAR2(255),
  Q11DDEFTRUE    NUMBER(1),
  Q11DMOSTTRUE   NUMBER(1),
  Q11DNOTSURE    NUMBER(1),
  Q11DMOSTFALSE  NUMBER(1),
  Q11DDEFFALSE   NUMBER(1),
  Q11DCMT        VARCHAR2(255),
  Q12ANOTANS     NUMBER(1),
  Q12ANOT        NUMBER(1),
  Q12ALITTLE     NUMBER(1),
  Q12ASOME       NUMBER(1),
  Q12ALOT        NUMBER(1),
  Q12AMUCH       NUMBER(1),
  Q12ACMT        VARCHAR2(255)
)
;
alter table FORMSF36
  add primary key (ID);

prompt
prompt Creating table FORMSF36CAREGIVER
prompt ================================
prompt
create table FORMSF36CAREGIVER
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null,
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6) not null,
  STUDYID        VARCHAR2(20) default 'N/A' not null,
  Q1EX           NUMBER(1),
  Q1VG           NUMBER(1),
  Q1G            NUMBER(1),
  Q1F            NUMBER(1),
  Q1P            NUMBER(1),
  Q1CMT          VARCHAR2(255),
  Q2MUCHBETTER   NUMBER(1),
  Q2BETTER       NUMBER(1),
  Q2SAME         NUMBER(1),
  Q2WORSE        NUMBER(1),
  Q2MUCHWORSE    NUMBER(1),
  Q2CMT          VARCHAR2(255),
  Q3AYESLOT      NUMBER(1),
  Q3AYESLITTLE   NUMBER(1),
  Q3ANO          NUMBER(1),
  Q3ACMT         VARCHAR2(255),
  Q3BYESLOT      NUMBER(1),
  Q3BYESLITTLE   NUMBER(1),
  Q3BNO          NUMBER(1),
  Q3BCMT         VARCHAR2(255),
  Q3CYESLOT      NUMBER(1),
  Q3CYESLITTLE   NUMBER(1),
  Q3CNO          NUMBER(1),
  Q3CCMT         VARCHAR2(255),
  Q3DYESLOT      NUMBER(1),
  Q3DYESLITTLE   NUMBER(1),
  Q3DNO          NUMBER(1),
  Q3DCMT         VARCHAR2(255),
  Q3EYESLOT      NUMBER(1),
  Q3EYESLITTLE   NUMBER(1),
  Q3ENO          NUMBER(1),
  Q3ECMT         VARCHAR2(255),
  Q3FYESLOT      NUMBER(1),
  Q3FYESLITTLE   NUMBER(1),
  Q3FNO          NUMBER(1),
  Q3FCMT         VARCHAR2(255),
  Q3GYESLOT      NUMBER(1),
  Q3GYESLITTLE   NUMBER(1),
  Q3GNO          NUMBER(1),
  Q3GCMT         VARCHAR2(255),
  Q3HYESLOT      NUMBER(1),
  Q3HYESLITTLE   NUMBER(1),
  Q3HNO          NUMBER(1),
  Q3HCMT         VARCHAR2(255),
  Q3IYESLOT      NUMBER(1),
  Q3IYESLITTLE   NUMBER(1),
  Q3INO          NUMBER(1),
  Q3ICMT         VARCHAR2(255),
  Q3JYESLOT      NUMBER(1),
  Q3JYESLITTLE   NUMBER(1),
  Q3JNO          NUMBER(1),
  Q3JCMT         VARCHAR2(255),
  Q4AALL         NUMBER(1),
  Q4AMOST        NUMBER(1),
  Q4ASOME        NUMBER(1),
  Q4ALITTLE      NUMBER(1),
  Q4ANONE        NUMBER(1),
  Q4ACMT         VARCHAR2(255),
  Q4BALL         NUMBER(1),
  Q4BMOST        NUMBER(1),
  Q4BSOME        NUMBER(1),
  Q4BLITTLE      NUMBER(1),
  Q4BNONE        NUMBER(1),
  Q4BCMT         VARCHAR2(255),
  Q4CALL         NUMBER(1),
  Q4CMOST        NUMBER(1),
  Q4CSOME        NUMBER(1),
  Q4CLITTLE      NUMBER(1),
  Q4CNONE        NUMBER(1),
  Q4CCMT         VARCHAR2(255),
  Q4DALL         NUMBER(1),
  Q4DMOST        NUMBER(1),
  Q4DSOME        NUMBER(1),
  Q4DLITTLE      NUMBER(1),
  Q4DNONE        NUMBER(1),
  Q4DCMT         VARCHAR2(255),
  Q5AALL         NUMBER(1),
  Q5AMOST        NUMBER(1),
  Q5ASOME        NUMBER(1),
  Q5ALITTLE      NUMBER(1),
  Q5ANONE        NUMBER(1),
  Q5ACMT         VARCHAR2(255),
  Q5BALL         NUMBER(1),
  Q5BMOST        NUMBER(1),
  Q5BSOME        NUMBER(1),
  Q5BLITTLE      NUMBER(1),
  Q5BNONE        NUMBER(1),
  Q5BCMT         VARCHAR2(255),
  Q5CALL         NUMBER(1),
  Q5CMOST        NUMBER(1),
  Q5CSOME        NUMBER(1),
  Q5CLITTLE      NUMBER(1),
  Q5CNONE        NUMBER(1),
  Q5CCMT         VARCHAR2(255),
  Q6NOTATALL     NUMBER(1),
  Q6SLIGHTLY     NUMBER(1),
  Q6MODERATELY   NUMBER(1),
  Q6QUITEABIT    NUMBER(1),
  Q6EXTREMELY    NUMBER(1),
  Q6CMT          VARCHAR2(255),
  Q7NONE         NUMBER(1),
  Q7VERYMILD     NUMBER(1),
  Q7MILD         NUMBER(1),
  Q7MODERATE     NUMBER(1),
  Q7SEVERE       NUMBER(1),
  Q7VERYSEVERE   NUMBER(1),
  Q7CMT          VARCHAR2(255),
  Q8NOTATALL     NUMBER(1),
  Q8SLIGHTLY     NUMBER(1),
  Q8MODERATELY   NUMBER(1),
  Q8QUITEABIT    NUMBER(1),
  Q8EXTREMELY    NUMBER(1),
  Q8CMT          VARCHAR2(255),
  Q9AALL         NUMBER(1),
  Q9AMOST        NUMBER(1),
  Q9ASOME        NUMBER(1),
  Q9ALITTLE      NUMBER(1),
  Q9ANONE        NUMBER(1),
  Q9ACMT         VARCHAR2(255),
  Q9BALL         NUMBER(1),
  Q9BMOST        NUMBER(1),
  Q9BSOME        NUMBER(1),
  Q9BLITTLE      NUMBER(1),
  Q9BNONE        NUMBER(1),
  Q9BCMT         VARCHAR2(255),
  Q9CALL         NUMBER(1),
  Q9CMOST        NUMBER(1),
  Q9CSOME        NUMBER(1),
  Q9CLITTLE      NUMBER(1),
  Q9CNONE        NUMBER(1),
  Q9CCMT         VARCHAR2(255),
  Q9DALL         NUMBER(1),
  Q9DMOST        NUMBER(1),
  Q9DSOME        NUMBER(1),
  Q9DLITTLE      NUMBER(1),
  Q9DNONE        NUMBER(1),
  Q9DCMT         VARCHAR2(255),
  Q9EALL         NUMBER(1),
  Q9EMOST        NUMBER(1),
  Q9ESOME        NUMBER(1),
  Q9ELITTLE      NUMBER(1),
  Q9ENONE        NUMBER(1),
  Q9ECMT         VARCHAR2(255),
  Q9FALL         NUMBER(1),
  Q9FMOST        NUMBER(1),
  Q9FSOME        NUMBER(1),
  Q9FLITTLE      NUMBER(1),
  Q9FNONE        NUMBER(1),
  Q9FCMT         VARCHAR2(255),
  Q9GALL         NUMBER(1),
  Q9GMOST        NUMBER(1),
  Q9GSOME        NUMBER(1),
  Q9GLITTLE      NUMBER(1),
  Q9GNONE        NUMBER(1),
  Q9GCMT         VARCHAR2(255),
  Q9HALL         NUMBER(1),
  Q9HMOST        NUMBER(1),
  Q9HSOME        NUMBER(1),
  Q9HLITTLE      NUMBER(1),
  Q9HNONE        NUMBER(1),
  Q9HCMT         VARCHAR2(255),
  Q9IALL         NUMBER(1),
  Q9IMOST        NUMBER(1),
  Q9ISOME        NUMBER(1),
  Q9ILITTLE      NUMBER(1),
  Q9INONE        NUMBER(1),
  Q9ICMT         VARCHAR2(255),
  Q10ALL         NUMBER(1),
  Q10MOST        NUMBER(1),
  Q10SOME        NUMBER(1),
  Q10LITTLE      NUMBER(1),
  Q10NONE        NUMBER(1),
  Q10CMT         VARCHAR2(255),
  Q11ADEFTRUE    NUMBER(1),
  Q11AMOSTTRUE   NUMBER(1),
  Q11ANOTSURE    NUMBER(1),
  Q11AMOSTFALSE  NUMBER(1),
  Q11ADEFFALSE   NUMBER(1),
  Q11ACMT        VARCHAR2(255),
  Q11BDEFTRUE    NUMBER(1),
  Q11BMOSTTRUE   NUMBER(1),
  Q11BNOTSURE    NUMBER(1),
  Q11BMOSTFALSE  NUMBER(1),
  Q11BDEFFALSE   NUMBER(1),
  Q11BCMT        VARCHAR2(255),
  Q11CDEFTRUE    NUMBER(1),
  Q11CMOSTTRUE   NUMBER(1),
  Q11CNOTSURE    NUMBER(1),
  Q11CMOSTFALSE  NUMBER(1),
  Q11CDEFFALSE   NUMBER(1),
  Q11CCMT        VARCHAR2(255),
  Q11DDEFTRUE    NUMBER(1),
  Q11DMOSTTRUE   NUMBER(1),
  Q11DNOTSURE    NUMBER(1),
  Q11DMOSTFALSE  NUMBER(1),
  Q11DDEFFALSE   NUMBER(1),
  Q11DCMT        VARCHAR2(255),
  Q12ANOTANS     NUMBER(1),
  Q12ANOT        NUMBER(1),
  Q12ALITTLE     NUMBER(1),
  Q12ASOME       NUMBER(1),
  Q12ALOT        NUMBER(1),
  Q12AMUCH       NUMBER(1),
  Q12ACMT        VARCHAR2(255)
)
;
alter table FORMSF36CAREGIVER
  add primary key (ID);

prompt
prompt Creating table FORMTREATMENTPREF
prompt ================================
prompt
create table FORMTREATMENTPREF
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null,
  PROVIDER_NO    NUMBER(10),
  FORMCREATED    DATE,
  FORMEDITED     TIMESTAMP(6) not null,
  STUDYID        VARCHAR2(20) default 'N/A' not null,
  TREATMENTGR    NUMBER(1),
  CONTROLGR      NUMBER(1),
  EITHERGR       NUMBER(1)
)
;
alter table FORMTREATMENTPREF
  add primary key (ID);

prompt
prompt Creating table FORMTYPE2DIABETES
prompt ================================
prompt
create table FORMTYPE2DIABETES
(
  ID              NUMBER(10) not null,
  DEMOGRAPHIC_NO  NUMBER(10),
  PROVIDER_NO     NUMBER(10),
  FORMCREATED     DATE,
  FORMEDITED      TIMESTAMP(6) not null,
  PNAME           VARCHAR2(60),
  BIRTHDATE       DATE,
  DATEDX          DATE,
  HEIGHT          VARCHAR2(10),
  DATE1           DATE,
  DATE2           DATE,
  DATE3           DATE,
  DATE4           DATE,
  DATE5           DATE,
  WEIGHT1         VARCHAR2(20),
  WEIGHT2         VARCHAR2(20),
  WEIGHT3         VARCHAR2(20),
  WEIGHT4         VARCHAR2(20),
  WEIGHT5         VARCHAR2(20),
  BP1             VARCHAR2(20),
  BP2             VARCHAR2(20),
  BP3             VARCHAR2(20),
  BP4             VARCHAR2(20),
  BP5             VARCHAR2(20),
  GLUCOSEA1       VARCHAR2(50),
  GLUCOSEA2       VARCHAR2(50),
  GLUCOSEA3       VARCHAR2(50),
  GLUCOSEA4       VARCHAR2(50),
  GLUCOSEA5       VARCHAR2(50),
  GLUCOSEB1       VARCHAR2(50),
  GLUCOSEB2       VARCHAR2(50),
  GLUCOSEB3       VARCHAR2(50),
  GLUCOSEB4       VARCHAR2(50),
  GLUCOSEB5       VARCHAR2(50),
  GLUCOSEC1       VARCHAR2(50),
  GLUCOSEC2       VARCHAR2(50),
  GLUCOSEC3       VARCHAR2(50),
  GLUCOSEC4       VARCHAR2(50),
  GLUCOSEC5       VARCHAR2(50),
  RENAL1          VARCHAR2(50),
  RENAL2          VARCHAR2(50),
  RENAL3          VARCHAR2(50),
  RENAL4          VARCHAR2(50),
  RENAL5          VARCHAR2(50),
  URINERATIO1     VARCHAR2(50),
  URINERATIO2     VARCHAR2(50),
  URINERATIO3     VARCHAR2(50),
  URINERATIO4     VARCHAR2(50),
  URINERATIO5     VARCHAR2(50),
  URINECLEARANCE1 VARCHAR2(100),
  URINECLEARANCE2 VARCHAR2(100),
  URINECLEARANCE3 VARCHAR2(100),
  URINECLEARANCE4 VARCHAR2(100),
  URINECLEARANCE5 VARCHAR2(100),
  LIPIDSA1        VARCHAR2(30),
  LIPIDSA2        VARCHAR2(30),
  LIPIDSA3        VARCHAR2(30),
  LIPIDSA4        VARCHAR2(30),
  LIPIDSA5        VARCHAR2(30),
  LIPIDSB1        VARCHAR2(30),
  LIPIDSB2        VARCHAR2(30),
  LIPIDSB3        VARCHAR2(30),
  LIPIDSB4        VARCHAR2(30),
  LIPIDSB5        VARCHAR2(30),
  LIPIDSC1        VARCHAR2(30),
  LIPIDSC2        VARCHAR2(30),
  LIPIDSC3        VARCHAR2(30),
  LIPIDSC4        VARCHAR2(30),
  LIPIDSC5        VARCHAR2(30),
  OPHTHALMOLOGIST VARCHAR2(50),
  EYES1           VARCHAR2(50),
  EYES2           VARCHAR2(50),
  EYES3           VARCHAR2(50),
  EYES4           VARCHAR2(50),
  EYES5           VARCHAR2(50),
  FEET1           VARCHAR2(100),
  FEET2           VARCHAR2(100),
  FEET3           VARCHAR2(100),
  FEET4           VARCHAR2(100),
  FEET5           VARCHAR2(100),
  METFORMIN       NUMBER(1),
  ACEINHIBITOR    NUMBER(1),
  GLYBURIDE       NUMBER(1),
  ASA             NUMBER(1),
  OTHEROHA        NUMBER(1),
  OTHERBOX7       VARCHAR2(20),
  INSULIN         NUMBER(1),
  OTHERBOX8       VARCHAR2(20),
  MEDS1           VARCHAR2(100),
  MEDS2           VARCHAR2(100),
  MEDS3           VARCHAR2(100),
  MEDS4           VARCHAR2(100),
  MEDS5           VARCHAR2(100),
  LIFESTYLE1      VARCHAR2(50),
  LIFESTYLE2      VARCHAR2(50),
  LIFESTYLE3      VARCHAR2(50),
  LIFESTYLE4      VARCHAR2(50),
  LIFESTYLE5      VARCHAR2(50),
  EXERCISE1       VARCHAR2(50),
  EXERCISE2       VARCHAR2(50),
  EXERCISE3       VARCHAR2(50),
  EXERCISE4       VARCHAR2(50),
  EXERCISE5       VARCHAR2(50),
  ALCOHOL1        VARCHAR2(50),
  ALCOHOL2        VARCHAR2(50),
  ALCOHOL3        VARCHAR2(50),
  ALCOHOL4        VARCHAR2(50),
  ALCOHOL5        VARCHAR2(50),
  SEXUALFUNCTION1 VARCHAR2(50),
  SEXUALFUNCTION2 VARCHAR2(50),
  SEXUALFUNCTION3 VARCHAR2(50),
  SEXUALFUNCTION4 VARCHAR2(50),
  SEXUALFUNCTION5 VARCHAR2(50),
  DIET1           VARCHAR2(50),
  DIET2           VARCHAR2(50),
  DIET3           VARCHAR2(50),
  DIET4           VARCHAR2(50),
  DIET5           VARCHAR2(50),
  OTHERPLAN1      VARCHAR2(100),
  OTHERPLAN2      VARCHAR2(100),
  OTHERPLAN3      VARCHAR2(100),
  OTHERPLAN4      VARCHAR2(100),
  OTHERPLAN5      VARCHAR2(100),
  CONSULTANT      VARCHAR2(30),
  EDUCATOR        VARCHAR2(30),
  NUTRITIONIST    VARCHAR2(30),
  CDN1            VARCHAR2(100),
  CDN2            VARCHAR2(100),
  CDN3            VARCHAR2(100),
  CDN4            VARCHAR2(100),
  CDN5            VARCHAR2(100),
  INITIALS1       VARCHAR2(30),
  INITIALS2       VARCHAR2(30),
  INITIALS3       VARCHAR2(30),
  INITIALS4       VARCHAR2(30),
  INITIALS5       VARCHAR2(30),
  RESOURCE1       NUMBER(1),
  RESOURCE2       NUMBER(1)
)
;
alter table FORMTYPE2DIABETES
  add primary key (ID);

prompt
prompt Creating table FORMVTFORM
prompt =========================
prompt
create table FORMVTFORM
(
  ID                      NUMBER(10) not null,
  DEMOGRAPHIC_NO          NUMBER(10) not null,
  PROVIDER_NO             NUMBER(10),
  FORMCREATED             DATE,
  FORMEDITED              TIMESTAMP(6) default current_timestamp not null,
  VISITCOD                VARCHAR2(10),
  HTVALUE                 VARCHAR2(10),
  HTDATE                  VARCHAR2(10),
  HTCOMMENTS              VARCHAR2(255),
  WTVALUE                 VARCHAR2(10),
  WTDATE                  VARCHAR2(10),
  WTCOMMENTS              VARCHAR2(255),
  WHRVALUE                VARCHAR2(10),
  WHRDATE                 VARCHAR2(10),
  WHRCOMMENTS             VARCHAR2(255),
  WCVALUE                 VARCHAR2(10),
  WCDATE                  VARCHAR2(10),
  WCCOMMENTS              VARCHAR2(255),
  HCVALUE                 VARCHAR2(10),
  HCDATE                  VARCHAR2(10),
  HCCOMMENTS              VARCHAR2(255),
  BPVALUE                 VARCHAR2(10),
  BPDATE                  VARCHAR2(10),
  BPCOMMENTS              VARCHAR2(255),
  HRVALUE                 VARCHAR2(10),
  HRDATE                  VARCHAR2(10),
  HRCOMMENTS              VARCHAR2(255),
  HBA1VALUE               VARCHAR2(10),
  HBA1DATE                VARCHAR2(10),
  HBA1COMMENTS            VARCHAR2(255),
  BGVALUE                 VARCHAR2(10),
  BGDATE                  VARCHAR2(10),
  BGCOMMENTS              VARCHAR2(255),
  LDLVALUE                VARCHAR2(10),
  LDLDATE                 VARCHAR2(10),
  LDLCOMMENTS             VARCHAR2(255),
  HDLVALUE                VARCHAR2(10),
  HDLDATE                 VARCHAR2(10),
  HDLCOMMENTS             VARCHAR2(255),
  TCHLVALUE               VARCHAR2(10),
  TCHLDATE                VARCHAR2(10),
  TCHLCOMMENTS            VARCHAR2(255),
  TRIGVALUE               VARCHAR2(10),
  TRIGDATE                VARCHAR2(10),
  TRIGCOMMENTS            VARCHAR2(255),
  UALBVALUE               VARCHAR2(10),
  UALBDATE                VARCHAR2(10),
  UALBCOMMENTS            VARCHAR2(255),
  F24UAVALUE              VARCHAR2(10),
  F24UADATE               VARCHAR2(10),
  F24UACOMMENTS           VARCHAR2(255),
  FTEXVALUE               VARCHAR2(10),
  FTEXDATE                VARCHAR2(10),
  FTEXCOMMENTS            VARCHAR2(255),
  FTNEVALUE               VARCHAR2(10),
  FTNEDATE                VARCHAR2(10),
  FTNECOMMENTS            VARCHAR2(255),
  FTISVALUE               VARCHAR2(10),
  FTISDATE                VARCHAR2(10),
  FTISCOMMENTS            VARCHAR2(255),
  FTULVALUE               VARCHAR2(10),
  FTULDATE                VARCHAR2(10),
  FTULCOMMENTS            VARCHAR2(255),
  FTINVALUE               VARCHAR2(10),
  FTINDATE                VARCHAR2(10),
  FTINCOMMENTS            VARCHAR2(255),
  FTOTVALUE               VARCHAR2(10),
  FTOTDATE                VARCHAR2(10),
  FTOTCOMMENTS            VARCHAR2(255),
  FTREVALUE               VARCHAR2(10),
  FTREDATE                VARCHAR2(10),
  FTRECOMMENTS            VARCHAR2(255),
  IEXVALUE                VARCHAR2(10),
  IEXDATE                 VARCHAR2(10),
  IEXCOMMENTS             VARCHAR2(255),
  IDIAVALUE               VARCHAR2(10),
  IDIADATE                VARCHAR2(10),
  IDIACOMMENTS            VARCHAR2(255),
  IHYPVALUE               VARCHAR2(10),
  IHYPDATE                VARCHAR2(10),
  IHYPCOMMENTS            VARCHAR2(255),
  IOTHVALUE               VARCHAR2(10),
  IOTHDATE                VARCHAR2(10),
  IOTHCOMMENTS            VARCHAR2(255),
  IREFVALUE               VARCHAR2(10),
  IREFDATE                VARCHAR2(10),
  IREFCOMMENTS            VARCHAR2(255),
  SMKSVALUE               VARCHAR2(10),
  SMKSDATE                VARCHAR2(10),
  SMKSCOMMENTS            VARCHAR2(255),
  SMKHVALUE               VARCHAR2(10),
  SMKHDATE                VARCHAR2(10),
  SMKHCOMMENTS            VARCHAR2(255),
  SMKCVALUE               VARCHAR2(10),
  SMKCDATE                VARCHAR2(10),
  SMKCCOMMENTS            VARCHAR2(255),
  EXERVALUE               VARCHAR2(10),
  EXERDATE                VARCHAR2(10),
  EXERCOMMENTS            VARCHAR2(255),
  DIETVALUE               VARCHAR2(10),
  DIETDATE                VARCHAR2(10),
  DIETCOMMENTS            VARCHAR2(255),
  DPSCVALUE               VARCHAR2(10),
  DPSCDATE                VARCHAR2(10),
  DPSCCOMMENTS            VARCHAR2(255),
  STSCVALUE               VARCHAR2(10),
  STSCDATE                VARCHAR2(10),
  STSCCOMMENTS            VARCHAR2(255),
  LCCTVALUE               VARCHAR2(10),
  LCCTDATE                VARCHAR2(10),
  LCCTCOMMENTS            VARCHAR2(255),
  MEDGVALUE               VARCHAR2(10),
  MEDGDATE                VARCHAR2(10),
  MEDGCOMMENTS            VARCHAR2(255),
  MEDNVALUE               VARCHAR2(10),
  MEDNDATE                VARCHAR2(10),
  MEDNCOMMENTS            VARCHAR2(255),
  MEDRVALUE               VARCHAR2(10),
  MEDRDATE                VARCHAR2(10),
  MEDRCOMMENTS            VARCHAR2(255),
  MEDAVALUE               VARCHAR2(10),
  MEDADATE                VARCHAR2(10),
  MEDACOMMENTS            VARCHAR2(255),
  NTRCVALUE               VARCHAR2(10),
  NTRCDATE                VARCHAR2(10),
  NTRCCOMMENTS            VARCHAR2(255),
  EXECVALUE               VARCHAR2(10),
  EXECDATE                VARCHAR2(10),
  EXECCOMMENTS            VARCHAR2(255),
  SMCCVALUE               VARCHAR2(10),
  SMCCDATE                VARCHAR2(10),
  SMCCCOMMENTS            VARCHAR2(255),
  DIACVALUE               VARCHAR2(10),
  DIACDATE                VARCHAR2(10),
  DIACCOMMENTS            VARCHAR2(255),
  PSYCVALUE               VARCHAR2(10),
  PSYCDATE                VARCHAR2(10),
  PSYCCOMMENTS            VARCHAR2(255),
  OTHCVALUE               VARCHAR2(10),
  OTHCDATE                VARCHAR2(10),
  OTHCCOMMENTS            VARCHAR2(255),
  DMVALUE                 VARCHAR2(10),
  HTNVALUE                VARCHAR2(10),
  HCHLVALUE               VARCHAR2(10),
  MIVALUE                 VARCHAR2(10),
  ANGVALUE                VARCHAR2(10),
  ACSVALUE                VARCHAR2(10),
  RVTNVALUE               VARCHAR2(10),
  CVDVALUE                VARCHAR2(10),
  PVDVALUE                VARCHAR2(10),
  DIAGNOSIS               VARCHAR2(4000),
  SUBJECTIVE              VARCHAR2(4000),
  OBJECTIVE               VARCHAR2(4000),
  ASSESSMENT              VARCHAR2(4000),
  PLAN                    VARCHAR2(4000),
  WHRBVALUE               VARCHAR2(10),
  WHRBDATE                VARCHAR2(10),
  WHRBCOMMENTS            VARCHAR2(255),
  WHRBLASTDATA            VARCHAR2(10),
  WHRBLASTDATAENTEREDDATE VARCHAR2(10)
)
;
alter table FORMVTFORM
  add constraint PK_FORMVTFORM primary key (ID);
create index IDX_FORMVTFORM_DNO on FORMVTFORM (DEMOGRAPHIC_NO);

prompt
prompt Creating table FUNCTIONAL_USER_TYPE
prompt ===================================
prompt
create table FUNCTIONAL_USER_TYPE
(
  ID   NUMBER(20) not null,
  NAME VARCHAR2(255)
)
;
alter table FUNCTIONAL_USER_TYPE
  add primary key (ID);

prompt
prompt Creating table GROUPMEMBERS_TBL
prompt ===============================
prompt
create table GROUPMEMBERS_TBL
(
  GROUPID     NUMBER(10),
  PROVIDER_NO VARCHAR2(6)
)
;

prompt
prompt Creating table GROUPS_TBL
prompt =========================
prompt
create table GROUPS_TBL
(
  GROUPID   NUMBER(10) not null,
  PARENTID  NUMBER(10),
  GROUPDESC VARCHAR2(50)
)
;
alter table GROUPS_TBL
  add primary key (GROUPID);

prompt
prompt Creating table HASH_AUDIT
prompt =========================
prompt
create table HASH_AUDIT
(
  SIGNATURE VARCHAR2(255) not null,
  ID        NUMBER(10) default 0,
  TYPE      VARCHAR2(3) not null,
  ALGORITHM VARCHAR2(127)
)
;
alter table HASH_AUDIT
  add constraint PK_HASH_AUDIT primary key (SIGNATURE);

prompt
prompt Creating table HL7TEXTINFO
prompt ==========================
prompt
create table HL7TEXTINFO
(
  ID                 NUMBER(10) not null,
  LAB_NO             NUMBER(10) not null,
  SEX                VARCHAR2(1),
  HEALTH_NO          VARCHAR2(12),
  RESULT_STATUS      VARCHAR2(1),
  FINAL_RESULT_COUNT NUMBER(10),
  OBR_DATE           VARCHAR2(20),
  PRIORITY           VARCHAR2(1),
  REQUESTING_CLIENT  VARCHAR2(100),
  DISCIPLINE         VARCHAR2(100),
  LAST_NAME          VARCHAR2(30),
  FIRST_NAME         VARCHAR2(30),
  REPORT_STATUS      VARCHAR2(1) not null,
  ACCESSIONNUM       VARCHAR2(20) not null
)
;
alter table HL7TEXTINFO
  add primary key (ID);

prompt
prompt Creating table HL7TEXTMESSAGE
prompt =============================
prompt
create table HL7TEXTMESSAGE
(
  LAB_ID             NUMBER(10) not null,
  FILEUPLOADCHECK_ID NUMBER(10) not null,
  MESSAGE            VARCHAR2(4000) not null,
  TYPE               VARCHAR2(100)
)
;
alter table HL7TEXTMESSAGE
  add primary key (LAB_ID);

prompt
prompt Creating table ICD10
prompt ====================
prompt
create table ICD10
(
  CODE       VARCHAR2(6) not null,
  SHORT_DESC VARCHAR2(50) not null,
  LONG_DESC  VARCHAR2(255) not null
)
;
alter table ICD10
  add primary key (CODE);

prompt
prompt Creating table ICHPPCCODE
prompt =========================
prompt
create table ICHPPCCODE
(
  ICHPPCCODE      VARCHAR2(10),
  DIAGNOSTIC_CODE VARCHAR2(10),
  DESCRIPTION     VARCHAR2(255)
)
;

prompt
prompt Creating table IMMUNIZATIONS
prompt ============================
prompt
create table IMMUNIZATIONS
(
  ID             NUMBER(11) not null,
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  PROVIDER_NO    VARCHAR2(6),
  IMMUNIZATIONS  VARCHAR2(4000),
  SAVE_DATE      DATE default '01-JAN-1900' not null,
  ARCHIVED       NUMBER(1) default '0' not null
)
;
alter table IMMUNIZATIONS
  add primary key (ID);
create index IDX_IMMUNIZATIONS_1 on IMMUNIZATIONS (DEMOGRAPHIC_NO);

prompt
prompt Creating table INCOMINGLABRULES
prompt ===============================
prompt
create table INCOMINGLABRULES
(
  ID              NUMBER(10) not null,
  PROVIDER_NO     VARCHAR2(6) not null,
  STATUS          VARCHAR2(1),
  FRWDPROVIDER_NO VARCHAR2(6),
  ARCHIVE         VARCHAR2(1) default 0
)
;
alter table INCOMINGLABRULES
  add primary key (ID);

prompt
prompt Creating table INTAKE_NODE_TYPE
prompt ===============================
prompt
create table INTAKE_NODE_TYPE
(
  INTAKE_NODE_TYPE_ID NUMBER(10) not null,
  TYPE                VARCHAR2(255) not null
)
;
alter table INTAKE_NODE_TYPE
  add primary key (INTAKE_NODE_TYPE_ID);

prompt
prompt Creating table INTAKE_NODE_TEMPLATE
prompt ===================================
prompt
create table INTAKE_NODE_TEMPLATE
(
  INTAKE_NODE_TEMPLATE_ID        NUMBER(10) not null,
  REMOTE_INTAKE_NODE_TEMPLATE_ID NUMBER(10),
  INTAKE_NODE_TYPE_ID            NUMBER(10) not null,
  INTAKE_NODE_LABEL_ID           NUMBER(10)
)
;
alter table INTAKE_NODE_TEMPLATE
  add primary key (INTAKE_NODE_TEMPLATE_ID);
alter table INTAKE_NODE_TEMPLATE
  add constraint FK_INTAKE_NODE_TEMPLATE_2 foreign key (INTAKE_NODE_TYPE_ID)
  references INTAKE_NODE_TYPE (INTAKE_NODE_TYPE_ID);
create index IDX_INTAKE_NODE_TEMPLATE_1 on INTAKE_NODE_TEMPLATE (INTAKE_NODE_TYPE_ID);
create index IDX_INTAKE_NODE_TEMPLATE_2 on INTAKE_NODE_TEMPLATE (INTAKE_NODE_LABEL_ID);

prompt
prompt Creating table INTAKE_NODE
prompt ==========================
prompt
create table INTAKE_NODE
(
  INTAKE_NODE_ID          NUMBER(10) not null,
  INTAKE_NODE_TEMPLATE_ID NUMBER(10) not null,
  INTAKE_NODE_LABEL_ID    NUMBER(10),
  POS                     NUMBER(10) default '0',
  PARENT_INTAKE_NODE_ID   NUMBER(10)
)
;
alter table INTAKE_NODE
  add primary key (INTAKE_NODE_ID);
alter table INTAKE_NODE
  add constraint FK_INTAKE_NODE_1 foreign key (INTAKE_NODE_TEMPLATE_ID)
  references INTAKE_NODE_TEMPLATE (INTAKE_NODE_TEMPLATE_ID);
alter table INTAKE_NODE
  add constraint FK_INTAKE_NODE_3 foreign key (PARENT_INTAKE_NODE_ID)
  references INTAKE_NODE (INTAKE_NODE_ID);
create index IDX_INTAKE_NODE_1 on INTAKE_NODE (INTAKE_NODE_TEMPLATE_ID);
create index IDX_INTAKE_NODE_2 on INTAKE_NODE (INTAKE_NODE_LABEL_ID);
create index IDX_INTAKE_NODE_3 on INTAKE_NODE (PARENT_INTAKE_NODE_ID);

prompt
prompt Creating table INTAKE
prompt =====================
prompt
create table INTAKE
(
  INTAKE_ID      NUMBER(10) not null,
  INTAKE_NODE_ID NUMBER(10) not null,
  CLIENT_ID      NUMBER(10) not null,
  STAFF_ID       VARCHAR2(6) not null,
  CREATION_DATE  DATE not null
)
;
alter table INTAKE
  add primary key (INTAKE_ID);
alter table INTAKE
  add constraint FK_INTAKE_1 foreign key (INTAKE_NODE_ID)
  references INTAKE_NODE (INTAKE_NODE_ID);
create index IDX_INTAKE_1 on INTAKE (INTAKE_NODE_ID);
create index IDX_INTAKE_2 on INTAKE (CLIENT_ID, CREATION_DATE);

prompt
prompt Creating table INTAKEREQUIREDFIELDS
prompt ===================================
prompt
create table INTAKEREQUIREDFIELDS
(
  FIELDKEY   VARCHAR2(255) not null,
  ISREQUIRED NUMBER not null
)
;
alter table INTAKEREQUIREDFIELDS
  add primary key (FIELDKEY);

prompt
prompt Creating table INTAKE_ANSWER
prompt ============================
prompt
create table INTAKE_ANSWER
(
  INTAKE_ANSWER_ID NUMBER(10) not null,
  INTAKE_ID        NUMBER(10) not null,
  INTAKE_NODE_ID   NUMBER(10) not null,
  VAL              VARCHAR2(4000) not null
)
;
alter table INTAKE_ANSWER
  add primary key (INTAKE_ANSWER_ID);
alter table INTAKE_ANSWER
  add constraint FK_INTAKE_ANSWER_1 foreign key (INTAKE_ID)
  references INTAKE (INTAKE_ID);
alter table INTAKE_ANSWER
  add constraint FK_INTAKE_ANSWER_2 foreign key (INTAKE_NODE_ID)
  references INTAKE_NODE (INTAKE_NODE_ID);
create index IDX_INTAKE_ANSWER_1 on INTAKE_ANSWER (INTAKE_ID);
create index IDX_INTAKE_ANSWER_2 on INTAKE_ANSWER (INTAKE_NODE_ID);

prompt
prompt Creating table INTAKE_ANSWER_VALIDATION
prompt =======================================
prompt
create table INTAKE_ANSWER_VALIDATION
(
  INTAKE_ANSWER_VALIDATION_ID NUMBER(10) not null,
  TYPE                        VARCHAR2(255) not null
)
;
alter table INTAKE_ANSWER_VALIDATION
  add primary key (INTAKE_ANSWER_VALIDATION_ID);

prompt
prompt Creating table INTAKE_ANSWER_ELEMENT
prompt ====================================
prompt
create table INTAKE_ANSWER_ELEMENT
(
  INTAKE_ANSWER_ELEMENT_ID    NUMBER(10) not null,
  INTAKE_NODE_TEMPLATE_ID     NUMBER(10) not null,
  INTAKE_ANSWER_VALIDATION_ID NUMBER(10),
  DFLT                        NUMBER(1) default '0',
  ELEMENT                     VARCHAR2(255) not null
)
;
alter table INTAKE_ANSWER_ELEMENT
  add primary key (INTAKE_ANSWER_ELEMENT_ID);
alter table INTAKE_ANSWER_ELEMENT
  add constraint FK_INTAKE_ANSWER_ELEMENT_1 foreign key (INTAKE_NODE_TEMPLATE_ID)
  references INTAKE_NODE_TEMPLATE (INTAKE_NODE_TEMPLATE_ID);
alter table INTAKE_ANSWER_ELEMENT
  add constraint FK_INTAKE_ANSWER_ELEMENT_2 foreign key (INTAKE_ANSWER_VALIDATION_ID)
  references INTAKE_ANSWER_VALIDATION (INTAKE_ANSWER_VALIDATION_ID);
create index IDX_INTAKE_ANSWER_ELEMENT_1 on INTAKE_ANSWER_ELEMENT (INTAKE_NODE_TEMPLATE_ID);
create index IDX_INTAKE_ANSWER_ELEMENT_2 on INTAKE_ANSWER_ELEMENT (INTAKE_ANSWER_VALIDATION_ID);

prompt
prompt Creating table INTAKE_NODE_LABEL
prompt ================================
prompt
create table INTAKE_NODE_LABEL
(
  INTAKE_NODE_LABEL_ID NUMBER(10) not null,
  LBL                  VARCHAR2(4000) not null
)
;
alter table INTAKE_NODE_LABEL
  add primary key (INTAKE_NODE_LABEL_ID);

prompt
prompt Creating table ISSUE
prompt ====================
prompt
create table ISSUE
(
  ISSUE_ID    NUMBER(10) not null,
  CODE        VARCHAR2(20) default ' ' not null,
  DESCRIPTION VARCHAR2(255) default ' ' not null,
  ROLE        VARCHAR2(100) default ' ' not null,
  UPDATE_DATE DATE default '01-JAN-1900' not null,
  PRIORITY    VARCHAR2(10)
)
;
alter table ISSUE
  add primary key (ISSUE_ID);

prompt
prompt Creating table ISSUEGROUP
prompt =========================
prompt
create table ISSUEGROUP
(
  ID   NUMBER not null,
  NAME VARCHAR2(255) not null
)
;
alter table ISSUEGROUP
  add primary key (ID);

prompt
prompt Creating table ISSUEGROUPISSUES
prompt ===============================
prompt
create table ISSUEGROUPISSUES
(
  ISSUEGROUPID NUMBER not null,
  ISSUE_ID     NUMBER not null
)
;
create unique index IDX_ISSUEGROUPISSUES_1 on ISSUEGROUPISSUES (ISSUEGROUPID, ISSUE_ID);

prompt
prompt Creating table JOINT_ADMISSIONS
prompt ===============================
prompt
create table JOINT_ADMISSIONS
(
  ID                    NUMBER(11) not null,
  CLIENT_ID             NUMBER(11) default '0' not null,
  TYPE_ID               NUMBER(3) default '0' not null,
  PROVIDER_NO           VARCHAR2(6) default '0' not null,
  ADMISSION_DATE        DATE,
  HEAD_CLIENT_ID        NUMBER(11) default '0' not null,
  ARCHIVED              NUMBER(1) default '0',
  ARCHIVING_PROVIDER_NO VARCHAR2(6)
)
;
alter table JOINT_ADMISSIONS
  add primary key (ID);
create index IDX_JOINT_ADMISSIONS_1 on JOINT_ADMISSIONS (CLIENT_ID);
create index IDX_JOINT_ADMISSIONS_2 on JOINT_ADMISSIONS (HEAD_CLIENT_ID);

prompt
prompt Creating table LABPATIENTPHYSICIANINFO
prompt ======================================
prompt
create table LABPATIENTPHYSICIANINFO
(
  ID                    NUMBER(10) not null,
  LABREPORTINFO_ID      NUMBER(10),
  ACCESSION_NUM         VARCHAR2(64),
  PHYSICIAN_ACCOUNT_NUM VARCHAR2(30),
  SERVICE_DATE          VARCHAR2(10),
  PATIENT_FIRST_NAME    VARCHAR2(100),
  PATIENT_LAST_NAME     VARCHAR2(100),
  PATIENT_SEX           VARCHAR2(1),
  PATIENT_HEALTH_NUM    VARCHAR2(20),
  PATIENT_DOB           VARCHAR2(15),
  LAB_STATUS            VARCHAR2(1),
  DOC_NUM               VARCHAR2(50),
  DOC_NAME              VARCHAR2(100),
  DOC_ADDR1             VARCHAR2(100),
  DOC_ADDR2             VARCHAR2(100),
  DOC_ADDR3             VARCHAR2(100),
  DOC_POSTAL            VARCHAR2(15),
  DOC_ROUTE             VARCHAR2(50),
  COMMENT1              VARCHAR2(4000),
  COMMENT2              VARCHAR2(4000),
  PATIENT_PHONE         VARCHAR2(20),
  DOC_PHONE             VARCHAR2(20),
  COLLECTION_DATE       VARCHAR2(12)
)
;
alter table LABPATIENTPHYSICIANINFO
  add primary key (ID);

prompt
prompt Creating table LABREPORTINFORMATION
prompt ===================================
prompt
create table LABREPORTINFORMATION
(
  ID          NUMBER(10) not null,
  LOCATION_ID VARCHAR2(255),
  PRINT_DATE  VARCHAR2(10),
  PRINT_TIME  VARCHAR2(10),
  TOTAL_BTYPE VARCHAR2(5),
  TOTAL_CTYPE VARCHAR2(5),
  TOTAL_DTYPE VARCHAR2(5)
)
;
alter table LABREPORTINFORMATION
  add primary key (ID);

prompt
prompt Creating table LABTESTRESULTS
prompt =============================
prompt
create table LABTESTRESULTS
(
  ID                         NUMBER(10) not null,
  LABPATIENTPHYSICIANINFO_ID NUMBER(10),
  LINE_TYPE                  VARCHAR2(1),
  TITLE                      VARCHAR2(255),
  NOTUSED1                   VARCHAR2(255),
  NOTUSED2                   VARCHAR2(255),
  TEST_NAME                  VARCHAR2(255),
  ABN                        VARCHAR2(1),
  MINIMUM                    VARCHAR2(65),
  MAXIMUM                    VARCHAR2(65),
  UNITS                      VARCHAR2(65),
  RESULT                     VARCHAR2(65),
  DESCRIPTION                VARCHAR2(4000),
  LOCATION_ID                VARCHAR2(255),
  LAST                       VARCHAR2(1)
)
;
alter table LABTESTRESULTS
  add primary key (ID);

prompt
prompt Creating table LOG
prompt ==================
prompt
create table LOG
(
  DATETIME    TIMESTAMP(6) default current_timestamp not null,
  PROVIDER_NO VARCHAR2(10) not null,
  ACTION      VARCHAR2(20),
  CONTENT     VARCHAR2(80),
  CONTENTID   VARCHAR2(80),
  IP          VARCHAR2(30)
)
;
alter table LOG
  add primary key (DATETIME, PROVIDER_NO);
create index IDX_LOG_1 on LOG (ACTION);
create index IDX_LOG_2 on LOG (CONTENT);
create index IDX_LOG_3 on LOG (CONTENTID);

prompt
prompt Creating table LOG_LETTERS
prompt ==========================
prompt
create table LOG_LETTERS
(
  ID          NUMBER(10) not null,
  DATE_TIME   DATE,
  PROVIDER_NO VARCHAR2(6),
  LOG         VARCHAR2(4000),
  REPORT_ID   NUMBER(10)
)
;
alter table LOG_LETTERS
  add primary key (ID);
create index IDX_LOG_LETTERS_1 on LOG_LETTERS (REPORT_ID);
create index IDX_LOG_LETTERS_2 on LOG_LETTERS (PROVIDER_NO);
create index IDX_LOG_LETTERS_3 on LOG_LETTERS (DATE_TIME);

prompt
prompt Creating table MDSMSH
prompt =====================
prompt
create table MDSMSH
(
  SEGMENTID      NUMBER(10) not null,
  SENDINGAPP     VARCHAR2(180),
  DATETIME       DATE default '01-JAN-1900' not null,
  TYPE           VARCHAR2(7),
  MESSAGECONID   VARCHAR2(20),
  PROCESSINGID   VARCHAR2(3),
  VERSIONID      VARCHAR2(8),
  ACCEPTACKTYPE  VARCHAR2(2),
  APPACKTYPE     VARCHAR2(2),
  DEMOGRAPHIC_NO NUMBER(10) default '0'
)
;
alter table MDSMSH
  add primary key (SEGMENTID);

prompt
prompt Creating table MDSOBR
prompt =====================
prompt
create table MDSOBR
(
  SEGMENTID           NUMBER(10),
  OBRID               NUMBER(10),
  PLACERORDERNO       VARCHAR2(75),
  UNIVERSALSERVICEID  VARCHAR2(200),
  OBSERVATIONDATETIME VARCHAR2(26),
  SPECIMENRECDATETIME VARCHAR2(26),
  FILLERFIELDONE      VARCHAR2(60),
  QUANTITYTIMING      VARCHAR2(200)
)
;
create index IDX_MDSOBR_1 on MDSOBR (SEGMENTID);

prompt
prompt Creating table MDSOBX
prompt =====================
prompt
create table MDSOBX
(
  SEGMENTID               NUMBER(10),
  OBXID                   NUMBER(10) default '0',
  VALUETYPE               VARCHAR2(2),
  OBSERVATIONIDEN         VARCHAR2(80),
  OBSERVATIONSUBID        VARCHAR2(255),
  OBSERVATIONVALUE        VARCHAR2(255),
  ABNORMALFLAGS           VARCHAR2(5),
  OBSERVATIONRESULTSTATUS VARCHAR2(2),
  PRODUCERSID             VARCHAR2(60),
  ASSOCIATEDOBR           NUMBER(10)
)
;
create index IDX_MDSOBX_1 on MDSOBX (SEGMENTID);

prompt
prompt Creating table MDSPID
prompt =====================
prompt
create table MDSPID
(
  SEGMENTID    NUMBER(10),
  INTPATIENTID VARCHAR2(16),
  ALTPATIENTID VARCHAR2(15),
  PATIENTNAME  VARCHAR2(48),
  DOB          VARCHAR2(26),
  SEX          VARCHAR2(1),
  HOMEPHONE    VARCHAR2(40),
  HEALTHNUMBER VARCHAR2(16)
)
;
create index IDX_MDSPID_1 on MDSPID (SEGMENTID);

prompt
prompt Creating table MDSPV1
prompt =====================
prompt
create table MDSPV1
(
  SEGMENTID       NUMBER(10),
  PATIENTCLASS    VARCHAR2(1),
  PATIENTLOCATION VARCHAR2(80),
  REFDOCTOR       VARCHAR2(60),
  CONDOCTOR       VARCHAR2(60),
  ADMDOCTOR       VARCHAR2(60),
  VNUMBER         VARCHAR2(20),
  ACCSTATUS       VARCHAR2(2),
  ADMDATETIME     VARCHAR2(26)
)
;
create index IDX_MDSPV1_1 on MDSPV1 (SEGMENTID);

prompt
prompt Creating table MDSZCL
prompt =====================
prompt
create table MDSZCL
(
  SEGMENTID       NUMBER(10),
  SETID           VARCHAR2(4),
  CONSULTDOC      VARCHAR2(60),
  CLIENTADDRESS   VARCHAR2(106),
  ROUTE           VARCHAR2(6),
  STOP            VARCHAR2(6),
  AREA            VARCHAR2(2),
  REPORTSET       VARCHAR2(1),
  CLIENTTYPE      VARCHAR2(5),
  CLIENTMODEMPOOL VARCHAR2(2),
  CLIENTFAXNUMBER VARCHAR2(40),
  CLIENTBAKFAX    VARCHAR2(40)
)
;
create index IDX_MDSZCL_1 on MDSZCL (SEGMENTID);

prompt
prompt Creating table MDSZCT
prompt =====================
prompt
create table MDSZCT
(
  SEGMENTID           NUMBER(10),
  BARCODEIDENTIFIER   VARCHAR2(14),
  PLACERGROUPNO       VARCHAR2(14),
  OBSERVATIONDATETIME VARCHAR2(26)
)
;
create index IDX_MDSZCT_1 on MDSZCT (SEGMENTID);

prompt
prompt Creating table MDSZFR
prompt =====================
prompt
create table MDSZFR
(
  SEGMENTID        NUMBER(10),
  REPORTFORM       VARCHAR2(1),
  REPORTFORMSTATUS VARCHAR2(1),
  TESTINGLAB       VARCHAR2(5),
  MEDICALDIRECTOR  VARCHAR2(255),
  EDITFLAG         VARCHAR2(255),
  ABNORMALFLAG     VARCHAR2(255)
)
;
create index IDX_MDSZFR_1 on MDSZFR (SEGMENTID);

prompt
prompt Creating table MDSZLB
prompt =====================
prompt
create table MDSZLB
(
  SEGMENTID         NUMBER(10),
  LABID             VARCHAR2(5),
  LABIDVERSION      VARCHAR2(255),
  LABADDRESS        VARCHAR2(100),
  PRIMARYLAB        VARCHAR2(5),
  PRIMARYLABVERSION VARCHAR2(5),
  MDSLU             VARCHAR2(5),
  MDSLV             VARCHAR2(5)
)
;
create index IDX_MDSZLB_1 on MDSZLB (SEGMENTID);

prompt
prompt Creating table MDSZMC
prompt =====================
prompt
create table MDSZMC
(
  SEGMENTID              NUMBER(10),
  SETID                  VARCHAR2(10),
  MESSAGECODEIDENTIFIER  VARCHAR2(10),
  MESSAGECODEVERSION     VARCHAR2(255),
  NOMESSAGECODEDESCLINES VARCHAR2(30),
  SIGFLAG                VARCHAR2(5),
  MESSAGECODEDESC        VARCHAR2(255)
)
;
create index IDX_MDSZMC_1 on MDSZMC (SEGMENTID);

prompt
prompt Creating table MDSZMN
prompt =====================
prompt
create table MDSZMN
(
  SEGMENTID             NUMBER(10),
  RESULTMNEMONIC        VARCHAR2(20),
  RESULTMNEMONICVERSION VARCHAR2(255),
  REPORTNAME            VARCHAR2(255),
  UNITS                 VARCHAR2(60),
  CUMULATIVESEQUENCE    VARCHAR2(255),
  REFERENCERANGE        VARCHAR2(255),
  RESULTCODE            VARCHAR2(20),
  REPORTFORM            VARCHAR2(255),
  REPORTGROUP           VARCHAR2(10),
  REPORTGROUPVERSION    VARCHAR2(255)
)
;
create index IDX_MDSZMN_1 on MDSZMN (SEGMENTID);

prompt
prompt Creating table MDSZRG
prompt =====================
prompt
create table MDSZRG
(
  SEGMENTID          NUMBER(10),
  REPORTSEQUENCE     VARCHAR2(255),
  REPORTGROUPID      VARCHAR2(10),
  REPORTGROUPVERSION VARCHAR2(10),
  REPORTFLAGS        VARCHAR2(255),
  REPORTGROUPDESC    VARCHAR2(30),
  MDSINDEX           VARCHAR2(255),
  REPORTGROUPHEADING VARCHAR2(255)
)
;
create index IDX_MDSZRG_1 on MDSZRG (SEGMENTID);

prompt
prompt Creating table MEASUREMENTCSSLOCATION
prompt =====================================
prompt
create table MEASUREMENTCSSLOCATION
(
  CSSID    NUMBER(9) not null,
  LOCATION VARCHAR2(255) not null
)
;
alter table MEASUREMENTCSSLOCATION
  add primary key (CSSID);

prompt
prompt Creating table MEASUREMENTGROUP
prompt ===============================
prompt
create table MEASUREMENTGROUP
(
  NAME            VARCHAR2(100) not null,
  TYPEDISPLAYNAME VARCHAR2(255)
)
;
create index IDX_MEASUREMENTGROUP_1 on MEASUREMENTGROUP (NAME);

prompt
prompt Creating table MEASUREMENTGROUPSTYLE
prompt ====================================
prompt
create table MEASUREMENTGROUPSTYLE
(
  GROUPID   NUMBER(9) not null,
  GROUPNAME VARCHAR2(100) not null,
  CSSID     NUMBER(9) not null
)
;
alter table MEASUREMENTGROUPSTYLE
  add primary key (GROUPID);

prompt
prompt Creating table MEASUREMENTMAP
prompt =============================
prompt
create table MEASUREMENTMAP
(
  ID         NUMBER(10) not null,
  LOINC_CODE VARCHAR2(20) not null,
  IDENT_CODE VARCHAR2(20) not null,
  NAME       VARCHAR2(255),
  LAB_TYPE   VARCHAR2(10) not null
)
;
alter table MEASUREMENTMAP
  add primary key (ID);
create index IDX_MEASUREMENTMAP_1 on MEASUREMENTMAP (IDENT_CODE);

prompt
prompt Creating table MEASUREMENTS
prompt ===========================
prompt
create table MEASUREMENTS
(
  ID                   NUMBER not null,
  TYPE                 VARCHAR2(4) not null,
  DEMOGRAPHICNO        NUMBER(10) default '0' not null,
  PROVIDERNO           VARCHAR2(6),
  DATAFIELD            VARCHAR2(10) not null,
  MEASURINGINSTRUCTION VARCHAR2(255) not null,
  COMMENTS             VARCHAR2(255) not null,
  DATEOBSERVED         DATE not null,
  DATEENTERED          DATE not null
)
;
alter table MEASUREMENTS
  add primary key (ID);
create index IDX_MEASUREMENTS_1 on MEASUREMENTS (TYPE);
create index IDX_MEASUREMENTS_2 on MEASUREMENTS (MEASURINGINSTRUCTION);
create index IDX_MEASUREMENTS_3 on MEASUREMENTS (DEMOGRAPHICNO);

prompt
prompt Creating table MEASUREMENTSDELETED
prompt ==================================
prompt
create table MEASUREMENTSDELETED
(
  ID                   NUMBER not null,
  TYPE                 VARCHAR2(4) not null,
  DEMOGRAPHICNO        NUMBER(10) default '0' not null,
  PROVIDERNO           VARCHAR2(6),
  DATAFIELD            VARCHAR2(10) not null,
  MEASURINGINSTRUCTION VARCHAR2(255) not null,
  COMMENTS             VARCHAR2(255) not null,
  DATEOBSERVED         DATE not null,
  DATEENTERED          DATE not null,
  DATEDELETED          DATE not null
)
;
alter table MEASUREMENTSDELETED
  add primary key (ID);

prompt
prompt Creating table MEASUREMENTSEXT
prompt ==============================
prompt
create table MEASUREMENTSEXT
(
  ID             NUMBER(10) not null,
  MEASUREMENT_ID NUMBER(10) not null,
  KEYVAL         VARCHAR2(20) not null,
  VAL            VARCHAR2(20) not null
)
;
alter table MEASUREMENTSEXT
  add primary key (ID);
create index IDX_MEASUREMENTSEXT_1 on MEASUREMENTSEXT (MEASUREMENT_ID);
create index IDX_MEASUREMENTSEXT_2 on MEASUREMENTSEXT (VAL);

prompt
prompt Creating table MEASUREMENTTYPE
prompt ==============================
prompt
create table MEASUREMENTTYPE
(
  ID                   NUMBER not null,
  TYPE                 VARCHAR2(4) not null,
  TYPEDISPLAYNAME      VARCHAR2(255) not null,
  TYPEDESCRIPTION      VARCHAR2(255) not null,
  MEASURINGINSTRUCTION VARCHAR2(255) not null,
  VALIDATION           VARCHAR2(100) not null
)
;
alter table MEASUREMENTTYPE
  add primary key (ID);

prompt
prompt Creating table MEASUREMENTTYPEDELETED
prompt =====================================
prompt
create table MEASUREMENTTYPEDELETED
(
  ID                   NUMBER not null,
  TYPE                 VARCHAR2(4) not null,
  TYPEDISPLAYNAME      VARCHAR2(20) not null,
  TYPEDESCRIPTION      VARCHAR2(255) not null,
  MEASURINGINSTRUCTION VARCHAR2(255) not null,
  VALIDATION           VARCHAR2(100) not null,
  DATEDELETED          DATE not null
)
;
alter table MEASUREMENTTYPEDELETED
  add primary key (ID);

prompt
prompt Creating table MESSAGELISTTBL
prompt =============================
prompt
create table MESSAGELISTTBL
(
  MESSAGE        NUMBER(9),
  PROVIDER_NO    VARCHAR2(6),
  STATUS         VARCHAR2(10),
  REMOTELOCATION NUMBER(10)
)
;

prompt
prompt Creating table MESSAGETBL
prompt =========================
prompt
create table MESSAGETBL
(
  MESSAGEID      NUMBER(9) not null,
  THEDATE        DATE,
  THEIME         DATE,
  THEMESSAGE     VARCHAR2(4000),
  THESUBJECT     VARCHAR2(128),
  SENTBY         VARCHAR2(62),
  SENTTO         VARCHAR2(255),
  SENTBYNO       VARCHAR2(6),
  SENTBYLOCATION NUMBER(10),
  ATTACHMENT     VARCHAR2(4000),
  PDFATTACHMENT  VARCHAR2(4000),
  ACTIONSTATUS   VARCHAR2(2)
)
;
alter table MESSAGETBL
  add primary key (MESSAGEID);

prompt
prompt Creating table MSGDEMOMAP
prompt =========================
prompt
create table MSGDEMOMAP
(
  MESSAGEID      NUMBER(9) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null
)
;
alter table MSGDEMOMAP
  add primary key (MESSAGEID, DEMOGRAPHIC_NO);

prompt
prompt Creating table MYGROUP
prompt ======================
prompt
create table MYGROUP
(
  MYGROUP_NO  VARCHAR2(10) not null,
  PROVIDER_NO VARCHAR2(6) not null,
  LAST_NAME   VARCHAR2(30),
  FIRST_NAME  VARCHAR2(30),
  VIEWORDER   VARCHAR2(2)
)
;
alter table MYGROUP
  add primary key (MYGROUP_NO, PROVIDER_NO);

prompt
prompt Creating table OSCARCOMMLOCATIONS
prompt =================================
prompt
create table OSCARCOMMLOCATIONS
(
  LOCATIONID      NUMBER(10) default '0' not null,
  LOCATIONDESC    VARCHAR2(50),
  LOCATIONAUTH    VARCHAR2(30),
  CURRENT1        NUMBER(1) default '0' not null,
  ADDRESSBOOK     VARCHAR2(4000),
  REMOTESERVERURL VARCHAR2(30)
)
;
alter table OSCARCOMMLOCATIONS
  add primary key (LOCATIONID);

prompt
prompt Creating table OSCARKEYS
prompt ========================
prompt
create table OSCARKEYS
(
  NAME    VARCHAR2(100) not null,
  PUBKEY  VARCHAR2(4000),
  PRIVKEY VARCHAR2(4000)
)
;
alter table OSCARKEYS
  add primary key (NAME);

prompt
prompt Creating table PATIENTLABROUTING
prompt ================================
prompt
create table PATIENTLABROUTING
(
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  LAB_NO         NUMBER(10) default '0' not null,
  LAB_TYPE       VARCHAR2(3) default 'MDS' not null,
  ID             NUMBER(10) not null
)
;
alter table PATIENTLABROUTING
  add primary key (ID);
create index IDX_PATIENTLABROUTING_1 on PATIENTLABROUTING (DEMOGRAPHIC_NO);
create index IDX_PATIENTLABROUTING_2 on PATIENTLABROUTING (LAB_TYPE);
create index IDX_PATIENTLABROUTING_3 on PATIENTLABROUTING (LAB_NO);
create index IDX_PATIENTLABROUTING_4 on PATIENTLABROUTING (LAB_TYPE, LAB_NO, DEMOGRAPHIC_NO);

prompt
prompt Creating table PHARMACYINFO
prompt ===========================
prompt
create table PHARMACYINFO
(
  RECORDID   NUMBER(10) not null,
  ID         NUMBER(5),
  NAME       VARCHAR2(255),
  ADDRESS    VARCHAR2(4000),
  CITY       VARCHAR2(255),
  PROVINCE   VARCHAR2(255),
  POSTALCODE VARCHAR2(20),
  PHONE1     VARCHAR2(20),
  PHONE2     VARCHAR2(20),
  FAX        VARCHAR2(20),
  EMAIL      VARCHAR2(100),
  NOTES      VARCHAR2(4000),
  ADDDATE    TIMESTAMP(6),
  STATUS     VARCHAR2(1) default '1'
)
;
alter table PHARMACYINFO
  add primary key (RECORDID);

prompt
prompt Creating table PMM_LOG
prompt ======================
prompt
create table PMM_LOG
(
  ID          NUMBER not null,
  PROVIDER_NO VARCHAR2(255),
  DATETIME    DATE,
  ACTION      VARCHAR2(20),
  CONTENTID   VARCHAR2(80),
  CONTENT     VARCHAR2(80) not null,
  IP          VARCHAR2(30)
)
;
alter table PMM_LOG
  add primary key (ID);

prompt
prompt Creating table PREFERENCE
prompt =========================
prompt
create table PREFERENCE
(
  PREFERENCE_NO              NUMBER(6) not null,
  PROVIDER_NO                VARCHAR2(6) default ' ' not null,
  START_HOUR                 VARCHAR2(2),
  END_HOUR                   VARCHAR2(2),
  EVERY_MIN                  VARCHAR2(3),
  MYGROUP_NO                 VARCHAR2(10),
  COLOR_TEMPLATE             VARCHAR2(10),
  DEFAULT_SERVICETYPE        VARCHAR2(10),
  DEFAULT_CAISI_PMM          VARCHAR2(10) default 'disabled',
  NEW_TICKLER_WARNING_WINDOW VARCHAR2(10)
)
;
alter table PREFERENCE
  add primary key (PREFERENCE_NO);
create index IDX_PREFERENCE_1 on PREFERENCE (PROVIDER_NO);

prompt
prompt Creating table PRESCRIBE
prompt ========================
prompt
create table PRESCRIBE
(
  PRESCRIBE_NO   NUMBER(12) not null,
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  PROVIDER_NO    VARCHAR2(6),
  PRESCRIBE_DATE DATE default '01-JAN-1900' not null,
  PRESCRIBE_TIME DATE default '01-JAN-1900' not null,
  CONTENT        CLOB
)
;
alter table PRESCRIBE
  add primary key (PRESCRIBE_NO);

prompt
prompt Creating table PRESCRIPTION
prompt ===========================
prompt
create table PRESCRIPTION
(
  SCRIPT_NO       NUMBER(10) not null,
  PROVIDER_NO     VARCHAR2(6),
  DEMOGRAPHIC_NO  NUMBER(10),
  DATE_PRESCRIBED DATE,
  DATE_PRINTED    DATE,
  DATES_REPRINTED VARCHAR2(4000),
  TEXTVIEW        VARCHAR2(4000)
)
;
alter table PRESCRIPTION
  add primary key (SCRIPT_NO);

prompt
prompt Creating table PREVENTIONS
prompt ==========================
prompt
create table PREVENTIONS
(
  ID              NUMBER(10) not null,
  DEMOGRAPHIC_NO  NUMBER(10) default '0' not null,
  CREATION_DATE   DATE,
  PREVENTION_DATE DATE,
  PROVIDER_NO     VARCHAR2(6),
  PROVIDER_NAME   VARCHAR2(255),
  PREVENTION_TYPE VARCHAR2(20),
  DELETED         VARCHAR2(1) default '0',
  REFUSED         VARCHAR2(1) default '0',
  NEXT_DATE       DATE,
  NEVER           VARCHAR2(1) default '0',
  CREATOR         NUMBER(10)
)
;
alter table PREVENTIONS
  add primary key (ID);
create index IDX_PREVENTIONS_1 on PREVENTIONS (DEMOGRAPHIC_NO);
create index IDX_PREVENTIONS_2 on PREVENTIONS (PROVIDER_NO);
create index IDX_PREVENTIONS_3 on PREVENTIONS (PREVENTION_TYPE);
create index IDX_PREVENTIONS_4 on PREVENTIONS (REFUSED);
create index IDX_PREVENTIONS_5 on PREVENTIONS (DELETED);
create index IDX_PREVENTIONS_6 on PREVENTIONS (NEVER);
create index IDX_PREVENTIONS_7 on PREVENTIONS (CREATION_DATE);
create index IDX_PREVENTIONS_8 on PREVENTIONS (NEXT_DATE);

prompt
prompt Creating table PREVENTIONSEXT
prompt =============================
prompt
create table PREVENTIONSEXT
(
  ID            NUMBER(10) not null,
  PREVENTION_ID NUMBER(10),
  KEYVAL        VARCHAR2(20),
  VAL           VARCHAR2(4000)
)
;
alter table PREVENTIONSEXT
  add primary key (ID);
create index IDX_PREVENTIONSEXT_1 on PREVENTIONSEXT (PREVENTION_ID);
create index IDX_PREVENTIONSEXT_2 on PREVENTIONSEXT (KEYVAL);

prompt
prompt Creating table PROFESSIONALSPECIALISTS
prompt ======================================
prompt
create table PROFESSIONALSPECIALISTS
(
  SPECID     NUMBER(10) not null,
  FNAME      VARCHAR2(32),
  LNAME      VARCHAR2(32),
  PROLETTERS VARCHAR2(20),
  ADDRESS    VARCHAR2(255),
  PHONE      VARCHAR2(30),
  FAX        VARCHAR2(30),
  WEBSITE    VARCHAR2(128),
  EMAIL      VARCHAR2(128),
  SPECTYPE   VARCHAR2(128)
)
;
alter table PROFESSIONALSPECIALISTS
  add primary key (SPECID);

prompt
prompt Creating table PROGRAM
prompt ======================
prompt
create table PROGRAM
(
  PROGRAM_ID               NUMBER(10) not null,
  USERDEFINED              NUMBER(1) not null,
  INTAKE_PROGRAM           NUMBER,
  NAME                     VARCHAR2(70) default ' ' not null,
  DESCR                    VARCHAR2(255),
  ADDRESS                  VARCHAR2(255) default ' ',
  PHONE                    VARCHAR2(255) default ' ',
  FAX                      VARCHAR2(255) default ' ',
  URL                      VARCHAR2(255) default ' ',
  EMAIL                    VARCHAR2(255) default ' ',
  EMERGENCY_NUMBER         VARCHAR2(255) default '',
  TYPE                     VARCHAR2(50),
  LOCATION                 VARCHAR2(70),
  MAX_ALLOWED              NUMBER(8) default '0',
  NUM_OF_MEMBERS           NUMBER(6) default '0',
  HOLDING_TANK             NUMBER(1) default '0',
  ALLOW_BATCH_ADMISSION    NUMBER(1),
  ALLOW_BATCH_DISCHARGE    NUMBER(1),
  HIC                      NUMBER(1),
  PROGRAM_STATUS           VARCHAR2(8) default 'active' not null,
  BED_PROGRAM_LINK_ID      NUMBER(10) default 0,
  MANORWOMAN               VARCHAR2(6),
  TRANSGENDER              NUMBER(1) default 0,
  FIRSTNATION              NUMBER(1) default 0,
  BEDPROGRAMAFFILIATED     NUMBER(1) default 0,
  ALCOHOL                  NUMBER(1) default 0,
  ABSTINENCESUPPORT        VARCHAR2(20),
  PHYSICALHEALTH           NUMBER(1) default 0,
  MENTALHEALTH             NUMBER(1) default 0,
  HOUSING                  NUMBER(1) default 0,
  EXCLUSIVE_VIEW           VARCHAR2(20) default 'no',
  MAXIMUM_RESTRICTION_DAYS NUMBER(11),
  DEFAULT_RESTRICTION_DAYS NUMBER(11) default 30,
  AGEMIN                   NUMBER default 0 not null,
  AGEMAX                   NUMBER default 0 not null
)
;
alter table PROGRAM
  add primary key (PROGRAM_ID);

prompt
prompt Creating table PROGRAMSIGNATURE
prompt ===============================
prompt
create table PROGRAMSIGNATURE
(
  ID            NUMBER(10) not null,
  PROGRAMID     NUMBER(10) default '0' not null,
  PROGRAMNAME   VARCHAR2(70) default ' ' not null,
  PROVIDERID    VARCHAR2(6) default '0' not null,
  PROVIDERNAME  VARCHAR2(60) default ' ' not null,
  CAISIROLENAME VARCHAR2(255) default ' ' not null,
  UPDATEDATE    DATE default '01-JAN-1900'
)
;
alter table PROGRAMSIGNATURE
  add primary key (ID);

prompt
prompt Creating table PROGRAM_ACCESS
prompt =============================
prompt
create table PROGRAM_ACCESS
(
  ID             NUMBER(20) not null,
  PROGRAM_ID     NUMBER(20),
  ACCESS_TYPE_ID VARCHAR2(255),
  ALL_ROLES      NUMBER(1)
)
;
alter table PROGRAM_ACCESS
  add primary key (ID);
create index IDX_PROGRAM_ACCESS_1 on PROGRAM_ACCESS (ACCESS_TYPE_ID);

prompt
prompt Creating table PROGRAM_ACCESS_ROLES
prompt ===================================
prompt
create table PROGRAM_ACCESS_ROLES
(
  ID      NUMBER(20) default '0' not null,
  ROLE_ID NUMBER(20) default '0' not null
)
;
alter table PROGRAM_ACCESS_ROLES
  add primary key (ID, ROLE_ID);
create index IDX_PROGRAM_ACCESS_ROLES_1 on PROGRAM_ACCESS_ROLES (ID);
create index IDX_PROGRAM_ACCESS_ROLES_2 on PROGRAM_ACCESS_ROLES (ROLE_ID);

prompt
prompt Creating table PROGRAM_CLIENTSTATUS
prompt ===================================
prompt
create table PROGRAM_CLIENTSTATUS
(
  CLIENTSTATUS_ID NUMBER(20) not null,
  NAME            VARCHAR2(255),
  PROGRAM_ID      NUMBER(20)
)
;
alter table PROGRAM_CLIENTSTATUS
  add primary key (CLIENTSTATUS_ID);

prompt
prompt Creating table PROGRAM_CLIENT_RESTRICTION
prompt =========================================
prompt
create table PROGRAM_CLIENT_RESTRICTION
(
  ID             NUMBER(22) not null,
  PROGRAM_ID     NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null,
  PROVIDER_NO    VARCHAR2(6) not null,
  COMMENTS       VARCHAR2(255),
  IS_ENABLED     NUMBER(1) default '1' not null,
  START_DATE     DATE not null,
  END_DATE       DATE not null
)
;
alter table PROGRAM_CLIENT_RESTRICTION
  add primary key (ID);
alter table PROGRAM_CLIENT_RESTRICTION
  add constraint FK_PROGRAM_CLIENT_RESTRICT_1 foreign key (PROGRAM_ID)
  references PROGRAM (PROGRAM_ID);
alter table PROGRAM_CLIENT_RESTRICTION
  add constraint FK_PROGRAM_CLIENT_RESTRICT_3 foreign key (DEMOGRAPHIC_NO)
  references DEMOGRAPHIC (DEMOGRAPHIC_NO);

prompt
prompt Creating table PROGRAM_FUNCTIONAL_USER
prompt ======================================
prompt
create table PROGRAM_FUNCTIONAL_USER
(
  ID           NUMBER(20) not null,
  PROGRAM_ID   NUMBER(20),
  USER_TYPE_ID NUMBER(20),
  PROVIDER_NO  NUMBER(20)
)
;
alter table PROGRAM_FUNCTIONAL_USER
  add primary key (ID);
create index IDX_PROGRAM_FUNCTIONAL_USER_1 on PROGRAM_FUNCTIONAL_USER (USER_TYPE_ID);
create index IDX_PROGRAM_FUNCTIONAL_USER_2 on PROGRAM_FUNCTIONAL_USER (PROVIDER_NO);

prompt
prompt Creating table PROGRAM_PROVIDER
prompt ===============================
prompt
create table PROGRAM_PROVIDER
(
  ID          NUMBER(20) not null,
  PROGRAM_ID  NUMBER(20),
  PROVIDER_NO VARCHAR2(6) not null,
  ROLE_ID     NUMBER(20),
  TEAM_ID     NUMBER(20)
)
;
alter table PROGRAM_PROVIDER
  add primary key (ID);
create index IDX_PROGRAM_PROVIDER_1 on PROGRAM_PROVIDER (ROLE_ID);
create index IDX_PROGRAM_PROVIDER_2 on PROGRAM_PROVIDER (PROVIDER_NO);
create index IDX_PROGRAM_PROVIDER_3 on PROGRAM_PROVIDER (TEAM_ID);

prompt
prompt Creating table PROGRAM_PROVIDER_TEAM
prompt ====================================
prompt
create table PROGRAM_PROVIDER_TEAM
(
  ID  NUMBER(20) default '0' not null,
  ELT NUMBER(20) default '0' not null
)
;
alter table PROGRAM_PROVIDER_TEAM
  add primary key (ID, ELT);
create index IDX_PROGRAM_PROVIDER_TEAM_1 on PROGRAM_PROVIDER_TEAM (ID);
create index IDX_PROGRAM_PROVIDER_TEAM_2 on PROGRAM_PROVIDER_TEAM (ELT);

prompt
prompt Creating table PROGRAM_QUEUE
prompt ============================
prompt
create table PROGRAM_QUEUE
(
  QUEUE_ID                 NUMBER(20) not null,
  CLIENT_ID                NUMBER(20) default '0' not null,
  REFERRAL_DATE            DATE,
  PROVIDER_NO              NUMBER(20) default '0' not null,
  NOTES                    VARCHAR2(255),
  PROGRAM_ID               NUMBER(20) default '0' not null,
  STATUS                   VARCHAR2(30),
  REFERRAL_ID              NUMBER(20),
  TEMPORARY_ADMISSION_FLAG NUMBER(1),
  PRESENT_PROBLEMS         VARCHAR2(255)
)
;
alter table PROGRAM_QUEUE
  add primary key (QUEUE_ID);

prompt
prompt Creating table PROGRAM_TEAM
prompt ===========================
prompt
create table PROGRAM_TEAM
(
  TEAM_ID    NUMBER(20) not null,
  NAME       VARCHAR2(255),
  PROGRAM_ID NUMBER(20)
)
;
alter table PROGRAM_TEAM
  add primary key (TEAM_ID);

prompt
prompt Creating table PROPERTY
prompt =======================
prompt
create table PROPERTY
(
  NAME        VARCHAR2(255) not null,
  VALUE       VARCHAR2(255),
  ID          NUMBER(10) not null,
  PROVIDER_NO VARCHAR2(6)
)
;
alter table PROPERTY
  add primary key (ID);

prompt
prompt Creating table PROVIDER
prompt =======================
prompt
create table PROVIDER
(
  PROVIDER_NO       VARCHAR2(6) not null,
  LAST_NAME         VARCHAR2(30),
  FIRST_NAME        VARCHAR2(30),
  PROVIDER_TYPE     VARCHAR2(15),
  SPECIALTY         VARCHAR2(20),
  TEAM              VARCHAR2(20),
  SEX               VARCHAR2(1),
  DOB               DATE,
  ADDRESS           VARCHAR2(40),
  PHONE             VARCHAR2(20),
  WORK_PHONE        VARCHAR2(50),
  OHIP_NO           VARCHAR2(20),
  RMA_NO            VARCHAR2(20),
  BILLING_NO        VARCHAR2(20),
  HSO_NO            VARCHAR2(10),
  STATUS            VARCHAR2(1),
  COMMENTS          VARCHAR2(4000),
  PROVIDER_ACTIVITY VARCHAR2(3)
)
;
alter table PROVIDER
  add primary key (PROVIDER_NO);

prompt
prompt Creating table PROVIDERBILLCENTER
prompt =================================
prompt
create table PROVIDERBILLCENTER
(
  PROVIDER_NO     VARCHAR2(6) not null,
  BILLCENTER_CODE VARCHAR2(2) not null
)
;
alter table PROVIDERBILLCENTER
  add constraint PK_PROVIDERBILLCENTER primary key (PROVIDER_NO);

prompt
prompt Creating table PROVIDEREXT
prompt ==========================
prompt
create table PROVIDEREXT
(
  PROVIDER_NO VARCHAR2(6),
  SIGNATURE   VARCHAR2(255)
)
;

prompt
prompt Creating table PROVIDERLABROUTING
prompt =================================
prompt
create table PROVIDERLABROUTING
(
  PROVIDER_NO VARCHAR2(6) default ' ' not null,
  LAB_NO      NUMBER(10) default '0' not null,
  STATUS      VARCHAR2(1) default ' ',
  comment     VARCHAR2(255) default ' ',
  TIMESTAMP   TIMESTAMP(6) not null,
  LAB_TYPE    VARCHAR2(3) default 'MDS',
  ID          NUMBER(10) not null
)
;
alter table PROVIDERLABROUTING
  add primary key (ID);

prompt
prompt Creating table PROVIDER_DEFAULT_PROGRAM
prompt =======================================
prompt
create table PROVIDER_DEFAULT_PROGRAM
(
  ID          NUMBER(10) not null,
  PROVIDER_NO VARCHAR2(6) default ' ' not null,
  PROGRAM_ID  NUMBER(10) default '0' not null,
  SIGNNOTE    NUMBER(1) default '0'
)
;
alter table PROVIDER_DEFAULT_PROGRAM
  add primary key (ID);

prompt
prompt Creating table PUBLICKEYS
prompt =========================
prompt
create table PUBLICKEYS
(
  SERVICE VARCHAR2(100) not null,
  TYPE    VARCHAR2(20) not null,
  PUBKEY  VARCHAR2(4000) not null
)
;
alter table PUBLICKEYS
  add primary key (SERVICE);

prompt
prompt Creating table QUICKLIST
prompt ========================
prompt
create table QUICKLIST
(
  QUICKLISTNAME     VARCHAR2(255) not null,
  CREATEDBYPROVIDER NUMBER(10),
  DXRESEARCHCODE    VARCHAR2(10),
  CODINGSYSTEM      VARCHAR2(20)
)
;

prompt
prompt Creating table QUICKLISTUSER
prompt ============================
prompt
create table QUICKLISTUSER
(
  PROVIDERNO    NUMBER(10) not null,
  QUICKLISTNAME VARCHAR2(10) not null,
  LASTUSED      DATE
)
;

prompt
prompt Creating table RADETAIL
prompt =======================
prompt
create table RADETAIL
(
  RADETAIL_NO     NUMBER(6) not null,
  RAHEADER_NO     NUMBER(6) default '0' not null,
  PROVIDEROHIP_NO VARCHAR2(12) default '' not null,
  BILLING_NO      NUMBER(6) default '0' not null,
  SERVICE_CODE    VARCHAR2(5),
  SERVICE_COUNT   VARCHAR2(2),
  HIN             VARCHAR2(12),
  AMOUNTCLAIM     VARCHAR2(8),
  AMOUNTPAY       VARCHAR2(8),
  SERVICE_DATE    VARCHAR2(12),
  ERROR_CODE      VARCHAR2(2),
  BILLTYPE        VARCHAR2(3)
)
;
alter table RADETAIL
  add primary key (RADETAIL_NO);

prompt
prompt Creating table RAHEADER
prompt =======================
prompt
create table RAHEADER
(
  RAHEADER_NO NUMBER(6) not null,
  FILENAME    VARCHAR2(12),
  PAYMENTDATE VARCHAR2(8),
  PAYABLE     VARCHAR2(30),
  TOTALAMOUNT VARCHAR2(10),
  RECORDS     VARCHAR2(5),
  CLAIMS      VARCHAR2(5),
  STATUS      VARCHAR2(1),
  READDATE    VARCHAR2(12),
  CONTENT     VARCHAR2(4000)
)
;
alter table RAHEADER
  add primary key (RAHEADER_NO);

prompt
prompt Creating table RECYCLEBIN
prompt =========================
prompt
create table RECYCLEBIN
(
  RECYCLEBIN_NO  NUMBER(12) not null,
  PROVIDER_NO    VARCHAR2(6),
  UPDATEDATETIME DATE,
  TABLE_NAME     VARCHAR2(30),
  KEYWORD        VARCHAR2(50),
  TABLE_CONTENT  VARCHAR2(4000)
)
;
alter table RECYCLEBIN
  add primary key (RECYCLEBIN_NO);
create index IDX_RECYCLEBIN_1 on RECYCLEBIN (KEYWORD);

prompt
prompt Creating table RECYCLE_BIN
prompt ==========================
prompt
create table RECYCLE_BIN
(
  PROVIDER_NO    VARCHAR2(6),
  TABLE_NAME     VARCHAR2(30),
  TABLE_CONTENT  VARCHAR2(4000),
  UPDATEDATETIME DATE
)
;

prompt
prompt Creating table REDIRECTLINK
prompt ===========================
prompt
create table REDIRECTLINK
(
  ID  NUMBER not null,
  URL VARCHAR2(255) not null
)
;
alter table REDIRECTLINK
  add primary key (ID);

prompt
prompt Creating table REHABSTUDY2004
prompt =============================
prompt
create table REHABSTUDY2004
(
  STUDYID        NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) not null
)
;
alter table REHABSTUDY2004
  add primary key (STUDYID);

prompt
prompt Creating table RELATIONSHIPS
prompt ============================
prompt
create table RELATIONSHIPS
(
  ID                      NUMBER(10) not null,
  DEMOGRAPHIC_NO          NUMBER(10) default '0' not null,
  RELATION_DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  RELATION                VARCHAR2(20),
  CREATION_DATE           DATE,
  CREATOR                 VARCHAR2(6),
  SUB_DECISION_MAKER      VARCHAR2(1) default '0',
  EMERGENCY_CONTACT       VARCHAR2(1) default '0',
  NOTES                   VARCHAR2(4000),
  DELETED                 VARCHAR2(1) default '0'
)
;
alter table RELATIONSHIPS
  add primary key (ID);

prompt
prompt Creating table REPORT
prompt =====================
prompt
create table REPORT
(
  REPORTNO      NUMBER(10) not null,
  TITLE         VARCHAR2(80),
  DESCRIPTION   VARCHAR2(255),
  ORGAPPLICABLE NUMBER(1),
  REPORTTYPE    VARCHAR2(3),
  DATEOPTION    VARCHAR2(1),
  DATEPART      VARCHAR2(1),
  REPORTGROUP   NUMBER,
  NOTES         VARCHAR2(1024),
  TABLENAME     VARCHAR2(30),
  UPDATEDBY     VARCHAR2(20),
  UPDATEDDATE   DATE,
  SPTORUN       VARCHAR2(32)
)
;
alter table REPORT
  add constraint PK_REPORT primary key (REPORTNO);

prompt
prompt Creating table REPORTAGESEX
prompt ===========================
prompt
create table REPORTAGESEX
(
  DEMOGRAPHIC_NO NUMBER(10),
  AGE            NUMBER(4) default '0',
  ROSTER         VARCHAR2(4),
  SEX            VARCHAR2(2),
  PROVIDER_NO    VARCHAR2(6),
  REPORTDATE     DATE,
  STATUS         VARCHAR2(2),
  DATE_JOINED    DATE default '01-JAN-1900'
)
;

prompt
prompt Creating table REPORTBYEXAMPLESFAVORITE
prompt =======================================
prompt
create table REPORTBYEXAMPLESFAVORITE
(
  ID         NUMBER(9) not null,
  PROVIDERNO VARCHAR2(6) not null,
  QUERY      BLOB not null,
  NAME       VARCHAR2(255) not null
)
;
alter table REPORTBYEXAMPLESFAVORITE
  add primary key (ID);

prompt
prompt Creating table REPORTCONFIG
prompt ===========================
prompt
create table REPORTCONFIG
(
  ID         NUMBER(7) not null,
  REPORT_ID  NUMBER(5),
  NAME       VARCHAR2(80),
  CAPTION    VARCHAR2(80),
  ORDER_NO   NUMBER(3),
  TABLE_NAME VARCHAR2(80),
  SAVE       VARCHAR2(80) default 'default' not null
)
;
alter table REPORTCONFIG
  add primary key (ID);
create unique index IDX_REPORTCONFIG_1 on REPORTCONFIG (REPORT_ID);
create unique index IDX_REPORTCONFIG_2 on REPORTCONFIG (NAME);

prompt
prompt Creating table REPORTFILTER
prompt ===========================
prompt
create table REPORTFILTER
(
  ID          NUMBER(7) not null,
  REPORT_ID   NUMBER(5),
  DESCRIPTION VARCHAR2(4000),
  VALUE       VARCHAR2(255),
  POSITION    VARCHAR2(80),
  STATUS      NUMBER(1) default 1,
  ORDER_NO    NUMBER(3),
  JAVASCRIPT  VARCHAR2(4000),
  DATE_FORMAT VARCHAR2(20)
)
;
alter table REPORTFILTER
  add primary key (ID);
create unique index IDX_REPORTFILTER_1 on REPORTFILTER (REPORT_ID, ORDER_NO);
create index IDX_REPORTFILTER_2 on REPORTFILTER (REPORT_ID);

prompt
prompt Creating table REPORTITEM
prompt =========================
prompt
create table REPORTITEM
(
  ID          NUMBER(5) not null,
  REPORT_NAME VARCHAR2(80),
  STATUS      NUMBER(1) default 1
)
;
alter table REPORTITEM
  add primary key (ID);

prompt
prompt Creating table REPORTPROVIDER
prompt =============================
prompt
create table REPORTPROVIDER
(
  PROVIDER_NO VARCHAR2(10),
  TEAM        VARCHAR2(10),
  ACTION      VARCHAR2(20),
  STATUS      VARCHAR2(1)
)
;

prompt
prompt Creating table REPORTTABLEFIELDCAPTION
prompt ======================================
prompt
create table REPORTTABLEFIELDCAPTION
(
  ID         NUMBER(7) not null,
  TABLE_NAME VARCHAR2(80),
  NAME       VARCHAR2(80),
  CAPTION    VARCHAR2(80)
)
;
alter table REPORTTABLEFIELDCAPTION
  add primary key (ID);
create unique index IDX_REPORTTABLEFIELDCAPTION_1 on REPORTTABLEFIELDCAPTION (TABLE_NAME, NAME);

prompt
prompt Creating table REPORTTEMP
prompt =========================
prompt
create table REPORTTEMP
(
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  EDB            DATE default '01-JAN-1900' not null,
  DEMO_NAME      VARCHAR2(60),
  PROVIDER_NO    VARCHAR2(6),
  ADDRESS        VARCHAR2(4000),
  CREATOR        VARCHAR2(10)
)
;
alter table REPORTTEMP
  add primary key (DEMOGRAPHIC_NO, EDB);

prompt
prompt Creating table REPORTTEMPLATES
prompt ==============================
prompt
create table REPORTTEMPLATES
(
  TEMPLATEID          NUMBER(11) not null,
  TEMPLATETITLE       VARCHAR2(80),
  TEMPLATEDESCRIPTION VARCHAR2(4000) not null,
  TEMPLATESQL         VARCHAR2(4000) not null,
  TEMPLATEXML         VARCHAR2(4000) not null,
  ACTIVE              NUMBER(10) default 1 not null
)
;
alter table REPORTTEMPLATES
  add primary key (TEMPLATEID);

prompt
prompt Creating table REPORT_DATE
prompt ==========================
prompt
create table REPORT_DATE
(
  SESSIONID   VARCHAR2(32) not null,
  STARTDATE   DATE,
  ENDDATE     DATE,
  ASOFDATE    DATE,
  STARTDATE_S VARCHAR2(8),
  ENDDATE_S   VARCHAR2(8),
  ASOFDATE_S  VARCHAR2(8)
)
;
alter table REPORT_DATE
  add constraint PK_REPORT_DATE primary key (SESSIONID);

prompt
prompt Creating table REPORT_DATE_SP
prompt =============================
prompt
create table REPORT_DATE_SP
(
  REPORTNO    NUMBER not null,
  STARTDATE   DATE,
  ENDDATE     DATE,
  ASOFDATE    DATE,
  STARTDATE_S VARCHAR2(8),
  ENDDATE_S   VARCHAR2(8),
  ASOFDATE_S  VARCHAR2(8),
  SPTORUN     VARCHAR2(32)
)
;
alter table REPORT_DATE_SP
  add constraint PK_REPORT_DATE_SP_REPORTNO primary key (REPORTNO);

prompt
prompt Creating table REPORT_DOCTEXT
prompt =============================
prompt
create table REPORT_DOCTEXT
(
  DOCID       NUMBER(10) not null,
  DOCDATA     BLOB,
  REVDATETIME TIMESTAMP(6)
)
;
alter table REPORT_DOCTEXT
  add constraint PRI_DOCTEXT primary key (DOCID);

prompt
prompt Creating table REPORT_DOCUMENT
prompt ==============================
prompt
create table REPORT_DOCUMENT
(
  DOCID          NUMBER(10) not null,
  SUBJECT        VARCHAR2(256),
  PRIVACYCD      VARCHAR2(3),
  OWNERID        VARCHAR2(12),
  CHECKOUTYN     VARCHAR2(1),
  CHECKOUTUSERID VARCHAR2(12),
  CHECKOUTDATE   DATE,
  DOCTYPE        VARCHAR2(3),
  FILENAME       VARCHAR2(128),
  MODULEID       VARCHAR2(6),
  REFNO          VARCHAR2(12),
  FILETYPE       VARCHAR2(5),
  VIEWID         VARCHAR2(36),
  VIEWREFNO      VARCHAR2(12),
  REVDATETIME    DATE
)
;
alter table REPORT_DOCUMENT
  add constraint PRI_REPORT_DOCUMENT primary key (DOCID);

prompt
prompt Creating table REPORT_FILTER
prompt ============================
prompt
create table REPORT_FILTER
(
  REPORTNO          NUMBER(10) not null,
  FIELDNO           NUMBER(10) not null,
  FIELDNAME         VARCHAR2(32),
  FIELDDESC         VARCHAR2(80),
  FIELDTYPE         VARCHAR2(10),
  LOOKUP_TABLE      VARCHAR2(30),
  ISCROSSTABHEADERS VARCHAR2(1),
  OPERATOR          VARCHAR2(10),
  LOOKUP_TREE       VARCHAR2(1),
  FIELDSQL          VARCHAR2(32),
  LOOKUP_SCRIPT     VARCHAR2(50),
  NOTE              VARCHAR2(128),
  VALUEFORMAT       VARCHAR2(32)
)
;
alter table REPORT_FILTER
  add constraint PK_REPORT_FILTER primary key (FIELDNO);

prompt
prompt Creating table REPORT_LETTERS
prompt =============================
prompt
create table REPORT_LETTERS
(
  ID          NUMBER(10) not null,
  PROVIDER_NO VARCHAR2(6),
  REPORT_NAME VARCHAR2(255),
  FILE_NAME   VARCHAR2(255),
  REPORT_FILE BLOB,
  DATE_TIME   DATE,
  ARCHIVE     VARCHAR2(1) default 0
)
;
alter table REPORT_LETTERS
  add primary key (ID);
create index IDX_REPORT_LETTERS_1 on REPORT_LETTERS (ARCHIVE);
create index IDX_REPORT_LETTERS_2 on REPORT_LETTERS (PROVIDER_NO);
create index IDX_REPORT_LETTERS_3 on REPORT_LETTERS (DATE_TIME);

prompt
prompt Creating table REPORT_LK_REPORTGROUP
prompt ====================================
prompt
create table REPORT_LK_REPORTGROUP
(
  ID           NUMBER not null,
  DESCRIPTION  VARCHAR2(40),
  SHORTDESC    VARCHAR2(10),
  ACTIVEYN     VARCHAR2(1),
  ORDERBYINDEX NUMBER,
  NOTE         VARCHAR2(128)
)
;
alter table REPORT_LK_REPORTGROUP
  add constraint PRI_REP_LK_REPORTGROUP primary key (ID);

prompt
prompt Creating table REPORT_OPTION
prompt ============================
prompt
create table REPORT_OPTION
(
  REPORTNO       NUMBER(10) not null,
  REPORTOPTIONID NUMBER(10) not null,
  OPTIONTITLE    VARCHAR2(120),
  LONGDESC       VARCHAR2(120),
  ACTIVEYN       NUMBER(1),
  DEFAULTYN      NUMBER(1),
  DATEFIELD      VARCHAR2(32),
  DATEFIELDDESC  VARCHAR2(32),
  SQLWHERE       VARCHAR2(512),
  SQLORDERBY     VARCHAR2(512),
  RPTFILENAME    VARCHAR2(128),
  RPTFILENO      NUMBER(10),
  RPTVERSION     DATE,
  DATEFIELDTYPE  VARCHAR2(3)
)
;
alter table REPORT_OPTION
  add constraint PK_REPORT_OPTION primary key (REPORTOPTIONID);

prompt
prompt Creating table REPORT_QGVIEWFIELD
prompt =================================
prompt
create table REPORT_QGVIEWFIELD
(
  QGVIEWNO      NUMBER(10) not null,
  FIELDNAME     VARCHAR2(32) not null,
  FIELDNO       NUMBER(10) not null,
  DESCRIPTION   VARCHAR2(80),
  FIELDTYPECODE VARCHAR2(8),
  NUMBERMASK    VARCHAR2(16),
  FIELDLENGTH   NUMBER(10),
  SOURCETXT     VARCHAR2(1024),
  NOTE          VARCHAR2(256),
  GROUPRANK     NUMBER(10),
  LOOKUPTABLE   VARCHAR2(6)
)
;
alter table REPORT_QGVIEWFIELD
  add constraint PK_QGR_QGVIEWFIELD primary key (QGVIEWNO, FIELDNO);

prompt
prompt Creating table REPORT_QGVIEWSUMMARY
prompt ===================================
prompt
create table REPORT_QGVIEWSUMMARY
(
  QGVIEWNO    NUMBER(10) not null,
  QGVIEWCODE  VARCHAR2(30) not null,
  DESCRIPTION VARCHAR2(80),
  GROUPCODE   VARCHAR2(10),
  MASTERTYPE  VARCHAR2(1),
  UPDATEDBY   VARCHAR2(12),
  UPDATEDDATE DATE,
  NOTE        VARCHAR2(512),
  ACTIVEYN    VARCHAR2(1),
  SECUREYN    VARCHAR2(1),
  DBENTITY    VARCHAR2(40),
  REFVIEWS    VARCHAR2(512),
  RELATIONS   VARCHAR2(2048),
  FILTERS     VARCHAR2(1024),
  OBJECT_TYPE VARCHAR2(5),
  DISTINCTYN  VARCHAR2(1)
)
;
alter table REPORT_QGVIEWSUMMARY
  add constraint PK_QGR_QGVIEWSUMMARY primary key (QGVIEWNO);

prompt
prompt Creating table REPORT_ROLE
prompt ==========================
prompt
create table REPORT_ROLE
(
  REPORTNO    NUMBER(10) not null,
  ROLECODE    VARCHAR2(20) not null,
  ACCESS_TYPE VARCHAR2(1)
)
;
alter table REPORT_ROLE
  add constraint PK_REPORT_ROLE primary key (REPORTNO, ROLECODE);

prompt
prompt Creating table REPORT_TEMPLATE
prompt ==============================
prompt
create table REPORT_TEMPLATE
(
  TEMPLATENO     NUMBER(10) not null,
  REPORTNO       NUMBER(10) not null,
  REPORTOPTIONID NUMBER(10),
  DESCRIPTION    VARCHAR2(120),
  STARTDATE      DATE,
  ENDDATE        DATE,
  STARTPAYPERIOD VARCHAR2(10),
  ENDPAYPERIOD   VARCHAR2(10),
  LOGINID        VARCHAR2(20),
  UPDATEDATE     DATE,
  PRIVATEYN      NUMBER(1) not null
)
;
alter table REPORT_TEMPLATE
  add constraint PK_REPORT_TEMPLATE primary key (TEMPLATENO);

prompt
prompt Creating table REPORT_TEMPLATE_CRITERIA
prompt =======================================
prompt
create table REPORT_TEMPLATE_CRITERIA
(
  COUNTER    NUMBER(10) not null,
  TEMPLATENO NUMBER(10) not null,
  RELATION   VARCHAR2(10),
  FIELDNO    NUMBER(10),
  OPERATOR   VARCHAR2(10),
  OPERATORS  VARCHAR2(10),
  VAL        VARCHAR2(40),
  VALDESC    VARCHAR2(120),
  REQUIRED   NUMBER(1)
)
;
alter table REPORT_TEMPLATE_CRITERIA
  add constraint PK_REPORT_TEMPLATE_CRITERIA primary key (TEMPLATENO, COUNTER);

prompt
prompt Creating table REPORT_TEMPLATE_ORG
prompt ==================================
prompt
create table REPORT_TEMPLATE_ORG
(
  COUNTER    NUMBER(10) not null,
  TEMPLATENO NUMBER(10) not null,
  ORGCD      VARCHAR2(72)
)
;
alter table REPORT_TEMPLATE_ORG
  add constraint PK_REPORT_TEMPLATE_ORG primary key (TEMPLATENO, COUNTER);

prompt
prompt Creating table ROOM
prompt ===================
prompt
create table ROOM
(
  ROOM_ID      NUMBER(10) not null,
  ROOM_TYPE_ID NUMBER(10) default '1' not null,
  PROGRAM_ID   NUMBER(10) default 0,
  NAME         VARCHAR2(45),
  FLOOR        VARCHAR2(45),
  ACTIVE       NUMBER(1) default '1' not null,
  FACILITY_ID  NUMBER(10) not null,
  ASSIGNED_BED NUMBER(1) default 1 not null,
  OCCUPANCY    NUMBER(10) default 0 not null
)
;
alter table ROOM
  add primary key (ROOM_ID);
alter table ROOM
  add constraint FK_ROOM_1 foreign key (FACILITY_ID)
  references FACILITY (ID);

prompt
prompt Creating table ROOM_BED
prompt =======================
prompt
create table ROOM_BED
(
  ROOM_ID      NUMBER(10) default 0 not null,
  BED_ID       NUMBER(10) default 0 not null,
  ASSIGN_START DATE default '01-Jan-1900',
  ASSIGN_END   DATE default '01-Jan-1900',
  comments     VARCHAR2(50)
)
;
alter table ROOM_BED
  add primary key (ROOM_ID, BED_ID);

prompt
prompt Creating table ROOM_BED_HISTORICAL
prompt ==================================
prompt
create table ROOM_BED_HISTORICAL
(
  ROOM_ID       NUMBER(10) not null,
  BED_ID        NUMBER(10) not null,
  CONTAIN_START DATE not null,
  CONTAIN_END   DATE not null
)
;
alter table ROOM_BED_HISTORICAL
  add primary key (ROOM_ID, BED_ID, CONTAIN_START);

prompt
prompt Creating table ROOM_DEMOGRAPHIC
prompt ===============================
prompt
create table ROOM_DEMOGRAPHIC
(
  ROOM_ID        NUMBER(10) default '0' not null,
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  PROVIDER_NO    VARCHAR2(6),
  ASSIGN_START   DATE default '01-Jan-1900',
  ASSIGN_END     DATE default '01-Jan-1900',
  COMMENTS       VARCHAR2(50)
)
;
alter table ROOM_DEMOGRAPHIC
  add primary key (ROOM_ID, DEMOGRAPHIC_NO);

prompt
prompt Creating table ROOM_TYPE
prompt ========================
prompt
create table ROOM_TYPE
(
  ROOM_TYPE_ID NUMBER(10) not null,
  NAME         VARCHAR2(45) default '',
  DFLT         NUMBER(1) default '0' not null
)
;
alter table ROOM_TYPE
  add primary key (ROOM_TYPE_ID);

prompt
prompt Creating table RSCHEDULE
prompt ========================
prompt
create table RSCHEDULE
(
  ID          NUMBER(6) not null,
  PROVIDER_NO VARCHAR2(6),
  SDATE       DATE default '01-JAN-1900' not null,
  EDATE       DATE,
  AVAILABLE   VARCHAR2(1),
  DAY_OF_WEEK VARCHAR2(30),
  AVAIL_HOURB VARCHAR2(255),
  AVAIL_HOUR  VARCHAR2(230),
  CREATOR     VARCHAR2(50),
  STATUS      VARCHAR2(1)
)
;
alter table RSCHEDULE
  add primary key (ID);

prompt
prompt Creating table SCHEDULEDATE
prompt ===========================
prompt
create table SCHEDULEDATE
(
  ID          NUMBER(6) not null,
  SDATE       DATE default '01-JAN-1900' not null,
  PROVIDER_NO VARCHAR2(6),
  AVAILABLE   VARCHAR2(1),
  PRIORITY    VARCHAR2(1),
  REASON      VARCHAR2(255),
  HOUR        VARCHAR2(255),
  CREATOR     VARCHAR2(50),
  STATUS      VARCHAR2(1)
)
;
alter table SCHEDULEDATE
  add primary key (ID);

prompt
prompt Creating table SCHEDULEDAYTEMPLATE
prompt ==================================
prompt
create table SCHEDULEDAYTEMPLATE
(
  PROVIDER_NO   VARCHAR2(6) not null,
  DAY           DATE default '01-JAN-1900' not null,
  TEMPLATE_NAME VARCHAR2(20)
)
;
alter table SCHEDULEDAYTEMPLATE
  add primary key (PROVIDER_NO, DAY);

prompt
prompt Creating table SCHEDULEHOLIDAY
prompt ==============================
prompt
create table SCHEDULEHOLIDAY
(
  SDATE        DATE default '01-JAN-1900' not null,
  HOLIDAY_NAME VARCHAR2(100)
)
;
alter table SCHEDULEHOLIDAY
  add primary key (SDATE);

prompt
prompt Creating table SCHEDULETEMPLATE
prompt ===============================
prompt
create table SCHEDULETEMPLATE
(
  PROVIDER_NO VARCHAR2(6) not null,
  NAME        VARCHAR2(20) not null,
  SUMMARY     VARCHAR2(80),
  TIMECODE    VARCHAR2(4000)
)
;
alter table SCHEDULETEMPLATE
  add primary key (PROVIDER_NO, NAME);

prompt
prompt Creating table SCHEDULETEMPLATECODE
prompt ===================================
prompt
create table SCHEDULETEMPLATECODE
(
  CODE        VARCHAR2(1),
  DESCRIPTION VARCHAR2(80),
  DURATION    VARCHAR2(3),
  COLOR       VARCHAR2(10),
  CONFIRM     VARCHAR2(3) default 'No' not null
)
;

prompt
prompt Creating table SCRATCH_PAD
prompt ==========================
prompt
create table SCRATCH_PAD
(
  ID           NUMBER(10) not null,
  PROVIDER_NO  VARCHAR2(6),
  DATE_TIME    DATE,
  SCRATCH_TEXT VARCHAR2(4000)
)
;
alter table SCRATCH_PAD
  add primary key (ID);

prompt
prompt Creating table SECOBJECTNAME
prompt ============================
prompt
create table SECOBJECTNAME
(
  OBJECTNAME VARCHAR2(100) not null
)
;
alter table SECOBJECTNAME
  add primary key (OBJECTNAME);

prompt
prompt Creating table SECOBJPRIVILEGE
prompt ==============================
prompt
create table SECOBJPRIVILEGE
(
  ROLEUSERGROUP VARCHAR2(30) not null,
  OBJECTNAME    VARCHAR2(100) not null,
  PRIVILEGE     VARCHAR2(100) default '|0|' not null,
  PRIORITY      NUMBER(2) default '0',
  PROVIDER_NO   VARCHAR2(6)
)
;
alter table SECOBJPRIVILEGE
  add primary key (ROLEUSERGROUP, OBJECTNAME);

prompt
prompt Creating table SECPRIVILEGE
prompt ===========================
prompt
create table SECPRIVILEGE
(
  ID          NUMBER(2) not null,
  PRIVILEGE   VARCHAR2(5) default '0' not null,
  DESCRIPTION VARCHAR2(80)
)
;
alter table SECPRIVILEGE
  add primary key (ID);
create unique index IDX_SECPRIVILEGE_1 on SECPRIVILEGE (PRIVILEGE);

prompt
prompt Creating table SECROLE
prompt ======================
prompt
create table SECROLE
(
  ROLE_NO   NUMBER(3) not null,
  ROLE_NAME VARCHAR2(30) default ' '
)
;
alter table SECROLE
  add primary key (ROLE_NO);
create unique index IDX_SECROLE_1 on SECROLE (ROLE_NAME);

prompt
prompt Creating table SECURITY
prompt =======================
prompt
create table SECURITY
(
  SECURITY_NO     NUMBER(6) not null,
  USER_NAME       VARCHAR2(30),
  PASSWORD        VARCHAR2(80),
  PROVIDER_NO     VARCHAR2(6),
  PIN             VARCHAR2(6),
  B_REMOTELOCKSET NUMBER(1) default 1,
  B_LOCALLOCKSET  NUMBER(1) default 1,
  DATE_EXPIREDATE DATE default '01-JAN-2999',
  B_EXPIRESET     NUMBER(1) default 1
)
;
alter table SECURITY
  add primary key (SECURITY_NO);
create unique index IDX_SECURITY_1 on SECURITY (USER_NAME);

prompt
prompt Creating table SECUSERROLE
prompt ==========================
prompt
create table SECUSERROLE
(
  PROVIDER_NO VARCHAR2(6) not null,
  ROLE_NAME   VARCHAR2(30) not null
)
;
alter table SECUSERROLE
  add primary key (PROVIDER_NO, ROLE_NAME);

prompt
prompt Creating table SERVICESPECIALISTS
prompt =================================
prompt
create table SERVICESPECIALISTS
(
  SERVICEID NUMBER(10),
  SPECID    NUMBER(10)
)
;

prompt
prompt Creating table SPECIALISTSJAVASCRIPT
prompt ====================================
prompt
create table SPECIALISTSJAVASCRIPT
(
  SETID            VARCHAR2(1),
  JAVASCRIPTSTRING VARCHAR2(2048)
)
;

prompt
prompt Creating table STUDY
prompt ====================
prompt
create table STUDY
(
  STUDY_NO         NUMBER not null,
  STUDY_NAME       VARCHAR2(20),
  STUDY_LINK       VARCHAR2(255),
  DESCRIPTION      VARCHAR2(255),
  FORM_NAME        VARCHAR2(30),
  CURRENT1         NUMBER default '0',
  REMOTE_SERVERURL VARCHAR2(50),
  PROVIDER_NO      VARCHAR2(6),
  TIMESTAMP        TIMESTAMP(6) default CURRENT_TIMESTAMP
)
;
alter table STUDY
  add constraint PK_STUDY primary key (STUDY_NO);

prompt
prompt Creating table STUDYDATA
prompt ========================
prompt
create table STUDYDATA
(
  STUDYDATA_NO   NUMBER(10) not null,
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  STUDY_NO       NUMBER(3) default '0' not null,
  PROVIDER_NO    VARCHAR2(6),
  TIMESTAMP      TIMESTAMP(6) not null,
  STATUS         VARCHAR2(30),
  CONTENT        VARCHAR2(4000)
)
;
alter table STUDYDATA
  add primary key (STUDYDATA_NO);

prompt
prompt Creating table STUDYLOGIN
prompt =========================
prompt
create table STUDYLOGIN
(
  ID                NUMBER not null,
  PROVIDER_NO       VARCHAR2(6),
  STUDY_NO          NUMBER,
  REMOTE_LOGIN_URL  VARCHAR2(100),
  URL_NAME_USERNAME VARCHAR2(20),
  URL_NAME_PASSWORD VARCHAR2(20),
  USERNAME          VARCHAR2(30),
  PASSWORD          VARCHAR2(100),
  CURRENT1          NUMBER,
  CREATOR           VARCHAR2(6),
  TIMESTAMP         TIMESTAMP(6) default current_timestamp not null
)
;
alter table STUDYLOGIN
  add constraint PK_STUDYLOGIN primary key (ID);

prompt
prompt Creating table SURVEY
prompt =====================
prompt
create table SURVEY
(
  SURVEYID             NUMBER(20) not null,
  DESCRIPTION          VARCHAR2(255),
  SURVEYDATA           CLOB,
  STATUS               NUMBER(6),
  USERID               NUMBER(20),
  DATECREATED          DATE,
  DATELAUNCHED         DATE,
  DATECLOSED           DATE,
  LAUNCHED_INSTANCE_ID NUMBER(20),
  VERSION              NUMBER(20) default '0'
)
;
alter table SURVEY
  add primary key (SURVEYID);

prompt
prompt Creating table SURVEYDATA
prompt =========================
prompt
create table SURVEYDATA
(
  SURVEYDATAID   NUMBER(10) not null,
  SURVEYID       VARCHAR2(5),
  DEMOGRAPHIC_NO NUMBER(10),
  PROVIDER_NO    VARCHAR2(6),
  STATUS         VARCHAR2(2),
  SURVEY_DATE    DATE,
  ANSWER         VARCHAR2(10),
  PROCESSED      NUMBER(10)
)
;
alter table SURVEYDATA
  add primary key (SURVEYDATAID);
create index IDX_SURVEYDATA_1 on SURVEYDATA (SURVEYID);
create index IDX_SURVEYDATA_2 on SURVEYDATA (DEMOGRAPHIC_NO);
create index IDX_SURVEYDATA_3 on SURVEYDATA (PROVIDER_NO);
create index IDX_SURVEYDATA_4 on SURVEYDATA (STATUS);
create index IDX_SURVEYDATA_5 on SURVEYDATA (SURVEY_DATE);
create index IDX_SURVEYDATA_6 on SURVEYDATA (ANSWER);
create index IDX_SURVEYDATA_7 on SURVEYDATA (PROCESSED);

prompt
prompt Creating table SURVEY_TEST_DATA
prompt ===============================
prompt
create table SURVEY_TEST_DATA
(
  ID          NUMBER(20) not null,
  INSTANCE_ID NUMBER(20),
  PAGE_NUMBER NUMBER(20),
  SECTION_ID  NUMBER(20),
  QUESTION_ID NUMBER(20),
  VALUE       VARCHAR2(4000),
  DATA_KEY    VARCHAR2(255),
  TYPE        VARCHAR2(255)
)
;
alter table SURVEY_TEST_DATA
  add primary key (ID);
create index IDX_SURVEY_TEST_DATA_1 on SURVEY_TEST_DATA (INSTANCE_ID);

prompt
prompt Creating table SURVEY_TEST_INSTANCE
prompt ===================================
prompt
create table SURVEY_TEST_INSTANCE
(
  ID           NUMBER(20) not null,
  SURVEY_ID    NUMBER(20),
  DATE_CREATED DATE,
  USER_ID      NUMBER(20),
  CLIENT_ID    NUMBER(20)
)
;
alter table SURVEY_TEST_INSTANCE
  add primary key (ID);

prompt
prompt Creating table SYSTEM_MESSAGE
prompt =============================
prompt
create table SYSTEM_MESSAGE
(
  ID            NUMBER(10) not null,
  MESSAGE       VARCHAR2(4000) not null,
  CREATION_DATE DATE default '01-JAN-1900',
  EXPIRY_DATE   DATE default '01-JAN-1900'
)
;
alter table SYSTEM_MESSAGE
  add primary key (ID);

prompt
prompt Creating table TABLE_MODIFICATION
prompt =================================
prompt
create table TABLE_MODIFICATION
(
  ID                NUMBER(10) not null,
  DEMOGRAPHIC_NO    NUMBER(10) default '0' not null,
  PROVIDER_NO       VARCHAR2(6),
  MODIFICATION_DATE DATE,
  MODIFICATION_TYPE VARCHAR2(20),
  TABLE_NAME        VARCHAR2(255),
  ROW_ID            VARCHAR2(20),
  RESULTSET         VARCHAR2(4000)
)
;
alter table TABLE_MODIFICATION
  add primary key (ID);
create index IDX_TABLE_MODIFICATION_1 on TABLE_MODIFICATION (DEMOGRAPHIC_NO);
create index IDX_TABLE_MODIFICATION_2 on TABLE_MODIFICATION (PROVIDER_NO);
create index IDX_TABLE_MODIFICATION_3 on TABLE_MODIFICATION (MODIFICATION_TYPE);

prompt
prompt Creating table TICKLER
prompt ======================
prompt
create table TICKLER
(
  TICKLER_NO       NUMBER(10) not null,
  DEMOGRAPHIC_NO   NUMBER(10) default '0',
  MESSAGE          VARCHAR2(4000),
  STATUS           VARCHAR2(1),
  UPDATE_DATE      DATE default '01-JAN-1900',
  SERVICE_DATE     DATE,
  CREATOR          VARCHAR2(6),
  PRIORITY         VARCHAR2(6) default 'Normal',
  TASK_ASSIGNED_TO VARCHAR2(255)
)
;
alter table TICKLER
  add primary key (TICKLER_NO);

prompt
prompt Creating table TICKLER_COMMENTS
prompt ===============================
prompt
create table TICKLER_COMMENTS
(
  ID          NUMBER(10) not null,
  TICKLER_NO  NUMBER(10) not null,
  MESSAGE     VARCHAR2(4000),
  PROVIDER_NO VARCHAR2(6) not null,
  UPDATE_DATE DATE not null
)
;
alter table TICKLER_COMMENTS
  add primary key (ID);

prompt
prompt Creating table TICKLER_UPDATE
prompt =============================
prompt
create table TICKLER_UPDATE
(
  ID          NUMBER(10) not null,
  TICKLER_NO  NUMBER(10) not null,
  STATUS      VARCHAR2(1) not null,
  PROVIDER_NO VARCHAR2(6) not null,
  UPDATE_DATE DATE not null
)
;
alter table TICKLER_UPDATE
  add primary key (ID);

prompt
prompt Creating table TMPDIAGNOSTICCODE
prompt ================================
prompt
create table TMPDIAGNOSTICCODE
(
  DIAGNOSTICCODE_NO NUMBER(5) not null,
  DIAGNOSTIC_CODE   VARCHAR2(5),
  DESCRIPTION       VARCHAR2(4000),
  STATUS            VARCHAR2(1)
)
;
alter table TMPDIAGNOSTICCODE
  add primary key (DIAGNOSTICCODE_NO);

prompt
prompt Creating table VALIDATIONS
prompt ==========================
prompt
create table VALIDATIONS
(
  ID         NUMBER not null,
  NAME       VARCHAR2(100) not null,
  REGULAREXP VARCHAR2(100),
  MAXVALUE   NUMBER(12,2),
  MINVALUE   NUMBER(12,2),
  MAXLENGTH  NUMBER(3),
  MINLENGTH  NUMBER(3),
  ISNUMERIC  VARCHAR2(1),
  ISTRUE     VARCHAR2(1),
  ISDATE     VARCHAR2(1)
)
;
alter table VALIDATIONS
  add primary key (ID);

prompt
prompt Creating table WAITINGLIST
prompt ==========================
prompt
create table WAITINGLIST
(
  ID             NUMBER(20) not null,
  LISTID         NUMBER(11),
  DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  NOTE           VARCHAR2(255),
  POSITION       NUMBER(20) default '0' not null,
  ONLISTSINCE    DATE default '01-JAN-1900' not null,
  IS_HISTORY     VARCHAR2(1)
)
;
alter table WAITINGLIST
  add primary key (ID);
create index IDX_WAITINGLIST_1 on WAITINGLIST (LISTID);

prompt
prompt Creating table WAITINGLISTNAME
prompt ==============================
prompt
create table WAITINGLISTNAME
(
  ID          NUMBER(11) not null,
  NAME        VARCHAR2(80),
  GROUP_NO    VARCHAR2(10),
  PROVIDER_NO VARCHAR2(6),
  CREATE_DATE DATE default '01-JAN-1900' not null,
  IS_HISTORY  VARCHAR2(1) default 'N'
)
;
alter table WAITINGLISTNAME
  add primary key (ID);

prompt
prompt Creating sequence DAWSON_ACCOUNTSEQ
prompt ===================================
prompt
create sequence DAWSON_ACCOUNTSEQ
minvalue 1
maxvalue 9999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence DAWSON_CONTACTSEQ
prompt ===================================
prompt
create sequence DAWSON_CONTACTSEQ
minvalue 1
maxvalue 9999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence HIBERNATE_SEQUENCE
prompt ====================================
prompt
create sequence HIBERNATE_SEQUENCE
minvalue 1
maxvalue 999999999999999999999999999
start with 200741
increment by 1
cache 20;

prompt
prompt Creating view V_LK_NAME
prompt =======================
prompt
CREATE OR REPLACE VIEW V_LK_NAME AS
SELECT
0 grandParentID, 0 parentID, BED_TYPE_ID ID, Name Description
FROM
  BED_TYPE a
/

prompt
prompt Creating view V_LK_ORG
prompt ======================
prompt
CREATE OR REPLACE VIEW V_LK_ORG AS
SELECT
0 grandParentID, 0 parentID, ID, Name Description
FROM
  AGENCY a
/

prompt
prompt Creating view V_LOOKUP_TABLE
prompt ============================
prompt
CREATE OR REPLACE VIEW V_LOOKUP_TABLE AS
select 'BTY' prefix, to_char(BED_TYPE_ID) code, NAME description, '' parentcode,1 active,rownum lineID
    from BED_TYPE
union
select 'BED' prefix, to_char(BED_ID) code, NAME description, to_char(ROOM_ID) parentcode, active,rownum lineID
    from BED
union
select 'ORG' prefix, to_char(id), description,  null parentcode,1, rownum lineID
    from V_LK_ORG
union
select 'ROL', role_name,role_name,  null parentcode, 1,rownum
       from SECROLE
union
select 'ROC', to_char(role_id), name,  null parentcode,1,rownum
       from CAISI_ROLE
union
select 'QGV',QGVIEWCODE,DESCRIPTION,GROUPCODE,1,rownum
       FROM REPORT_QGVIEWSUMMARY
union
SELECT 'RPG',to_char(ID),DESCRIPTION,null,1,orderbyindex
       FROM REPORT_LK_REPORTGROUP
union
SELECT 'LKT',TABLEID,DESCRIPTION,to_char(MODULEID),ACTIVEYN,rownum
       FROM APP_LOOKUPTABLE
union
SELECT 'MOD',to_char(module_Id),description, null, 1,rownum
from APP_MODULE
union
SELECT 'ISS',to_char(ISSUE_ID),DESCRIPTION,ROLE,1,rowNum
FROM ISSUE
Union
SELECT 'ISG',to_char(id),Name,null,1, rownum
from ISSUEGROUP
/

prompt
prompt Creating view V_REP_BEDLOG
prompt ==========================
prompt
CREATE OR REPLACE VIEW V_REP_BEDLOG AS
SELECT
a.client_id client_id,
  a.admission_date admission_date,
  a.discharge_date discharge_date,
  b.name program_name,
  b.descr program_description,
  c.bed_id bed_id,
  c.name bed_name,
  d.room_id room_id,
  d.name room_name,
  e.name client_prog_st_name,
  f.last_name last_name,
  f.first_name first_name
FROM
  admission a,
  program b,
  bed c,
  room d,
  program_clientstatus e,
  demographic f

WHERE
     a.program_id=b.program_id
     and
     a.program_id=d.program_id
     and
     a.program_id=e.program_id(+)
     and
     d.room_id=c.room_id
     and
     a.client_id=f.demographic_no
/

prompt
prompt Creating view V_REP_CLIENT
prompt ==========================
prompt
CREATE OR REPLACE VIEW V_REP_CLIENT AS
(SELECT DEMOGRAPHIC.LAST_NAME LAST_NAME,  
DEMOGRAPHIC.FIRST_NAME FIRST_NAME,  
DEMOGRAPHIC.ADDRESS ADDRESS,  
DEMOGRAPHIC.CITY CITY,  
DEMOGRAPHIC.PROVINCE PROVINCE,  
DEMOGRAPHIC.POSTAL POSTAL,  
DEMOGRAPHIC.ALIAS ALIAS,  
INTAKE.INTAKE_ID INTAKE_ID,  
INTAKE.CLIENT_ID CLIENT_ID,  
INTAKE.STAFF_ID STAFF_ID,  
INTAKE.CREATION_DATE CREATION_DATE 
FROM QUATROSHELTER.INTAKE INNER JOIN DEMOGRAPHIC ON INTAKE.CLIENT_ID=DEMOGRAPHIC.DEMOGRAPHIC_NO )
/

prompt
prompt Creating view V_REP_USERLIST
prompt ============================
prompt
CREATE OR REPLACE VIEW V_REP_USERLIST AS
(SELECT DISTINCT PROVIDER.PROVIDER_NO PROVIDER_NO,  
PROVIDER.LAST_NAME LAST_NAME,  
PROVIDER.FIRST_NAME FIRST_NAME,  
SECUSERROLE.ROLE_NAME ROLE_NAME 
FROM QUATROSHELTER.PROVIDER INNER JOIN SECUSERROLE ON PROVIDER.PROVIDER_NO=SECUSERROLE.PROVIDER_NO )
/

prompt
prompt Creating view V_USER_REPORT
prompt ===========================
prompt
CREATE OR REPLACE VIEW V_USER_REPORT AS
SELECT     a.REPORTNO, a.TITLE, a.DESCRIPTION, a.ORGAPPLICABLE, a.REPORTTYPE, a.DATEOPTION, a.DATEPART,
           a.REPORTGROUP,d.ID REPORTGROUPID, a.TABLENAME,
           a.NOTES, c.PROVIDER_NO,
           MAX(b.ACCESS_TYPE) ACCESS_TYPE, d.DESCRIPTION REPORTGROUPDESC, a.UPDATEDBY, a.UPDATEDDATE
FROM        REPORT a,REPORT_ROLE b,
            SECUSERROLE c ,
            REPORT_LK_REPORTGROUP d
 where  a.REPORTNO = b.REPORTNO and  b.ROLECODE = c.ROLE_NAME and a.REPORTGROUP(+)= d.ID
GROUP BY a.REPORTNO, a.TITLE, a.DESCRIPTION, a.ORGAPPLICABLE, a.REPORTTYPE, a.DATEOPTION, a.DATEPART, a.REPORTGROUP,d.ID, a.TABLENAME,
                      a.NOTES, c.PROVIDER_NO, d.DESCRIPTION, a.UPDATEDBY, a.UPDATEDDATE
/

prompt
prompt Creating function F_GETREPORTFIELDTYPE
prompt ======================================
prompt
CREATE OR REPLACE FUNCTION F_GetReportFieldType (p_reportNo NUMBER, p_fieldName VARCHAR2)
RETURN varchar2 AS
ret varchar(1);
v_TABLE VARCHAR2(32);
v_ViewNo NUMBER;
BEGIN

     BEGIN
     SELECT TABLENAME INTO v_table
     FROM REPORT
     WHERE REPORTNO  = p_reportNo;

     SELECT QGVIEWNO INTO v_ViewNO
     FROM REPORT_QGVIEWSUMMARY
     WHERE QGVIEWCODE = v_table;

     SELECT FIELDTYPECODE INTO ret
     FROM REPORT_QGVIEWFIELD
     WHERE QGVIEWNO = v_ViewNo and FIELDNAME = p_fieldName;

     EXCEPTION
      WHEN OTHERS THEN ret := 'S';
      END;
	return ret;
END;
/

prompt
prompt Creating function LEFT
prompt ======================
prompt
create or replace function LEFT(
       p_str varchar2,
       p_len NUMBER 
 ) return varchar2 is
  Result varchar2(4000);
begin
  IF NOT p_str IS NULL THEN
      RESULT := SUBSTR(p_str,1,p_len);
  END IF;     
  return(Result);
end LEFT;
/

prompt
prompt Creating function NOW
prompt =====================
prompt
create or replace function NOW
return DATE is
  Result DATE;
begin
  Result := SYSDATE;
  return(Result);
end NOW;
/

prompt
prompt Creating function TO_DAYS
prompt =========================
prompt
create or replace function TO_DAYS(
       p_date Date
 ) return NUMBER is
  Result NUMBER;
  d1 DATE;
begin
  IF NOT p_date IS NULL THEN
     d1 := TO_DATE('19000101','YYYYMMDD');
     RESULT := trunc(p_date) - d1;
  END IF;
  return(Result);
end TO_DAYS;
/

prompt
prompt Creating procedure SP_LOOKUPTABLES_I
prompt ====================================
prompt
CREATE OR REPLACE PROCEDURE "SP_LOOKUPTABLES_I"
(
  p_moduleId varchar2,
  p_tableid varchar2,
	p_tablename varchar2,
  p_description varchar2
)
AS

BEGIN

	INSERT INTO APP_LOOKUPTABLE
	(TABLEID,MODULEID,TABLE_NAME,DESCRIPTION,ISTREE,TREECODE_LENGTH,ACTIVEYN)
	VALUES (p_tableid,p_moduleid,p_tablename, p_description,'0',0,'1');

	INSERT INTO APP_LOOKUPTABLE_FIELDS
	(TABLEID,FIELDNAME,FIELDSQL,EDITYN,FIELDTYPE)
	SELECT p_tableId, b.COLUMN_NAME, b.COLUMN_NAME,'1',DECODE(b.DATA_TYPE,'VARCHAR2','S','DATE','D','N')
	FROM ALL_OBJECTS a, USER_TAB_COLUMNS b
  WHERE a.OBJECT_NAME = b.TABLE_NAME
  AND a.object_type IN ('VIEW','TABLE')
	AND a.Object_name = p_tablename;
EXCEPTION
         WHEN OTHERS THEN
              dbms_output.PUT_LINE ('FAILED to add the TABLE, the TABLE ID MAY ALREADY BEEN USED');
END;
/

prompt
prompt Creating procedure SP_QGVIEW_OBJECTS_I
prompt ======================================
prompt
CREATE OR REPLACE PROCEDURE "SP_QGVIEW_OBJECTS_I"
(
	p_name varchar2
)
AS
	v_QGVIEWNO integer;

BEGIN

  BEGIN
	SELECT QGVIEWNO INTO v_QGVIEWNO
	FROM  REPORT_QGVIEWSUMMARY
	where qgviewcode = p_name;
  EXCEPTION
           WHEN OTHERS THEN
                v_QGVIEWNO := 0;
  END;

	IF (v_QGVIEWNO > 0)  THEN
		DELETE REPORT_QGVIEWSUMMARY
		where qgviewcode = p_name;

		DELETE REPORT_QGVIEWFIELD
		where qgviewno= v_QGVIEWNO;
	END IF;

	SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO v_QGVIEWNO
  FROM DUAL;

	INSERT INTO REPORT_QGVIEWSUMMARY
	(QGVIEWNO,QGVIEWCODE, GROUPCODE, MASTERTYPE, UPDATEDBY, UPDATEDDATE, NOTE, ACTIVEYN, SECUREYN, DBENTITY, REFVIEWS, OBJECT_TYPE)
	SELECT v_QGVIEWNO,OBJECT_NAME, '90','M','oscardoc',SYSDATE, 'intial setup','Y','N',OBJECT_name,OBJECT_name,OBJECT_TYPE
	FROM ALL_OBJECTS
	where object_name = p_name AND object_type in ('TABLE','VIEW');

	INSERT INTO REPORT_QGVIEWFIELD
	(QGVIEWNO,FIELDNAME, FIELDNO, FIELDTYPECODE,SOURCETXT, NOTE)
	SELECT v_QGVIEWNO, b.COLUMN_NAME, b.COLUMN_ID,
  DECODE(b.DATA_TYPE,'VARCHAR2','S','DATE','D','N'), b.COLUMN_NAME, 'initial set up'
	FROM ALL_OBJECTS a, USER_TAB_COLUMNS b
  WHERE a.OBJECT_NAME = b.TABLE_NAME
  AND a.object_type IN ('VIEW','TABLE')
	AND a.Object_name = p_name;

END;
/

prompt
prompt Creating trigger TRI_BILLINGREFERRAL_NO
prompt =======================================
prompt
CREATE OR REPLACE TRIGGER TRI_BILLINGREFERRAL_NO
BEFORE INSERT
ON BILLINGREFERRAL
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN

     SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO IDX FROM DUAL;
     :new.BILLINGREFERRAL_NO := IDX;
END;
/

prompt
prompt Creating trigger TRI_DEMOGRAPHIC_DEMOGRAPHIC_NO
prompt ===============================================
prompt
CREATE OR REPLACE TRIGGER TRI_DEMOGRAPHIC_DEMOGRAPHIC_NO
BEFORE INSERT
ON DEMOGRAPHIC
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN
     IF :new.DEMOGRAPHIC_NO IS NULL THEN
          SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO IDX FROM DUAL; 
          :new.DEMOGRAPHIC_NO := IDX;
     END IF;
END;
/

prompt
prompt Creating trigger TRI_DEMOGRAPHIC_MERGED_ID
prompt ==========================================
prompt
CREATE OR REPLACE TRIGGER TRI_DEMOGRAPHIC_MERGED_ID
BEFORE INSERT
ON DEMOGRAPHIC_MERGED
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN
     IF :new.ID IS NULL THEN
          SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO IDX FROM DUAL;
          :new.ID := IDX;
     END IF;
END;
/

prompt
prompt Creating trigger TRI_DOCUMENT_NO
prompt ================================
prompt
CREATE OR REPLACE TRIGGER TRI_DOCUMENT_NO
BEFORE INSERT
ON DOCUMENT
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN

     SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO IDX FROM DUAL;
     :new.document_no := IDX;
END;
/

prompt
prompt Creating trigger TRI_GROUPS_TBL_ID
prompt ==================================
prompt
CREATE OR REPLACE TRIGGER TRI_GROUPS_TBL_ID
BEFORE INSERT
ON GROUPS_TBL
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN

     SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO IDX FROM DUAL;
     :new.groupid := IDX;
END;
/

prompt
prompt Creating trigger TRI_MESSAGETBL_ID
prompt ==================================
prompt
CREATE OR REPLACE TRIGGER TRI_MESSAGETBL_ID
BEFORE INSERT
ON MESSAGETBL
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN
     IF :new.messageid is null THEN
     SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO IDX FROM DUAL;
     :new.messageid := IDX;
     END IF;
END;
/

prompt
prompt Creating trigger TRI_RECYCLEBIN_ID
prompt ==================================
prompt
CREATE OR REPLACE TRIGGER TRI_RECYCLEBIN_ID
BEFORE INSERT
ON RECYCLEBIN
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN
     IF :new.RECYCLEBIN_NO IS NULL THEN
          SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO IDX FROM DUAL;
          :new.RECYCLEBIN_NO := IDX;
     END IF;
END;
/

prompt
prompt Creating trigger TRI_RELATIONSHIPS_ID
prompt =====================================
prompt
CREATE OR REPLACE TRIGGER TRI_RELATIONSHIPS_ID
BEFORE INSERT
ON RELATIONSHIPS
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN

     SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO IDX FROM DUAL; 
     :new.ID := IDX;
END;
/

prompt
prompt Creating trigger TRI_SECURITY_ID
prompt ================================
prompt
CREATE OR REPLACE TRIGGER TRI_SECURITY_ID
BEFORE INSERT
ON SECURITY
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN

     SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO IDX FROM DUAL; 
     :new.security_no := IDX;
END;
/


spool off
