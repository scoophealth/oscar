package org.oscarehr.billing.CA.dao;

import org.oscarehr.billing.CA.model.BillActivity;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class BillActivityDao extends AbstractDao<BillActivity>{

	public BillActivityDao() {
		super(BillActivity.class);
	}
}
