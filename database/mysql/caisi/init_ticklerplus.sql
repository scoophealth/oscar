create table if not exists tickler_update (
	id int(10) not null auto_increment,
	tickler_no int(10) not null,
	status char(1) not null,
	provider_no varchar(6) not null,
	update_date datetime not null,
	primary key(id)
) TYPE MyISAM;

create table if not exists tickler_comments (
	id int(10) not null auto_increment,
	tickler_no int(10) not null,
	message text,
	provider_no varchar(6) not null,
	update_date datetime not null,
	primary key(id)
) TYPE MyISAM;

create table if not exists custom_filter (
	id int(10) not null auto_increment,
	provider_no varchar(6) not null,
	name varchar(255) unique not null,
	start_date date not null,
	end_date date not null,
	status char(1) not null,
	priority varchar(20) not null,
	demographic_no varchar(20) not null,
        shortcut tinyint(1) default '0',
	primary key(id)
) TYPE MyISAM;

create table if not exists custom_filter_providers (
	filter_id int(10) not null,
	provider_no varchar(6) not null
) TYPE MyISAM;

create table if not exists custom_filter_assignees (
	filter_id int(10) not null,
	provider_no varchar(6) not null
) TYPE MyISAM;

