alter table provider drop practitionerNo;
alter table provider drop init;
alter table provider drop job_title;
alter table provider drop email;
alter table provider drop title;
alter table provider drop lastUpdateUser;
alter table provider drop lastUpdateDate;

alter table provider add lastUpdated datetime not null;
update provider set lastUpdated=now();