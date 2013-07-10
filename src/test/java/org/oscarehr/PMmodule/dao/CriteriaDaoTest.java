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
package org.oscarehr.PMmodule.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.PMmodule.model.Criteria;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CriteriaDaoTest extends DaoTestFixtures {

	public CriteriaDao dao = SpringUtils.getBean(CriteriaDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("criteria");
	}
	
	@Test
	public void testCreate() throws Exception {
		Criteria entity = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testGetCriteriaByTemplateId() throws Exception {
		
		int templateId1 = 101, templateId2 = 202;
		
		Criteria criteria1 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria1);
		criteria1.setTemplateId(templateId1);
		dao.persist(criteria1);
		
		Criteria criteria2 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria2);
		criteria2.setTemplateId(templateId2);
		dao.persist(criteria2);
		
		Criteria criteria3 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria3);
		criteria3.setTemplateId(templateId1);
		dao.persist(criteria3);
		
		Criteria criteria4 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria4);
		criteria4.setTemplateId(templateId2);
		dao.persist(criteria4);
		
		Criteria criteria5 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria5);
		criteria5.setTemplateId(templateId1);
		dao.persist(criteria5);
		
		List<Criteria> expectedResult = new ArrayList<Criteria>(Arrays.asList(criteria1, criteria3, criteria5));
		List<Criteria> result = dao.getCriteriaByTemplateId(templateId1);
		
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
	public void testGetCriteriaByTemplateIdVacancyIdTypeId() throws Exception {
		
		int templateId1 = 101, templateId2 = 202;
		int vacancyId1 = 111, vacancyId2 = 222;
		int typeId1 = 333, typeId2 = 444;
		
		Criteria criteria1 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria1);
		criteria1.setTemplateId(templateId1);
		criteria1.setVacancyId(vacancyId1);
		criteria1.setCriteriaTypeId(typeId1);
		dao.persist(criteria1);
		
		Criteria criteria2 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria2);
		criteria2.setTemplateId(templateId2);
		criteria2.setVacancyId(vacancyId1);
		criteria2.setCriteriaTypeId(typeId2);
		dao.persist(criteria2);
		
		Criteria criteria3 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria3);
		criteria3.setTemplateId(templateId1);
		criteria3.setVacancyId(vacancyId2);
		criteria3.setCriteriaTypeId(typeId1);
		dao.persist(criteria3);
		
		Criteria criteria4 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria4);
		criteria4.setTemplateId(templateId2);
		criteria4.setVacancyId(vacancyId2);
		criteria4.setCriteriaTypeId(typeId1);
		dao.persist(criteria4);
		
		Criteria expectedResult = criteria1;
		Criteria result = dao.getCriteriaByTemplateIdVacancyIdTypeId(templateId1, vacancyId1, typeId1);
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testGetCriteriasByVacancyId() throws Exception {
		
		int vacancyId1 = 101, vacancyId2 = 202;
		
		Criteria criteria1 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria1);
		criteria1.setVacancyId(vacancyId1);
		dao.persist(criteria1);
		
		Criteria criteria2 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria2);
		criteria2.setVacancyId(vacancyId2);
		dao.persist(criteria2);
		
		Criteria criteria3 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria3);
		criteria3.setVacancyId(vacancyId1);
		dao.persist(criteria3);
		
		Criteria criteria4 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria4);
		criteria4.setVacancyId(vacancyId2);
		dao.persist(criteria4);
		
		Criteria criteria5 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria5);
		criteria5.setVacancyId(vacancyId1);
		dao.persist(criteria5);
		
		List<Criteria> expectedResult = new ArrayList<Criteria>(Arrays.asList(criteria1, criteria3, criteria5));
		List<Criteria> result = dao.getCriteriasByVacancyId(vacancyId1);
		
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
	public void testGetRefinedCriteriasByVacancyId() throws Exception {

		int vacancyId1 = 101, vacancyId2 = 202;
		int canBeAdhoc1 = 0, canBeAdhoc2 = 11;
		
		Criteria criteria1 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria1);
		criteria1.setVacancyId(vacancyId1);
		criteria1.setCanBeAdhoc(canBeAdhoc2);
		dao.persist(criteria1);
		
		Criteria criteria2 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria2);
		criteria2.setVacancyId(vacancyId2);
		criteria2.setCanBeAdhoc(canBeAdhoc1);
		dao.persist(criteria2);
		
		Criteria criteria3 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria3);
		criteria3.setVacancyId(vacancyId1);
		criteria3.setCanBeAdhoc(canBeAdhoc1);
		dao.persist(criteria3);
		
		Criteria criteria4 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria4);
		criteria4.setVacancyId(vacancyId2);
		criteria4.setCanBeAdhoc(canBeAdhoc2);
		dao.persist(criteria4);
		
		Criteria criteria5 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria5);
		criteria5.setVacancyId(vacancyId1);
		criteria5.setCanBeAdhoc(canBeAdhoc2);
		dao.persist(criteria5);
		
		List<Criteria> expectedResult = new ArrayList<Criteria>(Arrays.asList(criteria1, criteria5));
		List<Criteria> result = dao.getRefinedCriteriasByVacancyId(vacancyId1);
		
		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not match.");
				fail("Items not match.");
			}
		}
		assertTrue(true);
	}
	
	@Test
	public void testGetRefinedCriteriasByTemplateId() throws Exception {
		
		int templateId1 = 101, templateId2 = 202;
		int canBeAdhoc1 = 0, canBeAdhoc2 = 11;
		
		Criteria criteria1 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria1);
		criteria1.setTemplateId(templateId1);
		criteria1.setCanBeAdhoc(canBeAdhoc2);
		dao.persist(criteria1);
		
		Criteria criteria2 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria2);
		criteria2.setTemplateId(templateId2);
		criteria2.setCanBeAdhoc(canBeAdhoc1);
		dao.persist(criteria2);
		
		Criteria criteria3 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria3);
		criteria3.setTemplateId(templateId1);
		criteria3.setCanBeAdhoc(canBeAdhoc1);
		dao.persist(criteria3);
		
		Criteria criteria4 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria4);
		criteria4.setTemplateId(templateId2);
		criteria4.setCanBeAdhoc(canBeAdhoc2);
		dao.persist(criteria4);
		
		Criteria criteria5 = new Criteria();
		EntityDataGenerator.generateTestDataForModelClass(criteria5);
		criteria5.setTemplateId(templateId1);
		criteria5.setCanBeAdhoc(canBeAdhoc2);
		dao.persist(criteria5);
		
		List<Criteria> expectedResult = new ArrayList<Criteria>(Arrays.asList(criteria1, criteria5));
		List<Criteria> result = dao.getRefinedCriteriasByTemplateId(templateId1);
		
		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not match.");
				fail("Items not match.");
			}
		}
		assertTrue(true);
	}
}