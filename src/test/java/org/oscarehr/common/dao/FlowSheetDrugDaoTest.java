package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.FlowSheetDrug;
import org.oscarehr.util.SpringUtils;

public class FlowSheetDrugDaoTest {

	private FlowSheetDrugDao dao = SpringUtils.getBean(FlowSheetDrugDao.class);

	public FlowSheetDrugDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("flowsheet_drug");
	}

	@Test
	public void testCreate() throws Exception {
		FlowSheetDrug entity = new FlowSheetDrug();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
