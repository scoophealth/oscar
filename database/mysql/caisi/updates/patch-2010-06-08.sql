alter table secRole modify role_name varchar(60) NOT NULL default '';
delete from program_provider where role_id is null;
update secRole set role_name="counsellor" where role_name="Counsellor";
alter table OcanStaffForm modify startDate date;
INSERT INTO `OcanFormOption` VALUES (1203,'1.2','Number Of Centres','5','5'), (1204,'1.2','Number Of Centres','6','6'),(1205,'1.2','Number Of Centres','7','7'),(1206,'1.2','Number Of Centres','8','8'),(1207,'1.2','Number Of Centres','9','9'),(1208,'1.2','Number Of Centres','10','10');


insert into caisi_role (name,userDefined,oscar_name,update_date) values('Recreation Therapist', 1,'',now());

insert into access_type (name, type) values("read ticklers assigned to a Recreation Therapist","access");
insert into access_type (name, type) values("write Recreation Therapist issues","access");
insert into access_type (name, type) values("read Recreation Therapist issues","access");
insert into access_type (name, type) values("read Recreation Therapist notes","access");

insert into `secRole` (role_name, description) values('Recreation Therapist', 'Recreation Therapist');

insert into `secObjPrivilege` values('Recreation Therapist','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_tasks','x',0,'999998');

delete from default_role_access where role_id in (select role_no from secRole where role_name="Recreation Therapist");
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a doctor'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read doctor issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write doctor issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read doctor notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a psychiatrist'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read psychiatrist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write psychiatrist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read psychiatrist notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a RN'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read RN issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write RN issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read RN notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a RPN'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read RPN issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write RPN issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read RPN notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a Nurse Manager'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Nurse Manager issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write Nurse Manager issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Nurse Manager notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a Recreation Therapist'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Recreation Therapist notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a Clinical Case Manager'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Clinical Case Manager issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write Clinical Case Manager issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Clinical Case Manager notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a Clinical Case Manager'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Clinical Social Worker issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write Clinical Social Worker issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Clinical Social Worker notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a counsellor'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read counsellor issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write counsellor issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read counsellor notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a Case Manager'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Case Manager issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write Case Manager issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Case Manager notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a Housing Worker'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Housing Worker issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write Housing Worker issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Housing Worker notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a Medical Secretary'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Medical Secretary issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write Medical Secretary issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Medical Secretary notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a Clinical Assistant'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Clinical Assistant issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write Clinical Assistant issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read Clinical Assistant notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers assigned to a secretary'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read secretary issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='write secretary issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read secretary notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='Write Ticklers'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='immunization'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='prevention'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='oscarcomm'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='disease registry'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='medical form'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='measurements'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='eform'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='prescription Read'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='master file'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='read ticklers'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='Perform program registration intake'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='perform registration intake'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='perform admissions'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='perform discharges'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='perform bed assignments'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Recreation Therapist'),(select access_id from access_type where name='print bed rosters and reports'));




insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='read ticklers assigned to a Recreation Therapist'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='read Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='write Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='read Recreation Therapist notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='psychiatrist'),(select access_id from access_type where name='read ticklers assigned to a Recreation Therapist'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='psychiatrist'),(select access_id from access_type where name='read Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='psychiatrist'),(select access_id from access_type where name='write Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='psychiatrist'),(select access_id from access_type where name='read Recreation Therapist notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RN'),(select access_id from access_type where name='read ticklers assigned to a Recreation Therapist'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RN'),(select access_id from access_type where name='read Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RN'),(select access_id from access_type where name='write Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RN'),(select access_id from access_type where name='read Recreation Therapist notes'));


insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RPN'),(select access_id from access_type where name='read ticklers assigned to a Recreation Therapist'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RPN'),(select access_id from access_type where name='read Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RPN'),(select access_id from access_type where name='write Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='RPN'),(select access_id from access_type where name='read Recreation Therapist notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Clinical Social Worker'),(select access_id from access_type where name='read ticklers assigned to a Recreation Therapist'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Clinical Social Worker'),(select access_id from access_type where name='read Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Clinical Social Worker'),(select access_id from access_type where name='write Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Clinical Social Worker'),(select access_id from access_type where name='read Recreation Therapist notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Nurse Manager'),(select access_id from access_type where name='read ticklers assigned to a Recreation Therapist'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Nurse Manager'),(select access_id from access_type where name='read Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Nurse Manager'),(select access_id from access_type where name='write Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Nurse Manager'),(select access_id from access_type where name='read Recreation Therapist notes'));

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Clinical Case Manager'),(select access_id from access_type where name='read ticklers assigned to a Recreation Therapist'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Clinical Case Manager'),(select access_id from access_type where name='read Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Clinical Case Manager'),(select access_id from access_type where name='write Recreation Therapist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='Clinical Case Manager'),(select access_id from access_type where name='read Recreation Therapist notes'));


