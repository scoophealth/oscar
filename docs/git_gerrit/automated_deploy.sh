#!/bin/bash

#######
# Paths
#######

WORKING_ROOT=~/myoscar
SERVER_NAME=myoscar_server

SRC_DIR=${WORKING_ROOT}/git/${SERVER_NAME}

export JAVA_HOME=${WORKING_ROOT}/jdk1.6.0_23
PATH=${PATH}:${JAVA_HOME}/bin

export MAVEN_HOME=${WORKING_ROOT}/apache-maven-2.2.1
PATH=${PATH}:${MAVEN_HOME}/bin

export CATALINA_HOME=${WORKING_ROOT}/apache-tomcat-6.0.29
PATH=${PATH}:${CATALINA_HOME}/bin

export CATALINA_BASE=${WORKING_ROOT}/catalina_base

########
# Script
########

pushd ${SRC_DIR}

	# get latest code
	git pull
	
	# clear out local maven repo
	# Nah we don't need to do this, we're not hudson checking the build, we're just a deployment
	# mv ~/.m2/repository /tmp/`date --rfc-3339=seconds`.repo
	
	# build the code
	mvn clean install
	
	if [ $? = 0 ];
	then
	
		# stop tomcat, sleep to allow tomcat to shutdown before restarting
		catalina.sh stop
		sleep 60
		mv ${CATALINA_BASE}/logs/catalina.out ${CATALINA_BASE}/logs/catalina.out.`date -I` 
		
		# copy the built result to catalina base
		cd ${CATALINA_BASE}/webapps
		rm -rf ${SERVER_NAME}
		mkdir ${SERVER_NAME}
		cd ${SERVER_NAME}
		unzip ${SRC_DIR}/target/${SERVER_NAME}-3.1-SNAPSHOT.war
		
		# reset database
		cd ${SRC_DIR}
		mysql -e "drop database ${SERVER_NAME}"
		mysql -e "create database ${SERVER_NAME}"
		mysql ${SERVER_NAME} -e "source docs/freshIndivo.sql"
		
		# start tomcat
		catalina.sh start
		
	fi
	
popd 