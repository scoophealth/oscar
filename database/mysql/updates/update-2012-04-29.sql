create table MyGroupAccessRestriction (
        id int not null auto_increment,
        myGroupNo varchar(50) not null,
        providerNo varchar(20) not null,
        lastUpdateUser varchar(20),
        lastUpdateDate datetime,
        key(myGroupNo),
        key(myGroupNo,providerNo),
        primary key(id)
);

