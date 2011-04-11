alter table ProviderPreference change defaultDoNotDeleteBilling  defaultDoNotDeleteBilling tinyint(1) not null;
-- this is because on older versions at least (if not current versions still as well as many databases like postgres/oracle, altering a constraint after data exists will leave data that violates the constraint in violation in the database.
update ProviderPreference set defaultDoNotDeleteBilling=0 where defaultDoNotDeleteBilling is null;
