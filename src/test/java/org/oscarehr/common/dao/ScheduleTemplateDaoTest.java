package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ScheduleTemplate;
import org.oscarehr.common.model.ScheduleTemplatePrimaryKey;
import org.oscarehr.util.SpringUtils;

public class ScheduleTemplateDaoTest extends DaoTestFixtures {

	private ScheduleTemplateDao dao = SpringUtils.getBean(ScheduleTemplateDao.class);

	public ScheduleTemplateDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("scheduletemplate");
	}

	@Test
	public void testCreate() throws Exception {
		ScheduleTemplate entity = new ScheduleTemplate();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(new ScheduleTemplatePrimaryKey("000001","a"));
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
