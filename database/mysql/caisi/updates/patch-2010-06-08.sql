alter table secRole modify role_name varchar(60) NOT NULL default '';
delete from program_provider where role_id is null;
update secRole set role_name="counsellor" where role_name="Counsellor";