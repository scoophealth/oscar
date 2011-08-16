drop table RemoteDataRetrievalLog;

create table RemoteDataLog
(
	id bigint primary key auto_increment,
	providerNo varchar(255) not null,
	actionDate datetime not null,
	action varchar(32) not null,
	documentId varchar(255) not null,
	documentContents blob not null
);
