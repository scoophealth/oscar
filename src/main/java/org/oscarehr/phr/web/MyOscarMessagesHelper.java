package org.oscarehr.phr.web;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.oscarehr.myoscar_server.ws.MessageTransfer;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.util.MyOscarMessageManager;

public final class MyOscarMessagesHelper
{
	public static ArrayList<MessageTransfer> getMessages(HttpSession session)
	{
		PHRAuthentication auth = (PHRAuthentication) session.getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
		ArrayList<MessageTransfer> remoteMessages = MyOscarMessageManager.getReceivedMessages(auth.getMyOscarUserId(), auth.getMyOscarPassword());
		return(remoteMessages);
	}
}