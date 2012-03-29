CREATE TABLE `batch_billing` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(10),
  `billing_provider_no` int(10),
  `service_code` varchar(10),
  `dxcode` varchar(5),
  `billing_amount` varchar(10),
  `lastbilled_date` date,
  `create_date` timestamp,
  `creator` int(10),
  PRIMARY KEY (`id`)
);
