#added a security object named _rx
#this controls whether the rx module  is accessible for certain roles.
#to patch your database (add the permission for the doctor, locum and nurse roles), see database/mysql/updates

insert into secObjectName values ('_rx');
insert into secObjPrivilege values ('doctor','_rx','|*|',0,999998);
insert into secObjPrivilege values ('nurse','_rx','|*|',0,999998);
insert into secObjPrivilege values ('locum','_rx','|*|',0,999998);
