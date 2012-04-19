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


package org.oscarehr.hospitalReportManager.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.hospitalReportManager.model.HRMCategory;
import org.springframework.stereotype.Repository;

@Repository
public class HRMCategoryDao extends AbstractDao<HRMCategory> {
	
	public HRMCategoryDao() {
	    super(HRMCategory.class);
    }

	public List<HRMCategory> findById(int id) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.id=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, id);
		@SuppressWarnings("unchecked")
		List<HRMCategory> documents = query.getResultList();
		return documents;
	}
	
	public List<HRMCategory> findAll() {
		String sql = "select x from " + this.modelClass.getName() + " x ";
		Query query = entityManager.createQuery(sql);
		@SuppressWarnings("unchecked")
		List<HRMCategory> documents = query.getResultList();
		return documents;
	}
	
	public HRMCategory findBySubClassNameMnemonic(String subClassNameMnemonic)
	{
		try{
			String sql = "select x from " + modelClass.getSimpleName() + " x where x.subClassNameMnemonic=?1";
			Query query = entityManager.createQuery(sql);
			query.setParameter(1, subClassNameMnemonic);
			return (HRMCategory) (query.getSingleResult());
		} catch(NoResultException e) {
	        return null;
	    }
	}
	
}
