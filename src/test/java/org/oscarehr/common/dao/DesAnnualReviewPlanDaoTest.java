package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DesAnnualReviewPlan;
import org.oscarehr.util.SpringUtils;

public class DesAnnualReviewPlanDaoTest extends DaoTestFixtures {

	private DesAnnualReviewPlanDao dao = SpringUtils.getBean(DesAnnualReviewPlanDao.class);

	public DesAnnualReviewPlanDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("desannualreviewplan");
	}

	@Test
	public void testCreate() throws Exception {
		DesAnnualReviewPlan entity = new DesAnnualReviewPlan();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}

	@Test
	public void testSearch() throws Exception {
		DesAnnualReviewPlan entity = new DesAnnualReviewPlan();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDemographicNo(1);
		entity.setFormNo(1);
		dao.persist(entity);

		entity = new DesAnnualReviewPlan();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDemographicNo(1);
		entity.setFormNo(2);
		dao.persist(entity);

		DesAnnualReviewPlan darp = dao.search(2, 1);
		assertNotNull(darp);



	}
}
