package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderSite;
import org.oscarehr.common.model.ProviderSitePK;
import org.oscarehr.common.model.Site;
import org.oscarehr.util.SpringUtils;

public class SiteDaoTest extends DaoTestFixtures {

	private SiteDao dao = SpringUtils.getBean(SiteDao.class);

	public SiteDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("site","providersite","provider");
	}

	@Test
	public void testCreate() throws Exception {
		Site entity = new Site();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setStatus((byte)1);
		dao.persist(entity);
		assertNotNull(entity.getId());
		assertNotNull(dao.find(entity.getId()));
	}

	@Test
	public void testManyToManyRelationship() throws Exception {
		Site entity = new Site();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setStatus((byte)1);
		dao.persist(entity);
		assertNotNull(entity.getId());
		Integer siteId1 = entity.getId();

		entity = new Site();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setStatus((byte)1);
		dao.persist(entity);
		assertNotNull(entity.getId());
		Integer siteId2 = entity.getId();

		//create a provider
		Provider p = new Provider();
		p.setProviderNo("000001");
		p.setLastName("Smith");
		p.setFirstName("John");
		p.setProviderType("doctor");
		p.setDob(new Date());
		p.setLastUpdateUser("999998");
		p.setLastUpdateDate(new Date());
		p.setSex("M");
		p.setSpecialty("");
		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		providerDao.saveProvider(p);

		ProviderSiteDao providerSiteDao = SpringUtils.getBean(ProviderSiteDao.class);
		ProviderSite ps = new ProviderSite();
		ps.setId(new ProviderSitePK());
		ps.getId().setProviderNo(p.getProviderNo());
		ps.getId().setSiteId(siteId1);
		providerSiteDao.persist(ps);

		ps = new ProviderSite();
		ps.setId(new ProviderSitePK());
		ps.getId().setProviderNo(p.getProviderNo());
		ps.getId().setSiteId(siteId2);
		providerSiteDao.persist(ps);

		Site s = dao.find(siteId1);
		assertNotNull(s.getProviders());
		assertEquals(s.getProviders().size(),1);
	}


}
