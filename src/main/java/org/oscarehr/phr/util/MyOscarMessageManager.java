package org.oscarehr.phr.util;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.RemoteDataLogDao;
import org.oscarehr.common.model.RemoteDataLog;
import org.oscarehr.myoscar_server.ws.MessageTransfer;
import org.oscarehr.myoscar_server.ws.MessageWs;
import org.oscarehr.myoscar_server.ws.NoSuchItemException_Exception;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class MyOscarMessageManager {
	private static final Logger logger=MiscUtils.getLogger();
	
	private static RemoteDataLogDao remoteDataLogDao=(RemoteDataLogDao) SpringUtils.getBean("remoteDataLogDao");
	
	public static List<MessageTransfer> getReceivedMessages(Long myOscarUserId, String myOscarPassword, Boolean active, int startIndex, int itemsToReturn)
	{		
		MessageWs messageWs=MyOscarServerWebServicesManager.getMessageWs(myOscarUserId, myOscarPassword);
		
		List<MessageTransfer> messageTransfers=messageWs.getReceivedMessages(myOscarUserId, active, startIndex, itemsToReturn);
		logger.debug("getReceivedMessages from MyOscar Server : "+messageTransfers.size());
			
		for (MessageTransfer messageTransfer : messageTransfers)
		{
			makeLogEntry("MESSAGE", messageTransfer.getId(), RemoteDataLog.Action.RETRIEVE, ReflectionToStringBuilder.toString(messageTransfer));
		}
			
		return(messageTransfers);
	}

	public static List<MessageTransfer> getSentMessages(Long myOscarUserId, String myOscarPassword, int startIndex, int itemsToReturn)
	{		
		MessageWs messageWs=MyOscarServerWebServicesManager.getMessageWs(myOscarUserId, myOscarPassword);
		
		// when viewing sent messages we always view all even if the receipient has deleted/archived/inactived the message
		List<MessageTransfer> messageTransfers=messageWs.getSentMessages(myOscarUserId, null, startIndex, itemsToReturn);
		logger.debug("getSentMessages from MyOscar Server : "+messageTransfers.size());
			
		for (MessageTransfer messageTransfer : messageTransfers)
		{
			makeLogEntry("MESSAGE", messageTransfer.getId(), RemoteDataLog.Action.RETRIEVE, ReflectionToStringBuilder.toString(messageTransfer));
		}
			
		return(messageTransfers);
	}

	public static MessageTransfer getMessage(Long myOscarUserId, String myOscarPassword, Long messageId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
	{
		MessageWs messageWs=MyOscarServerWebServicesManager.getMessageWs(myOscarUserId, myOscarPassword);
		MessageTransfer messageTransfer=messageWs.getMessage(messageId);

		makeLogEntry("MESSAGE", messageTransfer.getId(), RemoteDataLog.Action.RETRIEVE, ReflectionToStringBuilder.toString(messageTransfer));
		
		return(messageTransfer);
	}
	
	public static void markRead(Long myOscarUserId, String myOscarPassword, Long messageId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
	{
		MessageWs messageWs=MyOscarServerWebServicesManager.getMessageWs(myOscarUserId, myOscarPassword);
		messageWs.markAsRead(messageId);
	}

	/**
	 * @return Returns the ID of the new message that is created ie the reply message.
	 * @throws NoSuchItemException_Exception 
	 */
	public static Long sendReply(Long myOscarUserId, String myOscarPassword, Long messageId, String contents) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
	{
		MessageWs messageWs=MyOscarServerWebServicesManager.getMessageWs(myOscarUserId, myOscarPassword);
		Long repliedMessageId = messageWs.replyToMessage(messageId, contents);
		makeLogEntry("MESSAGE_REPLY", null, RemoteDataLog.Action.SEND, "repliedToMessageId="+messageId+", contents="+contents);
		return repliedMessageId;
	}

	public static void sendMessage(Long myOscarUserId, String myOscarPassword, Long recipientPersonId, String subject, String contents) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs=MyOscarServerWebServicesManager.getMessageWs(myOscarUserId, myOscarPassword);
		messageWs.sendMessage(recipientPersonId, subject, contents);

		makeLogEntry("MESSAGE", null, RemoteDataLog.Action.SEND, "recipientPersonId="+recipientPersonId+", subject="+subject+", contents="+contents);
	}
	
	private static void makeLogEntry(String documentType, Object objectId, RemoteDataLog.Action action, String documentContents)
	{
		RemoteDataLog remoteDataLog=new RemoteDataLog();
		
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		remoteDataLog.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
		remoteDataLog.setDocumentId(MyOscarServerWebServicesManager.getMyOscarServerBaseUrl(), documentType, objectId);
		remoteDataLog.setAction(action);
		remoteDataLog.setDocumentContents(documentContents);
		
		remoteDataLogDao.persist(remoteDataLog);
	}

	public static void flipActive(Long myOscarUserId, String myOscarPassword, Long messageId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception {
		MessageWs messageWs=MyOscarServerWebServicesManager.getMessageWs(myOscarUserId, myOscarPassword);
		MessageTransfer messageTransfer=messageWs.getMessage(messageId);
		messageWs.setMessageActive(messageId, !messageTransfer.isActive());
    }
}
