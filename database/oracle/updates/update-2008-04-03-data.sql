-- insert a top org
insert into lst_orgcd (code, description, activeyn, orderbyindex,codetree)
valuse('R0000001','Shelter Management Information System',1,10,'R000001');
insert into lst_orgcd (code, description, activeyn, orderbyindex,codetree)
values('O0000020','Salvation Army',1,30,'R0000001O0000020');
-- init secuserrole
update secuserrole set ORGCD='R0000001', activeyn=1;
-- missed object names
insert into secobjectname (objectname,orgapplicable)
select distinct objectname,0 from secobjprivilege where not objectname in (select objectname from secobjectname)
-- update privilege table, 
-- the privilge implies a hierachy o-r-u-w-x, in alphabetic order
truncate table secprivilege;
INSERT INTO secprivilege t (id,privilege, description)
  VALUES (1, 'o', 'No rights.');
INSERT INTO secprivilege t (id,privilege, description)
  VALUES (2, 'r', 'Read');
INSERT INTO secprivilege t (id,privilege, description)
  VALUES (3, 'u', 'Update');
INSERT INTO secprivilege t (id,privilege, description)
  VALUES (4, 'w', 'Write');
INSERT INTO secprivilege t (id,privilege, description)
  VALUES (5, 'x', 'All Rights');

-- reserve current privilege settings
update secobjprivilege set privilege='o' where privilege='|o|';
update secobjprivilege set privilege='r' where privilege='|r|';
update secobjprivilege set privilege='r' where privilege='|or|';
update secobjprivilege set privilege='w' where privilege='|d|';
update secobjprivilege set privilege='w' where privilege='|od|';
update secobjprivilege set privilege='w' where privilege='|w|';
update secobjprivilege set privilege='w' where privilege='|ow|';
update secobjprivilege set privilege='x' where privilege='|*|';

-- initialize description fo roles
update secrole set description=role_name;

-- update description of objects
UPDATE secobjectname set description = 'Administration', orgapplicable=0
  WHERE objectname= '_admin';
UPDATE secobjectname set description = 'Administration - Caisi', orgapplicable=0
  WHERE objectname= '_admin.caisi';
UPDATE secobjectname set description = 'Administration - Manage Caisi Roles', orgapplicable=0
  WHERE objectname= '_admin.caisiRoles';
UPDATE secobjectname set description = 'Administration - Setup Cookie Revolver', orgapplicable=0
  WHERE objectname= '_admin.cookieRevolver';
UPDATE secobjectname set description = 'Administration - Issue Editor', orgapplicable=0
  WHERE objectname= '_admin.issueEditor';
UPDATE secobjectname set description = 'Administration - Lookup Field Editor', orgapplicable=0
  WHERE objectname= '_admin.lookupFieldEditor';
UPDATE secobjectname set description = 'Administration - Provider', orgapplicable=1
  WHERE objectname= '_admin.provider';
UPDATE secobjectname set description = 'Administration - Security', orgapplicable=0
  WHERE objectname= '_admin.security';
UPDATE secobjectname set description = 'Administration - Security Log Report', orgapplicable=0
  WHERE objectname= '_admin.securityLogReport';
UPDATE secobjectname set description = 'Administration - Unlock Account', orgapplicable=0
  WHERE objectname= '_admin.unlockAccount';
UPDATE secobjectname set description = 'Administration - User Created Forms', orgapplicable=0
  WHERE objectname= '_admin.userCreatedForms';
UPDATE secobjectname set description = 'Client Demographic Info', orgapplicable=1
  WHERE objectname= '_demographic';
UPDATE secobjectname set description = 'eChart', orgapplicable=0
  WHERE objectname= '_eChart';
UPDATE secobjectname set description = 'Client Master Record', orgapplicable=1
  WHERE objectname= '_masterLink';
UPDATE secobjectname set description = 'Client - Merge Records', orgapplicable=1
  WHERE objectname= '_pmm.mergeRecords';
UPDATE secobjectname set description = 'Program - Access', orgapplicable=1
  WHERE objectname= '_pmm_editProgram.access';
UPDATE secobjectname set description = 'Program - Bed Check', orgapplicable=1
  WHERE objectname= '_pmm_editProgram.bedCheck';
UPDATE secobjectname set description = 'Program - Client Status', orgapplicable=1
  WHERE objectname= '_pmm_editProgram.clientStatus';
UPDATE secobjectname set description = 'Program - Clients', orgapplicable=1
  WHERE objectname= '_pmm_editProgram.clients';
UPDATE secobjectname set description = 'Program - User', orgapplicable=1
  WHERE objectname= '_pmm_editProgram.functionUser';
UPDATE secobjectname set description = 'Program - General', orgapplicable=1
  WHERE objectname= '_pmm_editProgram.general';
UPDATE secobjectname set description = 'Program - Queue', orgapplicable=1
  WHERE objectname= '_pmm_editProgram.queue';
UPDATE secobjectname set description = 'Program - Service Restrictions', orgapplicable=1
  WHERE objectname= '_pmm_editProgram.serviceRestrictions';
UPDATE secobjectname set description = 'Program - Staff', orgapplicable=1
  WHERE objectname= '_pmm_editProgram.staff';
UPDATE secobjectname set description = 'Program - Team', orgapplicable=1
  WHERE objectname= '_pmm_editProgram.teams';
UPDATE secobjectname set description = 'Report Runner', orgapplicable=0
  WHERE objectname= '_reportRunner';
UPDATE secobjectname set description = 'Report Writer', orgapplicable=0
  WHERE objectname= '_reportWriter';
COMMIT;
