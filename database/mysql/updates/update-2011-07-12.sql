create table FlowSheetUserCreated(
  id int(10) auto_increment primary key,
  name varchar(4),
  dxcodeTriggers varchar(255),	
  displayName varchar(255),
  warningColour varchar(20),
  recommendationColour varchar(20),
  topHTML text,
  archived tinyint(1),
  createdDate date,
  KEY FlowSheetUserCreated_archived (archived)
);
