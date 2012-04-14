package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.FlowSheetDx;
import org.oscarehr.util.SpringUtils;

public class FlowSheetDxDaoTest extends DaoTestFixtures {

	private FlowSheetDxDao dao = SpringUtils.getBean(FlowSheetDxDao.class);

	public FlowSheetDxDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("flowsheet_dx");
	}

	@Test
	public void testCreate() throws Exception {
		FlowSheetDx entity = new FlowSheetDx();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
