package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DiagnosticCode;
import org.oscarehr.util.SpringUtils;

public class DiagnosticCodeDaoTest extends DaoTestFixtures {

	private DiagnosticCodeDao dao = SpringUtils.getBean(DiagnosticCodeDao.class);

	public DiagnosticCodeDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("diagnosticcode");
	}

	@Test
	public void testCreate() throws Exception {
		DiagnosticCode entity = new DiagnosticCode();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}

	@Test
	public void testFindByDiagnosticCode() throws Exception {
		DiagnosticCode entity = new DiagnosticCode();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDiagnosticCode("a");
		dao.persist(entity);

		assertEquals(1,dao.findByDiagnosticCode(entity.getDiagnosticCode()).size());
	}

	@Test
	public void testFindByDiagnosticCodeAndRegion() throws Exception {
		DiagnosticCode entity = new DiagnosticCode();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDiagnosticCode("a");
		entity.setRegion("b");
		dao.persist(entity);

		assertEquals(1,dao.findByDiagnosticCodeAndRegion(entity.getDiagnosticCode(),entity.getRegion()).size());
	}
}
