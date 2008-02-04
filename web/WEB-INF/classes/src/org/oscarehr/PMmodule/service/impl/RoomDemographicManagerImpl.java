/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.BedDAO;
import org.oscarehr.PMmodule.dao.BedDemographicDAO;
import org.oscarehr.PMmodule.dao.RoomDemographicDAO;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.RoomDAO;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.RoomDemographic;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.service.RoomDemographicManager;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;

/**
 * Implementation of RoomDemographicManager interface
 */
public class RoomDemographicManagerImpl implements RoomDemographicManager {

	private static final Log log = LogFactory.getLog(RoomDemographicManagerImpl.class);
	
	private static <T extends Exception> void handleException(T e) throws T {
		log.error(e);
		throw e;
	}

	private BedDemographicDAO bedDemographicDAO;
	private RoomDemographicDAO roomDemographicDAO;
	private ProviderDao providerDAO;
	private BedDAO bedDAO;
	private ClientDao demographicDAO;
	private RoomDAO roomDAO;
	private ProgramDao programDAO;

	public void setRoomDemographicDAO(RoomDemographicDAO roomDemographicDAO) {
		this.roomDemographicDAO = roomDemographicDAO;
	}

	public void setProviderDAO(ProviderDao providerDAO) {
		this.providerDAO = providerDAO;
	}

	public void setBedDAO(BedDAO bedDAO) {
		this.bedDAO = bedDAO;
	}

	public void setBedDemographicDAO(BedDemographicDAO bedDemographicDAO) {
		this.bedDemographicDAO = bedDemographicDAO;
	}

	public void setDemographicDAO(ClientDao demographicDAO) {
		this.demographicDAO = demographicDAO;
	}

	public void setRoomDAO(RoomDAO roomDAO) {
		this.roomDAO = roomDAO;
	}

	public void setProgramDAO(ProgramDao programDAO) {
		this.programDAO = programDAO;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.RoomDemographicManager#demographicExists(java.lang.Integer)
	 */
	public boolean demographicExists(Integer roomId) {
		return roomDemographicDAO.demographicExists(roomId);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.RoomDemographicManager#getRoomDemographicByRoom(java.lang.Integer)
	 */
	public List<RoomDemographic> getRoomDemographicByRoom(Integer roomId) {
		if (roomId == null) {
			handleException(new IllegalArgumentException("roomId must not be null"));
		}
		List<RoomDemographic> roomDemographicList = null;
		roomDemographicList = roomDemographicDAO.getRoomDemographicByRoom(roomId);
			
		if(roomDemographicList != null  &&  roomDemographicList.size() > 0){
			//Demographic demographic = demographicDAO.getClientByDemographicNo(roomDemographicList.get(0).getId().getDemographicNo());
			//roomDemographicList.get(0).setDemographic(demographic);
		}
		return roomDemographicList;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.RoomDemographicManager#getRoomDemographicByDemographic(java.lang.Integer)
	 */
	public RoomDemographic getRoomDemographicByDemographic(Integer demographicNo) {
		if (demographicNo == null) {
			handleException(new IllegalArgumentException("demographicNo must not be null"));
		}
		RoomDemographic roomDemographic = roomDemographicDAO.getRoomDemographicByDemographic(demographicNo);
			
		if (roomDemographic != null) {			
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
		
		RoomDemographic roomDemographicPrevious = getRoomDemographicByDemographic(roomDemographic.getId().getDemographicNo()); 
		if(roomDemographicPrevious != null){
			deleteRoomDemographic(roomDemographicPrevious);
		}
		if(!isNoRoomAssigned){
			roomDemographicDAO.saveRoomDemographic(roomDemographic);
		}
	}

	public void cleanUpBedTables(RoomDemographic roomDemographic){
		
		if(roomDemographic == null){
			return;
		}
		BedDemographic bedDemographic = bedDemographicDAO.getBedDemographicByDemographic(
				roomDemographic.getId().getDemographicNo());
		if(bedDemographic != null){
			bedDemographicDAO.deleteBedDemographic(bedDemographic);
		}
	}
	
	/**
	 * @see org.oscarehr.PMmodule.service.RoomDemographicManager#deleteRoomDemographic(RoomDemographic)
	 */
	public void deleteRoomDemographic(RoomDemographic roomDemographic) {
		if (roomDemographic == null) {
			handleException(new IllegalArgumentException("roomDemographic must not be null"));
		}
		
		roomDemographicDAO.deleteRoomDemographic(roomDemographic);
	}

	void setAttributes(RoomDemographic roomDemographic) {

//		roomDemographic.setAssignEnd(duration);

		String providerNo = roomDemographic.getProviderNo();
		roomDemographic.setProvider(providerDAO.getProvider(providerNo));
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
		if (!providerDAO.providerExists(providerId)) {
			handleException(new IllegalArgumentException("no provider with id : " + providerId));
		}
	}

	void validateRoom(Integer roomId) {
		if (!roomDAO.roomExists(roomId)) {
			handleException(new IllegalArgumentException("no room with id : " + roomId));
		}
	}

	void validateDemographic(Integer demographicNo) {
		if (!demographicDAO.clientExists(demographicNo)) {
			handleException(new IllegalArgumentException("no demographic with id : " + demographicNo));
		}
	}

}