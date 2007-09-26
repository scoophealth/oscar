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

package org.oscarehr.PMmodule.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.RoomDAO;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.model.RoomType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of RoomDAO
 */
public class RoomDAOHibernate extends HibernateDaoSupport implements RoomDAO {

	private static final Log log = LogFactory.getLog(RoomDAOHibernate.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.RoomDAO#roomExists(java.lang.Integer)
	 */
	public boolean roomExists(Integer roomId) {
		boolean exists = (((Long) getHibernateTemplate().iterate("select count(*) from Room where id = " + roomId).next()) == 1);
		log.debug("roomExists: " + exists);

		return exists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.RoomDAO#roomTypeExists(short)
	 */
	public boolean roomTypeExists(Integer roomTypeId) {
		boolean exists = (((Long) getHibernateTemplate().iterate("select count(*) from RoomType where id = " + roomTypeId).next()) == 1);
		log.debug("roomTypeExists: " + exists);

		return exists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.RoomDAO#getRoom(java.lang.Integer)
	 */
	public Room getRoom(Integer roomId) {
		Room room = (Room) getHibernateTemplate().get(Room.class, roomId);
		log.debug("getRoom: id: " + roomId);

		return room;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.RoomDAO#getRoomType(java.lang.Integer)
	 */
	public RoomType getRoomType(Integer roomTypeId) {
		RoomType roomType = (RoomType) getHibernateTemplate().get(RoomType.class, roomTypeId);
		log.debug("getRoom: id: " + roomTypeId);

		return roomType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.RoomDAO#getRooms(java.lang.Boolean)
	 */
	@SuppressWarnings("unchecked")
    public Room[] getRooms(Integer programId, Boolean active) {
		String queryString = getRoomsQueryString(programId, active);
		Object[] values = getRoomsValues(programId, active);

		List rooms = (programId != null || active != null) ? getHibernateTemplate().find(queryString, values) : getHibernateTemplate().find(queryString);
		log.debug("getRooms: size: " + rooms.size());

		return (Room[]) rooms.toArray(new Room[rooms.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.RoomDAO#getRoomTypes()
	 */
	@SuppressWarnings("unchecked")
    public RoomType[] getRoomTypes() {
		List roomTypes = getHibernateTemplate().find("from RoomType rt");
		log.debug("getRooms: size: " + roomTypes.size());

		return (RoomType[]) roomTypes.toArray(new RoomType[roomTypes.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.RoomDAO#saveRoom(org.oscarehr.PMmodule.model.Room)
	 */
	public void saveRoom(Room room) {
		updateHistory(room);
		getHibernateTemplate().saveOrUpdate(room);
		getHibernateTemplate().flush();
		getHibernateTemplate().refresh(room);
		
		log.debug("saveRoom: id: " + room.getId());
	}

	String getRoomsQueryString(Integer programId, Boolean active) {
		StringBuilder queryBuilder = new StringBuilder("from Room r");

		if (programId != null || active != null) {
			queryBuilder.append(" where ");
		}

		if (programId != null) {
			queryBuilder.append("r.programId = ?");
		}

		if (programId != null && active != null) {
			queryBuilder.append(" and ");
		}

		if (active != null) {
			queryBuilder.append("r.active = ?");
		}

		return queryBuilder.toString();
	}
	
	Object[] getRoomsValues(Integer programId, Boolean active) {
		List<Object> values = new ArrayList<Object>();
		
		if (programId != null) {
			values.add(programId);
		}
		
		if (active != null) {
			values.add(active);
		}
		
		return (Object[]) values.toArray(new Object[values.size()]);
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