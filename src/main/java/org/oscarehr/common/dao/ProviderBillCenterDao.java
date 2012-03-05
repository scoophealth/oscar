package org.oscarehr.common.dao;

import org.oscarehr.common.model.ProviderBillCenter;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderBillCenterDao extends AbstractDao<ProviderBillCenter>{

	public ProviderBillCenterDao() {
		super(ProviderBillCenter.class);
	}
}
