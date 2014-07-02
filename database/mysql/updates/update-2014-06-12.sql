alter table drugs add dispenseInternal tinyint(1);
alter table favorites add dispenseInternal tinyint(1) not null;

create table DrugProduct(
	id int(9) NOT NULL auto_increment, 
	name varchar(255),
	code varchar(255),
	lotNumber varchar(255),
	dispensingEvent int(9),
	amount int not null,
	expiryDate date,
	location int,
	primary key (id)
);

create table DrugDispensing (
	id int(9) not null auto_increment,
	drugId int(9),
	dateCreated datetime,
	productId int(9),
	quantity int(9),
	unit varchar(20),
	dispensingProviderNo varchar(20),
	providerNo varchar(20),
	paidFor tinyint(1),
	notes text,
	programNo int,
	primary key(id)
);

insert into `secObjectName` (`objectName`) values('_rx.dispense');
insert into `secObjPrivilege` values('doctor','_rx.dispense','x',0,'999998');


create table DrugDispensingMapping (
        id int(9) not null auto_increment,
        din varchar(50),
	duration varchar(255),
	durUnit char(1),
	freqCode varchar(6),
	quantity varchar(20),
	takeMin float,
	takeMax float,
        productCode varchar(255),
        dateCreated datetime,
        primary key(id)
);




