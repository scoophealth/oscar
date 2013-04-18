### Description of Integration Point: MyOSCAR Patient Registration

#### Workflow

##### 1. Search User (demographic/search.jsp)

![2](resources/2.png?raw=true)
A user may be search by Name, Phone, DOB yyyy-mm-dd, Address, Health Ins. #, Chart No.

In this scenario, the user's lastname was entered for the Name search.
In the search listings, the second user was clicked.

##### 2. Edit User (demographic/demographiccontrol.jsp?demographic_no=2&displaymode=edit&dboperation=search_detail)

![3](resources/3.png?raw=true)

In order to Register for MyOSCAR, the EDIT link needs to be clicked.
The Edit link also allows the user to update a user.
Generally, once any updates are made, click on "Update Record" to save all changes.

##### 3. Click on the "Register for MyOSCAR" link (demographic/demographiccontrol.jsp?demographic_no=2&displaymode=edit&dboperation=search_detail)

![4](resources/4.png?raw=true)

##### 4. The MyOSCAR Patient Registration popup screen will appear.  (phr/indivo/RegisterIndivo.jsp?demographicNo=2)
![5](5.png?raw=true)

This is the exact integration point where Oscar speaks to MyOSCAR (in PHRUserManagementAction.java)

		newAccount = AccountManager.createPerson(myOscarLoggedInInfo, newAccount, newAccountPassword);

Username is the username that will be trasmitted to MyOSCAR and entered into MyOSCAR.  Note, by default, "Username" is the Oscar user's firstname.lastname.

A default password is also generated.

Upon submitting the MyOSCAR Patient Registration form, 2 scenarios can happen.

1) The Oscar user will be registered in MyOSCAR.  This can be confirmed by a user search on the MyOscar side.

![6](resources/6.png?raw=true)

Further, if the user is successfully created, another web service call is initiated (from PHRUserManagementAction.java):

		addRelationships(request, newAccount);

which calls the following web service:

		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(myOscarLoggedInInfo);


Note the last user, Username: firstname.lastname

2) An Exception will be thrown.  Known Issues: At this time, if there are more then 2 users in Oscar with the same first name and lastname, only one can be registered in MyOSCAR with that "Username" i.e. firstname.lastname.  A future enhancement will allow a current user with the same name to be registered with MyOSCAR with a modified Username i.e. firstname1.lastname1

Also, the current error handling does not accurately explain the error in Oscar (from PHRUserManagementAction.java):

		} catch (InvalidRequestException_Exception e) {
			log.debug("error", e);
			ar.addParameter("failmessage", "Error, most likely cause is the username already exists. Check to make sure this person doesn't already have a myoscar user or try a different 				username.");
		} catch (Exception e) {
			log.error("Failed to register myOSCAR user", e);
			if (e.getClass().getName().indexOf("ActionNotPerformedException") != -1) {
				ar.addParameter("failmessage", "Error on the myOSCAR server.  Perhaps the user already exists.");
			} else if (e.getClass().getName().indexOf("IndivoException") != -1) {
				ar.addParameter("failmessage", "Error on the myOSCAR server.  Perhaps the user already exists.");
			} else if (e.getClass().getName().indexOf("JAXBException") != -1) {
				ar.addParameter("failmessage", "Error: Could not generate sharing permissions (JAXBException)");
			} else {
				ar.addParameter("failmessage", "Unknown Error: Check the log file for details.");
			}
		}

Currently, the last line of the error handling clause above shows on screen in Oscar:

![7](resources/7.png?raw=true)

In MyOSCAR, the following stacktrace shows a duplicate username already exists and is therefore rejected, note the "ItemAlreadyExistsException":

		ERROR [TransactionInterceptor:439] Application exception overridden by commit exception
		org.oscarehr.myoscar_server.managers.ItemAlreadyExistsException: The transaction has been rolled back.  See the nested exceptions for details on the errors that occurred.
		at org.oscarehr.myoscar_server.managers.AccountManager.addPerson(AccountManager.java:133)
		at org.oscarehr.myoscar_server.managers.AccountManager$$FastClassByCGLIB$$d255311d.invoke(<generated>)
		

		ERROR [MyOscarFaultListener:51] '{http://ws.myoscar_server.oscarehr.org/}AccountWsService#{http://ws.myoscar_server.oscarehr.org/}addPerson3' 
		org.springframework.dao.DataIntegrityViolationException: The transaction has been rolled back.  See the nested exceptions for details on the errors that occurred.; nested exception is 		<openjpa-2.2.1-r422266:1396819 fatal store error> org.apache.openjpa.persistence.EntityExistsException: The transaction has been rolled back.  See the nested exceptions for details on the 			errors that occurred.


NOTE: It is also possible to have an error thrown during the MyOSCAR User Registration AND the user still is registered.  There is atleast one known case in PHRuserManagementAction.java where

		File docDirectory = new File(docDir);

a file is attempted to be created and if the file is NOT successfully created, it will throw this error from PHRuserManagementAction.java:

		ar.addParameter("failmessage", "Unknown Error: Check the log file for details.");		

The call to create a file is AFTER the RegisterUser call:

		PersonTransfer3 newAccount = sendUserRegistration(myOscarLoggedInInfo, ht);


