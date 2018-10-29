package org.oscarehr.managers;

import org.junit.Before;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class SearchManagerTest extends DaoTestFixtures {
	
	AppointmentSearchManager appointmentSearchManager = SpringUtils.getBean(AppointmentSearchManager.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(new String[] { "demographic", "appointment" });
	}
	
	@Test
	public void searchAppointment() {
		
	}

}
