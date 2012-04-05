package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ScheduleDate;
import org.oscarehr.util.SpringUtils;

public class ScheduleDateDaoTest extends DaoTestFixtures {

	private ScheduleDateDao dao = SpringUtils.getBean(ScheduleDateDao.class);

	public ScheduleDateDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("scheduledate");
	}

	@Test
	public void testCreate() throws Exception {
		ScheduleDate entity = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}

	@Test
	public void testFindByProviderNoAndDate()  {
		ScheduleDate entity = new ScheduleDate();
		entity.setAvailable('1');
		entity.setCreator("000001");
		entity.setDate(new Date());
		entity.setHour("12");
		entity.setPriority('P');
		entity.setProviderNo("000001");
		entity.setReason("reason");
		entity.setStatus('A');
		dao.persist(entity);

		assertNotNull(dao.findByProviderNoAndDate("000001", new Date()));
		assertEquals(entity.getId(),dao.findByProviderNoAndDate("000001", new Date()).getId());

	}
}
