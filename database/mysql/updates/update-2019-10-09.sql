CREATE TABLE `EFormDocs` (
  `id` int(10) NOT NULL auto_increment PRIMARY KEY,
  `fdid` int(10) NOT NULL,
  `document_no` int(10) NOT NULL,
  `doctype` char(1) NOT NULL,
  `deleted` char(1) DEFAULT NULL,
  `attach_date` date,
  `provider_no` varchar(6) NOT NULL
);

