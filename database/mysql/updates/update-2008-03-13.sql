alter table RedirectLinkTracking change provider_no provider_no varchar(6) not null;
alter table provider_facility change provider_id provider_no varchar(6) not null;
alter table RedirectLinkTracking change date redirectDate datetime not null;
