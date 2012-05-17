@echo off
rem required parameters : %1=user %2=password %3=databasename

call ./createdatabase_generic.bat %* bc 9
SET USER=%1
SET PASSWORD=%2
SET DATABASE_NAME=%3
echo loading rourke2009_from_oscarinit_bc.sql...
mysql -u%USER% -p%PASSWORD% %DATABASE_NAME% < rourke2009_from_oscarinit_bc.sql
