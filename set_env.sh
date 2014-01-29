#!/bin/sh

# This script is used to setup the config to run the server / tomcat.

#export JAVA_HOME=${APP_ROOT}/openjdk1.7.0.3/jvm/java-1.7.0-openjdk-1.7.0.3.x86_64
#export PATH=${JAVA_HOME}/bin:${PATH}

#export JAVA_HOME=/usr/lib/jvm/java/
export WORKING_ROOT=`pwd`

export CATALINA_BASE=${WORKING_ROOT}/catalina_base

MEM_SETTINGS="-Xms640m -Xmx640m -Xss640k -XX:MaxNewSize=64m -XX:MaxPermSize=256m "
export JAVA_OPTS="-Djava.awt.headless=true -server -Xincgc -Dorg.apache.el.parser.COERCE_TO_ZERO=false -Dorg.apache.commons.logging.Log=org.apache.cxf.common.logging.Log4jLogger -Dorg.apache.cxf.Logger=org.apache.cxf.common.logging.Log4jLogger -Dlog4j.override.configuration=${WORKING_ROOT}/override_log4j.xml -Doscar_override_properties=${WORKING_ROOT}/oscar.properties "${MEM_SETTINGS}

export MAVEN_OPTS="-Doscar_override_properties=${WORKING_ROOT}/oscar.properties"
