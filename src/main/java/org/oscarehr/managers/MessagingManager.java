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
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.common.model.MessageList;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.oscarehr.util.SpringUtils;
import oscar.log.LogAction;

@Service
public class MessagingManager {

	@Autowired
	private MessageListDao messageListDao;
	
	public List<MessageList> getMyInboxMessages(LoggedInInfo loggedInInfo, String providerNo, int offset, int limit) {		
		return getMyInboxMessages(loggedInInfo, providerNo, null, offset, limit);
	}
	
	public List<MessageList> getMyInboxMessages(LoggedInInfo loggedInInfo, String providerNo, String status, int offset, int limit) {		
		
		List<MessageList> msgs = messageListDao.search(providerNo, status, offset, limit);
		
		for(MessageList msg:msgs) {
	        	LogAction.addLogSynchronous(loggedInInfo, "MessagingManager.getMyInboxMessages", "msglistid="+msg.getId());
		}
		 
		return msgs;
	}
	
	public Integer getMyInboxMessagesCount(LoggedInInfo loggedInInfo, String providerNo, String status) {
		
		Integer result = messageListDao.searchAndReturnTotal(providerNo, status);
		 
		return result;
	}
	
	public int getMyInboxMessageCount(LoggedInInfo loggedInInfo, String providerNo, boolean onlyWithPatientAttached) {		
		
		if(!onlyWithPatientAttached) {
			return getMyInboxMessagesCount(loggedInInfo, providerNo, MessageList.STATUS_NEW);
		} else {
			return messageListDao.findUnreadByProviderAndAttachedCount(providerNo);
		}
		
	}
	
	public String getLabRecallMsgSubjectPref(LoggedInInfo loggedInInfo){
		String subject = "";
		String providerNo = loggedInInfo.getLoggedInProviderNo();
		
	    	UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
	    	UserProperty prop = userPropertyDao.getProp(providerNo, UserProperty.LAB_RECALL_MSG_SUBJECT);
	    	
	    	if(prop!=null){
	    		subject = prop.getValue();
	    	}
    	
		return subject;
	}

	
	public String getLabRecallDelegatePref(LoggedInInfo loggedInInfo){
		String delegate = "";
		String providerNo = loggedInInfo.getLoggedInProviderNo();

	    	UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
	    	UserProperty prop = userPropertyDao.getProp(providerNo, UserProperty.LAB_RECALL_DELEGATE);
	    	
	    	if(prop!=null){
	    		delegate = prop.getValue();		
	    	}

		return delegate;
	}
	
	public String getDelegateName(String delegate){
		String delegateName = "";
		ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
		ProviderData pd = dao.findByProviderNo(delegate);
		
		delegateName = pd.getLastName() + ", " + pd.getFirstName();
			
		return delegateName;
	}
	
}
