create table `eyeform_followup` (
	`id` integer not null auto_increment,
	`appointment_no` integer,
	`demographic_no` integer,
	`timespan` integer,
	`timeframe` varchar(25),
	`followup_provider` varchar(100),
	`date` timestamp,
	primary key(id)
);

alter table document add appointment_no integer;

insert into issue (code,description,role,update_date,priority,type) values ('PastOcularHistory','Past Ocular History','nurse',now(),NULL,'system');
insert into issue (code,description,role,update_date,priority,type) values ('DiagnosticNotes','Diagnostic Notes','nurse',now(),NULL,'system');
insert into issue (code,description,role,update_date,priority,type) values ('OcularMedication','Ocular Medication','nurse',now(),NULL,'system');
insert into issue (code,description,role,update_date,priority,type) values ('PatientLog','Patient Log','nurse',now(),NULL,'system');
