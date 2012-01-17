alter table preventions add column lastUpdateDate datetime not null;
update preventions set lastUpdateDate=now();

