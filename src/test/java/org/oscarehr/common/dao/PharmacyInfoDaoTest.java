package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.PharmacyInfo;
import org.oscarehr.util.SpringUtils;

public class PharmacyInfoDaoTest extends DaoTestFixtures {

	private PharmacyInfoDao dao = (PharmacyInfoDao)SpringUtils.getBean("pharmacyInfoDao");

	public PharmacyInfoDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("pharmacyInfo");
	}

	@Test
	public void testCreate() throws Exception {
		PharmacyInfo entity = new PharmacyInfo();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testAddPharmacy() {
		int size = dao.getAllPharmacies().size();
		dao.addPharmacy("name","address","city","province","postalCode", "333-333-3333","444-444-4444", "555-555-5555",
				"a@b.com", "1", "notes");
		int newSize = dao.getAllPharmacies().size();
		assertEquals(size+1,newSize);
		dao.addPharmacy("name","address","city","province","postalCode", "333-333-3333","444-444-4444", "555-555-5555",
				"a@b.com", "1", "notes");
		newSize = dao.getAllPharmacies().size();
		assertEquals(size+2,newSize);
	}

	@Test
	public void testDeletePharmacy() throws Exception {
		PharmacyInfo entity = new PharmacyInfo();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());

		dao.deletePharmacy(String.valueOf(entity.getId2()));

		assertEquals("0",dao.find(entity.getId()).getStatus());
	}

	@Test
	public void testGetPharmacy() throws Exception {
		PharmacyInfo entity = new PharmacyInfo();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());

		PharmacyInfo obj = dao.getPharmacy(String.valueOf(entity.getId2()));

		assertEquals(obj.getId(),entity.getId());
	}

	@Test
	public void testGetPharmacyByRecordID() throws Exception {
		PharmacyInfo entity = new PharmacyInfo();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());

		PharmacyInfo obj = dao.getPharmacyByRecordID(String.valueOf(entity.getId()));

		assertEquals(obj.getId(),entity.getId());
	}

	@Test
	public void testGetAllPharmacies() throws Exception {
		int size = dao.getAllPharmacies().size();
		PharmacyInfo entity = new PharmacyInfo();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setStatus("1");
		dao.persist(entity);
		assertNotNull(entity.getId());
		entity = new PharmacyInfo();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setStatus("1");
		dao.persist(entity);
		assertNotNull(entity.getId());
		entity = new PharmacyInfo();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setStatus("0");
		dao.persist(entity);
		assertNotNull(entity.getId());

		assertEquals(dao.getAllPharmacies().size(),size+2);
	}
}
