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

import org.apache.log4j.Logger;
import org.junit.Before;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;

public class CdsFormOptionDaoTest {

	Logger logger = MiscUtils.getLogger();
	
	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("CdsFormOption");
	}

//	@Test @Ignore
//	/**
//	 * Ensures that CdsFormOptions can be found by version and category.
//	 * WARNING: As there are no setters for the CdsFormOption class,
//	 * the tests will be ignored for now.
//	 * @throws Exception
//	 */
//	public void testFindByVersionAndCategory() throws Exception {
//		String formVersion = "1.1.0";
//		String mainCatgeory = "Basic";
//		
//		CdsFormOption formOption1 = new CdsFormOption();
//		EntityDataGenerator.generateTestDataForModelClass(formOption1);
//		// formOption1.setCdsFormVersion(formVersion);
//		// formOption1.setCdsDataCategory(mainCategory);
//		
//		CdsFormOption formOption2 = new CdsFormOption();
//		EntityDataGenerator.generateTestDataForModelClass(formOption2);
//		// formOption2.setCdsFormVersion(formVersion);
//		// formOption2.setCdsDataCategory(mainCategory);
//		
//		CdsFormOption formOption3 = new CdsFormOption();
//		EntityDataGenerator.generateTestDataForModelClass(formOption3);
//		// formOption3.setCdsFormVersion("Invalid version");
//		// formOption3.setCdsDataCategory("Invalid category");
//		
//		dao.persist(formOption1);
//		dao.persist(formOption2);
//		dao.persist(formOption3);
//		
//		List<CdsFormOption> result = dao.findByVersionAndCategory(formVersion, mainCatgeory);
//		List<CdsFormOption> expectedResult = new ArrayList<CdsFormOption>(Arrays.asList(
//				formOption1,
//				formOption2
//				));
//
//		// fail if list sizes aren't the same
//		if (result.size() != expectedResult.size()) {
//			logger.warn("Array sizes do not match.");
//			fail("Array sizes do not match.");
//		}
//
//		for (int i = 0; i < expectedResult.size(); i++) {
//			if (!expectedResult.get(i).equals(result.get(i))){
//				logger.warn("Items not sorted by Billing Service Date.");
//				fail("Items not sorted by Billing Service Date.");
//			}
//		}
//		assertTrue(true);
//	}

//	@Test @Ignore
//	public void testFindByVersion() {
//		String formVersion = "1.1.0";
//		
//		CdsFormOption formOption1 = new CdsFormOption();
//		EntityDataGenerator.generateTestDataForModelClass(formOption1);
//		// formOption1.setCdsFormVersion(formVersion);
//		// formOption1.setCdsDataCategory("Test1");
//		
//		CdsFormOption formOption2 = new CdsFormOption();
//		EntityDataGenerator.generateTestDataForModelClass(formOption2);
//		// formOption2.setCdsFormVersion(formVersion);
//		// formOption2.setCdsDataCategory("Test2");
//		
//		CdsFormOption formOption3 = new CdsFormOption();
//		EntityDataGenerator.generateTestDataForModelClass(formOption3);
//		// formOption3.setCdsFormVersion("Invalid version");
//		// formOption3.setCdsDataCategory("Test3");
//		
//		dao.persist(formOption1);
//		dao.persist(formOption2);
//		dao.persist(formOption3);
//		
//		List<CdsFormOption> result = dao.findByVersion(formVersion);
//		List<CdsFormOption> expectedResult = new ArrayList<CdsFormOption>(Arrays.asList(
//				formOption1,
//				formOption2
//				));
//
//		// fail if list sizes aren't the same
//		if (result.size() != expectedResult.size()) {
//			logger.warn("Array sizes do not match.");
//			fail("Array sizes do not match.");
//		}
//
//		for (int i = 0; i < expectedResult.size(); i++) {
//			if (!expectedResult.get(i).equals(result.get(i))){
//				logger.warn("Items not sorted by Billing Service Date.");
//				fail("Items not sorted by Billing Service Date.");
//			}
//		}
//		assertTrue(true);
//	}

}
