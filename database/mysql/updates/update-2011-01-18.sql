create table consultationRequestExt(
 id int(10) NOT NULL auto_increment, 
 requestId int(10) NOT NULL, 
 name varchar(100) NOT NULL,
 value varchar(100) NOT NULL,
 dateCreated date not null,
 primary key(id),
 key(requestId)
);
