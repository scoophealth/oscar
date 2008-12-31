create table ClientLink
(
	id int primary key auto_increment,
	clientId int not null, index(clientId), foreign key (clientId) references demographic(demographic_no),
	linkType varchar(32) not null,
	remoteLinkId int not null,
	creationDate datetime not null,
	creatorProviderId varchar(6) not null,  foreign key (creatorProviderId) references provider(provider_no),
	active tinyint(1) not null, index(active)
);
