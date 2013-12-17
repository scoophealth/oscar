create table PreventionsLotNrs(
  id int(10) NOT NULL AUTO_INCREMENT, 
  creationDate datetime,
  providerNo varchar(6) NOT NULL,
  preventionType varchar(20) NOT NULL,
  lotNr text NOT NULL,
  deleted boolean NOT NULL, 
  lastUpdateDate datetime NOT NULL,
  PRIMARY KEY (`id`)
);
