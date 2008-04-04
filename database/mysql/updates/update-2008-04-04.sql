--Change primary key of hash audit table
alter table hash_audit drop primary key;
alter table hash_audit add column pkid int(10) auto_increment primary key first;

--set cpp issues
insert into issue (`code`,`description`,`role`,`update_date`) Values('OMeds','Other Meds as part of cpp', 'nurse', now());
insert into issue (`code`,`description`,`role`,`update_date`) Values('SocHistory','Social History as part of cpp', 'nurse', now());
insert into issue (`code`,`description`,`role`,`update_date`) Values('MedHistory','Medical History as part of cpp', 'nurse', now());
insert into issue (`code`,`description`,`role`,`update_date`) Values('Concerns','Ongoing Concerns as part of cpp', 'nurse', now());
insert into issue (`code`,`description`,`role`,`update_date`) Values('Reminders','Reminders as part of cpp', 'nurse', now());