create table LookupList (
	id int(11) NOT NULL AUTO_INCREMENT,
	name varchar(50) NOT NULL,
	description varchar(255),
	categoryId int,
	`active` tinyint(1) not null,
	createdBy varchar(8) not null,
	dateCreated timestamp not null,
	primary key(id)
);

create table LookupListItem (
	id int(11) NOT NULL AUTO_INCREMENT,
	lookupListId int(11) not null,
	value varchar(50) NOT NULL,
	label varchar(255),
	displayOrder int not null,
	`active` tinyint(1) not null,
	createdBy varchar(8) not null,
	dateCreated timestamp not null,
	primary key(id)
);


