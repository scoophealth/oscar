package org.oscarehr.common.dao;

import org.oscarehr.common.model.CtlBillingServicePremium;
import org.springframework.stereotype.Repository;

@Repository
public class CtlBillingServicePremiumDao extends AbstractDao<CtlBillingServicePremium>{

	public CtlBillingServicePremiumDao() {
		super(CtlBillingServicePremium.class);
	}
}
