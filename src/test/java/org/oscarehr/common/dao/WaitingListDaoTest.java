package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.WaitingList;
import org.oscarehr.util.SpringUtils;

public class WaitingListDaoTest extends DaoTestFixtures {

	private WaitingListDao dao = SpringUtils.getBean(WaitingListDao.class);

	public WaitingListDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("waitingList");
	}

	@Test
	public void testCreate() throws Exception {
		WaitingList entity = new WaitingList();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
