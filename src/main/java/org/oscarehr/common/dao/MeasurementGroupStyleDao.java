package org.oscarehr.common.dao;

import org.oscarehr.common.model.MeasurementGroupStyle;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementGroupStyleDao extends AbstractDao<MeasurementGroupStyle>{

	public MeasurementGroupStyleDao() {
		super(MeasurementGroupStyle.class);
	}
}
