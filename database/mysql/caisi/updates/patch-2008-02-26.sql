insert into `secObjectName` values('_pmm.clientSearch');
insert into `secObjectName` values('_pmm.newClient');
insert into `secObjectName` values('_pmm.mergeRecords');
insert into `secObjectName` values('_pmm.caseManagement');

-- PMM administration part
insert into `secObjectName` values('_admin.facilityMessage');
insert into `secObjectName` values('_admin.systemMessage');
insert into `secObjectName` values('_pmm.agencyInformation');
insert into `secObjectName` values('_pmm.manageFacilities');
insert into `secObjectName` values('_pmm.staffList');
insert into `secObjectName` values('_pmm.programList');
insert into `secObjectName` values('_pmm.addProgram');
insert into `secObjectName` values('_pmm.globalRoleAccess');
insert into `secObjectName` values('_pmm.caisiRoles');

insert into `secObjPrivilege` values('doctor','_pmm.clientSearch','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.newClient','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm.mergeRecords','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.caseManagement','x',0,999998);

insert into `secObjPrivilege` values('admin','_admin.facilityMessage','x',0,999998);
insert into `secObjPrivilege` values('admin','_admin.systemMessage','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm.agencyInformation','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm.manageFacilities','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm.staffList','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm.programList','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm.addProgram','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm.globalRoleAccess','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm.caisiRoles','x',0,999998);
