create table Facility (
    id int primary key auto_increment,
    name varchar(32) NOT NULL, unique(name),
    description VARCHAR(150),
    contactName varchar(255),
    contactEmail varchar(255),
    contactPhone varchar(255),
    hic tinyint(1) NOT NULL,
    disabled tinyint(1) NOT NULL,
    orgId int NOT NULL,
    sectorId int NOT NULL,
	integratorEnabled tinyint(1) not null,
	integratorUrl varchar(255),
	integratorUser varchar(255),
	integratorPassword varchar(255),
	integratorLastPushTime datetime,
	useQuickConsent tinyint(1) not null
);

insert into Facility select * from facility;

drop table facility;
