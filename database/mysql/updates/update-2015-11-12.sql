alter table security add storageVersion int NOT NULL;
update security set storageVersion = 1;
alter table security modify password varchar(255);
alter table security modify pin varchar(255);
