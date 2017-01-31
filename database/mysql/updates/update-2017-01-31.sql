insert into scheduletemplate Values('Public','P:OnCallClinic','Weekends/Holidays','________________________________________CCCCCCCCCCCCCCCC________________________________________');

insert into scheduletemplatecode (id,code,description,duration,color,confirm,bookinglimit) Values(null,'C','On Call Clinic','15','green','Onc',1);

insert into OscarJobType Values(null,'OSCAR ON CALL CLINIC', 'Notifies MRP if patient seen during on-call clinic','org.oscarehr.jobs.OscarOnCallClinic',false,now());

insert into OscarJob Values(null,'OSCAR On-Call Clinic',null,(select id from OscarJobType where name = 'OSCAR ON CALL CLINIC'),'0 0 4 * * *','999998',false,now());


CREATE TABLE `onCallClinicDates` (
  `id` int(10),
  `startDate` date,
  `endDate` date,
  `name` varchar(256),
  `location` varchar(256),
  `color` varchar(7),
  PRIMARY KEY (`id`)
);
