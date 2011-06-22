alter table Facility add rxInteractionWarningLevel int(10) not null;
update Facility set rxInteractionWarningLevel=0;

