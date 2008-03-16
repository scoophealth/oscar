alter table custom_filter add column programId varchar(10) after demographic_no;

INSERT INTO `access_type` (name,type) VALUES ('read counsellor ticklers','access'),('write csw issues','access'),('read csw issues','access'),('read csw notes','access'),('read csw ticklers','access');