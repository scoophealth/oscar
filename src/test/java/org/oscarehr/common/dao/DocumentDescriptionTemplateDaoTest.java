/**
 * Copyright (c) 2012- Centre de Medecine Integree
 *
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
 * This software was written for
 * Centre de Medecine Integree, Saint-Laurent, Quebec, Canada to be provided
 * as part of the OSCAR McMaster EMR System
 */

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DocumentDescriptionTemplate;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class DocumentDescriptionTemplateDaoTest extends DaoTestFixtures {

	protected DocumentDescriptionTemplateDao dao = SpringUtils.getBean(DocumentDescriptionTemplateDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("documentDescriptionTemplate");
	}

	@Test
	public void testCreate() throws Exception {
		DocumentDescriptionTemplate entity = new DocumentDescriptionTemplate();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFind() throws Exception {
		
		
                String description1="alpha",description2="bravo",description3="charlie";
                String descriptionShortcut1="alpha",descriptionShortcut2="bravo",descriptionShortcut3="charlie";
                String docType1="alpha", docType2="bravo",docType3="charlie";
                String providerNo1="123456",providerNo2=null,providerNo3="654321";

                int id;
                
		DocumentDescriptionTemplate documentDescriptionTemplate1 = new DocumentDescriptionTemplate();
		EntityDataGenerator.generateTestDataForModelClass(documentDescriptionTemplate1);
                documentDescriptionTemplate1.setDescription(description1);
                documentDescriptionTemplate1.setDescriptionShortcut(descriptionShortcut1);
                documentDescriptionTemplate1.setDocType(docType1);
                documentDescriptionTemplate1.setProviderNo(providerNo1);
		dao.persist(documentDescriptionTemplate1);
                
                DocumentDescriptionTemplate documentDescriptionTemplate2 = new DocumentDescriptionTemplate();
		EntityDataGenerator.generateTestDataForModelClass(documentDescriptionTemplate2);
                documentDescriptionTemplate2.setDescription(description2);
                documentDescriptionTemplate2.setDescriptionShortcut(descriptionShortcut2);
                documentDescriptionTemplate2.setDocType(docType2);
                documentDescriptionTemplate2.setProviderNo(providerNo2);
		dao.persist(documentDescriptionTemplate2);
                
		id = documentDescriptionTemplate2.getId();

                DocumentDescriptionTemplate documentDescriptionTemplate3 = new DocumentDescriptionTemplate();
		EntityDataGenerator.generateTestDataForModelClass(documentDescriptionTemplate3);
                documentDescriptionTemplate1.setDescription(description3);
                documentDescriptionTemplate1.setDescriptionShortcut(descriptionShortcut3);
                documentDescriptionTemplate1.setDocType(docType3);
                documentDescriptionTemplate1.setProviderNo(providerNo3);
		dao.persist(documentDescriptionTemplate3);
                
		DocumentDescriptionTemplate expectedResult = documentDescriptionTemplate2;
		DocumentDescriptionTemplate result = dao.find(id);
                assertEquals(expectedResult, result);
        }                
        
        @Test
	public void testFindByDocTypeAndProviderNo() throws Exception {
		
		
                String description1="alpha",description2="bravo",description3="charlie";
                String descriptionShortcut1="a",descriptionShortcut2="b",descriptionShortcut3="c";
                String docType1="mylab", docType2="bravo",docType3="mylab";
                String providerNo1="123456",providerNo2=null,providerNo3="123456";

		DocumentDescriptionTemplate documentDescriptionTemplate1 = new DocumentDescriptionTemplate();
		EntityDataGenerator.generateTestDataForModelClass(documentDescriptionTemplate1);
                documentDescriptionTemplate1.setDescription(description1);
                documentDescriptionTemplate1.setDescriptionShortcut(descriptionShortcut1);
                documentDescriptionTemplate1.setDocType(docType1);
                documentDescriptionTemplate1.setProviderNo(providerNo1);
		dao.persist(documentDescriptionTemplate1);
                
                DocumentDescriptionTemplate documentDescriptionTemplate2 = new DocumentDescriptionTemplate();
		EntityDataGenerator.generateTestDataForModelClass(documentDescriptionTemplate2);
                documentDescriptionTemplate2.setDescription(description2);
                documentDescriptionTemplate2.setDescriptionShortcut(descriptionShortcut2);
                documentDescriptionTemplate2.setDocType(docType2);
                documentDescriptionTemplate2.setProviderNo(providerNo2);
		dao.persist(documentDescriptionTemplate2);
		

                DocumentDescriptionTemplate documentDescriptionTemplate3 = new DocumentDescriptionTemplate();
		EntityDataGenerator.generateTestDataForModelClass(documentDescriptionTemplate3);
                documentDescriptionTemplate3.setDescription(description3);
                documentDescriptionTemplate3.setDescriptionShortcut(descriptionShortcut3);
                documentDescriptionTemplate3.setDocType(docType3);
                documentDescriptionTemplate3.setProviderNo(providerNo3);
		dao.persist(documentDescriptionTemplate3);
                
		List<DocumentDescriptionTemplate> expectedResult = new ArrayList<DocumentDescriptionTemplate>(Arrays.asList(documentDescriptionTemplate1, documentDescriptionTemplate3));
		List<DocumentDescriptionTemplate> result = dao.findByDocTypeAndProviderNo(docType3, providerNo3);

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