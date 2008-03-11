alter table intake_node add column mandatory boolean not null default false;
alter table survey add column facilityId int after userId; 
alter table caisi_form add column facilityId int after form_id; 

