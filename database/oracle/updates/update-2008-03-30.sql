-- this table is created by Jason
 create table user_ds_message_prefs(
            id number,
            resource_type varchar2(255),
            resource_id varchar2(255),
            resource_updated_date date,
            provider_no  varchar2(6),
            record_created date,
            archived number(1) default 1,
            primary key(id)
         );
CREATE OR REPLACE TRIGGER tri_user_ds_message_prefs
BEFORE INSERT
ON user_ds_message_prefs
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN
     IF :new.ID IS NULL THEN
        SELECT MAX(ID)+1 INTO IDX FROM user_ds_message_prefs;
        :new.ID := IDX;
     END IF;
END;
