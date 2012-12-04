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
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.OscarLog;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class OscarLogDaoTest extends DaoTestFixtures {
	protected OscarLogDao dao = (OscarLogDao)SpringUtils.getBean(OscarLogDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("log");
	}

	@Test
	public void testFindByDemographicId() throws Exception {
		
		int demographicId1 = 100;
		int demographicId2 = 200;
		
		OscarLog oscarLog1 = new OscarLog();
		EntityDataGenerator.generateTestDataForModelClass(oscarLog1);
		oscarLog1.setDemographicId(demographicId1);
		dao.persist(oscarLog1);
		
		OscarLog oscarLog2 = new OscarLog();
		EntityDataGenerator.generateTestDataForModelClass(oscarLog2);
		oscarLog2.setDemographicId(demographicId2);
		dao.persist(oscarLog2);
		
		OscarLog oscarLog3 = new OscarLog();
		EntityDataGenerator.generateTestDataForModelClass(oscarLog3);
		oscarLog3.setDemographicId(demographicId1);
		dao.persist(oscarLog3);
		
		List<OscarLog> expectedResult = new ArrayList<OscarLog>(Arrays.asList(oscarLog1, oscarLog3));
		List<OscarLog> result = dao.findByDemographicId(demographicId1);

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
	public void testHasRead() throws Exception {
		
		String providerNo1 = "100";
		String providerNo2 = "200";
		
		String action1 = "read";
		String action2 = "NotRead";
		
		String content1 = "epsilon";
		String content2 = "lambda";
		
		String contentId1 = "111";
		String contentId2 = "222";
		
		OscarLog oscarLog1 = new OscarLog();
		EntityDataGenerator.generateTestDataForModelClass(oscarLog1);
		oscarLog1.setProviderNo(providerNo1);
		oscarLog1.setContent(content1);
		oscarLog1.setContentId(contentId1);
		oscarLog1.setAction(action1);
		dao.persist(oscarLog1);
		
		OscarLog oscarLog2 = new OscarLog();
		EntityDataGenerator.generateTestDataForModelClass(oscarLog2);
		oscarLog2.setProviderNo(providerNo2);
		oscarLog2.setContent(content2);
		oscarLog2.setContentId(contentId2);
		oscarLog2.setAction(action2);
		dao.persist(oscarLog2);
		
		OscarLog oscarLog3 = new OscarLog();
		EntityDataGenerator.generateTestDataForModelClass(oscarLog3);
		oscarLog3.setProviderNo(providerNo1);
		oscarLog3.setContent(content1);
		oscarLog3.setContentId(contentId1);
		oscarLog3.setAction(action2);
		dao.persist(oscarLog3);
		
		boolean expectedResult = true;
		boolean result = dao.hasRead(providerNo1, content1, contentId1);

		assertEquals(expectedResult, result);
	}
}