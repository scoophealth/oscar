
insert into encounterForm values("Mental Health Form42","../form/formMentalHealthForm42.jsp?demographic_no=","formMentalHealthForm42",0);

create table formMentalHealthForm42(
	id int primary key auto_increment,
	demographic_no bigint(11) NOT NULL default 0,  
  	formCreated date default NULL,
  	formEdited timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,  
  	
  	
  	name varchar(60) default null,
	homeAddress varchar(255) default null,
	physician varchar(60) default null,
	dateOfExamination varchar(10) default null,
	
	chkThreatenedA tinyint(1) default 0,
	chkBehavedA tinyint(1) default 0,
	chkCompetenceA tinyint(1) default 0,
	chkHarmYourselfA tinyint(1) default 0,
	chkHarmAnotherA tinyint(1) default 0,
	chkImpairmentA tinyint(1) default 0,
	
	chkHarmYourselfB tinyint(1) default 0,
	chkHarmAnotherB tinyint(1) default 0,
	chkDeteriorationB tinyint(1) default 0,
	chkImpairmentB tinyint(1) default 0,
	
	chkHarmYourselfB2 tinyint(1) default 0,
	chkHarmAnotherB2 tinyint(1) default 0,
	chkDeteriorationB2 tinyint(1) default 0,
	chkImpairmentB2 tinyint(1) default 0,
	
	dateOfSign varchar(10) default 0,
	signPhysician varchar(60) default null,
	
	name2 varchar(60) default null,
	homeAddress2 varchar(255) default null,
	nameOfMinisterHealth varchar(255) default null,
	
	chkHarmYourself2 tinyint(1) default 0,
	chkHarmAnother2 tinyint(1) default 0,
	dateOfOrder varchar(10) default null,
	dateOfSign2 varchar(10) default null,
	signPhysician2 varchar(60) default null

);

