DROP TABLE IF EXISTS icd10_temp;
CREATE TABLE icd10_temp (
  id int(11) NOT NULL auto_increment,
  icd10 varchar(7) NOT NULL default '',
  description varchar(255) NOT NULL default '',
  PRIMARY KEY  (id)
) ENGINE = InnoDB;


insert into icd10_temp (icd10,description) select code,long_desc from icd10;

drop table icd10;

rename table icd10_temp TO icd10;

