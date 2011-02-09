create table if not exists provider_facility
(
	provider_id int not null,
	facility_id int not null,
	unique (provider_id, facility_id),
	index (facility_id)
);
