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
import java.util.Locale;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.RaDetail;
import org.oscarehr.common.model.RaHeader;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class RaDetailDaoTest extends DaoTestFixtures {

	protected RaDetailDao dao = SpringUtils.getBean(RaDetailDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("radetail", "raheader", "provider");
	}

	@Test
	public void testCreate() throws Exception {
		RaDetail d = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(d);
		dao.persist(d);
		assertNotNull(d);
	}

	@Test
	public void testFindByBillingNo() throws Exception {

		int billingNo1 = 101;
		int billingNo2 = 202;

		int raHeaderNo1 = 111;
		int raHeaderNo2 = 222;
		int raHeaderNo3 = 333;
		int raHeaderNo4 = 444;
		int raHeaderNo5 = 555;

		RaDetail raDetail1 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail1);
		raDetail1.setBillingNo(billingNo1);
		raDetail1.setRaHeaderNo(raHeaderNo1);
		dao.persist(raDetail1);

		RaDetail raDetail2 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail2);
		raDetail2.setBillingNo(billingNo2);
		raDetail2.setRaHeaderNo(raHeaderNo2);
		dao.persist(raDetail2);

		RaDetail raDetail3 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail3);
		raDetail3.setBillingNo(billingNo1);
		raDetail3.setRaHeaderNo(raHeaderNo3);
		dao.persist(raDetail3);

		RaDetail raDetail4 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail4);
		raDetail4.setBillingNo(billingNo2);
		raDetail4.setRaHeaderNo(raHeaderNo4);
		dao.persist(raDetail4);

		RaDetail raDetail5 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail5);
		raDetail5.setBillingNo(billingNo1);
		raDetail5.setRaHeaderNo(raHeaderNo5);
		dao.persist(raDetail5);

		List<RaDetail> expectedResult = new ArrayList<RaDetail>(Arrays.asList(raDetail5, raDetail3, raDetail1));
		List<RaDetail> result = dao.findByBillingNo(billingNo1);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testFindByRaHeaderNo() throws Exception {

		int raHeaderNo1 = 111;
		int raHeaderNo2 = 222;

		RaDetail raDetail1 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail1);
		raDetail1.setRaHeaderNo(raHeaderNo1);
		dao.persist(raDetail1);

		RaDetail raDetail2 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail2);
		raDetail2.setRaHeaderNo(raHeaderNo2);
		dao.persist(raDetail2);

		RaDetail raDetail3 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail3);
		raDetail3.setRaHeaderNo(raHeaderNo1);
		dao.persist(raDetail3);

		RaDetail raDetail4 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail4);
		raDetail4.setRaHeaderNo(raHeaderNo2);
		dao.persist(raDetail4);

		RaDetail raDetail5 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail5);
		raDetail5.setRaHeaderNo(raHeaderNo1);
		dao.persist(raDetail5);

		List<RaDetail> expectedResult = new ArrayList<RaDetail>(Arrays.asList(raDetail1, raDetail3, raDetail5));
		List<RaDetail> result = dao.findByRaHeaderNo(raHeaderNo1);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testFindUniqueBillingNoByRaHeaderNoAndProviderAndNotErrorCode() throws Exception {

		int raHeaderNo1 = 111;
		int raHeaderNo2 = 222;

		int billingNo1 = 11;
		int billingNo2 = 12;
		int billingNo3 = 13;
		int billingNo4 = 14;
		int billingNo5 = 15;

		String providerOhipNo1 = "101";
		String providerOhipNo2 = "202";

		String errorCode1 = "al";
		String errorCode2 = "br";
		String errorCode3 = "ch";

		RaDetail raDetail1 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail1);
		raDetail1.setRaHeaderNo(raHeaderNo1);
		raDetail1.setProviderOhipNo(providerOhipNo1);
		raDetail1.setErrorCode(errorCode1);
		raDetail1.setBillingNo(billingNo1);
		dao.persist(raDetail1);

		RaDetail raDetail2 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail2);
		raDetail2.setRaHeaderNo(raHeaderNo2);
		raDetail2.setProviderOhipNo(providerOhipNo1);
		raDetail2.setErrorCode(errorCode2);
		raDetail2.setBillingNo(billingNo2);
		dao.persist(raDetail2);

		RaDetail raDetail3 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail3);
		raDetail3.setRaHeaderNo(raHeaderNo1);
		raDetail3.setProviderOhipNo(providerOhipNo2);
		raDetail3.setErrorCode(errorCode3);
		raDetail3.setBillingNo(billingNo3);
		dao.persist(raDetail3);

		RaDetail raDetail4 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail4);
		raDetail4.setRaHeaderNo(raHeaderNo1);
		raDetail4.setProviderOhipNo(providerOhipNo1);
		raDetail4.setErrorCode(errorCode3);
		raDetail4.setBillingNo(billingNo4);
		dao.persist(raDetail4);

		RaDetail raDetail5 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail5);
		raDetail5.setRaHeaderNo(raHeaderNo1);
		raDetail5.setProviderOhipNo(providerOhipNo1);
		raDetail5.setErrorCode(errorCode3);
		raDetail5.setBillingNo(billingNo5);
		dao.persist(raDetail5);

		List<Integer> expectedResult = new ArrayList<Integer>(Arrays.asList(billingNo4, billingNo5));
		String codes = errorCode1 + "," + errorCode2;
		List<Integer> result = dao.findUniqueBillingNoByRaHeaderNoAndProviderAndNotErrorCode(raHeaderNo1, providerOhipNo1, codes);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testGetRaDetailByDateDateDateLocale() throws Exception {

		RaHeaderDao raHeaderDao = (RaHeaderDao) SpringUtils.getBean(RaHeaderDao.class);

		String paymentDate1 = "20130101";
		String paymentDate2 = "20120101";
		String paymentDate3 = "20110101";
		String paymentDate4 = "20100101";
		String paymentDate5 = "20080101";

		RaHeader raHeader1 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader1);
		raHeader1.setPaymentDate(paymentDate1);
		raHeaderDao.persist(raHeader1);

		RaHeader raHeader2 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader2);
		raHeader2.setPaymentDate(paymentDate2);
		raHeaderDao.persist(raHeader2);

		RaHeader raHeader3 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader3);
		raHeader3.setPaymentDate(paymentDate3);
		raHeaderDao.persist(raHeader3);

		RaHeader raHeader4 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader4);
		raHeader4.setPaymentDate(paymentDate4);
		raHeaderDao.persist(raHeader4);

		RaHeader raHeader5 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader5);
		raHeader5.setPaymentDate(paymentDate5);
		raHeaderDao.persist(raHeader5);

		int billingNo1 = 101;
		int billingNo2 = 202;

		Date startDate = new Date(dfm.parse("20090101").getTime());
		Date endDate = new Date(dfm.parse("20121215").getTime());

		Locale locale = Locale.getDefault();

		RaDetail raDetail1 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail1);
		raDetail1.setRaHeaderNo(raHeader1.getId());
		raDetail1.setBillingNo(billingNo1);
		dao.persist(raDetail1);

		RaDetail raDetail2 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail2);
		raDetail2.setRaHeaderNo(raHeader2.getId());
		raDetail2.setBillingNo(billingNo2);
		dao.persist(raDetail2);

		RaDetail raDetail3 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail3);
		raDetail3.setRaHeaderNo(raHeader3.getId());
		raDetail3.setBillingNo(billingNo1);
		dao.persist(raDetail3);

		RaDetail raDetail4 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail4);
		raDetail4.setRaHeaderNo(raHeader4.getId());
		raDetail4.setBillingNo(billingNo1);
		dao.persist(raDetail4);

		RaDetail raDetail5 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail5);
		raDetail5.setRaHeaderNo(raHeader5.getId());
		raDetail5.setBillingNo(billingNo1);
		dao.persist(raDetail5);

		List<RaDetail> expectedResult = new ArrayList<RaDetail>(Arrays.asList(raDetail2, raDetail3, raDetail4));
		List<RaDetail> result = dao.getRaDetailByDate(startDate, endDate, locale);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testGetRaDetailByDateProviderDateDateLocale() throws Exception {

		RaHeaderDao raHeaderDao = (RaHeaderDao) SpringUtils.getBean(RaHeaderDao.class);

		String paymentDate1 = "20130101";
		String paymentDate2 = "20120101";
		String paymentDate3 = "20110101";
		String paymentDate4 = "20100101";
		String paymentDate5 = "20080101";

		String ohipNo1 = "101";
		String ohipNo2 = "202";

		Provider provider1 = new Provider();
		provider1.setOhipNo(ohipNo1);

		Provider provider2 = new Provider();
		provider2.setOhipNo(ohipNo2);

		RaHeader raHeader1 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader1);
		raHeader1.setPaymentDate(paymentDate1);
		raHeaderDao.persist(raHeader1);

		RaHeader raHeader2 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader2);
		raHeader2.setPaymentDate(paymentDate2);
		raHeaderDao.persist(raHeader2);

		RaHeader raHeader3 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader3);
		raHeader3.setPaymentDate(paymentDate3);
		raHeaderDao.persist(raHeader3);

		RaHeader raHeader4 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader4);
		raHeader4.setPaymentDate(paymentDate4);
		raHeaderDao.persist(raHeader4);

		RaHeader raHeader5 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader5);
		raHeader5.setPaymentDate(paymentDate5);
		raHeaderDao.persist(raHeader5);

		RaHeader raHeader6 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader6);
		raHeader6.setPaymentDate(paymentDate4);
		raHeaderDao.persist(raHeader6);

		int billingNo1 = 101;
		int billingNo2 = 202;

		Date startDate = new Date(dfm.parse("20090101").getTime());
		Date endDate = new Date(dfm.parse("20121215").getTime());

		Locale locale = Locale.getDefault();

		RaDetail raDetail1 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail1);
		raDetail1.setRaHeaderNo(raHeader1.getId());
		raDetail1.setBillingNo(billingNo1);
		raDetail1.setProviderOhipNo(provider1.getOhipNo());
		dao.persist(raDetail1);

		RaDetail raDetail2 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail2);
		raDetail2.setRaHeaderNo(raHeader2.getId());
		raDetail2.setBillingNo(billingNo2);
		raDetail2.setProviderOhipNo(provider1.getOhipNo());
		dao.persist(raDetail2);

		RaDetail raDetail3 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail3);
		raDetail3.setRaHeaderNo(raHeader3.getId());
		raDetail3.setBillingNo(billingNo1);
		raDetail3.setProviderOhipNo(provider1.getOhipNo());
		dao.persist(raDetail3);

		RaDetail raDetail4 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail4);
		raDetail4.setRaHeaderNo(raHeader4.getId());
		raDetail4.setBillingNo(billingNo1);
		raDetail4.setProviderOhipNo(provider1.getOhipNo());
		dao.persist(raDetail4);

		RaDetail raDetail5 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail5);
		raDetail5.setRaHeaderNo(raHeader5.getId());
		raDetail5.setBillingNo(billingNo1);
		raDetail5.setProviderOhipNo(provider1.getOhipNo());
		dao.persist(raDetail5);

		RaDetail raDetail6 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail6);
		raDetail6.setRaHeaderNo(raHeader6.getId());
		raDetail6.setBillingNo(billingNo1);
		raDetail6.setProviderOhipNo(provider2.getOhipNo());
		dao.persist(raDetail6);

		List<RaDetail> expectedResult = new ArrayList<RaDetail>(Arrays.asList(raDetail2, raDetail3, raDetail4));
		List<RaDetail> result = dao.getRaDetailByDate(provider1, startDate, endDate, locale);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testGetRaDetailByClaimNo() throws Exception {

		String claimNo1 = "111";
		String claimNo2 = "222";

		RaDetail raDetail1 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail1);
		raDetail1.setClaimNo(claimNo1);
		dao.persist(raDetail1);

		RaDetail raDetail2 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail2);
		raDetail2.setClaimNo(claimNo2);
		dao.persist(raDetail2);

		RaDetail raDetail3 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail3);
		raDetail3.setClaimNo(claimNo1);
		dao.persist(raDetail3);

		RaDetail raDetail4 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail4);
		raDetail4.setClaimNo(claimNo1);
		dao.persist(raDetail4);

		List<RaDetail> expectedResult = new ArrayList<RaDetail>(Arrays.asList(raDetail1, raDetail3, raDetail4));
		List<RaDetail> result = dao.getRaDetailByClaimNo(claimNo1);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testGetBillingExplanatoryList() throws Exception {

		int billingNo1 = 111;
		int billingNo2 = 222;

		String errorCode1 = "a";
		String errorCode2 = "b";
		String errorCode4 = "d";

		RaDetail raDetail1 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail1);
		raDetail1.setBillingNo(billingNo1);
		raDetail1.setErrorCode(errorCode1);
		dao.persist(raDetail1);

		RaDetail raDetail2 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail2);
		raDetail2.setBillingNo(billingNo2);
		raDetail2.setErrorCode(errorCode2);
		dao.persist(raDetail2);

		RaDetail raDetail3 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail3);
		raDetail3.setBillingNo(billingNo1);
		raDetail3.setErrorCode(errorCode1);
		dao.persist(raDetail3);

		RaDetail raDetail4 = new RaDetail();
		EntityDataGenerator.generateTestDataForModelClass(raDetail4);
		raDetail4.setBillingNo(billingNo2);
		raDetail4.setErrorCode(errorCode4);
		dao.persist(raDetail4);

		List<String> expectedResult = new ArrayList<String>(Arrays.asList(errorCode1));
		List<String> result = dao.getBillingExplanatoryList(billingNo1);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testFindByBillingNoServiceDateAndProviderNo() {
		assertNotNull(dao.findByBillingNoServiceDateAndProviderNo(100, ConversionUtils.toDateString(new Date()), "100"));
	}

	@Test
	public void testFindByBillingNoAndErrorCode() {
		assertNotNull(dao.findByBillingNoAndErrorCode(100, "CODE"));
	}

	@Test
	public void testFindByHeaderAndBillingNos() {
		assertNotNull(dao.findByHeaderAndBillingNos(100, 100));
	}

	@Test
	public void testFindByRaHeaderNoAndServiceCodes() {
		assertNotNull(dao.findByRaHeaderNoAndServiceCodes(100, Arrays.asList(new String[] { "CODE" })));
	}

    @Test
    public void testFindByRaHeaderNoAndProviderOhipNo() {
	    assertNotNull(dao.findByRaHeaderNoAndProviderOhipNo(100, "10"));
    }
}