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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Property;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class PropertyDaoTest extends DaoTestFixtures {

	protected PropertyDao dao = SpringUtils.getBean(PropertyDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("property");
	}

	@Test
	public void testFindByName() throws Exception {
		
		String name1 = "alpha";
		String name2 = "bravo";
		
		Property property1 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property1);
		property1.setName(name1);
		dao.persist(property1);
		
		Property property2 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property2);
		property2.setName(name2);
		dao.persist(property2);
		
		Property property3 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property3);
		property3.setName(name1);
		dao.persist(property3);
		
		List<Property> expectedResult = new ArrayList<Property>(Arrays.asList(property1, property3));
		List<Property> result = dao.findByName(name1);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}
	
	@Test
	public void testFindByNameAndProvider() throws Exception {
		
		String name1 = "alpha";
		String name2 = "bravo";
		
		String providerNo1 = "101";
		String providerNo2 = "202";
		
		Property property1 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property1);
		property1.setName(name1);
		property1.setProviderNo(providerNo1);
		dao.persist(property1);
		
		Property property2 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property2);
		property2.setName(name2);
		property2.setProviderNo(providerNo2);
		dao.persist(property2);
		
		Property property3 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property3);
		property3.setName(name1);
		property3.setProviderNo(providerNo1);
		dao.persist(property3);
		
		Property property4 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property4);
		property4.setName(name2);
		property4.setProviderNo(providerNo1);
		dao.persist(property4);
		
		List<Property> expectedResult = new ArrayList<Property>(Arrays.asList(property1, property3));
		List<Property> result = dao.findByNameAndProvider(name1, providerNo1);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}
	
	@Test
	public void testCheckByName() throws Exception {
		
		String name1 = "alpha";
		String name2 = "bravo";
		String name3 = "charlie";
		
		Property property1 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property1);
		property1.setName(name1);
		dao.persist(property1);
		
		Property property2 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property2);
		property2.setName(name2);
		dao.persist(property2);
		
		Property property3 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property3);
		property3.setName(name3);
		dao.persist(property3);
		
		Property expectedResult = property2;
		Property result = dao.checkByName(name2);
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testFindByNameAndValue() throws Exception {

		String name1 = "alpha";
		String name2 = "bravo";
		
		String value1 = "111";
		String value2 = "222";
		
		Property property1 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property1);
		property1.setName(name1);
		property1.setValue(value1);
		dao.persist(property1);
		
		Property property2 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property2);
		property2.setName(name2);
		property2.setValue(value1);
		dao.persist(property2);
		
		Property property3 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property3);
		property3.setName(name1);
		property3.setValue(value1);
		dao.persist(property3);
		
		Property property4 = new Property();
		EntityDataGenerator.generateTestDataForModelClass(property4);
		property4.setName(name1);
		property4.setValue(value2);
		dao.persist(property4);
		
		List<Property> expectedResult = new ArrayList<Property>(Arrays.asList(property1, property3));
		List<Property> result = dao.findByNameAndValue(name1, value1);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);	
		
	}

}
