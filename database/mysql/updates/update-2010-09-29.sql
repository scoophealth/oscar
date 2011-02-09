update eform_data set patient_independent=0 where patient_independent is null;
alter table eform_data change patient_independent patient_independent tinyint(1) not null;
