package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Desaprisk;
import org.oscarehr.util.SpringUtils;

public class DesapriskDaoTest extends DaoTestFixtures {

	private DesapriskDao dao = SpringUtils.getBean(DesapriskDao.class);

	public DesapriskDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("desaprisk");
	}

	@Test
	public void testCreate() throws Exception {
		Desaprisk entity = new Desaprisk();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
