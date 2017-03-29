#!/bin/sh

# This script is used to setup the config to run the server / tomcat.
# You will need to have set your JAVA_HOME and MAVEN_ROOT prior to running this 

export WORKING_ROOT=`pwd`

export CATALINA_BASE=${WORKING_ROOT}/catalina_base

MEM_SETTINGS="-Xms640m -Xmx640m -Xss512k -XX:NewRatio=4 -XX:MaxPermSize=256m "
export JAVA_OPTS="-Djava.awt.headless=true -server -Xincgc -Dorg.apache.el.parser.COERCE_TO_ZERO=false -Dorg.apache.commons.logging.Log=org.apache.cxf.common.logging.Log4jLogger -Dorg.apache.cxf.Logger=org.apache.cxf.common.logging.Log4jLogger -Dlog4j.override.configuration=${WORKING_ROOT}/override_log4j.xml -Doscar_override_properties=${WORKING_ROOT}/oscar.properties ${MEM_SETTINGS}"

export MAVEN_OPTS="-Doscar_override_properties=${WORKING_ROOT}/oscar.properties ${MEM_SETTINGS}"

