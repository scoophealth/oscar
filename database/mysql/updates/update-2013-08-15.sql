CREATE TABLE  documentDescriptionTemplate (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  doctype varchar(60) NOT NULL,
  description varchar(255) NOT NULL,
  descriptionShortcut varchar(20) NOT NULL,
  provider_no varchar(6),
  lastUpdated timestamp NOT NULL,
  PRIMARY KEY (id)
);

insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('lab','Hematology','Hema',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('lab','Biochemistry','Bio',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('lab','ECG','ECG',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','Ultrasound','US',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','MRI','MRI',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','CT-SCAN','Scan',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','X-Ray','XRay',NULL);