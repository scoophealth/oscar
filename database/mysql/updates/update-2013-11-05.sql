create table ORNPreImplementationReportLog (
  id int(10) NOT NULL auto_increment,
  providerNo varchar(10) not null,
  reportData text not null,
  lastUpdateDate datetime not null,
  primary key(id)
);
