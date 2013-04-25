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
import org.oscarehr.common.model.PageMonitor;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class PageMonitorDaoTest extends DaoTestFixtures {
	
	protected PageMonitorDao dao = (PageMonitorDao)SpringUtils.getBean(PageMonitorDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("PageMonitor");
	}

	@Test
	public void testFindByPage() throws Exception {
		
		String pageId1 = "100";
		String pageId2 = "200";
		
		String pageName1 = "alpha";
		String pageName2 = "bravo";
		
		PageMonitor pageMonitor1 = new PageMonitor();
		EntityDataGenerator.generateTestDataForModelClass(pageMonitor1);
		pageMonitor1.setPageId(pageId1);
		pageMonitor1.setPageName(pageName1);
		Date updateDate1 = new Date(dfm.parse("20010701").getTime());
		pageMonitor1.setUpdateDate(updateDate1);
		dao.persist(pageMonitor1);
		
		PageMonitor pageMonitor2 = new PageMonitor();
		EntityDataGenerator.generateTestDataForModelClass(pageMonitor2);
		pageMonitor2.setPageId(pageId2);
		pageMonitor2.setPageName(pageName2);
		Date updateDate2 = new Date(dfm.parse("20100701").getTime());
		pageMonitor2.setUpdateDate(updateDate2);
		dao.persist(pageMonitor2);
		
		PageMonitor pageMonitor3 = new PageMonitor();
		EntityDataGenerator.generateTestDataForModelClass(pageMonitor3);
		pageMonitor3.setPageId(pageId1);
		pageMonitor3.setPageName(pageName1);
		Date updateDate3 = new Date(dfm.parse("20110701").getTime());
		pageMonitor3.setUpdateDate(updateDate3);
		dao.persist(pageMonitor3);
		
		List<PageMonitor> expectedResult = new ArrayList<PageMonitor>(Arrays.asList(pageMonitor3, pageMonitor1));
		List<PageMonitor> result = dao.findByPage(pageName1, pageId1);

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
	public void testFindByPageName() throws Exception {
		
		String pageId1 = "100";
		String pageId2 = "200";
		
		String pageName1 = "alpha";
		String pageName2 = "bravo";
		
		PageMonitor pageMonitor1 = new PageMonitor();
		EntityDataGenerator.generateTestDataForModelClass(pageMonitor1);
		pageMonitor1.setPageId(pageId1);
		pageMonitor1.setPageName(pageName1);
		Date updateDate1 = new Date(dfm.parse("20010701").getTime());
		pageMonitor1.setUpdateDate(updateDate1);
		dao.persist(pageMonitor1);
		
		PageMonitor pageMonitor2 = new PageMonitor();
		EntityDataGenerator.generateTestDataForModelClass(pageMonitor2);
		pageMonitor2.setPageId(pageId2);
		pageMonitor2.setPageName(pageName2);
		Date updateDate2 = new Date(dfm.parse("20100701").getTime());
		pageMonitor2.setUpdateDate(updateDate2);
		dao.persist(pageMonitor2);
		
		PageMonitor pageMonitor3 = new PageMonitor();
		EntityDataGenerator.generateTestDataForModelClass(pageMonitor3);
		pageMonitor3.setPageId(pageId1);
		pageMonitor3.setPageName(pageName1);
		Date updateDate3 = new Date(dfm.parse("20110701").getTime());
		pageMonitor3.setUpdateDate(updateDate3);
		dao.persist(pageMonitor3);
		
		List<PageMonitor> expectedResult = new ArrayList<PageMonitor>(Arrays.asList(pageMonitor3, pageMonitor1));
		List<PageMonitor> result = dao.findByPageName(pageName1);

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
