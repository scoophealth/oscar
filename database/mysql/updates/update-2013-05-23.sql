ALTER TABLE document ADD contentdatetime datetime;
update document set contentdatetime=updatedatetime;
