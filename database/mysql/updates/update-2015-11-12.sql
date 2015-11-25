alter table security add storageVersion int NOT NULL;
update security set storageVersion = 1;
alter table security modify password varchar(255);
alter table security modify pin varchar(255);
alter table security add passwordUpdateDate datetime;
alter table security add pinUpdateDate datetime;
alter table security add lastUpdateUser varchar(20);
alter table security add lastUpdateDate timestamp;
