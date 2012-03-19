package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.util.SpringUtils;

public class ConsultDocsDaoTest extends DaoTestFixtures {

	private ConsultDocsDao dao = (ConsultDocsDao)SpringUtils.getBean("consultDocsDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("consultdocs");
	}

	@Test
	public void testCreate() throws Exception {
		ConsultDocs entity = new ConsultDocs();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindByX()  {
		ConsultDocs entity = new ConsultDocs();
		entity.setAttachDate(new Date());
		entity.setDeleted(null);
		entity.setDocType("D");
		entity.setDocumentNo(99);
		entity.setProviderNo("999998");
		entity.setRequestId(55);

		dao.persist(entity);
		assertNotNull(entity.getId());

		List<ConsultDocs> consultDocs = dao.findByRequestIdDocumentNoAndDocumentType
		(entity.getRequestId(), entity.getDocumentNo(), entity.getDocType());

		assertNotNull(consultDocs);
		assertEquals(consultDocs.size(),1);
		assertEquals(consultDocs.get(0).getId(),entity.getId());
	}

}
