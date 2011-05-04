update secObjectName set objectName='_pmm.editor' where objectName='_pmm.eidtor';
update secObjPrivilege set objectName='_pmm.editor' where objectName='_pmm.eidtor';

alter table OcanStaffFormData modify answer text not null;