alter table measurements modify dataField varchar(20);

update validations set regularExp="YES|yes|Yes|Y|NO|no|No|N|NotApplicable" where name="Yes/No";

