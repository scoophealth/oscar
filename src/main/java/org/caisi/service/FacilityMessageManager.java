/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.caisi.service;

import java.util.List;

import org.oscarehr.common.dao.FacilityMessageDao;
import org.oscarehr.common.model.FacilityMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class FacilityMessageManager {

	@Autowired
	private FacilityMessageDao facilityMessageDao;	
	
	public FacilityMessage getMessage(String messageId) {
		return facilityMessageDao.find(Integer.valueOf(messageId));
	}
	
	public void saveFacilityMessage(FacilityMessage msg) {
		if(msg.getId() == null || msg.getId().intValue() == 0) {
			msg.setId(null);
			facilityMessageDao.persist(msg);
		} else {
			facilityMessageDao.merge(msg);
		}
	}
	
	public List<FacilityMessage> getMessages() {
		return facilityMessageDao.getMessages();
	}

	public List<FacilityMessage> getMessagesByFacilityId(Integer facilityId) {
		if (facilityId == null || facilityId.intValue() == 0) {           
        		return null;
        	}
		return facilityMessageDao.getMessagesByFacilityId(facilityId);
	}
	
	public List<FacilityMessage> getMessagesByFacilityIdOrNull(Integer facilityId) {
		if (facilityId == null || facilityId.intValue() == 0) {           
        		return null;
        	}
		return facilityMessageDao.getMessagesByFacilityIdOrNull(facilityId);
	}
	
	public List<FacilityMessage> getMessagesByFacilityIdAndProgramId(Integer facilityId, Integer programId) {
		if (facilityId == null || facilityId.intValue() == 0) {           
        		return null;
        	}
		return facilityMessageDao.getMessagesByFacilityIdAndProgramId(facilityId,programId);
	}
	
	public List<FacilityMessage> getMessagesByFacilityIdOrNullAndProgramIdOrNull(Integer facilityId, Integer programId) {
		if (facilityId == null || facilityId.intValue() == 0) {           
        		return null;
        	}
		return facilityMessageDao.getMessagesByFacilityIdOrNullAndProgramIdOrNull(facilityId,programId);
	}
}
