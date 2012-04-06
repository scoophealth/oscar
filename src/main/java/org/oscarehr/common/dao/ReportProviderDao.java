package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ReportProvider;
import org.springframework.stereotype.Repository;

@Repository
public class ReportProviderDao extends AbstractDao<ReportProvider>{

	public ReportProviderDao() {
		super(ReportProvider.class);
	}

	public List<ReportProvider> findByAction(String action) {
    	String sql = "select x from ReportProvider x where x.action=?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,action);

        @SuppressWarnings("unchecked")
        List<ReportProvider> results = query.getResultList();
        return results;
    }

}
