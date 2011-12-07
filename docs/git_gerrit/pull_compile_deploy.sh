#!/bin/bash

# source set_env.sh should be called before this script

#######
# Paths
#######

GIT_BASE=${WORKING_ROOT}/git

########
# Script
########

##############
# stop tomcat early so it has time to fiddle and shut down while we compile
##############
catalina.sh stop

#########################
# get latest server code
#########################
cd ${GIT_BASE}/myoscar_server2
git pull

mvn -Dmaven.test.skip=true -Dcheckstyle.skip=true -Dpmd.skip=true clean package

cd ${CATALINA_BASE}/webapps
rm -rf myoscar_server
mkdir myoscar_server
cd myoscar_server
unzip ${GIT_BASE}/myoscar_server2/target/myoscar_server-SNAPSHOT.war

########################
# get latest client code
########################
cd ${GIT_BASE}/myoscar_client2
git pull

mvn -Dmaven.test.skip=true -Dcheckstyle.skip=true -Dpmd.skip=true clean package

cd ${CATALINA_BASE}/webapps
rm -rf myoscar_client
mkdir myoscar_client
cd myoscar_client
unzip ${GIT_BASE}/myoscar_client2/target/myoscar_client-SNAPSHOT.war

##############
# start tomcat
##############
catalina.sh start
	