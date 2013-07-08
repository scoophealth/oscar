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
import org.oscarehr.common.model.Study;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class StudyDaoTest extends DaoTestFixtures {
	
	protected StudyDao dao = (StudyDao)SpringUtils.getBean(StudyDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("study");
	}
	
	@Test
	public void testFindAll() throws Exception {
		
		Study study1 = new Study();
		EntityDataGenerator.generateTestDataForModelClass(study1);
		dao.persist(study1);
		
		Study study2 = new Study();
		EntityDataGenerator.generateTestDataForModelClass(study2);
		dao.persist(study2);
		
		Study study3 = new Study();
		EntityDataGenerator.generateTestDataForModelClass(study3);
		dao.persist(study3);
		
		List<Study> expectedResult = new ArrayList<Study>(Arrays.asList(study1, study2, study3));
		List<Study> result = dao.findAll();

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
	public void testFindByName() throws Exception {
		
		String studyName1 = "alpha";
		String studyName2 = "bravo";

		Study study1 = new Study();
		EntityDataGenerator.generateTestDataForModelClass(study1);
		study1.setStudyName(studyName1);
		dao.persist(study1);
		
		Study study2 = new Study();
		EntityDataGenerator.generateTestDataForModelClass(study2);
		study1.setStudyName(studyName2);
		dao.persist(study2);
		
		Study expectedResult = study1;		
		Study result = dao.findByName(studyName1);
		
		assertEquals(expectedResult, result);
	}

	@Test
	public void testFindByCurrent1() throws Exception {
		
		int current11 = 101, current12 = 202;
		Study study1 = new Study();
		EntityDataGenerator.generateTestDataForModelClass(study1);
		study1.setCurrent1(current11);
		dao.persist(study1);
		
		Study study2 = new Study();
		EntityDataGenerator.generateTestDataForModelClass(study2);
		study2.setCurrent1(current12);
		dao.persist(study2);
		
		Study study3 = new Study();
		EntityDataGenerator.generateTestDataForModelClass(study3);
		study3.setCurrent1(current11);
		dao.persist(study3);
		
		List<Study> expectedResult = new ArrayList<Study>(Arrays.asList(study1, study3));
		List<Study> result = dao.findByCurrent1(current11);

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
