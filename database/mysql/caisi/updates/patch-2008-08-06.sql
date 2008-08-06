create table SystemMessage 
(
	id int primary key auto_increment,
	message text NOT NULL,
	creationDate datetime NOT NULL,
	expiryDate datetime NOT NULL
);

insert into SystemMessage select * from system_message;

drop table system_message;

