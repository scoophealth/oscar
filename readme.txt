======================
OSCAR McMaster Project
======================

Note: this readme is now out of date, and marked for change. For the latest documentation, visit https://oscaremr.atlassian.net/wiki/display/OS


------------
Requirements  
------------
These are not necessarily requirements but the version of software used by the author of this read me at the time of this writing. Generally speaking any newer version should work.
- mysql 5.5/5.6
- jdk1.7
- tomcat 7
- maven 3.3.x

-----------
Directories
-----------
catalina_base : is a catalina_base that's provided, it can be useful as a starting point for deployments or development.
database : contains sql scripts to initialise the database schema.
docs : developer style documentation
local_repo : a maven repository for libraries not found at the publicly accessible maven repositories.
src : the source code for this project, structured in a standard maven structure.
utils : some random utilities and files we don't have anywhere else to put.

--------
Building 
--------
This is a standard maven project. "mvn package" should create a target directory and there should be a war file in there.

To test jsp compilations, as well as run pmd, run "mvn verify". You will need your CATALINA_HOME environment variable set

For developers, if you are doing testing and need to skip the junit test, pmd checks, and checkstyle checks, do "mvn -Dmaven.test.skip=true -Dcheckstyle.skip=true -Dpmd.skip=true package". You should always try to run it with full checks before committing though.


-------
Tests
------
If you run the unit tests, maven needs a live MySQL database to load the schema, and test data into, as well as to perform the checks.
The defaults are a database named 'oscar_test', and full credentials to it for user 'oscar' and password 'oscar'. You can override
these properties if they don't suit you. The properties are available in src/test/resources/over_ride_config.properties. You can make your
own file and run maven with -Doscar_override_properties=/<full_path>/myoverrides.properties and those values will take prescendence.

-------------
NetBeans Note
-------------
If you are running the unit tests, and using your own override properties file in Netbeans, use 
-DargLine="-Doscar_override_properties=/<full_path>/myoverrides.properties"


Add this to a netbeans build.xml file for it to build and run.

<target name="-post-compile">
        <echo message="deleting hbm.xml files from src directory." />
            <delete>
                <fileset dir="${build.classes.dir}/src/" includes="**/*.hbm.xml"/>
            </delete>
</target>
    

    
    
