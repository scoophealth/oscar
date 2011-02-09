create table RedirectLink (id int primary key auto_increment, url varchar(255) not null);
create table RedirectLinkTracking (date datetime not null, provider_no int not null, redirectLinkId int not null, index(redirectLinkId,date));

