CREATE TABLE DHIRSubmissionLog (
    id int(11) auto_increment,
    demographicNo int,
    preventionId int,
    submitterProviderNo varchar(255),
    status varchar(255),
    dateCreated datetime,
    transactionId varchar(100),
    bundleId varchar(255),
    response mediumtext,
    contentLocation varchar(255),
    clientRequestId varchar(100),
    clientResponseId varchar(100),
    PRIMARY KEY(id)
);

alter table BornTransmissionLog add demographicNo int;
alter table BornTransmissionLog add type varchar(20);
alter table BornTransmissionLog add httpCode varchar(20);
alter table BornTransmissionLog add httpResult mediumtext;
alter table BornTransmissionLog add httpHeaders text;
alter table BornTransmissionLog add hialTransactionId varchar(255);
alter table BornTransmissionLog add contentLocation varchar(255);
alter table BornTransmissionLog modify filename varchar(100);


