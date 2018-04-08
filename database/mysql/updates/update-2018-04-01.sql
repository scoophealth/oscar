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
    PRIMARY KEY(id)
);
