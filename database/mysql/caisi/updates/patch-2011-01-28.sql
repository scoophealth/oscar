alter table OcanStaffForm add assessmentId int after id;
alter table OcanStaffForm add clientFormCreated datetime after created;
alter table OcanStaffForm add clientFormProviderNo varchar(6) after providerNo;
alter table OcanStaffForm add clientFormProviderName varchar(100) after providerName;

alter table OcanStaffForm modify providerNo varchar(6);
alter table OcanStaffForm modify created datetime;

delete from OcanFormOption where id=704 or id=709;