package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Diseases;
import org.oscarehr.util.SpringUtils;

public class DiseasesDaoTest extends DaoTestFixtures {

	private DiseasesDao dao = SpringUtils.getBean(DiseasesDao.class);

	public DiseasesDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("diseases");
	}

	@Test
	public void testCreate() throws Exception {
		Diseases entity = new Diseases();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}

	@Test
	public void testFindByDemographicNo() throws Exception {
		Diseases entity = new Diseases();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDemographicNo(1);
		dao.persist(entity);

		assertEquals(1,dao.findByDemographicNo(1).size());
	}

	@Test
	public void testFindByIcd9() throws Exception {
		Diseases entity = new Diseases();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDemographicNo(1);
		entity.setIcd9Entry("250");
		dao.persist(entity);

		assertEquals(1,dao.findByIcd9("250").size());
	}
}
