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
package org.oscarehr.ws.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.oscarehr.common.model.MessageList;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.MessagingManager;
import org.oscarehr.ws.rest.conversion.MessagingConverter;
import org.oscarehr.ws.rest.to.MessagingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/messaging")
@Component("messagingService")
public class MessagingService extends AbstractServiceImpl {

	@Autowired
	private MessagingManager messagingManager; 
	
	private MessagingConverter messagingConverter = new MessagingConverter();
	

	@GET
	@Path("/unread")
	@Produces("application/json")
	public MessagingResponse getMyUnreadMessages(@QueryParam("startIndex") int startIndex, @QueryParam("limit") int limit) {
		Provider provider=getCurrentProvider();
		List<MessageList> msgs = messagingManager.getMyInboxMessages(getLoggedInInfo(), provider.getProviderNo(),MessageList.STATUS_NEW,startIndex,limit);
		
		MessagingResponse result = new MessagingResponse();
		result.getContent().addAll(messagingConverter.getAllAsTransferObjects(getLoggedInInfo(),msgs)); 
		
		if(msgs.size()==limit) {
			 result.setTotal(messagingManager.getMyInboxMessagesCount(getLoggedInInfo(), provider.getProviderNo(),MessageList.STATUS_NEW));
		} else {
			result.setTotal(msgs.size());
		}
		
		
		return result;
	}
	
	
	@GET
	@Path("/count")
	public int getMyUnreadMessages(@QueryParam("demoAttachedOnly") boolean demoAttachedOnly) {
		Provider provider=getCurrentProvider();

		int count = messagingManager.getMyInboxMessageCount(getLoggedInInfo(), provider.getProviderNo(),demoAttachedOnly);
		
		return count;
	}
	
}
