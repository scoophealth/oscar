create table RemoteDataRetrievalLog
(
	id bigint primary key auto_increment,
	providerNo varchar(255) not null,
	retrievalDate datetime not null,
	remoteDocumentId varchar(255) not null,
	remoteDocumentContents blob not null
);

DROP TABLE if exists DemographicContact;
create table DemographicContact (
id integer not null auto_increment primary key,
created timestamp not null,
updateDate timestamp not null,
deleted tinyint,
demographicNo integer,
contactId varchar(100),
role varchar(100),
type integer,
sdm varchar(25),
ec varchar(25),
category varchar(100),
KEY (`demographicNo`)
);

DROP TABLE if exists Contact;
create table Contact (
id integer not null auto_increment primary key,
type varchar(20),
updateDate timestamp not null,
lastName varchar(100),
firstName varchar(100),
address varchar(255),
address2 varchar(255),
city varchar(100),
province varchar(25),
country varchar(25),
postal varchar(25),
residencePhone varchar(30),
cellPhone varchar(30),
workPhone varchar(30),
workPhoneExtension varchar(10),
email varchar(50),
fax varchar(30),
note text,
specialty varchar(255),
cpso varchar(10),
systemId varchar(30),
deleted tinyint(1),
KEY (`type`),
KEY (`cpso`),
KEY (`systemId`)
);
