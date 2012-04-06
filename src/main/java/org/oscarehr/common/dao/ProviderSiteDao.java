package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ProviderSite;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderSiteDao extends AbstractDao<ProviderSite>{

	public ProviderSiteDao() {
		super(ProviderSite.class);
	}

	public List<ProviderSite> findByProviderNo(String providerNo) {
    	String sql = "select x from ProviderSite x where x.id.providerNo=?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,providerNo);

        @SuppressWarnings("unchecked")
        List<ProviderSite> results = query.getResultList();
        return results;
    }
}
