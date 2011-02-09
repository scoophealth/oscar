alter table eform add roleType varchar(50) default null;
alter table eform_data add roleType varchar(50) default null;
insert into `secObjectName` (`objectName`) values ('_eform.doctor');
insert into `secObjPrivilege` values('doctor','_eform.doctor','x',0,'999998');




