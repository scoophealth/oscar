package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.View;
import org.oscarehr.util.SpringUtils;

public class ViewDaoTest extends DaoTestFixtures {

	private ViewDao dao = SpringUtils.getBean(ViewDao.class);

	public ViewDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("view");
	}

	@Test
	public void testCreate() throws Exception {
		View entity = new View();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
