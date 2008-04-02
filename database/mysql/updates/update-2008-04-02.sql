alter table admission drop agency_id;
alter table client_referral drop source_agency_id, drop agency_id;
alter table program drop agency_id;
alter table program_queue drop agency_id;
alter table casemgmt_note drop agency_no;