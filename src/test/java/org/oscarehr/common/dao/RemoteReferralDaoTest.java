package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.RemoteReferral;
import org.oscarehr.util.SpringUtils;

public class RemoteReferralDaoTest extends DaoTestFixtures {

	private RemoteReferralDao dao = (RemoteReferralDao)SpringUtils.getBean("remoteReferralDao");

	public RemoteReferralDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(new String[]{"RemoteReferral"});
	}

	@Test
	public void testCreate() throws Exception {
		RemoteReferral entity = new RemoteReferral();
		 EntityDataGenerator.generateTestDataForModelClass(entity);
		 dao.persist(entity);
		 assertNotNull(entity.getId());
	}

	@Test
	public void testCount() throws Exception {
		RemoteReferral entity = new RemoteReferral();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertEquals(dao.getCountAll(),1);

	}
}
