insert into property(id,name,value,provider_no)
values(1,'IS_PIN_ENCRYPTED',0,999998);

insert into access_type	(name, type) values("Run Report Runner","access");
insert into access_type	(name, type) values("Design Reports","access");

insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='Run Report Runner'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='Design Reports'));

INSERT INTO `caisi_editor`(category,label,type,labelValue,labelCode,horizontal,isActive) VALUES('Agency','Organization','','Good Shepherd','4','','Yes'),('Client','Service Restriction','','Assault of client','1','','Yes'),('Client','Service Restriction','','Assault of staff','2','','Yes'),('Client','Service Restriction','','other','3','','Yes'),('Program','Discharge Reason','','Service restriction','1','','Yes'),('Program','Discharge Reason','','Client self discharge','2','','Yes'),('Program','Discharge Reason','','More appropriate for other program','3','','Yes'),('Program','Discharge Reason','','Completed care','4','','Yes');

-- quatro report tables :

DROP TABLE IF EXISTS report;
create table report
(
  reportno     	bigint(10) 	NOT NULL auto_increment,
  title         varchar(80) default NULL,
  description	varchar(255) default NULL,
  orgapplicable tinyint(1) 	default 0,
  reporttype	char(3) 	default NULL,
  dateoption	char(1)	default NULL,
  datepart		char(1)	default NULL,
  reportgroup	int(10)		default 0,
  notes			text 		default NULL,
  tablename		varchar(30)	default NULL,
  updatedby		varchar(20)	default NULL,
  updateddate	datetime 	default NULL,
  sptorun		varchar(32) default NULL,
  PRIMARY KEY  (reportno)
);

DROP TABLE IF EXISTS report_date;
create table report_date
(
  sessionid		varchar(32) NOT NULL ,
  startdate   	date default NULL,
  enddate		date default NULL,
  asofdate		date default NULL,
  startdate_s 	varchar(8) 	default NULL,
  enddate_s		varchar(8)	default NULL,   
  asofdate_s	varchar(8)	default NULL,
  PRIMARY KEY  (sessionid)
);

DROP TABLE IF EXISTS `report_date_sp`;
create table report_date_sp
(
  reportno		int(10)    NOT NULL auto_increment,
  startdate		date default NULL , 
  enddate		date default NULL,
  asofdate		date default NULL,
  startdate_s	varchar(8) default NULL,
  enddate_s		varchar(8) default NULL,
  asofdate_s	varchar(8) default NULL,
  sptorun		varchar(32) default NULL,
  PRIMARY KEY  (reportno)
);

DROP TABLE IF EXISTS `report_doctext`;
create table report_doctext
(
  docid       int(10) NOT NULL auto_increment,
  docdata     text,
  revdatetime TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (docid)
);

DROP TABLE IF EXISTS `report_document`;
create table report_document
(
  docid          int(10) NOT NULL auto_increment,
  subject        varchar(256) default NULL,
  privacycd      varchar(3) default NULL,
  ownerid        varchar(12) default NULL,
  checkoutyn     char(1) default NULL,
  checkoutuserid	varchar(12) default NULL,
  checkoutdate	datetime 	default NULL,
  doctype		varchar(3) 	default NULL,
  filename		varchar(128) default NULL,
  moduleid		varchar(6) 	default NULL,
  refno			varchar(12) default NULL,
  filetype		varchar(5) 	default NULL,
  viewid		varchar(36) default NULL,
  viewrefno		varchar(12)	default NULL,
  revdatetime   datetime default NULL,
  PRIMARY KEY  (docid)
);

DROP TABLE IF EXISTS `report_filter`;
create table report_filter
(  
  fieldno           int(10) NOT NULL auto_increment,
  reportno          int(10) default 0,
  fieldname         varchar(32) default NULL,
  fielddesc         varchar(80)	default NULL,
  fieldtype         varchar(10)	default NULL,
  lookup_table      varchar(30) default NULL,
  iscrosstabheaders char(1)	default NULL,
  operator			varchar(10)	default NULL,
  lookup_tree       char(1)	default NULL,
  fieldsql          varchar(32)	default NULL,
  lookup_script     varchar(50)	default NULL,
  note              varchar(128)	default NULL,
  valueformat       varchar(32)	default NULL,
  PRIMARY KEY  (fieldno)
);

DROP TABLE IF EXISTS `report_lk_reportgroup`;
create table report_lk_reportgroup
(
  id           int(10) NOT NULL auto_increment,
  description  varchar(40)	default NULL,
  shortdesc    varchar(10)	default NULL,
  activeyn     char(1)	default NULL,
  orderbyindex int(10)	default 0,
  note         varchar(128)	default NULL,
  PRIMARY KEY  (id)
);

