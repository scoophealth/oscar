package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ScheduleTemplateCode;
import org.oscarehr.util.SpringUtils;

public class ScheduleTemplateCodeDaoTest extends DaoTestFixtures {

	private ScheduleTemplateCodeDao dao = SpringUtils.getBean(ScheduleTemplateCodeDao.class);

	public ScheduleTemplateCodeDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("scheduletemplatecode");
	}

	@Test
	public void testCreate() throws Exception {
		ScheduleTemplateCode entity = new ScheduleTemplateCode();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setCode('A');
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
