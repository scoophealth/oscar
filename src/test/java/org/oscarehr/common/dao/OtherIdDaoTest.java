package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.OtherId;
import org.oscarehr.util.SpringUtils;

public class OtherIdDaoTest extends DaoTestFixtures {

	private OtherIdDAO dao = SpringUtils.getBean(OtherIdDAO.class);

	public OtherIdDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("other_id");
	}

	@Test
	public void testCreate() throws Exception {
		OtherId entity = new OtherId();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
