DROP TABLE APP_LOOKUPTABLE;
create table APP_LOOKUPTABLE
(
  TABLEID         VARCHAR2(32) not null,
  MODULEID        VARCHAR2(5),
  TABLE_NAME      VARCHAR2(32),
  DESCRIPTION     VARCHAR2(80),
  ISTREE          NUMBER(1),
  TREECODE_LENGTH NUMBER,
  ACTIVEYN        NUMBER(1),
  PRIMARY KEY  (TABLEID)
)
;
DROP TABLE APP_LOOKUPTABLE_FIELDS;
create table APP_LOOKUPTABLE_FIELDS
(
  TABLEID     NVARCHAR2(10) not null,
  FIELDNAME   NVARCHAR2(20) not null,
  FIELDDESC   NVARCHAR2(40),
  EDITYN      NVARCHAR2(1),
  FIELDTYPE   NVARCHAR2(1),
  LOOKUPTABLE NVARCHAR2(6),
  FIELDSQL    NVARCHAR2(32),
  FIELDINDEX  NUMBER,
  UNIQUEYN    NUMBER,
  primary key (TABLEID, FIELDNAME))
;
DROP TABLE APP_MODULE;
create table APP_MODULE
(
  MODULE_ID   NUMBER(10) not null,
  DESCRIPTION VARCHAR2(128) not null,
  primary key (MODULE_ID)
)
;
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
