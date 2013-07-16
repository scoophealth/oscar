INSERT INTO measurementTypeDeleted(type, typeDisplayName, typeDescription, measuringInstruction, validation, dateDeleted) SELECT mt.type, mt.typeDisplayName, mt.typeDescription, mt.measuringInstruction, mt.validation, NOW() FROM measurementType AS mt WHERE mt.type="WT" && measuringInstruction="in BMI" && exists(select * from measurementType where type='WT' GROUP BY type HAVING (COUNT(type) > 1));

delete from measurementType where type='WT' && measuringInstruction = 'in BMI' && exists(
	SELECT * FROM (
		select * from measurementType where type='WT' GROUP BY type HAVING (COUNT(type) > 1)
	) AS find
);
