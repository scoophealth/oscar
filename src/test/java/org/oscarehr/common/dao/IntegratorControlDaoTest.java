package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.IntegratorControl;
import org.oscarehr.util.SpringUtils;


public class IntegratorControlDaoTest extends DaoTestFixtures {

	private IntegratorControlDao dao = SpringUtils.getBean(IntegratorControlDao.class);

	public IntegratorControlDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("IntegratorControl");
	}

	@Test
	public void testCreate() throws Exception {
		IntegratorControl entity = new IntegratorControl();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
