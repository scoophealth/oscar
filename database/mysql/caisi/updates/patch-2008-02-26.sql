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


insert into `secObjPrivilege` values('doctor','_pmm.clientSearch','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.newClient','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.mergeRecords','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.caseManagement','|*|',0,999998);

insert into `secObjPrivilege` values('doctor','_admin.facilityMessage','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_admin.systemMessage','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.agencyInformation','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.manageFacilities','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.staffList','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.programList','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.addProgram','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.globalRoleAccess','|*|',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.caisiRoles','|*|',0,999998);
