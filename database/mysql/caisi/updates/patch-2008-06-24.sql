alter table facility add column useQuickConsent tinyint(1) not null;
update facility set useQuickConsent=1;