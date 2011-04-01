create table RemoteDataRetrievalLog
(
	id bigint primary key auto_increment,
	providerNo varchar(255) not null,
	retrievalDate datetime not null,
	remoteDocumentId varchar(255) not null,
	remoteDocumentContents blob not null
);
