CREATE TABLE `dx_associations` (
	`id` int primary key auto_increment,
	`dx_codetype` varchar(50) not null,
	`dx_code` varchar(50) not null,
	`codetype` varchar(50) not null,
	`code` varchar(50) not null,
	`update_date` timestamp not null
);
