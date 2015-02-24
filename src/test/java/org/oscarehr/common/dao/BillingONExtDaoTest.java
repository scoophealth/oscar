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
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingONPayment;
import org.oscarehr.util.SpringUtils;

public class BillingONExtDaoTest extends DaoTestFixtures {

	protected BillingONExtDao dao = (BillingONExtDao)SpringUtils.getBean(BillingONExtDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billing_on_ext", "billing_on_payment", "billing_on_cheader1");
	}
	
	@Test
	/**
	 * Ensures that the getPayment() method returns proper payment data.
	 * @throws Exception
	 */
	public void testGetPaymentReturnsValidData() {
		BillingONPaymentDao paymentDao = (BillingONPaymentDao)SpringUtils.getBean(BillingONPaymentDao.class);
		BillingONPayment paymentRecord = new BillingONPayment();
		paymentRecord.setBillingNo(1);
		paymentRecord.setPaymentDate(new Date());
		
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setBillingNo(1);
		extraBillingPayment.setKeyVal("payment");
		extraBillingPayment.setValue("10");
		extraBillingPayment.setPaymentId(1);
	
		paymentDao.persist(paymentRecord);
		dao.persist(extraBillingPayment);
		
		BigDecimal payment = dao.getPayment(paymentRecord);
		assertEquals(new BigDecimal("10"), payment);
	}
	
//	@Test @Ignore
//	/**
//	 * Tests if getPayment() method catches multiple extra-billing records with the same
//	 * payment id, billing number, and payment amount.
//	 * 
//	 * Working on a way to test if "Multiple payments found for Payment Id:"
//	 * appears in the logger.
//	 * @throws Exception
//	 */
//	public void testGetPaymentDuplicates() {
//		BillingONPaymentDao paymentDao = (BillingONPaymentDao)SpringUtils.getBean(BillingONPaymentDao.class);
//		BillingONPayment paymentRecord = new BillingONPayment();
//		paymentRecord.setBillingNo(1);
//		
//		// Create 2 identical BillingOnExt objects
//		BillingONExt extraBillingPayment = new BillingONExt();
//		extraBillingPayment.setBillingNo(1);
//		extraBillingPayment.setKeyVal("payment");
//		extraBillingPayment.setValue("10000");
//		extraBillingPayment.setPaymentId(1);
//		
//		BillingONExt extraBillingPayment2 = new BillingONExt();
//		extraBillingPayment2.setBillingNo(1);
//		extraBillingPayment2.setKeyVal("payment");
//		extraBillingPayment2.setValue("10000");
//		extraBillingPayment2.setPaymentId(1);
//		
//		paymentDao.persist(paymentRecord);
//		dao.persist(extraBillingPayment);
//		dao.persist(extraBillingPayment2);
//		
//		BigDecimal payment = dao.getPayment(paymentRecord);
//		Logger logger = MiscUtils.getLogger();
//		
//		// test incomplete
//	}
	
	
	@Test
	/**
	 * Ensures that if there are no extra-billing records 
	 * with the same paymentID, billing number, and keyval as
	 * the given payment record, the getPayment method returns an amount of $0.00
	 * @throws Exception
	 */
	public void testGetPaymentEmptyListOfPayments() {
		BillingONPaymentDao paymentDao = (BillingONPaymentDao)SpringUtils.getBean(BillingONPaymentDao.class);
		BillingONPayment paymentRecord = new BillingONPayment();
		paymentRecord.setBillingNo(1);
		paymentRecord.setPaymentDate(new Date());
		
		// Create extra billing payment where paymentID, billing number, 
		// and keyval are all different from those of the payment record
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setPaymentId(2);
		extraBillingPayment.setBillingNo(2);
		extraBillingPayment.setKeyVal("notpayment");
		
		paymentDao.persist(paymentRecord);
		dao.persist(extraBillingPayment);
		BigDecimal payment = dao.getPayment(paymentRecord);
		assertEquals(new BigDecimal("0.00"), payment);
	}
	
	
	@Test //(expected = NumberFormatException.class)
	/**
	 * Ensures that only valid integers are acceptable 
	 * for the extra-billing record "value" column.
	 */
	public void testGetPaymentInvalidPaymentValue() {
		BillingONPaymentDao paymentDao = (BillingONPaymentDao)SpringUtils.getBean(BillingONPaymentDao.class);
		BillingONPayment paymentRecord = new BillingONPayment();
		paymentRecord.setBillingNo(1);
		paymentRecord.setPaymentDate(new Date());
		
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setBillingNo(1);
		extraBillingPayment.setKeyVal("payment");
		extraBillingPayment.setValue("abc123");
		extraBillingPayment.setPaymentId(1);
		
		paymentDao.persist(paymentRecord);
		dao.persist(extraBillingPayment);
		
		BigDecimal payment = dao.getPayment(paymentRecord);
		assertEquals(new BigDecimal("0.00"), payment);
	}
	
	
	@Test
	/**
	 * Ensure that the getRefund() method returns the proper refund data.
	 */
	public void testGetRefundReturnsValidData() {
		BillingONPaymentDao paymentDao = (BillingONPaymentDao)SpringUtils.getBean(BillingONPaymentDao.class);
		BillingONPayment paymentRecord = new BillingONPayment();
		paymentRecord.setBillingNo(1);
		paymentRecord.setPaymentDate(new Date());
		
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setBillingNo(1);
		extraBillingPayment.setKeyVal("refund");
		extraBillingPayment.setValue("10");
		extraBillingPayment.setPaymentId(1);
		
		paymentDao.persist(paymentRecord);
		dao.persist(extraBillingPayment);
		
		BigDecimal refund = dao.getRefund(paymentRecord);
		assertEquals(new BigDecimal("10"), refund);
	}
	
	
//	@Test @Ignore
//	/**
//	 * Tests if getRefund() method catches multiple extra-billing records with the same
//	 * payment id, billing number, and payment amount.
//	 * 
//	 * Working on a way to test if expected message appears in the logger.
//	 * @throws Exception
//	 */
//	public void testGetRefundDuplicates() {
//		BillingONPaymentDao paymentDao = (BillingONPaymentDao)SpringUtils.getBean(BillingONPaymentDao.class);
//		BillingONPayment paymentRecord = new BillingONPayment();
//		paymentRecord.setBillingNo(1);
//		
//		// Create 2 identical BillingOnExt objects
//		BillingONExt extraBillingPayment = new BillingONExt();
//		extraBillingPayment.setBillingNo(1);
//		extraBillingPayment.setKeyVal("refund");
//		extraBillingPayment.setValue("10000");
//		extraBillingPayment.setPaymentId(1);
//		
//		BillingONExt extraBillingPayment2 = new BillingONExt();
//		extraBillingPayment2.setBillingNo(1);
//		extraBillingPayment2.setKeyVal("refund");
//		extraBillingPayment2.setValue("10000");
//		extraBillingPayment2.setPaymentId(1);
//		
//		paymentDao.persist(paymentRecord);
//		dao.persist(extraBillingPayment);
//		dao.persist(extraBillingPayment2);
//		
//		BigDecimal refund = dao.getRefund(paymentRecord);
//		Logger logger = MiscUtils.getLogger();
//		
//		// test incomplete
//	}
	
	
	@Test
	/**
	 * Ensures that if there are no extra-billing records 
	 * with the same paymentID, billing number, and keyval as
	 * the given payment record, the getRefund() method returns an amount of $0.00
	 * @throws Exception
	 */
	public void testGetPaymentEmptyListOfRefunds() {
		BillingONPaymentDao paymentDao = (BillingONPaymentDao)SpringUtils.getBean(BillingONPaymentDao.class);
		BillingONPayment paymentRecord = new BillingONPayment();
		paymentRecord.setBillingNo(1);
		paymentRecord.setPaymentDate(new Date());
		
		// Create extra billing payment where paymentID, billing number, 
		// and keyval are all different from those of the payment record
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setPaymentId(2);
		extraBillingPayment.setBillingNo(2);
		extraBillingPayment.setKeyVal("notpayment");
		
		paymentDao.persist(paymentRecord);
		dao.persist(extraBillingPayment);
		
		BigDecimal refund = dao.getRefund(paymentRecord);
		assertEquals(new BigDecimal("0.00"), refund);
	}
	
	
	@Test //(expected = NumberFormatException.class)
	/**
	 * Ensures that only valid integers are acceptable 
	 * for the extra-billing "value" column.
	 */
	public void testGetRefundInvalidPaymentValue() {
		BillingONPaymentDao paymentDao = (BillingONPaymentDao)SpringUtils.getBean(BillingONPaymentDao.class);
		BillingONPayment paymentRecord = new BillingONPayment();
		paymentRecord.setBillingNo(1);
		paymentRecord.setPaymentDate(new Date());
		
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setBillingNo(1);
		extraBillingPayment.setKeyVal("refund");
		extraBillingPayment.setValue("abc123");
		extraBillingPayment.setPaymentId(1);
		
		paymentDao.persist(paymentRecord);
		dao.persist(extraBillingPayment);
		
		BigDecimal refund = dao.getRefund(paymentRecord);
		assertEquals(new BigDecimal("0.00"), refund);
	}
	
	
	@Test
	/**
	 * Ensures that the getRemitTo() method returns expected data.
	 */
	public void testGetRemitToReturnsValidData() throws Exception {
		BillingONCHeader1Dao cHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean(BillingONCHeader1Dao.class);
		BillingONCHeader1 cHeader1 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(cHeader1);
		
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setBillingNo(1);
		extraBillingPayment.setStatus('1');
		extraBillingPayment.setKeyVal("remitTo");
		extraBillingPayment.setPaymentId(1);
		
		cHeader1Dao.persist(cHeader1);
		dao.persist(extraBillingPayment);
		
		BillingONExt billingRecord = dao.getRemitTo(cHeader1);
		assertEquals(extraBillingPayment, billingRecord);
	}
	
	
	@Test
	/**
	 * Ensures that the getRemitTo() method returns
	 * null when the query returns an empty result set.
	 */
	public void testGetRemitToResultsNull() throws Exception {
		BillingONCHeader1Dao cHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean(BillingONCHeader1Dao.class);
		BillingONCHeader1 cHeader1 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(cHeader1);
		
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setBillingNo(1);
		extraBillingPayment.setPaymentId(1);
		
		// setting the status to 'A' will return
		// an empty result set for the sql query
		extraBillingPayment.setStatus('A');
		extraBillingPayment.setKeyVal("remitTo");
		
		cHeader1Dao.persist(cHeader1);
		dao.persist(extraBillingPayment);
		
		BillingONExt billingRecord = dao.getRemitTo(cHeader1);
		assertNull(billingRecord);
	}
	
	
//	@Test @Ignore
//	/**
//	 * Tests if getRemitTo() method catches multiple extra-billing records with the same
//	 * billing number, status, and keyVal values.
//	 * 
//	 * Working on a way to test if expected message appears in the logger.
//	 * @throws Exception
//	 */
//	public void testGetRemitToDuplicates() {
//		BillingONCHeader1Dao cHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean(BillingONCHeader1Dao.class);
//		BillingONCHeader1 cHeader1 = new BillingONCHeader1();
//		
//		// Create 2 identical BillingOnExt objects
//		BillingONExt extraBillingPayment = new BillingONExt();
//		extraBillingPayment.setBillingNo(1);
//		extraBillingPayment.setStatus('1');
//		extraBillingPayment.setKeyVal("remitTo");
//		
//		BillingONExt extraBillingPayment2 = new BillingONExt();
//		extraBillingPayment.setBillingNo(1);
//		extraBillingPayment.setStatus('1');
//		extraBillingPayment.setKeyVal("remitTo");
//		
//		cHeader1Dao.persist(cHeader1);
//		dao.persist(extraBillingPayment);
//		dao.persist(extraBillingPayment2);
//		
//		BillingONExt billingRecord = dao.getRemitTo(cHeader1);
//		Logger logger = MiscUtils.getLogger();
//		
//		// test incomplete
//	}
	
	
	@Test
	/**
	 * Ensures that the getBillTo() method returns expected data.
	 */
	public void testGetBillToReturnsValidData() throws Exception {
		BillingONCHeader1Dao cHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean(BillingONCHeader1Dao.class);
		BillingONCHeader1 cHeader1 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(cHeader1);
		
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setBillingNo(1);
		extraBillingPayment.setStatus('1');
		extraBillingPayment.setKeyVal("billTo");
		extraBillingPayment.setPaymentId(1);
		
		cHeader1Dao.persist(cHeader1);
		dao.persist(extraBillingPayment);
		
		BillingONExt billingRecord = dao.getBillTo(cHeader1);
		assertEquals(extraBillingPayment, billingRecord);
	}
	
	
	@Test
	/**
	 * Ensures that the getBillTo() method returns
	 * null when the query returns an empty result set.
	 */
	public void testGetBillToResultsNull() throws Exception {
		BillingONCHeader1Dao cHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean(BillingONCHeader1Dao.class);
		BillingONCHeader1 cHeader1 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(cHeader1);
		
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setBillingNo(1);
		extraBillingPayment.setPaymentId(1);
		
		// setting the status to 'A' will return
		// an empty result set for the sql query
		extraBillingPayment.setStatus('A');
		extraBillingPayment.setKeyVal("billTo");
		
		cHeader1Dao.persist(cHeader1);
		dao.persist(extraBillingPayment);
		
		BillingONExt billingRecord = dao.getBillTo(cHeader1);
		assertNull(billingRecord);
	}
	
	
//	@Test @Ignore
//	/**
//	 * Tests if the gitBillTo() method catches multiple extra-billing records with the same
//	 * billing number, status, and keyVal values.
//	 * 
//	 * Working on a way to test if expected message appears in the logger.
//	 * @throws Exception
//	 */
//	public void testGetBillToDuplicates() {
//		BillingONCHeader1Dao cHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean(BillingONCHeader1Dao.class);
//		BillingONCHeader1 cHeader1 = new BillingONCHeader1();
//		
//		// Create 2 identical BillingOnExt objects
//		BillingONExt extraBillingPayment = new BillingONExt();
//		extraBillingPayment.setBillingNo(1);
//		extraBillingPayment.setStatus('1');
//		extraBillingPayment.setKeyVal("billTo");
//		
//		BillingONExt extraBillingPayment2 = new BillingONExt();
//		extraBillingPayment.setBillingNo(1);
//		extraBillingPayment.setStatus('1');
//		extraBillingPayment.setKeyVal("billTo");
//		
//		cHeader1Dao.persist(cHeader1);
//		dao.persist(extraBillingPayment);
//		dao.persist(extraBillingPayment2);
//		
//		BillingONExt billingRecord = dao.getBillTo(cHeader1);
//		Logger logger = MiscUtils.getLogger();
//		
//		// test incomplete
//	}
	
	
	@Test
	/**
	 * Ensures that the getBillToInactive() method returns expected data.
	 */
	public void testGetBillToInactiveReturnsValidData() throws Exception {
		BillingONCHeader1Dao cHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean(BillingONCHeader1Dao.class);
		BillingONCHeader1 cHeader1 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(cHeader1);
		
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setBillingNo(1);
		extraBillingPayment.setStatus('0');
		extraBillingPayment.setKeyVal("billTo");
		extraBillingPayment.setPaymentId(1);
		
		cHeader1Dao.persist(cHeader1);
		dao.persist(extraBillingPayment);
		
		BillingONExt billingRecord = dao.getBillToInactive(cHeader1);
		assertEquals(extraBillingPayment, billingRecord);
	}
	
	
	@Test
	/**
	 * Ensures that the getBillToInactive() method returns
	 * null when the query returns an empty result set.
	 */
	public void testGetBillToInactiveResultsNull() throws Exception {
		BillingONCHeader1Dao cHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean(BillingONCHeader1Dao.class);
		BillingONCHeader1 cHeader1 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(cHeader1);
		
		BillingONExt extraBillingPayment = new BillingONExt();
		extraBillingPayment.setBillingNo(1);
		extraBillingPayment.setPaymentId(1);
		
		// setting the status to 'A' will return
		// an empty result set for the sql query
		extraBillingPayment.setStatus('A');
		extraBillingPayment.setKeyVal("billTo");
		
		cHeader1Dao.persist(cHeader1);
		dao.persist(extraBillingPayment);
		
		BillingONExt billingRecord = dao.getBillToInactive(cHeader1);
		assertNull(billingRecord);
	}
	
