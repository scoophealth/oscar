package org.oscarehr.billing.CA.dao;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.model.GstControl;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class GstControlDaoTest {

	private GstControlDao dao = SpringUtils.getBean(GstControlDao.class);

	public GstControlDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("gstControl");
	}

	@Test
	public void testCreate() throws Exception {
		GstControl entity = new GstControl();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setGstPercent(new BigDecimal("13.00"));
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
