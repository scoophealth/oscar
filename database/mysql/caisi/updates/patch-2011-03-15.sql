alter table ProviderPreference add defaultDoNotDeleteBilling  tinyint(1) ;
alter table ProviderPreference add defaultDxCode varchar(4) ;
update ProviderPreference set defaultDoNotDeleteBilling=0 where defaultDoNotDeleteBilling is NULL;
