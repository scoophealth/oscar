@echo off
rem script expects 1 argument: the full path to oscar.properties file for db access
rem JRE has to be in the PATH
rem

set LIBPATH=../../../web/WEB-INF/lib

java -cp .;%LIBPATH%/commons-codec-1.3.jar;%LIBPATH%/hibernate3.jar;%LIBPATH%/mysql-connector-java-3.0.11-stable-bin.jar;%LIBPATH%/commons-logging-1.1.1.jar ImageStringToBlob %1%