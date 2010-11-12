alter table demographic add roster_date date;

update table demographic set roster_date = hc_renew_date;