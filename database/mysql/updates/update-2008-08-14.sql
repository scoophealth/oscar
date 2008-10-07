--Indexes for msg module

create index message on messagelisttbl (message);
create index provider_no on messagelisttbl (provider_no);
create index status on messagelisttbl(status);
create index remoteLocation on messagelisttbl(remoteLocation);
