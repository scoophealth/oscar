create table document_storage (
	id int(10)  NOT NULL auto_increment primary key,
	documentNo int(10),
	fileContents mediumblob,
	uploadDate Date
);

alter table demographicArchive add id bigint auto_increment primary key;

