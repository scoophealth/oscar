package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.UserDSMessagePrefs;
import org.oscarehr.util.SpringUtils;

public class UserDSMessagePrefsDaoTest {

	private UserDSMessagePrefsDao dao = SpringUtils.getBean(UserDSMessagePrefsDao.class);

	public UserDSMessagePrefsDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("user_ds_message_prefs");
	}

	@Test
	public void testCreate() throws Exception {
		UserDSMessagePrefs entity = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
}
