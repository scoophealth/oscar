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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ConsultDocsDaoTest extends DaoTestFixtures {

	protected ConsultDocsDao dao = (ConsultDocsDao)SpringUtils.getBean(ConsultDocsDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("consultdocs", "patientLabRouting");
	}

        @Test
        public void testCreate() throws Exception {
                ConsultDocs entity = new ConsultDocs();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);
                assertNotNull(entity.getId());
        }

	@Test
	public void testFindByRequestIdDocNoDocType() throws Exception {
		
		int requestId1 = 111;
		int requestId2 = 222;
		
		int documentNo1 = 101;
		int documentNo2 = 202;
		
		String docType1 = "a";
		String docType2 = "b";
		
		ConsultDocs consultDocs1 = new ConsultDocs();
		EntityDataGenerator.generateTestDataForModelClass(consultDocs1);
		consultDocs1.setRequestId(requestId1);
		consultDocs1.setDocumentNo(documentNo1);
		consultDocs1.setDocType(docType1);
		consultDocs1.setDeleted(null);
		dao.persist(consultDocs1);
		
		ConsultDocs consultDocs2 = new ConsultDocs();
		EntityDataGenerator.generateTestDataForModelClass(consultDocs2);
		consultDocs2.setRequestId(requestId2);
		consultDocs2.setDocumentNo(documentNo1);
		consultDocs2.setDocType(docType1);
		consultDocs2.setDeleted(null);
		dao.persist(consultDocs2);
		
		ConsultDocs consultDocs3 = new ConsultDocs();
		EntityDataGenerator.generateTestDataForModelClass(consultDocs3);
		consultDocs3.setRequestId(requestId1);
		consultDocs3.setDocumentNo(documentNo2);
		consultDocs3.setDocType(docType1);
		consultDocs3.setDeleted(null);
		dao.persist(consultDocs3);
		
		ConsultDocs consultDocs4 = new ConsultDocs();
		EntityDataGenerator.generateTestDataForModelClass(consultDocs4);
		consultDocs4.setRequestId(requestId1);
		consultDocs4.setDocumentNo(documentNo1);
		consultDocs4.setDocType(docType2);
		dao.persist(consultDocs4);
		
		ConsultDocs consultDocs5 = new ConsultDocs();
		EntityDataGenerator.generateTestDataForModelClass(consultDocs5);
		consultDocs5.setRequestId(requestId1);
		consultDocs5.setDocumentNo(documentNo1);
		consultDocs5.setDocType(docType1);
		consultDocs5.setDeleted(null);
		dao.persist(consultDocs5);
		
		ConsultDocs consultDocs6 = new ConsultDocs();
		EntityDataGenerator.generateTestDataForModelClass(consultDocs6);
		consultDocs6.setRequestId(requestId1);
		consultDocs6.setDocumentNo(documentNo1);
		consultDocs6.setDocType(docType1);
		consultDocs6.setDeleted("Y");
		dao.persist(consultDocs6);
		
		List<ConsultDocs> expectedResult = new ArrayList<ConsultDocs>(Arrays.asList(consultDocs1, consultDocs5));
		List<ConsultDocs> result = dao.findByRequestIdDocNoDocType(requestId1, documentNo1, docType1);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. Result: " + result.size());
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
