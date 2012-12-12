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
import org.oscarehr.common.model.DemographicMerged;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class DemographicMergedDaoTest extends DaoTestFixtures {

	protected DemographicMergedDao dao = SpringUtils.getBean(DemographicMergedDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographic_merged");
	}

	@Test
	public void testCreate() throws Exception {
		DemographicMerged entity = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test 
	public void testFindCurrentByMergedTo() throws Exception {
	
		int mergedTo1 = 111;
		int mergedTo2 = 222;
		
		int isNotDeleted = 0;
		int isDeleted = 1;
		
		DemographicMerged demoMerged1 = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(demoMerged1);
		demoMerged1.setMergedTo(mergedTo1);
		demoMerged1.setDeleted(isNotDeleted);
		dao.persist(demoMerged1);
		
		DemographicMerged demoMerged2 = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(demoMerged2);
		demoMerged2.setMergedTo(mergedTo2);
		demoMerged2.setDeleted(isNotDeleted);
		dao.persist(demoMerged2);
		
		DemographicMerged demoMerged3 = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(demoMerged3);
		demoMerged3.setMergedTo(mergedTo1);
		demoMerged3.setDeleted(isNotDeleted);
		dao.persist(demoMerged3);
		
		DemographicMerged demoMerged4 = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(demoMerged4);
		demoMerged4.setMergedTo(mergedTo1);
		demoMerged4.setDeleted(isDeleted);
		dao.persist(demoMerged4);
		
		List<DemographicMerged> expectedResult = new ArrayList<DemographicMerged>(Arrays.asList(demoMerged1, demoMerged3));		
		List<DemographicMerged> result = dao.findCurrentByMergedTo(mergedTo1);
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
	public void testFindCurrentByDemographicNo() throws Exception {
		
		int demographicNo1 = 111;
		int demographicNo2 = 222;
		
		int isNotDeleted = 0;
		int isDeleted = 1;
		
		DemographicMerged demoMerged1 = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(demoMerged1);
		demoMerged1.setDemographicNo(demographicNo1);
		demoMerged1.setDeleted(isNotDeleted);
		dao.persist(demoMerged1);
		
		DemographicMerged demoMerged2 = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(demoMerged2);
		demoMerged2.setDemographicNo(demographicNo2);
		demoMerged2.setDeleted(isNotDeleted);
		dao.persist(demoMerged2);
		
		DemographicMerged demoMerged3 = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(demoMerged3);
		demoMerged3.setDemographicNo(demographicNo1);
		demoMerged3.setDeleted(isNotDeleted);
		dao.persist(demoMerged3);
		
		DemographicMerged demoMerged4 = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(demoMerged4);
		demoMerged4.setDemographicNo(demographicNo1);
		demoMerged4.setDeleted(isDeleted);
		dao.persist(demoMerged4);
		
		List<DemographicMerged> expectedResult = new ArrayList<DemographicMerged>(Arrays.asList(demoMerged1, demoMerged3));		
		List<DemographicMerged> result = dao.findCurrentByDemographicNo(demographicNo1);
		Logger logger = MiscUtils.getLogger();
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. Result: "+result.size());
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
	public void testFindByDemographicNo() throws Exception {
		
		int demographicNo1 = 111;
		int demographicNo2 = 222;
				
		DemographicMerged demoMerged1 = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(demoMerged1);
		demoMerged1.setDemographicNo(demographicNo1);
		dao.persist(demoMerged1);
		
		DemographicMerged demoMerged2 = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(demoMerged2);
		demoMerged2.setDemographicNo(demographicNo2);
		dao.persist(demoMerged2);
		
		DemographicMerged demoMerged3 = new DemographicMerged();
		EntityDataGenerator.generateTestDataForModelClass(demoMerged3);
		demoMerged3.setDemographicNo(demographicNo2);
		dao.persist(demoMerged3);
		
		List<DemographicMerged> expectedResult = new ArrayList<DemographicMerged>(Arrays.asList(demoMerged1));		
		List<DemographicMerged> result = dao.findByDemographicNo(demographicNo1);
		
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
}