package org.oscarehr.common.dao;

import org.oscarehr.common.model.RecycleBinBilling;
import org.springframework.stereotype.Repository;

@Repository
public class RecycleBinBillingDao extends AbstractDao<RecycleBinBilling>{

	public RecycleBinBillingDao() {
		super(RecycleBinBilling.class);
	}
}
