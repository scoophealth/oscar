
CML Lab Management README
#########################
This module is now deprecated and should not be used unless you can only work with CML's ABCD format files. CML's HL7 Files are handled by the HL7FileManagement

Building
#########

A jar file can be built with the ant build script ( located in the build directory )

#ant cml-jar 

will create the cml client jar file in the build/tmp directory

Jar file will be in the form of cml-YYYYMMDD-HHDD.jar  with the date and time built.



SET-UP
######

In windows create the following directories

C:\CML
C:\CML\completed
C:\CML\dups
C:\CML\error
C:\CML\incoming

Copy the following files to the C\:CML directory

cml.jar ( rename it from cml-YYYYMMDD-HHMM.jar )
commons-httpclient-2.0.jar
commons-logging.jar            (these two jars can be found in the web/WEB-INF/lib directory of this CVS tree

Create a file named lab.properties in C:\CML directory with the following entries
Make sure you change the serverUrl and serverKey properties

### lab.properties
busyFile=busy.txt
workingFile=UPLD.TXT
incomingHL7dir=C:/CML/incoming    
moddedTime=MODTIME.TXT
errorHL7dir=C:/CML/error
dupsHL7dir=C:/CML/dups
completedHL7dir=C:/CML/completed
serverUrl=https://{URL_OR_IP_OF_OSCARSERVER:{PORT_NUMBER}/{WEBAPP_NAME}/lab/CMLlabUpload.do
serverKey=# This key must be the same as the CML_UPLOAD_KEY defined in the OSCAR servers property file


RUNNING
#######

It is assumed that you have blast configured and running properly. If not contact your lab vendor for help.  
Make sure the lab vendor knows to put all new incoming labs into C:\CML\incoming


In Windows this command can be used. ( Best placed in a Bat file eg. cmlRun.bat )

java -cp .;C:\CML\cml.jar;C:\CML\commons-httpclient-2.0.jar;C:\CML\commons-logging.jar   oscar_mcmaster.CMLFileManagement.CMLClient c:\CML\lab.properties


AUTOMATED
#########

A windows version of cron can be used to automate this process.

A freeware version can be found here

http://www.kalab.com/freeware/cron/cron.htm

An example crontab file would look like this.


# This is a sample crontab file. This file must be copied to the same directory as cron.exe
# A log is written to cron.log

# This runs the cml client at the 10th minute every hour.
# ie. if the java command above has been entered into a batch file named cmlRun.bat
10 * * * * C:\CML\cmlRun.bat
                 









