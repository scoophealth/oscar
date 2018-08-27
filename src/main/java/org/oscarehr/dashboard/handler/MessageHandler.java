/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.dashboard.handler;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.model.OscarMsgType;

import oscar.oscarMessenger.data.MsgMessageData;
import oscar.oscarMessenger.data.MsgProviderData;
import oscar.oscarMessenger.util.MsgDemoMap;

public class MessageHandler {

	private static final String SYSTEM_USER_ID = "-1";
	private static final String SYSTEM_USER_NAME = "System";

	// This is based on EaapsHandler.notifyProvider
	public void notifyProvider(
		String subject,
		String messageBody,
		String providerNo,
		List<Integer> linkedDemographicNumbers
	) {
		String userName = SYSTEM_USER_NAME;
		String userNo = SYSTEM_USER_ID;
		String attachment = null;
		String pdfAttachment = null;

		MsgMessageData messageData = new MsgMessageData();

		String[] providerIds = new String[] { providerNo };
		String sentToWho = messageData.createSentToString(providerIds);
		ArrayList<MsgProviderData> providerListing = messageData.getProviderStructure(providerIds);

		String messageId = messageData.sendMessage2(
			messageBody,
			subject,
			userName,
			sentToWho,
			userNo,
			providerListing,
			attachment,
			pdfAttachment,
			OscarMsgType.GENERAL_TYPE
		);

		if (linkedDemographicNumbers == null) {
			return;
		}

		MsgDemoMap msgDemoMap = new MsgDemoMap();
		for (Integer demographicNumber : linkedDemographicNumbers) {
			msgDemoMap.linkMsg2Demo(messageId, demographicNumber.toString());
		}
	}
}
