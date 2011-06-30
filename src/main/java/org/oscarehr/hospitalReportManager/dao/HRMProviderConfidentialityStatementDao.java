package org.oscarehr.hospitalReportManager.dao;


import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.hospitalReportManager.model.HRMProviderConfidentialityStatement;
import org.springframework.stereotype.Repository;

@Repository
public class HRMProviderConfidentialityStatementDao extends AbstractDao<HRMProviderConfidentialityStatement> {

	public HRMProviderConfidentialityStatementDao() {
		super (HRMProviderConfidentialityStatement.class);
	}

	public String getConfidentialityStatementForProvider(String providerNo) {
		String sql = "select x.statement from " + this.modelClass.getName() + " x where x.providerNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);
		try {
			return (String) query.getSingleResult();
		} catch (Exception e) {
			// No statement for this provider
			return "";
		}
	}


}
