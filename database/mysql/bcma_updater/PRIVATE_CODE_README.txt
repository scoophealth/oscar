BCMA Update Utility
Date: November 22, 2005 
Author:Joel Legris
Email:joel.legris@egadss.org 

--------------------------------------------------------------------------------------------------------------
Description
--------------------------------------------------------------------------------------------------------------

This script updates the billingservice table by prepending a private code with 'A' 
rather than 'P'

--------------------------------------------------------------------------------------------------------------
Program Files
--------------------------------------------------------------------------------------------------------------

privatecode_updater.py(written in Jython)
privatecode_updater.jar

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

		jython privatecode_updater.py

	2. Execute jar file as follows(If Jython interpreter is not installed):

		java -jar privatecode_updater.jar
		
		








