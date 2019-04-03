CREATE TABLE AppointmentSearch (
			id int(10)  NOT NULL auto_increment primary key,
			providerNo varchar(6),
			searchType varchar(100),
			searchName varchar(100),
			fileContents mediumblob,
			updateDate datetime,
			createDate datetime,
			active boolean,
			uuid char(40),
			KEY(providerNo),
			KEY(uuid)
);