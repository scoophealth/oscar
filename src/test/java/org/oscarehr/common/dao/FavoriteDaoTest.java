package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Favorite;
import org.oscarehr.util.SpringUtils;

public class FavoriteDaoTest extends DaoTestFixtures {

	private FavoriteDao dao = SpringUtils.getBean(FavoriteDao.class);

	public FavoriteDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("favorites");
	}

	@Test
	public void testCreate() throws Exception {
		Favorite entity = new Favorite();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
