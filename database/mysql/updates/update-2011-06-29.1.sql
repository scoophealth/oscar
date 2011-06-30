alter table HRMDocumentToProvider add column viewed int default 0;

CREATE TABLE `HRMDocumentComment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `providerNo` varchar(20) DEFAULT NULL,
  `hrmDocumentId` int(11) DEFAULT NULL,
  `comment` text,
  `commentTime` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `HRMProviderConfidentialityStatement` (
  `providerNo` varchar(20) NOT NULL DEFAULT '',
  `statement` text,
  PRIMARY KEY (`providerNo`)
);
