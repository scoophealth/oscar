-- default agency
insert into agency (id, name, description, local, hic) values (0, 'Default Agency', 'Default Agency', 1, 0);

-- default roles
INSERT INTO `caisi_role` VALUES (1,'doctor','',now()),(2,'nurse','',now()),(3,'counsellor','',now()),(4,'csw','',now());
INSERT INTO `access_type` VALUES (1,'write doctor issues','access'),(2,'read doctor issues','access'),(3,'read doctor notes','access'),(4,'write nurse issues','access'),(5,'read nurse issues','access'),(6,'read nurse notes','access'),(7,'write counsellor issues','access'),(8,'read counsellor issues','access'),(9,'read counsellor notes','access'),(10,'write csw issues','access'),(11,'read csw issues','access'),(12,'read csw notes','access');

-- access types
INSERT INTO `access_type` (name,type) VALUES ('Write Ticklers','Action'),('prescription Write','access'),('billing','access'),('medical encounter','access'),('immunization','access'),('prevention','access'),('oscarcomm','access'),('disease registry','access'),('medical form','access'),('measurements','access'),('eform','access'),('lab','access'),('prescription Read','access'),('read ticklers','access'),('master file','access');

-- default role access (global) (doctor gets all)
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='Write Ticklers'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='prescription Write'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='billing'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='medical encounter'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='immunization'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='prevention'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='oscarcomm'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='disease registry'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='medical form'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='measurements'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='eform'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='lab'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='prescription Read'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='read ticklers'));
insert into default_role_access (role_id,access_id) values ((select role_id from caisi_role where name='doctor'),(select access_id from access_type where name='master file'));

-- issues
INSERT INTO issue (code, role,update_date,description) SELECT icd9.icd9,'doctor',now(),icd9.description from icd9;

INSERT INTO issue (code,description,role,update_date) values('CTCMM1000','Safety','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM2000','Health','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM3000','Personal ID','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM4000','Financial','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM5000','Legal','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM6000','Housing','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM7000','Education','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM8000','Employment','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM9000','Family','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM10000','Community Support','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM11000','Psycho-social concerns','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM12000','Client feedback / suggestions','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM13000','Physical Health','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM14000','Mental Health','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM15000','Addictions','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM16000','Social Support','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM17000','Respite','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM18000','Counselling Support','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM19000','Recovery/Healing','counsellor',Now());
INSERT INTO issue (code,description,role,update_date) values('CTCMM20000','Wellness Program','counsellor',Now());
insert into issue (code,description,role,update_date) values('CTCMM21000','Medication','counsellor',Now());
insert into issue (code,description,role,update_date) values('CTCMM22000','Hygiene/Clothing','counsellor',Now());
insert into issue (code,description,role,update_date) values('CTCMM23000','Increase Community Supports','counsellor',Now());
insert into issue (code,description,role,update_date) values('CTCMM24000','Discharge Planning','counsellor',Now());
insert into issue (code,description,role,update_date) values('CTCMM25000','Re- Admission Update','counsellor',Now());
insert into issue (code,description,role,update_date) values('CTCMM26000','Drug Card','counsellor',Now());
insert into issue (code,description,role,update_date) values('CTCMM27000','Suicidal Tendencies','counsellor',Now());
insert into issue (code,description,role,update_date) values('CTCMM28000','Incident Report','counsellor',Now());

INSERT INTO issue (code,description,role,update_date) values('ICSW100','program-client conflict','CSW',Now());
INSERT INTO issue (code,description,role,update_date) values('ICSW200','difficulties with hygeine','CSW',Now());
INSERT INTO issue (code,description,role,update_date) values('ICSW300','difficulties with eating','CSW',Now());
INSERT INTO issue (code,description,role,update_date) values('ICSW301','anorexia, loss of apetite','CSW',Now());

-- community programs
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10001,0,'Subsidized Housing','Subsidized Housing','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10002,0,'Private Market Housing','Private Market Housing','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10003,0,'Returned to Previous Address','Returned to Previous Address','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10004,0,'Returned To Partner','Returned To Partner','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10005,0,'Returned to Parents','Returned to Parents','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10006,0,'Moved in with Friends or Relatives','Moved in with Friends or Relatives','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10007,0,'Continued at Another Hospital or Treatment','Continued at Another Hospital or Treatment','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10008,0,'Left the City','Left the City','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10009,0,'Whereabouts Unknown (AWOL)','Whereabouts Unknown (AWOL)','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10010,0,'Community, unknown destination','Community, unknown destination','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10011,0,'Incarcerated','Incarcerated','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10012,0,'Deceased','Deceased','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic)  VALUES (10013,0,'Community Shelter','Community Shelter','','','','','','','community','',99999,0,0,0,0,0);
INSERT INTO `program` (program_id,agency_id,name,descr,address,phone,fax,url,email,emergency_number,type,location,max_allowed,num_of_members,holding_tank,allow_batch_admission,allow_batch_discharge,hic) VALUES (10014,0,'Other','Other','','','','','','','community','',99999,0,0,0,0,0);

-- DEFAULT BED TYPE
INSERT INTO `bed_type` VALUES (1, 'N/A', 1);

-- DEFAULT ROOM TYPES
INSERT INTO `room_type` VALUES (1, 'N/A', 1);

-- DEFAULT BED DEMOGRAPHIC STATUSES
INSERT INTO `bed_demographic_status` VALUES (1, 'N/A', 0, 1);

-- SYSTEM PROVIDER
insert into `provider` (`provider_no`, `last_name`, `first_name`, `provider_type`, `specialty`, `sex`, `status`) values (-1, 'system', 'system', 'system', 'system', 's', 1);

--
-- Populate intake node types
--
INSERT INTO `intake_node_type` VALUES
	(1, 'quick intake'),
	(2, 'in-depth intake'),
	(3, 'program intake'),
	(4, 'page'),
	(5, 'section'),
	(6, 'question'),
	(7, 'question single choice'),
	(8, 'question multiple choice'),
	(9, 'answer boolean'),
	(10, 'answer date'),
	(11, 'answer integer'),
	(12, 'answer string'),
	(13, 'answer telephone'),
	(14, 'answer email');

--
-- Populate intake nodes
--
INSERT INTO `intake_node` VALUES
	(1, 1, NULL, NULL),
	(2, 2, NULL, NULL);

--
-- Populate intake labels
--
INSERT INTO `intake_label` VALUES
	(1, 1, 0, 'Quick Intake'),
	(2, 2, 0, 'In-depth Intake');