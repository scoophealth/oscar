--database changes --
alter table casemgmt_note add column observation_date datetime NOT NULL default '0000-00-00 00:00:00';
alter table casemgmt_tmpsave add column note_id int(10);
alter table program_provider modify provider_no varchar(6) NOT NULL;
alter table admission modify provider_no varchar(6) NOT NULL;
alter table casemgmt_cpp add column provider_no varchar(6) NOT NULL;