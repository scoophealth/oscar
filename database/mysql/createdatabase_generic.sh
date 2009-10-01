#!/bin/sh

##CREATE DATABASE

USER=$1
PASSWORD=$2
DATABASE_NAME=$3

# should be "on" or "bc" corresponding to the oscarinit_XX.sql XX qualifier
LOCATION=$4

# should be "9" or "10" corresponding to the icdXX.sql qualifier
ICD=$5

mysqladmin -u${USER} -p$PASSWORD create $DATABASE_NAME
echo "grant all on ${DATABASE_NAME}.* to ${USER}@localhost identified by \"$PASSWORD\"" |  mysql -u${USER} -p$PASSWORD  $DATABASE_NAME

mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscarinit.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscarinit_${LOCATION}.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscardata.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscardata_${LOCATION}.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < icd${ICD}.sql

cd caisi
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < initcaisi.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < initcaisidata.sql
cd ..

mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < icd${ICD}_issue_groups.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < measurementMapData.sql
