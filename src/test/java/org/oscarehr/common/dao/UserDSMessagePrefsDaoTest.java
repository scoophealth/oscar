/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.UserDSMessagePrefs;
import org.oscarehr.util.SpringUtils;

public class UserDSMessagePrefsDaoTest extends DaoTestFixtures {

	protected UserDSMessagePrefsDao dao = SpringUtils.getBean(UserDSMessagePrefsDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

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

	@Test
	public void testGetMessagePrefsOnType() throws Exception {
		
		String providerNo1 = "101";
		String providerNo2 = "202";
		
		String resourceType1 = "alpha";
		String resourceType2 = "bravo";
		
		boolean isArchived = true;
		
		UserDSMessagePrefs userDSMessagePrefs1 = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(userDSMessagePrefs1);
		userDSMessagePrefs1.setProviderNo(providerNo1);
		userDSMessagePrefs1.setResourceType(resourceType1);
		userDSMessagePrefs1.setArchived(isArchived);
		dao.persist(userDSMessagePrefs1);
		
		UserDSMessagePrefs userDSMessagePrefs2 = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(userDSMessagePrefs2);
		userDSMessagePrefs2.setProviderNo(providerNo2);
		userDSMessagePrefs2.setResourceType(resourceType2);
		userDSMessagePrefs2.setArchived(isArchived);
		dao.persist(userDSMessagePrefs2);
		
		UserDSMessagePrefs userDSMessagePrefs3 = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(userDSMessagePrefs3);
		userDSMessagePrefs3.setProviderNo(providerNo1);
		userDSMessagePrefs3.setResourceType(resourceType1);
		userDSMessagePrefs3.setArchived(!isArchived);
		dao.persist(userDSMessagePrefs3);
		
		UserDSMessagePrefs expectedResult = userDSMessagePrefs1;
		UserDSMessagePrefs result = dao.getMessagePrefsOnType(providerNo1, resourceType1);
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testGetHashofMessages() throws Exception {
		
		String providerNo1 = "101";
		String providerNo2 = "202";
		
		String resourceType1 = "alpha";
		String resourceType2 = "bravo";
		
		boolean isArchived = true;
		
		Date resourceUpdatedDate1 = new Date(dfm.parse("20111221").getTime());
		Date resourceUpdatedDate2 = new Date(dfm.parse("20101011").getTime());
		Date resourceUpdatedDate3 = new Date(dfm.parse("20091123").getTime());
		
		String resourceId1 = "111";
		String resourceId2 = "222";
		
		UserDSMessagePrefs userDSMessagePrefs1 = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(userDSMessagePrefs1);
		userDSMessagePrefs1.setResourceId(resourceId1);
		userDSMessagePrefs1.setProviderNo(providerNo1);
		userDSMessagePrefs1.setResourceType(resourceType1);
		userDSMessagePrefs1.setArchived(isArchived);
		userDSMessagePrefs1.setResourceUpdatedDate(resourceUpdatedDate1);
		dao.persist(userDSMessagePrefs1);
		
		UserDSMessagePrefs userDSMessagePrefs2 = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(userDSMessagePrefs2);
		userDSMessagePrefs2.setResourceId(resourceId2);
		userDSMessagePrefs2.setProviderNo(providerNo2);
		userDSMessagePrefs2.setResourceType(resourceType2);
		userDSMessagePrefs2.setArchived(isArchived);
		userDSMessagePrefs2.setResourceUpdatedDate(resourceUpdatedDate2);
		dao.persist(userDSMessagePrefs2);
		
		UserDSMessagePrefs userDSMessagePrefs3 = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(userDSMessagePrefs3);
		userDSMessagePrefs3.setResourceId(resourceId1);
		userDSMessagePrefs3.setProviderNo(providerNo1);
		userDSMessagePrefs3.setResourceType(resourceType1);
		userDSMessagePrefs3.setArchived(!isArchived);
		userDSMessagePrefs3.setResourceUpdatedDate(resourceUpdatedDate3);
		dao.persist(userDSMessagePrefs3);
		
		long longDate = resourceUpdatedDate1.getTime();
		
		Hashtable<String,Long> expectedResult = new Hashtable<String,Long>();
		expectedResult.put(resourceType1+resourceId1, longDate);
		Hashtable<String,Long> result = dao.getHashofMessages(providerNo1, resourceType1);
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testGetDsMessage() throws Exception {

		String providerNo1 = "101";
		String providerNo2 = "202";
		
		String resourceType1 = "alpha";
		String resourceType2 = "bravo";
		
		boolean isArchived = true;
		
		String resourceId1 = "111";
		String resourceId2 = "222";
		
		UserDSMessagePrefs userDSMessagePrefs1 = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(userDSMessagePrefs1);
		userDSMessagePrefs1.setProviderNo(providerNo1);
		userDSMessagePrefs1.setResourceType(resourceType1);
		userDSMessagePrefs1.setArchived(isArchived);
		userDSMessagePrefs1.setResourceId(resourceId1);
		dao.persist(userDSMessagePrefs1);
		
		UserDSMessagePrefs userDSMessagePrefs2 = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(userDSMessagePrefs2);
		userDSMessagePrefs2.setProviderNo(providerNo2);
		userDSMessagePrefs2.setResourceType(resourceType2);
		userDSMessagePrefs2.setArchived(isArchived);
		userDSMessagePrefs2.setResourceId(resourceId2);
		dao.persist(userDSMessagePrefs2);
		
		UserDSMessagePrefs userDSMessagePrefs3 = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(userDSMessagePrefs3);
		userDSMessagePrefs3.setProviderNo(providerNo1);
		userDSMessagePrefs3.setResourceType(resourceType1);
		userDSMessagePrefs3.setArchived(!isArchived);
		userDSMessagePrefs3.setResourceId(resourceId1);
		dao.persist(userDSMessagePrefs3);
		
		UserDSMessagePrefs userDSMessagePrefs4 = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(userDSMessagePrefs4);
		userDSMessagePrefs4.setProviderNo(providerNo2);
		userDSMessagePrefs4.setResourceType(resourceType1);
		userDSMessagePrefs4.setArchived(isArchived);
		userDSMessagePrefs4.setResourceId(resourceId1);
		dao.persist(userDSMessagePrefs4);
		
		UserDSMessagePrefs userDSMessagePrefs5 = new UserDSMessagePrefs();
		EntityDataGenerator.generateTestDataForModelClass(userDSMessagePrefs5);
		userDSMessagePrefs5.setProviderNo(providerNo1);
		userDSMessagePrefs5.setResourceType(resourceType1);
		userDSMessagePrefs5.setArchived(isArchived);
		userDSMessagePrefs5.setResourceId(resourceId2);
		dao.persist(userDSMessagePrefs5);
		
		UserDSMessagePrefs expectedResult = userDSMessagePrefs1;
		UserDSMessagePrefs result = dao.getDsMessage(providerNo1, resourceType1, resourceId1, isArchived);
		
		assertEquals(expectedResult, result);
	}
}