package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Billingreferral;
import org.oscarehr.util.SpringUtils;

public class BillingreferralDaoTest extends DaoTestFixtures {

	private BillingreferralDao dao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDAO");

	public BillingreferralDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billingreferral");
	}

	@Test
	public void testCreate() throws Exception {
		Billingreferral entity = new Billingreferral();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setBillingreferralNo(null);
		dao.updateBillingreferral(entity);
		assertNotNull(entity.getBillingreferralNo());
	}

	@Test
	public void testSearchReferralCode() throws Exception {
		Billingreferral entity = new Billingreferral();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setBillingreferralNo(null);
		entity.setReferralNo("123456");
		entity.setLastName("Smith");
		entity.setFirstName("John");
		dao.updateBillingreferral(entity);

		assertEquals(1,dao.searchReferralCode("123456", "123456", "123456", "Smith", "John", "Smith", "John", "Smith", "John").size());
	}
}
