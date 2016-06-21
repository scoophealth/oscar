
/**
 * Author:  rjonasz
 * Created: 5-Jun-2016
 */

DROP TABLE IF EXISTS resident_oscarMsg;
CREATE TABLE resident_oscarMsg (
    id int(11) auto_increment,
    supervisor_no varchar(6),
    resident_no varchar(6),
    demographic_no int(11),
    appointment_no int(11),    
    note_id int(10),
    complete int(1),
    create_time timestamp,
    complete_time timestamp,
    PRIMARY KEY(id),
    index note_id_idx (note_id)
);


ALTER TABLE messagetbl ADD column type int(10);
ALTER TABLE messagetbl ADD column type_link varchar(2048);

alter table msgDemoMap drop primary key;
alter table msgDemoMap add column id int(11) auto_increment primary key;
alter table msgDemoMap add index demoMap_messageID_demographic_no ( messageID, demographic_no );

ALTER TABLE provider ADD column supervisor varchar(6);

CREATE TABLE oscar_msg_type (
    type int(10),
    description varchar(255),
    PRIMARY KEY(type)
);

INSERT INTO oscar_msg_type Values(1,'OSCAR Resident Review');

insert into oscar_msg_type Values(2,'General');
update messagetbl set type = 2 where type is null;


INSERT INTO `OscarJobType` VALUES (null,'OSCAR MSG REVIEW','Sends OSCAR Messages to Residents Supervisors when charts need to be reviewed','org.oscarehr.jobs.OscarMsgReviewSender',0,now());
INSERT INTO `OscarJob` VALUES (null,'OSCAR Message Review','',(select id from OscarJobType where name = 'OSCAR MSG REVIEW') ,'0 0/30 * * * *','999998',0,now());
