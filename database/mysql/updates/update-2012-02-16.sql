drop table if exists doc_manager;
drop table if exists doc_category;
CREATE TABLE `doc_category` (
                `cat_id` int(11) NOT NULL auto_increment,
                `level` int(11) NOT NULL ,
                `parent_id` int(11),
                `category` varchar(40) NOT NULL ,
                `description` varchar(100),
                `testmark` tinyint(4),
                PRIMARY KEY  (`cat_id`),
                KEY `parent_id` (`parent_id`)
);

CREATE TABLE `doc_manager` (
               `id` int(20) NOT NULL auto_increment,
               `category_id` int(11),
               `doc_path` varchar(100),
               `demographic_no` int(20) NOT NULL,
               `primary_provider_no` varchar(6) NOT NULL ,
               `reviewed_flag` tinyint(1) NOT NULL,
               `upload_from_id` int(11),
               `tickler_no` int(11),
               PRIMARY KEY  (`id`),
               KEY `FK_doc_manager` (`category_id`),
               CONSTRAINT `doc_manager_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `doc_category` (`cat_id`)
);

drop table if exists uploadfile_from;
CREATE TABLE `uploadfile_from` (
                   `id` int(11) NOT NULL auto_increment,
                   `from_name` varchar(50) NOT NULL,
                   PRIMARY KEY  (`id`)
) ;

ALTER TABLE document MODIFY document_no int(20) NOT NULL auto_increment;
alter table ctl_document add PRIMARY KEY (module_id,module,document_no);
alter table ctl_document add index (module);
alter table ctl_document add index (document_no);
