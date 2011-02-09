create table CdsClientForm
(
	id int primary key auto_increment,
	cdsFormVersion varchar(16) not null,
	index(cdsFormVersion),
	providerNo varchar(6) not null,
	signed tinyint not null,
	index(signed),
	created datetime not null,
	facilityId int not null,
	clientId int not null,
	index(facilityId, clientId),
	admissionId int not null,
	index(admissionId),
	clientAge int,
	index(clientAge)
);

create table CdsClientFormData
(
	id int primary key auto_increment,
	cdsClientFormId int not null,
	index(cdsClientFormId),
	question varchar(64) not null,
	index(question),
	answer varchar(16) not null
);

