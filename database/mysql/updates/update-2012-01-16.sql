alter table allergies add column lastUpdateDate datetime;
update allergies set lastUpdateDate=now();
alter table allergies change column lastUpdateDate lastUpdateDate datetime not null;

alter table allergies add column providerNo varchar(6);
