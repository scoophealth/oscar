CREATE TABLE `hsfo_recommit_schedule` (   
                          `id` int(11) NOT NULL auto_increment,   
                          `status` varchar(2) default NULL,       
                          `memo` text,                            
                          `schedule_time` datetime default NULL,  
                          `user_no` varchar(6) default NULL,      
                          `check_flag` tinyint(1) default NULL,   
                          PRIMARY KEY  (`id`)                     
                        ) ENGINE=InnoDB DEFAULT CHARSET=utf8; 