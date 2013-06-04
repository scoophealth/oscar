### Description of Integration Point: View Messages Sent from My Oscar

#### Workflow 

##### 1. Click MyOscar link on banner

1. Invokes phr/PhrMessage.do?method=viewMessages

##### 2. View List of Messages (/phr/msg/DisplayPHRMessages.jsp)

1. View Received Messages
	1.1 Calls MyOscarMessagesHelper.getReceivedMessages(session, true, startIndex)
	1.2 Invokes a call on the MessageWs. The boolean attribute active=true indicates new messages, as opposed to archived.

	Integration Point with MyOscar:
	
		`messageWs.getReceivedMessages3(recipientPersonId, active, startIndex, itemsToReturn);`

	MyOscar
	1.3 MessageManager looks up the requested messages (messageManager.getReceivedMessages()) and returns as an ArrayList of Message3 objects.
	1.4 If the requesting user doesn't have permissions to the requested messages, throws NotAuthorisedException
	1.5 If there are no messages, throws NoSuchItemException
	

2. View Archived Messages
	2.1 Calls MyOscarMessagesHelper.getReceivedMessages(session, false, startIndex);
	2.2 Invokes a call on the MessageWs. The boolean attribute active=false indicates archived, as opposed to new.

	Integration Point with MyOscar:

		`messageWs.getReceivedMessages3(recipientPersonId, active, startIndex, itemsToReturn);`

	MyOscar
	2.3 MessageManager looks up the requested messages (messageManager.getReceivedMessages()) and returns as an ArrayList of Message3 objects.
	2.4 If the requesting user doesn't have permissions to the requested messages, throws NotAuthorisedException
	2.5 If there are no messages, throws NoSuchItemException

3. View Sent Messages 
	3.1 Calls MyOscarMessagesHelper.getSentMessages(session, startIndex);
	3.2 Invokes a call on the MessageWs

	Integration Point with MyOscar:

		`messageWs.getSentMessages3(senderPersonId, startIndex, itemsToReturn);`

	MyOscar
	3.3 MessageManager looks up the requested messages (messageManager.getReceivedMessages()) and returns as an ArrayList of Message3 objects.
	3.4 If the requesting user doesn't have permissions to the requested messages, throws NotAuthorisedException
	3.5 If there are no messages, throws NoSuchItemException

##### 4. Click on a message 

1. Invokes /phr/PhrMessage.do?&method=read

##### 5. View a message (/phr/msg/ReadPHRMessage.jsp)

1. Calls MyOscarMessagesHelper.readMessage() passing in message ID
2. Invokes a call to MessageWs

	Integration Point with MyOscar
		`messageWs.getMessage3(messageId);`

MyOscar	
3. Message Manager looks up the requested message.
4. If the requesting user doesn't have permissions to the requested messages, throws NotAuthorisedException
5. If no message matches the one requested, throws NoSuchItemException

	
##### 6. Reply to a Message 

1. Navigates to phr/msg/CreatePHRMessage.jsp (phr/msg/CreatePHRMessage.jsp?replyToMessageId=4)
2. Compose message.
3. Click Send Message.
4. Invokes sendReply on /oscar/phr/PhrMessage.do -> PHRMessageAction.sendReply()
5. Calls MessageManager.sendMessage()

This is the integration point:
	`messageWs.sendMessage(recipientPersonId, subject, contents);`

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

