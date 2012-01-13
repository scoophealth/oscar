alter table drugs add column lastUpdateDate datetime;
update drugs set lastUpdateDate=now();
alter table drugs change column lastUpdateDate lastUpdateDate datetime not null;