alter table intake add intake_status varchar(10) default null;
alter table intake add intake_location int(10) default 0;


-- added the following lines for QUATRO group's report runner.

create table lst_gender
(
	code char(1) NOT NULL,
 	description varchar(80),
 	isactive tinyint(1),
 	displayorder int(10),
 	PRIMARY KEY (code)
);
insert into lst_gender (code,description,isactive,displayorder) values ('M','Male',1,2);
insert into lst_gender (code,description,isactive,displayorder) values ('F','Female',1,1);
insert into lst_gender (code,description,isactive,displayorder) values ('T','Transgender',1,3);

create table lst_sector
(
	id int(10) NOT NULL auto_increment,
 	description varchar(80),
 	isactive tinyint(1),
 	displayorder int(10),
 	PRIMARY KEY (id)
);
insert into lst_sector (id,description,isactive,displayorder) values (1,'Men',1,1);
insert into lst_sector (id,description,isactive,displayorder) values (2,'Women',1,2);
insert into lst_sector (id,description,isactive,displayorder) values (3,'Families',1,3);
insert into lst_sector (id,description,isactive,displayorder) values (4,'Youth',1,4);

create table lst_organization
(
	id int(10) NOT NULL auto_increment,
 	description varchar(80),
 	isactive tinyint(1),
 	displayorder int(10),
 	PRIMARY KEY (id)
);

insert into lst_organization (id,description,isactive,displayorder) values (1,'City of Toronto',1,1);
insert into lst_organization (id,description,isactive,displayorder) values (2,'Salvation Army',1,2);
insert into lst_organization (id,description,isactive,displayorder) values (3,'Fred Victor',1,3);


create table lst_discharge_reason
(
  id   int(10) not null auto_increment,
  description 	varchar(80),
  needsecondary tinyint(1),  
  isactive    	tinyint(1),
  displayorder 	int(10),
  primary key (id)
);
insert into lst_discharge_reason (id,description,needsecondary,isactive,displayorder) 
values (1,'Service restriction',0,1,10);
insert into lst_discharge_reason (id,description,needsecondary,isactive,displayorder) 
values (2,'Client Self Discharge',0,1,10);
insert into lst_discharge_reason (id,description,needsecondary,isactive,displayorder) 
values (3,'Other Program more appropriate',0,1,10);


create table lst_field_category
(
 id int(10) NOT NULL auto_increment,
 description varchar(80),
 isactive tinyint(1),
 displayorder int(10),
 PRIMARY KEY (id)
);
insert into lst_field_category (id, description,isactive,displayorder)
values(1,'Agency',	1,	10);
insert into lst_field_category (id, description,isactive,displayorder)
values(2,'Client',  1, 20);
insert into lst_field_category (id, description,isactive,displayorder)
values(3,'Intake',	1,	30);
insert into lst_field_category (id, description,isactive,displayorder)
values(4,'Program', 1, 40);

create table lst_service_restriction
(
 id int(10) NOT NULL auto_increment,
 description varchar(80),
 isactive tinyint(1),
 displayorder int(10),
 PRIMARY KEY (id)
);
insert into lst_service_restriction(id,description,isactive,displayorder)
values (1,'Assault of client',1,10);    
insert into lst_service_restriction(id,description,isactive,displayorder)
values (2,'Assault of staff',1,20);    
insert into lst_service_restriction(id,description,isactive,displayorder)
values (3,'Other',1,30);    



