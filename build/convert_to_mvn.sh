#!/bin/sh

mkdir -p mvn
cp pom.xml mvn
cp ant_pom_tasks.xml mvn
cp maven_jspc.xml mvn

main_dir=mvn/src/main
test_dir=mvn/src/test

# make source directories
rm -rf ${main_dir}
mkdir -p ${main_dir}/webapp
mkdir -p ${main_dir}/java
mkdir -p ${main_dir}/resources

# make test directories
mkdir -p ${test_dir}/java
mkdir -p ${test_dir}/resources

# copy web code over
cp -r ../web/* ${main_dir}/webapp
rm -rf ${main_dir}/webapp/WEB-INF/*
cp ../web/WEB-INF/* ${main_dir}/webapp/WEB-INF

# copy java code over
cp -r ../web/WEB-INF/classes/src/{*.java,com,filters,net,org,oscar} ${main_dir}/java

# copy resources over 
cp ../web/WEB-INF/classes/{*.properties,*.xml,src/*.properties} ${main_dir}/resources
pushd ../web/WEB-INF/classes/src/
zip -qr /tmp/xml.zip {com,oscar,org,META-INF} -i \*.xml \*.drl
popd
pushd ${main_dir}/resources
unzip -q /tmp/xml.zip
popd

# get rid of cvs files
zip -qrm /tmp/cvs_dirs.zip mvn -i \*/CVS/\*

# make local repo and add libs
mkdir -p mvn/local_repo

# integrator isn't in maven
./mvn_install.sh -DgroupId=org.oscarehr -DartifactId=caisi_integrator_client -Dversion=0.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/caisi_integrator_client.jar

# indivo isn't in maven
./mvn_install.sh -DgroupId=org.indivo -DartifactId=indivo-client-java -Dversion=3.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/indivo-client-java-3.0-SNAPSHOT.jar
./mvn_install.sh -DgroupId=org.indivo -DartifactId=indivo-core -Dversion=3.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/indivo-core-3.0-SNAPSHOT.jar
./mvn_install.sh -DgroupId=org.indivo -DartifactId=indivo-model-core -Dversion=3.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/indivo-model-core-3.0-SNAPSHOT.jar
./mvn_install.sh -DgroupId=org.indivo -DartifactId=indivo-model-phr -Dversion=3.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/indivo-model-phr-3.0-SNAPSHOT.jar
./mvn_install.sh -DgroupId=org.indivo -DartifactId=indivo-protocol -Dversion=3.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/indivo-protocol-3.0-SNAPSHOT.jar
./mvn_install.sh -DgroupId=org.indivo -DartifactId=indivo-security-core -Dversion=3.2-SNAPSHOT -Dfile=../web/WEB-INF/lib/indivo-security-core-3.2-SNAPSHOT.jar
./mvn_install.sh -DgroupId=org.indivo -DartifactId=indivo-security-model -Dversion=3.2-SNAPSHOT -Dfile=../web/WEB-INF/lib/indivo-security-model-3.2-SNAPSHOT.jar

# jcharts doesn't have right version in maven
./mvn_install.sh -DgroupId=net.sf.jcharts -DartifactId=krysalis-jCharts -Dversion=0.7.5 -Dfile=../web/WEB-INF/lib/jCharts-0.7.5.jar

# cds isn't in maven
./mvn_install.sh -DgroupId=cds -DartifactId=cds -Dversion=0.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/cds.jar

# hsfo is not in maven
./mvn_install.sh -DgroupId=hsfo -DartifactId=hsfo -Dversion=2007-02-12 -Dfile=../web/WEB-INF/lib/hsfo_2007-02-12.jar

# cookie revolver is not in maven
./mvn_install.sh -DgroupId=net.sf.cookierevolver -DartifactId=cookierevolver -Dversion=0.2.5 -Dfile=../web/WEB-INF/lib/cookierev-0.2.5.jar

# dm.jar I suspect this shouldn't even be in a jar like this ... but we'll deal with it later, I think it's generated classes
./mvn_install.sh -DgroupId=dm -DartifactId=dm -Dversion=0.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/dm.jar

# patientSiteVisit.jar I suspect this shouldn't even be in a jar like this ... but we'll deal with it later, I think it's generated classes
./mvn_install.sh -DgroupId=patientSiteVisit -DartifactId=patientSiteVisit -Dversion=0.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/patientSiteVisit.jar

