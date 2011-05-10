package org.oscarehr.common.dao;

import org.oscarehr.common.model.DemographicCust;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicCustDao extends AbstractDao<DemographicCust> {

	public DemographicCustDao() {
		super(DemographicCust.class);
	}
}
