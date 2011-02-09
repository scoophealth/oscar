create table FunctionalCentre
(
	accountId varchar(64) primary key,
	description varchar(255) not null
);

insert into FunctionalCentre values ('7* 5 09 76','COM Case Management Mental Health');
insert into FunctionalCentre values ('7* 5 09 78 11','COM Case Management - Substance Abuse');
insert into FunctionalCentre values ('7* 5 09 78 12','COM Case Management - Problem Gambling');

alter table program add column functionalCentreId varchar(64) after description;

alter table Facility add `ocanServiceOrgNumber` int(10) not null default '0';
alter table Facility add enableOcanForms tinyint(1) not null;

create table OcanFormOption
(
        id int primary key auto_increment,
        ocanFormVersion varchar(16) not null,
        ocanDataCategory varchar(100) not null,
	ocanDataCategoryValue varchar(100) not null,
        ocanDataCategoryName varchar(255) not null
);

create table OcanStaffForm
(
        id int primary key auto_increment,
        ocanFormVersion varchar(16) not null,
        index(ocanFormVersion),
        providerNo varchar(6) not null,
        signed tinyint not null,
        index(signed),
        created datetime not null,
        facilityId int not null,
        clientId int not null,
        index(facilityId, clientId),
        admissionId int,
        index(admissionId),
        clientAge int,
        index(clientAge),
	lastName varchar(100),
	firstName varchar(100),
	addressLine1 varchar(100),
	addressLine2 varchar(100),
	city varchar(100),
	province varchar(10),
	postalCode varchar(100),
	phoneNumber varchar(100),
	email varchar(100),
	hcNumber varchar(100),
	hcVersion varchar(100),
	dateOfBirth varchar(100)
);

create table OcanStaffFormData
(
        id int primary key auto_increment,
        ocanStaffFormId int not null,
        index(ocanStaffFormId),
        question varchar(64) not null,
        index(question),
        answer varchar(16) not null
);
