drop table if exists EyeformConsultationReport;
drop table if exists EyeformConsulationReport;

create table EyeformConsultationReport (
 id int(10) NOT NULL auto_increment,
 `date` date,
 referralId integer,
 greeting integer,
 appointmentNo integer,
 appointmentDate date,
 appointmentTime time,
 demographicNo int,
 reason varchar(255),
 type varchar(255),
 cc varchar(255),
 memo varchar(255),
 clinicalInfo varchar(255),
 currentMeds varchar(255),
 allergies varchar(255),
 providerNo varchar(20),
 status varchar(255),
 sendTo varchar(255),
 examination varchar(255),
 concurrentProblems varchar(255),
 impression varchar(255),
 plan varchar(255),
 urgency varchar(100),
 patientWillBook integer,
 primary key(id)
);

