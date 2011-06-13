CREATE TABLE SecurityToken (
  id int(10) NOT NULL auto_increment,
  token varchar(100) not null,
  created timestamp not null,
  expiry datetime not null,
  data varchar(255),
  providerNo varchar(10),
  PRIMARY KEY  (id),
  KEY (token)
);

