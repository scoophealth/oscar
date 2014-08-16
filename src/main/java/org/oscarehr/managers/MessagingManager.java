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
package org.oscarehr.managers;

import java.util.List;

import org.oscarehr.common.dao.MessageListDao;
import org.oscarehr.common.dao.MsgDemoMapDao;
import org.oscarehr.common.model.MessageList;
import org.oscarehr.common.model.MsgDemoMap;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class MessagingManager {

	//private static Logger logger=MiscUtils.getLogger();

	@Autowired
	private MessageListDao messageListDao;
	
	@Autowired
	private MsgDemoMapDao msgDemoMapDao;
	
	
	public List<MessageList> getMyInboxMessages(LoggedInInfo loggedInInfo, String providerNo, int offset, int limit) {		
		List<MessageList> msgs = messageListDao.findMessageRangeByProviderNo(providerNo, offset, limit);
		 
		for(MessageList msg:msgs) {
	        	LogAction.addLogSynchronous(loggedInInfo, "MessagingManager.getMyInboxMessages", "msglistid="+msg.getId());
		}
		 
		return msgs;
	}
	
	public int getMyInboxMessageCount(String providerNo, boolean onlyWithPatientAttached) {		
		List<MessageList> msgs = messageListDao.findUnreadByProvider(providerNo);
		
		if(!onlyWithPatientAttached) {
			return msgs.size();
		}
		
		int total = 0;
		for(MessageList msg:msgs) {
			List<MsgDemoMap> demos = msgDemoMapDao.findByMessageId((int)msg.getMessage());
			if(demos != null && demos.size()>0) {
				total++;
			}
		}
		
		return total;
	}

}
