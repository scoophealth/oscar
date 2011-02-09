insert into issue (`code`,`description`,`role`,`update_date`) Values('CurrentHistory','Current History', 'nurse', now());

alter table eyeform_macro add discharge varchar(20);
alter table eyeform_macro add stat varchar(20);
alter table eyeform_macro add opt varchar(20);
