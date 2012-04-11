package org.oscarehr.billing.CA.ON.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.ON.model.BillingClaimHeader1;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class BillingClaimDAOTest {

	private BillingClaimDAO dao = SpringUtils.getBean(BillingClaimDAO.class);

	public BillingClaimDAOTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billing_on_cheader1","billing_on_item");
	}

	@Test
	public void testCreate() throws Exception {
		BillingClaimHeader1 entity = new BillingClaimHeader1();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
