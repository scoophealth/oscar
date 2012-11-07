update admission set clientstatus_id=NULL where clientstatus_id=0;
update admission set team_id=NULL where team_id=0;
alter table quickListUser change providerNo providerNo varchar(20) not null;
alter table secUserRole change role_name role_name varchar(60) not null;
