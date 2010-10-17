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

# integrator isn't in maven
mvn install:install-file -DgroupId=org.oscarehr -DartifactId=caisi_integrator_client -Dversion=0.0-SNAPSHOT -Dpackaging=jar -Dfile=../web/WEB-INF/lib/caisi_integrator_client.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo

# indivo isn't in maven
mvn install:install-file -DgroupId=org.indivo -DartifactId=indivo-client-java -Dversion=3.0-SNAPSHOT -Dpackaging=jar -Dfile=../web/WEB-INF/lib/indivo-client-java-3.0-SNAPSHOT.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo
mvn install:install-file -DgroupId=org.indivo -DartifactId=indivo-core -Dversion=3.0-SNAPSHOT -Dpackaging=jar -Dfile=../web/WEB-INF/lib/indivo-core-3.0-SNAPSHOT.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo
mvn install:install-file -DgroupId=org.indivo -DartifactId=indivo-model-core -Dversion=3.0-SNAPSHOT -Dpackaging=jar -Dfile=../web/WEB-INF/lib/indivo-model-core-3.0-SNAPSHOT.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo
mvn install:install-file -DgroupId=org.indivo -DartifactId=indivo-model-phr -Dversion=3.0-SNAPSHOT -Dpackaging=jar -Dfile=../web/WEB-INF/lib/indivo-model-phr-3.0-SNAPSHOT.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo
mvn install:install-file -DgroupId=org.indivo -DartifactId=indivo-protocol -Dversion=3.0-SNAPSHOT -Dpackaging=jar -Dfile=../web/WEB-INF/lib/indivo-protocol-3.0-SNAPSHOT.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo
mvn install:install-file -DgroupId=org.indivo -DartifactId=indivo-security-core -Dversion=3.2-SNAPSHOT -Dpackaging=jar -Dfile=../web/WEB-INF/lib/indivo-security-core-3.2-SNAPSHOT.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo
mvn install:install-file -DgroupId=org.indivo -DartifactId=indivo-security-model -Dversion=3.2-SNAPSHOT -Dpackaging=jar -Dfile=../web/WEB-INF/lib/indivo-security-model-3.2-SNAPSHOT.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo

# jcharts doesn't have right version in maven
mvn install:install-file -DgroupId=net.sf.jcharts -DartifactId=krysalis-jCharts -Dversion=0.7.5 -Dpackaging=jar -Dfile=../web/WEB-INF/lib/jCharts-0.7.5.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo

# cds isn't in maven
mvn install:install-file -DgroupId=cds -DartifactId=cds -Dversion=0.0-SNAPSHOT -Dpackaging=jar -Dfile=../web/WEB-INF/lib/cds.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo

# hsfo is not in maven
mvn install:install-file -DgroupId=hsfo -DartifactId=hsfo -Dversion=2007-02-12 -Dpackaging=jar -Dfile=../web/WEB-INF/lib/hsfo_2007-02-12.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo

# cookie revolver is not in maven
mvn install:install-file -DgroupId=net.sf.cookierevolver -DartifactId=cookierevolver -Dversion=0.2.5 -Dpackaging=jar -Dfile=../web/WEB-INF/lib/cookierev-0.2.5.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo

# crystal reports... tried to ask SAP if these were free/opensource/redistributable but they wouldn't answer with out me buying support, their loss 
mvn install:install-file -DgroupId=com.crystaldecisions -DartifactId=webreporting -Dversion=0.0-SNAPSHOT -Dpackaging=jar -Dfile=../web/WEB-INF/lib/webreporting.jar -DgeneratePom=true -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo
