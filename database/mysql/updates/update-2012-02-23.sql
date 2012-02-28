create table RemoteReferral
(
	id int primary key auto_increment,
	facilityId int not null,
	index(facilityId),
	demographicId int not null,
	index(demographicId),
	referringProviderNo varchar(6) not null,
	referredToFacilityName varchar(255) not null,
	referredToProgramName varchar(255) not null,
	referalDate datetime not null,
	reasonForReferral varchar(255),
	presentingProblem varchar(255),
	createDate datetime not null
);