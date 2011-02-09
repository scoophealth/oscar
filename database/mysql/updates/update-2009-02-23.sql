alter table demographic change column preferred_lang official_lang varchar(60);
alter table demographic add column spoken_lang varchar(60) after official_lang;
alter table drugs change column outside_provider outside_provider_name varchar(100);
alter table drugs add column outside_provider_ohip varchar(20);
alter table drugs add column written_date date after end_date;

