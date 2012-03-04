package org.oscarehr.common.dao;

import org.oscarehr.common.model.DemographicSets;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicSetsDao extends AbstractDao<DemographicSets>{

	public DemographicSetsDao() {
		super(DemographicSets.class);
	}
}
