CREATE TABLE IntegratorFileLog (
    id int(11) auto_increment,
    filename varchar(255),
    checksum varchar(255),
    lastDateUpdated datetime,
    currentDate datetime,
    integratorStatus varchar(100),
    dateCreated timestamp,
    PRIMARY KEY(id)
);

create index measurement_integrator on measurements(demographicNo,dateEntered);
create index dxresearch_integrator on dxresearch(demographic_no,update_date);
