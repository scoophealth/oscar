alter table app_lookuptable add readonly tinyint(1);
update app_lookuptable set readonly=0;
alter table app_lookuptable_fields add fieldlength int(10);
update app_lookuptable_fields set fieldlength=0;

