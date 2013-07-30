ALTER TABLE appointment ADD COLUMN reasonCode INT(11) NULL;

INSERT INTO LookupList(name, description, categoryId, active, createdBy, dateCreated) VALUES('reasonCode', 'Reason Code', null, 1, 'oscar', CURRENT_TIMESTAMP);

SET @lookupListId:=LAST_INSERT_ID();

INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Contraception'                 , 'Contraception'                 , 1 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Counselling'                   , 'Counselling'                   , 2 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'ECP'                           , 'ECP'                           , 3 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Follow-Up'                     , 'Follow-Up'                     , 4 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Genital Warts Treatment'       , 'Genital Warts Treatment'       , 5 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'HIV Testing'                   , 'HIV Testing'                   , 6 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Immunization'                  , 'Immunization'                  , 7 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'IUD Removal'                   , 'IUD Removal'                   , 8 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Needle Exchange'               , 'Needle Exchange'               , 9 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'PAP Test'                      , 'PAP Test'                      , 10, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Pregnancy Test'                , 'Pregnancy Test'                , 11, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Repeat PAP Test'               , 'Repeat PAP Test'               , 12, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Results'                       , 'Results'                       , 13, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'STI Exam'                      , 'STI Exam'                      , 14, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'STI Prescription/Treatment'    , 'STI Prescription/Treatment'    , 15, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Therapeutic Abortion Follow-Up', 'Therapeutic Abortion Follow-Up', 16, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Others'						 , 'Others'						   , 99, 1, 'oscar', CURRENT_TIMESTAMP);

COMMIT;


insert into FunctionalCentre values ('725 51 76 11','Peer/Self Help Initiatives');
