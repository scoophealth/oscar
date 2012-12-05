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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.CtlDocClass;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CtlDocClassDaoTest extends DaoTestFixtures {

	protected CtlDocClassDao dao = (CtlDocClassDao)SpringUtils.getBean(CtlDocClassDao.class);
	Logger logger = MiscUtils.getLogger();

	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("ctl_doc_class");
	}

	@Test
	/**
	 * Ensures that the findUniqueReportClass() method returns a set of
	 * report classes without returning duplicates.
	 * @throws Exception
	 */
	public void testFindSubClassesByReportClass() throws Exception {
		String reportClass = "classA";

		CtlDocClass class1 = new CtlDocClass();
		EntityDataGenerator.generateTestDataForModelClass(class1);
		class1.setReportClass(reportClass);
		class1.setSubClass("subA");
		class1.setId(160);

		CtlDocClass class2 = new CtlDocClass();
		EntityDataGenerator.generateTestDataForModelClass(class2);
		class2.setReportClass(reportClass);
		class2.setSubClass("subB");
		class2.setId(161);

		CtlDocClass class3 = new CtlDocClass();
		EntityDataGenerator.generateTestDataForModelClass(class3);
		class3.setReportClass("classB");
		class3.setSubClass("subC");
		class3.setId(162);

		CtlDocClass class4 = new CtlDocClass();
		EntityDataGenerator.generateTestDataForModelClass(class4);
		class4.setReportClass("classC");
		class4.setSubClass("subD");
		class4.setId(163);

		dao.persist(class1);
		dao.persist(class2);
		dao.persist(class3);
		dao.persist(class4);

		List<String> result = dao.findSubClassesByReportClass(reportClass);
		List<String> expectedResult = new ArrayList<String>(Arrays.asList("subA", "subB"));

		assertEquals(result.size(), expectedResult.size());
		assertTrue(result.containsAll(expectedResult));
		// ensure that all persisted items are in the table
		// ensure ordered by reportclass lexicographically
		String str1;
		String str2;
		for (int i = 0; i < result.size() - 1; i++) {
			str1 = result.get(i);
			str2 = result.get(i+1);
			if (str1.compareTo(str2) > -1 ){
				fail("Results not ordred by sub class.");
			}
		}
		assertTrue(true);
		assertTrue(result.containsAll(expectedResult));
	}

	@Test
	/**
	 * Ensure that all report classes in the table
	 * are ordered by reportClass lexicographically.
	 * Ensure that all report classes are unique.
	 * @throws Exception
	 */
	public void testFindUniqueReportClasses() throws Exception {
		CtlDocClass class1 = new CtlDocClass();
		EntityDataGenerator.generateTestDataForModelClass(class1);
		class1.setReportClass("classA");
		class1.setId(160);

		CtlDocClass class2 = new CtlDocClass();
		EntityDataGenerator.generateTestDataForModelClass(class2);
		class2.setReportClass("classA");
		class2.setId(161);

		CtlDocClass class3 = new CtlDocClass();
		EntityDataGenerator.generateTestDataForModelClass(class3);
		class3.setReportClass("classB");
		class3.setId(162);

		CtlDocClass class4 = new CtlDocClass();
		EntityDataGenerator.generateTestDataForModelClass(class4);
		class4.setReportClass("classC");
		class4.setId(164);

		dao.persist(class1);
		dao.persist(class2);
		dao.persist(class3);
		dao.persist(class4);

		List<String> result = dao.findUniqueReportClasses();
		List<String> expectedResult = new ArrayList<String>(Arrays.asList("classA","classB", "classC"));
		assertTrue(result.containsAll(expectedResult));

		// make sure all items are unique
		final List<String> uniqueClasses = new ArrayList<String>();
	    final HashMap<String,String> hm = new HashMap<String,String>();
	    for (String reportClass : result) {
	        if (hm.get(reportClass) == null) {
	            hm.put(reportClass,reportClass);
	            uniqueClasses.add(reportClass);
	        }
	    }
		assertEquals(uniqueClasses.size(), dao.findUniqueReportClasses().size());

		// ensure that all persisted items are in the table
		// ordered by reportClass lexicographically
		for (int i = 0; i < result.size() - 1; i++) {
			if ((result.get(i).toLowerCase()).compareTo(result.get(i+1).toLowerCase()) > 0) {
				fail("Results not ordered by report class.");
			}
		}
		assertTrue(true);
	}	
}
