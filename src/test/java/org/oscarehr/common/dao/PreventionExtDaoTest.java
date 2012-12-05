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

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class PreventionExtDaoTest extends DaoTestFixtures {
	
	protected PreventionExtDao dao = (PreventionExtDao)SpringUtils.getBean(PreventionExtDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("preventionsExt");
	}

	@Test
	public void testFindByPreventionId() throws Exception {
		
		int preventionId1 = 100;
		int preventionId2 = 200;
		
		PreventionExt prevenExt1 = new PreventionExt();
		EntityDataGenerator.generateTestDataForModelClass(prevenExt1);
		prevenExt1.setPreventionId(preventionId1);
		dao.persist(prevenExt1);
		
		PreventionExt prevenExt2 = new PreventionExt();
		EntityDataGenerator.generateTestDataForModelClass(prevenExt2);
		prevenExt2.setPreventionId(preventionId2);
		dao.persist(prevenExt2);
		
		PreventionExt prevenExt3 = new PreventionExt();
		EntityDataGenerator.generateTestDataForModelClass(prevenExt3);
		prevenExt3.setPreventionId(preventionId1);
		dao.persist(prevenExt3);
		
		List<PreventionExt> expectedResult = new ArrayList<PreventionExt>(Arrays.asList(prevenExt1, prevenExt3));
		List<PreventionExt> result = dao.findByPreventionId(preventionId1);

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
	public void testFindByKeyAndValue() throws Exception {
		
		String val1 = "100";
		String val2 = "200";
		
		String keyVal1 = "alpha";
		String keyVal2 = "bravo";
		
		PreventionExt prevenExt1 = new PreventionExt();
		EntityDataGenerator.generateTestDataForModelClass(prevenExt1);
		prevenExt1.setVal(val1);
		prevenExt1.setKeyval(keyVal1);
		dao.persist(prevenExt1);
		
		PreventionExt prevenExt2 = new PreventionExt();
		EntityDataGenerator.generateTestDataForModelClass(prevenExt2);
		prevenExt2.setVal(val2);
		prevenExt2.setKeyval(keyVal2);
		dao.persist(prevenExt2);
		
		PreventionExt prevenExt3 = new PreventionExt();
		EntityDataGenerator.generateTestDataForModelClass(prevenExt3);
		prevenExt3.setVal(val1);
		prevenExt3.setKeyval(keyVal1);
		dao.persist(prevenExt3);
		
		List<PreventionExt> expectedResult = new ArrayList<PreventionExt>(Arrays.asList(prevenExt1, prevenExt3));
		List<PreventionExt> result = dao.findByKeyAndValue(keyVal1, val1);

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
	public void testFindByPreventionIdAndKey() throws Exception {
		
		int preventionId1 = 100;
		int preventionId2 = 200;
		
		String keyVal1 = "alpha";
		String keyVal2 = "bravo";
				
		PreventionExt prevenExt1 = new PreventionExt();
		EntityDataGenerator.generateTestDataForModelClass(prevenExt1);
		prevenExt1.setPreventionId(preventionId1);
		prevenExt1.setKeyval(keyVal1);
		dao.persist(prevenExt1);
		
		PreventionExt prevenExt2 = new PreventionExt();
		EntityDataGenerator.generateTestDataForModelClass(prevenExt2);
		prevenExt2.setPreventionId(preventionId2);
		prevenExt2.setKeyval(keyVal2);
		dao.persist(prevenExt2);
		
		PreventionExt prevenExt3 = new PreventionExt();
		EntityDataGenerator.generateTestDataForModelClass(prevenExt3);
		prevenExt3.setPreventionId(preventionId1);
		prevenExt3.setKeyval(keyVal1);
		dao.persist(prevenExt3);
		
		List<PreventionExt> expectedResult = new ArrayList<PreventionExt>(Arrays.asList(prevenExt1, prevenExt3));
		List<PreventionExt> result = dao.findByPreventionIdAndKey(preventionId1, keyVal1);

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
	public void testGetPreventionExt() throws Exception {
		
		int preventionId1 = 1;
		int preventionId2 = 2;
		
		String val1 = "100";
		String val2 = "200";
		
		String keyVal1 = "alpha";
		String keyVal2 = "bravo";
		
		PreventionExt prevenExt1 = new PreventionExt();
		EntityDataGenerator.generateTestDataForModelClass(prevenExt1);
		prevenExt1.setPreventionId(preventionId1);
		prevenExt1.setVal(val1);
		prevenExt1.setKeyval(keyVal1);
		dao.persist(prevenExt1);
		
		PreventionExt prevenExt2 = new PreventionExt();
		EntityDataGenerator.generateTestDataForModelClass(prevenExt2);
		prevenExt2.setPreventionId(preventionId2);
		prevenExt2.setVal(val2);
		prevenExt2.setKeyval(keyVal2);
		dao.persist(prevenExt2);
		
		HashMap<String, String> expectedResult = new HashMap<String, String>();
		expectedResult.put(keyVal1, val1);
		HashMap<String, String> result = dao.getPreventionExt(preventionId1);
		
		assertEquals(expectedResult, result);
	}

}