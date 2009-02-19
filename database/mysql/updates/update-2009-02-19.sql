alter table `document` add column `source` varchar(60) after `doccreator`;
alter table `drugs` add column `outside_provider` varchar(100);