	@Test
	public void testFind() {
		assertNotNull(dao.find(100, "KEY", new Date(), new Date()));
	}
	
//	@Test @Ignore
//	/**
//	 * Tests if the getBillToInactive() method catches multiple extra-billing
//	 * records with the same billing number, status, and keyVal values.
//	 * 
//	 * Working on a way to test if expected message appears in the logger.
//	 * @throws Exception
//	 */
//	public void testGetBillToInactiveDuplicates() {
//		BillingONCHeader1Dao cHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean(BillingONCHeader1Dao.class);
//		BillingONCHeader1 cHeader1 = new BillingONCHeader1();
//		
//		// Create 2 identical BillingOnExt objects
//		BillingONExt extraBillingPayment = new BillingONExt();
//		extraBillingPayment.setBillingNo(1);
//		extraBillingPayment.setStatus('0');
//		extraBillingPayment.setKeyVal("billTo");
//		
//		BillingONExt extraBillingPayment2 = new BillingONExt();
//		extraBillingPayment.setBillingNo(1);
//		extraBillingPayment.setStatus('0');
//		extraBillingPayment.setKeyVal("billTo");
//		
//		cHeader1Dao.persist(cHeader1);
//		dao.persist(extraBillingPayment);
//		dao.persist(extraBillingPayment2);
//		
//		BillingONExt billingRecord = dao.getBillToInactive(cHeader1);
//		Logger logger = MiscUtils.getLogger();
//		
//		// test incomplete
//	}
}
