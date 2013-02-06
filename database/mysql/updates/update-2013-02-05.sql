alter table measurementsDeleted change comments comments varchar(255);
alter table measurementsDeleted change dateObserved dateObserved datetime;

alter table measurements change appointmentNo appointmentNo int(10);