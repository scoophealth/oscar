create table IntegratorControl (
	id int primary key auto_increment,
	facilityId int not null, foreign key (facilityId) references Facility(id),
	control varchar(80),
	execute boolean);

