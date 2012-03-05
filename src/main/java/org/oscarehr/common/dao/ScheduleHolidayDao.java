package org.oscarehr.common.dao;

import org.oscarehr.common.model.ScheduleHoliday;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleHolidayDao extends AbstractDao<ScheduleHoliday>{

	public ScheduleHolidayDao() {
		super(ScheduleHoliday.class);
	}
}
