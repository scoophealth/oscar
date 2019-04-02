alter table document add receivedDate date default NULL;
alter table document add abnormal int(1) NOT NULL default '0';

CREATE TABLE DocumentExtraReviewer (
  `id` int(11) NOT NULL auto_increment,
  `documentNo` integer,
  `reviewerProviderNo` varchar(40),
  `reviewDateTime` timestamp,
  PRIMARY KEY  (`id`)
);


