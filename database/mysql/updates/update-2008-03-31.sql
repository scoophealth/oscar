-- Table used to store users preferences on which ds messages should be displayed.

 create table user_ds_message_prefs(
            id int(10) primary key auto_increment,
            resource_type varchar(255),
            resource_id varchar(255),
            resource_updated_date date,
            provider_no  varchar(6),
            record_created date,
            archived tinyint(1) default 1,
            key archived (archived),
            key `provider_no` (provider_no),
            key `resource_id` (resource_id),
            key `resource_type` (resource_type)
         );