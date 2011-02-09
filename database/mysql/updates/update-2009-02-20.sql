  create table flowsheet_customization(
      id int(10) NOT NULL auto_increment primary key,  
      flowsheet varchar(40),
      action varchar(10),
      measurement varchar(255),
      payload text,
      provider_no varchar(6),
      demographic_no int(10),
      create_date datetime,
      archived char(1) default '0',
      archived_date datetime
      
     
    ) ; 