package org.oscarehr.common.dao;

import org.oscarehr.common.model.Billing;
import org.springframework.stereotype.Repository;

@Repository
public class BillingDao extends AbstractDao<Billing>{

	public BillingDao() {
		super(Billing.class);
	}

}