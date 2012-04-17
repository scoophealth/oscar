package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.SpringUtils;

public class UserPropertyDaoTest extends DaoTestFixtures {

	private UserPropertyDAO dao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

	public UserPropertyDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("property");
	}

	@Test
	public void testCreate() throws Exception {
		UserProperty entity = new UserProperty();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
