package org.oscarehr.billing.CA.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.model.BillingDetail;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class BillingDetailDaoTest {

	private BillingDetailDao dao = SpringUtils.getBean(BillingDetailDao.class);

	public BillingDetailDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billingdetail");
	}

	@Test
	public void testCreate() throws Exception {
		BillingDetail entity = new BillingDetail();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
