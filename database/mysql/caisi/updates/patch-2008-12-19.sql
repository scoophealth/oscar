
insert into encounterForm values("Mental Health Form1","../form/formMentalHealthForm1.jsp?demographic_no=","formMentalHealthForm1",0);
insert into encounterForm values("Mental Health Form14","../form/formMentalHealthForm14.jsp?demographic_no=","formMentalHealthForm14",0);

create table formMentalHealthForm1(
	id int primary key auto_increment,
	demographic_no bigint(11) NOT NULL default 0,  
  	formCreated date default NULL,
  	formEdited timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,  
	physicianName varchar(60),
	physicianAddress varchar(255),
	telephoneNumber varchar(20),
	faxNumber varchar(20),
	onDate varchar(10),	
	clientName varchar(60) default NULL,
	clientAddress varchar(250) default NULL,	
	threatened tinyint(1) default 0,
	attempted tinyint(1) default 0,
	behaved tinyint(1) default 0,
	caused tinyint(1) default 0,
	shown tinyint(1) default 0,	
	observation text default NULL,
	facts text default NULL,
	harmHimself tinyint(1) default 0,
	harmOthers tinyint(1) default 0,
	impairment tinyint(1) default 0,
	observation2 text default NULL,
	facts2 text default NULL,	
	harmHimselfB tinyint(1) default 0,
	harmOthersB tinyint(1) default 0,
	deteriorationB tinyint(1) default 0,
	impairmentB tinyint(1) default 0,	
	harmHimselfB2 tinyint(1) default 0,
	harmOthersB2 tinyint(1) default 0,
	deteriorationB2 tinyint(1) default 0,
	impairmentB2 tinyint(1) default 0,	
	observationB text default NULL,
	factsB text default NULL,
	todayDate varchar(20) default NULL,
	todayTime varchar(8) default NULL,
	signature varchar(60) default NULL,
	datetimeOfDetention varchar(50) default NULL,
	signature1 varchar(60) default NULL,
	signature2 varchar(60) default NULL
);


create table formMentalHealthForm14 (
	id int primary key auto_increment,
	demographic_no bigint(11) NOT NULL default 0,  
  	formCreated date default NULL,
  	formEdited timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,  
	name varchar(60),
	address varchar(255),
	physicianName varchar(60),
	nameOfFacility varchar(100),
	clientName varchar(60),
	clientDOB varchar(10),
	witness varchar(60),
	signature varchar(60),
	relationship varchar(20),
	signatureDate varchar(20)
);


