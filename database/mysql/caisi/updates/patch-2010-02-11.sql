alter table OcanStaffForm add startDate date NOT NULL;
alter table OcanStaffForm add completionDate date;
alter table OcanStaffForm add reasonForAssessment varchar(100);
alter table OcanStaffForm add assessmentStatus varchar(40) NOT NULL;

create table OcanClientForm
(
        id int primary key auto_increment,
        ocanFormVersion varchar(16) not null,
        index(ocanFormVersion),
        providerNo varchar(6) not null,
        created datetime not null,
        facilityId int not null,
        clientId int not null,
        index(facilityId, clientId),
        lastName varchar(100),
        firstName varchar(100),
        dateOfBirth varchar(100)
);

create table OcanClientFormData
(
        id int primary key auto_increment,
        ocanClientFormId int not null,
        index(ocanClientFormId),
        question varchar(64) not null,
        index(question),
        answer varchar(16) not null
);

alter table OcanClientForm add startDate date NOT NULL;
alter table OcanClientForm add completionDate date;

