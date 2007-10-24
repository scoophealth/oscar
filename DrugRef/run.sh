#!/bin/bash

echo "Please specify the ATC codes (at least two which are separated by spaces): "
read CODES 

if [ ! -f ./build/testClient.class ]; then
	mkdir build
	javac -cp "./src/:./lib/xmlrpc-1.2-b1.jar" -d "./build/" ./src/testClient.java
	
fi

java -cp ".:./build/:./lib/xmlrpc-1.2-b1.jar" testClient $CODES

