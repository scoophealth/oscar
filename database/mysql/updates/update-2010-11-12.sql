alter table demographic add roster_date date;

update demographic set roster_date = hc_renew_date;

alter table demographicArchive add column roster_date date;