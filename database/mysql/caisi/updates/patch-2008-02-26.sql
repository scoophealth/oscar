-- insert into access_type	(name, type) values("Run Report Runner","access");
-- insert into access_type	(name, type) values("Design Reports","access");

-- insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='Run Report Runner'));
-- insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='Design Reports'));

DROP TABLE IF EXISTS `report`;
create table report
(
  reportno     	bigint(10) 	NOT NULL auto_increment,
  title         varchar(80) default NULL,
  description	varchar(255) default NULL,
  orgapplicable tinyint(1) 	default 0,
  reporttype	varchar(3) 	default NULL,
  dateoption	varchar(1)	default NULL,
  datepart		varchar(1)	default NULL,
  reportgroup	int(10)		default 0,
  notes			text 		default NULL,
  tablename		varchar(30)	default NULL,
  updatedby		varchar(20)	default NULL,
  updateddate	datetime 	default NULL,
  sptorun		varchar(32) default NULL,
  PRIMARY KEY  (reportno)
);

DROP TABLE IF EXISTS `report_date`;
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


insert into report (reportno,title,description,orgapplicable,reporttype,dateoption,datepart,reportgroup,notes,tablename,updatedby,updateddate,sptorun)
values (81, 'Shelter Bed Logs', 'Bed Logs in a given shelter on given dates ', 1, 'RPT', 'B', 'D', 15, null, 'v_rep_bedlog', 'mespina', '2007-07-12 09:37:47', null);
insert into report (reportno,title,description,orgapplicable,reporttype,dateoption,datepart,reportgroup,notes,tablename,updatedby,updateddate,sptorun)
values (82, 'Client List', 'Under designing, please do not test', 1, 'RPT', 'N', null, 5, null, null, 'mespina', '2007-07-12 09:37:47', null);
insert into report (reportno,title,description,orgapplicable,reporttype,dateoption,datepart,reportgroup,notes,tablename,updatedby,updateddate,sptorun)
values (83, 'Case List', 'Under designing, please do not test', 1, 'RPT', 'N', null, 10, null, null, 'mespina', '2007-07-12 09:37:47', null);


insert into report_date (sessionid,startdate,enddate,asofdate,startdate_s,enddate_s,asofdate_s)
values ('dMEYTjLW-JjQmxcKvmf3GHz','2008-02-11', '2008-02-22', null, null, null, null);

insert into report_date_sp (reportno,startdate,enddate,asofdate,startdate_s,enddate_s,asofdate_s,sptorun)
values (88, '2007-12-06', '2008-02-08', '2007-12-06', '20071206', '20080208', '20071206', 'sp_cr_insert_empreghrs_tbl');


insert into report_filter (reportno,fieldno,fieldname,fielddesc,fieldtype,lookup_table,iscrosstabheaders,operator,lookup_tree,fieldsql,lookup_script,note,valueformat)
values (81, 1, 'Last Name', 'Client Last Name', 'S', null, 'N', 'CL', null, 'LAST_NAME', null, null, null);
insert into report_filter (reportno,fieldno,fieldname,fielddesc,fieldtype,lookup_table,iscrosstabheaders,operator,lookup_tree,fieldsql,lookup_script,note,valueformat)
values (81, 2, 'First Name', 'Client First Name', 'S', null, 'N', 'CL', null, 'FIRST_NAME', null, null, null);
insert into report_filter (reportno,fieldno,fieldname,fielddesc,fieldtype,lookup_table,iscrosstabheaders,operator,lookup_tree,fieldsql,lookup_script,note,valueformat)
values (81, 3, 'Room Name', 'Room Name', 'S', null, 'N', 'CL', null, 'ROOM_NAME', null, null, null);

insert into report_lk_reportgroup (id,description,shortdesc,activeyn,orderbyindex, note)
values (5, 'Client Management', 'Client', 'Y', 1240, null);
insert into report_lk_reportgroup (id,description,shortdesc,activeyn,orderbyindex, note)
values (10, 'Case Management', 'Case', 'Y', 1241, null);
insert into report_lk_reportgroup (id,description,shortdesc,activeyn,orderbyindex, note)
values (15, 'Facility Management', 'Facility', 'Y', 1246, null);

insert into report_option (reportno,reportoptionid,optiontitle,longdesc,activeyn,defaultyn,datefield,datefielddesc,sqlwhere,sqlorderby,rptfilename,rptfileno,rptversion,datefieldtype)
values (81, 1, 'Bed log by date', 'Bed Log by date', 1, 1, 'ADMISSION_DATE', 'Admission Date', null, null, 'CR_SHEL_BEDLOG.rpt', 0, null, null);


insert into report_role (reportno, rolecode, access_type)
values (82, 'admin', 'W');
insert into report_role (reportno, rolecode, access_type)
values (82, 'doctor', 'W');
insert into report_role (reportno, rolecode, access_type)
values (81, 'admin', 'W');
insert into report_role (reportno, rolecode, access_type)
values (81, 'doctor', 'W');
insert into report_role (reportno, rolecode, access_type)
values (83, 'admin', 'W');
insert into report_role (reportno, rolecode, access_type)
values (83, 'doctor', 'W');























