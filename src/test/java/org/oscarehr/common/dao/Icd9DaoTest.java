package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Icd9;
import org.oscarehr.util.SpringUtils;

public class Icd9DaoTest extends DaoTestFixtures {

	private Icd9Dao dao = SpringUtils.getBean(Icd9Dao.class);

	public Icd9DaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("icd9");
	}

	@Test
	public void testCreate() throws Exception {
		Icd9 entity = new Icd9();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
