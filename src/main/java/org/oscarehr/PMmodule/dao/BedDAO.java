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
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedType;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of BedDAO
 */
public class BedDAO extends HibernateDaoSupport {

    private static final Logger log=MiscUtils.getLogger();

    /**
     * Check for the existence of a bed with this ID.
     * @param bedId
     * @return boolean
     */
    public boolean bedExists(Integer bedId) {

        return (((Long) getHibernateTemplate().iterate("select count(*) from Bed where id = " + bedId).next()) == 1);
    }

    /**
     * Return if this is a valid bed type
     *
     * @param bedTypeId type of bed
     * @return boolean
     */
    public boolean bedTypeExists(Integer bedTypeId) {
        boolean exists = (((Long) getHibernateTemplate().iterate("select count(*) from BedType where id = " + bedTypeId).next()) == 1);
        log.debug("bedTypeExists: " + exists);

        return exists;
    }

    /**
     * Return the bed associated with an id
     * @param bedId bed id to look up
     * @return the bed
     */
    public Bed getBed(Integer bedId) {
        Bed bed = getHibernateTemplate().get(Bed.class, bedId);
        log.debug("getBed: id " + bedId);

        return bed;
    }

    public BedType getBedType(Integer bedTypeId) {
        BedType bedType = getHibernateTemplate().get(BedType.class, bedTypeId);
        log.debug("getBedType: id " + bedTypeId);

        return bedType;
    }

    /**
     * All beds for a given room
     * @param roomId the room id to look up
     * @param active activity flag
     * @return an array of beds
     */
    @SuppressWarnings("unchecked")
    public Bed[] getBedsByRoom(Integer roomId, Boolean active) {
        String query = getBedsQuery(null, roomId, active);
        Object[] values = getBedsValues(null, roomId, active);
        List beds = getBeds(query, values);
        log.debug("getBedsByRoom: size " + beds.size());

        return (Bed[]) beds.toArray(new Bed[beds.size()]);
    }

    @SuppressWarnings("unchecked")
    public List<Bed> getBedsByFacility(Integer facilityId, Boolean active) {
        String query = getBedsQuery(facilityId, null, active);
        Object[] values = getBedsValues(facilityId, null, active);

        return getBeds(query, values);
    }
    
    @SuppressWarnings("unchecked")
    public Bed[] getBedsByFilter(Integer facilityId, Integer roomId, Boolean active) {
        String query = getBedsQuery(facilityId, roomId, active);
        Object[] values = getBedsValues(facilityId, roomId, active);
        List beds = getBeds(query, values);
        log.debug("getBedsByFilter: size " + beds.size());

        return (Bed[]) beds.toArray(new Bed[beds.size()]);
    }

    /**
     * @return all bed types
     */
    @SuppressWarnings("unchecked")
    public BedType[] getBedTypes() {
        List bedTypes = getHibernateTemplate().find("from BedType bt");
        log.debug("getRooms: size: " + bedTypes.size());

        return (BedType[]) bedTypes.toArray(new BedType[bedTypes.size()]);
    }

    /*
      * (non-Javadoc)
      *
      * @see org.oscarehr.PMmodule.dao.BedDAO#saveBed(org.oscarehr.PMmodule.model.Bed)
      */
    public void saveBed(Bed bed) {
        updateHistory(bed);
        getHibernateTemplate().saveOrUpdate(bed);
        getHibernateTemplate().flush();
        getHibernateTemplate().refresh(bed);

        log.debug("saveBed: id " + bed.getId());
    }


    /**
     * Delete bed
     *
     * @param bed
     *            
     * @throws BedReservedException
     *             bed is inactive and reserved
     */
    public void deleteBed(Bed bed) {
        log.debug("deleteBed: id " + bed.getId());

        getHibernateTemplate().delete(bed);
    }


    String getBedsQuery(Integer facilityId, Integer roomId, Boolean active) {
        StringBuilder queryBuilder = new StringBuilder("from Bed b");

        queryBuilder.append(" where ");

        boolean andClause = false;
        if (facilityId != null) {
            queryBuilder.append("b.facilityId = ?");
            andClause = true;
        }

        if (roomId!= null) {
            if (andClause) queryBuilder.append(" and "); else andClause = true;
            queryBuilder.append("b.roomId = ?");
        }

        if (active != null) {
            if (andClause) queryBuilder.append(" and ");
            queryBuilder.append("b.active = ?");
        }

        return queryBuilder.toString();
    }

    
    Object[] getBedsValues(Integer facilityId, Integer roomId, Boolean active) {
        List<Object> values = new ArrayList<Object>();

        if (facilityId != null) {
            values.add(facilityId);
        }

        if (roomId != null) {
            values.add(roomId);
        }

        if (active != null) {
            values.add(active);
        }

        return values.toArray(new Object[values.size()]);
    }

    List<Bed> getBeds(String query, Object[] values) {
        return (values.length > 0) ? getHibernateTemplate().find(query, values) : getHibernateTemplate().find(query);
    }

    void updateHistory(Bed bed) {
        // TODO IC Bedlog Historical - if room to bed association has changed, create historical record
    }

}
