LINUX INSTALL
#############

PREREQUISITES
-------------
  - The java JDK must be installed
  - Apache Ant must be installed

STEP 1 - Create Key Pairs
-------------------------

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


STEP 2 - Configure the properties file
--------------------------------------

Edit the file 'LabProperties.properties' in the 'conf' directory to use
your own directories. If the directories do not exist they will be created 
at runtime. Also make sure to include the address of the oscar server and
the location of 'keyPair.key' that you saved in the previous step.


STEP 3 - Set the environmental variables
----------------------------------------
Make sure that JAVA_HOME has been set, open the terminal
and execute the following command:
	>$ echo $JAVA_HOME

If nothing appears when the above command is executed then
JAVA_HOME has not been set please set it using the following
command (be sure to replace the path with the location at
which your jdk is installed):
	>$ export JAVA_HOME=/usr/lib/jvm/java-1.5.0-sun 

Now that JAVA_HOME has been set MULE_HOME must be set using 
the same command. (the path is the path to mule-1.3.3 located
within the current directory)
	>$ export MULE_HOME=/home/username/oscar_mcmaster/HL7FileManagement/mule-1.3.3


STEP 4 - Installation
---------------------
Execute the install.sh with root access and follow the instructions:
	>$ sudo ./install.sh

This will install mule as a daemon which will start when the 
system is rebooted. It can be started without rebooting the system
with the following command:
	>$ sudo /etc/init.d/muled start
	(The status|restart|stop commands will also work with the mule daemon)



REMOVAL
#######

To remove mule simple run uninstall.sh with root access:
	>$sudo ./uninstall.sh
