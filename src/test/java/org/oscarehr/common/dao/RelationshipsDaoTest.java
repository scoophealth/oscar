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
import org.oscarehr.common.model.Relationships;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class RelationshipsDaoTest extends DaoTestFixtures {

	protected RelationshipsDao dao = SpringUtils.getBean(RelationshipsDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("relationships");
	}

	@Test
	public void testFindAll() throws Exception {
		
		int demographicNo1 = 300;
		int demographicNo2 = 100;
		int demographicNo3 = 200;
		
		Relationships relationships1 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships1);
		relationships1.setDemographicNo(demographicNo1);
		dao.persist(relationships1);

		Relationships relationships2 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships2);
		relationships2.setDemographicNo(demographicNo2);
		dao.persist(relationships2);
		
		Relationships relationships3 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships3);
		relationships3.setDemographicNo(demographicNo3);
		dao.persist(relationships3);
		
		List<Relationships> expectedResult = new ArrayList<Relationships>(Arrays.asList(relationships2, relationships3, relationships1));
		List<Relationships> result = dao.findAll();

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
	public void testFindActive() throws Exception {
		
		Relationships relationships1 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships1);
		relationships1.setDeleted(null);
		dao.persist(relationships1);

		Relationships relationships2 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships2);
		relationships2.setDeleted(null);
		dao.persist(relationships2);
		
		Relationships relationships3 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships3);
		relationships3.setDeleted("S");
		dao.persist(relationships3);
		
		Relationships expectedResult = relationships1;
		Relationships result = dao.findActive(1);
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testFindByDemographicNumber() throws Exception {
		
		int demographicNo1 = 101;
		int demographicNo2 = 202;
		
		Relationships relationships1 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships1);
		relationships1.setDemographicNo(demographicNo1);
		relationships1.setDeleted(null);
		dao.persist(relationships1);

		Relationships relationships2 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships2);
		relationships2.setDemographicNo(demographicNo2);
		relationships2.setDeleted(null);
		dao.persist(relationships2);
		
		Relationships relationships3 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships3);
		relationships3.setDemographicNo(demographicNo1);
		relationships3.setDeleted("S");
		dao.persist(relationships3);
		
		Relationships relationships4 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships4);
		relationships4.setDemographicNo(demographicNo1);
		relationships4.setDeleted(null);
		dao.persist(relationships4);
		
		List<Relationships> expectedResult = new ArrayList<Relationships>(Arrays.asList(relationships1, relationships4));
		List<Relationships> result = dao.findByDemographicNumber(demographicNo1);

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
	public void testFindActiveSubDecisionMaker() throws Exception {
		
		int demographicNo1 = 101;
		int demographicNo2 = 202;
		
		String subDecisionMaker1 = "1";
		String subDecisionMaker2 = "FALSE";
		
		Relationships relationships1 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships1);
		relationships1.setDemographicNo(demographicNo1);
		relationships1.setSubDecisionMaker(subDecisionMaker1);
		relationships1.setDeleted(null);
		dao.persist(relationships1);

		Relationships relationships2 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships2);
		relationships2.setDemographicNo(demographicNo2);
		relationships2.setSubDecisionMaker(subDecisionMaker2);
		relationships2.setDeleted(null);
		dao.persist(relationships2);
		
		Relationships relationships3 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships3);
		relationships3.setDemographicNo(demographicNo1);
		relationships3.setSubDecisionMaker(subDecisionMaker1);
		relationships3.setDeleted("S");
		dao.persist(relationships3);
		
		Relationships relationships4 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships4);
		relationships4.setDemographicNo(demographicNo1);
		relationships4.setSubDecisionMaker(subDecisionMaker1);
		relationships4.setDeleted(null);
		dao.persist(relationships4);
		
		List<Relationships> expectedResult = new ArrayList<Relationships>(Arrays.asList(relationships1, relationships4));
		List<Relationships> result = dao.findActiveSubDecisionMaker(demographicNo1);

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
	public void testFindActiveByDemographicNumberAndFacility() throws Exception {
		
		int demographicNo1 = 101;
		int demographicNo2 = 202;
		
		int facilityId1 = 111;
		int facilityId2 = 222;
		
		Relationships relationships1 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships1);
		relationships1.setDemographicNo(demographicNo1);
		relationships1.setFacilityId(facilityId1);
		relationships1.setDeleted(null);
		dao.persist(relationships1);

		Relationships relationships2 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships2);
		relationships2.setDemographicNo(demographicNo2);
		relationships2.setFacilityId(facilityId2);
		relationships2.setDeleted(null);
		dao.persist(relationships2);
		
		Relationships relationships3 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships3);
		relationships3.setDemographicNo(demographicNo1);
		relationships3.setFacilityId(facilityId1);
		relationships3.setDeleted("S");
		dao.persist(relationships3);
		
		Relationships relationships4 = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(relationships4);
		relationships4.setDemographicNo(demographicNo1);
		relationships4.setFacilityId(facilityId1);
		relationships4.setDeleted(null);
		dao.persist(relationships4);
		
		List<Relationships> expectedResult = new ArrayList<Relationships>(Arrays.asList(relationships1, relationships4));
		List<Relationships> result = dao.findActiveByDemographicNumberAndFacility(demographicNo1, facilityId1);

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
}