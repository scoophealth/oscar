package org.oscarehr.billing.CA.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.model.BillActivity;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class BillActivityDaoTest {

	private BillActivityDao dao = SpringUtils.getBean(BillActivityDao.class);

	public BillActivityDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billactivity");
	}

	@Test
	public void testCreate() throws Exception {
		BillActivity entity = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
