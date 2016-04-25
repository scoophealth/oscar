CREATE TABLE billingperclimit (
  service_code varchar(10) NOT NULL ,
  min varchar(8),
  max varchar(8),
  effective_date date,
  id int auto_increment,
  PRIMARY KEY  (id)
) ;

