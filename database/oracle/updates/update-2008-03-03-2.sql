drop table LST_GENDER;
create table LST_GENDER
(CODE varchar2(1) NOT NULL,
 DESCRIPTION varchar2(80),
 ISACTIVE NUMBER(1),
 DISPLAYORDER NUMBER,
 PRIMARY KEY (CODE)
);
insert into lst_gender (code,description,isactive,displayorder) values ('M','Male',1,2);
insert into lst_gender (code,description,isactive,displayorder) values ('F','Female',1,1);
insert into lst_gender (code,description,isactive,displayorder) values ('T','Transgender',1,3);

drop table LST_SECTOR;
create table LST_SECTOR
(ID NUMBER NOT NULL,
 DESCRIPTION varchar2(80),
 ISACTIVE NUMBER(1),
 DISPLAYORDER NUMBER,
 PRIMARY KEY (ID)
);
insert into lst_sector (id,description,isactive,displayorder) values (1,'Men',1,1);
insert into lst_sector (id,description,isactive,displayorder) values (2,'Women',1,2);
insert into lst_sector (id,description,isactive,displayorder) values (3,'Families',1,3);
insert into lst_sector (id,description,isactive,displayorder) values (4,'Youth',1,4);

drop table LST_ORGANIZATION;
create table LST_ORGANIZATION
(ID NUMBER NOT NULL,
 DESCRIPTION varchar2(80),
 ISACTIVE NUMBER(1),
 DISPLAYORDER NUMBER,
 PRIMARY KEY (ID)
);
insert into lst_organization (id,description,isactive,displayorder) values (1,'City of Toronto',1,1);
insert into lst_organization (id,description,isactive,displayorder) values (2,'Salvation Army',1,2);
insert into lst_organization (id,description,isactive,displayorder) values (3,'Fred Victor',1,3);
;
alter table APP_MODULE add ISACTIVE number(1);
alter table APP_MODULE add DISPLAYORDER number;

truncate table app_module;
insert into app_module (module_id, description, isactive,displayorder) values(1,'Client Management',1,10);
insert into app_module (module_id, description, isactive,displayorder) values(2,'Shelter Management',2,20);
insert into app_module (module_id, description, isactive,displayorder) values(3,'Case Management',3,30);
insert into app_module (module_id, description, isactive,displayorder) values(4,'System Administration',4,40);
insert into app_module (module_id, description, isactive,displayorder) values(5,'Reports',1,50);
insert into app_module (module_id, description, isactive,displayorder) values(6,'Intake',1,60);
insert into app_module (module_id, description, isactive,displayorder) values(7,'Agency',1,70);

delete app_lookuptable where tableid in ('GEN','SEC','OGN','DRN','SRT','ORG','LKT','FCT','ISS','IGP');
insert into app_lookuptable(tableid,moduleid,table_name,description,istree,treecode_length,activeyn)
values ('GEN',4,'LST_GENDER','Gender',0,0,1);
insert into app_lookuptable(tableid,moduleid,table_name,description,istree,treecode_length,activeyn)
values ('SEC',2,'LST_SECTOR','Sector',0,0,1);
insert into app_lookuptable(tableid,moduleid,table_name,description,istree,treecode_length,activeyn)
values ('OGN',2,'LST_ORGANIZATION','Organization',0,0,1);
insert into app_lookuptable(tableid,moduleid,table_name,description,istree,treecode_length,activeyn)
values ('DRN',5,'LST_DISCHARGE_REASON','Discharge Reason',0,0,1);
insert into app_lookuptable(tableid,moduleid,table_name,description,istree,treecode_length,activeyn)
values ('SRT',3,'LST_SERVICE_RESTRICTION','Service Restriction',0,0,1);
insert into app_lookuptable(tableid,moduleid,table_name,description,istree,treecode_length,activeyn)
values ('ORG',1,'V_LK_ORG','Organization Chart',0,0,1);
insert into app_lookuptable(tableid,moduleid,table_name,description,istree,treecode_length,activeyn)
values ('LKT',1,'APP_LOOKUPTABLE','Available Fields',0,0,1);
insert into app_lookuptable(tableid,moduleid,table_name,description,istree,treecode_length,activeyn)
values ('FCT',1,'LST_FIELD_CATEGORY','Field Category',0,0,1);
insert into app_lookuptable(tableid,moduleid,table_name,description,istree,treecode_length,activeyn)
values ('ISS',3,'ISSUE','Issue',0,0,1);
insert into app_lookuptable(tableid,moduleid,table_name,description,istree,treecode_length,activeyn)
values ('IGP',3,'ISSUEGROUP','Issue Group',0,0,1);