DROP TABLE IF EXISTS `report_option`;
create table report_option
(  
  reportoptionid int(10) NOT NULL auto_increment,
  reportno       int(10) not null,
  optiontitle    varchar(120)	default NULL,
  longdesc       varchar(120)	default NULL,
  activeyn       tinyint(1)	default 0,
  defaultyn      tinyint(1)	default 0,
  datefield      varchar(32)	default NULL,
  datefielddesc  varchar(32)	default NULL,
  sqlwhere       text	default NULL,
  sqlorderby     text	default NULL,
  rptfilename    varchar(128)	default NULL,
  rptfileno      int(10)	default 0,
  rptversion     datetime default NULL,
  datefieldtype  varchar(3)	default NULL,
  PRIMARY KEY  (reportoptionid)
);

DROP TABLE IF EXISTS `report_qgviewfield`;
create table report_qgviewfield
(
  qgviewno      int(10) NOT NULL auto_increment,  
  fieldno       int(10) not null,
  fieldname     varchar(32) not null,
  description   varchar(80)	default NULL,
  fieldtypecode varchar(8)	default NULL,
  numbermask    varchar(16)	default NULL,
  fieldlength   int(10) 	default 0,
  sourcetxt     text	default NULL,
  note          varchar(255) default NULL,
  grouprank     int(10)		default 0,
  lookuptable   varchar(6)	default NULL,
  PRIMARY KEY  (qgviewno,fieldno)
);

DROP TABLE IF EXISTS `report_qgviewsummary`;
create table report_qgviewsummary
(
  qgviewno    int(10) NOT NULL auto_increment,
  qgviewcode  varchar(30) 	not null ,
  description varchar(80) 	default NULL,
  groupcode   varchar(10)	default NULL,
  mastertype  char(1)	default NULL,
  updatedby   varchar(12)	default NULL,
  updateddate datetime	default NULL,
  note        text	default NULL,
  activeyn    char(1)	default NULL,
  secureyn    char(1)	default NULL,
  dbentity    varchar(40)	default NULL,
  refviews    varchar(512)	default NULL,
  relations   text	default NULL,
  filters     text	default NULL,
  object_type varchar(5)	default NULL,
  distinctyn  char(1)	default NULL,
  PRIMARY KEY  (qgviewno)
);

DROP TABLE IF EXISTS `report_role`;
create table report_role
(
  reportno    	int(10) NOT NULL default 0,
  rolecode		varchar(20) not null ,
  access_type	char(1) 	default NULL,
  PRIMARY KEY  (reportno,rolecode)
);

DROP TABLE IF EXISTS `report_template`;
create table report_template
(
  templateno     int(10) NOT NULL auto_increment,
  reportno       int(10) not null	default 0,
  reportoptionid int(10)	default 0,
  description    varchar(120)	default NULL,
  startdate      datetime	default NULL,
  enddate        datetime	default NULL,
  startpayperiod varchar(10)	default NULL,
  endpayperiod   varchar(10)	default NULL,
  loginid        varchar(20)	default NULL,
  updatedate     datetime	default NULL,
  privateyn      char(1)	default NULL,
  PRIMARY KEY  (templateno)
);

DROP TABLE IF EXISTS `report_template_criteria`;
create table report_template_criteria 
(
  counter    int(10) not null	default 0,
  templateno int(10) not null	default 0,
  relation   varchar(10)	default NULL,
  fieldno    int(10)	default 0,
  operator   varchar(10)	default NULL,
  operators  varchar(10)	default NULL,
  val        varchar(40)	default NULL,
  valdesc    varchar(120)	default NULL,
  required   char(1)	default NULL,
  PRIMARY KEY  (templateno,counter)
);

DROP TABLE IF EXISTS `report_template_org`;
create table report_template_org
(
  counter    int(10) not null,
  templateno int(10) not null,
  orgcd      varchar(72),
  PRIMARY KEY  (templateno,counter)
);

DROP TABLE IF EXISTS app_lookuptable;
create table app_lookuptable
(
  tableid         varchar(32) not null,
  moduleid        varchar(5),
  table_name      varchar(32),
  description     varchar(80),
  istree          tinyint(1),
  treecode_length int(10),
  activeyn        tinyint(1),
  PRIMARY KEY  (tableid)
);

DROP TABLE IF EXISTS app_lookuptable_fields;
create table app_lookuptable_fields
(
  tableid     VARCHAR(10) not null,
  fieldname   VARCHAR(20) not null,
  fielddesc   VARCHAR(40),
  edityn      VARCHAR(1),
  fieldtype   VARCHAR(1),
  lookuptable VARCHAR(6),
  fieldsql    VARCHAR(32),
  fieldindex  int(10),
  uniqueyn    int(10),
  genericidx  int(10),
  autoyn      tinyint(1),
  primary key (tableid, fieldname)
);

DROP TABLE IF EXISTS app_module;
create table app_module
(
  module_id   int(10) not null,
  description varchar(128) not null,
  primary key (module_id)
);


-- views:
CREATE OR REPLACE VIEW v_lk_name AS
SELECT 0 grandParentID, 0 parentID, bed_type_id id, name description
FROM bed_type a;

CREATE OR REPLACE VIEW v_lk_org AS
SELECT 0 grandParentID, 0 parentID, id, name description
FROM agency;

