package org.oscarehr.common.dao;

import org.oscarehr.common.model.DemographicAccessory;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicAccessoryDao extends AbstractDao<DemographicAccessory>{

	public DemographicAccessoryDao() {
		super(DemographicAccessory.class);
	}
}
