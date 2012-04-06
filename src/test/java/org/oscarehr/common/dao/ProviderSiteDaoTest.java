package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ProviderSite;
import org.oscarehr.common.model.ProviderSitePK;
import org.oscarehr.util.SpringUtils;

public class ProviderSiteDaoTest extends DaoTestFixtures {

	private ProviderSiteDao dao = SpringUtils.getBean(ProviderSiteDao.class);

	public ProviderSiteDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("providersite");
	}

	@Test
	public void testCreate() {
		ProviderSite entity = new ProviderSite();
		entity.setId(new ProviderSitePK());
		entity.getId().setProviderNo("000001");
		entity.getId().setSiteId(1);
		dao.persist(entity);
		assertNotNull(entity.getId());
		assertNotNull(dao.find(entity.getId()));
	}

	@Test
	public void testFindByProviderNo() {
		ProviderSite entity = new ProviderSite();
		entity.setId(new ProviderSitePK());
		entity.getId().setProviderNo("000001");
		entity.getId().setSiteId(1);
		dao.persist(entity);

		assertEquals(1,dao.findByProviderNo("000001").size());
	}
}
