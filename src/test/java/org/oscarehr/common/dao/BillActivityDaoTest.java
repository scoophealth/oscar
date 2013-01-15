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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.dao.BillActivityDao;
import org.oscarehr.billing.CA.model.BillActivity;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class BillActivityDaoTest extends DaoTestFixtures {

	protected BillActivityDao dao = (BillActivityDao)SpringUtils.getBean(BillActivityDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	@Before
	public void setup() throws Exception {
		SchemaUtils.restoreTable(false, "billactivity");
	}

	@Test
	public void testCreate() throws Exception {
		BillActivity entity = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	/**
	 * Test throws errors with sql query (BillActivityDao.java, line 45)
	 * @throws Exception
	 */
	@Test
	public void testFindCurrentByMonthCodeAndGroupNo() throws Exception {
		String monthCode = "A";
		String groupNo= "101";
		Date updateDateTime = new Date(dfm.parse("20080101").getTime());

		BillActivity billActivity1 = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(billActivity1);
		Date date1 = new Date(dfm.parse("20090101").getTime());
		billActivity1.setUpdateDateTime(date1);
		billActivity1.setMonthCode(monthCode);
		billActivity1.setGroupNo(groupNo);
		billActivity1.setStatus("A");
		billActivity1.setBatchCount(10);

		// wrong monthCode; should not be selected
		BillActivity billActivity2 = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(billActivity2);
		Date date2 = new Date(dfm.parse("20090101").getTime());
		billActivity2.setUpdateDateTime(date2);
		billActivity2.setMonthCode("B");
		billActivity2.setGroupNo(groupNo);
		billActivity2.setStatus("A");

		// wrong group number; should not be selected
		BillActivity billActivity3 = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(billActivity3);
		Date date3 = new Date(dfm.parse("20090101").getTime());
		billActivity3.setUpdateDateTime(date3);
		billActivity3.setMonthCode(monthCode);
		billActivity3.setGroupNo("102");
		billActivity3.setStatus("A");

		// update time older than specified; should not be selected
		BillActivity billActivity4 = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(billActivity4);
		Date date4 = new Date(dfm.parse("20070101").getTime());
		billActivity4.setUpdateDateTime(date4);
		billActivity4.setMonthCode(monthCode);
		billActivity4.setGroupNo(groupNo);
		billActivity4.setStatus("A");

		// inactive; should not be selected
		BillActivity billActivity5 = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(billActivity5);
		Date date5 = new Date(dfm.parse("20090101").getTime());
		billActivity5.setUpdateDateTime(date5);
		billActivity5.setMonthCode(monthCode);
		billActivity5.setGroupNo(groupNo);
		billActivity5.setStatus("D");

		BillActivity billActivity6 = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(billActivity6);
		Date date6 = new Date(dfm.parse("20090101").getTime());
		billActivity6.setUpdateDateTime(date6);
		billActivity6.setMonthCode(monthCode);
		billActivity6.setGroupNo(groupNo);
		billActivity6.setStatus("A");
		billActivity6.setBatchCount(6);

		dao.persist(billActivity1);
		dao.persist(billActivity2);
		dao.persist(billActivity3);
		dao.persist(billActivity4);
		dao.persist(billActivity5);
		dao.persist(billActivity6);

		List<BillActivity> result = dao.findCurrentByMonthCodeAndGroupNo(monthCode, groupNo, updateDateTime);
		List<BillActivity> expectedResult = new ArrayList<BillActivity>(Arrays.asList(
				billActivity6, billActivity1));

		if (result.size() != expectedResult.size()) {
			fail("Array sizes do not match.");
		}

		for (int i =0; i < result.size(); i++) {
			if (!result.get(i).equals(expectedResult.get(i))) {
				fail("Items not ordered by batch count.");
			}
		}

		assertTrue(true);
	}

	/**
	 * Test throws errors with sql query (BillActivityDao.java, line 57)
	 * @throws Exception
	 */
	@Test
	public void testFindCurrentByDateRange() throws Exception {
		String monthCode = "A";
		String groupNo= "101";
		Date startDate = new Date(dfm.parse("20090101").getTime());
		Date endDate = new Date(dfm.parse("20090301").getTime());

		BillActivity billActivity1 = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(billActivity1);
		Date date1 = new Date(dfm.parse("20090101").getTime());
		billActivity1.setUpdateDateTime(date1);
		billActivity1.setMonthCode(monthCode);
		billActivity1.setGroupNo(groupNo);
		billActivity1.setStatus("A");

		BillActivity billActivity2 = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(billActivity2);
		Date date2 = new Date(dfm.parse("20090201").getTime());
		billActivity2.setUpdateDateTime(date2);
		billActivity2.setStatus("A");

		BillActivity billActivity3 = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(billActivity3);
		Date date3 = new Date(dfm.parse("20090301").getTime());
		billActivity3.setUpdateDateTime(date3);
		billActivity3.setStatus("A");

		BillActivity billActivity4 = new BillActivity();
		EntityDataGenerator.generateTestDataForModelClass(billActivity4);
		Date date4 = new Date(dfm.parse("20081231").getTime());
		billActivity4.setUpdateDateTime(date4);
		billActivity4.setStatus("A");
		
		dao.persist(billActivity1);
		dao.persist(billActivity2);
		dao.persist(billActivity3);
		dao.persist(billActivity4);
		
		List<BillActivity> result = dao.findCurrentByDateRange(startDate, endDate);
		List<BillActivity> expectedResult = new ArrayList<BillActivity>(Arrays.asList(
				billActivity3,billActivity2, billActivity1));

		if (result.size() != expectedResult.size()) {
			fail("Array sizes do not match.");
		}

		for (int i =0; i < result.size(); i++) {
			if (!result.get(i).equals(expectedResult.get(i))) {
				fail("Items not ordered by id.");
			}
		}

		assertTrue(true);
	}
}
