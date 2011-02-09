 	DROP TABLE IF EXISTS `formConsult`;
 	CREATE TABLE `formConsult` (
 	  `ID` int(10) NOT NULL auto_increment,
 	  `provider_no` int(10) default NULL,
 	  `doc_name` varchar(60) default NULL,
 	  `cl_name` varchar(60) default NULL,
 	  `cl_address1` varchar(170) default NULL,
 	  `cl_address2` varchar(170) default NULL,
 	  `cl_phone` varchar(16) default NULL,
 	  `cl_fax` varchar(16) default NULL,
 	  `billingreferral_no` int(10) default NULL,
 	  `t_name` varchar(60) default NULL,
 	  `t_address1` varchar(170) default NULL,
 	  `t_address2` varchar(170) default NULL,
 	  `t_phone` varchar(16) default NULL,
 	  `t_fax` varchar(16) default NULL,
 	  `demographic_no` int(10) not null default 0,
 	  `p_name` varchar(60) default NULL,
 	  `p_address1` varchar(170) default NULL,
 	  `p_address2` varchar(170) default NULL,
 	  `p_phone` varchar(16) default NULL,
 	  `p_birthdate` varchar(30) default NULL,
 	  `p_healthcard` varchar(20) default NULL,
 	  `comments` text,
 	  `formCreated` date default NULL,
 	  `formEdited` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
 	  PRIMARY KEY  (`ID`)
 	) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
 	
 
 	
 	LOCK TABLES `encounterForm` WRITE;
 	insert into encounterForm values("Letterhead", "../form/formConsultant.jsp?demographic_no=", "formConsult", 0);
 	UNLOCK TABLES;
