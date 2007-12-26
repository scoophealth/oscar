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

package org.oscarehr.PMmodule.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.BedDAO;
import org.oscarehr.PMmodule.dao.FacilityDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.RoomDAO;
import org.oscarehr.PMmodule.exception.RoomHasActiveBedsException;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.model.RoomDemographic;
import org.oscarehr.PMmodule.model.RoomType;
import org.oscarehr.PMmodule.service.BedManager;
import org.oscarehr.PMmodule.service.RoomDemographicManager;
import org.springframework.beans.factory.annotation.Required;

/**
 * Management of rooms
 */
public class RoomManager {

    private static final Log log = LogFactory.getLog(RoomManager.class);

    private static <T extends Exception> void handleException(T e) throws T {
        log.error(e);
        throw e;
    }

    private RoomDAO roomDAO;
    private BedManager bedManager;
    private RoomDemographicManager roomDemographicManager;
    private ProgramDao programDao;
    private BedDAO bedDAO;
    private FacilityDAO facilityDAO;

    /**
     * Get room
     *
     * @param roomId
     *            room identifier
     * @return room
     */
    public Room getRoom(Integer roomId) {
        if (roomId == null) {
            handleException(new IllegalArgumentException("roomId must not be null"));
        }

        Room room = roomDAO.getRoom(roomId);
        setAttributes(room);

        return room;
    }

    /**
     * Get rooms
     *
     * @return list of rooms
     */
    public Room[] getRooms(Integer facilityId) {
        Room[] rooms = roomDAO.getRooms(facilityId, null, null);

        for (Room room : rooms) {
            setAttributes(room);
        }

        return rooms;
    }

    /**
     * Get rooms
     *
     * @return array of rooms that have beds assigned to them.
     */
    public Room[] getRooms(Integer[][] roomsOccupancy) {
    	
    	if(roomsOccupancy == null || roomsOccupancy[0] == null  ||  roomsOccupancy[0].length == 0){
    		return null;
    	}
    	Room[] rooms = new Room[roomsOccupancy[0].length];
    	for(int i=0; i < rooms.length; i++){
    		rooms[i] = getRoom(roomsOccupancy[0][i]);
    		//not needed, this is the number of beds assigned to this particular room -- which has its occupancy limit
    		//rooms[i].setOccupancy(roomsOccupancy[1][i]);
    	}

        return rooms;
    }
    
    /**
     * Get assigned rooms
     *
     * @return list of assigned rooms
     */
    public Room[] getAssignedBedRooms(Integer facilityId) {
        Room[] rooms = roomDAO.getAssignedBedRooms(facilityId, null, null);

        for (Room room : rooms) {
            setAttributes(room);
        }

        return rooms;
    }
    
    
	/**
	 * Get available rooms
	 *
	 * @param facilityId
	 * @param programId
	 * @param active           
	 * @return list of available bed rooms that have client assigned less than 
	 * 			its occupancy limit. 
	 */
    @SuppressWarnings("unchecked")
    public Room[] getAvailableRooms(Integer facilityId, Integer programId, Boolean active) {
    	Room[] rooms = roomDAO.getAssignedBedRooms(facilityId, programId, active);
    	List<RoomDemographic> roomDemograhics = null;
    	List<Room> availableRooms = new ArrayList<Room>();
    	
    	for(int i=0; rooms != null  &&  i < rooms.length; i++){

			Bed[] bedsForRoom = null;
    		if(rooms != null && rooms.length > 0){
   				//room[i].getRoomOccupancy - bedManager.getBedsInRoom(room[i].getRoomId()) > 0
				bedsForRoom = bedManager.getBedsByRoom(rooms[i].getId());
				
				if(bedsForRoom != null){
					if(rooms[i].getOccupancy().intValue() -  bedsForRoom.length > 0){
    					availableRooms.add(rooms[i]);
					}
				}else{//if no bed assigned to this room, try checking whether it is in RoomDemographic table
					  //i.e. whether this room has been assigned to client without associating it with beds
		    		roomDemograhics = roomDemographicManager.getRoomDemographicByRoom(rooms[i].getId());
		    		
		    		if(roomDemograhics != null && roomDemograhics.size() > 0){
		    			if(rooms[i].getOccupancy().intValue() - roomDemograhics.size() > 0){
		    				availableRooms.add(rooms[i]);
		    			}
		    		}else{
		    			availableRooms.add(rooms[i]);
		    		}
				}//end of  bedsForRoom == null
    			
    		}else{//end of rooms != null
    			//availableRooms.add(rooms[i]);
    		}//end of rooms == null
    	}
		log.debug("getAvailableRooms(): availableRooms = " + availableRooms.size());
		return (Room[]) availableRooms.toArray(new Room[availableRooms.size()]);
	}
    

