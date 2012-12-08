alter table billinginr change provider_no provider_no varchar(10);
update program set defaultServiceRestrictionDays=0 where defaultServiceRestrictionDays is null;

