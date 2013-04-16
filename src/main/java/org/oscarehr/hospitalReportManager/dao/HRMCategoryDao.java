/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
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
