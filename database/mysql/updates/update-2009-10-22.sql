-- new columns to help manage meds in the drug profile.
alter table drugs add column archived_reason varchar(100) default 'deleted';
alter table drugs add column archived_date datetime;
alter table drugs add column hide_from_drug_profile tinyint(1) default '0';