drop table if exists `programSignature`;
create table programSignature (
	`id` int(10) NOT NULL auto_increment,
  	`programId` int(10) NOT NULL default '0',   	 
  	`programName` varchar(70) NOT NULL default '',
  	`providerId` varchar(6) NOT NULL default '0',
  	`providerName` varchar(60) NOT NULL default '',
  	`caisiRoleName` varchar(255) NOT NULL default '',
  	`updateDate` datetime default '0000-00-00 00:00:00',
  	PRIMARY KEY (`id`)
)ENGINE=MyISAM DEFAULT CHARSET=latin1;
  	