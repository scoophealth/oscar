CREATE TABLE `RemoteIntegratedDataCopy` (
  id int(11) NOT NULL auto_increment,
  demographic_no int(10) ,
  datatype varchar(255) ,
  data longtext,
  lastUpdateDate datetime ,
  signature varchar(255) ,
  facilityId int(11) ,
  provider_no varchar(6) ,
  archived tinyint(1) ,
  PRIMARY KEY  (`id`),
  KEY RIDopy_demo_dataT_fac_arch (`demographic_no`,`datatype`,`facilityId`,`archived`), 
  KEY RIDopy_demo_dataT_sig_fac_arch (`demographic_no`,`datatype`,`signature`,`facilityId`,`archived`)
);