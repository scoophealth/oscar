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
package org.oscarehr.ws.rest.conversion;

import java.text.SimpleDateFormat;
import java.util.List;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.MessageTblDao;
import org.oscarehr.common.dao.MsgDemoMapDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.MessageList;
import org.oscarehr.common.model.MessageTbl;
import org.oscarehr.common.model.MsgDemoMap;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.model.MessageTo1;
import org.springframework.stereotype.Component;

@Component
public class MessagingConverter extends AbstractConverter<MessageList, MessageTo1> {

	 
	//private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	private MessageTblDao messageTblDao;
	
	@Override
	public MessageList getAsDomainObject(LoggedInInfo loggedInInfo,MessageTo1 t) throws ConversionException {
		MessageList d = new MessageList();
		
		return d;
	}

	@Override
	public MessageTo1 getAsTransferObject(LoggedInInfo loggedInInfo,MessageList t) throws ConversionException {
		
		messageTblDao = SpringUtils.getBean(MessageTblDao.class);
		MessageTbl msg = messageTblDao.find((int)t.getMessage());
		
		MsgDemoMapDao msgDemoMapDao = SpringUtils.getBean(MsgDemoMapDao.class);
		List<MsgDemoMap> demos = msgDemoMapDao.findByMessageId((int)t.getMessage());
		
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		
		if(msg == null) {
			return null;
		}
		
		
		MessageTo1 d = new MessageTo1();
		d.setId(msg.getId());
		
		String strDate = (new SimpleDateFormat("yyyy-MM-dd")).format(msg.getDate()) + " " + (new SimpleDateFormat("HH:mm")).format(msg.getTime());

		d.setDateOfMessage(strDate);
		d.setFromName(msg.getSentBy());
		d.setMessage(msg.getMessage());
		d.setSubject(msg.getSubject());
		
		if(demos != null && demos.size()>0) {
			Integer demographicNo = demos.get(0).getDemographic_no();
			if(demographicNo != null) {
				Demographic demographic = demographicDao.getDemographicById(demographicNo);
				d.setDemographicNo(demographicNo);
				d.setDemographicName(demographic!=null?demographic.getFormattedName():"N/A");
			}
		}
		
		return d;
	}

}
