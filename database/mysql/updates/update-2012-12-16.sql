create table Institution (
	id int PRIMARY KEY NOT NULL auto_increment,
	name varchar(255) not null,
	address varchar(255),
	city varchar(100),
	province varchar(100),
	postal varchar(10),
	country varchar(25),
	phone varchar(25),
	fax varchar(25),
	website varchar(100),
	email varchar(50),
	annotation text
);

create table Department (
        id int PRIMARY KEY NOT NULL auto_increment,
        name varchar(255) not null,
	annotation text
);


create table InstitutionDepartment (
	institutionId int not null,
	departmentId int not null
);
