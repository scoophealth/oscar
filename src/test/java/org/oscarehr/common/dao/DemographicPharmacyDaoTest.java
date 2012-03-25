package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DemographicPharmacy;
import org.oscarehr.util.SpringUtils;

public class DemographicPharmacyDaoTest extends DaoTestFixtures {

	private DemographicPharmacyDao dao = (DemographicPharmacyDao)SpringUtils.getBean(DemographicPharmacyDao.class);

	public DemographicPharmacyDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographicPharmacy");
	}

	@Test
	public void testCreate() throws Exception {
		DemographicPharmacy entity = new DemographicPharmacy();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testDataMethods()  {
		dao.addPharmacyToDemographic("1","100");
		assertEquals(1,dao.findByDemographicId("100").getPharmacyId());
	}
}
