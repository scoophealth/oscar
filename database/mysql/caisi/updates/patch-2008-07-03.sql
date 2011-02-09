alter table program drop column userDefined;
alter table program change program_id id int auto_increment;

alter table program drop foreign key program_ibfk_1;
alter table program change facility_id facilityId int not null;
alter table program add foreign key (facilityId) references Facility(id);

alter table program change descr description varchar(255);
alter table program change max_allowed maxAllowed int NOT NULL;
alter table program change emergency_number emergencyNumber varchar(255) NOT NULL;
alter table program change holding_tank holdingTank tinyint(1) NOT NULL;
alter table program change allow_batch_admission allowBatchAdmission tinyint(1);
alter table program change allow_batch_discharge allowBatchDischarge tinyint(1);
alter table program change program_status programStatus varchar(8) NOT NULL;
alter table program change intake_program intakeProgram int;
alter table program change bed_program_link_id bedProgramLinkId int;
alter table program change exclusive_view exclusiveView varchar(20) not null;
alter table program change maximum_restriction_days maximumServiceRestrictionDays int;
alter table program change default_restriction_days defaultServiceRestrictionDays int not null;

alter table program drop column num_of_members;