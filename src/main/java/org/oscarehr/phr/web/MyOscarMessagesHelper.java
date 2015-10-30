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

package org.oscarehr.phr.web;

import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.oscarehr.myoscar.client.ws_manager.MessageManager;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.myoscar_server.ws.Message2DataTransfer;
import org.oscarehr.myoscar_server.ws.Message2RecipientPersonAttributesTransfer;
import org.oscarehr.myoscar_server.ws.MessageTransfer3;
import org.oscarehr.myoscar_server.ws.NoSuchItemException_Exception;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;

public final class MyOscarMessagesHelper {
	public static final int MESSAGE_DISPLAY_SIZE = 25;

	public static final String ATTACH_FILE_DATA_TYPE="FILE_ATTACHMENT";

	public static int getNextPageStartIndex(int currentStartIndex) {
		return (currentStartIndex + MESSAGE_DISPLAY_SIZE);
	}

	public static int getPreviousPageStartIndex(int currentStartIndex) {
		int temp = currentStartIndex - MESSAGE_DISPLAY_SIZE;
		return (Math.max(0, temp));
	}
	
	public static List<MessageTransfer3> getReceivedMessages(MyOscarLoggedInInfo myOscarLoggedInInfo, Boolean active, int startIndex) throws NoSuchItemException_Exception, NotAuthorisedException_Exception {
		List<MessageTransfer3> remoteMessages = MessageManager.getReceivedMessages(myOscarLoggedInInfo, myOscarLoggedInInfo.getLoggedInPersonId(), active, startIndex, MESSAGE_DISPLAY_SIZE);
		return (remoteMessages);
	}
	
	public static List<MessageTransfer3> getReceivedMessages(HttpSession session, Boolean active, int startIndex) throws NoSuchItemException_Exception, NotAuthorisedException_Exception {
		MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(session);
		return getReceivedMessages(myOscarLoggedInInfo,active,startIndex);
	}

	public static List<MessageTransfer3> getSentMessages(HttpSession session, int startIndex) throws NoSuchItemException_Exception, NotAuthorisedException_Exception {
		MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(session);
		List<MessageTransfer3> remoteMessages = MessageManager.getSentMessages(myOscarLoggedInInfo, myOscarLoggedInInfo.getLoggedInPersonId(), startIndex, MESSAGE_DISPLAY_SIZE);
		return (remoteMessages);
	}

	public static MessageTransfer3 readMessage(HttpSession session, Long messageId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception {
		MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(session);
		MessageTransfer3 messageTransfer = MessageManager.getMessage(myOscarLoggedInInfo, messageId);

		if (messageTransfer != null) {
			// can only mark as read if I'm the recipient.
			if (messageTransfer.getRecipientPeopleIds().contains(myOscarLoggedInInfo.getLoggedInPersonId())) {
				Message2RecipientPersonAttributesTransfer recipientAttributes = MessageManager.getMessageRecipientPersonAttributesTransfer(myOscarLoggedInInfo, messageId, myOscarLoggedInInfo.getLoggedInPersonId());
				recipientAttributes.setFirstViewDate(new GregorianCalendar());
				MessageManager.updateMessageRecipientPersonAttributesTransfer(myOscarLoggedInInfo, recipientAttributes);
			}
		}

		return (messageTransfer);
	}
	
	/**
	 * convenience method to get a specific content data
	 */
	public static Message2DataTransfer getMessagePart(MessageTransfer3 messageTransfer, String dataType)
	{
		for (Message2DataTransfer messageDataTransfer : messageTransfer.getMessageDataList())
		{
			if (messageDataTransfer.getDataType().equals(dataType)) return (messageDataTransfer);
		}

		return (null);
	}

	public static Message2DataTransfer getFileAttachment(MessageTransfer3 messageTransfer)
	{
		return(getMessagePart(messageTransfer, ATTACH_FILE_DATA_TYPE));
	}
}
