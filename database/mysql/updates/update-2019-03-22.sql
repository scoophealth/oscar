ALTER TABLE document ADD report_status varchar(30);
ALTER TABLE clinic ADD cdx_oid varchar(30);
ALTER TABLE clinic ADD CONSTRAINT cdx_oid_unique UNIQUE ( cdx_oid );