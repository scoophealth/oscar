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

package org.oscarehr.PMmodule.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.PMmodule.model.CriteriaType;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class CriteriaTypeDao extends AbstractDao<CriteriaType> {

	public CriteriaTypeDao() {
		super(CriteriaType.class);
	}
	
	public List<CriteriaType> findAll() {
		Query query = entityManager.createQuery("select x from CriteriaType x");
		
		@SuppressWarnings("unchecked")
	    List<CriteriaType> results = query.getResultList();
		
		return results;
	}

	public CriteriaType findByName(String fieldName) {		
		Query query = entityManager.createQuery("select x from CriteriaType x where x.fieldName=?");
		query.setParameter(1, fieldName);	
		
		@SuppressWarnings("unchecked")
	    List<CriteriaType> results = query.getResultList();
		
		if(results.size()>0)
			return results.get(0);
		
		return null;
	}
	
	public List<CriteriaType> getAllCriteriaTypes() {
		Query query = entityManager.createQuery("select x from CriteriaType x where x.wlProgramId=? order by x.fieldType DESC");
		query.setParameter(1, 1);
		
		@SuppressWarnings("unchecked")
	    List<CriteriaType> results = query.getResultList();
		
		return results;
	}
	
	public List<CriteriaType> getAllCriteriaTypesByWlProgramId(Integer wlProgramId) {
		Query query = entityManager.createQuery("select x from CriteriaType x where x.wlProgramId=? order by x.fieldType DESC");
		query.setParameter(1, wlProgramId);
		
		@SuppressWarnings("unchecked")
	    List<CriteriaType> results = query.getResultList();
		
		return results;
	}
}
