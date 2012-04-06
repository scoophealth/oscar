package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.MyGroup;
import org.oscarehr.common.model.MyGroupPrimaryKey;
import org.oscarehr.util.SpringUtils;

public class MyGroupDaoTest extends DaoTestFixtures {

	private MyGroupDao dao = SpringUtils.getBean(MyGroupDao.class);

	public MyGroupDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("mygroup");
	}

	@Test
	public void testCreate() throws Exception {
		MyGroup entity = new MyGroup();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(new MyGroupPrimaryKey());
		entity.getId().setMyGroupNo("a");
		entity.getId().setProviderNo("999998");
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
