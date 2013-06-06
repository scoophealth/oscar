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
import org.oscarehr.common.model.Bed;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class BedDao extends AbstractDao<Bed>{
	
	private Logger log = MiscUtils.getLogger();

	public BedDao() {
		super(Bed.class);
	}
	
	 
	/**
	 * Check for the existence of a bed with this ID.
	 * @param bedId
	 * @return boolean
	 */
	public boolean bedExists(Integer bedId) {
		Query query = entityManager.createQuery("select count(*) from Bed b where b.id = ?");
		query.setParameter(1, bedId);
		
		Long result = (Long)query.getSingleResult();
		
		return (result.intValue() == 1);
	}

	/**
	 * Use find(bedId)
	 * 
	 * Return the bed associated with an id
	 * @param bedId bed id to look up
	 * @return the bed
	 */
	@Deprecated
	public Bed getBed(Integer bedId) {
	   return find(bedId);
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
	    List<Bed> beds = getBeds(query, values);
	    log.debug("getBedsByRoom: size " + beds.size());
	
	    return beds.toArray(new Bed[beds.size()]);
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
        List<Bed> beds = getBeds(query, values);
        log.debug("getBedsByFilter: size " + beds.size());

        return beds.toArray(new Bed[beds.size()]);
    }
	
    /**
     * use persist()
     * 
     * @param bed
     */
    @Deprecated
    public void saveBed(Bed bed) {
    	if(bed == null)
    		return;
    	
        updateHistory(bed);
       
        if(bed.getId() == null || bed.getId().intValue() == 0)
        	persist(bed);
        else
        	merge(bed);
        
        
        log.debug("saveBed: id " + bed.getId());
    }


    /**
     * Use remove(bed)
     * 
     * Delete bed
     *
     * @param bed
     *            
     * @throws BedReservedException
     *             bed is inactive and reserved
     */
    @Deprecated
    public void deleteBed(Bed bed) {
        log.debug("deleteBed: id " + bed.getId());

        remove(bed);
    }


    String getBedsQuery(Integer facilityId, Integer roomId, Boolean active) {
        StringBuilder queryBuilder = new StringBuilder("select b from Bed b");

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

	    List<Bed> getBeds(String queryStr, Object[] values) {
	    	Query query = entityManager.createQuery(queryStr);
	    	if(values != null) {
	    		for(int x=0;x<values.length;x++) {
	    			query.setParameter(x+1, values[x]);
	    		}
	    	}
	    	@SuppressWarnings("unchecked")
	    	List<Bed> results = query.getResultList();
	    	
	    	return results;
	    }

	    void updateHistory(Bed bed) {
	        // TODO IC Bedlog Historical - if room to bed association has changed, create historical record
	    }
}
