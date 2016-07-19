CREATE TABLE `demographicSite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `siteId` int(11) NOT NULL,
  `demographicId` int(10) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE  `site_role_mpg` (
  `id` int not null auto_increment,
  `site_id` int(10) unsigned NOT NULL,
  `access_role_id` int(10) unsigned ,
  `crt_dt` timestamp NOT NULL ,
  `admit_discharge_role_id` int(10) unsigned,
  primary key (`id`)
);