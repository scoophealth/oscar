-- insert a top org
insert into lst_orgcd (code, description, activeyn, orderbyindex,codetree)
valuse('R0000001','Shelter Management Information System',1,10,'R000001');
insert into lst_orgcd (code, description, activeyn, orderbyindex,codetree)
values('O0000020','Salvation Army',1,30,'R0000001O0000020');

-- update privilege table, 
-- the privilge implies a hierachy o-r-u-w-x, in alphabetic order
truncate table secprivilege;
INSERT INTO secprivilege t (id,privilige, description)
  VALUES (1, 'o', 'No rights.');
INSERT INTO secprivilege t (id,privilige, description)
  VALUES (2, 'r', 'Read');
INSERT INTO secprivilege t (id,privilige, description)
  VALUES (3, 'u', 'Update');
INSERT INTO secprivilege t (id,privilige, description)
  VALUES (4, 'w', 'Write');
INSERT INTO secprivilege t (id,privilige, description)
  VALUES (5, 'x', 'All Rights');

-- reserve current privilege settings
update secobjprivilege set privilige='o' where privilege='|o|';
update secobjprivilege set privilige='r' where privilege='|r|';
update secobjprivilege set privilige='r' where privilege='|or|';
update secobjprivilege set privilige='w' where privilege='|d|';
update secobjprivilege set privilige='w' where privilege='|od|';
update secobjprivilege set privilige='w' where privilege='|w|';
update secobjprivilege set privilige='w' where privilege='|ow|';
update secobjprivilege set privilige='x' where privilege='|*|';

-- initialize description fo roles
update secrole set description=role_name;

-- update description of objects
UPDATE secobjectname set description = 'Administration'
  WHERE objectname= '_admin';
UPDATE secobjectname set description = 'Administration - Casi'
  WHERE objectname= '_admin.caisi';
UPDATE secobjectname set description = 'Administration - Manage Roles'
  WHERE objectname= '_admin.caisiRoles';
UPDATE secobjectname set description = 'Administration - Setup Cookie Revolver'
  WHERE objectname= '_admin.cookieRevolver';
UPDATE secobjectname set description = 'Administration - Issue Editor'
  WHERE objectname= '_admin.issueEditor';
UPDATE secobjectname set description = 'Administration - Lookup Field Editor'
  WHERE objectname= '_admin.lookupFieldEditor';
UPDATE secobjectname set description = 'Administration - Provider'
  WHERE objectname= '_admin.provider';
UPDATE secobjectname set description = 'Administration - Security'
  WHERE objectname= '_admin.security';
UPDATE secobjectname set description = 'Administration - Security Log Report'
  WHERE objectname= '_admin.securityLogReport';
UPDATE secobjectname set description = 'Administration - Unlock Account'
  WHERE objectname= '_admin.unlockAccount';
UPDATE secobjectname set description = 'Administration - User Created Forms'
  WHERE objectname= '_admin.userCreatedForms';
UPDATE secobjectname set description = 'Client Demographic Info'
  WHERE objectname= '_demographic';
UPDATE secobjectname set description = 'eChart'
  WHERE objectname= '_eChart';
UPDATE secobjectname set description = 'Client Master Record'
  WHERE objectname= '_masterLink';
UPDATE secobjectname set description = 'Client - Merge Record'
  WHERE objectname= '_pmm.mergeRecords';
UPDATE secobjectname set description = 'Program - Access'
  WHERE objectname= '_pmm_editProgram.access';
UPDATE secobjectname set description = 'Program - Bed Check'
  WHERE objectname= '_pmm_editProgram.bedCheck';
UPDATE secobjectname set description = 'Program - Client Status'
  WHERE objectname= '_pmm_editProgram.clientStatus';
UPDATE secobjectname set description = 'Program - Clients'
  WHERE objectname= '_pmm_editProgram.clients';
UPDATE secobjectname set description = 'Program - User'
  WHERE objectname= '_pmm_editProgram.functionUser';
UPDATE secobjectname set description = 'Program - General'
  WHERE objectname= '_pmm_editProgram.general';
UPDATE secobjectname set description = 'Program - Queue'
  WHERE objectname= '_pmm_editProgram.queue';
UPDATE secobjectname set description = 'Program - Service Restriction'
  WHERE objectname= '_pmm_editProgram.serviceRestrictions';
UPDATE secobjectname set description = 'Program - Staff'
  WHERE objectname= '_pmm_editProgram.staff';
UPDATE secobjectname set description = 'Program - Team'
  WHERE objectname= '_pmm_editProgram.teams';
UPDATE secobjectname set description = 'Report Runer'
  WHERE objectname= '_reportRunner';
UPDATE secobjectname set description = 'Report Writer'
  WHERE objectname= '_reportWriter';
COMMIT;
