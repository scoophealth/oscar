alter table caisi_form_instance add column program_id int after client_id;
alter table caisi_form_instance_tmpsave add column program_id int after client_id;
alter table casemgmt_issue add column program_id int after resolved;
alter table relationships add column facility_id int after id;


insert into `secObjectName` values('_pmm_editProgram.general');
insert into `secObjectName` values('_pmm_editProgram.staff');
insert into `secObjectName` values('_pmm_editProgram.functionUser');
insert into `secObjectName` values('_pmm_editProgram.teams');
insert into `secObjectName` values('_pmm_editProgram.clients');
insert into `secObjectName` values('_pmm_editProgram.queue');
insert into `secObjectName` values('_pmm_editProgram.access');
insert into `secObjectName` values('_pmm_editProgram.bedCheck');
insert into `secObjectName` values('_pmm_editProgram.clientStatus');
insert into `secObjectName` values('_pmm_editProgram.serviceRestrictions');

insert into `secObjPrivilege` values('admin','_pmm_editProgram.general','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm_editProgram.staff','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm_editProgram.functionUser','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm_editProgram.teams','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm_editProgram.clients','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm_editProgram.queue','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm_editProgram.access','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm_editProgram.bedCheck','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm_editProgram.clientStatus','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm_editProgram.serviceRestrictions','x',0,999998);

INSERT INTO `access_type` (name,type) VALUES ('read doctor ticklers','access'),('read nurse ticklers','access'),('read counsellor ticklers','access'),('read csw ticklers','access');
