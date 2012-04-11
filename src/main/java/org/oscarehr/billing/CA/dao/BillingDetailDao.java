package org.oscarehr.billing.CA.dao;

import org.oscarehr.billing.CA.model.BillingDetail;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class BillingDetailDao extends AbstractDao<BillingDetail>{

	public BillingDetailDao() {
		super(BillingDetail.class);
	}
}
