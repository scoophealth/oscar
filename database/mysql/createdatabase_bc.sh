#!/bin/sh
if [ "$#" -ne 3 ]; then
        echo "Usage: ./createdatabase_on.sh [database user] [database password] [database name]"
        exit
fi
./createdatabase_generic.sh $@ bc 9
echo "Loading rourke2009_from_oscarinit_bc.sql..."
mysql -u$1 -p$2  $3  < rourke2009_from_oscarinit_bc.sql
