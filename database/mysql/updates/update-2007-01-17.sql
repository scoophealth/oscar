#This update is for the BC-Birth summary
#
#I noticed in the cvs that the following 3 fields were not in the bc init script.


alter table formBCBirthSumMo add column ppLos char(3) default NULL;
alter table formBCBirthSumMo add column  admisDateTime varchar(16) default NULL;
alter table formBCBirthSumMo add column  dischargeDateTime varchar(16) default NULL;

create index demographic_no on formBCBirthSumMo (demographic_no);
create index formCreated on formBCBirthSumMo (formCreated);
