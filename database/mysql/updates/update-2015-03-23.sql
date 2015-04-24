create table EFormReportTool (
  `id` int(11) NOT NULL auto_increment,
  `tableName` varchar(255) not null,
  `eformId` int not null,
  `expiryDate` datetime,
  `dateCreated` timestamp,
  `providerNo` varchar(6),
  `name` varchar(255),
  `dateLastPopulated` timestamp null,
  `latestMarked` tinyint(1) not null,
  PRIMARY KEY  (`id`)
);

