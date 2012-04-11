package org.oscarehr.billing.CA.dao;

import org.oscarehr.billing.CA.model.BillingInr;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class BillingInrDao extends AbstractDao<BillingInr>{

	public BillingInrDao() {
		super(BillingInr.class);
	}
}
