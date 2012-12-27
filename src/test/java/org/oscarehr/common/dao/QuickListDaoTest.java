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
import org.oscarehr.common.model.QuickList;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class QuickListDaoTest extends DaoTestFixtures {

	// TODO Make it protected when test ignores are merged in
	private QuickListDao dao = SpringUtils.getBean(QuickListDao.class);

	public QuickListDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("quickList");
	}

        @Test
        public void testCreate() throws Exception {
                 QuickList ql = new QuickList();
                 EntityDataGenerator.generateTestDataForModelClass(ql);
                 dao.persist(ql);
                 assertNotNull(ql.getId());
        }

	@Test
	public void testFindByNameResearchCodeAndCodingSystem() throws Exception {
		 
		String quickListName1 = "alpha";
		String quickListName2 = "bravo";
		
		String dxResearchCode1 = "111";
		String dxResearchCode2 = "222";
		
		String codingSystem1 = "101";
		String codingSystem2 = "202";
		
		QuickList quickList1 = new QuickList();
		EntityDataGenerator.generateTestDataForModelClass(quickList1);
		quickList1.setQuickListName(quickListName1);
		quickList1.setDxResearchCode(dxResearchCode1);
		quickList1.setCodingSystem(codingSystem1);
		dao.persist(quickList1);
		
		QuickList quickList2 = new QuickList();
		EntityDataGenerator.generateTestDataForModelClass(quickList2);
		quickList2.setQuickListName(quickListName2);
		quickList2.setDxResearchCode(dxResearchCode2);
		quickList2.setCodingSystem(codingSystem2);
		dao.persist(quickList2);
		
		QuickList quickList3 = new QuickList();
		EntityDataGenerator.generateTestDataForModelClass(quickList3);
		quickList3.setQuickListName(quickListName1);
		quickList3.setDxResearchCode(dxResearchCode1);
		quickList3.setCodingSystem(codingSystem1);
		dao.persist(quickList3);
		
		List<QuickList> expectedResult = new ArrayList<QuickList>(Arrays.asList(quickList1, quickList3));
		List<QuickList> result = dao.findByNameResearchCodeAndCodingSystem(quickListName1, dxResearchCode1, codingSystem1);

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
