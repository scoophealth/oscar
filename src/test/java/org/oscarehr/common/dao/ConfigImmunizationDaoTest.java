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
import org.oscarehr.common.model.ConfigImmunization;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ConfigImmunizationDaoTest extends DaoTestFixtures {

	protected ConfigImmunizationDao dao = (ConfigImmunizationDao)SpringUtils.getBean(ConfigImmunizationDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "config_Immunization");
	}

	@Test
	public void testCreate() throws Exception {
		ConfigImmunization entity = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindAll() throws Exception {
		
		int archived1 = 0;
		int archived2 = 1;
		
		String name1 = "delta";
		String name2 = "alpha";
		String name3 = "bravo";
		String name4 = "charlie";
		
		ConfigImmunization configImmunization1 = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(configImmunization1);
		configImmunization1.setArchived(archived1);
		configImmunization1.setName(name1);
		dao.persist(configImmunization1);
		
		ConfigImmunization configImmunization2 = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(configImmunization2);
		configImmunization2.setArchived(archived1);
		configImmunization2.setName(name2);
		dao.persist(configImmunization2);
		
		ConfigImmunization configImmunization3 = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(configImmunization3);
		configImmunization3.setArchived(archived2);
		configImmunization3.setName(name3);
		dao.persist(configImmunization3);
		
		ConfigImmunization configImmunization4 = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(configImmunization4);
		configImmunization4.setArchived(archived1);
		configImmunization4.setName(name4);
		dao.persist(configImmunization4);
		
		List<ConfigImmunization> expectedResult = new ArrayList<ConfigImmunization>(Arrays.asList(configImmunization2, configImmunization4, configImmunization1));
		List<ConfigImmunization> result = dao.findAll();

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
	public void testFindByArchived() throws Exception {
		
		int archived1 = 0;
		int archived2 = 1;
		
		String name1 = "delta";
		String name2 = "alpha";
		String name3 = "bravo";
		String name4 = "charlie";
		
		ConfigImmunization configImmunization1 = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(configImmunization1);
		configImmunization1.setArchived(archived1);
		configImmunization1.setName(name1);
		dao.persist(configImmunization1);
		
		ConfigImmunization configImmunization2 = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(configImmunization2);
		configImmunization2.setArchived(archived1);
		configImmunization2.setName(name2);
		dao.persist(configImmunization2);
		
		ConfigImmunization configImmunization3 = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(configImmunization3);
		configImmunization3.setArchived(archived2);
		configImmunization3.setName(name3);
		dao.persist(configImmunization3);
		
		ConfigImmunization configImmunization4 = new ConfigImmunization();
		EntityDataGenerator.generateTestDataForModelClass(configImmunization4);
		configImmunization4.setArchived(archived1);
		configImmunization4.setName(name4);
		dao.persist(configImmunization4);
		
		List<ConfigImmunization> expectedResult = new ArrayList<ConfigImmunization>(Arrays.asList(configImmunization2, configImmunization4, configImmunization1));
		List<ConfigImmunization> result = dao.findByArchived(archived1, true);

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