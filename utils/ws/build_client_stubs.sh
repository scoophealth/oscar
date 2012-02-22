#!/bin/sh

rm -rf ../../target/client_stubs
rm -rf ../../target/client_stubs_classes

for foo in `cat services.txt` 
do
	echo $foo
	ant -f build_client_stubs.xml -DserviceName=${foo}
done

ant -f build_client_stubs.xml compile_client_stubs

#rm -f ~/.m2/repository/org/oscarehr/client_stubs/SNAPSHOT/*.jar
#
#PROJECTS="oscar myoscar_client2 sample_myoscar_component"
#
#for foo in $PROJECTS;
#do
#   mvn install:install-file -DgroupId=org.oscarehr -DartifactId=oscar_server_client_stubs -Dversion=SNAPSHOT -Dpackaging=jar -Dfile=../../target/oscar_client_stubs.jar -DcreateChecksum=true -DgeneratePom=true -DlocalRepositoryPath=../../../${foo}/local_repo -DlocalRepositoryId=local_repo
#done;
