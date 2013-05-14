Configuring OLIS
================

The assumes that you have done the paperwork needed to sign up for OLIS



Generate CSR 
--------------


	keytool -genkey -keyalg RSA -alias olis -keystore olis.jks


Olis should provide you with the data they would like to see in the certificate. Remember the password used!


	keytool -certreq -alias olis -file csr.txt -keystore olis.jks


Email eHealth Ontario with the csr.txt file





Importing the Certificate Files
-------------------------------

eHealth will return two files. A signed Certificate and a root certificate that must be imported into the OLIS keystore.  The root certificate must be imported first.


	keytool -import -trustcacerts -alias root -file SSHARoot.crt -keystore olis.jks
	
	#Then import the signed cert
	
	keytool -import -trustcacerts -alias olis -file EMR.CLINIC.NAME.PROD.txt -keystore olis.jks
	
	
	
	
	
Installing the Keystore in OSCAR
--------------------------------

Configure the following properties in the OSCAR properies file

	olis_keystore=/<PATHTO>/olis.jks
	olis_ssl_keystore=/<PATHTO>/olis.jks
	olis_ssl_keystore_password=*******
	olis_truststore=/<PATHTO>/jssecacerts
	olis_truststore_password=********
	olis_returned_cert=/<PATHTO>/EMR.CLINIC.NAME.PROD.txt
	
	
eHealth may have other configuration required on the server.


Use In OSCAR
------------

OLIS patient search is accessed in the inbox screen.  The minimum search requirement is a date period to search,  a patient, and a requesting HIC (Health Information Custodian).

The search will return a list of labs that the user can then:

Preview - View but not save to local database (action will be logged though)

File - Same as "File" in the inbox. saves to patient's chart but doesn't acknowledge

Acknowledge - Same as Acknowledge in the inbox. Saves to the patient's chart while acknowledging.

Add to Inbox - Adds the lab result to the provider's inbox.  Provider can then file or acknowledge from their inbox.


HIC providers will need their Full Name filled in their provider record.  The new "official name" fields are important.  They must match the provider's college id record exactly.  It will not work if they don't match.

	http://www.cpso.on.ca/docsearch/


Automatic Provider Query
------------------------

The automatic provider query is not currently live in production from eHealth
