alter table program add column enableEncounterTime tinyint(1);
alter table program add column enableEncounterTransportationTime tinyint(1);

update program set enableEncounterTime = 0;
update program set enableEncounterTransportationTime = 0;