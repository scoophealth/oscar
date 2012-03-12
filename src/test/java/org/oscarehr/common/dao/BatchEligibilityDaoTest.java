package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.BatchEligibility;
import org.oscarehr.util.SpringUtils;

public class BatchEligibilityDaoTest extends DaoTestFixtures {

	private BatchEligibilityDao dao = (BatchEligibilityDao)SpringUtils.getBean("batchEligibilityDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("batchEligibility");
	}

	@Test
	public void testCreate() throws Exception {
		BatchEligibility entity = new BatchEligibility();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(1000);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