CREATE OR REPLACE VIEW v_lookup_table AS
select 'BTY' prefix, bed_type_id code, name description, '' parentcode,1 active,bed_type_id lineID, dflt buf1
    from bed_type
union
select 'BED' prefix, bed_id code, name description, room_id parentcode, active,bed_id,bed_type_id 
    from bed
union
select 'ORG' prefix, id, description,  null parentcode,1, 0 lineID, null
    from v_lk_org
union
select 'ROL', role_name,role_name,  null parentcode, 1,0, null 
       from secRole
union
select 'ROC', role_id, name,  null parentcode,1,0 ,null
       from caisi_role
union
select 'QGV', qgviewcode, description, groupcode,1,0,null
       FROM report_qgviewsummary
union
SELECT 'RPG',id , description, null,1,orderbyindex, null
       FROM report_lk_reportgroup
union
SELECT 'LKT', tableid, description, moduleid, activeyn,0 , null
       FROM app_lookuptable
union
SELECT 'MOD',module_id, description, null, 1,0 , null
from app_module
union
SELECT 'FCT',id,description, null, 1,0,null
from lst_field_category
union
SELECT 'ISS',issue_id, description, role,1,0, null
FROM issue
union
SELECT 'ISG', id, name,null,1, 0, null
from IssueGroup
union
SELECT 'GEN',code,description,null,isactive, displayorder,null
FROM lst_gender
union
SELECT 'SEC',id,description,null,isactive, displayorder, null
FROM lst_sector
union
SELECT 'OGN',id,description,null,isactive, displayorder, null
FROM lst_organization
union
SELECT 'DRN',id,description,null,isactive, displayorder, needsecondary 
FROM lst_discharge_reason
union
SELECT 'SRT',id,description,null,isactive, displayorder,null
FROM lst_service_restriction
;


CREATE OR REPLACE VIEW v_rep_bedlog AS
SELECT
a.client_id client_id,
  a.admission_date admission_date,
  a.discharge_date discharge_date,
  p.name program_name,
  p.descr program_description,
  b.bed_id bed_id,
  b.name bed_name,
  r.room_id room_id,
  r.name room_name,
  pc.name client_prog_st_name,
  d.last_name last_name,
  d.first_name first_name,
  p.agency_id orgCd
FROM
	admission a left join program_clientstatus pc on a.program_id=pc.program_id  
	join program p on a.program_id= p.program_id 	
	join demographic d on a.client_id=d.demographic_no 
	join (room r join bed b on r.room_id=b.room_id) on a.program_id=r.program_id 
;


CREATE OR REPLACE VIEW v_user_report AS
SELECT a.reportno, a.title, a.description, a.orgapplicable, a.reporttype, a.dateoption, a.datepart,
       a.reportgroup,d.id reportgroupid, a.tablename, a.notes, c.provider_no,
       MAX(b.access_type) access_type, d.description reportgroupdesc, a.updatedby, a.updateddate
FROM  report a left join report_lk_reportgroup d on a.reportgroup=d.id 
		join (report_role b join secUserRole c on b.rolecode = c.role_name) on a.reportno = b.reportno 
GROUP BY a.reportno, a.title, a.description, a.orgapplicable, a.reporttype, a.dateoption, a.datepart, 
	a.reportgroup,d.id, a.tablename,a.notes, c.provider_no, d.description, a.updatedby, a.updateddate
;

-- data 
INSERT INTO secObjectName (objectName) VALUES ('_reportRunner');
INSERT INTO secObjectName (objectName) VALUES ('_reportWriter');


INSERT INTO secObjPrivilege (roleUserGroup, objectName, privilege, priority, provider_no)
VALUES ('admin', '_reportRunner', '|*|', 0, '999998');
INSERT INTO secObjPrivilege (roleUserGroup, objectName, privilege, priority, provider_no)
VALUES ('admin', '_reportWriter', '|*|', 0, '999998');
INSERT INTO secObjPrivilege (roleUserGroup, objectName, privilege, priority, provider_no)
VALUES ('doctor', '_reportRunner', '|*|', 0, '999998');
INSERT INTO secObjPrivilege (roleUserGroup, objectName, privilege, priority, provider_no)
VALUES ('doctor', '_reportWriter', '|*|', 0, '999998');


insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('BTY', '1', 'BED_TYPE', 'Bed Type', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('ORG', '2', 'AGENCY', 'Organization', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('ROL', '4', 'SECROLE', 'System Role', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('QGV', '5', 'REPORT_QGVIEWSUMMARY', 'List of Views', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('RPG', '5', 'REPORT_LK_REPORTGROUP', 'Report Group', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('LKT', '4', 'APP_LOOKUPTABLE', 'Lookup fields in the system', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('ROC', '4', 'CAISI_ROLE', 'Program Management Role', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('ISS', '1', 'ISSUE', 'Issue', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('IGP', '1', 'ISSUEGROUP', 'Issue Group', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('RMT', '1', 'ROOM_TYPE', 'Room Type', 0, 0, 1);


insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('BTY', 'BED_TYPE_ID', 'Bed Type Id', '0', 'N', null, 'BED_TYPE_ID', 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('BTY', 'NAME', 'Description', '1', 'S', null, 'NAME', 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('BTY', 'DFLT', 'Default Capacity', '1', 'N', null, 'DFLT', 3, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('ISS', 'ISSUE_ID', 'Issue Id', '0', 'N', null, 'ISSUE_ID', 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('ISS', 'CODE', 'Code', '1', 'S', null, 'CODE', 2, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('ISS', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 3, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('ISS', 'ROLE', 'Role', '1', 'S', 'ROL', 'ROLE', 4, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('ISS', 'UPDATE_DATE', 'Update Date', '1', 'D', null, 'UPDATE_DATE', 6, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('ISS', 'PRIORITY', 'Priority', '1', 'S', null, 'PRIORITY', 5, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('IGP', 'ID', 'Id', '0', 'N', null, 'ID', 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('IGP', 'NAME', 'Name', '1', 'S', null, 'NAME', 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('RMT', 'ROOM_TYPE_ID', 'Room Type Id', '0', 'S', null, 'ROOM_TYPE_ID', 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('RMT', 'NAME', 'Name', '1', 'S', null, 'NAME', 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn)
values ('RMT', 'DFLT', 'Default', '1', 'N', null, 'DFLT', 3, 0);



insert into app_module (module_id, description)
values (1, 'Client Management');
insert into app_module (module_id, description)
values (2, 'Shelter Management');
insert into app_module (module_id, description)
values (3, 'Case Management');
insert into app_module (module_id, description)
values (4, 'System Administration');
insert into app_module (module_id, description)
values (5, 'Reports');


insert into report (reportno, title, description, orgapplicable, reporttype, dateoption, datepart, reportgroup, notes, tablename, updatedby, updateddate, sptorun)
values (81, 'Shelter Bed Logs', 'Bed Logs in a given shelter on given dates ', 1, 'RPT', 'B', 'D', 15, null, 'V_REP_BEDLOG', 'oscardoc', '2007-12-07 09:37:47', null);
insert into report (reportno, title, description, orgapplicable, reporttype, dateoption, datepart, reportgroup, notes, tablename, updatedby, updateddate, sptorun)
values (82, 'Client List', 'Under designing, please do not test', 1, 'RPT', 'N', null, 5, null, null, 'mespina', '2007-12-07 09:37:47', null);
insert into report (reportno, title, description, orgapplicable, reporttype, dateoption, datepart, reportgroup, notes, tablename, updatedby, updateddate, sptorun)
values (83, 'Case List', 'Under designing, please do not test', 1, 'RPT', 'N', null, 10, null, null, 'mespina', '2007-12-07 09:37:47', null);
insert into report (reportno, title, description, orgapplicable, reporttype, dateoption, datepart, reportgroup, notes, tablename, updatedby, updateddate, sptorun)
values (200594, 'User List', 'List of Users', 1, 'RPT', 'L', 'M', 90, null, 'V_REP_USERLIST', 'oscardoc', '2008-02-22 23:19:44', null);


insert into report_date_sp (reportno, startdate, enddate, asofdate, startdate_s, enddate_s, asofdate_s, sptorun)
values (88, '2007-12-06', '2008-02-08', '2007-12-06', '20071206', '20080208', '20071206', 'sp_cr_insert_empreghrs_tbl');

insert into report_document (docid, subject, privacycd, ownerid, checkoutyn, checkoutuserid, checkoutdate, doctype, filename, moduleid, refno, filetype, viewid, viewrefno, revdatetime)
values (200696, 'List Of Users', 'P', 'oscardoc', '0', null, null, 'RPT', 'CR_USER_LIST.rpt', 'REPORT', null, null, null, null, '2008-02-27 14:36:13');

insert into report_filter (reportno, fieldno, fieldname, fielddesc, fieldtype, lookup_table, iscrosstabheaders, operator, lookup_tree, fieldsql, lookup_script, note, valueformat)
values (200594, 200618, 'ROLE_NAME', null, 'S', 'ROL', null, 'C', null, 'ROLE_NAME', null, null, null);
insert into report_filter (reportno, fieldno, fieldname, fielddesc, fieldtype, lookup_table, iscrosstabheaders, operator, lookup_tree, fieldsql, lookup_script, note, valueformat)
values (200594, 200599, 'FIRST_NAME', null, 'S', null, null, 'CL', null, 'FIRST_NAME', null, null, null);
insert into report_filter (reportno, fieldno, fieldname, fielddesc, fieldtype, lookup_table, iscrosstabheaders, operator, lookup_tree, fieldsql, lookup_script, note, valueformat)
values (200594, 200600, 'Last Name', null, 'S', null, null, 'CL', null, 'LAST_NAME', null, null, null);
insert into report_filter (reportno, fieldno, fieldname, fielddesc, fieldtype, lookup_table, iscrosstabheaders, operator, lookup_tree, fieldsql, lookup_script, note, valueformat)
values (81, 1, 'Last Name', 'Client Last Name', 'S', 'BED', 'N', 'CL', null, 'LAST_NAME', null, null, null);
insert into report_filter (reportno, fieldno, fieldname, fielddesc, fieldtype, lookup_table, iscrosstabheaders, operator, lookup_tree, fieldsql, lookup_script, note, valueformat)
values (81, 2, 'First Name', 'Client First Name', 'S', 'BED', 'N', 'CL', null, 'FIRST_NAME', null, null, null);
insert into report_filter (reportno, fieldno, fieldname, fielddesc, fieldtype, lookup_table, iscrosstabheaders, operator, lookup_tree, fieldsql, lookup_script, note, valueformat)
values (81, 3, 'Room Name', 'Room Name', 'S', null, 'N', 'CL', null, 'ROOM_NAME', null, null, null);

insert into report_lk_reportgroup (id, description, shortdesc, activeyn, orderbyindex, note)
values (90, 'System Reserved', 'System', 'Y', 1500, 'This code is reserved for QGView use');
insert into report_lk_reportgroup (id, description, shortdesc, activeyn, orderbyindex, note)
values (5, 'Client Management', 'Client', 'Y', 1240, null);
insert into report_lk_reportgroup (id, description, shortdesc, activeyn, orderbyindex, note)
values (10, 'Case Management', 'Case', 'Y', 1241, null);
insert into report_lk_reportgroup (id, description, shortdesc, activeyn, orderbyindex, note)
values (15, 'Facility Management', 'Facility', 'Y', 1246, null);

insert into report_option (reportno, reportoptionid, optiontitle, longdesc, activeyn, defaultyn, datefield, datefielddesc, sqlwhere, sqlorderby, rptfilename, rptfileno, rptversion, datefieldtype)
values (81, 2, 'Bed log by data not works', 'Bed Log by data not works', 1, 0, 'ADMISSION_DATE', 'Admission Date', null, null, 'CR_SHEL_BEDLOG.rpt', 0, null, null);
insert into report_option (reportno, reportoptionid, optiontitle, longdesc, activeyn, defaultyn, datefield, datefielddesc, sqlwhere, sqlorderby, rptfilename, rptfileno, rptversion, datefieldtype)
values (81, 1, 'Bed log by date', 'Bed Log by date', 1, 1, 'ADMISSION_DATE', 'Admission Date', null, null, 'CR_SHEL_BEDLOG.rpt', 0, null, null);
insert into report_option (reportno, reportoptionid, optiontitle, longdesc, activeyn, defaultyn, datefield, datefielddesc, sqlwhere, sqlorderby, rptfilename, rptfileno, rptversion, datefieldtype)
values (200594, 200595, 'List Of Users', 'List f Users', 1, 1, 'PROVIDER_NO', null, null, null, 'CR_USER_LIST.rpt', 200696, null, null);

insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'CLIENT_ID', 1, null, 'N', null, null, 'CLIENT_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'ADMISSION_DATE', 2, null, 'D', null, null, 'ADMISSION_DATE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'DISCHARGE_DATE', 3, null, 'D', null, null, 'DISCHARGE_DATE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'PROGRAM_NAME', 4, null, 'S', null, null, 'PROGRAM_NAME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'PROGRAM_DESCRIPTION', 5, null, 'S', null, null, 'PROGRAM_DESCRIPTION', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'BED_ID', 6, null, 'N', null, null, 'BED_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'BED_NAME', 7, null, 'S', null, null, 'BED_NAME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'ROOM_ID', 8, null, 'N', null, null, 'ROOM_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'ROOM_NAME', 9, null, 'S', null, null, 'ROOM_NAME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'CLIENT_PROG_ST_NAME', 10, null, 'S', null, null, 'CLIENT_PROG_ST_NAME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'LAST_NAME', 11, null, 'S', null, null, 'LAST_NAME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'FIRST_NAME', 12, null, 'S', null, null, 'FIRST_NAME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200549, 'ORGCD', 13, null, 'N', null, null, 'ORGCD', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200554, 'PROVIDER_NO', 1, null, 'S', null, 0, 'PROVIDER.PROVIDER_NO', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200554, 'LAST_NAME', 2, 'Last Name', 'S', null, 0, 'PROVIDER.LAST_NAME', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200554, 'FIRST_NAME', 3, null, 'S', null, 0, 'PROVIDER.FIRST_NAME', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200554, 'ROLE_NAME', 4, null, 'S', null, 0, 'SECUSERROLE.ROLE_NAME', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'PROVIDER_NO', 1, null, 'S', null, null, 'PROVIDER_NO', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'LAST_NAME', 2, null, 'S', null, null, 'LAST_NAME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'FIRST_NAME', 3, null, 'S', null, null, 'FIRST_NAME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'PROVIDER_TYPE', 4, null, 'S', null, null, 'PROVIDER_TYPE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'SPECIALTY', 5, null, 'S', null, null, 'SPECIALTY', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'TEAM', 6, null, 'S', null, null, 'TEAM', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'SEX', 7, null, 'S', null, null, 'SEX', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'DOB', 8, null, 'D', null, null, 'DOB', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'ADDRESS', 9, null, 'S', null, null, 'ADDRESS', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'PHONE', 10, null, 'S', null, null, 'PHONE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'WORK_PHONE', 11, null, 'S', null, null, 'WORK_PHONE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'OHIP_NO', 12, null, 'S', null, null, 'OHIP_NO', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'RMA_NO', 13, null, 'S', null, null, 'RMA_NO', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'BILLING_NO', 14, null, 'S', null, null, 'BILLING_NO', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'HSO_NO', 15, null, 'S', null, null, 'HSO_NO', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'STATUS', 16, null, 'S', null, null, 'STATUS', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'COMMENTS', 17, null, 'S', null, null, 'COMMENTS', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200555, 'PROVIDER_ACTIVITY', 18, null, 'S', null, null, 'PROVIDER_ACTIVITY', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200556, 'PROVIDER_NO', 1, null, 'S', null, null, 'PROVIDER_NO', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200556, 'ROLE_NAME', 2, null, 'S', null, null, 'ROLE_NAME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200611, 'LAST_NAME', 1, null, 'S', null, 0, 'DEMOGRAPHIC.LAST_NAME', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200611, 'FIRST_NAME', 2, null, 'S', null, 0, 'DEMOGRAPHIC.FIRST_NAME', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200611, 'ADDRESS', 3, null, 'S', null, 0, 'DEMOGRAPHIC.ADDRESS', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200611, 'CITY', 4, null, 'S', null, 0, 'DEMOGRAPHIC.CITY', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200611, 'PROVINCE', 5, null, 'S', null, 0, 'DEMOGRAPHIC.PROVINCE', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200611, 'POSTAL', 6, null, 'S', null, 0, 'DEMOGRAPHIC.POSTAL', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200611, 'ALIAS', 7, null, 'S', null, 0, 'DEMOGRAPHIC.ALIAS', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200611, 'INTAKE_ID', 8, null, 'N', null, 0, 'INTAKE.INTAKE_ID', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200611, 'CLIENT_ID', 9, null, 'N', null, 0, 'INTAKE.CLIENT_ID', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200611, 'STAFF_ID', 10, null, 'S', null, 0, 'INTAKE.STAFF_ID', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200611, 'CREATION_DATE', 11, null, 'D', null, 0, 'INTAKE.CREATION_DATE', null, 0, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'DEMOGRAPHIC_NO', 1, null, 'N', null, null, 'DEMOGRAPHIC_NO', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'LAST_NAME', 2, null, 'S', null, null, 'LAST_NAME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'FIRST_NAME', 3, null, 'S', null, null, 'FIRST_NAME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'ADDRESS', 4, null, 'S', null, null, 'ADDRESS', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'CITY', 5, null, 'S', null, null, 'CITY', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'PROVINCE', 6, null, 'S', null, null, 'PROVINCE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'POSTAL', 7, null, 'S', null, null, 'POSTAL', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'PHONE', 8, null, 'S', null, null, 'PHONE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'PHONE2', 9, null, 'S', null, null, 'PHONE2', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'EMAIL', 10, null, 'S', null, null, 'EMAIL', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'PIN', 11, null, 'S', null, null, 'PIN', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'YEAR_OF_BIRTH', 12, null, 'S', null, null, 'YEAR_OF_BIRTH', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'MONTH_OF_BIRTH', 13, null, 'S', null, null, 'MONTH_OF_BIRTH', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'DATE_OF_BIRTH', 14, null, 'S', null, null, 'DATE_OF_BIRTH', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'HIN', 15, null, 'S', null, null, 'HIN', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'VER', 16, null, 'S', null, null, 'VER', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'ROSTER_STATUS', 17, null, 'S', null, null, 'ROSTER_STATUS', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'PATIENT_STATUS', 18, null, 'S', null, null, 'PATIENT_STATUS', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'DATE_JOINED', 19, null, 'D', null, null, 'DATE_JOINED', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'CHART_NO', 20, null, 'S', null, null, 'CHART_NO', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'PROVIDER_NO', 21, null, 'S', null, null, 'PROVIDER_NO', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'SEX', 22, null, 'S', null, null, 'SEX', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'END_DATE', 23, null, 'D', null, null, 'END_DATE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'EFF_DATE', 24, null, 'D', null, null, 'EFF_DATE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'PCN_INDICATOR', 25, null, 'S', null, null, 'PCN_INDICATOR', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'HC_TYPE', 26, null, 'S', null, null, 'HC_TYPE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'HC_RENEW_DATE', 27, null, 'D', null, null, 'HC_RENEW_DATE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'FAMILY_DOCTOR', 28, null, 'S', null, null, 'FAMILY_DOCTOR', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'ALIAS', 29, null, 'S', null, null, 'ALIAS', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'PREVIOUSADDRESS', 30, null, 'S', null, null, 'PREVIOUSADDRESS', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'CHILDREN', 31, null, 'S', null, null, 'CHILDREN', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'SOURCEOFINCOME', 32, null, 'S', null, null, 'SOURCEOFINCOME', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'CITIZENSHIP', 33, null, 'S', null, null, 'CITIZENSHIP', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200615, 'SIN', 34, null, 'S', null, null, 'SIN', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200616, 'INTAKE_ID', 1, null, 'N', null, null, 'INTAKE_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200616, 'INTAKE_NODE_ID', 2, null, 'N', null, null, 'INTAKE_NODE_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200616, 'CLIENT_ID', 3, null, 'N', null, null, 'CLIENT_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200616, 'STAFF_ID', 4, null, 'S', null, null, 'STAFF_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200616, 'CREATION_DATE', 5, null, 'D', null, null, 'CREATION_DATE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'AM_ID', 1, null, 'N', null, null, 'AM_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'CLIENT_ID', 2, null, 'N', null, null, 'CLIENT_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'PROGRAM_ID', 3, null, 'N', null, null, 'PROGRAM_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'PROVIDER_NO', 4, null, 'S', null, null, 'PROVIDER_NO', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'ADMISSION_DATE', 5, null, 'D', null, null, 'ADMISSION_DATE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'ADMISSION_NOTES', 6, null, 'S', null, null, 'ADMISSION_NOTES', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'TEMP_ADMISSION', 7, null, 'S', null, null, 'TEMP_ADMISSION', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'DISCHARGE_DATE', 8, null, 'D', null, null, 'DISCHARGE_DATE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'DISCHARGE_NOTES', 9, null, 'S', null, null, 'DISCHARGE_NOTES', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'TEMP_ADMIT_DISCHARGE', 10, null, 'S', null, null, 'TEMP_ADMIT_DISCHARGE', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'ADMISSION_STATUS', 11, null, 'S', null, null, 'ADMISSION_STATUS', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'TEAM_ID', 12, null, 'N', null, null, 'TEAM_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'TEMPORARY_ADMISSION_FLAG', 13, null, 'N', null, null, 'TEMPORARY_ADMISSION_FLAG', 'initial set up', null, null);


insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'AGENCY_ID', 14, null, 'N', null, null, 'AGENCY_ID', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'RADIODISCHARGEREASON', 15, null, 'S', null, null, 'RADIODISCHARGEREASON', 'initial set up', null, null);
insert into report_qgviewfield (qgviewno, fieldname, fieldno, description, fieldtypecode, numbermask, fieldlength, sourcetxt, note, grouprank, lookuptable)
values (200617, 'CLIENTSTATUS_ID', 16, null, 'N', null, null, 'CLIENTSTATUS_ID', 'initial set up', null, null);

insert into report_qgviewsummary (qgviewno, qgviewcode, description, groupcode, mastertype, updatedby, updateddate, note, activeyn, secureyn, dbentity, refviews, relations, filters, object_type, distinctyn)
values (200615, 'DEMOGRAPHIC', null, '90', 'M', 'oscardoc', '2008-02-22 21:13:31', 'intial setup', '1', '0', 'DEMOGRAPHIC', 'DEMOGRAPHIC', null, null, 'TABLE', null);
insert into report_qgviewsummary (qgviewno, qgviewcode, description, groupcode, mastertype, updatedby, updateddate, note, activeyn, secureyn, dbentity, refviews, relations, filters, object_type, distinctyn)
values (200616, 'INTAKE', null, '90', 'M', 'oscardoc', '2008-02-22', 'intial setup', '1', '0', 'INTAKE', 'INTAKE', null, null, 'TABLE', null);
insert into report_qgviewsummary (qgviewno, qgviewcode, description, groupcode, mastertype, updatedby, updateddate, note, activeyn, secureyn, dbentity, refviews, relations, filters, object_type, distinctyn)
values (200617, 'ADMISSION', null, '90', 'M', 'oscardoc', '2008-02-22', 'intial setup', '1', '0', 'ADMISSION', 'ADMISSION', null, null, 'TABLE', null);
insert into report_qgviewsummary (qgviewno, qgviewcode, description, groupcode, mastertype, updatedby, updateddate, note, activeyn, secureyn, dbentity, refviews, relations, filters, object_type, distinctyn)
values (200549, 'V_REP_BEDLOG', null, '90', 'M', 'oscardoc', '2008-02-20 14:19:40', 'intial setup', '1', '0', 'V_REP_BEDLOG', 'V_REP_BEDLOG', null, null, 'VIEW', null);
insert into report_qgviewsummary (qgviewno, qgviewcode, description, groupcode, mastertype, updatedby, updateddate, note, activeyn, secureyn, dbentity, refviews, relations, filters, object_type, distinctyn)
values (200554, 'V_REP_USERLIST', 'User List', '90', 'N', 'oscardoc', '2008-02-21 15:37:41', null, '0', '0', 'V_REP_USERLIST', 'PROVIDER,SECUSERROLE', 'PROVIDER INNER JOIN SECUSERROLE ON PROVIDER.PROVIDER_NO=SECUSERROLE.PROVIDER_NO', null, 'VIEW', '1');
insert into report_qgviewsummary (qgviewno, qgviewcode, description, groupcode, mastertype, updatedby, updateddate, note, activeyn, secureyn, dbentity, refviews, relations, filters, object_type, distinctyn)
values (200611, 'V_REP_CLIENT', 'List of clients', '5', 'N', 'oscardoc', '2008-02-22 23:00:16', 'This is a master view for clients', '0', '0', 'V_REP_CLIENT', 'DEMOGRAPHIC,INTAKE', 'INTAKE INNER JOIN DEMOGRAPHIC ON INTAKE.CLIENT_ID=DEMOGRAPHIC.DEMOGRAPHIC_NO', null, 'VIEW', null);
insert into report_qgviewsummary (qgviewno, qgviewcode, description, groupcode, mastertype, updatedby, updateddate, note, activeyn, secureyn, dbentity, refviews, relations, filters, object_type, distinctyn)
values (200555, 'PROVIDER', null, '90', 'M', 'oscardoc', '2008-02-21', 'intial setup', '1', '0', 'PROVIDER', 'PROVIDER', null, null, 'TABLE', null);
insert into report_qgviewsummary (qgviewno, qgviewcode, description, groupcode, mastertype, updatedby, updateddate, note, activeyn, secureyn, dbentity, refviews, relations, filters, object_type, distinctyn)
values (200556, 'SECUSERROLE', null, '90', 'M', 'oscardoc', '2008-02-21 12:59:53', 'intial setup', '1', '0', 'SECUSERROLE', 'SECUSERROLE', null, null, 'TABLE', null);


insert into report_role (reportno, rolecode, access_type)
values (82, 'doctor', '1');
insert into report_role (reportno, rolecode, access_type)
values (81, 'doctor', '1');
insert into report_role (reportno, rolecode, access_type)
values (83, 'doctor', '1');
insert into report_role (reportno, rolecode, access_type)
values (200594, 'doctor', '1');
insert into report_role (reportno, rolecode, access_type)
values (200594, 'admin', '1');

insert into report_template (templateno, reportno, reportoptionid, description, startdate, enddate, startpayperiod, endpayperiod, loginid, updatedate, privateyn)
values (200682, 81, 2, 'asdfgh', '2008-02-04', '2008-02-22', null, null, '999998', null, 0);
insert into report_template (templateno, reportno, reportoptionid, description, startdate, enddate, startpayperiod, endpayperiod, loginid, updatedate, privateyn)
values (200686, 81, 1, 'qwert', '2008-02-01', '2008-02-28', null, null, '999998', null, 0);
insert into report_template (templateno, reportno, reportoptionid, description, startdate, enddate, startpayperiod, endpayperiod, loginid, updatedate, privateyn)
values (200622, 200594, 200595, 'List of doctors', '1899-12-30', '1899-12-30', null, null, '999998', '2008-02-22 21:37:02', 0);
insert into report_template (templateno, reportno, reportoptionid, description, startdate, enddate, startpayperiod, endpayperiod, loginid, updatedate, privateyn)
values (313, 83, 7, 'Incident Cost Exception Report - Estimated Costs =$0', '1899-12-30', '1899-12-30', null, null, '999998', '2007-11-07 10:34:00', 0);
insert into report_template (templateno, reportno, reportoptionid, description, startdate, enddate, startpayperiod, endpayperiod, loginid, updatedate, privateyn)
values (312, 83, 6, 'Fatal Public Electrical Incidents', '1899-12-30', '1899-12-30', null, null, '999998', '2007-07-10 15:32:56', 0);



insert into report_template_criteria (counter, templateno, relation, fieldno, operator, operators, val, valdesc, required)
values (200688, 200686, null, 2, '=', null, '200284', 'Bed 10', 0);
insert into report_template_criteria (counter, templateno, relation, fieldno, operator, operators, val, valdesc, required)
values (1, 200622, null, 200618, '=', 'C', 'doctor', 'doctor', 0);
insert into report_template_criteria (counter, templateno, relation, fieldno, operator, operators, val, valdesc, required)
values (1, 200605, null, 200599, 'Like', 'CL', '*doc*', null, 0);
insert into report_template_criteria (counter, templateno, relation, fieldno, operator, operators, val, valdesc, required)
values (1, 200606, null, 200599, 'Like', 'CL', '*doc*', null, 0);
insert into report_template_criteria (counter, templateno, relation, fieldno, operator, operators, val, valdesc, required)
values (200694, 200682, null, 2, '=', null, '200267', 'Bed 1', 0);
insert into report_template_criteria (counter, templateno, relation, fieldno, operator, operators, val, valdesc, required)
values (200695, 200682, 'AND', 1, '<=', null, '200268', 'Bed 2', 0);


insert into report_template_org (counter, templateno, orgcd)
values (200687, 200686, '0');
insert into report_template_org (counter, templateno, orgcd)
values (1, 200622, 'ORG');
insert into report_template_org (counter, templateno, orgcd)
values (200693, 200682, '0');




