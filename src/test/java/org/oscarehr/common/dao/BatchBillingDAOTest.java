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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.BatchBilling;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class BatchBillingDAOTest extends DaoTestFixtures{
	protected BatchBillingDAO dao = (BatchBillingDAO)SpringUtils.getBean(BatchBillingDAO.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
	Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("batch_billing");
	}
	
	@Test 
	public void testFindIntegerString() throws Exception {
		
		int demoNo1 = 1;
		int demoNo2 = 2;
		String serviceCode1 = "100";
		String serviceCode2 = "200";
		
		BatchBilling bBilling1 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling1);
		bBilling1.setCreateDate(currentTimestamp);
		bBilling1.setDemographicNo(demoNo1);	
		bBilling1.setServiceCode(serviceCode1);
		dao.persist(bBilling1);
		
		BatchBilling bBilling2 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling2);
		bBilling2.setCreateDate(currentTimestamp);
		bBilling2.setDemographicNo(demoNo2);
		bBilling2.setServiceCode(serviceCode2);
		dao.persist(bBilling2);
		
		BatchBilling bBilling3 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling3);
		bBilling3.setCreateDate(currentTimestamp);
		bBilling3.setDemographicNo(demoNo1);	
		bBilling3.setServiceCode(serviceCode1);
		dao.persist(bBilling3);
		
		List<BatchBilling> expectedResult = new ArrayList<BatchBilling>(Arrays.asList(bBilling1, bBilling3));
		List<BatchBilling> result = dao.find(demoNo1, serviceCode1);

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
	public void testFindByProviderString() throws Exception {
		
		String providerNo1 = "1";
		String providerNo2 = "2";
		
		BatchBilling bBilling1 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling1);
		bBilling1.setCreateDate(currentTimestamp);
		bBilling1.setBillingProviderNo(providerNo1);
		dao.persist(bBilling1);
		
		BatchBilling bBilling2 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling2);
		bBilling2.setCreateDate(currentTimestamp);
		bBilling2.setBillingProviderNo(providerNo2);
		dao.persist(bBilling2);
		
		BatchBilling bBilling3 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling3);
		bBilling3.setCreateDate(currentTimestamp);
		bBilling3.setBillingProviderNo(providerNo1);
		dao.persist(bBilling3);
		
		List<BatchBilling> expectedResult = new ArrayList<BatchBilling>(Arrays.asList(bBilling1, bBilling3));
		List<BatchBilling> result = dao.findByProvider(providerNo1);

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
	public void testFindByProviderStringString() throws Exception {
		String serviceCode1 = "101";
		String serviceCode2 = "181";
		String providerNo1 = "1";
		String providerNo2 = "2";
		
		BatchBilling bBilling1 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling1);
		bBilling1.setCreateDate(currentTimestamp);
		bBilling1.setBillingProviderNo(providerNo1);
		bBilling1.setServiceCode(serviceCode1);
		dao.persist(bBilling1);
		
		BatchBilling bBilling2 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling2);
		bBilling2.setCreateDate(currentTimestamp);
		bBilling2.setBillingProviderNo(providerNo2);
		bBilling2.setServiceCode(serviceCode2);
		dao.persist(bBilling2);
		
		BatchBilling bBilling3 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling3);
		bBilling3.setCreateDate(currentTimestamp);
		bBilling3.setBillingProviderNo(providerNo1);
		bBilling3.setServiceCode(serviceCode1);
		dao.persist(bBilling3);
		
		List<BatchBilling> expectedResult = new ArrayList<BatchBilling>(Arrays.asList(bBilling1, bBilling3));		
		List<BatchBilling> result = dao.findByProvider(providerNo1, serviceCode1);
		
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
	public void testFindByServiceCode() throws Exception {
		String serviceCode1 = "101";
		String serviceCode2 = "181";
		
		BatchBilling bBilling1 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling1);
		bBilling1.setCreateDate(currentTimestamp);
		bBilling1.setServiceCode(serviceCode1);
		dao.persist(bBilling1);
		
		BatchBilling bBilling2 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling2);
		bBilling2.setCreateDate(currentTimestamp);
		bBilling2.setServiceCode(serviceCode2);
		dao.persist(bBilling2);
		
		List<BatchBilling> expectedResult = new ArrayList<BatchBilling>(Arrays.asList(bBilling1));		
		List<BatchBilling> result = dao.findByServiceCode(serviceCode1);
		
		assertEquals(expectedResult.get(0), result.get(0));
		
	}

	@Test 
	public void testFindAll() throws Exception {
		String serviceCode1 = "101";
		String serviceCode2 = "181";
		
		BatchBilling bBilling1 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling1);
		bBilling1.setCreateDate(currentTimestamp);
		bBilling1.setServiceCode(serviceCode1);
		dao.persist(bBilling1);
		
		BatchBilling bBilling2 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling2);
		bBilling2.setCreateDate(currentTimestamp);
		bBilling2.setServiceCode(serviceCode2);
		dao.persist(bBilling2);
		
		BatchBilling bBilling3 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling3);
		bBilling3.setCreateDate(currentTimestamp);
		bBilling3.setServiceCode(serviceCode2);
		dao.persist(bBilling3);
		
		List<BatchBilling> expectedResult = new ArrayList<BatchBilling>(Arrays.asList(bBilling1, bBilling2, bBilling3));		
		List<BatchBilling> result = dao.findAll();
		
		Logger logger = MiscUtils.getLogger();
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items do not match.");
				fail("Items do not match.");
			}
		}
		assertTrue(true);
	}

	@Test 
	public void testFindDistinctServiceCodes() throws Exception {
		
		String serviceCode1 = "101";
		String serviceCode2 = "181";
		BatchBilling bBilling1 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling1);
		bBilling1.setCreateDate(currentTimestamp);
		bBilling1.setServiceCode(serviceCode1);
		dao.persist(bBilling1);
		
		BatchBilling bBilling2 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling2);
		bBilling2.setCreateDate(currentTimestamp);
		bBilling2.setServiceCode(serviceCode1);
		dao.persist(bBilling2);
		
		BatchBilling bBilling3 = new BatchBilling();
		EntityDataGenerator.generateTestDataForModelClass(bBilling3);
		bBilling3.setCreateDate(currentTimestamp);
		bBilling3.setServiceCode(serviceCode2);
		dao.persist(bBilling3);
		
		List<String> expectedResult = new ArrayList<String>(Arrays.asList(serviceCode1,serviceCode2));		
		List<String> result = dao.findDistinctServiceCodes();
	
		
		Logger logger = MiscUtils.getLogger();
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items do not match.");
				fail("Items do not match.");
			}
		}
		assertTrue(true);
	}
}