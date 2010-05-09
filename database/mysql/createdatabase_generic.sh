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

echo loading oscarinit.sql...
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscarinit.sql
echo loading oscarinit_${LOCATION}.sql...
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscarinit_${LOCATION}.sql
echo loading oscardata.sql...
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscardata.sql
echo loading oscardata_${LOCATION}.sql...
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscardata_${LOCATION}.sql
echo loading icd${ICD}.sql...
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < icd${ICD}.sql

echo changing to caisi directory...
cd caisi
echo loading initcaisi.sql...
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < initcaisi.sql
echo loading initcaisidata.sql...
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < initcaisidata.sql
echo changing back to the mysql directory...
cd ..

echo loading icd${ICD}_issue_groups.sql...
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < icd${ICD}_issue_groups.sql
echo loading measurementMapData.sql...
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < measurementMapData.sql
echo loading expire_oscardoc.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < expire_oscardoc.sql
echo all done!
echo the default user is oscardoc
echo password mac2002
echo pin 1117
echo For security reasons these credentials are set to expire in a month!
