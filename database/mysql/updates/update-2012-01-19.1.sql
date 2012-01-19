alter table measurementsDeleted add column originalId int(10) unsigned not null;
update measurementsDeleted set originalId = 0;

