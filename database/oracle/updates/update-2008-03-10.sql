#This script is originated by MySQL
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
