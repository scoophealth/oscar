-- For Caisi RFQ
INSERT INTO `caisi_editor`(category,label,type,labelValue,labelCode,horizontal,isActive) VALUES ('intake','Gender','','Male','M','','Yes'),('intake','Gender','','Female','F','','Yes'),('intake','Gender','','Transgender','T','','Yes'),('Agency','Sector','','Men','1','','Yes'),('Agency','sector','','women','2','','Yes'),('Agency','Sector','','Families','3','','Yes'),('Agency','Sector','','Youth','4','','Yes'),('Agency','Organization','','City of Toronto','1','','Yes'),('Agency','Organization','','Salvation Army','2','','Yes'),('Agency','Organization','','Fred Victor','3','','Yes');

alter table facility add org_id int(10) NOT NULL default 0;
alter table facility add sector_id int(10) NOT NULL default 0;
