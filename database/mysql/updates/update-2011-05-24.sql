CREATE TABLE drugReason (
  id int(10) NOT NULL auto_increment,
  drugId int(10) NOT NULL,
  codingSystem varchar(255),
  code varchar(255),
  comments text,
  primaryReasonFlag tinyint(1) NOT NULL,
  archivedFlag tinyint(1) NOT NULL,
  archivedReason text,
  demographicNo int(10) NOT NULL,
  providerNo varchar(6) NOT NULL,
  dateCoded date,
  PRIMARY KEY  (id),
  KEY (drugId),
  KEY (archivedFlag),
  KEY (codingSystem,code)
);
