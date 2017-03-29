
create table functionalCentreAdmission
(
        id int(11) NOT NULL auto_increment,
        demographicNo int(11) NOT NULL,
        functionalCentreId varchar(64) NOT NULL,
        referralDate date,
        admissionDate date,
        serviceInitiationDate date,
        dischargeDate date,
        discharged tinyint(1) NOT NULL,
        providerNo varchar(6) NOT NULL,
        updateDate datetime NOT NULL,
	dischargeReason varchar(200),
        PRIMARY KEY  (id)
);

alter table FunctionalCentre add enableCbiForm tinyint(1);
update FunctionalCentre set enableCbiForm=1;

alter table CdsClientForm add serviceInitiationDate date;

alter table admission add newAdmissionId int(11);
alter table admission add fc varchar(20);

insert into `secObjectName` (`objectName`) values('_pmm.functionalCentre');
insert into `secObjPrivilege` values('admin','_pmm.functionalCentre','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.functionalCentre','x',0,999998);

-- convert admission ID to functional centre ID
update admission set fc="";
update admission set fc = (select p.functionalCentreId from program p where p.id=admission.program_id) where fc="";

update admission set newAdmissionId=0;
update admission set newAdmissionId=(select id from functionalCentreAdmission where demographicNo=admission.client_id and functionalCentreId=admission.fc limit 1) where newAdmissionId=0;
update admission set newAdmissionId=0 where newAdmissionId is null;

insert into functionalCentreAdmission select distinct null , o.clientId, p.functionalCentreId, o.initialContactDate, o.assessmentDate, o.serviceInitiationDate, NULL, 0, o.providerNo, o.created, NULL from CdsClientForm o, admission a , program p where o.admissionId=a.am_id and a.program_id=p.id and p.functionalCentreId is not null and p.functionalCentreId!="" and a.newAdmissionId=0 and a.fc is not null group by clientId, functionalCentreId;

update admission set newAdmissionId=(select id from functionalCentreAdmission where demographicNo=admission.client_id and functionalCentreId=admission.fc ) where newAdmissionId=0;

update admission set newAdmissionId=0 where newAdmissionId is null;

-- cds form's admission id data conversion
alter table CdsClientForm add new_fca_id int;

update CdsClientForm set new_fca_id=0;

update CdsClientForm set new_fca_id=(select newAdmissionId from admission where am_id=CdsClientForm.admissionId and client_id=CdsClientForm.clientId limit 1) where new_fca_id=0;

update CdsClientForm set admissionId=new_fca_id ;

update functionalCentreAdmission set admissionDate=(select admission_date from admission where newAdmissionId=functionalCentreAdmission.id and client_id=functionalCentreAdmission.demographicNo limit 1) where admissionDate is null;

alter table CdsClientForm drop column new_fca_id;
alter table admission drop column fc;
alter table admission drop column newAdmissionId;

-- cbi form : add missing or mis-mapped functional centre id
-- Just for data merging
create table test (
OcanStaffForm_id int,
clientId_cbiForm int,
admissionId_cbiForm int,
admissionDate_cbiForm date,
clientId_in_functionalCentreAdmisison int,
fcCount int,
fc_functionalCentreAdmisison varchar(50),
fc_admissionDate date,
newAdmissionId int
);

insert into test select oo.id, oo.clientId,oo.admissionId,oo.admissionDate, oo.demographicNo,0, null, null, 0 from (select o.id, o.clientId, o.admissionId, o.admissionDate, f.demographicNo from OcanStaffForm o join functionalCentreAdmission f on o.admissionId=f.id where o.ocanType='CBI') as oo where oo.clientId<>oo.demographicNo;

update test set fcCount=(select count(*) from functionalCentreAdmission where demographicNo=test.clientId_cbiForm) ;

update test set fc_functionalCentreAdmisison=(select functionalCentreId from functionalCentreAdmission where demographicNo=test.clientId_cbiForm) where fcCount=1;

update test set fc_admissionDate=(select admissionDate from functionalCentreAdmission where demographicNo=test.clientId_cbiForm) where fcCount=1;

update test set newAdmissionId=(select id from functionalCentreAdmission where demographicNo=test.clientId_cbiForm) where fcCount=1;

alter table OcanStaffForm add newAdmissionId int;
update OcanStaffForm set newAdmissionId=(select newAdmissionId from test where OcanStaffForm_id=OcanStaffForm.id and clientId_cbiForm=OcanStaffForm.clientId and fcCount=1) where ocanType='CBI';
update OcanStaffForm set admissionId=newAdmissionId where newAdmissionId is not null;

alter table test add assessmentId int;
update test set assessmentId=(select assessmentId from OcanStaffForm where id=OcanStaffForm_id);

update test set fc_functionalCentreAdmisison=(select functionalCentreId from functionalCentreAdmission where demographicNo=test.clientId_cbiForm and admissionDate=test.admissionDate_cbiForm limit 1) where fcCount>1;

update test set newAdmissionId=(select id from functionalCentreAdmission where demographicNo=test.clientId_cbiForm and admissionDate=test.admissionDate_cbiForm limit 1) where fcCount>1;

update OcanStaffForm set newAdmissionId=(select newAdmissionId from test where OcanStaffForm_id=OcanStaffForm.id and clientId_cbiForm=OcanStaffForm.clientId and fcCount>1) where ocanType='CBI' and newAdmissionId is null;

update OcanStaffForm set admissionId=newAdmissionId where newAdmissionId is not null;

drop table test;
alter table OcanStaffForm drop column newAdmissionId;


