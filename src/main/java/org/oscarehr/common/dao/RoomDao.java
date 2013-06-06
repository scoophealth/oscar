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
package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.Room;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao extends AbstractDao<Room>{

	private Logger log = MiscUtils.getLogger();

	public RoomDao() {
		super(Room.class);
	}
	
	
	/**
	 * Does room with id exist
	 *
	 * @param roomId
	 *            id
	 * @return true if room exists
	 */
    public boolean roomExists(Integer roomId) {
    	Query query = entityManager.createQuery("select count(*) from Room r where r.id = ?");
		query.setParameter(1, roomId);
		
		Long result = (Long)query.getSingleResult();
		
		boolean exists =  (result.intValue() == 1);
		
		log.debug("roomExists: " + exists);

		return exists;
	}


	/**
	 * Get room by id
	 *
	 * @param roomId
	 *            id
	 * @return room
	 */
    public Room getRoom(Integer roomId) {
		Room room = find(roomId);
		log.debug("getRoom: id: " + roomId);

		return room;
	}

	/**
	 * Get rooms
	 *
	 * @param active
	 *            filter
	 * @return list of rooms
	 */
    @SuppressWarnings("unchecked")
    public Room[] getRooms(Integer facilityId, Integer programId, Boolean active) {
    	//must be commented out as to add rooms in Bed.jsp
    	//if(programId == null  ||  active == null){
    	//	return null;
    	//}
		String queryString = getRoomsQueryString(facilityId, programId, active);
		Object[] values = getRoomsValues(facilityId, programId, active);
		
		Query query = entityManager.createQuery(queryString);
		if (facilityId != null || programId != null || active != null) {
			for(int x=0;x<values.length;x++) {
				query.setParameter(x+1, values[x]);
			}
			
		}
		
		List<Room> rooms = query.getResultList();
		
		if(rooms != null){		
				log.debug("RoomDAO.getRooms(): rooms.size() = " + rooms.size());
			
				return rooms.toArray(new Room[rooms.size()]);
		} else
			return null;
	}

	/**
	 * Get assigned bed rooms
	 *
	 * @param facilityId
	 * @param programId
	 * @param active           
	 * @return list of assigned bed rooms
	 */
    @SuppressWarnings("unchecked")
    public Room[] getAssignedBedRooms(Integer facilityId, Integer programId, Boolean active) {
    	//if(programId == null  ||  active == null){
    	//	return null;
    	//}
		String queryString = getAssignedBedRoomsQueryString(facilityId, programId, active);
		Object[] values = getRoomsValues(facilityId, programId, active);
		
		Query query = entityManager.createQuery(queryString);
		if (facilityId != null || programId != null || active != null) {
			for(int x=0;x<values.length;x++) {
				query.setParameter(x+1, values[x]);
			}
			
		}
		
		List<Room> rooms = query.getResultList();
		
		if(rooms!=null) {		
			log.debug("getRooms: size: " + rooms.size());
			return rooms.toArray(new Room[rooms.size()]);
		} else {
			return null;
		}
	}

    @SuppressWarnings("unchecked")
    public Room[] getAvailableRooms(Integer facilityId, Integer programId, Boolean active) {
    	//condition placed here on purpose to disallow rooms to display in dropdown list
    	//when clients don't belong to any bed program -- to fix a bug
    	if(programId == null  ||  active == null){
    		return null;
    	}
		String queryString = getRoomsQueryString(facilityId, programId, active);
		Object[] values = getRoomsValues(facilityId, programId, active);
		
		Query query = entityManager.createQuery(queryString);
		if (facilityId != null || programId != null || active != null) {
			for(int x=0;x<values.length;x++) {
				query.setParameter(x+1, values[x]);
			}
			
		}
		
		List<Room> rooms = query.getResultList();
		
		
		if(rooms != null){		
				log.debug("RoomDAO.getRooms(): rooms.size() = " + rooms.size());
			
				return rooms.toArray(new Room[rooms.size()]);
		} else
			return null;
	}

	/**
	 * Save room
	 *
	 * @param room
	 *            room to save
	 */
    @Deprecated
    public void saveRoom(Room room) {
    	if(room == null)
    		return;
		//updateHistory(room);
    	
    	if(room.getId() == null || room.getId().intValue() == 0) 
    		persist(room);
    	else
    		merge(room);
		
		log.debug("saveRoom: id: " + room.getId());
	}

    public void deleteRoom(Room room) {
    	if(room != null){
        	log.debug("deleteRoom: id " + room.getId());
		}
        remove(room.getId());
    }

	String getRoomsQueryString(Integer facilityId, Integer programId, Boolean active) {
		StringBuilder queryBuilder = new StringBuilder("select r from Room r");

        queryBuilder.append(" where ");

        boolean andClause = false;
        if (facilityId != null) {
            queryBuilder.append("r.facilityId = ?");
            andClause = true;
        }

        if (programId != null) {
            if (andClause) queryBuilder.append(" and "); else andClause = true;
            queryBuilder.append("r.programId = ?");
        }


        if (active != null) {
            if (andClause) queryBuilder.append(" and ");
            queryBuilder.append("r.active = ?");
        }

        return queryBuilder.toString();
	}

	String getAssignedBedRoomsQueryString(Integer facilityId, Integer programId, Boolean active) {
		StringBuilder queryBuilder = new StringBuilder("select r from Room r");

        queryBuilder.append(" where ");

        boolean andClause = false;
        if (facilityId != null) {
            queryBuilder.append("r.facilityId = ?");
            andClause = true;
        }

        if (programId != null) {
            if (andClause){
            	queryBuilder.append(" and "); 
            }else{
            	andClause = true;
            }
            queryBuilder.append("r.programId = ?");
        }


        if (active != null) {
            if (andClause){
            	queryBuilder.append(" and ");
            }else{
            	andClause = true;
            }

            queryBuilder.append("r.active = ?");
        }
        
        if (andClause) {
        	queryBuilder.append(" and ");
        }
        
        queryBuilder.append("r.assignedBed = 1");

        return queryBuilder.toString();
	}
	
	Object[] getRoomsValues(Integer facilityId, Integer programId, Boolean active) {
		List<Object> values = new ArrayList<Object>();

        if (facilityId != null) {
            values.add(facilityId);
        }

        if (programId != null) {
			values.add(programId);
		}

		if (active != null) {
			values.add(active);
		}
		return values.toArray(new Object[values.size()]);
	}

	void updateHistory(Room room) {
		// TODO IC Bedlog Historical - update create and persist historical data
		// get previous programroom
		// set end date to today
		// create new programroom
		// set start date to today
		// save previous and new programrooms
	}

}
