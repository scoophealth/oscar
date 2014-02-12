insert into `secRole` (role_name, description) values('midwife', 'midwife');

#no worry here, it's a duplicate of providerNo column
alter table HRMDocumentToProvider drop `signOffProvider`;
