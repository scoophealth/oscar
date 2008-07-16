alter table facility change id id int auto_increment;


insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.provider', 'Administration - Provider',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.security', 'Administration - Security',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.securityLogReport', 'Administration - Security Log Report',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.unlockAccount', 'Administration - Unlock Account',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.cookieRevolver', 'Administration - Cookie Revolver',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.caisi', 'Administration - Caisi',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.lookupFieldEditor', 'Administration - Lookup Field Editor',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.issueEditor', 'Administration - Issue Editor',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.caisiRoles', 'Administration - Manage Caisi Roles',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.userCreatedForms', 'Administration - User Created Forms',0);


insert into `secObjPrivilege` values('admin','_admin.provider','x',0,999998);
insert into `secObjPrivilege` values('admin','_admin.security','x',0,999998);
insert into `secObjPrivilege` values('admin','_admin.securityLogReport','x',0,999998);
insert into `secObjPrivilege` values('admin','_admin.unlockAccount','x',0,999998);
insert into `secObjPrivilege` values('admin','_admin.cookieRevolver','x',0,999998);
insert into `secObjPrivilege` values('admin','_admin.caisi','x',0,999998);
insert into `secObjPrivilege` values('admin','_admin.lookupFieldEditor','x',0,999998);
insert into `secObjPrivilege` values('admin','_admin.issueEditor','x',0,999998);
insert into `secObjPrivilege` values('admin','_admin.caisiRoles','x',0,999998);
insert into `secObjPrivilege` values('admin','_admin.userCreatedForms','x',0,999998);



