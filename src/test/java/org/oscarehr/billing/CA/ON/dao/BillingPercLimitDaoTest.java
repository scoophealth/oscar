package org.oscarehr.billing.CA.ON.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.ON.model.BillingPercLimit;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class BillingPercLimitDaoTest {

	private BillingPercLimitDao dao = SpringUtils.getBean(BillingPercLimitDao.class);

	public BillingPercLimitDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billingperclimit");
	}

	@Test
	public void testCreate() throws Exception {
		BillingPercLimit entity = new BillingPercLimit();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