-- Create table
drop table LST_DISCHARGE_REASON;
create table LST_DISCHARGE_REASON
(
  ID   NUMBER not null,
  DESCRIPTION VARCHAR2(80),
  NEEDSECONDARY NUMBER(1),  
  ISACTIVE    NUMBER(1),
  DISPLAYORDER NUMBER,
  primary key (ID)
);
insert into LST_DISCHARGE_REASON (ID,DESCRIPTION,NEEDSECONDARY,ISACTIVE,DISPLAYORDER) 
values (1,'Service restriction',0,1,10);
insert into LST_DISCHARGE_REASON (ID,DESCRIPTION,NEEDSECONDARY,ISACTIVE,DISPLAYORDER) 
values (2,'Client Self Discharge',0,1,10);
insert into LST_DISCHARGE_REASON (ID,DESCRIPTION,NEEDSECONDARY,ISACTIVE,DISPLAYORDER) 
values (3,'Other Program more appropriate',0,1,10);
;
drop table LST_FIELD_CATEGORY;
create table LST_FIELD_CATEGORY
(ID NUMBER NOT NULL,
 DESCRIPTION varchar2(80),
 ISACTIVE NUMBER(1),
 DISPLAYORDER NUMBER,
 PRIMARY KEY (ID)
);
insert into lst_field_category (id, description,isactive,displayorder)
values(1,'System',	1,	10);
insert into lst_field_category (id, description,isactive,displayorder)
values(2,'Agency',	1,	10);
insert into lst_field_category (id, description,isactive,displayorder)
values(3,'Client',  1, 20);
insert into lst_field_category (id, description,isactive,displayorder)
values(4,'Intake',	1,	30);
insert into lst_field_category (id, description,isactive,displayorder)
values(5,'Program', 1, 40);

drop table LST_SERVICE_RESTRICTION;
create table LST_SERVICE_RESTRICTION
(ID NUMBER NOT NULL,
 DESCRIPTION varchar2(80),
 ISACTIVE NUMBER(1),
 DISPLAYORDER NUMBER,
 PRIMARY KEY (ID)
);
insert into lst_service_restriction(id,description,isactive,displayorder)
values (1,'Assault of client',1,10);    
insert into lst_service_restriction(id,description,isactive,displayorder)
values (2,'Assault of staff',1,20);    
insert into lst_service_restriction(id,description,isactive,displayorder)
values (3,'Other',1,30);    

drop table APP_LOOKUPTABLE_FIELDS;
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
  UNIQUEYN    NUMBER,
  GENERICIDX  NUMBER,
  AUTOYN      NUMBER,
  primary key (TABLEID, FIELDNAME)
)
;
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('FCT', 'ID', 'Id', '0', 'N', null, 'ID', 1, 1, 1, 1);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('FCT', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('FCT', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 3, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('FCT', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 4, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('BTY', 'BED_TYPE_ID', 'Id', '0', 'N', null, 'BED_TYPE_ID', 1, 1, 1, 1);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('BTY', 'NAME', 'Name', '1', 'S', null, 'NAME', 2, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('BTY', 'DFLT', 'Default Capacity', '1', 'N', null, 'DFLT', 3, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('LKT', 'TABLEID', 'Id', '1', 'S', null, 'TABLEID', 1, 1, 1, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('LKT', 'MODULEID', 'Category', '1', 'S', 'FCT', 'MODULEID', 2, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('LKT', 'TABLE_NAME', 'Table Name', '1', 'S', null, 'TABLE_NAME', 3, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('LKT', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 4, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('LKT', 'ISTREE', 'Is Tree Structured?', '1', 'B', null, 'ISTREE', 5, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('LKT', 'TREECODE_LENGTH', 'Tree Code Length (if is tree)', '1', 'N', null, 'TREECODE_LENGTH', 6, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('LKT', 'ACTIVEYN', 'Active?', '1', 'B', null, 'ACTIVEYN', 7, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('GEN', 'CODE', 'Code', '1', 'S', null, 'CODE', 1, 1, 1, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('GEN', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('GEN', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 3, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('GEN', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 4, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('SEC', 'ID', 'Id', '0', 'N', null, 'ID', 1, 1, 1, 1);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('SEC', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('SEC', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 3, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('SEC', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 4, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('OGN', 'ID', 'Id', '0', 'N', null, 'ID', 1, 1, 1, 1);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('OGN', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('OGN', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 3, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('OGN', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 4, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('DRN', 'ID', 'Id', '0', 'N', null, 'ID', 1, 1, 1, 1);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('DRN', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('DRN', 'NEEDSECONDARY', 'Has Associated Secondary Reason?', '1', 'N', null, 'NEEDSECONDARY', 3, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('DRN', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 4, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('DRN', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 5, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('SRT', 'ID', 'Id', '1', 'N', null, 'ID', 1, 1, 1, 1);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('SRT', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('SRT', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 3, 0, 0, 0);
insert into APP_LOOKUPTABLE_FIELDS (TABLEID, FIELDNAME, FIELDDESC, EDITYN, FIELDTYPE, LOOKUPTABLE, FIELDSQL, FIELDINDEX, UNIQUEYN, GENERICIDX, AUTOYN)
values ('SRT', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 4, 0, 0, 0);
commit;

CREATE OR REPLACE VIEW V_LOOKUP_TABLE AS
select 'BTY' prefix, to_char(BED_TYPE_ID) code, NAME description, '' parentcode,1 active,BED_TYPE_ID lineID, to_char(DFLT) BUF1
    from BED_TYPE
union
select 'BED' prefix, to_char(BED_ID) code, NAME description, to_char(ROOM_ID) parentcode, active,BED_ID,to_char(BED_TYPE_ID)
    from BED
union
select 'ORG' prefix, to_char(id), description,  null parentcode,isactive, 0 lineID, null
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
SELECT 'FCT',to_char(Id),description, null, isactive,0,null
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
;