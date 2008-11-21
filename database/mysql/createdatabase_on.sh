##CREATE DATABASE

PASSWORD=$1
DATABASE_NAME=$2

mysqladmin -uroot -p$PASSWORD create $DATABASE_NAME
echo "grant all on $DATABASE_NAME.* to root@localhost identified by \"$PASSWORD\"" |  mysql -uroot -p$PASSWORD  $DATABASE_NAME
mysql -uroot -p$PASSWORD  $DATABASE_NAME  < oscarinit.sql
mysql -uroot -p$PASSWORD  $DATABASE_NAME  < oscarinit_on.sql
mysql -uroot -p$PASSWORD  $DATABASE_NAME  < oscardata.sql
mysql -uroot -p$PASSWORD  $DATABASE_NAME  < oscardata_on.sql
mysql -uroot -p$PASSWORD  $DATABASE_NAME  < icd9.sql
mysql -uroot -p$PASSWORD  $DATABASE_NAME  < caisi/initcaisi.sql
mysql -uroot -p$PASSWORD  $DATABASE_NAME  < caisi/initcaisidata.sql
mysql -uroot -p$PASSWORD  $DATABASE_NAME  < measurementMapData.sql