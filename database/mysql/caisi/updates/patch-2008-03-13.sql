alter table facility change id id int auto_increment;

insert into `secObjectName` values('_admin.provider');
insert into `secObjectName` values('_admin.security');
insert into `secObjectName` values('_admin.securityLogReport');
insert into `secObjectName` values('_admin.unlockAccount');
insert into `secObjectName` values('_admin.cookieRevolver');
insert into `secObjectName` values('_admin.caisi');
insert into `secObjectName` values('_admin.lookupFieldEditor');
insert into `secObjectName` values('_admin.issueEditor');
insert into `secObjectName` values('_admin.caisiRoles');
insert into `secObjectName` values('_admin.userCreatedForms');


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



