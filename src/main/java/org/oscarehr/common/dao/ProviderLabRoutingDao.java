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

import javax.persistence.Query;

import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ProviderLabRoutingDao extends AbstractDao<ProviderLabRoutingModel> {

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


}