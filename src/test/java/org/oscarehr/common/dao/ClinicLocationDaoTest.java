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
import org.oscarehr.common.model.ClinicLocation;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ClinicLocationDaoTest extends DaoTestFixtures {

	protected ClinicLocationDao dao = (ClinicLocationDao)SpringUtils.getBean(ClinicLocationDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "clinic_location");
	}

        @Test
        public void testCreate() throws Exception {
                ClinicLocation entity = new ClinicLocation();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);
                assertNotNull(entity.getId());
        }


	@Test
	public void testFindByClinicNo() throws Exception {
		
		int clinicNo1 = 101;
		int clinicNo2 = 202;
		
		ClinicLocation clinicLocation1 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation1);
		clinicLocation1.setClinicNo(clinicNo1);
		dao.persist(clinicLocation1);
		
		ClinicLocation clinicLocation2 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation2);
		clinicLocation2.setClinicNo(clinicNo2);
		dao.persist(clinicLocation2);
		
		ClinicLocation clinicLocation3 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation3);
		clinicLocation3.setClinicNo(clinicNo1);
		dao.persist(clinicLocation3);
		
		List<ClinicLocation> expectedResult = new ArrayList<ClinicLocation>(Arrays.asList(clinicLocation1, clinicLocation3));
		List<ClinicLocation> result = dao.findByClinicNo(clinicNo1);

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
	public void testSearchVisitLocation() throws Exception {
		
		String clinicNo1 = "101";
		String clinicNo2 = "202";
		String clinicNo3 = "303";
		
		String clinicLocationName1 = "alpha";
		String clinicLocationName2 = "bravo";
		String clinicLocationName3 = "charlie";
		
		ClinicLocation clinicLocation1 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation1);
		clinicLocation1.setClinicLocationNo(clinicNo1);
		clinicLocation1.setClinicLocationName(clinicLocationName1);
		dao.persist(clinicLocation1);
		
		ClinicLocation clinicLocation2 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation2);
		clinicLocation2.setClinicLocationNo(clinicNo2);
		clinicLocation2.setClinicLocationName(clinicLocationName2);
		dao.persist(clinicLocation2);
		
		ClinicLocation clinicLocation3 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation3);
		clinicLocation3.setClinicLocationNo(clinicNo3);
		clinicLocation3.setClinicLocationName(clinicLocationName3);
		dao.persist(clinicLocation3);
		
		String expectedResult = clinicLocationName2;
		String result = dao.searchVisitLocation(clinicNo2);
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testSearchBillLocation() throws Exception {
		
		int clinicNo1 = 101;
		int clinicNo2 = 202;
		int clinicNo3 = 303;
		
		String clinicLocationNo1 = "111";
		String clinicLocationNo2 = "222";
		String clinicLocationNo3 = "333";
		
		ClinicLocation clinicLocation1 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation1);
		clinicLocation1.setClinicNo(clinicNo1);
		clinicLocation1.setClinicLocationNo(clinicLocationNo1);
		dao.persist(clinicLocation1);
		
		ClinicLocation clinicLocation2 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation2);
		clinicLocation2.setClinicNo(clinicNo2);
		clinicLocation2.setClinicLocationNo(clinicLocationNo2);
		dao.persist(clinicLocation2);
		
		ClinicLocation clinicLocation3 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation3);
		clinicLocation3.setClinicNo(clinicNo3);
		clinicLocation3.setClinicLocationNo(clinicLocationNo3);
		dao.persist(clinicLocation3);
		
		ClinicLocation expectedResult = clinicLocation2;
		ClinicLocation result = dao.searchBillLocation(clinicNo2, clinicLocationNo2);
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testRemoveByClinicLocationNo() throws Exception {
		
		String clinicLocationNo1 = "111";
		String clinicLocationNo2 = "222";
		String clinicLocationNo3 = "333";
		
		ClinicLocation clinicLocation1 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation1);
		clinicLocation1.setClinicLocationNo(clinicLocationNo1);
		dao.persist(clinicLocation1);
		
		ClinicLocation clinicLocation2 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation2);
		clinicLocation2.setClinicLocationNo(clinicLocationNo2);
		dao.persist(clinicLocation2);
		
		ClinicLocation clinicLocation3 = new ClinicLocation();
		EntityDataGenerator.generateTestDataForModelClass(clinicLocation3);
		clinicLocation3.setClinicLocationNo(clinicLocationNo3);
		dao.persist(clinicLocation3);
		
		dao.removeByClinicLocationNo(clinicLocationNo2);
		
		List<ClinicLocation> expectedResult = new ArrayList<ClinicLocation>(Arrays.asList(clinicLocation1, clinicLocation3));
		List<ClinicLocation> result = dao.findAll();

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
