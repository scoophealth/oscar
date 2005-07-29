BCMA Update Utility
Date: July 19, 2005 
Author:Joel Legris
Email:joel.legris@egadss.org 

--------------------------------------------------------------------------------------------------------------
Description
--------------------------------------------------------------------------------------------------------------

This script updates the billingservice table with bcma codes/fees that reside in a pipe delimited file with the following structure:

	code|description|msp fee|bcma fee

Script Logic:

The script performs the update according to the following logic:

	- if a bcma fee exists, update its fee
	- else create new bcma/private fee code entries 

***WARNING: Make sure you that you perform a couple of tests on a test database, this is alpha software!***


--------------------------------------------------------------------------------------------------------------
Program Files
--------------------------------------------------------------------------------------------------------------

bcma_updater.py(written in Jython)
bcma_updater.jar
bcmafees_2004.04.01.csv


--------------------------------------------------------------------------------------------------------------
Dependencies
--------------------------------------------------------------------------------------------------------------

In order to execute the script, ensure that the following library and interpreter dependencies are satisfied:

Jython 2.1 interpreter

and/or

Java 1.4.2 JRE/SDK

--------------------------------------------------------------------------------------------------------------
Installation
--------------------------------------------------------------------------------------------------------------

Place the utility files in the directory of choice.


--------------------------------------------------------------------------------------------------------------
Execution
--------------------------------------------------------------------------------------------------------------

In order for the program to succesfully execute, ensure that the db.properties file exists in the same directory(configured with the appropriate db parameters).
A sample configuration is commented in the included file. 

There is a choice of two methods to execute the program.

	1. Execute Jython script on command line as follows(Jython interpreter must be installed):

		jython bcma_updater.py

	2. Execute jar file as follows(If Jython interpreter is not installed):

		java -jar bcma_updater.jar







