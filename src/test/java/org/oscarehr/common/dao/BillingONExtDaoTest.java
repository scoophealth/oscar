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

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingONPayment;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class BillingONExtDaoTest extends DaoTestFixtures {

private BillingONExtDao dao = (BillingONExtDao)SpringUtils.getBean(BillingONExtDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billing_on_ext", "billing_on_payment");
	}
	
	@Test
	/**
	 * Ensures that the getPayment() method returns proper payment data.
	 * @throws Exception
	 */
	public void testGetPaymentMatches() throws Exception {
		BillingONPaymentDao paymentDao = (BillingONPaymentDao)SpringUtils.getBean(BillingONPaymentDao.class);
		BillingONPayment paymentRecord = createPaymentRecord();
		BillingONExt extraBillingPayment = createExtraBillingPayment();
		extraBillingPayment.setKeyVal("payment");
		extraBillingPayment.setValue("10");
		extraBillingPayment.setPaymentId(1);
		
		paymentDao.persist(paymentRecord);
		dao.persist(extraBillingPayment);
		
		BigDecimal payment = dao.getPayment(paymentRecord);
		assertEquals(new BigDecimal("10"), payment);
	}
	
	@Test @Ignore
	/**
	 * Tests if sql query catches 2multiple extra-billing records with the same
	 * payment id, billing number, and payment amount. 
	 * @throws Exception
	 */
	public void testGetPaymentDuplicates() throws Exception {
		BillingONPaymentDao paymentDao = (BillingONPaymentDao)SpringUtils.getBean(BillingONPaymentDao.class);
		BillingONPayment paymentRecord = createPaymentRecord();
		BillingONExt extraBillingPayment = createExtraBillingPayment();
		
		// Create 2 identical BillingOnExt objects
		extraBillingPayment.setKeyVal("payment");
		extraBillingPayment.setValue("10000");
		extraBillingPayment.setPaymentId(1);
		
		BillingONExt extraBillingPayment2 = createExtraBillingPayment();
		extraBillingPayment2.setKeyVal("payment");
		extraBillingPayment2.setValue("10000");
		extraBillingPayment2.setPaymentId(1);
		
		paymentDao.persist(paymentRecord);
		dao.persist(extraBillingPayment);
		dao.persist(extraBillingPayment2);
		
		BigDecimal payment = dao.getPayment(paymentRecord);
		Logger logger = MiscUtils.getLogger();
		logger.info("BEFORE READING LOG");
		
		// Working on a way to test if "Multiple payments found for Payment Id:"
		// appears in the logger.
	}
	
	
	
	/**
	 * Helper method that creates a new BillingONExt object and adds data. 
	 * @return BillingONExt
	 * @throws Exception
	 */
	public BillingONExt createExtraBillingPayment() throws Exception {
		BillingONExt bExt= new BillingONExt();
		EntityDataGenerator.generateTestDataForModelClass(bExt);
		bExt.setBillingNo(1);
		return bExt;
	}
	
	
	
	/**
	 * Helper method that creates a new BillingONPayment object and adds data. 
	 * @return BillingONPayment
	 * @throws Exception
	 */
	public BillingONPayment createPaymentRecord() throws Exception {
		BillingONPayment payment = new BillingONPayment();
		payment.setBillingNo(1);
		return payment;
	}	
}
