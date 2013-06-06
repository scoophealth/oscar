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
import org.oscarehr.common.model.BedDemographicStatus;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class BedDemographicStatusDao extends AbstractDao<BedDemographicStatus>{

	private Logger log = MiscUtils.getLogger();
	
	public BedDemographicStatusDao() {
		super(BedDemographicStatus.class);
	}
	
  
	public boolean bedDemographicStatusExists(Integer bedDemographicStatusId) {			   
		Query query = entityManager.createQuery("select count(*) from BedDemographicStatus b where b.id = ?");
		query.setParameter(1, bedDemographicStatusId);
	
		Long result = (Long)query.getSingleResult();
	
		return (result.intValue() == 1);
	}
	   
	@SuppressWarnings("unchecked")    
	public BedDemographicStatus[] getBedDemographicStatuses() {
		   Query query = entityManager.createQuery("select b from BedDemographicStatus b");
	   
	   List<BedDemographicStatus> bedDemographicStatuses = query.getResultList();
	    
	   log.debug("getBedDemographicStatuses: size: " + bedDemographicStatuses.size());
	
	    return bedDemographicStatuses.toArray(new BedDemographicStatus[bedDemographicStatuses.size()]);
	}
	   
	   
	public BedDemographicStatus getBedDemographicStatus(Integer bedDemographicStatusId) {
		BedDemographicStatus bedDemographicStatus = find(bedDemographicStatusId);
		
		log.debug("getBedDemographicStatus: id: " + (bedDemographicStatus != null?bedDemographicStatus.getId():null));

		return bedDemographicStatus;    
	}
}
