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
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.BillingONPremium;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class BillingONPremiumDaoTest extends DaoTestFixtures {

	protected BillingONPremiumDao dao = (BillingONPremiumDao)SpringUtils.getBean(BillingONPremiumDao.class);
	protected DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("billing_on_premium");
	}

	@Test
	/**
	 * Ensures that the getActiveRAPremiumsByPayDate function only returns premiums where
	 * the paydate is within the given range, and the status is set to true.
	 * @throws Exception
	 */
	public void testGetActiveRAPremiumsByPayDate() throws Exception {
		Date startDate = new Date(dfm.parse("20090101").getTime());
		Date endDate = new Date(dfm.parse("20120101").getTime());
		Locale locale = new Locale(""); // not used in dao method

		// One day before lower boundary
		BillingONPremium billONPrem1 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem1);
		Date date1= new Date(dfm.parse("20081231").getTime());
		billONPrem1.setPayDate(date1);
		billONPrem1.setStatus(true);

		// On day of lower boundary
		BillingONPremium billONPrem2 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem2);
		Date date2= new Date(dfm.parse("20090101").getTime());
		billONPrem2.setPayDate(date2);
		billONPrem2.setStatus(true);

		// Inside range but status is false
		BillingONPremium billONPrem3 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem3);
		Date date3= new Date(dfm.parse("20100601").getTime());
		billONPrem3.setPayDate(date3);
		billONPrem3.setStatus(false);

		// Inside range
		BillingONPremium billONPrem4 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem4);
		Date date4 = new Date(dfm.parse("20110101").getTime());
		billONPrem4.setPayDate(date4);
		billONPrem4.setStatus(true);

		// On day of upper boundary
		BillingONPremium billONPrem5 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem5);
		Date date5 = new Date(dfm.parse("20120101").getTime());
		billONPrem5.setPayDate(date5);
		billONPrem5.setStatus(true);

		// One day after upper boundary
		BillingONPremium billONPrem6 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem6);
		Date date6 = new Date(dfm.parse("20120102").getTime());
		billONPrem6.setPayDate(date6);
		billONPrem6.setStatus(true);

		dao.persist(billONPrem1);
		dao.persist(billONPrem2);
		dao.persist(billONPrem3);
		dao.persist(billONPrem4);
		dao.persist(billONPrem5);
		dao.persist(billONPrem6);

		List<BillingONPremium> expectedList = new ArrayList<BillingONPremium>(Arrays.asList(
				billONPrem2,
				billONPrem4
				));

		List<BillingONPremium> resultList = dao.getActiveRAPremiumsByPayDate(startDate, endDate, locale);

		// fail if list sizes aren't the same
		if (resultList.size() != expectedList.size()) {
			fail("Array sizes do not match.");
		}

		boolean containsAllItems = resultList.containsAll(expectedList);
		assertTrue(containsAllItems);
	}

	@Test
	/**
	 * Ensures that query only returns premiums where
	 * the paydate is within the given range, 
	 * the status is set to true, and
	 * the provider numbers match.
	 */
	public void testGetActiveRAPremiumsByProvider() throws Exception {
		Provider provider = new Provider();
		provider.setProviderNo("1");
		Date startDate = new Date(dfm.parse("20090101").getTime());
		Date endDate = new Date(dfm.parse("20120101").getTime());
		Locale locale = new Locale(""); // not used in dao method

		// On day of lower boundary
		// Matching provider number
		BillingONPremium billONPrem1 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem1);
		Date date1 = new Date(dfm.parse("20090101").getTime());
		billONPrem1.setPayDate(date1);
		billONPrem1.setProviderNo("1");
		billONPrem1.setStatus(true);

		// Inside range
		// Non-matching provider number
		BillingONPremium billONPrem2 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem2);
		Date date2 = new Date(dfm.parse("20100601").getTime());
		billONPrem2.setPayDate(date2);
		billONPrem2.setProviderNo("2");
		billONPrem2.setStatus(true);

		// Inside range
		// Matching provider number
		BillingONPremium billONPrem3 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem3);
		Date date3 = new Date(dfm.parse("20110101").getTime());
		billONPrem3.setPayDate(date3);
		billONPrem3.setProviderNo("1");
		billONPrem3.setStatus(true);

		// Inside range
		// Matching provider number
		BillingONPremium billONPrem4 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem4);
		Date date4 = new Date(dfm.parse("20110601").getTime());
		billONPrem4.setPayDate(date4);
		billONPrem4.setProviderNo("1");
		billONPrem4.setStatus(false);

		// On day of upper boundary
		// Matching provider number
		BillingONPremium billONPrem5 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem5);
		Date date5 = new Date(dfm.parse("20120101").getTime());
		billONPrem5.setPayDate(date5);
		billONPrem5.setProviderNo("1");
		billONPrem5.setStatus(true);

		dao.persist(billONPrem1);
		dao.persist(billONPrem2);
		dao.persist(billONPrem3);
		dao.persist(billONPrem4);
		dao.persist(billONPrem5);

		List<BillingONPremium> expectedList = new ArrayList<BillingONPremium>(Arrays.asList(
				billONPrem1,
				billONPrem3
				));

		List<BillingONPremium> resultList = dao.getActiveRAPremiumsByProvider(provider, startDate, endDate, locale);

		// fail if list sizes aren't the same
		if (resultList.size() != expectedList.size()) {
			fail("Array sizes do not match.");
		}

		boolean containsAllItems = resultList.containsAll(expectedList);
		assertTrue(containsAllItems);
	}

	@Test
	/**
	 * Ensures that premiums can be selected by header number.
	 * @throws Exception
	 */
	public void testGetRAPremiumsByRaHeaderNo() throws Exception {
		Integer raHeaderNo = 1;

		BillingONPremium billONPrem1 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem1);
		billONPrem1.setRAHeaderNo(2);

		BillingONPremium billONPrem2 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem2);
		billONPrem2.setRAHeaderNo(1);

		BillingONPremium billONPrem3 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem3);
		billONPrem3.setRAHeaderNo(3);

		BillingONPremium billONPrem4 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem4);
		billONPrem4.setRAHeaderNo(1);

		dao.persist(billONPrem1);
		dao.persist(billONPrem2);
		dao.persist(billONPrem3);
		dao.persist(billONPrem4);

		List<BillingONPremium> expectedList = new ArrayList<BillingONPremium>(Arrays.asList(
				billONPrem2,
				billONPrem4
				));

		List<BillingONPremium> resultList = dao.getRAPremiumsByRaHeaderNo(raHeaderNo);

		// fail if list sizes aren't the same
		if (resultList.size() != expectedList.size()) {
			fail("Array sizes do not match.");
		}

		boolean containsAllItems = resultList.containsAll(expectedList);
		assertTrue(containsAllItems);
	}

	@Test
	/**
	 * Ensures that negative header numbers will
	 * still return a set of premiums.
	 * @throws Exception
	 */
	public void testGetRAPremiumsByRaHeaderNo_NegativeHeaderNo() throws Exception {
		Integer raHeaderNo = -1;

		BillingONPremium billONPrem1 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem1);
		billONPrem1.setRAHeaderNo(2);

		BillingONPremium billONPrem2 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem2);
		billONPrem2.setRAHeaderNo(-1);

		BillingONPremium billONPrem3 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem3);
		billONPrem3.setRAHeaderNo(3);

		BillingONPremium billONPrem4 = new BillingONPremium();
		EntityDataGenerator.generateTestDataForModelClass(billONPrem4);
		billONPrem4.setRAHeaderNo(-1);

		dao.persist(billONPrem1);
		dao.persist(billONPrem2);
		dao.persist(billONPrem3);
		dao.persist(billONPrem4);

		List<BillingONPremium> expectedList = new ArrayList<BillingONPremium>(Arrays.asList(
				billONPrem2,
				billONPrem4
				));

		List<BillingONPremium> resultList = dao.getRAPremiumsByRaHeaderNo(raHeaderNo);

		// fail if list sizes aren't the same
		if (resultList.size() != expectedList.size()) {
			fail("Array sizes do not match.");
		}

		boolean containsAllItems = resultList.containsAll(expectedList);
		assertTrue(containsAllItems);
	}
}
