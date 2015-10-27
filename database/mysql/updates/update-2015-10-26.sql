delete from secObjPrivilege where roleUserGroup = 'doctor' and objectName='_phr' and privilege = 'x';
insert into `secObjectName` (`objectName`) values ('_phr');
insert into `secObjPrivilege` values('doctor','_phr','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_phr','x',0,'999998');
