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
import org.oscarehr.PMmodule.model.RoomBed;
import org.oscarehr.PMmodule.model.RoomBedPK;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of RoomBedDAO
 */
public class RoomBedDAO extends HibernateDaoSupport {

    private static final Logger log=MiscUtils.getLogger();

    /**
     * @see org.oscarehr.PMmodule.dao.RoomBedDAO#bedExists(java.lang.Integer)
     */
    public boolean bedExists(Integer roomId) {
        boolean exists = (((Long)getHibernateTemplate().iterate("select count(*) from RoomBed bd where bd.id.roomId = " + roomId).next()) == 1);
        log.debug("clientExists: " + exists);

        return exists;
    }

    /**
     * @see org.oscarehr.PMmodule.dao.RoomBedDAO#roomExists(java.lang.Integer)
     */
    public boolean roomExists(Integer bedId) {
        boolean exists = (((Long)getHibernateTemplate().iterate("select count(*) from RoomBed bd where bd.id.bedId = " + bedId).next()) == 1);
        log.debug("roomExists: " + exists);

        return exists;
    }

    /**
     * @see org.oscarehr.PMmodule.dao.RoomBedDAO#getRoomBedByRoom(java.lang.Integer)
     */
    public RoomBed getRoomBedByRoom(Integer roomId) {
        List roomBeds = getHibernateTemplate().find("from RoomBed bd where bd.id.roomId = ?", roomId);

        if (roomBeds.size() > 1) {
            throw new IllegalStateException("Room is assigned to more than one client");
        }

        RoomBed roomBed = (RoomBed)((roomBeds.size() == 1)?roomBeds.get(0):null);

        log.debug("getRoomBedByRoom: " + roomId);

        return roomBed;
    }

    /**
     * @see org.oscarehr.PMmodule.dao.RoomBedDAO#getRoomBedByBed(java.lang.Integer)
     */
    public RoomBed getRoomBedByBed(Integer bedId) {
        List roomBeds = getHibernateTemplate().find("from RoomBed bd where bd.id.bedId = ?", bedId);

        if (roomBeds.size() > 1) {
            throw new IllegalStateException("Client is assigned to more than one room");
        }

        RoomBed roomBed = (RoomBed)((roomBeds.size() == 1)?roomBeds.get(0):null);

        log.debug("getRoomBedByBed: " + bedId);

        return roomBed;
    }

    /**
     * @see org.oscarehr.PMmodule.dao.RoomBedDAO#saveRoomBed(org.oscarehr.PMmodule.model.RoomBed)
     */
    public void saveRoomBed(RoomBed roomBed) {
        updateHistory(roomBed);

        roomBed = getHibernateTemplate().merge(roomBed);
        getHibernateTemplate().flush();

        log.debug("saveRoomBed: " + roomBed);
    }

    public void deleteRoomBed(RoomBed roomBed) {
         // delete
        getHibernateTemplate().delete(roomBed);
        getHibernateTemplate().flush();
    }

    boolean roomBedExists(RoomBedPK id) {
        boolean exists = (((Long)getHibernateTemplate().iterate("select count(*) from RoomBed bd where bd.id.roomId = " + id.getRoomId() + " and bd.id.bedId = " + id.getBedId()).next()) == 1);
        log.debug("roomBedExists: " + exists);

        return exists;
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
