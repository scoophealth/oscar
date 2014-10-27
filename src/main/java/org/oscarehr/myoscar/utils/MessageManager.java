/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.myoscar.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.InvalidRequestException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MessageAndRecipientAtrributesTransfer;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MessagePartTransfer;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MessageRecipientAttributesTransfer;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MessageTransfer4;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MessageWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MessagingPreferencesTransfer;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MinimalPersonTransfer3;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.NoSuchItemException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.UnsupportedEncodingException_Exception;
import org.oscarehr.util.ConfigXmlUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;

public final class MessageManager
{
	private static Logger logger = MiscUtils.getLogger();
	public static final String ATTACH_FILE_DATA_TYPE="FILE_ATTACHMENT";
	public static final String ATTACH_FILE_XML_MIME_TYPE="application/xml";

	private static final int UNREAD_MESSAGE_CACHE_SIZE = ConfigXmlUtils.getPropertyInt("myoscar_client", "unread_message_cache_size");
	private static final long UNREAD_MESSAGE_CACHE_TIME_MS = ConfigXmlUtils.getPropertyInt("myoscar_client", "unread_message_cache_time_ms");
	private static final int MESSAGE_CACHE_SIZE = ConfigXmlUtils.getPropertyInt("myoscar_client", "message_cache_size");
	private static final long MESSAGE_CACHE_TIME_MS = ConfigXmlUtils.getPropertyInt("myoscar_client", "message_cache_time_ms");

	private static QueueCache<Long, Integer> unreadMessageCount = new QueueCache<Long, Integer>(4, UNREAD_MESSAGE_CACHE_SIZE, UNREAD_MESSAGE_CACHE_TIME_MS, null);
	private static QueueCache<MessageTransferCacheKey, MessageTransfer4> messageCache = new QueueCache<MessageTransferCacheKey, MessageTransfer4>(4, MESSAGE_CACHE_SIZE, MESSAGE_CACHE_TIME_MS, null);

