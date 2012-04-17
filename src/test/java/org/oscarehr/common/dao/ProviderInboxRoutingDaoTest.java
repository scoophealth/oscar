package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ProviderInboxItem;
import org.oscarehr.util.SpringUtils;

public class ProviderInboxRoutingDaoTest extends DaoTestFixtures {

	private ProviderInboxRoutingDao dao = SpringUtils.getBean(ProviderInboxRoutingDao.class);

	public ProviderInboxRoutingDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("providerLabRouting");
	}

	@Test
	public void testCreate() throws Exception {
		ProviderInboxItem entity = new ProviderInboxItem();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
