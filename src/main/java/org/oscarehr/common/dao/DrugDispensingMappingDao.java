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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Query;

import org.oscarehr.common.model.DrugDispensingMapping;
import org.springframework.stereotype.Repository;

@Repository
public class DrugDispensingMappingDao extends AbstractDao<DrugDispensingMapping>{

	public DrugDispensingMappingDao() {
		super(DrugDispensingMapping.class);
	}
	
	public DrugDispensingMapping findMappingByDin(String din) {
		
		Query query = entityManager.createQuery("SELECT x FROM DrugDispensingMapping x where x.din = ?1");
		query.setParameter(1, din);
		
		return this.getSingleResultOrNull(query);
	}
	
	public DrugDispensingMapping findMapping(String din,String duration, String durUnit, String freqCode, String quantity, Float takeMin, Float takeMax) {
		
		StringBuilder queryStr = new StringBuilder("SELECT x FROM DrugDispensingMapping x where x.din = :din and x.duration = :duration and x.quantity = :quantity");
		
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		if(durUnit != null) {
			queryStr.append(" and x.durUnit = :durUnit ");
			params.put("durUnit",durUnit);
		}
		if(freqCode != null) {
			queryStr.append(" and x.freqCode = :freqCode ");
			params.put("freqCode",freqCode);
		}
		if(takeMin != null) {
			queryStr.append(" and x.takeMin = :takeMin ");
			params.put("takeMin",takeMin);
		}
		if(takeMax != null) {
			queryStr.append(" and x.takeMax = :takeMax ");
			params.put("takeMax",takeMax);
		}
		
		Query query = entityManager.createQuery(queryStr.toString());
			
		query.setParameter("din", din);
		query.setParameter("duration", duration);
		query.setParameter("quantity", quantity);
		
		for(String key:params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		
		
		return this.getSingleResultOrNull(query);
	}
}
