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

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.StudyData;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class StudyDataDaoTest extends DaoTestFixtures {
	
	protected StudyDataDao dao = (StudyDataDao)SpringUtils.getBean(StudyDataDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("studydata");
	}
	
	@Test
	@Ignore
	public void testFindByContent() throws Exception {
		
		String content1 = "alpha", content2 = "bravo";
		
		StudyData studyData1 = new StudyData();
		EntityDataGenerator.generateTestDataForModelClass(studyData1);
		studyData1.setContent(content1);
		dao.persist(studyData1);
		
		StudyData studyData2 = new StudyData();
		EntityDataGenerator.generateTestDataForModelClass(studyData2);
		studyData2.setContent(content2);
		dao.persist(studyData2);
		
		StudyData studyData3 = new StudyData();
		EntityDataGenerator.generateTestDataForModelClass(studyData3);
		studyData3.setContent(content1);
		dao.persist(studyData3);
		
		StudyData studyData4 = new StudyData();
		EntityDataGenerator.generateTestDataForModelClass(studyData4);
		studyData4.setContent(content1);
		dao.persist(studyData4);
		
		List<StudyData> result = dao.findByContent(content1);
		List<StudyData> expectedResult = new ArrayList<StudyData>(Arrays.asList(studyData1, studyData3, studyData4));
		
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
	@Ignore
	public void testFindSingleByContent() throws Exception {
		
		String content1 = "alpha", content2 = "bravo";
		
		StudyData studyData1 = new StudyData();
		EntityDataGenerator.generateTestDataForModelClass(studyData1);
		studyData1.setContent(content1);
		dao.persist(studyData1);
		
		StudyData studyData2 = new StudyData();
		EntityDataGenerator.generateTestDataForModelClass(studyData2);
		studyData2.setContent(content2);
		dao.persist(studyData2);
		
		StudyData studyData3 = new StudyData();
		EntityDataGenerator.generateTestDataForModelClass(studyData3);
		studyData3.setContent(content1);
		dao.persist(studyData3);
		
		StudyData expectedResult = studyData2;
		StudyData result = dao.findSingleByContent(content2);
		
		assertEquals(expectedResult, result);
	}

	@Test
	public void testFindByDemoAndStudy() throws Exception {
		
		int demoNo1 = 100;
		int demoNo2 = 200;
		
		int studyNo1 = 111;
		int studyNo2 = 222;
		
		StudyData studyData1 = new StudyData();
		EntityDataGenerator.generateTestDataForModelClass(studyData1);
		studyData1.setDeleted(false);
		studyData1.setDemographicNo(demoNo1);
		studyData1.setStudyNo(studyNo1);
		dao.persist(studyData1);
		
		StudyData studyData2 = new StudyData();
		EntityDataGenerator.generateTestDataForModelClass(studyData2);
		studyData2.setDeleted(false);
		studyData2.setDemographicNo(demoNo2);
		studyData2.setStudyNo(studyNo2);
		dao.persist(studyData2);
		
		StudyData studyData3 = new StudyData();
		EntityDataGenerator.generateTestDataForModelClass(studyData3);
		studyData3.setDeleted(false);
		studyData3.setDemographicNo(demoNo1);
		studyData3.setStudyNo(studyNo1);
		dao.persist(studyData3);
		
		List<StudyData> result = dao.findByDemoAndStudy(demoNo1, studyNo1);
		List<StudyData> expectedResult = new ArrayList<StudyData>(Arrays.asList(studyData1, studyData3));
		
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
