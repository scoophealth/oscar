package org.oscarehr.phr.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.oscarehr.myoscar_server.ws.MessageTransfer;
import org.oscarehr.myoscar_server.ws.NoSuchItemException_Exception;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.util.MyOscarMessageManager;
import org.oscarehr.phr.util.MyOscarUtils;

public final class MyOscarMessagesHelper {
	private static final int MESSAGE_DISPLAY_SIZE=25;
	
	public static int getNextPageStartIndex(int currentStartIndex)
	{
		return(currentStartIndex+MESSAGE_DISPLAY_SIZE);
	}
	
	public static int getPreviousPageStartIndex(int currentStartIndex)
	{
		int temp=currentStartIndex-MESSAGE_DISPLAY_SIZE;
		return(Math.max(0,  temp));
	}
	
	public static List<MessageTransfer> getReceivedMessages(HttpSession session, Boolean active, int startIndex) {
		PHRAuthentication auth = (PHRAuthentication) session.getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
		List<MessageTransfer> remoteMessages = MyOscarMessageManager.getReceivedMessages(auth.getMyOscarUserId(), auth.getMyOscarPassword(), active, startIndex, MESSAGE_DISPLAY_SIZE);
		return (remoteMessages);
	}

	public static List<MessageTransfer> getSentMessages(HttpSession session, int startIndex) {
		PHRAuthentication auth = (PHRAuthentication) session.getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
		List<MessageTransfer> remoteMessages = MyOscarMessageManager.getSentMessages(auth.getMyOscarUserId(), auth.getMyOscarPassword(), startIndex, MESSAGE_DISPLAY_SIZE);
		return (remoteMessages);
	}

	public static MessageTransfer readMessage(HttpSession session, Long messageId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
	{
		PHRAuthentication auth=MyOscarUtils.getPHRAuthentication(session);
		MessageTransfer messageTransfer=MyOscarMessageManager.getMessage(auth.getMyOscarUserId(), auth.getMyOscarPassword(), messageId);
		
		// can only mark as read if I'm the recipient.
		if (messageTransfer!=null && messageTransfer.getRecipientPersonId().equals(auth.getMyOscarUserId()))
		{
			MyOscarMessageManager.markRead(auth.getMyOscarUserId(), auth.getMyOscarPassword(), messageId);
		}
		
		return(messageTransfer);
	}
}