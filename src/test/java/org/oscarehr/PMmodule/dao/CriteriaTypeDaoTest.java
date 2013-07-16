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
import org.oscarehr.PMmodule.model.CriteriaType;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CriteriaTypeDaoTest extends DaoTestFixtures {

	public CriteriaTypeDao dao = SpringUtils.getBean(CriteriaTypeDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "criteria_type");
	}

	@Test
	public void testCreate() throws Exception {
		CriteriaType entity = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindAll() throws Exception {
		
		CriteriaType cT1 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT1);
		dao.persist(cT1);
		
		CriteriaType cT2 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT2);
		dao.persist(cT2);
		
		CriteriaType cT3 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT3);
		dao.persist(cT3);
		
		CriteriaType cT4 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT4);
		dao.persist(cT4);
		
		List<CriteriaType> expectedResult = new ArrayList<CriteriaType>(Arrays.asList(cT1, cT2, cT3, cT4));
		List<CriteriaType> result = dao.findAll();

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
	public void testFindByName() throws Exception {
		
		String fieldName1 = "alpha", fieldName2 = "bravo", fieldName3 = "charlie";
		
		CriteriaType cT1 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT1);
		cT1.setFieldName(fieldName1);
		dao.persist(cT1);
		
		CriteriaType cT2 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT2);
		cT2.setFieldName(fieldName2);
		dao.persist(cT2);
		
		CriteriaType cT3 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT3);
		cT3.setFieldName(fieldName3);
		dao.persist(cT3);
		
		CriteriaType expectedResult = cT2;
		CriteriaType result = dao.findByName(fieldName2);
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testGetAllCriteriaTypes() throws Exception {
		
		String fieldName1 = "alpha", fieldName2 = "bravo", fieldName3 = "charlie", fieldName4 = "delta", fieldName5 = "sigma";
		int wlProgramId1 = 1, wlProgramId2 = 222;
		
		CriteriaType cT1 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT1);
		cT1.setFieldType(fieldName2);
		cT1.setWlProgramId(wlProgramId1);
		dao.persist(cT1);
		
		CriteriaType cT2 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT2);
		cT2.setFieldType(fieldName4);
		cT2.setWlProgramId(wlProgramId2);
		dao.persist(cT2);
		
		CriteriaType cT3 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT3);
		cT3.setFieldType(fieldName5);
		cT3.setWlProgramId(wlProgramId1);
		dao.persist(cT3);
		
		CriteriaType cT4 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT4);
		cT4.setFieldType(fieldName1);
		cT4.setWlProgramId(wlProgramId1);
		dao.persist(cT4);
		
		CriteriaType cT5 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT5);
		cT5.setFieldType(fieldName3);
		cT5.setWlProgramId(wlProgramId1);
		dao.persist(cT5);
		
		List<CriteriaType> expectedResult = new ArrayList<CriteriaType>(Arrays.asList(cT3, cT5, cT1, cT4));
		List<CriteriaType> result = dao.getAllCriteriaTypes();

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
	public void testGetAllCriteriaTypesByWlProgramId() throws Exception {
		
		String fieldName1 = "alpha", fieldName2 = "bravo", fieldName3 = "charlie", fieldName4 = "delta", fieldName5 = "sigma";
		int wlProgramId1 = 111, wlProgramId2 = 222;
		
		CriteriaType cT1 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT1);
		cT1.setFieldType(fieldName2);
		cT1.setWlProgramId(wlProgramId1);
		dao.persist(cT1);
		
		CriteriaType cT2 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT2);
		cT2.setFieldType(fieldName4);
		cT2.setWlProgramId(wlProgramId2);
		dao.persist(cT2);
		
		CriteriaType cT3 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT3);
		cT3.setFieldType(fieldName5);
		cT3.setWlProgramId(wlProgramId1);
		dao.persist(cT3);
		
		CriteriaType cT4 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT4);
		cT4.setFieldType(fieldName1);
		cT4.setWlProgramId(wlProgramId1);
		dao.persist(cT4);
		
		CriteriaType cT5 = new CriteriaType();
		EntityDataGenerator.generateTestDataForModelClass(cT5);
		cT5.setFieldName(fieldName3);
		cT5.setWlProgramId(wlProgramId1);
		dao.persist(cT5);
		
		List<CriteriaType> expectedResult = new ArrayList<CriteriaType>(Arrays.asList(cT3, cT5, cT1, cT4));
		List<CriteriaType> result = dao.getAllCriteriaTypesByWlProgramId(wlProgramId1);

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