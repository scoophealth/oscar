PREREQUISITES
#############


(1)	The java jdk must be installed. It can be found here:
  		http://java.sun.com/javase/downloads/index.jsp

	Follow the instructions to download and install the latest version with JDK


(2)	Apache ant must be installed. It can be found here:
		http://ant.apache.org/bindownload.cgi

	Download and extract the .zip file to c:\Program Files 


SETUP 
######

STEP 1
------

Add the location of your Mule installation( the mule-1.3.3 directory )to your path.   

- Open the 'System' control in the 'Control Panel'
- Under the 'Advanced' tab click the 'Environment Variables' button
- In the 'System variables' window click the 'New...' button
	- Variable Name: MULE_HOME
	- Variable Value: c:\path\to\mule\HL7FileManagement\mule-1.3.3
	- Click 'OK'
- In the 'System variables' window click the 'New...' button
	- Variable Name: ANT_HOME
	- Variable Value: C:\Program Files\apache-ant-1.7.0
	- Click 'OK'
- In the 'System variables' window highlight the 'Path' variable and click the 'Edit...' button
	- add ';%MULE_HOME%\bin;%ANT_HOME%\bin;%ANT_HOME%\lib' to the end of the 'Variable Value'
	- Click 'OK'
- Click 'OK'

The environment variables are now set.

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

If you wish to use multiple message formats such as MDS and CML you will need
a different keypair for each message format.

STEP 3
------

Edit the file 'LabProperties.properties' in the 'conf' directory to use
your own directories. If the directories do not exist they will be created 
at runtime. Also make sure to include the address of the oscar server and
the location of 'keyPair.key' that you saved in the previous step.

STEP 4
------

Depending on how many different message formats you wish to use you will have
to use a different config file.

Rename the appropriate 'mule-config_***.xml' file located in the 'conf'
directory to 'mule-config.xml'


STEP 5 - Build Mule
-------------------

Open the command line by selecting 'Run...' from the start menu and typing in 'cmd' and clicking 'OK'

Navigate to the 'HL7FileManagement' directory within the command line.

Set JAVA_HOME to point to the JDK using the following command:
	> set JAVA_HOME=c:\Program Files\java\jdk1.6.0_02
		( be sure the directory is correct )

Use the ant build script in the current directory to build the source:
	> ant

Leave the command line open for the next step.

STEP 6 - Install Mule
---------------------

Type the following into the command line:
	> mule install

Mule is now installed as a windows service, it will start when windows boots. It can be started or stopped using the following commands:
	> mule start
	> mule stop

Mule can be removed from being a service with the following command
	> mule remove


