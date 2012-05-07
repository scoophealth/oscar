alter table labPatientPhysicianInfo modify collection_date varchar(20);
alter table document add sourceFacility varchar(120);

update ctl_doc_class set reportclass="Diagnostic Test Report" where reportclass="Diagnostic Test Reports";
update ctl_doc_class set reportclass="Other Letter" where reportclass="Other Letters";
update ctl_doc_class set reportclass="Consultant ReportA" where reportclass="ConsultA";
update ctl_doc_class set reportclass="Consultant ReportB" where reportclass="ConsultB";
insert into ctl_doc_class (reportclass,subclass) values ("Medical Record Report","");
insert into ctl_doc_class (reportclass,subclass) values ("Lab Report","");

update document set docClass="Diagnostic Test Report" where docClass="Diagnostic Test Reports";
update document set docClass="Other Letter" where docClass="Other Letters";
update document set docClass="Consultant Report" where docClass="Consult";

