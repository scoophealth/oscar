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
/**
 * @author Shazib
 */
package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class UserPropertyDAOTest extends DaoTestFixtures{
	
	protected UserPropertyDAO dao = (UserPropertyDAO)SpringUtils.getBean(UserPropertyDAO.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("property");
	}

	@Test
	public void testGetPropStringString() throws Exception {
		
		String name1 = "alpha";
		String name2 = "bravo";
		String providerNo1 = "100";
		String providerNo2 = "200";
		
		UserProperty userProperty1 = new UserProperty();
		EntityDataGenerator.generateTestDataForModelClass(userProperty1);
		userProperty1.setName(name1);
		userProperty1.setProviderNo(providerNo1);
		dao.persist(userProperty1);
		
		UserProperty userProperty2 = new UserProperty();
		EntityDataGenerator.generateTestDataForModelClass(userProperty2);
		userProperty2.setProviderNo(name2);
		userProperty2.setProviderNo(providerNo2);
		dao.persist(userProperty2);
		
		UserProperty expectedResult = userProperty1;
		UserProperty result = dao.getProp(providerNo1, name1);
		
		assertEquals(expectedResult, result);
	}

	@Test 
	public void testGetPropString() throws Exception {
		
		String name1 = "alpha";
		String name2 = "bravo";
		
		UserProperty userProperty1 = new UserProperty();
		EntityDataGenerator.generateTestDataForModelClass(userProperty1);
		userProperty1.setName(name1);
		dao.persist(userProperty1);
		
		UserProperty userProperty2 = new UserProperty();
		EntityDataGenerator.generateTestDataForModelClass(userProperty2);
		userProperty2.setProviderNo(name2);
		dao.persist(userProperty2);
		
		UserProperty expectedResult = userProperty1;
		UserProperty result = dao.getProp(name1);
		
		assertEquals(expectedResult, result);
	}

	@Test
	public void testGetDemographicProperties() throws Exception {

		String providerNo1 = "100";
		String providerNo2 = "200";
		
		UserProperty userProperty1 = new UserProperty();
		EntityDataGenerator.generateTestDataForModelClass(userProperty1);
		userProperty1.setProviderNo(providerNo1);
		dao.persist(userProperty1);
		
		UserProperty userProperty2 = new UserProperty();
		EntityDataGenerator.generateTestDataForModelClass(userProperty2);
		userProperty2.setProviderNo(providerNo2);
		dao.persist(userProperty2);
		
		UserProperty userProperty3 = new UserProperty();
		EntityDataGenerator.generateTestDataForModelClass(userProperty3);
		userProperty3.setProviderNo(providerNo1);
		dao.persist(userProperty3);
		
		List<UserProperty> expectedResult = new ArrayList<UserProperty>(Arrays.asList(userProperty1, userProperty3));
		List<UserProperty> result = dao.getDemographicProperties(providerNo1);
		
		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items do not match.");
				fail("Items do not match.");
			}
		}
		assertTrue(true);
		
	}

	@Test
	public void testGetProviderPropertiesAsMap() throws Exception {
		
		String providerNo1 = "100";
		String providerNo2 = "200";
		String name1 = "alpha";
		String name2 = "bravo";
		String value1 = "Value1";
		String value2 = "Value2";
		
		UserProperty userProperty1 = new UserProperty();
		EntityDataGenerator.generateTestDataForModelClass(userProperty1);
		userProperty1.setProviderNo(providerNo1);
		userProperty1.setName(name1);
		userProperty1.setValue(value1);
		dao.persist(userProperty1);
		
		UserProperty userProperty2 = new UserProperty();
		EntityDataGenerator.generateTestDataForModelClass(userProperty2);
		userProperty2.setProviderNo(providerNo2);
		userProperty1.setName(name2);
		userProperty1.setValue(value2);
		dao.persist(userProperty2);
		
		Map<String,String> expectedResult = new HashMap<String,String>();
		Map<String,String> result = dao.getProviderPropertiesAsMap(providerNo1);
		
		expectedResult.put(name1, value1);
		
		assertEquals(expectedResult, result);	
	}
}