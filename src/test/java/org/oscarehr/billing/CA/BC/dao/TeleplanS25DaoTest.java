package org.oscarehr.billing.CA.BC.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.BC.model.TeleplanS25;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class TeleplanS25DaoTest extends DaoTestFixtures {

	private TeleplanS25Dao dao = SpringUtils.getBean(TeleplanS25Dao.class);

	public TeleplanS25DaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("teleplanS25");
	}

	@Test
	public void testCreate() throws Exception {
		TeleplanS25 entity = new TeleplanS25();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
