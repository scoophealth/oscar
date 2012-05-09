ALTER TABLE billingreferral add country varchar(30) after province;
update billingreferral set country='CA';

