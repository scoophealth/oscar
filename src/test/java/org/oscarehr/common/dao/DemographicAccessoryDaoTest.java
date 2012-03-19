package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DemographicAccessory;
import org.oscarehr.util.SpringUtils;

public class DemographicAccessoryDaoTest extends DaoTestFixtures {

	private DemographicAccessoryDao dao = (DemographicAccessoryDao)SpringUtils.getBean("demographicAccessoryDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographicaccessory");
	}

	@Test
	public void testCreate() throws Exception {
		DemographicAccessory entity = new DemographicAccessory();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDemographicNo(1);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testCount() throws Exception {
		DemographicAccessory entity = new DemographicAccessory();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDemographicNo(100);
		dao.persist(entity);
		assertNotNull(entity.getId());

		assertEquals(dao.findCount(100),1);
	}


}
