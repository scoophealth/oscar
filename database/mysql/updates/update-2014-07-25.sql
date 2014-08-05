update drugs set dispenseInternal =0 where dispenseInternal is null;
alter table drugs modify dispenseInternal tinyint(1) not null;
