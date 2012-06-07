CREATE TABLE `PageMonitor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  pageName varchar(100) NOT NULL,
  session varchar(100),
  remoteAddr varchar(20),
  locked tinyint(1),
  updateDate timestamp not null,
  timeout int(10),
  providerNo varchar(10),
  providerName varchar(100),
  PRIMARY KEY (`id`)
);

