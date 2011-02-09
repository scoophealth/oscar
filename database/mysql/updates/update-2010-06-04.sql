create table queue (id int(10) not null auto_increment,name varchar(40) not null ,primary key(id),unique (name) );
create table queue_provider_link (id int(10) not null auto_increment,queue_id int(10),provider_id varchar(6), primary key(id) );
create table queue_document_link (id int(10) not null auto_increment,queue_id int(10) not null, document_id int(10) not null,primary key (id));