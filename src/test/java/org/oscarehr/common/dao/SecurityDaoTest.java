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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class SecurityDaoTest extends DaoTestFixtures {
	
	protected SecurityDao dao = SpringUtils.getBean(SecurityDao.class);
	
	@Override
	@Test
	public void doSimpleExceptionTest() {
		MiscUtils.getLogger().error("Unable to run doSimpleExceptionTest on this DAO");
	}
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "security");
	}

	@Test 
	public void testFindAllOrderBy() throws Exception {
		
		String userName1 = "bravo";
		String userName2 = "alpha";
		String userName3 = "delta";
		String userName4 = "charlie";
		
		String columnName = "userName";
		
		Security security1 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security1);
		security1.setUserName(userName1);
		dao.persist(security1);
		
		Security security2 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security2);
		security2.setUserName(userName2);
		dao.persist(security2);
		
		Security security3 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security3);
		security3.setUserName(userName3);
		dao.persist(security3);
		
		Security security4 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security4);
		security4.setUserName(userName4);
		dao.persist(security4);
		
		List<Security> expectedResult = new ArrayList<Security>(Arrays.asList(security2, security1, security4, security3));
		List<Security> result = dao.findAllOrderBy(columnName);

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
		String providerNo3 = "303";
		
		Security security1 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security1);
		security1.setProviderNo(providerNo1);
		dao.persist(security1);
		
		Security security2 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security2);
		security2.setProviderNo(providerNo2);
		dao.persist(security2);
		
		Security security3 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security3);
		security3.setProviderNo(providerNo3);
		dao.persist(security3);
		
		Security security4 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security4);
		security4.setProviderNo(providerNo1);
		dao.persist(security4);
		
		List<Security> expectedResult = new ArrayList<Security>(Arrays.asList(security1, security4));
		List<Security> result = dao.findByProviderNo(providerNo1);

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
	public void testFindByLikeProviderNo() throws Exception {
		
		String providerNo1 = "alpha1";
		String providerNo2 = "bravo1";
		String providerNo3 = "alpha2";
		String providerNo4 = "alpha4";
		
		String likeProvider = "alp%";
		
		Security security1 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security1);
		security1.setProviderNo(providerNo1);
		dao.persist(security1);
		
		Security security2 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security2);
		security2.setProviderNo(providerNo2);
		dao.persist(security2);
		
		Security security3 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security3);
		security3.setProviderNo(providerNo3);
		dao.persist(security3);
		
		Security security4 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security4);
		security4.setProviderNo(providerNo4);
		dao.persist(security4);
		
		List<Security> expectedResult = new ArrayList<Security>(Arrays.asList(security1, security3, security4));
		List<Security> result = dao.findByLikeProviderNo(likeProvider);

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
	public void testFindByUserName() throws Exception {
		
		String userName1 = "alpha";
		String userName2 = "bravo";
		String userName3 = "charlie";
		
		Security security1 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security1);
		security1.setUserName(userName1);
		dao.persist(security1);
		
		Security security2 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security2);
		security2.setUserName(userName2);
		dao.persist(security2);
		
		Security security3 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security3);
		security3.setUserName(userName3);
		dao.persist(security3);
		
		List<Security> expectedResult = new ArrayList<Security>(Arrays.asList(security1));
		List<Security> result = dao.findByUserName(userName1);

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
	public void testFindByLikeUserName() throws Exception {
		
		String userName1 = "alpha1";
		String userName2 = "bravo";
		String userName3 = "alpha3";
		String userName4 = "bravo3";
		
		String likeUserName = "brav%";
		
		Security security1 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security1);
		security1.setUserName(userName1);
		dao.persist(security1);
		
		Security security2 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security2);
		security2.setUserName(userName2);
		dao.persist(security2);
		
		Security security3 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security3);
		security3.setUserName(userName3);
		dao.persist(security3);
		
		Security security4 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security4);
		security4.setUserName(userName4);
		dao.persist(security4);
		
		List<Security> expectedResult = new ArrayList<Security>(Arrays.asList(security2, security4));
		List<Security> result = dao.findByLikeUserName(likeUserName);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.Result: " +result.size());
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
	public void testGetByProviderNo() throws Exception {
		
		String providerNo1 = "101";
		String providerNo2 = "202";
		String providerNo3 = "303";
		String providerNo4 = "404";
		
		Security security1 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security1);
		security1.setProviderNo(providerNo1);
		dao.persist(security1);
		
		Security security2 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security2);
		security2.setProviderNo(providerNo2);
		dao.persist(security2);
		
		Security security3 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security3);
		security3.setProviderNo(providerNo3);
		dao.persist(security3);
		
		Security security4 = new Security();
		EntityDataGenerator.generateTestDataForModelClass(security4);
		security4.setProviderNo(providerNo4);
		dao.persist(security4);
		
		Security expectedResult = security3;
		Security result = dao.getByProviderNo(providerNo3);
		
		assertEquals(expectedResult, result);
	}
}