# hsfo is not in maven
./mvn_install.sh -DgroupId=javax.xml -DartifactId=jaxm-api -Dversion=UNKNOWN -Dfile=../web/WEB-INF/lib/jaxm-api.jar

# surveyModel.jar I suspect this shouldn't even be in a jar like this ... but we'll deal with it later, I think it's generated classes
./mvn_install.sh -DgroupId=surveyModel -DartifactId=surveyModel -Dversion=0.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/surveyModel.jar

# ostermillerutils_1_04_03_for_java_1_4.jar, we need to stop using this apache commons replaces this
./mvn_install.sh -DgroupId=com.ostermiller -DartifactId=ostermillerutils -Dversion=1.4.3 -Dfile=../web/WEB-INF/lib/ostermillerutils_1_04_03_for_java_1_4.jar

# sunxacml-1.2.jar
./mvn_install.sh -DgroupId=com.sun -DartifactId=xacml -Dversion=1.2 -Dfile=../web/WEB-INF/lib/sunxacml-1.2.jar

# PDFRenderer.jar
./mvn_install.sh -DgroupId=com.sun -DartifactId=pdfview -Dversion=UNKNOWN -Dfile=../web/WEB-INF/lib/PDFRenderer.jar

# ocan.jar I suspect this shouldn't even be in a jar like this ... but we'll deal with it later, I think it's generated classes
./mvn_install.sh -DgroupId=ocan -DartifactId=ocan -Dversion=0.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/ocan.jar

# zxing libraries aren't in maven
./mvn_install.sh -DgroupId=zxing -DartifactId=zxing-core -Dversion=1.5 -Dfile=../web/WEB-INF/lib/zxing_core_15.jar
./mvn_install.sh -DgroupId=zxing -DartifactId=zxing-j2se -Dversion=1.5 -Dfile=../web/WEB-INF/lib/zxing_javase_15.jar

# chip ping libraries
./mvn_install.sh -DgroupId=org.chip.ping -DartifactId=oscar-ping -Dversion=UNKNOWN -Dfile=../web/WEB-INF/lib/oscar-ping.jar
./mvn_install.sh -DgroupId=org.chip.ping -DartifactId=ping-client -Dversion=UNKNOWN -Dfile=../web/WEB-INF/lib/ping-client.jar
./mvn_install.sh -DgroupId=org.chip.ping -DartifactId=ping-core -Dversion=UNKNOWN -Dfile=../web/WEB-INF/lib/ping-core.jar
./mvn_install.sh -DgroupId=org.chip.ping -DartifactId=ping-server -Dversion=UNKNOWN -Dfile=../web/WEB-INF/lib/ping-server.jar
./mvn_install.sh -DgroupId=org.chip.ping -DartifactId=ping-xml -Dversion=UNKNOWN -Dfile=../web/WEB-INF/lib/ping-xml.jar

# TrackingBasicDataSource get rid of this once we fix all jdbc stuff
./mvn_install.sh -DgroupId=TrackingBasicDataSource -DartifactId=TrackingBasicDataSource -Dversion=0.0-SNAPSHOT -Dfile=../web/WEB-INF/lib/tracking_basic_datasource.jar

# our barbecue renderer is too old to be in maven
./mvn_install.sh -DgroupId=net.sourceforge.barbecue -DartifactId=barbecue -Dversion=1.0.6b -Dfile=../web/WEB-INF/lib/barbecue-1.0.6b.jar

# our struts-el is too old to be in maven
./mvn_install.sh -DgroupId=org.apache.struts -DartifactId=struts-el -Dversion=REALLY_OLD -Dfile=../web/WEB-INF/lib/struts-el.jar

# our struts-menu is not what it claims to be, it's some unknown or altered version
./mvn_install.sh -DgroupId=org.apache.struts -DartifactId=struts-menu -Dversion=UNKNOWN -Dfile=../web/WEB-INF/lib/struts-menu-2.4.3.jar

# plugin-framework... oh boy... we can't get rid of this quick enough
./mvn_install.sh -DgroupId=pluginframework -DartifactId=pluginframework -Dversion=0.9.13 -Dfile=../web/WEB-INF/lib/pluginframework-0.9.13.jar

# standard.jar ... no comment, refactoring needed in the future
./mvn_install.sh -DgroupId=standard -DartifactId=standard -Dversion=UNKNOWN -Dfile=../web/WEB-INF/lib/standard.jar
