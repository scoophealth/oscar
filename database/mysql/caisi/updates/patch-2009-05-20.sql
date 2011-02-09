alter table Facility drop allowQuickConsent;
alter table Facility add column lastUpdate datetime not null;
update Facility set lastUpdate=now();