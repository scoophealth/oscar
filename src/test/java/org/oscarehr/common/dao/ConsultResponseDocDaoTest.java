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
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ConsultResponseDoc;
import org.oscarehr.util.SpringUtils;

public class ConsultResponseDocDaoTest extends DaoTestFixtures {

	protected ConsultResponseDocDao dao = (ConsultResponseDocDao)SpringUtils.getBean(ConsultResponseDocDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("consultResponseDoc", "patientLabRouting");
	}

        @Test
        public void testCreate() throws Exception {
                ConsultResponseDoc entity = new ConsultResponseDoc();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);
                assertNotNull(entity.getId());
        }
	
	@Test
	public void testFindByResponseIdDocNoDocType() throws Exception {
		
		int responseId1 = 111;
		int responseId2 = 222;
		
		int documentNo1 = 101;
		int documentNo2 = 202;
		
		String docType1 = "a";
		String docType2 = "b";
		
		ConsultResponseDoc consultDoc1 = new ConsultResponseDoc();
		EntityDataGenerator.generateTestDataForModelClass(consultDoc1);
		consultDoc1.setResponseId(responseId1);
		consultDoc1.setDocumentNo(documentNo1);
		consultDoc1.setDocType(docType1);
		consultDoc1.setDeleted(null);
		dao.persist(consultDoc1);
		
		ConsultResponseDoc consultDoc2 = new ConsultResponseDoc();
		EntityDataGenerator.generateTestDataForModelClass(consultDoc2);
		consultDoc2.setResponseId(responseId2);
		consultDoc2.setDocumentNo(documentNo1);
		consultDoc2.setDocType(docType1);
		consultDoc2.setDeleted(null);
		dao.persist(consultDoc2);
		
		ConsultResponseDoc consultDoc3 = new ConsultResponseDoc();
		EntityDataGenerator.generateTestDataForModelClass(consultDoc3);
		consultDoc3.setResponseId(responseId1);
		consultDoc3.setDocumentNo(documentNo2);
		consultDoc3.setDocType(docType1);
		consultDoc3.setDeleted(null);
		dao.persist(consultDoc3);
		
		ConsultResponseDoc consultDoc4 = new ConsultResponseDoc();
		EntityDataGenerator.generateTestDataForModelClass(consultDoc4);
		consultDoc4.setResponseId(responseId1);
		consultDoc4.setDocumentNo(documentNo1);
		consultDoc4.setDocType(docType2);
		consultDoc4.setDeleted(null);
		dao.persist(consultDoc4);
		
		ConsultResponseDoc consultDoc5 = new ConsultResponseDoc();
		EntityDataGenerator.generateTestDataForModelClass(consultDoc5);
		consultDoc5.setResponseId(responseId1);
		consultDoc5.setDocumentNo(documentNo1);
		consultDoc5.setDocType(docType1);
		consultDoc5.setDeleted("Y");
		dao.persist(consultDoc5);
		
		ConsultResponseDoc expectedResult = consultDoc1;
		ConsultResponseDoc result = dao.findByResponseIdDocNoDocType(responseId1, documentNo1, docType1);

		assertEquals(result, expectedResult);
	}
	
	@Test
	public void testFindByResponseId() throws Exception {
		
		int responseId1 = 2111;
		int responseId2 = 1222;
		
		ConsultResponseDoc consultDoc1 = new ConsultResponseDoc();
		EntityDataGenerator.generateTestDataForModelClass(consultDoc1);
		consultDoc1.setResponseId(responseId1);
		consultDoc1.setDeleted(null);
		dao.persist(consultDoc1);
		
		ConsultResponseDoc consultDoc2 = new ConsultResponseDoc();
		EntityDataGenerator.generateTestDataForModelClass(consultDoc2);
		consultDoc2.setResponseId(responseId2);
		consultDoc2.setDeleted(null);
		dao.persist(consultDoc2);
		
		ConsultResponseDoc consultDoc3 = new ConsultResponseDoc();
		EntityDataGenerator.generateTestDataForModelClass(consultDoc3);
		consultDoc3.setResponseId(responseId1);
		consultDoc3.setDeleted(null);
		dao.persist(consultDoc3);
		
		ConsultResponseDoc consultDoc4 = new ConsultResponseDoc();
		EntityDataGenerator.generateTestDataForModelClass(consultDoc4);
		consultDoc4.setResponseId(responseId1);
		consultDoc4.setDeleted("Y");
		dao.persist(consultDoc4);
		
		List<ConsultResponseDoc> expectedResult = new ArrayList<ConsultResponseDoc>(Arrays.asList(consultDoc1, consultDoc3));
		List<ConsultResponseDoc> result = dao.findByResponseId(responseId1);

		assertEquals("Result count does not match.", expectedResult.size(), result.size());
		for (int i = 0; i < expectedResult.size(); i++) {
			assertEquals("Items do not match ["+i+"].", expectedResult.get(i), result.get(i));
		}
	}
}
