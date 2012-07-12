insert into access_type (name,type) values('read receptionist notes','access');
insert into access_type (name,type) values('write receptionist notes','access');
insert into access_type (name,type) values('write receptionist issues','access');
insert into access_type (name,type) values('read receptionist issues','access');
insert into access_type (name,type) values('read receptionist ticklers','access');

insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='read receptionist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='read receptionist notes'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='write receptionist issues'));
insert into default_role_access (role_id,access_id) values ((select role_no from secRole where role_name ='doctor'),(select access_id from access_type where name='write receptionist notes'));

