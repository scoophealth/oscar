/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.common.dao;

import javax.persistence.Query;

import org.oscarehr.common.model.HnrDataValidation;
import org.springframework.stereotype.Repository;

@Repository
public class HnrDataValidationDao extends AbstractDao {

	public HnrDataValidation find(Integer id) {
		return (entityManager.find(HnrDataValidation.class, id));
	}

	/**
	 * @param facilityId can not be null
	 * @param clientId can not be null
	 * @param type can not be null
	 */
	public HnrDataValidation findMostCurrentByFacilityIdClientIdType(Integer facilityId, Integer clientId, HnrDataValidation.Type type) {
		// build sql string
		StringBuilder sqlCommand = new StringBuilder();
		sqlCommand.append("select x from HnrDataValidation x where x.facilityId=?1 and x.clientId=?2 and x.validationType=?3 order by x.created desc");

		// set parameters
		Query query = entityManager.createQuery(sqlCommand.toString());
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		query.setParameter(3, type);
		query.setMaxResults(1);
		
		// run query
		HnrDataValidation result = (HnrDataValidation)getSingleResultOrNull(query);
		return (result);
	}
}
