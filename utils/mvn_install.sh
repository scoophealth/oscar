#!/bin/sh
mvn install:install-file -Dpackaging=jar -DcreateChecksum=true -DlocalRepositoryPath=mvn/local_repo -DlocalRepositoryId=local_repo -DgeneratePom=true $@