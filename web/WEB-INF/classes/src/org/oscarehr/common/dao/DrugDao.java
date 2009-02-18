/**
 * Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
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
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Drug;
import org.springframework.stereotype.Repository;

@Repository
public class DrugDao extends AbstractDao {

	public Drug find(Integer id) {
		return (entityManager.find(Drug.class, id));
	}

	/**
	 * @param archived can be null for both archived and non archived entries
	 */
	public List<Drug> findByDemographicIdOrderByDate(Integer demographicId, Boolean archived) {
		// build sql string
		String sqlCommand="select x from Drug x where x.demographicId=?1 "+(archived==null?"":"and x.archived=?2")+" order by x.rxDate desc, x.id desc";
		
		// set parameters
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		if (archived!=null) query.setParameter(2, archived);
		
		// run query
		@SuppressWarnings("unchecked")
		List<Drug> results = query.getResultList();

		return(results);
	}
}
