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

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.hospitalReportManager.model.HRMDocumentSubClass;
import org.springframework.stereotype.Repository;

@Repository
public class HRMDocumentSubClassDao extends AbstractDao<HRMDocumentSubClass> {

	public HRMDocumentSubClassDao() {
		super(HRMDocumentSubClass.class);
	}

	public List<HRMDocumentSubClass> getSubClassesByDocumentId(Integer id) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, id);
		@SuppressWarnings("unchecked")
		List<HRMDocumentSubClass> subClasses = query.getResultList();
		return subClasses;
	}
	
	public List<HRMDocumentSubClass> getActiveSubClassesByDocumentId(Integer id) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.isActive=1";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, id);
		@SuppressWarnings("unchecked")
		List<HRMDocumentSubClass> subClasses = query.getResultList();
		return subClasses;
	}
	
	public boolean setAllSubClassesForDocumentAsInactive(Integer id) {
		String sql = "update " + this.modelClass.getName() + " x set isActive=false where x.hrmDocumentId=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, id);
		return query.executeUpdate() > 0;
	}
}
