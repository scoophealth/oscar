#Increases the eChart table max size from 4GB (default for mysql 4.1) to 281TB
#Note: the maximum allowable file size for the ext3 file system is 4TB

alter table eChart max_rows=200000000 AVG_ROW_LENGTH=9000;