#!/bin/sh

./createdatabase_generic.sh $@ bc 9

USER=$1
PASSWORD=$2
DATABASE_NAME=$3
echo loading rourke2009_from_oscarinit_bc.sql...
mysql -u${USER} -p$PASSWORD  $DATABASE_NAME  < rourke2009_from_oscarinit_bc.sql

