drop table if exists system_message;
create table system_message (
	id int(10) not null auto_increment,
	message text not null,
	creation_date datetime not null,
	expiry_date datetime not null,
	primary key(id)
) TYPE MyISAM;

drop table if exists provider_caisi_role;
create table provider_caisi_role (
	provider_no varchar(20) not null,
	role_id int(10) not null,
	update_date datetime not null,
	primary key(provider_no)
) TYPE MyISAM;

drop table if exists caisi_role;
create table caisi_role (
	role_id int(10) not null auto_increment,
	name varchar(255) not null,
	oscar_name varchar(255) not null,
	update_date datetime not null,
	primary key(role_id)
) TYPE MyISAM;

drop table if exists provider_default_program;
create table provider_default_program (
	id int(10) not null auto_increment,
	provider_no varchar(6) not null,
	program_id int(10) not null,
	`signnote` tinyint(1) default '0',
	primary key(id)
) TYPE MyISAM;

insert into caisi_role (name,oscar_name,update_date) values('doctor','',now());
insert into caisi_role (name,oscar_name,update_date) values('nurse','',now());
insert into caisi_role (name,oscar_name,update_date) values('counsellor','',now());
insert into caisi_role (name,oscar_name,update_date) values('CSW','',now());
