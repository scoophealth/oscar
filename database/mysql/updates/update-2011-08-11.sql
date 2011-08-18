alter table demographic change pin myOscarUserName varchar(255);
update demographic set myOscarUserName=null where  myOscarUserName='@myoscar.org';
update demographic set myOscarUserName=null where  myOscarUserName='';
alter table demographic add unique(myOscarUserName);

