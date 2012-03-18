package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.util.SpringUtils;

public class ConsultationServicesDaoTest extends DaoTestFixtures {

	private ConsultationServiceDao dao = (ConsultationServiceDao)SpringUtils.getBean("consultationServiceDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("consultationServices","serviceSpecialists","professionalSpecialists");
	}

	@Test
	public void testCreate() throws Exception {
		ConsultationServices entity = new ConsultationServices();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

}
