OSCAR McMaster cvs

This folder contains the install instructions and licence terms of OSCAR software.

If running in netbeans add this to the bottom of the build.xml file

<target name="-post-compile">
        <echo message="deleting hbm.xml files from src directory." />
            <delete>
                <fileset dir="${build.classes.dir}/src/" includes="**/*.hbm.xml"/>
            </delete>
    </target>

