package org.oscarehr.common.dao;

import org.oscarehr.common.model.ScheduleTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleTemplateDao extends AbstractDao<ScheduleTemplate> {
	
	public ScheduleTemplateDao() {
		super(ScheduleTemplate.class);
	}
	
}
