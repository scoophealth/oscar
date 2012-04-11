package org.oscarehr.common.dao;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.dao.BillingInrDao;
import org.oscarehr.billing.CA.model.BillingInr;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class BillingInrDaoTest {

	private BillingInrDao dao = SpringUtils.getBean(BillingInrDao.class);

	public BillingInrDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billinginr");
	}

	@Test
	public void testCreate() throws Exception {
		BillingInr entity = new BillingInr();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}