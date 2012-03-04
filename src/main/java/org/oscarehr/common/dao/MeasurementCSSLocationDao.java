package org.oscarehr.common.dao;

import org.oscarehr.common.model.MeasurementCSSLocation;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementCSSLocationDao extends AbstractDao<MeasurementCSSLocation>{

	public MeasurementCSSLocationDao() {
		super(MeasurementCSSLocation.class);
	}
}
