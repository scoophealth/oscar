package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.RSchedule;
import org.oscarehr.util.SpringUtils;

public class RScheduleDaoTest extends DaoTestFixtures {

	private RScheduleDao dao = (RScheduleDao) SpringUtils.getBean("rScheduleDao");

	public RScheduleDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("rschedule");
	}

	@Test
	public void testCreate() throws Exception {
		RSchedule entity = new RSchedule();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}

	@Test
	public void testFindByProviderAvailableAndDate() throws Exception {
		RSchedule entity = new RSchedule();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setProviderNo("000001");
		entity.setAvailable("1");
		entity.setsDate(new Date());
		dao.persist(entity);

		assertEquals(1,dao.findByProviderAvailableAndDate("000001", "1", new Date()).size());
	}
}
