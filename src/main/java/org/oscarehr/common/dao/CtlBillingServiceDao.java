package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.CtlBillingService;
import org.springframework.stereotype.Repository;

@Repository
public class CtlBillingServiceDao extends AbstractDao<CtlBillingService> {

	public CtlBillingServiceDao() {
		super(CtlBillingService.class);
	}
	
	public List<Object[]> getUniqueServiceTypes() {
		Query query = entityManager.createQuery("select distinct b.serviceType, b.serviceTypeName from CtlBillingService b where b.status=?");
		query.setParameter(1, "A");
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		return results;
	}
}
