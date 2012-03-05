alter table hl7TextInfo add 
(filler_order_num varchar(50), 
sending_facility varchar(50));

create table HL7HandlerMSHMapping (id int(50) NOT NULL AUTO_INCREMENT, hospital_site varchar(255), facility varchar(100), facility_name varchar(255), notes varchar(255), PRIMARY KEY (id));


INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health",".","Lakeridge Health Oshawa");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","MHB","Lakeridge Health Bowmanville");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","NDP","Lakeridge Health Port Perry");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","OE.LHC","Lakeridge Health ");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","RAD.OSG","Lakeridge Health");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","RVA","Rouge Valley Ajax and Pickering");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","RVC","Rouge Valley Centenary");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","RAD.APG","Rouge Valley");
	
INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","PRH","Peterborough Regional Health Centre");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","NHC","Northumberland Hills Hospital");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","CMH","Campbellford Memorial Hospital");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","RAD.PRH","Peterborough/Northumberland/Campbellford");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("The Scarborough Hospitals","GRA","The Scarborough Hospital - Birchmount Campus");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("The Scarborough Hospitals","SCS","The Scarborough Hospital - General Campus");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("The Scarborough Hospitals","RAD.SCS","The Scarborough Hospital");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Ontario Shores","WHA","Ontario Shores");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","APG","Rouge Valley Ajax and Pickering");
