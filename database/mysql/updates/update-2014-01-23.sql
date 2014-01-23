/*for PHV flowsheet*/
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CAVD', 'Calcium and Vitamin D', 'NULL', 'Review', '16', '2014-01-23 13:00:00'),
( 'STIS', 'STI Screening', 'Sexual Transmitted Infections', 'Review', '16', '2014-01-23 13:00:00'),
( 'SSXC', 'Safe Sex Counselling', 'NULL', 'Review', '16', '2014-01-23 13:00:00');

/*this was added by PHV and requested to delete by PHV*/
delete from measurementType where type='PCPR';
delete from measurementType where type='NFSW';
delete from measurementType where type='HOTW';
delete from measurementType where type='PWC';