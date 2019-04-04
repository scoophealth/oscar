alter table HRMDocument add formattedName varchar(100);
alter table HRMDocument add dob varchar(10);
alter table HRMDocument add gender varchar(1);
alter table HRMDocument add hcn varchar(20);
alter table HRMDocument add recipientId varchar(15);
alter table HRMDocument add recipientName varchar(255);
alter table HRMDocument add recipientProviderNo varchar(25);
alter table HRMDocument add className varchar(255);
alter table HRMDocument add subClassName varchar(255);
alter table HRMDocument add sourceFacilityReportNo varchar(100);

create table HrmLog (
  id int(11) auto_increment,
  started timestamp not null,
  initiatingProviderNo varchar(25),
  transactionType varchar(25),
  externalSystem varchar(50),
  error varchar(255),
  connected tinyint(1), 
  downloadedFiles tinyint(1), 
  numFilesDownloaded int,
  deleted tinyint(1), 
  PRIMARY KEY(id)
);

create table HrmLogEntry (
  id int(11) auto_increment,
  hrmLogId int(11),
  encryptedFileName varchar(255),
  decrypted tinyint(1), 
  decryptedFileName varchar(255),
  filename varchar(255),
  error varchar(255),
  parsed tinyint(1),
  recipientId varchar(100),
  recipientName varchar(255),
  distributed tinyint(1),
  PRIMARY KEY(id)
);

insert into secObjectName values ('_admin.hrm',NULL,0);
insert into secObjectName values ('_hrm.administrator',NULL,0);
insert into secObjPrivilege values('admin','_admin.hrm','x',0,'999998');
insert into secRole values(\N,'HRMAdmin','HRM Administator');
insert into secObjPrivilege values('HRMAdmin','_hrm.administrator','x',0,'999998');

update HRMCategory set categoryName = 'Oscar HRM Category Uncategorized' where categoryName = 'General Oscar Lab';
