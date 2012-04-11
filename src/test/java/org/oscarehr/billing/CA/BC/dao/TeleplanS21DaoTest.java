package org.oscarehr.billing.CA.BC.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.BC.model.TeleplanS21;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class TeleplanS21DaoTest extends DaoTestFixtures {

	private TeleplanS21Dao dao = SpringUtils.getBean(TeleplanS21Dao.class);

	public TeleplanS21DaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("teleplanS21");
	}

	@Test
	public void testCreate() throws Exception {
		TeleplanS21 entity = new TeleplanS21();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
