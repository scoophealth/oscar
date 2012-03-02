package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.util.SpringUtils;

public class AllergyDaoTest extends DaoTestFixtures {

	private AllergyDao dao = (AllergyDao)SpringUtils.getBean("AllergyDao");

	public AllergyDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(new String[]{"allergies"});
	}

	@Test
	public void testCreate() throws Exception {
		 Allergy allergy = new Allergy();
		 EntityDataGenerator.generateTestDataForModelClass(allergy);
		 dao.persist(allergy);
		 assertNotNull(allergy.getId());
	}

	@Test
	public void testCount() throws Exception {
		Allergy allergy = new Allergy();
		EntityDataGenerator.generateTestDataForModelClass(allergy);
		dao.persist(allergy);
		assertEquals(dao.getCountAll(),1);

	}

}

