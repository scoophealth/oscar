package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DemographicCust;
import org.oscarehr.util.SpringUtils;

public class DemographicCustDaoTest extends DaoTestFixtures {

	private DemographicCustDao dao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographiccust");
	}

	@Test
	public void testCreate() throws Exception {
		DemographicCust entity = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(1);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindMultipleMidwife() throws Exception {
		DemographicCust entity = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(1);
		entity.setMidwife("a");
		dao.persist(entity);

		entity = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(2);
		entity.setMidwife("a");
		dao.persist(entity);

		entity = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(3);
		entity.setMidwife("b");
		dao.persist(entity);

		List<Integer> dnos = new ArrayList<Integer>();
		dnos.add(1); dnos.add(2); dnos.add(3);

		List<DemographicCust> dc = dao.findMultipleMidwife(dnos, "a");
		assertEquals(dc.size(),2);
	}

}
