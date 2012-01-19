alter table measurementType add column createDate datetime;
update measurementType set createDate=now();
alter table measurementType change column createDate createDate datetime not null;
