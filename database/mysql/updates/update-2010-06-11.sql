update provider set dob=null where dob='0001-01-01';

update demographic set end_date=null where end_date='0001-01-01';
update demographic set eff_date=null where eff_date='0001-01-01';
update demographic set hc_renew_date=null where hc_renew_date='0001-01-01';