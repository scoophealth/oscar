CREATE TABLE if not exists `doc_category` (                  
                `cat_id` int(11) NOT NULL auto_increment,    
                `level` int(11) NOT NULL default '1',        
                `parent_id` int(11) default NULL,            
                `category` varchar(40) NOT NULL default '',  
                `description` varchar(100) default NULL, 
                `testmark` tinyint(4) default '0',    
                PRIMARY KEY  (`cat_id`),                     
                KEY `parent_id` (`parent_id`)                
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE if not exists `doc_manager` (                                                                        
               `id` int(20) NOT NULL auto_increment,                                                             
               `category_id` int(11) default NULL,                                                               
               `doc_path` varchar(100) default NULL,                                                             
               `demographic_no` int(20) NOT NULL default '0',                                                    
               `primary_provider_no` varchar(6) NOT NULL default '0',                                            
               `reviewed_flag` tinyint(1) NOT NULL default '0',                                                  
               `upload_from_id` int(11) default NULL,                                                            
               `tickler_no` int(11) default '0',                                                                 
               PRIMARY KEY  (`id`),                                                                              
               KEY `FK_doc_manager` (`category_id`),                                                             
               CONSTRAINT `doc_manager_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `doc_category` (`cat_id`)  
             ) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='InnoDB free: 8192 kB';            


drop table if exists digital_photo;
CREATE TABLE `digital_photo` (             
                 `id` int(11) NOT NULL auto_increment,    
                 `eyeform_id` bigint(20) default NULL,    
                 `provider` varchar(60) default NULL,     
                 `appointment_no` int(11) default NULL,   
                 `demographic_no` int(11) default NULL,   
                 `name` varchar(100) NOT NULL default '',  
                 `description` varchar(50) default NULL,  
                 `document_id` int(11) default '0',       
                 `date` datetime default NULL,            
                 PRIMARY KEY  (`id`)                      
               ) ENGINE=InnoDB DEFAULT CHARSET=utf8;   
               
drop table if exists eye_exam;
CREATE TABLE `eye_exam` (                                  
			`id` int(11) NOT NULL auto_increment,                    
            `eyeform_id` bigint(11) default NULL,                    
            `app_no` int(11) default NULL,                           
            `demographic_no` int(11) default NULL,                   
            `provider` varchar(60) default NULL,                     
            `date` timestamp NULL default NULL,                      
            `od_os_type` varchar(2) NOT NULL default '',             
            `specs_sph` varchar(10) default NULL,                    
            `specs_cyl` varchar(10) default NULL,                    
            `specs_axis` varchar(10) default NULL,                   
            `specs_add` varchar(10) default NULL,                    
            `specs_prism` varchar(10) default NULL,                  
            `ar_sph` varchar(10) default NULL,                       
            `ar_cyl` varchar(10) default NULL,                       
            `ar_axis` varchar(10) default NULL,                      
            `k1` varchar(10) default NULL,                           
            `k2` varchar(10) default NULL,                           
            `k2_axis` varchar(10) default NULL,                      
            `disdance_sc` varchar(16) default NULL,                  
            `distance_ph` varchar(16) default NULL,                  
            `distance_cc` varchar(16) default NULL,                  
            `near_sc` varchar(10) default NULL,                      
            `near_cc` varchar(10) default NULL,                      
            `manifest_refraction_sph` varchar(10) default NULL,      
            `manifest_refraction_cyl` varchar(10) default NULL,      
            `manifest_refraction_axis` varchar(10) default NULL,     
            `manifest_refraction_add` varchar(10) default NULL,      
            `cycloplegic_refraction_sph` varchar(10) default NULL,   
            `cycloplegic_refraction_cyl` varchar(10) default NULL,   
            `cycloplegic_refraction_axis` varchar(10) default NULL,  
            `cycloplegic_refraction_add` varchar(10) default NULL,   
            `disdance_m` varchar(16) default NULL,                   
            `disdance_mc` varchar(16) default NULL,                  
            `disdance_mn` varchar(16) default NULL,                  
            `iop_nct` varchar(4) default NULL,                       
            `iop_nct_time` timestamp NULL default NULL,              
            `iop_applanation` varchar(100) default NULL,             
            `iop_applanation_time` timestamp NULL default NULL,      
            `cct` varchar(10) default NULL,                          
            `color_vision_type` varchar(16) default NULL,            
            `color_vision_result` varchar(50) default NULL,          
            `pupil` varchar(100) default NULL,                       
            `amsler_grid` varchar(100) default NULL,                 
            `pam` varchar(16) default NULL,                          
            `confrontation_0_0` varchar(100) default NULL,           
            `confrontation_0_1` varchar(8) default NULL,             
            `confrontation_1_0` varchar(8) default NULL,             
            `confrontation_1_1` varchar(8) default NULL,             
            `eom_up` varchar(10) default NULL,                       
            `eom_down` varchar(10) default NULL,                     
            `eom_0_0` varchar(4) default NULL,                       
            `eom_0_1` varchar(4) default NULL,                       
            `eom_1_0` varchar(4) default NULL,                       
            `eom_1_1` varchar(4) default NULL,                       
            `eom_2_0` varchar(4) default NULL,                       
            `eom_2_1` varchar(4) default NULL,                       
            `eom_middel_0` varchar(16) default NULL,                 
            `eom_middel_1` varchar(16) default NULL,                 
            `eom_middel_2` varchar(16) default NULL,                 
            `eom_near_sc` varchar(16) default NULL,                  
            `eom_near_cc` varchar(16) default NULL,                  
            `eom_comment` varchar(100) default NULL,                 
            `cornea` varchar(100) default NULL,                      
            `conjunctiva_sclera` varchar(100) default NULL,          
            `anterior_chamber` varchar(100) default NULL,            
            `angle_up` varchar(4) default NULL,                      
            `angle_middle_0` varchar(4) default NULL,                
            `angle_middle_1` varchar(50) default NULL,               
            `angle_middle_2` varchar(4) default NULL,                
            `angle_down` varchar(4) default NULL,                    
            `iris` varchar(100) default NULL,                        
            `lens` varchar(100) default NULL,                        
            `anterior_vitreous` varchar(100) default NULL,           
            `disc` varchar(100) default NULL,                        
            `cd_ratio_vertical` varchar(50) default NULL,            
            `cd_ratio_horizontal` varchar(50) default NULL,          
            `macula` varchar(100) default NULL,                      
            `retina` varchar(100) default NULL,                      
            `vitreous` varchar(100) default NULL,                    
            `face` varchar(100) default NULL,                        
            `upper_lid` varchar(100) default NULL,                   
            `lower_lid` varchar(100) default NULL,                   
            `punctum` varchar(100) default NULL,                     
            `lacrimal_lake` varchar(100) default NULL,               
            `lacrimal_irrigation_normal` varchar(100) default NULL,  
            `lacrimal_irrigation_sup` varchar(8) default NULL,       
            `lacrimal_irrigation_inf` varchar(8) default NULL,       
            `nld` varchar(100) default NULL,                         
            `dye_disappearance` varchar(100) default NULL,           
            `mrd` varchar(10) default NULL,                          
            `levator_function` varchar(10) default NULL,             
            `inferior_scleral_show` varchar(10) default NULL,        
            `cn_vii` varchar(10) default NULL,                       
            `blink` varchar(10) default NULL,                        
            `bells` varchar(10) default NULL,                        
            `lagophthalmos` varchar(10) default NULL,                
            `hertel` varchar(10) default NULL,                       
            `retropulsion` varchar(10) default NULL,                 
            PRIMARY KEY  (`id`),                                     
            KEY `od_os_type` (`od_os_type`),                         
            KEY `demographic_no` (`demographic_no`),                 
            KEY `app_no` (`app_no`)                                  
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 drop table if exists eyeform;
 CREATE TABLE `eyeform` (                        
           `id` bigint(20) NOT NULL auto_increment,      
           `provider` varchar(60) default NULL,          
           `demographic_no` int(11) default NULL,        
           `echart_id` int(11) default NULL,             
           `appointment_no` int(11) default NULL,        
           `reason` text default NULL,            
           `ocular_history` text default NULL,   
           `current_history` text default NULL,  
           `impression` text default NULL,       
           `diagnostics` text default NULL,
           `followdoctor` varchar(6) default '',      
           `followup_discharge` int(11) default NULL,    
           `followup_stat` int(11) default NULL,
           `followup_opt` int(11) default NULL,          
           `followupno` varchar(10) default NULL,        
           `followupframe` varchar(10) default NULL,     
           `collapse_status` text,
           `white_status` text default NULL,
           `sign_provider` varchar(60) default NULL,     
           `date` timestamp NULL default NULL,
		   `nsticky_comment` text,                     
           `eye_med` text,
           PRIMARY KEY  (`id`),
           KEY `demographic_no` (`demographic_no`),
           KEY `appointment_no` (`appointment_no`)
         ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists ocularprocedurehis;
CREATE TABLE `ocularprocedurehis` (                  
                      `id` int(11) NOT NULL auto_increment,              
                      `demographic_no` int(11) NOT NULL default '0',     
                      `provider` varchar(60) default NULL,               
                      `date` date NOT NULL default '0000-00-00',         
                      `eye` varchar(2) NOT NULL default '',              
                      `procedure_name` varchar(100) NOT NULL default '',
                      `procedure_type` varchar(30) default NULL,         
                      `procedure_note` text,  
                      `doctor` varchar(30) default NULL,                 
                      `location` varchar(30) default NULL,               
                      `update_time` datetime default NULL,
					  `status` varchar(2) default 'A',
                      PRIMARY KEY  (`id`)                                
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
                    
drop table if exists procedurebook;
CREATE TABLE `procedurebook` (                
                 `id` int(11) NOT NULL auto_increment,       
                 `eyeform_id` bigint(20) default NULL,       
                 `demographic_no` int(11) default NULL,      
                 `appointment_no` int(11) default NULL,      
                 `provider` varchar(60) default NULL,        
                 `procedure_name` varchar(30) default NULL,  
                 `eye` varchar(20) default NULL,              
                 `location` varchar(50) default NULL,        
                 `urgency` varchar(10) default NULL,         
                 `comment` text default NULL,        
                 `date` datetime default NULL,
				 `status` varchar(2) default 'A',
                 PRIMARY KEY  (`id`)                         
               ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
               
drop table if exists procedure_contaract;
CREATE TABLE `procedure_contaract` (          
                       `id` int(11) NOT NULL auto_increment,       
                       `procedure_name` varchar(30) default NULL,  
                       `target_se` varchar(100) default NULL,      
                       `iol_planned` varchar(100) default NULL,    
                       `lri_planned` varchar(100) default NULL,    
                       `iol_actual` varchar(100) default NULL,     
                       `or_note` varchar(100) default NULL,        
                       `demographic_no` int(11) default NULL,      
                       `provider` varchar(60) default NULL,        
                       `date` timestamp NULL default NULL,         
                       PRIMARY KEY  (`id`)                         
                     ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
                     
drop table if exists procedure_laser;
CREATE TABLE `procedure_laser` (              
                   `id` int(11) NOT NULL auto_increment,       
                   `procedure_name` varchar(30) default NULL,  
                   `laser_type` varchar(100) default NULL,     
                   `energy` varchar(100) default NULL,         
                   `shots` varchar(100) default NULL,          
                   `spot_size` varchar(100) default NULL,      
                   `duration` varchar(100) default NULL,       
                   `demographic_no` int(11) default NULL,      
                   `provider` varchar(60) default NULL,        
                   `date` timestamp NULL default NULL,         
                   PRIMARY KEY  (`id`)                         
                 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
                 
drop table if exists procedure_mina;
CREATE TABLE `procedure_mina` (               
                  `id` int(11) NOT NULL auto_increment,       
                  `procedure_name` varchar(30) default NULL,  
                  `or_note` varchar(100) default NULL,        
                  `demographic_no` int(11) default NULL,      
                  `provider` varchar(60) default NULL,        
                  `date` timestamp NULL default NULL,         
                  PRIMARY KEY  (`id`)                         
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
                
drop table if exists rxglasses;
CREATE TABLE `rxglasses` (                        
             `id` int(11) NOT NULL auto_increment,           
             `demographic_no` int(11) NOT NULL default '0',  
             `provider` varchar(60) default NULL,            
             `date` date default NULL,                       
             `type` varchar(10) default NULL,                
             `od_sph` varchar(10) default NULL,              
             `od_cyl` varchar(10) default NULL,              
             `od_axis` varchar(10) default NULL,             
             `od_add` varchar(10) default NULL,              
             `od_prism` varchar(10) default NULL,            
             `os_sph` varchar(10) default NULL,              
             `os_cyl` varchar(10) default NULL,              
             `os_axis` varchar(10) default NULL,             
             `os_add` varchar(10) default NULL,              
             `os_prism` varchar(10) default NULL,            
             PRIMARY KEY  (`id`)                             
           ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
           
drop table if exists specshis;
CREATE TABLE `specshis` (                         
            `id` int(11) NOT NULL auto_increment,           
            `demographic_no` int(11) NOT NULL default '0',  
            `provider` varchar(60) default NULL,            
            `date` date default NULL,                       
            `doctor` varchar(30) default NULL,              
            `type` varchar(30) default NULL,                
            `od_sph` varchar(10) default NULL,              
            `od_cyl` varchar(10) default NULL,              
            `od_axis` varchar(10) default NULL,             
            `od_add` varchar(10) default NULL,              
            `od_prism` varchar(10) default NULL,            
            `os_sph` varchar(10) default NULL,              
            `os_cyl` varchar(10) default NULL,              
            `os_axis` varchar(10) default NULL,             
            `os_add` varchar(10) default NULL,              
            `os_prism` varchar(10) default NULL,            
            `update_time` datetime default NULL,
			`status` varchar(2) default 'A',
            PRIMARY KEY  (`id`)                             
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
          
          
drop table if exists test_a_scan;
CREATE TABLE `test_a_scan` (                  
               `id` int(11) NOT NULL auto_increment,       
               `eye_type` varchar(2) NOT NULL default '',  
               `demographic_no` int(11) default NULL,      
               `provider` varchar(60) default NULL,        
               `a_k1` varchar(50) default NULL,            
               `a_k2` varchar(50) default NULL,            
               `a_al` varchar(50) default NULL,            
               `date` timestamp NULL default NULL,         
               PRIMARY KEY  (`id`)                         
             ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
             
drop table if exists test_hrt;
CREATE TABLE `test_hrt` (                 
            `id` int(11) NOT NULL auto_increment,   
            `eye_type` varchar(2) default NULL,     
            `demographic_no` int(11) default NULL,  
            `provider` varchar(60) default NULL,    
            `rim_area` varchar(50) default NULL,    
            `rim_volume` varchar(50) default NULL,  
            `date` timestamp NULL default NULL,     
            PRIMARY KEY  (`id`)                     
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
          
drop table if exists test_hvf;
CREATE TABLE `test_hvf` (                 
            `id` int(11) NOT NULL auto_increment,   
            `eye_type` varchar(2) default NULL,     
            `demographic_no` int(11) default NULL,  
            `provider` varchar(60) default NULL,    
            `md` varchar(50) default NULL,          
            `psd` varchar(50) default NULL,         
            `date` timestamp NULL default NULL,     
            PRIMARY KEY  (`id`)                     
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
          
drop table if exists test_iol_master;
CREATE TABLE `test_iol_master` (          
                   `id` int(11) NOT NULL auto_increment,   
                   `eye_type` varchar(2) default NULL,     
                   `demographic_no` int(11) default NULL,  
                   `provider` varchar(60) default NULL,    
                   `i_k1` varchar(50) default NULL,        
                   `i_k2` varchar(50) default NULL,        
                   `i_al` varchar(50) default NULL,        
                   `date` timestamp NULL default NULL,     
                   PRIMARY KEY  (`id`)                     
                 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
                 
                 
drop table if exists test_oct;
CREATE TABLE `test_oct` (                 
            `id` int(11) NOT NULL auto_increment,   
            `eye_type` varchar(2) default NULL,     
            `demographic_no` int(11) default NULL,  
            `provider` varchar(60) default NULL,    
            `smax` varchar(50) default NULL,        
            `smin` varchar(50) default NULL,        
            `date` timestamp NULL default NULL,     
            PRIMARY KEY  (`id`)                     
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
          
          
drop table if exists test_pachy;
CREATE TABLE `test_pachy` (               
              `id` int(11) NOT NULL auto_increment,   
              `eye_type` varchar(2) default NULL,     
              `demographic_no` int(11) default NULL,  
              `provider` varchar(60) default NULL,    
              `up` varchar(50) default NULL,          
              `down` varchar(50) default NULL,        
              `left` varchar(50) default NULL,        
              `right` varchar(50) default NULL,       
              `date` timestamp NULL default NULL,     
              PRIMARY KEY  (`id`)                     
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists testbookrecord;            
CREATE TABLE `testbookrecord` (           
                  `id` int(11) NOT NULL auto_increment,   
                  `testname` varchar(60) default NULL,    
                  `appointment_no` int(11) default NULL,  
                  `demographic_no` int(11) default NULL,  
                  `provider` varchar(60) default NULL,    
                  `eyeform_id` bigint(20) default NULL,   
                  `eye` varchar(20) default NULL,          
                  `urgency` varchar(30) default NULL,     
                  `comment` varchar(100) default NULL,    
                  `date` datetime default NULL,
				  `status` varchar(2) default 'A',
                  PRIMARY KEY  (`id`)                     
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists consultationreport;               
CREATE TABLE `consultationreport` (                      
                        `date` date default NULL,                         
                        `request_id` int(10) NOT NULL auto_increment,                  
                        `referal_id` int(10) default NULL,
                        `appointmentNo` int(12) default 0,                           
                        `appointment_date` date default NULL,                     
                        `appointment_time` time default NULL, 
                        `cc_text` text,                    
                        `reason` text,                                           
                        `clinical_info` text,                                     
                        `current_meds` text,                                      
                        `allergies` text,                                        
                        `provider` varchar(60) default NULL,                    
                        `demographicNo` int(10) default NULL,                    
                        `status` char(2) default NULL,                           
                        `status_text` text,                                       
                        `sendto` varchar(20) default NULL,                       
                        `examination` text,             
                        `concurrentproblems` text,
                        `impression` text,
                        `plan` text,
                        `con_type` varchar(5) default "",
                        `con_memo` text default null,
                        `urgency` char(2) default NULL,                          
                        `patient_will_book` tinyint(1) default NULL,  
                        `greeting` int default '1',           
                        PRIMARY KEY  (`request_id`),
                      	KEY `date` (`date`)    
                      ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists consultationrequestcc;                     
CREATE TABLE `consultationrequestcc` (        
                         `request_id` int(10) NOT NULL default '0',  
                         `cc_text` text,                             
                         `special_text` text,                        
                         PRIMARY KEY  (`request_id`)                 
                       ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
                       
drop table if exists diagram_draw;                       
CREATE TABLE `diagram_draw` (             
                 `id` int(11) NOT NULL auto_increment,    
                 `eyeform_id` bigint(20) default NULL,    
                 `provider` varchar(60) default NULL,     
                 `appointment_no` int(11) default NULL,   
                 `demographic_no` int(11) default NULL,   
                 `name` varchar(50) NOT NULL default '',  
                 `description` varchar(50) default NULL,  
                 `document_id` int(11) default '0',       
                 `date` datetime default NULL,            
                 PRIMARY KEY  (`id`)                      
               ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists patient_log;
CREATE TABLE `patient_log` (              
               `id` int(11) NOT NULL auto_increment,   
               `creator` varchar(60) default NULL,     
               `demographic_no` int(11) default NULL,  
               `log` text,                             
               `split_flag` varchar(2) default NULL,   
               `update_date` datetime default NULL,    
               PRIMARY KEY  (`id`)                     
             ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists latest_eye_exam_field;
CREATE TABLE `latest_eye_exam_field` (                                  
            `id` int(11) NOT NULL auto_increment,                    
            `eyeform_id` bigint(11) default NULL,                    
            `app_no` int(11) default NULL,                           
            `demographic_no` int(11) default NULL,                   
            `provider` varchar(60) default NULL,                     
            `date` timestamp NULL default NULL,                      
            `od_os_type` varchar(2) NOT NULL default '',             
            `specs_sph` varchar(10) default NULL,                    
            `specs_cyl` varchar(10) default NULL,                    
            `specs_axis` varchar(10) default NULL,                   
            `specs_add` varchar(10) default NULL,                    
            `specs_prism` varchar(10) default NULL,                  
            `ar_sph` varchar(10) default NULL,                       
            `ar_cyl` varchar(10) default NULL,                       
            `ar_axis` varchar(10) default NULL,                      
            `k1` varchar(10) default NULL,                           
            `k2` varchar(10) default NULL,                           
            `k2_axis` varchar(10) default NULL,                      
            `disdance_sc` varchar(16) default NULL,                  
            `distance_ph` varchar(16) default NULL,                  
            `distance_cc` varchar(16) default NULL,                  
            `near_sc` varchar(10) default NULL,                      
            `near_cc` varchar(10) default NULL,                      
            `manifest_refraction_sph` varchar(10) default NULL,      
            `manifest_refraction_cyl` varchar(10) default NULL,      
            `manifest_refraction_axis` varchar(10) default NULL,     
            `manifest_refraction_add` varchar(10) default NULL,      
            `cycloplegic_refraction_sph` varchar(10) default NULL,   
            `cycloplegic_refraction_cyl` varchar(10) default NULL,   
            `cycloplegic_refraction_axis` varchar(10) default NULL,  
            `cycloplegic_refraction_add` varchar(10) default NULL,   
            `disdance_m` varchar(16) default NULL,                   
            `disdance_mc` varchar(16) default NULL,                  
            `disdance_mn` varchar(16) default NULL,                  
            `iop_nct` varchar(4) default NULL,                       
            `iop_nct_time` timestamp NULL default NULL,              
            `iop_applanation` varchar(100) default NULL,             
            `iop_applanation_time` timestamp NULL default NULL,      
            `cct` varchar(10) default NULL,                          
            `color_vision_type` varchar(16) default NULL,            
            `color_vision_result` varchar(50) default NULL,          
            `pupil` varchar(100) default NULL,                       
            `amsler_grid` varchar(100) default NULL,                 
            `pam` varchar(16) default NULL,                          
            `confrontation_0_0` varchar(100) default NULL,           
            `confrontation_0_1` varchar(8) default NULL,             
            `confrontation_1_0` varchar(8) default NULL,             
            `confrontation_1_1` varchar(8) default NULL,             
            `eom_up` varchar(10) default NULL,                       
            `eom_down` varchar(10) default NULL,                     
            `eom_0_0` varchar(4) default NULL,                       
            `eom_0_1` varchar(4) default NULL,                       
            `eom_1_0` varchar(4) default NULL,                       
            `eom_1_1` varchar(4) default NULL,                       
            `eom_2_0` varchar(4) default NULL,                       
            `eom_2_1` varchar(4) default NULL,                       
            `eom_middel_0` varchar(16) default NULL,                 
            `eom_middel_1` varchar(16) default NULL,                 
            `eom_middel_2` varchar(16) default NULL,                 
            `eom_near_sc` varchar(16) default NULL,                  
            `eom_near_cc` varchar(16) default NULL,                  
            `eom_comment` varchar(100) default NULL,                 
            `cornea` varchar(100) default NULL,                      
            `conjunctiva_sclera` varchar(100) default NULL,          
            `anterior_chamber` varchar(100) default NULL,            
            `angle_up` varchar(4) default NULL,                      
            `angle_middle_0` varchar(4) default NULL,                
            `angle_middle_1` varchar(50) default NULL,               
            `angle_middle_2` varchar(4) default NULL,                
            `angle_down` varchar(4) default NULL,                    
            `iris` varchar(100) default NULL,                        
            `lens` varchar(100) default NULL,                        
            `anterior_vitreous` varchar(100) default NULL,           
            `disc` varchar(100) default NULL,                        
            `cd_ratio_vertical` varchar(50) default NULL,            
            `cd_ratio_horizontal` varchar(50) default NULL,          
            `macula` varchar(100) default NULL,                      
            `retina` varchar(100) default NULL,                      
            `vitreous` varchar(100) default NULL,                    
            `face` varchar(100) default NULL,                        
            `upper_lid` varchar(100) default NULL,                   
            `lower_lid` varchar(100) default NULL,                   
            `punctum` varchar(100) default NULL,                     
            `lacrimal_lake` varchar(100) default NULL,               
            `lacrimal_irrigation_normal` varchar(100) default NULL,  
            `lacrimal_irrigation_sup` varchar(8) default NULL,       
            `lacrimal_irrigation_inf` varchar(8) default NULL,       
            `nld` varchar(100) default NULL,                         
            `dye_disappearance` varchar(100) default NULL,           
            `mrd` varchar(10) default NULL,                          
            `levator_function` varchar(10) default NULL,             
            `inferior_scleral_show` varchar(10) default NULL,        
            `cn_vii` varchar(10) default NULL,                       
            `blink` varchar(10) default NULL,                        
            `bells` varchar(10) default NULL,                        
            `lagophthalmos` varchar(10) default NULL,                
            `hertel` varchar(10) default NULL,                       
            `retropulsion` varchar(10) default NULL,                 
            PRIMARY KEY  (`id`),                                     
            KEY `od_os_type` (`od_os_type`),                         
            KEY `demographic_no` (`demographic_no`),                 
            KEY `app_no` (`app_no`)                                  
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
               
alter table appointment modify resources varchar(255) default NULL;
update encounterWindow set rowThreeSize=30;
alter table encounterWindow modify rowThreeSize int(10) NOT NULL default '30';