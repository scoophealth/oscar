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

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.RoomDemographic;
import org.oscarehr.PMmodule.model.RoomDemographicPK;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of RoomDemographicDao
 */
public class RoomDemographicDao extends HibernateDaoSupport {

    private static final Logger log=MiscUtils.getLogger();

    /**
     * @see org.oscarehr.PMmodule.dao.RoomDemographicDao#roomDemographicExists(java.lang.Integer)
     */
    public boolean roomDemographicExists(Integer demographicNo) {
        boolean exists = (((Long)getHibernateTemplate().iterate("select count(*) from RoomDemographic rd where rd.id.demographicNo = " + demographicNo).next()) == 1);
        log.debug("roomExists: " + exists);

        return exists;
    }

    boolean roomDemographicExists(RoomDemographicPK id) {
        boolean exists = (((Long)getHibernateTemplate().iterate("select count(*) from RoomDemographic rd where rd.id.roomId = " + id.getRoomId() + " and rd.id.demographicNo = " + id.getDemographicNo()).next()) == 1);
        log.debug("roomDemographicExists: " + exists);

        return exists;
    }


    public int getRoomOccupanyByRoom(Integer roomId) {
		Long count = (Long)getHibernateTemplate().find("select count(*) from RoomDemographic rd where rd.id.roomId = ?", roomId).get(0);
        if(count != null){
        	log.debug("getRoomDemographicByRoom: roomOccupancy = " + count.intValue());
        }
        return count.intValue();
    }

    /**
     * @see org.oscarehr.PMmodule.dao.RoomDemographicDao#getRoomDemographicByRoom(java.lang.Integer)
     */
    public List<RoomDemographic> getRoomDemographicByRoom(Integer roomId) {
        List roomDemographics = getHibernateTemplate().find("from RoomDemographic rd where rd.id.roomId = ?", roomId);

        if(roomDemographics != null){
        	log.debug("getRoomDemographicByRoom: roomDemographics.size()" + roomDemographics.size());
        }
        return roomDemographics;
    }

    /**
     * @see org.oscarehr.PMmodule.dao.RoomDemographicDao#getRoomDemographicByDemographic(java.lang.Integer)
     */
    public RoomDemographic getRoomDemographicByDemographic(Integer demographicNo) {
   	
        List roomDemographics = getHibernateTemplate().find("from RoomDemographic rd where rd.id.demographicNo = ?", demographicNo);
        if(roomDemographics == null){
        	return null;
        }
        if (roomDemographics.size() > 1) {
            throw new IllegalStateException("Client is assigned to more than one room");
        }

        RoomDemographic roomDemographic = (RoomDemographic)((roomDemographics.size() == 1)? roomDemographics.get(0): null);

        log.debug("getRoomDemographicByDemographic: " + demographicNo);
        return roomDemographic;
    }

    /**
     * @see org.oscarehr.PMmodule.dao.RoomDemographicDao#saveRoomDemographic(org.oscarehr.PMmodule.model.RoomDemographic)
     */
    public void saveRoomDemographic(RoomDemographic roomDemographic) {
        //updateHistory(roomDemographic);
        getHibernateTemplate().saveOrUpdate(roomDemographic);
        getHibernateTemplate().flush();
        
        log.debug("saveRoomDemographic: " + roomDemographic);
    }

    public void deleteRoomDemographic(RoomDemographic roomDemographic) {
         // delete
        getHibernateTemplate().delete(roomDemographic);
        getHibernateTemplate().flush();
    }

}
