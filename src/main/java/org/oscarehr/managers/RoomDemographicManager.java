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

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.BedDemographicDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.RoomDao;
import org.oscarehr.common.dao.RoomDemographicDao;
import org.oscarehr.common.model.BedDemographic;
import org.oscarehr.common.model.Room;
import org.oscarehr.common.model.RoomDemographic;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomDemographicManager {

	private Logger log = MiscUtils.getLogger();
	
	private <T extends Exception> void handleException(T e) throws T {
		log.error("Error", e);
		throw e;
	}

	@Autowired
	private BedDemographicDao bedDemographicDao;
	@Autowired
	private RoomDemographicDao roomDemographicDao;
	@Autowired
	private ProviderDao providerDao;
	@Autowired
	private DemographicDao demographicDao;
	@Autowired
	private RoomDao roomDao;

	
	public boolean roomDemographicExists(Integer roomId) {
		return roomDemographicDao.roomDemographicExists(roomId);
	}

	public int getRoomOccupanyByRoom(Integer roomId){
		return roomDemographicDao.getRoomOccupanyByRoom(roomId);
	}
	

	public List<RoomDemographic> getRoomDemographicByRoom(Integer roomId) {
		if (roomId == null) {
			handleException(new IllegalArgumentException("roomId must not be null"));
		}
		List<RoomDemographic> roomDemographicList = null;
		roomDemographicList = roomDemographicDao.getRoomDemographicByRoom(roomId);
			
		if(roomDemographicList != null  &&  roomDemographicList.size() > 0){
			//Demographic demographic = demographicDao.getClientByDemographicNo(roomDemographicList.get(0).getId().getDemographicNo());
			//roomDemographicList.get(0).setDemographic(demographic);
		}
		return roomDemographicList;
	}

	public RoomDemographic getRoomDemographicByDemographic(Integer demographicNo, Integer facilityId) {
		if (demographicNo == null) {
			handleException(new IllegalArgumentException("demographicNo must not be null"));
		}
		RoomDemographic roomDemographic = roomDemographicDao.getRoomDemographicByDemographic(demographicNo);

		if (roomDemographic != null) {			
	        // filter in facility
	        if (facilityId!=null)
	        {
	            Room room=roomDao.getRoom(roomDemographic.getId().getRoomId());
	            if (room.getFacilityId()!=null && facilityId.intValue()!=room.getFacilityId().intValue()) return(null);
	        }

	        setAttributes(roomDemographic);
		}
		return roomDemographic;
	}


	public void saveRoomDemographic(RoomDemographic roomDemographic) {
		if (roomDemographic == null) {
			handleException(new IllegalArgumentException("roomDemographic must not be null"));
		}
		boolean isNoRoomAssigned = (roomDemographic.getId().getRoomId().intValue() == 0);
		
		if(!isNoRoomAssigned){
			validate(roomDemographic);
		}

		// only discharge out of previous room in the same facility
        Room room=roomDao.getRoom(roomDemographic.getId().getRoomId());
		RoomDemographic roomDemographicPrevious = getRoomDemographicByDemographic(roomDemographic.getId().getDemographicNo(), room.getFacilityId()); 
		if(roomDemographicPrevious != null){
			deleteRoomDemographic(roomDemographicPrevious);
		}
		if(!isNoRoomAssigned){
			roomDemographicDao.saveRoomDemographic(roomDemographic);
		}
	}

	public void cleanUpBedTables(RoomDemographic roomDemographic){
		
		if(roomDemographic == null){
			return;
		}
		BedDemographic bedDemographic = bedDemographicDao.getBedDemographicByDemographic(
				roomDemographic.getId().getDemographicNo());
		if(bedDemographic != null){
			bedDemographicDao.deleteBedDemographic(bedDemographic);
		}
	}
	
	
	public void deleteRoomDemographic(RoomDemographic roomDemographic) {
		if (roomDemographic == null) {
			handleException(new IllegalArgumentException("roomDemographic must not be null"));
		}
		
		roomDemographicDao.deleteRoomDemographic(roomDemographic);
	}

	void setAttributes(RoomDemographic roomDemographic) {

//		roomDemographic.setAssignEnd(duration);

		String providerNo = roomDemographic.getProviderNo();
		roomDemographic.setProvider(providerDao.getProvider(providerNo));
	}

	void validate(RoomDemographic roomDemographic) {
		validateProvider(roomDemographic.getProviderNo());
		validateRoom(roomDemographic.getId().getRoomId());
		validateDemographic(roomDemographic.getId().getDemographicNo());
	}

	void validateRoomDemographic(RoomDemographic roomDemographic) {
		if (!roomDemographic.isValidAssign()) {
			handleException(new IllegalArgumentException("invalid Assignvation: " + roomDemographic.getAssignStart() + " - " + roomDemographic.getAssignEnd()));
		}
	}

	void validateProvider(String providerId) {
		if (!providerDao.providerExists(providerId)) {
			handleException(new IllegalArgumentException("no provider with id : " + providerId));
		}
	}

	void validateRoom(Integer roomId) {
		if (!roomDao.roomExists(roomId)) {
			handleException(new IllegalArgumentException("no room with id : " + roomId));
		}
	}

	void validateDemographic(Integer demographicNo) {
		if (!demographicDao.clientExists(demographicNo)) {
			handleException(new IllegalArgumentException("no demographic with id : " + demographicNo));
		}
	}
}
