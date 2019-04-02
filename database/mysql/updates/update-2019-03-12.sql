alter table OLISProviderPreferences add lastRun datetime;


CREATE TABLE OLISResults (
    id int(11) auto_increment,
    requestingHICProviderNo varchar(30),
    providerNo varchar(30),
    queryType varchar(20),
    results text,
    hash varchar(255),
    status varchar(10),
    uuid varchar(255),
    query varchar(255),
    demographicNo integer,
    queryUuid varchar(255),
    PRIMARY KEY(id)
);
CREATE TABLE OLISQueryLog (
    id int(11) auto_increment,
    initiatingProviderNo varchar(30),
    queryType varchar(20),
    queryExecutionDate datetime,
    uuid varchar(255),
    requestingHIC varchar(30),
    demographicNo integer,
    PRIMARY KEY(id)
);

