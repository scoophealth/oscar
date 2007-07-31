# role used for security enforcement of opt out in searches


update secRole set role_name='er_clerk' where role_name='ER Clerk';
update secUserRole set role_name='er_clerk' where role_name='ER Clerk'; 

#The following line should be put in oscar db init file.
#insert into `secRole` values(null, 'er_clerk');
