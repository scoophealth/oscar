alter table drugs add column lastUpdateDate datetime;
update drugs set lastUpdateDate=now();
alter table drugs change column lastUpdateDate lastUpdateDate datetime not null;

update drugs set customName=null where customName='null';
update drugs set special_instruction=null where special_instruction='null';
update drugs set unitName=null where unitName='null';
update drugs set eTreatmentType=null where eTreatmentType='null';
update drugs set rxStatus=null where rxStatus='null';