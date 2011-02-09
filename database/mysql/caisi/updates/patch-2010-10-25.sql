update OcanFormOption set ocanDataCategoryValue="NOTAVAIL" where id=185;
alter table IntegratorConsent add signatureStatus varchar(10) not NULL;
update IntegratorConsent set signatureStatus="PAPER" where signatureStatus=NULL or signatureStatus="";