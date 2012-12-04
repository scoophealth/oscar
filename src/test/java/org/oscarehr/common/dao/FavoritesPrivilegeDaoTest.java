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
import org.oscarehr.common.model.FavoritesPrivilege;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class FavoritesPrivilegeDaoTest extends DaoTestFixtures {

	protected FavoritesPrivilegeDao dao = SpringUtils.getBean(FavoritesPrivilegeDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("favoritesprivilege");
	}

       @Test
        public void testCreate() throws Exception {
                FavoritesPrivilege entity = new FavoritesPrivilege();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);
                assertNotNull(entity.getId());
        }

	@Test
	public void testGetProviders() throws Exception {
		
		boolean isOpenToPublic = true; 
		
		String provideNo1 = "111";
		String providerNo2 = "222";
		String providerNo3 = "333";
		
		FavoritesPrivilege favoritesPrivilege1 = new FavoritesPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(favoritesPrivilege1);
		favoritesPrivilege1.setOpenToPublic(isOpenToPublic);
		favoritesPrivilege1.setProviderNo(provideNo1);
		dao.persist(favoritesPrivilege1);
		
		FavoritesPrivilege favoritesPrivilege2 = new FavoritesPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(favoritesPrivilege2);
		favoritesPrivilege2.setOpenToPublic(!isOpenToPublic);
		favoritesPrivilege2.setProviderNo(providerNo2);
		dao.persist(favoritesPrivilege2);
		
		FavoritesPrivilege favoritesPrivilege3 = new FavoritesPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(favoritesPrivilege3);
		favoritesPrivilege3.setOpenToPublic(isOpenToPublic);
		favoritesPrivilege3.setProviderNo(providerNo3);
		dao.persist(favoritesPrivilege3);
		
		List<String> expectedResult = new ArrayList<String>(Arrays.asList(provideNo1, providerNo3));
		List<String> result = dao.getProviders();

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
		
		String provideNo1 = "111";
		String providerNo2 = "222";
		String providerNo3 = "333";
		
		FavoritesPrivilege favoritesPrivilege1 = new FavoritesPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(favoritesPrivilege1);
		favoritesPrivilege1.setProviderNo(provideNo1);
		dao.persist(favoritesPrivilege1);
		
		FavoritesPrivilege favoritesPrivilege2 = new FavoritesPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(favoritesPrivilege2);
		favoritesPrivilege2.setProviderNo(providerNo2);
		dao.persist(favoritesPrivilege2);
		
		FavoritesPrivilege favoritesPrivilege3 = new FavoritesPrivilege();
		EntityDataGenerator.generateTestDataForModelClass(favoritesPrivilege3);
		favoritesPrivilege3.setProviderNo(providerNo3);
		dao.persist(favoritesPrivilege3);
		
		FavoritesPrivilege expectedResult = favoritesPrivilege2;
		FavoritesPrivilege result = dao.findByProviderNo(providerNo2);
		
		assertEquals(expectedResult, result);
	}
}
