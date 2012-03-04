package org.oscarehr.common.dao;

import org.oscarehr.common.model.CtlBillingType;
import org.springframework.stereotype.Repository;

@Repository
public class CtlBillingTypeDao extends AbstractDao<CtlBillingType>{

	public CtlBillingTypeDao() {
		super(CtlBillingType.class);
	}

}
