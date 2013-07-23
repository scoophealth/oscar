/*This is an update to resolve duplicate measurement type="WT" with measuringInstruction="in BMI" for those users where this data exists

Before deleting the duplicate WT (in BMI) we should record it was deleted in measurementTypeDeleted. To do this here we look for more than one measurement type "WT" then look for the one with measuringInstruction equals "in BMI" and insert that record into measurementTypeDeleted */
INSERT INTO measurementTypeDeleted(type, typeDisplayName, typeDescription, measuringInstruction, validation, dateDeleted) SELECT mt.type, mt.typeDisplayName, mt.typeDescription, mt.measuringInstruction, mt.validation, NOW() FROM measurementType AS mt WHERE mt.type="WT" && measuringInstruction="in BMI" && exists(select * from measurementType where type='WT' GROUP BY type HAVING (COUNT(type) > 1));

/*to make sure we do not simply delete the only measurement type "WT" we again look for more than one "WT" and delete the one where the 
measuringInstruction = 'in BMI' */
delete from measurementType where type='WT' && measuringInstruction = 'in BMI' && exists(
	SELECT * FROM (
		select * from measurementType where type='WT' GROUP BY type HAVING (COUNT(type) > 1)
	) AS find
);