	public static List<MessageAndRecipientAtrributesTransfer> getReceivedMessages(MyOscarLoggedInInfo credentials, int startIndex, int itemsToReturn) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		List<MessageAndRecipientAtrributesTransfer> results = messageWs.getAllReceivedMessages(credentials.getLoggedInPersonId(), startIndex, itemsToReturn);
		return (results);
	}

	public static List<MessageTransfer4> getMessagesByIds(MyOscarLoggedInInfo credentials, List<byte[]> msgIds)
	{
		// 1) find what's missing locally
		// 2) retrieve what's missing from remote server
		// 3) assemble results

		Long userId = credentials.getLoggedInPersonId();
		String sessionId = credentials.getLoggedInSessionId();

		// --- find what's missing ---
		// double buffer available msg (to prevent cache flush during this processing)
		HashMap<MessageTransferCacheKey, MessageTransfer4> tempBuffer = new HashMap<MessageTransferCacheKey, MessageTransfer4>();
		List<byte[]> missingIds = new ArrayList<byte[]>();

		for (byte[] msgId : msgIds)
		{
			MessageTransferCacheKey cacheKey = new MessageTransferCacheKey(userId, sessionId, msgId);
			MessageTransfer4 msg = messageCache.get(cacheKey);

			if (msg != null)
			{
				tempBuffer.put(cacheKey, msg);
			}
			else
			{
				missingIds.add(msgId);
			}
		}

		// --- retrieve what's missing ---
		if (missingIds.size() > 0)
		{
			MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
			List<MessageTransfer4> results = messageWs.getMessagesByIds(msgIds);

			for (MessageTransfer4 transfer : results)
			{
				byte[] msgId = transfer.getId();
				MessageTransferCacheKey cacheKey = new MessageTransferCacheKey(userId, sessionId, msgId);

				tempBuffer.put(cacheKey, transfer);

				messageCache.put(cacheKey, transfer);
			}
		}

		// --- assemble results ---
		ArrayList<MessageTransfer4> results = new ArrayList<MessageTransfer4>();
		for (byte[] msgId : msgIds)
		{
			MessageTransferCacheKey cacheKey = new MessageTransferCacheKey(userId, sessionId, msgId);
			MessageTransfer4 msg = tempBuffer.get(cacheKey);
			if (msg != null) results.add(msg);
		}

		return (results);
	}

	public static List<MessageTransfer4> getInBoxMessages(MyOscarLoggedInInfo credentials, int startIndex, int itemsToReturn) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		List<byte[]> ids = messageWs.getInboxMessageIds(credentials.getLoggedInPersonId(), startIndex, itemsToReturn);
		return (getMessagesByIds(credentials, ids));
	}
	
	public static boolean isInInbox(MyOscarLoggedInInfo credentials, byte[] messageId) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		boolean result=messageWs.isInInbox(credentials.getLoggedInPersonId(), messageId);
		return(result);
	}

	public static void removeFromInbox(MyOscarLoggedInInfo credentials, byte[] messageId) throws InvalidRequestException_Exception, NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		messageWs.removeFromInBox(credentials.getLoggedInPersonId(), messageId);
	}
	
	public static List<MessageTransfer4> getSentMessages(MyOscarLoggedInInfo credentials, int startIndex, int itemsToReturn) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		List<byte[]> ids = messageWs.getSentMessageIds(credentials.getLoggedInPersonId(), startIndex, itemsToReturn);
		return (getMessagesByIds(credentials, ids));
	}

	public static int getUnreadMessageCount(MyOscarLoggedInInfo credentials) throws NotAuthorisedException_Exception
	{
		Integer result = unreadMessageCount.get(credentials.getLoggedInPersonId());

		if (result == null)
		{
			MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
			result = messageWs.getUnreadMessageCount(credentials.getLoggedInPersonId());
			unreadMessageCount.put(credentials.getLoggedInPersonId(), result);
		}

		return (result);
	}

	public static MessageTransfer4 getMessage(MyOscarLoggedInInfo credentials, byte[] messageId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
	{
		MessageTransferCacheKey cacheKey = new MessageTransferCacheKey(credentials.getLoggedInPersonId(), credentials.getLoggedInSessionId(), messageId);
		MessageTransfer4 result = messageCache.get(cacheKey);

		if (result == null)
		{
			MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
			result = messageWs.getMessagesById(messageId);
			if (result != null)
			{
				messageCache.put(cacheKey, result);
			}
		}

		return (result);
	}

	public static void sendMessage(MyOscarLoggedInInfo credentials, Long recipientPersonId, String subject, String contents) throws NotAuthorisedException_Exception, UnsupportedEncodingException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		messageWs.sendSimpleMessage(recipientPersonId, subject, contents);

		// flush cache
		unreadMessageCount.remove(recipientPersonId);
	}

	/**
	 * This is a convenience method to make a basic message transfer object so you can add to it to build a more complex one
	 * @param messageThreadId is the messageThread you're replying to, or can be null for new messages.
	 * @param replyToPersonId if the replyTo person is different than the sending person, put their ID here, otherwise leave null
	 */
	public static MessageTransfer4 makeBasicMessageTransfer(MyOscarLoggedInInfo credentials, byte[] messageThreadId, Long replyToPersonId, String subject, String messageBody)
	{
		try
		{
			MessageTransfer4 messageTransfer = new MessageTransfer4();
			messageTransfer.setMessageThreadId(messageThreadId);
			messageTransfer.setReplyToPersonId(replyToPersonId);
			messageTransfer.setSenderPersonId(credentials.getLoggedInPersonId());
			messageTransfer.setSentTime(new GregorianCalendar());

			MessagePartTransfer subjectPart = new MessagePartTransfer();
			if (subject != null) subjectPart.setContents(subject.getBytes("UTF-8"));
			subjectPart.setDataType("SUBJECT");
			subjectPart.setMimeType("text/plain");
			messageTransfer.getMessageParts().add(subjectPart);

			MessagePartTransfer bodyPart = new MessagePartTransfer();
			if (messageBody != null) bodyPart.setContents(messageBody.getBytes("UTF-8"));
			bodyPart.setDataType("MESSAGE");
			bodyPart.setMimeType("text/plain");
			messageTransfer.getMessageParts().add(bodyPart);

			return (messageTransfer);
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("this should never happen", e);
			throw (new RuntimeException(e));
		}
	}

	public static void sendMessage(MyOscarLoggedInInfo credentials, MessageTransfer4 messageTransfer) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		messageWs.sendMessage2(messageTransfer);

		// flush caches
		for (Long recipientPersonId : messageTransfer.getRecipientPersonIdList())
		{
			unreadMessageCount.remove(recipientPersonId);
		}
	}

	public static boolean isAllowedToSendMessage(MyOscarLoggedInInfo credentials, Long senderPersonId, Long recipientPersonId) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		boolean result = messageWs.isAllowedToSendMessage(senderPersonId, recipientPersonId);
		return (result);
	}

	public static MessageRecipientAttributesTransfer getMessageRecipientAttributes(MyOscarLoggedInInfo credentials, byte[] messageId, Long recipientPersonId) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		MessageRecipientAttributesTransfer messageRecipientAttributesTransfer = messageWs.getMessageRecipientAttributes(recipientPersonId, messageId);
		return (messageRecipientAttributesTransfer);
	}

	public static void updateMessageRecipientAttributes(MyOscarLoggedInInfo credentials, MessageRecipientAttributesTransfer messageRecipientAttributesTransfer) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		messageWs.updateRecipientAttributes2(messageRecipientAttributesTransfer);

		// flush caches
		unreadMessageCount.remove(messageRecipientAttributesTransfer.getRecipientPersonId());
	}

	/**
	 * This will only update the first replied to date if it hasn't already been set.
	 */
	public static void setFirstRepliedDate(MyOscarLoggedInInfo credentials, byte[] messageId, Long recipientPersonId) throws  NotAuthorisedException_Exception
	{
		MessageRecipientAttributesTransfer recipientAttributes = getMessageRecipientAttributes(credentials, messageId, recipientPersonId);
		if (recipientAttributes!=null && recipientAttributes.getFirstRepliedDate() == null)
		{
			recipientAttributes.setFirstRepliedDate(new GregorianCalendar());
			updateMessageRecipientAttributes(credentials, recipientAttributes);
		}
	}

	/**
	 * This will only update the first replied to date if it hasn't already been set.
	 */
	public static void setFirstViewedDate(MyOscarLoggedInInfo credentials, byte[] messageId, Long recipientPersonId) throws NotAuthorisedException_Exception
	{
		MessageRecipientAttributesTransfer recipientAttributes = getMessageRecipientAttributes(credentials, messageId, recipientPersonId);
		if (recipientAttributes!=null && recipientAttributes.getFirstRepliedDate() == null)
		{
			recipientAttributes.setFirstViewedDate(new GregorianCalendar());
			updateMessageRecipientAttributes(credentials, recipientAttributes);
		}
	}

	public static String getSubject(MessageTransfer4 messageTransfer) 
	{
		return (getMessagePartAsString(messageTransfer, "SUBJECT"));
	}

	public static String getMessageBody(MessageTransfer4 messageTransfer)
	{
		return (getMessagePartAsString(messageTransfer, "MESSAGE"));
	}

	public static String getMessagePartAsString(MessageTransfer4 messageTransfer, String dataType)
	{
		try
		{
			MessagePartTransfer messagePart = getMessagePart(messageTransfer, dataType);
			if (messagePart != null && messagePart.getContents() != null) return (new String(messagePart.getContents(), "UTF-8"));
			else return (null);
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("this should never happen", e);
			throw (new RuntimeException(e));
		}
	}

	/**
	 * convenience method to get a specific content data
	 */
	public static MessagePartTransfer getMessagePart(MessageTransfer4 messageTransfer, String dataType)
	{
		for (MessagePartTransfer messagePart : messageTransfer.getMessageParts())
		{
			if (messagePart.getDataType().equals(dataType)) return (messagePart);
		}

		return (null);
	}

	public static MessagingPreferencesTransfer getMyMessagingPreferences(MyOscarLoggedInInfo credentials) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		return (messageWs.getMessagingPreferences(credentials.getLoggedInPersonId()));
	}

	public static void updateMessagingPreferences(MyOscarLoggedInInfo credentials, MessagingPreferencesTransfer messagingPreferences) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		messageWs.updateMessagingPreferences(messagingPreferences);
	}

	public static List<MinimalPersonTransfer3> getCanSendToList(MyOscarLoggedInInfo credentials, Long senderPersonId) throws NotAuthorisedException_Exception
	{
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(credentials);
		return (messageWs.getCanSendToList(senderPersonId));
	}
}
