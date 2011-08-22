alter table drugs add home_med boolean;
alter table drugs add comment varchar(255);
alter table drugs add start_date_unknown boolean;

update drugs set home_med=0;
update drugs set start_date_unknown=0;
update drugs set comment='';
