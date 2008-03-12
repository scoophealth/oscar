--Drop table
drop table lst_admission_status;
-- Create table
create table lst_admission_status
(
  code          varchar(20) not null,
  description   VARCHAR2(80),
  isactive      NUMBER(1),
  displayorder  NUMBER,
  primary key (code)
);
--Drop Table
drop table lst_program_type;
-- Create table
create table lst_program_type
(
  code         VARCHAR2(20) not null,
  description  VARCHAR2(80),
  isactive     NUMBER(1),
  displayorder NUMBER,
  primary key (code)
);

CREATE OR REPLACE VIEW V_LOOKUP_TABLE AS
select 'BTY' prefix, to_char(BED_TYPE_ID) code, NAME description, '' parentcode,1 active,BED_TYPE_ID lineID, to_char(DFLT) BUF1
    from BED_TYPE
union
select 'BED' prefix, to_char(BED_ID) code, NAME description, to_char(ROOM_ID) parentcode, active,BED_ID,to_char(BED_TYPE_ID)
    from BED
union
select 'ORG' prefix, to_char(id), description,  null parentcode,1, 0 lineID, null
    from V_LK_ORG
union
select 'ROL', role_name,role_name,  null parentcode, 1,0,null
       from SECROLE
union
select 'ROC', to_char(role_id), name,  null parentcode,1,0,null
       from CAISI_ROLE
union
select 'QGV',QGVIEWCODE,DESCRIPTION,GROUPCODE,1,0,null
       FROM REPORT_QGVIEWSUMMARY
union
SELECT 'RPG',to_char(ID),DESCRIPTION,null,1,orderbyindex,null
       FROM REPORT_LK_REPORTGROUP
union
SELECT 'LKT',TABLEID,DESCRIPTION,to_char(MODULEID),ACTIVEYN,0,null
       FROM APP_LOOKUPTABLE
union
SELECT 'MOD',to_char(module_Id),description, null, 1,0,null
from APP_MODULE
union
SELECT 'FCT',to_char(Id),description, null, isactive,displayorder,null
from LST_FIELD_CATEGORY
union
SELECT 'ISS',to_char(ISSUE_ID),DESCRIPTION,ROLE,1,0,null
FROM ISSUE
Union
SELECT 'IGP',to_char(id),Name,null,1, 0,null
from ISSUEGROUP
union
SELECT 'GEN',CODE,DESCRIPTION,null,isactive, displayorder,null
FROM LST_GENDER
union
SELECT 'SEC',to_char(id),DESCRIPTION,null,isactive, displayorder, null
FROM LST_SECTOR
union
SELECT 'OGN',to_char(id),DESCRIPTION,null,isactive, displayorder, null
FROM LST_ORGANIZATION
union
SELECT 'DRN',to_char(id),DESCRIPTION,null,isactive, displayorder, to_char(needsecondary)
FROM LST_DISCHARGE_REASON
union
SELECT 'SRT',to_char(id),DESCRIPTION,null,isactive, displayorder,null
FROM LST_SERVICE_RESTRICTION
union
select 'PRO',to_char(program_id),name DESCRIPTION,to_char(facility_id) parentcode,to_number(decode(program_status, 'active','1','inactive','0')) isactive,null,null
from program
union
select 'FAC',to_char(id)facility_id,name description,to_char(org_id) parentcode,to_number(decode(disabled, '1','0','0','1'))isactive,null,null
from facility
union
select 'AST',to_char(code) code,description,null,isactive,displayorder,null
from LST_ADMISSION_STATUS
union
select 'PTY',to_char(code) code,description,null,isactive,displayorder,null
from LST_PROGRAM_TYPE
