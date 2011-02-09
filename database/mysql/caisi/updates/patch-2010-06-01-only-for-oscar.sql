
insert into `secRole` (role_name, description) values('psychiatrist', 'psychiatrist');
insert into `secRole` (role_name, description) values('RN', 'Registered Nurse');
insert into `secRole` (role_name, description) values('RPN', 'Registered Practical Nurse');
insert into `secRole` (role_name, description) values('Nurse Manager', 'Nurse Manager');
insert into `secRole` (role_name, description) values('Clinical Social Worker','Clinical Social Worker');
insert into `secRole` (role_name, description) values('Clinical Case Manager','Clinical Case Manager');
insert into `secRole` (role_name, description) values('Medical Secretary', 'Medical Secretary');
insert into `secRole` (role_name, description) values('Clinical Assistant', 'Clinical Assistant');
insert into `secRole` (role_name, description) values('secretary', 'secretary');
insert into `secRole` (role_name, description) values('counsellor', 'counsellor');
insert into `secRole` (role_name, description) values('Case Manager', 'Case Manager');
insert into `secRole` (role_name, description) values('Housing Worker', 'Housing Worker');
insert into `secRole` (role_name, description) values('Support Worker', 'Support Worker');
insert into `secRole` (role_name, description) values('Client Service Worker', 'Client Service Worker');

insert into access_type (name, type) values("read ticklers assigned to a doctor","access");

insert into access_type (name, type) values("read ticklers assigned to a psychiatrist","access");
insert into access_type (name, type) values("write psychiatrist issues","access");
insert into access_type (name, type) values("read psychiatrist issues","access");
insert into access_type (name, type) values("read psychiatrist notes","access");

insert into access_type (name, type) values("read ticklers assigned to a RN","access");
insert into access_type (name, type) values("write RN issues","access");
insert into access_type (name, type) values("read RN issues","access");
insert into access_type (name, type) values("read RN notes","access");

insert into access_type (name, type) values("read ticklers assigned to a RPN","access");
insert into access_type (name, type) values("write RPN issues","access");
insert into access_type (name, type) values("read RPN issues","access");
insert into access_type (name, type) values("read RPN notes","access");


insert into access_type (name, type) values("read ticklers assigned to a Nurse Manager","access");
insert into access_type (name, type) values("write Nurse Manager issues","access");
insert into access_type (name, type) values("read Nurse Manager issues","access");
insert into access_type (name, type) values("read Nurse Manager notes","access");


insert into access_type (name, type) values("read ticklers assigned to a Clinical Social Worker","access");
insert into access_type (name, type) values("write Clinical Social Worker issues","access");
insert into access_type (name, type) values("read Clinical Social Worker issues","access");
insert into access_type (name, type) values("read Clinical Social Worker notes","access");



insert into access_type (name, type) values("read ticklers assigned to a Clinical Case Manager","access");
insert into access_type (name, type) values("write Clinical Case Manager issues","access");
insert into access_type (name, type) values("read Clinical Case Manager issues","access");
insert into access_type (name, type) values("read Clinical Case Manager notes","access");



insert into access_type (name, type) values("read ticklers assigned to a counsellor","access");


insert into access_type (name, type) values("read ticklers assigned to a Case Manager","access");
insert into access_type (name, type) values("write Case Manager issues","access");
insert into access_type (name, type) values("read Case Manager issues","access");
insert into access_type (name, type) values("read Case Manager notes","access");


insert into access_type (name, type) values("read ticklers assigned to a Housing Worker","access");
insert into access_type (name, type) values("write Housing Worker issues","access");
insert into access_type (name, type) values("read Housing Worker issues","access");
insert into access_type (name, type) values("read Housing Worker notes","access");


insert into access_type (name, type) values("read ticklers assigned to a Medical Secretary","access");
insert into access_type (name, type) values("write Medical Secretary issues","access");
insert into access_type (name, type) values("read Medical Secretary issues","access");
insert into access_type (name, type) values("read Medical Secretary notes","access");


insert into access_type (name, type) values("read ticklers assigned to a Clinical Assistant","access");
insert into access_type (name, type) values("write Clinical Assistant issues","access");
insert into access_type (name, type) values("read Clinical Assistant issues","access");
insert into access_type (name, type) values("read Clinical Assistant notes","access");


insert into access_type (name, type) values("read ticklers assigned to a secretary","access");
insert into access_type (name, type) values("write secretary issues","access");
insert into access_type (name, type) values("read secretary issues","access");
insert into access_type (name, type) values("read secretary notes","access");

insert into access_type (name, type) values("read ticklers assigned to a Support Worker","access");
insert into access_type (name, type) values("write Support Worker issues","access");
insert into access_type (name, type) values("read Support Worker issues","access");
insert into access_type (name, type) values("read Support Worker notes","access");

insert into access_type (name, type) values("read ticklers assigned to a Client Service Worker","access");
insert into access_type (name, type) values("write Client Service Worker issues","access");
insert into access_type (name, type) values("read Client Service Worker issues","access");
insert into access_type (name, type) values("read Client Service Worker notes","access");

