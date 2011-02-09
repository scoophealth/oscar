#added a security object named _masterLink
#this controls whether the link for the master file is displayed for certain roles.
#to patch your database (add the permission for the 5 default roles), see database/mysql/updates

insert into secObjectName values ('_masterLink');
insert into secObjPrivilege values ('receptionist','_masterLink','|*|',0,999998);
insert into secObjPrivilege values ('doctor','_masterLink','|*|',0,999998);
insert into secObjPrivilege values ('nurse','_masterLink','|*|',0,999998);
insert into secObjPrivilege values ('locum','_masterLink','|*|',0,999998);
insert into secObjPrivilege values ('admin','_masterLink','|*|',0,999998);
