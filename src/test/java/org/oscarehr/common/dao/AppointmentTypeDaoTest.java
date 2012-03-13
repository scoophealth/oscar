package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.AppointmentType;
import org.oscarehr.util.SpringUtils;

public class AppointmentTypeDaoTest extends DaoTestFixtures {

	private AppointmentTypeDao dao = (AppointmentTypeDao)SpringUtils.getBean("appointmentTypeDao");

	public AppointmentTypeDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("appointmentType");
	}

	@Test
	public void testCreate() throws Exception {
		 AppointmentType entity = new AppointmentType();
		 EntityDataGenerator.generateTestDataForModelClass(entity);
		 dao.persist(entity);
		 assertNotNull(entity.getId());
	}
}
