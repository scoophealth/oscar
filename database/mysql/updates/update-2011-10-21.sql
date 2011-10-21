create table PHRVerification(
	id int(10) auto_increment primary key,
	demographicNo int(10),
	phrUserName varchar(255),
	verificationLevel varchar(100),
	verificationDate datetime,
	verificationBy varchar(6),
	photoId tinyint(1),
	parentGuardian tinyint(1),
	comments text,
	createdDate datetime,
	archived tinyint(1),
	KEY `PHRVerification_archived` (`archived`)
);