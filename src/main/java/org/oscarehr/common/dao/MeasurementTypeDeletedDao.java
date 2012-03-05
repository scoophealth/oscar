package org.oscarehr.common.dao;

import org.oscarehr.common.model.MeasurementTypeDeleted;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementTypeDeletedDao extends AbstractDao<MeasurementTypeDeleted>{

	public MeasurementTypeDeletedDao() {
		super(MeasurementTypeDeleted.class);
	}
}
