package org.oscarehr.billing.CA.BC.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.BC.model.TeleplanC12;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class TeleplanC12DaoTest extends DaoTestFixtures {

	private TeleplanC12Dao dao = SpringUtils.getBean(TeleplanC12Dao.class);

	public TeleplanC12DaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("teleplanC12");
	}

	@Test
	public void testCreate() throws Exception {
		TeleplanC12 entity = new TeleplanC12();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
