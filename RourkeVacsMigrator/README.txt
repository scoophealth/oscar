Rourke Vaccination Migration Utility
Date March 24, 2005 
Author:Joel Legris
Email:joel.legris@egadss.org 

--------------------------------------------------------------------------------------------------------------
Description
--------------------------------------------------------------------------------------------------------------

This utility migrates immunization flag data from the formrourke table in OSCAR 2.0
Each Immunization field is associated with a corresponding free text field as well 
as a textual description.If an immunization field contains a value of "1", a line feed 
as well as its correspondening textual description is appended to the corresponding free text field, 
in the formrourke table. It is important to note that although the immunization data will be migrated to
the associated free text fields, the existing data will still remain in the original database fields.


***WARNING: Make sure you that you perform a couple of tests on a test database, this is alpha software!***


--------------------------------------------------------------------------------------------------------------
Program Files
--------------------------------------------------------------------------------------------------------------

RourkeVacsMigrator.py(written in Jython)
RourkeMig.jar
db.properties


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

		jython RourkeVacsMigrator.py

	2. Execute jar file as follows(If Jython interpreter is not installed):

		java -jar RourkeVacsMigrator.jar


--------------------------------------------------------------------------------------------------------------
Follow Up
--------------------------------------------------------------------------------------------------------------

Upon execution of the utility, a log file called rourketransfer.log is created, to aid you in assessing the the results of the migration. 

An example of the log file is as follows:

IMPORT STARTED@Tue Mar 22 23:53:39 PST 2005
UPDATED RECORD#: 113 APPENDED: Immunization TO FIELD: p1_immunization1m
UPDATED RECORD#: 114 APPENDED: Immunization TO FIELD: p1_immunization1m
UPDATED RECORD#: 114 APPENDED: Acetaminophen TO FIELD: p1_immunization1m
UPDATED RECORD#: 114 APPENDED: Hep. B vaccine TO FIELD: p1_immunization1m
UPDATED RECORD#: 114 APPENDED: MMR TO FIELD: p3_immunization4y
UPDATED RECORD#: 114 APPENDED: aPDT polio TO FIELD: p3_immunization4y






