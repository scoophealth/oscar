update OcanFormOption set ocanDataCategoryValue="OTH" where ocanDataCategoryValue="OTHR";
update OcanFormOption set ocanDataCategoryValue="OTHR" where id=638;
update OcanFormOption set ocanDataCategoryValue="OPD" where id=171;
update OcanFormOption set ocanDataCategoryName="Other Physical Disabilities" where id=171;

INSERT INTO `OcanFormOption` VALUES (1212,'1.2','Diagnostic - Other Illness','OCIL','Other Chronic Illnesses');

INSERT INTO issue (code,description,role,update_date,type) values('CTCMM9500','Intimate Relationships','counsellor',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('CTCMM11500','Sexual Expression','counsellor',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('CTCMM14500','Client Capacity','counsellor',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('CTCMM21500','Self Care','counsellor',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('CTCMM22500','Looking After the Home','counsellor',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('CTCMM23500','Transportation','counsellor',Now(),'userDefined');


INSERT INTO issue (code,description,role,update_date,type) values('ICSW210','requires assistance with grooming/personal hygiene','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW310','requires assistance with eating','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW410','requires assistance of reading (vision problem)','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW420','requires assistance of hearing (hearing aids) ','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW430','requires assistance with seizures','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW440','language barriers','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW450','requires assistance with medical care advocacy','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW460','problems related to sleep pattern','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW400','Requires assistance with bathing','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW500','requires assistance with getting dressed','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW600','requires assistance with getting around floor e.g. dining room','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW700','requires assistance with cleaning of living space','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW800','requires assistance with laundry','CSW',Now(),'userDefined');
INSERT INTO issue (code,description,role,update_date,type) values('ICSW1000','Care provided in Managed Alcohol Program','CSW',Now(),'userDefined');


insert into `secRole` (role_name, description) values('property staff', 'property staff');
insert into `secRole` (role_name, description) values('Support Counsellor', 'Support Counsellor');

insert into `secObjPrivilege` values('property staff','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('property staff','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('property staff','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('property staff','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('property staff','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('property staff','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('property staff','_pmm.caisiRoles','x',0,'999998');
insert into `secObjPrivilege` values('property staff','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('property staff','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('property staff','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('property staff','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('property staff','_tasks','x',0,'999998');

insert into `secObjPrivilege` values('Support Counsellor','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Support Counsellor','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Support Counsellor','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Support Counsellor','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Support Counsellor','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Support Counsellor','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Support Counsellor','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Support Counsellor','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Support Counsellor','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Support Counsellor','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Support Counsellor','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Support Counsellor','_tasks','x',0,'999998');

insert into access_type (name, type) values("read ticklers assigned to a property staff","access");
insert into access_type (name, type) values("write property staff issues","access");
insert into access_type (name, type) values("read property staff issues","access");
insert into access_type (name, type) values("read property staff notes","access");

insert into access_type (name, type) values("read ticklers assigned to a Support Counsellor","access");
insert into access_type (name, type) values("write Support Counsellor issues","access");
insert into access_type (name, type) values("read Support Counsellor issues","access");
insert into access_type (name, type) values("read Support Counsellor notes","access");

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='property staff'),(select access_id from access_type where name='read ticklers assigned to a property staff'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='property staff'),(select access_id from access_type where name='read property staff issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='property staff'),(select access_id from access_type where name='write property staff issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='property staff'),(select access_id from access_type where name='read property staff notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='property staff'),(select access_id from access_type where name='Write Ticklers'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='property staff'),(select access_id from access_type where name='oscarcomm'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='property staff'),(select access_id from access_type where name='eform'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='property staff'),(select access_id from access_type where name='master file'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='property staff'),(select access_id from access_type where name='read ticklers'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name='property staff'),(select access_id from access_type where name='print bed rosters and reports'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read ticklers assigned to a Support Counsellor'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read Support Counsellor issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='write Support Counsellor issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read Support Counsellor notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read ticklers assigned to a Support Worker'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read Support Worker issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='write Support Worker issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read Support Worker notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read ticklers assigned to a Client Service Worker'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read Client Service Worker issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='write Client Service Worker issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read Client Service Worker notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read ticklers assigned to a secretary'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read secretary issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='write secretary issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read secretary notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='Write Ticklers'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='oscarcomm'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='measurements'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='eform'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='master file'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='read ticklers'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='Perform program registration intake'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='perform registration intake'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='perform admissions'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='perform discharges'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='perform bed assignments'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Counsellor'),(select access_id from access_type where name='print bed rosters and reports'));


insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='read Support Counsellor notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='read property staff notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='psychiatrist'),(select access_id from access_type where name='read Support Counsellor notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='psychiatrist'),(select access_id from access_type where name='read property staff notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RN'),(select access_id from access_type where name='read Support Counsellor notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RN'),(select access_id from access_type where name='read property staff notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RPN'),(select access_id from access_type where name='read Support Counsellor notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RPN'),(select access_id from access_type where name='read property staff notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Nurse Manager'),(select access_id from access_type where name='read Support Counsellor notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Nurse Manager'),(select access_id from access_type where name='read property staff notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Clinical Social Worker'),(select access_id from access_type where name='read Support Counsellor notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Clinical Social Worker'),(select access_id from access_type where name='read property staff notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Client Service Worker'),(select access_id from access_type where name='eform'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Support Worker'),(select access_id from access_type where name='eform'));





