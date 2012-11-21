/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ProviderLabRoutingDao extends AbstractDao<ProviderLabRoutingModel> {

	@PersistenceContext
	protected EntityManager entityManager = null;

	public ProviderLabRoutingDao() {
		super(ProviderLabRoutingModel.class);
	}

	@SuppressWarnings("unchecked")
	private List<ProviderLabRoutingModel> getProviderLabRoutings(String labNo, String labType, String providerNo, String status) {
		Query q = entityManager.createQuery("select x from " + modelClass.getName() + " x where x.labNo=? and x.labType=? and x.providerNo=? and x.status=?");
		q.setParameter(1, labNo != null ? Integer.parseInt(labNo) : "%");
		q.setParameter(2, labType != null ? labType : "%");
		q.setParameter(3, providerNo != null ? providerNo : "%");
		q.setParameter(4, status != null ? status : "%");

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
    public List<ProviderLabRoutingModel> findByLabNoAndLabTypeAndProviderNo(int labNo, String labType, String providerNo) {
		Query q = entityManager.createQuery("select x from " + modelClass.getName() + " x where x.labNo=? and x.labType=? and x.providerNo=?");
		q.setParameter(1, labNo);
		q.setParameter(2, labType);
		q.setParameter(3, providerNo);
	
		return q.getResultList();
	}
	
	public List<ProviderLabRoutingModel> getProviderLabRoutingDocuments(String labNo) {
		return getProviderLabRoutings(labNo, "DOC", null, null);
	}

	public List<ProviderLabRoutingModel> getProviderLabRoutingForLabProviderType(String labNo, String providerNo, String labType) {
		return getProviderLabRoutings(labNo, labType, providerNo, null);
	}

	public List<ProviderLabRoutingModel> getProviderLabRoutingForLabAndType(String labNo, String labType) {
		return getProviderLabRoutings(labNo, labType, null, "N");
	}

	public void updateStatus(String labNo, String labType) {
		String updateString = "UPDATE " + modelClass.getName() + " x set x.status='N' WHERE x.labNo=? AND x.labType=?";

		Query query = entityManager.createQuery(updateString);
		query.setParameter(1, labNo);
		query.setParameter(2, labType);

		query.executeUpdate();
	}

	public ProviderLabRoutingModel findByLabNo(int labNo) {
		Query query = entityManager.createQuery("select x from " + modelClass.getName() + " x where x.labNo=?");
		query.setParameter(1, labNo);

		return this.getSingleResultOrNull(query);
	}
	
	public ProviderLabRoutingModel findByLabNoAndLabType(int labNo, String labType) {
		Query query = entityManager.createQuery("select x from " + modelClass.getName() + " x where x.labNo=? and x.labType = ?");
		query.setParameter(1, labNo);
		query.setParameter(2, labType);
		
		return this.getSingleResultOrNull(query);
	}

	/**
	 * Finds all providers and lab routing models for the specified lab
	 * 
	 * @param labNo
	 * 		Lab number to find data for
	 * @param labType
	 * 		Lab type to find data for
	 * @return
	 * 		Returns an array of objects containing {@link Provider}, {@link ProviderLabRoutingModel} pairs.
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getProviderLabRoutings(Integer labNo, String labType) {
		Query query = entityManager.createQuery("FROM " + Provider.class.getSimpleName() + " p, ProviderLabRoutingModel r WHERE p.id = r.providerNo AND r.labNo = :labNo AND r.labType = :labType");
		query.setParameter("labNo", labNo);
		query.setParameter("labType", labType);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
    public List<ProviderLabRoutingModel> findByStatusANDLabNoType(Integer labNo, String labType, String status) {
	    Query query = createQuery("r", "r.labNo = :labNo and r.labType = :labType and r.status = :status");
	    query.setParameter("labNo", labNo);
	    query.setParameter("labType", labType);
	    query.setParameter("status", status);
	    return query.getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<ProviderLabRoutingModel> findByProviderNo(String providerNo, String status) {
	    Query query = createQuery("p", "p.providerNo = :pNo AND p.status = :sts");
	    query.setParameter("pNo", providerNo);
	    query.setParameter("sts", status);
	    return query.getResultList();
    }

	@SuppressWarnings("unchecked")
	public List<ProviderLabRoutingModel> findByLabNoTypeAndStatus(int labId, String labType, String status) {
		Query query = createQuery("p", "p.labNo = :lNo AND p.status = :sts AND p.labType = :lType");
		query.setParameter("lNo", labId);
		query.setParameter("sts", status);
		query.setParameter("lType", labType);
		return query.getResultList();
    }
}