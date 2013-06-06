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
import org.oscarehr.common.model.RoomBed;
import org.oscarehr.common.model.RoomBedPK;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class RoomBedDao extends AbstractDao<RoomBed>{

	private Logger log = MiscUtils.getLogger();

	public RoomBedDao() {
		super(RoomBed.class);
	}
	
   
    public boolean bedExists(Integer roomId) {
    	Query query = entityManager.createQuery("select count(*) from RoomBed rb where rb.id.roomId = ?");
		query.setParameter(1, roomId);
		
		Long result = (Long)query.getSingleResult();
		
		return (result.intValue() == 1);
    }

   
    public boolean roomExists(Integer bedId) {
    	Query query = entityManager.createQuery("select count(*) from RoomBed rb where rb.id.bedId = ?");
		query.setParameter(1, bedId);
		
		Long result = (Long)query.getSingleResult();
		
		return (result.intValue() == 1);
    }

   
    public RoomBed getRoomBedByRoom(Integer roomId) {
    	Query query = entityManager.createQuery("select bd from RoomBed bd where bd.id.roomId = ?");
    	query.setParameter(1, roomId);
    	
    	@SuppressWarnings("unchecked")
        List<RoomBed> roomBeds =query.getResultList();

        if (roomBeds.size() > 1) {
            throw new IllegalStateException("Room is assigned to more than one client");
        }

        RoomBed roomBed = ((roomBeds.size() == 1)?roomBeds.get(0):null);

        log.debug("getRoomBedByRoom: " + roomId);

        return roomBed;
    }

  
    public RoomBed getRoomBedByBed(Integer bedId) {
    	Query query = entityManager.createQuery("select bd from RoomBed bd where bd.id.bedId = ?");
    	query.setParameter(1, bedId);
    	
    	@SuppressWarnings("unchecked")
        List<RoomBed> roomBeds =query.getResultList();
    	
    	
        if (roomBeds.size() > 1) {
            throw new IllegalStateException("Client is assigned to more than one room");
        }

        RoomBed roomBed = ((roomBeds.size() == 1)?roomBeds.get(0):null);

        log.debug("getRoomBedByBed: " + bedId);

        return roomBed;
    }

    
    public void saveRoomBed(RoomBed roomBed) {
        updateHistory(roomBed);
        
        if(roomBed == null)
        	return;
        
        if(roomBed.getId() == null || roomBed.getId().getBedId() == null || roomBed.getId().getRoomId() == null)
        	persist(roomBed);
        else
        	merge(roomBed);


        log.debug("saveRoomBed: " + roomBed);
    }

    public void deleteRoomBed(RoomBed roomBed) {
         // delete
    	if(roomBed != null)
    		remove(roomBed.getId());
    }

    boolean roomBedExists(RoomBedPK id) {
    	Query query = entityManager.createQuery("select count(*) from RoomBed rb where rb.id.roomId = ? and rb.id.bedId = ?");
		query.setParameter(1, id.getRoomId());
		query.setParameter(1, id.getBedId());
		
		Long result = (Long)query.getSingleResult();
		
		return (result.intValue() == 1);
    }

    void updateHistory(RoomBed roomBed) {
        RoomBed existing = null;

        RoomBedPK id = roomBed.getId();

        if (!roomBedExists(id)) {
            Integer bedId = id.getBedId();
            Integer roomId = id.getRoomId();

            if (roomExists(bedId)) {
                existing = getRoomBedByBed(bedId);
            }
            else if (bedExists(roomId)) {
                existing = getRoomBedByRoom(roomId);
            }
        }

        if (existing != null) {
            deleteRoomBed(existing);
        }
    }
}
