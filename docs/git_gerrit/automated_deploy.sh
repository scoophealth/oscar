#!/bin/bash

#####
Paths
#####
#WORKING_ROOT=/opt/git/integrator
WORKING_ROOT=/tmp/mog/integrator

######
Script
######

pushd ${WORKING_ROOT}

	# get latest code
	git fetch origin
	git merge origin
	
	# clear out local maven repo
	mv ~/.m2/repository /tmp/`date --rfc-3339=seconds`.repo
	
	# build the code
	mvn clean install
	
	# copy the built result to catalina base
	
	# restart tomcat
	
popd 