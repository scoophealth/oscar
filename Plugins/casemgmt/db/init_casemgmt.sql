drop table if exists issue;
CREATE TABLE issue (
	issue_id int(10) not null auto_increment,
	code varchar(20) not null,
	description varchar(255) not null,
	role varchar(100) not null,
	update_date datetime not null,
	primary key(issue_id)
);



drop table if exists casemgmt_issue;
CREATE TABLE casemgmt_issue (
	id int(10) not null auto_increment,
	demographic_no varchar(20) not null,
	issue_id int(10) not null,
	acute boolean not null,
	certain boolean not null,
	major boolean not null,
	resolved boolean not null, 
	type varchar(100) not null,
	update_date datetime not null,
	primary key(id)
);



drop table if exists casemgmt_note;
CREATE TABLE casemgmt_note (
	note_id int(10) not null auto_increment,
	update_date datetime not null,
	demographic_no varchar(20) not null,
	provider_no varchar(20) not null,
	note text not null,
	signed boolean not null,
	include_issue_innote boolean not null,
	signing_provider_no varchar(20) not null,
	encounter_type varchar(100) not null,
	billing_code varchar(100) not null,
	program_no varchar(20) NOT NULL default '',
	agency_no varchar(20) NOT NULL default '',
	reporter_caisi_role varchar(20) NOT NULL default '',
	reporter_program_team varchar(20) NOT NULL default '',
	primary key(note_id)
);

drop table if exists casemgmt_cpp;
create table casemgmt_cpp (
	id int(10) not null auto_increment,
	demographic_no varchar(10) not null,
	socialHistory text,
	familyHistory text,
	medicalHistory text,
	ongoingConcerns text,
	reminders text,
	update_date timestamp not null default CURRENT_TIMESTAMP,
	primary key(id)
);

drop table if exists casemgmt_issue_notes;
CREATE TABLE `casemgmt_issue_notes` (                                                                                                                                                      
	id int(10) NOT NULL default '0',                                                                                                                                                       
	note_id int(10) NOT NULL default '0',                                                                                                                                                  
	PRIMARY KEY  (id, note_id)                                                                                                                                                            
);


INSERT INTO issue (code,description,role,update_date) values('CTCMM1000','Safety','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM2000','Health','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM3000','Personal ID','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM4000','Financial','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM5000','Legal','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM6000','Housing','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM7000','Education','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM8000','Employment','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM9000','Family','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM10000','Community Support','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM11000','Psycho-social concerns','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM12000','Client feedback / suggestions','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM13000','Physical Health','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM14000','Mental Health','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM15000','Addictions','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM16000','Social Support','counsellor',Now());


INSERT INTO issue (code,description,role,update_date) values('ICSW100','program-client conflict','CSW',Now());
INSERT INTO issue (code,description,role,update_date) values('ICSW200','difficulties with hygeine','CSW',Now());
INSERT INTO issue (code,description,role,update_date) values('ICSW300','difficulties with eating','CSW',Now());
INSERT INTO issue (code,description,role,update_date) values('ICSW301','anorexia, loss of apetite','CSW',Now());

INSERT INTO issue (code, role,update_date,description) SELECT icd9.icd9,'doctor',now(),icd9.description from icd9;
