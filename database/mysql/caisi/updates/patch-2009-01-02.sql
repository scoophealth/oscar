alter table ClientLink drop column creationDate;
alter table ClientLink drop foreign key ClientLink_ibfk_2;
alter table ClientLink drop column creatorProviderId;
alter table ClientLink drop column active;

alter table ClientLink add column linkDate datetime not null;
alter table ClientLink add column linkProviderNo varchar(6) not null, add foreign key (linkProviderNo) references provider(provider_no);
alter table ClientLink add column unlinkDate datetime;
alter table ClientLink add column unlinkProviderNo varchar(6), add foreign key (unlinkProviderNo) references provider(provider_no);
