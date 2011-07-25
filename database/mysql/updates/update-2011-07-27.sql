alter table HRMDocument add sourceFacility varchar(120);

create table providerLabRoutingFavorites ( 
 id int auto_increment primary key, 
 provider_no varchar(6), 
 index(provider_no), 
 route_to_provider_no varchar(6) 
);
