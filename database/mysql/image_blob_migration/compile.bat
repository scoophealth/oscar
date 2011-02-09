@echo off
set LIBPATH=../../../web/WEB-INF/lib

javac -classpath .;%LIBPATH%/commons-codec-1.3.jar;%LIBPATH%/hibernate3.jar ImageStringToBlob.java