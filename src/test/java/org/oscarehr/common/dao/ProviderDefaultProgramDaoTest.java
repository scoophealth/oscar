package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ProviderDefaultProgram;
import org.oscarehr.util.SpringUtils;

public class ProviderDefaultProgramDaoTest extends DaoTestFixtures {

	private ProviderDefaultProgramDao dao = SpringUtils.getBean(ProviderDefaultProgramDao.class);

	public ProviderDefaultProgramDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("provider_default_program");
	}

	@Test
	public void testCreate() throws Exception {
		ProviderDefaultProgram entity = new ProviderDefaultProgram();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
