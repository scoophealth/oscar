# add new field '_newCasemgmt.templates to Assign Role/Right to Object
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.templates');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.templates','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.templates','x',0,'999998');