    /**
     * Get room types
     *
     * @return list of room types
     */
    public RoomType[] getRoomTypes() {
        return roomDAO.getRoomTypes();
    }

    /**
     * Add new rooms
     *
     * @param numRooms
     *            number of rooms
     * @throws RoomHasActiveBedsException
     *             room has active beds
     */
    public void addRooms(Integer facilityId, int numRooms) throws RoomHasActiveBedsException {
        if (numRooms < 1) {
            handleException(new IllegalArgumentException("numRooms must be greater than or equal to 1"));
        }

        RoomType defaultRoomType = getDefaultRoomType();

        for (int i = 0; i < numRooms; i++) {
            saveRoom(Room.create(facilityId, defaultRoomType));
        }
    }

    /**
     * Save rooms
     *
     * @param rooms
     *            rooms to create or update
     * @throws RoomHasActiveBedsException
     *             room has active beds
     */
    public void saveRooms(Room[] rooms) throws RoomHasActiveBedsException {
        if (rooms == null) {
            handleException(new IllegalArgumentException("rooms must not be null"));
        }

        for (Room room : rooms) {
            saveRoom(room);
        }
    }

    void saveRoom(Room room) throws RoomHasActiveBedsException {
        validate(room);
        roomDAO.saveRoom(room);
    }

    RoomType getDefaultRoomType() {
        for (RoomType roomType : getRoomTypes()) {
            if (roomType.isDefault()) {
                return roomType;
            }
        }

        handleException(new IllegalStateException("no default room type"));

        return null;
    }

    void setAttributes(Room room) {
        Integer roomTypeId = room.getRoomTypeId();
        room.setRoomType(roomDAO.getRoomType(roomTypeId));
        room.setFacility(facilityDAO.getFacility(room.getFacilityId()));

        Integer programId = room.getProgramId();

        if (programId != null) {
            room.setProgram(programDao.getProgram(programId));
        }
    }

    void validate(Room room) throws RoomHasActiveBedsException {
        if (room == null) {
            handleException(new IllegalStateException("room must not be null"));
        }

        validateRoom(room);
        validateRoomType(room.getRoomTypeId());
        validateProgram(room.getProgramId());
    }

    void validateRoom(Room room) throws RoomHasActiveBedsException {
        Integer roomId = room.getId();

        if (roomId != null) {
            if (!roomDAO.roomExists(roomId)) {
                handleException(new IllegalStateException("no room with id : " + roomId));
            }

            if (!room.isActive() && bedDAO.getBedsByRoom(roomId, Boolean.TRUE).length > 0) {
                handleException(new RoomHasActiveBedsException("room with id : " + roomId + " has active beds"));
            }
        }
    }

    void validateRoomType(Integer roomTypeId) {
        if (!roomDAO.roomTypeExists(roomTypeId)) {
            handleException(new IllegalStateException("no room type with id : " + roomTypeId));
        }
    }

    void validateProgram(Integer programId) {
        if (programId != null && !programDao.isBedProgram(programId)) {
            handleException(new IllegalStateException("no bed program with id : " + programId));
        }
    }

    @Required
    public void setFacilityDAO(FacilityDAO facilityDAO) {
        this.facilityDAO = facilityDAO;
    }

    @Required
    public void setRoomDAO(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    @Required
    public void setProgramDao(ProgramDao programDao) {
        this.programDao = programDao;
    }

    @Required
    public void setBedDAO(BedDAO bedDAO) {
        this.bedDAO = bedDAO;
    }

    @Required
    public void setBedManager(BedManager bedManager) {
        this.bedManager = bedManager;
    }

    @Required
    public void setRoomDemographicManager(RoomDemographicManager roomDemographicManager) {
        this.roomDemographicManager = roomDemographicManager;
    }

}