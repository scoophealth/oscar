package org.oscarehr.common.dao;

import org.oscarehr.common.model.RSchedule;
import org.springframework.stereotype.Repository;

@Repository
public class RScheduleDao extends AbstractDao<RSchedule>{

	public RScheduleDao() {
		super(RSchedule.class);
	}
}
