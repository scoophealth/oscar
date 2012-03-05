package org.oscarehr.common.dao;

import org.oscarehr.common.model.ProviderFacility;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderFacilityDao extends AbstractDao<ProviderFacility>{

	public ProviderFacilityDao() {
		super(ProviderFacility.class);
	}
}
