package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.FlowSheetCustomization;
import org.oscarehr.util.SpringUtils;

public class FlowSheetCustomizationDaoTest extends DaoTestFixtures{

	private FlowSheetCustomizationDao dao = SpringUtils.getBean(FlowSheetCustomizationDao.class);

	public FlowSheetCustomizationDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("flowsheet_customization");
	}

	@Test
	public void testCreate() throws Exception {
		FlowSheetCustomization entity = new FlowSheetCustomization();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
