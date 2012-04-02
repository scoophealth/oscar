package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DemographicStudy;
import org.oscarehr.common.model.DemographicStudyPK;
import org.oscarehr.util.SpringUtils;

public class DemographicStudyDaoTest extends DaoTestFixtures {

	private DemographicStudyDao dao = SpringUtils.getBean(DemographicStudyDao.class);

	public DemographicStudyDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographicstudy");
	}

	@Test
	public void testCreate() throws Exception {
		DemographicStudy entity = new DemographicStudy();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(new DemographicStudyPK());
		entity.getId().setDemographicNo(1);
		entity.getId().setStudyNo(1);
		dao.persist(entity);

	}

	@Test
	public void testFind() throws Exception {
		DemographicStudy entity = new DemographicStudy();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(new DemographicStudyPK());
		entity.getId().setDemographicNo(1);
		entity.getId().setStudyNo(1);
		dao.persist(entity);

		DemographicStudyPK pk = new DemographicStudyPK();
		pk.setDemographicNo(1);
		pk.setStudyNo(1);
		assertNotNull(dao.find(pk));
	}

	public void testRemoveByDemographicNo() throws Exception {
		DemographicStudy entity = new DemographicStudy();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(new DemographicStudyPK());
		entity.getId().setDemographicNo(1);
		entity.getId().setStudyNo(1);
		dao.persist(entity);

		entity = new DemographicStudy();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(new DemographicStudyPK());
		entity.getId().setDemographicNo(1);
		entity.getId().setStudyNo(2);
		dao.persist(entity);

		entity = new DemographicStudy();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(new DemographicStudyPK());
		entity.getId().setDemographicNo(2);
		entity.getId().setStudyNo(1);
		dao.persist(entity);

		assertEquals(2,dao.removeByDemographicNo(1));
	}

	@Test
	public void testFindByDemographicNoAndStudyNo() throws Exception {
		DemographicStudy entity = new DemographicStudy();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(new DemographicStudyPK());
		entity.getId().setDemographicNo(1);
		entity.getId().setStudyNo(1);
		dao.persist(entity);

		assertNotNull(dao.findByDemographicNoAndStudyNo(1,1));
	}

}
