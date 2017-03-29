/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.hospitalReportManager.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToProvider;
import org.springframework.stereotype.Repository;

@Repository
public class HRMDocumentToProviderDao extends AbstractDao<HRMDocumentToProvider> {

	public HRMDocumentToProviderDao() {
		super(HRMDocumentToProvider.class);
	}

	public List<HRMDocumentToProvider> findAllUnsigned(Integer page, Integer pageSize) {
		String sql = "select x from " + this.modelClass.getName() + " x where (x.signedOff IS NULL or x.signedOff = 0)";
		Query query = entityManager.createQuery(sql);
		query.setMaxResults(pageSize);
		query.setFirstResult(page*pageSize);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}

	public List<HRMDocumentToProvider> findByProviderNo(String providerNo, Integer page, Integer pageSize) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.providerNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);
		query.setMaxResults(pageSize);
		query.setFirstResult(page*pageSize);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}

	public List<HRMDocumentToProvider> findByProviderNoLimit(String providerNo, Date newestDate, Date oldestDate, Integer viewed, Integer signedOff) {
		String sql = "select x from " + this.modelClass.getName() + " x, HRMDocument h where x.hrmDocumentId=h.id and x.providerNo like ?";
		if (newestDate != null)
			sql += " and h.reportDate <= :newest";
		if (oldestDate != null)
			sql += " and h.reportDate >= :oldest";
		if (viewed != 2)
			sql += " and x.viewed = :viewed";
		if (signedOff != 2)
			sql += " and x.signedOff = :signedOff";

		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);

		if (newestDate != null)
			query.setParameter("newest", newestDate);

		if (oldestDate != null)
			query.setParameter("oldest", oldestDate);

		if (viewed != 2)
			query.setParameter("viewed", viewed);

		if (signedOff != 2)
			query.setParameter("signedOff", signedOff);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}


	public List<HRMDocumentToProvider> findByHrmDocumentId(String hrmDocumentId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hrmDocumentId);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}

	public List<HRMDocumentToProvider> findByHrmDocumentIdNoSystemUser(String hrmDocumentId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.providerNo != '-1'";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hrmDocumentId);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}

	public HRMDocumentToProvider findByHrmDocumentIdAndProviderNo(String hrmDocumentId, String providerNo) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.providerNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hrmDocumentId);
		query.setParameter(2, providerNo);
		try {
			return (HRMDocumentToProvider) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<HRMDocumentToProvider> findByHrmDocumentIdAndProviderNoList(String hrmDocumentId, String providerNo) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.providerNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hrmDocumentId);
		query.setParameter(2, providerNo);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}
	
	public List<HRMDocumentToProvider> findNonViewedByProviderNo(String providerNo) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.providerNo=? and x.viewed = 0";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}
	
}
