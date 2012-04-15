package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.TicklerLink;
import org.oscarehr.util.SpringUtils;

public class TicklerLinkDaoTest {

	private TicklerLinkDao dao = SpringUtils.getBean(TicklerLinkDao.class);

	public TicklerLinkDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("tickler_link");
	}

	@Test
	public void testCreate() throws Exception {
		TicklerLink entity = new TicklerLink();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
