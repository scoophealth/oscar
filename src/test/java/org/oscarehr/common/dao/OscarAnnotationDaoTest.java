package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.OscarAnnotation;
import org.oscarehr.util.SpringUtils;

public class OscarAnnotationDaoTest extends DaoTestFixtures {

	private OscarAnnotationDao dao = SpringUtils.getBean(OscarAnnotationDao.class);

	public OscarAnnotationDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("oscar_annotations");
	}

	@Test
	public void testCreate() throws Exception {
		OscarAnnotation entity = new OscarAnnotation();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
