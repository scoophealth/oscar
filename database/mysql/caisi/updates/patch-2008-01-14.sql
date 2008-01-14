alter table caisi_role add column userDefined tinyint not null after name;
update caisi_role set userDefined=1 where name not in ('doctor', 'nurse', 'counsellor', 'csw');