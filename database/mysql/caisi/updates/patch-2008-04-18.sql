alter table facility add column integratorEnabled tinyint(1) not null;
alter table facility add column integratorUrl varchar(255);
alter table facility add column integratorUser varchar(255);
alter table facility add column integratorPassword varchar(255);