alter table eform add column showLatestFormOnly boolean not null;
alter table eform modify patient_independent boolean not null;

alter table eform_data add column showLatestFormOnly boolean not null;

