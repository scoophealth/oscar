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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.exception.DuplicateRoomNameException;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.dao.BedDao;
import org.oscarehr.common.dao.RoomDao;
import org.oscarehr.common.dao.RoomTypeDao;
import org.oscarehr.common.model.Bed;
import org.oscarehr.common.model.Room;
import org.oscarehr.common.model.RoomDemographic;
import org.oscarehr.common.model.RoomType;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomManager {

	    private Logger log=MiscUtils.getLogger();

	    @Autowired
	    private RoomDao roomDao;
	    @Autowired
	    private RoomTypeDao roomTypeDao;
	    @Autowired
	    private RoomDemographicManager roomDemographicManager;
	    @Autowired
	    private ProgramDao programDao;
	    @Autowired
	    private BedDao bedDao;
	    

	    private <T extends Exception> void handleException(T e) throws T {
	        log.error("Error", e);
	        throw e;
	    }


	    
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

	        Room room = roomDao.getRoom(roomId);
	        setAttributes(room);

	        return room;
	    }

	    /**
	     * Get rooms
	     * @return list of rooms
	     */
	    public Room[] getRooms(Integer facilityId) {
	        Room[] rooms = roomDao.getRooms(facilityId, null, null);
	        if(rooms!=null){
	          for (Room room : rooms) {
	            setAttributes(room);
	          }
	        }
	        return rooms;
	    }

	    public Room[] getRooms(Integer facilityId, Integer programId, Boolean active) {
	        Room[] rooms = roomDao.getRooms(facilityId, programId, active);
	        if(rooms!=null){
	          for (Room room : rooms) {
	            setAttributes(room);
	          }
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
	        Room[] rooms = roomDao.getAssignedBedRooms(facilityId, programId, active);
	        if(rooms!=null){
	          for (Room room : rooms) {
	            setAttributes(room);
	          }
	        }
	        return rooms;
	    }

	    /**
	     * Get assigned rooms
	     * @return list of assigned rooms
	     */
	    public Room[] getAssignedBedRooms(Integer facilityId) {
	        Room[] rooms = roomDao.getAssignedBedRooms(facilityId, null, null);
	        if(rooms!=null){
	          for (Room room : rooms) {
	            setAttributes(room);
	          }
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
	        return roomList.toArray(new Room[roomList.size()]);
	    
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
	    public Room[] getAvailableRooms(Integer facilityId, Integer programId, Boolean active, String demographicNo) {
	    	//rooms of particular facilityId, programId, active=1, (assignedBed=1 or assignedBed=0)
	    	Room[] rooms = roomDao.getAvailableRooms(facilityId, programId, active);

	    	List<RoomDemographic> roomDemograhics = null;
	    	List<Room> availableRooms = new ArrayList<Room>();

	    	//get rooms that are not full or clients can still be assigned to these rooms
	    	//however, even if room capacity is reached, the rooms will still be added if that particular client is
	    	//assigned to that particular room.
	    	for(int i=0; rooms != null  &&  i < rooms.length; i++){
	    			int totalClientsInRoom = 0;
					//get  all (multiple) demographicNo  from  table  'room_demographic' via rooms[i].id
					roomDemograhics = roomDemographicManager.getRoomDemographicByRoom(rooms[i].getId());
					List<Integer> roomDemographicNumbers = new ArrayList<Integer>();

					if(roomDemograhics != null){
						totalClientsInRoom = roomDemograhics.size();
						for(int j=0; j < roomDemograhics.size(); j++){
							roomDemographicNumbers.add(roomDemograhics.get(j).getId().getDemographicNo() );
						}
					}
					//if client is assigned to this room, even if capacity reached, still display room in dropdown
					if(isClientAssignedToThisRoom(Integer.valueOf(demographicNo), roomDemographicNumbers)){
						availableRooms.add(rooms[i]);
					}else{
						//if client not in this room, only display room if capacity is not reached
						if(rooms[i].getOccupancy().intValue() -  totalClientsInRoom > 0){
								availableRooms.add(rooms[i]);
						}
					}

	    	}

			log.debug("getAvailableRooms(): availableRooms = " + availableRooms.size());
			return availableRooms.toArray(new Room[availableRooms.size()]);
		}

	    private boolean isClientAssignedToThisRoom(Integer demographicNo, List<Integer> demographicNumbers){
	     	try{
	    		if(demographicNo == null  ||  demographicNumbers == null){
	    			return false;
	    		}
	    		for(int i=0; i < demographicNumbers.size(); i++ ){
		    		if(demographicNo.intValue() == demographicNumbers.get(i).intValue()){
		    			return true;
		    		}
	    		}
	    		return false;
	    	}catch(Exception ex){
	    		return false;
	    	}
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
	    	List<Integer> roomIdKeys = new ArrayList<Integer>();
	    	List<Integer> roomIdCounts = new ArrayList<Integer>();
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

		        roomsOccupancy[i][0] = roomIdKeys.get(i);
		        roomsOccupancy[i][1] = roomIdCounts.get(i);
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
		 * Test to see whether room is assigned with beds - return true or
		 * assigned with no beds yet - return false
	     * @param roomId
	     */
	    public boolean isRoomAssignedWithBeds(Integer roomId){
	    	if(roomId == null){
	    		return false;
	    	}
	    	Bed[] beds = bedDao.getBedsByRoom(roomId, true);
	    	if(beds != null  &&  beds.length > 0){
	    		return true;
	    	}
	    	return false;
	    }

	    /**
		 * Used by AdmissionManager during processDischarge()  to  delete discharged
	     * program-related room/bed reservation records
	     * @param demographicNo
	     * @param programId
	     */
	    public boolean isRoomOfDischargeProgramAssignedToClient(Integer demographicNo, Integer programId){
	    	/*
	    	 *(1)admission.clientId ===[table:room_demographic]===>>  roomDemographic.roomId
			 *(2)roomDemographic.roomId ===[table:room]===>>   room.programId
			 *(3)Compare  admission.programId  with  room.programId
			 *   - if true -->  return true -- delete  roomDemographic record
			 *   - if false -->  return false -- do nothing
	    	 */

	    	if(demographicNo == null  ||  programId == null){
	    		return false;
	    	}
	    	Program program=programDao.getProgram(programId);
	    	RoomDemographic roomDemographic = roomDemographicManager.getRoomDemographicByDemographic(demographicNo, program.getFacilityId());
	    	if(roomDemographic != null){
	 	    	Room room = getRoom(roomDemographic.getId().getRoomId());
		    	if(room != null  &&  programId.intValue() == room.getProgramId().intValue()){
		    		return true;
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
	        return roomTypeDao.getRoomTypes();
	    }

	    /**
	     * Add new rooms
	     *
	     * @param numRooms
	     *            number of rooms
	     * @throws RoomHasActiveBedsException
	     *             room has active beds
	     */
	    public void addRooms(Integer facilityId, int numRooms) {
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
	    public void saveRooms(Room[] rooms) throws  DuplicateRoomNameException {
	        if (rooms == null) {
	            handleException(new IllegalArgumentException("rooms must not be null"));
	        }
	        //Check for duplicate room names.
	        for (int i = 0; i < rooms.length; i++) {
	        	for (int j = 0; j < rooms.length; j++) {
	        		if ((i != j) && (rooms[i].getName().equals(rooms[j].getName()) && rooms[i].getProgramId().equals(rooms[j].getProgramId()))) {
	        			handleException(new DuplicateRoomNameException(rooms[i].getName()));
	        			return;
	        		}
	        	}
	        }
	        for (Room room : rooms) {
	            saveRoom(room);
	        }
	    }

	    void saveRoom(Room room)  {
	        validate(room);
	        roomDao.saveRoom(room);
	    }

	    public void deleteRoom(Room room)  {
	        roomDao.deleteRoom(room);
	    }

	    RoomType getDefaultRoomType() {
	        for (RoomType roomType : getRoomTypes()) {
	            if (roomType.getDflt()) {
	                return roomType;
	            }
	        }

	        handleException(new IllegalStateException("no default room type"));

	        return null;
	    }


	    void setAttributes(Room room) {

	    	if(room == null){
	    		return;
	    	}
	        Integer roomTypeId = room.getRoomTypeId();
	        room.setRoomType(roomTypeDao.getRoomType(roomTypeId));

	        Integer programId = room.getProgramId();

	        if (programId != null) {
	            room.setProgram(programDao.getProgram(programId));
	        }
	    }

	    void validate(Room room)  {
	        if (room == null) {
	            handleException(new IllegalStateException("room must not be null"));
	        }

	        validateRoom(room);
	        validateRoomType(room.getRoomTypeId());
	        validateProgram(room.getProgramId());
	    }

	    void validateRoom(Room room)  {
	        Integer roomId = room.getId();

	        if (roomId != null) {
	            if (!roomDao.roomExists(roomId)) {
	                handleException(new IllegalStateException("no room with id : " + roomId));
	            }
	/*
	            if (!room.isActive() && bedDAO.getBedsByRoom(roomId, Boolean.TRUE).length > 0) {
	                handleException(new RoomHasActiveBedsException("room with id : " + roomId + " has active beds"));
	            }
	*/
	        }
	    }

	    void validateRoomType(Integer roomTypeId) {
	        if (!roomTypeDao.roomTypeExists(roomTypeId)) {
	            handleException(new IllegalStateException("no room type with id : " + roomTypeId));
	        }
	    }

	    void validateProgram(Integer programId) {
	        if (programId != null && !programDao.isBedProgram(programId)) {
	            handleException(new IllegalStateException("no bed program with id : " + programId));
	        }
	    }
	

}
