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

package org.oscarehr.PMmodule.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.BedDemographicDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.RoomDAO;
import org.oscarehr.PMmodule.dao.RoomDemographicDao;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.model.RoomDemographic;
import org.oscarehr.PMmodule.service.RoomDemographicManager;
import org.oscarehr.util.MiscUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of RoomDemographicManager interface
 */
@Transactional
public class RoomDemographicManagerImpl implements RoomDemographicManager {

	private static final Logger log=MiscUtils.getLogger();
	
	private static <T extends Exception> void handleException(T e) throws T {
		log.error("Error", e);
		throw e;
	}

	private BedDemographicDao bedDemographicDao;
	private RoomDemographicDao roomDemographicDao;
	private ProviderDao providerDao;
	private DemographicDao demographicDao;
	private RoomDAO roomDAO;

	public void setRoomDemographicDao(RoomDemographicDao roomDemographicDao) {
		this.roomDemographicDao = roomDemographicDao;
	}

	public void setProviderDao(ProviderDao providerDao) {
		this.providerDao = providerDao;
	}

	public void setBedDemographicDao(BedDemographicDao bedDemographicDao) {
		this.bedDemographicDao = bedDemographicDao;
	}

	public void setDemographicDao(DemographicDao demographicDao) {
		this.demographicDao = demographicDao;
	}

	public void setRoomDAO(RoomDAO roomDAO) {
		this.roomDAO = roomDAO;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.RoomDemographicManager#roomDemographicExists(java.lang.Integer)
	 */
	public boolean roomDemographicExists(Integer roomId) {
		return roomDemographicDao.roomDemographicExists(roomId);
	}

	public int getRoomOccupanyByRoom(Integer roomId){
		return roomDemographicDao.getRoomOccupanyByRoom(roomId);
	}
	
	/**
	 * @see org.oscarehr.PMmodule.service.RoomDemographicManager#getRoomDemographicByRoom(java.lang.Integer)
	 */
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
	            Room room=roomDAO.getRoom(roomDemographic.getRoomId());
	            if (room.getFacilityId()!=null && facilityId.intValue()!=room.getFacilityId().intValue()) return(null);
	        }

	        setAttributes(roomDemographic);
		}
		return roomDemographic;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.RoomDemographicManager#saveRoomDemographic(org.oscarehr.PMmodule.model.RoomDemographic)
	 */
	public void saveRoomDemographic(RoomDemographic roomDemographic) {
		if (roomDemographic == null) {
			handleException(new IllegalArgumentException("roomDemographic must not be null"));
		}
		boolean isNoRoomAssigned = (roomDemographic.getId().getRoomId().intValue() == 0);
		
		if(!isNoRoomAssigned){
			validate(roomDemographic);
		}

		// only discharge out of previous room in the same facility
        Room room=roomDAO.getRoom(roomDemographic.getRoomId());
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
	
	/**
	 * @see org.oscarehr.PMmodule.service.RoomDemographicManager#deleteRoomDemographic(RoomDemographic)
	 */
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
		if (!roomDAO.roomExists(roomId)) {
			handleException(new IllegalArgumentException("no room with id : " + roomId));
		}
	}

	void validateDemographic(Integer demographicNo) {
		if (!demographicDao.clientExists(demographicNo)) {
			handleException(new IllegalArgumentException("no demographic with id : " + demographicNo));
		}
	}

}
