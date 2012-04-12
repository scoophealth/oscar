package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ProviderFacility;
import org.oscarehr.common.model.ProviderFacilityPK;
import org.oscarehr.util.SpringUtils;

public class ProviderFacilityDaoTest {

	private ProviderFacilityDao dao = SpringUtils.getBean(ProviderFacilityDao.class);

	public ProviderFacilityDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("provider_facility");
	}

	@Test
	public void testCreate() {
		ProviderFacility entity = new ProviderFacility();
		entity.setId(new ProviderFacilityPK());
		entity.getId().setProviderNo("000001");
		entity.getId().setFacilityId(1);
		dao.persist(entity);
		assertNotNull(entity.getId());
		assertNotNull(dao.find(entity.getId()));
	}
}
