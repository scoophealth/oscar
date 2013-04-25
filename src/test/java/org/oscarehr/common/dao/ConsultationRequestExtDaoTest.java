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
import org.oscarehr.common.model.ConsultationRequestExt;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ConsultationRequestExtDaoTest extends DaoTestFixtures {

	protected ConsultationRequestExtDao dao = (ConsultationRequestExtDao)SpringUtils.getBean(ConsultationRequestExtDao.class);
	Logger logger = MiscUtils.getLogger();
	
	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("consultationRequestExt");
	}

	@Test
	/**
	 * Ensures that only the Consultation Request Ext items
	 * with the specified request ID are selected.
	 * @throws Exception
	 */
	public void testGetConsultationRequestExts() throws Exception {
		int requestId = 10;
		
		ConsultationRequestExt conReqExt1 = new ConsultationRequestExt();
		EntityDataGenerator.generateTestDataForModelClass(conReqExt1);
		conReqExt1.setRequestId(requestId);
		
		// Wrong request ID
		ConsultationRequestExt conReqExt2 = new ConsultationRequestExt();
		EntityDataGenerator.generateTestDataForModelClass(conReqExt2);
		conReqExt2.setRequestId(9999);
		
		ConsultationRequestExt conReqExt3 = new ConsultationRequestExt();
		EntityDataGenerator.generateTestDataForModelClass(conReqExt3);
		conReqExt3.setRequestId(requestId);
		
		dao.persist(conReqExt1);
		dao.persist(conReqExt2);
		dao.persist(conReqExt3);
		
		List<ConsultationRequestExt> result = dao.getConsultationRequestExts(requestId); 
		List<ConsultationRequestExt> expectedResult = new ArrayList<ConsultationRequestExt>(Arrays.asList(
				conReqExt1,
				conReqExt3
				));

		assertTrue(result.size() == expectedResult.size());
		assertTrue(result.containsAll(expectedResult));
	}

	@Test
	/**
	 * Ensures that the value is retrieved only on records where
	 * the request ID and key matches.
	 * @throws Exception
	 */
	public void testGetConsultationRequestExtsByKey() throws Exception {
		int requestId = 10;
		String key = "password";
		
		ConsultationRequestExt conReqExt1 = new ConsultationRequestExt();
		EntityDataGenerator.generateTestDataForModelClass(conReqExt1);
		conReqExt1.setRequestId(requestId);
		conReqExt1.setKey(key);
		conReqExt1.setValue("value1");
		
		// Wrong request ID
		ConsultationRequestExt conReqExt2 = new ConsultationRequestExt();
		EntityDataGenerator.generateTestDataForModelClass(conReqExt2);
		conReqExt2.setRequestId(9999);
		conReqExt2.setKey(key);
		conReqExt2.setValue("value2");
		
		// Wrong key
		ConsultationRequestExt conReqExt3 = new ConsultationRequestExt();
		EntityDataGenerator.generateTestDataForModelClass(conReqExt3);
		conReqExt3.setRequestId(requestId);
		conReqExt3.setKey("wrongKey");
		conReqExt3.setValue("value3");
		
		ConsultationRequestExt conReqExt4 = new ConsultationRequestExt();
		EntityDataGenerator.generateTestDataForModelClass(conReqExt4);
		conReqExt4.setRequestId(requestId);
		conReqExt4.setKey(key);
		conReqExt4.setValue("value4");
		
		dao.persist(conReqExt1);
		dao.persist(conReqExt2);
		dao.persist(conReqExt3);
		dao.persist(conReqExt4);
		
		String expectedResult = "value1";
		String result = dao.getConsultationRequestExtsByKey(requestId, key);
		
		assertEquals(expectedResult, result);
	}

}
