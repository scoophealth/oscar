create table flowsheet_drug (
      id int(10) NOT NULL auto_increment primary key,  
      flowsheet varchar(40),
      atc_code varchar(40),
      provider_no varchar(6),
      demographic_no int(10),
      create_date datetime,
      archived char(1) default '0',
      archived_date datetime
  
    ) ; 