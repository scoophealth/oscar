
SETUP 
######

STEP 1
------

Add the location of your Mule installation( the mule-1.3.3 directory )to your path.   
This will depend on your platform, but is usually something like 
the following:

Linux / Unix
------------
export MULE_HOME=/home/username/oscar_mcmaster/HL7FileManagement/mule-1.3.3
export PATH=$PATH:$MULE_HOME/bin

Windows
-------
set MULE_HOME=C:\Program Files\oscar_mcmaster\HL7FileManagement\mule-1.3.3
set PATH=%PATH%;%MULE_HOME%\bin

(or by using the System utility in the Control Panel for Windows NT/2000/XP)



STEP 2
------

Log into the oscar server and click the admin tab at the top right.
Scroll to the bottom of the Administrative Page and click on the
Key Pair Generator link.

	1.	Input 'oscar' without the quotes as the service name and press
		the 'Create Key Pair' button. If you recieved the error:
			"Failed: The oscar key pair has already been created"
		It is ok to ignore it.

	2.	Input your service as the service name and select the message 
		type from the drop down menu and click the 'Create Key Pair'
		button. A window will pop up asking you to save 'keyPair.key'.
		Remeber the location where you saved it.


STEP 3
------

Edit the file 'LabProperties.properties' in the 'conf' directory to use
your own directories. If the directories do not exist they will be created 
at runtime. Also make sure to include the address of the oscar server and
the location of 'keyPair.key' that you saved in the previous step.


STEP 4
------

Use the ant build script in the current directory to build the source by 
typing 'ant' into the terminal



RUNNING
#######

The client can now be run from the terminal by typing in 'mule' without
the quotes. Any files saved in the 'incomingHL7dir' specified in the
properties file will be uploaded to the server and then moved to the
'completedHL7dir'. The 'incomingHL7dir' will be checked once every
minute for new files. If there are any errors the file will not be uploaded
and it will be moved to the 'errorHL7dir'.


