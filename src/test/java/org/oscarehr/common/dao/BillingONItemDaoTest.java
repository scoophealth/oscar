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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class BillingONItemDaoTest extends DaoTestFixtures {

	protected BillingONItemDao dao = SpringUtils.getBean(BillingONItemDao.class);
	protected BillingONCHeader1Dao bONCH1Dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billing_on_item","billing_on_cheader1");
	}
	
	@Test
	public void testGetBillingItemByCh1Id() throws Exception {
		
		int ch1Id1 = 101, ch1Id2 = 202;
		
		BillingONItem bONI1 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI1);
		bONI1.setCh1Id(ch1Id2);
		dao.persist(bONI1);
		
		BillingONItem bONI2 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI2);
		bONI2.setCh1Id(ch1Id1);
		dao.persist(bONI2);
		
		BillingONItem bONI3 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI3);
		bONI3.setCh1Id(ch1Id2);
		dao.persist(bONI3);
		
		BillingONItem bONI4 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI4);
		bONI4.setCh1Id(ch1Id1);
		dao.persist(bONI4);
		
		BillingONItem bONI5 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI5);
		bONI5.setCh1Id(ch1Id1);
		dao.persist(bONI5);
		
		List<BillingONItem> expectedResult = new ArrayList<BillingONItem>(Arrays.asList(bONI2, bONI4, bONI5));
		List<BillingONItem> result = dao.getBillingItemByCh1Id(ch1Id1);

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
	public void testGetActiveBillingItemByCh1Id() throws Exception {
		
		int ch1Id1 = 101, ch1Id2 = 202;
		String status1 = "D", status2 = "Active";
		
		BillingONItem bONI1 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI1);
		bONI1.setCh1Id(ch1Id2);
		bONI1.setStatus(status1);
		dao.persist(bONI1);
		
		BillingONItem bONI2 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI2);
		bONI2.setCh1Id(ch1Id1);
		bONI2.setStatus(status2);
		dao.persist(bONI2);
		
		BillingONItem bONI3 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI3);
		bONI3.setCh1Id(ch1Id2);
		bONI3.setStatus(status2);
		dao.persist(bONI3);
		
		BillingONItem bONI4 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI4);
		bONI4.setCh1Id(ch1Id1);
		bONI4.setStatus(status1);
		dao.persist(bONI4);
		
		BillingONItem bONI5 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI5);
		bONI5.setCh1Id(ch1Id1);
		bONI5.setStatus(status2);
		dao.persist(bONI5);
		
		BillingONItem bONI6 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI6);
		bONI6.setCh1Id(ch1Id1);
		bONI6.setStatus(status2);
		dao.persist(bONI6);
	
		List<BillingONItem> expectedResult = new ArrayList<BillingONItem>(Arrays.asList(bONI2, bONI5, bONI6));
		List<BillingONItem> result = dao.getActiveBillingItemByCh1Id(ch1Id1);

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
	public void testGetCh1ByDemographicNo() throws Exception {
		
		Integer demographicNo1 = 101, demographicNo2 = 202;
		
		BillingONCHeader1 bONCH11 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(bONCH11);
		bONCH11.setDemographicNo(demographicNo1);
		bONCH1Dao.persist(bONCH11);
		
		BillingONCHeader1 bONCH12 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(bONCH12);
		bONCH12.setDemographicNo(demographicNo2);
		bONCH1Dao.persist(bONCH12);
		
		BillingONCHeader1 bONCH13 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(bONCH13);
		bONCH13.setDemographicNo(demographicNo1);
		bONCH1Dao.persist(bONCH13);
		
		BillingONCHeader1 bONCH14 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(bONCH14);
		bONCH14.setDemographicNo(demographicNo1);
		bONCH1Dao.persist(bONCH14);
		
		BillingONCHeader1 bONCH15 = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(bONCH15);
		bONCH15.setDemographicNo(demographicNo2);
		bONCH1Dao.persist(bONCH15);
		
		List<BillingONCHeader1> expectedResult = new ArrayList<BillingONCHeader1>(Arrays.asList( bONCH11,  bONCH13,  bONCH14));
		List<BillingONCHeader1> result = dao.getCh1ByDemographicNo(demographicNo1);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).getDemographicNo().equals(result.get(i).getDemographicNo())){
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testFindByCh1Id() throws Exception {
		
		int ch1Id1 = 101, ch1Id2 = 202;
		String status1 = "D", status2 = "N", status3 = "S";
		
		BillingONItem bONI1 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI1);
		bONI1.setCh1Id(ch1Id2);
		bONI1.setStatus(status1);
		dao.persist(bONI1);
		
		BillingONItem bONI2 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI2);
		bONI2.setCh1Id(ch1Id1);
		bONI2.setStatus(status2);
		dao.persist(bONI2);
		
		BillingONItem bONI3 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI3);
		bONI3.setCh1Id(ch1Id2);
		bONI3.setStatus(status2);
		dao.persist(bONI3);
		
		BillingONItem bONI4 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI4);
		bONI4.setCh1Id(ch1Id1);
		bONI4.setStatus(status1);
		dao.persist(bONI4);
		
		BillingONItem bONI5 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI5);
		bONI5.setCh1Id(ch1Id1);
		bONI5.setStatus(status2);
		dao.persist(bONI5);
		
		BillingONItem bONI6 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI6);
		bONI6.setCh1Id(ch1Id1);
		bONI6.setStatus(status2);
		dao.persist(bONI6);
		
		BillingONItem bONI7 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI7);
		bONI7.setCh1Id(ch1Id1);
		bONI7.setStatus(status3);
		dao.persist(bONI7);
		
		BillingONItem bONI8 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI8);
		bONI8.setCh1Id(ch1Id1);
		bONI8.setStatus(status2);
		dao.persist(bONI8);
		
		BillingONItem bONI9 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI9);
		bONI9.setCh1Id(ch1Id2);
		bONI9.setStatus(status2);
		dao.persist(bONI9);
	
		List<BillingONItem> expectedResult = new ArrayList<BillingONItem>(Arrays.asList(bONI2, bONI5, bONI6, bONI8));
		List<BillingONItem> result = dao.findByCh1Id(ch1Id1);

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
	public void testFindByCh1IdAndStatusNotEqual() throws Exception {
		
		int ch1Id1 = 101, ch1Id2 = 202;
		String status1 = "D", status2 = "Active";
		
		BillingONItem bONI1 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI1);
		bONI1.setCh1Id(ch1Id2);
		bONI1.setStatus(status1);
		dao.persist(bONI1);
		
		BillingONItem bONI2 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI2);
		bONI2.setCh1Id(ch1Id1);
		bONI2.setStatus(status2);
		dao.persist(bONI2);
		
		BillingONItem bONI3 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI3);
		bONI3.setCh1Id(ch1Id2);
		bONI3.setStatus(status2);
		dao.persist(bONI3);
		
		BillingONItem bONI4 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI4);
		bONI4.setCh1Id(ch1Id1);
		bONI4.setStatus(status1);
		dao.persist(bONI4);
		
		BillingONItem bONI5 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI5);
		bONI5.setCh1Id(ch1Id1);
		bONI5.setStatus(status2);
		dao.persist(bONI5);
		
		BillingONItem bONI6 = new BillingONItem();
		EntityDataGenerator.generateTestDataForModelClass(bONI6);
		bONI6.setCh1Id(ch1Id1);
		bONI6.setStatus(status2);
		dao.persist(bONI6);
	
		List<BillingONItem> expectedResult = new ArrayList<BillingONItem>(Arrays.asList(bONI2, bONI5, bONI6));
		List<BillingONItem> result = dao.findByCh1IdAndStatusNotEqual(ch1Id1, status1);

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