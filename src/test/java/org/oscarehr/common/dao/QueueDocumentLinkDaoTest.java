package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.QueueDocumentLink;
import org.oscarehr.util.SpringUtils;

public class QueueDocumentLinkDaoTest extends DaoTestFixtures {

	private QueueDocumentLinkDao dao = SpringUtils.getBean(QueueDocumentLinkDao.class);

	public QueueDocumentLinkDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("queue_document_link");
	}

	@Test
	public void testCreate() throws Exception {
		QueueDocumentLink entity = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
