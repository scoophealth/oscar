-- INSERT INTO `secObjectName` (`objectName`, `description`, `orgapplicable`, `note`) VALUES 
--  ('_casemgmt.issues','Access to Case Management Issues',0,NULL),
--  ('_casemgmt.notes','Permissions for Case Management Notes',0,NULL);
-- COMMIT;

insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_casemgmt.issues','Access to Case Management Issues',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_casemgmt.notes','Permissions for Case Management Notes',0);

insert into `secObjPrivilege` values('doctor', '_casemgmt.issues', 'x', 0, '999998');
insert into `secObjPrivilege` values('doctor', '_casemgmt.notes', 'x', 0, '999998');

insert into `secObjPrivilege` values('admin', '_casemgmt.issues', 'x', 0, '999998');
insert into `secObjPrivilege` values('admin', '_casemgmt.notes', 'x', 0, '999998');

insert into `secObjPrivilege` values('locum', '_casemgmt.issues', 'x', 0, '999998');
insert into `secObjPrivilege` values('locum', '_casemgmt.notes', 'x', 0, '999998');

insert into `secObjPrivilege` values('receptionist', '_casemgmt.issues', 'x', 0, '999998');
insert into `secObjPrivilege` values('receptionist', '_casemgmt.notes', 'x', 0, '999998');

insert into `secObjPrivilege` values('nurse', '_casemgmt.issues', 'x', 0, '999998');
insert into `secObjPrivilege` values('nurse', '_casemgmt.notes', 'x', 0, '999998');




