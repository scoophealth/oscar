-- insert a top org

insert into lst_orgcd (code, description, activeyn, orderbyindex,codetree)

values('R0000001','Shelter Management Information System',1,10,'R000001');

insert into lst_orgcd (code, description, activeyn, orderbyindex,codetree)

values('O0000020','Salvation Army',1,30,'R0000001O0000020');

 

-- update privilege table,

-- the privilge implies a hierachy o-r-u-w-x, in alphabetic order

--truncate table secPrivilege;
delete from secPrivilege;

INSERT INTO secPrivilege (privilege, description)

  VALUES ('o', 'No rights.');

INSERT INTO secPrivilege (privilege, description)

  VALUES ('r', 'Read');

INSERT INTO secPrivilege (privilege, description)

  VALUES ('u', 'Update');

INSERT INTO secPrivilege (privilege, description)

  VALUES ('w', 'Write');

INSERT INTO secPrivilege (privilege, description)

  VALUES ('x', 'All Rights');

 

-- reserve current privilege settings

update secObjPrivilege set privilege='o' where privilege='|o|';

update secObjPrivilege set privilege='r' where privilege='|r|';

update secObjPrivilege set privilege='r' where privilege='|or|';

update secObjPrivilege set privilege='w' where privilege='|d|';

update secObjPrivilege set privilege='w' where privilege='|od|';

update secObjPrivilege set privilege='w' where privilege='|w|';

update secObjPrivilege set privilege='w' where privilege='|ow|';

update secObjPrivilege set privilege='x' where privilege='|*|';

 

-- initialize description for roles

update secRole set description=role_name;

 

-- update description of objects

UPDATE secObjectName set description = 'Administration'

  WHERE objectname= '_admin';

UPDATE secObjectName set description = 'Administration - Caisi'

  WHERE objectname= '_admin.caisi';

UPDATE secObjectName set description = 'Administration - Manage Caisi Roles'

  WHERE objectname= '_admin.caisiRoles';

UPDATE secObjectName set description = 'Administration - Setup Cookie Revolver'

  WHERE objectname= '_admin.cookieRevolver';

UPDATE secObjectName set description = 'Administration - Issue Editor'

  WHERE objectname= '_admin.issueEditor';

UPDATE secObjectName set description = 'Administration - Lookup Field Editor'

  WHERE objectname= '_admin.lookupFieldEditor';

UPDATE secObjectName set description = 'Administration - Provider'

  WHERE objectname= '_admin.provider';

UPDATE secObjectName set description = 'Administration - Security'

  WHERE objectname= '_admin.security';

UPDATE secObjectName set description = 'Administration - Security Log Report'

  WHERE objectname= '_admin.securityLogReport';

UPDATE secObjectName set description = 'Administration - Unlock Account'

  WHERE objectname= '_admin.unlockAccount';

UPDATE secObjectName set description = 'Administration - User Created Forms'

  WHERE objectname= '_admin.userCreatedForms';

UPDATE secObjectName set description = 'Client Demographic Info'

  WHERE objectname= '_demographic';

UPDATE secObjectName set description = 'eChart'

  WHERE objectname= '_eChart';

UPDATE secObjectName set description = 'Client Master Record'

  WHERE objectname= '_masterLink';

UPDATE secObjectName set description = 'Client - Merge Records'

  WHERE objectname= '_pmm.mergeRecords';

UPDATE secObjectName set description = 'Program - Access'

  WHERE objectname= '_pmm_editProgram.access';

UPDATE secObjectName set description = 'Program - Bed Check'

  WHERE objectname= '_pmm_editProgram.bedCheck';

UPDATE secObjectName set description = 'Program - Client Status'

  WHERE objectname= '_pmm_editProgram.clientStatus';

UPDATE secObjectName set description = 'Program - Clients'

  WHERE objectname= '_pmm_editProgram.clients';

UPDATE secObjectName set description = 'Program - User'

  WHERE objectname= '_pmm_editProgram.functionUser';

UPDATE secObjectName set description = 'Program - General'

  WHERE objectname= '_pmm_editProgram.general';

UPDATE secObjectName set description = 'Program - Queue'

  WHERE objectname= '_pmm_editProgram.queue';

UPDATE secObjectName set description = 'Program - Service Restrictions'

  WHERE objectname= '_pmm_editProgram.serviceRestrictions';

UPDATE secObjectName set description = 'Program - Staff'

  WHERE objectname= '_pmm_editProgram.staff';

UPDATE secObjectName set description = 'Program - Team'

  WHERE objectname= '_pmm_editProgram.teams';

UPDATE secObjectName set description = 'Report Runner'

  WHERE objectname= '_reportRunner';

UPDATE secObjectName set description = 'Report Writer'

  WHERE objectname= '_reportWriter';