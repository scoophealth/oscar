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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.CaisiFormData;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CaisiFormDataDaoTest extends DaoTestFixtures {
	
	public CaisiFormDataDao dao = SpringUtils.getBean(CaisiFormDataDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("caisi_form_data");
	}

	@Test
	public void testFindByInstanceId() throws Exception {
		
		int instanceId1 = 101;
		int instanceId2 = 202;
		
		CaisiFormData caisiFormData1 = new CaisiFormData();
		EntityDataGenerator.generateTestDataForModelClass(caisiFormData1);
		caisiFormData1.setInstanceId(instanceId1);
		dao.persist(caisiFormData1);
		
		CaisiFormData caisiFormData2 = new CaisiFormData();
		EntityDataGenerator.generateTestDataForModelClass(caisiFormData2);
		caisiFormData2.setInstanceId(instanceId2);
		dao.persist(caisiFormData2);
		
		CaisiFormData caisiFormData3 = new CaisiFormData();
		EntityDataGenerator.generateTestDataForModelClass(caisiFormData3);
		caisiFormData3.setInstanceId(instanceId1);
		dao.persist(caisiFormData3);
		
		List<CaisiFormData> expectedResult = new ArrayList<CaisiFormData>(Arrays.asList(caisiFormData1, caisiFormData3));
		List<CaisiFormData> result = dao.findByInstanceId(instanceId1);

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
	public void testFind() throws Exception {
		
		int instanceId1 = 101, instanceId2 = 202;
		int pageNumber1 = 111, pageNumber2 = 222;
		int sectionId1 = 121, sectionId2 = 131;
		int questionId1 = 1111, questionId2 = 2222;
		
		CaisiFormData caisiFormData1 = new CaisiFormData();
		EntityDataGenerator.generateTestDataForModelClass(caisiFormData1);
		caisiFormData1.setInstanceId(instanceId1);
		caisiFormData1.setPageNumber(pageNumber1);
		caisiFormData1.setSectionId(sectionId1);
		caisiFormData1.setQuestionId(questionId1);
		dao.persist(caisiFormData1);
		
		CaisiFormData caisiFormData2 = new CaisiFormData();
		EntityDataGenerator.generateTestDataForModelClass(caisiFormData2);
		caisiFormData2.setInstanceId(instanceId2);
		caisiFormData2.setPageNumber(pageNumber1);
		caisiFormData2.setSectionId(sectionId1);
		caisiFormData2.setQuestionId(questionId1);
		dao.persist(caisiFormData2);
		
		CaisiFormData caisiFormData3 = new CaisiFormData();
		EntityDataGenerator.generateTestDataForModelClass(caisiFormData3);
		caisiFormData3.setInstanceId(instanceId1);
		caisiFormData3.setPageNumber(pageNumber2);
		caisiFormData3.setSectionId(sectionId1);
		caisiFormData3.setQuestionId(questionId1);
		dao.persist(caisiFormData3);
		
		CaisiFormData caisiFormData4 = new CaisiFormData();
		EntityDataGenerator.generateTestDataForModelClass(caisiFormData4);
		caisiFormData4.setInstanceId(instanceId1);
		caisiFormData4.setPageNumber(pageNumber2);
		caisiFormData4.setSectionId(sectionId2);
		caisiFormData4.setQuestionId(questionId1);
		dao.persist(caisiFormData4);
		
		CaisiFormData caisiFormData5 = new CaisiFormData();
		EntityDataGenerator.generateTestDataForModelClass(caisiFormData5);
		caisiFormData5.setInstanceId(instanceId1);
		caisiFormData5.setPageNumber(pageNumber1);
		caisiFormData5.setSectionId(sectionId1);
		caisiFormData5.setQuestionId(questionId1);
		dao.persist(caisiFormData5);

		CaisiFormData caisiFormData6 = new CaisiFormData();
		EntityDataGenerator.generateTestDataForModelClass(caisiFormData6);
		caisiFormData6.setInstanceId(instanceId1);
		caisiFormData6.setPageNumber(pageNumber1);
		caisiFormData6.setSectionId(sectionId1);
		caisiFormData6.setQuestionId(questionId2);
		dao.persist(caisiFormData6);
		
		List<CaisiFormData> expectedResult = new ArrayList<CaisiFormData>(Arrays.asList(caisiFormData1, caisiFormData5));
		List<CaisiFormData> result = dao.find(instanceId1, pageNumber1, sectionId1, questionId1);

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