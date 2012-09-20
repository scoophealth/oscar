--
-- Table structure for table `formCounseling`
--
CREATE TABLE `formCounseling` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `provider_no` int(10),    
  `doc_name` varchar(60),
  `cl_name` varchar(60),
  `cl_address1` varchar(170),
  `cl_address2` varchar(170),
  `cl_phone` varchar(16),
  `cl_fax` varchar(16),
  `billingreferral_no` int(10),
  `t_name` varchar(60),
  `t_name2` varchar(60),
  `t_address1` varchar(170),
  `t_address2` varchar(170),
  `t_phone` varchar(16),
  `t_fax` varchar(16),
  `demographic_no` int(10) NOT NULL,  
  `p_name` varchar(60),
  `p_address1` varchar(170),
  `p_address2` varchar(170),
  `p_phone` varchar(16),
  `p_birthdate` varchar(30),
  `p_healthcard` varchar(20),  
  `comments` text,
  `formCreated` date,
  `formEdited` timestamp,
  `consultTime` date,  
  PRIMARY KEY (`ID`)
);
