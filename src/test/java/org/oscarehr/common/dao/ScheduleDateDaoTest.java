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
import org.oscarehr.common.model.ScheduleDate;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ScheduleDateDaoTest extends DaoTestFixtures {
	protected ScheduleDateDao dao = SpringUtils.getBean(ScheduleDateDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	public ScheduleDateDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("scheduledate","mygroup","scheduletemplate");
	}

       @Test
        public void testCreate() throws Exception {
                ScheduleDate entity = new ScheduleDate();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);

                assertNotNull(entity.getId());
        }

	@Test
	public void testFindByProviderNoAndDate() throws Exception {
		
		String providerNo1 = "111";
		String providerNo2 = "222";
		
		Date date1 = new Date(dfm.parse("20110301").getTime());
		Date date2 = new Date(dfm.parse("20100514").getTime());
		
		
		ScheduleDate scheduleDate1 = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(scheduleDate1);
		scheduleDate1.setProviderNo(providerNo1);
		scheduleDate1.setDate(date1);
		scheduleDate1.setStatus('A');
		dao.persist(scheduleDate1);
		
		ScheduleDate scheduleDate2 = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(scheduleDate2);
		scheduleDate2.setProviderNo(providerNo2);
		scheduleDate2.setDate(date2);
		scheduleDate2.setStatus('A');
		dao.persist(scheduleDate2);
		
		ScheduleDate scheduleDate3 = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(scheduleDate3);
		scheduleDate3.setProviderNo(providerNo2);
		scheduleDate3.setDate(date1);
		scheduleDate3.setStatus('B');
		dao.persist(scheduleDate3);
		
		ScheduleDate expectedResult = scheduleDate1;
		ScheduleDate result = dao.findByProviderNoAndDate(providerNo1, date1);
		
		assertEquals(expectedResult, result);

	}
	
	@Test
	public void testFindByProviderPriorityAndDateRange() throws Exception {
		
		String providerNo1 = "111";
		String providerNo2 = "222";
		
		char priority1 = 'a';
		char priority2 = 'b';
		
		Date date1 = new Date(dfm.parse("20110301").getTime());
		Date date2 = new Date(dfm.parse("20100514").getTime());
		Date date3 = new Date(dfm.parse("20090514").getTime());
		Date date4 = new Date(dfm.parse("20131024").getTime());
		
		Date startDate = new Date(dfm.parse("20081210").getTime());
		Date endDate = new Date(dfm.parse("20121014").getTime());
		
		ScheduleDate scheduleDate1 = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(scheduleDate1);
		scheduleDate1.setProviderNo(providerNo1);
		scheduleDate1.setPriority(priority1);
		scheduleDate1.setDate(date1);
		dao.persist(scheduleDate1);
		
		ScheduleDate scheduleDate2 = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(scheduleDate2);
		scheduleDate2.setProviderNo(providerNo2);
		scheduleDate2.setPriority(priority2);
		scheduleDate2.setDate(date2);
		dao.persist(scheduleDate2);
		
		ScheduleDate scheduleDate3 = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(scheduleDate3);
		scheduleDate3.setProviderNo(providerNo1);
		scheduleDate3.setPriority(priority1);
		scheduleDate3.setDate(date3);
		dao.persist(scheduleDate3);
		
		ScheduleDate scheduleDate4 = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(scheduleDate4);
		scheduleDate4.setProviderNo(providerNo2);
		scheduleDate4.setPriority(priority1);
		scheduleDate4.setDate(date4);
		dao.persist(scheduleDate4);
		
		List<ScheduleDate> expectedResult = new ArrayList<ScheduleDate>(Arrays.asList(scheduleDate1, scheduleDate3));
		List<ScheduleDate> result = dao.findByProviderAndDateRange(providerNo1, startDate, endDate);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. Result: " +result.size());
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
	public void testFindByProviderAndDateRange() throws Exception {

		String providerNo1 = "111";
		String providerNo2 = "222";
		
		Date date1 = new Date(dfm.parse("20110301").getTime());
		Date date2 = new Date(dfm.parse("20100514").getTime());
		Date date3 = new Date(dfm.parse("20090514").getTime());
		Date date4 = new Date(dfm.parse("20131024").getTime());
		
		Date startDate = new Date(dfm.parse("20081210").getTime());
		Date endDate = new Date(dfm.parse("20121014").getTime());
		
		ScheduleDate scheduleDate1 = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(scheduleDate1);
		scheduleDate1.setProviderNo(providerNo1);
		scheduleDate1.setDate(date1);
		dao.persist(scheduleDate1);
		
		ScheduleDate scheduleDate2 = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(scheduleDate2);
		scheduleDate2.setProviderNo(providerNo2);
		scheduleDate2.setDate(date2);
		dao.persist(scheduleDate2);
		
		ScheduleDate scheduleDate3 = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(scheduleDate3);
		scheduleDate3.setProviderNo(providerNo1);
		scheduleDate3.setDate(date3);
		dao.persist(scheduleDate3);
		
		ScheduleDate scheduleDate4 = new ScheduleDate();
		EntityDataGenerator.generateTestDataForModelClass(scheduleDate4);
		scheduleDate4.setProviderNo(providerNo2);
		scheduleDate4.setDate(date4);
		dao.persist(scheduleDate4);
		
		List<ScheduleDate> expectedResult = new ArrayList<ScheduleDate>(Arrays.asList(scheduleDate1, scheduleDate3));
		List<ScheduleDate> result = dao.findByProviderAndDateRange(providerNo1, startDate, endDate);

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
    public void testFindByProviderStartDateAndPriority() {
	    assertNotNull(dao.findByProviderStartDateAndPriority("100", new Date(), "PRI"));
    }
}
