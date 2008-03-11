#This script is originated by MySQL
drop table  relationships;
create table RELATIONSHIPS
(
  ID                      NUMBER(10) not null,
  FACILITY_ID             NUMBER(10) default '0' not null,
  DEMOGRAPHIC_NO          NUMBER(10) default '0' not null,
  RELATION_DEMOGRAPHIC_NO NUMBER(10) default '0' not null,
  RELATION                VARCHAR2(20),
  CREATION_DATE           DATE,
  CREATOR                 VARCHAR2(6),
  SUB_DECISION_MAKER      VARCHAR2(1) default '0',
  EMERGENCY_CONTACT       VARCHAR2(1) default '0',
  NOTES                   VARCHAR2(4000),
  DELETED                 VARCHAR2(1) default '0',
  primary key (ID)
)

alter table caisi_form_instance add  program_id number;
alter table caisi_form_instance_tmpsave add  program_id number;
Drop table CASEMGMT_ISSUE;
create table CASEMGMT_ISSUE
(
  ID             NUMBER(10) not null,
  DEMOGRAPHIC_NO VARCHAR2(20),
  ISSUE_ID       NUMBER(10) default '0' not null,
  ACUTE          NUMBER(1) default '0' not null,
  CERTAIN        NUMBER(1) default '0' not null,
  MAJOR          NUMBER(1) default '0' not null,
  RESOLVED       NUMBER(1) default '0' not null,
  PROGRAM_ID     NUMBER(10),
  TYPE           VARCHAR2(100),
  UPDATE_DATE    DATE default '1900/01/01' not null,
  primary key (ID)
)
;
create index IDX_CASEMGMT_ISSUE_1 on CASEMGMT_ISSUE (ISSUE_ID)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );


insert into secObjectName values('_pmm_editProgram.general');
insert into secObjectName values('_pmm_editProgram.staff');
insert into secObjectName values('_pmm_editProgram.functionUser');
insert into secObjectName values('_pmm_editProgram.teams');
insert into secObjectName values('_pmm_editProgram.clients');
insert into secObjectName values('_pmm_editProgram.queue');
insert into secObjectName values('_pmm_editProgram.access');
insert into secObjectName values('_pmm_editProgram.bedCheck');
insert into secObjectName values('_pmm_editProgram.clientStatus');
insert into secObjectName values('_pmm_editProgram.serviceRestrictions');

insert into secObjPrivilege values('doctor','_pmm_editProgram.general','|*|',0,999998);
insert into secObjPrivilege values('doctor','_pmm_editProgram.staff','|*|',0,999998);
insert into secObjPrivilege values('doctor','_pmm_editProgram.functionUser','|*|',0,999998);
insert into secObjPrivilege values('doctor','_pmm_editProgram.teams','|*|',0,999998);
insert into secObjPrivilege values('doctor','_pmm_editProgram.clients','|*|',0,999998);
insert into secObjPrivilege values('doctor','_pmm_editProgram.queue','|*|',0,999998);
insert into secObjPrivilege values('doctor','_pmm_editProgram.access','|*|',0,999998);
insert into secObjPrivilege values('doctor','_pmm_editProgram.bedCheck','|*|',0,999998);
insert into secObjPrivilege values('doctor','_pmm_editProgram.clientStatus','|*|',0,999998);
insert into secObjPrivilege values('doctor','_pmm_editProgram.serviceRestrictions','|*|',0,999998);


INSERT INTO access_type (access_id,name,type) VALUES (60,'read doctor ticklers','access');
INSERT INTO access_type (access_id,name,type) VALUES (61,'read nurse ticklers','access');
INSERT INTO access_type (access_id,name,type) VALUES (62,'read counsellor ticklers','access');
INSERT INTO access_type (access_id,name,type) VALUES (63,'read csw ticklers','access');


