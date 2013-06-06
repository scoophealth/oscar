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

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.RoomType;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class RoomTypeDao extends AbstractDao<RoomType> {

	private Logger log = MiscUtils.getLogger();
	
	public RoomTypeDao() {
		super(RoomType.class);
	}
	
	/**
	 * Does room type with id exist
	 *
	 * @param roomTypeId
	 *            id
	 * @return true if room type exists
	 */
    public boolean roomTypeExists(Integer roomTypeId) {
    	Query query = entityManager.createQuery("select count(*) from RoomType r where r.id = ?");
		query.setParameter(1, roomTypeId);
		
		Long result = (Long)query.getSingleResult();
		
		return (result.intValue() == 1);
	}
    


	/**
	 * Get room type by id
	 *
	 * @param roomTypeId
	 *            id
	 * @return room type
	 */
    public RoomType getRoomType(Integer roomTypeId) {
		RoomType roomType = find(roomTypeId);
		log.debug("getRoom: id: " + roomTypeId);

		return roomType;
	}
    
	/**
	 * Get room types
	 *
	 */
    @SuppressWarnings("unchecked")
    public RoomType[] getRoomTypes() {
    	Query query = entityManager.createQuery("select r from RoomType r");
    	
    	List<RoomType> roomTypes = query.getResultList();
		log.debug("getRooms: size: " + roomTypes.size());

		return roomTypes.toArray(new RoomType[roomTypes.size()]);
	}
}
