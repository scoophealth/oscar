#insert into secObjectName values ('_merge');

insert into access_type (name, type) values("Sex restriction override on referral","access");
insert into access_type (name, type) values("Sex restriction override on admission","access");
insert into access_type (name, type) values("Gender restriction override on referral","access");
insert into access_type (name, type) values("Gender restriction override on admission","access");
insert into access_type (name, type) values("Age restriction override on referral","access");
insert into access_type (name, type) values("Age restriction override on admission","access");
insert into access_type (name, type) values("Allow duplicate client merge","access");
insert into access_type (name, type) values("Perform program registration intake","access");
insert into access_type (name, type) values("perform registration intake","access");
insert into access_type (name, type) values("perform admissions","access");
insert into access_type (name, type) values("perform discharges","access");
insert into access_type (name, type) values("perform bed assignments","access");
insert into access_type (name, type) values("print bed rosters and reports","access");

#update agency set intake_quick_state='HS';