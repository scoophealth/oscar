create table Episode (
        id int not null auto_increment primary key,
        demographicNo int not null,
        startDate date not null,
        endDate date,
        code varchar(50),
        codingSystem varchar(50),
        description varchar(255),
        status varchar(25),
        lastUpdateUser varchar(25) not null,
        lastUpdateTime timestamp not null
);

