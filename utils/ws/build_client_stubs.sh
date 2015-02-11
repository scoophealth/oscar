#!/bin/sh

rm -rf ../../target/client_stubs
rm -rf ../../target/client_stubs_classes

for foo in `cat services.txt` 
do
	echo $foo
	ant -f build_client_stubs.xml -DserviceName=${foo}
done

ant -f build_client_stubs.xml compile_client_stubs

VERSION=2015.01.28


mvn install:install-file -DgroupId=org.oscarehr.oscar -DartifactId=oscar_client_stubs -Dversion=${VERSION} -Dpackaging=jar -Dfile=../../target/oscar_client_stubs.jar -DcreateChecksum=true -DgeneratePom=true
mvn install:install-file -DgroupId=org.oscarehr.oscar -DartifactId=oscar_client_stubs -Dversion=${VERSION} -Dpackaging=jar -Dfile=../../target/oscar_client_stubs.jar -DcreateChecksum=true -DgeneratePom=true -DlocalRepositoryPath=../../../oscar_maven_repo/local_repo -DlocalRepositoryId=local_repo
