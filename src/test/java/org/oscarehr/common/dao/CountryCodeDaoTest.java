package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.CountryCode;
import org.oscarehr.util.SpringUtils;

public class CountryCodeDaoTest extends DaoTestFixtures {

	private CountryCodeDao dao = SpringUtils.getBean(CountryCodeDao.class);

	public CountryCodeDaoTest() {

	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("country_codes");
	}

	@Test
	public void testCreate() throws Exception {
		CountryCode entity = new CountryCode();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

}
