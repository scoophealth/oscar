package org.oscarehr.billing.CA.ON.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.billing.CA.ON.model.BillingPercLimit;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class BillingPercLimitDao extends AbstractDao<BillingPercLimit>{

	public BillingPercLimitDao() {
		super(BillingPercLimit.class);
	}


    public List<BillingPercLimit> findByServiceCode(String serviceCode) {
    	String sql = "select x from BillingPercLimit x where x.service_code=?1";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,serviceCode);

        @SuppressWarnings("unchecked")
        List<BillingPercLimit> results = query.getResultList();
        return results;
    }

    public BillingPercLimit findByServiceCodeAndEffectiveDate(String serviceCode,Date effectiveDate) {
    	String sql = "select x from BillingPercLimit x where x.service_code=? and x.effective_date=?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,serviceCode);
    	query.setParameter(2, effectiveDate);

        BillingPercLimit results = this.getSingleResultOrNull(query);
        return results;
    }
}
