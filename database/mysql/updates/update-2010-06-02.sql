create table other_id (
	id int not null auto_increment primary key,
	table_name int not null,
	table_id int not null,
	other_key varchar(30) not null,
	other_id varchar(30) not null,
	deleted boolean not null
);

