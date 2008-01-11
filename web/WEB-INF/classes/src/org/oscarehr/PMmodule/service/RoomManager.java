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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.BedDAO;
import org.oscarehr.PMmodule.dao.FacilityDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.RoomDAO;
import org.oscarehr.PMmodule.exception.RoomHasActiveBedsException;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedDemographic;
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
    private BedDemographicManager bedDemographicManager;
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
     * @return array of rooms that have beds assigned to them.
     */
    
    public Room[] getRooms(Integer[][] roomsOccupancy) {
    	if(roomsOccupancy == null || roomsOccupancy[0] == null  ||  roomsOccupancy[0].length == 0){
    		return null;
    	}
    	Room[] rooms = new Room[roomsOccupancy.length];
    	for(int i=0; i < rooms.length; i++){
    		rooms[i] = getRoom(roomsOccupancy[i][0]);
    		//not needed, this is the number of beds assigned to this particular room -- which has its occupancy limit
    		//rooms[i].setOccupancy(roomsOccupancy[1][i]);
    	}
        return rooms;
    }
    
    /**
     * Get assigned rooms
     * @return list of assigned rooms
     */
    public Room[] getAssignedBedRooms(Integer facilityId, Integer programId, Boolean active) {
        Room[] rooms = roomDAO.getAssignedBedRooms(facilityId, programId, active);

        for (Room room : rooms) {
            setAttributes(room);
        }
        return rooms;
    }
   
    /**
     * Get assigned rooms
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
     * Get rooms that are not full
     * @return list of unfilled rooms
     */
    public Room[] getUnfilledRoomIds(Bed[] beds) {
    	
    	List<Room> roomList = new ArrayList<Room>();
    	Integer[][] roomIdAndOccupancy = calculateOccupancyAsNumOfBedsAssignedToRoom(beds);

		if(roomIdAndOccupancy == null){
			return null;
		}
    	Room[] rooms = getRooms(roomIdAndOccupancy); 

    	//rooms subject to condition: roomCapacity - occupancy > 0
    	for(int i=0; rooms != null  &&  i < rooms.length; i++){
    		
    		for(int j=0; j < roomIdAndOccupancy.length; j++){
    			
    			if(  rooms[i].getId().intValue() == roomIdAndOccupancy[j][0].intValue() ){
   			
    				if( rooms[i].getOccupancy().intValue() - roomIdAndOccupancy[j][1].intValue() >= 0 ){
    					roomList.add(rooms[i]);
    				}
    			}
    		}
    	}

    	if(roomList == null || roomList.isEmpty()){
    		return null;
    	}
        return (Room[]) roomList.toArray(new Room[roomList.size()]);
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
    	//rooms of particular facilityId, programId, active=1, (assignedBed=1 or assignedBed=0) 
    	Room[] rooms = roomDAO.getRooms(facilityId, programId, active);
    	List<Room> availableRooms = new ArrayList<Room>();
    	
//    	get rooms that are not full or clients can still be assigned to these rooms
    	for(int i=0; rooms != null  &&  i < rooms.length; i++){
    		
    	List<RoomDemographic> roomDemograhics = null;
    	
    	List clientsFromBedDemographic = new ArrayList();
    	List clientsFromRoomDemographic = new ArrayList();
    	int numOfUniqueClientsAssignedToRoom = 0;
    	
		/*
			roomId -->  get  all (multiple) bedIds from  table 'bed'
			       -->  get  all demographicNo  from  table  'room_demographic'
			bedIds -->  get all (1 to 1 relationship) demographicNo  from  table  'bed_demographic'
			numOfClientsAssignedToRoom  ==  sum of  all unique demographicNo (subtracting the duplicates
			of clients from both 'bed_demographic' & 'room_demographic' tables)
		*/    	
    	  	

			Bed[] bedsForRoom = null;
    		if(rooms != null && rooms.length > 0){
   				//get  all bedIds from  table 'bed' via room[i].id
				bedsForRoom = bedManager.getBedsByRoom(rooms[i].getId());
				int bedsInOneRoom = bedsForRoom.length;
				
				if(bedsForRoom != null){
					//get all demographicNo  from  table  'bed_demographic' via Bed[j].id -- 1 to 1 relationship
					for(int j=0; j < bedsForRoom.length; j++){
						BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByBed(bedsForRoom[j].getId()); 
						if(bedDemographic != null ){
							clientsFromBedDemographic.add(bedDemographic.getId().getDemographicNo());
						}
					}
				}
				//get  all demographicNo  from  table  'room_demographic' via room[i].id	
				roomDemograhics = roomDemographicManager.getRoomDemographicByRoom(rooms[i].getId());
				
				if(roomDemograhics != null  &&  roomDemograhics.size() == 1){
					clientsFromRoomDemographic.add(((RoomDemographic)roomDemograhics.get(0)).getId().getDemographicNo());
				}
				numOfUniqueClientsAssignedToRoom = getNumOfUniqueClientsAssignedToRoom(clientsFromBedDemographic, clientsFromRoomDemographic);	
				
				//What is the meaning occupancy, how many beds in the room ? why is it always 1 ? 
				//if(rooms[i].getOccupancy().intValue() -  numOfUniqueClientsAssignedToRoom > 0){
				if(bedsInOneRoom -  numOfUniqueClientsAssignedToRoom > 0){
					availableRooms.add(rooms[i]);
				}
    			
    		}else{//end of rooms != null
    			return null;
    		}//end of rooms == null
    	}
		log.debug("getAvailableRooms(): availableRooms = " + availableRooms.size());
		return (Room[]) availableRooms.toArray(new Room[availableRooms.size()]);
	}

    private int getNumOfUniqueClientsAssignedToRoom(List clientsFromBedDemographic, List clientsFromRoomDemographic){
    	
    	if(clientsFromBedDemographic == null  &&  clientsFromRoomDemographic == null){
    		return 0;
    	}
    	List<Integer> clientsCombined = new ArrayList<Integer>();
    	clientsCombined.addAll(clientsFromBedDemographic);
    	clientsCombined.addAll(clientsFromRoomDemographic);
    	
    	Collections.sort(clientsCombined);
    	Set treeSet = new TreeSet(clientsCombined);
    	if(treeSet == null){
    		return 0;
    	}
    	return treeSet.size();
    }
    
    /**
     * Calculate occupancy number as number of beds assigned to each room when 
     * assignedBed attribute is set to 'Y'
     * @param beds
     */
    public Integer[][] calculateOccupancyAsNumOfBedsAssignedToRoom(Bed[] beds){
    	if(beds == null){
    		return null;
    	}
    	List roomIdKeys = new ArrayList();
    	List roomIdCounts = new ArrayList();
    	Integer[][] roomsOccupancy = null;
    	int count = 1;
    	int roomIdKey = -1;

    	Integer[] roomIds = new Integer[beds.length];
    	for(int i=0; i < beds.length; i++){
    		roomIds[i] = beds[i].getRoomId();
    	}
    	
    	Arrays.sort( roomIds  );  
		
		//adding up repeated roomIds as the number of beds assigned to this roomId
    	if(roomIds != null  &&  roomIds.length > 0){
    		roomIdKey = roomIds[0];
    		for( int i=1; i < roomIds.length; i++  ){
    			if( roomIdKey  == roomIds[i] ){
    				count++;
    			}else{
     				if(i > 0){
    					roomIdKeys.add(roomIdKey);
    				}
    				roomIdCounts.add( new Integer( count ) );
    				count = 1;
    			}
    			roomIdKey = roomIds[i];
    			if( i == roomIds.length - 1 ){
    				roomIdKeys.add(roomIdKey);
    				roomIdCounts.add( new Integer( count ) );
    			}
    		}
    	}
    	
    	if(roomIdKeys == null  ||  roomIdKeys.size() <= 0){
    		return null;
    	}
    	
    	
    	roomsOccupancy = new Integer[roomIdKeys.size()][2];
    	for(int i=0; i < roomsOccupancy.length; i++){
    		
	        roomsOccupancy[i][0] = (Integer)roomIdKeys.get(i);
	        roomsOccupancy[i][1] = (Integer)roomIdCounts.get(i);
    	}
    	
    	return roomsOccupancy;
    }
    
    public boolean isAssignedBed( String roomId, Room[] rooms ){
    	
    	if(roomId == null  ||  rooms == null){
    		return false;
    	}
    	for(int i=0; i < rooms.length; i++){
			try{
	    		if( Integer.parseInt(roomId) == rooms[i].getId().intValue()  &&  rooms[i].getAssignedBed().intValue() == 1){
	    			return true;
	    		}else if( Integer.parseInt(roomId) == rooms[i].getId().intValue()  &&  rooms[i].getAssignedBed().intValue() == 0){
	    			return false;
	    		}
			}catch(NumberFormatException nfex){
				return false;
			}
    	}
    	return false;
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
    
    @Required
    public void setBedDemographicManager(BedDemographicManager bedDemographicManager) {
        this.bedDemographicManager = bedDemographicManager;
    }

}