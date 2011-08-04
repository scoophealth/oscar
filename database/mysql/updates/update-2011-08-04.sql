alter table OLISSystemPreferences add filterPatients tinyint(1);
update OLISSystemPreferences set filterPatients=0;
