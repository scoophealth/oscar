### Description of Integration Point: Get Logged In Person ID

#### Workflow

The method MyOscarLoggedInInfo.getLoggedInPerson() only performs a conditional call to the web service, based on caching.

It is called in 
1. class PHRViewPatientRecord.viewMyOscarRecord()
1. DisplayPHRMessages.jsp
2. PHRLoginAndRedirect.jsp
3. Response.jsp

However, it only invokes the Web Service when person object is not already cached, therefore on the first time.

#### Integration point (MyOscarLoggedInInfo.getLoggedInPerson())

1. Calls AccountManager.getPerson()

	`public static PersonTransfer3 getPerson(MyOscarLoggedInInfoInterface credentials, String personUserName) throws NotAuthorisedException_Exception
	{
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		PersonTransfer3 personTransfer = accountWs.getPersonByUserName3(personUserName, true);

		String cacheKey = getCacheKey(credentials, personTransfer.getId());
		personCache.put(cacheKey, personTransfer);

		return(personTransfer);
	}`

MyOscar
1. Does lookup on that user, and returns a Person3 object if applicable:

 	`return(PersonTransfer3.getTransfer(accountManager.getPerson(userName, active)));`

2. If user is not authorized to do this lookup, throws NotAuthorizedException



