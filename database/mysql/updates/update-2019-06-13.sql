alter table ServiceClient add lifetime int;
update ServiceClient set lifetime = -1 where lifetime is null;
