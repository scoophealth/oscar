alter table demographic change pin myOscarUserName varchar(255);
alter table demographic add unique(myOscarUserName);
update demographic set myOscarUserName=null where  myOscarUserName='';

