
CREATE TABLE OLISResultNomenclature (
	 id INT NOT NULL AUTO_INCREMENT,
         nameId  VARCHAR(10),
         name TEXT,
	 PRIMARY KEY(id)
       );
LOAD DATA LOCAL INFILE 'OLISTestResultNomenclature.csv'
INTO TABLE OLISResultNomenclature
FIELDS TERMINATED BY '\t'
       OPTIONALLY ENCLOSED BY '\"' 
LINES TERMINATED BY '\n'
(nameId, name);

CREATE TABLE OLISRequestNomenclature (
	 id INT NOT NULL AUTO_INCREMENT,
         nameId  VARCHAR(10),
         name TEXT,
	 category VARCHAR(20),
	 PRIMARY KEY(id)
       );

LOAD DATA LOCAL INFILE 'OLISTestRequestNomenclature.csv'
INTO TABLE OLISRequestNomenclature
FIELDS TERMINATED BY '\t'
       OPTIONALLY ENCLOSED BY '\"' 
LINES TERMINATED BY '\n'
(nameId, name, category);


CREATE TABLE OLISProviderPreferences (
     providerId  VARCHAR(10),
     startTime VARCHAR(20),
         PRIMARY KEY(providerId));
CREATE TABLE OLISSystemPreferences (
     id INT NOT NULL AUTO_INCREMENT,
     startTime VARCHAR(20),
     endTime VARCHAR(20),
     pollFrequency INT,
     lastRun timestamp,
     filterPatients tinyint(1),
         PRIMARY KEY(id)
);

update OLISSystemPreferences set filterPatients=0;
