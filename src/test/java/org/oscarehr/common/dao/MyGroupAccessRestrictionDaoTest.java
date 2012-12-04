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
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.MyGroupAccessRestriction;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.MiscUtils;


public class MyGroupAccessRestrictionDaoTest extends DaoTestFixtures {

	protected MyGroupAccessRestrictionDao dao = SpringUtils.getBean(MyGroupAccessRestrictionDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("MyGroupAccessRestriction");
	}

	@Test
	public void testCreate() throws Exception {
		MyGroupAccessRestriction entity = new MyGroupAccessRestriction();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	public void testFindByGroupId() throws Exception{
		
		String myGroupNo1 = "111";
		String myGroupNo2 = "222";
		
		MyGroupAccessRestriction myGrpAccessRestriction1 = new MyGroupAccessRestriction();
		EntityDataGenerator.generateTestDataForModelClass(myGrpAccessRestriction1);
		myGrpAccessRestriction1.setMyGroupNo(myGroupNo1);
		dao.persist(myGrpAccessRestriction1);
		
		MyGroupAccessRestriction myGrpAccessRestriction2 = new MyGroupAccessRestriction();
		EntityDataGenerator.generateTestDataForModelClass(myGrpAccessRestriction2);
		myGrpAccessRestriction2.setMyGroupNo(myGroupNo2);
		dao.persist(myGrpAccessRestriction2);
		
		MyGroupAccessRestriction myGrpAccessRestriction3 = new MyGroupAccessRestriction();
		EntityDataGenerator.generateTestDataForModelClass(myGrpAccessRestriction3);
		myGrpAccessRestriction3.setMyGroupNo(myGroupNo1);
		dao.persist(myGrpAccessRestriction3);
		
		List<MyGroupAccessRestriction> expectedResult = new ArrayList<MyGroupAccessRestriction>(Arrays.asList(myGrpAccessRestriction1, myGrpAccessRestriction3));
		List<MyGroupAccessRestriction> result = dao.findByGroupId(myGroupNo1);

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
	public void testFindByProviderNo() throws Exception {
		
		String providerNo1 = "101";
		String providerNo2 = "202";
		
		MyGroupAccessRestriction myGrpAccessRestriction1 = new MyGroupAccessRestriction();
		EntityDataGenerator.generateTestDataForModelClass(myGrpAccessRestriction1);
		myGrpAccessRestriction1.setProviderNo(providerNo1);
		dao.persist(myGrpAccessRestriction1);
		
		MyGroupAccessRestriction myGrpAccessRestriction2 = new MyGroupAccessRestriction();
		EntityDataGenerator.generateTestDataForModelClass(myGrpAccessRestriction2);
		myGrpAccessRestriction2.setProviderNo(providerNo2);
		dao.persist(myGrpAccessRestriction2);
		
		MyGroupAccessRestriction myGrpAccessRestriction3 = new MyGroupAccessRestriction();
		EntityDataGenerator.generateTestDataForModelClass(myGrpAccessRestriction3);
		myGrpAccessRestriction3.setProviderNo(providerNo1);
		dao.persist(myGrpAccessRestriction3);
		
		List<MyGroupAccessRestriction> expectedResult = new ArrayList<MyGroupAccessRestriction>(Arrays.asList(myGrpAccessRestriction1, myGrpAccessRestriction3));
		List<MyGroupAccessRestriction> result = dao.findByProviderNo(providerNo1);

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
	public void testFindByGroupNoAndProvider() throws Exception {
				
		String myGroupNo1 = "111";
		String myGroupNo2 = "222";
		
		String providerNo1 = "101";
		String providerNo2 = "202";
		
		MyGroupAccessRestriction myGrpAccessRestriction1 = new MyGroupAccessRestriction();
		EntityDataGenerator.generateTestDataForModelClass(myGrpAccessRestriction1);
		myGrpAccessRestriction1.setProviderNo(providerNo1);
		myGrpAccessRestriction1.setMyGroupNo(myGroupNo1);
		dao.persist(myGrpAccessRestriction1);
		
		MyGroupAccessRestriction myGrpAccessRestriction2 = new MyGroupAccessRestriction();
		EntityDataGenerator.generateTestDataForModelClass(myGrpAccessRestriction2);
		myGrpAccessRestriction2.setProviderNo(providerNo2);
		myGrpAccessRestriction1.setMyGroupNo(myGroupNo2);
		dao.persist(myGrpAccessRestriction2);
		
		MyGroupAccessRestriction expectedResult = myGrpAccessRestriction1;
		MyGroupAccessRestriction result = dao.findByGroupNoAndProvider(myGroupNo1, providerNo1);

		assertEquals(expectedResult, result);
	}
}
