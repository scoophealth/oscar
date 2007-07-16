alter table client_referral add present_problems varchar(255) default NULL;
alter table client_referral add radioRejectionReason varchar(10) default '0';
alter table program_queue add present_problems varchar(255) default NULL;



