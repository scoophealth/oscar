package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.CtlSpecialInstructions;
import org.oscarehr.util.SpringUtils;

public class CtlSpecialInstructionsDaoTest extends DaoTestFixtures {

	private CtlSpecialInstructionsDao dao = (CtlSpecialInstructionsDao)SpringUtils.getBean("ctlSpecialInstructionsDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("ctl_specialinstructions");
	}

	@Test
	public void testCreate() throws Exception {
		CtlSpecialInstructions entity = new CtlSpecialInstructions();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindAll() throws Exception {

		int startNo = dao.findAll().size();

		CtlSpecialInstructions entity = new CtlSpecialInstructions();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());

		List<CtlSpecialInstructions> list = dao.findAll();

		assertNotNull(list);
		assertEquals(list.size(),startNo+1);
	}
}
