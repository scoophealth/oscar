
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_pmm.clientSearch', 'Client - Search', 0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_pmm.newClient', 'Client - New Client',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_pmm.mergeRecords', 'Client - Merge Records',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_pmm.caseManagement','PMM - Case Management',0);

-- PMM administration part

insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.facilityMessage', 'Administration - Facility Message',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.systemMessage', 'Administration - System Message',0);

insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_pmm.agencyInformation','Program - Agency Information',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_pmm.manageFacilities','Program - Manage Facilities',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_pmm.staffList','Program - Staff List',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_pmm.programList','Program - Program List',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_pmm.addProgram','Program - Add Program',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_pmm.globalRoleAccess','Program - Global Role Access',0);


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
