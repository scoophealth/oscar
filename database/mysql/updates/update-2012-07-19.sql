CREATE TABLE `PrintResourceLog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  resourceName varchar(100) NOT NULL,
  resourceId varchar(50) NOT NULL,
  dateTime timestamp not null,
  providerNo varchar(10),
  externalLocation varchar(200),
  externalMethod varchar(100),
  PRIMARY KEY (`id`)
);

