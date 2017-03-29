alter table document modify contenttype varchar(255);
update document set contenttype='application/vnd.openxmlformats-officedocument.wordprocessingml.document' where contenttype='application/vnd.openxmlformats-officedocument.wordprocessing';
update document set contenttype='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' where contenttype='application/vnd.openxmlformats-officedocument.spreadsheetml.';


