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
	git fetch origin
	git merge origin
	
	# clear out local maven repo
	# Nah we don't need to do this, we're not hudson checking the build, we're just a deployment
	# mv ~/.m2/repository /tmp/`date --rfc-3339=seconds`.repo
	
	# build the code
	mvn clean install
	
	# copy the built result to catalina base
	cd ${CATALINA_BASE}
	rm -rf ${SERVER_NAME}
	mkdir ${SERVER_NAME}
	cd ${SERVER_NAME}
	unzip ${SRC_DIR}/${SERVER_NAME}-0.0-SNAPSHOT.war
	
	# stop tomcat
	catalina.sh stop
	
	# reset database
	mysql -e "drop database ${SERVER_NAME}"
	mysql -e "create database ${SERVER_NAME}"
	mysql -e "source ${SRC_DIR}/docs/freshIndivo.sql"
	
	# start tomcat
	catalina.sh start
	
popd 