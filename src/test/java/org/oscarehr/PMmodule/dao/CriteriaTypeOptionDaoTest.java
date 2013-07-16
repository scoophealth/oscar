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
package org.oscarehr.PMmodule.dao;

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
import org.oscarehr.PMmodule.model.CriteriaTypeOption;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CriteriaTypeOptionDaoTest extends DaoTestFixtures {

	public CriteriaTypeOptionDao dao = SpringUtils.getBean(CriteriaTypeOptionDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "criteria_type_option");
	}

	@Test
	public void testCreate() throws Exception {
		CriteriaTypeOption entity = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindAll() throws Exception {
		
		CriteriaTypeOption cTO1 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO1);
		dao.persist(cTO1);
		
		CriteriaTypeOption cTO2 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO2);
		dao.persist(cTO2);
		
		CriteriaTypeOption cTO3 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO3);
		dao.persist(cTO3);
		
		CriteriaTypeOption cTO4 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO4);
		dao.persist(cTO4);
		
		List<CriteriaTypeOption> expectedResult = new ArrayList<CriteriaTypeOption>(Arrays.asList(cTO1, cTO2, cTO3, cTO4));
		List<CriteriaTypeOption> result = dao.findAll();
		
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
	public void testGetCriteriaTypeOptionByTypeId() throws Exception {
		
		int criteriaTypeId1 = 101, criteriaTypeId2 = 202;
		
		CriteriaTypeOption cTO1 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO1);
		cTO1.setCriteriaTypeId(criteriaTypeId1);
		dao.persist(cTO1);
		
		CriteriaTypeOption cTO2 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO2);
		cTO2.setCriteriaTypeId(criteriaTypeId2);
		dao.persist(cTO2);
		
		CriteriaTypeOption cTO3 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO3);
		cTO3.setCriteriaTypeId(criteriaTypeId2);
		dao.persist(cTO3);
		
		CriteriaTypeOption cTO4 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO4);
		cTO4.setCriteriaTypeId(criteriaTypeId1);
		dao.persist(cTO4);
		
		CriteriaTypeOption cTO5 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO5);
		cTO5.setCriteriaTypeId(criteriaTypeId1);
		dao.persist(cTO5);
		
		List<CriteriaTypeOption> expectedResult = new ArrayList<CriteriaTypeOption>(Arrays.asList(cTO1, cTO4, cTO5));
		List<CriteriaTypeOption> result = dao.getCriteriaTypeOptionByTypeId(criteriaTypeId1);
		
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
	public void testGetByValue() throws Exception {
		
		String optionValue1 = "alpha", optionValue2 = "bravo", optionValue3 = "charlie";
		
		CriteriaTypeOption cTO1 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO1);
		cTO1.setOptionValue(optionValue1);
		dao.persist(cTO1);
		
		CriteriaTypeOption cTO2 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO2);
		cTO2.setOptionValue(optionValue2);
		dao.persist(cTO2);
		
		CriteriaTypeOption cTO3 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO3);
		cTO3.setOptionValue(optionValue3);
		dao.persist(cTO3);
		
		CriteriaTypeOption expectedResult = cTO2;
		CriteriaTypeOption result = dao.getByValue(optionValue2);
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testGetByValueAndTypeId() throws Exception {
		
		int criteriaTypeId1 = 101, criteriaTypeId2 = 202;
		String optionValue1 = "alpha", optionValue2 = "bravo";
		
		CriteriaTypeOption cTO1 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO1);
		cTO1.setCriteriaTypeId(criteriaTypeId1);
		cTO1.setOptionValue(optionValue1);
		dao.persist(cTO1);
		
		CriteriaTypeOption cTO2 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO2);
		cTO2.setCriteriaTypeId(criteriaTypeId1);
		cTO2.setOptionValue(optionValue2);
		dao.persist(cTO2);
		
		CriteriaTypeOption cTO3 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO3);
		cTO3.setCriteriaTypeId(criteriaTypeId2);
		cTO3.setOptionValue(optionValue1);
		dao.persist(cTO3);
		
		CriteriaTypeOption cTO4 = new CriteriaTypeOption();
		EntityDataGenerator.generateTestDataForModelClass(cTO4);
		cTO4.setCriteriaTypeId(criteriaTypeId2);
		cTO4.setOptionValue(optionValue2);
		dao.persist(cTO4);
		
		CriteriaTypeOption expectedResult = cTO4;
		CriteriaTypeOption result = dao.getByValueAndTypeId(optionValue2, criteriaTypeId2);
		
		assertEquals(expectedResult, result);		
	}
}