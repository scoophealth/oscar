alter table Facility drop enableCdsForms;

create table CdsHospitalisationDays
(
	id int primary key auto_increment,
	clientId int not null,
	admitted date not null,
	discharged date
);