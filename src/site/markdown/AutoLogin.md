### Description of Integration Point: Log into MyOscar automatically

#### Workflow

1. On the main schedule screen (appointmentprovideradminday.jsp), the first screen after login, the jsp executes this code by default if MyOscar is enabled:

	`MyOscarUtils.attemptMyOscarAutoLoginIfNotAlreadyLoggedInAsynchronously(loggedInInfo);`


2. This method fires off a thread to login, executing this code:

		`try
		{
			LoginResultTransfer3 loginResultTransfer=AccountManager.login(MyOscarLoggedInInfo.getMyOscarServerBaseUrl(), myOscarUserName, decryptedMyOscarPasswordString);
			
				myOscarLoggedInInfo=new MyOscarLoggedInInfo(loginResultTransfer.getPerson().getId(), loginResultTransfer.getSecurityTokenKey(), session.getId(), loggedInInfo.locale);
				MyOscarLoggedInInfo.setLoggedInInfo(session, myOscarLoggedInInfo);
			}
			catch (NotAuthorisedException_Exception e) {
				logger.warn("Could not login to MyOscar, invalid credentials. myOscarUserName="+myOscarUserName);
				
				// login failed, remove myoscar saved password
				providerPreference.setEncryptedMyOscarPassword(null);
				providerPreferenceDao.merge(providerPreference);
			}
		} catch (Throwable t) {
			logger.error("Error attempting auto-myoscar login", t);
		}`

#### Integration Point 

1. AccountManager.login() gets an instance of LoginWS and makes the log in request here:

		`try
		{
			LoginWs loginWs = MyOscarServerWebServicesManager.getLoginWs(myOscarServerBaseUrl);
			LoginResultTransfer3 loginResultTransfer = loginWs.login4(userName, password);
			return(loginResultTransfer);
		}
		catch (NotAuthorisedException_Exception e)
		{
			logger.warn("Invalid Login Request userName=" + userName);
			throw(e);
		}`

MyOscar (LoginWS.login4())
1. The method LoginWS.login4() generates a security token for the user if the login is successful, and returns an object of type LoginResultTransfer3.
2. If user credentials are incorrect, throws NotAuthorisedException
