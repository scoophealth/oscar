insert into issue (code,description,role,update_date,priority,type,sortOrderId) values ('Misc','Misc','nurse',now(),NULL,'system',0);

CREATE TABLE `cssStyles` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255),
  `style` text,
  `status` char(1) NOT NULL,
  PRIMARY KEY (`id`)
);

alter table billingservice add column `displaystyle` int(10);
