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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DemographicArchive;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class DemographicArchiveDaoTest extends DaoTestFixtures {

	protected DemographicArchiveDao dao = (DemographicArchiveDao)SpringUtils.getBean(DemographicArchiveDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographicArchive");
	}

        @Test
        public void testCreate() throws Exception {
                DemographicArchive entity = new DemographicArchive();
                EntityDataGenerator.generateTestDataForModelClass(entity);

                dao.persist(entity);
                assertNotNull(entity.getId());
        }

	@Test
	public void testFindByDemographicNo() throws Exception {
		
		int demoNo1 = 101;
		int demoNo2 = 202;
		
		DemographicArchive demographicArchive1 = new DemographicArchive();
		EntityDataGenerator.generateTestDataForModelClass(demographicArchive1);
		demographicArchive1.setDemographicNo(demoNo1);
		dao.persist(demographicArchive1);
		
		DemographicArchive demographicArchive2 = new DemographicArchive();
		EntityDataGenerator.generateTestDataForModelClass(demographicArchive2);
		demographicArchive2.setDemographicNo(demoNo1);
		dao.persist(demographicArchive2);
		
		DemographicArchive demographicArchive3 = new DemographicArchive();
		EntityDataGenerator.generateTestDataForModelClass(demographicArchive3);
		demographicArchive3.setDemographicNo(demoNo2);
		dao.persist(demographicArchive3);
		
		DemographicArchive demographicArchive4 = new DemographicArchive();
		EntityDataGenerator.generateTestDataForModelClass(demographicArchive4);
		demographicArchive4.setDemographicNo(demoNo1);
		dao.persist(demographicArchive4);
		
		List<DemographicArchive> expectedResult = new ArrayList<DemographicArchive>(Arrays.asList(demographicArchive1, demographicArchive2, demographicArchive4));
		List<DemographicArchive> result = dao.findByDemographicNo(demoNo1);

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
	public void testFindRosterStatusHistoryByDemographicNo() throws Exception {
		
		int demoNo1 = 101;
		int demoNo2 = 202;
		
		String rosterStatus1 = "alpha";
		String rosterStatus2 = "bravo";
		String rosterStatus3 = "charlie";
		
		DemographicArchive demographicArchive1 = new DemographicArchive();
		EntityDataGenerator.generateTestDataForModelClass(demographicArchive1);
		demographicArchive1.setDemographicNo(demoNo1);
		demographicArchive1.setRosterStatus(rosterStatus1);
		dao.persist(demographicArchive1);
		
		DemographicArchive demographicArchive2 = new DemographicArchive();
		EntityDataGenerator.generateTestDataForModelClass(demographicArchive2);
		demographicArchive2.setDemographicNo(demoNo1);
		demographicArchive2.setRosterStatus(rosterStatus2);
		dao.persist(demographicArchive2);
		
		DemographicArchive demographicArchive3 = new DemographicArchive();
		EntityDataGenerator.generateTestDataForModelClass(demographicArchive3);
		demographicArchive3.setDemographicNo(demoNo2);
		demographicArchive3.setRosterStatus(rosterStatus3);
		dao.persist(demographicArchive3);
		
		DemographicArchive demographicArchive4 = new DemographicArchive();
		EntityDataGenerator.generateTestDataForModelClass(demographicArchive4);
		demographicArchive4.setDemographicNo(demoNo1);
		demographicArchive4.setRosterStatus(rosterStatus3);
		dao.persist(demographicArchive4);
		
		DemographicArchive demographicArchive5 = new DemographicArchive();
		EntityDataGenerator.generateTestDataForModelClass(demographicArchive5);
		demographicArchive5.setDemographicNo(demoNo1);
		demographicArchive5.setRosterStatus(rosterStatus3);
		dao.persist(demographicArchive5);
		
		List<DemographicArchive> expectedResult = new ArrayList<DemographicArchive>(Arrays.asList(demographicArchive4, demographicArchive2, demographicArchive1));
		List<DemographicArchive> result = dao.findRosterStatusHistoryByDemographicNo(demoNo1);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. Result: " + result.size());
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
