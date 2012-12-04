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
import org.oscarehr.common.model.QueueDocumentLink;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class QueueDocumentLinkDaoTest extends DaoTestFixtures {

	protected QueueDocumentLinkDao dao = SpringUtils.getBean(QueueDocumentLinkDao.class);

	public QueueDocumentLinkDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("queue_document_link");
	}

        @Test
        public void testCreate() throws Exception {
                QueueDocumentLink entity = new QueueDocumentLink();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);

                assertNotNull(entity.getId());
        }

	@Test
	public void testGetQueueDocLinks() throws Exception {
		
		QueueDocumentLink queueDocLink1 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink1);
		dao.persist(queueDocLink1);
		
		QueueDocumentLink queueDocLink2 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink2);
		dao.persist(queueDocLink2);
		
		QueueDocumentLink queueDocLink3 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink3);
		dao.persist(queueDocLink3);
		
		QueueDocumentLink queueDocLink4 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink4);
		dao.persist(queueDocLink4);
		
		List<QueueDocumentLink> expectedResult = new ArrayList<QueueDocumentLink>(Arrays.asList(queueDocLink1, queueDocLink2, queueDocLink3, queueDocLink4));
		List<QueueDocumentLink> result = dao.getQueueDocLinks();

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
	public void testGetActiveQueueDocLink() throws Exception {
		
		String statusA = "A";
		String statusNotA = "N";
		
		QueueDocumentLink queueDocLink1 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink1);
		queueDocLink1.setStatus(statusA);
		dao.persist(queueDocLink1);
		
		QueueDocumentLink queueDocLink2 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink2);
		queueDocLink2.setStatus(statusNotA);
		dao.persist(queueDocLink2);
		
		QueueDocumentLink queueDocLink3 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink3);
		queueDocLink3.setStatus(statusA);
		dao.persist(queueDocLink3);
		
		QueueDocumentLink queueDocLink4 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink4);
		queueDocLink4.setStatus(statusA);
		dao.persist(queueDocLink4);
		
		List<QueueDocumentLink> expectedResult = new ArrayList<QueueDocumentLink>(Arrays.asList(queueDocLink1, queueDocLink3, queueDocLink4));
		List<QueueDocumentLink> result = dao.getActiveQueueDocLink();

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
	public void testGetQueueFromDocument() throws Exception {
		
		int dId1 = 111;
		int dId2 = 222;
		
		QueueDocumentLink queueDocLink1 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink1);
		queueDocLink1.setDocId(dId1);
		dao.persist(queueDocLink1);
		
		QueueDocumentLink queueDocLink2 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink2);
		queueDocLink2.setDocId(dId2);
		dao.persist(queueDocLink2);
		
		QueueDocumentLink queueDocLink3 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink3);
		queueDocLink3.setDocId(dId1);
		dao.persist(queueDocLink3);
		
		QueueDocumentLink queueDocLink4 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink4);
		queueDocLink4.setDocId(dId1);
		dao.persist(queueDocLink4);
		
		List<QueueDocumentLink> expectedResult = new ArrayList<QueueDocumentLink>(Arrays.asList(queueDocLink1, queueDocLink3, queueDocLink4));
		List<QueueDocumentLink> result = dao.getQueueFromDocument(dId1);

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
	public void testGetDocumentFromQueue() throws Exception {
		
		int qId1 = 111;
		int qId2 = 222;
		
		QueueDocumentLink queueDocLink1 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink1);
		queueDocLink1.setQueueId(qId1);
		dao.persist(queueDocLink1);
		
		QueueDocumentLink queueDocLink2 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink2);
		queueDocLink2.setQueueId(qId2);
		dao.persist(queueDocLink2);
		
		QueueDocumentLink queueDocLink3 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink3);
		queueDocLink3.setQueueId(qId1);
		dao.persist(queueDocLink3);
		
		QueueDocumentLink queueDocLink4 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink4);
		queueDocLink4.setQueueId(qId1);
		dao.persist(queueDocLink4);
		
		List<QueueDocumentLink> expectedResult = new ArrayList<QueueDocumentLink>(Arrays.asList(queueDocLink1, queueDocLink3, queueDocLink4));
		List<QueueDocumentLink> result = dao.getDocumentFromQueue(qId1);

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
	public void testHasQueueBeenLinkedWithDocument() throws Exception {
		
		int qId1 = 111;
		int qId2 = 222;
		
		int dId1 = 101;
		int dId2 = 202;
		
		QueueDocumentLink queueDocLink1 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink1);
		queueDocLink1.setDocId(dId1);
		queueDocLink1.setQueueId(qId1);
		dao.persist(queueDocLink1);
		
		QueueDocumentLink queueDocLink2 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink2);
		queueDocLink2.setDocId(dId2);
		queueDocLink2.setQueueId(qId2);
		dao.persist(queueDocLink2);
		
		QueueDocumentLink queueDocLink3 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink3);
		queueDocLink3.setQueueId(qId1);
		dao.persist(queueDocLink3);
		
		QueueDocumentLink queueDocLink4 = new QueueDocumentLink();
		EntityDataGenerator.generateTestDataForModelClass(queueDocLink4);
		queueDocLink4.setDocId(dId1);
		dao.persist(queueDocLink4);	
		
		boolean expectedResult = true;
		boolean result = dao.hasQueueBeenLinkedWithDocument(dId1, qId1);
		
		assertEquals(expectedResult, result);
	}
}
