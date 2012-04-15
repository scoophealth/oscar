package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.util.SpringUtils;

public class ClinicDAOTest extends DaoTestFixtures {

	private ClinicDAO dao = (ClinicDAO)SpringUtils.getBean(ClinicDAO.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("clinic");
	}

	@Test
	public void testCreate() throws Exception {
		Clinic entity = new Clinic();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
