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
import org.oscarehr.common.model.CaisiFormQuestion;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CaisiFormQuestionDaoTest extends DaoTestFixtures {

	public CaisiFormQuestionDao dao = SpringUtils.getBean(CaisiFormQuestionDao.class);
	
	@Before
	public void before() throws Exception {
		
		SchemaUtils.restoreTable("caisi_form_question");
	}
	
	@Test
	public void testFindByFormId() throws Exception {
		
		int formId1 = 101, formId2 = 202;
		
		CaisiFormQuestion cFQ1 = new CaisiFormQuestion();
		EntityDataGenerator.generateTestDataForModelClass(cFQ1);
		cFQ1.setFormId(formId1);
		dao.persist(cFQ1);
		
		CaisiFormQuestion cFQ2 = new CaisiFormQuestion();
		EntityDataGenerator.generateTestDataForModelClass(cFQ2);
		cFQ2.setFormId(formId2);
		dao.persist(cFQ2);
		
		CaisiFormQuestion cFQ3 = new CaisiFormQuestion();
		EntityDataGenerator.generateTestDataForModelClass(cFQ3);
		cFQ3.setFormId(formId1);
		dao.persist(cFQ3);
		
		List<CaisiFormQuestion> expectedResult = new ArrayList<CaisiFormQuestion>(Arrays.asList(cFQ1, cFQ3));
		List<CaisiFormQuestion> result = dao.findByFormId(formId1);

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