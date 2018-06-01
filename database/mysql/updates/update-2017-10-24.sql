
alter table resourcestorage add column update_date datetime;
alter table resourcestorage add column reference_date datetime;

create index ResourceStorage_resourceType_uuid on resourcestorage(uuid);

alter table surveyData modify column surveyId varchar(40);

alter table surveyData add column period int(10);
alter table surveyData add column randomness int(10);
alter table surveyData add column version int(10);


create table SurveillanceData (
			id int(10)  NOT NULL auto_increment primary key,
			surveyId varchar(50),
			data mediumblob,
			createDate datetime,
			lastUpdateDate datetime,
			transmissionDate datetime,
			sent boolean
			);
			
			
