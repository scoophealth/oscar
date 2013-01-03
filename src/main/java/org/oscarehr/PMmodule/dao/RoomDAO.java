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

package org.oscarehr.PMmodule.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.model.RoomType;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of RoomDAO
 */
public class RoomDAO extends HibernateDaoSupport {

	private static final Logger log=MiscUtils.getLogger();

	/**
	 * Does room with id exist
	 *
	 * @param roomId
	 *            id
	 * @return true if room exists
	 */
    public boolean roomExists(Integer roomId) {
		boolean exists = (((Long) getHibernateTemplate().iterate("select count(*) from Room where id = " + roomId).next()) == 1);
		log.debug("roomExists: " + exists);

		return exists;
	}


	/**
	 * Does room type with id exist
	 *
	 * @param roomTypeId
	 *            id
	 * @return true if room type exists
	 */
    public boolean roomTypeExists(Integer roomTypeId) {
		boolean exists = (((Long) getHibernateTemplate().iterate("select count(*) from RoomType where id = " + roomTypeId).next()) == 1);
		log.debug("roomTypeExists: " + exists);

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
		Room room = getHibernateTemplate().get(Room.class, roomId);
		log.debug("getRoom: id: " + roomId);

		return room;
	}


	/**
	 * Get room type by id
	 *
	 * @param roomTypeId
	 *            id
	 * @return room type
	 */
    public RoomType getRoomType(Integer roomTypeId) {
		RoomType roomType = getHibernateTemplate().get(RoomType.class, roomTypeId);
		log.debug("getRoom: id: " + roomTypeId);

		return roomType;
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
		List rooms = (facilityId != null || programId != null || active != null) ? getHibernateTemplate().find(queryString, values) : getHibernateTemplate().find(queryString);

		if(rooms != null){		
				log.debug("RoomDAO.getRooms(): rooms.size() = " + rooms.size());
			
				return (Room[]) rooms.toArray(new Room[rooms.size()]);
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
		List rooms = (facilityId != null || programId != null || active != null) ? 
				getHibernateTemplate().find(queryString, values) : getHibernateTemplate().find(queryString);
		if(rooms!=null) {		
			log.debug("getRooms: size: " + rooms.size());
			return (Room[]) rooms.toArray(new Room[rooms.size()]);
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
		List rooms = (facilityId != null || programId != null || active != null) ? getHibernateTemplate().find(queryString, values) : getHibernateTemplate().find(queryString);

		if(rooms != null){		
				log.debug("RoomDAO.getRooms(): rooms.size() = " + rooms.size());
			
				return (Room[]) rooms.toArray(new Room[rooms.size()]);
		} else
			return null;
	}

    
	/**
	 * Get room types
	 *
	 */
    @SuppressWarnings("unchecked")
    public RoomType[] getRoomTypes() {
		List roomTypes = getHibernateTemplate().find("from RoomType rt");
		log.debug("getRooms: size: " + roomTypes.size());

		return (RoomType[]) roomTypes.toArray(new RoomType[roomTypes.size()]);
	}


	/**
	 * Save room
	 *
	 * @param room
	 *            room to save
	 */
    public void saveRoom(Room room) {
		//updateHistory(room);
		getHibernateTemplate().saveOrUpdate(room);
		getHibernateTemplate().flush();
		getHibernateTemplate().refresh(room);

		log.debug("saveRoom: id: " + room.getId());
	}

    public void deleteRoom(Room room) {
    	if(room != null){
        	log.debug("deleteRoom: id " + room.getId());
		}
        getHibernateTemplate().delete(room);
    }

	String getRoomsQueryString(Integer facilityId, Integer programId, Boolean active) {
		StringBuilder queryBuilder = new StringBuilder("from Room r");

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
		StringBuilder queryBuilder = new StringBuilder("from Room r");

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
