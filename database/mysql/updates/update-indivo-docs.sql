#Keep track of documents sent to indivo server aka myOscar

CREATE TABLE indivoDocs (
id int(10) not null auto_increment primary key,
oscarDocNo int(10) not null,
indivoDocIdx varchar(255) not null,
docType varchar(20) not null,
dateSent date not null,
`update` char(1)
);
