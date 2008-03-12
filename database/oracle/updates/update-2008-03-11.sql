alter table client_referral add facility_id number(10);

alter table intake_node add mandatory number(1) default 0 not null;
Drop table survey;
create table SURVEY
(
  SURVEYID             NUMBER not null,
  DESCRIPTION          VARCHAR2(255),
  SURVEYDATA           CLOB,
  STATUS               NUMBER,
  USERID               NUMBER,
  FACILITY_ID          NUMBER,  -- newly added
  DATECREATED          DATE,
  DATELAUNCHED         DATE,
  DATECLOSED           DATE,
  LAUNCHED_INSTANCE_ID NUMBER,
  VERSION              NUMBER default '0',
  primary key (SURVEYID)
);
Drop table CAISI_FORM;
create table CAISI_FORM
(
  FORM_ID     NUMBER not null,
  FACILITYID  NUMBER,           -- newly added
  DESCRIPTION VARCHAR2(255),
  SURVEYDATA  CLOB,
  STATUS      NUMBER(6),
  VERSION     NUMBER default '0',
  primary key (FORM_ID)
);
alter table intake add facility_id number;
alter table admission add automatic_discharge number(1) default 0 not null;
