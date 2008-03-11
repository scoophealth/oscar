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

insert into `secObjPrivilege` values('doctor','_pmm_editProgram.general','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.staff','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.functionUser','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.teams','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.clients','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.queue','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.access','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.bedCheck','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.clientStatus','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.serviceRestrictions','|*|',0,999998);

INSERT INTO `access_type` (name,type) VALUES ('read doctor ticklers','access'),('read nurse ticklers','access'),('read counsellor ticklers','access'),('read csw ticklers','access');
