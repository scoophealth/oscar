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
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONPayment;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class BillingONPaymentDaoTest extends DaoTestFixtures {
	protected BillingONPaymentDao dao = (BillingONPaymentDao)SpringUtils.getBean(BillingONPaymentDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billing_on_payment", "billing_on_cheader1","billing_on_ext");

	}

	@Test
	public void testFind3rdPartyPayRecordsByBill() throws Exception {
		
		BillingONCHeader1Dao daoBONCH = (BillingONCHeader1Dao)SpringUtils.getBean(BillingONCHeader1Dao.class);
		BillingONCHeader1 bONCHeader1 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(bONCHeader1);
		
		int billingNo = 1;
		
		BillingONPayment bONPayment1 = new BillingONPayment();
		EntityDataGenerator.generateTestDataForModelClass(bONPayment1);
		Date date1 = new Date(dfm.parse("20110101").getTime());
		bONPayment1.setBillingNo(billingNo);
		bONPayment1.setPaymentDate(date1);
					
		BillingONPayment bONPayment2 = new BillingONPayment();
		EntityDataGenerator.generateTestDataForModelClass(bONPayment2);
		Date date2 = new Date(dfm.parse("20110701").getTime());
		bONPayment2.setBillingNo(billingNo);
		bONPayment2.setPaymentDate(date2);
				
		BillingONPayment bONPayment3 = new BillingONPayment();
		EntityDataGenerator.generateTestDataForModelClass(bONPayment3);
		Date date3 = new Date(dfm.parse("20110301").getTime());
		bONPayment3.setBillingNo(billingNo);
		bONPayment3.setPaymentDate(date3);
		
		daoBONCH.persist(bONCHeader1);
		dao.persist(bONPayment1);
		dao.persist(bONPayment2);
		dao.persist(bONPayment3);	
		
		List<BillingONPayment> result = dao.find3rdPartyPayRecordsByBill(bONCHeader1);
		List<BillingONPayment> expectedResult = new ArrayList<BillingONPayment>(Arrays.asList(
				bONPayment1,
				bONPayment3,
				bONPayment2));///add the three payment obj in the expected order and assert = expected result
			
		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not sorted by Billing Payment Date.");
				fail("Items not sorted by Billing Payment Date.");
			}
		}
		assertTrue(true);		
	}

	@Test
	public void testFind3rdPartyPayRecordsByBillBillingONCHeader1DateDate() throws Exception {
		
		BillingONCHeader1Dao daoBONCH = (BillingONCHeader1Dao)SpringUtils.getBean(BillingONCHeader1Dao.class);
		
		BillingONCHeader1 bONCHeader1 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(bONCHeader1);
		Date startDate = new Date(dfm.parse("20101230").getTime());
		Date endDate = new Date(dfm.parse("20120101").getTime());
		
		
		BillingONPayment bONPayment1 = new BillingONPayment();
		EntityDataGenerator.generateTestDataForModelClass(bONPayment1);
		Date Date1 = new Date(dfm.parse("20110102").getTime());
		bONPayment1.setBillingNo(1);
		bONPayment1.setPaymentDate(Date1);
		
		BillingONPayment bONPayment2 = new BillingONPayment();
		EntityDataGenerator.generateTestDataForModelClass(bONPayment2);
		Date Date2 = new Date(dfm.parse("20110302").getTime());
		bONPayment2.setBillingNo(1);
		bONPayment2.setPaymentDate(Date2);
		
		BillingONPayment bONPayment3 = new BillingONPayment();
		EntityDataGenerator.generateTestDataForModelClass(bONPayment3);
		Date Date3 = new Date(dfm.parse("20110502").getTime());
		bONPayment3.setBillingNo(1);
		bONPayment3.setPaymentDate(Date3);
		
		///out of bound dates
		BillingONPayment bONPayment4 = new BillingONPayment();
		EntityDataGenerator.generateTestDataForModelClass(bONPayment4);
		Date Date4 = new Date(dfm.parse("20090502").getTime());
		bONPayment4.setBillingNo(1);
		bONPayment4.setPaymentDate(Date4);
		
		BillingONPayment bONPayment5 = new BillingONPayment();
		EntityDataGenerator.generateTestDataForModelClass(bONPayment5);
		Date Date5 = new Date(dfm.parse("20130502").getTime());
		bONPayment5.setBillingNo(1);
		bONPayment5.setPaymentDate(Date5);
		
		daoBONCH.persist(bONCHeader1);
		dao.persist(bONPayment1);
		dao.persist(bONPayment2);
		dao.persist(bONPayment3);
		
		List<BillingONPayment> result = dao.find3rdPartyPayRecordsByBill(bONCHeader1, startDate, endDate);
		List<BillingONPayment> expectedResult = new ArrayList<BillingONPayment>(Arrays.asList(
				bONPayment1,
				bONPayment2,
				bONPayment3));
		
		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not sorted by Billing Payment Date.");
				fail("Items not sorted by Billing Payment Date.");
			}
		}
		assertTrue(true);
	}
}
