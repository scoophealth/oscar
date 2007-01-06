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

import org.oscarehr.PMmodule.dao.BedDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.RoomDAO;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.model.RoomType;
import org.oscarehr.PMmodule.service.RoomManager;

/**
 * Implementation of RoomManager interface
 */
public class RoomManagerImpl implements RoomManager {

	private RoomDAO roomDAO;

	private ProgramDao programDAO;

	private BedDAO bedDAO;

	public void setRoomDAO(RoomDAO roomDAO) {
		this.roomDAO = roomDAO;
	}

	public void setProgramDAO(ProgramDao programDAO) {
		this.programDAO = programDAO;
	}
	
	public void setBedDAO(BedDAO bedDAO) {
	    this.bedDAO = bedDAO;
    }

	/**
	 * @see org.oscarehr.PMmodule.service.RoomManager#getRoom(java.lang.Integer)
	 */
	public Room getRoom(Integer roomId) {
		Room room = roomDAO.getRoom(roomId);
		setAttributes(room);

		return room;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.RoomManager#getRooms()
	 */
	public Room[] getRooms() {
		Room[] rooms = roomDAO.getRooms(null, null);

		for (Room room : rooms) {
			setAttributes(room);
		}

		return rooms;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.RoomManager#getRoomTypes()
	 */
	public RoomType[] getRoomTypes() {
		return roomDAO.getRoomTypes();
	}

	/**
	 * @see org.oscarehr.PMmodule.service.RoomManager#addRooms(int)
	 */
	public void addRooms(int numRooms) {
		RoomType roomType = getDefaultRoomType();
		
		for (int i = 0; i < numRooms; i++) {
			Room newRoom = Room.create(roomType);
			validate(newRoom);
			roomDAO.saveRoom(newRoom);
        }
	}

	/**
	 * @see org.oscarehr.PMmodule.service.RoomManager#saveRooms(java.util.List)
	 */
	public void saveRooms(Room[] rooms) {
		if (rooms == null) {
			throw new IllegalArgumentException("array rooms is null");
		}

		for (Room room : rooms) {
			validate(room);
			roomDAO.saveRoom(room);
		}
	}

	RoomType getDefaultRoomType() {
    	for (RoomType roomType : getRoomTypes()) {
    		if (roomType.isDefault()) {
    			return roomType;
    		}
    	}
    
    	throw new IllegalStateException("no default room type");
    }

	void setAttributes(Room room) {
		// room type is mandatory
		Integer roomTypeId = room.getRoomTypeId();
		room.setRoomType(roomDAO.getRoomType(roomTypeId));

		// program is optional
		Integer programId = room.getProgramId();

		if (programId != null) {
			room.setProgram(programDAO.getProgram(programId));
		}
	}

	void validate(Room room) {
		if (room == null) {
			throw new IllegalArgumentException("room is null");
		}

		validateRoom(room);

		// mandatory
		validateRoomType(room.getRoomTypeId());

		// optional
		validateProgram(room.getProgramId());
	}

	void validateRoom(Room room) {
		Integer roomId = room.getId();

		if (roomId != null) {
			if (!roomDAO.roomExists(roomId)) {
				throw new IllegalArgumentException("no room with id : " + roomId);
			}

			if (!room.isActive() && bedDAO.getBeds(roomId, true).length > 0) {
				throw new IllegalStateException("inactive room with id : " + roomId + " has active beds");
			}
		}
	}

	void validateRoomType(Integer roomTypeId) {
		if (!roomDAO.roomTypeExists(roomTypeId)) {
			throw new IllegalArgumentException("no room type with id : " + roomTypeId);
		}
	}

	void validateProgram(Integer programId) {
		if (programId != null && !programDAO.isBedProgram(programId)) {
			throw new IllegalArgumentException("no bed program with id : " + programId);
		}
	}

}