package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ClinicLocation;
import org.oscarehr.util.SpringUtils;

public class ClinicLocationDaoTest extends DaoTestFixtures {

	private ClinicLocationDao dao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("clinic_location");
	}

	@Test
	public void testCreate() throws Exception {
		ClinicLocation entity = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindByClinicNo() throws Exception {
		ClinicLocation entity = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setClinicNo(2);
		dao.persist(entity);

		List<ClinicLocation> clinicLocations = dao.findByClinicNo(2);
		assertNotNull(clinicLocations);
		assertEquals(clinicLocations.size(),1);
	}

	@Test
	public void testSearchVisitLocation() throws Exception {
		ClinicLocation entity = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setClinicNo(2);
		entity.setClinicLocationNo("1000");
		dao.persist(entity);

		String name = dao.searchVisitLocation("1000");
		assertEquals(name,entity.getClinicLocationName());
	}

	@Test
	public void testSearchBillLocation() throws Exception {
		ClinicLocation entity = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setClinicNo(2);
		entity.setClinicLocationNo("1000");
		dao.persist(entity);

		ClinicLocation result = dao.searchBillLocation(2,"1000");
		assertEquals(result.getId(),entity.getId());
	}

	@Test
	public void testRemoveByClinicLocationNo() throws Exception {
		ClinicLocation entity = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setClinicNo(2);
		entity.setClinicLocationNo("1000");
		dao.persist(entity);

		dao.removeByClinicLocationNo("1000");
		assertNull(dao.searchBillLocation(2, "1000"));
	}
}
