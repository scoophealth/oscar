Rourke Vaccination Migration Script:

Description:

This script migrates immunization flag data from the formrourke table in OSCAR 2.0
Each Immunization field is associated with a corresponding free text field as well 
as a textual description.If an immunization field contains a value of "1", a line feed 
as well as its correspondening textual description is appended to the corresponding free text field, 
in the formrourke table. Upon execution of the script, a log file called rourketransfer.log is created, to aid you in assessing
the the results of the migration. 

An example of the log file is as follows:

IMPORT STARTED@Tue Mar 22 23:53:39 PST 2005
UPDATED RECORD#: 113 APPENDED: Immunization TO FIELD: p1_immunization1m
UPDATED RECORD#: 114 APPENDED: Immunization TO FIELD: p1_immunization1m
UPDATED RECORD#: 114 APPENDED: Acetaminophen TO FIELD: p1_immunization1m
UPDATED RECORD#: 114 APPENDED: Hep. B vaccine TO FIELD: p1_immunization1m
UPDATED RECORD#: 114 APPENDED: MMR TO FIELD: p3_immunization4y
UPDATED RECORD#: 114 APPENDED: aPDT polio TO FIELD: p3_immunization4y



Dependencies:

In order to execute the script, ensure that the following library and interpreter dependencies are satisfied:

Jython 2.1 interpreter
Java 1.4 JRE/SDK

Installation:

Place the script in the directory of choice.

Execution:

In order for the script to succesfully execute, ensure that the db.properties file exists in the same directory(with the appropriate db parameters).
A sample configuration is commented in the include file. Make sure you that you perform a couple of tests on a dummy database, this is alpha software!


Author:Joel Legris
Email:joel.legris@egadss.org
