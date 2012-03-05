package org.oscarehr.common.dao;

import org.oscarehr.common.model.DemographicStudy;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicStudyDao extends AbstractDao<DemographicStudy>{

	public DemographicStudyDao() {
		super(DemographicStudy.class);
	}

}
