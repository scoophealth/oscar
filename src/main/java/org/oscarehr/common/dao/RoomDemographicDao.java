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
import org.oscarehr.common.model.RoomDemographic;
import org.oscarehr.common.model.RoomDemographicPK;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDemographicDao extends AbstractDao<RoomDemographic>{

	private Logger log = MiscUtils.getLogger();

	public RoomDemographicDao() {
		super(RoomDemographic.class);
	}
	
    public boolean roomDemographicExists(Integer demographicNo) {
    	Query query = entityManager.createQuery("select count(*) from RoomDemographic rd where rd.id.demographicNo = ?");
		query.setParameter(1, demographicNo);
		
		Long result = (Long)query.getSingleResult();
		
		return (result.intValue() == 1);
    }

    boolean roomDemographicExists(RoomDemographicPK id) {
    	Query query = entityManager.createQuery("select count(*) from RoomDemographic rd where rd.id.roomId = ?1 and rd.id.demographicNo = ?2");
		query.setParameter(1, id.getRoomId());
		query.setParameter(2, id.getDemographicNo());
		
		Long result = (Long)query.getSingleResult();
		
		return (result.intValue() == 1);
    }


    public int getRoomOccupanyByRoom(Integer roomId) {
    	Query query = entityManager.createQuery("select count(*) from RoomDemographic rd where rd.id.roomId = ?1");
		query.setParameter(1, roomId);
		
		Long result = (Long)query.getSingleResult();
		
        return result.intValue();
    }

 
    public List<RoomDemographic> getRoomDemographicByRoom(Integer roomId) {
    	Query query = entityManager.createQuery("select rd from RoomDemographic rd where rd.id.roomId = ?1");
		query.setParameter(1, roomId);
		
		@SuppressWarnings("unchecked")
		List<RoomDemographic> roomDemographics = query.getResultList();
        
        if(roomDemographics != null){
        	log.debug("getRoomDemographicByRoom: roomDemographics.size()" + roomDemographics.size());
        }
        return roomDemographics;
    }

   
    public RoomDemographic getRoomDemographicByDemographic(Integer demographicNo) {
    	Query query = entityManager.createQuery("select rd from RoomDemographic rd where rd.id.demographicNo = ?1");
		query.setParameter(1, demographicNo);
		
		@SuppressWarnings("unchecked")
		List<RoomDemographic> roomDemographics = query.getResultList();
   	
        if(roomDemographics == null){
        	return null;
        }
        if (roomDemographics.size() > 1) {
            throw new IllegalStateException("Client is assigned to more than one room");
        }

        RoomDemographic roomDemographic = ((roomDemographics.size() == 1)? roomDemographics.get(0): null);

        log.debug("getRoomDemographicByDemographic: " + demographicNo);
        return roomDemographic;
    }

   
    @Deprecated
    public void saveRoomDemographic(RoomDemographic roomDemographic) {
    	if(roomDemographic==null)
    		return;
        //updateHistory(roomDemographic);
    	
    	if(roomDemographic.getId() == null || roomDemographic.getId().getDemographicNo() == null || roomDemographic.getId().getRoomId() == null)
    		persist(roomDemographic);
    	else
    		merge(roomDemographic);
        
        log.debug("saveRoomDemographic: " + roomDemographic);
    }

    @Deprecated
    public void deleteRoomDemographic(RoomDemographic roomDemographic) {
        remove(roomDemographic.getId());
    }
}
