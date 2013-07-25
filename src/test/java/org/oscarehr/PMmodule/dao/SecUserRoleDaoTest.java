/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */
package org.oscarehr.PMmodule.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class SecUserRoleDaoTest extends DaoTestFixtures {

	protected SecUserRoleDao dao = SpringUtils.getBean(SecUserRoleDao.class);
	
	@Before
	public void before() throws Exception {
		
		SchemaUtils.restoreTable("secUserRole");
	}
	
	@Test
	public void testGetUserRoles() throws Exception {

		String providerNo1 = "111", providerNo2 = "222";
		
		SecUserRole sUR1 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR1);
		sUR1.setProviderNo(providerNo1);
		dao.save(sUR1);
		
		SecUserRole sUR2 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR2);
		sUR2.setProviderNo(providerNo2);
		dao.save(sUR2);
		
		SecUserRole sUR3 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR3);
		sUR3.setProviderNo(providerNo1);
		dao.save(sUR3);
		
		SecUserRole sUR4 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR4);
		sUR4.setProviderNo(providerNo1);
		dao.save(sUR4);
		
		List<SecUserRole> expectedResult = new ArrayList<SecUserRole>(Arrays.asList(sUR1, sUR3, sUR4));
		List<SecUserRole> result = dao.getUserRoles(providerNo1);

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
	public void testGetSecUserRolesByRoleName() throws Exception {
		
		String roleName1 = "alpha", roleName2 = "bravo";
		String providerNo1 = "111", providerNo2 = "222";
		
		SecUserRole sUR1 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR1);
		sUR1.setRoleName(roleName1);
		sUR1.setProviderNo(providerNo1);
		dao.save(sUR1);
		
		SecUserRole sUR2 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR2);
		sUR2.setRoleName(roleName2);
		sUR2.setProviderNo(providerNo2);
		dao.save(sUR2);
		
		SecUserRole sUR3 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR3);
		sUR3.setRoleName(roleName1);
		sUR3.setProviderNo(providerNo1);
		dao.save(sUR3);
		
		SecUserRole sUR4 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR4);
		sUR4.setRoleName(roleName1);
		sUR4.setProviderNo(providerNo1);
		dao.save(sUR4);
		
		List<SecUserRole> expectedResult = new ArrayList<SecUserRole>(Arrays.asList(sUR1, sUR3, sUR4));
		List<SecUserRole> result = dao.getSecUserRolesByRoleName(roleName1);

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
	public void testFindByRoleNameAndProviderNo() throws Exception {
		
		String roleName1 = "alpha", roleName2 = "bravo";
		String providerNo1 = "111", providerNo2 = "222";
		
		SecUserRole sUR1 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR1);
		sUR1.setRoleName(roleName1);
		sUR1.setProviderNo(providerNo1);
		dao.save(sUR1);
		
		SecUserRole sUR2 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR2);
		sUR2.setRoleName(roleName2);
		sUR2.setProviderNo(providerNo2);
		dao.save(sUR2);
		
		SecUserRole sUR3 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR3);
		sUR3.setRoleName(roleName1);
		sUR3.setProviderNo(providerNo1);
		dao.save(sUR3);
		
		SecUserRole sUR4 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR4);
		sUR4.setRoleName(roleName1);
		sUR4.setProviderNo(providerNo1);
		dao.save(sUR4);
		
		List<SecUserRole> expectedResult = new ArrayList<SecUserRole>(Arrays.asList(sUR1, sUR3, sUR4));
		List<SecUserRole> result = dao.findByRoleNameAndProviderNo(roleName1, providerNo1);

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
	public void test() throws Exception {
		
		String roleName1 = "alpha", roleName2 = "admin";
		String providerNo1 = "111", providerNo2 = "222";
		
		SecUserRole sUR1 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR1);
		sUR1.setRoleName(roleName1);
		sUR1.setProviderNo(providerNo1);
		dao.save(sUR1);
		
		SecUserRole sUR2 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR2);
		sUR2.setRoleName(roleName2);
		sUR2.setProviderNo(providerNo2);
		dao.save(sUR2);
		
		SecUserRole sUR3 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR3);
		sUR3.setRoleName(roleName1);
		sUR3.setProviderNo(providerNo1);
		dao.save(sUR3);
		
		SecUserRole sUR4 = new SecUserRole();
		EntityDataGenerator.generateTestDataForModelClass(sUR4);
		sUR4.setRoleName(roleName2);
		sUR4.setProviderNo(providerNo1);
		dao.save(sUR4);
		
		boolean expectedResult = true;
		boolean result = dao.hasAdminRole(providerNo1);
		
		assertEquals(expectedResult, result);
	}
}