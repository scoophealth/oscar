drop table REPORT cascade constraints;
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
  PRIMARY KEY (REPORTNO)
);


drop table REPORT_DOCTEXT cascade constraints;
-- Create table
create table REPORT_DOCTEXT
(
  DOCID       NUMBER(10) not null,
  DOCDATA     BLOB,
  REVDATETIME TIMESTAMP(6)
  PRIMARY KEY (DOCID)
);


drop table REPORT_FILTER cascade constraints;
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
  PRIMARY KEY (FIELDNO)
);


drop table REPORT_OPTION cascade constraints;
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
  PRIMARY KEY (REPORTOPTIONID)
);


drop table REPORT_TEMPLATE cascade constraints;
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
  PRIMARY KEY (TEMPLATENO)
);


drop table REPORT_TEMPLATE_CRITERIA cascade constraints;
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
  PRIMARY KEY (TEMPLATENO, COUNTER)
);


drop table REPORT_TEMPLATE_ORG cascade constraints;
create table REPORT_TEMPLATE_ORG
(
  COUNTER    NUMBER(10) not null,
  TEMPLATENO NUMBER(10) not null,
  ORGCD      VARCHAR2(72)
  PRIMARY KEY (TEMPLATENO, COUNTER)
);



CREATE OR REPLACE VIEW V_LK_NAME AS
SELECT 0 grandParentID, 0 parentID, BED_TYPE_ID ID, Name Description
FROM BED_TYPE a;


CREATE OR REPLACE VIEW V_LK_ORG AS
SELECT 0 grandParentID, 0 parentID, ID, Name Description
FROM AGENCY a;


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
from ISSUEGROUP;



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
  f.first_name first_name,
  b.agency_id orgCd
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
     a.client_id=f.demographic_no;


CREATE OR REPLACE VIEW V_USER_REPORT AS
SELECT a.REPORTNO, a.TITLE, a.DESCRIPTION, a.ORGAPPLICABLE, a.REPORTTYPE, a.DATEOPTION, a.DATEPART,
       a.REPORTGROUP,d.ID REPORTGROUPID, a.TABLENAME, a.NOTES, c.PROVIDER_NO,
       MAX(b.ACCESS_TYPE) ACCESS_TYPE, d.DESCRIPTION REPORTGROUPDESC, a.UPDATEDBY, a.UPDATEDDATE
FROM  REPORT a,REPORT_ROLE b, SECUSERROLE c, REPORT_LK_REPORTGROUP d
where  a.REPORTNO = b.REPORTNO and  b.ROLECODE = c.ROLE_NAME and a.REPORTGROUP(+)= d.ID
  GROUP BY a.REPORTNO, a.TITLE, a.DESCRIPTION, a.ORGAPPLICABLE, a.REPORTTYPE, a.DATEOPTION, a.DATEPART, a.REPORTGROUP,d.ID, a.TABLENAME,
            a.NOTES, c.PROVIDER_NO, d.DESCRIPTION, a.UPDATEDBY, a.UPDATEDDATE;




