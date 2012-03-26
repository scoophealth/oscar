package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DemographicQueryFavourite;
import org.oscarehr.util.SpringUtils;

public class DemographicQueryFavouritesDaoTest extends DaoTestFixtures {

	private DemographicQueryFavouritesDao dao = (DemographicQueryFavouritesDao)SpringUtils.getBean(DemographicQueryFavouritesDao.class);

	public DemographicQueryFavouritesDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographicQueryFavourites");
	}

	@Test
	public void testCreate() throws Exception {
		DemographicQueryFavourite entity = new DemographicQueryFavourite();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindByArchived() throws Exception {
		DemographicQueryFavourite entity = new DemographicQueryFavourite();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setArchived("1");
		dao.persist(entity);
		entity = new DemographicQueryFavourite();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setArchived("1");
		dao.persist(entity);
		entity = new DemographicQueryFavourite();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setArchived("0");
		dao.persist(entity);

		assertEquals(2,dao.findByArchived("1").size());
	}
}
