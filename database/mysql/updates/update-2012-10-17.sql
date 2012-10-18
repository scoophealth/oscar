CREATE TABLE `workflow` (
  ID int(10) NOT NULL AUTO_INCREMENT,
  workflow_type varchar(100),
  provider_no varchar(20),
  demographic_no int(10),
  completion_date date,
  current_state varchar(50),
  create_date_time datetime, 
  PRIMARY KEY(`ID`)
);
