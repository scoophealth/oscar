create table BornTransmissionLog(
	id integer not null auto_increment,
	submitDateTime timestamp not null,
	success tinyint(1) default 0,
	filename varchar(100) not null,
	primary key(id)
);
