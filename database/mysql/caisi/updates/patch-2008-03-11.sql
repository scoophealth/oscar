alter table intake_node add column mandatory boolean not null default false;
alter table survey add column facilityId int after userId; 
alter table caisi_form add column facilityId int after form_id; 

alter table intake add column facility_id int(11);
alter table admission add column automatic_discharge tinyint(1) default 0;
alter table client_referral add facility_id int;