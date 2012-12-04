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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.PrintResourceLog;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.MiscUtils;

public class PrintResourceLogDaoTest extends DaoTestFixtures {

	protected PrintResourceLogDao dao = SpringUtils.getBean(PrintResourceLogDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("PrintResourceLog");
	}

	@Test
	public void testCreate() throws Exception {
		PrintResourceLog entity = new PrintResourceLog();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	
	@Test
	public void testFindByResource() throws Exception {
		
		String resourceId1 = "100";
		String resourceId2 = "200";
		
		String resourceName1 = "alpha";
		String resourceName2 = "bravo";
		
		PrintResourceLog printResourceLog1 = new PrintResourceLog();
		EntityDataGenerator.generateTestDataForModelClass(printResourceLog1);
		printResourceLog1.setResourceId(resourceId1);
		printResourceLog1.setResourceName(resourceName1);
		Date date1 = new Date(dfm.parse("20010101").getTime());
		printResourceLog1.setDateTime(date1);
		dao.persist(printResourceLog1);
		
		PrintResourceLog printResourceLog2 = new PrintResourceLog();
		EntityDataGenerator.generateTestDataForModelClass(printResourceLog2);
		printResourceLog2.setResourceId(resourceId2);
		printResourceLog2.setResourceName(resourceName2);
		Date date2 = new Date(dfm.parse("20100101").getTime());
		printResourceLog2.setDateTime(date2);
		dao.persist(printResourceLog2);
		
		PrintResourceLog printResourceLog3 = new PrintResourceLog();
		EntityDataGenerator.generateTestDataForModelClass(printResourceLog3);
		printResourceLog3.setResourceId(resourceId1);
		printResourceLog3.setResourceName(resourceName1);
		Date date3 = new Date(dfm.parse("20080101").getTime());
		printResourceLog3.setDateTime(date3);
		dao.persist(printResourceLog3);
		
		List<PrintResourceLog> expectedResult = new ArrayList<PrintResourceLog>(Arrays.asList(printResourceLog3, printResourceLog1));
		List<PrintResourceLog> result = dao.findByResource(resourceName1, resourceId1);

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
