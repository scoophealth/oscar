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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.oscarehr.common.model.BillingService;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class BillingServiceDaoTest extends DaoTestFixtures {

	protected BillingServiceDao dao = (BillingServiceDao) SpringUtils.getBean(BillingServiceDao.class);
	protected DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
	Logger logger = MiscUtils.getLogger();

	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("billingservice", "billingperclimit", "ctl_billingservice", "wcb", "billing");
	}

	@Test
	public void getBillingCodeAttrTest() /*throws Exception*/{

		dao.getBillingCodeAttr("A001A");
	}

	@Test
	/**
	 * Ensures that the codeRequiresSLI function returns true when
	 * there is a record with a matching service code, and sli flag is true.
	 * @throws Exception
	 */
	public void testCodeRequiresSLI_SCMatches_SLITrue() throws Exception {
		String serviceCode = "service001";
		BillingService billingService = new BillingService();
		EntityDataGenerator.generateTestDataForModelClass(billingService);
		billingService.setServiceCode(serviceCode);
		billingService.setSliFlag(true);

		dao.persist(billingService);
		assertTrue(dao.codeRequiresSLI(serviceCode));
	}

	@Test
	/**
	 * Ensures that the codeRequiresSLI functions returns false when
	 * there are no records with a matching service code. SLI flag is true.
	 * @throws Exception
	 */
	public void testCodeRequiresSLI_SCMismatch_SLITrue() throws Exception {
		String serviceCode = "service001";
		BillingService billingService = new BillingService();
		EntityDataGenerator.generateTestDataForModelClass(billingService);
		billingService.setServiceCode("service002");
		billingService.setSliFlag(true);

		dao.persist(billingService);
		assertFalse(dao.codeRequiresSLI(serviceCode));
	}

	@Test
	/**
	 * Ensures that the codeRequiresSLI function returns false when
	 * there is a record with a matching service code, and sli flag is false.
	 * @throws Exception
	 */
	public void testCodeRequiresSLI_SCMatches_SLIFalse() throws Exception {
		String serviceCode = "service001";
		BillingService billingService = new BillingService();
		EntityDataGenerator.generateTestDataForModelClass(billingService);
		billingService.setServiceCode(serviceCode);
		billingService.setSliFlag(false);

		dao.persist(billingService);
		assertFalse(dao.codeRequiresSLI(serviceCode));
	}

	@Test
	/**
	 * Ensures that the codeRequiresSLI function returns false when
	 * there are no recrods with a matching service code, and sli flag is false.
	 * @throws Exception
	 */
	public void testCodeRequiresSLI_SCMismatche_SLIFalse() throws Exception {
		String serviceCode = "service001";
		BillingService billingService = new BillingService();
		EntityDataGenerator.generateTestDataForModelClass(billingService);
		billingService.setServiceCode("service002");
		billingService.setSliFlag(false);

		dao.persist(billingService);
		assertFalse(dao.codeRequiresSLI(serviceCode));
	}

	@Test
	/**
	 * Ensures that the codeRequiresSLI function returns false when
	 * there are no records in the table.
	 */
	public void testCodeRequiresSLI_EmptyTable() {
		String serviceCode = "service001";
		assertFalse(dao.codeRequiresSLI(serviceCode));
	}

	@Test
	/**
	 * Ensures that the findBillingCodesByCode function returns services where
	 * the service code and region matches, and that the function orders them by date.
	 * @throws Exception
	 */
	public void testFindBillingCodesByCode_CodeRegion() throws Exception {
		String serviceCode = "service001";
		String region = "ON";

		// Matching service code; Matching region. Oldest matching billing service.
		BillingService billingService1 = createBillingServiceWithRegion("service001", "ON", "20080101");
		// Matching service code; Non-matching region
		BillingService billingService2 = createBillingServiceWithRegion("service001", "BC", "20010101");
		// Non-matching service code; Matching region
		BillingService billingService3 = createBillingServiceWithRegion("service002", "ON", "20010101");
		// Non-matching service code; Non-Matching region
		BillingService billingService4 = createBillingServiceWithRegion("service002", "BC", "20010101");
		// Matching service code; Matching region. Newest matching billing service.
		BillingService billingService5 = createBillingServiceWithRegion("service001", "ON", "20120101");
		// Matching service code; Matching region. Central matching billing service.
		BillingService billingService6 = createBillingServiceWithRegion("service001", "ON", "20100101");

		dao.persist(billingService1);
		dao.persist(billingService2);
		dao.persist(billingService3);
		dao.persist(billingService4);
		dao.persist(billingService5);
		dao.persist(billingService6);

		List<BillingService> resultList = dao.findBillingCodesByCode(serviceCode, region);
		List<BillingService> expectedList = new ArrayList<BillingService>(Arrays.asList(billingService1, billingService6, billingService5));

		// fail if list sizes aren't the same
		if (resultList.size() != expectedList.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedList.size(); i++) {
			if (!expectedList.get(i).equals(resultList.get(i))) {
				logger.warn("Items not sorted by Billing Service Date.");
				fail("Items not sorted by Billing Service Date.");
			}
		}
		assertTrue(true);
	}

	@Test
	/**
	 * Ensures that the findByServiceCode function returns the services where
	 * the service codes match, and orders them by service date (newest to oldest).
	 * @throws Exception
	 */
	public void testFindByServiceCode() throws Exception {
		String serviceCode = "service001";

		// Matching service code. Oldest matching billing service.
		BillingService billingService1 = createBillingServiceWithRegion("service001", "ON", "20080101");
		// Matching service code. Newest matching billing service.
		BillingService billingService2 = createBillingServiceWithRegion("service001", "ON", "20120101");
		// Matching service code. Central matching billing service.
		BillingService billingService3 = createBillingServiceWithRegion("service001", "ON", "20100101");
		// Non-matching service code.		
		BillingService billingService4 = createBillingServiceWithRegion("service002", "ON", "20100101");

		dao.persist(billingService1);
		dao.persist(billingService2);
		dao.persist(billingService3);
		dao.persist(billingService4);

		List<BillingService> resultList = dao.findByServiceCode(serviceCode);
		List<BillingService> expectedList = new ArrayList<BillingService>(Arrays.asList(billingService2, billingService3, billingService1));

		// fail if list sizes aren't the same
		if (resultList.size() != expectedList.size()) {
			logger.info("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedList.size(); i++) {
			if (!expectedList.get(i).equals(resultList.get(i))) {
				logger.warn("Items not sorted by Billing Service Date.");
				fail("Items not sorted by Billing Service Date Descending.");
			}
		}
		assertTrue(true);
	}

	@Test
	/**
	 * Ensures that the findBillingCodesByCode function (signature: String, String, int)
	 * returns the newest services where the service codes and regions match.
	 * Ensures that the list of services are ordered from newest to oldest.
	 * @throws Exception
	 */
	public void testFindBillingCodesByCode_CodeRegionOrder_UsingOrder() throws Exception {
		String serviceCode = "service001";
		String region = "ON";
		int order = 1;

		// Matching service code and region. Matches date requirement.
		BillingService billingService1 = createBillingServiceWithRegion("service001", "ON", "20090101");
		// Matching service code and region. Outside of date range.
		BillingService billingService2 = createBillingServiceWithRegion("service001", "ON", "20100101");
		// Matching service code. Non-matching region. Matches date requirement.
		BillingService billingService3 = createBillingServiceWithRegion("service001", "BC", "20090101");
		// Non-matching service code. Matching region. Matches date requirement.
		BillingService billingService4 = createBillingServiceWithRegion("service002", "ON", "20090101");
		// Matching service code. Matching region. Matches date requirement.
		BillingService billingService5 = createBillingServiceWithRegion("service001", "ON", "20090102");
		// Matching service code. Matching region. Matches date requirement.
		BillingService billingService6 = createBillingServiceWithRegion("service001", "ON", "20090102");

		dao.persist(billingService1);
		dao.persist(billingService2);
		dao.persist(billingService3);
		dao.persist(billingService4);
		dao.persist(billingService5);
		dao.persist(billingService6);

		List<BillingService> resultList = dao.findBillingCodesByCode(serviceCode, region, order);
		List<BillingService> expectedList = new ArrayList<BillingService>(Arrays.asList(billingService2));

		// fail if list sizes aren't the same
		if (resultList.size() != expectedList.size()) {
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedList.size(); i++) {
			if (!expectedList.get(i).equals(resultList.get(i))) {
				logger.warn("Items not sorted by Billing Service Date.");
				fail("Items not sorted by Billing Service Date Descending.");
			}
		}
		assertTrue(true);
	}

	@Test
	/**
	 * Ensures that the findBillingCodesByCode function (signature: String, String, Date, int)
	 * returns the services where the service codes and regions match.
	 * Ensures that the list of services are ordered from newest to oldest.
	 * @throws Exception
	 */
	public void testFindBillingCodesByCode_CodeRegionDateOrder_UsingOrder() throws Exception {
		String serviceCode = "service001";
		String region = "ON";
		Date date = new Date(dfm.parse("20091231").getTime());
		int order = 1;

		// Matching service code and region. Matches date requirement.
		BillingService billingService1 = createBillingServiceWithRegion("service001", "ON", "20090101");
		// Matching service code and region. Outside of date range.
		BillingService billingService2 = createBillingServiceWithRegion("service001", "ON", "20100101");
		// Matching service code. Non-matching region. Matches date requirement.
		BillingService billingService3 = createBillingServiceWithRegion("service001", "BC", "20090101");
		// Non-matching service code. Matching region. Matches date requirement.
		BillingService billingService4 = createBillingServiceWithRegion("service002", "ON", "20090101");
		// Matching service code. Matching region. Matches date requirement.
		BillingService billingService5 = createBillingServiceWithRegion("service001", "ON", "20090102");
		// Matching service code. Matching region. Matches date requirement.
		BillingService billingService6 = createBillingServiceWithRegion("service001", "ON", "20090102");

		dao.persist(billingService1);
		dao.persist(billingService2);
		dao.persist(billingService3);
		dao.persist(billingService4);
		dao.persist(billingService5);
		dao.persist(billingService6);

		List<BillingService> resultList = dao.findBillingCodesByCode(serviceCode, region, date, order);
		List<BillingService> expectedList = new ArrayList<BillingService>(Arrays.asList(billingService5, billingService6));

		// fail if list sizes aren't the same
		if (resultList.size() != expectedList.size()) {
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedList.size(); i++) {
			if (!expectedList.get(i).equals(resultList.get(i))) {
				logger.warn("Items not sorted by Billing Service Date.");
				fail("Items not sorted by Billing Service Date Descending.");
			}
		}
		assertTrue(true);
	}

	@Test
	/**
	 * Ensures that the findBillingCodesByCode function (signature: String, String, Date, int)
	 * returns the services where the service codes and regions match.
	 * Results should not be ordered from newest to oldest.
	 * @throws Exception
	 */
	public void testFindBillingCodesByCode_CodeRegionDateOrder_NotUsingOrder() throws Exception {
		String serviceCode = "service001";
		String region = "ON";
		Date date = new Date(dfm.parse("20091231").getTime());
		int order = 11;

		// Matching service code and region. Matches date requirement.
		BillingService billingService1 = createBillingServiceWithRegion("service001", "ON", "20090101");
		// Matching service code. Matching region. Matches date requirement.
		BillingService billingService2 = createBillingServiceWithRegion("service001", "ON", "20090101");

		dao.persist(billingService1);
		dao.persist(billingService2);

		List<BillingService> resultList = dao.findBillingCodesByCode(serviceCode, region, date, order);
		List<BillingService> expectedList = new ArrayList<BillingService>(Arrays.asList(billingService1, billingService2));

		// fail if list sizes aren't the same
		if (resultList.size() != expectedList.size()) {
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedList.size(); i++) {
			if (!expectedList.get(i).equals(resultList.get(i))) {
				logger.warn("Items not sorted by Billing Service Date.");
				fail("Items not sorted by Billing Service Date Descending.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testSearchDescBillingCode() throws Exception {
		String serviceCode = "service001";
		String region = "ON";

		// Valid input.
		BillingService billingService1 = createBillingServiceWithDesc("service001", "ON", "20090101", "Service 1 description.");
		// Valid input.
		BillingService billingService2 = createBillingServiceWithDesc("service001", "ON", "20090101", "-----");
		// Invalid description.
		BillingService billingService3 = createBillingServiceWithDesc("service001", "ON", "20090101", "");
		// Invalid description.
		BillingService billingService4 = createBillingServiceWithDesc("service001", "ON", "20090101", "----");
		// Non-matching region.
		BillingService billingService5 = createBillingServiceWithDesc("service001", "BC", "20090101", "Valid desciption.");
		// Non-matching service code.
		BillingService billingService6 = createBillingServiceWithDesc("service002", "ON", "20090101", "Valid desciption.");
		// Valid input.
		BillingService billingService7 = createBillingServiceWithDesc("service001", "ON", "20100101", "Service 7 description.");
		// Valid input.
		BillingService billingService8 = createBillingServiceWithDesc("service001", "ON", "20110101", "Service 8 description.");

		dao.persist(billingService1);
		dao.persist(billingService2);
		dao.persist(billingService3);
		dao.persist(billingService4);
		dao.persist(billingService5);
		dao.persist(billingService6);
		dao.persist(billingService7);
		dao.persist(billingService8);

		String result = dao.searchDescBillingCode(serviceCode, region);
		String expectedResult = "Service 8 description.";
		assertEquals(expectedResult, result);
	}

	@Test
	public void testSearchDescBillingCode_EmptySet() {
		String serviceCode = "service001";
		String region = "ON";
		String result = dao.searchDescBillingCode(serviceCode, region);
		String expectedResult = "----";
		assertEquals(expectedResult, result);
	}

	@Test
	public void testSearch() throws Exception {
		String searchString = "service001";
		String region = "ON";
		Date date = new Date(dfm.parse("20091231").getTime());

		// Valid input.
		BillingService billingService1 = createBillingServiceWithDesc("service001", "ON", "20090101", "Service 1 description.");
		// Valid input.
		BillingService billingService2 = createBillingServiceWithDesc("some service", "ON", "20090101", "service001");
		// Date out of range.
		BillingService billingService3 = createBillingServiceWithDesc("service001", "ON", "20100101", "service001");
		// Non-matching region.
		BillingService billingService4 = createBillingServiceWithDesc("service001", "BC", "20090101", "service001");
		// Non-matching description.
		BillingService billingService5 = createBillingServiceWithDesc("service001", "BC", "20090101", "service001   ");

		dao.persist(billingService1);
		dao.persist(billingService2);
		dao.persist(billingService3);
		dao.persist(billingService4);
		dao.persist(billingService5);

		List<BillingService> resultList = dao.search(searchString, region, date);
		List<BillingService> expectedList = new ArrayList<BillingService>(Arrays.asList(billingService1, billingService2));

		// fail if list sizes aren't the same
		if (resultList.size() != expectedList.size()) {
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedList.size(); i++) {
			if (!expectedList.get(i).equals(resultList.get(i))) {
				logger.warn("Items not sorted by Billing Service Date.");
				fail("Items not sorted by Billing Service Date Descending.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testSearch_EmptySet() throws Exception {
		String searchString = "service001";
		String region = "ON";
		Date date = new Date(dfm.parse("20091231").getTime());

		List<BillingService> resultList = dao.search(searchString, region, date);
		List<BillingService> expectedList = new ArrayList<BillingService>();

		assertEquals(expectedList, resultList);
	}

	@Test
	public void testSearch_NotNull() throws Exception {
		String searchString = "service001";
		String region = "ON";
		Date date = new Date(dfm.parse("20091231").getTime());
		List<BillingService> resultList = dao.search(searchString, region, date);
		assertNotNull(resultList);
	}

	@Test
	public void testSearchBillingCode_QueryRegion() throws Exception {
		String searchString = "service001";
		String region = "ON";

		// Valid input.
		BillingService billingService1 = createBillingServiceWithRegion("service001", "ON", "20070101");
		// Valid input.
		BillingService billingService2 = createBillingServiceWithRegion("service0012345", "ON", "20080101");
		// Valid input.
		BillingService billingService3 = createBillingServiceWithRegion("service001 test", "ON", "20090101");
		// Latest valid input.
		BillingService billingService4 = createBillingServiceWithRegion("service001", "ON", "20100101");
		// Non-matching region.
		BillingService billingService5 = createBillingServiceWithRegion("service001", "BC", "20090101");

		dao.persist(billingService1);
		dao.persist(billingService2);
		dao.persist(billingService3);
		dao.persist(billingService4);
		dao.persist(billingService5);

		BillingService expectedResult = billingService4;
		BillingService result = dao.searchBillingCode(searchString, region);

		assertEquals(expectedResult, result);
	}

	@Test
	public void testSearchBillingCode_QueryRegionDate() throws Exception {
		String searchString = "service001";
		String region = "ON";
		Date date = new Date(dfm.parse("20091231").getTime());

		// Valid input.
		BillingService billingService1 = createBillingServiceWithRegion("service001", "ON", "20070101");
		// Valid input.
		BillingService billingService2 = createBillingServiceWithRegion("service0012345", "ON", "20080101");
		// Valid input.
		BillingService billingService3 = createBillingServiceWithRegion("service001 test", "ON", "20090101");
		// Date out of range.
		BillingService billingService4 = createBillingServiceWithRegion("service001", "ON", "20100101");
		// Non-matching region.
		BillingService billingService5 = createBillingServiceWithRegion("service001", "BC", "20090101");

		dao.persist(billingService1);
		dao.persist(billingService2);
		dao.persist(billingService3);
		dao.persist(billingService4);
		dao.persist(billingService5);

		BillingService expectedResult = billingService3;
		BillingService result = dao.searchBillingCode(searchString, region, date);

		assertEquals(expectedResult, result);
	}

	@Test
	public void testSearchBillingCode_Null() throws Exception {
		String searchString = "service001";
		String region = "ON";
		Date date = new Date(dfm.parse("20091231").getTime());
		BillingService result = dao.searchBillingCode(searchString, region, date);
		assertNull(result);
	}

	//	@Test @Ignore
	//	/**
	//	 * Test ignored because expected result is not actual result.
	//	 * Test will fail as is.
	//	 * @throws Exception
	//	 */
	//	public void testSearchPrivateBillingCode() throws Exception {
	//		String searchString = "protected01";
	//		Date date = new Date(dfm.parse("20091231").getTime());
	//
	//		// Valid input.
	//		BillingService billingService1 = createBillingService("\\_" + searchString, "20070101");
	//		billingService1.setRegion(null);
	//		// Valid input.
	//		BillingService billingService2 = createBillingService("\\"+ searchString + "test", "20080101");
	//		billingService2.setRegion(null);
	//		// Region not null.
	//		BillingService billingService3 = createBillingServiceWithRegion(searchString, "ON", "20090101");
	//
	//		dao.persist(billingService1);
	//		dao.persist(billingService2);
	//		dao.persist(billingService3);
	//
	//		BillingService result = dao.searchPrivateBillingCode(searchString, date);
	//		BillingService expectedResult = billingService2;
	//
	//		assertEquals(expectedResult, result);
	//	}

	//	@Test @Ignore
	//	/**
	//	 * Test ignored because expected result is not actual result.
	//	 * Test will fail as is.
	//	 * @throws Exception
	//	 */
	//	public void testSearchPrivateBillingCode_WithUnderscore() throws Exception {
	//		String searchString = "_protected01";
	//		Date date = new Date(dfm.parse("20091231").getTime());
	//
	//		// Valid input.
	//		BillingService billingService1 = createBillingService("\\_" + searchString, "20070101");
	//		billingService1.setRegion(null);
	//		// Valid input.
	//		BillingService billingService2 = createBillingService("\\"+ searchString + "test", "20080101");
	//		billingService2.setRegion(null);
	//		// Region not null.
	//		BillingService billingService3 = createBillingServiceWithRegion(searchString, "ON", "20090101");
	//
	//		dao.persist(billingService1);
	//		dao.persist(billingService2);
	//		dao.persist(billingService3);
	//
	//		BillingService result = dao.searchPrivateBillingCode(searchString, date);
	//		BillingService expectedResult = billingService2;
	//
	//		assertEquals(expectedResult, result);
	//	}

	@Test
	public void testSearchPrivateBillingCode_EmptySet() throws Exception {
		String searchString = "_protected01";
		Date date = new Date(dfm.parse("20091231").getTime());
		BillingService result = dao.searchPrivateBillingCode(searchString, date);
		assertNull(result);
	}

	@Test
	public void testEditBillingCodeDesc() throws Exception {
		String description = "New description";
		String value = "value1";
		int codeID = 1;
		String date = "20091231";
		BillingService billingService1 = createBillingService("Some service", date);
		dao.persist(billingService1);
		boolean pass = dao.editBillingCodeDesc(description, value, codeID);
		assertTrue(pass);
		billingService1 = dao.find(1);
		// Since the id of this billing service should be 1,
		// test to see if the editBillingCodeDesc function persisted its own changes.
		assertEquals(description, billingService1.getDescription());
		assertEquals(value, billingService1.getValue());
	}

	@Test
	public void testEditBillingCode() throws Exception {
		String value = "value1";
		int codeID = 1;
		BillingService billingService1 = createBillingService("Some service", "20090101");
		dao.persist(billingService1);
		boolean pass = dao.editBillingCode(value, codeID);
		assertTrue(pass);
		billingService1 = dao.find(1);
		assertEquals(value, billingService1.getValue());
	}

	//	@Test @Ignore
	//	/**
	//	 * Ignored because the InsertBillingCode() method does not
	//	 * populate all columns in the BillingService table with data.
	//	 * Some columns cannot be null.
	//	 * @throws Exception
	//	 */
	//	public void testInsertBillingCode() throws Exception {
	//		String code = "service001";
	//		String date = "20090101";
	//		String description = "New description";
	//		String termDate = "20091231";
	//		String region = "ON";
	//		boolean pass = dao.insertBillingCode(code, date, description, termDate, region);
	//		assertTrue(pass);
	//		BillingService billingService1 = dao.find(1);
	//		if (!billingService1.getServiceCode().equals(code) &&
	//				billingService1.getDescription().equals(description) &&
	//				billingService1.getTerminationDate() == new Date(dfm.parse(termDate).getTime()) &&
	//				billingService1.getBillingserviceDate() == new Date(dfm.parse(date).getTime()) &&
	//				billingService1.getRegion().equals(region)) {
	//			fail("Insert Billing Code did not properly persist data.");
	//		}
	//		assertTrue(true);
	//	}

	@Test
	public void testGetLatestServiceDate() throws Exception {
		Date date = new Date(dfm.parse("20091231").getTime());
		String serviceCode = "service001";
		BillingService billingService1 = createBillingService(serviceCode, "20080101");
		BillingService billingService2 = createBillingService(serviceCode, "20090101");
		BillingService billingService3 = createBillingService(serviceCode, "20100101");

		dao.persist(billingService3);
		dao.persist(billingService2);
		dao.persist(billingService1);

		Date expectedResult = billingService2.getBillingserviceDate();
		Date result = dao.getLatestServiceDate(date, serviceCode);

		assertEquals(expectedResult, result);
	}

	@Test
	public void testGetUnitPrice() throws Exception {
		String serviceCode = "service001";
		String referralDate = "2009-12-31";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		BillingService billingService1 = createBillingService(serviceCode, "00000000");
		Date date1 = sdf.parse("2008-01-01");
		billingService1.setBillingserviceDate(date1);
		billingService1.setValue("value1");
		billingService1.setGstFlag(true);

		BillingService billingService2 = createBillingService(serviceCode, "00000000");
		Date date2 = sdf.parse("2009-01-01");
		billingService2.setBillingserviceDate(date2);
		billingService2.setValue("value2");
		billingService2.setGstFlag(true);

		BillingService billingService3 = createBillingService(serviceCode, "00000000");
		Date date3 = sdf.parse("2010-01-01");
		billingService3.setBillingserviceDate(date3);
		billingService3.setValue("value3");
		billingService3.setGstFlag(true);

		dao.persist(billingService1);
		dao.persist(billingService2);
		dao.persist(billingService3);

		Object[] expectedResult = { "value2", true };
		Object[] result = dao.getUnitPrice(serviceCode, sdf.parse(referralDate));

		// fail if list sizes aren't the same
		if (result.length != expectedResult.length) {
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < result.length; i++) {
			if (!expectedResult[i].equals(result[i])) {
				fail("Objects not equal.");
			}
		}
		assertTrue(true);
	}

	//	@Test @Ignore
	//	/**
	//	 * working on a way to parse log file for exception
	//	 */
	//	public void testGetUnitPrice_ParseException() {
	//		String serviceCode = "service001";
	//		String referralDate = "notadate";
	//		Object[] result = dao.getUnitPrice(serviceCode, referralDate);
	//		// test incomplete
	//	}

	@Test
	public void testGetUnitPrice_EmptySet() throws Exception {
		String serviceCode = "service001";
		String referralDate = "2009-12-31";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Object[] result = dao.getUnitPrice(serviceCode, sdf.parse(referralDate));
		assertNull(result);
	}

	@Test
	public void testGetUnitPercentage() throws Exception {
		String serviceCode = "service001";
		String referralDate = "2009-12-31";
		BillingService billingService1 = createBillingService(serviceCode, "20080101");
		billingService1.setPercentage("20");
		BillingService billingService2 = createBillingService(serviceCode, "20090101");
		billingService2.setPercentage("40");
		BillingService billingService3 = createBillingService(serviceCode, "20100101");
		billingService3.setPercentage("60");
		dao.persist(billingService3);
		dao.persist(billingService2);
		dao.persist(billingService1);
		String expectedResult = "40";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String result = dao.getUnitPercentage(serviceCode, sdf.parse(referralDate));
		assertEquals(expectedResult, result);
	}

	//	@Test @Ignore
	//	/**
	//	 * working on a way to parse log file for exception
	//	 */
	//	public void testGetUnitPercentage_ParseException() {
	//		String serviceCode = "service001";
	//		String referralDate = "notadate";
	//		Object[] result = dao.getUnitPrice(serviceCode, referralDate);
	//		// test incomplete
	//	}

	@Test
	public void testGetUnitPercentage_EmptySet() throws Exception {
		String serviceCode = "service001";
		String referralDate = "2009-12-31";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Object[] result = dao.getUnitPrice(serviceCode, sdf.parse(referralDate));
		assertNull(result);
	}

	@Test
	public void testFindBillingCodesByFontStyle() throws Exception {
		int displayStyle = 1111;
		String serviceCode = "service001";

		BillingService billingService1 = createBillingService(serviceCode, "20080101");
		billingService1.setDisplayStyle(displayStyle);

		BillingService billingService2 = createBillingService(serviceCode, "20090101");
		billingService2.setDisplayStyle(1112);

		BillingService billingService3 = createBillingService(serviceCode, "20100101");
		billingService3.setDisplayStyle(displayStyle);

		dao.persist(billingService1);
		dao.persist(billingService2);
		dao.persist(billingService3);

		List<BillingService> result = dao.findBillingCodesByFontStyle(displayStyle);
		List<BillingService> expectedResult = new ArrayList<BillingService>(Arrays.asList(billingService1, billingService3));

		// fail if list sizes aren't the same
		if (result.size() != expectedResult.size()) {
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items not sorted by Billing Service Date.");
				fail("Items not sorted by Billing Service Date Descending.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testFindBillingCodesByFontStyle_EmptySet() {
		int displayStyle = 1111;
		List<BillingService> result = dao.findBillingCodesByFontStyle(displayStyle);
		assertTrue(result.isEmpty());
	}

	/**
	 * Creates and populates a new BillingService with only the code and date.
	 * @param code Service code
	 * @param date Billing service date
	 * @return BillingService
	 * @throws Exception
	 */
	public BillingService createBillingService(String code, String date) throws Exception {
		BillingService billingService = new BillingService();
		EntityDataGenerator.generateTestDataForModelClass(billingService);
		billingService.setServiceCode(code);
		Date newDate = new Date(dfm.parse(date).getTime());
		billingService.setBillingserviceDate(newDate);
		return billingService;
	}

	/**
	 * Creates and populates a new BillingService with a region.
	 * @param code Service code
	 * @param region Region
	 * @param date Billing service date
	 * @return BillingService
	 * @throws Exception
	 */
	public BillingService createBillingServiceWithRegion(String code, String region, String date) throws Exception {
		BillingService billingService = new BillingService();
		EntityDataGenerator.generateTestDataForModelClass(billingService);
		billingService.setServiceCode(code);
		billingService.setRegion(region);
		Date newDate = new Date(dfm.parse(date).getTime());
		billingService.setBillingserviceDate(newDate);
		return billingService;
	}

	/**
	 * Creates and populates a new BillingService with a region and description.
	 * @param code Service code
	 * @param region Region
	 * @param date Billing service date
	 * @param description Description of the billing service.
	 * @return BillingService
	 * @throws Exception
	 */
	public BillingService createBillingServiceWithDesc(String code, String region, String date, String description) throws Exception {
		BillingService billingService = new BillingService();
		EntityDataGenerator.generateTestDataForModelClass(billingService);
		billingService.setServiceCode(code);
		billingService.setRegion(region);
		Date newDate = new Date(dfm.parse(date).getTime());
		billingService.setBillingserviceDate(newDate);
		billingService.setDescription(description);
		return billingService;
	}

	@Test
	public void testBillingService() {
		List<BillingService> billingServices;
		billingServices = dao.findByRegionGroupAndType("REG", "GRP", "TYP");
		assertNotNull(billingServices);
	}

	@Test
	public void testFindByServiceCodeOrDescription() {
		List<BillingService> bss = dao.findByServiceCodeOrDescription("VOTGVNO");
		assertNotNull(bss);
	}

	@Test
	public void testFindMostRecentByServiceCode() {
		List<BillingService> bss = dao.findMostRecentByServiceCode("VOTGVNO");
		assertNotNull(bss);
	}

	@Test
	public void testFindByServiceCodeAndDate() {
		List<BillingService> bss = dao.findByServiceCodeAndDate("PRSHA", new Date());
		assertNotNull(bss);
	}

	@Test
	public void testFindGsts() {
		List<BillingService> bss = dao.findGst("CODE", new Date());
		assertNotNull(bss);
	}

	@Test
	public void testFindByServiceCodeAndLatestDate() {
		assertNotNull(dao.findByServiceCodeAndLatestDate("CDO", new Date()));
	}

	@Test
	public void testFindBillingCodesByCodeAndTerminationDate() {
		assertNotNull(dao.findBillingCodesByCodeAndTerminationDate("CDE", new Date()));
	}
}
