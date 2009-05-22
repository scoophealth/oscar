alter table provider add lastUpdated datetime not null;
update provider set lastUpdated=now();