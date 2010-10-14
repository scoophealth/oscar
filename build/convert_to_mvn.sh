#!/bin/sh

rm -rf mvn
mkdir mvn
cp pom.xml mvn

main_dir=mvn/src/main
test_dir=mvn/src/test

# make source directories
mkdir -p ${main_dir}/webapp
mkdir -p ${main_dir}/java
mkdir -p ${main_dir}/resources

# make test directories
mkdir -p ${test_dir}/java
mkdir -p ${test_dir}/resources

# copy web code over
cp -r ../web ${main_dir}/webapp
rm -rf ${main_dir}/webapp/WEB-INF/*
cp -r ../web/WEB-INF/web.xml ${main_dir}/webapp/WEB-INF

# copy java code over
cp -r ../web/WEB-INF/classes/src/{*.java,com,filters,net,org,oscar} ${main_dir}/java

# copy resources over 
cp -r ../web/WEB-INF/classes/{*.properties,*.xml,src/*.properties} ${main_dir}/resources

# get rid of cvs files
zip -rm cvs.zip mvn -i \*/CVS/\*
rm cvs.zip

# make local repo and add libs
mkdir mvn/local_repo
mvn install:install-file -DgroupId=org.oscarehr -DartifactId=caisi_integrator_client -Dversion=0.0-SNAPSHOT -Dpackaging=jar -Dfile=../web/WEB-INF/lib/caisi_integrator_client.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo
