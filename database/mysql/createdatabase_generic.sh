#!/bin/sh

##CREATE DATABASE

USER=$1
PASSWORD=$2
DATABASE_NAME=$3
LOCATION=$4

mysqladmin -u${USER} -p$PASSWORD create $DATABASE_NAME
echo "grant all on ${DATABASE_NAME}.* to ${USER}@localhost identified by \"$PASSWORD\"" |  mysql -u${USER} -p$PASSWORD  $DATABASE_NAME
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscarinit.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscarinit_${LOCATION}.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscardata.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < oscardata_${LOCATION}.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < icd9.sql

pushd caisi
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < initcaisi.sql
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < initcaisidata.sql
popd

mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < measurementMapData.sql
