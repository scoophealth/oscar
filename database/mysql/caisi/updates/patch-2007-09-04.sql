alter table admission modify discharge_notes text default null;
alter table admission modify admission_notes text default null;
alter table client_referral modify notes text default null;
alter table client_referral modify present_problems text default null;
alter table client_referral modify completion_notes text default null;