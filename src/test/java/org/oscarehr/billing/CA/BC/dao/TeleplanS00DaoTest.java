package org.oscarehr.billing.CA.BC.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.BC.model.TeleplanS00;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class TeleplanS00DaoTest extends DaoTestFixtures {

	private TeleplanS00Dao dao = SpringUtils.getBean(TeleplanS00Dao.class);

	public TeleplanS00DaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("teleplanS00");
	}

	@Test
	public void testCreate() throws Exception {
		TeleplanS00 entity = new TeleplanS00();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
