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

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.common.model.BedDemographicHistorical;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class BedDemographicHistoricalDao extends AbstractDao<BedDemographicHistorical>{

	private Logger log = MiscUtils.getLogger();
	
	public BedDemographicHistoricalDao() {
		super(BedDemographicHistorical.class);
	}
	   
	@SuppressWarnings("unchecked")    
	public BedDemographicHistorical[] getBedDemographicHistoricals(Date since) {
		Query query = entityManager.createQuery("select bdh from BedDemographicHistorical bdh where bdh.usageEnd >= ?");
		query.setParameter(1, DateTimeFormatUtils.getDateFromDate(since));
		
		List<BedDemographicHistorical> bedDemographicHistoricals = query.getResultList();
		
		log.debug("getBedDemographicHistoricals: size: " + bedDemographicHistoricals.size());

	        
		return bedDemographicHistoricals.toArray(new BedDemographicHistorical[bedDemographicHistoricals.size()]);
	}
}
