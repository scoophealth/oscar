package org.oscarehr.billing.CA.BC.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.BC.model.TeleplanS23;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class TeleplanS23DaoTest extends DaoTestFixtures {

	private TeleplanS23Dao dao = SpringUtils.getBean(TeleplanS23Dao.class);

	public TeleplanS23DaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("teleplanS23");
	}

	@Test
	public void testCreate() throws Exception {
		TeleplanS23 entity = new TeleplanS23();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
