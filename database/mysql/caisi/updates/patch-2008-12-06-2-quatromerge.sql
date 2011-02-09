
#alter table app_lookuptable add `readonly` integer default 0;

#alter table app_lookuptable_fields add fieldLength int(10);

delete from lst_field_category;
alter table lst_field_category
add lastUpdateUser varchar(6),
add lastUpdateDate date;

create table lst_shelter (
        id int(10) not null auto_increment,
        name varchar(32) default '',
        description varchar(80) default '',
        contact_name varchar(255),
        active int(1),
        orgid int(10),
        type varchar(20),
        street_1 varchar(255),
        street_2 varchar(255),
        city varchar(32),
        province varchar(32),
        postal_code varchar(7),
        telephone varchar(32),
        fax varchar(32),
        lastUpdateUser varchar(6),
        lastUpdateDate date,
        primary key(`id`)
);


delete from app_module;
alter table `app_module`
add isActive integer,
add displayOrder integer,
add lastUpdateUser varchar(6),
add lastUpdateDate date;

create table lst_aboriginal (
	code int(10) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_actions_content (
	code varchar(80) not null,
	description varchar(80)
);

delete from lst_admission_status;
alter table lst_admission_status 
add lastUpdateUser varchar(6),
add lastUpdateDate date;


create table lst_bed_type (
	bed_type_id int(10) not null,
	name varchar(45),
	isActive int(1) default 0,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(bed_type_id)
);


create table lst_casestatus (
        id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10),
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);

create table lst_complaint_method (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);

create table lst_complaint_outcome (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);

create table lst_complaint_section (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);

create table lst_complaint_source (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);


create table lst_complaint_subsection (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	sectionId int(10),
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);

create table lst_componentofservice (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);

create table lst_country (
	code varchar(3) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_cursleeparrangement (
	code int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);


drop table lst_discharge_reason;
create table lst_discharge_reason (
	code varchar(3) not null,
	description varchar(80),
	needSecondary int(1),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_documentcategory (
	code varchar(3) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);


create table lst_documenttype (
	code varchar(8) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	mime varchar(50),
	shortDesc varchar(10),
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_encounter_type (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);

create table lst_family_relationship (
	code varchar(2) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);


create table lst_fieldtype (
	code varchar(8) not null,
	description varchar(80),
	activeyn int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

delete from lst_gender;
alter table lst_gender 
add progDesc varchar(80),
add lastUpdateUser varchar(6),
add lastUpdateDate date;


create table lst_incident_clientissues (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);


create table lst_incident_disposition (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);

create table lst_incident_nature (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);


create table lst_incident_others (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);


create table lst_intake_reject_reason (
	code int(10) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_language (
	code varchar(3) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_lengthofhomeless (
	code int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_livedbefore (
	code int(10) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_message_type (
	code varchar(8) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_operator (
	code varchar(8) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

alter table lst_organization
add lastUpdateUser varchar(6),
add lastUpdateDate date;

delete from lst_program_type;
alter table lst_program_type
add lastUpdateUser varchar(6),
add lastUpdateDate date;

create table lst_province (
	code varchar(3) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_reasonforhomeless (
	code int(10) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_reasonforservice (
	code int(10) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_reasonnoadmit (
	code int(10) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_reason_notsign (
	code varchar(3) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_referredby (
	code int(10) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_referredto (
	code int(10) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_room_type (
	room_type_id int(10) not null,
	name varchar(45),
	dflt int(10) default 0,
	isActive int(1) default 0,
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(room_type_id)
);

delete from lst_sector;
alter table lst_sector
add lastUpdateUser varchar(6),
add lastUpdateDate date;

delete from lst_service_restriction;
alter table lst_service_restriction
add lastUpdateUser varchar(6),
add lastUpdateDate date;

create table lst_shelter_standards (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	sectionId int(10),
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);

create table lst_sourceincome (
	code int(10) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_statusincanada (
	code int(10) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

create table lst_title (
	id int(10) not null auto_increment,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(id)
);

create table lst_transportation_type (
	code int(10) not null,
	description varchar(80),
	isActive int(1),
	displayOrder int(10) default 1,
	lastUpdateUser varchar(6),
	lastUpdateDate date,
	primary key(code)
);

