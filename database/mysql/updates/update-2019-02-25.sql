CREATE TABLE consultationRequestsArchive (
  Id int(10) NOT NULL auto_increment,
  referalDate date default NULL,
  serviceId int(10) default NULL,
  specId int(10) default NULL,
  appointmentDate date default NULL,
  appointmentTime time default NULL,
  reason text,
  clinicalInfo text,
  currentMeds text,
  allergies text,
  providerNo varchar(6) default NULL,
  demographicNo int(10) default NULL,
  status char(2) default NULL,
  statusText text,
  sendTo varchar(20) default NULL,
  requestId int(10) NOT NULL,
  concurrentProblems text,
  urgency char(2) default NULL,
  appointmentInstructions VARCHAR(256),
  patientWillBook tinyint(1),
  followUpDate date default NULL,
  site_name varchar(255),
  signature_img VARCHAR(20),
  letterheadName VARCHAR(20),
  letterheadAddress TEXT,
  letterheadPhone VARCHAR(50),
  letterheadFax VARCHAR(50),
  `lastUpdateDate` datetime not null,
  fdid int(10),
  source varchar(50),
  PRIMARY KEY  (id)
) ;


create table consultationRequestExtArchive(
 id int(10) NOT NULL auto_increment,
 originalId int(10) NOT NULL,
 requestId int(10) NOT NULL,
 name varchar(100) NOT NULL,
 value text NOT NULL,
 dateCreated date not null,
 consultationRequestArchiveId int(10) NOT NULL,
 primary key(id),
 key(requestId)
);

