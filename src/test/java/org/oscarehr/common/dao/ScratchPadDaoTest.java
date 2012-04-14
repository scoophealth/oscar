package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ScratchPad;
import org.oscarehr.util.SpringUtils;

public class ScratchPadDaoTest extends DaoTestFixtures {

	private ScratchPadDao dao = SpringUtils.getBean(ScratchPadDao.class);

	public ScratchPadDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("scratch_pad");
	}

	@Test
	public void testCreate() throws Exception {
		ScratchPad entity = new ScratchPad();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
