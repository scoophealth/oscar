
INSERT INTO  `secObjectName` (`objectName`) VALUES ('_admin.eformreporttool') ON DUPLICATE KEY UPDATE objectName='_admin.eformreporttool' ;
INSERT INTO  `secObjectName` (`objectName`) VALUES ('_admin.eform') ON DUPLICATE KEY UPDATE objectName='_admin.eform' ;
INSERT INTO  `secObjPrivilege` VALUES('admin', '_admin.eformreporttool', 'x', 0, '999998') ON DUPLICATE KEY UPDATE objectName='_admin.eformreporttool' ;
INSERT INTO  `secObjPrivilege` VALUES('admin', '_admin.eform', 'x', 0, '999998') ON DUPLICATE KEY UPDATE objectName='_admin.eform' ;