package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.CtlFrequency;
import org.oscarehr.util.SpringUtils;

public class CtlFrequencyDaoTest extends DaoTestFixtures {

	private CtlFrequencyDao dao = (CtlFrequencyDao)SpringUtils.getBean("ctlFrequencyDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("ctl_frequency");
	}

	@Test
	public void testCreate() throws Exception {
		CtlFrequency entity = new CtlFrequency();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindAll() throws Exception {

		int startNo = dao.findAll().size();

		CtlFrequency entity = new CtlFrequency();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());

		List<CtlFrequency> list = dao.findAll();

		assertNotNull(list);
		assertEquals(list.size(),startNo+1);
	}
}
