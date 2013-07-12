# OSCAR - MyOSCAR - integration testing #

## General Setup Information ##

Services/systems needed

1. OSCAR 
2. MyOSCAR server
3. MyOSCAR client

All three services/systems need to be configured and running in order for the testing to start.  Please use oscar, myoscar_server and myoscar_client as commited and configured in git.


### OSCAR - Install and setup ###

You can find the updated installation/upgrade notes for OSCAR on the www.oscarmanual.org site located at :

http://www.oscarmanual.org/oscar_emr_12/developers/installation/updating-oscar

You can use the following steps below to reinitialize the db to a fresh clean base install.  (default user:oscardoc / pass:mac2002 / pin:1117)

1. Grant the user ' oscar ' DB privileges on the database
2. Run createdatabase_on.sh to reset the database
3. Run update-phr-docs.sql in the /mysql/updates folder.


### MYOSCAR - Install and setup ###

You can find the updated installation/upgrade notes for MyOSCAR server on the www.oscarmanual.org site located at : 

http://www.oscarmanual.org/myoscar-phr/developers/installing-myoscar2/myoscar2-install

You can use the following steps below to reinitialize the db to a fresh clean base install

1. Use reset_test_data.sh in myoscar_server2 to setup a fresh clean database.


