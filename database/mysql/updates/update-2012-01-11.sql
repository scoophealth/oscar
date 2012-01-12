alter table prescription add column lastUpdateDate datetime;
update prescription set lastUpdateDate=now();
alter table prescription change column lastUpdateDate lastUpdateDate datetime not null;