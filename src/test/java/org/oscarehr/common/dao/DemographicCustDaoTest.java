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
import org.oscarehr.common.model.DemographicCust;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class DemographicCustDaoTest extends DaoTestFixtures {

	protected DemographicCustDao dao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographic","demographiccust");
	}

	@Test
	public void testCreate() throws Exception {
		DemographicCust entity = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(1);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test 
	public void testFindMultipleMidwife() throws Exception {
		
		int id1 = 101;
		int id2 = 202;
		int id3 = 303;
		
		String midwife1 = "alpha";
		String midwife2 = "bravo";
		
		DemographicCust demoCust1 = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(demoCust1);
		demoCust1.setId(id1);
		demoCust1.setMidwife(midwife1);
		dao.persist(demoCust1);
		
		DemographicCust demoCust2 = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(demoCust2);
		demoCust2.setId(id2);
		demoCust2.setMidwife(midwife2);
		dao.persist(demoCust2);
		
		DemographicCust demoCust3 = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(demoCust3);
		demoCust3.setId(id3);
		demoCust3.setMidwife(midwife1);
		dao.persist(demoCust3);
		
		List<Integer> demographicNos = new ArrayList<Integer>(Arrays.asList(id1, id2, id3));
		
		List<DemographicCust> expectedResult = new ArrayList<DemographicCust>(Arrays.asList(demoCust1, demoCust3));
		List<DemographicCust> result = dao.findMultipleMidwife(demographicNos, midwife1);
		
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
	public void testFindMultipleResident() throws Exception {
		
		int id1 = 101;
		int id2 = 202;
		int id3 = 303;
		
		String resident1 = "alpha";
		String resident2 = "bravo";
		
		DemographicCust demoCust1 = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(demoCust1);
		demoCust1.setId(id1);
		demoCust1.setResident(resident1);
		dao.persist(demoCust1);
		
		DemographicCust demoCust2 = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(demoCust2);
		demoCust2.setId(id2);
		demoCust2.setResident(resident2);
		dao.persist(demoCust2);
		
		DemographicCust demoCust3 = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(demoCust3);
		demoCust3.setId(id3);
		demoCust3.setResident(resident1);
		dao.persist(demoCust3);
		
		List<Integer> demographicNos = new ArrayList<Integer>(Arrays.asList(id1, id2, id3));
		
		List<DemographicCust> expectedResult = new ArrayList<DemographicCust>(Arrays.asList(demoCust1, demoCust3));
		List<DemographicCust> result = dao.findMultipleResident(demographicNos, resident1);
		
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
	public void testFindMultipleNurse() throws Exception {
		
		int id1 = 101;
		int id2 = 202;
		int id3 = 303;
		
		String nurse1 = "alpha";
		String nurse2 = "bravo";
		
		DemographicCust demoCust1 = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(demoCust1);
		demoCust1.setId(id1);
		demoCust1.setNurse(nurse1);
		dao.persist(demoCust1);
		
		DemographicCust demoCust2 = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(demoCust2);
		demoCust2.setId(id2);
		demoCust2.setNurse(nurse2);
		dao.persist(demoCust2);
		
		DemographicCust demoCust3 = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(demoCust3);
		demoCust3.setId(id3);
		demoCust3.setNurse(nurse1);
		dao.persist(demoCust3);
		
		List<Integer> demographicNos = new ArrayList<Integer>(Arrays.asList(id1, id2, id3));
		
		List<DemographicCust> expectedResult = new ArrayList<DemographicCust>(Arrays.asList(demoCust1, demoCust3));
		List<DemographicCust> result = dao.findMultipleNurse(demographicNos, nurse1);
		
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
