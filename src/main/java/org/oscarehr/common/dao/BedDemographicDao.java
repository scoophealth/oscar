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

import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.BedDemographic;
import org.oscarehr.common.model.BedDemographicHistorical;
import org.oscarehr.common.model.BedDemographicPK;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class BedDemographicDao extends AbstractDao<BedDemographic>{

	private Logger log = MiscUtils.getLogger();

	public BedDemographicDao() {
		super(BedDemographic.class);
	}
	
    public boolean demographicExists(int bedId) {
    	Query query = entityManager.createQuery("select count(*) from BedDemographic b where b.id.bedId = ?1");
		query.setParameter(1, bedId);
		
		Long result = (Long)query.getSingleResult();
		
		return (result.intValue() == 1);
    }

  
    public boolean bedExists(int demographicNo) {
    	Query query = entityManager.createQuery("select count(*) from BedDemographic b where b.id.demographicNo = ?");
		query.setParameter(1, demographicNo);
		
		Long result = (Long)query.getSingleResult();
		
		return (result.intValue() == 1);
    }

    
    public BedDemographic getBedDemographicByBed(int bedId) {
    	Query query = entityManager.createQuery("select b from BedDemographic b where b.id.bedId = ?");
		query.setParameter(1, bedId);
		
		@SuppressWarnings("unchecked")
		List<BedDemographic> bedDemographics = query.getResultList();
       
        if (bedDemographics.size() > 1) {
            throw new IllegalStateException("Bed is assigned to more than one client");
        }

        BedDemographic bedDemographic = ((bedDemographics.size() == 1)?bedDemographics.get(0):null);

        log.debug("getBedDemographicByBed: " + bedId);

        return bedDemographic;
    }

   
    public BedDemographic getBedDemographicByDemographic(int demographicNo) {
    	Query query = entityManager.createQuery("select b from BedDemographic b where b.id.demographicNo = ?");
		query.setParameter(1, demographicNo);
		
		@SuppressWarnings("unchecked")
		List<BedDemographic> bedDemographics = query.getResultList();
       
        if (bedDemographics.size() > 1) {
            throw new IllegalStateException("Client is assigned to more than one bed");
        }

        BedDemographic bedDemographic = ((bedDemographics.size() == 1)?bedDemographics.get(0):null);

        log.debug("getBedDemographicByDemographic: " + demographicNo);

        return bedDemographic;
    }

    
    public void saveBedDemographic(BedDemographic bedDemographic) {
    	if(bedDemographic == null)
        	return;
    	
        updateHistory(bedDemographic);
        
        if(bedDemographic.getId().getBedId() == null || bedDemographic.getId().getBedId().intValue() == 0 || 
        		bedDemographic.getId().getDemographicNo() == null || bedDemographic.getId().getDemographicNo().intValue() == 0 )
        	persist(bedDemographic);
        else
        	merge(bedDemographic);
  
        log.debug("saveBedDemographic: " + bedDemographic);
    }

    public void deleteBedDemographic(BedDemographic bedDemographic) {
        // save historical
        if (!DateUtils.isSameDay(bedDemographic.getReservationStart(), Calendar.getInstance().getTime())) {
            BedDemographicHistorical historical = BedDemographicHistorical.create(bedDemographic);
            persist(historical);
        }

        // delete
        remove(bedDemographic.getId());
        
    }

    boolean bedDemographicExists(BedDemographicPK id) {
    	Query query = entityManager.createQuery("select count(*) from BedDemographic b where  b.id.bedId = ? and b.id.demographicNo = ?");
		query.setParameter(1, id.getBedId());
		query.setParameter(2, id.getDemographicNo());
		
		Long result = (Long)query.getSingleResult();
		
		return (result.intValue() == 1);
    }

    void updateHistory(BedDemographic bedDemographic) {
        BedDemographic existing = null;

        BedDemographicPK id = bedDemographic.getId();

        if (!bedDemographicExists(id)) {
            Integer demographicNo = id.getDemographicNo();
            Integer bedId = id.getBedId();

            if (bedExists(demographicNo)) {
                existing = getBedDemographicByDemographic(demographicNo);
            }
            else if (demographicExists(bedId)) {
                existing = getBedDemographicByBed(bedId);
            }
        }

        if (existing != null) {
            deleteBedDemographic(existing);
        }
    }
}

