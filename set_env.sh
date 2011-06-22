#!/bin/sh

# This script is used to setup the config to run the server / tomcat.

export WORKING_ROOT=`pwd`

export CATALINA_BASE=${WORKING_ROOT}/catalina_base

MEM_SETTINGS="-Xms512m -Xmx512m -Xss128k -XX:MaxNewSize=64m -XX:MaxPermSize=256m "
export JAVA_OPTS="-Djava.awt.headless=true -server -XX:+UseConcMarkSweepGC -Dcom.sun.management.jmxremote -Dorg.apache.cxf.Logger=org.apache.cxf.common.logging.Log4jLogger -Dlog4j.override.configuration=${WORKING_ROOT}/override_log4j.xml "${MEM_SETTINGS}

