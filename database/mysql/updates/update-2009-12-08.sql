alter table drugs alter column archived_reason set default '';
update drugs set drugs.archived_reason='' where drugs.archived_reason='deleted' and drugs.archived=0;
update drugs set drugs.archived_reason='deleted' where drugs.archived_reason='' and drugs.archived=1;