package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Document;
import org.oscarehr.util.SpringUtils;

public class DocumentDaoTest extends DaoTestFixtures {

	private DocumentDao dao = SpringUtils.getBean(DocumentDao.class);

	public DocumentDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("document");
	}

	@Test
	public void testCreate() throws Exception {
		Document entity = new Document();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDocumentNo(null);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
