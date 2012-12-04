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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.CdsClientFormData;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CdsClientFormDataDaoTest extends DaoTestFixtures {

	protected CdsClientFormDataDao dao = (CdsClientFormDataDao)SpringUtils.getBean(CdsClientFormDataDao.class);
	Logger logger = MiscUtils.getLogger();
	
	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("CdsClientFormData");
	}

	@Test
	public void testFindByQuestion() throws Exception {
		int cdsClientFormId = 10;
		String question = "Test question";
		
		CdsClientFormData formData1 = new CdsClientFormData();
		EntityDataGenerator.generateTestDataForModelClass(formData1);
		formData1.setCdsClientFormId(100);
		formData1.setQuestion("Another question");
		formData1.setAnswer("Test answer");
		
		CdsClientFormData formData2 = new CdsClientFormData();
		EntityDataGenerator.generateTestDataForModelClass(formData2);
		formData2.setCdsClientFormId(cdsClientFormId);
		formData2.setQuestion(question);
		formData2.setAnswer("Test answer");
		
		CdsClientFormData formData3 = new CdsClientFormData();
		EntityDataGenerator.generateTestDataForModelClass(formData3);
		formData3.setCdsClientFormId(cdsClientFormId);
		formData3.setQuestion(question);
		formData3.setAnswer("Test answer");
		
		dao.persist(formData1);
		dao.persist(formData2);
		dao.persist(formData3);
		
		List<CdsClientFormData> result = dao.findByQuestion(cdsClientFormId, question);
		List<CdsClientFormData> expectedResult = new ArrayList<CdsClientFormData>(Arrays.asList(
				formData2,
				formData3
				));
		
		assertTrue(result.size() == expectedResult.size());
		assertTrue(result.containsAll(expectedResult));
	}

	@Test
	public void testFindByAnswer() throws Exception {
		int cdsClientFormId = 10;
		String answer = "Test answer";
		
		CdsClientFormData formData1 = new CdsClientFormData();
		EntityDataGenerator.generateTestDataForModelClass(formData1);
		formData1.setCdsClientFormId(100);
		formData1.setAnswer("Another answer");
		
		CdsClientFormData formData2 = new CdsClientFormData();
		EntityDataGenerator.generateTestDataForModelClass(formData2);
		formData2.setCdsClientFormId(cdsClientFormId);
		formData2.setAnswer(answer);
		
		dao.persist(formData1);
		dao.persist(formData2);
		
		CdsClientFormData result = dao.findByAnswer(cdsClientFormId, answer);
		CdsClientFormData expectedResult = formData2;
		
		assertEquals(result, expectedResult);
	}

}
