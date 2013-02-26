alter table Facility add vacancyWithdrawnTicklerProvider varchar(25);
alter table Facility add vacancyWithdrawnTicklerDemographic int(10);

alter table vacancy add statusUpdateUser varchar(25);
alter table vacancy add statusUpdateDate datetime;
