package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ConfigImmunization;
import org.oscarehr.util.SpringUtils;

public class ConfigImmunizationDaoTest extends DaoTestFixtures {

	private ConfigImmunizationDao dao = (ConfigImmunizationDao)SpringUtils.getBean("configImmunizationDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("config_Immunization");
	}

	@Test
	public void testCreate() throws Exception {
		ConfigImmunization entity = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindAll() throws Exception {
		int initialSize = dao.findAll().size();

		ConfigImmunization entity = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName(null);
		entity.setArchived(0);
		dao.persist(entity);

		assertEquals(dao.findAll().size(),initialSize);

		entity = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setName("");
		entity.setArchived(0);
		dao.persist(entity);

		assertEquals(dao.findAll().size(),initialSize);

	}

	@Test
	public void testFindByArchive() throws Exception {
		int initialSize0 = dao.findByArchived(0,true).size();
		int initialSize1 = dao.findByArchived(1,false).size();

		ConfigImmunization entity = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setArchived(0);
		dao.persist(entity);

		assertEquals(dao.findByArchived(0,true).size(),initialSize0+1);

		entity = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setArchived(1);
		dao.persist(entity);

		assertEquals(dao.findByArchived(1,false).size(),initialSize1+1);

	}
}
