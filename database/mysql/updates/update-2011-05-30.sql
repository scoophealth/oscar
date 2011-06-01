alter table Facility add column enableEncounterTime tinyint(1) not null;
alter table Facility add column enableEncounterTransportationTime tinyint(1) not null;
alter table casemgmt_note add column hourOfEncounterTime int;
alter table casemgmt_note add column minuteOfEncounterTime int;
alter table casemgmt_note add column hourOfEncTransportationTime int;
alter table casemgmt_note add column minuteOfEncTransportationTime int;