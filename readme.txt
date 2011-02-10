======================
OSCAR McMaster Project
======================

------------
Requirements  
------------
These are not necessarily requirements but the version of software used by the author of this read me at the time of this writing. Generally speaking any newer version should work.
- mysql 5.1.52
- jdk1.6.0_23
- tomcat-6.0.29
- maven 2.2.1
- ant 1.8.1

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

To test jsp compilations "ant -f jspc.xml" can be run, it presumes you have just run "mvn package" and that the target directory and corresponding war file is present.

For developers, if you are doing testing and need to skip the junit test, pmd checks, and checkstyle checks, do "mvn -Dmaven.test.skip=true -Dcheckstyle.skip=true -Dpmd.skip=true package". You should always try to run it with full checks before committing though.

-------------
NetBeans Note
-------------
Add this to a netbeans build.xml file for it to build and run.

<target name="-post-compile">
        <echo message="deleting hbm.xml files from src directory." />
            <delete>
                <fileset dir="${build.classes.dir}/src/" includes="**/*.hbm.xml"/>
            </delete>
</target>
    
