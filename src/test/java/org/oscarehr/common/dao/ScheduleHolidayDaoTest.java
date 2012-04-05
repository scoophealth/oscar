package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ScheduleHoliday;
import org.oscarehr.util.SpringUtils;

public class ScheduleHolidayDaoTest extends DaoTestFixtures {

	private ScheduleHolidayDao dao = SpringUtils.getBean(ScheduleHolidayDao.class);

	public ScheduleHolidayDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("scheduleholiday");
	}

	@Test
	public void testCreate() throws Exception {
		ScheduleHoliday entity = new ScheduleHoliday();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(new Date());
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
