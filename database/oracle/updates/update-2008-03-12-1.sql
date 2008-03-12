insert into lst_admission_status(code,description, isactive, displayorder)
values ('current','Current', 1, 10);
insert into lst_admission_status(code,description, isactive, displayorder)
values ('discharged','Discharged', 1, 20);

insert into lst_program_type values('01','BED',1,1);
insert into lst_program_type values('02','SERVICE',1,2);
insert into lst_program_type values('03','EXTERNAL',1,3);

truncate table app_lookuptable_fields;
truncate table app_lookuptable;

insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('PRM', '1', 'PROGRAM', 'Program', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('BTY', '4', 'BED_TYPE', 'Bed Type', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('ISS', '2', 'ISSUE', 'Issue', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('IGP', '2', 'ISSUEGROUP', 'Issue Group', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('LKT', '1', 'APP_LOOKUPTABLE', 'Available Fields in the system', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('FCT', '1', 'LST_FIELD_CATEGORY', 'Field Category', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('GEN', '4', 'LST_GENDER', 'Gender', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('SEC', '2', 'LST_SECTOR', 'Sector', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('OGN', '2', 'LST_ORGANIZATION', 'Organization', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('DRN', '5', 'LST_DISCHARGE_REASON', 'Discharge Reason', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('DR2', '5', 'LST_DISCHARGE_REASON_SECONDARY', 'Discharge Reason, Secondary', 0, 0, 0);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('SRT', '3', 'LST_SERVICE_RESTRICTION', 'Service Restriction', 0, 0, 1);

insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('FCT', 'ID', 'Id', '0', 'N', null, 'ID', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('FCT', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('FCT', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 3, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('BTY', 'BED_TYPE_ID', 'Id', '1', 'N', null, 'BED_TYPE_ID', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('BTY', 'NAME', 'Name', '1', 'S', null, 'NAME', 2, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('BTY', 'DFLT', 'Default Capacity', '1', 'N', null, 'DFLT', 3, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('LKT', 'TABLEID', 'Id', '0', 'S', null, 'TABLEID', 1, 1, 1, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('LKT', 'MODULEID', 'Category', '1', 'S', 'FCT', 'MODULEID', 2, 0, 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('LKT', 'TABLE_NAME', 'Table Name', '1', 'S', null, 'TABLE_NAME', 3, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('LKT', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 4, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('LKT', 'ISTREE', 'Is Tree Structured?', '1', 'B', null, 'ISTREE', 5, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('LKT', 'TREECODE_LENGTH', 'Tree Code Length (if is tree)', '1', 'N', null, 'TREECODE_LENGTH', 6, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('LKT', 'ACTIVEYN', 'Active?', '1', 'B', null, 'ACTIVEYN', 7, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('FCT', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 4, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('GEN', 'CODE', 'Code', '1', 'S', null, 'CODE', 1, 1, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('GEN', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('GEN', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 3, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('GEN', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 4, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('SEC', 'ID', 'Id', '1', 'N', null, 'ID', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('SEC', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('SEC', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 3, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('SEC', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 4, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('OGN', 'ID', 'Id', '0', 'N', null, 'ID', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('OGN', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('OGN', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 3, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('OGN', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 4, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('DRN', 'ID', 'Id', '1', 'N', null, 'ID', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('DRN', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('DRN', 'NEEDSECONDARY', 'Has Associated Secondary Reason?', '1', 'N', null, 'NEEDSECONDARY', 3, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('DRN', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 4, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('DRN', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 5, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('DR2', 'ID', 'Id', '1', 'N', null, 'ID', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('DR2', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('DR2', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 3, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('DR2', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 4, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('SRT', 'ID', 'Id', '1', 'N', null, 'ID', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('SRT', 'DESCRIPTION', 'Description', '1', 'S', null, 'DESCRIPTION', 2, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('SRT', 'ISACTIVE', 'Active?', '1', 'B', null, 'ISACTIVE', 3, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericidx, autoyn)
values ('SRT', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'DISPLAYORDER', 4, 0, 0, 0);
