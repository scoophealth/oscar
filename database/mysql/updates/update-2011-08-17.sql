update property set value=replace(value, '@myoscar.org', '') where name='MyOscarId';
update demographic set myOscarUserName=replace(myOscarUserName, '@myoscar.org', '');

alter table demographicArchive change pin myOscarUserName varchar(255);

create table Flowsheet (
  id int(10) auto_increment primary key,
  name varchar(25),
  content text,
  enabled tinyint(1),
  external tinyint(1),
  createdDate date
);

