create table log_temp like log;
insert into log_temp select * from log;

drop table log;

CREATE TABLE `log` (
  id bigint auto_increment primary key,
  `dateTime` datetime not null,
  `provider_no` varchar(10),
  index datetime (`dateTime`, `provider_no`),
  `action` varchar(64),
  INDEX `action` (`action`),
  `content` varchar(80),
  INDEX `content` (`content`),
  `contentId` varchar(80),
  INDEX `contentId` (`contentId`),
  `ip` varchar(30),
  `demographic_no` int(10),
  INDEX `demographic_no` (`demographic_no`),
  `data` text
) ;

insert into log (dateTime,provider_no,action,content,contentId,ip,demographic_no,data) 
  select dateTime,provider_no,action,content,contentId,ip,demographic_no,data from log_temp;

update log set content=null where content='';
update log set provider_no=null where provider_no='';

drop table log_temp;
