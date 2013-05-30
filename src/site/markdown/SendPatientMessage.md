### Description of Integration Point: Send Patient Message

#### Workflow

##### 1. Search User (demographic/search.jsp)
A user may be search by Name, Phone, DOB yyyy-mm-dd, Address, Health Ins. #, Chart No.

In this scenario, the user's lastname was entered for the Name search.
In the search listings, id number link was clicked to bring us to the Patient Detail screen.

##### 2. Patient Detail (demographiccontrol.jsp)

On Patient Detail Info screen, click Send Message to PHR.
Note: this is the only flow that allows the user to start a message thread with the Patient. This design limits the risk of sending a message to the wrong patient.

Click "Send Message to PHR". 
1. Invokes phr/PhrMessage.do?method=createMessage

##### 3. Create Message (phr/msg/CreatePHRessage.jsp)
1. User fills out Subject and Body of message. 
2. Clicks "Send Message"

##### 4. Send Message (/phr/PhrMessage.do?method=sendPatient)

Oscar:
1. Invokes /phr/PhrMessage bean -> calls sendPatient() on org.oscarehr.phr.web.PHRMessageAction 

This is the integration point:

	`try {
	        MessageManager.sendMessage(myOscarLoggedInInfo, recipientMyOscarUserId, subject, messageBody);
        } catch (NotAuthorisedException_Exception e) {
	        WebUtils.addErrorMessage(request.getSession(), "This patient has not given you permissions to send them a message.");
	        return mapping.findForward("create");
        }`

MessageManager here is the org.oscarehr.myoscar.client.ws_manager.MessageManager. It gets an instance of a MessageWs class by passing in the credentials of the current user. MessageWs then makes the call to the associated MyOscar component.

MyOscar:
1. Invokes sendMessage() on org.oscarehr.myoscar_server.managers.MessageManager.
2. Checks credentials and relationship of user requesting to send message. Will throw a NotAuthorizedException if this method returns false:

	`private boolean isAllowedToSendMessage(Person requestingPerson, Message2 message)
	{
		if (Role.SYSTEM_ADMINISTRATOR.name().equals(requestingPerson.getRole())) return(true);

		// messages must be sent by the sender (for now anyways)
		if (!requestingPerson.getId().equals(message.getAttributes().getSenderPersonId())) return(false);

		// check all recipients
		if (message.getRecipientPeopleIds() != null) for (Long recipientPersonId : message.getRecipientPeopleIds())
		{
			boolean allowed = isAllowedToSendToPerson(message.getAttributes().getSenderPersonId(), recipientPersonId);
			if (!allowed) return(false);
		}

		// check all groups
		if (message.getRecipientGroupIds() != null) for (Long recipientGroupId : message.getRecipientGroupIds())
		{
			boolean allowed = isAllowedToSendToGroup(message.getAttributes().getSenderPersonId(), recipientGroupId);
			if (!allowed) return(false);
		}

		return(true);
	}`
