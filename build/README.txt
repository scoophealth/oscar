OSCAR McMaster cvs

This folder contains the necessary ant scripts to build the distribution version of the software.

If you are not having an IDE supply the build file for you
overwrite the build.xml with the buildTomcatXX.xml corresponding to your version

Add this to a netbeans build.xml file for it to build and run.


<target name="-post-compile">
        <echo message="deleting hbm.xml files from src directory." />
            <delete>
                <fileset dir="${build.classes.dir}/src/" includes="**/*.hbm.xml"/>
            </delete>
</target>
    
