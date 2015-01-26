CREATE TABLE `IntegratorProgress` (
 `id` int(11) NOT NULL auto_increment,
 `dateCreated` timestamp,
 `status` varchar(50),
 `errorMessage` varchar(255),
 PRIMARY KEY  (`id`),
 KEY `idx_status` (`status`)
);



CREATE TABLE `IntegratorProgressItem` (
 `id` int(11) NOT NULL auto_increment,
  `demographicNo` int not null,
  `integratorProgressId` int not null,
 `dateUpdated` timestamp,
 `status` varchar(50),
 PRIMARY KEY  (`id`),
 KEY `idx_id` (`integratorProgressId`),
 KEY `idx_status` (`status`)
);


