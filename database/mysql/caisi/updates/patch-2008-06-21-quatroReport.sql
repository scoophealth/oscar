-- not checked in yet, need to confirm it later.....
-- it also needs to change initcaisidata.sql later

truncate table app_lookuptable;
truncate table app_lookuptable_fields;

insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('ORG', '1', 'lst_orgcd', 'Org Chart', 1, 8, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('BTY', '4', 'bed_type', 'Bed Type', 0, 0, 0);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('GEN', '4', 'lst_gender', 'Gender', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('SEC', '2', 'lst_sector', 'Sector', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('OGN', '2', 'lst_organization', 'Organization', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('DRN', '5', 'lst_discharge_reason', 'Discharge Reason', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('SRT', '3', 'lst_service_restriction', 'Service Restriction', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('LKT', '1', 'app_lookuptable', 'Available Fields', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('FCT', '1', 'lst_field_category', 'Field Category', 0, 0, 1);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('PRO', '5', 'program', 'Program', 0, 0, 0);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('FAC', '5', 'facility', 'Facility', 0, 0, 0);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('AST', '5', 'lst_admission_status', 'Admission Status', 0, 0, 0);
insert into app_lookuptable (tableid, moduleid, table_name, description, istree, treecode_length, activeyn)
values ('PTY', '5', 'lst_program_type', 'Program Type', 0, 0, 0);
commit;

insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('ORG', 'code', 'CODE', '1', 'S', null, 'code', 1, 1, 1, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('ORG', 'description', 'DESCRIPTION', '1', 'S', null, 'description', 2, 0, 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('ORG', 'activeyn', 'ACTIVEYN', '1', 'N', null, 'activeyn', 3, 0, 3, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('ORG', 'orderbyindex', 'ORDERBYINDEX', '1', 'N', null, 'orderbyindex', 4, 0, 4, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('ORG', 'codetree', 'CODETREE', '1', 'S', null, 'codetree', 5, 0, 7, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('FCT', 'ID', 'Id', '0', 'N', null, 'id', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('FCT', 'DESCRIPTION', 'Description', '1', 'S', null, 'description', 2, 0, 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('FCT', 'ISACTIVE', 'Active?', '1', 'B', null, 'isactive', 3, 0, 3, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('FCT', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'displayorder', 4, 0, 4, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('BTY', 'BED_TYPE_ID', 'Id', '0', 'N', null, 'bed_type_id', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('BTY', 'NAME', 'Name', '1', 'S', null, 'name', 2, 0, 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('BTY', 'DFLT', 'Default Capacity', '1', 'N', null, 'dflt', 3, 0, 6, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('LKT', 'TABLEID', 'Id', '0', 'S', null, 'tableid', 1, 1, 1, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('LKT', 'MODULEID', 'Category', '1', 'S', 'FCT', 'moduleid', 2, 0, 5, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('LKT', 'TABLE_NAME', 'Table Name', '1', 'S', null, 'table_name', 3, 0, 6, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('LKT', 'DESCRIPTION', 'Description', '1', 'S', null, 'description', 4, 0, 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('LKT', 'ISTREE', 'Is Tree Structured?', '1', 'B', null, 'istree', 5, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('LKT', 'TREECODE_LENGTH', 'Tree Code Length (if is tree)', '1', 'N', null, 'treecode_length', 6, 0, 0, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('LKT', 'ACTIVEYN', 'Active?', '1', 'B', null, 'activeyn', 7, 0, 3, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('GEN', 'CODE', 'Code', '1', 'S', null, 'code', 1, 1, 1, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('GEN', 'DESCRIPTION', 'Description', '1', 'S', null, 'description', 2, 0, 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('GEN', 'ISACTIVE', 'Active?', '1', 'B', null, 'isactive', 3, 0, 3, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('GEN', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'displayorder', 4, 0, 4, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('SEC', 'ID', 'Id', '0', 'N', null, 'id', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('SEC', 'DESCRIPTION', 'Description', '1', 'S', null, 'description', 2, 0, 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('SEC', 'ISACTIVE', 'Active?', '1', 'B', null, 'isactive', 3, 0, 3, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('SEC', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'displayorder', 4, 0, 4, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('OGN', 'ID', 'Id', '0', 'N', null, 'id', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('OGN', 'DESCRIPTION', 'Description', '1', 'S', null, 'description', 2, 0, 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('OGN', 'ISACTIVE', 'Active?', '1', 'B', null, 'isactive', 3, 0, 3, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('OGN', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'displayorder', 4, 0, 4, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('DRN', 'ID', 'Id', '0', 'N', null, 'id', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('DRN', 'DESCRIPTION', 'Description', '1', 'S', null, 'description', 2, 0, 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('DRN', 'NEEDSECONDARY', 'Has Associated Secondary Reason?', '1', 'N', null, 'needsecondary', 3, 0, 6, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('DRN', 'ISACTIVE', 'Active?', '1', 'B', null, 'isactive', 4, 0, 3, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('DRN', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'displayorder', 5, 0, 4, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('SRT', 'ID', 'Id', '0', 'N', null, 'id', 1, 1, 1, 1);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('SRT', 'DESCRIPTION', 'Description', '1', 'S', null, 'description', 2, 0, 2, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('SRT', 'ISACTIVE', 'Active?', '1', 'B', null, 'isactive', 3, 0, 3, 0);
insert into app_lookuptable_fields (tableid, fieldname, fielddesc, edityn, fieldtype, lookuptable, fieldsql, fieldindex, uniqueyn, genericiDX, AUTOYN)
values ('SRT', 'DISPLAYORDER', 'Dispaly Order', '1', 'N', null, 'displayorder', 4, 0, 4, 0);
commit;
