package org.oscarehr.common.dao;

import org.oscarehr.common.model.MeasurementGroup;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementGroupDao extends AbstractDao<MeasurementGroup>{

	public MeasurementGroupDao() {
		super(MeasurementGroup.class);
	}
}
