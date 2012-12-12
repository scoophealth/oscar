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
import org.oscarehr.common.model.TicklerLink;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class TicklerLinkDaoTest extends DaoTestFixtures {

	protected TicklerLinkDao dao = SpringUtils.getBean(TicklerLinkDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("tickler_link");
	}

	@Test
	public void testCreate() throws Exception {
		TicklerLink entity = new TicklerLink();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test 
	public void testGetLinkByTableId() throws Exception {
		
		String tableName1 = "alp";
		String tableName2 = "brv";
		
		long tableId1 = 101;
		long tableId2 = 202;
		
		TicklerLink ticklerLink1 = new TicklerLink();
		EntityDataGenerator.generateTestDataForModelClass(ticklerLink1);
		ticklerLink1.setTableName(tableName1);
		ticklerLink1.setTableId(tableId1);
		dao.persist(ticklerLink1);
		
		TicklerLink ticklerLink2 = new TicklerLink();
		EntityDataGenerator.generateTestDataForModelClass(ticklerLink2);
		ticklerLink2.setTableName(tableName2);
		ticklerLink2.setTableId(tableId1);
		dao.persist(ticklerLink2);
		
		TicklerLink ticklerLink3 = new TicklerLink();
		EntityDataGenerator.generateTestDataForModelClass(ticklerLink3);
		ticklerLink3.setTableName(tableName1);
		ticklerLink3.setTableId(tableId1);
		dao.persist(ticklerLink3);
		
		TicklerLink ticklerLink4 = new TicklerLink();
		EntityDataGenerator.generateTestDataForModelClass(ticklerLink4);
		ticklerLink4.setTableName(tableName1);
		ticklerLink4.setTableId(tableId2);
		dao.persist(ticklerLink4);
		
		List<TicklerLink> expectedResult = new ArrayList<TicklerLink>(Arrays.asList(ticklerLink1, ticklerLink3));		
		List<TicklerLink> result = dao.getLinkByTableId(tableName1, tableId1);
		
		Logger logger = MiscUtils.getLogger();
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. Result: "+result.size());
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
	public void testGetLinkByTickler() throws Exception {
		
		int ticklerNo1 = 101;
		int ticklerNo2 = 202;
		
		TicklerLink ticklerLink1 = new TicklerLink();
		EntityDataGenerator.generateTestDataForModelClass(ticklerLink1);
		ticklerLink1.setTicklerNo(ticklerNo1);
		dao.persist(ticklerLink1);
		
		TicklerLink ticklerLink2 = new TicklerLink();
		EntityDataGenerator.generateTestDataForModelClass(ticklerLink2);
		ticklerLink2.setTicklerNo(ticklerNo2);
		dao.persist(ticklerLink2);
		
		TicklerLink ticklerLink3 = new TicklerLink();
		EntityDataGenerator.generateTestDataForModelClass(ticklerLink3);
		ticklerLink3.setTicklerNo(ticklerNo1);
		dao.persist(ticklerLink3);
		
		TicklerLink ticklerLink4 = new TicklerLink();
		EntityDataGenerator.generateTestDataForModelClass(ticklerLink4);
		ticklerLink4.setTicklerNo(ticklerNo1);
		dao.persist(ticklerLink4);
		
		List<TicklerLink> expectedResult = new ArrayList<TicklerLink>(Arrays.asList(ticklerLink1, ticklerLink3, ticklerLink4));		
		List<TicklerLink> result = dao.getLinkByTickler(ticklerNo1);
		
		Logger logger = MiscUtils.getLogger();
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. Result: "+result.size());
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
