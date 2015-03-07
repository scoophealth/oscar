CREATE TABLE consultationResponse (
  responseId int(10) NOT NULL auto_increment,
  responseDate date,
  referralDate date,
  referringDocId int(10),
  appointmentDate date,
  appointmentTime time,
  appointmentNote text,
  referralReason text,
  examination text,
  impression text,
  plan text,
  clinicalInfo text,
  currentMeds text,
  allergies text,
  providerNo varchar(6),
  demographicNo int(10),
  status char(2),
  sendTo varchar(20),
  concurrentProblems text,
  urgency char(2),
  followUpDate date,
  signatureImg VARCHAR(20),
  letterheadName VARCHAR(20),
  letterheadAddress TEXT,
  letterheadPhone VARCHAR(50),
  letterheadFax VARCHAR(50),
  PRIMARY KEY  (responseId)
) ;

INSERT INTO consultationServices (serviceDesc, active) VALUES ('Referring Doctor', '02');

CREATE TABLE `consultResponseDoc` (
  `id` int(10) NOT NULL auto_increment PRIMARY KEY,
  `responseId` int(10) NOT NULL,
  `documentNo` int(10) NOT NULL,
  `docType` char(1) NOT NULL,
  `deleted` char(1),
  `attachDate` date,
  `providerNo` varchar(6) NOT NULL
);

