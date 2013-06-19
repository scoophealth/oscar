# OSCAR - MyOSCAR - integration testing #

## OSCAR 12.1 Release download ##

Get the final 12.1 release from Jenkins and/or SourceForge.  The location on Jenkins is located here.

https://demo.oscarmcmaster.org:11042/job/oscarMaster121/


### OSCAR - Install and setup ###

You can find the updated installation/upgrade notes for OSCAR on the www.oscarmanual.org site located at :

http://www.oscarmanual.org/oscar_emr_12/developers/installation/updating-oscar

If you have already installed OSCAR, then you can use the following steps below to reinitialize the db to a fresh clean base install.  (default user:oscardoc / pass:mac2002 / pin:1117)

1. Make sure the user oscar has DB privileges 
2. Run createdatabase_on.sh for OSCAR to setup a fresh clean database (only oscardoc will exist)
3. Run MySQL updates for the DB (located in /workspace/oscar/database/mysql/updates) if there have been DB changes since the creation of the DB
4. Run update-phr-docs.sql in the /mysql/updates folder.
5. Make sure that your instance of OSCAR you are testing is pointing to the correct DB (oscar.properties)


## MyOSCAR release download ##

Get the latest release (database and .war) for MyOSCAR from SourceForge.  The location on SourceForge is location here.

https://sourceforge.net/projects/myoscar/files/myoscar2/


### MYOSCAR - Install and setup ###

You can find the updated installation/upgrade notes for MyOSCAR on the www.oscarmanual.org site located at : 

http://www.oscarmanual.org/myoscar-phr/developers/installing-myoscar2/myoscar2-install

If you have already installed MyOSCAR, then you can use the following steps below to reinitialize the db to a fresh clean base install

1. use ./reset_test_data.sh in myoscar_server2 to setup a fresh clean database.  Make sure to run in the SAME SESSION first " export JAVA_HOME=/usr/lib/jvm/java-7-oracle "
or
1. Drop the schema (MyOSCAR database)
2. Run the startup script to start MyOSCAR (aaron -> deploy-myoscar.eclipse.sh)
3. Navigate to http://localhost:8090/myoscar_server/admin/install.jsp to initialize the DB


