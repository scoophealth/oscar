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
		String sql = "select x from " + this.modelClass.getName() + " x, HRMDocument h where x.hrmDocumentId=h.id and x.providerNo like ? and x.signedOff=?";
		if (newestDate != null)
			sql += " and h.reportDate <= :newest";
		if (oldestDate != null)
			sql += " and h.reportDate >= :oldest";
		if (viewed != 2)
			sql += " and x.viewed = :viewed";

		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);
		query.setParameter(2, signedOff);

		if (newestDate != null)
			query.setParameter("newest", newestDate);

		if (oldestDate != null)
			query.setParameter("oldest", oldestDate);

		if (viewed != 2)
			query.setParameter("viewed", viewed);

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
}
