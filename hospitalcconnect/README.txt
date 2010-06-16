#Hamilton Health Sciences Clinical Connect Interface sub project.
############################################

#Purpose:
#########
This sub-project helps automate retrieving HL7 Hospital Data from Clinical Connect and uploading it to OSCAR

#Overview:
##########
This project calls a web service (WSDL src/main/java/resources/EmrDownloadService.wsdl) provided by Clinical Connect (Medseek) to download a zip file
containing one HL7 file.  This file is written to the incoming directory that is specified in the properties file.

The file is unzipped,  if it's empty, indicating no data is sent, it is moved to an empty directory as specified in the properties file.
However, if the file has data, it's sent to the oscar server via the same interface
that HL7FileManagement uses. (https://localhost/oscar/lab/newLabUpload.do).  If Oscar returns "success" the file is moved to the completed directory that is specified in the properties file.)

*Note* that everytime the webservice is called a file will be returned, even if there is no data.

Not much had to be changed on the oscar side.  A parser was added (HHSEmrDownloadHandler) and labDisplay.jsp was altered to better handle Formatted Text but this was a benefit to the interface
and not just to this project.

This sub-project borrows alot of code from the HL7FileManagement sub-project (and will most likely be merged into it).

#NOTES :
#######
-Everytime the webservice is called a file will be returned, even if there is no data.
-Oscar 912+ is required to display this data. 
        A parser was added (HHSEmrDownloadHandler) and labDisplay.jsp was altered to better handle HL7 Formatted Text          
-This sub-project borrows alot of code from the HL7FileManagement sub-project (and will most likely be merged into it).


#Build information
##################

Maven can be used to build this project.  To run the application use the call below

java -cp <PATH_TO>/cxf-ws-clinicalconnect-0.0.1-SNAPSHOT.jar:<PATH_TO>/dependency/* org.oscarehr.clinicalconnect.GetClinicalConnectData ClinicalConnect.properties

The cxf-ws-clinicalconnect.0.0.1-SNAPSHOT.jar will be in the target directory created by Maven

To get the dependency directory run this command

mvn dependency:copy-dependencies

This will copy all the required jar files to a directory "dependency" in the target directory created by Maven.

ClinicalConnect.properties (example in src/main/resources/ClinicalConnect.properties) has the following properties

    #
    # This information is provided by Clinical Connect
    group=
    password=

    serviceUsername=
    servicePassword=
    serviceLocation=

    #
    #The location of the public key file to encrypt the HL7 file download before sending it to oscar(URL)
    keyLocation=
    #The URL should be in this format https://localhost:11042/oscar_mcmaster
    URL=

    ##The directories used to process the files downloaded files from Clinical Connect.
    ##incoming is where the files are placed when they are first downloaded.
    ##If there is a problem reading the format of the file it will be moved to the error directory
    ##If the file is empty it will move to the empty directory
    ##And if the file is successfully uploaded to oscar, it will move to the completed directory
    incomingHL7dir=/var/lib/OscarDocument/oscar_mcmaster/clinicalconnect/incoming
    errorHL7dir=/var/lib/OscarDocument/oscar_mcmaster/clinicalconnect/error
    completedHL7dir=/var/lib/OscarDocument/oscar_mcmaster/clinicalconnect/completed
    emptyHL7dir=/var/lib/OscarDocument/oscar_mcmaster/clinicalconnect/empty


#On the oscar side:
###################

A key/pair will need to be generated in the Admin screen (Use the type "HHSEMR").
Download the file to the server and reference the file in the keyLocation property in the ClinicalConnect.properties file.




